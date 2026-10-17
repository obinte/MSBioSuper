package com.acosux.MSBiosuper.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configuración de persistencia de MSBioSuper / MSAbricmar (mismo WAR).
 * <p>
 * US-198 (patrón US-190): las credenciales (jdbc.user y jdbc.password) se leen de AWS
 * Secrets Manager cuando aws.secret.name está definido. El código NO depende del nombre
 * del rol: usuario y contraseña salen del secreto. En Fase 1 el secreto contiene las
 * credenciales de administradores (transición); en Fase 2 se cambia solo el contenido
 * del secreto al rol propio, sin tocar código.
 * <p>
 * El pool es configurable por propiedad (pool.*); su tamaño está pendiente de validar
 * con el tráfico real (US-240). Las conexiones aparecen en
 * pg_stat_activity.application_name con jdbc.applicationname (por despliegue:
 * MSBioSuper-cloud, MSAbricmar-shrimp). Con jdbc.user/jdbc.password=_REQUIRES_AWS_SECRET_
 * la conexión falla si no se puede leer el secreto (fallo cerrado).
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.acosux")
@PropertySource("classpath:application.properties")
public class PersistenceConfig {

    private static final Logger LOG = Logger.getLogger(PersistenceConfig.class.getName());

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        String bd = "postgresql";
        String driver = "org.postgresql.Driver";

        String nombreServidor = env.getProperty("jdbc.server");
        String puertoServidor = env.getProperty("jdbc.port");
        String nombreBaseDatos = env.getProperty("jdbc.database");
        String username = env.getProperty("jdbc.user");
        String password = env.getProperty("jdbc.password");

        // AWS Secrets Manager — nombre del secret configurable desde application.properties
        // (mismo mecanismo que MSVitapro/ShrimpSoftServer, US-190/US-198).
        String secretName = env.getProperty("aws.secret.name");
        String origenUsuario = "application.properties";
        String origenPassword = "application.properties";

        if (secretName != null && !secretName.isEmpty()) {
            try {
                SecretsProvider secrets = new SecretsProvider(secretName);
                if (secrets.isLoaded()) {
                    String secretUser = secrets.get("jdbc.user", null);
                    String secretPassword = secrets.get("jdbc.password", null);
                    if (secretUser != null) {
                        username = secretUser;
                        origenUsuario = "AWS Secrets Manager (" + secretName + ")";
                    }
                    if (secretPassword != null) {
                        password = secretPassword;
                        origenPassword = "AWS Secrets Manager (" + secretName + ")";
                    }
                    LOG.log(Level.INFO, "[PersistenceConfig] Credentials loaded from AWS Secrets Manager ({0})", secretName);
                    if (secretUser == null || secretPassword == null) {
                        LOG.log(Level.SEVERE, "[PersistenceConfig] El secreto {0} no tiene las claves jdbc.user y jdbc.password: "
                                + "se usa application.properties para la que falta", secretName);
                    }
                } else {
                    // US-198 (patrón US-190): fallo del secreto como SEVERE. Con
                    // jdbc.password=_REQUIRES_AWS_SECRET_ la conexión fallará (fallo cerrado).
                    LOG.log(Level.SEVERE, "[PersistenceConfig] No se pudo cargar el secreto {0} de AWS Secrets Manager: "
                            + "se usan las credenciales de application.properties", secretName);
                }
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "[PersistenceConfig] Could not load secrets from AWS. Using credentials from application.properties", e);
            }
        }
        String origenCredenciales = origenUsuario.equals(origenPassword)
                ? origenUsuario
                : "usuario de " + origenUsuario + ", contraseña de " + origenPassword;

        // Nombre con el que las conexiones aparecen en pg_stat_activity.application_name.
        // Por defecto MSBioSuper-<base>; se fija por despliegue con jdbc.applicationname
        // (MSBioSuper-cloud, MSAbricmar-shrimp).
        String nombreBase = nombreBaseDatos.split("\\?", 2)[0];
        String applicationName = env.getProperty("jdbc.applicationname", "MSBioSuper-" + nombreBase);

        // ApplicationName va al final de la URL: si jdbc.database ya trae uno, el driver se
        // queda con el último valor.
        String url = "jdbc:" + bd + "://" + nombreServidor + ":" + puertoServidor + "/" + nombreBaseDatos;
        url += (nombreBaseDatos.contains("?") ? "&" : "?") + "ApplicationName=" + codificarUrl(applicationName);

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        // Pool acotado: el rol de base de datos tiene CONNECTION LIMIT bajo (US-198).
        // Si no hay conexión libre en pool.maxWaitMillis, falla la obtención de conexión.
        dataSource.setMaxTotal(env.getProperty("pool.maxTotal", Integer.class, 2));
        dataSource.setMaxIdle(env.getProperty("pool.maxIdle", Integer.class, 2));
        dataSource.setMinIdle(env.getProperty("pool.minIdle", Integer.class, 0));
        dataSource.setInitialSize(env.getProperty("pool.initialSize", Integer.class, 0));
        dataSource.setMaxWaitMillis(env.getProperty("pool.maxWaitMillis", Long.class, 8000L));
        dataSource.setTestOnBorrow(true);

        // Evidencia (US-198): de dónde salen las credenciales, usuario, base y
        // ApplicationName. Nunca la contraseña.
        LOG.log(Level.INFO, "MSBioSuper DataSource: credenciales={0}, user={1}, base={2}, servidor={3}:{4}, applicationName={5}, "
                + "maxTotal={6}, maxIdle={7}, minIdle={8}, initialSize={9}, maxWaitMillis={10}",
                new Object[]{origenCredenciales, username, nombreBase, nombreServidor, puertoServidor, applicationName,
                    dataSource.getMaxTotal(), dataSource.getMaxIdle(), dataSource.getMinIdle(),
                    dataSource.getInitialSize(), String.valueOf(dataSource.getMaxWaitMillis())});
        return dataSource;
    }

    private static String codificarUrl(String valor) {
        try {
            return URLEncoder.encode(valor, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @SuppressWarnings("serial")
    Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                setProperty("hibernate.show_sql", "true");
            }
        };
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.acosux");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory().getObject());

        return txManager;
    }
}

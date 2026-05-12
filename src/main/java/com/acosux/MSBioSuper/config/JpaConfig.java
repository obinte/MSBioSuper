package com.acosux.MSBioSuper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Configuración JPA moderna para Spring Boot 3 y Hibernate 6.
 * Reemplaza la configuración antigua basada en SessionFactory de Hibernate 4.
 * 
 * Esta configuración utiliza:
 * - LocalContainerEntityManagerFactoryBean en lugar de LocalSessionFactoryBean
 * - JpaTransactionManager en lugar de HibernateTransactionManager
 * - HikariCP como pool de conexiones (configurado automáticamente por Spring Boot)
 * 
 * NOTA: El DataSource y HikariCP se configuran automáticamente mediante las propiedades
 * spring.datasource.* y spring.datasource.hikari.* en application.properties.
 * Spring Boot 3 gestiona automáticamente HikariCP como pool de conexiones por defecto.
 */
@Configuration
@EnableTransactionManagement
public class JpaConfig {

    /**
     * Configura el EntityManagerFactory usando JPA en lugar de SessionFactory de Hibernate.
     * Esta es la configuración moderna para Spring Boot 3 y Hibernate 6.
     * 
     * El DataSource se inyecta automáticamente desde la configuración de Spring Boot,
     * que incluye HikariCP con las siguientes configuraciones:
     * - Tamaño máximo del pool: 20 conexiones
     * - Timeout de conexión: 30 segundos
     * - Timeout de inactividad: 10 minutos
     * - Detección de fugas de conexión: habilitada (2 segundos)
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan("com.acosux");
        
        // Configurar el adaptador de JPA para Hibernate
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(false); // Controlado por spring.jpa.show-sql en properties
        vendorAdapter.setGenerateDdl(false);
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        
        // Propiedades específicas de Hibernate 6
        entityManagerFactory.setJpaProperties(hibernateProperties());
        
        return entityManagerFactory;
    }

    /**
     * Propiedades de Hibernate 6 optimizadas para PostgreSQL.
     * Estas propiedades complementan las configuradas en application.properties.
     */
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        
        // Configuración de rendimiento para batch processing
        properties.setProperty("hibernate.jdbc.batch_size", "20");
        properties.setProperty("hibernate.order_inserts", "true");
        properties.setProperty("hibernate.order_updates", "true");
        properties.setProperty("hibernate.jdbc.batch_versioned_data", "true");
        
        // Caché de segundo nivel (desactivado por defecto)
        properties.setProperty("hibernate.cache.use_second_level_cache", "false");
        properties.setProperty("hibernate.cache.use_query_cache", "false");
        
        // Estadísticas de Hibernate (útil para monitoreo en desarrollo)
        properties.setProperty("hibernate.generate_statistics", "false");
        
        return properties;
    }

    /**
     * Configura el JpaTransactionManager para gestión de transacciones JPA.
     * Reemplaza HibernateTransactionManager de Hibernate 4.
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    /**
     * Post-procesador para traducir excepciones de persistencia a excepciones de Spring.
     * Permite el uso de anotaciones @Repository para traducción automática de excepciones.
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}

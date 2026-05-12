package com.acosux.MSBioSuper;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Clase de inicialización para despliegue WAR en Tomcat externo.
 * 
 * Esta clase solo se utiliza cuando la aplicación se empaqueta como WAR
 * y se despliega en un servidor Tomcat externo.
 * 
 * Para generar WAR:
 * mvn clean package -P war
 * 
 * Para generar JAR (Docker/standalone):
 * mvn clean package
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-05-04
 */
public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * Configura la aplicación Spring Boot para ejecución en contenedor servlet externo.
     * 
     * @param application el builder de la aplicación Spring
     * @return el builder configurado
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MSBioSuperApplication.class);
    }
}

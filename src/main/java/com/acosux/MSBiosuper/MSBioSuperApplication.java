package com.acosux.MSBioSuper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación MSBioSuper.
 * 
 * NOTA: Se ha eliminado la exclusión de HibernateJpaAutoConfiguration que estaba causando problemas.
 * Spring Boot 3 con Hibernate 6 gestiona automáticamente la configuración de JPA.
 * La configuración personalizada se encuentra en JpaConfig.java.
 */
@SpringBootApplication
public class MSBioSuperApplication {

    public static void main(String[] args) {
        SpringApplication.run(MSBioSuperApplication.class, args);
    }

}

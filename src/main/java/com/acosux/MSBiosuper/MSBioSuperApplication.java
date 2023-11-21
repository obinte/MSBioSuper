package com.acosux.MSBiosuper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class MSBioSuperApplication {

    public static void main(String[] args) {
        SpringApplication.run(MSBioSuperApplication.class, args);
    }

}

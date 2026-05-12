package com.acosux.MSBioSuper.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración adicional para SpringDoc OpenAPI.
 * 
 * Esta clase asegura que SpringDoc se inicialice correctamente
 * y registre sus endpoints (/v3/api-docs, /swagger-ui.html).
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-05-07
 */
@Configuration
public class SpringDocConfig {

    /**
     * Define un grupo de API para todos los endpoints.
     * 
     * Esto fuerza la inicialización de SpringDoc y asegura que
     * los endpoints de documentación se registren correctamente.
     * 
     * @return GroupedOpenApi configurado para todos los endpoints
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("msbiosuper-api")
                .pathsToMatch("/api/**", "/token/**", "/auth/**")
                .build();
    }
}

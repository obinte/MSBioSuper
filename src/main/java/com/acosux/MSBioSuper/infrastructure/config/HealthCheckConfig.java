package com.acosux.MSBioSuper.infrastructure.config;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.health.Health;

/**
 * Configuración de Health Checks para Spring Boot Actuator.
 * 
 * Esta clase configura los indicadores de salud personalizados y adicionales
 * para monitorear el estado de la aplicación MSBioSuper.
 * 
 * Los health indicators personalizados incluyen:
 * - DatabaseHealthIndicator: Verifica conectividad con PostgreSQL
 * - BusinessLogicHealthIndicator: Verifica componentes de lógica de negocio
 * - DiskSpaceHealthIndicator: Monitorea espacio disponible en disco
 * 
 * Endpoints disponibles:
 * - /actuator/health - Estado general de salud
 * - /actuator/health/liveness - Sonda de liveness (Kubernetes)
 * - /actuator/health/readiness - Sonda de readiness (Kubernetes)
 * 
 * @author MSBioSuper Team
 * @since 1.0
 */
@Configuration
public class HealthCheckConfig {

    /**
     * Indicador de salud adicional para verificar el estado general de la aplicación.
     * Este es un health check simple que siempre retorna UP si la aplicación está ejecutándose.
     */
    @Bean
    public HealthIndicator applicationHealthIndicator() {
        return () -> Health.up()
                .withDetail("application", "MSBioSuper")
                .withDetail("version", "1.0.0")
                .withDetail("status", "Aplicación ejecutándose correctamente")
                .withDetail("javaVersion", System.getProperty("java.version"))
                .withDetail("springBootVersion", org.springframework.boot.SpringBootVersion.getVersion())
                .build();
    }
}

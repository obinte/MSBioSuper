package com.acosux.MSBioSuper.infrastructure.health;

import com.acosux.MSBioSuper.domain.repository.ResumenPescaRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Indicador de salud personalizado para verificar la lógica de negocio crítica.
 * 
 * Este health indicator verifica:
 * - Disponibilidad de repositorios críticos
 * - Capacidad de ejecutar operaciones básicas de negocio
 * - Estado de componentes esenciales de la aplicación
 * 
 * @author MSBioSuper Team
 * @since 1.0
 */
@Component
public class BusinessLogicHealthIndicator implements HealthIndicator {

    private final ResumenPescaRepository resumenPescaRepository;

    public BusinessLogicHealthIndicator(ResumenPescaRepository resumenPescaRepository) {
        this.resumenPescaRepository = resumenPescaRepository;
    }

    @Override
    public Health health() {
        try {
            // Verificar que el repositorio está disponible y puede ejecutar operaciones
            long startTime = System.currentTimeMillis();
            
            // Ejecutar una operación simple para verificar que la lógica de negocio funciona
            // Usamos findAll() que es una operación básica disponible en todos los repositorios
            List<?> results = resumenPescaRepository.findAll();
            long count = results != null ? results.size() : 0;
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return Health.up()
                    .withDetail("status", "Lógica de negocio operativa")
                    .withDetail("repositoryCheck", "OK")
                    .withDetail("resumenPescaRecordsAvailable", count)
                    .withDetail("responseTime", responseTime + "ms")
                    .withDetail("components", "ResumenPescaRepository disponible")
                    .build();
            
        } catch (Exception e) {
            return Health.down()
                    .withDetail("status", "Error en lógica de negocio")
                    .withDetail("repositoryCheck", "FAILED")
                    .withDetail("error", e.getClass().getSimpleName())
                    .withDetail("message", e.getMessage())
                    .withDetail("recommendation", "Verificar conectividad con base de datos y configuración de repositorios")
                    .build();
        }
    }
}

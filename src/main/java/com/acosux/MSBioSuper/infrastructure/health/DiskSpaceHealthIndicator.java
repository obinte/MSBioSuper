package com.acosux.MSBioSuper.infrastructure.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Indicador de salud personalizado para verificar el espacio disponible en disco.
 * 
 * Este health indicator monitorea:
 * - Espacio total en disco
 * - Espacio libre disponible
 * - Espacio utilizado
 * - Porcentaje de uso
 * 
 * Marca como DOWN si el espacio libre es menor al 10% del total.
 * 
 * @author MSBioSuper Team
 * @since 1.0
 */
@Component
public class DiskSpaceHealthIndicator implements HealthIndicator {

    private static final double THRESHOLD_PERCENTAGE = 10.0; // 10% de espacio libre mínimo

    @Override
    public Health health() {
        try {
            File root = new File("/");
            
            long totalSpace = root.getTotalSpace();
            long freeSpace = root.getFreeSpace();
            long usableSpace = root.getUsableSpace();
            long usedSpace = totalSpace - freeSpace;
            
            double freePercentage = (freeSpace * 100.0) / totalSpace;
            double usedPercentage = (usedSpace * 100.0) / totalSpace;
            
            Health.Builder healthBuilder;
            
            if (freePercentage < THRESHOLD_PERCENTAGE) {
                healthBuilder = Health.down()
                        .withDetail("status", "Espacio en disco crítico");
            } else {
                healthBuilder = Health.up()
                        .withDetail("status", "Espacio en disco suficiente");
            }
            
            return healthBuilder
                    .withDetail("total", formatBytes(totalSpace))
                    .withDetail("free", formatBytes(freeSpace))
                    .withDetail("usable", formatBytes(usableSpace))
                    .withDetail("used", formatBytes(usedSpace))
                    .withDetail("freePercentage", String.format("%.2f%%", freePercentage))
                    .withDetail("usedPercentage", String.format("%.2f%%", usedPercentage))
                    .withDetail("threshold", THRESHOLD_PERCENTAGE + "%")
                    .build();
            
        } catch (Exception e) {
            return Health.down()
                    .withDetail("status", "Error al verificar espacio en disco")
                    .withDetail("error", e.getClass().getSimpleName())
                    .withDetail("message", e.getMessage())
                    .build();
        }
    }
    
    /**
     * Formatea bytes a una representación legible (KB, MB, GB, TB).
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        }
        
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char unit = "KMGT".charAt(exp - 1);
        double value = bytes / Math.pow(1024, exp);
        
        return String.format("%.2f %sB", value, unit);
    }
}

package com.acosux.MSBioSuper.infrastructure.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Indicador de salud personalizado para verificar la conectividad y estado de la base de datos PostgreSQL.
 * 
 * Este health indicator realiza una consulta simple a la base de datos para verificar:
 * - Conectividad con PostgreSQL
 * - Capacidad de ejecutar consultas
 * - Tiempo de respuesta de la base de datos
 * 
 * @author MSBioSuper Team
 * @since 1.0
 */
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try {
            long startTime = System.currentTimeMillis();
            
            // Intentar obtener una conexión y ejecutar una consulta simple
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT 1")) {
                
                long responseTime = System.currentTimeMillis() - startTime;
                
                if (resultSet.next()) {
                    return Health.up()
                            .withDetail("database", "PostgreSQL")
                            .withDetail("status", "Conectado")
                            .withDetail("responseTime", responseTime + "ms")
                            .withDetail("validationQuery", "SELECT 1")
                            .build();
                } else {
                    return Health.down()
                            .withDetail("database", "PostgreSQL")
                            .withDetail("status", "Error en consulta de validación")
                            .withDetail("reason", "La consulta no retornó resultados")
                            .build();
                }
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("database", "PostgreSQL")
                    .withDetail("status", "Error de conexión")
                    .withDetail("error", e.getClass().getSimpleName())
                    .withDetail("message", e.getMessage())
                    .build();
        }
    }
}

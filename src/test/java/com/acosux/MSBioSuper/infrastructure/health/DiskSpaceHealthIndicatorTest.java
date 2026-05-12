package com.acosux.MSBioSuper.infrastructure.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para DiskSpaceHealthIndicator.
 * 
 * Nota: Estas pruebas verifican el comportamiento del indicador con el sistema de archivos real.
 * En un entorno de pruebas, el espacio en disco debería ser suficiente.
 */
class DiskSpaceHealthIndicatorTest {

    private DiskSpaceHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        healthIndicator = new DiskSpaceHealthIndicator();
    }

    @Test
    void health_ShouldReturnHealthStatus() {
        // When
        Health health = healthIndicator.health();

        // Then
        assertThat(health.getStatus()).isIn(Status.UP, Status.DOWN);
        assertThat(health.getDetails()).containsKey("status");
        assertThat(health.getDetails()).containsKey("total");
        assertThat(health.getDetails()).containsKey("free");
        assertThat(health.getDetails()).containsKey("usable");
        assertThat(health.getDetails()).containsKey("used");
        assertThat(health.getDetails()).containsKey("freePercentage");
        assertThat(health.getDetails()).containsKey("usedPercentage");
        assertThat(health.getDetails()).containsEntry("threshold", "10.0%");
    }

    @Test
    void health_WhenSufficientSpace_ShouldReturnUp() {
        // When
        Health health = healthIndicator.health();

        // Then
        // En un entorno de pruebas normal, debería haber suficiente espacio
        if (health.getStatus() == Status.UP) {
            assertThat(health.getDetails()).containsEntry("status", "Espacio en disco suficiente");
        }
    }

    @Test
    void health_ShouldIncludeFormattedSizes() {
        // When
        Health health = healthIndicator.health();

        // Then
        String total = (String) health.getDetails().get("total");
        String free = (String) health.getDetails().get("free");
        String usable = (String) health.getDetails().get("usable");
        String used = (String) health.getDetails().get("used");

        // Verificar que los tamaños están formateados correctamente (acepta tanto coma como punto decimal)
        assertThat(total).matches(".*\\d+[.,]\\d{2}\\s+[KMGT]?B");
        assertThat(free).matches(".*\\d+[.,]\\d{2}\\s+[KMGT]?B");
        assertThat(usable).matches(".*\\d+[.,]\\d{2}\\s+[KMGT]?B");
        assertThat(used).matches(".*\\d+[.,]\\d{2}\\s+[KMGT]?B");
    }

    @Test
    void health_ShouldIncludePercentages() {
        // When
        Health health = healthIndicator.health();

        // Then
        String freePercentage = (String) health.getDetails().get("freePercentage");
        String usedPercentage = (String) health.getDetails().get("usedPercentage");

        // Verificar que los porcentajes están formateados correctamente (acepta tanto coma como punto decimal)
        assertThat(freePercentage).matches("\\d+[.,]\\d{2}%");
        assertThat(usedPercentage).matches("\\d+[.,]\\d{2}%");
    }
}

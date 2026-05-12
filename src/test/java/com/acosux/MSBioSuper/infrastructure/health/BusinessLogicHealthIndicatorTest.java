package com.acosux.MSBioSuper.infrastructure.health;

import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import com.acosux.MSBioSuper.domain.repository.ResumenPescaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para BusinessLogicHealthIndicator.
 */
@ExtendWith(MockitoExtension.class)
class BusinessLogicHealthIndicatorTest {

    @Mock
    private ResumenPescaRepository resumenPescaRepository;

    private BusinessLogicHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        healthIndicator = new BusinessLogicHealthIndicator(resumenPescaRepository);
    }

    @Test
    void health_WhenBusinessLogicIsOperational_ShouldReturnUp() {
        // Given
        List<PrdResumenCorridaTO> mockResults = new ArrayList<>();
        // Simular algunos registros
        for (int i = 0; i < 10; i++) {
            mockResults.add(new PrdResumenCorridaTO());
        }
        when(resumenPescaRepository.findAll()).thenReturn(mockResults);

        // When
        Health health = healthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("status", "Lógica de negocio operativa");
        assertThat(health.getDetails()).containsEntry("repositoryCheck", "OK");
        assertThat(health.getDetails()).containsEntry("resumenPescaRecordsAvailable", 10L);
        assertThat(health.getDetails()).containsKey("responseTime");
        assertThat(health.getDetails()).containsEntry("components", "ResumenPescaRepository disponible");

        verify(resumenPescaRepository).findAll();
    }

    @Test
    void health_WhenRepositoryFails_ShouldReturnDown() {
        // Given
        RuntimeException exception = new RuntimeException("Database connection lost");
        when(resumenPescaRepository.findAll()).thenThrow(exception);

        // When
        Health health = healthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("status", "Error en lógica de negocio");
        assertThat(health.getDetails()).containsEntry("repositoryCheck", "FAILED");
        assertThat(health.getDetails()).containsEntry("error", "RuntimeException");
        assertThat(health.getDetails()).containsEntry("message", "Database connection lost");
        assertThat(health.getDetails()).containsEntry("recommendation", 
                "Verificar conectividad con base de datos y configuración de repositorios");

        verify(resumenPescaRepository).findAll();
    }

    @Test
    void health_WhenRepositoryReturnsEmptyList_ShouldStillReturnUp() {
        // Given
        when(resumenPescaRepository.findAll()).thenReturn(new ArrayList<>());

        // When
        Health health = healthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("resumenPescaRecordsAvailable", 0L);
        assertThat(health.getDetails()).containsEntry("repositoryCheck", "OK");
    }

    @Test
    void health_WhenRepositoryReturnsNull_ShouldHandleGracefully() {
        // Given
        when(resumenPescaRepository.findAll()).thenReturn(null);

        // When
        Health health = healthIndicator.health();

        // Then
        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("resumenPescaRecordsAvailable", 0L);
        assertThat(health.getDetails()).containsEntry("repositoryCheck", "OK");
    }
}

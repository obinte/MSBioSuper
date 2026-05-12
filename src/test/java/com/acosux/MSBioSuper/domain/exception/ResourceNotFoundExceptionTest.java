package com.acosux.MSBioSuper.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para ResourceNotFoundException.
 * 
 * Valida la construcción y comportamiento de la excepción de recurso no encontrado.
 * 
 * @author Josias Rubén Aldaz Arcalles
 */
@DisplayName("ResourceNotFoundException - Pruebas Unitarias")
class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con mensaje personalizado")
    void constructor_ConMensaje_CreaExcepcionCorrectamente() {
        // Given
        String mensaje = "Recurso no encontrado en el sistema";

        // When
        ResourceNotFoundException excepcion = new ResourceNotFoundException(mensaje);

        // Then
        assertThat(excepcion.getMessage()).isEqualTo(mensaje);
        assertThat(excepcion.getErrorCode()).isEqualTo("RESOURCE_NOT_FOUND");
    }

    @Test
    @DisplayName("Debe crear excepción con tipo de recurso e ID")
    void constructor_ConTipoYId_CreaExcepcionConMensajeFormateado() {
        // Given
        String tipoRecurso = "Liquidación";
        String resourceId = "LIQ-001";

        // When
        ResourceNotFoundException excepcion = new ResourceNotFoundException(tipoRecurso, resourceId);

        // Then
        assertThat(excepcion.getMessage()).isEqualTo("Liquidación con ID 'LIQ-001' no encontrado");
        assertThat(excepcion.getErrorCode()).isEqualTo("RESOURCE_NOT_FOUND");
    }

    @Test
    @DisplayName("Debe crear excepción con tipo de recurso e ID numérico")
    void constructor_ConTipoYIdNumerico_CreaExcepcionConMensajeFormateado() {
        // Given
        String tipoRecurso = "Producto";
        Integer resourceId = 12345;

        // When
        ResourceNotFoundException excepcion = new ResourceNotFoundException(tipoRecurso, resourceId);

        // Then
        assertThat(excepcion.getMessage()).isEqualTo("Producto con ID '12345' no encontrado");
        assertThat(excepcion.getErrorCode()).isEqualTo("RESOURCE_NOT_FOUND");
    }

    @Test
    @DisplayName("Debe ser una BusinessException")
    void resourceNotFoundException_EsBusinessException() {
        // Given
        ResourceNotFoundException excepcion = new ResourceNotFoundException("Recurso no encontrado");

        // Then
        assertThat(excepcion).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("Debe ser una RuntimeException")
    void resourceNotFoundException_EsRuntimeException() {
        // Given
        ResourceNotFoundException excepcion = new ResourceNotFoundException("Recurso no encontrado");

        // Then
        assertThat(excepcion).isInstanceOf(RuntimeException.class);
    }
}

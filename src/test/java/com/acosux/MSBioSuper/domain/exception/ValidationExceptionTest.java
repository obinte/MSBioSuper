package com.acosux.MSBioSuper.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para ValidationException.
 * 
 * Valida la construcción y comportamiento de la excepción de validación de negocio.
 * 
 * @author Josias Rubén Aldaz Arcalles
 */
@DisplayName("ValidationException - Pruebas Unitarias")
class ValidationExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con mensaje de validación")
    void constructor_ConMensaje_CreaExcepcionCorrectamente() {
        // Given
        String mensaje = "El saldo no puede ser negativo";

        // When
        ValidationException excepcion = new ValidationException(mensaje);

        // Then
        assertThat(excepcion.getMessage()).isEqualTo(mensaje);
        assertThat(excepcion.getErrorCode()).isEqualTo("VALIDATION_ERROR");
    }

    @Test
    @DisplayName("Debe crear excepción con mensaje y causa")
    void constructor_ConMensajeYCausa_CreaExcepcionCorrectamente() {
        // Given
        String mensaje = "Error al validar datos";
        Throwable causa = new IllegalArgumentException("Valor inválido");

        // When
        ValidationException excepcion = new ValidationException(mensaje, causa);

        // Then
        assertThat(excepcion.getMessage()).isEqualTo(mensaje);
        assertThat(excepcion.getCause()).isEqualTo(causa);
        assertThat(excepcion.getErrorCode()).isEqualTo("VALIDATION_ERROR");
    }

    @Test
    @DisplayName("Debe ser una BusinessException")
    void validationException_EsBusinessException() {
        // Given
        ValidationException excepcion = new ValidationException("Error de validación");

        // Then
        assertThat(excepcion).isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("Debe ser una RuntimeException")
    void validationException_EsRuntimeException() {
        // Given
        ValidationException excepcion = new ValidationException("Error de validación");

        // Then
        assertThat(excepcion).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Debe tener código de error específico de validación")
    void validationException_TieneCodigoEspecifico() {
        // Given
        ValidationException excepcion = new ValidationException("Error de validación");

        // Then
        assertThat(excepcion.getErrorCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(excepcion.getErrorCode()).isNotEqualTo("BUSINESS_ERROR");
    }
}

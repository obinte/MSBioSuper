package com.acosux.MSBioSuper.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para BusinessException.
 * 
 * Valida la construcción y comportamiento de la excepción base de negocio.
 * 
 * @author Josias Rubén Aldaz Arcalles
 */
@DisplayName("BusinessException - Pruebas Unitarias")
class BusinessExceptionTest {

    @Test
    @DisplayName("Debe crear excepción con mensaje")
    void constructor_ConMensaje_CreaExcepcionCorrectamente() {
        // Given
        String mensaje = "Error de negocio";

        // When
        BusinessException excepcion = new BusinessException(mensaje);

        // Then
        assertThat(excepcion.getMessage()).isEqualTo(mensaje);
        assertThat(excepcion.getErrorCode()).isEqualTo("BUSINESS_ERROR");
    }

    @Test
    @DisplayName("Debe crear excepción con mensaje y código personalizado")
    void constructor_ConMensajeYCodigo_CreaExcepcionCorrectamente() {
        // Given
        String mensaje = "Error de validación";
        String codigo = "VAL_001";

        // When
        BusinessException excepcion = new BusinessException(mensaje, codigo);

        // Then
        assertThat(excepcion.getMessage()).isEqualTo(mensaje);
        assertThat(excepcion.getErrorCode()).isEqualTo(codigo);
    }

    @Test
    @DisplayName("Debe crear excepción con mensaje y causa")
    void constructor_ConMensajeYCausa_CreaExcepcionCorrectamente() {
        // Given
        String mensaje = "Error de negocio";
        Throwable causa = new RuntimeException("Causa raíz");

        // When
        BusinessException excepcion = new BusinessException(mensaje, causa);

        // Then
        assertThat(excepcion.getMessage()).isEqualTo(mensaje);
        assertThat(excepcion.getCause()).isEqualTo(causa);
        assertThat(excepcion.getErrorCode()).isEqualTo("BUSINESS_ERROR");
    }

    @Test
    @DisplayName("Debe crear excepción con mensaje, código y causa")
    void constructor_ConMensajeCodigoYCausa_CreaExcepcionCorrectamente() {
        // Given
        String mensaje = "Error de validación";
        String codigo = "VAL_001";
        Throwable causa = new IllegalArgumentException("Argumento inválido");

        // When
        BusinessException excepcion = new BusinessException(mensaje, codigo, causa);

        // Then
        assertThat(excepcion.getMessage()).isEqualTo(mensaje);
        assertThat(excepcion.getErrorCode()).isEqualTo(codigo);
        assertThat(excepcion.getCause()).isEqualTo(causa);
    }

    @Test
    @DisplayName("Debe ser una RuntimeException")
    void businessException_EsRuntimeException() {
        // Given
        BusinessException excepcion = new BusinessException("Error");

        // Then
        assertThat(excepcion).isInstanceOf(RuntimeException.class);
    }
}

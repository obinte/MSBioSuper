/*
 * Excepción para errores de validación de reglas de negocio.
 */
package com.acosux.MSBioSuper.domain.exception;

/**
 * Excepción lanzada cuando una regla de validación de negocio falla.
 * Diferente de las validaciones de entrada (Jakarta Validation).
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0 - Java 21 Migration
 */
public class ValidationException extends BusinessException {

    /**
     * Constructor con mensaje de error.
     * 
     * @param message Mensaje descriptivo de la validación fallida
     */
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje descriptivo de la validación fallida
     * @param cause Causa raíz del error
     */
    public ValidationException(String message, Throwable cause) {
        super(message, "VALIDATION_ERROR", cause);
    }
}

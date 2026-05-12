/*
 * Excepción base para errores de lógica de negocio.
 */
package com.acosux.MSBioSuper.domain.exception;

/**
 * Excepción base para errores relacionados con reglas de negocio.
 * Todas las excepciones de negocio específicas deben extender esta clase.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0 - Java 21 Migration
 */
public class BusinessException extends RuntimeException {

    private final String errorCode;

    /**
     * Constructor con mensaje de error.
     * 
     * @param message Mensaje descriptivo del error
     */
    public BusinessException(String message) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
    }

    /**
     * Constructor con mensaje y código de error.
     * 
     * @param message Mensaje descriptivo del error
     * @param errorCode Código único del error
     */
    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor con mensaje y causa.
     * 
     * @param message Mensaje descriptivo del error
     * @param cause Causa raíz del error
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "BUSINESS_ERROR";
    }

    /**
     * Constructor completo con mensaje, código y causa.
     * 
     * @param message Mensaje descriptivo del error
     * @param errorCode Código único del error
     * @param cause Causa raíz del error
     */
    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Obtiene el código de error.
     * 
     * @return Código único del error
     */
    public String getErrorCode() {
        return errorCode;
    }
}

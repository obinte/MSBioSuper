/*
 * Excepción para recursos no encontrados.
 */
package com.acosux.MSBioSuper.domain.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no existe en el sistema.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0 - Java 21 Migration
 */
public class ResourceNotFoundException extends BusinessException {

    /**
     * Constructor con mensaje de error.
     * 
     * @param message Mensaje descriptivo del recurso no encontrado
     */
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }

    /**
     * Constructor con tipo de recurso e identificador.
     * 
     * @param resourceType Tipo de recurso (ej: "Liquidación", "Producto")
     * @param resourceId Identificador del recurso
     */
    public ResourceNotFoundException(String resourceType, Object resourceId) {
        super(String.format("%s con ID '%s' no encontrado", resourceType, resourceId), 
              "RESOURCE_NOT_FOUND");
    }
}

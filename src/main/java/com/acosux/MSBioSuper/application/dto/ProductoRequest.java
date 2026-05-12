package com.acosux.MSBioSuper.application.dto;

import com.acosux.MSBioSuper.application.validation.ValidEmpresaCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO de solicitud para obtener productos.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
public record ProductoRequest(
    @NotBlank(message = "El código de empresa es obligatorio")
    @ValidEmpresaCode
    String empresa,
    
    @NotBlank(message = "El código de bodega es obligatorio")
    @Size(min = 1, max = 20, message = "El código de bodega debe tener entre 1 y 20 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "El código de bodega solo puede contener letras, números y guiones")
    String bodega,
    
    @Size(max = 50, message = "El código a buscar no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "El código a buscar solo puede contener letras, números, espacios y guiones")
    String codigoBuscar,
    
    @Size(max = 50, message = "La categoría no puede exceder 50 caracteres")
    String categoria,
    
    @Size(max = 100, message = "La búsqueda no puede exceder 100 caracteres")
    String busqueda,
    
    @Positive(message = "El tipo de precio debe ser un número positivo")
    Integer tipoPrecio,
    
    Boolean incluirInactivos
) {
    /**
     * Constructor compacto para validaciones adicionales si es necesario
     */
    public ProductoRequest {
        // Validaciones adicionales pueden ir aquí
    }
}

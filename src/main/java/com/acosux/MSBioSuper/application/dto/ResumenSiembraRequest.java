package com.acosux.MSBioSuper.application.dto;

import com.acosux.MSBioSuper.application.validation.ValidDateFormat;
import com.acosux.MSBioSuper.application.validation.ValidDateRange;
import com.acosux.MSBioSuper.application.validation.ValidEmpresaCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de solicitud para obtener el resumen de siembra.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
@ValidDateRange(fechaDesde = "fechaDesde", fechaHasta = "fechaHasta")
public record ResumenSiembraRequest(
    @NotBlank(message = "El código de empresa es obligatorio")
    @ValidEmpresaCode
    String empresa,
    
    @NotBlank(message = "El sector es obligatorio")
    @Size(min = 1, max = 50, message = "El sector debe tener entre 1 y 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]+$", message = "El sector solo puede contener letras, números, espacios y guiones")
    String sector,
    
    @NotNull(message = "La fecha desde es obligatoria")
    @NotBlank(message = "La fecha desde no puede estar vacía")
    @ValidDateFormat
    String fechaDesde,
    
    @NotNull(message = "La fecha hasta es obligatoria")
    @NotBlank(message = "La fecha hasta no puede estar vacía")
    @ValidDateFormat
    String fechaHasta,
    
    @Size(max = 50, message = "La piscina no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "La piscina solo puede contener letras, números, espacios y guiones")
    String piscina,
    
    @Size(max = 50, message = "La corrida no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "La corrida solo puede contener letras, números, espacios y guiones")
    String corrida
) {
    /**
     * Constructor compacto para validaciones adicionales si es necesario
     */
    public ResumenSiembraRequest {
        // Validaciones adicionales pueden ir aquí
    }
}

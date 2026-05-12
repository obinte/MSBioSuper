package com.acosux.MSBioSuper.application.dto;

import com.acosux.MSBioSuper.application.validation.ValidDateFormat;
import com.acosux.MSBioSuper.application.validation.ValidDateRange;
import com.acosux.MSBioSuper.application.validation.ValidEmpresaCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de solicitud para obtener cuentas por pagar.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
@ValidDateRange(fechaDesde = "fechaDesde", fechaHasta = "fechaHasta")
public record CuentasPorPagarRequest(
    @NotBlank(message = "El código de empresa es obligatorio")
    @ValidEmpresaCode
    String empresa,
    
    @NotNull(message = "La fecha desde es obligatoria")
    @NotBlank(message = "La fecha desde no puede estar vacía")
    @ValidDateFormat
    String fechaDesde,
    
    @NotNull(message = "La fecha hasta es obligatoria")
    @NotBlank(message = "La fecha hasta no puede estar vacía")
    @ValidDateFormat
    String fechaHasta,
    
    @Size(max = 20, message = "El ID de proveedor no puede exceder 20 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9-]*$", message = "El ID de proveedor solo puede contener letras, números y guiones")
    String proveedorId,
    
    @Size(max = 20, message = "El periodo no puede exceder 20 caracteres")
    @Pattern(regexp = "^[0-9/-]*$", message = "El periodo solo puede contener números, guiones y barras")
    String periodo,
    
    @Size(max = 100, message = "El motivo no puede exceder 100 caracteres")
    String motivo,
    
    @Size(max = 50, message = "La zona no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "La zona solo puede contener letras, números, espacios y guiones")
    String zona,
    
    Boolean proveedorRelacionado
) {
    /**
     * Constructor compacto para validaciones adicionales si es necesario
     */
    public CuentasPorPagarRequest {
        // Validaciones adicionales pueden ir aquí
    }
}

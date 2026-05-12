package com.acosux.MSBioSuper.application.dto;

import com.acosux.MSBioSuper.application.validation.ValidDateFormat;
import com.acosux.MSBioSuper.application.validation.ValidDateRange;
import com.acosux.MSBioSuper.application.validation.ValidEmpresaCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de solicitud para obtener el resumen de pesca.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
@ValidDateRange(fechaDesde = "fechaDesde", fechaHasta = "fechaHasta")
@Schema(description = "Solicitud para consultar el resumen de pesca con filtros de búsqueda")
public record ResumenPescaRequest(
    @Schema(description = "Código de la empresa", example = "01", required = true)
    @NotBlank(message = "El código de empresa es obligatorio")
    @ValidEmpresaCode
    String empresa,
    
    @Schema(description = "Código del sector", example = "SECTOR-A", required = true)
    @NotBlank(message = "El sector es obligatorio")
    @Size(min = 1, max = 50, message = "El sector debe tener entre 1 y 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]+$", message = "El sector solo puede contener letras, números, espacios y guiones")
    String sector,
    
    @Schema(description = "Fecha de inicio del rango de búsqueda (formato: yyyy-MM-dd)", example = "2024-01-01", required = true)
    @NotNull(message = "La fecha desde es obligatoria")
    @NotBlank(message = "La fecha desde no puede estar vacía")
    @ValidDateFormat
    String fechaDesde,
    
    @Schema(description = "Fecha de fin del rango de búsqueda (formato: yyyy-MM-dd)", example = "2024-12-31", required = true)
    @NotNull(message = "La fecha hasta es obligatoria")
    @NotBlank(message = "La fecha hasta no puede estar vacía")
    @ValidDateFormat
    String fechaHasta,
    
    @Schema(description = "Número de piscina (opcional)", example = "P-001", required = false)
    @Size(max = 50, message = "La piscina no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "La piscina solo puede contener letras, números, espacios y guiones")
    String piscina,
    
    @Schema(description = "Número de corrida (opcional)", example = "C-2024-01", required = false)
    @Size(max = 50, message = "La corrida no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "La corrida solo puede contener letras, números, espacios y guiones")
    String corrida
) {
    /**
     * Constructor compacto para validaciones adicionales si es necesario
     */
    public ResumenPescaRequest {
        // Validaciones adicionales pueden ir aquí
    }
}

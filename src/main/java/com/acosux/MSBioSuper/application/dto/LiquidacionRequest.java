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
 * DTO de solicitud para obtener liquidaciones.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
@ValidDateRange(fechaDesde = "fechaDesde", fechaHasta = "fechaHasta")
@Schema(description = "Solicitud para consultar liquidaciones con filtros de búsqueda")
public record LiquidacionRequest(
    @Schema(description = "Código de la empresa", example = "01", required = true)
    @NotBlank(message = "El código de empresa es obligatorio")
    @ValidEmpresaCode
    String empresa,
    
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
    
    @Schema(description = "Código del sector (opcional)", example = "SECTOR-A", required = false)
    @Size(max = 50, message = "El sector no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "El sector solo puede contener letras, números, espacios y guiones")
    String sector,
    
    @Schema(description = "Número de piscina (opcional)", example = "P-001", required = false)
    @Size(max = 50, message = "La piscina no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "La piscina solo puede contener letras, números, espacios y guiones")
    String piscina,
    
    @Schema(description = "Número de corrida (opcional)", example = "C-2024-01", required = false)
    @Size(max = 50, message = "La corrida no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "La corrida solo puede contener letras, números, espacios y guiones")
    String corrida,
    
    @Schema(description = "Nombre del cliente (opcional)", example = "Cliente XYZ S.A.", required = false)
    @Size(max = 100, message = "El cliente no puede exceder 100 caracteres")
    String cliente,
    
    @Schema(description = "Tipo de liquidación (opcional)", example = "DIRECTA", required = false)
    @Size(max = 20, message = "El tipo no puede exceder 20 caracteres")
    @Pattern(regexp = "^[A-Za-z]*$", message = "El tipo solo puede contener letras")
    String tipo
) {
    /**
     * Constructor compacto para validaciones adicionales si es necesario
     */
    public LiquidacionRequest {
        // Validaciones adicionales pueden ir aquí
    }
}

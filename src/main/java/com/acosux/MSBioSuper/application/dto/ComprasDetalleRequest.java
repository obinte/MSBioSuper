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
 * DTO de solicitud para obtener el detalle de compras por producto.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 * 
 * Este DTO incluye todos los parámetros necesarios para consultar
 * el detalle de compras, incluyendo el flag incluirIMB para
 * incluir o excluir movimientos de bodega (IMB).
 */
@ValidDateRange(fechaDesde = "fechaDesde", fechaHasta = "fechaHasta")
@Schema(description = "Solicitud para consultar el detalle de compras por producto con filtros de búsqueda")
public record ComprasDetalleRequest(
    @Schema(description = "Código de la empresa", example = "01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El código de empresa es obligatorio")
    @ValidEmpresaCode
    String empresa,
    
    @Schema(description = "Código del sector (opcional)", example = "SECTOR-A", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 50, message = "El sector no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "El sector solo puede contener letras, números, espacios y guiones")
    String sector,
    
    @Schema(description = "Número de piscina (opcional)", example = "P-001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 50, message = "La piscina no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "La piscina solo puede contener letras, números, espacios y guiones")
    String piscina,
    
    @Schema(description = "Código del proveedor (opcional)", example = "PROV-001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 50, message = "El proveedor no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "El proveedor solo puede contener letras, números, espacios y guiones")
    String proveedor,
    
    @Schema(description = "Código del producto (opcional)", example = "PROD-001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 50, message = "El producto no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "El producto solo puede contener letras, números, espacios y guiones")
    String producto,
    
    @Schema(description = "Fecha de inicio del rango de búsqueda (formato: yyyy-MM-dd)", example = "2024-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La fecha desde es obligatoria")
    @NotBlank(message = "La fecha desde no puede estar vacía")
    @ValidDateFormat
    String fechaDesde,
    
    @Schema(description = "Fecha de fin del rango de búsqueda (formato: yyyy-MM-dd)", example = "2024-12-31", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La fecha hasta es obligatoria")
    @NotBlank(message = "La fecha hasta no puede estar vacía")
    @ValidDateFormat
    String fechaHasta,
    
    @Schema(description = "Código de bodega (opcional)", example = "BOD-001", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 50, message = "La bodega no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9\\s-]*$", message = "La bodega solo puede contener letras, números, espacios y guiones")
    String bodega,
    
    @Schema(
        description = "Indica si se deben incluir movimientos de bodega (IMB). Por defecto es false.", 
        example = "false", 
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        defaultValue = "false"
    )
    Boolean incluirIMB
) {
    /**
     * Constructor compacto para establecer valores por defecto
     */
    public ComprasDetalleRequest {
        // Si incluirIMB es null, establecer false por defecto
        if (incluirIMB == null) {
            incluirIMB = false;
        }
    }
}

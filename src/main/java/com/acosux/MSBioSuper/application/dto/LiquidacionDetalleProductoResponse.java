package com.acosux.MSBioSuper.application.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO de respuesta para liquidaciones detalle por producto.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 * Mapea los resultados de la función produccion.fun_liquidacion_detalle_producto.
 */
public record LiquidacionDetalleProductoResponse(
    Integer id,
    String liqNumeroSistema,
    Date liqFecha,
    String liqClienteId,
    String liqClienteRazonSocial,
    String liqProveedorId,
    String liqProveedorRazonSocial,
    String liqLote,
    String detProductoCodigo,
    String detProductoNombre,
    String detProductoClase,
    String detProductoTipo,
    BigDecimal detLibras,
    BigDecimal detPrecio,
    String pisSector,
    String pisNumero,
    String liqTallaCodigo,
    String liqTallaDetalle,
    String liqTipoCodigo,
    String liqTipoDetalle
) {
}

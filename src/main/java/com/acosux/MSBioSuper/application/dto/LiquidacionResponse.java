package com.acosux.MSBioSuper.application.dto;

import java.math.BigDecimal;

/**
 * DTO de respuesta para liquidaciones.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
public record LiquidacionResponse(
    Integer id,
    String liqNumero,
    String liqFecha,
    String liqCliente,
    String liqLote,
    String liqCorrida,
    BigDecimal liqLibrasEnviadas,
    BigDecimal liqLibrasRecibidas,
    BigDecimal liqDiferencia,
    BigDecimal liqLibrasBasura,
    BigDecimal liqLibrasNetas,
    BigDecimal liqLibrasEntero,
    BigDecimal liqRendimiento,
    BigDecimal liq20_30,
    BigDecimal liq30_40,
    BigDecimal liq40_50,
    BigDecimal liq50_60,
    BigDecimal liq60_70,
    BigDecimal liqOtrasTallas,
    BigDecimal liqTotalMonto,
    BigDecimal liqPrecioLibraPromedio,
    String liqMes,
    String liqTipo,
    BigDecimal liqGramaje,
    String liqIdCliente
) {
}

package com.acosux.MSBioSuper.application.dto;

import java.math.BigDecimal;

/**
 * DTO de respuesta para consumos diarios.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
public record ConsumosDiariosResponse(
    Integer id,
    String consumoEmpresa,
    String consumoSector,
    String consumoPiscina,
    String consumoFecha,
    String consumoProducto,
    String consumoCodigo,
    String consumoMedida,
    BigDecimal consumoTotal,
    BigDecimal consumoCostoTotal,
    String consumoCorrida
) {
}

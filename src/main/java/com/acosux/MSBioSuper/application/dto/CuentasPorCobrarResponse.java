package com.acosux.MSBioSuper.application.dto;

import java.math.BigDecimal;

/**
 * DTO de respuesta para cuentas por cobrar.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
public record CuentasPorCobrarResponse(
    Integer id,
    String cxcdEmpresa,
    String cxcdPeriodo,
    String cxcdMotivo,
    String cxcdNumero,
    String cxcdDocumentoNumero,
    String cxcdClienteId,
    String cxcdFechaEmision,
    String cxcdFechaVencimiento,
    BigDecimal cxcdRetencionValor,
    BigDecimal cxcdAbono,
    BigDecimal cxcdSaldo,
    BigDecimal cxcdTotal
) {
}

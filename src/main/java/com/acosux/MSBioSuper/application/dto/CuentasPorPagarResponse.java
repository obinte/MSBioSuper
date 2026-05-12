package com.acosux.MSBioSuper.application.dto;

import java.math.BigDecimal;

/**
 * DTO de respuesta para cuentas por pagar.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
public record CuentasPorPagarResponse(
    Integer id,
    String cxpdPeriodo,
    String cxpdMotivo,
    String cxpdNumero,
    String cxpdDocumento,
    String cxpdObservaciones,
    String cxpdProveedorCodigo,
    String cxpdProveedorId,
    String cxpdProveedorRazonSocial,
    String cxpdFechaEmision,
    String cxpdFechaVencimiento,
    BigDecimal cxpdSaldo,
    String cxpdUsuarioApruebaPago,
    String cxpdDiasCredito,
    String cxpdDiasVencidos,
    String cxpdZona,
    BigDecimal cxpdSaldoVencido,
    BigDecimal cxpdValorAprobado,
    BigDecimal cxpdAbonos,
    BigDecimal cxpdSaldoPorVencer,
    String cxpdCp,
    Boolean cxpdProvRelacionado
) {
}

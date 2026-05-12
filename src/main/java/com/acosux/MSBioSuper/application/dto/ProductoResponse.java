package com.acosux.MSBioSuper.application.dto;

import java.math.BigDecimal;

/**
 * DTO de respuesta para productos.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
public record ProductoResponse(
    Integer id,
    String proCodigoPrincipal,
    String proNombre,
    String proCategoria,
    String proEmpaque,
    BigDecimal stockSaldo,
    BigDecimal stockUltimoCosto,
    BigDecimal stockCostoPromedio,
    BigDecimal factorConversion,
    String detalleMedida,
    BigDecimal proPrecio,
    BigDecimal proCantidad,
    BigDecimal proPrecioBase,
    BigDecimal proDescuento,
    BigDecimal proMargenUtilidad,
    BigDecimal proPrecio2,
    BigDecimal proPrecio3,
    String proGravaIva,
    String tipTipo,
    boolean precioFijo,
    boolean proInactivo,
    boolean proExigirSerie,
    BigDecimal proCostoReferencial,
    Boolean tieneImagenes,
    String proCreditoTributario,
    Boolean proEcommerce,
    String tipTipoTipoProducto,
    BigDecimal icePorcentaje,
    BigDecimal iceTarifaFija,
    String iceCodigo,
    Boolean lpspExigirLote,
    String lpspSubcategoria,
    String lpspCuentaInventario,
    String lpspNombreInventario,
    String lpspCuentaCostoConsumo,
    String lpspNombreCostoConsumo,
    String lpspPiCodigo,
    BigDecimal lpspPiPorcentaje
) {
}

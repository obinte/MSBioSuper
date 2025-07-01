package com.acosux.MSBiosuper.util.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InvListaProductosGeneralTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "lpsp_codigo_principal")
    private String proCodigoPrincipal;
    @Column(name = "lpsp_nombre")
    private String proNombre;
    @Column(name = "lpsp_categoria")
    private String proCategoria;
    @Column(name = "lpsp_empaque")
    private String proEmpaque;
    @Column(name = "lpsp_saldo")
    private BigDecimal stockSaldo;
    @Column(name = "lpsp_ultimo_costo")
    private BigDecimal stockUltimoCosto;
    @Column(name = "lpsp_costo_promedio")
    private BigDecimal stockCostoPromedio;
    @Column(name = "lpsp_factor_conversion")
    private BigDecimal factorConversion;
    @Column(name = "lpsp_medida")
    private String detalleMedida;
    @Column(name = "lpsp_precio")
    private BigDecimal proPrecio;
    @Column(name = "lpsp_cantidad")
    private BigDecimal proCantidad;
    @Column(name = "lpsp_base_imponible")
    private BigDecimal proPrecioBase;
    @Column(name = "lpsp_descuento")
    private BigDecimal proDescuento;
    @Column(name = "lpsp_margen_utilidad")
    private BigDecimal proMargenUtilidad;
    @Column(name = "lpsp_precio2")
    private BigDecimal proPrecio2;
    @Column(name = "lpsp_precio3")
    private BigDecimal proPrecio3;
    @Column(name = "lpsp_iva")
    private String proGravaIva;
    @Column(name = "lpsp_tipo")
    private String tipTipo;
    @Column(name = "lpsp_precio_fijo")
    private boolean precioFijo;
    @Column(name = "lpsp_inactivo")
    private boolean proInactivo;
    @Column(name = "lpsp_exigir_serie")
    private boolean proExigirSerie;
    @Column(name = "lpsp_costo_referencial")
    private BigDecimal proCostoReferencial;
    @Column(name = "lpsp_imagenes")
    private Boolean tieneImagenes;
    @Column(name = "lpsp_credito_tributario")
    private String proCreditoTributario;
    @Column(name = "lpsp_ecommerce")
    private Boolean proEcommerce;
    @Column(name = "lpsp_tipo_tipo_producto")
    private String tipTipoTipoProducto;
    @Column(name = "lpsp_porcentaje_ice")
    private BigDecimal icePorcentaje;
    @Column(name = "lpsp_tarifa_fija_ice")
    private BigDecimal iceTarifaFija;
    @Column(name = "lpsp_ice_codigo")
    private String iceCodigo;
    @Column(name = "lpsp_exigir_lote")
    private Boolean lpspExigirLote;
    @Column(name = "lpsp_subcategoria")
    private String lpspSubcategoria;
    @Column(name = "lpsp_cuenta_inventario")
    private String lpspCuentaInventario;
    @Column(name = "lpsp_nombre_inventario")
    private String lpspNombreInventario;
    @Column(name = "lpsp_cuenta_costo_consumo")
    private String lpspCuentaCostoConsumo;
    @Column(name = "lpsp_nombre_costo_consumo")
    private String lpspNombreCostoConsumo;
    @Column(name = "lpsp_pi_codigo")
    private String lpspPiCodigo;
    @Column(name = "lpsp_pi_porcentaje")
    private BigDecimal lpspPiPorcentaje;

    public InvListaProductosGeneralTO() {
    }

    public InvListaProductosGeneralTO(Integer id, String proCodigoPrincipal, String proNombre, String proCategoria, String proEmpaque, BigDecimal stockSaldo, BigDecimal stockUltimoCosto, BigDecimal stockCostoPromedio, String detalleMedida, BigDecimal proPrecio, BigDecimal proCantidad, BigDecimal proDescuento, BigDecimal proMargenUtilidad, String proGravaIva, boolean proInactivo, BigDecimal proCostoReferencial) {
        this.id = id;
        this.proCodigoPrincipal = proCodigoPrincipal;
        this.proNombre = proNombre;
        this.proCategoria = proCategoria;
        this.proEmpaque = proEmpaque;
        this.stockSaldo = stockSaldo;
        this.stockUltimoCosto = stockUltimoCosto;
        this.stockCostoPromedio = stockCostoPromedio;
        this.detalleMedida = detalleMedida;
        this.proPrecio = proPrecio;
        this.proCantidad = proCantidad;
        this.proDescuento = proDescuento;
        this.proMargenUtilidad = proMargenUtilidad;
        this.proGravaIva = proGravaIva;
        this.proInactivo = proInactivo;
        this.proCostoReferencial = proCostoReferencial;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProCodigoPrincipal() {
        return proCodigoPrincipal;
    }

    public void setProCodigoPrincipal(String proCodigoPrincipal) {
        this.proCodigoPrincipal = proCodigoPrincipal;
    }

    public String getProNombre() {
        return proNombre;
    }

    public void setProNombre(String proNombre) {
        this.proNombre = proNombre;
    }

    public String getProCategoria() {
        return proCategoria;
    }

    public void setProCategoria(String proCategoria) {
        this.proCategoria = proCategoria;
    }

    public String getProEmpaque() {
        return proEmpaque;
    }

    public void setProEmpaque(String proEmpaque) {
        this.proEmpaque = proEmpaque;
    }

    public BigDecimal getStockSaldo() {
        return stockSaldo;
    }

    public void setStockSaldo(BigDecimal stockSaldo) {
        this.stockSaldo = stockSaldo;
    }

    public BigDecimal getStockUltimoCosto() {
        return stockUltimoCosto;
    }

    public void setStockUltimoCosto(BigDecimal stockUltimoCosto) {
        this.stockUltimoCosto = stockUltimoCosto;
    }

    public BigDecimal getStockCostoPromedio() {
        return stockCostoPromedio;
    }

    public void setStockCostoPromedio(BigDecimal stockCostoPromedio) {
        this.stockCostoPromedio = stockCostoPromedio;
    }

    public String getDetalleMedida() {
        return detalleMedida;
    }

    public void setDetalleMedida(String detalleMedida) {
        this.detalleMedida = detalleMedida;
    }

    public BigDecimal getProPrecio() {
        return proPrecio;
    }

    public void setProPrecio(BigDecimal proPrecio) {
        this.proPrecio = proPrecio;
    }

    public BigDecimal getProCantidad() {
        return proCantidad;
    }

    public void setProCantidad(BigDecimal proCantidad) {
        this.proCantidad = proCantidad;
    }

    public BigDecimal getProDescuento() {
        return proDescuento;
    }

    public void setProDescuento(BigDecimal proDescuento) {
        this.proDescuento = proDescuento;
    }

    public BigDecimal getProMargenUtilidad() {
        return proMargenUtilidad;
    }

    public void setProMargenUtilidad(BigDecimal proMargenUtilidad) {
        this.proMargenUtilidad = proMargenUtilidad;
    }

    public String getProGravaIva() {
        return proGravaIva;
    }

    public void setProGravaIva(String proGravaIva) {
        this.proGravaIva = proGravaIva;
    }

    public boolean isProInactivo() {
        return proInactivo;
    }

    public void setProInactivo(boolean proInactivo) {
        this.proInactivo = proInactivo;
    }

    public String getTipTipo() {
        return tipTipo;
    }

    public void setTipTipo(String tipTipo) {
        this.tipTipo = tipTipo;
    }

    public BigDecimal getFactorConversion() {
        return factorConversion;
    }

    public void setFactorConversion(BigDecimal factorConversion) {
        this.factorConversion = factorConversion;
    }

    public boolean isPrecioFijo() {
        return precioFijo;
    }

    public void setPrecioFijo(boolean precioFijo) {
        this.precioFijo = precioFijo;
    }

    public BigDecimal getProPrecioBase() {
        return proPrecioBase;
    }

    public void setProPrecioBase(BigDecimal proPrecioBase) {
        this.proPrecioBase = proPrecioBase;
    }

    public boolean isProExigirSerie() {
        return proExigirSerie;
    }

    public void setProExigirSerie(boolean proExigirSerie) {
        this.proExigirSerie = proExigirSerie;
    }

    public BigDecimal getProCostoReferencial() {
        return proCostoReferencial;
    }

    public void setProCostoReferencial(BigDecimal proCostoReferencial) {
        this.proCostoReferencial = proCostoReferencial;
    }

    public Boolean getTieneImagenes() {
        return tieneImagenes;
    }

    public void setTieneImagenes(Boolean tieneImagenes) {
        this.tieneImagenes = tieneImagenes;
    }

    public String getProCreditoTributario() {
        return proCreditoTributario;
    }

    public void setProCreditoTributario(String proCreditoTributario) {
        this.proCreditoTributario = proCreditoTributario;
    }

    public Boolean getProEcommerce() {
        return proEcommerce;
    }

    public void setProEcommerce(Boolean proEcommerce) {
        this.proEcommerce = proEcommerce;
    }

    public String getTipTipoTipoProducto() {
        return tipTipoTipoProducto;
    }

    public void setTipTipoTipoProducto(String tipTipoTipoProducto) {
        this.tipTipoTipoProducto = tipTipoTipoProducto;
    }

    public BigDecimal getIcePorcentaje() {
        return icePorcentaje;
    }

    public void setIcePorcentaje(BigDecimal icePorcentaje) {
        this.icePorcentaje = icePorcentaje;
    }

    public BigDecimal getIceTarifaFija() {
        return iceTarifaFija;
    }

    public void setIceTarifaFija(BigDecimal iceTarifaFija) {
        this.iceTarifaFija = iceTarifaFija;
    }

    public String getIceCodigo() {
        return iceCodigo;
    }

    public void setIceCodigo(String iceCodigo) {
        this.iceCodigo = iceCodigo;
    }

    public BigDecimal getProPrecio2() {
        return proPrecio2;
    }

    public void setProPrecio2(BigDecimal proPrecio2) {
        this.proPrecio2 = proPrecio2;
    }

    public BigDecimal getProPrecio3() {
        return proPrecio3;
    }

    public void setProPrecio3(BigDecimal proPrecio3) {
        this.proPrecio3 = proPrecio3;
    }

    public Boolean getLpspExigirLote() {
        return lpspExigirLote;
    }

    public void setLpspExigirLote(Boolean lpspExigirLote) {
        this.lpspExigirLote = lpspExigirLote;
    }

    public String getLpspSubcategoria() {
        return lpspSubcategoria;
    }

    public void setLpspSubcategoria(String lpspSubcategoria) {
        this.lpspSubcategoria = lpspSubcategoria;
    }

    public String getLpspCuentaInventario() {
        return lpspCuentaInventario;
    }

    public void setLpspCuentaInventario(String lpspCuentaInventario) {
        this.lpspCuentaInventario = lpspCuentaInventario;
    }

    public String getLpspNombreInventario() {
        return lpspNombreInventario;
    }

    public void setLpspNombreInventario(String lpspNombreInventario) {
        this.lpspNombreInventario = lpspNombreInventario;
    }

    public String getLpspCuentaCostoConsumo() {
        return lpspCuentaCostoConsumo;
    }

    public void setLpspCuentaCostoConsumo(String lpspCuentaCostoConsumo) {
        this.lpspCuentaCostoConsumo = lpspCuentaCostoConsumo;
    }

    public String getLpspNombreCostoConsumo() {
        return lpspNombreCostoConsumo;
    }

    public void setLpspNombreCostoConsumo(String lpspNombreCostoConsumo) {
        this.lpspNombreCostoConsumo = lpspNombreCostoConsumo;
    }

    public String getLpspPiCodigo() {
        return lpspPiCodigo;
    }

    public void setLpspPiCodigo(String lpspPiCodigo) {
        this.lpspPiCodigo = lpspPiCodigo;
    }

    public BigDecimal getLpspPiPorcentaje() {
        return lpspPiPorcentaje;
    }

    public void setLpspPiPorcentaje(BigDecimal lpspPiPorcentaje) {
        this.lpspPiPorcentaje = lpspPiPorcentaje;
    }
    
}

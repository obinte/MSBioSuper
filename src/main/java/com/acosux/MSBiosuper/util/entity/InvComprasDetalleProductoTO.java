package com.acosux.MSBiosuper.util.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author DEVELOPER
 */
@Entity
public class InvComprasDetalleProductoTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "comp_numero_sistema")
    private String compNumeroSistema;
    @Column(name = "comp_fecha")
    private Date compFecha;
    @Column(name = "comp_fecha_vencimiento")
    private Date compFechaVencimiento;
    @Column(name = "comp_dias_credito")
    private Integer compDiasCredito;
    @Column(name = "comp_cliente_id")
    private String compClienteId;
    @Column(name = "comp_cliente_razon_social")
    private String compClienteRazonSocial;
    @Column(name = "comp_documento_tipo")
    private String compDocumentoTipo;
    @Column(name = "comp_documento_numero")
    private String compDocumentoNumero;
    @Column(name = "det_bodega")
    private String detBodega;
    @Column(name = "det_producto_codigo")
    private String detProductoCodigo;
    @Column(name = "det_producto_nombre")
    private String detProductoNombre;
    @Column(name = "det_producto_marca")
    private String detProductoMarca;
    @Column(name = "det_producto_tipo")
    private String detProductoTipo;

    @Column(name = "det_producto_detalle")
    private String detProductoDetalle;

    @Column(name = "det_producto_observaciones")
    private String detProductoObservaciones;
    @Column(name = "det_cantidad")
    private BigDecimal detCantidad;
    @Column(name = "det_producto_medida")
    private String detProductoMedida;
    @Column(name = "det_precio")
    private BigDecimal detPrecio;
    @Column(name = "det_subtotal")
    private BigDecimal detSubtotal;
    @Column(name = "det_ice")
    private BigDecimal detIce;
    @Column(name = "det_iva")
    private BigDecimal detIva;
    @Column(name = "det_iva_personalizado")
    private BigDecimal detIvaPersonalizado;
    @Column(name = "det_total")
    private BigDecimal detTotal;
    @Column(name = "comp_forma_pago")
    private String compFormaPago;
    @Column(name = "comp_observaciones")
    private String compObservaciones;
    @Column(name = "det_centro_produccion")
    private String detCentroProduccion;
    @Column(name = "det_centro_costo")
    private String detCentroCosto;

    @Column(name = "ec_codigo")
    private String ecCodigo;
    @Column(name = "comp_datos_adjuntos")
    private Boolean compDatosAdjuntos;
    @Column(name = "comp_motivo_pago")
    private String compMotivoPago;
    @Column(name = "det_iva_8")
    private BigDecimal detIva8;

    public InvComprasDetalleProductoTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompNumeroSistema() {
        return compNumeroSistema;
    }

    public void setCompNumeroSistema(String compNumeroSistema) {
        this.compNumeroSistema = compNumeroSistema;
    }

    public Date getCompFecha() {
        return compFecha;
    }

    public void setCompFecha(Date compFecha) {
        this.compFecha = compFecha;
    }

    public Date getCompFechaVencimiento() {
        return compFechaVencimiento;
    }

    public void setCompFechaVencimiento(Date compFechaVencimiento) {
        this.compFechaVencimiento = compFechaVencimiento;
    }

    public Integer getCompDiasCredito() {
        return compDiasCredito;
    }

    public void setCompDiasCredito(Integer compDiasCredito) {
        this.compDiasCredito = compDiasCredito;
    }

    public String getCompClienteId() {
        return compClienteId;
    }

    public void setCompClienteId(String compClienteId) {
        this.compClienteId = compClienteId;
    }

    public String getCompClienteRazonSocial() {
        return compClienteRazonSocial;
    }

    public void setCompClienteRazonSocial(String compClienteRazonSocial) {
        this.compClienteRazonSocial = compClienteRazonSocial;
    }

    public String getCompDocumentoTipo() {
        return compDocumentoTipo;
    }

    public void setCompDocumentoTipo(String compDocumentoTipo) {
        this.compDocumentoTipo = compDocumentoTipo;
    }

    public String getCompDocumentoNumero() {
        return compDocumentoNumero;
    }

    public void setCompDocumentoNumero(String compDocumentoNumero) {
        this.compDocumentoNumero = compDocumentoNumero;
    }

    public String getDetBodega() {
        return detBodega;
    }

    public void setDetBodega(String detBodega) {
        this.detBodega = detBodega;
    }

    public String getDetProductoCodigo() {
        return detProductoCodigo;
    }

    public void setDetProductoCodigo(String detProductoCodigo) {
        this.detProductoCodigo = detProductoCodigo;
    }

    public String getDetProductoNombre() {
        return detProductoNombre;
    }

    public void setDetProductoNombre(String detProductoNombre) {
        this.detProductoNombre = detProductoNombre;
    }

    public String getDetProductoMarca() {
        return detProductoMarca;
    }

    public void setDetProductoMarca(String detProductoMarca) {
        this.detProductoMarca = detProductoMarca;
    }

    public String getDetProductoTipo() {
        return detProductoTipo;
    }

    public void setDetProductoTipo(String detProductoTipo) {
        this.detProductoTipo = detProductoTipo;
    }

    public BigDecimal getDetCantidad() {
        return detCantidad;
    }

    public void setDetCantidad(BigDecimal detCantidad) {
        this.detCantidad = detCantidad;
    }

    public String getDetProductoMedida() {
        return detProductoMedida;
    }

    public void setDetProductoMedida(String detProductoMedida) {
        this.detProductoMedida = detProductoMedida;
    }

    public BigDecimal getDetPrecio() {
        return detPrecio;
    }

    public void setDetPrecio(BigDecimal detPrecio) {
        this.detPrecio = detPrecio;
    }

    public BigDecimal getDetSubtotal() {
        return detSubtotal;
    }

    public void setDetSubtotal(BigDecimal detSubtotal) {
        this.detSubtotal = detSubtotal;
    }

    public BigDecimal getDetIce() {
        return detIce;
    }

    public void setDetIce(BigDecimal detIce) {
        this.detIce = detIce;
    }

    public BigDecimal getDetIva() {
        return detIva;
    }

    public void setDetIva(BigDecimal detIva) {
        this.detIva = detIva;
    }

    public BigDecimal getDetIvaPersonalizado() {
        return detIvaPersonalizado;
    }

    public void setDetIvaPersonalizado(BigDecimal detIvaPersonalizado) {
        this.detIvaPersonalizado = detIvaPersonalizado;
    }

    public BigDecimal getDetTotal() {
        return detTotal;
    }

    public void setDetTotal(BigDecimal detTotal) {
        this.detTotal = detTotal;
    }

    public String getCompFormaPago() {
        return compFormaPago;
    }

    public void setCompFormaPago(String compFormaPago) {
        this.compFormaPago = compFormaPago;
    }

    public String getCompObservaciones() {
        return compObservaciones;
    }

    public void setCompObservaciones(String compObservaciones) {
        this.compObservaciones = compObservaciones;
    }

    public String getDetCentroProduccion() {
        return detCentroProduccion;
    }

    public void setDetCentroProduccion(String detCentroProduccion) {
        this.detCentroProduccion = detCentroProduccion;
    }

    public String getDetCentroCosto() {
        return detCentroCosto;
    }

    public void setDetCentroCosto(String detCentroCosto) {
        this.detCentroCosto = detCentroCosto;
    }

    public String getDetProductoObservaciones() {
        return detProductoObservaciones;
    }

    public void setDetProductoObservaciones(String detProductoObservaciones) {
        this.detProductoObservaciones = detProductoObservaciones;
    }

    public String getDetProductoDetalle() {
        return detProductoDetalle;
    }

    public void setDetProductoDetalle(String detProductoDetalle) {
        this.detProductoDetalle = detProductoDetalle;
    }

    public String getEcCodigo() {
        return ecCodigo;
    }

    public void setEcCodigo(String ecCodigo) {
        this.ecCodigo = ecCodigo;
    }

    public Boolean getCompDatosAdjuntos() {
        return compDatosAdjuntos;
    }

    public void setCompDatosAdjuntos(Boolean compDatosAdjuntos) {
        this.compDatosAdjuntos = compDatosAdjuntos;
    }

    public String getCompMotivoPago() {
        return compMotivoPago;
    }

    public void setCompMotivoPago(String compMotivoPago) {
        this.compMotivoPago = compMotivoPago;
    }

    public BigDecimal getDetIva8() {
        return detIva8;
    }

    public void setDetIva8(BigDecimal detIva8) {
        this.detIva8 = detIva8;
    }

}

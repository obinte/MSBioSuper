/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.util.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author User
 */
@Entity
public class PrdLiquidacionDetalleProductoTO implements Serializable {

    @Id
    private Integer id;
    @Column(name = "liq_numero_sistema")
    private String liqNumeroSistema;

    @Column(name = "liq_fecha")
    private Date liqFecha;

    @Column(name = "liq_cliente_id")
    private String liqClienteId;

    @Column(name = "liq_cliente_razon_social")
    private String liqClienteRazonSocial;

    @Column(name = "liq_proveedor_id")
    private String liqProveedorId;

    @Column(name = "liq_proveedor_razon_social")
    private String liqProveedorRazonSocial;

    @Column(name = "liq_lote")
    private String liqLote;

    @Column(name = "det_producto_codigo")
    private String detProductoCodigo;

    @Column(name = "det_producto_nombre")
    private String detProductoNombre;

    @Column(name = "det_producto_clase")
    private String detProductoClase;

    @Column(name = "det_producto_tipo")
    private String detProductoTipo;

    @Column(name = "det_libras")
    private BigDecimal detLibras;

    @Column(name = "det_precio")
    private BigDecimal detPrecio;

    @Column(name = "pis_sector")
    private String pisSector;

    @Column(name = "pis_numero")
    private String pisNumero;

    @Column(name = "liq_talla_codigo")
    private String liqTallaCodigo;

    @Column(name = "liq_talla_detalle")
    private String liqTallaDetalle;

    @Column(name = "liq_tipo_codigo")
    private String liqTipoCodigo;

    @Column(name = "liq_tipo_detalle")
    private String liqTipoDetalle;

    public PrdLiquidacionDetalleProductoTO() {
    }

    public PrdLiquidacionDetalleProductoTO(Integer id, String liqNumeroSistema, Date liqFecha, String liqClienteId, String liqClienteRazonSocial, String liqLote, String detProductoCodigo, String detProductoNombre, String detProductoClase, String detProductoTipo, BigDecimal detLibras, BigDecimal detPrecio, String pisSector, String pisNumero, String liqTallaCodigo, String liqTallaDetalle, String liqTipoCodigo, String liqTipoDetalle) {
        this.id = id;
        this.liqNumeroSistema = liqNumeroSistema;
        this.liqFecha = liqFecha;
        this.liqClienteId = liqClienteId;
        this.liqClienteRazonSocial = liqClienteRazonSocial;
        this.liqLote = liqLote;
        this.detProductoCodigo = detProductoCodigo;
        this.detProductoNombre = detProductoNombre;
        this.detProductoClase = detProductoClase;
        this.detProductoTipo = detProductoTipo;
        this.detLibras = detLibras;
        this.detPrecio = detPrecio;
        this.pisSector = pisSector;
        this.pisNumero = pisNumero;
        this.liqTallaCodigo = liqTallaCodigo;
        this.liqTallaDetalle = liqTallaDetalle;
        this.liqTipoCodigo = liqTipoCodigo;
        this.liqTipoDetalle = liqTipoDetalle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLiqNumeroSistema() {
        return liqNumeroSistema;
    }

    public void setLiqNumeroSistema(String liqNumeroSistema) {
        this.liqNumeroSistema = liqNumeroSistema;
    }

    public Date getLiqFecha() {
        return liqFecha;
    }

    public void setLiqFecha(Date liqFecha) {
        this.liqFecha = liqFecha;
    }

    public String getLiqClienteId() {
        return liqClienteId;
    }

    public void setLiqClienteId(String liqClienteId) {
        this.liqClienteId = liqClienteId;
    }

    public String getLiqClienteRazonSocial() {
        return liqClienteRazonSocial;
    }

    public void setLiqClienteRazonSocial(String liqClienteRazonSocial) {
        this.liqClienteRazonSocial = liqClienteRazonSocial;
    }

    public String getLiqLote() {
        return liqLote;
    }

    public void setLiqLote(String liqLote) {
        this.liqLote = liqLote;
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

    public String getDetProductoClase() {
        return detProductoClase;
    }

    public void setDetProductoClase(String detProductoClase) {
        this.detProductoClase = detProductoClase;
    }

    public String getDetProductoTipo() {
        return detProductoTipo;
    }

    public void setDetProductoTipo(String detProductoTipo) {
        this.detProductoTipo = detProductoTipo;
    }

    public BigDecimal getDetLibras() {
        return detLibras;
    }

    public void setDetLibras(BigDecimal detLibras) {
        this.detLibras = detLibras;
    }

    public BigDecimal getDetPrecio() {
        return detPrecio;
    }

    public void setDetPrecio(BigDecimal detPrecio) {
        this.detPrecio = detPrecio;
    }

    public String getPisSector() {
        return pisSector;
    }

    public void setPisSector(String pisSector) {
        this.pisSector = pisSector;
    }

    public String getPisNumero() {
        return pisNumero;
    }

    public void setPisNumero(String pisNumero) {
        this.pisNumero = pisNumero;
    }

    public String getLiqTallaCodigo() {
        return liqTallaCodigo;
    }

    public void setLiqTallaCodigo(String liqTallaCodigo) {
        this.liqTallaCodigo = liqTallaCodigo;
    }

    public String getLiqTallaDetalle() {
        return liqTallaDetalle;
    }

    public void setLiqTallaDetalle(String liqTallaDetalle) {
        this.liqTallaDetalle = liqTallaDetalle;
    }

    public String getLiqTipoCodigo() {
        return liqTipoCodigo;
    }

    public void setLiqTipoCodigo(String liqTipoCodigo) {
        this.liqTipoCodigo = liqTipoCodigo;
    }

    public String getLiqTipoDetalle() {
        return liqTipoDetalle;
    }

    public void setLiqTipoDetalle(String liqTipoDetalle) {
        this.liqTipoDetalle = liqTipoDetalle;
    }

    public String getLiqProveedorId() {
        return liqProveedorId;
    }

    public void setLiqProveedorId(String liqProveedorId) {
        this.liqProveedorId = liqProveedorId;
    }

    public String getLiqProveedorRazonSocial() {
        return liqProveedorRazonSocial;
    }

    public void setLiqProveedorRazonSocial(String liqProveedorRazonSocial) {
        this.liqProveedorRazonSocial = liqProveedorRazonSocial;
    }

    @Override
    public String toString() {
        return "PrdLiquidacionDetalleProductoTO{" + "id=" + id + ", liqNumeroSistema=" + liqNumeroSistema + ", liqFecha=" + liqFecha + ", liqClienteId=" + liqClienteId + ", liqClienteRazonSocial=" + liqClienteRazonSocial + ", liqLote=" + liqLote + ", detProductoCodigo=" + detProductoCodigo + ", detProductoNombre=" + detProductoNombre + ", detProductoClase=" + detProductoClase + ", detProductoTipo=" + detProductoTipo + ", detLibras=" + detLibras + ", detPrecio=" + detPrecio + ", pisSector=" + pisSector + ", pisNumero=" + pisNumero + ", liqTallaCodigo=" + liqTallaCodigo + ", liqTallaDetalle=" + liqTallaDetalle + ", liqTipoCodigo=" + liqTipoCodigo + ", liqTipoDetalle=" + liqTipoDetalle + '}';
    }

}

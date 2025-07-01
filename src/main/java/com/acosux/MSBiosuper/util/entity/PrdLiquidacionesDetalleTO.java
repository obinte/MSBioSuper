/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.acosux.MSBiosuper.util.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author User
 */
@Entity
public class PrdLiquidacionesDetalleTO implements Serializable {

    @Id
    private Integer id;

    @Column(name = "liq_numero")
    private String liqNumero;

    @Column(name = "liq_fecha")
    private String liqFecha;

    @Column(name = "liq_cliente")
    private String liqCliente;

    @Column(name = "liq_lote")
    private String liqLote;

    @Column(name = "liq_corrida")
    private String liqCorrida;

    @Column(name = "liq_libras_enviadas")
    private BigDecimal liqLibrasEnviadas;

    @Column(name = "liq_libras_recibidas")
    private BigDecimal liqLibrasRecibidas;

    @Column(name = "liq_diferencia")
    private BigDecimal liqDiferencia;

    @Column(name = "liq_libras_basura")
    private BigDecimal liqLibrasBasura;

    @Column(name = "liq_libras_netas")
    private BigDecimal liqLibrasNetas;

    @Column(name = "liq_libras_entero")
    private BigDecimal liqLibrasEntero;

    @Column(name = "liq_rendimiento")
    private BigDecimal liqRendimiento;

    @Column(name = "liq_20/30")
    private BigDecimal liq20_30;

    @Column(name = "liq_30/40")
    private BigDecimal liq30_40;

    @Column(name = "liq_40/50")
    private BigDecimal liq40_50;

    @Column(name = "liq_50/60")
    private BigDecimal liq50_60;

    @Column(name = "liq_60/70")
    private BigDecimal liq60_70;

    @Column(name = "liq_otras_tallas")
    private BigDecimal liqOtrasTallas;

    @Column(name = "liq_total_monto")
    private BigDecimal liqTotalMonto;

    @Column(name = "liq_precio_libra_promedio")
    private BigDecimal liqPrecioLibraPromedio;

    @Column(name = "liq_mes")
    private String liqMes;

    @Column(name = "liq_tipo")
    private String liqTipo;

    @Column(name = "liq_gramaje")
    private BigDecimal liqGramaje;
    
    @Column(name = "liq_id_cliente")
    private String liqIdCliente;

    public PrdLiquidacionesDetalleTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLiqNumero() {
        return liqNumero;
    }

    public void setLiqNumero(String liqNumero) {
        this.liqNumero = liqNumero;
    }

    public String getLiqFecha() {
        return liqFecha;
    }

    public void setLiqFecha(String liqFecha) {
        this.liqFecha = liqFecha;
    }

    public String getLiqCliente() {
        return liqCliente;
    }

    public void setLiqCliente(String liqCliente) {
        this.liqCliente = liqCliente;
    }

    public String getLiqLote() {
        return liqLote;
    }

    public void setLiqLote(String liqLote) {
        this.liqLote = liqLote;
    }

    public String getLiqCorrida() {
        return liqCorrida;
    }

    public void setLiqCorrida(String liqCorrida) {
        this.liqCorrida = liqCorrida;
    }

    public BigDecimal getLiqLibrasEnviadas() {
        return liqLibrasEnviadas;
    }

    public void setLiqLibrasEnviadas(BigDecimal liqLibrasEnviadas) {
        this.liqLibrasEnviadas = liqLibrasEnviadas;
    }

    public BigDecimal getLiqLibrasRecibidas() {
        return liqLibrasRecibidas;
    }

    public void setLiqLibrasRecibidas(BigDecimal liqLibrasRecibidas) {
        this.liqLibrasRecibidas = liqLibrasRecibidas;
    }

    public BigDecimal getLiqDiferencia() {
        return liqDiferencia;
    }

    public void setLiqDiferencia(BigDecimal liqDiferencia) {
        this.liqDiferencia = liqDiferencia;
    }

    public BigDecimal getLiqLibrasBasura() {
        return liqLibrasBasura;
    }

    public void setLiqLibrasBasura(BigDecimal liqLibrasBasura) {
        this.liqLibrasBasura = liqLibrasBasura;
    }

    public BigDecimal getLiqLibrasNetas() {
        return liqLibrasNetas;
    }

    public void setLiqLibrasNetas(BigDecimal liqLibrasNetas) {
        this.liqLibrasNetas = liqLibrasNetas;
    }

    public BigDecimal getLiqLibrasEntero() {
        return liqLibrasEntero;
    }

    public void setLiqLibrasEntero(BigDecimal liqLibrasEntero) {
        this.liqLibrasEntero = liqLibrasEntero;
    }

    public BigDecimal getLiqRendimiento() {
        return liqRendimiento;
    }

    public void setLiqRendimiento(BigDecimal liqRendimiento) {
        this.liqRendimiento = liqRendimiento;
    }

    public BigDecimal getLiq20_30() {
        return liq20_30;
    }

    public void setLiq20_30(BigDecimal liq20_30) {
        this.liq20_30 = liq20_30;
    }

    public BigDecimal getLiq30_40() {
        return liq30_40;
    }

    public void setLiq30_40(BigDecimal liq30_40) {
        this.liq30_40 = liq30_40;
    }

    public BigDecimal getLiq40_50() {
        return liq40_50;
    }

    public void setLiq40_50(BigDecimal liq40_50) {
        this.liq40_50 = liq40_50;
    }

    public BigDecimal getLiq50_60() {
        return liq50_60;
    }

    public void setLiq50_60(BigDecimal liq50_60) {
        this.liq50_60 = liq50_60;
    }

    public BigDecimal getLiq60_70() {
        return liq60_70;
    }

    public void setLiq60_70(BigDecimal liq60_70) {
        this.liq60_70 = liq60_70;
    }

    public BigDecimal getLiqOtrasTallas() {
        return liqOtrasTallas;
    }

    public void setLiqOtrasTallas(BigDecimal liqOtrasTallas) {
        this.liqOtrasTallas = liqOtrasTallas;
    }

    public BigDecimal getLiqTotalMonto() {
        return liqTotalMonto;
    }

    public void setLiqTotalMonto(BigDecimal liqTotalMonto) {
        this.liqTotalMonto = liqTotalMonto;
    }

    public BigDecimal getLiqPrecioLibraPromedio() {
        return liqPrecioLibraPromedio;
    }

    public void setLiqPrecioLibraPromedio(BigDecimal liqPrecioLibraPromedio) {
        this.liqPrecioLibraPromedio = liqPrecioLibraPromedio;
    }

    public String getLiqMes() {
        return liqMes;
    }

    public void setLiqMes(String liqMes) {
        this.liqMes = liqMes;
    }

    public String getLiqTipo() {
        return liqTipo;
    }

    public void setLiqTipo(String liqTipo) {
        this.liqTipo = liqTipo;
    }

    public BigDecimal getLiqGramaje() {
        return liqGramaje;
    }

    public void setLiqGramaje(BigDecimal liqGramaje) {
        this.liqGramaje = liqGramaje;
    }

    public String getLiqIdCliente() {
        return liqIdCliente;
    }

    public void setLiqIdCliente(String liqIdCliente) {
        this.liqIdCliente = liqIdCliente;
    }

}

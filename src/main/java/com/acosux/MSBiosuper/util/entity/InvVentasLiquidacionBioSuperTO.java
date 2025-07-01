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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author CarolValdiviezo
 */
@Entity
@Table(name = "inv_ventas_liquidacion", schema = "inventario")
public class InvVentasLiquidacionBioSuperTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "det_secuencial")
    private Integer detSecuencial;
    @JoinColumns({
        @JoinColumn(name = "vta_empresa", referencedColumnName = "vta_empresa"),
        @JoinColumn(name = "vta_periodo", referencedColumnName = "vta_periodo"),
        @JoinColumn(name = "vta_motivo", referencedColumnName = "vta_motivo"),
        @JoinColumn(name = "vta_numero", referencedColumnName = "vta_numero")})
    @ManyToOne(optional = false)
    private InvVentasBioSuperTO invVentas;
    @Column(name = "liq_empresa")
    private String liqEmpresa;
    @Column(name = "liq_motivo")
    private String liqMotivo;
    @Column(name = "liq_numero")
    private String liqNumero;
    @Column(name = "liq_total")
    private BigDecimal liqTotal;

    public InvVentasLiquidacionBioSuperTO() {
    }

    public Integer getDetSecuencial() {
        return detSecuencial;
    }

    public void setDetSecuencial(Integer detSecuencial) {
        this.detSecuencial = detSecuencial;
    }

    public InvVentasBioSuperTO getInvVentas() {
        return invVentas;
    }

    public void setInvVentas(InvVentasBioSuperTO invVentas) {
        this.invVentas = new InvVentasBioSuperTO(invVentas.getInvVentasPK());
    }

    public String getLiqEmpresa() {
        return liqEmpresa;
    }

    public void setLiqEmpresa(String liqEmpresa) {
        this.liqEmpresa = liqEmpresa;
    }

    public String getLiqMotivo() {
        return liqMotivo;
    }

    public void setLiqMotivo(String liqMotivo) {
        this.liqMotivo = liqMotivo;
    }

    public String getLiqNumero() {
        return liqNumero;
    }

    public void setLiqNumero(String liqNumero) {
        this.liqNumero = liqNumero;
    }

    public BigDecimal getLiqTotal() {
        return liqTotal;
    }

    public void setLiqTotal(BigDecimal liqTotal) {
        this.liqTotal = liqTotal;
    }

}

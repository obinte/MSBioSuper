package com.acosux.MSBiosuper.util.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CuentasPorCobrarDetalladoApiTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "cxcd_empresa")
    private String cxcdEmpresa;
    @Column(name = "cxcd_periodo")
    private String cxcdPeriodo;
    @Column(name = "cxcd_motivo")
    private String cxcdMotivo;
    @Column(name = "cxcd_numero")
    private String cxcdNumero;
    @Column(name = "cxcd_documento_numero")
    private String cxcdDocumentoNumero;
    @Column(name = "cxcd_cliente_id")
    private String cxcdClienteId;
    @Column(name = "cxcd_fecha_emision")
    private String cxcdFechaEmision;
    @Column(name = "cxcd_fecha_vencimiento")
    private String cxcdFechaVencimiento;
    @Column(name = "cxcd_retencion_valor")
    private BigDecimal cxcdRetencionValor;
    @Column(name = "cxcd_abono")
    private BigDecimal cxcdAbono;
    @Column(name = "cxcd_saldo")
    private BigDecimal cxcdSaldo;
    @Column(name = "cxcd_total")
    private BigDecimal cxcdTotal;

    public CuentasPorCobrarDetalladoApiTO() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCxcdPeriodo() {
        return cxcdPeriodo;
    }

    public void setCxcdPeriodo(String cxcdPeriodo) {
        this.cxcdPeriodo = cxcdPeriodo;
    }

    public String getCxcdMotivo() {
        return cxcdMotivo;
    }

    public void setCxcdMotivo(String cxcdMotivo) {
        this.cxcdMotivo = cxcdMotivo;
    }

    public String getCxcdNumero() {
        return cxcdNumero;
    }

    public void setCxcdNumero(String cxcdNumero) {
        this.cxcdNumero = cxcdNumero;
    }

    public String getCxcdFechaEmision() {
        return cxcdFechaEmision;
    }

    public void setCxcdFechaEmision(String cxcdFechaEmision) {
        this.cxcdFechaEmision = cxcdFechaEmision;
    }

    public String getCxcdFechaVencimiento() {
        return cxcdFechaVencimiento;
    }

    public void setCxcdFechaVencimiento(String cxcdFechaVencimiento) {
        this.cxcdFechaVencimiento = cxcdFechaVencimiento;
    }

    public BigDecimal getCxcdSaldo() {
        return cxcdSaldo;
    }

    public void setCxcdSaldo(BigDecimal cxcdSaldo) {
        this.cxcdSaldo = cxcdSaldo;
    }

    public String getCxcdClienteId() {
        return cxcdClienteId;
    }

    public BigDecimal getCxcdTotal() {
        return cxcdTotal;
    }

    public void setCxcdTotal(BigDecimal cxcdTotal) {
        this.cxcdTotal = cxcdTotal;
    }

    public BigDecimal getCxcdRetencionValor() {
        return cxcdRetencionValor;
    }

    public void setCxcdRetencionValor(BigDecimal cxcdRetencionValor) {
        this.cxcdRetencionValor = cxcdRetencionValor;
    }

    public BigDecimal getCxcdAbono() {
        return cxcdAbono;
    }

    public void setCxcdAbono(BigDecimal cxcdAbono) {
        this.cxcdAbono = cxcdAbono;
    }

    public String getCxcdEmpresa() {
        return cxcdEmpresa;
    }

    public void setCxcdEmpresa(String cxcdEmpresa) {
        this.cxcdEmpresa = cxcdEmpresa;
    }

    public String getCxcdDocumentoNumero() {
        return cxcdDocumentoNumero;
    }

    public void setCxcdDocumentoNumero(String cxcdDocumentoNumero) {
        this.cxcdDocumentoNumero = cxcdDocumentoNumero;
    }

}

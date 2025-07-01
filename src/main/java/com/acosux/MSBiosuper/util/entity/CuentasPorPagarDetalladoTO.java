package com.acosux.MSBiosuper.util.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CuentasPorPagarDetalladoTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "cxpd_periodo")
    private String cxpdPeriodo;
    @Column(name = "cxpd_motivo")
    private String cxpdMotivo;
    @Column(name = "cxpd_numero")
    private String cxpdNumero;
    @Column(name = "cxpd_documento")
    private String cxpdDocumento;
    @Column(name = "cxpd_observaciones")
    private String cxpdObservaciones;
    @Column(name = "cxpd_proveedor_codigo")
    private String cxpdProveedorCodigo;
    @Column(name = "cxpd_proveedor_id")
    private String cxpdProveedorId;
    @Column(name = "cxpd_proveedor_razon_social")
    private String cxpdProveedorRazonSocial;
    @Column(name = "cxpd_fecha_emision")
    private String cxpdFechaEmision;
    @Column(name = "cxpd_fecha_vencimiento")
    private String cxpdFechaVencimiento;
    @Column(name = "cxpd_saldo")
    private BigDecimal cxpdSaldo;
    @Column(name = "cxpd_usuario_aprueba_pago")
    private String cxpdUsuarioApruebaPago;
    @Column(name = "cxpd_dias_credito")
    private String cxpdDiasCredito;
    @Column(name = "cxpd_dias_vencidos")
    private String cxpdDiasVencidos;
    @Column(name = "cxpd_zona")
    private String cxpdZona;

    @Column(name = "cxpd_saldo_vencido")
    private BigDecimal cxpdSaldoVencido;
    @Column(name = "cxpd_valor_aprobado")
    private BigDecimal cxpdValorAprobado;
    @Column(name = "cxpd_abonos")
    private BigDecimal cxpdAbonos;
    @Column(name = "cxpd_saldo_por_vencer")
    private BigDecimal cxpdSaldoPorVencer;
    @Column(name = "cxpd_cp")
    private String cxpdCp;
    @Column(name = "cxpd_prov_relacionado")
    private Boolean cxpdProvRelacionado;

    public CuentasPorPagarDetalladoTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCxpdFechaEmision() {
        return cxpdFechaEmision;
    }

    public void setCxpdFechaEmision(String cxpdFechaEmision) {
        this.cxpdFechaEmision = cxpdFechaEmision;
    }

    public String getCxpdFechaVencimiento() {
        return cxpdFechaVencimiento;
    }

    public void setCxpdFechaVencimiento(String cxpdFechaVencimiento) {
        this.cxpdFechaVencimiento = cxpdFechaVencimiento;
    }

    public String getCxpdMotivo() {
        return cxpdMotivo;
    }

    public void setCxpdMotivo(String cxpdMotivo) {
        this.cxpdMotivo = cxpdMotivo;
    }

    public String getCxpdNumero() {
        return cxpdNumero;
    }

    public void setCxpdNumero(String cxpdNumero) {
        this.cxpdNumero = cxpdNumero;
    }

    public String getCxpdPeriodo() {
        return cxpdPeriodo;
    }

    public void setCxpdPeriodo(String cxpdPeriodo) {
        this.cxpdPeriodo = cxpdPeriodo;
    }

    public BigDecimal getCxpdSaldo() {
        return cxpdSaldo;
    }

    public void setCxpdSaldo(BigDecimal cxpdSaldo) {
        this.cxpdSaldo = cxpdSaldo;
    }

    public String getCxpdUsuarioApruebaPago() {
        return cxpdUsuarioApruebaPago;
    }

    public void setCxpdUsuarioApruebaPago(String cxpdUsuarioApruebaPago) {
        this.cxpdUsuarioApruebaPago = cxpdUsuarioApruebaPago;
    }

    public String getCxpdProveedorCodigo() {
        return cxpdProveedorCodigo;
    }

    public void setCxpdProveedorCodigo(String cxpdProveedorCodigo) {
        this.cxpdProveedorCodigo = cxpdProveedorCodigo;
    }

    public String getCxpdProveedorId() {
        return cxpdProveedorId;
    }

    public void setCxpdProveedorId(String cxpdProveedorId) {
        this.cxpdProveedorId = cxpdProveedorId;
    }

    public String getCxpdProveedorRazonSocial() {
        return cxpdProveedorRazonSocial;
    }

    public void setCxpdProveedorRazonSocial(String cxpdProveedorRazonSocial) {
        this.cxpdProveedorRazonSocial = cxpdProveedorRazonSocial;
    }

    public String getCxpdDocumento() {
        return cxpdDocumento;
    }

    public void setCxpdDocumento(String cxpdDocumento) {
        this.cxpdDocumento = cxpdDocumento;
    }

    public String getCxpdObservaciones() {
        return cxpdObservaciones;
    }

    public void setCxpdObservaciones(String cxpdObservaciones) {
        this.cxpdObservaciones = cxpdObservaciones;
    }

    public String getCxpdDiasCredito() {
        return cxpdDiasCredito;
    }

    public void setCxpdDiasCredito(String cxpdDiasCredito) {
        this.cxpdDiasCredito = cxpdDiasCredito;
    }

    public String getCxpdDiasVencidos() {
        return cxpdDiasVencidos;
    }

    public void setCxpdDiasVencidos(String cxpdDiasVencidos) {
        this.cxpdDiasVencidos = cxpdDiasVencidos;
    }

    public String getCxpdZona() {
        return cxpdZona;
    }

    public void setCxpdZona(String cxpdZona) {
        this.cxpdZona = cxpdZona;
    }

    public BigDecimal getCxpdSaldoVencido() {
        return cxpdSaldoVencido;
    }

    public void setCxpdSaldoVencido(BigDecimal cxpdSaldoVencido) {
        this.cxpdSaldoVencido = cxpdSaldoVencido;
    }

    public BigDecimal getCxpdValorAprobado() {
        return cxpdValorAprobado;
    }

    public void setCxpdValorAprobado(BigDecimal cxpdValorAprobado) {
        this.cxpdValorAprobado = cxpdValorAprobado;
    }

    public BigDecimal getCxpdSaldoPorVencer() {
        return cxpdSaldoPorVencer;
    }

    public void setCxpdSaldoPorVencer(BigDecimal cxpdSaldoPorVencer) {
        this.cxpdSaldoPorVencer = cxpdSaldoPorVencer;
    }

    public BigDecimal getCxpdAbonos() {
        return cxpdAbonos;
    }

    public void setCxpdAbonos(BigDecimal cxpdAbonos) {
        this.cxpdAbonos = cxpdAbonos;
    }

    public String getCxpdCp() {
        return cxpdCp;
    }

    public void setCxpdCp(String cxpdCp) {
        this.cxpdCp = cxpdCp;
    }

    public Boolean getCxpdProvRelacionado() {
        return cxpdProvRelacionado;
    }

    public void setCxpdProvRelacionado(Boolean cxpdProvRelacionado) {
        this.cxpdProvRelacionado = cxpdProvRelacionado;
    }

}

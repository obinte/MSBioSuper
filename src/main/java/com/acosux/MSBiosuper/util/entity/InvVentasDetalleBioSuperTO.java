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

@Entity
@Table(name = "inv_ventas_detalle", schema = "inventario")
public class InvVentasDetalleBioSuperTO implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "det_secuencial")
    private Integer detSecuencial;
    @Column(name = "det_orden")
    private int detOrden;
    @Column(name = "det_pendiente")
    private boolean detPendiente;
    @Column(name = "det_cantidad")
    private BigDecimal detCantidad;
    @Column(name = "det_precio")
    private BigDecimal detPrecio;
    @Column(name = "det_parcial")
    private BigDecimal detParcial;
    @Column(name = "det_porcentaje_recargo")
    private BigDecimal detPorcentajeRecargo;
    @Column(name = "det_porcentaje_descuento")
    private BigDecimal detPorcentajeDescuento;
    @Column(name = "det_valor_promedio", insertable = false, updatable = false)
    private BigDecimal detValorPromedio;
    @Column(name = "det_valor_ultima_compra", insertable = false, updatable = false)
    private BigDecimal detValorUltimaCompra;
    @Column(name = "det_saldo", insertable = false, updatable = false)
    private BigDecimal detSaldo;
    @Column(name = "pro_nombre")
    private String proNombre;
    @Column(name = "pro_credito_tributario")
    private String proCreditoTributario;
    @Column(name = "sec_empresa")
    private String secEmpresa;
    @Column(name = "sec_codigo")
    private String secCodigo;
    @Column(name = "pis_empresa")
    private String pisEmpresa;
    @Column(name = "pis_sector")
    private String pisSector;
    @Column(name = "pis_numero")
    private String pisNumero;
    @Column(name = "pro_empresa")
    private String proEmpresa;
    @Column(name = "pro_codigo_principal")
    private String proCodigoPrincipal;
    @JoinColumns({
        @JoinColumn(name = "vta_empresa", referencedColumnName = "vta_empresa"),
        @JoinColumn(name = "vta_periodo", referencedColumnName = "vta_periodo"),
        @JoinColumn(name = "vta_motivo", referencedColumnName = "vta_motivo"),
        @JoinColumn(name = "vta_numero", referencedColumnName = "vta_numero")})
    @ManyToOne(optional = false)
    private InvVentasBioSuperTO invVentas;
    @Column(name = "bod_empresa")
    private String bodEmpresa;
    @Column(name = "bod_codigo")
    private String bodCodigo;
    //ice
    @Column(name = "det_monto_ice")
    private BigDecimal detMontoIce;
    @Column(name = "ice_tarifa_fija")
    private BigDecimal iceTarifaFija;
    @Column(name = "ice_porcentaje")
    private BigDecimal icePorcentaje;
    @Column(name = "ice_codigo")
    private String iceCodigo;
    @Column(name = "pro_complementario")
    private Integer proComplementario;
    @Column(name = "det_observaciones")
    private String detObservaciones;

    @Column(name = "det_empaque")
    private String detEmpaque;
    @Column(name = "det_empaque_cantidad")
    private BigDecimal detEmpaqueCantidad;
    @Column(name = "det_conversion_peso_neto")
    private BigDecimal detConversionPesoNeto;

    @Column(name = "det_lote")
    private String detLote;

    public InvVentasDetalleBioSuperTO() {
    }

    public InvVentasDetalleBioSuperTO(Integer detSecuencial) {
        this.detSecuencial = detSecuencial;
    }

    public InvVentasDetalleBioSuperTO(Integer detSecuencial, int detOrden, boolean detPendiente, String proNombre,
            String proCreditoTributario) {
        this.detSecuencial = detSecuencial;
        this.detOrden = detOrden;
        this.detPendiente = detPendiente;
        this.proNombre = proNombre;
        this.proCreditoTributario = proCreditoTributario;
    }

    public Integer getDetSecuencial() {
        return detSecuencial;
    }

    public void setDetSecuencial(Integer detSecuencial) {
        this.detSecuencial = detSecuencial;
    }

    public int getDetOrden() {
        return detOrden;
    }

    public void setDetOrden(int detOrden) {
        this.detOrden = detOrden;
    }

    public boolean getDetPendiente() {
        return detPendiente;
    }

    public void setDetPendiente(boolean detPendiente) {
        this.detPendiente = detPendiente;
    }

    public BigDecimal getDetCantidad() {
        return detCantidad;
    }

    public void setDetCantidad(BigDecimal detCantidad) {
        this.detCantidad = detCantidad;
    }

    public BigDecimal getDetPrecio() {
        return detPrecio;
    }

    public void setDetPrecio(BigDecimal detPrecio) {
        this.detPrecio = detPrecio;
    }

    public BigDecimal getDetPorcentajeRecargo() {
        return detPorcentajeRecargo;
    }

    public void setDetPorcentajeRecargo(BigDecimal detPorcentajeRecargo) {
        this.detPorcentajeRecargo = detPorcentajeRecargo;
    }

    public BigDecimal getDetPorcentajeDescuento() {
        return detPorcentajeDescuento;
    }

    public void setDetPorcentajeDescuento(BigDecimal detPorcentajeDescuento) {
        this.detPorcentajeDescuento = detPorcentajeDescuento;
    }

    public BigDecimal getDetValorPromedio() {
        return detValorPromedio;
    }

    public void setDetValorPromedio(BigDecimal detValorPromedio) {
        this.detValorPromedio = detValorPromedio;
    }

    public BigDecimal getDetValorUltimaCompra() {
        return detValorUltimaCompra;
    }

    public void setDetValorUltimaCompra(BigDecimal detValorUltimaCompra) {
        this.detValorUltimaCompra = detValorUltimaCompra;
    }

    public BigDecimal getDetSaldo() {
        return detSaldo;
    }

    public void setDetSaldo(BigDecimal detSaldo) {
        this.detSaldo = detSaldo;
    }

    public String getProNombre() {
        return proNombre;
    }

    public void setProNombre(String proNombre) {
        this.proNombre = proNombre;
    }

    public String getProCreditoTributario() {
        return proCreditoTributario;
    }

    public void setProCreditoTributario(String proCreditoTributario) {
        this.proCreditoTributario = proCreditoTributario;
    }

    public String getSecEmpresa() {
        return secEmpresa;
    }

    public void setSecEmpresa(String secEmpresa) {
        this.secEmpresa = secEmpresa;
    }

    public String getSecCodigo() {
        return secCodigo;
    }

    public void setSecCodigo(String secCodigo) {
        this.secCodigo = secCodigo;
    }

    public String getPisEmpresa() {
        return pisEmpresa;
    }

    public void setPisEmpresa(String pisEmpresa) {
        this.pisEmpresa = pisEmpresa;
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

    public String getProEmpresa() {
        return proEmpresa;
    }

    public void setProEmpresa(String proEmpresa) {
        this.proEmpresa = proEmpresa;
    }

    public String getProCodigoPrincipal() {
        return proCodigoPrincipal;
    }

    public void setProCodigoPrincipal(String proCodigoPrincipal) {
        this.proCodigoPrincipal = proCodigoPrincipal;
    }

    public InvVentasBioSuperTO getInvVentas() {
        return invVentas;
    }

    public void setInvVentas(InvVentasBioSuperTO invVentas) {
        this.invVentas = new InvVentasBioSuperTO(invVentas.getInvVentasPK());
    }

    public String getBodEmpresa() {
        return bodEmpresa;
    }

    public void setBodEmpresa(String bodEmpresa) {
        this.bodEmpresa = bodEmpresa;
    }

    public String getBodCodigo() {
        return bodCodigo;
    }

    public void setBodCodigo(String bodCodigo) {
        this.bodCodigo = bodCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detSecuencial != null ? detSecuencial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof InvVentasDetalleBioSuperTO)) {
            return false;
        }
        InvVentasDetalleBioSuperTO other = (InvVentasDetalleBioSuperTO) object;
        if ((this.detSecuencial == null && other.detSecuencial != null)
                || (this.detSecuencial != null && !this.detSecuencial.equals(other.detSecuencial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "inventario.entity.InvVentasDetalle[ detSecuencial=" + detSecuencial + " ]";
    }

    public BigDecimal getDetParcial() {
        return detParcial;
    }

    public void setDetParcial(BigDecimal detParcial) {
        this.detParcial = detParcial;
    }

    public BigDecimal getDetMontoIce() {
        return detMontoIce;
    }

    public void setDetMontoIce(BigDecimal detMontoIce) {
        this.detMontoIce = detMontoIce;
    }

    public BigDecimal getIceTarifaFija() {
        return iceTarifaFija;
    }

    public void setIceTarifaFija(BigDecimal iceTarifaFija) {
        this.iceTarifaFija = iceTarifaFija;
    }

    public BigDecimal getIcePorcentaje() {
        return icePorcentaje;
    }

    public void setIcePorcentaje(BigDecimal icePorcentaje) {
        this.icePorcentaje = icePorcentaje;
    }

    public String getIceCodigo() {
        return iceCodigo;
    }

    public void setIceCodigo(String iceCodigo) {
        this.iceCodigo = iceCodigo;
    }

    public Integer getProComplementario() {
        return proComplementario;
    }

    public void setProComplementario(Integer proComplementario) {
        this.proComplementario = proComplementario;
    }

    public String getDetObservaciones() {
        return detObservaciones;
    }

    public void setDetObservaciones(String detObservaciones) {
        this.detObservaciones = detObservaciones;
    }

    public String getDetEmpaque() {
        return detEmpaque;
    }

    public void setDetEmpaque(String detEmpaque) {
        this.detEmpaque = detEmpaque;
    }

    public BigDecimal getDetEmpaqueCantidad() {
        return detEmpaqueCantidad;
    }

    public void setDetEmpaqueCantidad(BigDecimal detEmpaqueCantidad) {
        this.detEmpaqueCantidad = detEmpaqueCantidad;
    }

    public BigDecimal getDetConversionPesoNeto() {
        return detConversionPesoNeto;
    }

    public void setDetConversionPesoNeto(BigDecimal detConversionPesoNeto) {
        this.detConversionPesoNeto = detConversionPesoNeto;
    }

    public String getDetLote() {
        return detLote;
    }

    public void setDetLote(String detLote) {
        this.detLote = detLote;
    }

}

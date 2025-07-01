package com.acosux.MSBiosuper.util.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PrdConsumosDiarioMSTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "consumo_empresa")
    private String consumoEmpresa;
    @Column(name = "consumo_sector")
    private String consumoSector;
    @Column(name = "consumo_piscina")
    private String consumoPiscina;
    @Column(name = "consumo_fecha")
    private String consumoFecha;
    @Column(name = "consumo_producto")
    private String consumoProducto;
    @Column(name = "consumo_codigo")
    private String consumoCodigo;
    @Column(name = "consumo_medida")
    private String consumoMedida;
    @Column(name = "consumo_total")
    private BigDecimal consumoTotal;
    @Column(name = "consumo_costo_total")
    private BigDecimal consumoCostoTotal;
    @Column(name = "consumo_corrida")
    private String consumoCorrida;

    public PrdConsumosDiarioMSTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConsumoEmpresa() {
        return consumoEmpresa;
    }

    public void setConsumoEmpresa(String consumoEmpresa) {
        this.consumoEmpresa = consumoEmpresa;
    }

    public String getConsumoSector() {
        return consumoSector;
    }

    public void setConsumoSector(String consumoSector) {
        this.consumoSector = consumoSector;
    }

    public String getConsumoFecha() {
        return consumoFecha;
    }

    public void setConsumoFecha(String consumoFecha) {
        this.consumoFecha = consumoFecha;
    }

    public String getConsumoProducto() {
        return consumoProducto;
    }

    public void setConsumoProducto(String consumoProducto) {
        this.consumoProducto = consumoProducto;
    }

    public String getConsumoCodigo() {
        return consumoCodigo;
    }

    public void setConsumoCodigo(String consumoCodigo) {
        this.consumoCodigo = consumoCodigo;
    }

    public String getConsumoMedida() {
        return consumoMedida;
    }

    public void setConsumoMedida(String consumoMedida) {
        this.consumoMedida = consumoMedida;
    }

    public BigDecimal getConsumoTotal() {
        return consumoTotal;
    }

    public void setConsumoTotal(BigDecimal consumoTotal) {
        this.consumoTotal = consumoTotal;
    }

    public String getConsumoPiscina() {
        return consumoPiscina;
    }

    public void setConsumoPiscina(String consumoPiscina) {
        this.consumoPiscina = consumoPiscina;
    }

    public BigDecimal getConsumoCostoTotal() {
        return consumoCostoTotal;
    }

    public void setConsumoCostoTotal(BigDecimal consumoCostoTotal) {
        this.consumoCostoTotal = consumoCostoTotal;
    }

    public String getConsumoCorrida() {
        return consumoCorrida;
    }

    public void setConsumoCorrida(String consumoCorrida) {
        this.consumoCorrida = consumoCorrida;
    }

}

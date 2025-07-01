package com.acosux.MSBiosuper.util.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Andres Guachisaca
 */
@Entity
public class PrdListaCostosDetalleCorridaTO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "costo_producto")
	private String costoProducto;

	@Column(name = "costo_codigo")
	private String costoCodigo;

	@Column(name = "costo_medida")
	private String costoMedida;

	@Column(name = "costo_cantidad")
	private BigDecimal costoCantidad;

	@Column(name = "costo_total")
	private BigDecimal costoTotal;

	@Column(name = "costo_porcentaje")
	private BigDecimal costoPorcentaje;
        
//        @Transient
//        private InvProducto invProducto;

	public PrdListaCostosDetalleCorridaTO() {
	}

	public PrdListaCostosDetalleCorridaTO(String costoProducto, String costoCodigo, String costoMedida,
			BigDecimal costoCantidad, BigDecimal costoTotal, BigDecimal costoPorcentaje, Integer id) {
		this.costoProducto = costoProducto;
		this.costoCodigo = costoCodigo;
		this.costoMedida = costoMedida;
		this.costoCantidad = costoCantidad;
		this.costoTotal = costoTotal;
		this.costoPorcentaje = costoPorcentaje;
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getCostoCantidad() {
		return costoCantidad;
	}

	public void setCostoCantidad(BigDecimal costoCantidad) {
		this.costoCantidad = costoCantidad;
	}

	public String getCostoCodigo() {
		return costoCodigo;
	}

	public void setCostoCodigo(String costoCodigo) {
		this.costoCodigo = costoCodigo;
	}

	public String getCostoMedida() {
		return costoMedida;
	}

	public void setCostoMedida(String costoMedida) {
		this.costoMedida = costoMedida;
	}

	public BigDecimal getCostoPorcentaje() {
		return costoPorcentaje;
	}

	public void setCostoPorcentaje(BigDecimal costoPorcentaje) {
		this.costoPorcentaje = costoPorcentaje;
	}

	public String getCostoProducto() {
		return costoProducto;
	}

	public void setCostoProducto(String costoProducto) {
		this.costoProducto = costoProducto;
	}

	public BigDecimal getCostoTotal() {
		return costoTotal;
	}

	public void setCostoTotal(BigDecimal costoTotal) {
		this.costoTotal = costoTotal;
	}

}

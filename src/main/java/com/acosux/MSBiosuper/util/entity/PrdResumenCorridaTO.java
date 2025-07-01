package com.acosux.MSBiosuper.util.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class PrdResumenCorridaTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "rc_piscina")
    private String pisNumero;
    @Column(name = "rc_empresa")
    private String empCodigo;
    @Column(name = "rc_sector")
    private String secCodigo;
    @Column(name = "rc_hectareaje")
    private BigDecimal rcHectareaje;
    @Column(name = "rc_corrida")
    private String rcCorridaNumero;
    @Column(name = "rc_fecha_desde")
    private String rcFechaDesde;
    @Column(name = "rc_fecha_siembra")
    private String rcFechaSiembra;
    @Column(name = "rc_fecha_pesca")
    private String rcFechaPesca;
    @Column(name = "rc_fecha_hasta")
    private String rcFechaHasta;
    @Column(name = "rc_edad")
    private BigDecimal rcEdad;
    @Column(name = "rc_edad_inicial")
    private BigDecimal rcEdadInicial;
    @Column(name = "rc_dias_secos")
    private BigDecimal rcDiasSecos;
    @Column(name = "rc_numero_larvas")
    private BigDecimal rcNumeroLarvas;
    @Column(name = "rc_codigo_larva_lote")
    private String rcCodigoLarvaLote;
    @Column(name = "rc_densidad")
    private BigDecimal rcDensidad;
    @Column(name = "rc_laboratorio")
    private String rcLaboratorio;
    @Column(name = "rc_nauplio")
    private String rcNauplio;
    @Column(name = "rc_balanceado")
    private BigDecimal rcLibrasBalanceados;
    @Column(name = "rc_balanceado_transferido")
    private BigDecimal rcBalanceadoTransferido;
    @Column(name = "rc_costo_balanceado")
    private BigDecimal rcCostoBalanceado;
    @Column(name = "rc_costo_balanceado_transferido")
    private BigDecimal rcCostoBalanceadoTransferido;
    @Column(name = "rc_biomasa")
    private BigDecimal rcBiomasa;
    @Column(name = "rc_biomasa_real")
    private BigDecimal rcBiomasaReal;
    @Column(name = "rc_biomasa_proyectada")
    private BigDecimal rcBiomasaProyectada;
    @Column(name = "rc_venta_proyectada")
    private BigDecimal rcVentaProyectada;
    @Column(name = "rc_conversion")
    private BigDecimal rcConversion;
    @Column(name = "rc_talla_grande")
    private BigDecimal rcTGrande;
    @Column(name = "rc_talla_mediano")
    private BigDecimal rcTMediano;
    @Column(name = "rc_talla_pequeno")
    private BigDecimal rcTPequeno;
    @Column(name = "rc_talla_promedio")
    private BigDecimal rcTPromedio;

    @Column(name = "rc_crecimiento_dia")
    private BigDecimal rcCrecimientoDia;

    @Column(name = "rc_peso_ideal")
    private BigDecimal rcPesoIdeal;
    @Column(name = "rc_sobrevivencia")
    private String rcSobrevivencia;

    @Column(name = "rc_costo_total")
    private BigDecimal rcCostoTotal;
    @Column(name = "rc_costo_directo")
    private BigDecimal rcCostoDirecto;
    @Column(name = "rc_costo_indirecto")
    private BigDecimal rcCostoIndirecto;
    @Column(name = "rc_costo_transferencia")
    private BigDecimal rcCostoTransferencia;
    @Column(name = "rc_costo_directo_transferido")
    private BigDecimal rcCostoDirectoTransferido;
    @Column(name = "rc_costo_indirecto_transferido")
    private BigDecimal rcCostoIndirectoTransferido;

    @Column(name = "rc_valor_venta")
    private BigDecimal rcValorVenta;
    @Column(name = "rc_valor_venta_transferida")
    private BigDecimal rcValorVentaTransferida;
    @Column(name = "rc_resultado")
    private BigDecimal rcResultado;
    @Column(name = "rc_rendimiento")
    private BigDecimal rcRendimiento;
    @Column(name = "rc_costo_hectarea")
    private BigDecimal rcCostoHectarea;
    @Column(name = "rc_venta_hectarea")
    private BigDecimal rcVentaHectarea;
    @Column(name = "rc_resultado_hectarea")
    private BigDecimal rcResultadoHectarea;
    @Column(name = "rc_resultado_hectarea_dia")
    private BigDecimal rcResultadoHectareaDia;
    @Column(name = "rc_costo_libra")
    private BigDecimal rcCostoLibra;
    @Column(name = "rc_venta_libra")
    private BigDecimal rcVentaLibra;
    @Column(name = "rc_resultado_libra")
    private BigDecimal rcResultadoLibra;

    @Column(name = "rc_costo_total_hectarea_dia")
    private BigDecimal rcCostoTotalHectareaDia;
    @Column(name = "rc_costo_directo_hectarea_dia")
    private BigDecimal rcCostoDirectoHectareaDia;
    @Column(name = "rc_costo_indirecto_hectarea_dia")
    private BigDecimal rcCostoindirectoHectareaDia;
    @Column(name = "rc_peso_inicial")
    private BigDecimal rcPesoInicial;
    @Column(name = "rc_costo_transferencia_hectarea_dia")
    private BigDecimal rcCostoTransferenciaHectareaDia;
    @Column(name = "rc_costo_directo_transferido_hectarea_dia")
    private BigDecimal rcCostoDirectoTransferidoHectareaDia;
    @Column(name = "rc_costo_indirecto_transferido_hectarea_dia")
    private BigDecimal rcCostoIndirectoTransferidoHectareaDia;
    @Transient
    private BigDecimal rcOtrosCostos;
    @Transient
    private BigDecimal rcOtrosHectareaDias;
    @Column(name= "rc_animales_metro_cuadrado")
    private BigDecimal rcAnimales;

    @Column(name = "rc_corrida_origen")
    private String rcCorridaOrigen;
    @Column(name = "rc_corrida_destino")
    private String rcCorridaDestino;
    @Column(name = "rc_costo_millar")
    private BigDecimal rcCostoMillar;    
    @Column(name = "rc_biomasa_empacadora")
    private BigDecimal rcBiomasaEmpacadora;    
    @Column(name = "rc_peso_cosecha")
    private BigDecimal rcPesoCosecha;    
    @Column(name = "rc_tipo_liquidacion")
    private String rcTipoLiquidacion;
    @Column(name = "rc_biomasa_transferida")
    private BigDecimal rcBiomasaTransferida;
    @Column(name = "rc_libras_pescadas")
    private BigDecimal rcLibrasPescadas;
    @Column(name = "rc_peso_promedio_cosechado")
    private BigDecimal rcPesoPromedioCosechado;
    @Column(name = "rc_empacadora_codigo")
    private String rcEmpacadoraCodigo;
          
    public PrdResumenCorridaTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPisNumero() {
        return pisNumero;
    }

    public void setPisNumero(String pisNumero) {
        this.pisNumero = pisNumero;
    }

    public String getEmpCodigo() {
        return empCodigo;
    }

    public void setEmpCodigo(String empCodigo) {
        this.empCodigo = empCodigo;
    }

    public String getSecCodigo() {
        return secCodigo;
    }

    public void setSecCodigo(String secCodigo) {
        this.secCodigo = secCodigo;
    }

    public BigDecimal getRcHectareaje() {
        return rcHectareaje;
    }

    public void setRcHectareaje(BigDecimal rcHectareaje) {
        this.rcHectareaje = rcHectareaje;
    }

    public String getRcCorridaNumero() {
        return rcCorridaNumero;
    }

    public void setRcCorridaNumero(String rcCorridaNumero) {
        this.rcCorridaNumero = rcCorridaNumero;
    }

    public String getRcFechaDesde() {
        return rcFechaDesde;
    }

    public void setRcFechaDesde(String rcFechaDesde) {
        this.rcFechaDesde = rcFechaDesde;
    }

    public String getRcFechaSiembra() {
        return rcFechaSiembra;
    }

    public void setRcFechaSiembra(String rcFechaSiembra) {
        this.rcFechaSiembra = rcFechaSiembra;
    }

    public String getRcFechaPesca() {
        return rcFechaPesca;
    }

    public void setRcFechaPesca(String rcFechaPesca) {
        this.rcFechaPesca = rcFechaPesca;
    }

    public String getRcFechaHasta() {
        return rcFechaHasta;
    }

    public void setRcFechaHasta(String rcFechaHasta) {
        this.rcFechaHasta = rcFechaHasta;
    }

    public BigDecimal getRcEdad() {
        return rcEdad;
    }

    public void setRcEdad(BigDecimal rcEdad) {
        this.rcEdad = rcEdad;
    }

    public BigDecimal getRcEdadInicial() {
        return rcEdadInicial;
    }

    public void setRcEdadInicial(BigDecimal rcEdadInicial) {
        this.rcEdadInicial = rcEdadInicial;
    }

    public BigDecimal getRcDiasSecos() {
        return rcDiasSecos;
    }

    public void setRcDiasSecos(BigDecimal rcDiasSecos) {
        this.rcDiasSecos = rcDiasSecos;
    }

    public BigDecimal getRcNumeroLarvas() {
        return rcNumeroLarvas;
    }

    public void setRcNumeroLarvas(BigDecimal rcNumeroLarvas) {
        this.rcNumeroLarvas = rcNumeroLarvas;
    }

    public String getRcCodigoLarvaLote() {
        return rcCodigoLarvaLote;
    }

    public void setRcCodigoLarvaLote(String rcCodigoLarvaLote) {
        this.rcCodigoLarvaLote = rcCodigoLarvaLote;
    }
    
    public BigDecimal getRcDensidad() {
        return rcDensidad;
    }

    public void setRcDensidad(BigDecimal rcDensidad) {
        this.rcDensidad = rcDensidad;
    }

    public String getRcLaboratorio() {
        return rcLaboratorio;
    }

    public void setRcLaboratorio(String rcLaboratorio) {
        this.rcLaboratorio = rcLaboratorio;
    }

    public String getRcNauplio() {
        return rcNauplio;
    }

    public void setRcNauplio(String rcNauplio) {
        this.rcNauplio = rcNauplio;
    }

    public BigDecimal getRcLibrasBalanceados() {
        return rcLibrasBalanceados;
    }

    public void setRcLibrasBalanceados(BigDecimal rcLibrasBalanceados) {
        this.rcLibrasBalanceados = rcLibrasBalanceados;
    }

    public BigDecimal getRcBiomasa() {
        return rcBiomasa;
    }

    public void setRcBiomasa(BigDecimal rcBiomasa) {
        this.rcBiomasa = rcBiomasa;
    }

    public BigDecimal getRcBiomasaReal() {
        return rcBiomasaReal;
    }

    public void setRcBiomasaReal(BigDecimal rcBiomasaReal) {
        this.rcBiomasaReal = rcBiomasaReal;
    }

    public BigDecimal getRcConversion() {
        return rcConversion;
    }

    public void setRcConversion(BigDecimal rcConversion) {
        this.rcConversion = rcConversion;
    }

    public BigDecimal getRcTGrande() {
        return rcTGrande;
    }

    public void setRcTGrande(BigDecimal rcTGrande) {
        this.rcTGrande = rcTGrande;
    }

    public BigDecimal getRcTMediano() {
        return rcTMediano;
    }

    public void setRcTMediano(BigDecimal rcTMediano) {
        this.rcTMediano = rcTMediano;
    }

    public BigDecimal getRcTPequeno() {
        return rcTPequeno;
    }

    public void setRcTPequeno(BigDecimal rcTPequeno) {
        this.rcTPequeno = rcTPequeno;
    }

    public BigDecimal getRcTPromedio() {
        return rcTPromedio;
    }

    public void setRcTPromedio(BigDecimal rcTPromedio) {
        this.rcTPromedio = rcTPromedio;
    }

    public BigDecimal getRcCrecimientoDia() {
        return rcCrecimientoDia;
    }

    public void setRcCrecimientoDia(BigDecimal rcCrecimientoDia) {
        this.rcCrecimientoDia = rcCrecimientoDia;
    }

    public BigDecimal getRcPesoIdeal() {
        return rcPesoIdeal;
    }

    public void setRcPesoIdeal(BigDecimal rcPesoIdeal) {
        this.rcPesoIdeal = rcPesoIdeal;
    }

    public String getRcSobrevivencia() {
        return rcSobrevivencia;
    }

    public void setRcSobrevivencia(String rcSobrevivencia) {
        this.rcSobrevivencia = rcSobrevivencia;
    }

    public BigDecimal getRcCostoTotal() {
        return rcCostoTotal;
    }

    public void setRcCostoTotal(BigDecimal rcCostoTotal) {
        this.rcCostoTotal = rcCostoTotal;
    }

    public BigDecimal getRcCostoDirecto() {
        return rcCostoDirecto;
    }

    public void setRcCostoDirecto(BigDecimal rcCostoDirecto) {
        this.rcCostoDirecto = rcCostoDirecto;
    }

    public BigDecimal getRcCostoIndirecto() {
        return rcCostoIndirecto;
    }

    public void setRcCostoIndirecto(BigDecimal rcCostoIndirecto) {
        this.rcCostoIndirecto = rcCostoIndirecto;
    }

    public BigDecimal getRcCostoTransferencia() {
        return rcCostoTransferencia;
    }

    public void setRcCostoTransferencia(BigDecimal rcCostoTransferencia) {
        this.rcCostoTransferencia = rcCostoTransferencia;
    }

    public BigDecimal getRcValorVenta() {
        return rcValorVenta;
    }

    public void setRcValorVenta(BigDecimal rcValorVenta) {
        this.rcValorVenta = rcValorVenta;
    }

    public BigDecimal getRcResultado() {
        return rcResultado;
    }

    public void setRcResultado(BigDecimal rcResultado) {
        this.rcResultado = rcResultado;
    }

    public BigDecimal getRcResultadoHectareaDia() {
        return rcResultadoHectareaDia;
    }

    public void setRcResultadoHectareaDia(BigDecimal rcResultadoHectareaDia) {
        this.rcResultadoHectareaDia = rcResultadoHectareaDia;
    }

    public BigDecimal getRcRendimiento() {
        return rcRendimiento;
    }

    public void setRcRendimiento(BigDecimal rcRendimiento) {
        this.rcRendimiento = rcRendimiento;
    }

    public BigDecimal getRcCostoHectarea() {
        return rcCostoHectarea;
    }

    public void setRcCostoHectarea(BigDecimal rcCostoHectarea) {
        this.rcCostoHectarea = rcCostoHectarea;
    }

    public BigDecimal getRcVentaHectarea() {
        return rcVentaHectarea;
    }

    public void setRcVentaHectarea(BigDecimal rcVentaHectarea) {
        this.rcVentaHectarea = rcVentaHectarea;
    }

    public BigDecimal getRcResultadoHectarea() {
        return rcResultadoHectarea;
    }

    public void setRcResultadoHectarea(BigDecimal rcResultadoHectarea) {
        this.rcResultadoHectarea = rcResultadoHectarea;
    }

    public BigDecimal getRcCostoLibra() {
        return rcCostoLibra;
    }

    public void setRcCostoLibra(BigDecimal rcCostoLibra) {
        this.rcCostoLibra = rcCostoLibra;
    }

    public BigDecimal getRcVentaLibra() {
        return rcVentaLibra;
    }

    public void setRcVentaLibra(BigDecimal rcVentaLibra) {
        this.rcVentaLibra = rcVentaLibra;
    }

    public BigDecimal getRcResultadoLibra() {
        return rcResultadoLibra;
    }

    public void setRcResultadoLibra(BigDecimal rcResultadoLibra) {
        this.rcResultadoLibra = rcResultadoLibra;
    }

    public BigDecimal getRcCostoTotalHectareaDia() {
        return rcCostoTotalHectareaDia;
    }

    public void setRcCostoTotalHectareaDia(BigDecimal rcCostoTotalHectareaDia) {
        this.rcCostoTotalHectareaDia = rcCostoTotalHectareaDia;
    }

    public BigDecimal getRcCostoDirectoHectareaDia() {
        return rcCostoDirectoHectareaDia;
    }

    public void setRcCostoDirectoHectareaDia(BigDecimal rcCostoDirectoHectareaDia) {
        this.rcCostoDirectoHectareaDia = rcCostoDirectoHectareaDia;
    }

    public BigDecimal getRcCostoindirectoHectareaDia() {
        return rcCostoindirectoHectareaDia;
    }

    public void setRcCostoindirectoHectareaDia(BigDecimal rcCostoindirectoHectareaDia) {
        this.rcCostoindirectoHectareaDia = rcCostoindirectoHectareaDia;
    }

    public BigDecimal getRcCostoTransferenciaHectareaDia() {
        return rcCostoTransferenciaHectareaDia;
    }

    public void setRcCostoTransferenciaHectareaDia(BigDecimal rcCostoTransferenciaHectareaDia) {
        this.rcCostoTransferenciaHectareaDia = rcCostoTransferenciaHectareaDia;
    }

    public BigDecimal getRcOtrosCostos() {
        return rcOtrosCostos;
    }

    public void setRcOtrosCostos(BigDecimal rcOtrosCostos) {
        this.rcOtrosCostos = rcOtrosCostos;
    }

    public BigDecimal getRcOtrosHectareaDias() {
        return rcOtrosHectareaDias;
    }

    public void setRcOtrosHectareaDias(BigDecimal rcOtrosHectareaDias) {
        this.rcOtrosHectareaDias = rcOtrosHectareaDias;
    }

    public BigDecimal getRcBalanceadoTransferido() {
        return rcBalanceadoTransferido;
    }

    public void setRcBalanceadoTransferido(BigDecimal rcBalanceadoTransferido) {
        this.rcBalanceadoTransferido = rcBalanceadoTransferido;
    }

    public BigDecimal getRcCostoBalanceado() {
        return rcCostoBalanceado;
    }

    public void setRcCostoBalanceado(BigDecimal rcCostoBalanceado) {
        this.rcCostoBalanceado = rcCostoBalanceado;
    }

    public BigDecimal getRcCostoBalanceadoTransferido() {
        return rcCostoBalanceadoTransferido;
    }

    public void setRcCostoBalanceadoTransferido(BigDecimal rcCostoBalanceadoTransferido) {
        this.rcCostoBalanceadoTransferido = rcCostoBalanceadoTransferido;
    }

    public BigDecimal getRcVentaProyectada() {
        return rcVentaProyectada;
    }

    public void setRcVentaProyectada(BigDecimal rcVentaProyectada) {
        this.rcVentaProyectada = rcVentaProyectada;
    }

    public BigDecimal getRcPesoInicial() {
        return rcPesoInicial;
    }

    public void setRcPesoInicial(BigDecimal rcPesoInicial) {
        this.rcPesoInicial = rcPesoInicial;
    }

    public BigDecimal getRcAnimales() {
        return rcAnimales;
    }

    public void setRcAnimales(BigDecimal rcAnimales) {
        this.rcAnimales = rcAnimales;
    }

    public BigDecimal getRcCostoDirectoTransferido() {
        return rcCostoDirectoTransferido;
    }

    public void setRcCostoDirectoTransferido(BigDecimal rcCostoDirectoTransferido) {
        this.rcCostoDirectoTransferido = rcCostoDirectoTransferido;
    }

    public BigDecimal getRcCostoIndirectoTransferido() {
        return rcCostoIndirectoTransferido;
    }

    public void setRcCostoIndirectoTransferido(BigDecimal rcCostoIndirectoTransferido) {
        this.rcCostoIndirectoTransferido = rcCostoIndirectoTransferido;
    }

    public BigDecimal getRcCostoDirectoTransferidoHectareaDia() {
        return rcCostoDirectoTransferidoHectareaDia;
    }

    public void setRcCostoDirectoTransferidoHectareaDia(BigDecimal rcCostoDirectoTransferidoHectareaDia) {
        this.rcCostoDirectoTransferidoHectareaDia = rcCostoDirectoTransferidoHectareaDia;
    }

    public BigDecimal getRcCostoIndirectoTransferidoHectareaDia() {
        return rcCostoIndirectoTransferidoHectareaDia;
    }

    public void setRcCostoIndirectoTransferidoHectareaDia(BigDecimal rcCostoIndirectoTransferidoHectareaDia) {
        this.rcCostoIndirectoTransferidoHectareaDia = rcCostoIndirectoTransferidoHectareaDia;
    }

    public String getRcCorridaOrigen() {
        return rcCorridaOrigen;
    }

    public void setRcCorridaOrigen(String rcCorridaOrigen) {
        this.rcCorridaOrigen = rcCorridaOrigen;
    }

    public String getRcCorridaDestino() {
        return rcCorridaDestino;
    }

    public void setRcCorridaDestino(String rcCorridaDestino) {
        this.rcCorridaDestino = rcCorridaDestino;
    }

    public BigDecimal getRcCostoMillar() {
        return rcCostoMillar;
    }

    public void setRcCostoMillar(BigDecimal rcCostoMillar) {
        this.rcCostoMillar = rcCostoMillar;
    }  

    public BigDecimal getRcBiomasaProyectada() {
        return rcBiomasaProyectada;
    }

    public void setRcBiomasaProyectada(BigDecimal rcBiomasaProyectada) {
        this.rcBiomasaProyectada = rcBiomasaProyectada;
    }

    public BigDecimal getRcBiomasaEmpacadora() {
        return rcBiomasaEmpacadora;
    }

    public void setRcBiomasaEmpacadora(BigDecimal rcBiomasaEmpacadora) {
        this.rcBiomasaEmpacadora = rcBiomasaEmpacadora;
    }

    public BigDecimal getRcPesoCosecha() {
        return rcPesoCosecha;
    }

    public void setRcPesoCosecha(BigDecimal rcPesoCosecha) {
        this.rcPesoCosecha = rcPesoCosecha;
    }

    public String getRcTipoLiquidacion() {
        return rcTipoLiquidacion;
    }

    public void setRcTipoLiquidacion(String rcTipoLiquidacion) {
        this.rcTipoLiquidacion = rcTipoLiquidacion;
    }

    public BigDecimal getRcValorVentaTransferida() {
        return rcValorVentaTransferida;
    }

    public void setRcValorVentaTransferida(BigDecimal rcValorVentaTransferida) {
        this.rcValorVentaTransferida = rcValorVentaTransferida;
    }

    public BigDecimal getRcBiomasaTransferida() {
        return rcBiomasaTransferida;
    }

    public void setRcBiomasaTransferida(BigDecimal rcBiomasaTransferida) {
        this.rcBiomasaTransferida = rcBiomasaTransferida;
    }

    public BigDecimal getRcLibrasPescadas() {
        return rcLibrasPescadas;
    }

    public void setRcLibrasPescadas(BigDecimal rcLibrasPescadas) {
        this.rcLibrasPescadas = rcLibrasPescadas;
    }

    public BigDecimal getRcPesoPromedioCosechado() {
        return rcPesoPromedioCosechado;
    }

    public void setRcPesoPromedioCosechado(BigDecimal rcPesoPromedioCosechado) {
        this.rcPesoPromedioCosechado = rcPesoPromedioCosechado;
    }

    public String getRcEmpacadoraCodigo() {
        return rcEmpacadoraCodigo;
    }

    public void setRcEmpacadoraCodigo(String rcEmpacadoraCodigo) {
        this.rcEmpacadoraCodigo = rcEmpacadoraCodigo;
    }
    
}

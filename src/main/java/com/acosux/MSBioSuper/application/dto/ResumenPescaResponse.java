package com.acosux.MSBioSuper.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * DTO de respuesta para el resumen de pesca.
 * Utiliza Java 21 Record para inmutabilidad y concisión.
 */
@Schema(description = "Respuesta con información detallada del resumen de pesca")
public record ResumenPescaResponse(
    @Schema(description = "Identificador único del registro", example = "1")
    Integer id,
    
    @Schema(description = "Número de piscina", example = "P-001")
    String pisNumero,
    
    @Schema(description = "Código de empresa", example = "01")
    String empCodigo,
    
    @Schema(description = "Código de sector", example = "SECTOR-A")
    String secCodigo,
    
    @Schema(description = "Hectareaje de la piscina", example = "2.5")
    BigDecimal rcHectareaje,
    
    @Schema(description = "Número de corrida", example = "C-2024-01")
    String rcCorridaNumero,
    
    @Schema(description = "Fecha de inicio de la corrida", example = "2024-01-15")
    String rcFechaDesde,
    
    @Schema(description = "Fecha de siembra", example = "2024-01-20")
    String rcFechaSiembra,
    
    @Schema(description = "Fecha de pesca", example = "2024-05-15")
    String rcFechaPesca,
    
    @Schema(description = "Fecha de fin de la corrida", example = "2024-05-20")
    String rcFechaHasta,
    
    @Schema(description = "Edad del cultivo en días", example = "115")
    BigDecimal rcEdad,
    
    @Schema(description = "Edad inicial del cultivo", example = "0")
    BigDecimal rcEdadInicial,
    
    @Schema(description = "Días secos (sin agua)", example = "5")
    BigDecimal rcDiasSecos,
    
    @Schema(description = "Número de larvas sembradas", example = "250000")
    BigDecimal rcNumeroLarvas,
    
    @Schema(description = "Código del lote de larvas", example = "LOTE-2024-001")
    String rcCodigoLarvaLote,
    
    @Schema(description = "Densidad de siembra (larvas/m²)", example = "100")
    BigDecimal rcDensidad,
    
    @Schema(description = "Nombre del laboratorio proveedor", example = "Lab Acuícola S.A.")
    String rcLaboratorio,
    
    @Schema(description = "Tipo de nauplio utilizado", example = "Nauplio V")
    String rcNauplio,
    
    @Schema(description = "Libras de balanceado consumidas", example = "15000.50")
    BigDecimal rcLibrasBalanceados,
    
    @Schema(description = "Balanceado transferido de otras piscinas", example = "500.00")
    BigDecimal rcBalanceadoTransferido,
    
    @Schema(description = "Costo total del balanceado", example = "18000.00")
    BigDecimal rcCostoBalanceado,
    
    @Schema(description = "Costo del balanceado transferido", example = "600.00")
    BigDecimal rcCostoBalanceadoTransferido,
    
    @Schema(description = "Biomasa total en libras", example = "12000.00")
    BigDecimal rcBiomasa,
    
    @Schema(description = "Biomasa real cosechada", example = "11800.00")
    BigDecimal rcBiomasaReal,
    
    @Schema(description = "Biomasa proyectada", example = "12500.00")
    BigDecimal rcBiomasaProyectada,
    
    @Schema(description = "Venta proyectada en dólares", example = "35000.00")
    BigDecimal rcVentaProyectada,
    
    @Schema(description = "Factor de conversión alimenticia", example = "1.25")
    BigDecimal rcConversion,
    
    @Schema(description = "Tamaño grande (gramos)", example = "25.5")
    BigDecimal rcTGrande,
    
    @Schema(description = "Tamaño mediano (gramos)", example = "20.0")
    BigDecimal rcTMediano,
    
    @Schema(description = "Tamaño pequeño (gramos)", example = "15.5")
    BigDecimal rcTPequeno,
    
    @Schema(description = "Tamaño promedio (gramos)", example = "21.0")
    BigDecimal rcTPromedio,
    
    @Schema(description = "Crecimiento diario (gramos/día)", example = "0.18")
    BigDecimal rcCrecimientoDia,
    
    @Schema(description = "Peso ideal proyectado", example = "22.0")
    BigDecimal rcPesoIdeal,
    
    @Schema(description = "Porcentaje de sobrevivencia", example = "85%")
    String rcSobrevivencia,
    
    @Schema(description = "Costo total de producción", example = "28000.00")
    BigDecimal rcCostoTotal,
    
    @Schema(description = "Costo directo", example = "22000.00")
    BigDecimal rcCostoDirecto,
    
    @Schema(description = "Costo indirecto", example = "6000.00")
    BigDecimal rcCostoIndirecto,
    
    @Schema(description = "Costo de transferencia", example = "800.00")
    BigDecimal rcCostoTransferencia,
    
    @Schema(description = "Costo directo transferido", example = "500.00")
    BigDecimal rcCostoDirectoTransferido,
    
    @Schema(description = "Costo indirecto transferido", example = "300.00")
    BigDecimal rcCostoIndirectoTransferido,
    
    @Schema(description = "Valor total de venta", example = "35400.00")
    BigDecimal rcValorVenta,
    
    @Schema(description = "Valor de venta transferida", example = "1200.00")
    BigDecimal rcValorVentaTransferida,
    
    @Schema(description = "Resultado financiero (ganancia/pérdida)", example = "7400.00")
    BigDecimal rcResultado,
    
    @Schema(description = "Rendimiento porcentual", example = "26.43")
    BigDecimal rcRendimiento,
    
    @Schema(description = "Costo por hectárea", example = "11200.00")
    BigDecimal rcCostoHectarea,
    
    @Schema(description = "Venta por hectárea", example = "14160.00")
    BigDecimal rcVentaHectarea,
    
    @Schema(description = "Resultado por hectárea", example = "2960.00")
    BigDecimal rcResultadoHectarea,
    
    @Schema(description = "Resultado por hectárea por día", example = "25.74")
    BigDecimal rcResultadoHectareaDia,
    
    @Schema(description = "Costo por libra producida", example = "2.33")
    BigDecimal rcCostoLibra,
    
    @Schema(description = "Precio de venta por libra", example = "3.00")
    BigDecimal rcVentaLibra,
    
    @Schema(description = "Resultado por libra", example = "0.67")
    BigDecimal rcResultadoLibra,
    
    @Schema(description = "Costo total por hectárea por día", example = "97.39")
    BigDecimal rcCostoTotalHectareaDia,
    
    @Schema(description = "Costo directo por hectárea por día", example = "76.52")
    BigDecimal rcCostoDirectoHectareaDia,
    
    @Schema(description = "Costo indirecto por hectárea por día", example = "20.87")
    BigDecimal rcCostoindirectoHectareaDia,
    
    @Schema(description = "Peso inicial del camarón", example = "0.001")
    BigDecimal rcPesoInicial,
    
    @Schema(description = "Costo de transferencia por hectárea por día", example = "2.78")
    BigDecimal rcCostoTransferenciaHectareaDia,
    
    @Schema(description = "Costo directo transferido por hectárea por día", example = "1.74")
    BigDecimal rcCostoDirectoTransferidoHectareaDia,
    
    @Schema(description = "Costo indirecto transferido por hectárea por día", example = "1.04")
    BigDecimal rcCostoIndirectoTransferidoHectareaDia,
    
    @Schema(description = "Otros costos adicionales", example = "500.00")
    BigDecimal rcOtrosCostos,
    
    @Schema(description = "Otros costos por hectárea por día", example = "1.74")
    BigDecimal rcOtrosHectareaDias,
    
    @Schema(description = "Número de animales cosechados", example = "212500")
    BigDecimal rcAnimales,
    
    @Schema(description = "Corrida de origen (en caso de transferencia)", example = "C-2023-12")
    String rcCorridaOrigen,
    
    @Schema(description = "Corrida de destino (en caso de transferencia)", example = "C-2024-02")
    String rcCorridaDestino,
    
    @Schema(description = "Costo por millar de larvas", example = "12.50")
    BigDecimal rcCostoMillar,
    
    @Schema(description = "Biomasa procesada en empacadora", example = "11500.00")
    BigDecimal rcBiomasaEmpacadora,
    
    @Schema(description = "Peso promedio en cosecha", example = "21.5")
    BigDecimal rcPesoCosecha,
    
    @Schema(description = "Tipo de liquidación", example = "DIRECTA")
    String rcTipoLiquidacion,
    
    @Schema(description = "Biomasa transferida a otras piscinas", example = "300.00")
    BigDecimal rcBiomasaTransferida,
    
    @Schema(description = "Libras totales pescadas", example = "11800.00")
    BigDecimal rcLibrasPescadas,
    
    @Schema(description = "Peso promedio cosechado", example = "21.3")
    BigDecimal rcPesoPromedioCosechado,
    
    @Schema(description = "Código de la empacadora", example = "EMP-001")
    String rcEmpacadoraCodigo
) {
}

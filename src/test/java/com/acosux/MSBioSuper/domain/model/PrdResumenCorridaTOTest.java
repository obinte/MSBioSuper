package com.acosux.MSBioSuper.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para PrdResumenCorridaTO.
 * 
 * Valida el comportamiento de la entidad de dominio para resumen de corridas.
 */
@DisplayName("PrdResumenCorridaTO - Pruebas Unitarias")
class PrdResumenCorridaTOTest {

    @Test
    @DisplayName("Debe crear instancia con constructor por defecto")
    void debeCrearInstanciaConConstructorPorDefecto() {
        // When
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();

        // Then
        assertNotNull(resumen);
    }

    @Test
    @DisplayName("Debe establecer y obtener ID correctamente")
    void debeEstablecerYObtenerIdCorrectamente() {
        // Given
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();
        Integer id = 1;

        // When
        resumen.setId(id);

        // Then
        assertEquals(id, resumen.getId());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de piscina correctamente")
    void debeEstablecerYObtenerDatosPiscina() {
        // Given
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();

        // When
        resumen.setPisNumero("PIS001");
        resumen.setEmpCodigo("EMP01");
        resumen.setSecCodigo("SEC01");
        resumen.setRcHectareaje(new BigDecimal("5.5"));

        // Then
        assertEquals("PIS001", resumen.getPisNumero());
        assertEquals("EMP01", resumen.getEmpCodigo());
        assertEquals("SEC01", resumen.getSecCodigo());
        assertEquals(new BigDecimal("5.5"), resumen.getRcHectareaje());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de corrida correctamente")
    void debeEstablecerYObtenerDatosCorrida() {
        // Given
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();

        // When
        resumen.setRcCorridaNumero("COR001");
        resumen.setRcFechaSiembra("2024-01-15");
        resumen.setRcFechaPesca("2024-06-15");
        resumen.setRcEdad(new BigDecimal("150"));

        // Then
        assertEquals("COR001", resumen.getRcCorridaNumero());
        assertEquals("2024-01-15", resumen.getRcFechaSiembra());
        assertEquals("2024-06-15", resumen.getRcFechaPesca());
        assertEquals(new BigDecimal("150"), resumen.getRcEdad());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de producción correctamente")
    void debeEstablecerYObtenerDatosProduccion() {
        // Given
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();

        // When
        resumen.setRcNumeroLarvas(new BigDecimal("500000"));
        resumen.setRcDensidad(new BigDecimal("25"));
        resumen.setRcBiomasa(new BigDecimal("1500.50"));
        resumen.setRcConversion(new BigDecimal("1.8"));

        // Then
        assertEquals(new BigDecimal("500000"), resumen.getRcNumeroLarvas());
        assertEquals(new BigDecimal("25"), resumen.getRcDensidad());
        assertEquals(new BigDecimal("1500.50"), resumen.getRcBiomasa());
        assertEquals(new BigDecimal("1.8"), resumen.getRcConversion());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de costos correctamente")
    void debeEstablecerYObtenerDatosCostos() {
        // Given
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();

        // When
        resumen.setRcCostoTotal(new BigDecimal("50000.00"));
        resumen.setRcCostoDirecto(new BigDecimal("35000.00"));
        resumen.setRcCostoIndirecto(new BigDecimal("15000.00"));
        resumen.setRcCostoBalanceado(new BigDecimal("25000.00"));

        // Then
        assertEquals(new BigDecimal("50000.00"), resumen.getRcCostoTotal());
        assertEquals(new BigDecimal("35000.00"), resumen.getRcCostoDirecto());
        assertEquals(new BigDecimal("15000.00"), resumen.getRcCostoIndirecto());
        assertEquals(new BigDecimal("25000.00"), resumen.getRcCostoBalanceado());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de ventas correctamente")
    void debeEstablecerYObtenerDatosVentas() {
        // Given
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();

        // When
        resumen.setRcValorVenta(new BigDecimal("75000.00"));
        resumen.setRcResultado(new BigDecimal("25000.00"));
        resumen.setRcRendimiento(new BigDecimal("85.5"));

        // Then
        assertEquals(new BigDecimal("75000.00"), resumen.getRcValorVenta());
        assertEquals(new BigDecimal("25000.00"), resumen.getRcResultado());
        assertEquals(new BigDecimal("85.5"), resumen.getRcRendimiento());
    }

    @Test
    @DisplayName("Debe manejar valores nulos correctamente")
    void debeManejarValoresNulosCorrectamente() {
        // Given
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();

        // When
        resumen.setRcBiomasa(null);
        resumen.setRcConversion(null);
        resumen.setPisNumero(null);

        // Then
        assertNull(resumen.getRcBiomasa());
        assertNull(resumen.getRcConversion());
        assertNull(resumen.getPisNumero());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de tallas correctamente")
    void debeEstablecerYObtenerDatosTallas() {
        // Given
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();

        // When
        resumen.setRcTGrande(new BigDecimal("30.5"));
        resumen.setRcTMediano(new BigDecimal("25.0"));
        resumen.setRcTPequeno(new BigDecimal("15.5"));
        resumen.setRcTPromedio(new BigDecimal("23.67"));

        // Then
        assertEquals(new BigDecimal("30.5"), resumen.getRcTGrande());
        assertEquals(new BigDecimal("25.0"), resumen.getRcTMediano());
        assertEquals(new BigDecimal("15.5"), resumen.getRcTPequeno());
        assertEquals(new BigDecimal("23.67"), resumen.getRcTPromedio());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de análisis por hectárea correctamente")
    void debeEstablecerYObtenerDatosAnalisisPorHectarea() {
        // Given
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();

        // When
        resumen.setRcCostoHectarea(new BigDecimal("10000.00"));
        resumen.setRcVentaHectarea(new BigDecimal("15000.00"));
        resumen.setRcResultadoHectarea(new BigDecimal("5000.00"));
        resumen.setRcResultadoHectareaDia(new BigDecimal("33.33"));

        // Then
        assertEquals(new BigDecimal("10000.00"), resumen.getRcCostoHectarea());
        assertEquals(new BigDecimal("15000.00"), resumen.getRcVentaHectarea());
        assertEquals(new BigDecimal("5000.00"), resumen.getRcResultadoHectarea());
        assertEquals(new BigDecimal("33.33"), resumen.getRcResultadoHectareaDia());
    }
}

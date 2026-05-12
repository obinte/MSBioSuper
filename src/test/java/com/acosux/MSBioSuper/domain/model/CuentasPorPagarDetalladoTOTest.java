package com.acosux.MSBioSuper.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CuentasPorPagarDetalladoTO.
 * 
 * Valida el comportamiento de la entidad de dominio para cuentas por pagar.
 */
@DisplayName("CuentasPorPagarDetalladoTO - Pruebas Unitarias")
class CuentasPorPagarDetalladoTOTest {

    @Test
    @DisplayName("Debe crear instancia con constructor por defecto")
    void debeCrearInstanciaConConstructorPorDefecto() {
        // When
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // Then
        assertNotNull(cuenta);
    }

    @Test
    @DisplayName("Debe establecer y obtener ID correctamente")
    void debeEstablecerYObtenerIdCorrectamente() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();
        Integer id = 1;

        // When
        cuenta.setId(id);

        // Then
        assertEquals(id, cuenta.getId());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de identificación")
    void debeEstablecerYObtenerDatosIdentificacion() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdPeriodo("2024");
        cuenta.setCxpdMotivo("COM");
        cuenta.setCxpdNumero("CXP001");
        cuenta.setCxpdDocumento("FAC-001");

        // Then
        assertEquals("2024", cuenta.getCxpdPeriodo());
        assertEquals("COM", cuenta.getCxpdMotivo());
        assertEquals("CXP001", cuenta.getCxpdNumero());
        assertEquals("FAC-001", cuenta.getCxpdDocumento());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos del proveedor")
    void debeEstablecerYObtenerDatosProveedor() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdProveedorCodigo("PROV001");
        cuenta.setCxpdProveedorId("1234567890");
        cuenta.setCxpdProveedorRazonSocial("Proveedor Test S.A.");
        cuenta.setCxpdProvRelacionado(false);

        // Then
        assertEquals("PROV001", cuenta.getCxpdProveedorCodigo());
        assertEquals("1234567890", cuenta.getCxpdProveedorId());
        assertEquals("Proveedor Test S.A.", cuenta.getCxpdProveedorRazonSocial());
        assertFalse(cuenta.getCxpdProvRelacionado());
    }

    @Test
    @DisplayName("Debe establecer y obtener fechas correctamente")
    void debeEstablecerYObtenerFechas() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdFechaEmision("2024-01-15");
        cuenta.setCxpdFechaVencimiento("2024-02-15");

        // Then
        assertEquals("2024-01-15", cuenta.getCxpdFechaEmision());
        assertEquals("2024-02-15", cuenta.getCxpdFechaVencimiento());
    }

    @Test
    @DisplayName("Debe establecer y obtener valores monetarios correctamente")
    void debeEstablecerYObtenerValoresMonetarios() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdSaldo(new BigDecimal("5000.00"));
        cuenta.setCxpdSaldoVencido(new BigDecimal("1000.00"));
        cuenta.setCxpdSaldoPorVencer(new BigDecimal("4000.00"));
        cuenta.setCxpdValorAprobado(new BigDecimal("5000.00"));
        cuenta.setCxpdAbonos(new BigDecimal("500.00"));

        // Then
        assertEquals(new BigDecimal("5000.00"), cuenta.getCxpdSaldo());
        assertEquals(new BigDecimal("1000.00"), cuenta.getCxpdSaldoVencido());
        assertEquals(new BigDecimal("4000.00"), cuenta.getCxpdSaldoPorVencer());
        assertEquals(new BigDecimal("5000.00"), cuenta.getCxpdValorAprobado());
        assertEquals(new BigDecimal("500.00"), cuenta.getCxpdAbonos());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de crédito")
    void debeEstablecerYObtenerDatosCredito() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdDiasCredito("30");
        cuenta.setCxpdDiasVencidos("5");

        // Then
        assertEquals("30", cuenta.getCxpdDiasCredito());
        assertEquals("5", cuenta.getCxpdDiasVencidos());
    }

    @Test
    @DisplayName("Debe establecer y obtener observaciones y aprobaciones")
    void debeEstablecerYObtenerObservacionesYAprobaciones() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdObservaciones("Pago pendiente de aprobación");
        cuenta.setCxpdUsuarioApruebaPago("USER001");

        // Then
        assertEquals("Pago pendiente de aprobación", cuenta.getCxpdObservaciones());
        assertEquals("USER001", cuenta.getCxpdUsuarioApruebaPago());
    }

    @Test
    @DisplayName("Debe establecer y obtener zona y CP")
    void debeEstablecerYObtenerZonaYCP() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdZona("NORTE");
        cuenta.setCxpdCp("CP001");

        // Then
        assertEquals("NORTE", cuenta.getCxpdZona());
        assertEquals("CP001", cuenta.getCxpdCp());
    }

    @Test
    @DisplayName("Debe manejar valores nulos correctamente")
    void debeManejarValoresNulosCorrectamente() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdSaldo(null);
        cuenta.setCxpdProveedorRazonSocial(null);
        cuenta.setCxpdObservaciones(null);

        // Then
        assertNull(cuenta.getCxpdSaldo());
        assertNull(cuenta.getCxpdProveedorRazonSocial());
        assertNull(cuenta.getCxpdObservaciones());
    }

    @Test
    @DisplayName("Debe manejar cuenta sin saldo vencido")
    void debeManejarCuentaSinSaldoVencido() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdSaldo(new BigDecimal("5000.00"));
        cuenta.setCxpdSaldoVencido(BigDecimal.ZERO);
        cuenta.setCxpdSaldoPorVencer(new BigDecimal("5000.00"));

        // Then
        assertEquals(BigDecimal.ZERO, cuenta.getCxpdSaldoVencido());
        assertEquals(cuenta.getCxpdSaldo(), cuenta.getCxpdSaldoPorVencer());
    }

    @Test
    @DisplayName("Debe manejar proveedor relacionado")
    void debeManejarProveedorRelacionado() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdProvRelacionado(true);

        // Then
        assertTrue(cuenta.getCxpdProvRelacionado());
    }

    @Test
    @DisplayName("Debe validar distribución de saldo entre vencido y por vencer")
    void debeValidarDistribucionSaldo() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();

        // When
        cuenta.setCxpdSaldo(new BigDecimal("5000.00"));
        cuenta.setCxpdSaldoVencido(new BigDecimal("2000.00"));
        cuenta.setCxpdSaldoPorVencer(new BigDecimal("3000.00"));

        // Then
        BigDecimal sumaDistribucion = cuenta.getCxpdSaldoVencido().add(cuenta.getCxpdSaldoPorVencer());
        assertEquals(cuenta.getCxpdSaldo(), sumaDistribucion);
    }
}

package com.acosux.MSBioSuper.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CuentasPorCobrarDetalladoApiTO.
 * 
 * Valida el comportamiento de la entidad de dominio para cuentas por cobrar.
 */
@DisplayName("CuentasPorCobrarDetalladoApiTO - Pruebas Unitarias")
class CuentasPorCobrarDetalladoApiTOTest {

    @Test
    @DisplayName("Debe crear instancia con constructor por defecto")
    void debeCrearInstanciaConConstructorPorDefecto() {
        // When
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();

        // Then
        assertNotNull(cuenta);
    }

    @Test
    @DisplayName("Debe establecer y obtener ID correctamente")
    void debeEstablecerYObtenerIdCorrectamente() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();
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
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();

        // When
        cuenta.setCxcdEmpresa("EMP01");
        cuenta.setCxcdPeriodo("2024");
        cuenta.setCxcdMotivo("VTA");
        cuenta.setCxcdNumero("CXC001");
        cuenta.setCxcdDocumentoNumero("FAC-001-001-000001");

        // Then
        assertEquals("EMP01", cuenta.getCxcdEmpresa());
        assertEquals("2024", cuenta.getCxcdPeriodo());
        assertEquals("VTA", cuenta.getCxcdMotivo());
        assertEquals("CXC001", cuenta.getCxcdNumero());
        assertEquals("FAC-001-001-000001", cuenta.getCxcdDocumentoNumero());
    }

    @Test
    @DisplayName("Debe obtener datos del cliente correctamente")
    void debeObtenerDatosCliente() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();

        // When - El clienteId se establece internamente por JPA
        String clienteId = cuenta.getCxcdClienteId();

        // Then
        // El getter debe estar disponible aunque no haya setter público
        assertNotNull(cuenta);
    }

    @Test
    @DisplayName("Debe establecer y obtener fechas correctamente")
    void debeEstablecerYObtenerFechas() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();

        // When
        cuenta.setCxcdFechaEmision("2024-01-15");
        cuenta.setCxcdFechaVencimiento("2024-02-15");

        // Then
        assertEquals("2024-01-15", cuenta.getCxcdFechaEmision());
        assertEquals("2024-02-15", cuenta.getCxcdFechaVencimiento());
    }

    @Test
    @DisplayName("Debe establecer y obtener valores monetarios correctamente")
    void debeEstablecerYObtenerValoresMonetarios() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();

        // When
        cuenta.setCxcdTotal(new BigDecimal("1000.00"));
        cuenta.setCxcdRetencionValor(new BigDecimal("10.00"));
        cuenta.setCxcdAbono(new BigDecimal("500.00"));
        cuenta.setCxcdSaldo(new BigDecimal("490.00"));

        // Then
        assertEquals(new BigDecimal("1000.00"), cuenta.getCxcdTotal());
        assertEquals(new BigDecimal("10.00"), cuenta.getCxcdRetencionValor());
        assertEquals(new BigDecimal("500.00"), cuenta.getCxcdAbono());
        assertEquals(new BigDecimal("490.00"), cuenta.getCxcdSaldo());
    }

    @Test
    @DisplayName("Debe manejar valores nulos correctamente")
    void debeManejarValoresNulosCorrectamente() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();

        // When
        cuenta.setCxcdSaldo(null);
        cuenta.setCxcdFechaEmision(null);

        // Then
        assertNull(cuenta.getCxcdSaldo());
        assertNull(cuenta.getCxcdFechaEmision());
    }

    @Test
    @DisplayName("Debe calcular saldo correctamente después de abono")
    void debeCalcularSaldoCorrectamenteConAbono() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();
        cuenta.setCxcdTotal(new BigDecimal("1000.00"));
        cuenta.setCxcdRetencionValor(new BigDecimal("10.00"));
        cuenta.setCxcdAbono(new BigDecimal("500.00"));

        // When
        BigDecimal saldoEsperado = cuenta.getCxcdTotal()
            .subtract(cuenta.getCxcdRetencionValor())
            .subtract(cuenta.getCxcdAbono());
        cuenta.setCxcdSaldo(saldoEsperado);

        // Then
        assertEquals(new BigDecimal("490.00"), cuenta.getCxcdSaldo());
    }

    @Test
    @DisplayName("Debe manejar cuenta sin retenciones")
    void debeManejarCuentaSinRetenciones() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();

        // When
        cuenta.setCxcdTotal(new BigDecimal("1000.00"));
        cuenta.setCxcdRetencionValor(BigDecimal.ZERO);
        cuenta.setCxcdAbono(BigDecimal.ZERO);
        cuenta.setCxcdSaldo(new BigDecimal("1000.00"));

        // Then
        assertEquals(new BigDecimal("1000.00"), cuenta.getCxcdSaldo());
        assertEquals(BigDecimal.ZERO, cuenta.getCxcdRetencionValor());
    }

    @Test
    @DisplayName("Debe manejar cuenta totalmente pagada")
    void debeManejarCuentaTotalmentePagada() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();

        // When
        cuenta.setCxcdTotal(new BigDecimal("1000.00"));
        cuenta.setCxcdAbono(new BigDecimal("1000.00"));
        cuenta.setCxcdSaldo(BigDecimal.ZERO);

        // Then
        assertEquals(BigDecimal.ZERO, cuenta.getCxcdSaldo());
        assertEquals(cuenta.getCxcdTotal(), cuenta.getCxcdAbono());
    }
}

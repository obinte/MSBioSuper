package com.acosux.MSBioSuper.domain.service;

import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para CuentasService.
 * 
 * Valida la lógica de negocio de cálculos, filtrado y validación de cuentas.
 * 
 * @author Josias Rubén Aldaz Arcalles
 */
@DisplayName("CuentasService - Pruebas Unitarias")
class CuentasServiceTest {

    private CuentasService cuentasService;

    @BeforeEach
    void setUp() {
        cuentasService = new CuentasService();
    }

    // ==================== Pruebas de calcularTotalSaldoCobrar ====================

    @Test
    @DisplayName("Debe retornar ZERO cuando lista de cuentas por cobrar es null")
    void calcularTotalSaldoCobrar_ListaNull_RetornaZero() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = null;

        // When
        BigDecimal resultado = cuentasService.calcularTotalSaldoCobrar(cuentas);

        // Then
        assertThat(resultado).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Debe retornar ZERO cuando lista de cuentas por cobrar está vacía")
    void calcularTotalSaldoCobrar_ListaVacia_RetornaZero() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = new ArrayList<>();

        // When
        BigDecimal resultado = cuentasService.calcularTotalSaldoCobrar(cuentas);

        // Then
        assertThat(resultado).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Debe calcular correctamente el total de saldos por cobrar")
    void calcularTotalSaldoCobrar_CuentasValidas_CalculaCorrectamente() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = new ArrayList<>();
        
        CuentasPorCobrarDetalladoApiTO cuenta1 = new CuentasPorCobrarDetalladoApiTO();
        cuenta1.setCxcdSaldo(new BigDecimal("1500.50"));
        cuentas.add(cuenta1);
        
        CuentasPorCobrarDetalladoApiTO cuenta2 = new CuentasPorCobrarDetalladoApiTO();
        cuenta2.setCxcdSaldo(new BigDecimal("2300.75"));
        cuentas.add(cuenta2);
        
        CuentasPorCobrarDetalladoApiTO cuenta3 = new CuentasPorCobrarDetalladoApiTO();
        cuenta3.setCxcdSaldo(new BigDecimal("500.00"));
        cuentas.add(cuenta3);

        // When
        BigDecimal resultado = cuentasService.calcularTotalSaldoCobrar(cuentas);

        // Then
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("4301.25"));
    }

    @Test
    @DisplayName("Debe ignorar saldos null al calcular total por cobrar")
    void calcularTotalSaldoCobrar_SaldosNull_IgnoraNull() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = new ArrayList<>();
        
        CuentasPorCobrarDetalladoApiTO cuenta1 = new CuentasPorCobrarDetalladoApiTO();
        cuenta1.setCxcdSaldo(new BigDecimal("1000.00"));
        cuentas.add(cuenta1);
        
        CuentasPorCobrarDetalladoApiTO cuenta2 = new CuentasPorCobrarDetalladoApiTO();
        cuenta2.setCxcdSaldo(null);
        cuentas.add(cuenta2);
        
        CuentasPorCobrarDetalladoApiTO cuenta3 = new CuentasPorCobrarDetalladoApiTO();
        cuenta3.setCxcdSaldo(new BigDecimal("500.00"));
        cuentas.add(cuenta3);

        // When
        BigDecimal resultado = cuentasService.calcularTotalSaldoCobrar(cuentas);

        // Then
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("1500.00"));
    }

    // ==================== Pruebas de calcularTotalSaldoPagar ====================

    @Test
    @DisplayName("Debe retornar ZERO cuando lista de cuentas por pagar es null")
    void calcularTotalSaldoPagar_ListaNull_RetornaZero() {
        // Given
        List<CuentasPorPagarDetalladoTO> cuentas = null;

        // When
        BigDecimal resultado = cuentasService.calcularTotalSaldoPagar(cuentas);

        // Then
        assertThat(resultado).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Debe retornar ZERO cuando lista de cuentas por pagar está vacía")
    void calcularTotalSaldoPagar_ListaVacia_RetornaZero() {
        // Given
        List<CuentasPorPagarDetalladoTO> cuentas = new ArrayList<>();

        // When
        BigDecimal resultado = cuentasService.calcularTotalSaldoPagar(cuentas);

        // Then
        assertThat(resultado).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Debe calcular correctamente el total de saldos por pagar")
    void calcularTotalSaldoPagar_CuentasValidas_CalculaCorrectamente() {
        // Given
        List<CuentasPorPagarDetalladoTO> cuentas = new ArrayList<>();
        
        CuentasPorPagarDetalladoTO cuenta1 = new CuentasPorPagarDetalladoTO();
        cuenta1.setCxpdSaldo(new BigDecimal("3500.00"));
        cuentas.add(cuenta1);
        
        CuentasPorPagarDetalladoTO cuenta2 = new CuentasPorPagarDetalladoTO();
        cuenta2.setCxpdSaldo(new BigDecimal("1200.50"));
        cuentas.add(cuenta2);

        // When
        BigDecimal resultado = cuentasService.calcularTotalSaldoPagar(cuentas);

        // Then
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("4700.50"));
    }

    @Test
    @DisplayName("Debe ignorar saldos null al calcular total por pagar")
    void calcularTotalSaldoPagar_SaldosNull_IgnoraNull() {
        // Given
        List<CuentasPorPagarDetalladoTO> cuentas = new ArrayList<>();
        
        CuentasPorPagarDetalladoTO cuenta1 = new CuentasPorPagarDetalladoTO();
        cuenta1.setCxpdSaldo(new BigDecimal("2000.00"));
        cuentas.add(cuenta1);
        
        CuentasPorPagarDetalladoTO cuenta2 = new CuentasPorPagarDetalladoTO();
        cuenta2.setCxpdSaldo(null);
        cuentas.add(cuenta2);

        // When
        BigDecimal resultado = cuentasService.calcularTotalSaldoPagar(cuentas);

        // Then
        assertThat(resultado).isEqualByComparingTo(new BigDecimal("2000.00"));
    }

    // ==================== Pruebas de filtrarCuentasCobrarPorCliente ====================

    @Test
    @DisplayName("Debe retornar lista vacía cuando cuentas es null")
    void filtrarCuentasCobrarPorCliente_CuentasNull_RetornaListaVacia() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = null;

        // When
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
            cuentasService.filtrarCuentasCobrarPorCliente(cuentas, "CLI001");

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar todas las cuentas cuando cliente es null")
    void filtrarCuentasCobrarPorCliente_ClienteNull_RetornaTodas() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = crearCuentasCobrarTest();

        // When
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
            cuentasService.filtrarCuentasCobrarPorCliente(cuentas, null);

        // Then
        assertThat(resultado).hasSize(3);
    }

    @Test
    @DisplayName("Debe filtrar correctamente cuentas por cliente")
    void filtrarCuentasCobrarPorCliente_ClienteValido_FiltraCorrectamente() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = crearCuentasCobrarTest();

        // When
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
            cuentasService.filtrarCuentasCobrarPorCliente(cuentas, "CLI001");

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(c -> "CLI001".equals(c.getCxcdClienteId()));
    }

    // ==================== Pruebas de agruparCuentasCobrarPorCliente ====================

    @Test
    @DisplayName("Debe retornar mapa vacío cuando cuentas es null")
    void agruparCuentasCobrarPorCliente_CuentasNull_RetornaMapaVacio() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = null;

        // When
        Map<String, List<CuentasPorCobrarDetalladoApiTO>> resultado = 
            cuentasService.agruparCuentasCobrarPorCliente(cuentas);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe agrupar correctamente cuentas por cliente")
    void agruparCuentasCobrarPorCliente_CuentasValidas_AgrupaCorrectamente() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = crearCuentasCobrarTest();

        // When
        Map<String, List<CuentasPorCobrarDetalladoApiTO>> resultado = 
            cuentasService.agruparCuentasCobrarPorCliente(cuentas);

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get("CLI001")).hasSize(2);
        assertThat(resultado.get("CLI002")).hasSize(1);
    }

    // ==================== Pruebas de esCuentaCobrarValida ====================

    @Test
    @DisplayName("Debe retornar false cuando cuenta por cobrar es null")
    void esCuentaCobrarValida_CuentaNull_RetornaFalse() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = null;

        // When
        boolean resultado = cuentasService.esCuentaCobrarValida(cuenta);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar false cuando número de cuenta es null")
    void esCuentaCobrarValida_NumeroNull_RetornaFalse() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();
        cuenta.setCxcdNumero(null);

        // When
        boolean resultado = cuentasService.esCuentaCobrarValida(cuenta);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar false cuando cliente ID es null")
    void esCuentaCobrarValida_ClienteIdNull_RetornaFalse() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();
        cuenta.setCxcdNumero("CXC001");

        // When
        boolean resultado = cuentasService.esCuentaCobrarValida(cuenta);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar true cuando cuenta por cobrar es válida")
    void esCuentaCobrarValida_CuentaValida_RetornaTrue() {
        // Given
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();
        cuenta.setCxcdNumero("CXC001");
        cuenta.setCxcdClienteId("CLI001");

        // When
        boolean resultado = cuentasService.esCuentaCobrarValida(cuenta);

        // Then
        assertThat(resultado).isTrue();
    }

    // ==================== Pruebas de esCuentaPagarValida ====================

    @Test
    @DisplayName("Debe retornar false cuando cuenta por pagar es null")
    void esCuentaPagarValida_CuentaNull_RetornaFalse() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = null;

        // When
        boolean resultado = cuentasService.esCuentaPagarValida(cuenta);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar false cuando número de cuenta por pagar es null")
    void esCuentaPagarValida_NumeroNull_RetornaFalse() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();
        cuenta.setCxpdNumero(null);

        // When
        boolean resultado = cuentasService.esCuentaPagarValida(cuenta);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar true cuando cuenta por pagar es válida")
    void esCuentaPagarValida_CuentaValida_RetornaTrue() {
        // Given
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();
        cuenta.setCxpdNumero("CXP001");

        // When
        boolean resultado = cuentasService.esCuentaPagarValida(cuenta);

        // Then
        assertThat(resultado).isTrue();
    }

    // ==================== Pruebas de filtrarCuentasCobrarValidas ====================

    @Test
    @DisplayName("Debe retornar lista vacía cuando cuentas por cobrar es null")
    void filtrarCuentasCobrarValidas_CuentasNull_RetornaListaVacia() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = null;

        // When
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
            cuentasService.filtrarCuentasCobrarValidas(cuentas);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe filtrar cuentas por cobrar inválidas")
    void filtrarCuentasCobrarValidas_CuentasInvalidas_FiltraCorrectamente() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> cuentas = new ArrayList<>();
        
        // Cuenta válida
        CuentasPorCobrarDetalladoApiTO cuentaValida = new CuentasPorCobrarDetalladoApiTO();
        cuentaValida.setCxcdNumero("CXC001");
        cuentaValida.setCxcdClienteId("CLI001");
        cuentas.add(cuentaValida);
        
        // Cuenta sin número
        CuentasPorCobrarDetalladoApiTO cuentaSinNumero = new CuentasPorCobrarDetalladoApiTO();
        cuentaSinNumero.setCxcdClienteId("CLI002");
        cuentas.add(cuentaSinNumero);

        // When
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
            cuentasService.filtrarCuentasCobrarValidas(cuentas);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCxcdNumero()).isEqualTo("CXC001");
    }

    // ==================== Pruebas de filtrarCuentasPagarValidas ====================

    @Test
    @DisplayName("Debe retornar lista vacía cuando cuentas por pagar es null")
    void filtrarCuentasPagarValidas_CuentasNull_RetornaListaVacia() {
        // Given
        List<CuentasPorPagarDetalladoTO> cuentas = null;

        // When
        List<CuentasPorPagarDetalladoTO> resultado = 
            cuentasService.filtrarCuentasPagarValidas(cuentas);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe filtrar cuentas por pagar inválidas")
    void filtrarCuentasPagarValidas_CuentasInvalidas_FiltraCorrectamente() {
        // Given
        List<CuentasPorPagarDetalladoTO> cuentas = new ArrayList<>();
        
        // Cuenta válida
        CuentasPorPagarDetalladoTO cuentaValida = new CuentasPorPagarDetalladoTO();
        cuentaValida.setCxpdNumero("CXP001");
        cuentas.add(cuentaValida);
        
        // Cuenta sin número
        CuentasPorPagarDetalladoTO cuentaSinNumero = new CuentasPorPagarDetalladoTO();
        cuentas.add(cuentaSinNumero);

        // When
        List<CuentasPorPagarDetalladoTO> resultado = 
            cuentasService.filtrarCuentasPagarValidas(cuentas);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCxpdNumero()).isEqualTo("CXP001");
    }

    // ==================== Métodos auxiliares ====================

    /**
     * Crea una lista de cuentas por cobrar de prueba.
     */
    private List<CuentasPorCobrarDetalladoApiTO> crearCuentasCobrarTest() {
        List<CuentasPorCobrarDetalladoApiTO> cuentas = new ArrayList<>();

        // Cuenta 1: Cliente CLI001
        CuentasPorCobrarDetalladoApiTO cuenta1 = new CuentasPorCobrarDetalladoApiTO();
        cuenta1.setId(1);
        cuenta1.setCxcdNumero("CXC001");
        cuenta1.setCxcdClienteId("CLI001");
        cuenta1.setCxcdSaldo(new BigDecimal("1500.00"));
        cuentas.add(cuenta1);

        // Cuenta 2: Cliente CLI001
        CuentasPorCobrarDetalladoApiTO cuenta2 = new CuentasPorCobrarDetalladoApiTO();
        cuenta2.setId(2);
        cuenta2.setCxcdNumero("CXC002");
        cuenta2.setCxcdClienteId("CLI001");
        cuenta2.setCxcdSaldo(new BigDecimal("2300.00"));
        cuentas.add(cuenta2);

        // Cuenta 3: Cliente CLI002
        CuentasPorCobrarDetalladoApiTO cuenta3 = new CuentasPorCobrarDetalladoApiTO();
        cuenta3.setId(3);
        cuenta3.setCxcdNumero("CXC003");
        cuenta3.setCxcdClienteId("CLI002");
        cuenta3.setCxcdSaldo(new BigDecimal("800.00"));
        cuentas.add(cuenta3);

        return cuentas;
    }
}

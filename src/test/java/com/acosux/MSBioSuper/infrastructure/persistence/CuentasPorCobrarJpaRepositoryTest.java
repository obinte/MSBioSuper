package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para CuentasPorCobrarJpaRepository.
 * 
 * Verifica el correcto funcionamiento de los métodos de consulta para cuentas por cobrar,
 * incluyendo filtrado por empresa, cliente, periodo, fechas y saldos.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de CuentasPorCobrarJpaRepository")
class CuentasPorCobrarJpaRepositoryTest {

    @Autowired
    private CuentasPorCobrarJpaRepository cuentasRepository;

    @Autowired
    private TestEntityManager entityManager;

    private CuentasPorCobrarDetalladoApiTO cuenta1;
    private CuentasPorCobrarDetalladoApiTO cuenta2;
    private CuentasPorCobrarDetalladoApiTO cuenta3;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba
        cuenta1 = new CuentasPorCobrarDetalladoApiTO();
        cuenta1.setEmpresa("01");
        cuenta1.setPeriodo("202401");
        cuenta1.setCliente("CLI001");
        cuenta1.setMotivo("VENTA");
        cuenta1.setNumeroDocumento("FAC-001");
        cuenta1.setFechaEmision("2024-01-15");
        cuenta1.setFechaVencimiento("2024-02-15");
        cuenta1.setSaldo(new BigDecimal("1000.00"));

        cuenta2 = new CuentasPorCobrarDetalladoApiTO();
        cuenta2.setEmpresa("01");
        cuenta2.setPeriodo("202401");
        cuenta2.setCliente("CLI002");
        cuenta2.setMotivo("VENTA");
        cuenta2.setNumeroDocumento("FAC-002");
        cuenta2.setFechaEmision("2024-01-20");
        cuenta2.setFechaVencimiento("2024-02-20");
        cuenta2.setSaldo(new BigDecimal("2000.00"));

        cuenta3 = new CuentasPorCobrarDetalladoApiTO();
        cuenta3.setEmpresa("02");
        cuenta3.setPeriodo("202401");
        cuenta3.setCliente("CLI001");
        cuenta3.setMotivo("SERVICIO");
        cuenta3.setNumeroDocumento("FAC-003");
        cuenta3.setFechaEmision("2024-01-25");
        cuenta3.setFechaVencimiento("2024-02-25");
        cuenta3.setSaldo(new BigDecimal("500.00"));

        entityManager.persist(cuenta1);
        entityManager.persist(cuenta2);
        entityManager.persist(cuenta3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar cuentas por cobrar por empresa")
    void debeEncontrarCuentasPorEmpresa() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = cuentasRepository.findByEmpresa("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(CuentasPorCobrarDetalladoApiTO::getEmpresa)
                .containsOnly("01");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por empresa y periodo")
    void debeEncontrarCuentasPorEmpresaYPeriodo() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndPeriodo("01", "202401");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(CuentasPorCobrarDetalladoApiTO::getPeriodo)
                .containsOnly("202401");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por empresa y cliente")
    void debeEncontrarCuentasPorEmpresaYCliente() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndCliente("01", "CLI001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCliente()).isEqualTo("CLI001");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por empresa y motivo")
    void debeEncontrarCuentasPorEmpresaYMotivo() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndMotivo("01", "VENTA");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(CuentasPorCobrarDetalladoApiTO::getMotivo)
                .containsOnly("VENTA");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por empresa y número de documento")
    void debeEncontrarCuentasPorEmpresaYNumeroDocumento() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndNumeroDocumento("01", "FAC-001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNumeroDocumento()).isEqualTo("FAC-001");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por empresa y rango de fechas de emisión")
    void debeEncontrarCuentasPorEmpresaYRangoFechasEmision() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndFechaEmisionBetween("01", "2024-01-01", "2024-01-18");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFechaEmision()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por empresa y rango de fechas de vencimiento")
    void debeEncontrarCuentasPorEmpresaYRangoFechasVencimiento() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndFechaVencimientoBetween("01", "2024-02-01", "2024-02-18");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFechaVencimiento()).isEqualTo("2024-02-15");
    }

    @Test
    @DisplayName("Debe encontrar cuentas con saldo mayor a un mínimo")
    void debeEncontrarCuentasConSaldoMayorAMinimo() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndSaldoGreaterThan("01", new BigDecimal("1500.00"));

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getSaldo()).isGreaterThan(new BigDecimal("1500.00"));
    }

    @Test
    @DisplayName("Debe encontrar cuentas por cliente y rango de fechas")
    void debeEncontrarCuentasPorClienteYRangoFechas() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndClienteAndFechaEmisionBetween(
                        "01", "CLI001", "2024-01-01", "2024-01-31");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCliente()).isEqualTo("CLI001");
    }

    @Test
    @DisplayName("Debe ordenar cuentas por fecha de emisión descendente")
    void debeOrdenarCuentasPorFechaEmisionDescendente() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaOrderByFechaEmisionDesc("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFechaEmision()).isEqualTo("2024-01-20");
        assertThat(resultado.get(1).getFechaEmision()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe ordenar cuentas por fecha de vencimiento ascendente")
    void debeOrdenarCuentasPorFechaVencimientoAscendente() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaOrderByFechaVencimientoAsc("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFechaVencimiento()).isEqualTo("2024-02-15");
        assertThat(resultado.get(1).getFechaVencimiento()).isEqualTo("2024-02-20");
    }

    @Test
    @DisplayName("Debe ordenar cuentas con saldo mayor a mínimo por saldo descendente")
    void debeOrdenarCuentasConSaldoMayorPorSaldoDescendente() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndSaldoGreaterThanOrderBySaldoDesc(
                        "01", new BigDecimal("500.00"));

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getSaldo()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(resultado.get(1).getSaldo()).isEqualTo(new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Debe encontrar cuentas por cliente ordenadas por fecha")
    void debeEncontrarCuentasPorClienteOrdenadasPorFecha() {
        // Arrange
        CuentasPorCobrarDetalladoApiTO cuenta4 = new CuentasPorCobrarDetalladoApiTO();
        cuenta4.setEmpresa("01");
        cuenta4.setCliente("CLI001");
        cuenta4.setFechaEmision("2024-01-30");
        cuenta4.setSaldo(new BigDecimal("1500.00"));
        entityManager.persist(cuenta4);
        entityManager.flush();

        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndClienteOrderByFechaEmisionDesc("01", "CLI001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFechaEmision()).isEqualTo("2024-01-30");
    }

    @Test
    @DisplayName("Debe encontrar cuentas vencidas")
    void debeEncontrarCuentasVencidas() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndFechaVencimientoLessThan("01", "2024-02-18");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFechaVencimiento()).isEqualTo("2024-02-15");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por periodo ordenadas por cliente")
    void debeEncontrarCuentasPorPeriodoOrdenadasPorCliente() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = 
                cuentasRepository.findByEmpresaAndPeriodoOrderByClienteAsc("01", "202401");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getCliente()).isEqualTo("CLI001");
        assertThat(resultado.get(1).getCliente()).isEqualTo("CLI002");
    }

    @Test
    @DisplayName("Debe contar cuentas por empresa")
    void debeContarCuentasPorEmpresa() {
        // Act
        long count = cuentasRepository.countByEmpresa("01");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar cuentas por empresa y cliente")
    void debeContarCuentasPorEmpresaYCliente() {
        // Act
        long count = cuentasRepository.countByEmpresaAndCliente("01", "CLI001");

        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe contar cuentas con saldo mayor a mínimo")
    void debeContarCuentasConSaldoMayorAMinimo() {
        // Act
        long count = cuentasRepository.countByEmpresaAndSaldoGreaterThan("01", new BigDecimal("1500.00"));

        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay cuentas")
    void debeRetornarListaVaciaCuandoNoHayCuentas() {
        // Act
        List<CuentasPorCobrarDetalladoApiTO> resultado = cuentasRepository.findByEmpresa("99");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar cero cuando no hay cuentas para contar")
    void debeRetornarCeroCuandoNoHayCuentasParaContar() {
        // Act
        long count = cuentasRepository.countByEmpresa("99");

        // Assert
        assertThat(count).isZero();
    }
}

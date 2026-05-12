package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;
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
 * Pruebas unitarias para CuentasPorPagarJpaRepository.
 * 
 * Verifica el correcto funcionamiento de los métodos de consulta para cuentas por pagar,
 * incluyendo filtrado por proveedor, periodo, fechas y saldos.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de CuentasPorPagarJpaRepository")
class CuentasPorPagarJpaRepositoryTest {

    @Autowired
    private CuentasPorPagarJpaRepository cuentasRepository;

    @Autowired
    private TestEntityManager entityManager;

    private CuentasPorPagarDetalladoTO cuenta1;
    private CuentasPorPagarDetalladoTO cuenta2;
    private CuentasPorPagarDetalladoTO cuenta3;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba
        cuenta1 = new CuentasPorPagarDetalladoTO();
        cuenta1.setPeriodo("202401");
        cuenta1.setProveedor("PROV001");
        cuenta1.setProveedorCodigo("P001");
        cuenta1.setMotivo("COMPRA");
        cuenta1.setNumeroDocumento("FAC-001");
        cuenta1.setFechaEmision("2024-01-15");
        cuenta1.setFechaVencimiento("2024-02-15");
        cuenta1.setSaldo(new BigDecimal("1000.00"));
        cuenta1.setSaldoVencido(new BigDecimal("0.00"));
        cuenta1.setZona("ZONA1");

        cuenta2 = new CuentasPorPagarDetalladoTO();
        cuenta2.setPeriodo("202401");
        cuenta2.setProveedor("PROV002");
        cuenta2.setProveedorCodigo("P002");
        cuenta2.setMotivo("SERVICIO");
        cuenta2.setNumeroDocumento("FAC-002");
        cuenta2.setFechaEmision("2024-01-20");
        cuenta2.setFechaVencimiento("2024-02-20");
        cuenta2.setSaldo(new BigDecimal("2000.00"));
        cuenta2.setSaldoVencido(new BigDecimal("500.00"));
        cuenta2.setZona("ZONA2");

        cuenta3 = new CuentasPorPagarDetalladoTO();
        cuenta3.setPeriodo("202402");
        cuenta3.setProveedor("PROV001");
        cuenta3.setProveedorCodigo("P001");
        cuenta3.setMotivo("COMPRA");
        cuenta3.setNumeroDocumento("FAC-003");
        cuenta3.setFechaEmision("2024-02-05");
        cuenta3.setFechaVencimiento("2024-03-05");
        cuenta3.setSaldo(new BigDecimal("1500.00"));
        cuenta3.setSaldoVencido(new BigDecimal("0.00"));
        cuenta3.setZona("ZONA1");

        entityManager.persist(cuenta1);
        entityManager.persist(cuenta2);
        entityManager.persist(cuenta3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar cuentas por periodo")
    void debeEncontrarCuentasPorPeriodo() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = cuentasRepository.findByPeriodo("202401");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(CuentasPorPagarDetalladoTO::getPeriodo)
                .containsOnly("202401");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por proveedor")
    void debeEncontrarCuentasPorProveedor() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = cuentasRepository.findByProveedor("PROV001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(CuentasPorPagarDetalladoTO::getProveedor)
                .containsOnly("PROV001");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por código de proveedor")
    void debeEncontrarCuentasPorCodigoProveedor() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = cuentasRepository.findByProveedorCodigo("P001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(CuentasPorPagarDetalladoTO::getProveedorCodigo)
                .containsOnly("P001");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por motivo")
    void debeEncontrarCuentasPorMotivo() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = cuentasRepository.findByMotivo("COMPRA");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(CuentasPorPagarDetalladoTO::getMotivo)
                .containsOnly("COMPRA");
    }

    @Test
    @DisplayName("Debe encontrar cuentas por rango de fechas de emisión")
    void debeEncontrarCuentasPorRangoFechasEmision() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = 
                cuentasRepository.findByFechaEmisionBetween("2024-01-01", "2024-01-31");

        // Assert
        assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("Debe encontrar cuentas con saldo mayor a mínimo")
    void debeEncontrarCuentasConSaldoMayorAMinimo() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = 
                cuentasRepository.findBySaldoGreaterThan(new BigDecimal("1200.00"));

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(c -> c.getSaldo().compareTo(new BigDecimal("1200.00")) > 0);
    }

    @Test
    @DisplayName("Debe encontrar cuentas con saldo vencido mayor a mínimo")
    void debeEncontrarCuentasConSaldoVencidoMayorAMinimo() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = 
                cuentasRepository.findBySaldoVencidoGreaterThan(new BigDecimal("100.00"));

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getSaldoVencido()).isGreaterThan(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Debe encontrar cuentas por zona")
    void debeEncontrarCuentasPorZona() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = cuentasRepository.findByZona("ZONA1");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(CuentasPorPagarDetalladoTO::getZona)
                .containsOnly("ZONA1");
    }

    @Test
    @DisplayName("Debe ordenar cuentas por fecha de emisión descendente")
    void debeOrdenarCuentasPorFechaEmisionDescendente() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = 
                cuentasRepository.findByFechaEmisionBetweenOrderByFechaEmisionDesc(
                        "2024-01-01", "2024-02-28");

        // Assert
        assertThat(resultado).hasSize(3);
        assertThat(resultado.get(0).getFechaEmision()).isEqualTo("2024-02-05");
        assertThat(resultado.get(2).getFechaEmision()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe ordenar cuentas por saldo descendente")
    void debeOrdenarCuentasPorSaldoDescendente() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = 
                cuentasRepository.findBySaldoGreaterThanOrderBySaldoDesc(new BigDecimal("500.00"));

        // Assert
        assertThat(resultado).hasSize(3);
        assertThat(resultado.get(0).getSaldo()).isEqualTo(new BigDecimal("2000.00"));
    }

    @Test
    @DisplayName("Debe encontrar cuentas vencidas")
    void debeEncontrarCuentasVencidas() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = 
                cuentasRepository.findByFechaVencimientoLessThan("2024-02-18");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFechaVencimiento()).isEqualTo("2024-02-15");
    }

    @Test
    @DisplayName("Debe contar cuentas por proveedor")
    void debeContarCuentasPorProveedor() {
        // Act
        long count = cuentasRepository.countByProveedor("PROV001");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar cuentas por periodo")
    void debeContarCuentasPorPeriodo() {
        // Act
        long count = cuentasRepository.countByPeriodo("202401");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar cuentas con saldo vencido")
    void debeContarCuentasConSaldoVencido() {
        // Act
        long count = cuentasRepository.countBySaldoVencidoGreaterThan(new BigDecimal("100.00"));

        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay cuentas")
    void debeRetornarListaVaciaCuandoNoHayCuentas() {
        // Act
        List<CuentasPorPagarDetalladoTO> resultado = cuentasRepository.findByPeriodo("999999");

        // Assert
        assertThat(resultado).isEmpty();
    }
}

package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.config.BaseIntegrationTest;
import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para CuentasPorPagarJpaRepository.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-24
 */
@Transactional
@DisplayName("Pruebas de Integración - CuentasPorPagarJpaRepository")
class CuentasPorPagarJpaRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CuentasPorPagarJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        // Crear datos de prueba
        CuentasPorPagarDetalladoTO cuenta1 = createCuenta(1, "2024-01", "PROV-001", "COMPRA", "COMP-001",
                "2024-01-15", "2024-02-15", new BigDecimal("1500"), new BigDecimal("750"), new BigDecimal("0"));
        
        CuentasPorPagarDetalladoTO cuenta2 = createCuenta(2, "2024-01", "PROV-001", "SERVICIO", "SERV-001",
                "2024-01-20", "2024-02-20", new BigDecimal("800"), new BigDecimal("800"), new BigDecimal("0"));
        
        CuentasPorPagarDetalladoTO cuenta3 = createCuenta(3, "2024-02", "PROV-002", "COMPRA", "COMP-002",
                "2024-02-10", "2024-03-10", new BigDecimal("3000"), new BigDecimal("3000"), new BigDecimal("0"));

        repository.save(cuenta1);
        repository.save(cuenta2);
        repository.save(cuenta3);
    }

    @Test
    @DisplayName("Debe buscar cuentas por periodo")
    void testFindByPeriodo() {
        // When
        List<CuentasPorPagarDetalladoTO> resultados = repository.findByPeriodo("2024-01");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(c -> c.getCxpPeriodo().equals("2024-01"));
    }

    @Test
    @DisplayName("Debe buscar cuentas por proveedor")
    void testFindByProveedor() {
        // When
        List<CuentasPorPagarDetalladoTO> resultados = repository.findByProveedor("PROV-001");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(c -> c.getCxpProveedor().equals("PROV-001"));
    }

    @Test
    @DisplayName("Debe buscar cuentas por motivo")
    void testFindByMotivo() {
        // When
        List<CuentasPorPagarDetalladoTO> resultados = repository.findByMotivo("COMPRA");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(c -> c.getCxpMotivo().equals("COMPRA"));
    }

    @Test
    @DisplayName("Debe buscar cuentas por número de documento")
    void testFindByNumeroDocumento() {
        // When
        List<CuentasPorPagarDetalladoTO> resultados = repository.findByNumeroDocumento("COMP-001");

        // Then
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getCxpNumeroDocumento()).isEqualTo("COMP-001");
    }

    @Test
    @DisplayName("Debe buscar cuentas por rango de fechas de emisión")
    void testFindByFechaEmisionBetween() {
        // When
        List<CuentasPorPagarDetalladoTO> resultados = repository.findByFechaEmisionBetween(
                "2024-01-01", "2024-01-31");

        // Then
        assertThat(resultados).hasSize(2);
    }

    @Test
    @DisplayName("Debe buscar cuentas con saldo mayor a un mínimo")
    void testFindBySaldoGreaterThan() {
        // When
        List<CuentasPorPagarDetalladoTO> resultados = repository.findBySaldoGreaterThan(new BigDecimal("1000"));

        // Then
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getCxpSaldo()).isGreaterThan(new BigDecimal("1000"));
    }

    @Test
    @DisplayName("Debe buscar cuentas ordenadas por fecha de emisión descendente")
    void testFindByFechaEmisionBetweenOrderByFechaEmisionDesc() {
        // When
        List<CuentasPorPagarDetalladoTO> resultados = repository.findByFechaEmisionBetweenOrderByFechaEmisionDesc(
                "2024-01-01", "2024-12-31");

        // Then
        assertThat(resultados).hasSize(3);
        assertThat(resultados.get(0).getCxpFechaEmision()).isEqualTo("2024-02-10");
        assertThat(resultados.get(1).getCxpFechaEmision()).isEqualTo("2024-01-20");
        assertThat(resultados.get(2).getCxpFechaEmision()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe buscar cuentas ordenadas por saldo descendente")
    void testFindBySaldoGreaterThanOrderBySaldoDesc() {
        // When
        List<CuentasPorPagarDetalladoTO> resultados = repository.findBySaldoGreaterThanOrderBySaldoDesc(
                new BigDecimal("0"));

        // Then
        assertThat(resultados).hasSize(3);
        assertThat(resultados.get(0).getCxpSaldo()).isGreaterThanOrEqualTo(resultados.get(1).getCxpSaldo());
        assertThat(resultados.get(1).getCxpSaldo()).isGreaterThanOrEqualTo(resultados.get(2).getCxpSaldo());
    }

    @Test
    @DisplayName("Debe buscar cuentas por proveedor ordenadas por fecha")
    void testFindByProveedorOrderByFechaEmisionDesc() {
        // When
        List<CuentasPorPagarDetalladoTO> resultados = repository.findByProveedorOrderByFechaEmisionDesc("PROV-001");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados.get(0).getCxpFechaEmision()).isEqualTo("2024-01-20");
        assertThat(resultados.get(1).getCxpFechaEmision()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe buscar cuentas vencidas")
    void testFindByFechaVencimientoLessThan() {
        // When
        List<CuentasPorPagarDetalladoTO> resultados = repository.findByFechaVencimientoLessThan("2024-02-18");

        // Then
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getCxpNumeroDocumento()).isEqualTo("COMP-001");
    }

    @Test
    @DisplayName("Debe contar cuentas por proveedor")
    void testCountByProveedor() {
        // When
        long count = repository.countByProveedor("PROV-001");

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar cuentas por periodo")
    void testCountByPeriodo() {
        // When
        long count = repository.countByPeriodo("2024-01");

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar cuentas con saldo mayor a un mínimo")
    void testCountBySaldoGreaterThan() {
        // When
        long count = repository.countBySaldoGreaterThan(new BigDecimal("1000"));

        // Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe hacer rollback de transacción")
    void testTransactionRollback() {
        // Given
        long countInicial = repository.count();

        // When
        try {
            CuentasPorPagarDetalladoTO nueva = createCuenta(99, "2024-12", "PROV-999", "COMPRA", "COMP-999",
                    "2024-12-31", "2025-01-31", new BigDecimal("1000"), new BigDecimal("1000"), new BigDecimal("0"));
            repository.save(nueva);
            throw new RuntimeException("Error simulado");
        } catch (RuntimeException e) {
            // Expected
        }

        // Then
        assertThat(repository.count()).isEqualTo(countInicial);
    }

    private CuentasPorPagarDetalladoTO createCuenta(Integer id, String periodo, String proveedor,
            String motivo, String numeroDoc, String fechaEmision, String fechaVencimiento,
            BigDecimal total, BigDecimal saldo, BigDecimal saldoVencido) {
        CuentasPorPagarDetalladoTO cuenta = new CuentasPorPagarDetalladoTO();
        cuenta.setId(id);
        cuenta.setCxpPeriodo(periodo);
        cuenta.setCxpProveedor(proveedor);
        cuenta.setCxpMotivo(motivo);
        cuenta.setCxpNumeroDocumento(numeroDoc);
        cuenta.setCxpFechaEmision(fechaEmision);
        cuenta.setCxpFechaVencimiento(fechaVencimiento);
        cuenta.setCxpTotal(total);
        cuenta.setCxpSaldo(saldo);
        cuenta.setCxpSaldoVencido(saldoVencido);
        return cuenta;
    }
}

package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.config.BaseIntegrationTest;
import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para CuentasPorCobrarJpaRepository.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-24
 */
@Transactional
@DisplayName("Pruebas de Integración - CuentasPorCobrarJpaRepository")
class CuentasPorCobrarJpaRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CuentasPorCobrarJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        // Crear datos de prueba
        CuentasPorCobrarDetalladoApiTO cuenta1 = createCuenta(1, "EMP001", "2024-01", "CLI-001", "FACTURA", 
                "FAC-001", "2024-01-15", "2024-02-15", new BigDecimal("1000"), new BigDecimal("500"));
        
        CuentasPorCobrarDetalladoApiTO cuenta2 = createCuenta(2, "EMP001", "2024-01", "CLI-001", "NOTA_DEBITO",
                "ND-001", "2024-01-20", "2024-02-20", new BigDecimal("500"), new BigDecimal("500"));
        
        CuentasPorCobrarDetalladoApiTO cuenta3 = createCuenta(3, "EMP001", "2024-02", "CLI-002", "FACTURA",
                "FAC-002", "2024-02-10", "2024-03-10", new BigDecimal("2000"), new BigDecimal("2000"));

        repository.save(cuenta1);
        repository.save(cuenta2);
        repository.save(cuenta3);
    }

    @Test
    @DisplayName("Debe buscar cuentas por empresa")
    void testFindByEmpresa() {
        // When
        List<CuentasPorCobrarDetalladoApiTO> resultados = repository.findByEmpresa("EMP001");

        // Then
        assertThat(resultados).hasSize(3);
        assertThat(resultados).allMatch(c -> c.getCxcEmpresa().equals("EMP001"));
    }

    @Test
    @DisplayName("Debe buscar cuentas por empresa y periodo")
    void testFindByEmpresaAndPeriodo() {
        // When
        List<CuentasPorCobrarDetalladoApiTO> resultados = repository.findByEmpresaAndPeriodo("EMP001", "2024-01");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(c -> c.getCxcPeriodo().equals("2024-01"));
    }

    @Test
    @DisplayName("Debe buscar cuentas por empresa y cliente")
    void testFindByEmpresaAndCliente() {
        // When
        List<CuentasPorCobrarDetalladoApiTO> resultados = repository.findByEmpresaAndCliente("EMP001", "CLI-001");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(c -> c.getCxcCliente().equals("CLI-001"));
    }

    @Test
    @DisplayName("Debe buscar cuentas por empresa y motivo")
    void testFindByEmpresaAndMotivo() {
        // When
        List<CuentasPorCobrarDetalladoApiTO> resultados = repository.findByEmpresaAndMotivo("EMP001", "FACTURA");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(c -> c.getCxcMotivo().equals("FACTURA"));
    }

    @Test
    @DisplayName("Debe buscar cuentas por rango de fechas de emisión")
    void testFindByEmpresaAndFechaEmisionBetween() {
        // When
        List<CuentasPorCobrarDetalladoApiTO> resultados = repository.findByEmpresaAndFechaEmisionBetween(
                "EMP001", "2024-01-01", "2024-01-31");

        // Then
        assertThat(resultados).hasSize(2);
    }

    @Test
    @DisplayName("Debe buscar cuentas con saldo mayor a un mínimo")
    void testFindByEmpresaAndSaldoGreaterThan() {
        // When
        List<CuentasPorCobrarDetalladoApiTO> resultados = repository.findByEmpresaAndSaldoGreaterThan(
                "EMP001", new BigDecimal("1000"));

        // Then
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getCxcSaldo()).isGreaterThan(new BigDecimal("1000"));
    }

    @Test
    @DisplayName("Debe buscar cuentas ordenadas por fecha de emisión descendente")
    void testFindByEmpresaOrderByFechaEmisionDesc() {
        // When
        List<CuentasPorCobrarDetalladoApiTO> resultados = repository.findByEmpresaOrderByFechaEmisionDesc("EMP001");

        // Then
        assertThat(resultados).hasSize(3);
        assertThat(resultados.get(0).getCxcFechaEmision()).isEqualTo("2024-02-10");
        assertThat(resultados.get(1).getCxcFechaEmision()).isEqualTo("2024-01-20");
        assertThat(resultados.get(2).getCxcFechaEmision()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe buscar cuentas vencidas")
    void testFindByEmpresaAndFechaVencimientoLessThan() {
        // When
        List<CuentasPorCobrarDetalladoApiTO> resultados = repository.findByEmpresaAndFechaVencimientoLessThan(
                "EMP001", "2024-02-18");

        // Then
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getCxcNumeroDocumento()).isEqualTo("FAC-001");
    }

    @Test
    @DisplayName("Debe contar cuentas por empresa")
    void testCountByEmpresa() {
        // When
        long count = repository.countByEmpresa("EMP001");

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("Debe contar cuentas por empresa y cliente")
    void testCountByEmpresaAndCliente() {
        // When
        long count = repository.countByEmpresaAndCliente("EMP001", "CLI-001");

        // Then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe hacer rollback de transacción")
    void testTransactionRollback() {
        // Given
        long countInicial = repository.count();

        // When
        try {
            CuentasPorCobrarDetalladoApiTO nueva = createCuenta(99, "EMP999", "2024-12", "CLI-999", "FACTURA",
                    "FAC-999", "2024-12-31", "2025-01-31", new BigDecimal("1000"), new BigDecimal("1000"));
            repository.save(nueva);
            throw new RuntimeException("Error simulado");
        } catch (RuntimeException e) {
            // Expected
        }

        // Then
        assertThat(repository.count()).isEqualTo(countInicial);
    }

    private CuentasPorCobrarDetalladoApiTO createCuenta(Integer id, String empresa, String periodo,
            String cliente, String motivo, String numeroDoc, String fechaEmision, String fechaVencimiento,
            BigDecimal total, BigDecimal saldo) {
        CuentasPorCobrarDetalladoApiTO cuenta = new CuentasPorCobrarDetalladoApiTO();
        cuenta.setId(id);
        cuenta.setCxcEmpresa(empresa);
        cuenta.setCxcPeriodo(periodo);
        cuenta.setCxcCliente(cliente);
        cuenta.setCxcMotivo(motivo);
        cuenta.setCxcNumeroDocumento(numeroDoc);
        cuenta.setCxcFechaEmision(fechaEmision);
        cuenta.setCxcFechaVencimiento(fechaVencimiento);
        cuenta.setCxcTotal(total);
        cuenta.setCxcSaldo(saldo);
        return cuenta;
    }
}

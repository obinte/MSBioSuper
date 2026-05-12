package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.config.BaseIntegrationTest;
import com.acosux.MSBioSuper.domain.model.PrdConsumosDiarioMSTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para ConsumosDiariosJpaRepository.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-24
 */
@Transactional
@DisplayName("Pruebas de Integración - ConsumosDiariosJpaRepository")
class ConsumosDiariosJpaRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ConsumosDiariosJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        // Crear datos de prueba
        PrdConsumosDiarioMSTO consumo1 = createConsumo(1, "EMP001", "SEC001", "PIS001", "COR001", "PROD001", "2024-01-15");
        PrdConsumosDiarioMSTO consumo2 = createConsumo(2, "EMP001", "SEC001", "PIS001", "COR001", "PROD002", "2024-01-16");
        PrdConsumosDiarioMSTO consumo3 = createConsumo(3, "EMP001", "SEC002", "PIS002", "COR002", "PROD001", "2024-02-10");

        repository.saveAndFlush(consumo1);
        repository.saveAndFlush(consumo2);
        repository.saveAndFlush(consumo3);
    }

    @Test
    @DisplayName("Debe buscar consumos por empresa")
    void testFindByEmpresa() {
        // When
        List<PrdConsumosDiarioMSTO> resultados = repository.findByEmpresa("EMP001");

        // Then
        assertThat(resultados).hasSize(3);
        assertThat(resultados).allMatch(c -> c.getEmpCodigo().equals("EMP001"));
    }

    @Test
    @DisplayName("Debe buscar consumos por empresa y piscina")
    void testFindByEmpresaAndPiscina() {
        // When
        List<PrdConsumosDiarioMSTO> resultados = repository.findByEmpresaAndPiscina("EMP001", "PIS001");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(c -> c.getPisNumero().equals("PIS001"));
    }

    @Test
    @DisplayName("Debe buscar consumos por rango de fechas")
    void testFindByEmpresaAndFechaBetween() {
        // When
        List<PrdConsumosDiarioMSTO> resultados = repository.findByEmpresaAndFechaBetween(
                "EMP001", "2024-01-01", "2024-01-31");

        // Then
        assertThat(resultados).hasSize(2);
    }

    @Test
    @DisplayName("Debe buscar consumos por empresa y producto")
    void testFindByEmpresaAndProducto() {
        // When
        List<PrdConsumosDiarioMSTO> resultados = repository.findByEmpresaAndProducto("EMP001", "PROD001");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(c -> c.getProCodigo().equals("PROD001"));
    }

    @Test
    @DisplayName("Debe buscar consumos ordenados por fecha descendente")
    void testFindByEmpresaOrderByFechaDesc() {
        // When
        List<PrdConsumosDiarioMSTO> resultados = repository.findByEmpresaOrderByFechaDesc("EMP001");

        // Then
        assertThat(resultados).hasSize(3);
        assertThat(resultados.get(0).getCdFecha()).isEqualTo("2024-02-10");
        assertThat(resultados.get(1).getCdFecha()).isEqualTo("2024-01-16");
        assertThat(resultados.get(2).getCdFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe contar consumos por empresa")
    void testCountByEmpresa() {
        // When
        long count = repository.countByEmpresa("EMP001");

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("Debe hacer rollback de transacción")
    void testTransactionRollback() {
        // Given
        long countInicial = repository.count();

        // When
        try {
            PrdConsumosDiarioMSTO nuevo = createConsumo(99, "EMP999", "SEC999", "PIS999", "COR999", "PROD999", "2024-12-31");
            repository.saveAndFlush(nuevo);
            throw new RuntimeException("Error simulado");
        } catch (RuntimeException e) {
            // Expected
        }

        // Then
        assertThat(repository.count()).isEqualTo(countInicial);
    }

    private PrdConsumosDiarioMSTO createConsumo(Integer id, String empresa, String sector,
            String piscina, String corrida, String producto, String fecha) {
        PrdConsumosDiarioMSTO consumo = new PrdConsumosDiarioMSTO();
        consumo.setId(id);
        consumo.setEmpCodigo(empresa);
        consumo.setSecCodigo(sector);
        consumo.setPisNumero(piscina);
        consumo.setCdCorrida(corrida);
        consumo.setProCodigo(producto);
        consumo.setCdFecha(fecha);
        consumo.setCdCantidad(new BigDecimal("50"));
        consumo.setCdCosto(new BigDecimal("125.50"));
        return consumo;
    }
}

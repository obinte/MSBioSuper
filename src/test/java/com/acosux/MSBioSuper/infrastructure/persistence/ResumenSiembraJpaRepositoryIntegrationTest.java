package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.config.BaseIntegrationTest;
import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para ResumenSiembraJpaRepository.
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-24
 */
@Transactional
@DisplayName("Pruebas de Integración - ResumenSiembraJpaRepository")
class ResumenSiembraJpaRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ResumenSiembraJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        // Crear datos de prueba
        PrdResumenCorridaTO resumen1 = createResumenSiembra(1, "EMP001", "SEC001", "PIS001", "COR001", "2024-01-10");
        PrdResumenCorridaTO resumen2 = createResumenSiembra(2, "EMP001", "SEC001", "PIS002", "COR002", "2024-02-15");
        PrdResumenCorridaTO resumen3 = createResumenSiembra(3, "EMP001", "SEC002", "PIS003", "COR003", "2024-03-20");

        repository.save(resumen1);
        repository.save(resumen2);
        repository.save(resumen3);
    }

    @Test
    @DisplayName("Debe buscar resúmenes de siembra por empresa")
    void testFindByEmpresa() {
        // When
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresa("EMP001");

        // Then
        assertThat(resultados).hasSize(3);
        assertThat(resultados).allMatch(r -> r.getEmpCodigo().equals("EMP001"));
    }

    @Test
    @DisplayName("Debe buscar resúmenes por empresa y sector")
    void testFindByEmpresaAndSector() {
        // When
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresaAndSector("EMP001", "SEC001");

        // Then
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(r -> 
                r.getEmpCodigo().equals("EMP001") && r.getSecCodigo().equals("SEC001"));
    }

    @Test
    @DisplayName("Debe buscar resúmenes por rango de fechas de siembra")
    void testFindByEmpresaAndFechaSiembraBetween() {
        // When
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresaAndFechaSiembraBetween(
                "EMP001", "2024-01-01", "2024-02-28");

        // Then
        assertThat(resultados).hasSize(2);
    }

    @Test
    @DisplayName("Debe buscar resúmenes ordenados por fecha de siembra descendente")
    void testFindByEmpresaOrderByFechaSiembraDesc() {
        // When
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresaOrderByFechaSiembraDesc("EMP001");

        // Then
        assertThat(resultados).hasSize(3);
        assertThat(resultados.get(0).getRcFechaSiembra()).isEqualTo("2024-03-20");
        assertThat(resultados.get(1).getRcFechaSiembra()).isEqualTo("2024-02-15");
        assertThat(resultados.get(2).getRcFechaSiembra()).isEqualTo("2024-01-10");
    }

    @Test
    @DisplayName("Debe contar resúmenes por empresa")
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
            PrdResumenCorridaTO nuevo = createResumenSiembra(99, "EMP999", "SEC999", "PIS999", "COR999", "2024-12-31");
            repository.save(nuevo);
            throw new RuntimeException("Error simulado");
        } catch (RuntimeException e) {
            // Expected
        }

        // Then
        assertThat(repository.count()).isEqualTo(countInicial);
    }

    private PrdResumenCorridaTO createResumenSiembra(Integer id, String empresa, String sector,
            String piscina, String corrida, String fechaSiembra) {
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();
        resumen.setId(id);
        resumen.setEmpCodigo(empresa);
        resumen.setSecCodigo(sector);
        resumen.setPisNumero(piscina);
        resumen.setRcCorridaNumero(corrida);
        resumen.setRcFechaSiembra(fechaSiembra);
        resumen.setRcHectareaje(new BigDecimal("2.5"));
        resumen.setRcDensidad(new BigDecimal("150000"));
        resumen.setRcNumeroLarvas(new BigDecimal("375000"));
        return resumen;
    }
}

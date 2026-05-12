package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.config.BaseIntegrationTest;
import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para ResumenPescaJpaRepository.
 * 
 * Estas pruebas utilizan Testcontainers con PostgreSQL real para validar:
 * - Operaciones CRUD básicas
 * - Métodos de consulta derivados
 * - Consultas personalizadas con @Query
 * - Paginación y ordenamiento
 * - Rollback de transacciones
 * 
 * @author Josias Rubén Aldaz Arcalles
 * @version 1.0
 * @since 2026-04-24
 */
@Transactional
@DisplayName("Pruebas de Integración - ResumenPescaJpaRepository")
class ResumenPescaJpaRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ResumenPescaJpaRepository repository;

    private PrdResumenCorridaTO resumenPesca1;
    private PrdResumenCorridaTO resumenPesca2;
    private PrdResumenCorridaTO resumenPesca3;

    @BeforeEach
    void setUp() {
        // Limpiar datos previos
        repository.deleteAll();

        // Arrange: Crear datos de prueba realistas
        resumenPesca1 = createResumenPesca(1, "EMP001", "SEC001", "PIS001", "COR001", 
                "2024-01-15", "2024-03-20", new BigDecimal("2.5"), new BigDecimal("5000"));
        
        resumenPesca2 = createResumenPesca(2, "EMP001", "SEC001", "PIS002", "COR002", 
                "2024-02-10", "2024-04-15", new BigDecimal("3.0"), new BigDecimal("6000"));
        
        resumenPesca3 = createResumenPesca(3, "EMP001", "SEC002", "PIS003", "COR003", 
                "2024-03-05", "2024-05-10", new BigDecimal("2.8"), new BigDecimal("5500"));

        // Guardar datos de prueba
        repository.save(resumenPesca1);
        repository.save(resumenPesca2);
        repository.save(resumenPesca3);
    }

    // ========== Pruebas de Operaciones CRUD Básicas ==========

    @Test
    @DisplayName("Debe guardar y recuperar un resumen de pesca por ID")
    void testSaveAndFindById() {
        // Given: Un nuevo resumen de pesca
        PrdResumenCorridaTO nuevoResumen = createResumenPesca(4, "EMP002", "SEC001", "PIS004", "COR004",
                "2024-04-01", "2024-06-05", new BigDecimal("3.2"), new BigDecimal("7000"));

        // When: Guardamos el resumen
        PrdResumenCorridaTO guardado = repository.save(nuevoResumen);

        // Then: Debe poder recuperarse por ID
        Optional<PrdResumenCorridaTO> recuperado = repository.findById(guardado.getId());
        
        assertThat(recuperado).isPresent();
        assertThat(recuperado.get().getEmpCodigo()).isEqualTo("EMP002");
        assertThat(recuperado.get().getPisNumero()).isEqualTo("PIS004");
        assertThat(recuperado.get().getRcCorridaNumero()).isEqualTo("COR004");
    }

    @Test
    @DisplayName("Debe recuperar todos los resúmenes de pesca")
    void testFindAll() {
        // When: Recuperamos todos los resúmenes
        List<PrdResumenCorridaTO> todos = repository.findAll();

        // Then: Debe retornar los 3 resúmenes creados en setUp
        assertThat(todos).hasSize(3);
        assertThat(todos).extracting(PrdResumenCorridaTO::getRcCorridaNumero)
                .containsExactlyInAnyOrder("COR001", "COR002", "COR003");
    }

    @Test
    @DisplayName("Debe eliminar un resumen de pesca")
    void testDelete() {
        // Given: Un resumen existente
        Integer id = resumenPesca1.getId();

        // When: Eliminamos el resumen
        repository.deleteById(id);

        // Then: No debe existir en la base de datos
        Optional<PrdResumenCorridaTO> eliminado = repository.findById(id);
        assertThat(eliminado).isEmpty();
        
        // Y debe quedar solo 2 resúmenes
        assertThat(repository.count()).isEqualTo(2);
    }

    // ========== Pruebas de Métodos de Consulta Derivados ==========

    @Test
    @DisplayName("Debe buscar resúmenes por código de empresa")
    void testFindByEmpresa() {
        // When: Buscamos por empresa
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresa("EMP001");

        // Then: Debe retornar los 3 resúmenes de EMP001
        assertThat(resultados).hasSize(3);
        assertThat(resultados).allMatch(r -> r.getEmpCodigo().equals("EMP001"));
    }

    @Test
    @DisplayName("Debe buscar resúmenes por empresa y sector")
    void testFindByEmpresaAndSector() {
        // When: Buscamos por empresa y sector
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresaAndSector("EMP001", "SEC001");

        // Then: Debe retornar solo los 2 resúmenes del sector SEC001
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(r -> 
                r.getEmpCodigo().equals("EMP001") && r.getSecCodigo().equals("SEC001"));
    }

    @Test
    @DisplayName("Debe buscar resúmenes por empresa y rango de fechas de pesca")
    void testFindByEmpresaAndFechaPescaBetween() {
        // When: Buscamos por rango de fechas
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresaAndFechaPescaBetween(
                "EMP001", "2024-03-01", "2024-04-30");

        // Then: Debe retornar solo los resúmenes dentro del rango
        assertThat(resultados).hasSize(2);
        assertThat(resultados).extracting(PrdResumenCorridaTO::getRcCorridaNumero)
                .containsExactlyInAnyOrder("COR001", "COR002");
    }

    @Test
    @DisplayName("Debe buscar resúmenes por empresa y corrida")
    void testFindByEmpresaAndCorrida() {
        // When: Buscamos por empresa y corrida
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresaAndCorrida("EMP001", "COR002");

        // Then: Debe retornar solo el resumen de la corrida COR002
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getRcCorridaNumero()).isEqualTo("COR002");
        assertThat(resultados.get(0).getPisNumero()).isEqualTo("PIS002");
    }

    @Test
    @DisplayName("Debe buscar resúmenes por empresa y piscina")
    void testFindByEmpresaAndPiscina() {
        // When: Buscamos por empresa y piscina
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresaAndPiscina("EMP001", "PIS001");

        // Then: Debe retornar solo el resumen de la piscina PIS001
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getPisNumero()).isEqualTo("PIS001");
        assertThat(resultados.get(0).getRcCorridaNumero()).isEqualTo("COR001");
    }

    @Test
    @DisplayName("Debe buscar resúmenes por empresa ordenados por fecha de pesca descendente")
    void testFindByEmpresaOrderByFechaPescaDesc() {
        // When: Buscamos ordenados por fecha descendente
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresaOrderByFechaPescaDesc("EMP001");

        // Then: Debe retornar los resúmenes ordenados de más reciente a más antiguo
        assertThat(resultados).hasSize(3);
        assertThat(resultados.get(0).getRcFechaPesca()).isEqualTo("2024-05-10");
        assertThat(resultados.get(1).getRcFechaPesca()).isEqualTo("2024-04-15");
        assertThat(resultados.get(2).getRcFechaPesca()).isEqualTo("2024-03-20");
    }

    @Test
    @DisplayName("Debe buscar resúmenes por empresa ordenados por corrida")
    void testFindByEmpresaOrderByCorrida() {
        // When: Buscamos ordenados por corrida
        List<PrdResumenCorridaTO> resultados = repository.findByEmpresaOrderByCorrida("EMP001");

        // Then: Debe retornar los resúmenes ordenados por corrida
        assertThat(resultados).hasSize(3);
        assertThat(resultados).extracting(PrdResumenCorridaTO::getRcCorridaNumero)
                .containsExactly("COR001", "COR002", "COR003");
    }

    @Test
    @DisplayName("Debe buscar resúmenes con filtros múltiples y ordenamiento")
    void testFindByEmpresaAndSectorAndFechaPescaBetweenOrderByFechaPescaDesc() {
        // When: Buscamos con múltiples filtros y ordenamiento
        List<PrdResumenCorridaTO> resultados = repository
                .findByEmpresaAndSectorAndFechaPescaBetweenOrderByFechaPescaDesc(
                        "EMP001", "SEC001", "2024-01-01", "2024-12-31");

        // Then: Debe retornar los resúmenes filtrados y ordenados
        assertThat(resultados).hasSize(2);
        assertThat(resultados.get(0).getRcFechaPesca()).isEqualTo("2024-04-15");
        assertThat(resultados.get(1).getRcFechaPesca()).isEqualTo("2024-03-20");
    }

    @Test
    @DisplayName("Debe contar resúmenes por empresa")
    void testCountByEmpresa() {
        // When: Contamos resúmenes por empresa
        long count = repository.countByEmpresa("EMP001");

        // Then: Debe retornar 3
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("Debe contar resúmenes por empresa y sector")
    void testCountByEmpresaAndSector() {
        // When: Contamos resúmenes por empresa y sector
        long count = repository.countByEmpresaAndSector("EMP001", "SEC001");

        // Then: Debe retornar 2
        assertThat(count).isEqualTo(2);
    }

    // ========== Pruebas de Paginación y Ordenamiento ==========

    @Test
    @DisplayName("Debe paginar resultados correctamente")
    void testPaginationAndSorting() {
        // Given: Creamos más datos para probar paginación
        for (int i = 4; i <= 10; i++) {
            PrdResumenCorridaTO resumen = createResumenPesca(i, "EMP001", "SEC001", "PIS00" + i, "COR00" + i,
                    "2024-0" + (i % 9 + 1) + "-01", "2024-0" + (i % 9 + 1) + "-28", 
                    new BigDecimal("2.5"), new BigDecimal("5000"));
            repository.save(resumen);
        }

        // When: Solicitamos la primera página con 5 elementos, ordenados por corrida
        var pageable = PageRequest.of(0, 5, Sort.by("rcCorridaNumero").ascending());
        var page = repository.findAll(pageable);

        // Then: Debe retornar la primera página correctamente
        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getContent().get(0).getRcCorridaNumero()).isEqualTo("COR001");
    }

    // ========== Pruebas de Rollback de Transacciones ==========

    @Test
    @DisplayName("Debe hacer rollback de transacción en caso de error")
    void testTransactionRollback() {
        // Given: Contamos los registros iniciales
        long countInicial = repository.count();

        // When: Intentamos guardar un registro y lanzamos una excepción
        try {
            PrdResumenCorridaTO nuevoResumen = createResumenPesca(99, "EMP999", "SEC999", "PIS999", "COR999",
                    "2024-12-01", "2024-12-31", new BigDecimal("1.0"), new BigDecimal("1000"));
            repository.save(nuevoResumen);
            
            // Simulamos un error que causa rollback
            throw new RuntimeException("Error simulado para probar rollback");
        } catch (RuntimeException e) {
            // Expected exception
        }

        // Then: El conteo debe ser el mismo (rollback automático por @Transactional)
        long countFinal = repository.count();
        assertThat(countFinal).isEqualTo(countInicial);
    }

    // ========== Métodos Auxiliares ==========

    /**
     * Crea un objeto PrdResumenCorridaTO con datos de prueba.
     */
    private PrdResumenCorridaTO createResumenPesca(Integer id, String empresa, String sector, 
            String piscina, String corrida, String fechaSiembra, String fechaPesca,
            BigDecimal hectareaje, BigDecimal biomasa) {
        
        PrdResumenCorridaTO resumen = new PrdResumenCorridaTO();
        resumen.setId(id);
        resumen.setEmpCodigo(empresa);
        resumen.setSecCodigo(sector);
        resumen.setPisNumero(piscina);
        resumen.setRcCorridaNumero(corrida);
        resumen.setRcFechaSiembra(fechaSiembra);
        resumen.setRcFechaPesca(fechaPesca);
        resumen.setRcHectareaje(hectareaje);
        resumen.setRcBiomasa(biomasa);
        resumen.setRcEdad(new BigDecimal("90"));
        resumen.setRcDensidad(new BigDecimal("150000"));
        resumen.setRcTPromedio(new BigDecimal("18.5"));
        resumen.setRcConversion(new BigDecimal("1.45"));
        resumen.setRcSobrevivencia("85%");
        
        return resumen;
    }
}

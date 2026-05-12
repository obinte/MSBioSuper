package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.config.BaseIntegrationTest;
import com.acosux.MSBioSuper.domain.model.PrdLiquidacionesDetalleTO;
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
 * Pruebas de integración para LiquidacionJpaRepository.
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
@DisplayName("Pruebas de Integración - LiquidacionJpaRepository")
class LiquidacionJpaRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private LiquidacionJpaRepository repository;

    private PrdLiquidacionesDetalleTO liquidacion1;
    private PrdLiquidacionesDetalleTO liquidacion2;
    private PrdLiquidacionesDetalleTO liquidacion3;

    @BeforeEach
    void setUp() {
        // Limpiar datos previos
        repository.deleteAll();

        // Arrange: Crear datos de prueba realistas
        liquidacion1 = createLiquidacion(1, "LIQ-001", "2024-01-15", "CLI-001", "LOTE-A", "COR-001",
                "ENTERO", "Enero", new BigDecimal("1000"), new BigDecimal("950"));
        
        liquidacion2 = createLiquidacion(2, "LIQ-002", "2024-02-20", "CLI-001", "LOTE-B", "COR-002",
                "COLA", "Febrero", new BigDecimal("1200"), new BigDecimal("1150"));
        
        liquidacion3 = createLiquidacion(3, "LIQ-003", "2024-03-10", "CLI-002", "LOTE-C", "COR-003",
                "ENTERO", "Marzo", new BigDecimal("800"), new BigDecimal("780"));

        // Guardar datos de prueba
        repository.save(liquidacion1);
        repository.save(liquidacion2);
        repository.save(liquidacion3);
    }

    // ========== Pruebas de Operaciones CRUD Básicas ==========

    @Test
    @DisplayName("Debe guardar y recuperar una liquidación por ID")
    void testSaveAndFindById() {
        // Given: Una nueva liquidación
        PrdLiquidacionesDetalleTO nuevaLiquidacion = createLiquidacion(4, "LIQ-004", "2024-04-05", 
                "CLI-003", "LOTE-D", "COR-004", "COLA", "Abril", 
                new BigDecimal("1500"), new BigDecimal("1450"));

        // When: Guardamos la liquidación
        PrdLiquidacionesDetalleTO guardada = repository.save(nuevaLiquidacion);

        // Then: Debe poder recuperarse por ID
        Optional<PrdLiquidacionesDetalleTO> recuperada = repository.findById(guardada.getId());
        
        assertThat(recuperada).isPresent();
        assertThat(recuperada.get().getLiqNumero()).isEqualTo("LIQ-004");
        assertThat(recuperada.get().getLiqCliente()).isEqualTo("CLI-003");
        assertThat(recuperada.get().getLiqLote()).isEqualTo("LOTE-D");
    }

    @Test
    @DisplayName("Debe recuperar todas las liquidaciones")
    void testFindAll() {
        // When: Recuperamos todas las liquidaciones
        List<PrdLiquidacionesDetalleTO> todas = repository.findAll();

        // Then: Debe retornar las 3 liquidaciones creadas en setUp
        assertThat(todas).hasSize(3);
        assertThat(todas).extracting(PrdLiquidacionesDetalleTO::getLiqNumero)
                .containsExactlyInAnyOrder("LIQ-001", "LIQ-002", "LIQ-003");
    }

    @Test
    @DisplayName("Debe actualizar una liquidación existente")
    void testUpdate() {
        // Given: Una liquidación existente
        liquidacion1.setLiqLibrasNetas(new BigDecimal("960"));
        liquidacion1.setLiqTotalMonto(new BigDecimal("2400"));

        // When: Actualizamos la liquidación
        PrdLiquidacionesDetalleTO actualizada = repository.save(liquidacion1);

        // Then: Los cambios deben persistirse
        assertThat(actualizada.getLiqLibrasNetas()).isEqualByComparingTo(new BigDecimal("960"));
        assertThat(actualizada.getLiqTotalMonto()).isEqualByComparingTo(new BigDecimal("2400"));
    }

    @Test
    @DisplayName("Debe eliminar una liquidación")
    void testDelete() {
        // Given: Una liquidación existente
        Integer id = liquidacion1.getId();

        // When: Eliminamos la liquidación
        repository.deleteById(id);

        // Then: No debe existir en la base de datos
        Optional<PrdLiquidacionesDetalleTO> eliminada = repository.findById(id);
        assertThat(eliminada).isEmpty();
        
        // Y debe quedar solo 2 liquidaciones
        assertThat(repository.count()).isEqualTo(2);
    }

    // ========== Pruebas de Métodos de Consulta Derivados ==========

    @Test
    @DisplayName("Debe buscar liquidaciones por número")
    void testFindByNumero() {
        // When: Buscamos por número de liquidación
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByNumero("LIQ-001");

        // Then: Debe retornar la liquidación correcta
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getLiqNumero()).isEqualTo("LIQ-001");
        assertThat(resultados.get(0).getLiqCliente()).isEqualTo("CLI-001");
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por cliente")
    void testFindByCliente() {
        // When: Buscamos por cliente
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByCliente("CLI-001");

        // Then: Debe retornar las 2 liquidaciones del cliente CLI-001
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(l -> l.getLiqCliente().equals("CLI-001"));
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por corrida")
    void testFindByCorrida() {
        // When: Buscamos por corrida
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByCorrida("COR-002");

        // Then: Debe retornar solo la liquidación de la corrida COR-002
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getLiqCorrida()).isEqualTo("COR-002");
        assertThat(resultados.get(0).getLiqNumero()).isEqualTo("LIQ-002");
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por rango de fechas")
    void testFindByFechaBetween() {
        // When: Buscamos por rango de fechas
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByFechaBetween(
                "2024-01-01", "2024-02-28");

        // Then: Debe retornar las liquidaciones dentro del rango
        assertThat(resultados).hasSize(2);
        assertThat(resultados).extracting(PrdLiquidacionesDetalleTO::getLiqNumero)
                .containsExactlyInAnyOrder("LIQ-001", "LIQ-002");
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por tipo")
    void testFindByTipo() {
        // When: Buscamos por tipo
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByTipo("ENTERO");

        // Then: Debe retornar las 2 liquidaciones de tipo ENTERO
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(l -> l.getLiqTipo().equals("ENTERO"));
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por mes")
    void testFindByMes() {
        // When: Buscamos por mes
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByMes("Febrero");

        // Then: Debe retornar solo la liquidación de febrero
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getLiqMes()).isEqualTo("Febrero");
        assertThat(resultados.get(0).getLiqNumero()).isEqualTo("LIQ-002");
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por cliente y rango de fechas")
    void testFindByClienteAndFechaBetween() {
        // When: Buscamos por cliente y rango de fechas
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByClienteAndFechaBetween(
                "CLI-001", "2024-01-01", "2024-12-31");

        // Then: Debe retornar las 2 liquidaciones del cliente en el rango
        assertThat(resultados).hasSize(2);
        assertThat(resultados).allMatch(l -> l.getLiqCliente().equals("CLI-001"));
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por lote")
    void testFindByLote() {
        // When: Buscamos por lote
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByLote("LOTE-A");

        // Then: Debe retornar solo la liquidación del lote LOTE-A
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getLiqLote()).isEqualTo("LOTE-A");
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por rango de fechas ordenadas descendente")
    void testFindByFechaBetweenOrderByFechaDesc() {
        // When: Buscamos por rango de fechas ordenadas
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByFechaBetweenOrderByFechaDesc(
                "2024-01-01", "2024-12-31");

        // Then: Debe retornar las liquidaciones ordenadas de más reciente a más antigua
        assertThat(resultados).hasSize(3);
        assertThat(resultados.get(0).getLiqFecha()).isEqualTo("2024-03-10");
        assertThat(resultados.get(1).getLiqFecha()).isEqualTo("2024-02-20");
        assertThat(resultados.get(2).getLiqFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por cliente ordenadas descendente")
    void testFindByClienteOrderByFechaDesc() {
        // When: Buscamos por cliente ordenadas
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByClienteOrderByFechaDesc("CLI-001");

        // Then: Debe retornar las liquidaciones ordenadas
        assertThat(resultados).hasSize(2);
        assertThat(resultados.get(0).getLiqFecha()).isEqualTo("2024-02-20");
        assertThat(resultados.get(1).getLiqFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por corrida ordenadas descendente")
    void testFindByCorridaOrderByFechaDesc() {
        // When: Buscamos por corrida ordenadas
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByCorridaOrderByFechaDesc("COR-001");

        // Then: Debe retornar la liquidación correcta
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getLiqCorrida()).isEqualTo("COR-001");
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por tipo y rango de fechas ordenadas ascendente")
    void testFindByTipoAndFechaBetweenOrderByFechaAsc() {
        // When: Buscamos por tipo y rango de fechas ordenadas
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByTipoAndFechaBetweenOrderByFechaAsc(
                "ENTERO", "2024-01-01", "2024-12-31");

        // Then: Debe retornar las liquidaciones ordenadas ascendente
        assertThat(resultados).hasSize(2);
        assertThat(resultados.get(0).getLiqFecha()).isEqualTo("2024-01-15");
        assertThat(resultados.get(1).getLiqFecha()).isEqualTo("2024-03-10");
    }

    @Test
    @DisplayName("Debe contar liquidaciones por cliente")
    void testCountByCliente() {
        // When: Contamos liquidaciones por cliente
        long count = repository.countByCliente("CLI-001");

        // Then: Debe retornar 2
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar liquidaciones por tipo")
    void testCountByTipo() {
        // When: Contamos liquidaciones por tipo
        long count = repository.countByTipo("ENTERO");

        // Then: Debe retornar 2
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe buscar liquidaciones por mes ordenadas ascendente")
    void testFindByMesOrderByFechaAsc() {
        // When: Buscamos por mes ordenadas
        List<PrdLiquidacionesDetalleTO> resultados = repository.findByMesOrderByFechaAsc("Enero");

        // Then: Debe retornar la liquidación de enero
        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getLiqMes()).isEqualTo("Enero");
    }

    // ========== Pruebas de Paginación y Ordenamiento ==========

    @Test
    @DisplayName("Debe paginar resultados correctamente")
    void testPaginationAndSorting() {
        // Given: Creamos más datos para probar paginación
        for (int i = 4; i <= 10; i++) {
            PrdLiquidacionesDetalleTO liquidacion = createLiquidacion(i, "LIQ-00" + i, 
                    "2024-0" + (i % 9 + 1) + "-01", "CLI-00" + (i % 3 + 1), "LOTE-" + (char)('A' + i),
                    "COR-00" + i, i % 2 == 0 ? "ENTERO" : "COLA", "Mes-" + i,
                    new BigDecimal("1000"), new BigDecimal("950"));
            repository.save(liquidacion);
        }

        // When: Solicitamos la primera página con 5 elementos, ordenados por número
        var pageable = PageRequest.of(0, 5, Sort.by("liqNumero").ascending());
        var page = repository.findAll(pageable);

        // Then: Debe retornar la primera página correctamente
        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(10);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getContent().get(0).getLiqNumero()).isEqualTo("LIQ-001");
    }

    // ========== Pruebas de Rollback de Transacciones ==========

    @Test
    @DisplayName("Debe hacer rollback de transacción en caso de error")
    void testTransactionRollback() {
        // Given: Contamos los registros iniciales
        long countInicial = repository.count();

        // When: Intentamos guardar un registro y lanzamos una excepción
        try {
            PrdLiquidacionesDetalleTO nuevaLiquidacion = createLiquidacion(99, "LIQ-999", "2024-12-31",
                    "CLI-999", "LOTE-Z", "COR-999", "ENTERO", "Diciembre",
                    new BigDecimal("1000"), new BigDecimal("950"));
            repository.save(nuevaLiquidacion);
            
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
     * Crea un objeto PrdLiquidacionesDetalleTO con datos de prueba.
     */
    private PrdLiquidacionesDetalleTO createLiquidacion(Integer id, String numero, String fecha,
            String cliente, String lote, String corrida, String tipo, String mes,
            BigDecimal librasEnviadas, BigDecimal librasRecibidas) {
        
        PrdLiquidacionesDetalleTO liquidacion = new PrdLiquidacionesDetalleTO();
        liquidacion.setId(id);
        liquidacion.setLiqNumero(numero);
        liquidacion.setLiqFecha(fecha);
        liquidacion.setLiqCliente(cliente);
        liquidacion.setLiqLote(lote);
        liquidacion.setLiqCorrida(corrida);
        liquidacion.setLiqTipo(tipo);
        liquidacion.setLiqMes(mes);
        liquidacion.setLiqLibrasEnviadas(librasEnviadas);
        liquidacion.setLiqLibrasRecibidas(librasRecibidas);
        liquidacion.setLiqDiferencia(librasEnviadas.subtract(librasRecibidas));
        liquidacion.setLiqLibrasBasura(new BigDecimal("10"));
        liquidacion.setLiqLibrasNetas(librasRecibidas.subtract(new BigDecimal("10")));
        liquidacion.setLiqLibrasEntero(new BigDecimal("800"));
        liquidacion.setLiqRendimiento(new BigDecimal("85.5"));
        liquidacion.setLiqTotalMonto(new BigDecimal("2500"));
        liquidacion.setLiqPrecioLibraPromedio(new BigDecimal("2.65"));
        liquidacion.setLiqGramaje(new BigDecimal("18.5"));
        liquidacion.setLiqIdCliente(cliente);
        
        return liquidacion;
    }
}

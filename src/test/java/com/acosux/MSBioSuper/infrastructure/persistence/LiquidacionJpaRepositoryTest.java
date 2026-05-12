package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.PrdLiquidacionesDetalleTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para LiquidacionJpaRepository.
 * 
 * Verifica el correcto funcionamiento de los métodos de consulta para liquidaciones,
 * incluyendo filtrado por número, cliente, corrida, tipo y fechas.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de LiquidacionJpaRepository")
class LiquidacionJpaRepositoryTest {

    @Autowired
    private LiquidacionJpaRepository liquidacionRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PrdLiquidacionesDetalleTO liquidacion1;
    private PrdLiquidacionesDetalleTO liquidacion2;
    private PrdLiquidacionesDetalleTO liquidacion3;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba
        liquidacion1 = new PrdLiquidacionesDetalleTO();
        liquidacion1.setNumero("LIQ-001");
        liquidacion1.setCliente("CLI001");
        liquidacion1.setCorrida("COR001");
        liquidacion1.setFecha("2024-01-15");
        liquidacion1.setTipo("VENTA");
        liquidacion1.setMes("202401");
        liquidacion1.setLote("LOTE001");

        liquidacion2 = new PrdLiquidacionesDetalleTO();
        liquidacion2.setNumero("LIQ-002");
        liquidacion2.setCliente("CLI002");
        liquidacion2.setCorrida("COR002");
        liquidacion2.setFecha("2024-01-20");
        liquidacion2.setTipo("VENTA");
        liquidacion2.setMes("202401");
        liquidacion2.setLote("LOTE002");

        liquidacion3 = new PrdLiquidacionesDetalleTO();
        liquidacion3.setNumero("LIQ-003");
        liquidacion3.setCliente("CLI001");
        liquidacion3.setCorrida("COR001");
        liquidacion3.setFecha("2024-02-05");
        liquidacion3.setTipo("COMPRA");
        liquidacion3.setMes("202402");
        liquidacion3.setLote("LOTE001");

        entityManager.persist(liquidacion1);
        entityManager.persist(liquidacion2);
        entityManager.persist(liquidacion3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar liquidaciones por número")
    void debeEncontrarLiquidacionesPorNumero() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = liquidacionRepository.findByNumero("LIQ-001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNumero()).isEqualTo("LIQ-001");
    }

    @Test
    @DisplayName("Debe encontrar liquidaciones por cliente")
    void debeEncontrarLiquidacionesPorCliente() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = liquidacionRepository.findByCliente("CLI001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdLiquidacionesDetalleTO::getCliente)
                .containsOnly("CLI001");
    }

    @Test
    @DisplayName("Debe encontrar liquidaciones por corrida")
    void debeEncontrarLiquidacionesPorCorrida() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = liquidacionRepository.findByCorrida("COR001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdLiquidacionesDetalleTO::getCorrida)
                .containsOnly("COR001");
    }

    @Test
    @DisplayName("Debe encontrar liquidaciones por rango de fechas")
    void debeEncontrarLiquidacionesPorRangoFechas() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = 
                liquidacionRepository.findByFechaBetween("2024-01-01", "2024-01-31");

        // Assert
        assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("Debe encontrar liquidaciones por tipo")
    void debeEncontrarLiquidacionesPorTipo() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = liquidacionRepository.findByTipo("VENTA");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdLiquidacionesDetalleTO::getTipo)
                .containsOnly("VENTA");
    }

    @Test
    @DisplayName("Debe encontrar liquidaciones por mes")
    void debeEncontrarLiquidacionesPorMes() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = liquidacionRepository.findByMes("202401");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdLiquidacionesDetalleTO::getMes)
                .containsOnly("202401");
    }

    @Test
    @DisplayName("Debe encontrar liquidaciones por lote")
    void debeEncontrarLiquidacionesPorLote() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = liquidacionRepository.findByLote("LOTE001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdLiquidacionesDetalleTO::getLote)
                .containsOnly("LOTE001");
    }

    @Test
    @DisplayName("Debe encontrar liquidaciones por cliente y rango de fechas")
    void debeEncontrarLiquidacionesPorClienteYRangoFechas() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = 
                liquidacionRepository.findByClienteAndFechaBetween("CLI001", "2024-01-01", "2024-01-31");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCliente()).isEqualTo("CLI001");
    }

    @Test
    @DisplayName("Debe ordenar liquidaciones por fecha descendente")
    void debeOrdenarLiquidacionesPorFechaDescendente() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = 
                liquidacionRepository.findByFechaBetweenOrderByFechaDesc("2024-01-01", "2024-02-28");

        // Assert
        assertThat(resultado).hasSize(3);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-02-05");
        assertThat(resultado.get(2).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe ordenar liquidaciones por cliente y fecha")
    void debeOrdenarLiquidacionesPorClienteYFecha() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = 
                liquidacionRepository.findByClienteOrderByFechaDesc("CLI001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-02-05");
    }

    @Test
    @DisplayName("Debe ordenar liquidaciones por corrida y fecha")
    void debeOrdenarLiquidacionesPorCorridaYFecha() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = 
                liquidacionRepository.findByCorridaOrderByFechaDesc("COR001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-02-05");
    }

    @Test
    @DisplayName("Debe encontrar liquidaciones por tipo y rango de fechas ordenadas")
    void debeEncontrarLiquidacionesPorTipoYRangoFechasOrdenadas() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = 
                liquidacionRepository.findByTipoAndFechaBetweenOrderByFechaAsc(
                        "VENTA", "2024-01-01", "2024-01-31");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe contar liquidaciones por cliente")
    void debeContarLiquidacionesPorCliente() {
        // Act
        long count = liquidacionRepository.countByCliente("CLI001");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar liquidaciones por tipo")
    void debeContarLiquidacionesPorTipo() {
        // Act
        long count = liquidacionRepository.countByTipo("VENTA");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe encontrar liquidaciones por mes ordenadas por fecha")
    void debeEncontrarLiquidacionesPorMesOrdenadasPorFecha() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = 
                liquidacionRepository.findByMesOrderByFechaAsc("202401");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay liquidaciones")
    void debeRetornarListaVaciaCuandoNoHayLiquidaciones() {
        // Act
        List<PrdLiquidacionesDetalleTO> resultado = liquidacionRepository.findByCliente("CLI999");

        // Assert
        assertThat(resultado).isEmpty();
    }
}

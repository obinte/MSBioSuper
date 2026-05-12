package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.PrdConsumosDiarioMSTO;
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
 * Pruebas unitarias para ConsumosDiariosJpaRepository.
 * 
 * Verifica el correcto funcionamiento de los métodos de consulta para consumos diarios,
 * incluyendo filtrado por empresa, sector, piscina, corrida, producto y rangos de fechas.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de ConsumosDiariosJpaRepository")
class ConsumosDiariosJpaRepositoryTest {

    @Autowired
    private ConsumosDiariosJpaRepository consumosRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PrdConsumosDiarioMSTO consumo1;
    private PrdConsumosDiarioMSTO consumo2;
    private PrdConsumosDiarioMSTO consumo3;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba
        consumo1 = new PrdConsumosDiarioMSTO();
        consumo1.setEmpresa("01");
        consumo1.setSector("SEC001");
        consumo1.setPiscina("PIS001");
        consumo1.setCorrida("COR001");
        consumo1.setProducto("PROD001");
        consumo1.setFecha("2024-01-15");

        consumo2 = new PrdConsumosDiarioMSTO();
        consumo2.setEmpresa("01");
        consumo2.setSector("SEC001");
        consumo2.setPiscina("PIS002");
        consumo2.setCorrida("COR002");
        consumo2.setProducto("PROD002");
        consumo2.setFecha("2024-01-20");

        consumo3 = new PrdConsumosDiarioMSTO();
        consumo3.setEmpresa("02");
        consumo3.setSector("SEC002");
        consumo3.setPiscina("PIS001");
        consumo3.setCorrida("COR001");
        consumo3.setProducto("PROD001");
        consumo3.setFecha("2024-01-25");

        entityManager.persist(consumo1);
        entityManager.persist(consumo2);
        entityManager.persist(consumo3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar consumos por empresa")
    void debeEncontrarConsumosPorEmpresa() {
        // Act
        List<PrdConsumosDiarioMSTO> resultado = consumosRepository.findByEmpresa("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdConsumosDiarioMSTO::getEmpresa)
                .containsOnly("01");
    }

    @Test
    @DisplayName("Debe encontrar consumos por empresa y sector")
    void debeEncontrarConsumosPorEmpresaYSector() {
        // Act
        List<PrdConsumosDiarioMSTO> resultado = 
                consumosRepository.findByEmpresaAndSector("01", "SEC001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdConsumosDiarioMSTO::getSector)
                .containsOnly("SEC001");
    }

    @Test
    @DisplayName("Debe encontrar consumos por empresa y piscina")
    void debeEncontrarConsumosPorEmpresaYPiscina() {
        // Act
        List<PrdConsumosDiarioMSTO> resultado = 
                consumosRepository.findByEmpresaAndPiscina("01", "PIS001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPiscina()).isEqualTo("PIS001");
    }

    @Test
    @DisplayName("Debe encontrar consumos por empresa y rango de fechas")
    void debeEncontrarConsumosPorEmpresaYRangoFechas() {
        // Act
        List<PrdConsumosDiarioMSTO> resultado = 
                consumosRepository.findByEmpresaAndFechaBetween("01", "2024-01-01", "2024-01-18");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe encontrar consumos por empresa y corrida")
    void debeEncontrarConsumosPorEmpresaYCorrida() {
        // Act
        List<PrdConsumosDiarioMSTO> resultado = 
                consumosRepository.findByEmpresaAndCorrida("01", "COR001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCorrida()).isEqualTo("COR001");
    }

    @Test
    @DisplayName("Debe encontrar consumos por empresa y producto")
    void debeEncontrarConsumosPorEmpresaYProducto() {
        // Act
        List<PrdConsumosDiarioMSTO> resultado = 
                consumosRepository.findByEmpresaAndProducto("01", "PROD001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProducto()).isEqualTo("PROD001");
    }

    @Test
    @DisplayName("Debe encontrar consumos por empresa, piscina y fecha")
    void debeEncontrarConsumosPorEmpresaPiscinaYFecha() {
        // Act
        List<PrdConsumosDiarioMSTO> resultado = 
                consumosRepository.findByEmpresaAndPiscinaAndFecha("01", "PIS001", "2024-01-15");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-15");
        assertThat(resultado.get(0).getPiscina()).isEqualTo("PIS001");
    }

    @Test
    @DisplayName("Debe ordenar consumos por fecha descendente")
    void debeOrdenarConsumosPorFechaDescendente() {
        // Act
        List<PrdConsumosDiarioMSTO> resultado = 
                consumosRepository.findByEmpresaOrderByFechaDesc("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-20");
        assertThat(resultado.get(1).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe ordenar consumos por piscina y fecha ascendente")
    void debeOrdenarConsumosPorPiscinaYFechaAscendente() {
        // Arrange - Agregar otro consumo para la misma piscina
        PrdConsumosDiarioMSTO consumo4 = new PrdConsumosDiarioMSTO();
        consumo4.setEmpresa("01");
        consumo4.setPiscina("PIS001");
        consumo4.setFecha("2024-01-10");
        entityManager.persist(consumo4);
        entityManager.flush();

        // Act
        List<PrdConsumosDiarioMSTO> resultado = 
                consumosRepository.findByEmpresaAndPiscinaOrderByFechaAsc("01", "PIS001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-10");
        assertThat(resultado.get(1).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe encontrar consumos por piscina y rango de fechas ordenados")
    void debeEncontrarConsumosPorPiscinaYRangoFechasOrdenados() {
        // Arrange
        PrdConsumosDiarioMSTO consumo4 = new PrdConsumosDiarioMSTO();
        consumo4.setEmpresa("01");
        consumo4.setPiscina("PIS001");
        consumo4.setFecha("2024-01-12");
        entityManager.persist(consumo4);
        entityManager.flush();

        // Act
        List<PrdConsumosDiarioMSTO> resultado = 
                consumosRepository.findByEmpresaAndPiscinaAndFechaBetweenOrderByFechaAsc(
                        "01", "PIS001", "2024-01-01", "2024-01-20");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-12");
        assertThat(resultado.get(1).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe contar consumos por empresa")
    void debeContarConsumosPorEmpresa() {
        // Act
        long count = consumosRepository.countByEmpresa("01");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar consumos por empresa y piscina")
    void debeContarConsumosPorEmpresaYPiscina() {
        // Act
        long count = consumosRepository.countByEmpresaAndPiscina("01", "PIS001");

        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe encontrar consumos por producto ordenados por fecha")
    void debeEncontrarConsumosPorProductoOrdenadosPorFecha() {
        // Arrange
        PrdConsumosDiarioMSTO consumo4 = new PrdConsumosDiarioMSTO();
        consumo4.setEmpresa("01");
        consumo4.setProducto("PROD001");
        consumo4.setFecha("2024-01-30");
        entityManager.persist(consumo4);
        entityManager.flush();

        // Act
        List<PrdConsumosDiarioMSTO> resultado = 
                consumosRepository.findByEmpresaAndProductoOrderByFechaDesc("01", "PROD001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-30");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay consumos")
    void debeRetornarListaVaciaCuandoNoHayConsumos() {
        // Act
        List<PrdConsumosDiarioMSTO> resultado = consumosRepository.findByEmpresa("99");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar cero cuando no hay consumos para contar")
    void debeRetornarCeroCuandoNoHayConsumosParaContar() {
        // Act
        long count = consumosRepository.countByEmpresa("99");

        // Assert
        assertThat(count).isZero();
    }
}

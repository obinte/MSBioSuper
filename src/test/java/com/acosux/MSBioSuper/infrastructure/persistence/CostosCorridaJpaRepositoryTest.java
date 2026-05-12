package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.PrdListaCostosDetalleCorridaTO;
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
 * Pruebas unitarias para CostosCorridaJpaRepository.
 * 
 * Verifica el correcto funcionamiento de los métodos de consulta para costos de corrida,
 * incluyendo filtrado por empresa, sector, piscina, corrida y producto.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de CostosCorridaJpaRepository")
class CostosCorridaJpaRepositoryTest {

    @Autowired
    private CostosCorridaJpaRepository costosRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PrdListaCostosDetalleCorridaTO costo1;
    private PrdListaCostosDetalleCorridaTO costo2;
    private PrdListaCostosDetalleCorridaTO costo3;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba
        costo1 = new PrdListaCostosDetalleCorridaTO();
        costo1.setEmpresa("01");
        costo1.setSector("SEC001");
        costo1.setPiscina("PIS001");
        costo1.setCorrida("COR001");
        costo1.setProducto("PROD001");
        costo1.setTotal(new BigDecimal("5000.00"));

        costo2 = new PrdListaCostosDetalleCorridaTO();
        costo2.setEmpresa("01");
        costo2.setSector("SEC001");
        costo2.setPiscina("PIS002");
        costo2.setCorrida("COR002");
        costo2.setProducto("PROD002");
        costo2.setTotal(new BigDecimal("3000.00"));

        costo3 = new PrdListaCostosDetalleCorridaTO();
        costo3.setEmpresa("02");
        costo3.setSector("SEC002");
        costo3.setPiscina("PIS001");
        costo3.setCorrida("COR001");
        costo3.setProducto("PROD001");
        costo3.setTotal(new BigDecimal("7000.00"));

        entityManager.persist(costo1);
        entityManager.persist(costo2);
        entityManager.persist(costo3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar costos por empresa")
    void debeEncontrarCostosPorEmpresa() {
        // Act
        List<PrdListaCostosDetalleCorridaTO> resultado = costosRepository.findByEmpresa("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdListaCostosDetalleCorridaTO::getEmpresa)
                .containsOnly("01");
    }

    @Test
    @DisplayName("Debe encontrar costos por empresa y sector")
    void debeEncontrarCostosPorEmpresaYSector() {
        // Act
        List<PrdListaCostosDetalleCorridaTO> resultado = 
                costosRepository.findByEmpresaAndSector("01", "SEC001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdListaCostosDetalleCorridaTO::getSector)
                .containsOnly("SEC001");
    }

    @Test
    @DisplayName("Debe encontrar costos por empresa y piscina")
    void debeEncontrarCostosPorEmpresaYPiscina() {
        // Act
        List<PrdListaCostosDetalleCorridaTO> resultado = 
                costosRepository.findByEmpresaAndPiscina("01", "PIS001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPiscina()).isEqualTo("PIS001");
    }

    @Test
    @DisplayName("Debe encontrar costos por empresa y corrida")
    void debeEncontrarCostosPorEmpresaYCorrida() {
        // Act
        List<PrdListaCostosDetalleCorridaTO> resultado = 
                costosRepository.findByEmpresaAndCorrida("01", "COR001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCorrida()).isEqualTo("COR001");
    }

    @Test
    @DisplayName("Debe encontrar costos por empresa y producto")
    void debeEncontrarCostosPorEmpresaYProducto() {
        // Act
        List<PrdListaCostosDetalleCorridaTO> resultado = 
                costosRepository.findByEmpresaAndProducto("01", "PROD001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProducto()).isEqualTo("PROD001");
    }

    @Test
    @DisplayName("Debe encontrar costos por empresa, sector y piscina")
    void debeEncontrarCostosPorEmpresaSectorYPiscina() {
        // Act
        List<PrdListaCostosDetalleCorridaTO> resultado = 
                costosRepository.findByEmpresaAndSectorAndPiscina("01", "SEC001", "PIS001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getSector()).isEqualTo("SEC001");
        assertThat(resultado.get(0).getPiscina()).isEqualTo("PIS001");
    }

    @Test
    @DisplayName("Debe encontrar costos por empresa, piscina y corrida")
    void debeEncontrarCostosPorEmpresaPiscinaYCorrida() {
        // Act
        List<PrdListaCostosDetalleCorridaTO> resultado = 
                costosRepository.findByEmpresaAndPiscinaAndCorrida("01", "PIS001", "COR001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPiscina()).isEqualTo("PIS001");
        assertThat(resultado.get(0).getCorrida()).isEqualTo("COR001");
    }

    @Test
    @DisplayName("Debe ordenar costos por total descendente")
    void debeOrdenarCostosPorTotalDescendente() {
        // Act
        List<PrdListaCostosDetalleCorridaTO> resultado = 
                costosRepository.findByEmpresaOrderByTotalDesc("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getTotal()).isEqualTo(new BigDecimal("5000.00"));
        assertThat(resultado.get(1).getTotal()).isEqualTo(new BigDecimal("3000.00"));
    }

    @Test
    @DisplayName("Debe contar costos por empresa")
    void debeContarCostosPorEmpresa() {
        // Act
        long count = costosRepository.countByEmpresa("01");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar costos por empresa y corrida")
    void debeContarCostosPorEmpresaYCorrida() {
        // Act
        long count = costosRepository.countByEmpresaAndCorrida("01", "COR001");

        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay costos")
    void debeRetornarListaVaciaCuandoNoHayCostos() {
        // Act
        List<PrdListaCostosDetalleCorridaTO> resultado = costosRepository.findByEmpresa("99");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar cero cuando no hay costos para contar")
    void debeRetornarCeroCuandoNoHayCostosParaContar() {
        // Act
        long count = costosRepository.countByEmpresa("99");

        // Assert
        assertThat(count).isZero();
    }

    @Test
    @DisplayName("Debe manejar múltiples costos con el mismo total")
    void debeManejarMultiplesCostosConMismoTotal() {
        // Arrange
        PrdListaCostosDetalleCorridaTO costo4 = new PrdListaCostosDetalleCorridaTO();
        costo4.setEmpresa("01");
        costo4.setSector("SEC001");
        costo4.setPiscina("PIS003");
        costo4.setCorrida("COR003");
        costo4.setProducto("PROD003");
        costo4.setTotal(new BigDecimal("5000.00"));
        entityManager.persist(costo4);
        entityManager.flush();

        // Act
        List<PrdListaCostosDetalleCorridaTO> resultado = 
                costosRepository.findByEmpresaOrderByTotalDesc("01");

        // Assert
        assertThat(resultado).hasSize(3);
        assertThat(resultado.get(0).getTotal()).isEqualTo(new BigDecimal("5000.00"));
        assertThat(resultado.get(1).getTotal()).isEqualTo(new BigDecimal("5000.00"));
    }
}

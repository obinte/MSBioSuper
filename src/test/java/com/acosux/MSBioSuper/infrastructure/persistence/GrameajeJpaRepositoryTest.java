package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.PrdListadoGrameajeTO;
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
 * Pruebas unitarias para GrameajeJpaRepository.
 * 
 * Verifica el correcto funcionamiento de los métodos de consulta para grameajes,
 * incluyendo filtrado por empresa, sector, piscina, corrida y fechas.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de GrameajeJpaRepository")
class GrameajeJpaRepositoryTest {

    @Autowired
    private GrameajeJpaRepository grameajeRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PrdListadoGrameajeTO grameaje1;
    private PrdListadoGrameajeTO grameaje2;
    private PrdListadoGrameajeTO grameaje3;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba
        grameaje1 = new PrdListadoGrameajeTO();
        grameaje1.setEmpresa("01");
        grameaje1.setSector("SEC001");
        grameaje1.setPiscina("PIS001");
        grameaje1.setCorrida("COR001");
        grameaje1.setFecha("2024-01-15");

        grameaje2 = new PrdListadoGrameajeTO();
        grameaje2.setEmpresa("01");
        grameaje2.setSector("SEC001");
        grameaje2.setPiscina("PIS002");
        grameaje2.setCorrida("COR002");
        grameaje2.setFecha("2024-01-20");

        grameaje3 = new PrdListadoGrameajeTO();
        grameaje3.setEmpresa("02");
        grameaje3.setSector("SEC002");
        grameaje3.setPiscina("PIS001");
        grameaje3.setCorrida("COR001");
        grameaje3.setFecha("2024-01-25");

        entityManager.persist(grameaje1);
        entityManager.persist(grameaje2);
        entityManager.persist(grameaje3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar grameajes por empresa")
    void debeEncontrarGrameajesPorEmpresa() {
        // Act
        List<PrdListadoGrameajeTO> resultado = grameajeRepository.findByEmpresa("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdListadoGrameajeTO::getEmpresa)
                .containsOnly("01");
    }

    @Test
    @DisplayName("Debe encontrar grameajes por empresa y sector")
    void debeEncontrarGrameajesPorEmpresaYSector() {
        // Act
        List<PrdListadoGrameajeTO> resultado = 
                grameajeRepository.findByEmpresaAndSector("01", "SEC001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdListadoGrameajeTO::getSector)
                .containsOnly("SEC001");
    }

    @Test
    @DisplayName("Debe encontrar grameajes por empresa y piscina")
    void debeEncontrarGrameajesPorEmpresaYPiscina() {
        // Act
        List<PrdListadoGrameajeTO> resultado = 
                grameajeRepository.findByEmpresaAndPiscina("01", "PIS001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPiscina()).isEqualTo("PIS001");
    }

    @Test
    @DisplayName("Debe encontrar grameajes por empresa y corrida")
    void debeEncontrarGrameajesPorEmpresaYCorrida() {
        // Act
        List<PrdListadoGrameajeTO> resultado = 
                grameajeRepository.findByEmpresaAndCorrida("01", "COR001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCorrida()).isEqualTo("COR001");
    }

    @Test
    @DisplayName("Debe encontrar grameajes por empresa y rango de fechas")
    void debeEncontrarGrameajesPorEmpresaYRangoFechas() {
        // Act
        List<PrdListadoGrameajeTO> resultado = 
                grameajeRepository.findByEmpresaAndFechaBetween("01", "2024-01-01", "2024-01-18");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe encontrar grameajes por empresa, sector y piscina")
    void debeEncontrarGrameajesPorEmpresaSectorYPiscina() {
        // Act
        List<PrdListadoGrameajeTO> resultado = 
                grameajeRepository.findByEmpresaAndSectorAndPiscina("01", "SEC001", "PIS001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getSector()).isEqualTo("SEC001");
        assertThat(resultado.get(0).getPiscina()).isEqualTo("PIS001");
    }

    @Test
    @DisplayName("Debe ordenar grameajes por piscina y fecha ascendente")
    void debeOrdenarGrameajesPorPiscinaYFechaAscendente() {
        // Arrange
        PrdListadoGrameajeTO grameaje4 = new PrdListadoGrameajeTO();
        grameaje4.setEmpresa("01");
        grameaje4.setPiscina("PIS001");
        grameaje4.setFecha("2024-01-10");
        entityManager.persist(grameaje4);
        entityManager.flush();

        // Act
        List<PrdListadoGrameajeTO> resultado = 
                grameajeRepository.findByEmpresaAndPiscinaOrderByFechaAsc("01", "PIS001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-10");
        assertThat(resultado.get(1).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe ordenar grameajes por fecha descendente")
    void debeOrdenarGrameajesPorFechaDescendente() {
        // Act
        List<PrdListadoGrameajeTO> resultado = 
                grameajeRepository.findByEmpresaOrderByFechaDesc("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-20");
        assertThat(resultado.get(1).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe encontrar grameajes por piscina y rango de fechas ordenados")
    void debeEncontrarGrameajesPorPiscinaYRangoFechasOrdenados() {
        // Arrange
        PrdListadoGrameajeTO grameaje4 = new PrdListadoGrameajeTO();
        grameaje4.setEmpresa("01");
        grameaje4.setPiscina("PIS001");
        grameaje4.setFecha("2024-01-12");
        entityManager.persist(grameaje4);
        entityManager.flush();

        // Act
        List<PrdListadoGrameajeTO> resultado = 
                grameajeRepository.findByEmpresaAndPiscinaAndFechaBetweenOrderByFechaAsc(
                        "01", "PIS001", "2024-01-01", "2024-01-20");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-12");
        assertThat(resultado.get(1).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe contar grameajes por empresa")
    void debeContarGrameajesPorEmpresa() {
        // Act
        long count = grameajeRepository.countByEmpresa("01");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar grameajes por empresa y piscina")
    void debeContarGrameajesPorEmpresaYPiscina() {
        // Act
        long count = grameajeRepository.countByEmpresaAndPiscina("01", "PIS001");

        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe contar grameajes por empresa y corrida")
    void debeContarGrameajesPorEmpresaYCorrida() {
        // Act
        long count = grameajeRepository.countByEmpresaAndCorrida("01", "COR001");

        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay grameajes")
    void debeRetornarListaVaciaCuandoNoHayGrameajes() {
        // Act
        List<PrdListadoGrameajeTO> resultado = grameajeRepository.findByEmpresa("99");

        // Assert
        assertThat(resultado).isEmpty();
    }
}

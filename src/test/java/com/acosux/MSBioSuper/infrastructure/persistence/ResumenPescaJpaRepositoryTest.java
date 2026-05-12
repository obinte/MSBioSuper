package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
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
 * Pruebas unitarias para ResumenPescaJpaRepository.
 * 
 * Verifica el correcto funcionamiento de los métodos de consulta para resumen de pesca,
 * incluyendo filtrado por empresa, sector, corrida, piscina y fechas de pesca.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de ResumenPescaJpaRepository")
class ResumenPescaJpaRepositoryTest {

    @Autowired
    private ResumenPescaJpaRepository resumenRepository;

    @Autowired
    private TestEntityManager entityManager;

    private PrdResumenCorridaTO resumen1;
    private PrdResumenCorridaTO resumen2;
    private PrdResumenCorridaTO resumen3;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba
        resumen1 = new PrdResumenCorridaTO();
        resumen1.setEmpresa("01");
        resumen1.setSector("SEC001");
        resumen1.setCorrida("COR001");
        resumen1.setPiscina("PIS001");
        resumen1.setFechaPesca("2024-01-15");

        resumen2 = new PrdResumenCorridaTO();
        resumen2.setEmpresa("01");
        resumen2.setSector("SEC001");
        resumen2.setCorrida("COR002");
        resumen2.setPiscina("PIS002");
        resumen2.setFechaPesca("2024-01-20");

        resumen3 = new PrdResumenCorridaTO();
        resumen3.setEmpresa("02");
        resumen3.setSector("SEC002");
        resumen3.setCorrida("COR001");
        resumen3.setPiscina("PIS001");
        resumen3.setFechaPesca("2024-01-25");

        entityManager.persist(resumen1);
        entityManager.persist(resumen2);
        entityManager.persist(resumen3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar resúmenes por empresa")
    void debeEncontrarResumenesPorEmpresa() {
        // Act
        List<PrdResumenCorridaTO> resultado = resumenRepository.findByEmpresa("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdResumenCorridaTO::getEmpresa)
                .containsOnly("01");
    }

    @Test
    @DisplayName("Debe encontrar resúmenes por empresa y sector")
    void debeEncontrarResumenesPorEmpresaYSector() {
        // Act
        List<PrdResumenCorridaTO> resultado = 
                resumenRepository.findByEmpresaAndSector("01", "SEC001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(PrdResumenCorridaTO::getSector)
                .containsOnly("SEC001");
    }

    @Test
    @DisplayName("Debe encontrar resúmenes por empresa y rango de fechas de pesca")
    void debeEncontrarResumenesPorEmpresaYRangoFechasPesca() {
        // Act
        List<PrdResumenCorridaTO> resultado = 
                resumenRepository.findByEmpresaAndFechaPescaBetween("01", "2024-01-01", "2024-01-18");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFechaPesca()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe encontrar resúmenes por empresa y corrida")
    void debeEncontrarResumenesPorEmpresaYCorrida() {
        // Act
        List<PrdResumenCorridaTO> resultado = 
                resumenRepository.findByEmpresaAndCorrida("01", "COR001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCorrida()).isEqualTo("COR001");
    }

    @Test
    @DisplayName("Debe encontrar resúmenes por empresa y piscina")
    void debeEncontrarResumenesPorEmpresaYPiscina() {
        // Act
        List<PrdResumenCorridaTO> resultado = 
                resumenRepository.findByEmpresaAndPiscina("01", "PIS001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPiscina()).isEqualTo("PIS001");
    }

    @Test
    @DisplayName("Debe ordenar resúmenes por fecha de pesca descendente")
    void debeOrdenarResumenesPorFechaPescaDescendente() {
        // Act
        List<PrdResumenCorridaTO> resultado = 
                resumenRepository.findByEmpresaOrderByFechaPescaDesc("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFechaPesca()).isEqualTo("2024-01-20");
        assertThat(resultado.get(1).getFechaPesca()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe ordenar resúmenes por corrida")
    void debeOrdenarResumenesPorCorrida() {
        // Act
        List<PrdResumenCorridaTO> resultado = 
                resumenRepository.findByEmpresaOrderByCorrida("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getCorrida()).isEqualTo("COR001");
        assertThat(resultado.get(1).getCorrida()).isEqualTo("COR002");
    }

    @Test
    @DisplayName("Debe encontrar resúmenes por empresa, sector y rango de fechas ordenados")
    void debeEncontrarResumenesPorEmpresaSectorYRangoFechasOrdenados() {
        // Act
        List<PrdResumenCorridaTO> resultado = 
                resumenRepository.findByEmpresaAndSectorAndFechaPescaBetweenOrderByFechaPescaDesc(
                        "01", "SEC001", "2024-01-01", "2024-01-31");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFechaPesca()).isEqualTo("2024-01-20");
    }

    @Test
    @DisplayName("Debe contar resúmenes por empresa")
    void debeContarResumenesPorEmpresa() {
        // Act
        long count = resumenRepository.countByEmpresa("01");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar resúmenes por empresa y sector")
    void debeContarResumenesPorEmpresaYSector() {
        // Act
        long count = resumenRepository.countByEmpresaAndSector("01", "SEC001");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay resúmenes")
    void debeRetornarListaVaciaCuandoNoHayResumenes() {
        // Act
        List<PrdResumenCorridaTO> resultado = resumenRepository.findByEmpresa("99");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar cero cuando no hay resúmenes para contar")
    void debeRetornarCeroCuandoNoHayResumenesParaContar() {
        // Act
        long count = resumenRepository.countByEmpresa("99");

        // Assert
        assertThat(count).isZero();
    }
}

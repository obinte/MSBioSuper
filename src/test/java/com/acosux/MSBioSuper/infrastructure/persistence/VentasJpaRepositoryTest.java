package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.InvVentasBioSuperTO;
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
 * Pruebas unitarias para VentasJpaRepository.
 * 
 * Verifica el correcto funcionamiento de los métodos de consulta para ventas,
 * incluyendo filtrado por empresa, cliente, periodo, tipo de documento y estado de autorización.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de VentasJpaRepository")
class VentasJpaRepositoryTest {

    @Autowired
    private VentasJpaRepository ventasRepository;

    @Autowired
    private TestEntityManager entityManager;

    private InvVentasBioSuperTO venta1;
    private InvVentasBioSuperTO venta2;
    private InvVentasBioSuperTO venta3;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba
        venta1 = new InvVentasBioSuperTO();
        venta1.setEmpresa("01");
        venta1.setPeriodo("202401");
        venta1.setMotivo("VENTA");
        venta1.setNumeroDocumento("FAC-001");
        venta1.setCliente("CLI001");
        venta1.setFecha("2024-01-15");
        venta1.setTipoDocumento("FACTURA");
        venta1.setAutorizado(true);
        venta1.setTotal(new BigDecimal("1000.00"));

        venta2 = new InvVentasBioSuperTO();
        venta2.setEmpresa("01");
        venta2.setPeriodo("202401");
        venta2.setMotivo("VENTA");
        venta2.setNumeroDocumento("FAC-002");
        venta2.setCliente("CLI002");
        venta2.setFecha("2024-01-20");
        venta2.setTipoDocumento("FACTURA");
        venta2.setAutorizado(true);
        venta2.setTotal(new BigDecimal("2000.00"));

        venta3 = new InvVentasBioSuperTO();
        venta3.setEmpresa("02");
        venta3.setPeriodo("202401");
        venta3.setMotivo("VENTA");
        venta3.setNumeroDocumento("FAC-003");
        venta3.setCliente("CLI001");
        venta3.setFecha("2024-01-25");
        venta3.setTipoDocumento("NOTA_VENTA");
        venta3.setAutorizado(false);
        venta3.setTotal(new BigDecimal("500.00"));

        entityManager.persist(venta1);
        entityManager.persist(venta2);
        entityManager.persist(venta3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar ventas por empresa")
    void debeEncontrarVentasPorEmpresa() {
        // Act
        List<InvVentasBioSuperTO> resultado = ventasRepository.findByEmpresa("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvVentasBioSuperTO::getEmpresa)
                .containsOnly("01");
    }

    @Test
    @DisplayName("Debe encontrar ventas por empresa y periodo")
    void debeEncontrarVentasPorEmpresaYPeriodo() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndPeriodo("01", "202401");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvVentasBioSuperTO::getPeriodo)
                .containsOnly("202401");
    }

    @Test
    @DisplayName("Debe encontrar ventas por empresa y motivo")
    void debeEncontrarVentasPorEmpresaYMotivo() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndMotivo("01", "VENTA");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvVentasBioSuperTO::getMotivo)
                .containsOnly("VENTA");
    }

    @Test
    @DisplayName("Debe encontrar ventas por empresa y número de documento")
    void debeEncontrarVentasPorEmpresaYNumeroDocumento() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndNumeroDocumento("01", "FAC-001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNumeroDocumento()).isEqualTo("FAC-001");
    }

    @Test
    @DisplayName("Debe encontrar ventas por empresa y cliente")
    void debeEncontrarVentasPorEmpresaYCliente() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndCliente("01", "CLI001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCliente()).isEqualTo("CLI001");
    }

    @Test
    @DisplayName("Debe encontrar ventas por empresa y rango de fechas")
    void debeEncontrarVentasPorEmpresaYRangoFechas() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndFechaBetween("01", "2024-01-01", "2024-01-18");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe encontrar ventas por empresa y tipo de documento")
    void debeEncontrarVentasPorEmpresaYTipoDocumento() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndTipoDocumento("01", "FACTURA");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvVentasBioSuperTO::getTipoDocumento)
                .containsOnly("FACTURA");
    }

    @Test
    @DisplayName("Debe encontrar ventas por empresa y estado de autorización")
    void debeEncontrarVentasPorEmpresaYEstadoAutorizacion() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndAutorizado("01", true);

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(InvVentasBioSuperTO::getAutorizado);
    }

    @Test
    @DisplayName("Debe ordenar ventas por fecha descendente")
    void debeOrdenarVentasPorFechaDescendente() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaOrderByFechaDesc("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-20");
        assertThat(resultado.get(1).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe ordenar ventas por cliente y fecha")
    void debeOrdenarVentasPorClienteYFecha() {
        // Arrange
        InvVentasBioSuperTO venta4 = new InvVentasBioSuperTO();
        venta4.setEmpresa("01");
        venta4.setCliente("CLI001");
        venta4.setFecha("2024-01-30");
        entityManager.persist(venta4);
        entityManager.flush();

        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndClienteOrderByFechaDesc("01", "CLI001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-30");
    }

    @Test
    @DisplayName("Debe ordenar ventas por periodo y número ascendente")
    void debeOrdenarVentasPorPeriodoYNumeroAscendente() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndPeriodoOrderByNumeroAsc("01", "202401");

        // Assert
        assertThat(resultado).hasSize(2);
        // Verificar que están ordenados (asumiendo que el número es parte del documento)
        assertThat(resultado).extracting(InvVentasBioSuperTO::getNumeroDocumento)
                .containsExactly("FAC-001", "FAC-002");
    }

    @Test
    @DisplayName("Debe encontrar ventas con total mayor a un mínimo")
    void debeEncontrarVentasConTotalMayorAMinimo() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndTotalGreaterThan("01", new BigDecimal("1500.00"));

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTotal()).isGreaterThan(new BigDecimal("1500.00"));
    }

    @Test
    @DisplayName("Debe encontrar ventas autorizadas por rango de fechas")
    void debeEncontrarVentasAutorizadasPorRangoFechas() {
        // Act
        List<InvVentasBioSuperTO> resultado = 
                ventasRepository.findByEmpresaAndAutorizadoTrueAndFechaBetween(
                        "01", "2024-01-01", "2024-01-31");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(InvVentasBioSuperTO::getAutorizado);
    }

    @Test
    @DisplayName("Debe contar ventas por empresa")
    void debeContarVentasPorEmpresa() {
        // Act
        long count = ventasRepository.countByEmpresa("01");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar ventas por empresa y cliente")
    void debeContarVentasPorEmpresaYCliente() {
        // Act
        long count = ventasRepository.countByEmpresaAndCliente("01", "CLI001");

        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe contar ventas por empresa y periodo")
    void debeContarVentasPorEmpresaYPeriodo() {
        // Act
        long count = ventasRepository.countByEmpresaAndPeriodo("01", "202401");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar ventas autorizadas por empresa")
    void debeContarVentasAutorizadasPorEmpresa() {
        // Act
        long count = ventasRepository.countByEmpresaAndAutorizadoTrue("01");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe verificar si existe venta por empresa y número de documento")
    void debeVerificarSiExisteVentaPorEmpresaYNumeroDocumento() {
        // Act
        boolean existe = ventasRepository.existsByEmpresaAndNumeroDocumento("01", "FAC-001");

        // Assert
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Debe retornar false cuando no existe venta")
    void debeRetornarFalseCuandoNoExisteVenta() {
        // Act
        boolean existe = ventasRepository.existsByEmpresaAndNumeroDocumento("01", "FAC-999");

        // Assert
        assertThat(existe).isFalse();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay ventas")
    void debeRetornarListaVaciaCuandoNoHayVentas() {
        // Act
        List<InvVentasBioSuperTO> resultado = ventasRepository.findByEmpresa("99");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar cero cuando no hay ventas para contar")
    void debeRetornarCeroCuandoNoHayVentasParaContar() {
        // Act
        long count = ventasRepository.countByEmpresa("99");

        // Assert
        assertThat(count).isZero();
    }
}

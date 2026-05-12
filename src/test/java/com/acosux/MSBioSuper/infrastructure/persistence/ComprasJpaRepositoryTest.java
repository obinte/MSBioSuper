package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.InvComprasDetalleProductoTO;
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
 * Pruebas unitarias para ComprasJpaRepository.
 * 
 * Utiliza @DataJpaTest para configurar un contexto de prueba con base de datos en memoria H2.
 * Las pruebas verifican:
 * - Métodos de consulta derivados (Query Methods)
 * - Métodos de conteo
 * - Ordenamiento y filtrado
 * 
 * Nota: Las consultas nativas con funciones de PostgreSQL no se prueban aquí
 * ya que requieren la base de datos real. Esas se prueban en pruebas de integración.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de ComprasJpaRepository")
class ComprasJpaRepositoryTest {

    @Autowired
    private ComprasJpaRepository comprasRepository;

    @Autowired
    private TestEntityManager entityManager;

    private InvComprasDetalleProductoTO compra1;
    private InvComprasDetalleProductoTO compra2;
    private InvComprasDetalleProductoTO compra3;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba
        compra1 = new InvComprasDetalleProductoTO();
        compra1.setEmpresa("01");
        compra1.setProveedor("PROV001");
        compra1.setProducto("PROD001");
        compra1.setBodega("BOD001");
        compra1.setSector("SEC001");
        compra1.setPiscina("PIS001");
        compra1.setFecha("2024-01-15");
        compra1.setCantidad(new BigDecimal("100.00"));
        compra1.setTotal(new BigDecimal("1000.00"));

        compra2 = new InvComprasDetalleProductoTO();
        compra2.setEmpresa("01");
        compra2.setProveedor("PROV002");
        compra2.setProducto("PROD002");
        compra2.setBodega("BOD001");
        compra2.setSector("SEC001");
        compra2.setPiscina("PIS002");
        compra2.setFecha("2024-01-20");
        compra2.setCantidad(new BigDecimal("200.00"));
        compra2.setTotal(new BigDecimal("2000.00"));

        compra3 = new InvComprasDetalleProductoTO();
        compra3.setEmpresa("02");
        compra3.setProveedor("PROV001");
        compra3.setProducto("PROD001");
        compra3.setBodega("BOD002");
        compra3.setSector("SEC002");
        compra3.setPiscina("PIS001");
        compra3.setFecha("2024-01-25");
        compra3.setCantidad(new BigDecimal("150.00"));
        compra3.setTotal(new BigDecimal("1500.00"));

        entityManager.persist(compra1);
        entityManager.persist(compra2);
        entityManager.persist(compra3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar compras por empresa")
    void debeEncontrarComprasPorEmpresa() {
        // Act - Ejecutar
        List<InvComprasDetalleProductoTO> resultado = comprasRepository.findByEmpresa("01");

        // Assert - Verificar
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvComprasDetalleProductoTO::getEmpresa)
                .containsOnly("01");
    }

    @Test
    @DisplayName("Debe encontrar compras por empresa y proveedor")
    void debeEncontrarComprasPorEmpresaYProveedor() {
        // Act
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findByEmpresaAndProveedor("01", "PROV001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProveedor()).isEqualTo("PROV001");
    }

    @Test
    @DisplayName("Debe encontrar compras por empresa y producto")
    void debeEncontrarComprasPorEmpresaYProducto() {
        // Act
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findByEmpresaAndProducto("01", "PROD001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProducto()).isEqualTo("PROD001");
    }

    @Test
    @DisplayName("Debe encontrar compras por empresa y bodega")
    void debeEncontrarComprasPorEmpresaYBodega() {
        // Act
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findByEmpresaAndBodega("01", "BOD001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvComprasDetalleProductoTO::getBodega)
                .containsOnly("BOD001");
    }

    @Test
    @DisplayName("Debe encontrar compras por empresa y rango de fechas")
    void debeEncontrarComprasPorEmpresaYRangoFechas() {
        // Act
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findByEmpresaAndFechaBetween("01", "2024-01-01", "2024-01-18");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe encontrar compras por empresa y sector")
    void debeEncontrarComprasPorEmpresaYSector() {
        // Act
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findByEmpresaAndSector("01", "SEC001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvComprasDetalleProductoTO::getSector)
                .containsOnly("SEC001");
    }

    @Test
    @DisplayName("Debe encontrar compras por empresa y piscina")
    void debeEncontrarComprasPorEmpresaYPiscina() {
        // Act
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findByEmpresaAndPiscina("01", "PIS001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getPiscina()).isEqualTo("PIS001");
    }

    @Test
    @DisplayName("Debe ordenar compras por fecha descendente")
    void debeOrdenarComprasPorFechaDescendente() {
        // Act
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findByEmpresaOrderByFechaDesc("01");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-20");
        assertThat(resultado.get(1).getFecha()).isEqualTo("2024-01-15");
    }

    @Test
    @DisplayName("Debe encontrar compras por empresa y proveedor ordenadas por fecha")
    void debeEncontrarComprasPorEmpresaYProveedorOrdenadasPorFecha() {
        // Arrange - Agregar otra compra del mismo proveedor
        InvComprasDetalleProductoTO compra4 = new InvComprasDetalleProductoTO();
        compra4.setEmpresa("01");
        compra4.setProveedor("PROV001");
        compra4.setProducto("PROD003");
        compra4.setFecha("2024-01-30");
        entityManager.persist(compra4);
        entityManager.flush();

        // Act
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findByEmpresaAndProveedorOrderByFechaDesc("01", "PROV001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getFecha()).isEqualTo("2024-01-30");
    }

    @Test
    @DisplayName("Debe encontrar compras con cantidad mayor a un mínimo")
    void debeEncontrarComprasConCantidadMayorAMinimo() {
        // Act
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findByEmpresaAndCantidadGreaterThan("01", new BigDecimal("150.00"));

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCantidad()).isGreaterThan(new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("Debe encontrar compras con total mayor a un mínimo")
    void debeEncontrarComprasConTotalMayorAMinimo() {
        // Act
        List<InvComprasDetalleProductoTO> resultado = 
                comprasRepository.findByEmpresaAndTotalGreaterThan("01", new BigDecimal("1500.00"));

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTotal()).isGreaterThan(new BigDecimal("1500.00"));
    }

    @Test
    @DisplayName("Debe contar compras por empresa")
    void debeContarComprasPorEmpresa() {
        // Act
        long count = comprasRepository.countByEmpresa("01");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar compras por empresa y proveedor")
    void debeContarComprasPorEmpresaYProveedor() {
        // Act
        long count = comprasRepository.countByEmpresaAndProveedor("01", "PROV001");

        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe contar compras por empresa y producto")
    void debeContarComprasPorEmpresaYProducto() {
        // Act
        long count = comprasRepository.countByEmpresaAndProducto("01", "PROD001");

        // Assert
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay compras para la empresa")
    void debeRetornarListaVaciaCuandoNoHayCompras() {
        // Act
        List<InvComprasDetalleProductoTO> resultado = comprasRepository.findByEmpresa("99");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar cero cuando no hay compras para contar")
    void debeRetornarCeroCuandoNoHayComprasParaContar() {
        // Act
        long count = comprasRepository.countByEmpresa("99");

        // Assert
        assertThat(count).isZero();
    }
}

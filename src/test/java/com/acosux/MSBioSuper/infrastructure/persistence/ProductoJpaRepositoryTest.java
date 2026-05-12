package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para ProductoJpaRepository.
 * 
 * Verifica el correcto funcionamiento de los métodos de consulta para productos,
 * incluyendo búsqueda por código, nombre, categoría, tipo y estado activo.
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Pruebas de ProductoJpaRepository")
class ProductoJpaRepositoryTest {

    @Autowired
    private ProductoJpaRepository productoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private InvListaProductosGeneralTO producto1;
    private InvListaProductosGeneralTO producto2;
    private InvListaProductosGeneralTO producto3;

    @BeforeEach
    void setUp() {
        // Arrange - Preparar datos de prueba
        producto1 = new InvListaProductosGeneralTO();
        producto1.setCodigoPrincipal("PROD001");
        producto1.setNombre("Producto Uno");
        producto1.setCategoria("CAT001");
        producto1.setSubcategoria("SUBCAT001");
        producto1.setTipo("TIPO1");
        producto1.setActivo(true);
        producto1.setGravaIva("S");
        producto1.setEcommerce(true);
        producto1.setExigirSerie(false);
        producto1.setExigirLote(true);

        producto2 = new InvListaProductosGeneralTO();
        producto2.setCodigoPrincipal("PROD002");
        producto2.setNombre("Producto Dos");
        producto2.setCategoria("CAT001");
        producto2.setSubcategoria("SUBCAT002");
        producto2.setTipo("TIPO2");
        producto2.setActivo(true);
        producto2.setGravaIva("N");
        producto2.setEcommerce(false);
        producto2.setExigirSerie(true);
        producto2.setExigirLote(false);

        producto3 = new InvListaProductosGeneralTO();
        producto3.setCodigoPrincipal("PROD003");
        producto3.setNombre("Producto Tres Inactivo");
        producto3.setCategoria("CAT002");
        producto3.setSubcategoria("SUBCAT001");
        producto3.setTipo("TIPO1");
        producto3.setActivo(false);
        producto3.setGravaIva("S");
        producto3.setEcommerce(false);
        producto3.setExigirSerie(false);
        producto3.setExigirLote(false);

        entityManager.persist(producto1);
        entityManager.persist(producto2);
        entityManager.persist(producto3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Debe encontrar producto por código principal")
    void debeEncontrarProductoPorCodigoPrincipal() {
        // Act
        Optional<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByCodigoPrincipal("PROD001");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getCodigoPrincipal()).isEqualTo("PROD001");
    }

    @Test
    @DisplayName("Debe encontrar productos por nombre que contiene texto")
    void debeEncontrarProductosPorNombreQueContieneTexto() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByNombreContaining("Producto");

        // Assert
        assertThat(resultado).hasSize(3);
    }

    @Test
    @DisplayName("Debe encontrar productos por categoría")
    void debeEncontrarProductosPorCategoria() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByCategoria("CAT001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvListaProductosGeneralTO::getCategoria)
                .containsOnly("CAT001");
    }

    @Test
    @DisplayName("Debe encontrar productos por subcategoría")
    void debeEncontrarProductosPorSubcategoria() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findBySubcategoria("SUBCAT001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvListaProductosGeneralTO::getSubcategoria)
                .containsOnly("SUBCAT001");
    }

    @Test
    @DisplayName("Debe encontrar productos por tipo")
    void debeEncontrarProductosPorTipo() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByTipo("TIPO1");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvListaProductosGeneralTO::getTipo)
                .containsOnly("TIPO1");
    }

    @Test
    @DisplayName("Debe encontrar productos activos")
    void debeEncontrarProductosActivos() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByActivoTrue();

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(InvListaProductosGeneralTO::getActivo);
    }

    @Test
    @DisplayName("Debe encontrar productos inactivos")
    void debeEncontrarProductosInactivos() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByActivoFalse();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado).noneMatch(InvListaProductosGeneralTO::getActivo);
    }

    @Test
    @DisplayName("Debe encontrar productos que gravan IVA")
    void debeEncontrarProductosQueGravanIva() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByGravaIva("S");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(InvListaProductosGeneralTO::getGravaIva)
                .containsOnly("S");
    }

    @Test
    @DisplayName("Debe encontrar productos por categoría y estado activo")
    void debeEncontrarProductosPorCategoriaYEstadoActivo() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByCategoriaAndActivo("CAT001", true);

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(p -> p.getCategoria().equals("CAT001") && p.getActivo());
    }

    @Test
    @DisplayName("Debe encontrar productos de ecommerce")
    void debeEncontrarProductosDeEcommerce() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByEcommerceTrue();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado).allMatch(InvListaProductosGeneralTO::getEcommerce);
    }

    @Test
    @DisplayName("Debe encontrar productos que exigen serie")
    void debeEncontrarProductosQueExigenSerie() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByExigirSerieTrue();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado).allMatch(InvListaProductosGeneralTO::getExigirSerie);
    }

    @Test
    @DisplayName("Debe encontrar productos que exigen lote")
    void debeEncontrarProductosQueExigenLote() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByExigirLoteTrue();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado).allMatch(InvListaProductosGeneralTO::getExigirLote);
    }

    @Test
    @DisplayName("Debe verificar si existe producto por código principal")
    void debeVerificarSiExisteProductoPorCodigoPrincipal() {
        // Act
        boolean existe = productoRepository.existsByCodigoPrincipal("PROD001");

        // Assert
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Debe encontrar productos por nombre ordenados alfabéticamente")
    void debeEncontrarProductosPorNombreOrdenadosAlfabeticamente() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByNombreContainingOrderByNombreAsc("Producto");

        // Assert
        assertThat(resultado).hasSize(3);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Producto Dos");
        assertThat(resultado.get(2).getNombre()).isEqualTo("Producto Uno");
    }

    @Test
    @DisplayName("Debe encontrar productos activos por categoría ordenados")
    void debeEncontrarProductosActivosPorCategoriaOrdenados() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByActivoTrueAndCategoriaOrderByNombreAsc("CAT001");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(p -> p.getActivo() && p.getCategoria().equals("CAT001"));
    }

    @Test
    @DisplayName("Debe encontrar productos por tipo ordenados por código")
    void debeEncontrarProductosPorTipoOrdenadosPorCodigo() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByTipoOrderByCodigoPrincipalAsc("TIPO1");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getCodigoPrincipal()).isEqualTo("PROD001");
    }

    @Test
    @DisplayName("Debe encontrar productos por categoría y subcategoría ordenados")
    void debeEncontrarProductosPorCategoriaYSubcategoriaOrdenados() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByCategoriaAndSubcategoriaOrderByNombreAsc("CAT001", "SUBCAT001");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCategoria()).isEqualTo("CAT001");
        assertThat(resultado.get(0).getSubcategoria()).isEqualTo("SUBCAT001");
    }

    @Test
    @DisplayName("Debe encontrar productos de ecommerce ordenados")
    void debeEncontrarProductosDeEcommerceOrdenados() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByEcommerceTrueOrderByNombreAsc();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado).allMatch(InvListaProductosGeneralTO::getEcommerce);
    }

    @Test
    @DisplayName("Debe encontrar productos activos que gravan IVA ordenados")
    void debeEncontrarProductosActivosQueGravanIvaOrdenados() {
        // Act
        List<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByActivoTrueAndGravaIvaOrderByNombreAsc("S");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado).allMatch(p -> p.getActivo() && p.getGravaIva().equals("S"));
    }

    @Test
    @DisplayName("Debe contar productos por categoría")
    void debeContarProductosPorCategoria() {
        // Act
        long count = productoRepository.countByCategoria("CAT001");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar productos activos")
    void debeContarProductosActivos() {
        // Act
        long count = productoRepository.countByActivoTrue();

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe contar productos por tipo")
    void debeContarProductosPorTipo() {
        // Act
        long count = productoRepository.countByTipo("TIPO1");

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando no existe producto")
    void debeRetornarOptionalVacioCuandoNoExisteProducto() {
        // Act
        Optional<InvListaProductosGeneralTO> resultado = 
                productoRepository.findByCodigoPrincipal("PROD999");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar false cuando no existe producto por código")
    void debeRetornarFalseCuandoNoExisteProductoPorCodigo() {
        // Act
        boolean existe = productoRepository.existsByCodigoPrincipal("PROD999");

        // Assert
        assertThat(existe).isFalse();
    }
}

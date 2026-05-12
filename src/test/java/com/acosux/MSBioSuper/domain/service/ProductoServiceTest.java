package com.acosux.MSBioSuper.domain.service;

import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para ProductoService.
 * 
 * Valida la lógica de negocio de filtrado, validación y procesamiento de productos.
 * 
 * @author Josias Rubén Aldaz Arcalles
 */
@DisplayName("ProductoService - Pruebas Unitarias")
class ProductoServiceTest {

    private ProductoService productoService;
    private List<InvListaProductosGeneralTO> productosTest;

    @BeforeEach
    void setUp() {
        productoService = new ProductoService();
        productosTest = crearProductosTest();
    }

    // ==================== Pruebas de aplicarFiltrosNegocio ====================

    @Test
    @DisplayName("Debe retornar lista vacía cuando productos es null")
    void aplicarFiltrosNegocio_ProductosNull_RetornaListaVacia() {
        // Given
        List<InvListaProductosGeneralTO> productos = null;

        // When
        List<InvListaProductosGeneralTO> resultado = productoService.aplicarFiltrosNegocio(
                productos, "PROD001", false, null, false);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando productos está vacía")
    void aplicarFiltrosNegocio_ProductosVacia_RetornaListaVacia() {
        // Given
        List<InvListaProductosGeneralTO> productos = new ArrayList<>();

        // When
        List<InvListaProductosGeneralTO> resultado = productoService.aplicarFiltrosNegocio(
                productos, "PROD001", false, null, false);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe filtrar por código exacto cuando buscarPorCodigo es true")
    void aplicarFiltrosNegocio_BuscarPorCodigoExacto_FiltraCorrectamente() {
        // Given
        String codigoBuscado = "PROD001";

        // When
        List<InvListaProductosGeneralTO> resultado = productoService.aplicarFiltrosNegocio(
                productosTest, codigoBuscado, true, null, false);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProCodigoPrincipal()).isEqualTo(codigoBuscado);
    }

    @Test
    @DisplayName("Debe filtrar por tipo de producto")
    void aplicarFiltrosNegocio_FiltrarPorTipo_FiltraCorrectamente() {
        // Given
        String tipo = "ALIMENTO";

        // When
        List<InvListaProductosGeneralTO> resultado = productoService.aplicarFiltrosNegocio(
                productosTest, null, false, tipo, false);

        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado).allMatch(p -> p.getTipTipo().contains(tipo));
    }

    @Test
    @DisplayName("Debe aplicar límite de resultados cuando excede el máximo")
    void aplicarFiltrosNegocio_ExcedeLimite_AplicaLimite() {
        // Given
        List<InvListaProductosGeneralTO> muchosProductos = new ArrayList<>();
        for (int i = 0; i < 1500; i++) {
            InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();
            producto.setId(i);
            producto.setProCodigoPrincipal("PROD" + i);
            producto.setProNombre("Producto " + i);
            muchosProductos.add(producto);
        }

        // When
        List<InvListaProductosGeneralTO> resultado = productoService.aplicarFiltrosNegocio(
                muchosProductos, null, false, null, true);

        // Then
        assertThat(resultado).hasSize(1000); // Límite máximo
    }

    @Test
    @DisplayName("Debe aplicar múltiples filtros en combinación")
    void aplicarFiltrosNegocio_MultipleFiltros_AplicaTodos() {
        // Given
        String codigoBuscado = "PROD001";
        String tipo = "ALIMENTO";

        // When
        List<InvListaProductosGeneralTO> resultado = productoService.aplicarFiltrosNegocio(
                productosTest, codigoBuscado, true, tipo, false);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProCodigoPrincipal()).isEqualTo(codigoBuscado);
        assertThat(resultado.get(0).getTipTipo()).contains(tipo);
    }

    // ==================== Pruebas de esProductoValido ====================

    @Test
    @DisplayName("Debe retornar false cuando producto es null")
    void esProductoValido_ProductoNull_RetornaFalse() {
        // Given
        InvListaProductosGeneralTO producto = null;

        // When
        boolean resultado = productoService.esProductoValido(producto);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar false cuando código principal es null")
    void esProductoValido_CodigoPrincipalNull_RetornaFalse() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();
        producto.setProCodigoPrincipal(null);
        producto.setProNombre("Producto Test");

        // When
        boolean resultado = productoService.esProductoValido(producto);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar false cuando código principal está vacío")
    void esProductoValido_CodigoPrincipalVacio_RetornaFalse() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();
        producto.setProCodigoPrincipal("");
        producto.setProNombre("Producto Test");

        // When
        boolean resultado = productoService.esProductoValido(producto);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar false cuando nombre es null")
    void esProductoValido_NombreNull_RetornaFalse() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();
        producto.setProCodigoPrincipal("PROD001");
        producto.setProNombre(null);

        // When
        boolean resultado = productoService.esProductoValido(producto);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar false cuando nombre está vacío")
    void esProductoValido_NombreVacio_RetornaFalse() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();
        producto.setProCodigoPrincipal("PROD001");
        producto.setProNombre("");

        // When
        boolean resultado = productoService.esProductoValido(producto);

        // Then
        assertThat(resultado).isFalse();
    }

    @Test
    @DisplayName("Debe retornar true cuando producto tiene código y nombre válidos")
    void esProductoValido_ProductoCompleto_RetornaTrue() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();
        producto.setProCodigoPrincipal("PROD001");
        producto.setProNombre("Producto Test");

        // When
        boolean resultado = productoService.esProductoValido(producto);

        // Then
        assertThat(resultado).isTrue();
    }

    // ==================== Pruebas de filtrarProductosValidos ====================

    @Test
    @DisplayName("Debe retornar lista vacía cuando productos es null")
    void filtrarProductosValidos_ProductosNull_RetornaListaVacia() {
        // Given
        List<InvListaProductosGeneralTO> productos = null;

        // When
        List<InvListaProductosGeneralTO> resultado = productoService.filtrarProductosValidos(productos);

        // Then
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe filtrar productos inválidos")
    void filtrarProductosValidos_ProductosInvalidos_FiltraCorrectamente() {
        // Given
        List<InvListaProductosGeneralTO> productos = new ArrayList<>();
        
        // Producto válido
        InvListaProductosGeneralTO productoValido = new InvListaProductosGeneralTO();
        productoValido.setProCodigoPrincipal("PROD001");
        productoValido.setProNombre("Producto Válido");
        productos.add(productoValido);
        
        // Producto sin código
        InvListaProductosGeneralTO productoSinCodigo = new InvListaProductosGeneralTO();
        productoSinCodigo.setProNombre("Producto Sin Código");
        productos.add(productoSinCodigo);
        
        // Producto sin nombre
        InvListaProductosGeneralTO productoSinNombre = new InvListaProductosGeneralTO();
        productoSinNombre.setProCodigoPrincipal("PROD002");
        productos.add(productoSinNombre);

        // When
        List<InvListaProductosGeneralTO> resultado = productoService.filtrarProductosValidos(productos);

        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProCodigoPrincipal()).isEqualTo("PROD001");
    }

    // ==================== Métodos auxiliares ====================

    /**
     * Crea una lista de productos de prueba.
     */
    private List<InvListaProductosGeneralTO> crearProductosTest() {
        List<InvListaProductosGeneralTO> productos = new ArrayList<>();

        // Producto 1: Alimento balanceado
        InvListaProductosGeneralTO producto1 = new InvListaProductosGeneralTO();
        producto1.setId(1);
        producto1.setProCodigoPrincipal("PROD001");
        producto1.setProNombre("Balanceado Premium");
        producto1.setTipTipo("ALIMENTO");
        producto1.setProPrecio(new BigDecimal("25.50"));
        productos.add(producto1);

        // Producto 2: Alimento estándar
        InvListaProductosGeneralTO producto2 = new InvListaProductosGeneralTO();
        producto2.setId(2);
        producto2.setProCodigoPrincipal("PROD002");
        producto2.setProNombre("Balanceado Estándar");
        producto2.setTipTipo("ALIMENTO");
        producto2.setProPrecio(new BigDecimal("18.00"));
        productos.add(producto2);

        // Producto 3: Químico
        InvListaProductosGeneralTO producto3 = new InvListaProductosGeneralTO();
        producto3.setId(3);
        producto3.setProCodigoPrincipal("PROD003");
        producto3.setProNombre("Desinfectante");
        producto3.setTipTipo("QUIMICO");
        producto3.setProPrecio(new BigDecimal("12.75"));
        productos.add(producto3);

        return productos;
    }
}

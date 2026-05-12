package com.acosux.MSBioSuper.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para InvListaProductosGeneralTO.
 * 
 * Valida el comportamiento de la entidad de dominio para productos.
 */
@DisplayName("InvListaProductosGeneralTO - Pruebas Unitarias")
class InvListaProductosGeneralTOTest {

    @Test
    @DisplayName("Debe crear instancia con constructor por defecto")
    void debeCrearInstanciaConConstructorPorDefecto() {
        // When
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // Then
        assertNotNull(producto);
    }

    @Test
    @DisplayName("Debe crear instancia con constructor parametrizado")
    void debeCrearInstanciaConConstructorParametrizado() {
        // When
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO(
            1, "PROD001", "Producto Test", "CAT01", "SACO",
            new BigDecimal("100"), new BigDecimal("50.00"), new BigDecimal("48.00"),
            "KG", new BigDecimal("55.00"), new BigDecimal("10"),
            new BigDecimal("5.00"), new BigDecimal("10.00"), "SI",
            false, new BigDecimal("45.00")
        );

        // Then
        assertNotNull(producto);
        assertEquals(1, producto.getId());
        assertEquals("PROD001", producto.getProCodigoPrincipal());
        assertEquals("Producto Test", producto.getProNombre());
        assertEquals("CAT01", producto.getProCategoria());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos básicos del producto")
    void debeEstablecerYObtenerDatosBasicos() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // When
        producto.setId(1);
        producto.setProCodigoPrincipal("PROD001");
        producto.setProNombre("Alimento Balanceado");
        producto.setProCategoria("ALIMENTOS");
        producto.setTipTipo("CONSUMIBLE");

        // Then
        assertEquals(1, producto.getId());
        assertEquals("PROD001", producto.getProCodigoPrincipal());
        assertEquals("Alimento Balanceado", producto.getProNombre());
        assertEquals("ALIMENTOS", producto.getProCategoria());
        assertEquals("CONSUMIBLE", producto.getTipTipo());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de inventario")
    void debeEstablecerYObtenerDatosInventario() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // When
        producto.setStockSaldo(new BigDecimal("500"));
        producto.setStockUltimoCosto(new BigDecimal("45.50"));
        producto.setStockCostoPromedio(new BigDecimal("44.00"));
        producto.setDetalleMedida("KG");

        // Then
        assertEquals(new BigDecimal("500"), producto.getStockSaldo());
        assertEquals(new BigDecimal("45.50"), producto.getStockUltimoCosto());
        assertEquals(new BigDecimal("44.00"), producto.getStockCostoPromedio());
        assertEquals("KG", producto.getDetalleMedida());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de precios")
    void debeEstablecerYObtenerDatosPrecios() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // When
        producto.setProPrecio(new BigDecimal("50.00"));
        producto.setProPrecio2(new BigDecimal("48.00"));
        producto.setProPrecio3(new BigDecimal("45.00"));
        producto.setProPrecioBase(new BigDecimal("40.00"));
        producto.setProDescuento(new BigDecimal("5.00"));
        producto.setProMargenUtilidad(new BigDecimal("25.00"));

        // Then
        assertEquals(new BigDecimal("50.00"), producto.getProPrecio());
        assertEquals(new BigDecimal("48.00"), producto.getProPrecio2());
        assertEquals(new BigDecimal("45.00"), producto.getProPrecio3());
        assertEquals(new BigDecimal("40.00"), producto.getProPrecioBase());
        assertEquals(new BigDecimal("5.00"), producto.getProDescuento());
        assertEquals(new BigDecimal("25.00"), producto.getProMargenUtilidad());
    }

    @Test
    @DisplayName("Debe establecer y obtener flags booleanos")
    void debeEstablecerYObtenerFlagsBooleanos() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // When
        producto.setProInactivo(false);
        producto.setPrecioFijo(true);
        producto.setProExigirSerie(false);
        producto.setProEcommerce(true);
        producto.setLpspExigirLote(true);

        // Then
        assertFalse(producto.isProInactivo());
        assertTrue(producto.isPrecioFijo());
        assertFalse(producto.isProExigirSerie());
        assertTrue(producto.getProEcommerce());
        assertTrue(producto.getLpspExigirLote());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de impuestos")
    void debeEstablecerYObtenerDatosImpuestos() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // When
        producto.setProGravaIva("SI");
        producto.setProCreditoTributario("100");
        producto.setIcePorcentaje(new BigDecimal("10.00"));
        producto.setIceTarifaFija(new BigDecimal("0.50"));
        producto.setIceCodigo("ICE001");

        // Then
        assertEquals("SI", producto.getProGravaIva());
        assertEquals("100", producto.getProCreditoTributario());
        assertEquals(new BigDecimal("10.00"), producto.getIcePorcentaje());
        assertEquals(new BigDecimal("0.50"), producto.getIceTarifaFija());
        assertEquals("ICE001", producto.getIceCodigo());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de empaque y conversión")
    void debeEstablecerYObtenerDatosEmpaqueYConversion() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // When
        producto.setProEmpaque("SACO 50KG");
        producto.setFactorConversion(new BigDecimal("50"));
        producto.setProCantidad(new BigDecimal("100"));

        // Then
        assertEquals("SACO 50KG", producto.getProEmpaque());
        assertEquals(new BigDecimal("50"), producto.getFactorConversion());
        assertEquals(new BigDecimal("100"), producto.getProCantidad());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos contables")
    void debeEstablecerYObtenerDatosContables() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // When
        producto.setLpspCuentaInventario("1.1.05.01");
        producto.setLpspNombreInventario("Inventario de Productos");
        producto.setLpspCuentaCostoConsumo("5.1.01.01");
        producto.setLpspNombreCostoConsumo("Costo de Ventas");

        // Then
        assertEquals("1.1.05.01", producto.getLpspCuentaInventario());
        assertEquals("Inventario de Productos", producto.getLpspNombreInventario());
        assertEquals("5.1.01.01", producto.getLpspCuentaCostoConsumo());
        assertEquals("Costo de Ventas", producto.getLpspNombreCostoConsumo());
    }

    @Test
    @DisplayName("Debe manejar valores nulos correctamente")
    void debeManejarValoresNulosCorrectamente() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // When
        producto.setProPrecio(null);
        producto.setStockSaldo(null);
        producto.setProCategoria(null);
        producto.setTieneImagenes(null);

        // Then
        assertNull(producto.getProPrecio());
        assertNull(producto.getStockSaldo());
        assertNull(producto.getProCategoria());
        assertNull(producto.getTieneImagenes());
    }

    @Test
    @DisplayName("Debe establecer y obtener subcategoría y tipo de producto")
    void debeEstablecerYObtenerSubcategoriaYTipo() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // When
        producto.setLpspSubcategoria("BALANCEADOS");
        producto.setTipTipoTipoProducto("BIEN");

        // Then
        assertEquals("BALANCEADOS", producto.getLpspSubcategoria());
        assertEquals("BIEN", producto.getTipTipoTipoProducto());
    }

    @Test
    @DisplayName("Debe establecer y obtener datos de plan de cuentas")
    void debeEstablecerYObtenerDatosPlanCuentas() {
        // Given
        InvListaProductosGeneralTO producto = new InvListaProductosGeneralTO();

        // When
        producto.setLpspPiCodigo("PI001");
        producto.setLpspPiPorcentaje(new BigDecimal("15.00"));

        // Then
        assertEquals("PI001", producto.getLpspPiCodigo());
        assertEquals(new BigDecimal("15.00"), producto.getLpspPiPorcentaje());
    }
}

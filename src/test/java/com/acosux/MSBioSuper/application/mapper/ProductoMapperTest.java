package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.ProductoRequest;
import com.acosux.MSBioSuper.application.dto.ProductoResponse;
import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para ProductoMapper.
 * Valida la conversión correcta entre DTOs y entidades.
 */
@DisplayName("ProductoMapper - Pruebas Unitarias")
class ProductoMapperTest {

    private ProductoMapper mapper;
    private InvListaProductosGeneralTO entidad;
    private ProductoRequest request;

    @BeforeEach
    void setUp() {
        mapper = new ProductoMapper();

        entidad = new InvListaProductosGeneralTO();
        entidad.setId(1);
        entidad.setProCodigoPrincipal("PROD-001");
        entidad.setProNombre("Producto Test");
        entidad.setProCategoria("CATEGORIA-A");
        entidad.setProEmpaque("CAJA");
        entidad.setStockSaldo(BigDecimal.valueOf(100.00));
        entidad.setStockUltimoCosto(BigDecimal.valueOf(50.00));
        entidad.setStockCostoPromedio(BigDecimal.valueOf(48.00));
        entidad.setFactorConversion(BigDecimal.ONE);
        entidad.setDetalleMedida("UNIDAD");
        entidad.setProPrecio(BigDecimal.valueOf(75.00));
        entidad.setProCantidad(BigDecimal.valueOf(100.00));
        entidad.setProPrecioBase(BigDecimal.valueOf(70.00));
        entidad.setProDescuento(BigDecimal.ZERO);
        entidad.setProMargenUtilidad(BigDecimal.valueOf(25.00));
        entidad.setProPrecio2(BigDecimal.valueOf(80.00));
        entidad.setProPrecio3(BigDecimal.valueOf(85.00));
        entidad.setProGravaIva("S");
        entidad.setTipTipo("PRODUCTO");
        entidad.setPrecioFijo(false);
        entidad.setProInactivo(false);
        entidad.setProExigirSerie(false);
        entidad.setProCostoReferencial(BigDecimal.valueOf(50.00));
        entidad.setTieneImagenes(true);
        entidad.setProCreditoTributario("N");
        entidad.setProEcommerce(true);
        entidad.setTipTipoTipoProducto("NORMAL");
        entidad.setIcePorcentaje(BigDecimal.ZERO);
        entidad.setIceTarifaFija(BigDecimal.ZERO);
        entidad.setIceCodigo(null);
        entidad.setLpspExigirLote(false);
        entidad.setLpspSubcategoria("SUBCAT-A");
        entidad.setLpspCuentaInventario("1101");
        entidad.setLpspNombreInventario("Inventario Productos");
        entidad.setLpspCuentaCostoConsumo("5101");
        entidad.setLpspNombreCostoConsumo("Costo de Ventas");
        entidad.setLpspPiCodigo("PI-001");
        entidad.setLpspPiPorcentaje(BigDecimal.valueOf(5.00));

        request = new ProductoRequest(
                "01",
                "BOD-001",
                "PROD-001",
                "CATEGORIA-A",
                "Test",
                1,
                false
        );
    }

    @Test
    @DisplayName("Debe convertir request a mapa de parámetros correctamente")
    void toParameterMap_ConRequestValido_DebeRetornarMapaConParametros() {
        // When
        Map<String, Object> resultado = mapper.toParameterMap(request);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).containsEntry("empresa", "01");
        assertThat(resultado).containsEntry("bodega", "BOD-001");
        assertThat(resultado).containsEntry("codigoBuscar", "PROD-001");
        assertThat(resultado).containsEntry("categoria", "CATEGORIA-A");
        assertThat(resultado).containsEntry("busqueda", "Test");
        assertThat(resultado).containsEntry("tipoPrecio", 1);
        assertThat(resultado).containsEntry("incluirInactivos", false);
    }

    @Test
    @DisplayName("Debe omitir parámetros opcionales nulos en el mapa")
    void toParameterMap_ConParametrosOpcionalesNulos_DebeOmitirlos() {
        // Given
        ProductoRequest requestSinOpcionales = new ProductoRequest(
                "01", "BOD-001", null, null, null, null, null
        );

        // When
        Map<String, Object> resultado = mapper.toParameterMap(requestSinOpcionales);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).containsEntry("empresa", "01");
        assertThat(resultado).containsEntry("bodega", "BOD-001");
        assertThat(resultado).doesNotContainKey("codigoBuscar");
        assertThat(resultado).doesNotContainKey("categoria");
        assertThat(resultado).doesNotContainKey("busqueda");
        assertThat(resultado).doesNotContainKey("tipoPrecio");
        assertThat(resultado).doesNotContainKey("incluirInactivos");
    }

    @Test
    @DisplayName("Debe convertir entidad a response correctamente")
    void toResponse_ConEntidadValida_DebeRetornarResponse() {
        // When
        ProductoResponse resultado = mapper.toResponse(entidad);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.proCodigoPrincipal()).isEqualTo("PROD-001");
        assertThat(resultado.proNombre()).isEqualTo("Producto Test");
        assertThat(resultado.proCategoria()).isEqualTo("CATEGORIA-A");
        assertThat(resultado.proEmpaque()).isEqualTo("CAJA");
        assertThat(resultado.stockSaldo()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(resultado.stockUltimoCosto()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
        assertThat(resultado.stockCostoPromedio()).isEqualByComparingTo(BigDecimal.valueOf(48.00));
        assertThat(resultado.factorConversion()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(resultado.detalleMedida()).isEqualTo("UNIDAD");
        assertThat(resultado.proPrecio()).isEqualByComparingTo(BigDecimal.valueOf(75.00));
        assertThat(resultado.proCantidad()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(resultado.proPrecioBase()).isEqualByComparingTo(BigDecimal.valueOf(70.00));
        assertThat(resultado.proDescuento()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(resultado.proMargenUtilidad()).isEqualByComparingTo(BigDecimal.valueOf(25.00));
        assertThat(resultado.proPrecio2()).isEqualByComparingTo(BigDecimal.valueOf(80.00));
        assertThat(resultado.proPrecio3()).isEqualByComparingTo(BigDecimal.valueOf(85.00));
        assertThat(resultado.proGravaIva()).isEqualTo("S");
        assertThat(resultado.tipTipo()).isEqualTo("PRODUCTO");
        assertThat(resultado.precioFijo()).isFalse();
        assertThat(resultado.proInactivo()).isFalse();
        assertThat(resultado.proExigirSerie()).isFalse();
        assertThat(resultado.proCostoReferencial()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
        assertThat(resultado.tieneImagenes()).isTrue();
        assertThat(resultado.proCreditoTributario()).isEqualTo("N");
        assertThat(resultado.proEcommerce()).isTrue();
        assertThat(resultado.tipTipoTipoProducto()).isEqualTo("NORMAL");
        assertThat(resultado.icePorcentaje()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(resultado.iceTarifaFija()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(resultado.iceCodigo()).isNull();
        assertThat(resultado.lpspExigirLote()).isFalse();
        assertThat(resultado.lpspSubcategoria()).isEqualTo("SUBCAT-A");
        assertThat(resultado.lpspCuentaInventario()).isEqualTo("1101");
        assertThat(resultado.lpspNombreInventario()).isEqualTo("Inventario Productos");
        assertThat(resultado.lpspCuentaCostoConsumo()).isEqualTo("5101");
        assertThat(resultado.lpspNombreCostoConsumo()).isEqualTo("Costo de Ventas");
        assertThat(resultado.lpspPiCodigo()).isEqualTo("PI-001");
        assertThat(resultado.lpspPiPorcentaje()).isEqualByComparingTo(BigDecimal.valueOf(5.00));
    }

    @Test
    @DisplayName("Debe retornar null cuando la entidad es nula")
    void toResponse_ConEntidadNula_DebeRetornarNull() {
        // When
        ProductoResponse resultado = mapper.toResponse(null);

        // Then
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Debe convertir lista de entidades a lista de responses")
    void toResponseList_ConListaDeEntidades_DebeRetornarListaDeResponses() {
        // Given
        InvListaProductosGeneralTO entidad2 = new InvListaProductosGeneralTO();
        entidad2.setId(2);
        entidad2.setProCodigoPrincipal("PROD-002");
        entidad2.setProNombre("Producto Test 2");

        List<InvListaProductosGeneralTO> entidades = Arrays.asList(entidad, entidad2);

        // When
        List<ProductoResponse> resultado = mapper.toResponseList(entidades);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1);
        assertThat(resultado.get(0).proCodigoPrincipal()).isEqualTo("PROD-001");
        assertThat(resultado.get(1).id()).isEqualTo(2);
        assertThat(resultado.get(1).proCodigoPrincipal()).isEqualTo("PROD-002");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades es nula")
    void toResponseList_ConListaNula_DebeRetornarListaVacia() {
        // When
        List<ProductoResponse> resultado = mapper.toResponseList(null);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades está vacía")
    void toResponseList_ConListaVacia_DebeRetornarListaVacia() {
        // When
        List<ProductoResponse> resultado = mapper.toResponseList(Collections.emptyList());

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe manejar valores nulos en campos de la entidad")
    void toResponse_ConCamposNulos_DebeMapearCorrectamente() {
        // Given
        InvListaProductosGeneralTO entidadConNulos = new InvListaProductosGeneralTO();
        entidadConNulos.setId(1);
        entidadConNulos.setProCodigoPrincipal("PROD-001");
        entidadConNulos.setProNombre(null);
        entidadConNulos.setStockSaldo(null);

        // When
        ProductoResponse resultado = mapper.toResponse(entidadConNulos);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.proCodigoPrincipal()).isEqualTo("PROD-001");
        assertThat(resultado.proNombre()).isNull();
        assertThat(resultado.stockSaldo()).isNull();
    }
}

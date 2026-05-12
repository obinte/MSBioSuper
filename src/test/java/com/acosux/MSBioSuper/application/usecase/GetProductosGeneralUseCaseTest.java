package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ProductoRequest;
import com.acosux.MSBioSuper.application.dto.ProductoResponse;
import com.acosux.MSBioSuper.application.mapper.ProductoMapper;
import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;
import com.acosux.MSBioSuper.domain.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para GetProductosGeneralUseCase.
 * Valida la lógica de negocio para consultar productos.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetProductosGeneralUseCase - Pruebas Unitarias")
class GetProductosGeneralUseCaseTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private GetProductosGeneralUseCase useCase;

    private ProductoRequest requestValido;
    private InvListaProductosGeneralTO entidadMock;
    private ProductoResponse responseMock;

    @BeforeEach
    void setUp() {
        requestValido = new ProductoRequest(
                "01",
                "BOD-001",
                null,
                null,
                null,
                null,
                null
        );

        entidadMock = new InvListaProductosGeneralTO();
        entidadMock.setId(1L);
        entidadMock.setProCodigoPrincipal("PROD-001");
        entidadMock.setProNombre("Producto Test");
        entidadMock.setProCategoria("Categoría A");
        entidadMock.setStockSaldo(BigDecimal.valueOf(100.0));
        entidadMock.setProPrecio(BigDecimal.valueOf(25.50));

        responseMock = new ProductoResponse(
                1L, "PROD-001", "Producto Test", "Categoría A", "UNIDAD",
                BigDecimal.valueOf(100.0), BigDecimal.valueOf(20.0), BigDecimal.valueOf(22.0),
                BigDecimal.ONE, "UNIDAD", BigDecimal.valueOf(25.50), BigDecimal.ONE,
                BigDecimal.valueOf(25.50), BigDecimal.ZERO, BigDecimal.valueOf(15.0),
                BigDecimal.valueOf(30.0), BigDecimal.valueOf(35.0), true, "BIEN",
                false, false, false, BigDecimal.valueOf(22.0), false, false,
                "BIEN", BigDecimal.ZERO, BigDecimal.ZERO, "ICE-001", false,
                "SUBCAT-001", "CTA-001", "Cuenta Inventario", "CTA-002",
                "Cuenta Costo", "PI-001", BigDecimal.ZERO
        );
    }

    @Test
    @DisplayName("Debe retornar lista de productos cuando los parámetros son válidos")
    void execute_ConParametrosValidos_DebeRetornarListaDeRespuestas() {
        // Given
        List<InvListaProductosGeneralTO> entidades = Arrays.asList(entidadMock);
        List<ProductoResponse> respuestasEsperadas = Arrays.asList(responseMock);

        when(productoRepository.findProductosSaldosPreciosWeb(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), anyInt(), anyBoolean()))
                .thenReturn(entidades);
        when(productoMapper.toResponseList(entidades)).thenReturn(respuestasEsperadas);

        // When
        List<ProductoResponse> resultado = useCase.execute(requestValido);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(responseMock);

        verify(productoRepository).findProductosSaldosPreciosWeb(
                "01", "BOD-001", null, "", "", null, 1, false);
        verify(productoMapper).toResponseList(entidades);
    }

    @Test
    @DisplayName("Debe usar valores por defecto cuando los parámetros opcionales son nulos")
    void execute_ConParametrosOpcionales_DebeUsarValoresPorDefecto() {
        // Given
        when(productoRepository.findProductosSaldosPreciosWeb(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), anyInt(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        when(productoMapper.toResponseList(anyList())).thenReturn(Collections.emptyList());

        // When
        List<ProductoResponse> resultado = useCase.execute(requestValido);

        // Then
        assertThat(resultado).isNotNull();
        verify(productoRepository).findProductosSaldosPreciosWeb(
                "01", "BOD-001", null, "", "", null, 1, false);
    }

    @Test
    @DisplayName("Debe incluir categoría cuando se especifica")
    void execute_ConCategoria_DebeIncluirEnConsulta() {
        // Given
        ProductoRequest requestConCategoria = new ProductoRequest(
                "01", "BOD-001", "CAT-A", null, null, null, null
        );

        when(productoRepository.findProductosSaldosPreciosWeb(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), anyInt(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        when(productoMapper.toResponseList(anyList())).thenReturn(Collections.emptyList());

        // When
        List<ProductoResponse> resultado = useCase.execute(requestConCategoria);

        // Then
        assertThat(resultado).isNotNull();
        verify(productoRepository).findProductosSaldosPreciosWeb(
                "01", "BOD-001", null, "CAT-A", "", null, 1, false);
    }

    @Test
    @DisplayName("Debe incluir búsqueda cuando se especifica")
    void execute_ConBusqueda_DebeIncluirEnConsulta() {
        // Given
        ProductoRequest requestConBusqueda = new ProductoRequest(
                "01", "BOD-001", null, null, "producto test", null, null
        );

        when(productoRepository.findProductosSaldosPreciosWeb(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), anyInt(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        when(productoMapper.toResponseList(anyList())).thenReturn(Collections.emptyList());

        // When
        List<ProductoResponse> resultado = useCase.execute(requestConBusqueda);

        // Then
        assertThat(resultado).isNotNull();
        verify(productoRepository).findProductosSaldosPreciosWeb(
                "01", "BOD-001", null, "", "producto test", null, 1, false);
    }

    @Test
    @DisplayName("Debe incluir código a buscar cuando se especifica")
    void execute_ConCodigoBuscar_DebeIncluirEnConsulta() {
        // Given
        ProductoRequest requestConCodigo = new ProductoRequest(
                "01", "BOD-001", null, "PROD-001", null, null, null
        );

        when(productoRepository.findProductosSaldosPreciosWeb(
                anyString(), anyString(), anyString(), anyString(), anyString(), isNull(), anyInt(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        when(productoMapper.toResponseList(anyList())).thenReturn(Collections.emptyList());

        // When
        List<ProductoResponse> resultado = useCase.execute(requestConCodigo);

        // Then
        assertThat(resultado).isNotNull();
        verify(productoRepository).findProductosSaldosPreciosWeb(
                "01", "BOD-001", "PROD-001", "", "", null, 1, false);
    }

    @Test
    @DisplayName("Debe usar tipo de precio especificado")
    void execute_ConTipoPrecio_DebeUsarValorEspecificado() {
        // Given
        ProductoRequest requestConTipoPrecio = new ProductoRequest(
                "01", "BOD-001", null, null, null, 2, null
        );

        when(productoRepository.findProductosSaldosPreciosWeb(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), anyInt(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        when(productoMapper.toResponseList(anyList())).thenReturn(Collections.emptyList());

        // When
        List<ProductoResponse> resultado = useCase.execute(requestConTipoPrecio);

        // Then
        assertThat(resultado).isNotNull();
        verify(productoRepository).findProductosSaldosPreciosWeb(
                "01", "BOD-001", null, "", "", null, 2, false);
    }

    @Test
    @DisplayName("Debe incluir inactivos cuando se especifica")
    void execute_ConIncluirInactivos_DebeUsarValorEspecificado() {
        // Given
        ProductoRequest requestConInactivos = new ProductoRequest(
                "01", "BOD-001", null, null, null, null, true
        );

        when(productoRepository.findProductosSaldosPreciosWeb(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), anyInt(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        when(productoMapper.toResponseList(anyList())).thenReturn(Collections.emptyList());

        // When
        List<ProductoResponse> resultado = useCase.execute(requestConInactivos);

        // Then
        assertThat(resultado).isNotNull();
        verify(productoRepository).findProductosSaldosPreciosWeb(
                "01", "BOD-001", null, "", "", null, 1, true);
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay resultados")
    void execute_SinResultados_DebeRetornarListaVacia() {
        // Given
        when(productoRepository.findProductosSaldosPreciosWeb(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), anyInt(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        when(productoMapper.toResponseList(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        // When
        List<ProductoResponse> resultado = useCase.execute(requestValido);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la solicitud es nula")
    void execute_ConSolicitudNula_DebeLanzarExcepcion() {
        // When & Then
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("La solicitud no puede ser nula");

        verify(productoRepository, never()).findProductosSaldosPreciosWeb(
                anyString(), anyString(), anyString(), anyString(), anyString(), any(), anyInt(), anyBoolean());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando empresa es nula o vacía")
    void execute_ConEmpresaNulaOVacia_DebeLanzarExcepcion() {
        // Given
        ProductoRequest requestSinEmpresa = new ProductoRequest(
                null, "BOD-001", null, null, null, null, null
        );

        // When & Then
        assertThatThrownBy(() -> useCase.execute(requestSinEmpresa))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El código de empresa es obligatorio");

        verify(productoRepository, never()).findProductosSaldosPreciosWeb(
                anyString(), anyString(), anyString(), anyString(), anyString(), any(), anyInt(), anyBoolean());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando bodega es nula o vacía")
    void execute_ConBodegaNulaOVacia_DebeLanzarExcepcion() {
        // Given
        ProductoRequest requestSinBodega = new ProductoRequest(
                "01", null, null, null, null, null, null
        );

        // When & Then
        assertThatThrownBy(() -> useCase.execute(requestSinBodega))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El código de bodega es obligatorio");

        verify(productoRepository, never()).findProductosSaldosPreciosWeb(
                anyString(), anyString(), anyString(), anyString(), anyString(), any(), anyInt(), anyBoolean());
    }
}

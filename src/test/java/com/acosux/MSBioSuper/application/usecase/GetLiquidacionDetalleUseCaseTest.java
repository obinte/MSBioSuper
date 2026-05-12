package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.LiquidacionRequest;
import com.acosux.MSBioSuper.application.dto.LiquidacionResponse;
import com.acosux.MSBioSuper.application.mapper.LiquidacionMapper;
import com.acosux.MSBioSuper.domain.model.PrdLiquidacionesDetalleTO;
import com.acosux.MSBioSuper.domain.repository.LiquidacionRepository;
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
 * Pruebas unitarias para GetLiquidacionDetalleUseCase.
 * Valida la lógica de negocio para consultar liquidaciones detalladas.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetLiquidacionDetalleUseCase - Pruebas Unitarias")
class GetLiquidacionDetalleUseCaseTest {

    @Mock
    private LiquidacionRepository liquidacionRepository;

    @Mock
    private LiquidacionMapper liquidacionMapper;

    @InjectMocks
    private GetLiquidacionDetalleUseCase useCase;

    private LiquidacionRequest requestValido;
    private PrdLiquidacionesDetalleTO entidadMock;
    private LiquidacionResponse responseMock;

    @BeforeEach
    void setUp() {
        requestValido = new LiquidacionRequest(
                "01",
                "2024-01-01",
                "2024-01-31",
                "SECTOR-A",
                null,
                null,
                null,
                null
        );

        entidadMock = new PrdLiquidacionesDetalleTO();
        entidadMock.setId(1L);
        entidadMock.setLiqNumero("LIQ-001");
        entidadMock.setLiqFecha("2024-01-15");
        entidadMock.setLiqCliente("Cliente ABC");
        entidadMock.setLiqLote("LOTE-001");
        entidadMock.setLiqCorrida("C-2024-01");
        entidadMock.setLiqLibrasEnviadas(BigDecimal.valueOf(1000.0));
        entidadMock.setLiqLibrasRecibidas(BigDecimal.valueOf(980.0));
        entidadMock.setLiqDiferencia(BigDecimal.valueOf(20.0));
        entidadMock.setLiqLibrasBasura(BigDecimal.valueOf(10.0));
        entidadMock.setLiqLibrasNetas(BigDecimal.valueOf(970.0));
        entidadMock.setLiqLibrasEntero(BigDecimal.valueOf(850.0));
        entidadMock.setLiqRendimiento(BigDecimal.valueOf(87.6));
        entidadMock.setLiqTotalMonto(BigDecimal.valueOf(4850.0));
        entidadMock.setLiqPrecioLibraPromedio(BigDecimal.valueOf(5.0));
        entidadMock.setLiqTipo("VENTA");
        entidadMock.setLiqIdCliente("CLI-001");

        responseMock = new LiquidacionResponse(
                1L, "LIQ-001", "2024-01-15", "Cliente ABC", "LOTE-001", "C-2024-01",
                BigDecimal.valueOf(1000.0), BigDecimal.valueOf(980.0), BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(10.0), BigDecimal.valueOf(970.0), BigDecimal.valueOf(850.0),
                BigDecimal.valueOf(87.6), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(4850.0),
                BigDecimal.valueOf(5.0), "01", "VENTA", BigDecimal.ZERO, "CLI-001"
        );
    }

    @Test
    @DisplayName("Debe retornar lista de liquidaciones cuando los parámetros son válidos")
    void execute_ConParametrosValidos_DebeRetornarListaDeRespuestas() {
        // Given
        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidadMock);
        List<LiquidacionResponse> respuestasEsperadas = Arrays.asList(responseMock);

        when(liquidacionRepository.findLiquidacionDetalleProducto(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), isNull(), isNull()))
                .thenReturn(entidades);
        when(liquidacionMapper.toResponseList(entidades)).thenReturn(respuestasEsperadas);

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestValido);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(responseMock);

        verify(liquidacionRepository).findLiquidacionDetalleProducto(
                "01", "SECTOR-A", null, "2024-01-01", "2024-01-31", null, null, null);
        verify(liquidacionMapper).toResponseList(entidades);
    }

    @Test
    @DisplayName("Debe incluir piscina en la consulta cuando se especifica")
    void execute_ConPiscina_DebeIncluirEnConsulta() {
        // Given
        LiquidacionRequest requestConPiscina = new LiquidacionRequest(
                "01", "2024-01-01", "2024-01-31", "SECTOR-A", "P-001", null, null, null
        );

        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidadMock);

        when(liquidacionRepository.findLiquidacionDetalleProducto(
                anyString(), anyString(), anyString(), anyString(), anyString(), isNull(), isNull(), isNull()))
                .thenReturn(entidades);
        when(liquidacionMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestConPiscina);

        // Then
        assertThat(resultado).isNotNull();
        verify(liquidacionRepository).findLiquidacionDetalleProducto(
                "01", "SECTOR-A", "P-001", "2024-01-01", "2024-01-31", null, null, null);
    }

    @Test
    @DisplayName("Debe filtrar por corrida cuando se especifica")
    void execute_ConFiltroCorrida_DebeAplicarFiltro() {
        // Given
        LiquidacionRequest requestConCorrida = new LiquidacionRequest(
                "01", "2024-01-01", "2024-01-31", "SECTOR-A", null, "C-2024-01", null, null
        );

        PrdLiquidacionesDetalleTO entidad1 = new PrdLiquidacionesDetalleTO();
        entidad1.setLiqCorrida("C-2024-01");
        entidad1.setLiqNumero("LIQ-001");

        PrdLiquidacionesDetalleTO entidad2 = new PrdLiquidacionesDetalleTO();
        entidad2.setLiqCorrida("C-2024-02");
        entidad2.setLiqNumero("LIQ-002");

        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidad1, entidad2);

        when(liquidacionRepository.findLiquidacionDetalleProducto(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), isNull(), isNull()))
                .thenReturn(entidades);
        when(liquidacionMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestConCorrida);

        // Then
        assertThat(resultado).isNotNull();
        verify(liquidacionMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getLiqCorrida().equals("C-2024-01")
        ));
    }

    @Test
    @DisplayName("Debe incluir cliente en la consulta cuando se especifica")
    void execute_ConCliente_DebeIncluirEnConsulta() {
        // Given
        LiquidacionRequest requestConCliente = new LiquidacionRequest(
                "01", "2024-01-01", "2024-01-31", "SECTOR-A", null, null, "CLI-001", null
        );

        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidadMock);

        when(liquidacionRepository.findLiquidacionDetalleProducto(
                anyString(), anyString(), isNull(), anyString(), anyString(), anyString(), isNull(), isNull()))
                .thenReturn(entidades);
        when(liquidacionMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestConCliente);

        // Then
        assertThat(resultado).isNotNull();
        verify(liquidacionRepository).findLiquidacionDetalleProducto(
                "01", "SECTOR-A", null, "2024-01-01", "2024-01-31", "CLI-001", null, null);
    }

    @Test
    @DisplayName("Debe incluir tipo en la consulta cuando se especifica")
    void execute_ConTipo_DebeIncluirEnConsulta() {
        // Given
        LiquidacionRequest requestConTipo = new LiquidacionRequest(
                "01", "2024-01-01", "2024-01-31", "SECTOR-A", null, null, null, "VENTA"
        );

        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidadMock);

        when(liquidacionRepository.findLiquidacionDetalleProducto(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), isNull(), anyString()))
                .thenReturn(entidades);
        when(liquidacionMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestConTipo);

        // Then
        assertThat(resultado).isNotNull();
        verify(liquidacionRepository).findLiquidacionDetalleProducto(
                "01", "SECTOR-A", null, "2024-01-01", "2024-01-31", null, null, "VENTA");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay resultados")
    void execute_SinResultados_DebeRetornarListaVacia() {
        // Given
        when(liquidacionRepository.findLiquidacionDetalleProducto(
                anyString(), anyString(), isNull(), anyString(), anyString(), isNull(), isNull(), isNull()))
                .thenReturn(Collections.emptyList());
        when(liquidacionMapper.toResponseList(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestValido);

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

        verify(liquidacionRepository, never()).findLiquidacionDetalleProducto(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }
}

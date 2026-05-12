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
 * Pruebas unitarias para ListarLiquidacionesUseCase.
 * Valida la lógica de negocio para listar liquidaciones.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ListarLiquidacionesUseCase - Pruebas Unitarias")
class ListarLiquidacionesUseCaseTest {

    @Mock
    private LiquidacionRepository liquidacionRepository;

    @Mock
    private LiquidacionMapper liquidacionMapper;

    @InjectMocks
    private ListarLiquidacionesUseCase useCase;

    private LiquidacionRequest requestValido;
    private PrdLiquidacionesDetalleTO entidadMock;
    private LiquidacionResponse responseMock;

    @BeforeEach
    void setUp() {
        requestValido = new LiquidacionRequest(
                "01",
                "2024-01-01",
                "2024-12-31",
                null,
                null,
                null,
                null,
                null
        );

        entidadMock = new PrdLiquidacionesDetalleTO();
        entidadMock.setId(1L);
        entidadMock.setLiqNumero("LIQ-001");
        entidadMock.setLiqFecha("2024-01-15");
        entidadMock.setLiqCliente("Cliente XYZ");
        entidadMock.setLiqIdCliente("CLI-001");
        entidadMock.setLiqCorrida("C-2024-01");
        entidadMock.setLiqTipo("VENTA");
        entidadMock.setLiqTotalMonto(BigDecimal.valueOf(5000.0));

        responseMock = new LiquidacionResponse(
                1L, "LIQ-001", "2024-01-15", "Cliente XYZ", "LOTE-001", "C-2024-01",
                BigDecimal.valueOf(1000.0), BigDecimal.valueOf(980.0), BigDecimal.valueOf(20.0),
                BigDecimal.valueOf(10.0), BigDecimal.valueOf(970.0), BigDecimal.valueOf(850.0),
                BigDecimal.valueOf(87.6), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(5000.0),
                BigDecimal.valueOf(5.0), "01", "VENTA", BigDecimal.ZERO, "CLI-001"
        );
    }

    @Test
    @DisplayName("Debe retornar lista de liquidaciones cuando los parámetros son válidos")
    void execute_ConParametrosValidos_DebeRetornarListaDeRespuestas() {
        // Given
        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidadMock);
        List<LiquidacionResponse> respuestasEsperadas = Arrays.asList(responseMock);

        when(liquidacionRepository.findLiquidacionesDetalle(
                anyString(), anyString(), anyString()))
                .thenReturn(entidades);
        when(liquidacionMapper.toResponseList(entidades)).thenReturn(respuestasEsperadas);

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestValido);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(responseMock);

        verify(liquidacionRepository).findLiquidacionesDetalle(
                "01", "2024-01-01", "2024-12-31");
        verify(liquidacionMapper).toResponseList(entidades);
    }

    @Test
    @DisplayName("Debe filtrar por cliente cuando se especifica")
    void execute_ConFiltroCliente_DebeAplicarFiltro() {
        // Given
        LiquidacionRequest requestConCliente = new LiquidacionRequest(
                "01", "2024-01-01", "2024-12-31", null, null, null, "CLI-001", null
        );

        PrdLiquidacionesDetalleTO entidad1 = new PrdLiquidacionesDetalleTO();
        entidad1.setLiqCliente("Cliente XYZ");
        entidad1.setLiqIdCliente("CLI-001");

        PrdLiquidacionesDetalleTO entidad2 = new PrdLiquidacionesDetalleTO();
        entidad2.setLiqCliente("Cliente ABC");
        entidad2.setLiqIdCliente("CLI-002");

        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidad1, entidad2);

        when(liquidacionRepository.findLiquidacionesDetalle(
                anyString(), anyString(), anyString()))
                .thenReturn(entidades);
        when(liquidacionMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestConCliente);

        // Then
        assertThat(resultado).isNotNull();
        verify(liquidacionMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getLiqIdCliente().equals("CLI-001")
        ));
    }

    @Test
    @DisplayName("Debe filtrar por tipo cuando se especifica")
    void execute_ConFiltroTipo_DebeAplicarFiltro() {
        // Given
        LiquidacionRequest requestConTipo = new LiquidacionRequest(
                "01", "2024-01-01", "2024-12-31", null, null, null, null, "VENTA"
        );

        PrdLiquidacionesDetalleTO entidad1 = new PrdLiquidacionesDetalleTO();
        entidad1.setLiqTipo("VENTA");
        entidad1.setLiqNumero("LIQ-001");

        PrdLiquidacionesDetalleTO entidad2 = new PrdLiquidacionesDetalleTO();
        entidad2.setLiqTipo("COMPRA");
        entidad2.setLiqNumero("LIQ-002");

        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidad1, entidad2);

        when(liquidacionRepository.findLiquidacionesDetalle(
                anyString(), anyString(), anyString()))
                .thenReturn(entidades);
        when(liquidacionMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestConTipo);

        // Then
        assertThat(resultado).isNotNull();
        verify(liquidacionMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getLiqTipo().equals("VENTA")
        ));
    }

    @Test
    @DisplayName("Debe filtrar por corrida cuando se especifica")
    void execute_ConFiltroCorrida_DebeAplicarFiltro() {
        // Given
        LiquidacionRequest requestConCorrida = new LiquidacionRequest(
                "01", "2024-01-01", "2024-12-31", null, null, "C-2024-01", null, null
        );

        PrdLiquidacionesDetalleTO entidad1 = new PrdLiquidacionesDetalleTO();
        entidad1.setLiqCorrida("C-2024-01");
        entidad1.setLiqNumero("LIQ-001");

        PrdLiquidacionesDetalleTO entidad2 = new PrdLiquidacionesDetalleTO();
        entidad2.setLiqCorrida("C-2024-02");
        entidad2.setLiqNumero("LIQ-002");

        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidad1, entidad2);

        when(liquidacionRepository.findLiquidacionesDetalle(
                anyString(), anyString(), anyString()))
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
    @DisplayName("Debe aplicar múltiples filtros cuando se especifican")
    void execute_ConMultiplesFiltros_DebeAplicarTodos() {
        // Given
        LiquidacionRequest requestConMultiplesFiltros = new LiquidacionRequest(
                "01", "2024-01-01", "2024-12-31", null, null, "C-2024-01", "CLI-001", "VENTA"
        );

        PrdLiquidacionesDetalleTO entidad1 = new PrdLiquidacionesDetalleTO();
        entidad1.setLiqCorrida("C-2024-01");
        entidad1.setLiqIdCliente("CLI-001");
        entidad1.setLiqTipo("VENTA");

        PrdLiquidacionesDetalleTO entidad2 = new PrdLiquidacionesDetalleTO();
        entidad2.setLiqCorrida("C-2024-01");
        entidad2.setLiqIdCliente("CLI-002");
        entidad2.setLiqTipo("VENTA");

        PrdLiquidacionesDetalleTO entidad3 = new PrdLiquidacionesDetalleTO();
        entidad3.setLiqCorrida("C-2024-02");
        entidad3.setLiqIdCliente("CLI-001");
        entidad3.setLiqTipo("COMPRA");

        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidad1, entidad2, entidad3);

        when(liquidacionRepository.findLiquidacionesDetalle(
                anyString(), anyString(), anyString()))
                .thenReturn(entidades);
        when(liquidacionMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestConMultiplesFiltros);

        // Then
        assertThat(resultado).isNotNull();
        verify(liquidacionMapper).toResponseList(argThat(lista ->
                lista.size() == 1 &&
                lista.get(0).getLiqCorrida().equals("C-2024-01") &&
                lista.get(0).getLiqIdCliente().equals("CLI-001") &&
                lista.get(0).getLiqTipo().equals("VENTA")
        ));
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay resultados")
    void execute_SinResultados_DebeRetornarListaVacia() {
        // Given
        when(liquidacionRepository.findLiquidacionesDetalle(
                anyString(), anyString(), anyString()))
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

        verify(liquidacionRepository, never()).findLiquidacionesDetalle(
                anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Debe ignorar filtros vacíos o en blanco")
    void execute_ConFiltrosVacios_DebeIgnorarFiltros() {
        // Given
        LiquidacionRequest requestConFiltrosVacios = new LiquidacionRequest(
                "01", "2024-01-01", "2024-12-31", null, null, "  ", "", "  "
        );

        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidadMock);

        when(liquidacionRepository.findLiquidacionesDetalle(
                anyString(), anyString(), anyString()))
                .thenReturn(entidades);
        when(liquidacionMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<LiquidacionResponse> resultado = useCase.execute(requestConFiltrosVacios);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        verify(liquidacionMapper).toResponseList(entidades);
    }
}

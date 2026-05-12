package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ResumenSiembraRequest;
import com.acosux.MSBioSuper.application.dto.ResumenSiembraResponse;
import com.acosux.MSBioSuper.application.mapper.ResumenSiembraMapper;
import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import com.acosux.MSBioSuper.domain.repository.ResumenSiembraRepository;
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
 * Pruebas unitarias para GetResumenSiembraUseCase.
 * Valida la lógica de negocio para consultar resúmenes de siembra.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetResumenSiembraUseCase - Pruebas Unitarias")
class GetResumenSiembraUseCaseTest {

    @Mock
    private ResumenSiembraRepository resumenSiembraRepository;

    @Mock
    private ResumenSiembraMapper resumenSiembraMapper;

    @InjectMocks
    private GetResumenSiembraUseCase useCase;

    private ResumenSiembraRequest requestValido;
    private PrdResumenCorridaTO entidadMock;
    private ResumenSiembraResponse responseMock;

    @BeforeEach
    void setUp() {
        requestValido = new ResumenSiembraRequest(
                "01",
                "SECTOR-B",
                "2024-01-01",
                "2024-12-31",
                null,
                null
        );

        entidadMock = new PrdResumenCorridaTO();
        entidadMock.setId(1L);
        entidadMock.setPisNumero("P-002");
        entidadMock.setRcCorridaNumero("S-2024-01");
        entidadMock.setRcNumeroLarvas(50000L);

        responseMock = new ResumenSiembraResponse(
                1L, "P-002", "01", "SECTOR-B", BigDecimal.valueOf(8.0),
                "S-2024-01", "2024-01-01", "2024-01-10", null,
                "2024-06-30", 0, 0, 0, 50000L, "LOTE-002",
                BigDecimal.valueOf(6.25), "LAB-B", "NAUPLIO-2",
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, null, null,
                BigDecimal.ZERO, null, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, null
        );
    }

    @Test
    @DisplayName("Debe retornar lista de resúmenes cuando los parámetros son válidos")
    void execute_ConParametrosValidos_DebeRetornarListaDeRespuestas() {
        // Given
        List<PrdResumenCorridaTO> entidades = Arrays.asList(entidadMock);
        List<ResumenSiembraResponse> respuestasEsperadas = Arrays.asList(responseMock);

        when(resumenSiembraRepository.findResumenSiembraMejorado(
                anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(entidades);
        when(resumenSiembraMapper.toResponseList(entidades)).thenReturn(respuestasEsperadas);

        // When
        List<ResumenSiembraResponse> resultado = useCase.execute(requestValido);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(responseMock);

        verify(resumenSiembraRepository).findResumenSiembraMejorado(
                "01", "SECTOR-B", "2024-01-01", "2024-12-31", true);
        verify(resumenSiembraMapper).toResponseList(entidades);
    }

    @Test
    @DisplayName("Debe filtrar por piscina cuando se especifica")
    void execute_ConFiltroPiscina_DebeAplicarFiltro() {
        // Given
        ResumenSiembraRequest requestConPiscina = new ResumenSiembraRequest(
                "01", "SECTOR-B", "2024-01-01", "2024-12-31", "P-002", null
        );

        PrdResumenCorridaTO entidad1 = new PrdResumenCorridaTO();
        entidad1.setPisNumero("P-002");
        entidad1.setRcCorridaNumero("S-001");

        PrdResumenCorridaTO entidad2 = new PrdResumenCorridaTO();
        entidad2.setPisNumero("P-003");
        entidad2.setRcCorridaNumero("S-002");

        List<PrdResumenCorridaTO> entidades = Arrays.asList(entidad1, entidad2);

        when(resumenSiembraRepository.findResumenSiembraMejorado(
                anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(entidades);
        when(resumenSiembraMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<ResumenSiembraResponse> resultado = useCase.execute(requestConPiscina);

        // Then
        assertThat(resultado).isNotNull();
        verify(resumenSiembraMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getPisNumero().equals("P-002")
        ));
    }

    @Test
    @DisplayName("Debe filtrar por corrida cuando se especifica")
    void execute_ConFiltroCorrida_DebeAplicarFiltro() {
        // Given
        ResumenSiembraRequest requestConCorrida = new ResumenSiembraRequest(
                "01", "SECTOR-B", "2024-01-01", "2024-12-31", null, "S-2024-01"
        );

        PrdResumenCorridaTO entidad1 = new PrdResumenCorridaTO();
        entidad1.setPisNumero("P-002");
        entidad1.setRcCorridaNumero("S-2024-01");

        PrdResumenCorridaTO entidad2 = new PrdResumenCorridaTO();
        entidad2.setPisNumero("P-003");
        entidad2.setRcCorridaNumero("S-2024-02");

        List<PrdResumenCorridaTO> entidades = Arrays.asList(entidad1, entidad2);

        when(resumenSiembraRepository.findResumenSiembraMejorado(
                anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(entidades);
        when(resumenSiembraMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<ResumenSiembraResponse> resultado = useCase.execute(requestConCorrida);

        // Then
        assertThat(resultado).isNotNull();
        verify(resumenSiembraMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getRcCorridaNumero().equals("S-2024-01")
        ));
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay resultados")
    void execute_SinResultados_DebeRetornarListaVacia() {
        // Given
        when(resumenSiembraRepository.findResumenSiembraMejorado(
                anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        when(resumenSiembraMapper.toResponseList(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        // When
        List<ResumenSiembraResponse> resultado = useCase.execute(requestValido);

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

        verify(resumenSiembraRepository, never()).findResumenSiembraMejorado(
                anyString(), anyString(), anyString(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("Debe aplicar ambos filtros cuando se especifican piscina y corrida")
    void execute_ConAmbosFiltros_DebeAplicarAmbos() {
        // Given
        ResumenSiembraRequest requestConAmbos = new ResumenSiembraRequest(
                "01", "SECTOR-B", "2024-01-01", "2024-12-31", "P-002", "S-2024-01"
        );

        PrdResumenCorridaTO entidad1 = new PrdResumenCorridaTO();
        entidad1.setPisNumero("P-002");
        entidad1.setRcCorridaNumero("S-2024-01");

        PrdResumenCorridaTO entidad2 = new PrdResumenCorridaTO();
        entidad2.setPisNumero("P-002");
        entidad2.setRcCorridaNumero("S-2024-02");

        PrdResumenCorridaTO entidad3 = new PrdResumenCorridaTO();
        entidad3.setPisNumero("P-003");
        entidad3.setRcCorridaNumero("S-2024-01");

        List<PrdResumenCorridaTO> entidades = Arrays.asList(entidad1, entidad2, entidad3);

        when(resumenSiembraRepository.findResumenSiembraMejorado(
                anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(entidades);
        when(resumenSiembraMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<ResumenSiembraResponse> resultado = useCase.execute(requestConAmbos);

        // Then
        assertThat(resultado).isNotNull();
        verify(resumenSiembraMapper).toResponseList(argThat(lista ->
                lista.size() == 1 &&
                lista.get(0).getPisNumero().equals("P-002") &&
                lista.get(0).getRcCorridaNumero().equals("S-2024-01")
        ));
    }
}

package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ResumenPescaRequest;
import com.acosux.MSBioSuper.application.dto.ResumenPescaResponse;
import com.acosux.MSBioSuper.application.mapper.ResumenPescaMapper;
import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import com.acosux.MSBioSuper.domain.repository.ResumenPescaRepository;
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
 * Pruebas unitarias para GetResumenPescaUseCase.
 * Valida la lógica de negocio para consultar resúmenes de pesca.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetResumenPescaUseCase - Pruebas Unitarias")
class GetResumenPescaUseCaseTest {

    @Mock
    private ResumenPescaRepository resumenPescaRepository;

    @Mock
    private ResumenPescaMapper resumenPescaMapper;

    @InjectMocks
    private GetResumenPescaUseCase useCase;

    private ResumenPescaRequest requestValido;
    private PrdResumenCorridaTO entidadMock;
    private ResumenPescaResponse responseMock;

    @BeforeEach
    void setUp() {
        // Given - Preparar datos de prueba
        requestValido = new ResumenPescaRequest(
                "01",
                "SECTOR-A",
                "2024-01-01",
                "2024-12-31",
                null,
                null
        );

        entidadMock = new PrdResumenCorridaTO();
        entidadMock.setId(1L);
        entidadMock.setPisNumero("P-001");
        entidadMock.setRcCorridaNumero("C-2024-01");
        entidadMock.setRcBiomasa(BigDecimal.valueOf(1000.50));

        responseMock = new ResumenPescaResponse(
                1L, "P-001", "01", "SECTOR-A", BigDecimal.valueOf(10.5),
                "C-2024-01", "2024-01-01", "2024-01-15", "2024-06-15",
                "2024-06-30", 150, 0, 0, 100000L, "LOTE-001",
                BigDecimal.valueOf(10.0), "LAB-A", "NAUPLIO-1",
                BigDecimal.valueOf(5000.0), BigDecimal.ZERO, BigDecimal.valueOf(2500.0),
                BigDecimal.ZERO, BigDecimal.valueOf(1000.50), BigDecimal.valueOf(1000.50),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(1.5),
                BigDecimal.valueOf(20.0), BigDecimal.valueOf(15.0), BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(15.0), BigDecimal.valueOf(0.5), BigDecimal.valueOf(18.0),
                BigDecimal.valueOf(85.0), BigDecimal.valueOf(10000.0), BigDecimal.valueOf(8000.0),
                BigDecimal.valueOf(2000.0), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.valueOf(12000.0), BigDecimal.ZERO, BigDecimal.valueOf(2000.0),
                BigDecimal.valueOf(95.0), BigDecimal.valueOf(950.0), BigDecimal.valueOf(1140.0),
                BigDecimal.valueOf(190.0), BigDecimal.valueOf(12.5), BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(12.0), BigDecimal.valueOf(2.0), BigDecimal.valueOf(6.5),
                BigDecimal.valueOf(5.2), BigDecimal.valueOf(1.3), BigDecimal.valueOf(1.0),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(500.0),
                null, null, BigDecimal.valueOf(100.0), "VENTA", BigDecimal.ZERO,
                BigDecimal.valueOf(1000.0), BigDecimal.valueOf(16.0), "EMP-001"
        );
    }

    @Test
    @DisplayName("Debe retornar lista de resúmenes cuando los parámetros son válidos")
    void execute_ConParametrosValidos_DebeRetornarListaDeRespuestas() {
        // Given
        List<PrdResumenCorridaTO> entidades = Arrays.asList(entidadMock);
        List<ResumenPescaResponse> respuestasEsperadas = Arrays.asList(responseMock);

        when(resumenPescaRepository.findResumenCorridaMejorado(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(entidades);
        when(resumenPescaMapper.toResponseList(entidades)).thenReturn(respuestasEsperadas);

        // When
        List<ResumenPescaResponse> resultado = useCase.execute(requestValido);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(responseMock);

        verify(resumenPescaRepository).findResumenCorridaMejorado(
                "01", "SECTOR-A", "PESCA*", "2024-01-01", "2024-12-31", true);
        verify(resumenPescaMapper).toResponseList(entidades);
    }

    @Test
    @DisplayName("Debe filtrar por piscina cuando se especifica en la solicitud")
    void execute_ConFiltroPiscina_DebeAplicarFiltro() {
        // Given
        ResumenPescaRequest requestConPiscina = new ResumenPescaRequest(
                "01", "SECTOR-A", "2024-01-01", "2024-12-31", "P-001", null
        );

        PrdResumenCorridaTO entidad1 = new PrdResumenCorridaTO();
        entidad1.setPisNumero("P-001");
        entidad1.setRcCorridaNumero("C-001");

        PrdResumenCorridaTO entidad2 = new PrdResumenCorridaTO();
        entidad2.setPisNumero("P-002");
        entidad2.setRcCorridaNumero("C-002");

        List<PrdResumenCorridaTO> entidades = Arrays.asList(entidad1, entidad2);

        when(resumenPescaRepository.findResumenCorridaMejorado(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(entidades);
        when(resumenPescaMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<ResumenPescaResponse> resultado = useCase.execute(requestConPiscina);

        // Then
        assertThat(resultado).isNotNull();
        verify(resumenPescaMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getPisNumero().equals("P-001")
        ));
    }

    @Test
    @DisplayName("Debe filtrar por corrida cuando se especifica en la solicitud")
    void execute_ConFiltroCorrida_DebeAplicarFiltro() {
        // Given
        ResumenPescaRequest requestConCorrida = new ResumenPescaRequest(
                "01", "SECTOR-A", "2024-01-01", "2024-12-31", null, "C-2024-01"
        );

        PrdResumenCorridaTO entidad1 = new PrdResumenCorridaTO();
        entidad1.setPisNumero("P-001");
        entidad1.setRcCorridaNumero("C-2024-01");

        PrdResumenCorridaTO entidad2 = new PrdResumenCorridaTO();
        entidad2.setPisNumero("P-002");
        entidad2.setRcCorridaNumero("C-2024-02");

        List<PrdResumenCorridaTO> entidades = Arrays.asList(entidad1, entidad2);

        when(resumenPescaRepository.findResumenCorridaMejorado(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(entidades);
        when(resumenPescaMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<ResumenPescaResponse> resultado = useCase.execute(requestConCorrida);

        // Then
        assertThat(resultado).isNotNull();
        verify(resumenPescaMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getRcCorridaNumero().equals("C-2024-01")
        ));
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay resultados")
    void execute_SinResultados_DebeRetornarListaVacia() {
        // Given
        when(resumenPescaRepository.findResumenCorridaMejorado(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(Collections.emptyList());
        when(resumenPescaMapper.toResponseList(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        // When
        List<ResumenPescaResponse> resultado = useCase.execute(requestValido);

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

        verify(resumenPescaRepository, never()).findResumenCorridaMejorado(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean());
    }

    @Test
    @DisplayName("Debe ignorar filtros vacíos o en blanco")
    void execute_ConFiltrosVacios_DebeIgnorarFiltros() {
        // Given
        ResumenPescaRequest requestConFiltrosVacios = new ResumenPescaRequest(
                "01", "SECTOR-A", "2024-01-01", "2024-12-31", "", "  "
        );

        List<PrdResumenCorridaTO> entidades = Arrays.asList(entidadMock);

        when(resumenPescaRepository.findResumenCorridaMejorado(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean()))
                .thenReturn(entidades);
        when(resumenPescaMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<ResumenPescaResponse> resultado = useCase.execute(requestConFiltrosVacios);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        verify(resumenPescaMapper).toResponseList(entidades);
    }
}

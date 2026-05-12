package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ConsumosDiariosRequest;
import com.acosux.MSBioSuper.application.dto.ConsumosDiariosResponse;
import com.acosux.MSBioSuper.application.mapper.ConsumosDiariosMapper;
import com.acosux.MSBioSuper.domain.model.PrdConsumosDiarioMSTO;
import com.acosux.MSBioSuper.domain.repository.ConsumosDiariosRepository;
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
 * Pruebas unitarias para GetConsumosDiariosUseCase.
 * Valida la lógica de negocio para consultar consumos diarios.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetConsumosDiariosUseCase - Pruebas Unitarias")
class GetConsumosDiariosUseCaseTest {

    @Mock
    private ConsumosDiariosRepository consumosDiariosRepository;

    @Mock
    private ConsumosDiariosMapper consumosDiariosMapper;

    @InjectMocks
    private GetConsumosDiariosUseCase useCase;

    private ConsumosDiariosRequest requestValido;
    private PrdConsumosDiarioMSTO entidadMock;
    private ConsumosDiariosResponse responseMock;

    @BeforeEach
    void setUp() {
        requestValido = new ConsumosDiariosRequest(
                "01",
                "SECTOR-A",
                "2024-01-01",
                "2024-01-31",
                null,
                null,
                null
        );

        entidadMock = new PrdConsumosDiarioMSTO();
        entidadMock.setId(1L);
        entidadMock.setConsumoEmpresa("01");
        entidadMock.setConsumoSector("SECTOR-A");
        entidadMock.setConsumoPiscina("P-001");
        entidadMock.setConsumoFecha("2024-01-15");
        entidadMock.setConsumoProducto("Balanceado Premium");
        entidadMock.setConsumoCodigo("BAL-001");
        entidadMock.setConsumoMedida("KG");
        entidadMock.setConsumoTotal(BigDecimal.valueOf(500.0));
        entidadMock.setConsumoCostoTotal(BigDecimal.valueOf(750.0));
        entidadMock.setConsumoCorrida("C-2024-01");

        responseMock = new ConsumosDiariosResponse(
                1L, "01", "SECTOR-A", "P-001", "2024-01-15",
                "Balanceado Premium", "BAL-001", "KG",
                BigDecimal.valueOf(500.0), BigDecimal.valueOf(750.0), "C-2024-01"
        );
    }

    @Test
    @DisplayName("Debe retornar lista de consumos cuando los parámetros son válidos")
    void execute_ConParametrosValidos_DebeRetornarListaDeRespuestas() {
        // Given
        List<PrdConsumosDiarioMSTO> entidades = Arrays.asList(entidadMock);
        List<ConsumosDiariosResponse> respuestasEsperadas = Arrays.asList(responseMock);

        when(consumosDiariosRepository.findConsumosDiariosMicroservice(
                anyString(), anyString(), isNull(), isNull(), isNull(), anyString(), anyString()))
                .thenReturn(entidades);
        when(consumosDiariosMapper.toResponseList(entidades)).thenReturn(respuestasEsperadas);

        // When
        List<ConsumosDiariosResponse> resultado = useCase.execute(requestValido);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(responseMock);

        verify(consumosDiariosRepository).findConsumosDiariosMicroservice(
                "01", "SECTOR-A", null, null, null, "2024-01-01", "2024-01-31");
        verify(consumosDiariosMapper).toResponseList(entidades);
    }

    @Test
    @DisplayName("Debe incluir piscina en la consulta cuando se especifica")
    void execute_ConPiscina_DebeIncluirEnConsulta() {
        // Given
        ConsumosDiariosRequest requestConPiscina = new ConsumosDiariosRequest(
                "01", "SECTOR-A", "2024-01-01", "2024-01-31", "P-001", null, null
        );

        List<PrdConsumosDiarioMSTO> entidades = Arrays.asList(entidadMock);

        when(consumosDiariosRepository.findConsumosDiariosMicroservice(
                anyString(), anyString(), anyString(), isNull(), isNull(), anyString(), anyString()))
                .thenReturn(entidades);
        when(consumosDiariosMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<ConsumosDiariosResponse> resultado = useCase.execute(requestConPiscina);

        // Then
        assertThat(resultado).isNotNull();
        verify(consumosDiariosRepository).findConsumosDiariosMicroservice(
                "01", "SECTOR-A", "P-001", null, null, "2024-01-01", "2024-01-31");
    }

    @Test
    @DisplayName("Debe filtrar por corrida cuando se especifica")
    void execute_ConFiltroCorrida_DebeAplicarFiltro() {
        // Given
        ConsumosDiariosRequest requestConCorrida = new ConsumosDiariosRequest(
                "01", "SECTOR-A", "2024-01-01", "2024-01-31", null, "C-2024-01", null
        );

        PrdConsumosDiarioMSTO entidad1 = new PrdConsumosDiarioMSTO();
        entidad1.setConsumoCorrida("C-2024-01");
        entidad1.setConsumoProducto("Producto A");

        PrdConsumosDiarioMSTO entidad2 = new PrdConsumosDiarioMSTO();
        entidad2.setConsumoCorrida("C-2024-02");
        entidad2.setConsumoProducto("Producto B");

        List<PrdConsumosDiarioMSTO> entidades = Arrays.asList(entidad1, entidad2);

        when(consumosDiariosRepository.findConsumosDiariosMicroservice(
                anyString(), anyString(), isNull(), isNull(), isNull(), anyString(), anyString()))
                .thenReturn(entidades);
        when(consumosDiariosMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<ConsumosDiariosResponse> resultado = useCase.execute(requestConCorrida);

        // Then
        assertThat(resultado).isNotNull();
        verify(consumosDiariosMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getConsumoCorrida().equals("C-2024-01")
        ));
    }

    @Test
    @DisplayName("Debe incluir producto en la consulta cuando se especifica")
    void execute_ConProducto_DebeIncluirEnConsulta() {
        // Given
        ConsumosDiariosRequest requestConProducto = new ConsumosDiariosRequest(
                "01", "SECTOR-A", "2024-01-01", "2024-01-31", null, null, "BAL-001"
        );

        List<PrdConsumosDiarioMSTO> entidades = Arrays.asList(entidadMock);

        when(consumosDiariosRepository.findConsumosDiariosMicroservice(
                anyString(), anyString(), isNull(), isNull(), anyString(), anyString(), anyString()))
                .thenReturn(entidades);
        when(consumosDiariosMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<ConsumosDiariosResponse> resultado = useCase.execute(requestConProducto);

        // Then
        assertThat(resultado).isNotNull();
        verify(consumosDiariosRepository).findConsumosDiariosMicroservice(
                "01", "SECTOR-A", null, null, "BAL-001", "2024-01-01", "2024-01-31");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay resultados")
    void execute_SinResultados_DebeRetornarListaVacia() {
        // Given
        when(consumosDiariosRepository.findConsumosDiariosMicroservice(
                anyString(), anyString(), isNull(), isNull(), isNull(), anyString(), anyString()))
                .thenReturn(Collections.emptyList());
        when(consumosDiariosMapper.toResponseList(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        // When
        List<ConsumosDiariosResponse> resultado = useCase.execute(requestValido);

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

        verify(consumosDiariosRepository, never()).findConsumosDiariosMicroservice(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Debe ignorar filtro de corrida cuando está vacío")
    void execute_ConCorridaVacia_DebeIgnorarFiltro() {
        // Given
        ConsumosDiariosRequest requestConCorridaVacia = new ConsumosDiariosRequest(
                "01", "SECTOR-A", "2024-01-01", "2024-01-31", null, "  ", null
        );

        List<PrdConsumosDiarioMSTO> entidades = Arrays.asList(entidadMock);

        when(consumosDiariosRepository.findConsumosDiariosMicroservice(
                anyString(), anyString(), isNull(), isNull(), isNull(), anyString(), anyString()))
                .thenReturn(entidades);
        when(consumosDiariosMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<ConsumosDiariosResponse> resultado = useCase.execute(requestConCorridaVacia);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        verify(consumosDiariosMapper).toResponseList(entidades);
    }
}

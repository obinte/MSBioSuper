package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.CuentasPorCobrarRequest;
import com.acosux.MSBioSuper.application.dto.CuentasPorCobrarResponse;
import com.acosux.MSBioSuper.application.mapper.CuentasPorCobrarMapper;
import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
import com.acosux.MSBioSuper.domain.repository.CuentasPorCobrarRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para GetCuentasPorCobrarUseCase.
 * Valida la lógica de negocio para consultar cuentas por cobrar.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetCuentasPorCobrarUseCase - Pruebas Unitarias")
class GetCuentasPorCobrarUseCaseTest {

    @Mock
    private CuentasPorCobrarRepository cuentasPorCobrarRepository;

    @Mock
    private CuentasPorCobrarMapper cuentasPorCobrarMapper;

    @InjectMocks
    private GetCuentasPorCobrarUseCase useCase;

    private CuentasPorCobrarRequest requestValido;
    private CuentasPorCobrarDetalladoApiTO entidadMock;
    private CuentasPorCobrarResponse responseMock;

    @BeforeEach
    void setUp() {
        requestValido = new CuentasPorCobrarRequest(
                "01",
                "2024-12-31",
                null,
                null,
                null,
                null
        );

        entidadMock = new CuentasPorCobrarDetalladoApiTO();
        entidadMock.setId(1L);
        entidadMock.setCxcdClienteId("CLI-001");
        entidadMock.setCxcdClienteNombre("Cliente ABC");
        entidadMock.setCxcdPeriodo("2024-01");
        entidadMock.setCxcdMotivo("VENTA");
        entidadMock.setCxcdDocumentoNumero("DOC-001");
        entidadMock.setCxcdSaldo(BigDecimal.valueOf(5000.0));
        entidadMock.setCxcdFechaEmision("2024-01-15");
        entidadMock.setCxcdFechaVencimiento("2024-02-15");

        responseMock = new CuentasPorCobrarResponse(
                1L, "CLI-001", "Cliente ABC", "2024-01", "VENTA", "DOC-001",
                BigDecimal.valueOf(5000.0), "2024-01-15", "2024-02-15",
                BigDecimal.valueOf(5000.0), BigDecimal.ZERO, 30, "VIGENTE"
        );
    }

    @Test
    @DisplayName("Debe retornar lista de cuentas cuando los parámetros son válidos")
    void execute_ConParametrosValidos_DebeRetornarListaDeRespuestas() {
        // Given
        List<CuentasPorCobrarDetalladoApiTO> entidades = Arrays.asList(entidadMock);
        List<CuentasPorCobrarResponse> respuestasEsperadas = Arrays.asList(responseMock);

        when(cuentasPorCobrarRepository.findCuentasPorCobrarDetalladoApi(
                anyString(), anyString()))
                .thenReturn(entidades);
        when(cuentasPorCobrarMapper.toResponseList(entidades)).thenReturn(respuestasEsperadas);

        // When
        List<CuentasPorCobrarResponse> resultado = useCase.execute(requestValido);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(responseMock);

        verify(cuentasPorCobrarRepository).findCuentasPorCobrarDetalladoApi(
                "01", "2024-12-31");
        verify(cuentasPorCobrarMapper).toResponseList(entidades);
    }

    @Test
    @DisplayName("Debe filtrar por cliente cuando se especifica")
    void execute_ConFiltroCliente_DebeAplicarFiltro() {
        // Given
        CuentasPorCobrarRequest requestConCliente = new CuentasPorCobrarRequest(
                "01", "2024-12-31", "CLI-001", null, null, null
        );

        CuentasPorCobrarDetalladoApiTO entidad1 = new CuentasPorCobrarDetalladoApiTO();
        entidad1.setCxcdClienteId("CLI-001");
        entidad1.setCxcdClienteNombre("Cliente ABC");

        CuentasPorCobrarDetalladoApiTO entidad2 = new CuentasPorCobrarDetalladoApiTO();
        entidad2.setCxcdClienteId("CLI-002");
        entidad2.setCxcdClienteNombre("Cliente XYZ");

        List<CuentasPorCobrarDetalladoApiTO> entidades = Arrays.asList(entidad1, entidad2);

        when(cuentasPorCobrarRepository.findCuentasPorCobrarDetalladoApi(
                anyString(), anyString()))
                .thenReturn(entidades);
        when(cuentasPorCobrarMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorCobrarResponse> resultado = useCase.execute(requestConCliente);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorCobrarMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getCxcdClienteId().equals("CLI-001")
        ));
    }

    @Test
    @DisplayName("Debe filtrar por periodo cuando se especifica")
    void execute_ConFiltroPeriodo_DebeAplicarFiltro() {
        // Given
        CuentasPorCobrarRequest requestConPeriodo = new CuentasPorCobrarRequest(
                "01", "2024-12-31", null, "2024-01", null, null
        );

        CuentasPorCobrarDetalladoApiTO entidad1 = new CuentasPorCobrarDetalladoApiTO();
        entidad1.setCxcdPeriodo("2024-01");
        entidad1.setCxcdClienteId("CLI-001");

        CuentasPorCobrarDetalladoApiTO entidad2 = new CuentasPorCobrarDetalladoApiTO();
        entidad2.setCxcdPeriodo("2024-02");
        entidad2.setCxcdClienteId("CLI-002");

        List<CuentasPorCobrarDetalladoApiTO> entidades = Arrays.asList(entidad1, entidad2);

        when(cuentasPorCobrarRepository.findCuentasPorCobrarDetalladoApi(
                anyString(), anyString()))
                .thenReturn(entidades);
        when(cuentasPorCobrarMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorCobrarResponse> resultado = useCase.execute(requestConPeriodo);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorCobrarMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getCxcdPeriodo().equals("2024-01")
        ));
    }

    @Test
    @DisplayName("Debe filtrar por motivo cuando se especifica")
    void execute_ConFiltroMotivo_DebeAplicarFiltro() {
        // Given
        CuentasPorCobrarRequest requestConMotivo = new CuentasPorCobrarRequest(
                "01", "2024-12-31", null, null, "VENTA", null
        );

        CuentasPorCobrarDetalladoApiTO entidad1 = new CuentasPorCobrarDetalladoApiTO();
        entidad1.setCxcdMotivo("VENTA");
        entidad1.setCxcdClienteId("CLI-001");

        CuentasPorCobrarDetalladoApiTO entidad2 = new CuentasPorCobrarDetalladoApiTO();
        entidad2.setCxcdMotivo("SERVICIO");
        entidad2.setCxcdClienteId("CLI-002");

        List<CuentasPorCobrarDetalladoApiTO> entidades = Arrays.asList(entidad1, entidad2);

        when(cuentasPorCobrarRepository.findCuentasPorCobrarDetalladoApi(
                anyString(), anyString()))
                .thenReturn(entidades);
        when(cuentasPorCobrarMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorCobrarResponse> resultado = useCase.execute(requestConMotivo);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorCobrarMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getCxcdMotivo().equals("VENTA")
        ));
    }

    @Test
    @DisplayName("Debe filtrar por número de documento cuando se especifica")
    void execute_ConFiltroNumeroDocumento_DebeAplicarFiltro() {
        // Given
        CuentasPorCobrarRequest requestConDocumento = new CuentasPorCobrarRequest(
                "01", "2024-12-31", null, null, null, "DOC-001"
        );

        CuentasPorCobrarDetalladoApiTO entidad1 = new CuentasPorCobrarDetalladoApiTO();
        entidad1.setCxcdDocumentoNumero("DOC-001");
        entidad1.setCxcdClienteId("CLI-001");

        CuentasPorCobrarDetalladoApiTO entidad2 = new CuentasPorCobrarDetalladoApiTO();
        entidad2.setCxcdDocumentoNumero("DOC-002");
        entidad2.setCxcdClienteId("CLI-002");

        List<CuentasPorCobrarDetalladoApiTO> entidades = Arrays.asList(entidad1, entidad2);

        when(cuentasPorCobrarRepository.findCuentasPorCobrarDetalladoApi(
                anyString(), anyString()))
                .thenReturn(entidades);
        when(cuentasPorCobrarMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorCobrarResponse> resultado = useCase.execute(requestConDocumento);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorCobrarMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getCxcdDocumentoNumero().equals("DOC-001")
        ));
    }

    @Test
    @DisplayName("Debe aplicar múltiples filtros cuando se especifican")
    void execute_ConMultiplesFiltros_DebeAplicarTodos() {
        // Given
        CuentasPorCobrarRequest requestConMultiplesFiltros = new CuentasPorCobrarRequest(
                "01", "2024-12-31", "CLI-001", "2024-01", "VENTA", "DOC-001"
        );

        CuentasPorCobrarDetalladoApiTO entidad1 = new CuentasPorCobrarDetalladoApiTO();
        entidad1.setCxcdClienteId("CLI-001");
        entidad1.setCxcdPeriodo("2024-01");
        entidad1.setCxcdMotivo("VENTA");
        entidad1.setCxcdDocumentoNumero("DOC-001");

        CuentasPorCobrarDetalladoApiTO entidad2 = new CuentasPorCobrarDetalladoApiTO();
        entidad2.setCxcdClienteId("CLI-001");
        entidad2.setCxcdPeriodo("2024-02");
        entidad2.setCxcdMotivo("VENTA");
        entidad2.setCxcdDocumentoNumero("DOC-002");

        List<CuentasPorCobrarDetalladoApiTO> entidades = Arrays.asList(entidad1, entidad2);

        when(cuentasPorCobrarRepository.findCuentasPorCobrarDetalladoApi(
                anyString(), anyString()))
                .thenReturn(entidades);
        when(cuentasPorCobrarMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorCobrarResponse> resultado = useCase.execute(requestConMultiplesFiltros);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorCobrarMapper).toResponseList(argThat(lista ->
                lista.size() == 1 &&
                lista.get(0).getCxcdClienteId().equals("CLI-001") &&
                lista.get(0).getCxcdPeriodo().equals("2024-01") &&
                lista.get(0).getCxcdMotivo().equals("VENTA") &&
                lista.get(0).getCxcdDocumentoNumero().equals("DOC-001")
        ));
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay resultados")
    void execute_SinResultados_DebeRetornarListaVacia() {
        // Given
        when(cuentasPorCobrarRepository.findCuentasPorCobrarDetalladoApi(
                anyString(), anyString()))
                .thenReturn(Collections.emptyList());
        when(cuentasPorCobrarMapper.toResponseList(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        // When
        List<CuentasPorCobrarResponse> resultado = useCase.execute(requestValido);

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

        verify(cuentasPorCobrarRepository, never()).findCuentasPorCobrarDetalladoApi(
                anyString(), anyString());
    }

    @Test
    @DisplayName("Debe ignorar filtros vacíos o en blanco")
    void execute_ConFiltrosVacios_DebeIgnorarFiltros() {
        // Given
        CuentasPorCobrarRequest requestConFiltrosVacios = new CuentasPorCobrarRequest(
                "01", "2024-12-31", "  ", "", "  ", ""
        );

        List<CuentasPorCobrarDetalladoApiTO> entidades = Arrays.asList(entidadMock);

        when(cuentasPorCobrarRepository.findCuentasPorCobrarDetalladoApi(
                anyString(), anyString()))
                .thenReturn(entidades);
        when(cuentasPorCobrarMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorCobrarResponse> resultado = useCase.execute(requestConFiltrosVacios);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        verify(cuentasPorCobrarMapper).toResponseList(entidades);
    }
}

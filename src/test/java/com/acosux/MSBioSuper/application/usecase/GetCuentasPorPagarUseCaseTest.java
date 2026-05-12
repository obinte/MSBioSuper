package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.CuentasPorPagarRequest;
import com.acosux.MSBioSuper.application.dto.CuentasPorPagarResponse;
import com.acosux.MSBioSuper.application.mapper.CuentasPorPagarMapper;
import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;
import com.acosux.MSBioSuper.domain.repository.CuentasPorPagarRepository;
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
 * Pruebas unitarias para GetCuentasPorPagarUseCase.
 * Valida la lógica de negocio para consultar cuentas por pagar.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetCuentasPorPagarUseCase - Pruebas Unitarias")
class GetCuentasPorPagarUseCaseTest {

    @Mock
    private CuentasPorPagarRepository cuentasPorPagarRepository;

    @Mock
    private CuentasPorPagarMapper cuentasPorPagarMapper;

    @InjectMocks
    private GetCuentasPorPagarUseCase useCase;

    private CuentasPorPagarRequest requestValido;
    private CuentasPorPagarDetalladoTO entidadMock;
    private CuentasPorPagarResponse responseMock;

    @BeforeEach
    void setUp() {
        requestValido = new CuentasPorPagarRequest(
                "01",
                "2024-01-01",
                "2024-12-31",
                null,
                null,
                null,
                null,
                null
        );

        entidadMock = new CuentasPorPagarDetalladoTO();
        entidadMock.setId(1L);
        entidadMock.setCxpdProveedorId("PROV-001");
        entidadMock.setCxpdProveedorNombre("Proveedor ABC");
        entidadMock.setCxpdPeriodo("2024-01");
        entidadMock.setCxpdMotivo("COMPRA");
        entidadMock.setCxpdDocumentoNumero("COMP-001");
        entidadMock.setCxpdSaldo(BigDecimal.valueOf(10000.0));
        entidadMock.setCxpdFechaEmision("2024-01-10");
        entidadMock.setCxpdFechaVencimiento("2024-02-10");
        entidadMock.setCxpdZona("ZONA-A");
        entidadMock.setCxpdProvRelacionado(false);

        responseMock = new CuentasPorPagarResponse(
                1L, "PROV-001", "Proveedor ABC", "2024-01", "COMPRA", "COMP-001",
                BigDecimal.valueOf(10000.0), "2024-01-10", "2024-02-10",
                BigDecimal.valueOf(10000.0), BigDecimal.ZERO, 30, "VIGENTE",
                "ZONA-A", false
        );
    }

    @Test
    @DisplayName("Debe retornar lista de cuentas cuando los parámetros son válidos")
    void execute_ConParametrosValidos_DebeRetornarListaDeRespuestas() {
        // Given
        List<CuentasPorPagarDetalladoTO> entidades = Arrays.asList(entidadMock);
        List<CuentasPorPagarResponse> respuestasEsperadas = Arrays.asList(responseMock);

        when(cuentasPorPagarRepository.findCuentasPorPagarDetallado(
                anyString(), isNull(), isNull(), anyString(), anyString(), anyBoolean(), anyBoolean(), isNull()))
                .thenReturn(entidades);
        when(cuentasPorPagarMapper.toResponseList(entidades)).thenReturn(respuestasEsperadas);

        // When
        List<CuentasPorPagarResponse> resultado = useCase.execute(requestValido);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(responseMock);

        verify(cuentasPorPagarRepository).findCuentasPorPagarDetallado(
                "01", null, null, "2024-01-01", "2024-12-31", false, false, null);
        verify(cuentasPorPagarMapper).toResponseList(entidades);
    }

    @Test
    @DisplayName("Debe incluir proveedor en la consulta cuando se especifica")
    void execute_ConProveedor_DebeIncluirEnConsulta() {
        // Given
        CuentasPorPagarRequest requestConProveedor = new CuentasPorPagarRequest(
                "01", "2024-01-01", "2024-12-31", "PROV-001", null, null, null, null
        );

        List<CuentasPorPagarDetalladoTO> entidades = Arrays.asList(entidadMock);

        when(cuentasPorPagarRepository.findCuentasPorPagarDetallado(
                anyString(), isNull(), anyString(), anyString(), anyString(), anyBoolean(), anyBoolean(), isNull()))
                .thenReturn(entidades);
        when(cuentasPorPagarMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorPagarResponse> resultado = useCase.execute(requestConProveedor);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorPagarRepository).findCuentasPorPagarDetallado(
                "01", null, "PROV-001", "2024-01-01", "2024-12-31", false, false, null);
    }

    @Test
    @DisplayName("Debe filtrar por periodo cuando se especifica")
    void execute_ConFiltroPeriodo_DebeAplicarFiltro() {
        // Given
        CuentasPorPagarRequest requestConPeriodo = new CuentasPorPagarRequest(
                "01", "2024-01-01", "2024-12-31", null, "2024-01", null, null, null
        );

        CuentasPorPagarDetalladoTO entidad1 = new CuentasPorPagarDetalladoTO();
        entidad1.setCxpdPeriodo("2024-01");
        entidad1.setCxpdProveedorId("PROV-001");

        CuentasPorPagarDetalladoTO entidad2 = new CuentasPorPagarDetalladoTO();
        entidad2.setCxpdPeriodo("2024-02");
        entidad2.setCxpdProveedorId("PROV-002");

        List<CuentasPorPagarDetalladoTO> entidades = Arrays.asList(entidad1, entidad2);

        when(cuentasPorPagarRepository.findCuentasPorPagarDetallado(
                anyString(), isNull(), isNull(), anyString(), anyString(), anyBoolean(), anyBoolean(), isNull()))
                .thenReturn(entidades);
        when(cuentasPorPagarMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorPagarResponse> resultado = useCase.execute(requestConPeriodo);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorPagarMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getCxpdPeriodo().equals("2024-01")
        ));
    }

    @Test
    @DisplayName("Debe filtrar por motivo cuando se especifica")
    void execute_ConFiltroMotivo_DebeAplicarFiltro() {
        // Given
        CuentasPorPagarRequest requestConMotivo = new CuentasPorPagarRequest(
                "01", "2024-01-01", "2024-12-31", null, null, "COMPRA", null, null
        );

        CuentasPorPagarDetalladoTO entidad1 = new CuentasPorPagarDetalladoTO();
        entidad1.setCxpdMotivo("COMPRA");
        entidad1.setCxpdProveedorId("PROV-001");

        CuentasPorPagarDetalladoTO entidad2 = new CuentasPorPagarDetalladoTO();
        entidad2.setCxpdMotivo("SERVICIO");
        entidad2.setCxpdProveedorId("PROV-002");

        List<CuentasPorPagarDetalladoTO> entidades = Arrays.asList(entidad1, entidad2);

        when(cuentasPorPagarRepository.findCuentasPorPagarDetallado(
                anyString(), isNull(), isNull(), anyString(), anyString(), anyBoolean(), anyBoolean(), isNull()))
                .thenReturn(entidades);
        when(cuentasPorPagarMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorPagarResponse> resultado = useCase.execute(requestConMotivo);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorPagarMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getCxpdMotivo().equals("COMPRA")
        ));
    }

    @Test
    @DisplayName("Debe filtrar por zona cuando se especifica")
    void execute_ConFiltroZona_DebeAplicarFiltro() {
        // Given
        CuentasPorPagarRequest requestConZona = new CuentasPorPagarRequest(
                "01", "2024-01-01", "2024-12-31", null, null, null, "ZONA-A", null
        );

        CuentasPorPagarDetalladoTO entidad1 = new CuentasPorPagarDetalladoTO();
        entidad1.setCxpdZona("ZONA-A");
        entidad1.setCxpdProveedorId("PROV-001");

        CuentasPorPagarDetalladoTO entidad2 = new CuentasPorPagarDetalladoTO();
        entidad2.setCxpdZona("ZONA-B");
        entidad2.setCxpdProveedorId("PROV-002");

        List<CuentasPorPagarDetalladoTO> entidades = Arrays.asList(entidad1, entidad2);

        when(cuentasPorPagarRepository.findCuentasPorPagarDetallado(
                anyString(), isNull(), isNull(), anyString(), anyString(), anyBoolean(), anyBoolean(), isNull()))
                .thenReturn(entidades);
        when(cuentasPorPagarMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorPagarResponse> resultado = useCase.execute(requestConZona);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorPagarMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getCxpdZona().equals("ZONA-A")
        ));
    }

    @Test
    @DisplayName("Debe filtrar por proveedor relacionado cuando se especifica")
    void execute_ConFiltroProveedorRelacionado_DebeAplicarFiltro() {
        // Given
        CuentasPorPagarRequest requestConProvRelacionado = new CuentasPorPagarRequest(
                "01", "2024-01-01", "2024-12-31", null, null, null, null, true
        );

        CuentasPorPagarDetalladoTO entidad1 = new CuentasPorPagarDetalladoTO();
        entidad1.setCxpdProvRelacionado(true);
        entidad1.setCxpdProveedorId("PROV-001");

        CuentasPorPagarDetalladoTO entidad2 = new CuentasPorPagarDetalladoTO();
        entidad2.setCxpdProvRelacionado(false);
        entidad2.setCxpdProveedorId("PROV-002");

        List<CuentasPorPagarDetalladoTO> entidades = Arrays.asList(entidad1, entidad2);

        when(cuentasPorPagarRepository.findCuentasPorPagarDetallado(
                anyString(), isNull(), isNull(), anyString(), anyString(), anyBoolean(), anyBoolean(), isNull()))
                .thenReturn(entidades);
        when(cuentasPorPagarMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorPagarResponse> resultado = useCase.execute(requestConProvRelacionado);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorPagarMapper).toResponseList(argThat(lista ->
                lista.size() == 1 && lista.get(0).getCxpdProvRelacionado().equals(true)
        ));
    }

    @Test
    @DisplayName("Debe aplicar múltiples filtros cuando se especifican")
    void execute_ConMultiplesFiltros_DebeAplicarTodos() {
        // Given
        CuentasPorPagarRequest requestConMultiplesFiltros = new CuentasPorPagarRequest(
                "01", "2024-01-01", "2024-12-31", "PROV-001", "2024-01", "COMPRA", "ZONA-A", false
        );

        CuentasPorPagarDetalladoTO entidad1 = new CuentasPorPagarDetalladoTO();
        entidad1.setCxpdProveedorId("PROV-001");
        entidad1.setCxpdPeriodo("2024-01");
        entidad1.setCxpdMotivo("COMPRA");
        entidad1.setCxpdZona("ZONA-A");
        entidad1.setCxpdProvRelacionado(false);

        CuentasPorPagarDetalladoTO entidad2 = new CuentasPorPagarDetalladoTO();
        entidad2.setCxpdProveedorId("PROV-001");
        entidad2.setCxpdPeriodo("2024-02");
        entidad2.setCxpdMotivo("COMPRA");
        entidad2.setCxpdZona("ZONA-A");
        entidad2.setCxpdProvRelacionado(false);

        List<CuentasPorPagarDetalladoTO> entidades = Arrays.asList(entidad1, entidad2);

        when(cuentasPorPagarRepository.findCuentasPorPagarDetallado(
                anyString(), isNull(), anyString(), anyString(), anyString(), anyBoolean(), anyBoolean(), isNull()))
                .thenReturn(entidades);
        when(cuentasPorPagarMapper.toResponseList(anyList())).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorPagarResponse> resultado = useCase.execute(requestConMultiplesFiltros);

        // Then
        assertThat(resultado).isNotNull();
        verify(cuentasPorPagarMapper).toResponseList(argThat(lista ->
                lista.size() == 1 &&
                lista.get(0).getCxpdPeriodo().equals("2024-01") &&
                lista.get(0).getCxpdMotivo().equals("COMPRA") &&
                lista.get(0).getCxpdZona().equals("ZONA-A") &&
                lista.get(0).getCxpdProvRelacionado().equals(false)
        ));
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay resultados")
    void execute_SinResultados_DebeRetornarListaVacia() {
        // Given
        when(cuentasPorPagarRepository.findCuentasPorPagarDetallado(
                anyString(), isNull(), isNull(), anyString(), anyString(), anyBoolean(), anyBoolean(), isNull()))
                .thenReturn(Collections.emptyList());
        when(cuentasPorPagarMapper.toResponseList(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        // When
        List<CuentasPorPagarResponse> resultado = useCase.execute(requestValido);

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

        verify(cuentasPorPagarRepository, never()).findCuentasPorPagarDetallado(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean(), anyBoolean(), anyString());
    }

    @Test
    @DisplayName("Debe ignorar filtros vacíos o en blanco")
    void execute_ConFiltrosVacios_DebeIgnorarFiltros() {
        // Given
        CuentasPorPagarRequest requestConFiltrosVacios = new CuentasPorPagarRequest(
                "01", "2024-01-01", "2024-12-31", null, "  ", "", "  ", null
        );

        List<CuentasPorPagarDetalladoTO> entidades = Arrays.asList(entidadMock);

        when(cuentasPorPagarRepository.findCuentasPorPagarDetallado(
                anyString(), isNull(), isNull(), anyString(), anyString(), anyBoolean(), anyBoolean(), isNull()))
                .thenReturn(entidades);
        when(cuentasPorPagarMapper.toResponseList(entidades)).thenReturn(Arrays.asList(responseMock));

        // When
        List<CuentasPorPagarResponse> resultado = useCase.execute(requestConFiltrosVacios);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(1);
        verify(cuentasPorPagarMapper).toResponseList(entidades);
    }
}

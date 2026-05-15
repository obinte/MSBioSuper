package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.CuentasPorCobrarRequest;
import com.acosux.MSBioSuper.application.dto.CuentasPorCobrarResponse;
import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
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
 * Pruebas unitarias para CuentasPorCobrarMapper.
 * Valida la conversión correcta entre DTOs y entidades.
 */
@DisplayName("CuentasPorCobrarMapper - Pruebas Unitarias")
class CuentasPorCobrarMapperTest {

    private CuentasPorCobrarMapper mapper;
    private CuentasPorCobrarDetalladoApiTO entidad;
    private CuentasPorCobrarRequest request;

    @BeforeEach
    void setUp() {
        mapper = new CuentasPorCobrarMapper();

        entidad = new CuentasPorCobrarDetalladoApiTO();
        entidad.setId(1);
        entidad.setCxcdEmpresa("01");
        entidad.setCxcdPeriodo("2024-01");
        entidad.setCxcdMotivo("VENTA");
        entidad.setCxcdNumero("CXC-001");
        entidad.setCxcdDocumentoNumero("FAC-001");
        entidad.setCxcdClienteId("CLI-001");
        entidad.setCxcdFechaEmision("2024-01-15");
        entidad.setCxcdFechaVencimiento("2024-02-15");
        entidad.setCxcdRetencionValor(BigDecimal.valueOf(10.50));
        entidad.setCxcdAbono(BigDecimal.valueOf(100.00));
        entidad.setCxcdSaldo(BigDecimal.valueOf(500.00));
        entidad.setCxcdTotal(BigDecimal.valueOf(600.00));

        request = new CuentasPorCobrarRequest(
                "01",
                "2024-01-01",
                "2024-12-31",
                "CLI-001",
                "2024-01",
                "VENTA",
                "FAC-001"
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
        assertThat(resultado).containsEntry("fechaDesde", "2024-01-01");
        assertThat(resultado).containsEntry("fechaHasta", "2024-12-31");
        assertThat(resultado).containsEntry("clienteId", "CLI-001");
        assertThat(resultado).containsEntry("periodo", "2024-01");
        assertThat(resultado).containsEntry("motivo", "VENTA");
        assertThat(resultado).containsEntry("numeroDocumento", "FAC-001");
    }

    @Test
    @DisplayName("Debe omitir parámetros opcionales nulos en el mapa")
    void toParameterMap_ConParametrosOpcionalesNulos_DebeOmitirlos() {
        // Given
        CuentasPorCobrarRequest requestSinOpcionales = new CuentasPorCobrarRequest(
                "01", "2024-01-01", "2024-12-31", null, null, null, null
        );

        // When
        Map<String, Object> resultado = mapper.toParameterMap(requestSinOpcionales);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).containsEntry("empresa", "01");
        assertThat(resultado).containsEntry("fechaDesde", "2024-01-01");
        assertThat(resultado).containsEntry("fechaHasta", "2024-12-31");
        assertThat(resultado).doesNotContainKey("clienteId");
        assertThat(resultado).doesNotContainKey("periodo");
        assertThat(resultado).doesNotContainKey("motivo");
        assertThat(resultado).doesNotContainKey("numeroDocumento");
    }

    @Test
    @DisplayName("Debe convertir entidad a response correctamente")
    void toResponse_ConEntidadValida_DebeRetornarResponse() {
        // When
        CuentasPorCobrarResponse resultado = mapper.toResponse(entidad);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.cxcdEmpresa()).isEqualTo("01");
        assertThat(resultado.cxcdPeriodo()).isEqualTo("2024-01");
        assertThat(resultado.cxcdMotivo()).isEqualTo("VENTA");
        assertThat(resultado.cxcdNumero()).isEqualTo("CXC-001");
        assertThat(resultado.cxcdDocumentoNumero()).isEqualTo("FAC-001");
        assertThat(resultado.cxcdClienteId()).isEqualTo("CLI-001");
        assertThat(resultado.cxcdFechaEmision()).isEqualTo("2024-01-15");
        assertThat(resultado.cxcdFechaVencimiento()).isEqualTo("2024-02-15");
        assertThat(resultado.cxcdRetencionValor()).isEqualByComparingTo(BigDecimal.valueOf(10.50));
        assertThat(resultado.cxcdAbono()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(resultado.cxcdSaldo()).isEqualByComparingTo(BigDecimal.valueOf(500.00));
        assertThat(resultado.cxcdTotal()).isEqualByComparingTo(BigDecimal.valueOf(600.00));
    }

    @Test
    @DisplayName("Debe retornar null cuando la entidad es nula")
    void toResponse_ConEntidadNula_DebeRetornarNull() {
        // When
        CuentasPorCobrarResponse resultado = mapper.toResponse(null);

        // Then
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Debe convertir lista de entidades a lista de responses")
    void toResponseList_ConListaDeEntidades_DebeRetornarListaDeResponses() {
        // Given
        CuentasPorCobrarDetalladoApiTO entidad2 = new CuentasPorCobrarDetalladoApiTO();
        entidad2.setId(2);
        entidad2.setCxcdEmpresa("01");
        entidad2.setCxcdNumero("CXC-002");
        entidad2.setCxcdClienteId("CLI-002");

        List<CuentasPorCobrarDetalladoApiTO> entidades = Arrays.asList(entidad, entidad2);

        // When
        List<CuentasPorCobrarResponse> resultado = mapper.toResponseList(entidades);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1);
        assertThat(resultado.get(0).cxcdNumero()).isEqualTo("CXC-001");
        assertThat(resultado.get(1).id()).isEqualTo(2);
        assertThat(resultado.get(1).cxcdNumero()).isEqualTo("CXC-002");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades es nula")
    void toResponseList_ConListaNula_DebeRetornarListaVacia() {
        // When
        List<CuentasPorCobrarResponse> resultado = mapper.toResponseList(null);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades está vacía")
    void toResponseList_ConListaVacia_DebeRetornarListaVacia() {
        // When
        List<CuentasPorCobrarResponse> resultado = mapper.toResponseList(Collections.emptyList());

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe manejar valores nulos en campos de la entidad")
    void toResponse_ConCamposNulos_DebeMapearCorrectamente() {
        // Given
        CuentasPorCobrarDetalladoApiTO entidadConNulos = new CuentasPorCobrarDetalladoApiTO();
        entidadConNulos.setId(1);
        entidadConNulos.setCxcdEmpresa("01");
        entidadConNulos.setCxcdNumero(null);
        entidadConNulos.setCxcdSaldo(null);

        // When
        CuentasPorCobrarResponse resultado = mapper.toResponse(entidadConNulos);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.cxcdEmpresa()).isEqualTo("01");
        assertThat(resultado.cxcdNumero()).isNull();
        assertThat(resultado.cxcdSaldo()).isNull();
    }
}

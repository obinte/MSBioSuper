package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.ResumenPescaRequest;
import com.acosux.MSBioSuper.application.dto.ResumenPescaResponse;
import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
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
 * Pruebas unitarias para ResumenPescaMapper.
 * Valida la conversión correcta entre DTOs y entidades.
 */
@DisplayName("ResumenPescaMapper - Pruebas Unitarias")
class ResumenPescaMapperTest {

    private ResumenPescaMapper mapper;
    private PrdResumenCorridaTO entidad;
    private ResumenPescaRequest request;

    @BeforeEach
    void setUp() {
        mapper = new ResumenPescaMapper();

        entidad = new PrdResumenCorridaTO();
        entidad.setId(1L);
        entidad.setPisNumero("P-001");
        entidad.setEmpCodigo("01");
        entidad.setSecCodigo("SECTOR-A");
        entidad.setRcCorridaNumero("C-2024-01");
        entidad.setRcBiomasa(BigDecimal.valueOf(1000.50));
        entidad.setRcFechaSiembra("2024-01-15");
        entidad.setRcFechaPesca("2024-06-15");

        request = new ResumenPescaRequest(
                "01",
                "SECTOR-A",
                "2024-01-01",
                "2024-12-31",
                "P-001",
                "C-2024-01"
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
        assertThat(resultado).containsEntry("sector", "SECTOR-A");
        assertThat(resultado).containsEntry("fechaDesde", "2024-01-01");
        assertThat(resultado).containsEntry("fechaHasta", "2024-12-31");
        assertThat(resultado).containsEntry("tipoResumen", "PESCA*");
        assertThat(resultado).containsEntry("piscina", "P-001");
        assertThat(resultado).containsEntry("corrida", "C-2024-01");
    }

    @Test
    @DisplayName("Debe omitir parámetros opcionales nulos en el mapa")
    void toParameterMap_ConParametrosOpcionalesNulos_DebeOmitirlos() {
        // Given
        ResumenPescaRequest requestSinOpcionales = new ResumenPescaRequest(
                "01", "SECTOR-A", "2024-01-01", "2024-12-31", null, null
        );

        // When
        Map<String, Object> resultado = mapper.toParameterMap(requestSinOpcionales);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).containsEntry("empresa", "01");
        assertThat(resultado).containsEntry("sector", "SECTOR-A");
        assertThat(resultado).doesNotContainKey("piscina");
        assertThat(resultado).doesNotContainKey("corrida");
    }

    @Test
    @DisplayName("Debe convertir entidad a response correctamente")
    void toResponse_ConEntidadValida_DebeRetornarResponse() {
        // When
        ResumenPescaResponse resultado = mapper.toResponse(entidad);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.pisNumero()).isEqualTo("P-001");
        assertThat(resultado.empCodigo()).isEqualTo("01");
        assertThat(resultado.secCodigo()).isEqualTo("SECTOR-A");
        assertThat(resultado.rcCorridaNumero()).isEqualTo("C-2024-01");
        assertThat(resultado.rcBiomasa()).isEqualByComparingTo(BigDecimal.valueOf(1000.50));
        assertThat(resultado.rcFechaSiembra()).isEqualTo("2024-01-15");
        assertThat(resultado.rcFechaPesca()).isEqualTo("2024-06-15");
    }

    @Test
    @DisplayName("Debe retornar null cuando la entidad es nula")
    void toResponse_ConEntidadNula_DebeRetornarNull() {
        // When
        ResumenPescaResponse resultado = mapper.toResponse(null);

        // Then
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Debe convertir lista de entidades a lista de responses")
    void toResponseList_ConListaDeEntidades_DebeRetornarListaDeResponses() {
        // Given
        PrdResumenCorridaTO entidad2 = new PrdResumenCorridaTO();
        entidad2.setId(2L);
        entidad2.setPisNumero("P-002");
        entidad2.setRcCorridaNumero("C-2024-02");

        List<PrdResumenCorridaTO> entidades = Arrays.asList(entidad, entidad2);

        // When
        List<ResumenPescaResponse> resultado = mapper.toResponseList(entidades);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).pisNumero()).isEqualTo("P-001");
        assertThat(resultado.get(1).id()).isEqualTo(2L);
        assertThat(resultado.get(1).pisNumero()).isEqualTo("P-002");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades es nula")
    void toResponseList_ConListaNula_DebeRetornarListaVacia() {
        // When
        List<ResumenPescaResponse> resultado = mapper.toResponseList(null);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades está vacía")
    void toResponseList_ConListaVacia_DebeRetornarListaVacia() {
        // When
        List<ResumenPescaResponse> resultado = mapper.toResponseList(Collections.emptyList());

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe manejar valores nulos en campos de la entidad")
    void toResponse_ConCamposNulos_DebeMapearCorrectamente() {
        // Given
        PrdResumenCorridaTO entidadConNulos = new PrdResumenCorridaTO();
        entidadConNulos.setId(1L);
        entidadConNulos.setPisNumero("P-001");
        entidadConNulos.setRcCorridaNumero(null);
        entidadConNulos.setRcBiomasa(null);

        // When
        ResumenPescaResponse resultado = mapper.toResponse(entidadConNulos);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.pisNumero()).isEqualTo("P-001");
        assertThat(resultado.rcCorridaNumero()).isNull();
        assertThat(resultado.rcBiomasa()).isNull();
    }
}

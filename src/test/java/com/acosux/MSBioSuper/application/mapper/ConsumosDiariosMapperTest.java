package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.ConsumosDiariosRequest;
import com.acosux.MSBioSuper.application.dto.ConsumosDiariosResponse;
import com.acosux.MSBioSuper.domain.model.PrdConsumosDiarioMSTO;
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
 * Pruebas unitarias para ConsumosDiariosMapper.
 * Valida la conversión correcta entre DTOs y entidades.
 */
@DisplayName("ConsumosDiariosMapper - Pruebas Unitarias")
class ConsumosDiariosMapperTest {

    private ConsumosDiariosMapper mapper;
    private PrdConsumosDiarioMSTO entidad;
    private ConsumosDiariosRequest request;

    @BeforeEach
    void setUp() {
        mapper = new ConsumosDiariosMapper();

        entidad = new PrdConsumosDiarioMSTO();
        entidad.setId(1L);
        entidad.setConsumoEmpresa("01");
        entidad.setConsumoSector("SECTOR-A");
        entidad.setConsumoPiscina("P-001");
        entidad.setConsumoFecha("2024-01-15");
        entidad.setConsumoProducto("Balanceado Premium");
        entidad.setConsumoCodigo("BAL-001");
        entidad.setConsumoMedida("KG");
        entidad.setConsumoTotal(BigDecimal.valueOf(500.0));
        entidad.setConsumoCostoTotal(BigDecimal.valueOf(750.0));
        entidad.setConsumoCorrida("C-2024-01");

        request = new ConsumosDiariosRequest(
                "01",
                "SECTOR-A",
                "2024-01-01",
                "2024-01-31",
                "P-001",
                "C-2024-01",
                "BAL-001"
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
        assertThat(resultado).containsEntry("fechaHasta", "2024-01-31");
        assertThat(resultado).containsEntry("piscina", "P-001");
        assertThat(resultado).containsEntry("corrida", "C-2024-01");
        assertThat(resultado).containsEntry("producto", "BAL-001");
    }

    @Test
    @DisplayName("Debe omitir parámetros opcionales nulos en el mapa")
    void toParameterMap_ConParametrosOpcionalesNulos_DebeOmitirlos() {
        // Given
        ConsumosDiariosRequest requestSinOpcionales = new ConsumosDiariosRequest(
                "01", "SECTOR-A", "2024-01-01", "2024-01-31", null, null, null
        );

        // When
        Map<String, Object> resultado = mapper.toParameterMap(requestSinOpcionales);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).containsEntry("empresa", "01");
        assertThat(resultado).containsEntry("sector", "SECTOR-A");
        assertThat(resultado).doesNotContainKey("piscina");
        assertThat(resultado).doesNotContainKey("corrida");
        assertThat(resultado).doesNotContainKey("producto");
    }

    @Test
    @DisplayName("Debe convertir entidad a response correctamente")
    void toResponse_ConEntidadValida_DebeRetornarResponse() {
        // When
        ConsumosDiariosResponse resultado = mapper.toResponse(entidad);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.consumoEmpresa()).isEqualTo("01");
        assertThat(resultado.consumoSector()).isEqualTo("SECTOR-A");
        assertThat(resultado.consumoPiscina()).isEqualTo("P-001");
        assertThat(resultado.consumoFecha()).isEqualTo("2024-01-15");
        assertThat(resultado.consumoProducto()).isEqualTo("Balanceado Premium");
        assertThat(resultado.consumoCodigo()).isEqualTo("BAL-001");
        assertThat(resultado.consumoMedida()).isEqualTo("KG");
        assertThat(resultado.consumoTotal()).isEqualByComparingTo(BigDecimal.valueOf(500.0));
        assertThat(resultado.consumoCostoTotal()).isEqualByComparingTo(BigDecimal.valueOf(750.0));
        assertThat(resultado.consumoCorrida()).isEqualTo("C-2024-01");
    }

    @Test
    @DisplayName("Debe retornar null cuando la entidad es nula")
    void toResponse_ConEntidadNula_DebeRetornarNull() {
        // When
        ConsumosDiariosResponse resultado = mapper.toResponse(null);

        // Then
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Debe convertir lista de entidades a lista de responses")
    void toResponseList_ConListaDeEntidades_DebeRetornarListaDeResponses() {
        // Given
        PrdConsumosDiarioMSTO entidad2 = new PrdConsumosDiarioMSTO();
        entidad2.setId(2L);
        entidad2.setConsumoEmpresa("01");
        entidad2.setConsumoPiscina("P-002");
        entidad2.setConsumoProducto("Balanceado Estándar");

        List<PrdConsumosDiarioMSTO> entidades = Arrays.asList(entidad, entidad2);

        // When
        List<ConsumosDiariosResponse> resultado = mapper.toResponseList(entidades);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).consumoPiscina()).isEqualTo("P-001");
        assertThat(resultado.get(1).id()).isEqualTo(2L);
        assertThat(resultado.get(1).consumoPiscina()).isEqualTo("P-002");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades es nula")
    void toResponseList_ConListaNula_DebeRetornarListaVacia() {
        // When
        List<ConsumosDiariosResponse> resultado = mapper.toResponseList(null);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades está vacía")
    void toResponseList_ConListaVacia_DebeRetornarListaVacia() {
        // When
        List<ConsumosDiariosResponse> resultado = mapper.toResponseList(Collections.emptyList());

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe manejar valores nulos en campos de la entidad")
    void toResponse_ConCamposNulos_DebeMapearCorrectamente() {
        // Given
        PrdConsumosDiarioMSTO entidadConNulos = new PrdConsumosDiarioMSTO();
        entidadConNulos.setId(1L);
        entidadConNulos.setConsumoEmpresa("01");
        entidadConNulos.setConsumoProducto(null);
        entidadConNulos.setConsumoTotal(null);

        // When
        ConsumosDiariosResponse resultado = mapper.toResponse(entidadConNulos);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.consumoEmpresa()).isEqualTo("01");
        assertThat(resultado.consumoProducto()).isNull();
        assertThat(resultado.consumoTotal()).isNull();
    }
}

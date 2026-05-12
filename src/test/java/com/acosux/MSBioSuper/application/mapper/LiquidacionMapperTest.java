package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.LiquidacionRequest;
import com.acosux.MSBioSuper.application.dto.LiquidacionResponse;
import com.acosux.MSBioSuper.domain.model.PrdLiquidacionesDetalleTO;
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
 * Pruebas unitarias para LiquidacionMapper.
 * Valida la conversión correcta entre DTOs y entidades.
 */
@DisplayName("LiquidacionMapper - Pruebas Unitarias")
class LiquidacionMapperTest {

    private LiquidacionMapper mapper;
    private PrdLiquidacionesDetalleTO entidad;
    private LiquidacionRequest request;

    @BeforeEach
    void setUp() {
        mapper = new LiquidacionMapper();

        entidad = new PrdLiquidacionesDetalleTO();
        entidad.setId(1L);
        entidad.setLiqNumero("LIQ-001");
        entidad.setLiqFecha("2024-01-15");
        entidad.setLiqCliente("Cliente ABC");
        entidad.setLiqLote("LOTE-001");
        entidad.setLiqCorrida("C-2024-01");
        entidad.setLiqLibrasEnviadas(BigDecimal.valueOf(1000.0));
        entidad.setLiqLibrasRecibidas(BigDecimal.valueOf(980.0));
        entidad.setLiqDiferencia(BigDecimal.valueOf(20.0));
        entidad.setLiqLibrasBasura(BigDecimal.valueOf(10.0));
        entidad.setLiqLibrasNetas(BigDecimal.valueOf(970.0));
        entidad.setLiqLibrasEntero(BigDecimal.valueOf(850.0));
        entidad.setLiqRendimiento(BigDecimal.valueOf(87.6));
        entidad.setLiqTotalMonto(BigDecimal.valueOf(4850.0));
        entidad.setLiqPrecioLibraPromedio(BigDecimal.valueOf(5.0));
        entidad.setLiqTipo("VENTA");
        entidad.setLiqIdCliente("CLI-001");

        request = new LiquidacionRequest(
                "01",
                "2024-01-01",
                "2024-01-31",
                "SECTOR-A",
                "P-001",
                "C-2024-01",
                "CLI-001",
                "VENTA"
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
        assertThat(resultado).containsEntry("fechaHasta", "2024-01-31");
        assertThat(resultado).containsEntry("sector", "SECTOR-A");
        assertThat(resultado).containsEntry("piscina", "P-001");
        assertThat(resultado).containsEntry("corrida", "C-2024-01");
        assertThat(resultado).containsEntry("cliente", "CLI-001");
        assertThat(resultado).containsEntry("tipo", "VENTA");
    }

    @Test
    @DisplayName("Debe omitir parámetros opcionales nulos en el mapa")
    void toParameterMap_ConParametrosOpcionalesNulos_DebeOmitirlos() {
        // Given
        LiquidacionRequest requestSinOpcionales = new LiquidacionRequest(
                "01", "2024-01-01", "2024-01-31", null, null, null, null, null
        );

        // When
        Map<String, Object> resultado = mapper.toParameterMap(requestSinOpcionales);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).containsEntry("empresa", "01");
        assertThat(resultado).containsEntry("fechaDesde", "2024-01-01");
        assertThat(resultado).containsEntry("fechaHasta", "2024-01-31");
        assertThat(resultado).doesNotContainKey("sector");
        assertThat(resultado).doesNotContainKey("piscina");
        assertThat(resultado).doesNotContainKey("corrida");
        assertThat(resultado).doesNotContainKey("cliente");
        assertThat(resultado).doesNotContainKey("tipo");
    }

    @Test
    @DisplayName("Debe convertir entidad a response correctamente")
    void toResponse_ConEntidadValida_DebeRetornarResponse() {
        // When
        LiquidacionResponse resultado = mapper.toResponse(entidad);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.liqNumero()).isEqualTo("LIQ-001");
        assertThat(resultado.liqFecha()).isEqualTo("2024-01-15");
        assertThat(resultado.liqCliente()).isEqualTo("Cliente ABC");
        assertThat(resultado.liqLote()).isEqualTo("LOTE-001");
        assertThat(resultado.liqCorrida()).isEqualTo("C-2024-01");
        assertThat(resultado.liqLibrasEnviadas()).isEqualByComparingTo(BigDecimal.valueOf(1000.0));
        assertThat(resultado.liqLibrasRecibidas()).isEqualByComparingTo(BigDecimal.valueOf(980.0));
        assertThat(resultado.liqDiferencia()).isEqualByComparingTo(BigDecimal.valueOf(20.0));
        assertThat(resultado.liqLibrasBasura()).isEqualByComparingTo(BigDecimal.valueOf(10.0));
        assertThat(resultado.liqLibrasNetas()).isEqualByComparingTo(BigDecimal.valueOf(970.0));
        assertThat(resultado.liqLibrasEntero()).isEqualByComparingTo(BigDecimal.valueOf(850.0));
        assertThat(resultado.liqRendimiento()).isEqualByComparingTo(BigDecimal.valueOf(87.6));
        assertThat(resultado.liqTotalMonto()).isEqualByComparingTo(BigDecimal.valueOf(4850.0));
        assertThat(resultado.liqPrecioLibraPromedio()).isEqualByComparingTo(BigDecimal.valueOf(5.0));
        assertThat(resultado.liqTipo()).isEqualTo("VENTA");
        assertThat(resultado.liqIdCliente()).isEqualTo("CLI-001");
    }

    @Test
    @DisplayName("Debe retornar null cuando la entidad es nula")
    void toResponse_ConEntidadNula_DebeRetornarNull() {
        // When
        LiquidacionResponse resultado = mapper.toResponse(null);

        // Then
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Debe convertir lista de entidades a lista de responses")
    void toResponseList_ConListaDeEntidades_DebeRetornarListaDeResponses() {
        // Given
        PrdLiquidacionesDetalleTO entidad2 = new PrdLiquidacionesDetalleTO();
        entidad2.setId(2L);
        entidad2.setLiqNumero("LIQ-002");
        entidad2.setLiqCliente("Cliente XYZ");

        List<PrdLiquidacionesDetalleTO> entidades = Arrays.asList(entidad, entidad2);

        // When
        List<LiquidacionResponse> resultado = mapper.toResponseList(entidades);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).liqNumero()).isEqualTo("LIQ-001");
        assertThat(resultado.get(1).id()).isEqualTo(2L);
        assertThat(resultado.get(1).liqNumero()).isEqualTo("LIQ-002");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades es nula")
    void toResponseList_ConListaNula_DebeRetornarListaVacia() {
        // When
        List<LiquidacionResponse> resultado = mapper.toResponseList(null);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades está vacía")
    void toResponseList_ConListaVacia_DebeRetornarListaVacia() {
        // When
        List<LiquidacionResponse> resultado = mapper.toResponseList(Collections.emptyList());

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe manejar valores nulos en campos de la entidad")
    void toResponse_ConCamposNulos_DebeMapearCorrectamente() {
        // Given
        PrdLiquidacionesDetalleTO entidadConNulos = new PrdLiquidacionesDetalleTO();
        entidadConNulos.setId(1L);
        entidadConNulos.setLiqNumero("LIQ-001");
        entidadConNulos.setLiqCliente(null);
        entidadConNulos.setLiqTotalMonto(null);

        // When
        LiquidacionResponse resultado = mapper.toResponse(entidadConNulos);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.liqNumero()).isEqualTo("LIQ-001");
        assertThat(resultado.liqCliente()).isNull();
        assertThat(resultado.liqTotalMonto()).isNull();
    }
}

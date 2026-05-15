package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.ResumenSiembraRequest;
import com.acosux.MSBioSuper.application.dto.ResumenSiembraResponse;
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
 * Pruebas unitarias para ResumenSiembraMapper.
 * Valida la conversión correcta entre DTOs y entidades.
 */
@DisplayName("ResumenSiembraMapper - Pruebas Unitarias")
class ResumenSiembraMapperTest {

    private ResumenSiembraMapper mapper;
    private PrdResumenCorridaTO entidad;
    private ResumenSiembraRequest request;

    @BeforeEach
    void setUp() {
        mapper = new ResumenSiembraMapper();

        entidad = new PrdResumenCorridaTO();
        entidad.setId(1);
        entidad.setPisNumero("P-001");
        entidad.setEmpCodigo("01");
        entidad.setSecCodigo("SECTOR-A");
        entidad.setRcHectareaje(BigDecimal.valueOf(2.5));
        entidad.setRcCorridaNumero("C-2024-01");
        entidad.setRcFechaDesde("2024-01-01");
        entidad.setRcFechaSiembra("2024-01-15");
        entidad.setRcFechaPesca("2024-04-15");
        entidad.setRcFechaHasta("2024-04-30");
        entidad.setRcEdad(90);
        entidad.setRcEdadInicial(0);
        entidad.setRcDiasSecos(15);
        entidad.setRcNumeroLarvas(250000);
        entidad.setRcCodigoLarvaLote("LARVA-2024-01");
        entidad.setRcDensidad(100000);
        entidad.setRcLaboratorio("LAB-001");
        entidad.setRcNauplio("NAUPLIO-A");
        entidad.setRcLibrasBalanceados(BigDecimal.valueOf(5000.00));
        entidad.setRcBalanceadoTransferido(BigDecimal.ZERO);
        entidad.setRcCostoBalanceado(BigDecimal.valueOf(7500.00));
        entidad.setRcCostoBalanceadoTransferido(BigDecimal.ZERO);
        entidad.setRcBiomasa(BigDecimal.valueOf(4500.00));
        entidad.setRcBiomasaReal(BigDecimal.valueOf(4450.00));
        entidad.setRcBiomasaProyectada(BigDecimal.valueOf(4600.00));
        entidad.setRcVentaProyectada(BigDecimal.valueOf(13800.00));
        entidad.setRcConversion(BigDecimal.valueOf(1.11));
        entidad.setRcTGrande(BigDecimal.valueOf(15.5));
        entidad.setRcTMediano(BigDecimal.valueOf(18.2));
        entidad.setRcTPequeno(BigDecimal.valueOf(22.3));
        entidad.setRcTPromedio(BigDecimal.valueOf(18.0));
        entidad.setRcCrecimientoDia(BigDecimal.valueOf(0.20));
        entidad.setRcPesoIdeal(BigDecimal.valueOf(18.0));
        entidad.setRcSobrevivencia(BigDecimal.valueOf(89.0));
        entidad.setRcCostoTotal(BigDecimal.valueOf(12000.00));
        entidad.setRcCostoDirecto(BigDecimal.valueOf(10000.00));
        entidad.setRcCostoIndirecto(BigDecimal.valueOf(2000.00));
        entidad.setRcCostoTransferencia(BigDecimal.ZERO);
        entidad.setRcCostoDirectoTransferido(BigDecimal.ZERO);
        entidad.setRcCostoIndirectoTransferido(BigDecimal.ZERO);
        entidad.setRcValorVenta(BigDecimal.valueOf(13500.00));
        entidad.setRcValorVentaTransferida(BigDecimal.ZERO);
        entidad.setRcResultado(BigDecimal.valueOf(1500.00));
        entidad.setRcRendimiento(BigDecimal.valueOf(11.11));
        entidad.setRcCostoHectarea(BigDecimal.valueOf(4800.00));
        entidad.setRcVentaHectarea(BigDecimal.valueOf(5400.00));
        entidad.setRcResultadoHectarea(BigDecimal.valueOf(600.00));
        entidad.setRcResultadoHectareaDia(BigDecimal.valueOf(6.67));
        entidad.setRcCostoLibra(BigDecimal.valueOf(2.67));
        entidad.setRcVentaLibra(BigDecimal.valueOf(3.00));
        entidad.setRcResultadoLibra(BigDecimal.valueOf(0.33));
        entidad.setRcCostoTotalHectareaDia(BigDecimal.valueOf(53.33));
        entidad.setRcCostoDirectoHectareaDia(BigDecimal.valueOf(44.44));
        entidad.setRcCostoindirectoHectareaDia(BigDecimal.valueOf(8.89));
        entidad.setRcPesoInicial(BigDecimal.valueOf(0.001));
        entidad.setRcCostoTransferenciaHectareaDia(BigDecimal.ZERO);
        entidad.setRcCostoDirectoTransferidoHectareaDia(BigDecimal.ZERO);
        entidad.setRcCostoIndirectoTransferidoHectareaDia(BigDecimal.ZERO);
        entidad.setRcOtrosCostos(BigDecimal.ZERO);
        entidad.setRcOtrosHectareaDias(BigDecimal.ZERO);
        entidad.setRcAnimales(222500);
        entidad.setRcCorridaOrigen(null);
        entidad.setRcCorridaDestino(null);
        entidad.setRcCostoMillar(BigDecimal.valueOf(53.93));
        entidad.setRcBiomasaEmpacadora(BigDecimal.valueOf(4450.00));
        entidad.setRcPesoCosecha(BigDecimal.valueOf(18.0));
        entidad.setRcTipoLiquidacion("DIRECTA");
        entidad.setRcBiomasaTransferida(BigDecimal.ZERO);
        entidad.setRcLibrasPescadas(BigDecimal.valueOf(4450.00));
        entidad.setRcPesoPromedioCosechado(BigDecimal.valueOf(18.0));
        entidad.setRcEmpacadoraCodigo("EMP-001");

        request = new ResumenSiembraRequest(
                "01",
                "SECTOR-A",
                "2024-01-01",
                "2024-04-30",
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
        assertThat(resultado).containsEntry("fechaHasta", "2024-04-30");
        assertThat(resultado).containsEntry("tipoResumen", "SIEMBRA");
        assertThat(resultado).containsEntry("piscina", "P-001");
        assertThat(resultado).containsEntry("corrida", "C-2024-01");
    }

    @Test
    @DisplayName("Debe incluir tipoResumen SIEMBRA en el mapa de parámetros")
    void toParameterMap_DebeIncluirTipoResumenSiembra() {
        // When
        Map<String, Object> resultado = mapper.toParameterMap(request);

        // Then
        assertThat(resultado).containsEntry("tipoResumen", "SIEMBRA");
    }

    @Test
    @DisplayName("Debe omitir parámetros opcionales nulos en el mapa")
    void toParameterMap_ConParametrosOpcionalesNulos_DebeOmitirlos() {
        // Given
        ResumenSiembraRequest requestSinOpcionales = new ResumenSiembraRequest(
                "01", "SECTOR-A", "2024-01-01", "2024-04-30", null, null
        );

        // When
        Map<String, Object> resultado = mapper.toParameterMap(requestSinOpcionales);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).containsEntry("empresa", "01");
        assertThat(resultado).containsEntry("sector", "SECTOR-A");
        assertThat(resultado).containsEntry("tipoResumen", "SIEMBRA");
        assertThat(resultado).doesNotContainKey("piscina");
        assertThat(resultado).doesNotContainKey("corrida");
    }

    @Test
    @DisplayName("Debe convertir entidad a response correctamente")
    void toResponse_ConEntidadValida_DebeRetornarResponse() {
        // When
        ResumenSiembraResponse resultado = mapper.toResponse(entidad);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.pisNumero()).isEqualTo("P-001");
        assertThat(resultado.empCodigo()).isEqualTo("01");
        assertThat(resultado.secCodigo()).isEqualTo("SECTOR-A");
        assertThat(resultado.rcHectareaje()).isEqualByComparingTo(BigDecimal.valueOf(2.5));
        assertThat(resultado.rcCorridaNumero()).isEqualTo("C-2024-01");
        assertThat(resultado.rcFechaDesde()).isEqualTo("2024-01-01");
        assertThat(resultado.rcFechaSiembra()).isEqualTo("2024-01-15");
        assertThat(resultado.rcFechaPesca()).isEqualTo("2024-04-15");
        assertThat(resultado.rcFechaHasta()).isEqualTo("2024-04-30");
        assertThat(resultado.rcEdad()).isEqualTo(90);
        assertThat(resultado.rcNumeroLarvas()).isEqualTo(250000);
        assertThat(resultado.rcDensidad()).isEqualTo(100000);
        assertThat(resultado.rcLaboratorio()).isEqualTo("LAB-001");
        assertThat(resultado.rcLibrasBalanceados()).isEqualByComparingTo(BigDecimal.valueOf(5000.00));
        assertThat(resultado.rcCostoBalanceado()).isEqualByComparingTo(BigDecimal.valueOf(7500.00));
        assertThat(resultado.rcBiomasa()).isEqualByComparingTo(BigDecimal.valueOf(4500.00));
        assertThat(resultado.rcConversion()).isEqualByComparingTo(BigDecimal.valueOf(1.11));
        assertThat(resultado.rcTPromedio()).isEqualByComparingTo(BigDecimal.valueOf(18.0));
        assertThat(resultado.rcSobrevivencia()).isEqualByComparingTo(BigDecimal.valueOf(89.0));
        assertThat(resultado.rcCostoTotal()).isEqualByComparingTo(BigDecimal.valueOf(12000.00));
        assertThat(resultado.rcValorVenta()).isEqualByComparingTo(BigDecimal.valueOf(13500.00));
        assertThat(resultado.rcResultado()).isEqualByComparingTo(BigDecimal.valueOf(1500.00));
        assertThat(resultado.rcAnimales()).isEqualTo(222500);
    }

    @Test
    @DisplayName("Debe retornar null cuando la entidad es nula")
    void toResponse_ConEntidadNula_DebeRetornarNull() {
        // When
        ResumenSiembraResponse resultado = mapper.toResponse(null);

        // Then
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Debe convertir lista de entidades a lista de responses")
    void toResponseList_ConListaDeEntidades_DebeRetornarListaDeResponses() {
        // Given
        PrdResumenCorridaTO entidad2 = new PrdResumenCorridaTO();
        entidad2.setId(2);
        entidad2.setPisNumero("P-002");
        entidad2.setEmpCodigo("01");
        entidad2.setRcCorridaNumero("C-2024-02");

        List<PrdResumenCorridaTO> entidades = Arrays.asList(entidad, entidad2);

        // When
        List<ResumenSiembraResponse> resultado = mapper.toResponseList(entidades);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1);
        assertThat(resultado.get(0).pisNumero()).isEqualTo("P-001");
        assertThat(resultado.get(0).rcCorridaNumero()).isEqualTo("C-2024-01");
        assertThat(resultado.get(1).id()).isEqualTo(2);
        assertThat(resultado.get(1).pisNumero()).isEqualTo("P-002");
        assertThat(resultado.get(1).rcCorridaNumero()).isEqualTo("C-2024-02");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades es nula")
    void toResponseList_ConListaNula_DebeRetornarListaVacia() {
        // When
        List<ResumenSiembraResponse> resultado = mapper.toResponseList(null);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades está vacía")
    void toResponseList_ConListaVacia_DebeRetornarListaVacia() {
        // When
        List<ResumenSiembraResponse> resultado = mapper.toResponseList(Collections.emptyList());

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe manejar valores nulos en campos de la entidad")
    void toResponse_ConCamposNulos_DebeMapearCorrectamente() {
        // Given
        PrdResumenCorridaTO entidadConNulos = new PrdResumenCorridaTO();
        entidadConNulos.setId(1);
        entidadConNulos.setPisNumero("P-001");
        entidadConNulos.setEmpCodigo("01");
        entidadConNulos.setRcCorridaNumero("C-2024-01");
        entidadConNulos.setRcLaboratorio(null);
        entidadConNulos.setRcBiomasa(null);
        entidadConNulos.setRcCostoTotal(null);

        // When
        ResumenSiembraResponse resultado = mapper.toResponse(entidadConNulos);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.pisNumero()).isEqualTo("P-001");
        assertThat(resultado.empCodigo()).isEqualTo("01");
        assertThat(resultado.rcCorridaNumero()).isEqualTo("C-2024-01");
        assertThat(resultado.rcLaboratorio()).isNull();
        assertThat(resultado.rcBiomasa()).isNull();
        assertThat(resultado.rcCostoTotal()).isNull();
    }

    @Test
    @DisplayName("Debe mapear correctamente todos los campos numéricos")
    void toResponse_DebeMapearCamposNumericos() {
        // When
        ResumenSiembraResponse resultado = mapper.toResponse(entidad);

        // Then
        assertThat(resultado.rcEdad()).isEqualTo(90);
        assertThat(resultado.rcEdadInicial()).isEqualTo(0);
        assertThat(resultado.rcDiasSecos()).isEqualTo(15);
        assertThat(resultado.rcNumeroLarvas()).isEqualTo(250000);
        assertThat(resultado.rcDensidad()).isEqualTo(100000);
        assertThat(resultado.rcAnimales()).isEqualTo(222500);
    }

    @Test
    @DisplayName("Debe mapear correctamente todos los campos BigDecimal")
    void toResponse_DebeMapearCamposBigDecimal() {
        // When
        ResumenSiembraResponse resultado = mapper.toResponse(entidad);

        // Then
        assertThat(resultado.rcHectareaje()).isEqualByComparingTo(BigDecimal.valueOf(2.5));
        assertThat(resultado.rcLibrasBalanceados()).isEqualByComparingTo(BigDecimal.valueOf(5000.00));
        assertThat(resultado.rcCostoBalanceado()).isEqualByComparingTo(BigDecimal.valueOf(7500.00));
        assertThat(resultado.rcBiomasa()).isEqualByComparingTo(BigDecimal.valueOf(4500.00));
        assertThat(resultado.rcBiomasaReal()).isEqualByComparingTo(BigDecimal.valueOf(4450.00));
        assertThat(resultado.rcBiomasaProyectada()).isEqualByComparingTo(BigDecimal.valueOf(4600.00));
        assertThat(resultado.rcConversion()).isEqualByComparingTo(BigDecimal.valueOf(1.11));
        assertThat(resultado.rcSobrevivencia()).isEqualByComparingTo(BigDecimal.valueOf(89.0));
        assertThat(resultado.rcCostoTotal()).isEqualByComparingTo(BigDecimal.valueOf(12000.00));
        assertThat(resultado.rcCostoDirecto()).isEqualByComparingTo(BigDecimal.valueOf(10000.00));
        assertThat(resultado.rcCostoIndirecto()).isEqualByComparingTo(BigDecimal.valueOf(2000.00));
        assertThat(resultado.rcValorVenta()).isEqualByComparingTo(BigDecimal.valueOf(13500.00));
        assertThat(resultado.rcResultado()).isEqualByComparingTo(BigDecimal.valueOf(1500.00));
    }

    @Test
    @DisplayName("Debe mapear correctamente campos de fechas")
    void toResponse_DebeMapearCamposFechas() {
        // When
        ResumenSiembraResponse resultado = mapper.toResponse(entidad);

        // Then
        assertThat(resultado.rcFechaDesde()).isEqualTo("2024-01-01");
        assertThat(resultado.rcFechaSiembra()).isEqualTo("2024-01-15");
        assertThat(resultado.rcFechaPesca()).isEqualTo("2024-04-15");
        assertThat(resultado.rcFechaHasta()).isEqualTo("2024-04-30");
    }
}

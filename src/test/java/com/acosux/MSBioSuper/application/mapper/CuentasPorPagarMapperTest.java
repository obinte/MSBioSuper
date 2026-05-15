package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.CuentasPorPagarRequest;
import com.acosux.MSBioSuper.application.dto.CuentasPorPagarResponse;
import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;
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
 * Pruebas unitarias para CuentasPorPagarMapper.
 * Valida la conversión correcta entre DTOs y entidades.
 */
@DisplayName("CuentasPorPagarMapper - Pruebas Unitarias")
class CuentasPorPagarMapperTest {

    private CuentasPorPagarMapper mapper;
    private CuentasPorPagarDetalladoTO entidad;
    private CuentasPorPagarRequest request;

    @BeforeEach
    void setUp() {
        mapper = new CuentasPorPagarMapper();

        entidad = new CuentasPorPagarDetalladoTO();
        entidad.setId(1);
        entidad.setCxpdPeriodo("2024-01");
        entidad.setCxpdMotivo("COMPRA");
        entidad.setCxpdNumero("CXP-001");
        entidad.setCxpdDocumento("FAC-PROV-001");
        entidad.setCxpdObservaciones("Compra de materiales");
        entidad.setCxpdProveedorCodigo("PROV-001");
        entidad.setCxpdProveedorId("PROV-001");
        entidad.setCxpdProveedorRazonSocial("Proveedor Test S.A.");
        entidad.setCxpdFechaEmision("2024-01-15");
        entidad.setCxpdFechaVencimiento("2024-02-15");
        entidad.setCxpdSaldo(BigDecimal.valueOf(1500.00));
        entidad.setCxpdUsuarioApruebaPago("admin");
        entidad.setCxpdDiasCredito("30");
        entidad.setCxpdDiasVencidos("0");
        entidad.setCxpdZona("ZONA-A");
        entidad.setCxpdSaldoVencido(BigDecimal.ZERO);
        entidad.setCxpdValorAprobado(BigDecimal.valueOf(1500.00));
        entidad.setCxpdAbonos(BigDecimal.ZERO);
        entidad.setCxpdSaldoPorVencer(BigDecimal.valueOf(1500.00));
        entidad.setCxpdCp("CP-001");
        entidad.setCxpdProvRelacionado(false);

        request = new CuentasPorPagarRequest(
                "01",
                "2024-01-01",
                "2024-12-31",
                "PROV-001",
                "2024-01",
                "COMPRA",
                "ZONA-A",
                false
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
        assertThat(resultado).containsEntry("proveedorId", "PROV-001");
        assertThat(resultado).containsEntry("periodo", "2024-01");
        assertThat(resultado).containsEntry("motivo", "COMPRA");
        assertThat(resultado).containsEntry("zona", "ZONA-A");
        assertThat(resultado).containsEntry("proveedorRelacionado", false);
    }

    @Test
    @DisplayName("Debe omitir parámetros opcionales nulos en el mapa")
    void toParameterMap_ConParametrosOpcionalesNulos_DebeOmitirlos() {
        // Given
        CuentasPorPagarRequest requestSinOpcionales = new CuentasPorPagarRequest(
                "01", "2024-01-01", "2024-12-31", null, null, null, null, null
        );

        // When
        Map<String, Object> resultado = mapper.toParameterMap(requestSinOpcionales);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).containsEntry("empresa", "01");
        assertThat(resultado).containsEntry("fechaDesde", "2024-01-01");
        assertThat(resultado).containsEntry("fechaHasta", "2024-12-31");
        assertThat(resultado).doesNotContainKey("proveedorId");
        assertThat(resultado).doesNotContainKey("periodo");
        assertThat(resultado).doesNotContainKey("motivo");
        assertThat(resultado).doesNotContainKey("zona");
        assertThat(resultado).doesNotContainKey("proveedorRelacionado");
    }

    @Test
    @DisplayName("Debe convertir entidad a response correctamente")
    void toResponse_ConEntidadValida_DebeRetornarResponse() {
        // When
        CuentasPorPagarResponse resultado = mapper.toResponse(entidad);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.cxpdPeriodo()).isEqualTo("2024-01");
        assertThat(resultado.cxpdMotivo()).isEqualTo("COMPRA");
        assertThat(resultado.cxpdNumero()).isEqualTo("CXP-001");
        assertThat(resultado.cxpdDocumento()).isEqualTo("FAC-PROV-001");
        assertThat(resultado.cxpdObservaciones()).isEqualTo("Compra de materiales");
        assertThat(resultado.cxpdProveedorCodigo()).isEqualTo("PROV-001");
        assertThat(resultado.cxpdProveedorId()).isEqualTo("PROV-001");
        assertThat(resultado.cxpdProveedorRazonSocial()).isEqualTo("Proveedor Test S.A.");
        assertThat(resultado.cxpdFechaEmision()).isEqualTo("2024-01-15");
        assertThat(resultado.cxpdFechaVencimiento()).isEqualTo("2024-02-15");
        assertThat(resultado.cxpdSaldo()).isEqualByComparingTo(BigDecimal.valueOf(1500.00));
        assertThat(resultado.cxpdUsuarioApruebaPago()).isEqualTo("admin");
        assertThat(resultado.cxpdDiasCredito()).isEqualTo("30");
        assertThat(resultado.cxpdDiasVencidos()).isEqualTo("0");
        assertThat(resultado.cxpdZona()).isEqualTo("ZONA-A");
        assertThat(resultado.cxpdSaldoVencido()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(resultado.cxpdValorAprobado()).isEqualByComparingTo(BigDecimal.valueOf(1500.00));
        assertThat(resultado.cxpdAbonos()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(resultado.cxpdSaldoPorVencer()).isEqualByComparingTo(BigDecimal.valueOf(1500.00));
        assertThat(resultado.cxpdCp()).isEqualTo("CP-001");
        assertThat(resultado.cxpdProvRelacionado()).isFalse();
    }

    @Test
    @DisplayName("Debe retornar null cuando la entidad es nula")
    void toResponse_ConEntidadNula_DebeRetornarNull() {
        // When
        CuentasPorPagarResponse resultado = mapper.toResponse(null);

        // Then
        assertThat(resultado).isNull();
    }

    @Test
    @DisplayName("Debe convertir lista de entidades a lista de responses")
    void toResponseList_ConListaDeEntidades_DebeRetornarListaDeResponses() {
        // Given
        CuentasPorPagarDetalladoTO entidad2 = new CuentasPorPagarDetalladoTO();
        entidad2.setId(2);
        entidad2.setCxpdNumero("CXP-002");
        entidad2.setCxpdProveedorId("PROV-002");

        List<CuentasPorPagarDetalladoTO> entidades = Arrays.asList(entidad, entidad2);

        // When
        List<CuentasPorPagarResponse> resultado = mapper.toResponseList(entidades);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1);
        assertThat(resultado.get(0).cxpdNumero()).isEqualTo("CXP-001");
        assertThat(resultado.get(1).id()).isEqualTo(2);
        assertThat(resultado.get(1).cxpdNumero()).isEqualTo("CXP-002");
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades es nula")
    void toResponseList_ConListaNula_DebeRetornarListaVacia() {
        // When
        List<CuentasPorPagarResponse> resultado = mapper.toResponseList(null);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando la lista de entidades está vacía")
    void toResponseList_ConListaVacia_DebeRetornarListaVacia() {
        // When
        List<CuentasPorPagarResponse> resultado = mapper.toResponseList(Collections.emptyList());

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Debe manejar valores nulos en campos de la entidad")
    void toResponse_ConCamposNulos_DebeMapearCorrectamente() {
        // Given
        CuentasPorPagarDetalladoTO entidadConNulos = new CuentasPorPagarDetalladoTO();
        entidadConNulos.setId(1);
        entidadConNulos.setCxpdNumero("CXP-001");
        entidadConNulos.setCxpdProveedorId(null);
        entidadConNulos.setCxpdSaldo(null);

        // When
        CuentasPorPagarResponse resultado = mapper.toResponse(entidadConNulos);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1);
        assertThat(resultado.cxpdNumero()).isEqualTo("CXP-001");
        assertThat(resultado.cxpdProveedorId()).isNull();
        assertThat(resultado.cxpdSaldo()).isNull();
    }
}

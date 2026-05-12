package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.ResumenPescaRequest;
import com.acosux.MSBioSuper.application.dto.ResumenPescaResponse;
import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapeador para convertir entre DTOs y entidades de Resumen de Pesca.
 * Implementa mapeo manual para mayor control y claridad.
 */
@Component
public class ResumenPescaMapper {

    /**
     * Convierte un ResumenPescaRequest a un Map de parámetros
     * para mantener compatibilidad con la capa de servicio existente.
     *
     * @param request DTO de solicitud
     * @return Map con los parámetros
     */
    public Map<String, Object> toParameterMap(ResumenPescaRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.empresa());
        params.put("sector", request.sector());
        params.put("fechaDesde", request.fechaDesde());
        params.put("fechaHasta", request.fechaHasta());
        params.put("tipoResumen", "PESCA*");
        
        if (request.piscina() != null) {
            params.put("piscina", request.piscina());
        }
        if (request.corrida() != null) {
            params.put("corrida", request.corrida());
        }
        
        return params;
    }

    /**
     * Convierte una entidad PrdResumenCorridaTO a un DTO de respuesta.
     *
     * @param entity Entidad de dominio
     * @return DTO de respuesta
     */
    public ResumenPescaResponse toResponse(PrdResumenCorridaTO entity) {
        if (entity == null) {
            return null;
        }

        return new ResumenPescaResponse(
            entity.getId(),
            entity.getPisNumero(),
            entity.getEmpCodigo(),
            entity.getSecCodigo(),
            entity.getRcHectareaje(),
            entity.getRcCorridaNumero(),
            entity.getRcFechaDesde(),
            entity.getRcFechaSiembra(),
            entity.getRcFechaPesca(),
            entity.getRcFechaHasta(),
            entity.getRcEdad(),
            entity.getRcEdadInicial(),
            entity.getRcDiasSecos(),
            entity.getRcNumeroLarvas(),
            entity.getRcCodigoLarvaLote(),
            entity.getRcDensidad(),
            entity.getRcLaboratorio(),
            entity.getRcNauplio(),
            entity.getRcLibrasBalanceados(),
            entity.getRcBalanceadoTransferido(),
            entity.getRcCostoBalanceado(),
            entity.getRcCostoBalanceadoTransferido(),
            entity.getRcBiomasa(),
            entity.getRcBiomasaReal(),
            entity.getRcBiomasaProyectada(),
            entity.getRcVentaProyectada(),
            entity.getRcConversion(),
            entity.getRcTGrande(),
            entity.getRcTMediano(),
            entity.getRcTPequeno(),
            entity.getRcTPromedio(),
            entity.getRcCrecimientoDia(),
            entity.getRcPesoIdeal(),
            entity.getRcSobrevivencia(),
            entity.getRcCostoTotal(),
            entity.getRcCostoDirecto(),
            entity.getRcCostoIndirecto(),
            entity.getRcCostoTransferencia(),
            entity.getRcCostoDirectoTransferido(),
            entity.getRcCostoIndirectoTransferido(),
            entity.getRcValorVenta(),
            entity.getRcValorVentaTransferida(),
            entity.getRcResultado(),
            entity.getRcRendimiento(),
            entity.getRcCostoHectarea(),
            entity.getRcVentaHectarea(),
            entity.getRcResultadoHectarea(),
            entity.getRcResultadoHectareaDia(),
            entity.getRcCostoLibra(),
            entity.getRcVentaLibra(),
            entity.getRcResultadoLibra(),
            entity.getRcCostoTotalHectareaDia(),
            entity.getRcCostoDirectoHectareaDia(),
            entity.getRcCostoindirectoHectareaDia(),
            entity.getRcPesoInicial(),
            entity.getRcCostoTransferenciaHectareaDia(),
            entity.getRcCostoDirectoTransferidoHectareaDia(),
            entity.getRcCostoIndirectoTransferidoHectareaDia(),
            entity.getRcOtrosCostos(),
            entity.getRcOtrosHectareaDias(),
            entity.getRcAnimales(),
            entity.getRcCorridaOrigen(),
            entity.getRcCorridaDestino(),
            entity.getRcCostoMillar(),
            entity.getRcBiomasaEmpacadora(),
            entity.getRcPesoCosecha(),
            entity.getRcTipoLiquidacion(),
            entity.getRcBiomasaTransferida(),
            entity.getRcLibrasPescadas(),
            entity.getRcPesoPromedioCosechado(),
            entity.getRcEmpacadoraCodigo()
        );
    }

    /**
     * Convierte una lista de entidades a una lista de DTOs de respuesta.
     *
     * @param entities Lista de entidades
     * @return Lista de DTOs de respuesta
     */
    public List<ResumenPescaResponse> toResponseList(List<PrdResumenCorridaTO> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
            .map(this::toResponse)
            .toList();
    }
}

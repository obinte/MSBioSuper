package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.LiquidacionRequest;
import com.acosux.MSBioSuper.application.dto.LiquidacionResponse;
import com.acosux.MSBioSuper.domain.model.PrdLiquidacionesDetalleTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapeador para convertir entre DTOs y entidades de Liquidación.
 * Implementa mapeo manual para mayor control y claridad.
 */
@Component
public class LiquidacionMapper {

    /**
     * Convierte un LiquidacionRequest a un Map de parámetros
     * para mantener compatibilidad con la capa de servicio existente.
     *
     * @param request DTO de solicitud
     * @return Map con los parámetros
     */
    public Map<String, Object> toParameterMap(LiquidacionRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.empresa());
        params.put("fechaDesde", request.fechaDesde());
        params.put("fechaHasta", request.fechaHasta());
        
        if (request.sector() != null) {
            params.put("sector", request.sector());
        }
        if (request.piscina() != null) {
            params.put("piscina", request.piscina());
        }
        if (request.corrida() != null) {
            params.put("corrida", request.corrida());
        }
        if (request.cliente() != null) {
            params.put("cliente", request.cliente());
        }
        if (request.tipo() != null) {
            params.put("tipo", request.tipo());
        }
        
        return params;
    }

    /**
     * Convierte una entidad PrdLiquidacionesDetalleTO a un DTO de respuesta.
     *
     * @param entity Entidad de dominio
     * @return DTO de respuesta
     */
    public LiquidacionResponse toResponse(PrdLiquidacionesDetalleTO entity) {
        if (entity == null) {
            return null;
        }

        return new LiquidacionResponse(
            entity.getId(),
            entity.getLiqNumero(),
            entity.getLiqFecha(),
            entity.getLiqCliente(),
            entity.getLiqLote(),
            entity.getLiqCorrida(),
            entity.getLiqLibrasEnviadas(),
            entity.getLiqLibrasRecibidas(),
            entity.getLiqDiferencia(),
            entity.getLiqLibrasBasura(),
            entity.getLiqLibrasNetas(),
            entity.getLiqLibrasEntero(),
            entity.getLiqRendimiento(),
            entity.getLiq20_30(),
            entity.getLiq30_40(),
            entity.getLiq40_50(),
            entity.getLiq50_60(),
            entity.getLiq60_70(),
            entity.getLiqOtrasTallas(),
            entity.getLiqTotalMonto(),
            entity.getLiqPrecioLibraPromedio(),
            entity.getLiqMes(),
            entity.getLiqTipo(),
            entity.getLiqGramaje(),
            entity.getLiqIdCliente()
        );
    }

    /**
     * Convierte una lista de entidades a una lista de DTOs de respuesta.
     *
     * @param entities Lista de entidades
     * @return Lista de DTOs de respuesta
     */
    public List<LiquidacionResponse> toResponseList(List<PrdLiquidacionesDetalleTO> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
            .map(this::toResponse)
            .toList();
    }
}

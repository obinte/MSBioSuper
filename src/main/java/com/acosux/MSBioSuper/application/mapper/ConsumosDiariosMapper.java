package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.ConsumosDiariosRequest;
import com.acosux.MSBioSuper.application.dto.ConsumosDiariosResponse;
import com.acosux.MSBioSuper.domain.model.PrdConsumosDiarioMSTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapeador para convertir entre DTOs y entidades de Consumos Diarios.
 * Implementa mapeo manual para mayor control y claridad.
 */
@Component
public class ConsumosDiariosMapper {

    /**
     * Convierte un ConsumosDiariosRequest a un Map de parámetros
     * para mantener compatibilidad con la capa de servicio existente.
     *
     * @param request DTO de solicitud
     * @return Map con los parámetros
     */
    public Map<String, Object> toParameterMap(ConsumosDiariosRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.empresa());
        params.put("sector", request.sector());
        params.put("fechaDesde", request.fechaDesde());
        params.put("fechaHasta", request.fechaHasta());
        
        if (request.piscina() != null) {
            params.put("piscina", request.piscina());
        }
        if (request.bodega() != null) {
            params.put("bodega", request.bodega());
        }
        if (request.producto() != null) {
            params.put("producto", request.producto());
        }
        
        return params;
    }

    /**
     * Convierte una entidad PrdConsumosDiarioMSTO a un DTO de respuesta.
     *
     * @param entity Entidad de dominio
     * @return DTO de respuesta
     */
    public ConsumosDiariosResponse toResponse(PrdConsumosDiarioMSTO entity) {
        if (entity == null) {
            return null;
        }

        return new ConsumosDiariosResponse(
            entity.getId(),
            entity.getConsumoEmpresa(),
            entity.getConsumoSector(),
            entity.getConsumoPiscina(),
            entity.getConsumoFecha(),
            entity.getConsumoProducto(),
            entity.getConsumoCodigo(),
            entity.getConsumoMedida(),
            entity.getConsumoTotal(),
            entity.getConsumoCostoTotal(),
            entity.getConsumoCorrida()
        );
    }

    /**
     * Convierte una lista de entidades a una lista de DTOs de respuesta.
     *
     * @param entities Lista de entidades
     * @return Lista de DTOs de respuesta
     */
    public List<ConsumosDiariosResponse> toResponseList(List<PrdConsumosDiarioMSTO> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
            .map(this::toResponse)
            .toList();
    }
}

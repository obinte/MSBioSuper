package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.CuentasPorCobrarRequest;
import com.acosux.MSBioSuper.application.dto.CuentasPorCobrarResponse;
import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapeador para convertir entre DTOs y entidades de Cuentas por Cobrar.
 * Implementa mapeo manual para mayor control y claridad.
 */
@Component
public class CuentasPorCobrarMapper {

    /**
     * Convierte un CuentasPorCobrarRequest a un Map de parámetros
     * para mantener compatibilidad con la capa de servicio existente.
     *
     * @param request DTO de solicitud
     * @return Map con los parámetros
     */
    public Map<String, Object> toParameterMap(CuentasPorCobrarRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.empresa());
        params.put("fechaDesde", request.fechaDesde());
        params.put("fechaHasta", request.fechaHasta());
        
        if (request.clienteId() != null) {
            params.put("clienteId", request.clienteId());
        }
        if (request.periodo() != null) {
            params.put("periodo", request.periodo());
        }
        if (request.motivo() != null) {
            params.put("motivo", request.motivo());
        }
        if (request.numeroDocumento() != null) {
            params.put("numeroDocumento", request.numeroDocumento());
        }
        
        return params;
    }

    /**
     * Convierte una entidad CuentasPorCobrarDetalladoApiTO a un DTO de respuesta.
     *
     * @param entity Entidad de dominio
     * @return DTO de respuesta
     */
    public CuentasPorCobrarResponse toResponse(CuentasPorCobrarDetalladoApiTO entity) {
        if (entity == null) {
            return null;
        }

        return new CuentasPorCobrarResponse(
            entity.getId(),
            entity.getCxcdEmpresa(),
            entity.getCxcdPeriodo(),
            entity.getCxcdMotivo(),
            entity.getCxcdNumero(),
            entity.getCxcdDocumentoNumero(),
            entity.getCxcdClienteId(),
            entity.getCxcdFechaEmision(),
            entity.getCxcdFechaVencimiento(),
            entity.getCxcdRetencionValor(),
            entity.getCxcdAbono(),
            entity.getCxcdSaldo(),
            entity.getCxcdTotal()
        );
    }

    /**
     * Convierte una lista de entidades a una lista de DTOs de respuesta.
     *
     * @param entities Lista de entidades
     * @return Lista de DTOs de respuesta
     */
    public List<CuentasPorCobrarResponse> toResponseList(List<CuentasPorCobrarDetalladoApiTO> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
            .map(this::toResponse)
            .toList();
    }
}

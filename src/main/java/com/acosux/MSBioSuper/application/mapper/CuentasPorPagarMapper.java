package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.CuentasPorPagarRequest;
import com.acosux.MSBioSuper.application.dto.CuentasPorPagarResponse;
import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapeador para convertir entre DTOs y entidades de Cuentas por Pagar.
 * Implementa mapeo manual para mayor control y claridad.
 */
@Component
public class CuentasPorPagarMapper {

    /**
     * Convierte un CuentasPorPagarRequest a un Map de parámetros
     * para mantener compatibilidad con la capa de servicio existente.
     *
     * @param request DTO de solicitud
     * @return Map con los parámetros
     */
    public Map<String, Object> toParameterMap(CuentasPorPagarRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.empresa());
        params.put("fechaDesde", request.fechaDesde());
        params.put("fechaHasta", request.fechaHasta());
        
        if (request.proveedorId() != null) {
            params.put("proveedorId", request.proveedorId());
        }
        if (request.periodo() != null) {
            params.put("periodo", request.periodo());
        }
        if (request.motivo() != null) {
            params.put("motivo", request.motivo());
        }
        if (request.zona() != null) {
            params.put("zona", request.zona());
        }
        if (request.proveedorRelacionado() != null) {
            params.put("proveedorRelacionado", request.proveedorRelacionado());
        }
        
        return params;
    }

    /**
     * Convierte una entidad CuentasPorPagarDetalladoTO a un DTO de respuesta.
     *
     * @param entity Entidad de dominio
     * @return DTO de respuesta
     */
    public CuentasPorPagarResponse toResponse(CuentasPorPagarDetalladoTO entity) {
        if (entity == null) {
            return null;
        }

        return new CuentasPorPagarResponse(
            entity.getId(),
            entity.getCxpdPeriodo(),
            entity.getCxpdMotivo(),
            entity.getCxpdNumero(),
            entity.getCxpdDocumento(),
            entity.getCxpdObservaciones(),
            entity.getCxpdProveedorCodigo(),
            entity.getCxpdProveedorId(),
            entity.getCxpdProveedorRazonSocial(),
            entity.getCxpdFechaEmision(),
            entity.getCxpdFechaVencimiento(),
            entity.getCxpdSaldo(),
            entity.getCxpdUsuarioApruebaPago(),
            entity.getCxpdDiasCredito(),
            entity.getCxpdDiasVencidos(),
            entity.getCxpdZona(),
            entity.getCxpdSaldoVencido(),
            entity.getCxpdValorAprobado(),
            entity.getCxpdAbonos(),
            entity.getCxpdSaldoPorVencer(),
            entity.getCxpdCp(),
            entity.getCxpdProvRelacionado()
        );
    }

    /**
     * Convierte una lista de entidades a una lista de DTOs de respuesta.
     *
     * @param entities Lista de entidades
     * @return Lista de DTOs de respuesta
     */
    public List<CuentasPorPagarResponse> toResponseList(List<CuentasPorPagarDetalladoTO> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
            .map(this::toResponse)
            .toList();
    }
}

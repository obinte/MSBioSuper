package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.CuentasPorPagarRequest;
import com.acosux.MSBioSuper.application.dto.CuentasPorPagarResponse;
import com.acosux.MSBioSuper.application.mapper.CuentasPorPagarMapper;
import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;
import com.acosux.MSBioSuper.domain.repository.CuentasPorPagarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para obtener cuentas por pagar.
 * Implementa la lógica de negocio para consultar cuentas por pagar detalladas
 * según los criterios especificados en la solicitud.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 * 
 * NOTA: readOnly = false porque la función de PostgreSQL puede crear tablas temporales.
 */
@Service
@Transactional(readOnly = false)
public class GetCuentasPorPagarUseCase {

    private final CuentasPorPagarRepository cuentasPorPagarRepository;
    private final CuentasPorPagarMapper cuentasPorPagarMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param cuentasPorPagarRepository Repositorio de cuentas por pagar
     * @param cuentasPorPagarMapper Mapeador de DTOs
     */
    public GetCuentasPorPagarUseCase(
            CuentasPorPagarRepository cuentasPorPagarRepository,
            CuentasPorPagarMapper cuentasPorPagarMapper) {
        this.cuentasPorPagarRepository = cuentasPorPagarRepository;
        this.cuentasPorPagarMapper = cuentasPorPagarMapper;
    }

    /**
     * Ejecuta el caso de uso para obtener cuentas por pagar.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Rango de fechas (obligatorio)
     * - Proveedor (opcional)
     * - Periodo (opcional)
     * - Motivo (opcional)
     * - Zona (opcional)
     * - Proveedor relacionado (opcional)
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de cuentas por pagar que cumplen los criterios
     */
    public List<CuentasPorPagarResponse> execute(CuentasPorPagarRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Obtener las cuentas por pagar desde el repositorio usando la función de PostgreSQL
        List<CuentasPorPagarDetalladoTO> cuentasEntidad = cuentasPorPagarRepository.findCuentasPorPagarDetallado(
                request.empresa(),
                null, // sector - no se especifica en el request básico
                request.proveedorId(),
                request.fechaDesde(),
                request.fechaHasta(),
                false, // excluirAprobadas - por defecto false
                false, // incluirCheques - por defecto false
                null // formatoMensual - no se especifica
        );

        // Aplicar filtros adicionales si se especifican
        if (request.periodo() != null && !request.periodo().isBlank()) {
            cuentasEntidad = cuentasEntidad.stream()
                    .filter(c -> request.periodo().equals(c.getCxpdPeriodo()))
                    .toList();
        }

        if (request.motivo() != null && !request.motivo().isBlank()) {
            cuentasEntidad = cuentasEntidad.stream()
                    .filter(c -> request.motivo().equals(c.getCxpdMotivo()))
                    .toList();
        }

        if (request.zona() != null && !request.zona().isBlank()) {
            cuentasEntidad = cuentasEntidad.stream()
                    .filter(c -> request.zona().equals(c.getCxpdZona()))
                    .toList();
        }

        if (request.proveedorRelacionado() != null) {
            cuentasEntidad = cuentasEntidad.stream()
                    .filter(c -> request.proveedorRelacionado().equals(c.getCxpdProvRelacionado()))
                    .toList();
        }

        // Convertir las entidades de dominio a DTOs de respuesta
        return cuentasPorPagarMapper.toResponseList(cuentasEntidad);
    }
}

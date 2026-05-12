package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.CuentasPorCobrarRequest;
import com.acosux.MSBioSuper.application.dto.CuentasPorCobrarResponse;
import com.acosux.MSBioSuper.application.mapper.CuentasPorCobrarMapper;
import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
import com.acosux.MSBioSuper.domain.repository.CuentasPorCobrarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para obtener cuentas por cobrar.
 * Implementa la lógica de negocio para consultar cuentas por cobrar detalladas
 * según los criterios especificados en la solicitud.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 * 
 * NOTA: readOnly = false porque la función de PostgreSQL crea tablas temporales.
 */
@Service
@Transactional(readOnly = false)
public class GetCuentasPorCobrarUseCase {

    private final CuentasPorCobrarRepository cuentasPorCobrarRepository;
    private final CuentasPorCobrarMapper cuentasPorCobrarMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param cuentasPorCobrarRepository Repositorio de cuentas por cobrar
     * @param cuentasPorCobrarMapper Mapeador de DTOs
     */
    public GetCuentasPorCobrarUseCase(
            CuentasPorCobrarRepository cuentasPorCobrarRepository,
            CuentasPorCobrarMapper cuentasPorCobrarMapper) {
        this.cuentasPorCobrarRepository = cuentasPorCobrarRepository;
        this.cuentasPorCobrarMapper = cuentasPorCobrarMapper;
    }

    /**
     * Ejecuta el caso de uso para obtener cuentas por cobrar.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Fecha hasta (obligatoria)
     * - Cliente (opcional)
     * - Periodo (opcional)
     * - Motivo (opcional)
     * - Número de documento (opcional)
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de cuentas por cobrar que cumplen los criterios
     */
    public List<CuentasPorCobrarResponse> execute(CuentasPorCobrarRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Obtener las cuentas por cobrar desde el repositorio usando la función de PostgreSQL
        List<CuentasPorCobrarDetalladoApiTO> cuentasEntidad = cuentasPorCobrarRepository.findCuentasPorCobrarDetalladoApi(
                request.empresa(),
                request.fechaHasta()
        );

        // Aplicar filtros adicionales si se especifican
        if (request.clienteId() != null && !request.clienteId().isBlank()) {
            cuentasEntidad = cuentasEntidad.stream()
                    .filter(c -> request.clienteId().equals(c.getCxcdClienteId()))
                    .toList();
        }

        if (request.periodo() != null && !request.periodo().isBlank()) {
            cuentasEntidad = cuentasEntidad.stream()
                    .filter(c -> request.periodo().equals(c.getCxcdPeriodo()))
                    .toList();
        }

        if (request.motivo() != null && !request.motivo().isBlank()) {
            cuentasEntidad = cuentasEntidad.stream()
                    .filter(c -> request.motivo().equals(c.getCxcdMotivo()))
                    .toList();
        }

        if (request.numeroDocumento() != null && !request.numeroDocumento().isBlank()) {
            cuentasEntidad = cuentasEntidad.stream()
                    .filter(c -> request.numeroDocumento().equals(c.getCxcdDocumentoNumero()))
                    .toList();
        }

        // Convertir las entidades de dominio a DTOs de respuesta
        return cuentasPorCobrarMapper.toResponseList(cuentasEntidad);
    }
}

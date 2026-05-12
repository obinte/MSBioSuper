package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ResumenSiembraRequest;
import com.acosux.MSBioSuper.application.dto.ResumenSiembraResponse;
import com.acosux.MSBioSuper.application.mapper.ResumenSiembraMapper;
import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import com.acosux.MSBioSuper.domain.repository.ResumenSiembraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para obtener el resumen de siembra.
 * Implementa la lógica de negocio para consultar resúmenes de corridas de siembra
 * según los criterios especificados en la solicitud.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 */
@Service
@Transactional(readOnly = false)
public class GetResumenSiembraUseCase {

    private final ResumenSiembraRepository resumenSiembraRepository;
    private final ResumenSiembraMapper resumenSiembraMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param resumenSiembraRepository Repositorio de resumen de siembra
     * @param resumenSiembraMapper Mapeador de DTOs
     */
    public GetResumenSiembraUseCase(
            ResumenSiembraRepository resumenSiembraRepository,
            ResumenSiembraMapper resumenSiembraMapper) {
        this.resumenSiembraRepository = resumenSiembraRepository;
        this.resumenSiembraMapper = resumenSiembraMapper;
    }

    /**
     * Ejecuta el caso de uso para obtener el resumen de siembra.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Sector (obligatorio)
     * - Rango de fechas (obligatorio)
     * - Piscina (opcional)
     * - Corrida (opcional)
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de resúmenes de siembra que cumplen los criterios
     */
    public List<ResumenSiembraResponse> execute(ResumenSiembraRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Obtener los resúmenes de siembra desde el repositorio
        List<PrdResumenCorridaTO> resumenesEntidad = resumenSiembraRepository.findResumenSiembraMejorado(
                request.empresa(),
                request.sector(),
                request.fechaDesde(),
                request.fechaHasta(),
                true // Ordenar por corrida
        );

        // Aplicar filtros adicionales si se especifican
        if (request.piscina() != null && !request.piscina().isBlank()) {
            resumenesEntidad = resumenesEntidad.stream()
                    .filter(r -> request.piscina().equals(r.getPisNumero()))
                    .toList();
        }

        if (request.corrida() != null && !request.corrida().isBlank()) {
            resumenesEntidad = resumenesEntidad.stream()
                    .filter(r -> request.corrida().equals(r.getRcCorridaNumero()))
                    .toList();
        }

        // Convertir las entidades de dominio a DTOs de respuesta
        return resumenSiembraMapper.toResponseList(resumenesEntidad);
    }
}

package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ResumenPescaRequest;
import com.acosux.MSBioSuper.application.dto.ResumenPescaResponse;
import com.acosux.MSBioSuper.application.mapper.ResumenPescaMapper;
import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import com.acosux.MSBioSuper.domain.repository.ResumenPescaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para obtener el resumen de pesca.
 * Implementa la lógica de negocio para consultar resúmenes de corridas de pesca
 * según los criterios especificados en la solicitud.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 */
@Service
@Transactional(readOnly = false)
public class GetResumenPescaUseCase {

    private final ResumenPescaRepository resumenPescaRepository;
    private final ResumenPescaMapper resumenPescaMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param resumenPescaRepository Repositorio de resumen de pesca
     * @param resumenPescaMapper Mapeador de DTOs
     */
    public GetResumenPescaUseCase(
            ResumenPescaRepository resumenPescaRepository,
            ResumenPescaMapper resumenPescaMapper) {
        this.resumenPescaRepository = resumenPescaRepository;
        this.resumenPescaMapper = resumenPescaMapper;
    }

    /**
     * Ejecuta el caso de uso para obtener el resumen de pesca.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Sector (obligatorio)
     * - Rango de fechas (obligatorio)
     * - Piscina (opcional)
     * - Corrida (opcional)
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de resúmenes de pesca que cumplen los criterios
     */
    public List<ResumenPescaResponse> execute(ResumenPescaRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Obtener los resúmenes de pesca desde el repositorio
        List<PrdResumenCorridaTO> resumenesEntidad = resumenPescaRepository.findResumenCorridaMejorado(
                request.empresa(),
                request.sector(),
                "PESCA*", // Tipo de resumen fijo para pesca
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
        return resumenPescaMapper.toResponseList(resumenesEntidad);
    }
}

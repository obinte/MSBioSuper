package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ResumenPescaRequest;
import com.acosux.MSBioSuper.domain.model.PrdListadoGrameajeTO;
import com.acosux.MSBioSuper.domain.repository.GrameajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para obtener el listado de gramajes.
 * Implementa la lógica de negocio para consultar gramajes de producción
 * según los criterios especificados en la solicitud.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 */
@Service
@Transactional(readOnly = false)
public class ObtenerGramajesUseCase {

    private final GrameajeRepository grameajeRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param grameajeRepository Repositorio de gramajes
     */
    public ObtenerGramajesUseCase(GrameajeRepository grameajeRepository) {
        this.grameajeRepository = grameajeRepository;
    }

    /**
     * Ejecuta el caso de uso para obtener el listado de gramajes.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Sector (obligatorio)
     * - Piscina (opcional)
     * - Rango de fechas (opcional)
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de gramajes que cumplen los criterios
     */
    public List<PrdListadoGrameajeTO> execute(ResumenPescaRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Obtener los gramajes desde el repositorio usando la función de PostgreSQL
        List<PrdListadoGrameajeTO> gramajesEntidad = grameajeRepository.findListadoGrameaje(
                request.empresa(),
                request.sector(),
                request.piscina(),
                request.fechaDesde(),
                request.fechaHasta()
        );

        // Aplicar filtro adicional por corrida si se especifica
        if (request.corrida() != null && !request.corrida().isBlank()) {
            gramajesEntidad = gramajesEntidad.stream()
                    .filter(g -> request.corrida().equals(g.getGraCorrida()))
                    .toList();
        }

        return gramajesEntidad;
    }
}

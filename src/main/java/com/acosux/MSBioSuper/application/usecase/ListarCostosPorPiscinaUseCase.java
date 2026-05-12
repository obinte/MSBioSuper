package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ResumenPescaRequest;
import com.acosux.MSBioSuper.domain.model.PrdListaCostosDetalleCorridaTO;
import com.acosux.MSBioSuper.domain.repository.CostosCorridaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para listar costos por piscina.
 * Implementa la lógica de negocio para consultar el detalle de costos
 * agrupados por piscina y corrida según los criterios especificados.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 */
@Service
@Transactional(readOnly = false)
public class ListarCostosPorPiscinaUseCase {

    private final CostosCorridaRepository costosCorridaRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param costosCorridaRepository Repositorio de costos de corrida
     */
    public ListarCostosPorPiscinaUseCase(CostosCorridaRepository costosCorridaRepository) {
        this.costosCorridaRepository = costosCorridaRepository;
    }

    /**
     * Ejecuta el caso de uso para listar costos por piscina.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Sector (obligatorio)
     * - Piscina (opcional)
     * - Corrida (opcional)
     * - Rango de fechas (opcional)
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de costos detallados por piscina
     */
    public List<PrdListaCostosDetalleCorridaTO> execute(ResumenPescaRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Obtener los costos desde el repositorio usando la función de PostgreSQL
        List<PrdListaCostosDetalleCorridaTO> costosEntidad = costosCorridaRepository.findCostosDetalleCorrida(
                request.empresa(),
                request.sector(),
                request.piscina(),
                request.corrida(),
                request.fechaDesde(),
                request.fechaHasta(),
                "PRODUCTO" // Agrupado por producto por defecto
        );

        return costosEntidad;
    }
}

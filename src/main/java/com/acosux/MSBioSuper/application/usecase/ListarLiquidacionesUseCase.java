package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.LiquidacionRequest;
import com.acosux.MSBioSuper.application.dto.LiquidacionResponse;
import com.acosux.MSBioSuper.application.mapper.LiquidacionMapper;
import com.acosux.MSBioSuper.domain.model.PrdLiquidacionesDetalleTO;
import com.acosux.MSBioSuper.domain.repository.LiquidacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para listar liquidaciones.
 * Implementa la lógica de negocio para consultar el listado general de liquidaciones
 * según los criterios especificados en la solicitud.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 */
@Service
@Transactional(readOnly = false)
public class ListarLiquidacionesUseCase {

    private final LiquidacionRepository liquidacionRepository;
    private final LiquidacionMapper liquidacionMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param liquidacionRepository Repositorio de liquidaciones
     * @param liquidacionMapper Mapeador de DTOs
     */
    public ListarLiquidacionesUseCase(
            LiquidacionRepository liquidacionRepository,
            LiquidacionMapper liquidacionMapper) {
        this.liquidacionRepository = liquidacionRepository;
        this.liquidacionMapper = liquidacionMapper;
    }

    /**
     * Ejecuta el caso de uso para listar liquidaciones.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Rango de fechas (obligatorio)
     * - Cliente (opcional)
     * - Tipo (opcional)
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de liquidaciones que cumplen los criterios
     */
    public List<LiquidacionResponse> execute(LiquidacionRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Obtener las liquidaciones desde el repositorio usando la función de PostgreSQL
        List<PrdLiquidacionesDetalleTO> liquidacionesEntidad = liquidacionRepository.findLiquidacionesDetalle(
                request.empresa(),
                request.fechaDesde(),
                request.fechaHasta()
        );

        // Aplicar filtros adicionales si se especifican
        if (request.cliente() != null && !request.cliente().isBlank()) {
            liquidacionesEntidad = liquidacionesEntidad.stream()
                    .filter(l -> request.cliente().equals(l.getLiqCliente()) || 
                                 request.cliente().equals(l.getLiqIdCliente()))
                    .toList();
        }

        if (request.tipo() != null && !request.tipo().isBlank()) {
            liquidacionesEntidad = liquidacionesEntidad.stream()
                    .filter(l -> request.tipo().equals(l.getLiqTipo()))
                    .toList();
        }

        if (request.corrida() != null && !request.corrida().isBlank()) {
            liquidacionesEntidad = liquidacionesEntidad.stream()
                    .filter(l -> request.corrida().equals(l.getLiqCorrida()))
                    .toList();
        }

        // Convertir las entidades de dominio a DTOs de respuesta
        return liquidacionMapper.toResponseList(liquidacionesEntidad);
    }
}

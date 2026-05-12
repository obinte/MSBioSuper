package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ConsumosDiariosRequest;
import com.acosux.MSBioSuper.application.dto.ConsumosDiariosResponse;
import com.acosux.MSBioSuper.application.mapper.ConsumosDiariosMapper;
import com.acosux.MSBioSuper.domain.model.PrdConsumosDiarioMSTO;
import com.acosux.MSBioSuper.domain.repository.ConsumosDiariosRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para obtener los consumos diarios.
 * Implementa la lógica de negocio para consultar consumos diarios de producción
 * según los criterios especificados en la solicitud.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 */
@Service
@Transactional(readOnly = false)
public class GetConsumosDiariosUseCase {

    private final ConsumosDiariosRepository consumosDiariosRepository;
    private final ConsumosDiariosMapper consumosDiariosMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param consumosDiariosRepository Repositorio de consumos diarios
     * @param consumosDiariosMapper Mapeador de DTOs
     */
    public GetConsumosDiariosUseCase(
            ConsumosDiariosRepository consumosDiariosRepository,
            ConsumosDiariosMapper consumosDiariosMapper) {
        this.consumosDiariosRepository = consumosDiariosRepository;
        this.consumosDiariosMapper = consumosDiariosMapper;
    }

    /**
     * Ejecuta el caso de uso para obtener los consumos diarios.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Sector (obligatorio)
     * - Rango de fechas (obligatorio)
     * - Piscina (opcional)
     * - Corrida (opcional)
     * - Producto (opcional)
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de consumos diarios que cumplen los criterios
     */
    public List<ConsumosDiariosResponse> execute(ConsumosDiariosRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Obtener los consumos diarios desde el repositorio usando la función de PostgreSQL
        // Orden de parámetros: empresa, sector, desde, hasta, piscina, bodega, producto
        List<PrdConsumosDiarioMSTO> consumosEntidad = consumosDiariosRepository.findConsumosDiariosMicroservice(
                request.empresa(),
                request.sector(),
                request.fechaDesde(),
                request.fechaHasta(),
                request.piscina(),
                request.bodega(),
                request.producto()
        );

        // Convertir las entidades de dominio a DTOs de respuesta
        return consumosDiariosMapper.toResponseList(consumosEntidad);
    }
}

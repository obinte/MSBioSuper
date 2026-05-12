package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ComprasDetalleRequest;
import com.acosux.MSBioSuper.domain.model.InvComprasDetalleProductoTO;
import com.acosux.MSBioSuper.domain.repository.ComprasRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para obtener el detalle de compras por producto.
 * Implementa la lógica de negocio para consultar compras detalladas
 * según los criterios especificados en la solicitud.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 */
@Service
@Transactional(readOnly = false)
public class GetComprasDetalleUseCase {

    private final ComprasRepository comprasRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param comprasRepository Repositorio de compras
     */
    public GetComprasDetalleUseCase(ComprasRepository comprasRepository) {
        this.comprasRepository = comprasRepository;
    }

    /**
     * Ejecuta el caso de uso para obtener el detalle de compras por producto.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Rango de fechas (obligatorio)
     * - Sector (opcional)
     * - Piscina (opcional)
     * - Proveedor (opcional)
     * - Producto (opcional)
     * - Bodega (opcional)
     * - IncluirIMB (opcional, por defecto false)
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de compras detalladas que cumplen los criterios
     */
    public List<InvComprasDetalleProductoTO> execute(ComprasDetalleRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Obtener las compras detalle desde el repositorio usando la función de PostgreSQL
        // Todos los parámetros opcionales se pasan tal como vienen del request
        List<InvComprasDetalleProductoTO> comprasEntidad = comprasRepository.findComprasDetalleProducto(
                request.empresa(),
                request.sector(),
                request.piscina(),
                request.proveedor(),
                request.producto(),
                request.fechaDesde(),
                request.fechaHasta(),
                request.bodega(),
                request.incluirIMB() // Ahora se usa el valor del request (por defecto false)
        );

        return comprasEntidad;
    }
}

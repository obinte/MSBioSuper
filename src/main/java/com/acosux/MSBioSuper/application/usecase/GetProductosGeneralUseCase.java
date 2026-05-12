package com.acosux.MSBioSuper.application.usecase;

import com.acosux.MSBioSuper.application.dto.ProductoRequest;
import com.acosux.MSBioSuper.application.dto.ProductoResponse;
import com.acosux.MSBioSuper.application.mapper.ProductoMapper;
import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;
import com.acosux.MSBioSuper.domain.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Caso de uso para obtener la lista general de productos.
 * Implementa la lógica de negocio para consultar productos con saldos y precios
 * según los criterios especificados en la solicitud.
 * 
 * Este caso de uso pertenece a la capa de aplicación y orquesta
 * las operaciones entre el dominio y la infraestructura.
 */
@Service
@Transactional(readOnly = false)
public class GetProductosGeneralUseCase {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param productoRepository Repositorio de productos
     * @param productoMapper Mapeador de DTOs
     */
    public GetProductosGeneralUseCase(
            ProductoRepository productoRepository,
            ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    /**
     * Ejecuta el caso de uso para obtener la lista general de productos.
     * 
     * Aplica los filtros especificados en la solicitud:
     * - Empresa (obligatorio)
     * - Bodega (obligatorio)
     * - Categoría (opcional)
     * - Búsqueda por texto (opcional)
     * - Código específico (opcional)
     * - Tipo de precio (opcional)
     * - Incluir inactivos (opcional)
     *
     * @param request DTO con los criterios de búsqueda
     * @return Lista de productos que cumplen los criterios
     */
    public List<ProductoResponse> execute(ProductoRequest request) {
        // Validar que la solicitud no sea nula
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula");
        }

        // Validar campos obligatorios
        if (request.empresa() == null || request.empresa().isBlank()) {
            throw new IllegalArgumentException("El código de empresa es obligatorio");
        }
        
        if (request.bodega() == null || request.bodega().isBlank()) {
            throw new IllegalArgumentException("El código de bodega es obligatorio");
        }

        // Preparar parámetros con valores por defecto
        String categoria = request.categoria() != null ? request.categoria() : "";
        String busqueda = request.busqueda() != null ? request.busqueda() : "";
        String bodega = request.bodega() != null ? request.bodega() : "";
        Integer tipoPrecio = request.tipoPrecio() != null ? request.tipoPrecio() : 1;
        boolean incluirInactivos = request.incluirInactivos() != null ? request.incluirInactivos() : false;

        // Obtener los productos desde el repositorio usando la función de PostgreSQL
        List<InvListaProductosGeneralTO> productosEntidad = productoRepository.findProductosSaldosPreciosWeb(
                request.empresa(),
                bodega,
                request.codigoBuscar(),
                categoria,
                busqueda,
                null, // fecha - usar fecha actual por defecto
                tipoPrecio,
                incluirInactivos
        );

        // Convertir las entidades de dominio a DTOs de respuesta
        return productoMapper.toResponseList(productosEntidad);
    }
}

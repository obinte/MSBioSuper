package com.acosux.MSBioSuper.application.mapper;

import com.acosux.MSBioSuper.application.dto.ProductoRequest;
import com.acosux.MSBioSuper.application.dto.ProductoResponse;
import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapeador para convertir entre DTOs y entidades de Producto.
 * Implementa mapeo manual para mayor control y claridad.
 */
@Component
public class ProductoMapper {

    /**
     * Convierte un ProductoRequest a un Map de parámetros
     * para mantener compatibilidad con la capa de servicio existente.
     *
     * @param request DTO de solicitud
     * @return Map con los parámetros
     */
    public Map<String, Object> toParameterMap(ProductoRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.empresa());
        params.put("bodega", request.bodega());
        
        if (request.categoria() != null) {
            params.put("categoria", request.categoria());
        }
        if (request.codigoBuscar() != null) {
            params.put("codigoBuscar", request.codigoBuscar());
        }
        if (request.busqueda() != null) {
            params.put("busqueda", request.busqueda());
        }
        if (request.tipoPrecio() != null) {
            params.put("tipoPrecio", request.tipoPrecio());
        }
        if (request.incluirInactivos() != null) {
            params.put("incluirInactivos", request.incluirInactivos());
        }
        
        return params;
    }

    /**
     * Convierte una entidad InvListaProductosGeneralTO a un DTO de respuesta.
     *
     * @param entity Entidad de dominio
     * @return DTO de respuesta
     */
    public ProductoResponse toResponse(InvListaProductosGeneralTO entity) {
        if (entity == null) {
            return null;
        }

        return new ProductoResponse(
            entity.getId(),
            entity.getProCodigoPrincipal(),
            entity.getProNombre(),
            entity.getProCategoria(),
            entity.getProEmpaque(),
            entity.getStockSaldo(),
            entity.getStockUltimoCosto(),
            entity.getStockCostoPromedio(),
            entity.getFactorConversion(),
            entity.getDetalleMedida(),
            entity.getProPrecio(),
            entity.getProCantidad(),
            entity.getProPrecioBase(),
            entity.getProDescuento(),
            entity.getProMargenUtilidad(),
            entity.getProPrecio2(),
            entity.getProPrecio3(),
            entity.getProGravaIva(),
            entity.getTipTipo(),
            entity.isPrecioFijo(),
            entity.isProInactivo(),
            entity.isProExigirSerie(),
            entity.getProCostoReferencial(),
            entity.getTieneImagenes(),
            entity.getProCreditoTributario(),
            entity.getProEcommerce(),
            entity.getTipTipoTipoProducto(),
            entity.getIcePorcentaje(),
            entity.getIceTarifaFija(),
            entity.getIceCodigo(),
            entity.getLpspExigirLote(),
            entity.getLpspSubcategoria(),
            entity.getLpspCuentaInventario(),
            entity.getLpspNombreInventario(),
            entity.getLpspCuentaCostoConsumo(),
            entity.getLpspNombreCostoConsumo(),
            entity.getLpspPiCodigo(),
            entity.getLpspPiPorcentaje()
        );
    }

    /**
     * Convierte una lista de entidades a una lista de DTOs de respuesta.
     *
     * @param entities Lista de entidades
     * @return Lista de DTOs de respuesta
     */
    public List<ProductoResponse> toResponseList(List<InvListaProductosGeneralTO> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream()
            .map(this::toResponse)
            .toList();
    }
}

package com.acosux.MSBioSuper.domain.service;

import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para operaciones de negocio relacionadas con productos.
 * 
 * Este servicio encapsula la lógica de negocio compleja para el filtrado,
 * validación y procesamiento de productos según reglas de negocio específicas.
 * 
 * Principios de diseño:
 * - No tiene dependencias de infraestructura (Spring, JPA, etc.)
 * - Contiene lógica de negocio pura
 * - Opera sobre entidades de dominio
 * - Es stateless (sin estado)
 * 
 * @author Josias Rubén Aldaz Arcalles
 */
public class ProductoService {

    /**
     * Límite máximo de productos a retornar cuando se aplica limitación.
     */
    private static final int LIMITE_MAXIMO_PRODUCTOS = 1000;

    /**
     * Aplica filtros de negocio a una lista de productos.
     * 
     * Este método aplica los siguientes filtros según los parámetros:
     * 1. Filtro por código exacto (si buscarPorCodigo es true)
     * 2. Filtro por tipo de producto
     * 3. Limitación de resultados (máximo 1000 productos)
     * 
     * @param productos Lista de productos a filtrar
     * @param busqueda Término de búsqueda (código del producto)
     * @param buscarPorCodigo Si true, filtra por código exacto
     * @param tipo Tipo de producto a filtrar (puede ser null)
     * @param aplicarLimite Si true, limita los resultados a LIMITE_MAXIMO_PRODUCTOS
     * @return Lista de productos filtrados según los criterios
     */
    public List<InvListaProductosGeneralTO> aplicarFiltrosNegocio(
            List<InvListaProductosGeneralTO> productos,
            String busqueda,
            boolean buscarPorCodigo,
            String tipo,
            boolean aplicarLimite) {

        if (productos == null || productos.isEmpty()) {
            return List.of();
        }

        List<InvListaProductosGeneralTO> productosFiltrados = productos;

        // Filtro 1: Búsqueda por código exacto
        if (buscarPorCodigo && busqueda != null && !busqueda.isEmpty()) {
            productosFiltrados = filtrarPorCodigoExacto(productosFiltrados, busqueda);
        }

        // Filtro 2: Filtrar por tipo de producto
        if (tipo != null && !tipo.isEmpty()) {
            productosFiltrados = filtrarPorTipo(productosFiltrados, tipo);
        }

        // Filtro 3: Aplicar límite de resultados
        if (aplicarLimite && productosFiltrados.size() > LIMITE_MAXIMO_PRODUCTOS) {
            productosFiltrados = aplicarLimiteResultados(productosFiltrados);
        }

        return productosFiltrados;
    }

    /**
     * Filtra productos por código exacto.
     * 
     * Regla de negocio: Solo se incluyen productos cuyo código principal
     * coincida exactamente con el término de búsqueda.
     * 
     * @param productos Lista de productos a filtrar
     * @param codigoBuscado Código a buscar
     * @return Lista de productos que coinciden con el código
     */
    private List<InvListaProductosGeneralTO> filtrarPorCodigoExacto(
            List<InvListaProductosGeneralTO> productos,
            String codigoBuscado) {

        return productos.stream()
                .filter(p -> codigoBuscado.equals(p.getProCodigoPrincipal()))
                .collect(Collectors.toList());
    }

    /**
     * Filtra productos por tipo.
     * 
     * Regla de negocio: Solo se incluyen productos cuyo tipo contenga
     * el valor especificado (búsqueda parcial, case-sensitive).
     * 
     * @param productos Lista de productos a filtrar
     * @param tipo Tipo de producto a buscar
     * @return Lista de productos que coinciden con el tipo
     */
    private List<InvListaProductosGeneralTO> filtrarPorTipo(
            List<InvListaProductosGeneralTO> productos,
            String tipo) {

        return productos.stream()
                .filter(p -> p.getTipTipo() != null && p.getTipTipo().contains(tipo))
                .collect(Collectors.toList());
    }

    /**
     * Aplica límite de resultados a la lista de productos.
     * 
     * Regla de negocio: Para evitar sobrecarga del sistema, se limita
     * el número máximo de productos retornados a LIMITE_MAXIMO_PRODUCTOS.
     * 
     * @param productos Lista de productos a limitar
     * @return Sublista con máximo LIMITE_MAXIMO_PRODUCTOS elementos
     */
    private List<InvListaProductosGeneralTO> aplicarLimiteResultados(
            List<InvListaProductosGeneralTO> productos) {

        return productos.subList(0, LIMITE_MAXIMO_PRODUCTOS);
    }

    /**
     * Valida que un producto tenga los datos mínimos requeridos.
     * 
     * Reglas de validación:
     * - El producto debe tener un código principal
     * - El producto debe tener un nombre
     * 
     * @param producto Producto a validar
     * @return true si el producto es válido, false en caso contrario
     */
    public boolean esProductoValido(InvListaProductosGeneralTO producto) {
        if (producto == null) {
            return false;
        }

        return producto.getProCodigoPrincipal() != null 
                && !producto.getProCodigoPrincipal().isEmpty()
                && producto.getProNombre() != null 
                && !producto.getProNombre().isEmpty();
    }

    /**
     * Filtra productos válidos según reglas de negocio.
     * 
     * @param productos Lista de productos a filtrar
     * @return Lista de productos válidos
     */
    public List<InvListaProductosGeneralTO> filtrarProductosValidos(
            List<InvListaProductosGeneralTO> productos) {

        if (productos == null) {
            return List.of();
        }

        return productos.stream()
                .filter(this::esProductoValido)
                .collect(Collectors.toList());
    }
}

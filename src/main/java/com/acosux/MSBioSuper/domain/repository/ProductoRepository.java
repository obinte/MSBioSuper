package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) para acceso a datos de productos.
 * Define el contrato para operaciones de consulta de productos del inventario.
 * Esta interfaz pertenece a la capa de dominio y no debe tener dependencias de frameworks.
 */
public interface ProductoRepository {

    /**
     * Busca un producto por su identificador único.
     *
     * @param id Identificador del producto
     * @return Optional con el producto si existe, vacío en caso contrario
     */
    Optional<InvListaProductosGeneralTO> findById(Integer id);

    /**
     * Busca un producto por su código principal.
     *
     * @param codigoPrincipal Código principal del producto
     * @return Optional con el producto si existe, vacío en caso contrario
     */
    Optional<InvListaProductosGeneralTO> findByProCodigoPrincipal(String codigoPrincipal);

    /**
     * Busca productos por nombre (búsqueda parcial).
     *
     * @param nombre Nombre o parte del nombre del producto
     * @return Lista de productos que coinciden con el nombre
     */
    List<InvListaProductosGeneralTO> findByNombreContaining(String nombre);

    /**
     * Busca productos por categoría.
     *
     * @param categoria Categoría del producto
     * @return Lista de productos de la categoría
     */
    List<InvListaProductosGeneralTO> findByCategoria(String categoria);

    /**
     * Busca productos por subcategoría.
     *
     * @param subcategoria Subcategoría del producto
     * @return Lista de productos de la subcategoría
     */
    List<InvListaProductosGeneralTO> findBySubcategoria(String subcategoria);

    /**
     * Busca productos por tipo.
     *
     * @param tipo Tipo de producto
     * @return Lista de productos del tipo especificado
     */
    List<InvListaProductosGeneralTO> findByTipo(String tipo);

    /**
     * Busca productos activos.
     *
     * @return Lista de productos activos (no inactivos)
     */
    List<InvListaProductosGeneralTO> findByActivoTrue();

    /**
     * Busca productos inactivos.
     *
     * @return Lista de productos inactivos
     */
    List<InvListaProductosGeneralTO> findByActivoFalse();

    /**
     * Busca productos que gravan IVA.
     *
     * @param gravaIva Indicador de si grava IVA
     * @return Lista de productos que gravan IVA
     */
    List<InvListaProductosGeneralTO> findByGravaIva(String gravaIva);

    /**
     * Busca productos por categoría y estado activo.
     *
     * @param categoria Categoría del producto
     * @param activo Estado activo del producto
     * @return Lista de productos de la categoría con el estado especificado
     */
    List<InvListaProductosGeneralTO> findByCategoriaAndActivo(String categoria, boolean activo);

    /**
     * Busca productos habilitados para e-commerce.
     *
     * @return Lista de productos habilitados para e-commerce
     */
    List<InvListaProductosGeneralTO> findByEcommerceTrue();

    /**
     * Busca productos que exigen serie.
     *
     * @return Lista de productos que exigen serie
     */
    List<InvListaProductosGeneralTO> findByExigirSerieTrue();

    /**
     * Busca productos que exigen lote.
     *
     * @return Lista de productos que exigen lote
     */
    List<InvListaProductosGeneralTO> findByExigirLoteTrue();

    /**
     * Obtiene todos los productos.
     *
     * @return Lista con todos los productos
     */
    List<InvListaProductosGeneralTO> findAll();

    /**
     * Guarda un producto.
     *
     * @param producto Producto a guardar
     * @return Producto guardado
     */
    InvListaProductosGeneralTO save(InvListaProductosGeneralTO producto);

    /**
     * Elimina un producto por su identificador.
     *
     * @param id Identificador del producto a eliminar
     */
    void deleteById(Integer id);

    /**
     * Verifica si existe un producto con el identificador dado.
     *
     * @param id Identificador del producto
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Integer id);

    /**
     * Verifica si existe un producto con el código principal dado.
     *
     * @param codigoPrincipal Código principal del producto
     * @return true si existe, false en caso contrario
     */
    boolean existsByProCodigoPrincipal(String codigoPrincipal);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * Obtiene lista de productos con saldos y precios usando función de PostgreSQL.
     * Esta consulta llama a la función inventario.fun_lista_productos_saldos_precios_web.
     *
     * @param empresa Código de la empresa
     * @param bodega Código de bodega
     * @param buscarPorCodigo Código específico a buscar (puede ser null)
     * @param categoria Categoría del producto
     * @param busqueda Texto de búsqueda
     * @param fecha Fecha para cálculo de precios (puede ser null)
     * @param precio Tipo de precio (1, 2, 3)
     * @param incluirInactivos Si se incluyen productos inactivos
     * @return Lista de productos con saldos y precios
     */
    List<InvListaProductosGeneralTO> findProductosSaldosPreciosWeb(
            String empresa,
            String bodega,
            String buscarPorCodigo,
            String categoria,
            String busqueda,
            String fecha,
            Integer precio,
            boolean incluirInactivos
    );

    /**
     * Busca productos por nombre ordenados alfabéticamente.
     *
     * @param nombre Nombre o parte del nombre del producto
     * @return Lista de productos ordenados por nombre
     */
    List<InvListaProductosGeneralTO> findByNombreContainingOrderByNombreAsc(String nombre);

    /**
     * Busca productos activos por categoría ordenados por nombre.
     *
     * @param categoria Categoría del producto
     * @return Lista de productos activos ordenados por nombre
     */
    List<InvListaProductosGeneralTO> findByActivoTrueAndCategoriaOrderByNombreAsc(String categoria);

    /**
     * Busca productos por tipo ordenados por código principal.
     *
     * @param tipo Tipo de producto
     * @return Lista de productos ordenados por código
     */
    List<InvListaProductosGeneralTO> findByTipoOrderByCodigoPrincipalAsc(String tipo);

    /**
     * Busca productos por categoría y subcategoría ordenados.
     *
     * @param categoria Categoría del producto
     * @param subcategoria Subcategoría del producto
     * @return Lista de productos ordenados por nombre
     */
    List<InvListaProductosGeneralTO> findByCategoriaAndSubcategoriaOrderByNombreAsc(
            String categoria,
            String subcategoria
    );

    /**
     * Busca productos habilitados para e-commerce ordenados por nombre.
     *
     * @return Lista de productos de e-commerce ordenados
     */
    List<InvListaProductosGeneralTO> findByEcommerceTrueOrderByNombreAsc();

    /**
     * Busca productos activos que gravan IVA ordenados.
     *
     * @param gravaIva Indicador de si grava IVA
     * @return Lista de productos ordenados por nombre
     */
    List<InvListaProductosGeneralTO> findByActivoTrueAndGravaIvaOrderByNombreAsc(String gravaIva);

    /**
     * Cuenta el número de productos por categoría.
     *
     * @param categoria Categoría del producto
     * @return Número de productos
     */
    long countByCategoria(String categoria);

    /**
     * Cuenta el número de productos activos.
     *
     * @return Número de productos activos
     */
    long countByActivoTrue();

    /**
     * Cuenta el número de productos por tipo.
     *
     * @param tipo Tipo de producto
     * @return Número de productos
     */
    long countByTipo(String tipo);

    /**
     * Busca productos por nombre que contenga el texto, limitado a cantidad específica.
     *
     * @param nombre Texto a buscar en el nombre
     * @param limit Número máximo de resultados
     * @return Lista limitada de productos
     */
    List<InvListaProductosGeneralTO> findTopByNombreContainingOrderByNombreAsc(String nombre, int limit);

    // ========== Métodos de Paginación y Ordenamiento ==========
    // Nota: Los métodos de paginación se implementarán en la capa de infraestructura
    // usando Page<T> y Pageable de Spring Data JPA
}

package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.InvListaProductosGeneralTO;
import com.acosux.MSBioSuper.domain.repository.ProductoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA para acceso a datos de productos.
 * Implementa la interfaz ProductoRepository (puerto del dominio) y extiende JpaRepository.
 * Esta clase pertenece a la capa de infraestructura y proporciona la implementación
 * concreta de persistencia usando Spring Data JPA.
 */
@Repository
public interface ProductoJpaRepository extends JpaRepository<InvListaProductosGeneralTO, Integer>, ProductoRepository {

    // ========== Métodos heredados de JpaRepository ==========
    // findById, findAll, save, deleteById, existsById ya están implementados por JpaRepository

    // ========== Métodos de consulta derivados (Query Methods) ==========
    // Spring Data JPA genera automáticamente la implementación basándose en el nombre del método

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<InvListaProductosGeneralTO> findByProCodigoPrincipal(String codigoPrincipal);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proNombre LIKE %:nombre%")
    List<InvListaProductosGeneralTO> findByNombreContaining(@Param("nombre") String nombre);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proCategoria = :categoria")
    List<InvListaProductosGeneralTO> findByCategoria(@Param("categoria") String categoria);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.lpspSubcategoria = :subcategoria")
    List<InvListaProductosGeneralTO> findBySubcategoria(@Param("subcategoria") String subcategoria);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.tipTipo = :tipo")
    List<InvListaProductosGeneralTO> findByTipo(@Param("tipo") String tipo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proInactivo = false")
    List<InvListaProductosGeneralTO> findByActivoTrue();

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proInactivo = true")
    List<InvListaProductosGeneralTO> findByActivoFalse();

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proGravaIva = :gravaIva")
    List<InvListaProductosGeneralTO> findByGravaIva(@Param("gravaIva") String gravaIva);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proCategoria = :categoria AND p.proInactivo = :inactivo")
    List<InvListaProductosGeneralTO> findByCategoriaAndActivo(@Param("categoria") String categoria, @Param("inactivo") boolean activo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proEcommerce = true")
    List<InvListaProductosGeneralTO> findByEcommerceTrue();

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proExigirSerie = true")
    List<InvListaProductosGeneralTO> findByExigirSerieTrue();

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.lpspExigirLote = true")
    List<InvListaProductosGeneralTO> findByExigirLoteTrue();

    /**
     * {@inheritDoc}
     */
    @Override
    boolean existsByProCodigoPrincipal(String codigoPrincipal);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proNombre LIKE %:nombre% ORDER BY p.proNombre ASC")
    List<InvListaProductosGeneralTO> findByNombreContainingOrderByNombreAsc(@Param("nombre") String nombre);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proInactivo = false AND p.proCategoria = :categoria ORDER BY p.proNombre ASC")
    List<InvListaProductosGeneralTO> findByActivoTrueAndCategoriaOrderByNombreAsc(@Param("categoria") String categoria);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.tipTipo = :tipo ORDER BY p.proCodigoPrincipal ASC")
    List<InvListaProductosGeneralTO> findByTipoOrderByCodigoPrincipalAsc(@Param("tipo") String tipo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proCategoria = :categoria AND p.lpspSubcategoria = :subcategoria ORDER BY p.proNombre ASC")
    List<InvListaProductosGeneralTO> findByCategoriaAndSubcategoriaOrderByNombreAsc(
            @Param("categoria") String categoria,
            @Param("subcategoria") String subcategoria
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proEcommerce = true ORDER BY p.proNombre ASC")
    List<InvListaProductosGeneralTO> findByEcommerceTrueOrderByNombreAsc();

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proInactivo = false AND p.proGravaIva = :gravaIva ORDER BY p.proNombre ASC")
    List<InvListaProductosGeneralTO> findByActivoTrueAndGravaIvaOrderByNombreAsc(@Param("gravaIva") String gravaIva);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(p) FROM InvListaProductosGeneralTO p WHERE p.proCategoria = :categoria")
    long countByCategoria(@Param("categoria") String categoria);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(p) FROM InvListaProductosGeneralTO p WHERE p.proInactivo = false")
    long countByActivoTrue();

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(p) FROM InvListaProductosGeneralTO p WHERE p.tipTipo = :tipo")
    long countByTipo(@Param("tipo") String tipo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT p FROM InvListaProductosGeneralTO p WHERE p.proNombre LIKE %:nombre% ORDER BY p.proNombre ASC LIMIT :limit")
    List<InvListaProductosGeneralTO> findTopByNombreContainingOrderByNombreAsc(@Param("nombre") String nombre, @Param("limit") int limit);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función inventario.fun_lista_productos_saldos_precios_web debe existir en la base de datos.
     */
    @Override
    @Query(value = "SELECT * FROM inventario.fun_lista_productos_saldos_precios_web(" +
            ":empresa, :bodega, :buscarPorCodigo, :categoria, :busqueda, :fecha, :precio, :incluirInactivos)",
            nativeQuery = true)
    List<InvListaProductosGeneralTO> findProductosSaldosPreciosWeb(
            @Param("empresa") String empresa,
            @Param("bodega") String bodega,
            @Param("buscarPorCodigo") String buscarPorCodigo,
            @Param("categoria") String categoria,
            @Param("busqueda") String busqueda,
            @Param("fecha") String fecha,
            @Param("precio") Integer precio,
            @Param("incluirInactivos") boolean incluirInactivos
    );
}

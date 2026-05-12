package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.PrdListaCostosDetalleCorridaTO;
import com.acosux.MSBioSuper.domain.repository.CostosCorridaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Adaptador JPA para acceso a datos de costos de corrida.
 * Implementa la interfaz CostosCorridaRepository (puerto del dominio) y extiende JpaRepository.
 * Esta clase pertenece a la capa de infraestructura y proporciona la implementación
 * concreta de persistencia usando Spring Data JPA.
 */
@Repository
public interface CostosCorridaJpaRepository extends JpaRepository<PrdListaCostosDetalleCorridaTO, Integer>, CostosCorridaRepository {

    // ========== Métodos heredados de JpaRepository ==========
    // findById, findAll, save, deleteById, existsById ya están implementados por JpaRepository

    // ========== Métodos de consulta derivados (Query Methods) ==========
    // Spring Data JPA genera automáticamente la implementación basándose en el nombre del método

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdListaCostosDetalleCorridaTO c WHERE c.costoCodigo = :empresaCodigo")
    List<PrdListaCostosDetalleCorridaTO> findByEmpresa(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdListaCostosDetalleCorridaTO c WHERE c.costoCodigo = :empresaCodigo AND c.costoProducto LIKE %:sectorCodigo%")
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndSector(@Param("empresaCodigo") String empresaCodigo, @Param("sectorCodigo") String sectorCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdListaCostosDetalleCorridaTO c WHERE c.costoCodigo = :empresaCodigo AND c.costoProducto LIKE %:piscinaNumero%")
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndPiscina(@Param("empresaCodigo") String empresaCodigo, @Param("piscinaNumero") String piscinaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdListaCostosDetalleCorridaTO c WHERE c.costoCodigo = :empresaCodigo AND c.costoProducto LIKE %:corridaNumero%")
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndCorrida(@Param("empresaCodigo") String empresaCodigo, @Param("corridaNumero") String corridaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdListaCostosDetalleCorridaTO c WHERE c.costoCodigo = :empresaCodigo AND c.costoProducto = :productoCodigo")
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndProducto(@Param("empresaCodigo") String empresaCodigo, @Param("productoCodigo") String productoCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdListaCostosDetalleCorridaTO c WHERE c.costoCodigo = :empresaCodigo AND c.costoProducto LIKE %:sectorCodigo% AND c.costoProducto LIKE %:piscinaNumero%")
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndSectorAndPiscina(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("sectorCodigo") String sectorCodigo,
            @Param("piscinaNumero") String piscinaNumero
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdListaCostosDetalleCorridaTO c WHERE c.costoCodigo = :empresaCodigo AND c.costoProducto LIKE %:piscinaNumero% AND c.costoProducto LIKE %:corridaNumero%")
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndPiscinaAndCorrida(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("piscinaNumero") String piscinaNumero,
            @Param("corridaNumero") String corridaNumero
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdListaCostosDetalleCorridaTO c WHERE c.costoCodigo = :empresaCodigo ORDER BY c.costoTotal DESC")
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaOrderByTotalDesc(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(c) FROM PrdListaCostosDetalleCorridaTO c WHERE c.costoCodigo = :empresaCodigo")
    long countByEmpresa(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(c) FROM PrdListaCostosDetalleCorridaTO c WHERE c.costoCodigo = :empresaCodigo AND c.costoProducto LIKE %:corridaNumero%")
    long countByEmpresaAndCorrida(@Param("empresaCodigo") String empresaCodigo, @Param("corridaNumero") String corridaNumero);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función produccion.fun_costos_detalle_corrida debe existir en la base de datos.
     * 
     * Nota: Los parámetros desde y hasta pueden ser null. En ese caso, se pasan como NULL a la función.
     * El parámetro agrupadoPor define el criterio de agrupación (PRODUCTO, CATEGORIA, etc.)
     */
    @Override
    @Query(value = "SELECT * FROM produccion.fun_costos_detalle_corrida(" +
            ":empresa, :sector, :piscina, :corrida, :desde, :hasta, :agrupadoPor)",
            nativeQuery = true)
    List<PrdListaCostosDetalleCorridaTO> findCostosDetalleCorrida(
            @Param("empresa") String empresa,
            @Param("sector") String sector,
            @Param("piscina") String piscina,
            @Param("corrida") String corrida,
            @Param("desde") String desde,
            @Param("hasta") String hasta,
            @Param("agrupadoPor") String agrupadoPor
    );
}

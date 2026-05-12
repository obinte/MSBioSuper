package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.PrdConsumosDiarioMSTO;
import com.acosux.MSBioSuper.domain.repository.ConsumosDiariosRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Adaptador JPA para acceso a datos de consumos diarios.
 * Implementa la interfaz ConsumosDiariosRepository (puerto del dominio) y extiende JpaRepository.
 * Esta clase pertenece a la capa de infraestructura y proporciona la implementación
 * concreta de persistencia usando Spring Data JPA.
 */
@Repository
public interface ConsumosDiariosJpaRepository extends JpaRepository<PrdConsumosDiarioMSTO, Integer>, ConsumosDiariosRepository {

    // ========== Métodos heredados de JpaRepository ==========
    // findById, findAll, save, deleteById, existsById ya están implementados por JpaRepository

    // ========== Métodos de consulta derivados (Query Methods) ==========
    // Spring Data JPA genera automáticamente la implementación basándose en el nombre del método

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo")
    List<PrdConsumosDiarioMSTO> findByEmpresa(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo AND c.consumoSector = :sectorCodigo")
    List<PrdConsumosDiarioMSTO> findByEmpresaAndSector(@Param("empresaCodigo") String empresaCodigo, @Param("sectorCodigo") String sectorCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo AND c.consumoPiscina = :piscinaNumero")
    List<PrdConsumosDiarioMSTO> findByEmpresaAndPiscina(@Param("empresaCodigo") String empresaCodigo, @Param("piscinaNumero") String piscinaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo AND c.consumoFecha BETWEEN :fechaDesde AND :fechaHasta")
    List<PrdConsumosDiarioMSTO> findByEmpresaAndFechaBetween(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo AND c.consumoCorrida = :corridaNumero")
    List<PrdConsumosDiarioMSTO> findByEmpresaAndCorrida(@Param("empresaCodigo") String empresaCodigo, @Param("corridaNumero") String corridaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo AND c.consumoProducto = :productoCodigo")
    List<PrdConsumosDiarioMSTO> findByEmpresaAndProducto(@Param("empresaCodigo") String empresaCodigo, @Param("productoCodigo") String productoCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo AND c.consumoPiscina = :piscinaNumero AND c.consumoFecha = :fecha")
    List<PrdConsumosDiarioMSTO> findByEmpresaAndPiscinaAndFecha(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("piscinaNumero") String piscinaNumero,
            @Param("fecha") String fecha
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo ORDER BY c.consumoFecha DESC")
    List<PrdConsumosDiarioMSTO> findByEmpresaOrderByFechaDesc(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo AND c.consumoPiscina = :piscinaNumero ORDER BY c.consumoFecha ASC")
    List<PrdConsumosDiarioMSTO> findByEmpresaAndPiscinaOrderByFechaAsc(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("piscinaNumero") String piscinaNumero
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo AND c.consumoPiscina = :piscinaNumero AND c.consumoFecha BETWEEN :fechaDesde AND :fechaHasta ORDER BY c.consumoFecha ASC")
    List<PrdConsumosDiarioMSTO> findByEmpresaAndPiscinaAndFechaBetweenOrderByFechaAsc(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("piscinaNumero") String piscinaNumero,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(c) FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo")
    long countByEmpresa(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(c) FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo AND c.consumoPiscina = :piscinaNumero")
    long countByEmpresaAndPiscina(@Param("empresaCodigo") String empresaCodigo, @Param("piscinaNumero") String piscinaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM PrdConsumosDiarioMSTO c WHERE c.consumoEmpresa = :empresaCodigo AND c.consumoProducto = :productoCodigo ORDER BY c.consumoFecha DESC")
    List<PrdConsumosDiarioMSTO> findByEmpresaAndProductoOrderByFechaDesc(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("productoCodigo") String productoCodigo
    );

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función produccion.fun_consumos_diarios_microservice debe existir en la base de datos.
     * 
     * NOTA: Se usan casts explícitos para asegurar que los tipos de datos coincidan
     * con los esperados por la función de PostgreSQL.
     * Orden de parámetros en PostgreSQL: text,text,text,text,text,date,date
     * (empresa, sector, piscina, bodega, producto, desde, hasta)
     */
    @Override
    @Query(value = "SELECT * FROM produccion.fun_consumos_diarios_microservice(" +
            "CAST(:empresa AS text), " +
            "CAST(:sector AS text), " +
            "CAST(:piscina AS text), " +
            "CAST(:bodega AS text), " +
            "CAST(:producto AS text), " +
            "CAST(:desde AS date), " +
            "CAST(:hasta AS date))",
            nativeQuery = true)
    List<PrdConsumosDiarioMSTO> findConsumosDiariosMicroservice(
           @Param("empresa") String empresa,
    @Param("sector") String sector,
    @Param("desde") String desde,
    @Param("hasta") String hasta,
    @Param("piscina") String piscina,
    @Param("bodega") String bodega,
    @Param("producto") String producto
);
}

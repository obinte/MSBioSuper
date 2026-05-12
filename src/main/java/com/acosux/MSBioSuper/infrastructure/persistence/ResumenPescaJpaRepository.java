package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import com.acosux.MSBioSuper.domain.repository.ResumenPescaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Adaptador JPA para acceso a datos de resumen de pesca.
 * Implementa la interfaz ResumenPescaRepository (puerto del dominio) y extiende JpaRepository.
 * Esta clase pertenece a la capa de infraestructura y proporciona la implementación
 * concreta de persistencia usando Spring Data JPA.
 */
@Repository
public interface ResumenPescaJpaRepository extends JpaRepository<PrdResumenCorridaTO, Integer>, ResumenPescaRepository {

    // ========== Métodos heredados de JpaRepository ==========
    // findById, findAll, save, deleteById, existsById ya están implementados por JpaRepository

    // ========== Métodos de consulta derivados (Query Methods) ==========
    // Spring Data JPA genera automáticamente la implementación basándose en el nombre del método

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT r FROM PrdResumenCorridaTO r WHERE r.empCodigo = :empresaCodigo")
    List<PrdResumenCorridaTO> findByEmpresa(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT r FROM PrdResumenCorridaTO r WHERE r.empCodigo = :empresaCodigo AND r.secCodigo = :sectorCodigo")
    List<PrdResumenCorridaTO> findByEmpresaAndSector(@Param("empresaCodigo") String empresaCodigo, @Param("sectorCodigo") String sectorCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT r FROM PrdResumenCorridaTO r WHERE r.empCodigo = :empresaCodigo AND r.rcFechaPesca BETWEEN :fechaDesde AND :fechaHasta")
    List<PrdResumenCorridaTO> findByEmpresaAndFechaPescaBetween(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT r FROM PrdResumenCorridaTO r WHERE r.empCodigo = :empresaCodigo AND r.rcCorridaNumero = :corridaNumero")
    List<PrdResumenCorridaTO> findByEmpresaAndCorrida(@Param("empresaCodigo") String empresaCodigo, @Param("corridaNumero") String corridaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT r FROM PrdResumenCorridaTO r WHERE r.empCodigo = :empresaCodigo AND r.pisNumero = :piscinaNumero")
    List<PrdResumenCorridaTO> findByEmpresaAndPiscina(@Param("empresaCodigo") String empresaCodigo, @Param("piscinaNumero") String piscinaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT r FROM PrdResumenCorridaTO r WHERE r.empCodigo = :empresaCodigo ORDER BY r.rcFechaPesca DESC")
    List<PrdResumenCorridaTO> findByEmpresaOrderByFechaPescaDesc(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT r FROM PrdResumenCorridaTO r WHERE r.empCodigo = :empresaCodigo ORDER BY r.rcCorridaNumero")
    List<PrdResumenCorridaTO> findByEmpresaOrderByCorrida(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT r FROM PrdResumenCorridaTO r WHERE r.empCodigo = :empresaCodigo AND r.secCodigo = :sectorCodigo AND r.rcFechaPesca BETWEEN :fechaDesde AND :fechaHasta ORDER BY r.rcFechaPesca DESC")
    List<PrdResumenCorridaTO> findByEmpresaAndSectorAndFechaPescaBetweenOrderByFechaPescaDesc(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("sectorCodigo") String sectorCodigo,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(r) FROM PrdResumenCorridaTO r WHERE r.empCodigo = :empresaCodigo")
    long countByEmpresa(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(r) FROM PrdResumenCorridaTO r WHERE r.empCodigo = :empresaCodigo AND r.secCodigo = :sectorCodigo")
    long countByEmpresaAndSector(@Param("empresaCodigo") String empresaCodigo, @Param("sectorCodigo") String sectorCodigo);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función produccion.fun_resumen_corrida_mejorado debe existir en la base de datos.
     */
    @Override
    @Query(value = "SELECT * FROM produccion.fun_resumen_corrida_mejorado(" +
            ":empresa, :sector, :tipoResumen, :fechaDesde, :fechaHasta, :ordenarPorCorrida)",
            nativeQuery = true)
    List<PrdResumenCorridaTO> findResumenCorridaMejorado(
            @Param("empresa") String empresa,
            @Param("sector") String sector,
            @Param("tipoResumen") String tipoResumen,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta,
            @Param("ordenarPorCorrida") boolean ordenarPorCorrida
    );
}

package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.PrdListadoGrameajeTO;
import com.acosux.MSBioSuper.domain.repository.GrameajeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Adaptador JPA para acceso a datos de grameajes.
 * Implementa la interfaz GrameajeRepository (puerto del dominio) y extiende JpaRepository.
 * Esta clase pertenece a la capa de infraestructura y proporciona la implementación
 * concreta de persistencia usando Spring Data JPA.
 */
@Repository
public interface GrameajeJpaRepository extends JpaRepository<PrdListadoGrameajeTO, Integer>, GrameajeRepository {

    // ========== Métodos heredados de JpaRepository ==========
    // findById, findAll, save, deleteById, existsById ya están implementados por JpaRepository

    // ========== Métodos de consulta derivados (Query Methods) ==========
    // Spring Data JPA genera automáticamente la implementación basándose en el nombre del método

    /**
     * {@inheritDoc}
     */
    @Override
    List<PrdListadoGrameajeTO> findByGraEmpresa(String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraSector(String empresaCodigo, String sectorCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraPiscina(String empresaCodigo, String piscinaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraCorrida(String empresaCodigo, String corridaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraFechaBetween(
            String empresaCodigo,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraSectorAndGraPiscina(
            String empresaCodigo,
            String sectorCodigo,
            String piscinaNumero
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraPiscinaOrderByGraFechaAsc(
            String empresaCodigo,
            String piscinaNumero
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<PrdListadoGrameajeTO> findByGraEmpresaOrderByGraFechaDesc(String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraPiscinaAndGraFechaBetweenOrderByGraFechaAsc(
            String empresaCodigo,
            String piscinaNumero,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    long countByGraEmpresa(String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    long countByGraEmpresaAndGraPiscina(String empresaCodigo, String piscinaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    long countByGraEmpresaAndGraCorrida(String empresaCodigo, String corridaNumero);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función produccion.fun_listado_grameaje debe existir en la base de datos.
     * 
     * Nota: Los parámetros piscina, desde y hasta pueden ser null. 
     * En ese caso, se pasan como NULL a la función para obtener todos los registros.
     */
    @Override
    @Query(value = "SELECT * FROM produccion.fun_listado_grameaje(" +
            ":empresa, :sector, :piscina, :desde, :hasta)",
            nativeQuery = true)
    List<PrdListadoGrameajeTO> findListadoGrameaje(
            @Param("empresa") String empresa,
            @Param("sector") String sector,
            @Param("piscina") String piscina,
            @Param("desde") String desde,
            @Param("hasta") String hasta
    );
}

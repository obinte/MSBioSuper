package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.PrdLiquidacionesDetalleTO;
import com.acosux.MSBioSuper.domain.repository.LiquidacionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Adaptador JPA para acceso a datos de liquidaciones.
 * Implementa la interfaz LiquidacionRepository (puerto del dominio) y extiende JpaRepository.
 * Esta clase pertenece a la capa de infraestructura y proporciona la implementación
 * concreta de persistencia usando Spring Data JPA.
 */
@Repository
public interface LiquidacionJpaRepository extends JpaRepository<PrdLiquidacionesDetalleTO, Integer>, LiquidacionRepository {

    // ========== Métodos heredados de JpaRepository ==========
    // findById, findAll, save, deleteById, existsById ya están implementados por JpaRepository

    // ========== Métodos de consulta derivados (Query Methods) ==========
    // Spring Data JPA genera automáticamente la implementación basándose en el nombre del método

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqNumero = :numeroLiquidacion")
    List<PrdLiquidacionesDetalleTO> findByNumero(@Param("numeroLiquidacion") String numeroLiquidacion);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqIdCliente = :clienteId")
    List<PrdLiquidacionesDetalleTO> findByCliente(@Param("clienteId") String clienteId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqCorrida = :corridaNumero")
    List<PrdLiquidacionesDetalleTO> findByCorrida(@Param("corridaNumero") String corridaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqFecha BETWEEN :fechaDesde AND :fechaHasta")
    List<PrdLiquidacionesDetalleTO> findByFechaBetween(@Param("fechaDesde") String fechaDesde, @Param("fechaHasta") String fechaHasta);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqTipo = :tipo")
    List<PrdLiquidacionesDetalleTO> findByTipo(@Param("tipo") String tipo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqMes = :mes")
    List<PrdLiquidacionesDetalleTO> findByMes(@Param("mes") String mes);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqIdCliente = :clienteId AND l.liqFecha BETWEEN :fechaDesde AND :fechaHasta")
    List<PrdLiquidacionesDetalleTO> findByClienteAndFechaBetween(
            @Param("clienteId") String clienteId,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqLote = :lote")
    List<PrdLiquidacionesDetalleTO> findByLote(@Param("lote") String lote);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqFecha BETWEEN :fechaDesde AND :fechaHasta ORDER BY l.liqFecha DESC")
    List<PrdLiquidacionesDetalleTO> findByFechaBetweenOrderByFechaDesc(
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqIdCliente = :clienteId ORDER BY l.liqFecha DESC")
    List<PrdLiquidacionesDetalleTO> findByClienteOrderByFechaDesc(@Param("clienteId") String clienteId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqCorrida = :corridaNumero ORDER BY l.liqFecha DESC")
    List<PrdLiquidacionesDetalleTO> findByCorridaOrderByFechaDesc(@Param("corridaNumero") String corridaNumero);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqTipo = :tipo AND l.liqFecha BETWEEN :fechaDesde AND :fechaHasta ORDER BY l.liqFecha ASC")
    List<PrdLiquidacionesDetalleTO> findByTipoAndFechaBetweenOrderByFechaAsc(
            @Param("tipo") String tipo,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(l) FROM PrdLiquidacionesDetalleTO l WHERE l.liqIdCliente = :clienteId")
    long countByCliente(@Param("clienteId") String clienteId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(l) FROM PrdLiquidacionesDetalleTO l WHERE l.liqTipo = :tipo")
    long countByTipo(@Param("tipo") String tipo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT l FROM PrdLiquidacionesDetalleTO l WHERE l.liqMes = :mes ORDER BY l.liqFecha ASC")
    List<PrdLiquidacionesDetalleTO> findByMesOrderByFechaAsc(@Param("mes") String mes);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función produccion.fun_liquidacion_detalle_producto debe existir en la base de datos.
     */
    @Override
    @Query(value = "SELECT * FROM produccion.fun_liquidacion_detalle_producto(" +
            ":empresa, :sector, :piscina, :desde, :hasta, :cliente, :talla, :tipo)",
            nativeQuery = true)
    List<PrdLiquidacionesDetalleTO> findLiquidacionDetalleProducto(
            @Param("empresa") String empresa,
            @Param("sector") String sector,
            @Param("piscina") String piscina,
            @Param("desde") String desde,
            @Param("hasta") String hasta,
            @Param("cliente") String cliente,
            @Param("talla") String talla,
            @Param("tipo") String tipo
    );

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función produccion.fun_liquidaciones_detalle debe existir en la base de datos.
     */
    @Override
    @Query(value = "SELECT * FROM produccion.fun_liquidaciones_detalle(" +
            ":empresa, :desde, :hasta)",
            nativeQuery = true)
    List<PrdLiquidacionesDetalleTO> findLiquidacionesDetalle(
            @Param("empresa") String empresa,
            @Param("desde") String desde,
            @Param("hasta") String hasta
    );
}

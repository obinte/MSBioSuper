package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
import com.acosux.MSBioSuper.domain.repository.CuentasPorCobrarRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Adaptador JPA para acceso a datos de cuentas por cobrar.
 * Implementa la interfaz CuentasPorCobrarRepository (puerto del dominio) y extiende JpaRepository.
 * Esta clase pertenece a la capa de infraestructura y proporciona la implementación
 * concreta de persistencia usando Spring Data JPA.
 */
@Repository
public interface CuentasPorCobrarJpaRepository extends JpaRepository<CuentasPorCobrarDetalladoApiTO, Integer>, CuentasPorCobrarRepository {

    // ========== Métodos heredados de JpaRepository ==========
    // findById, findAll, save, deleteById, existsById ya están implementados por JpaRepository

    // ========== Métodos de consulta derivados (Query Methods) ==========
    // Spring Data JPA genera automáticamente la implementación basándose en el nombre del método

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresa(String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdPeriodo(String empresaCodigo, String periodo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdClienteId(String empresaCodigo, String clienteId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdMotivo(String empresaCodigo, String motivo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdDocumentoNumero(
            String empresaCodigo,
            String numeroDocumento
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdFechaEmisionBetween(
            String empresaCodigo,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdFechaVencimientoBetween(
            String empresaCodigo,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdSaldoGreaterThan(
            String empresaCodigo,
            BigDecimal saldoMinimo
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdClienteIdAndCxcdFechaEmisionBetween(
            String empresaCodigo,
            String clienteId,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM CuentasPorCobrarDetalladoApiTO c WHERE c.cxcdEmpresa = :empresaCodigo ORDER BY c.cxcdFechaEmision DESC")
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaOrderByFechaEmisionDesc(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM CuentasPorCobrarDetalladoApiTO c WHERE c.cxcdEmpresa = :empresaCodigo ORDER BY c.cxcdFechaVencimiento ASC")
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaOrderByFechaVencimientoAsc(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM CuentasPorCobrarDetalladoApiTO c WHERE c.cxcdEmpresa = :empresaCodigo AND c.cxcdSaldo > :saldoMinimo ORDER BY c.cxcdSaldo DESC")
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaAndSaldoGreaterThanOrderBySaldoDesc(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("saldoMinimo") BigDecimal saldoMinimo
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM CuentasPorCobrarDetalladoApiTO c WHERE c.cxcdEmpresa = :empresaCodigo AND c.cxcdClienteId = :clienteId ORDER BY c.cxcdFechaEmision DESC")
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaAndClienteOrderByFechaEmisionDesc(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("clienteId") String clienteId
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM CuentasPorCobrarDetalladoApiTO c WHERE c.cxcdEmpresa = :empresaCodigo AND c.cxcdFechaVencimiento < :fechaActual")
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaAndFechaVencimientoLessThan(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("fechaActual") String fechaActual
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT c FROM CuentasPorCobrarDetalladoApiTO c WHERE c.cxcdEmpresa = :empresaCodigo AND c.cxcdPeriodo = :periodo ORDER BY c.cxcdClienteId ASC")
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaAndPeriodoOrderByClienteAsc(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("periodo") String periodo
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(c) FROM CuentasPorCobrarDetalladoApiTO c WHERE c.cxcdEmpresa = :empresaCodigo")
    long countByEmpresa(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(c) FROM CuentasPorCobrarDetalladoApiTO c WHERE c.cxcdEmpresa = :empresaCodigo AND c.cxcdClienteId = :clienteId")
    long countByEmpresaAndCliente(@Param("empresaCodigo") String empresaCodigo, @Param("clienteId") String clienteId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(c) FROM CuentasPorCobrarDetalladoApiTO c WHERE c.cxcdEmpresa = :empresaCodigo AND c.cxcdSaldo > :saldoMinimo")
    long countByEmpresaAndSaldoGreaterThan(@Param("empresaCodigo") String empresaCodigo, @Param("saldoMinimo") BigDecimal saldoMinimo);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función cartera.fun_cuentas_por_cobrar_detallado_api debe existir en la base de datos.
     * 
     * IMPORTANTE: La función espera (text, date) por lo que se hace cast explícito de los parámetros.
     */
    @Override
    @Query(value = "SELECT * FROM cartera.fun_cuentas_por_cobrar_detallado_api(CAST(:empresa AS text), CAST(:hasta AS date))",
            nativeQuery = true)
    List<CuentasPorCobrarDetalladoApiTO> findCuentasPorCobrarDetalladoApi(
            @Param("empresa") String empresa,
            @Param("hasta") String hasta
    );
}

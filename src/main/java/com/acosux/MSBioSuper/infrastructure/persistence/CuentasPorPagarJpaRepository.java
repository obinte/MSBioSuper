package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;
import com.acosux.MSBioSuper.domain.repository.CuentasPorPagarRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Adaptador JPA para acceso a datos de cuentas por pagar.
 * Implementa la interfaz CuentasPorPagarRepository (puerto del dominio) y extiende JpaRepository.
 * Esta clase pertenece a la capa de infraestructura y proporciona la implementación
 * concreta de persistencia usando Spring Data JPA.
 */
@Repository
public interface CuentasPorPagarJpaRepository extends JpaRepository<CuentasPorPagarDetalladoTO, Integer>, CuentasPorPagarRepository {

    // ========== Métodos heredados de JpaRepository ==========
    // findById, findAll, save, deleteById, existsById ya están implementados por JpaRepository

    // ========== Métodos de consulta derivados (Query Methods) ==========
    // Spring Data JPA genera automáticamente la implementación basándose en el nombre del método

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdPeriodo(String periodo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdMotivo(String motivo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdProveedorId(String proveedorId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdProveedorCodigo(String proveedorCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdDocumento(String numeroDocumento);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdFechaEmisionBetween(String fechaDesde, String fechaHasta);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdFechaVencimientoBetween(String fechaDesde, String fechaHasta);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdSaldoGreaterThan(BigDecimal saldoMinimo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdSaldoVencidoGreaterThan(BigDecimal saldoVencidoMinimo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdZona(String zona);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdUsuarioApruebaPago(String usuarioAprueba);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdProveedorIdAndCxpdFechaEmisionBetween(
            String proveedorId,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdProvRelacionado(Boolean relacionado);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdFechaEmisionBetweenOrderByCxpdFechaEmisionDesc(
            String fechaDesde,
            String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdFechaVencimientoBetweenOrderByCxpdFechaVencimientoAsc(
            String fechaDesde,
            String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdSaldoGreaterThanOrderByCxpdSaldoDesc(BigDecimal saldoMinimo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdSaldoVencidoGreaterThanOrderByCxpdSaldoVencidoDesc(
            BigDecimal saldoVencidoMinimo
    );

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdProveedorIdOrderByCxpdFechaEmisionDesc(String proveedorId);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdPeriodoOrderByCxpdProveedorRazonSocialAsc(String periodo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdMotivoOrderByCxpdFechaEmisionDesc(String motivo);

    /**
     * {@inheritDoc}
     */
    @Override
    List<CuentasPorPagarDetalladoTO> findByCxpdFechaVencimientoLessThan(String fechaActual);

    /**
     * {@inheritDoc}
     */
    @Override
    long countByCxpdProveedorId(String proveedorId);

    /**
     * {@inheritDoc}
     */
    @Override
    long countByCxpdPeriodo(String periodo);

    /**
     * {@inheritDoc}
     */
    @Override
    long countByCxpdSaldoGreaterThan(BigDecimal saldoMinimo);

    /**
     * {@inheritDoc}
     */
    @Override
    long countByCxpdSaldoVencidoGreaterThan(BigDecimal saldoVencidoMinimo);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función cartera.fun_cuentas_por_pagar_detallado debe existir en la base de datos.
     * 
     * NOTA: Se usan casts explícitos para asegurar que los tipos de datos coincidan
     * con los esperados por la función de PostgreSQL.
     */
    @Override
    @Query(value = "SELECT * FROM cartera.fun_cuentas_por_pagar_detallado(" +
            "CAST(:empresa AS text), " +
            "CAST(:sector AS text), " +
            "CAST(:proveedor AS text), " +
            "CAST(:desde AS date), " +
            "CAST(:hasta AS date), " +
            "CAST(:excluirAprobadas AS boolean), " +
            "CAST(:incluirCheques AS boolean), " +
            "CAST(:formatoMensual AS text))",
            nativeQuery = true)
    List<CuentasPorPagarDetalladoTO> findCuentasPorPagarDetallado(
            @Param("empresa") String empresa,
            @Param("sector") String sector,
            @Param("proveedor") String proveedor,
            @Param("desde") String desde,
            @Param("hasta") String hasta,
            @Param("excluirAprobadas") boolean excluirAprobadas,
            @Param("incluirCheques") boolean incluirCheques,
            @Param("formatoMensual") String formatoMensual
    );
}

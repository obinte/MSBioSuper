package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.InvVentasBioSuperTO;
import com.acosux.MSBioSuper.domain.model.InvVentasPK;
import com.acosux.MSBioSuper.domain.repository.VentasRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Adaptador JPA para acceso a datos de ventas.
 * Implementa la interfaz VentasRepository (puerto del dominio) y extiende JpaRepository.
 * Esta clase pertenece a la capa de infraestructura y proporciona la implementación
 * concreta de persistencia usando Spring Data JPA.
 */
@Repository
public interface VentasJpaRepository extends JpaRepository<InvVentasBioSuperTO, InvVentasPK>, VentasRepository {

    // ========== Métodos heredados de JpaRepository ==========
    // findById, findAll, save, deleteById, existsById ya están implementados por JpaRepository

    // ========== Métodos de consulta derivados (Query Methods) ==========
    // Spring Data JPA genera automáticamente la implementación basándose en el nombre del método

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo")
    List<InvVentasBioSuperTO> findByEmpresa(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.invVentasPK.vtaPeriodo = :periodo")
    List<InvVentasBioSuperTO> findByEmpresaAndPeriodo(@Param("empresaCodigo") String empresaCodigo, @Param("periodo") String periodo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.invVentasPK.vtaMotivo = :motivo")
    List<InvVentasBioSuperTO> findByEmpresaAndMotivo(@Param("empresaCodigo") String empresaCodigo, @Param("motivo") String motivo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.vtaDocumentoNumero = :numeroDocumento")
    List<InvVentasBioSuperTO> findByEmpresaAndNumeroDocumento(@Param("empresaCodigo") String empresaCodigo, @Param("numeroDocumento") String numeroDocumento);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.cliCodigo = :clienteId")
    List<InvVentasBioSuperTO> findByEmpresaAndCliente(@Param("empresaCodigo") String empresaCodigo, @Param("clienteId") String clienteId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.vtaFecha BETWEEN :fechaDesde AND :fechaHasta")
    List<InvVentasBioSuperTO> findByEmpresaAndFechaBetween(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.vtaDocumentoTipo = :tipoDocumento")
    List<InvVentasBioSuperTO> findByEmpresaAndTipoDocumento(@Param("empresaCodigo") String empresaCodigo, @Param("tipoDocumento") String tipoDocumento);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.vtaRevisado = :autorizado")
    List<InvVentasBioSuperTO> findByEmpresaAndAutorizado(@Param("empresaCodigo") String empresaCodigo, @Param("autorizado") Boolean autorizado);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo ORDER BY v.vtaFecha DESC")
    List<InvVentasBioSuperTO> findByEmpresaOrderByFechaDesc(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.cliCodigo = :clienteId ORDER BY v.vtaFecha DESC")
    List<InvVentasBioSuperTO> findByEmpresaAndClienteOrderByFechaDesc(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("clienteId") String clienteId
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.invVentasPK.vtaPeriodo = :periodo ORDER BY v.invVentasPK.vtaNumero ASC")
    List<InvVentasBioSuperTO> findByEmpresaAndPeriodoOrderByNumeroAsc(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("periodo") String periodo
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.vtaTotal > :totalMinimo")
    List<InvVentasBioSuperTO> findByEmpresaAndTotalGreaterThan(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("totalMinimo") BigDecimal totalMinimo
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT v FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.vtaRevisado = true AND v.vtaFecha BETWEEN :fechaDesde AND :fechaHasta")
    List<InvVentasBioSuperTO> findByEmpresaAndAutorizadoTrueAndFechaBetween(
            @Param("empresaCodigo") String empresaCodigo,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta
    );

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(v) FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo")
    long countByEmpresa(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(v) FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.cliCodigo = :clienteId")
    long countByEmpresaAndCliente(@Param("empresaCodigo") String empresaCodigo, @Param("clienteId") String clienteId);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(v) FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.invVentasPK.vtaPeriodo = :periodo")
    long countByEmpresaAndPeriodo(@Param("empresaCodigo") String empresaCodigo, @Param("periodo") String periodo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT COUNT(v) FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.vtaRevisado = true")
    long countByEmpresaAndAutorizadoTrue(@Param("empresaCodigo") String empresaCodigo);

    /**
     * {@inheritDoc}
     */
    @Override
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM InvVentasBioSuperTO v WHERE v.invVentasPK.vtaEmpresa = :empresaCodigo AND v.vtaDocumentoNumero = :numeroDocumento")
    boolean existsByEmpresaAndNumeroDocumento(@Param("empresaCodigo") String empresaCodigo, @Param("numeroDocumento") String numeroDocumento);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función anexo.fun_ventas_electronicas_autorizadas_listado_biosuper debe existir en la base de datos.
     * 
     * Esta función retorna las ventas con sus detalles (InvVentasDetalleBioSuperTO) y 
     * liquidaciones (InvVentasLiquidacionBioSuperTO) asociadas.
     * 
     * Nota: El tipo de documento '18' corresponde a facturas electrónicas.
     * Este valor está hardcodeado en la implementación original del DAO.
     */
    @Override
    @Query(value = "SELECT * FROM anexo.fun_ventas_electronicas_autorizadas_listado_biosuper(" +
            ":empresa, :fechaDesde, :fechaHasta, :tipoDocumento)",
            nativeQuery = true)
    List<InvVentasBioSuperTO> findVentasElectronicasAutorizadas(
            @Param("empresa") String empresa,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta,
            @Param("tipoDocumento") String tipoDocumento
    );
}

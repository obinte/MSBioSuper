package com.acosux.MSBioSuper.infrastructure.persistence;

import com.acosux.MSBioSuper.domain.model.InvComprasDetalleProductoTO;
import com.acosux.MSBioSuper.domain.repository.ComprasRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Adaptador JPA para acceso a datos de compras.
 * Implementa la interfaz ComprasRepository (puerto del dominio) y extiende JpaRepository.
 * Esta clase pertenece a la capa de infraestructura y proporciona la implementación
 * concreta de persistencia usando Spring Data JPA.
 */
@Repository
public interface ComprasJpaRepository extends JpaRepository<InvComprasDetalleProductoTO, Integer>, ComprasRepository {

    // ========== Métodos heredados de JpaRepository ==========
    // findById, findAll, save, deleteById, existsById ya están implementados por JpaRepository

    // ========== Métodos de consulta derivados (Query Methods) ==========
    // Spring Data JPA genera automáticamente la implementación basándose en el nombre del método

    // NOTA: Métodos comentados temporalmente para verificación de seguridad y documentación
    // La entidad InvComprasDetalleProductoTO usa 'ecCodigo' en lugar de 'empresa'
    // TODO: Corregir estos métodos para usar ecCodigo o agregar alias en la entidad
    
    /*
    @Override
    List<InvComprasDetalleProductoTO> findByEmpresa(String empresaCodigo);

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaAndProveedor(String empresaCodigo, String proveedorId);

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaAndProducto(String empresaCodigo, String productoCodigo);

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaAndBodega(String empresaCodigo, String bodegaCodigo);

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaAndFechaBetween(
            String empresaCodigo,
            String fechaDesde,
            String fechaHasta
    );

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaAndSector(String empresaCodigo, String sectorCodigo);

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaAndPiscina(String empresaCodigo, String piscinaNumero);

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaOrderByFechaDesc(String empresaCodigo);

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaAndProveedorOrderByFechaDesc(
            String empresaCodigo,
            String proveedorId
    );

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaAndProductoOrderByFechaDesc(
            String empresaCodigo,
            String productoCodigo
    );

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaAndCantidadGreaterThan(
            String empresaCodigo,
            BigDecimal cantidadMinima
    );

    @Override
    List<InvComprasDetalleProductoTO> findByEmpresaAndTotalGreaterThan(
            String empresaCodigo,
            BigDecimal totalMinimo
    );

    @Override
    long countByEmpresa(String empresaCodigo);

    @Override
    long countByEmpresaAndProveedor(String empresaCodigo, String proveedorId);

    @Override
    long countByEmpresaAndProducto(String empresaCodigo, String productoCodigo);
    */

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * {@inheritDoc}
     * 
     * Implementación usando consulta nativa a función de PostgreSQL.
     * La función inventario.fun_compras_detalle_producto debe existir en la base de datos.
     * 
     * Nota: Los parámetros sector, piscina, proveedor, producto y bodega pueden ser null.
     * En ese caso, se pasan como NULL a la función para obtener todos los registros.
     * El parámetro incluirIMB indica si se deben incluir movimientos de bodega.
     */
    @Override
    @Query(value = "SELECT * FROM inventario.fun_compras_detalle_producto(" +
            ":empresa, :sector, :piscina, :proveedor, :producto, " +
            "CAST(:fechaDesde AS date), CAST(:fechaHasta AS date), " +
            ":bodega, :incluirIMB)",
            nativeQuery = true)
    List<InvComprasDetalleProductoTO> findComprasDetalleProducto(
            @Param("empresa") String empresa,
            @Param("sector") String sector,
            @Param("piscina") String piscina,
            @Param("proveedor") String proveedor,
            @Param("producto") String producto,
            @Param("fechaDesde") String fechaDesde,
            @Param("fechaHasta") String fechaHasta,
            @Param("bodega") String bodega,
            @Param("incluirIMB") Boolean incluirIMB
    );
}

package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.InvComprasDetalleProductoTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) del repositorio de compras en la capa de dominio.
 * Define el contrato para acceso a datos de compras detalladas por producto.
 * Esta interfaz es independiente de la tecnología de persistencia.
 * 
 * Siguiendo el patrón de arquitectura hexagonal, esta interfaz representa un puerto
 * que será implementado por un adaptador en la capa de infraestructura.
 */
public interface ComprasRepository {

    /**
     * Busca todas las compras.
     * 
     * @return Lista de todas las compras
     */
    List<InvComprasDetalleProductoTO> findAll();

    /**
     * Busca una compra por su ID.
     * 
     * @param id ID de la compra
     * @return Optional con la compra encontrada o empty
     */
    Optional<InvComprasDetalleProductoTO> findById(Integer id);

    // NOTA: Métodos comentados temporalmente para verificación de seguridad y documentación
    // La entidad InvComprasDetalleProductoTO usa 'ecCodigo' en lugar de 'empresa'
    // TODO: Corregir estos métodos para usar ecCodigo o agregar alias en la entidad
    
    /*
    List<InvComprasDetalleProductoTO> findByEmpresa(String empresaCodigo);

    List<InvComprasDetalleProductoTO> findByEmpresaAndProveedor(String empresaCodigo, String proveedorId);

    List<InvComprasDetalleProductoTO> findByEmpresaAndProducto(String empresaCodigo, String productoCodigo);

    List<InvComprasDetalleProductoTO> findByEmpresaAndBodega(String empresaCodigo, String bodegaCodigo);

    List<InvComprasDetalleProductoTO> findByEmpresaAndFechaBetween(
            String empresaCodigo,
            String fechaDesde,
            String fechaHasta
    );

    List<InvComprasDetalleProductoTO> findByEmpresaAndSector(String empresaCodigo, String sectorCodigo);

    List<InvComprasDetalleProductoTO> findByEmpresaAndPiscina(String empresaCodigo, String piscinaNumero);

    List<InvComprasDetalleProductoTO> findByEmpresaOrderByFechaDesc(String empresaCodigo);

    List<InvComprasDetalleProductoTO> findByEmpresaAndProveedorOrderByFechaDesc(
            String empresaCodigo,
            String proveedorId
    );

    List<InvComprasDetalleProductoTO> findByEmpresaAndProductoOrderByFechaDesc(
            String empresaCodigo,
            String productoCodigo
    );

    List<InvComprasDetalleProductoTO> findByEmpresaAndCantidadGreaterThan(
            String empresaCodigo,
            BigDecimal cantidadMinima
    );

    List<InvComprasDetalleProductoTO> findByEmpresaAndTotalGreaterThan(
            String empresaCodigo,
            BigDecimal totalMinimo
    );

    long countByEmpresa(String empresaCodigo);

    long countByEmpresaAndProveedor(String empresaCodigo, String proveedorId);

    long countByEmpresaAndProducto(String empresaCodigo, String productoCodigo);
    */

    /**
     * Obtiene el listado de compras detallado por producto usando función de base de datos.
     * Esta consulta utiliza una función almacenada en PostgreSQL para obtener
     * el detalle completo de compras con todos los cálculos necesarios.
     * 
     * @param empresa Código de la empresa
     * @param sector Código del sector (puede ser null)
     * @param piscina Número de piscina (puede ser null)
     * @param proveedor ID del proveedor (puede ser null)
     * @param producto Código del producto (puede ser null)
     * @param fechaDesde Fecha desde
     * @param fechaHasta Fecha hasta
     * @param bodega Código de la bodega (puede ser null)
     * @param incluirIMB Incluir movimientos de bodega (true/false)
     * @return Lista de compras detalladas por producto
     */
    List<InvComprasDetalleProductoTO> findComprasDetalleProducto(
            String empresa,
            String sector,
            String piscina,
            String proveedor,
            String producto,
            String fechaDesde,
            String fechaHasta,
            String bodega,
            Boolean incluirIMB
    );
}

package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.InvVentasBioSuperTO;
import com.acosux.MSBioSuper.domain.model.InvVentasPK;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) del repositorio de ventas en la capa de dominio.
 * Define el contrato para acceso a datos de ventas de BioSuper.
 * Esta interfaz es independiente de la tecnología de persistencia.
 * 
 * Siguiendo el patrón de arquitectura hexagonal, esta interfaz representa un puerto
 * que será implementado por un adaptador en la capa de infraestructura.
 */
public interface VentasRepository {

    /**
     * Busca todas las ventas.
     * 
     * @return Lista de todas las ventas
     */
    List<InvVentasBioSuperTO> findAll();

    /**
     * Busca una venta por su clave primaria compuesta.
     * 
     * @param id Clave primaria compuesta de la venta
     * @return Venta encontrada o empty
     */
    Optional<InvVentasBioSuperTO> findById(InvVentasPK id);

    /**
     * Busca ventas por empresa.
     * 
     * @param empresaCodigo Código de la empresa
     * @return Lista de ventas de la empresa
     */
    List<InvVentasBioSuperTO> findByEmpresa(String empresaCodigo);

    /**
     * Busca ventas por empresa y periodo.
     * 
     * @param empresaCodigo Código de la empresa
     * @param periodo Periodo de la venta
     * @return Lista de ventas del periodo
     */
    List<InvVentasBioSuperTO> findByEmpresaAndPeriodo(String empresaCodigo, String periodo);

    /**
     * Busca ventas por empresa y motivo.
     * 
     * @param empresaCodigo Código de la empresa
     * @param motivo Motivo de la venta
     * @return Lista de ventas del motivo
     */
    List<InvVentasBioSuperTO> findByEmpresaAndMotivo(String empresaCodigo, String motivo);

    /**
     * Busca ventas por empresa y número de documento.
     * 
     * @param empresaCodigo Código de la empresa
     * @param numeroDocumento Número de documento
     * @return Lista de ventas con el número de documento
     */
    List<InvVentasBioSuperTO> findByEmpresaAndNumeroDocumento(String empresaCodigo, String numeroDocumento);

    /**
     * Busca ventas por empresa y cliente.
     * 
     * @param empresaCodigo Código de la empresa
     * @param clienteId ID del cliente
     * @return Lista de ventas del cliente
     */
    List<InvVentasBioSuperTO> findByEmpresaAndCliente(String empresaCodigo, String clienteId);

    /**
     * Busca ventas por empresa y rango de fechas.
     * 
     * @param empresaCodigo Código de la empresa
     * @param fechaDesde Fecha desde
     * @param fechaHasta Fecha hasta
     * @return Lista de ventas en el rango de fechas
     */
    List<InvVentasBioSuperTO> findByEmpresaAndFechaBetween(
            String empresaCodigo,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Busca ventas por empresa y tipo de documento.
     * 
     * @param empresaCodigo Código de la empresa
     * @param tipoDocumento Tipo de documento
     * @return Lista de ventas del tipo de documento
     */
    List<InvVentasBioSuperTO> findByEmpresaAndTipoDocumento(String empresaCodigo, String tipoDocumento);

    /**
     * Busca ventas por empresa y estado de autorización.
     * 
     * @param empresaCodigo Código de la empresa
     * @param autorizado Estado de autorización (true/false)
     * @return Lista de ventas según estado de autorización
     */
    List<InvVentasBioSuperTO> findByEmpresaAndAutorizado(String empresaCodigo, Boolean autorizado);

    /**
     * Busca ventas por empresa, ordenadas por fecha descendente.
     * 
     * @param empresaCodigo Código de la empresa
     * @return Lista de ventas ordenadas por fecha
     */
    List<InvVentasBioSuperTO> findByEmpresaOrderByFechaDesc(String empresaCodigo);

    /**
     * Busca ventas por empresa y cliente, ordenadas por fecha descendente.
     * 
     * @param empresaCodigo Código de la empresa
     * @param clienteId ID del cliente
     * @return Lista de ventas ordenadas por fecha
     */
    List<InvVentasBioSuperTO> findByEmpresaAndClienteOrderByFechaDesc(
            String empresaCodigo,
            String clienteId
    );

    /**
     * Busca ventas por empresa y periodo, ordenadas por número de documento.
     * 
     * @param empresaCodigo Código de la empresa
     * @param periodo Periodo de la venta
     * @return Lista de ventas ordenadas por número
     */
    List<InvVentasBioSuperTO> findByEmpresaAndPeriodoOrderByNumeroAsc(
            String empresaCodigo,
            String periodo
    );

    /**
     * Busca ventas con total mayor a un valor mínimo.
     * 
     * @param empresaCodigo Código de la empresa
     * @param totalMinimo Total mínimo
     * @return Lista de ventas con total mayor al mínimo
     */
    List<InvVentasBioSuperTO> findByEmpresaAndTotalGreaterThan(
            String empresaCodigo,
            BigDecimal totalMinimo
    );

    /**
     * Busca ventas autorizadas por empresa y rango de fechas.
     * 
     * @param empresaCodigo Código de la empresa
     * @param fechaDesde Fecha desde
     * @param fechaHasta Fecha hasta
     * @return Lista de ventas autorizadas
     */
    List<InvVentasBioSuperTO> findByEmpresaAndAutorizadoTrueAndFechaBetween(
            String empresaCodigo,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Cuenta ventas por empresa.
     * 
     * @param empresaCodigo Código de la empresa
     * @return Cantidad de ventas
     */
    long countByEmpresa(String empresaCodigo);

    /**
     * Cuenta ventas por empresa y cliente.
     * 
     * @param empresaCodigo Código de la empresa
     * @param clienteId ID del cliente
     * @return Cantidad de ventas
     */
    long countByEmpresaAndCliente(String empresaCodigo, String clienteId);

    /**
     * Cuenta ventas por empresa y periodo.
     * 
     * @param empresaCodigo Código de la empresa
     * @param periodo Periodo de la venta
     * @return Cantidad de ventas
     */
    long countByEmpresaAndPeriodo(String empresaCodigo, String periodo);

    /**
     * Cuenta ventas autorizadas por empresa.
     * 
     * @param empresaCodigo Código de la empresa
     * @return Cantidad de ventas autorizadas
     */
    long countByEmpresaAndAutorizadoTrue(String empresaCodigo);

    /**
     * Verifica si existe una venta con el número de documento especificado.
     * 
     * @param empresaCodigo Código de la empresa
     * @param numeroDocumento Número de documento
     * @return true si existe, false en caso contrario
     */
    boolean existsByEmpresaAndNumeroDocumento(String empresaCodigo, String numeroDocumento);

    /**
     * Obtiene el listado de ventas electrónicas autorizadas usando función de base de datos.
     * Esta consulta utiliza una función almacenada en PostgreSQL para obtener
     * el listado completo de ventas con sus detalles y liquidaciones.
     * 
     * @param empresa Código de la empresa
     * @param fechaDesde Fecha desde
     * @param fechaHasta Fecha hasta
     * @param tipoDocumento Tipo de documento (código)
     * @return Lista de ventas electrónicas autorizadas con detalles y liquidaciones
     */
    List<InvVentasBioSuperTO> findVentasElectronicasAutorizadas(
            String empresa,
            String fechaDesde,
            String fechaHasta,
            String tipoDocumento
    );
}

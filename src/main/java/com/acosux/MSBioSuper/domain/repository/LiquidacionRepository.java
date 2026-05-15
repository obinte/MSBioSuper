package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.PrdLiquidacionesDetalleTO;
import com.acosux.MSBioSuper.domain.model.PrdLiquidacionDetalleProductoTO;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) para acceso a datos de liquidaciones.
 * Define el contrato para operaciones de consulta de liquidaciones de producción.
 * Esta interfaz pertenece a la capa de dominio y no debe tener dependencias de frameworks.
 */
public interface LiquidacionRepository {

    /**
     * Busca una liquidación por su identificador único.
     *
     * @param id Identificador de la liquidación
     * @return Optional con la liquidación si existe, vacío en caso contrario
     */
    Optional<PrdLiquidacionesDetalleTO> findById(Integer id);

    /**
     * Busca liquidaciones por número de liquidación.
     *
     * @param numeroLiquidacion Número de liquidación
     * @return Lista de liquidaciones con el número especificado
     */
    List<PrdLiquidacionesDetalleTO> findByNumero(String numeroLiquidacion);

    /**
     * Busca liquidaciones por cliente.
     *
     * @param clienteId Identificador del cliente
     * @return Lista de liquidaciones del cliente
     */
    List<PrdLiquidacionesDetalleTO> findByCliente(String clienteId);

    /**
     * Busca liquidaciones por corrida.
     *
     * @param corridaNumero Número de corrida
     * @return Lista de liquidaciones de la corrida
     */
    List<PrdLiquidacionesDetalleTO> findByCorrida(String corridaNumero);

    /**
     * Busca liquidaciones por rango de fechas.
     *
     * @param fechaDesde Fecha inicial (formato String)
     * @param fechaHasta Fecha final (formato String)
     * @return Lista de liquidaciones en el rango de fechas
     */
    List<PrdLiquidacionesDetalleTO> findByFechaBetween(String fechaDesde, String fechaHasta);

    /**
     * Busca liquidaciones por tipo.
     *
     * @param tipo Tipo de liquidación
     * @return Lista de liquidaciones del tipo especificado
     */
    List<PrdLiquidacionesDetalleTO> findByTipo(String tipo);

    /**
     * Busca liquidaciones por mes.
     *
     * @param mes Mes de la liquidación
     * @return Lista de liquidaciones del mes
     */
    List<PrdLiquidacionesDetalleTO> findByMes(String mes);

    /**
     * Busca liquidaciones por cliente y rango de fechas.
     *
     * @param clienteId Identificador del cliente
     * @param fechaDesde Fecha inicial
     * @param fechaHasta Fecha final
     * @return Lista de liquidaciones del cliente en el rango de fechas
     */
    List<PrdLiquidacionesDetalleTO> findByClienteAndFechaBetween(
            String clienteId, 
            String fechaDesde, 
            String fechaHasta
    );

    /**
     * Busca liquidaciones por lote.
     *
     * @param lote Número de lote
     * @return Lista de liquidaciones del lote
     */
    List<PrdLiquidacionesDetalleTO> findByLote(String lote);

    /**
     * Obtiene todas las liquidaciones.
     *
     * @return Lista con todas las liquidaciones
     */
    List<PrdLiquidacionesDetalleTO> findAll();

    /**
     * Guarda una liquidación.
     *
     * @param liquidacion Liquidación a guardar
     * @return Liquidación guardada
     */
    PrdLiquidacionesDetalleTO save(PrdLiquidacionesDetalleTO liquidacion);

    /**
     * Elimina una liquidación por su identificador.
     *
     * @param id Identificador de la liquidación a eliminar
     */
    void deleteById(Integer id);

    /**
     * Verifica si existe una liquidación con el identificador dado.
     *
     * @param id Identificador de la liquidación
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Integer id);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * Obtiene liquidaciones detalle de producto usando función de PostgreSQL.
     * Esta consulta llama a la función produccion.fun_liquidacion_detalle_producto.
     *
     * @param empresa Código de la empresa (puede ser null)
     * @param sector Código del sector (puede ser null)
     * @param piscina Número de piscina (puede ser null)
     * @param desde Fecha inicial (puede ser null)
     * @param hasta Fecha final (puede ser null)
     * @param cliente Identificador del cliente (puede ser null)
     * @param talla Talla del producto (puede ser null)
     * @param tipo Tipo de liquidación (puede ser null)
     * @return Lista de liquidaciones detalle de producto
     */
    List<PrdLiquidacionDetalleProductoTO> findLiquidacionDetalleProducto(
            String empresa,
            String sector,
            String piscina,
            String desde,
            String hasta,
            String cliente,
            String talla,
            String tipo
    );

    /**
     * Obtiene liquidaciones detalle usando función de PostgreSQL.
     * Esta consulta llama a la función produccion.fun_liquidaciones_detalle.
     *
     * @param empresa Código de la empresa (puede ser null)
     * @param desde Fecha inicial (puede ser null)
     * @param hasta Fecha final (puede ser null)
     * @return Lista de liquidaciones detalle
     */
    List<PrdLiquidacionesDetalleTO> findLiquidacionesDetalle(
            String empresa,
            String desde,
            String hasta
    );

    /**
     * Busca liquidaciones ordenadas por fecha descendente.
     *
     * @param fechaDesde Fecha inicial
     * @param fechaHasta Fecha final
     * @return Lista de liquidaciones ordenadas por fecha descendente
     */
    List<PrdLiquidacionesDetalleTO> findByFechaBetweenOrderByFechaDesc(
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Busca liquidaciones por cliente ordenadas por fecha.
     *
     * @param clienteId Identificador del cliente
     * @return Lista de liquidaciones ordenadas por fecha descendente
     */
    List<PrdLiquidacionesDetalleTO> findByClienteOrderByFechaDesc(String clienteId);

    /**
     * Busca liquidaciones por corrida ordenadas por fecha.
     *
     * @param corridaNumero Número de corrida
     * @return Lista de liquidaciones ordenadas por fecha descendente
     */
    List<PrdLiquidacionesDetalleTO> findByCorridaOrderByFechaDesc(String corridaNumero);

    /**
     * Busca liquidaciones por tipo y rango de fechas ordenadas.
     *
     * @param tipo Tipo de liquidación
     * @param fechaDesde Fecha inicial
     * @param fechaHasta Fecha final
     * @return Lista de liquidaciones ordenadas por fecha
     */
    List<PrdLiquidacionesDetalleTO> findByTipoAndFechaBetweenOrderByFechaAsc(
            String tipo,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Cuenta el número de liquidaciones por cliente.
     *
     * @param clienteId Identificador del cliente
     * @return Número de liquidaciones
     */
    long countByCliente(String clienteId);

    /**
     * Cuenta el número de liquidaciones por tipo.
     *
     * @param tipo Tipo de liquidación
     * @return Número de liquidaciones
     */
    long countByTipo(String tipo);

    /**
     * Busca liquidaciones por mes ordenadas por fecha.
     *
     * @param mes Mes de la liquidación
     * @return Lista de liquidaciones ordenadas por fecha
     */
    List<PrdLiquidacionesDetalleTO> findByMesOrderByFechaAsc(String mes);

    // ========== Métodos de Paginación y Ordenamiento ==========
    // Nota: Los métodos de paginación se implementarán en la capa de infraestructura
    // usando Page<T> y Pageable de Spring Data JPA
}

package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.CuentasPorPagarDetalladoTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) para acceso a datos de cuentas por pagar.
 * Define el contrato para operaciones de consulta de cuentas por pagar.
 * Esta interfaz pertenece a la capa de dominio y no debe tener dependencias de frameworks.
 */
public interface CuentasPorPagarRepository {

    /**
     * Busca una cuenta por pagar por su identificador único.
     *
     * @param id Identificador de la cuenta por pagar
     * @return Optional con la cuenta si existe, vacío en caso contrario
     */
    Optional<CuentasPorPagarDetalladoTO> findById(Integer id);

    /**
     * Busca cuentas por pagar por periodo.
     *
     * @param periodo Periodo contable
     * @return Lista de cuentas por pagar del periodo
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdPeriodo(String periodo);

    /**
     * Busca cuentas por pagar por motivo.
     *
     * @param motivo Motivo de la cuenta por pagar
     * @return Lista de cuentas por pagar del motivo
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdMotivo(String motivo);

    /**
     * Busca cuentas por pagar por proveedor.
     *
     * @param proveedorId Identificador del proveedor
     * @return Lista de cuentas por pagar del proveedor
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdProveedorId(String proveedorId);

    /**
     * Busca cuentas por pagar por código de proveedor.
     *
     * @param proveedorCodigo Código del proveedor
     * @return Lista de cuentas por pagar del proveedor
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdProveedorCodigo(String proveedorCodigo);

    /**
     * Busca cuentas por pagar por número de documento.
     *
     * @param numeroDocumento Número de documento
     * @return Lista de cuentas por pagar del documento
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdDocumento(String numeroDocumento);

    /**
     * Busca cuentas por pagar por rango de fechas de emisión.
     *
     * @param fechaDesde Fecha inicial (formato String)
     * @param fechaHasta Fecha final (formato String)
     * @return Lista de cuentas por pagar en el rango de fechas
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdFechaEmisionBetween(String fechaDesde, String fechaHasta);

    /**
     * Busca cuentas por pagar por rango de fechas de vencimiento.
     *
     * @param fechaDesde Fecha inicial (formato String)
     * @param fechaHasta Fecha final (formato String)
     * @return Lista de cuentas por pagar en el rango de fechas de vencimiento
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdFechaVencimientoBetween(String fechaDesde, String fechaHasta);

    /**
     * Busca cuentas por pagar con saldo pendiente.
     *
     * @param saldoMinimo Saldo mínimo a considerar
     * @return Lista de cuentas por pagar con saldo mayor al mínimo
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdSaldoGreaterThan(BigDecimal saldoMinimo);

    /**
     * Busca cuentas por pagar vencidas (con saldo vencido mayor a cero).
     *
     * @param saldoVencidoMinimo Saldo vencido mínimo
     * @return Lista de cuentas por pagar vencidas
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdSaldoVencidoGreaterThan(BigDecimal saldoVencidoMinimo);

    /**
     * Busca cuentas por pagar por zona.
     *
     * @param zona Zona del proveedor
     * @return Lista de cuentas por pagar de la zona
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdZona(String zona);

    /**
     * Busca cuentas por pagar por usuario que aprueba el pago.
     *
     * @param usuarioAprueba Usuario que aprueba el pago
     * @return Lista de cuentas por pagar aprobadas por el usuario
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdUsuarioApruebaPago(String usuarioAprueba);

    /**
     * Busca cuentas por pagar por proveedor y rango de fechas.
     *
     * @param proveedorId Identificador del proveedor
     * @param fechaDesde Fecha inicial
     * @param fechaHasta Fecha final
     * @return Lista de cuentas por pagar del proveedor en el rango de fechas
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdProveedorIdAndCxpdFechaEmisionBetween(
            String proveedorId, 
            String fechaDesde, 
            String fechaHasta
    );

    /**
     * Busca cuentas por pagar de proveedores relacionados.
     *
     * @param relacionado Indicador de proveedor relacionado
     * @return Lista de cuentas por pagar de proveedores relacionados
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdProvRelacionado(Boolean relacionado);

    /**
     * Obtiene todas las cuentas por pagar.
     *
     * @return Lista con todas las cuentas por pagar
     */
    List<CuentasPorPagarDetalladoTO> findAll();

    /**
     * Guarda una cuenta por pagar.
     *
     * @param cuenta Cuenta por pagar a guardar
     * @return Cuenta por pagar guardada
     */
    CuentasPorPagarDetalladoTO save(CuentasPorPagarDetalladoTO cuenta);

    /**
     * Elimina una cuenta por pagar por su identificador.
     *
     * @param id Identificador de la cuenta por pagar a eliminar
     */
    void deleteById(Integer id);

    /**
     * Verifica si existe una cuenta por pagar con el identificador dado.
     *
     * @param id Identificador de la cuenta por pagar
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Integer id);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * Obtiene cuentas por pagar detalladas usando función de PostgreSQL.
     * Esta consulta llama a la función cartera.fun_cuentas_por_pagar_detallado.
     *
     * @param empresa Código de la empresa
     * @param sector Código del sector (puede ser null)
     * @param proveedor Identificador del proveedor (puede ser null)
     * @param desde Fecha inicial (puede ser null)
     * @param hasta Fecha final (puede ser null)
     * @param excluirAprobadas Si se excluyen las aprobadas
     * @param incluirCheques Si se incluyen cheques
     * @param formatoMensual Formato mensual (puede ser null)
     * @return Lista de cuentas por pagar detalladas
     */
    List<CuentasPorPagarDetalladoTO> findCuentasPorPagarDetallado(
            String empresa,
            String sector,
            String proveedor,
            String desde,
            String hasta,
            boolean excluirAprobadas,
            boolean incluirCheques,
            String formatoMensual
    );

    /**
     * Busca cuentas por pagar ordenadas por fecha de emisión descendente.
     *
     * @param fechaDesde Fecha inicial
     * @param fechaHasta Fecha final
     * @return Lista de cuentas ordenadas por fecha de emisión
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdFechaEmisionBetweenOrderByCxpdFechaEmisionDesc(
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Busca cuentas por pagar ordenadas por fecha de vencimiento ascendente.
     *
     * @param fechaDesde Fecha inicial
     * @param fechaHasta Fecha final
     * @return Lista de cuentas ordenadas por fecha de vencimiento
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdFechaVencimientoBetweenOrderByCxpdFechaVencimientoAsc(
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Busca cuentas por pagar con saldo pendiente ordenadas por saldo descendente.
     *
     * @param saldoMinimo Saldo mínimo a considerar
     * @return Lista de cuentas ordenadas por saldo descendente
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdSaldoGreaterThanOrderByCxpdSaldoDesc(BigDecimal saldoMinimo);

    /**
     * Busca cuentas por pagar vencidas ordenadas por saldo vencido descendente.
     *
     * @param saldoVencidoMinimo Saldo vencido mínimo
     * @return Lista de cuentas vencidas ordenadas por saldo vencido
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdSaldoVencidoGreaterThanOrderByCxpdSaldoVencidoDesc(
            BigDecimal saldoVencidoMinimo
    );

    /**
     * Busca cuentas por pagar por proveedor ordenadas por fecha de emisión.
     *
     * @param proveedorId Identificador del proveedor
     * @return Lista de cuentas ordenadas por fecha de emisión descendente
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdProveedorIdOrderByCxpdFechaEmisionDesc(String proveedorId);

    /**
     * Busca cuentas por pagar por periodo ordenadas por proveedor.
     *
     * @param periodo Periodo contable
     * @return Lista de cuentas ordenadas por proveedor
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdPeriodoOrderByCxpdProveedorRazonSocialAsc(String periodo);

    /**
     * Busca cuentas por pagar por motivo ordenadas por fecha.
     *
     * @param motivo Motivo de la cuenta por pagar
     * @return Lista de cuentas ordenadas por fecha de emisión
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdMotivoOrderByCxpdFechaEmisionDesc(String motivo);

    /**
     * Busca cuentas por pagar vencidas (fecha de vencimiento menor a fecha actual).
     *
     * @param fechaActual Fecha actual para comparación
     * @return Lista de cuentas vencidas
     */
    List<CuentasPorPagarDetalladoTO> findByCxpdFechaVencimientoLessThan(String fechaActual);

    /**
     * Cuenta el número de cuentas por pagar por proveedor.
     *
     * @param proveedorId Identificador del proveedor
     * @return Número de cuentas
     */
    long countByCxpdProveedorId(String proveedorId);

    /**
     * Cuenta el número de cuentas por pagar por periodo.
     *
     * @param periodo Periodo contable
     * @return Número de cuentas
     */
    long countByCxpdPeriodo(String periodo);

    /**
     * Cuenta el número de cuentas por pagar con saldo pendiente.
     *
     * @param saldoMinimo Saldo mínimo a considerar
     * @return Número de cuentas con saldo
     */
    long countByCxpdSaldoGreaterThan(BigDecimal saldoMinimo);

    /**
     * Cuenta el número de cuentas por pagar vencidas.
     *
     * @param saldoVencidoMinimo Saldo vencido mínimo
     * @return Número de cuentas vencidas
     */
    long countByCxpdSaldoVencidoGreaterThan(BigDecimal saldoVencidoMinimo);

    // ========== Métodos de Paginación y Ordenamiento ==========
    // Nota: Los métodos de paginación se implementarán en la capa de infraestructura
    // usando Page<T> y Pageable de Spring Data JPA
}

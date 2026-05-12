package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.CuentasPorCobrarDetalladoApiTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) para acceso a datos de cuentas por cobrar.
 * Define el contrato para operaciones de consulta de cuentas por cobrar.
 * Esta interfaz pertenece a la capa de dominio y no debe tener dependencias de frameworks.
 */
public interface CuentasPorCobrarRepository {

    /**
     * Busca una cuenta por cobrar por su identificador único.
     *
     * @param id Identificador de la cuenta por cobrar
     * @return Optional con la cuenta si existe, vacío en caso contrario
     */
    Optional<CuentasPorCobrarDetalladoApiTO> findById(Integer id);

    /**
     * Busca cuentas por cobrar por empresa.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de cuentas por cobrar de la empresa
     */
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresa(String empresaCodigo);

    /**
     * Busca cuentas por cobrar por empresa y periodo.
     *
     * @param empresaCodigo Código de la empresa
     * @param periodo Periodo contable
     * @return Lista de cuentas por cobrar del periodo
     */
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdPeriodo(String empresaCodigo, String periodo);

    /**
     * Busca cuentas por cobrar por empresa y cliente.
     *
     * @param empresaCodigo Código de la empresa
     * @param clienteId Identificador del cliente
     * @return Lista de cuentas por cobrar del cliente
     */
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdClienteId(String empresaCodigo, String clienteId);

    /**
     * Busca cuentas por cobrar por empresa y motivo.
     *
     * @param empresaCodigo Código de la empresa
     * @param motivo Motivo de la cuenta por cobrar
     * @return Lista de cuentas por cobrar del motivo
     */
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdMotivo(String empresaCodigo, String motivo);

    /**
     * Busca cuentas por cobrar por empresa y número de documento.
     *
     * @param empresaCodigo Código de la empresa
     * @param numeroDocumento Número de documento
     * @return Lista de cuentas por cobrar del documento
     */
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdDocumentoNumero(
            String empresaCodigo, 
            String numeroDocumento
    );

    /**
     * Busca cuentas por cobrar por empresa y rango de fechas de emisión.
     *
     * @param empresaCodigo Código de la empresa
     * @param fechaDesde Fecha inicial (formato String)
     * @param fechaHasta Fecha final (formato String)
     * @return Lista de cuentas por cobrar en el rango de fechas
     */
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdFechaEmisionBetween(
            String empresaCodigo, 
            String fechaDesde, 
            String fechaHasta
    );

    /**
     * Busca cuentas por cobrar por empresa y rango de fechas de vencimiento.
     *
     * @param empresaCodigo Código de la empresa
     * @param fechaDesde Fecha inicial (formato String)
     * @param fechaHasta Fecha final (formato String)
     * @return Lista de cuentas por cobrar en el rango de fechas de vencimiento
     */
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdFechaVencimientoBetween(
            String empresaCodigo, 
            String fechaDesde, 
            String fechaHasta
    );

    /**
     * Busca cuentas por cobrar con saldo pendiente por empresa.
     *
     * @param empresaCodigo Código de la empresa
     * @param saldoMinimo Saldo mínimo a considerar
     * @return Lista de cuentas por cobrar con saldo mayor al mínimo
     */
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdSaldoGreaterThan(
            String empresaCodigo, 
            BigDecimal saldoMinimo
    );

    /**
     * Busca cuentas por cobrar por empresa, cliente y rango de fechas.
     *
     * @param empresaCodigo Código de la empresa
     * @param clienteId Identificador del cliente
     * @param fechaDesde Fecha inicial
     * @param fechaHasta Fecha final
     * @return Lista de cuentas por cobrar del cliente en el rango de fechas
     */
    List<CuentasPorCobrarDetalladoApiTO> findByCxcdEmpresaAndCxcdClienteIdAndCxcdFechaEmisionBetween(
            String empresaCodigo, 
            String clienteId, 
            String fechaDesde, 
            String fechaHasta
    );

    /**
     * Obtiene todas las cuentas por cobrar.
     *
     * @return Lista con todas las cuentas por cobrar
     */
    List<CuentasPorCobrarDetalladoApiTO> findAll();

    /**
     * Guarda una cuenta por cobrar.
     *
     * @param cuenta Cuenta por cobrar a guardar
     * @return Cuenta por cobrar guardada
     */
    CuentasPorCobrarDetalladoApiTO save(CuentasPorCobrarDetalladoApiTO cuenta);

    /**
     * Elimina una cuenta por cobrar por su identificador.
     *
     * @param id Identificador de la cuenta por cobrar a eliminar
     */
    void deleteById(Integer id);

    /**
     * Verifica si existe una cuenta por cobrar con el identificador dado.
     *
     * @param id Identificador de la cuenta por cobrar
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Integer id);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * Obtiene cuentas por cobrar detalladas usando función de PostgreSQL.
     * Esta consulta llama a la función cartera.fun_cuentas_por_cobrar_detallado_api.
     *
     * @param empresa Código de la empresa
     * @param hasta Fecha límite (puede ser null)
     * @return Lista de cuentas por cobrar detalladas
     */
    List<CuentasPorCobrarDetalladoApiTO> findCuentasPorCobrarDetalladoApi(
            String empresa,
            String hasta
    );

    /**
     * Busca cuentas por cobrar ordenadas por fecha de emisión descendente.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de cuentas ordenadas por fecha de emisión
     */
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaOrderByFechaEmisionDesc(String empresaCodigo);

    /**
     * Busca cuentas por cobrar ordenadas por fecha de vencimiento ascendente.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de cuentas ordenadas por fecha de vencimiento
     */
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaOrderByFechaVencimientoAsc(String empresaCodigo);

    /**
     * Busca cuentas por cobrar con saldo pendiente ordenadas por saldo descendente.
     *
     * @param empresaCodigo Código de la empresa
     * @param saldoMinimo Saldo mínimo a considerar
     * @return Lista de cuentas ordenadas por saldo descendente
     */
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaAndSaldoGreaterThanOrderBySaldoDesc(
            String empresaCodigo,
            BigDecimal saldoMinimo
    );

    /**
     * Busca cuentas por cobrar por cliente ordenadas por fecha de emisión.
     *
     * @param empresaCodigo Código de la empresa
     * @param clienteId Identificador del cliente
     * @return Lista de cuentas ordenadas por fecha de emisión descendente
     */
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaAndClienteOrderByFechaEmisionDesc(
            String empresaCodigo,
            String clienteId
    );

    /**
     * Busca cuentas por cobrar vencidas (fecha de vencimiento menor a fecha actual).
     *
     * @param empresaCodigo Código de la empresa
     * @param fechaActual Fecha actual para comparación
     * @return Lista de cuentas vencidas
     */
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaAndFechaVencimientoLessThan(
            String empresaCodigo,
            String fechaActual
    );

    /**
     * Busca cuentas por cobrar por periodo ordenadas por cliente.
     *
     * @param empresaCodigo Código de la empresa
     * @param periodo Periodo contable
     * @return Lista de cuentas ordenadas por cliente
     */
    List<CuentasPorCobrarDetalladoApiTO> findByEmpresaAndPeriodoOrderByClienteAsc(
            String empresaCodigo,
            String periodo
    );

    /**
     * Cuenta el número de cuentas por cobrar por empresa.
     *
     * @param empresaCodigo Código de la empresa
     * @return Número de cuentas
     */
    long countByEmpresa(String empresaCodigo);

    /**
     * Cuenta el número de cuentas por cobrar por cliente.
     *
     * @param empresaCodigo Código de la empresa
     * @param clienteId Identificador del cliente
     * @return Número de cuentas
     */
    long countByEmpresaAndCliente(String empresaCodigo, String clienteId);

    /**
     * Cuenta el número de cuentas por cobrar con saldo pendiente.
     *
     * @param empresaCodigo Código de la empresa
     * @param saldoMinimo Saldo mínimo a considerar
     * @return Número de cuentas con saldo
     */
    long countByEmpresaAndSaldoGreaterThan(String empresaCodigo, BigDecimal saldoMinimo);

    // ========== Métodos de Paginación y Ordenamiento ==========
    // Nota: Los métodos de paginación se implementarán en la capa de infraestructura
    // usando Page<T> y Pageable de Spring Data JPA
}

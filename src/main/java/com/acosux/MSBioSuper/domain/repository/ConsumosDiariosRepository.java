package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.PrdConsumosDiarioMSTO;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) para acceso a datos de consumos diarios.
 * Define el contrato para operaciones de consulta de consumos diarios de producción.
 * Esta interfaz pertenece a la capa de dominio y no debe tener dependencias de frameworks.
 */
public interface ConsumosDiariosRepository {

    /**
     * Busca un consumo diario por su identificador único.
     *
     * @param id Identificador del consumo
     * @return Optional con el consumo si existe, vacío en caso contrario
     */
    Optional<PrdConsumosDiarioMSTO> findById(Integer id);

    /**
     * Busca consumos diarios por empresa.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de consumos diarios de la empresa
     */
    List<PrdConsumosDiarioMSTO> findByEmpresa(String empresaCodigo);

    /**
     * Busca consumos diarios por empresa y sector.
     *
     * @param empresaCodigo Código de la empresa
     * @param sectorCodigo Código del sector
     * @return Lista de consumos diarios del sector
     */
    List<PrdConsumosDiarioMSTO> findByEmpresaAndSector(String empresaCodigo, String sectorCodigo);

    /**
     * Busca consumos diarios por empresa y piscina.
     *
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @return Lista de consumos diarios de la piscina
     */
    List<PrdConsumosDiarioMSTO> findByEmpresaAndPiscina(String empresaCodigo, String piscinaNumero);

    /**
     * Busca consumos diarios por empresa y rango de fechas.
     *
     * @param empresaCodigo Código de la empresa
     * @param fechaDesde Fecha inicial (formato String)
     * @param fechaHasta Fecha final (formato String)
     * @return Lista de consumos diarios en el rango de fechas
     */
    List<PrdConsumosDiarioMSTO> findByEmpresaAndFechaBetween(
            String empresaCodigo, 
            String fechaDesde, 
            String fechaHasta
    );

    /**
     * Busca consumos diarios por empresa y corrida.
     *
     * @param empresaCodigo Código de la empresa
     * @param corridaNumero Número de corrida
     * @return Lista de consumos diarios de la corrida
     */
    List<PrdConsumosDiarioMSTO> findByEmpresaAndCorrida(String empresaCodigo, String corridaNumero);

    /**
     * Busca consumos diarios por empresa y producto.
     *
     * @param empresaCodigo Código de la empresa
     * @param productoCodigo Código del producto
     * @return Lista de consumos diarios del producto
     */
    List<PrdConsumosDiarioMSTO> findByEmpresaAndProducto(String empresaCodigo, String productoCodigo);

    /**
     * Busca consumos diarios por empresa, piscina y fecha.
     *
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @param fecha Fecha del consumo
     * @return Lista de consumos diarios
     */
    List<PrdConsumosDiarioMSTO> findByEmpresaAndPiscinaAndFecha(
            String empresaCodigo, 
            String piscinaNumero, 
            String fecha
    );

    /**
     * Obtiene todos los consumos diarios.
     *
     * @return Lista con todos los consumos diarios
     */
    List<PrdConsumosDiarioMSTO> findAll();

    /**
     * Guarda un consumo diario.
     *
     * @param consumo Consumo diario a guardar
     * @return Consumo diario guardado
     */
    PrdConsumosDiarioMSTO save(PrdConsumosDiarioMSTO consumo);

    /**
     * Elimina un consumo diario por su identificador.
     *
     * @param id Identificador del consumo a eliminar
     */
    void deleteById(Integer id);

    /**
     * Verifica si existe un consumo diario con el identificador dado.
     *
     * @param id Identificador del consumo
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Integer id);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * Obtiene consumos diarios usando función de PostgreSQL.
     * Esta consulta llama a la función produccion.fun_consumos_diarios_microservice.
     *
     * @param empresa Código de la empresa
     * @param codigoSector Código del sector (puede ser null)
     * @param piscina Número de piscina (puede ser null)
     * @param codigoBodega Código de bodega (puede ser null)
     * @param codigoProducto Código del producto (puede ser null)
     * @param fechaInicio Fecha inicial (puede ser null)
     * @param fechaFin Fecha final (puede ser null)
     * @return Lista de consumos diarios
     */
    List<PrdConsumosDiarioMSTO> findConsumosDiariosMicroservice(
            String empresa,
            String codigoSector,
            String piscina,
            String codigoBodega,
            String codigoProducto,
            String fechaInicio,
            String fechaFin
    );

    /**
     * Busca consumos diarios ordenados por fecha descendente.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de consumos ordenados por fecha descendente
     */
    List<PrdConsumosDiarioMSTO> findByEmpresaOrderByFechaDesc(String empresaCodigo);

    /**
     * Busca consumos diarios por empresa y piscina ordenados por fecha.
     *
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @return Lista de consumos ordenados por fecha
     */
    List<PrdConsumosDiarioMSTO> findByEmpresaAndPiscinaOrderByFechaAsc(
            String empresaCodigo,
            String piscinaNumero
    );

    /**
     * Busca consumos diarios por empresa, piscina y rango de fechas ordenados.
     *
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @param fechaDesde Fecha inicial
     * @param fechaHasta Fecha final
     * @return Lista de consumos ordenados por fecha
     */
    List<PrdConsumosDiarioMSTO> findByEmpresaAndPiscinaAndFechaBetweenOrderByFechaAsc(
            String empresaCodigo,
            String piscinaNumero,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Cuenta el número de consumos diarios por empresa.
     *
     * @param empresaCodigo Código de la empresa
     * @return Número de consumos
     */
    long countByEmpresa(String empresaCodigo);

    /**
     * Cuenta el número de consumos diarios por empresa y piscina.
     *
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @return Número de consumos
     */
    long countByEmpresaAndPiscina(String empresaCodigo, String piscinaNumero);

    /**
     * Busca consumos diarios por empresa y producto ordenados por fecha.
     *
     * @param empresaCodigo Código de la empresa
     * @param productoCodigo Código del producto
     * @return Lista de consumos ordenados por fecha descendente
     */
    List<PrdConsumosDiarioMSTO> findByEmpresaAndProductoOrderByFechaDesc(
            String empresaCodigo,
            String productoCodigo
    );

    // ========== Métodos de Paginación y Ordenamiento ==========
    // Nota: Los métodos de paginación se implementarán en la capa de infraestructura
    // usando Page<T> y Pageable de Spring Data JPA
}

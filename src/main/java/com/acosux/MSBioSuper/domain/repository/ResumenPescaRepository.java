package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) para acceso a datos de resumen de pesca.
 * Define el contrato para operaciones de consulta de resúmenes de corridas de pesca.
 * Esta interfaz pertenece a la capa de dominio y no debe tener dependencias de frameworks.
 */
public interface ResumenPescaRepository {

    /**
     * Busca un resumen de pesca por su identificador único.
     *
     * @param id Identificador del resumen
     * @return Optional con el resumen si existe, vacío en caso contrario
     */
    Optional<PrdResumenCorridaTO> findById(Integer id);

    /**
     * Busca resúmenes de pesca por empresa.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de resúmenes de pesca de la empresa
     */
    List<PrdResumenCorridaTO> findByEmpresa(String empresaCodigo);

    /**
     * Busca resúmenes de pesca por empresa y sector.
     *
     * @param empresaCodigo Código de la empresa
     * @param sectorCodigo Código del sector
     * @return Lista de resúmenes de pesca del sector
     */
    List<PrdResumenCorridaTO> findByEmpresaAndSector(String empresaCodigo, String sectorCodigo);

    /**
     * Busca resúmenes de pesca por empresa y rango de fechas de pesca.
     *
     * @param empresaCodigo Código de la empresa
     * @param fechaDesde Fecha inicial (formato String)
     * @param fechaHasta Fecha final (formato String)
     * @return Lista de resúmenes de pesca en el rango de fechas
     */
    List<PrdResumenCorridaTO> findByEmpresaAndFechaPescaBetween(
            String empresaCodigo, 
            String fechaDesde, 
            String fechaHasta
    );

    /**
     * Busca resúmenes de pesca por número de corrida.
     *
     * @param empresaCodigo Código de la empresa
     * @param corridaNumero Número de corrida
     * @return Lista de resúmenes de pesca de la corrida
     */
    List<PrdResumenCorridaTO> findByEmpresaAndCorrida(String empresaCodigo, String corridaNumero);

    /**
     * Busca resúmenes de pesca por piscina.
     *
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @return Lista de resúmenes de pesca de la piscina
     */
    List<PrdResumenCorridaTO> findByEmpresaAndPiscina(String empresaCodigo, String piscinaNumero);

    /**
     * Obtiene todos los resúmenes de pesca.
     *
     * @return Lista con todos los resúmenes de pesca
     */
    List<PrdResumenCorridaTO> findAll();

    /**
     * Guarda un resumen de pesca.
     *
     * @param resumen Resumen de pesca a guardar
     * @return Resumen de pesca guardado
     */
    PrdResumenCorridaTO save(PrdResumenCorridaTO resumen);

    /**
     * Elimina un resumen de pesca por su identificador.
     *
     * @param id Identificador del resumen a eliminar
     */
    void deleteById(Integer id);

    /**
     * Verifica si existe un resumen de pesca con el identificador dado.
     *
     * @param id Identificador del resumen
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Integer id);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * Obtiene resumen de corrida mejorado usando función de PostgreSQL.
     * Esta consulta llama a la función produccion.fun_resumen_corrida_mejorado.
     *
     * @param empresa Código de la empresa
     * @param sector Código del sector (puede ser null)
     * @param tipoResumen Tipo de resumen ('PESCA', 'SIEMBRA', etc.)
     * @param fechaDesde Fecha inicial (puede ser null)
     * @param fechaHasta Fecha final (puede ser null)
     * @param ordenarPorCorrida Si se debe ordenar por corrida
     * @return Lista de resúmenes de corrida
     */
    List<PrdResumenCorridaTO> findResumenCorridaMejorado(
            String empresa,
            String sector,
            String tipoResumen,
            String fechaDesde,
            String fechaHasta,
            boolean ordenarPorCorrida
    );

    /**
     * Busca resúmenes de pesca ordenados por fecha de pesca descendente.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de resúmenes ordenados por fecha descendente
     */
    List<PrdResumenCorridaTO> findByEmpresaOrderByFechaPescaDesc(String empresaCodigo);

    /**
     * Busca resúmenes de pesca ordenados por corrida.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de resúmenes ordenados por corrida
     */
    List<PrdResumenCorridaTO> findByEmpresaOrderByCorrida(String empresaCodigo);

    /**
     * Busca resúmenes de pesca por empresa, sector y rango de fechas con ordenamiento.
     *
     * @param empresaCodigo Código de la empresa
     * @param sectorCodigo Código del sector
     * @param fechaDesde Fecha inicial
     * @param fechaHasta Fecha final
     * @return Lista de resúmenes ordenados por fecha de pesca
     */
    List<PrdResumenCorridaTO> findByEmpresaAndSectorAndFechaPescaBetweenOrderByFechaPescaDesc(
            String empresaCodigo,
            String sectorCodigo,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Cuenta el número de resúmenes de pesca por empresa.
     *
     * @param empresaCodigo Código de la empresa
     * @return Número de resúmenes
     */
    long countByEmpresa(String empresaCodigo);

    /**
     * Cuenta el número de resúmenes de pesca por empresa y sector.
     *
     * @param empresaCodigo Código de la empresa
     * @param sectorCodigo Código del sector
     * @return Número de resúmenes
     */
    long countByEmpresaAndSector(String empresaCodigo, String sectorCodigo);

    // ========== Métodos de Paginación y Ordenamiento ==========
    // Nota: Los métodos de paginación se implementarán en la capa de infraestructura
    // usando Page<T> y Pageable de Spring Data JPA
}

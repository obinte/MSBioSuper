package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.PrdResumenCorridaTO;
import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) para acceso a datos de resumen de siembra.
 * Define el contrato para operaciones de consulta de resúmenes de corridas de siembra.
 * Esta interfaz pertenece a la capa de dominio y no debe tener dependencias de frameworks.
 */
public interface ResumenSiembraRepository {

    /**
     * Busca un resumen de siembra por su identificador único.
     *
     * @param id Identificador del resumen
     * @return Optional con el resumen si existe, vacío en caso contrario
     */
    Optional<PrdResumenCorridaTO> findById(Integer id);

    /**
     * Busca resúmenes de siembra por empresa.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de resúmenes de siembra de la empresa
     */
    List<PrdResumenCorridaTO> findByEmpresa(String empresaCodigo);

    /**
     * Busca resúmenes de siembra por empresa y sector.
     *
     * @param empresaCodigo Código de la empresa
     * @param sectorCodigo Código del sector
     * @return Lista de resúmenes de siembra del sector
     */
    List<PrdResumenCorridaTO> findByEmpresaAndSector(String empresaCodigo, String sectorCodigo);

    /**
     * Busca resúmenes de siembra por empresa y rango de fechas de siembra.
     *
     * @param empresaCodigo Código de la empresa
     * @param fechaDesde Fecha inicial (formato String)
     * @param fechaHasta Fecha final (formato String)
     * @return Lista de resúmenes de siembra en el rango de fechas
     */
    List<PrdResumenCorridaTO> findByEmpresaAndFechaSiembraBetween(
            String empresaCodigo, 
            String fechaDesde, 
            String fechaHasta
    );

    /**
     * Busca resúmenes de siembra por número de corrida.
     *
     * @param empresaCodigo Código de la empresa
     * @param corridaNumero Número de corrida
     * @return Lista de resúmenes de siembra de la corrida
     */
    List<PrdResumenCorridaTO> findByEmpresaAndCorrida(String empresaCodigo, String corridaNumero);

    /**
     * Busca resúmenes de siembra por piscina.
     *
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @return Lista de resúmenes de siembra de la piscina
     */
    List<PrdResumenCorridaTO> findByEmpresaAndPiscina(String empresaCodigo, String piscinaNumero);

    /**
     * Obtiene todos los resúmenes de siembra.
     *
     * @return Lista con todos los resúmenes de siembra
     */
    List<PrdResumenCorridaTO> findAll();

    /**
     * Guarda un resumen de siembra.
     *
     * @param resumen Resumen de siembra a guardar
     * @return Resumen de siembra guardado
     */
    PrdResumenCorridaTO save(PrdResumenCorridaTO resumen);

    /**
     * Elimina un resumen de siembra por su identificador.
     *
     * @param id Identificador del resumen a eliminar
     */
    void deleteById(Integer id);

    /**
     * Verifica si existe un resumen de siembra con el identificador dado.
     *
     * @param id Identificador del resumen
     * @return true si existe, false en caso contrario
     */
    boolean existsById(Integer id);

    // ========== Métodos de Consulta Personalizados con @Query ==========

    /**
     * Obtiene resumen de corrida mejorado para siembra usando función de PostgreSQL.
     * Esta consulta llama a la función produccion.fun_resumen_corrida_mejorado con tipo 'SIEMBRA'.
     *
     * @param empresa Código de la empresa
     * @param sector Código del sector (puede ser null)
     * @param fechaDesde Fecha inicial (puede ser null)
     * @param fechaHasta Fecha final (puede ser null)
     * @param ordenarPorCorrida Si se debe ordenar por corrida
     * @return Lista de resúmenes de corrida de siembra
     */
    List<PrdResumenCorridaTO> findResumenSiembraMejorado(
            String empresa,
            String sector,
            String fechaDesde,
            String fechaHasta,
            boolean ordenarPorCorrida
    );

    /**
     * Busca resúmenes de siembra ordenados por fecha de siembra descendente.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de resúmenes ordenados por fecha descendente
     */
    List<PrdResumenCorridaTO> findByEmpresaOrderByFechaSiembraDesc(String empresaCodigo);

    /**
     * Busca resúmenes de siembra ordenados por corrida.
     *
     * @param empresaCodigo Código de la empresa
     * @return Lista de resúmenes ordenados por corrida
     */
    List<PrdResumenCorridaTO> findByEmpresaOrderByCorrida(String empresaCodigo);

    /**
     * Busca resúmenes de siembra por empresa, sector y rango de fechas con ordenamiento.
     *
     * @param empresaCodigo Código de la empresa
     * @param sectorCodigo Código del sector
     * @param fechaDesde Fecha inicial
     * @param fechaHasta Fecha final
     * @return Lista de resúmenes ordenados por fecha de siembra
     */
    List<PrdResumenCorridaTO> findByEmpresaAndSectorAndFechaSiembraBetweenOrderByFechaSiembraDesc(
            String empresaCodigo,
            String sectorCodigo,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Cuenta el número de resúmenes de siembra por empresa.
     *
     * @param empresaCodigo Código de la empresa
     * @return Número de resúmenes
     */
    long countByEmpresa(String empresaCodigo);

    /**
     * Cuenta el número de resúmenes de siembra por empresa y sector.
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

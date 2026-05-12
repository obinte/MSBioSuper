package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.PrdListadoGrameajeTO;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) del repositorio de grameajes en la capa de dominio.
 * Define el contrato para acceso a datos de grameajes de producción.
 * Esta interfaz es independiente de la tecnología de persistencia.
 * 
 * Siguiendo el patrón de arquitectura hexagonal, esta interfaz representa un puerto
 * que será implementado por un adaptador en la capa de infraestructura.
 */
public interface GrameajeRepository {

    /**
     * Busca todos los grameajes.
     * 
     * @return Lista de todos los grameajes
     */
    List<PrdListadoGrameajeTO> findAll();

    /**
     * Busca un grameaje por su ID.
     * 
     * @param id ID del grameaje
     * @return Optional con el grameaje encontrado o empty
     */
    Optional<PrdListadoGrameajeTO> findById(Integer id);

    /**
     * Busca grameajes por empresa.
     * 
     * @param empresaCodigo Código de la empresa
     * @return Lista de grameajes de la empresa
     */
    List<PrdListadoGrameajeTO> findByGraEmpresa(String empresaCodigo);

    /**
     * Busca grameajes por empresa y sector.
     * 
     * @param empresaCodigo Código de la empresa
     * @param sectorCodigo Código del sector
     * @return Lista de grameajes del sector
     */
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraSector(String empresaCodigo, String sectorCodigo);

    /**
     * Busca grameajes por empresa y piscina.
     * 
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @return Lista de grameajes de la piscina
     */
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraPiscina(String empresaCodigo, String piscinaNumero);

    /**
     * Busca grameajes por empresa y corrida.
     * 
     * @param empresaCodigo Código de la empresa
     * @param corridaNumero Número de corrida
     * @return Lista de grameajes de la corrida
     */
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraCorrida(String empresaCodigo, String corridaNumero);

    /**
     * Busca grameajes por empresa y rango de fechas.
     * 
     * @param empresaCodigo Código de la empresa
     * @param fechaDesde Fecha desde
     * @param fechaHasta Fecha hasta
     * @return Lista de grameajes en el rango de fechas
     */
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraFechaBetween(
            String empresaCodigo,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Busca grameajes por empresa, sector y piscina.
     * 
     * @param empresaCodigo Código de la empresa
     * @param sectorCodigo Código del sector
     * @param piscinaNumero Número de piscina
     * @return Lista de grameajes
     */
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraSectorAndGraPiscina(
            String empresaCodigo,
            String sectorCodigo,
            String piscinaNumero
    );

    /**
     * Busca grameajes por empresa y piscina, ordenados por fecha ascendente.
     * 
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @return Lista de grameajes ordenados por fecha
     */
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraPiscinaOrderByGraFechaAsc(
            String empresaCodigo,
            String piscinaNumero
    );

    /**
     * Busca grameajes por empresa, ordenados por fecha descendente.
     * 
     * @param empresaCodigo Código de la empresa
     * @return Lista de grameajes ordenados por fecha
     */
    List<PrdListadoGrameajeTO> findByGraEmpresaOrderByGraFechaDesc(String empresaCodigo);

    /**
     * Busca grameajes por empresa, piscina y rango de fechas, ordenados por fecha.
     * 
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @param fechaDesde Fecha desde
     * @param fechaHasta Fecha hasta
     * @return Lista de grameajes ordenados por fecha
     */
    List<PrdListadoGrameajeTO> findByGraEmpresaAndGraPiscinaAndGraFechaBetweenOrderByGraFechaAsc(
            String empresaCodigo,
            String piscinaNumero,
            String fechaDesde,
            String fechaHasta
    );

    /**
     * Cuenta grameajes por empresa.
     * 
     * @param empresaCodigo Código de la empresa
     * @return Cantidad de grameajes
     */
    long countByGraEmpresa(String empresaCodigo);

    /**
     * Cuenta grameajes por empresa y piscina.
     * 
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @return Cantidad de grameajes
     */
    long countByGraEmpresaAndGraPiscina(String empresaCodigo, String piscinaNumero);

    /**
     * Cuenta grameajes por empresa y corrida.
     * 
     * @param empresaCodigo Código de la empresa
     * @param corridaNumero Número de corrida
     * @return Cantidad de grameajes
     */
    long countByGraEmpresaAndGraCorrida(String empresaCodigo, String corridaNumero);

    /**
     * Obtiene el listado de grameajes usando función de base de datos.
     * Esta consulta utiliza una función almacenada en PostgreSQL para obtener
     * el listado completo de grameajes con todos los cálculos necesarios.
     * 
     * @param empresa Código de la empresa
     * @param sector Código del sector
     * @param piscina Número de piscina (puede ser null)
     * @param desde Fecha desde (puede ser null)
     * @param hasta Fecha hasta (puede ser null)
     * @return Lista de grameajes con cálculos
     */
    List<PrdListadoGrameajeTO> findListadoGrameaje(
            String empresa,
            String sector,
            String piscina,
            String desde,
            String hasta
    );
}

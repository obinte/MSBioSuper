package com.acosux.MSBioSuper.domain.repository;

import com.acosux.MSBioSuper.domain.model.PrdListaCostosDetalleCorridaTO;

import java.util.List;
import java.util.Optional;

/**
 * Puerto (interfaz) del repositorio de costos de corrida en la capa de dominio.
 * Define el contrato para acceso a datos de costos por piscina/corrida.
 * Esta interfaz es independiente de la tecnología de persistencia.
 * 
 * Siguiendo el patrón de arquitectura hexagonal, esta interfaz representa un puerto
 * que será implementado por un adaptador en la capa de infraestructura.
 */
public interface CostosCorridaRepository {

    /**
     * Busca todos los costos de corrida.
     * 
     * @return Lista de todos los costos de corrida
     */
    List<PrdListaCostosDetalleCorridaTO> findAll();

    /**
     * Busca un costo de corrida por su ID.
     * 
     * @param id ID del costo de corrida
     * @return Optional con el costo de corrida encontrado o empty
     */
    Optional<PrdListaCostosDetalleCorridaTO> findById(Integer id);

    /**
     * Busca costos de corrida por empresa.
     * 
     * @param empresaCodigo Código de la empresa
     * @return Lista de costos de corrida de la empresa
     */
    List<PrdListaCostosDetalleCorridaTO> findByEmpresa(String empresaCodigo);

    /**
     * Busca costos de corrida por empresa y sector.
     * 
     * @param empresaCodigo Código de la empresa
     * @param sectorCodigo Código del sector
     * @return Lista de costos de corrida del sector
     */
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndSector(String empresaCodigo, String sectorCodigo);

    /**
     * Busca costos de corrida por empresa y piscina.
     * 
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @return Lista de costos de corrida de la piscina
     */
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndPiscina(String empresaCodigo, String piscinaNumero);

    /**
     * Busca costos de corrida por empresa y corrida.
     * 
     * @param empresaCodigo Código de la empresa
     * @param corridaNumero Número de corrida
     * @return Lista de costos de corrida de la corrida especificada
     */
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndCorrida(String empresaCodigo, String corridaNumero);

    /**
     * Busca costos de corrida por empresa y producto.
     * 
     * @param empresaCodigo Código de la empresa
     * @param productoCodigo Código del producto
     * @return Lista de costos de corrida del producto
     */
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndProducto(String empresaCodigo, String productoCodigo);

    /**
     * Busca costos de corrida por empresa, sector y piscina.
     * 
     * @param empresaCodigo Código de la empresa
     * @param sectorCodigo Código del sector
     * @param piscinaNumero Número de piscina
     * @return Lista de costos de corrida
     */
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndSectorAndPiscina(
            String empresaCodigo,
            String sectorCodigo,
            String piscinaNumero
    );

    /**
     * Busca costos de corrida por empresa, piscina y corrida.
     * 
     * @param empresaCodigo Código de la empresa
     * @param piscinaNumero Número de piscina
     * @param corridaNumero Número de corrida
     * @return Lista de costos de corrida
     */
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaAndPiscinaAndCorrida(
            String empresaCodigo,
            String piscinaNumero,
            String corridaNumero
    );

    /**
     * Busca costos de corrida ordenados por total descendente.
     * 
     * @param empresaCodigo Código de la empresa
     * @return Lista de costos de corrida ordenados por total
     */
    List<PrdListaCostosDetalleCorridaTO> findByEmpresaOrderByTotalDesc(String empresaCodigo);

    /**
     * Cuenta costos de corrida por empresa.
     * 
     * @param empresaCodigo Código de la empresa
     * @return Cantidad de costos de corrida
     */
    long countByEmpresa(String empresaCodigo);

    /**
     * Cuenta costos de corrida por empresa y corrida.
     * 
     * @param empresaCodigo Código de la empresa
     * @param corridaNumero Número de corrida
     * @return Cantidad de costos de corrida
     */
    long countByEmpresaAndCorrida(String empresaCodigo, String corridaNumero);

    /**
     * Obtiene el detalle de costos por corrida usando función de base de datos.
     * Esta consulta utiliza una función almacenada en PostgreSQL para obtener
     * el detalle completo de costos agrupados según el criterio especificado.
     * 
     * @param empresa Código de la empresa
     * @param sector Código del sector
     * @param piscina Número de piscina
     * @param corrida Número de corrida
     * @param desde Fecha desde (puede ser null)
     * @param hasta Fecha hasta (puede ser null)
     * @param agrupadoPor Criterio de agrupación (PRODUCTO, CATEGORIA, etc.)
     * @return Lista de costos detallados por corrida
     */
    List<PrdListaCostosDetalleCorridaTO> findCostosDetalleCorrida(
            String empresa,
            String sector,
            String piscina,
            String corrida,
            String desde,
            String hasta,
            String agrupadoPor
    );
}

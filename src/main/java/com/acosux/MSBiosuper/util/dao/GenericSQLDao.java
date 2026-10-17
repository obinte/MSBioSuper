package com.acosux.MSBiosuper.util.dao;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface GenericSQLDao {

    @Transactional
    <T> List<T> obtenerPorSql(String consulta, Class<T> type);

    /**
     * Igual que {@link #obtenerPorSql(String, Class)}, pero con parámetros enlazados
     * ({@code :nombre} en la consulta). Un valor null se enlaza como texto: tiparlo en la consulta
     * con {@code CAST(:nombre AS date|integer|boolean)} cuando el destino no es texto.
     */
    @Transactional
    <T> List<T> obtenerPorSql(String consulta, Class<T> type, Map<String, Object> parametros);

    @Transactional
    <T> List<T> obtenerPorSql(String consulta);

    @Transactional
    <T> T obtenerObjetoPorSql(String consulta, Class<T> type);

    @Transactional
    Object obtenerObjetoPorSql(String consulta);

    @Transactional
    int ejecutarSQL(String consulta);

    Session session();
}
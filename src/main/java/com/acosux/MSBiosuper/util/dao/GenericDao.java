package com.acosux.MSBiosuper.util.dao;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, K extends Serializable> {

    @Transactional
    void saveOrUpdate(T t);

    @Transactional
    void saveOrUpdate(List<T> list);

    @Transactional
    void insertar(T t);

    @Transactional
    void insertar(List<T> listInsertar);

    @Transactional
    List<T> obtenerLista(String consulta, Object[] valoresConsulta, boolean mensaje,
                         Object[] valoresInicializar);

    @Transactional
    void actualizar(T t);

    @Transactional
    void actualizar(List<T> listModificar);

    void eliminar(T t);

    void eliminarPorId(Class<T> type, K id);

    void evict(T t);

    Object contar(Class<T> type);

    Object contar(String consulta, Object[] valores);

    List<T> obtenerTodos(Class<T> type);

    List<T> obtenerTodosOrder(Class<T> type, String atributoOrden);

    List<T> obtener(Class<T> type, String atributoOrden, Boolean activo);

    List<T> obtenerListaPorAtributo(Class<T> type, String Atributo, String valorAtributo, Boolean activo);

    T obtenerPorAtributo(Class<T> type, String atributo, String valorAtributo, Boolean activo);

    List<T> obtenerPorHql(String consulta, Object[] valores);

    T obtenerObjetoPorHql(String consulta, Object[] valores);

    List<T> obtenerPorHql(String consulta, Object[] valores, int min, int max);

    T obtenerObjetoPorSql(String consulta, Class<T> type);

    List<T> obtenerPorSql(String consulta, Class<T> type);

    @Transactional
    T obtenerPorId(Class<T> type, K id);

    @Transactional
    T obtenerPorIdEvict(Class<T> type, K id);

    Session session();

}

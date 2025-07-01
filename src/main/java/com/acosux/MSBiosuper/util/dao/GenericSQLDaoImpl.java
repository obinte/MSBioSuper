package com.acosux.MSBiosuper.util.dao;

import org.apache.poi.ss.formula.functions.T;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GenericSQLDaoImpl implements GenericSQLDao {

    @Autowired
    public SessionFactory sessionFactory;

    @SuppressWarnings({"hiding", "unchecked"})
    @Override
    public <T> List<T> obtenerPorSql(String consulta, Class<T> type) {
        Query query = session().createSQLQuery(consulta).addEntity(type.getName());
        return query.list();
    }

    @SuppressWarnings({"hiding", "unchecked"})
    @Override
    public <T> List<T> obtenerPorSql(String consulta) {
        Query query = session().createSQLQuery(consulta);
        return query.list();
    }

    @SuppressWarnings({"hiding", "unchecked"})
    @Override
    public <T> T obtenerObjetoPorSql(String consulta, Class<T> type) {
        Query query = session().createSQLQuery(consulta).addEntity(type.getName());
        List<T> list = query.list();
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object obtenerObjetoPorSql(String consulta) {
        Query query = session().createSQLQuery(consulta);
        List<T> list = query.list();
        return list == null || list.isEmpty() ? null : list.get(0);
    }

    @Override
    public int ejecutarSQL(String consulta) {
        Query query = session().createSQLQuery(consulta);
        return query.executeUpdate();
    }

    @Override
    public Session session() {
        return sessionFactory.getCurrentSession();
    }
}
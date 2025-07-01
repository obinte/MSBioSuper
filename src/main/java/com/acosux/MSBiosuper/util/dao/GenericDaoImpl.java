package com.acosux.MSBiosuper.util.dao;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class GenericDaoImpl<T, K extends Serializable> implements GenericDao<T, K> {

    @Autowired
    public SessionFactory sessionFactory;

    @Override
    public void saveOrUpdate(T t) {
        T t1 = obtenerPorId(t);
        if (t1 == null) {
            insertar(t);
        } else {
            evict(t1);
            session().update(t);
        }

    }

    @Override
    public void saveOrUpdate(List<T> list) {
        for (T t : list) {
            T t1 = obtenerPorId(t);
            if (t1 == null) {
                insertar(t);
            } else {
                evict(t1);
                session().update(t);
            }
        }
    }

    @Override
    public void insertar(T t) {
        evict(t);
        session().save(t);
    }

    @Override
    public void insertar(List<T> listInsertar) {
        for (T t : listInsertar) {
            evict(t);
            session().save(t);
        }
    }

    @Override
    public void actualizar(T t) {
        T t1 = obtenerPorId(t);
        if (t1 != null) {
            evict(t1);
        }
        session().update(t);
    }

    @Override
    public void actualizar(List<T> listModificar) {
        for (T t : listModificar) {
            T t1 = obtenerPorId(t);
            if (t1 != null) {
                evict(t1);
            }
            session().update(t);
        }
    }

    @Override
    public void eliminar(T t) {
        T t1 = obtenerPorIdEliminar(t);
        if (t1 != null) {
            evict(t1);
        }
        session().delete(t);
    }

    @SuppressWarnings("unchecked")
    private T obtenerPorId(T t) {
        try {
            K id = null;
            Class<?> c = t.getClass();
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
                    field.setAccessible(true);
                    id = (K) field.get(t);
                    break;
                }
            }

            if (id == null) {
                for (Method method : c.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Id.class) || method.isAnnotationPresent(EmbeddedId.class)) {
                        id = (K) method.invoke(t, new Object[]{});
                        break;
                    }
                }
            }

            return (T) session().get(c, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private T obtenerPorIdEliminar(T t) {
        try {
            K id = null;
            Class<?> c = t.getClass();
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
                    field.setAccessible(true);
                    id = (K) field.get(t);
                    break;
                }
            }

            if (id == null) {
                for (Method method : c.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Id.class) || method.isAnnotationPresent(EmbeddedId.class)) {
                        id = (K) method.invoke(t, new Object[]{});
                        break;
                    }
                }
            }

            return (T) session().load(c, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void evict(T t) {
        session().evict(t);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void eliminarPorId(Class<T> type, K id) {
        T t = (T) session().load(type.getName(), id);
        session().delete(t);
    }

    @Override
    public Object contar(Class<T> type) {
        return session().createCriteria(type.getName()).setProjection(Projections.count("id")).uniqueResult();
    }

    @Override
    public Object contar(String consulta, Object[] valores) {
        Query query = session().createQuery(consulta);
        if (valores != null) {
            for (Integer i = 0; i < valores.length; i++) {
                Integer iparameter = i + 1;
                query.setParameter(iparameter.toString(), valores[i]);
            }
        }
        return query.uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> obtenerTodos(Class<T> type) {
        return session().createCriteria(type.getName()).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> obtenerTodosOrder(Class<T> type, String atributoOrden) {
        return session().createCriteria(type.getName()).addOrder(Order.asc(atributoOrden)).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> obtener(Class<T> type, String atributoOrden, Boolean activo) {
        if (activo != null) {
            return session().createCriteria(type.getName()).add(Restrictions.eq("activo", activo))
                    .addOrder(Order.asc(atributoOrden)).list();
        } else {
            return session().createCriteria(type.getName()).addOrder(Order.asc(atributoOrden)).list();
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> obtenerListaPorAtributo(Class<T> type, String Atributo, String valorAtributo, Boolean activo) {
        if (activo != null) {
            return session().createCriteria(type.getName()).add(Restrictions.eq(Atributo, valorAtributo))
                    .add(Restrictions.eq("activo", activo)).list();
        } else {
            return session().createCriteria(type.getName()).add(Restrictions.eq(Atributo, valorAtributo)).list();
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public T obtenerPorAtributo(Class<T> type, String Atributo, String valorAtributo, Boolean activo) {
        if (activo != null) {
            return (T) session().createCriteria(type.getName()).add(Restrictions.eq(Atributo, valorAtributo))
                    .add(Restrictions.eq("activo", activo)).uniqueResult();
        } else {
            return (T) session().createCriteria(type.getName()).add(Restrictions.eq(Atributo, valorAtributo))
                    .uniqueResult();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> obtenerPorHql(String consulta, Object[] valores) {
        Query query = session().createQuery(consulta);
        if (valores != null) {
            for (int i = 0; i < valores.length; i++) {
                if (valores[i] != null) {
                    query.setParameter(String.valueOf(i + 1), valores[i]);
                }
            }
        }
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> obtenerLista(String consulta, Object[] valoresConsulta, boolean mensaje,
                                Object[] valoresInicializar) {
        boolean validacion = false;
        List<T> list = null;
        Query query = session().createQuery(consulta);
        if (valoresConsulta != null) {
            for (int i = 0; i < valoresConsulta.length; i++) {
                if (valoresConsulta[i] != null) {

                    query.setParameter(String.valueOf(i + 1), valoresConsulta[i]);
                }
            }
        }
        if (validacion) {
            //
        } else {
            list = query.list();
            if (list.isEmpty() && mensaje) {
                //
            } else {
                for (T t : list) {
                    for (int i = 0; i < valoresInicializar.length; i++) {
                        try {
                            Hibernate.initialize(t.getClass().getMethod(valoresInicializar[i].toString()).invoke(t));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T obtenerObjetoPorHql(String consulta, Object[] valores) {
        Query query = session().createQuery(consulta);
        if (valores != null) {
            for (int i = 0; i < valores.length; i++) {
                if (valores[i] != null) {
                    query.setParameter(String.valueOf(i + 1), valores[i]);
                }
            }
        }
        List<T> lista = query.list();

        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> obtenerPorHql(String consulta, Object[] valores, int min, int max) {
        Query query = session().createQuery(consulta).setFirstResult(min).setMaxResults(max);
        if (valores != null) {
            for (int i = 0; i < valores.length; i++) {
                query.setParameter(String.valueOf(i + 1), valores[i]);
            }
        }

        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T obtenerObjetoPorSql(String consulta, Class<T> type) {
        Query query = session().createSQLQuery(consulta).addEntity(type.getName());
        List<T> lista = query.list();
        return lista == null || lista.isEmpty() ? null : lista.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> obtenerPorSql(String consulta, Class<T> type) {
        Query query = session().createSQLQuery(consulta).addEntity(type.getName());
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T obtenerPorId(Class<T> type, K id) {
        return (T) session().get(type.getName(), id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T obtenerPorIdEvict(Class<T> type, K id) {
        T entidad = (T) session().get(type.getName(), id);
        if (entidad != null) {
            evict(entidad);
        }
        return entidad;
    }

    public Session session() {
        return sessionFactory.getCurrentSession();
    }

}

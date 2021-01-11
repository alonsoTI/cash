package com.financiero.cash.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.hiper.cash.util.Util;

public class ECEntityDAO<E> {
	
	private static enum TipoConsulta{HQL,SQL};
	
    private static Logger logger = Logger.getLogger(ECEntityDAO.class);

	
	private static SessionFactory sf;

	static {
		try{		
			sf = new Configuration().configure("hibernate-easy.cfg.xml").buildSessionFactory();
		}catch(Throwable ex){
			logger.error("Error Inicializando Hibernate EasyCash" + ex);
		}
	}
	
	private static Session getSession() {
		return sf.openSession();
	}
	
	public static List<Object[]> getResultListSQL(String sql,Map<String, Object> parametros) {
		List<Object[]> lista=null;
		Session session=null;
		try {
			session = getSession();
			session.beginTransaction();
			Query q = createQuery(session, TipoConsulta.SQL, sql, parametros);
			lista = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}		
		return lista;

	}

	public static List<Object[]> getResultListSQL(String sql,Map<String, Object> parametros, int inicio, int nroRegistros) {
		List<Object[]> lista=null;
		Session session=null;
		try {
			session = getSession();
			session.beginTransaction();
			Query q = createQuery(session, TipoConsulta.SQL, sql, parametros,
					inicio, nroRegistros);
			lista = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		return lista;

	}
	
	public static Object getSingleResultSQL(String sql,	Map<String, Object> parametros) {
		Object obj=null;
		Session session=null;
		try {
			session = getSession();
			session.beginTransaction();
			Query q = createQuery(session, TipoConsulta.SQL, sql, parametros);
			obj = q.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		return obj;

	}
	
	private static Query createQuery(Session session,TipoConsulta tipo,String sql,Map<String, Object> parametros){
		Query query;
		if( tipo == TipoConsulta.SQL ){
			query = session.createSQLQuery(sql);
		}else{
			query = session.createQuery(sql);
		}
		Object obj;
		for (String parametro : parametros.keySet()) {
			obj  = parametros.get(parametro);
			if( obj instanceof List){
				query.setParameterList(parametro, (Collection) obj);
			}else{
				query.setParameter(parametro, obj);
			}
		}		

		return query;
	}
	
	private static Query createQuery(Session session,TipoConsulta tipo,String sql,Map<String, Object> parametros,
			int inicio, int nroRegistros){
		Query query = createQuery(session, tipo, sql, parametros);		
		query.setFirstResult(inicio);
		query.setMaxResults(nroRegistros);

		return query;
	}
}

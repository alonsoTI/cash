package com.financiero.cash.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.util.TipoConsulta;


public class EntityDAO<E> {
	@SuppressWarnings("unchecked")
	public static List<Object[]> find(String sql,Map<String,Object> parametros,int inicio,int nroRegistros)throws Exception{		
		Session session = null;
		List<Object[]> lista=null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query q = createQuery(session, TipoConsulta.HQL, sql, parametros,
					inicio, nroRegistros);
			lista = q.list();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}        
		return lista;
	}
	
	public static List<Object[]> find(String sql,Map<String,Object> parametros)throws Exception{		
		Session session = null;
		List<Object[]> lista=null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query q = createQuery(session, TipoConsulta.HQL, sql, parametros);
			lista = q.list();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}        
		return lista;
	}
	

	
	public static List<Object> findSQL(String sql,Map<String,Object> parametros)throws Exception{		
		Session session = null;
		List<Object> lista=null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query q = createQuery(session, TipoConsulta.SQL, sql, parametros);
			lista = q.list();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}        
		return lista;
	}
	
	public static int execute(String sql,Map<String,Object> parametros)throws Exception{		
		Session session = null;		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query q = createQuery(session, TipoConsulta.SQL, sql, parametros);
			return q.executeUpdate();			
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}	
	}
	
	
	
	public static List<Object[]> findSQL(Session s,String sql,Map<String,Object> parametros)throws Exception{		
		
		try {			
			Query q = createQuery(s, TipoConsulta.SQL, sql, parametros);
			return q.list();
		} catch (Exception e) {
			throw e;
		} finally {
			s.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <K> List<K> find(Class<K> entidad,String sql,Map<String,Object> parametros)throws Exception{		
		Session session = null;
		List<K> lista=null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query q = createQuery(session, TipoConsulta.HQL, sql, parametros);
			lista = q.list();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}        
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public static <K> List<K> find(Session session,Class<K> entidad,String sql,Map<String,Object> parametros)throws Exception{			
        session.beginTransaction();
        Query q = createQuery(session, TipoConsulta.HQL, sql, parametros);        
        List<K> lista = q.list();
        session.getTransaction().commit();
		return lista;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static <K> K findUnique(Class<K> entidad,String sql,Map<String,Object> parametros)throws Exception{		
		Session session = null;
		K obj=null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query q = createQuery(session, TipoConsulta.HQL, sql, parametros);
			obj = (K) q.uniqueResult();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		return obj;
	}
	

	
	public static int countSQL(String sql,Map<String,Object> parametros)throws Exception{		
		Session session = null;
		Integer obj=0;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query q = createQuery(session, TipoConsulta.SQL, sql, parametros);
			obj = (Integer) q.uniqueResult();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}        
		return obj.intValue();
	}
	
	public static int count(String sql,Map<String,Object> parametros)throws Exception{		
		Session session = null;
		Long obj= new Long(0);
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query q = createQuery(session, TipoConsulta.HQL, sql, parametros);
			obj = (Long) q.uniqueResult();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}        
		return obj.intValue();
	}
	
	public static double sumSQL(String sql,Map<String,Object> parametros)throws Exception{		
		Session session = null;		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query q = createQuery(session, TipoConsulta.SQL, sql, parametros);
			BigDecimal ob = (BigDecimal) q.uniqueResult();
			if( ob == null ){
				return 0.0;
			}
			return ob.doubleValue();
		}catch (NonUniqueResultException e) {
			return 0.0;
		}catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}       
		
	}
	
	public static Query createQuery(Session session,TipoConsulta tipo,String sql,Map<String, 
					Object> parametros,int inicio,int nroRegistros){
		return createQuery(session, tipo, sql, parametros).setFirstResult(inicio).setMaxResults(nroRegistros);		
	}
	
	@SuppressWarnings("unchecked")
	public static Query createQuery(Session session,TipoConsulta tipo,String sql,Map<String, Object> parametros){
		Query query;
		if( tipo == TipoConsulta.SQL ){
			query = session.createSQLQuery(sql);
		}else{
			query = session.createQuery(sql);
		}
		Object obj;
		if( parametros != null ){
			for (String parametro : parametros.keySet()) {
				obj  = parametros.get(parametro);
				if( obj instanceof List){
					query.setParameterList(parametro, (Collection) obj);
				}else{					
					query.setParameter(parametro, obj);					
				}
			}		
		}

		return query;
	}

	@SuppressWarnings("unchecked")
	public static <K> K findById(Class<K> entidad,Serializable pk )throws Exception{
		K objeto = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			objeto = (K) session.get(entidad, pk);
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		return objeto;
	}
	
	@SuppressWarnings("unchecked")
	public static <K> K loadById(Class<K> entidad,Serializable pk )throws Exception{
		K objeto = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			objeto = (K) session.load(entidad, pk);
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		return objeto;
	}
	
	@SuppressWarnings("unchecked")
	public static <K> K findById(Session session,Class<K> entidad,Serializable pk )throws Exception{
		K objeto = null;		
		try {
			session.beginTransaction();
			objeto = (K) session.get(entidad, pk);
			session.getTransaction().commit();
		} catch (Exception e) {
			throw e;
		} finally {
			session.close();
		}
		return objeto;
	}
}

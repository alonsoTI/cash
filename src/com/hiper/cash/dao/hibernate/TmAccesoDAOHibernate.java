package com.hiper.cash.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.financiero.cash.dao.EntityDAO;
import com.hiper.cash.dao.TmAccesoDAO;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TmAcceso;

public class TmAccesoDAOHibernate implements TmAccesoDAO {

	@Override
	public TmAcceso getTmAcceso(String tco) throws SQLException {		
		Session session = null; 
        try{
            session = HibernateUtil.getSeguridadSessionFactory().openSession();
			return EntityDAO.findById(session,TmAcceso.class,tco);			
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public boolean crear(String tco) throws SQLException {
		Session session = null; 
        try{
        	TmAcceso acceso = new TmAcceso();
        	acceso.setTarjeta(tco);
            session = HibernateUtil.getSeguridadSessionFactory().openSession();
			session.beginTransaction();
			session.persist(acceso);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			throw new SQLException(e);
		}finally{
			session.close();
		}
	}

	@Override
	public boolean actualizar(TmAcceso acceso) throws SQLException {
		Session session = null; 
        try{        	
            session = HibernateUtil.getSeguridadSessionFactory().openSession();
			session.beginTransaction();
			session.update(acceso);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			throw new SQLException(e);
		}finally{
			session.close();
		}
	}


	public boolean esConsulta(String tarjeta) throws SQLException {
		Session session = null; 
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createSQLQuery("EXEC BFSP_USUARIO_SOLO_CONSULTA :tarjeta")
            .setParameter("tarjeta", tarjeta);
            int n =  query.list().size();       
            session.getTransaction().commit();           
            if( n > 0 ){
            	return  true;
            }else{
            	return false;
            }
        }
        catch(Exception e){
        	throw new SQLException(e);	            
        }
        finally{
        	session.close();
        }
	}
}
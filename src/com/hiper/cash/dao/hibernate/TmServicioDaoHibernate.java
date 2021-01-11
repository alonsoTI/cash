/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TmServicioDao;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.domain.TmServicio;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.util.Constantes;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author esilva
 */
public class TmServicioDaoHibernate implements TmServicioDao{

    private static Logger logger = Logger.getLogger(TmServicioDaoHibernate.class);

    public TmServicio select(String codigo){
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();            
            TmServicio result = (TmServicio)session
                                .createQuery("from TmServicio ts where ts.csrIdServicio = :servicioid ")
                                .setParameter("servicioid", codigo)
                                .uniqueResult();            
            return result;
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
            return null;
        }finally{
        	session.close();
        }
    }

	
	public TmServicio getServicio(long idServicioEmpresa) {
		Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();            
            TmServicio result = (TmServicio) session.createQuery(
                " select ta.tmServicio from TaServicioxEmpresa ta " +
                " where ta.csemIdServicioEmpresa = :servicioid ").setParameter("servicioid", idServicioEmpresa).uniqueResult();            
            return result;
        } catch (Exception ex) {
            logger.error(ex.toString(),ex);
            return null;
        }finally{
        	session.close();
        }
	}
	
	public String obtenerIdServicio(long idServicioEmpresa)
    {
        String idServicio = null;
        try
        {
            Session session = null;
            StringBuilder sql = new StringBuilder(
                    "select tmServicio.csrIdServicio from TaServicioxEmpresa a where a.csemIdServicioEmpresa=:idServEmp");
            session = HibernateUtil.getSessionFactory().openSession();
            idServicio = (String) session.createQuery(sql.toString()).setParameter("idServEmp", idServicioEmpresa)
                    .uniqueResult();

        }
        catch (Exception e)
        {
            throw new RuntimeException("Error accesiendo a la bd", e);
        }
        return idServicio;
    }
}

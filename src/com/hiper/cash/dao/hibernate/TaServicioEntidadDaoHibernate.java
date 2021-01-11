/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaServicioEntidadDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TaServicioEntidad;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author jwong
 */
public class TaServicioEntidadDaoHibernate implements TaServicioEntidadDao{

    private static Logger logger = Logger.getLogger(TaServicioEntidadDaoHibernate.class);

    public TaServicioEntidad selectServicioEntidad(long cSEnIdServEmp, String cSEnIdEntidad, String cSEnTipoEntidad) {
        Session session = null;
        String query =  " from TaServicioEntidad " +
                        " where id.csenIdServEmp = :idservemp " +
                        " and id.csenIdEntidad = :identidad " +
                        " and id.csenTipoEntidad = :idTipoEntidad";
        try{
            session = HibernateUtil.getSessionFactory().openSession();            
            TaServicioEntidad result = (TaServicioEntidad)session.createQuery(query)
                                .setParameter("idservemp", cSEnIdServEmp)
                                .setParameter("identidad", cSEnIdEntidad)
                                .setParameter("idTipoEntidad", cSEnTipoEntidad)
                                .uniqueResult();            
            return result;
        }
        catch(Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
    }

    public boolean insert(TaServicioEntidad taServEntidad) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(taServEntidad);
            session.getTransaction().commit();
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
            return false;
        }finally{
        	session.close();
        }
        return true;
    }

    public boolean delete(TaServicioEntidad taServEntidad) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(taServEntidad);
            session.getTransaction().commit();
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
            return false;
        }finally{
        	session.close();
        }        
        return true;
    }

}

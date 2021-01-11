/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaServicioOpcionDao;
import com.hiper.cash.domain.TaServicioOpcion;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
/**
 *
 * @author esilva
 */
public class TaServicioOpcionDaoHibernate implements TaServicioOpcionDao{

    private static Logger logger = Logger.getLogger(TaServicioOpcionDaoHibernate.class);

    public TaServicioOpcion select(String modulo, String submodulo){
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();            
            TaServicioOpcion result = (TaServicioOpcion)session
                                .createQuery("from TaServicioOpcion tso where tso.id.csomodulo = :moduloid and tso.id.csoproceso = :procesoid ")
                                .setParameter("moduloid", modulo)
                                .setParameter("procesoid", submodulo)
                                .uniqueResult();
            return result;
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
            return null;
        }finally{
        	session.close();
        }       
    }
    
    public List select(String modulo){
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();            
            List result = session
                                .createQuery("from TaServicioOpcion tso where tso.id.csomodulo = :moduloid ")
                                .setParameter("moduloid", modulo)
                                .list();            
            return result;
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
            return null;
        }finally{
        	session.close();
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaMapaCamposDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TaMapaCampos;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author esilva
 */
public class TaMapaCamposDaoHibernate implements TaMapaCamposDao {

    private static Logger logger = Logger.getLogger(TaMapaCamposDaoHibernate.class);

    public TaMapaCampos selectMapaCampos(long servemp) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();                       
            String query =
                " from TaMapaCampos tmc " +
                " where tmc.cmcidMapaCampos = ( " +
                "   select tse.taMapaCampos.cmcidMapaCampos " +
                "   from TaServicioxEmpresa tse " +
                "   where tse.csemIdServicioEmpresa = :servempid) ";
            
            TaMapaCampos result = (TaMapaCampos)session.createQuery(query)
                                .setParameter("servempid", servemp).uniqueResult();
                        
            return result;
        } catch (Exception ex) {
            logger.error(ex.toString(),ex);
            return null;
        }finally{
        	session.close();
        }
    }    
}

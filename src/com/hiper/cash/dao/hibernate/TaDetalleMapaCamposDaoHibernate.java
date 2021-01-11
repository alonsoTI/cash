/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaDetalleMapaCamposDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author jwong
 */
public class TaDetalleMapaCamposDaoHibernate implements TaDetalleMapaCamposDao{

    private static Logger logger = Logger.getLogger(TaDetalleMapaCamposDaoHibernate.class);

    public List selectDetalleMapaCampos(long cdmidMapaCampos) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();            
            String query =
                " from TaDetalleMapaCampos where cdmidMapaCampos = :cdmid ";
            List result = session.createQuery(query)
                                .setParameter("cdmid", cdmidMapaCampos).list();           
            return result;
        } catch (Exception ex) {
            logger.error(ex.toString(),ex);
            return null;
        }finally{
        	session.close();
        }
    }
}

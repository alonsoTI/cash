/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TmBancoDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author jwong
 */
public class TmBancoDaoHibernate implements TmBancoDao{

    private static Logger logger = Logger.getLogger(TmBancoDaoHibernate.class);

    public List select() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();         
            List result = session.createQuery("from TmBanco").list();            
            return result;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
    }
    public String obtenerBanco(String codigoBco){
        Session session = null;
        String nombreBanco = "";
        try {
            session = HibernateUtil.getSessionFactory().openSession();            
            List result = session.createQuery(
                " select tm.cbaNombre " +
                " from TmBanco tm " +
                " where tm.dbaCodigo = :codigoBco ")
                .setString("codigoBco", codigoBco).list();            

            Iterator iter = result.iterator();

            if (iter.hasNext()) {
                nombreBanco = (String) iter.next();
            }
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return "";
        }finally{
        	session.close();
        }        
        return nombreBanco;
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.hiper.cash.dao.TmSoporteDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;

/**
 *
 * @author jwong
 */
public class TmSoporteDaoHibernate implements TmSoporteDao {

    private static Logger logger = Logger.getLogger(TmSoporteDaoHibernate.class);

    public List select() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();            
            List result = session.createQuery("from TmSoporte").list();            
            return result;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TxParameterDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TxParameter;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author jwong
 */
public class TxParameterDaoHibernate implements TxParameterDao{

    private static Logger logger = Logger.getLogger(TxParameterDaoHibernate.class);
    
    public TxParameter selectById(String cPmParameterId) {
        Session session = null;
        try {
            
            session = HibernateUtil.getSessionFactory().openSession();
                        
            TxParameter result = (TxParameter) session.createQuery(
                    "from TxParameter where cpmParameterId = :codeid ")
                    .setParameter("codeid", cPmParameterId)
                    .uniqueResult();

            return result;
        } catch (Exception e) {
            logger.error(e.toString(),e);            
            return null;
        }finally{
        	session.close();
        }
    }
}

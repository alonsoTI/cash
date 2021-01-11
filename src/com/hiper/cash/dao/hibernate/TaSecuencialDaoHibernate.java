/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaSecuencialDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author jmoreno
 */
public class TaSecuencialDaoHibernate implements TaSecuencialDao {

    private static Logger logger = Logger.getLogger(TaSecuencialDaoHibernate.class);

    public synchronized int getIdEnvio(String id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            int ultimo = -1;
            String hqlUpdate = "update TaSecuencial ta set ta.nseUltValor = ta.nseUltValor+1 where ta.cseId = :idSec";
            int updatedEntities = session.createQuery( hqlUpdate )
                                    .setString( "idSec", id )
                                    .executeUpdate();
            session.getTransaction().commit();
            
            if(updatedEntities==1){
                Session session_2 = HibernateUtil.getSessionFactory().openSession();
                session_2.beginTransaction();
                List result = session_2.createQuery("select ta.nseUltValor from TaSecuencial ta where ta.cseId = :idSec ")
                        .setParameter("idSec", id)
                        .list();
                session_2.getTransaction().commit();
                
                Iterator iter = result.iterator();
                Integer code = null;                
                if (iter.hasNext()) {
                    code = (Integer) iter.next();
                }
                ultimo = code.intValue();
            }
           return ultimo;
            
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return -1;
        }finally{
        	session.close();
        }
        
    }

}

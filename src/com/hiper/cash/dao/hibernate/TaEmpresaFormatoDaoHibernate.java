/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaEmpresaFormatoDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * TaEmpresaFormatoDaoHibernate
 * @version 1.0 05/01/2010
 * @author jmoreno
 * Copyright © HIPER S.A 
 */
public class TaEmpresaFormatoDaoHibernate implements TaEmpresaFormatoDao {
    private static Logger logger = Logger.getLogger(TaEmpresaFormatoDaoHibernate.class);
    public String getOutFormat(String idEmpresa) {
        Session session = null;
        String xmlOut = "";
        try {
            session = HibernateUtil.getSessionFactory().openSession();            
            List result = session.createQuery("select ta.defxmlsal from TaEmpresaFormato ta where ta.cefidEmpresa = :idEmpresa")
                    .setParameter("idEmpresa",idEmpresa)
                    .list();
            Iterator ite = result.iterator();
            while (ite.hasNext()) {
                xmlOut = (String) ite.next();
            }
        } catch (Exception e) {
            logger.error(e.toString(),e);
        }finally{
        	session.close();
        }
        return xmlOut;
        
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaEmpresaDeServicioDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author esilva
 */
public class TaEmpresaDeServicioDaoHibernate implements TaEmpresaDeServicioDao{

    private static Logger logger = Logger.getLogger(TaEmpresaDeServicioDaoHibernate.class);

    public Map selectEmpresaDeServicio(int grupo){
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();            
            List result = session.createQuery("select ta.cesid, ta.desaccion from TaEmpresaDeServicio ta where ta.cflagOffLine =:flagid ")
                                .setCharacter("flagid", '0')
                                .list();            
            Map empresa_map = new HashMap();
            Iterator iter = result.iterator();
            while (iter.hasNext()) {
                Object[] al_empresa = (Object[])iter.next();
                empresa_map.put(al_empresa[0].toString(), al_empresa[1].toString());
            }
            return empresa_map;
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
            return null;
        }finally{
        	session.close();
        }
    }
}

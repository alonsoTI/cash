/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import java.util.List;
import org.hibernate.Session;
import com.hiper.cash.dao.TmFuncionarioDao;
import com.hiper.cash.domain.TmFuncionario;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import org.apache.log4j.Logger;

/**
 *
 * @author jwong
 */
public class TmFuncionarioDaoHibernate implements TmFuncionarioDao{

    private static Logger logger = Logger.getLogger(TmFuncionarioDaoHibernate.class);


    public TmFuncionario selectFuncionarioByEmpresaAndType(String empresa, String tipoFuncionario) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();            
            TmFuncionario result = (TmFuncionario) session.createQuery(
                " from TmFuncionario " +
                " where cfuIdFuncionario in(" +
                "   select id.cfemIdFuncionario " +
                "   from TaFuncionarioEmpresa " +
                "   where id.cfemIdEmpresa = :empresaid " +
                "   and id.cfemTipo = :tipoFuncId) "
            ).setParameter("empresaid", empresa).setParameter("tipoFuncId", tipoFuncionario).uniqueResult();            
            return result;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
    }
}

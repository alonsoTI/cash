/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;
import static com.hiper.cash.util.CashConstants.FLAG_NUEVOS_CODIGOS_IBS;
import java.util.List;

import com.hiper.cash.dao.TxResultDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TxResult;
import org.apache.log4j.Logger;
import org.hibernate.Session;
/**
 *
 * @author jmoreno
 */
public class TxResultDaoHibernate implements TxResultDao {

    private static Logger logger = Logger.getLogger(TxResultDaoHibernate.class);

    public TxResult selectByCodIbs(String codIbs) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            TxResult result = (TxResult)session.createQuery("from TxResult where id.crsResultExt = :codIbs and crsTipoMensajes is null").
                        setParameter("codIbs",codIbs).uniqueResult();

            return result;         
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
    }
    
	@Override
	public List<TxResult> seleccionarTabla() {
		Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            List<TxResult> result = (List<TxResult>)session.createQuery("from TxResult where crsTipoMensajes is null").list();                        
            return result;         
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
	}

	@Override
	public TxResult obtenerNuevoCodigoIBS(String codigoIBS) {
		Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            TxResult result = (TxResult)session.createQuery("from TxResult where id.crsResultExt = :codIbs and " +
            		"crsTipoMensajes = :flagNuevosMensajes").
                        setParameter("codIbs",codigoIBS).
                        setParameter("flagNuevosMensajes",FLAG_NUEVOS_CODIGOS_IBS).uniqueResult();
            return result;         
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
	}

}

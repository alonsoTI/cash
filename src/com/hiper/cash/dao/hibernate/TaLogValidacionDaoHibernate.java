/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.hiper.cash.dao.TaLogValidacionDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.dao.jdbc.DBManager;
import com.hiper.cash.domain.TaLogValidacion;
import com.hiper.cash.entidad.BeanPaginacion;

/**
 * 
 * @author jmoreno
 */
public class TaLogValidacionDaoHibernate implements TaLogValidacionDao
{

    private static Logger logger = Logger.getLogger(TaLogValidacionDaoHibernate.class);

    public List obtenerLogErrores(int idEnvio)
    {
        Session session = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            List result = session.createQuery("from TaLogValidacion ta where ta.id.clvaIdEnvio = :idEnvio ")
                    .setParameter("idEnvio", idEnvio).list();
            return result;
        }
        catch (Exception e)
        {
            logger.error(e.toString(), e);
            return null;
        }
        finally
        {
            session.close();
        }
    }

    public List obtenerLogErrores(int idEnvio, BeanPaginacion bpag)
    {
        Session session = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            List result = session
                    .createQuery(
                            "from TaLogValidacion ta where ta.id.clvaIdEnvio = :idEnvio order by ta.id.nlvaNumLinea asc")
                    .setParameter("idEnvio", idEnvio).setFirstResult((int)bpag.getM_seleccion())
                    .setMaxResults(bpag.getM_regPagina()).list();
            return result;
        }
        catch (Exception e)
        {
            logger.error(e.toString(), e);
            return null;
        }
        finally
        {
            session.close();
        }
    }
    
    /**
     * 
     * @param idEnvio
     * @return
     */
    public TaLogValidacion obtenerLogError(int idEnvio, long numeroLinea)
    {
        TaLogValidacion resultado = null;
        Session session = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            resultado = (TaLogValidacion) session
                    .createQuery(
                            "from TaLogValidacion ta where ta.id.clvaIdEnvio = :idEnvio and ta.id.nlvaNumLinea = :numLinea")
                    .setParameter("idEnvio", idEnvio).setParameter("numLinea", numeroLinea).uniqueResult();
            
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error consultando datos", e);
        }
        finally
        {
            session.close();
        }
        return resultado;
    }

    public long obtenerCantErrores(int idEnvio)
    {
        Session session = null;
        try
        {
            session = HibernateUtil.getSessionFactory().openSession();
            List result = session
                    .createQuery(
                            "select count(ta.id.nlvaNumLinea) from TaLogValidacion ta where ta.id.clvaIdEnvio = :idEnvio")
                    .setParameter("idEnvio", idEnvio).list();
            Iterator iter = result.iterator();
            long cantidad = 0;
            Long code = null;
            if (iter.hasNext())
            {
                code = (Long) iter.next();
            }
            cantidad = code.longValue();
            return cantidad;
        }
        catch (Exception e)
        {
            logger.error(e.toString(), e);
            return 0;
        }
        finally
        {
            session.close();
        }
    }

    public void insertarLogValidacion(TaLogValidacion lv)
    {
        String sql = "insert into taLogValidacion values(?,?,?,?)";
        Connection conn = null;
        try
        {
            conn = DBManager.getConnectionDBTransaccional();
            PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.setLong(1, lv.getId().getClvaIdEnvio());
            pstm.setInt(2, (int) lv.getId().getNlvaNumLinea());
            pstm.setString(3, lv.getDlvaDescripcion());
            pstm.setString(4, lv.getEnlace());
            int insert = pstm.executeUpdate();
            if (insert != 1)
            {
                throw new RuntimeException("Error insertando datos");
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error insertando datos", e);
        }
        finally
        {
            DBManager.closeConnection(conn);
        }
    }

}

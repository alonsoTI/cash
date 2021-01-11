/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaCuentasServicioEmpresaDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.entidad.BeanCuenta;
import com.hiper.cash.util.Constantes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
/**
 *
 * @author esilva
 */
public class TaCuentasServicioEmpresaDaoHibernate implements TaCuentasServicioEmpresaDao{

    private static Logger logger = Logger.getLogger(TaCuentasServicioEmpresaDaoHibernate.class);

    public List selectServicioxEmpresa(String servicio){
        List result;
        Session session = null;
        try{            
            String sql = "from TaCuentasServicioEmpresa ta where ta.id.csemIdServicioEmpresa = :idServEmp and ta.ccsemEstado = :estadoCta";
            session = HibernateUtil.getSessionFactory().openSession();            
            result = session.createQuery(sql).setParameter("idServEmp",Long.parseLong(servicio))
                    .setParameter("estadoCta", Constantes.HQL_CASH_ESTADO_CUENTAXEMPRESA_ACTIVO).list();
        }
         catch(Exception e){
            logger.error(e.toString(),e);
            return new ArrayList();
        }finally{
        	session.close();
        }
        return result;
    }

    public BeanCuenta select(String cuenta, long servemp){
        String strQuery;
        Session session = null;
        strQuery =  "select ta.id.dcsemNumeroCuenta, ta.id.csemIdServicioEmpresa, ta.dcsemTipoCuenta, " +
                    " ta.dcsemDescripcion, ta.ccsemEstado, " +
                    " ta.ccsemMoneda " +
                    "from TaCuentasServicioEmpresa ta " +
                    "where ta.id.dcsemNumeroCuenta  =:cuentaid and ta.id.csemIdServicioEmpresa =:servempid ";
        try{
            session = HibernateUtil.getSessionFactory().openSession();            
            List result = session
                            .createQuery(strQuery)
                            .setParameter("cuentaid", cuenta)
                            .setParameter("servempid", servemp)
                            .list();            
            Iterator iter = result.iterator();
            BeanCuenta beancuenta = null;
            if(iter.hasNext()){
                Object[] al_cuenta = (Object[])iter.next();
                beancuenta = new BeanCuenta();
                beancuenta.setM_NumeroCuenta((String) al_cuenta[0]);
                beancuenta.setM_ServEmp(al_cuenta[1].toString());
                beancuenta.setM_TipoCuenta((String) al_cuenta[2]);
                beancuenta.setM_Descripcion( (String) al_cuenta[3]);
                beancuenta.setM_Estado(al_cuenta[4].toString());
                beancuenta.setM_Moneda((String) al_cuenta[5]);
            }
            result = null;
            return beancuenta;
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
            return null;
        }finally{
        	session.close();
        }
    }
	
	public String getTipoCuenta(String numCta, long idServxEmp) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();            
             List result = session.createQuery("select ta.dcsemTipoCuenta from TaCuentasServicioEmpresa ta where ta.id.dcsemNumeroCuenta = :numCta and ta.id.csemIdServicioEmpresa = :idSevxEmp ")
                        .setParameter("numCta", numCta)
                        .setParameter("idSevxEmp", idServxEmp)
                        .list();        
                Iterator iter = result.iterator();
                String tipoCta =null;                
                if (iter.hasNext()) {
                    tipoCta = (String) iter.next();
                }
                return tipoCta;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
    }
}

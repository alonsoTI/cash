/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.hiper.cash.dao.TxListFieldDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TxListField;
import com.hiper.cash.util.Constantes;

/**
 *
 * @author jwong
 */
public class TxListFieldDaoHibernate implements TxListFieldDao{

    private static Logger logger = Logger.getLogger(TxListFieldDaoHibernate.class);
    
    public List selectListFieldByFieldName(String dlfFieldName) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();            
            List result = session.createQuery("from TxListField where id.dlfFieldName = ? order by dlfDescription ").setString(0, dlfFieldName).list();
            return result;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
    }

    public TxListField selectListField(String idFieldName, String code) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            TxListField result = (TxListField) session.createQuery("from TxListField tx where tx.id.dlfFieldName = :fieldid and tx.id.clfCode = :codeid ")
                    .setParameter("fieldid", idFieldName)
                    .setParameter("codeid", code)
                    .uniqueResult();

            return result;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }
        finally{
        	session.close();
        }
    }
	//jwong 14/01/2009 para seleccionar solo el listado de codigos
    public List selectCodeListFieldByFieldName(String dlfFieldName) {
        List result;
        String strQuery = 
            " select distinct id.clfCode " +
            " from TxListField " +
            " where id.dlfFieldName = :fieldname";
        
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            

            result = session
                        .createQuery(strQuery)
                        .setParameter("fieldname", dlfFieldName)
                        .list();


            Iterator iter = result.iterator();

            ArrayList alresult = new ArrayList();
            while(iter.hasNext()){
                alresult.add((String) iter.next());
            }
            result = null;
            return alresult;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
        
    }
    


    //jwong 30/04/2009 para manejo de los tipos de cuentas en caso de CTS y Planilla
    public List selectListFieldByFieldName3(String dlfFieldName, String tipo) {
        String condicion = "";
        if(Constantes.TX_CASH_SERVICIO_PAGO_CTS.equalsIgnoreCase(tipo)){
            condicion = " and dlfDescription like '%CTS%' ";
        }
        else{
            condicion = " and dlfDescription not like '%CTS%' ";
        }
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            List result = session.createQuery("from TxListField where id.dlfFieldName = ? and id.clfCode between '01' and '99' " + condicion + " order by dlfDescription ").setString(0, dlfFieldName).list();
            session.getTransaction().commit();
            return result;
        } catch (Exception e) {
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }        
    }

	@Override
	public Map<String, String> mapaMoneda() {
		Session session = null;
        try {        
            session = HibernateUtil.getSessionFactory().openSession();                        
            List result = session.createQuery("select new Map(tx.id.clfCode,tx.dlfDescription) from TxListField tx where tx.id.dlfFieldName = ? ").setString(0, Constantes.FIELD_CASH_TIPO_MONEDA).list();
            return (Map<String, String>) result.get(0);            
        } catch (Exception e) {
        	System.out.println(e);
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
	}
	
	
	
	@Override
	public List<TxListField> listaMoneda() {
		Session session = null;
        try {        
            session = HibernateUtil.getSessionFactory().openSession();                        
            List result = session.createQuery("select tx from TxListField tx where tx.id.dlfFieldName = ? ").setString(0, Constantes.FIELD_CASH_TIPO_MONEDA).list();
            return result;        
        } catch (Exception e) {
        	System.out.println(e);
            logger.error(e.toString(),e);
            return null;
        }finally{
        	session.close();
        }
	}	
	
}
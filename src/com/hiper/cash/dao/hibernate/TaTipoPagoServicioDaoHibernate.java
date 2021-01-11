/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaTipoPagoServicioDao;
import java.util.List;
import org.hibernate.Session;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.entidad.BeanTipoPagoServicio;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.log4j.Logger;
/**
 *
 * @author esilva
 */
public class TaTipoPagoServicioDaoHibernate implements TaTipoPagoServicioDao{

    private static Logger logger = Logger.getLogger(TaTipoPagoServicioDaoHibernate.class);

    public List<BeanTipoPagoServicio> select(long idservemp) {
        Session session = null;
        List result;
        String strQuery =
                " select ta.id.ctpsidTipoPagoServicio, ta.id.csemIdServicioEmpresa, ta.id.ctpaIdTipoPago, ta.tmTipoPago.dtpaDescripcion " +
                " from TaTipoPagoServicio ta " +
                " where ta.id.csemIdServicioEmpresa = :servempid";
        try{
            session = HibernateUtil.getSessionFactory().openSession();            
            result = session.createQuery(strQuery)
                        .setParameter("servempid", idservemp).list();            
            Iterator iter = result.iterator();
            BeanTipoPagoServicio btps;
            ArrayList alresult = new ArrayList();
            while (iter.hasNext()) {
                Object[] al_tipopago = (Object[]) iter.next();
                btps = new BeanTipoPagoServicio();
                btps.setM_TipoPagoServicio( (String) al_tipopago[2]);
                btps.setM_Servicio( al_tipopago[1].toString());
                btps.setM_Descripcion((String) al_tipopago[3]);
                alresult.add(btps);
                btps = null;
            }
            result = null;
            return alresult;
        }catch(Exception ex){
            logger.error(ex.toString(),ex);
            return null;
        }finally{
        	session.close();
        }
    }
}

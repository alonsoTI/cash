/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaAprobacionOrdenDao;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.entidad.BeanAprobador;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author esilva
 */
public class TaAprobacionOrdenDaoHibernate implements TaAprobacionOrdenDao{

    private static Logger logger = Logger.getLogger(TaAprobacionOrdenDaoHibernate.class);

    public boolean insert(long orden, long servicio, long aprobador,String idUsuario,StringBuilder mensaje){
        boolean bResponse = false;
        Session session = null;        
        TaOrdenDao dao = new TaOrdenDaoHibernate();        
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            int numAprobaciones, numAprobadores = 0;
            Long lcont = null;
            Integer cont = null;            
            List result = session
                            .createQuery("select count(tao.id.caoidAprobacion) from TaAprobacionOrden tao where tao.id.caoidOrden =:ordenid and tao.id.caoidServEmp =:servicioid")
                            .setLong("ordenid", orden)
                            .setLong("servicioid", servicio)
                            .list();
            Iterator iter = result.iterator();
            if(iter.hasNext())
                lcont = (Long)iter.next();
            if (lcont == null || !(lcont.longValue() > 0))
                lcont = 0L;
            numAprobaciones = lcont.intValue();
            cont = null;
            List result2 = session
                            .createQuery("select ta.nsemNumeroAprobadores from TaServicioxEmpresa ta where ta.csemIdServicioEmpresa =:servicioid")
                            .setLong("servicioid", servicio)
                            .list();
            Iterator iter2 = result2.iterator();
            if(iter2.hasNext())
                cont = (Integer)iter2.next();
            if (cont == null || !(cont.intValue() > 0))
                cont = 0;
            numAprobadores = cont.intValue();
            if (numAprobadores > 0){
                String insert ="insert into taAprobacionOrden (cAOIdOrden, cAOIdServEmp, cAOIdAprobador, fAOFechaAprobacion,cAOIdUsuario,hOrHoraRegistro) values(?,?,?,?,?,?)";
                Query queryObject = session.createSQLQuery(insert);
                queryObject.setLong(0, orden);
                queryObject.setLong(1, servicio);
                queryObject.setLong(2, aprobador);
                queryObject.setString(3, Fecha.getFechaActual("yyyyMMdd"));
                queryObject.setString(4, idUsuario);
                queryObject.setString(5, Fecha.getFechaActual("HHmmss"));
                queryObject.executeUpdate();
                if ( numAprobaciones == 0 && numAprobadores > 1){
                    TaOrden ta = (TaOrden) session
                                .createQuery("from TaOrden ta where ta.id.corIdServicioEmpresa =:servicioid and ta.id.corIdOrden =:ordenid")
                                .setParameter("servicioid", servicio)
                                .setParameter("ordenid", orden)
                                .uniqueResult();
                    ta.setCorEstado(Constantes.HQL_CASH_ESTADO_ORDEN_PENDAUTO);
                    session.saveOrUpdate(ta);
                }else if( numAprobadores - numAprobaciones == 1){
                    TaOrden ta = (TaOrden) session
                                .createQuery("from TaOrden ta where ta.id.corIdServicioEmpresa =:servicioid and ta.id.corIdOrden =:ordenid")
                                .setParameter("servicioid", servicio)
                                .setParameter("ordenid", orden)
                                .uniqueResult();
                    ta.setCorEstado(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
                    session.saveOrUpdate(ta);
                    
                    dao.updateOrdenRep(servicio, orden, String.valueOf(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO));
                }
                bResponse = true;
            }else{
                mensaje.append("true");
            }
            session.getTransaction().commit();
        }catch(Exception e){
            logger.error(e.toString(),e);
            return false;
        }
        finally{
        	session.close();
        }
        return bResponse;
    }
    

	public  List selectAprobadores(long orden, long idServicioEmpresa){    	
   	 Session session = null;        
        List result;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createSQLQuery("EXEC spu_aprobadores_orden :codigo, :servemp")
            .setParameter("codigo", orden).setParameter("servemp", idServicioEmpresa);    
            result = query.list();             
            session.getTransaction().commit();                
            Iterator iter = result.iterator();            
            BeanAprobador beanAprobador;                       
            ArrayList alresult = new ArrayList();
            while(iter.hasNext()){           	 
                Object[] al_servicio = (Object[])iter.next();              
                beanAprobador = new BeanAprobador();
                beanAprobador.setM_IdOrden(al_servicio[0].toString());
                beanAprobador.setM_servicio(al_servicio[1].toString());
                beanAprobador.setM_fecha(al_servicio[2].toString());
                beanAprobador.setM_hora(al_servicio[3].toString());
                beanAprobador.setM_aprobador(al_servicio[4].toString());
                alresult.add(beanAprobador);
                beanAprobador = null;
            }
            result = null;
            return alresult;
        }
        catch(Exception ex){
            logger.error(ex.toString());
            return new ArrayList();
        }finally{
        	session.close();
        }
   }



}

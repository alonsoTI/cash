/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.dao.hibernate;

import com.hiper.cash.dao.TaEntidadEmpresaDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TaEntidadEmpresa;
import com.hiper.cash.domain.TaEntidadEmpresaId;
import com.hiper.cash.domain.TpDetalleOrden;
import com.hiper.cash.domain.TpDetalleOrdenId;
import com.hiper.cash.util.Constantes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 *
 * @author esilva
 */
public class TaEntidadEmpresaDaoHibernate implements TaEntidadEmpresaDao {

    private static Logger logger = Logger.getLogger(TaEntidadEmpresaDaoHibernate.class);

    public List selectEntidadEmpresa(String empresa, long servicio, String tipoEntidad) {
        String strQuery;
        Session session = null;
        try {           
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            List result = session.getNamedQuery(Constantes.QRY_GET_ENTIDAD_EMPRESA)
                                 .setParameter("empresaid", empresa)
                                 .setParameter("servicioid", servicio)
                                 .setParameter("tipoentidad", tipoEntidad)
                                 .list();
            session.getTransaction().commit();

            Iterator iter = result.iterator();
            
            TpDetalleOrden beandetorden;
            ArrayList alresult = new ArrayList();
            while (iter.hasNext()) {
                Object[] al_detorden = (Object[]) iter.next();
                beandetorden = new TpDetalleOrden();
                //KEY
                beandetorden.setId(new TpDetalleOrdenId());
                //Columns
                beandetorden.setNdodocumento((String) al_detorden[0]);//Column(3) - Numero de Documento
                beandetorden.setDdonombre((String) al_detorden[1]);//Column(5) - Descripcion Nombre
                beandetorden.setNdonumeroCuenta((String) al_detorden[2]);//Column(9) - Numero de Cuenta
                beandetorden.setDdotipoCuenta((String) al_detorden[3]);//Column(10) - Descripcion Tipo de Cuenta - {10, 20,40, AHO, CON, CTE}
                beandetorden.setNdomonto((al_detorden[4]== null)?new BigDecimal("0.0").setScale(2):new BigDecimal(al_detorden[4].toString()));//Column(11) - Monto
                beandetorden.setCdomoneda((String) al_detorden[5]);//Column(12) - Codigo Moneda - {PEN, USD}
                beandetorden.setDdotelefono((String) al_detorden[6]);//Column(13) - Descripcion de Telefono
                beandetorden.setDdoemail((String) al_detorden[7]);//Column(14) - Descripcion de Email
                beandetorden.setDdodescripcion((String) al_detorden[8]);//Column(15) - Descripcion de Entidad
                beandetorden.setDdotipoPago((String) al_detorden[9]);//Column(17) - Descripcion Tipo de Pago - {CCI, CHG, CRE, CTA, EFE, REC}
                beandetorden.setDdotipoDocumento((String) al_detorden[19]);//Column(18) - Descripcion Tipo de Documento - {Carnet Ext., DNI, RUC}
                //Adicional
                beandetorden.setIdEntidad((String) al_detorden[11]);//Codigo EntidadEmpresa
                beandetorden.setIdTipo((String) al_detorden[13]);//Codigo Tipo de Entidad - {01, 02}
                beandetorden.setDdocontrapartida((al_detorden[14] == null)? "-" : al_detorden[14].toString());//jmoreno 18-01-10                
                beandetorden.setIdServicioEntidad(al_detorden[15].toString());//Codigo Servicio Entidad
                beandetorden.setDescTipoCuenta((String) al_detorden[16]);//Descripcion Tipo de Cuenta - {Cuenta de Ahorro, Cuenta Corriente, CTS,...}
                beandetorden.setDescTipoPago((String) al_detorden[17]);//Descripcion Tipo de Pago Servicio - {Credito Cuenta, Cheque Gerencia,...}
                beandetorden.setDescTipo((String) al_detorden[18]);//Descripcion Tipo Entidad - {Empleado, Proveedor}
                beandetorden.setDescTipoMoneda((String) al_detorden[19]);//Descripcion Tipo Moneda - {S/., US$}
                beandetorden.setIdTipoDocumento((String) al_detorden[20]);//Codigo Tipo Documento - {01, 02, 03}
                
                if(Constantes.FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR.equalsIgnoreCase((String)al_detorden[13])){
                    beandetorden.setFlag_isproveedor("SI");
                }
                else{
                    beandetorden.setFlag_isproveedor("NO");
                }
                
                alresult.add(beandetorden);
                beandetorden = null;
            }
            result = null;
            return alresult;
        } catch (Exception e) {
            logger.error(e.toString(),e);
        }finally{
        	session.close();
        }
        return null;
    }

    //jwong 22/01/2009 para el mantenimiento de proveedores y de personal
    public List selectEntidadEmpresaByTipo(String idServEmp, String tipoEntidad){
        ArrayList alresult = null;
        Session session = null;
        try{
            /*
            String query =
                " select ee.ceeidEntidad, ee.cemIdEmpresa, ee.deenombre, " +
                "        ee.deeemail, ee.deetelefono, ee.deedescripcion, ee.deetipo, " +
                "        ee.ceetipoPago, ee.ceetipoDocumento, ee.neenumDocumento, lf1.dlfDescription, lf2.dlfDescription " +
                " from TaEntidadEmpresa ee " +
                " left outer join TxListField lf1 with (lf1.id.dlfFieldName = :fieldTipoPago and ee.ceetipoPago = lf1.id.clfCode ) " +
                " left outer join TxListField lf2 with (lf2.id.dlfFieldName = :fieldTipoDoc and ee.ceetipoDocumento = lf2.id.clfCode ) " +
                " where ee.id.cemIdEmpresa = :idempresa " +
                " and ee.deetipo = :idtipo ";
            */
            String select_prov = "";
            //si es tipo proveedor se debera incluir la columna tipo de pago para mostrar en consulta
            if(Constantes.FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR.equalsIgnoreCase(tipoEntidad)){
                select_prov = ",tpa.dTPaDescripcion "; //tipo de pago
            }
            

            String query =
                " select distinct ee.ceeidEntidad, ee.cEEIdServEmpresa, ee.deenombre, ee.deeemail, " +
                "        ee.deetelefono, ee.deedescripcion, ee.deetipo, " +
                "        ee.ceetipoDocumento, ee.neenumDocumento, lf2.dlfDescription as descTipoDoc,ee.cemIdEmpresa " +
                select_prov +
                " from TaEntidadEmpresa ee " +
                " left outer join TxListField lf2 on( " +
                "   ee.ceetipoDocumento = lf2.clfCode " +
                "   and lf2.dlfFieldName = :fieldTipoDoc " +
                " ) " +
                //jwong 10/03/2009 para mostrar la forma de pago
                " left outer join TaServicioEntidad sen on( " +
                "   sen.cSEnIdEntidad = ee.ceeidEntidad " +
                "   and sen.cSEnIdServEmp = ee.cEEIdServEmpresa " +
                "   and sen.cSEnTipoEntidad = ee.deetipo " +
                //"   and sen.cSEnEstado = :idestado " +
                " ) " +
                " left outer join TmTipoPago tpa on( " +
                "   sen.cSETipoPago = tpa.cTPaIdTipoPago " +
                " ) " +

                " where ee.cEEIdServEmpresa  = :idServEmp " +
                " and ee.deetipo = :idtipo ";

            session = HibernateUtil.getSessionFactory().openSession();            

            List result = session.createSQLQuery(query)
                                .setParameter("fieldTipoDoc", Constantes.FIELD_CASH_TIPO_DOCUMENTO)
                                //.setParameter("idestado", Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO) //debe estar habilitado el servicio
                                .setParameter("idServEmp", idServEmp)
                                .setParameter("idtipo", tipoEntidad)
                                .list();
            
            Iterator iter = result.iterator();

            TaEntidadEmpresa beanEntidad = null;
            TaEntidadEmpresaId id = null;
            alresult = new ArrayList();
            while(iter.hasNext()){
                Object[] al_detorden = (Object[])iter.next();
                beanEntidad = new TaEntidadEmpresa();

                //LLAVE
                id = new TaEntidadEmpresaId();
                id.setCeeidEntidad((String)al_detorden[0]);
                id.setCeeidServEmpresa(Long.parseLong(al_detorden[1].toString()));
                id.setDeetipo((String)al_detorden[6]);

                beanEntidad.setId(id);
                beanEntidad.setDeenombre((String)al_detorden[2]);
                beanEntidad.setDeeemail((String)al_detorden[3]);
                beanEntidad.setDeetelefono((String)al_detorden[4]);
                beanEntidad.setDeedescripcion((String)al_detorden[5]);                
                beanEntidad.setCeetipoDocumento((String)al_detorden[7]);
                beanEntidad.setNeenumDocumento((String)al_detorden[8]);
                beanEntidad.setDescripcionTipoDocumento((String)al_detorden[9]);
                beanEntidad.setCemIdEmpresa((String)al_detorden[10]);
                //si es tipo proveedor se debera incluir la columna tipo de pago para mostrar en consulta
                if(Constantes.FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR.equalsIgnoreCase(tipoEntidad)){
                    beanEntidad.setDescripcionTipoPago((String)al_detorden[11]);
                }                
                alresult.add(beanEntidad);
                beanEntidad = null;
            }
            result = null;
        }catch(Exception e){
            logger.error(e.toString(),e);
        }finally{
        	session.close();
        }
        
        return alresult;
    }

    public boolean insert(TaEntidadEmpresa taEntEmp) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(taEntEmp);
            session.getTransaction().commit();
        } catch (Exception ex) {
            logger.error(ex.toString(),ex);
            return false;
        }finally{
        	session.close();
        }
       
        return true;
    }

    public TaEntidadEmpresa selectEntidadEmpresa(String idServEmp, String idEntidad, String type){
        Session session = null;
        String query =  " from TaEntidadEmpresa " +
                        " where id.ceeidServEmpresa = :idServEmp " +
                        " and id.ceeidEntidad = :identidad " +
                        " and id.deetipo = :idtipo ";
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            TaEntidadEmpresa result = (TaEntidadEmpresa)session.createQuery(query)
                                .setParameter("idServEmp", Long.parseLong(idServEmp))
                                .setParameter("identidad", idEntidad)
                                .setParameter("idtipo", type)
                                .uniqueResult();
            session.getTransaction().commit();
            return result;
        } catch (Exception ex) {
            logger.error(ex.toString(),ex);
            return null;
        }finally{
        	session.close();
        }
    }

    //jwong 12/02/2009 para eliminar
    public boolean delete(TaEntidadEmpresa taEntEmp) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(taEntEmp);
            session.getTransaction().commit();
        } catch (Exception ex) {
            logger.error(ex.toString(),ex);
            return false;
        }finally{
        	session.close();
        }
        return true;
    }
}
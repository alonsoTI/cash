



package com.hiper.cash.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hiper.cash.domain.TpDetalleOrden;
import com.hiper.cash.entidad.BeanDetalleImporteEstado;
import com.hiper.cash.entidad.BeanDetalleOrden;
import com.hiper.cash.entidad.BeanPaginacion;


/**
 *
 * @author esilva
 */
public interface TpDetalleOrdenDao {
    

    

    public abstract List select(String servicio, String idorden, List estado,BeanPaginacion bpag);

    public abstract List selectOrdCan(String servicio, String idorden, List estado,BeanPaginacion bpag) throws Exception;

    //Detalle Ordenes Pago o Cobro
    public abstract List selectDetallePago(String servicio, String idorden,BeanPaginacion bpag); // jmoreno 31-07-09
    
    public abstract List selectDetallePago(String servicio, String idorden);
    
    //Detalle Ordenes Transferencia
    public abstract List selectDetalleTransferencia(String servicio, String idorden,BeanPaginacion bpag);//jmoreno 31-07-09
    
    public abstract List selectDetalleTransferencia(String servicio, String idorden);
    
    
    public abstract boolean update(String empresa, String servicio, String idorden, Map mapmontos, Map mapestados,
                                                 Map map_cuentas, Map map_nombres, Map map_telefs, Map map_emails,
                                                 Map map_tipcuentas, Map map_nrodocumentos,BeanPaginacion bpag);

    //Modificado por Grov 02/06/2010


    //jyamunaque 22/02/2011 Cambia estado de los items de una orden cuando se desea cancelar 
    public abstract boolean change_state_items(List lstIdDtlOrd, List lstIdOrd, List lstServEmp, Character state);
    
    //Fin Modificado por Grov 02/06/2010
    public abstract boolean insert(List listadetorden, Map montos);

    public Map<String,BigDecimal> registrar(List<TpDetalleOrden> lista) throws Exception;
    

    public Map<String,BigDecimal> registrar(TpDetalleOrden detalle) throws Exception;
    
    void registrarReplica(TpDetalleOrden detalle) throws Exception;
    //void registrarReplica(List<TpDetalleOrden> detalle) throws Exception;
    long obtenerIdDetalleOrden(TpDetalleOrden detalle) throws Exception;

    	
	//jwong 21/01/2009 detalle de los importes de las ordenes por estado
    public abstract BeanDetalleImporteEstado selectDetImporteXEstado(String servicio, String idorden);
   
    //jmoreno - insert detalle letras
     public boolean insert_2(List listadetorden, Map montos);
     //Insert Detalle Orden Tipo Transferencias
     public boolean insertTransferencia(List listadetorden, Map montos);
     //jmoreno - Cantidad Detalles de una Orden Cancelada
     public abstract long obtenerCantItems(String idOrden,List estadosxCancelar) ;
     //jmoreno 25-08-09 
     public long obtenerCantItemsCobro(String rucEmpresa,List criterios);    
     
     List<BeanDetalleOrden> selectDetallePagoReferenciaTrx(long orden,long servicio,String referencia,int inicio,int maximo) throws Exception;
     int cuentaDetallePagoReferenciaTrx(long orden, long servicio,String referencia) throws Exception;
     List<BeanDetalleOrden> selectDetallePagoReferenciaNoTrx(long orden,long servicio,String referencia,int inicio,int maximo) throws Exception;
     int cuentaDetallePagoReferenciaNoTrx(long orden, long servicio,String referencia) throws Exception;
     
     
     List<Object[]> seleccionarMovimientosNuevosCobros(String idServEmp, String estado,
 			String fecini, String fecfin, String referencia, String contrapartida,
 			BeanPaginacion bpag
 			);               
          
     List ejecutarConsultaMovimientosExportacion(String strQuery,
 			String idServEmp, String estado, String fecini, String fecfin,
 			String referencia, String contrapartida, String formato, boolean addOrderBy);
     
     List<Object[]> obtenerMontosMovimientosNuevosCobros(String idServEmp,
 			String estado, String fecini, String fecfin, String referencia,
 			String contrapartida);
     
     TpDetalleOrden getTpDetalleOrden(long idOrden,long idServicioEmpresa,long idItemDetalle);
     List<TpDetalleOrden> listaTpDetalleOrden(long idOrden,long idServicioEmpresa);
     
     //agregadas por Andy
     public void actualizarItemsConsultas(List lstIdDtlOrd, List lstIdOrd, List lstServEmp, Character state);
     
     int obtenerCantidadMovimientos(String idServEmp,
 			String estado, String fecini, String fecfin, String referencia,
 			String contrapartida);
      
    
}
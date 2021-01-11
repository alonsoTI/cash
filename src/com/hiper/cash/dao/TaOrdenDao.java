/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.entidad.BeanOrden;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.BeanTotalesConsMov;

/**
 *
 * @author esilva
 */
public interface TaOrdenDao {
    public List select(String empresa, List servicio, List estado);
    public BeanOrden select(String idorden, String idservemp);
    public boolean delete(Map ordenes, char estado_out, List elim);
    public boolean insert(TaOrden taorden);
    boolean insertReplica(TaOrden taorden);
    
    public boolean update(long orden, long servicio, BigDecimal montoSoles, BigDecimal montoDolares, BigDecimal montoEuros, int num);

    public abstract List selectOrdenesPendAprobacion(List servicio, List user);
    public abstract List selectOrdenesPendAprobacion(List servicio, String idUser);
    public abstract List selectOrdenesPendCobro(String proveedor, String empresa, List criterios, BeanPaginacion bpag);    
    //jwong 14/01/2009 consulta de ordenes
        
 
    //jmoreno 25-08-09
    public String getCodigosRptaIbs(String idOrden);
    public abstract List getConsultaOrdEstado(String idServEmp,String estado, String fecini, String fecfin, String referencia,
            String contrapartida, BeanTotalesConsMov beanTotales);

    //fin agregado por grov 27/05/2010
    public String obtenerTipoOrden(String orden,String servicio);
    
    public String obtenerFechaOrden(String idOrden,String idServEmp) throws Exception; 		
    	
  //agregado por Andy Quiñonez
    public boolean cancelarOrden(int idOrden, String idServEmp);
    
	public String validarCancelarOrden(String idOrden, String idServEmp);
	

	TaOrden getOrdenRep(long idOrden,long idServicioEmpresa)throws Exception; 
	
	List<BeanOrden> buscarOrdenesReferencia(List<Long> servicios,String estado,String fechaInicio,String fechaFin,String referencia,int inicio,int nroRegistros) throws Exception;
	int contarOrdenesReferencia(List<Long> servicios,String estado,String fechaInicio,String fechaFin,String referencia) throws Exception;
	
	List<BeanOrden> buscarOrdenesReferenciaTrx(List<Long> servicios,String estado,String fechaInicio,String fechaFin,String referencia,int inicio,int nroRegistros) throws Exception;
	int contarOrdenesReferenciaTrx(List<Long> servicios,String estado,String fechaInicio,String fechaFin,String referencia) throws Exception;
	
	List<BeanOrden> buscarOrdenesReferenciaNoTrx(List<Long> servicios,String estado,String fechaInicio,String fechaFin,String referencia,int inicio,int nroRegistros) throws Exception;
	int contarOrdenesReferenciaNoTrx(List<Long> servicios,String estado,String fechaInicio,String fechaFin,String referencia) throws Exception;

	
	//agregadas por Andy
	public void ejecutaBuzonesID(long idenvio, long idOrden, long idServEmp);
	
	public void guardaTransfConsultas(long idServEmp,long idOrden, long idDetalle);
	
	public void deleteOrdenRep(Map ordenes, char estado_out, List elim);
	
	public void updateOrdenRep(long idServEmp,long idOrden, String estado);
	
	public void deleteRep(List ordenes, char estado_out);
	
	public void guardaLetrasConsultas(long idServEmp,long idOrden);
}
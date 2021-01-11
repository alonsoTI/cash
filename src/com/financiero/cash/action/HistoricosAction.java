package com.financiero.cash.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.financiero.cash.component.ui.Paginado;
import com.financiero.cash.delegate.HistoricosDelegate;
import com.financiero.cash.form.HistoricosForm;
import com.financiero.cash.ui.model.DetalleOrdenEC;
import com.financiero.cash.ui.model.EmpresaEC;
import com.financiero.cash.ui.model.OrdenHistoricoEC;
import com.financiero.cash.ui.model.ServicioEC;
import com.hiper.cash.util.Util;

public class HistoricosAction extends DispatchAction {
	
	private HistoricosDelegate delegado = HistoricosDelegate.getInstance();
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static final String ATR_ORDENES = "ordenes";
	private static final String ATR_DETALLES_ORDEN = "detallesOrden";
	
	public ActionForward cargarEmpresas(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {    	
		String nroTarjeta="";
		StringBuilder sb = new StringBuilder();
		try {
	    	HttpSession session = request.getSession();
	    	nroTarjeta=(String) session.getAttribute("tarjetaActual");			
			List<EmpresaEC> empresas =  delegado.getEmpresasEC(nroTarjeta);
			if( empresas.size() == 0 ){
				sb = new StringBuilder("La Tarjeta Nro ").append(nroTarjeta).append(" no esta afiliada a empresa alguna");
				logger.error(sb.toString());
				request.setAttribute("mensaje", sb.toString());
				return mapping.findForward("error");
			}
			HistoricosForm hf = (HistoricosForm)form;
			hf.setFechaFinal(Util.strFecha());
			hf.setFechaInicial(Util.strFecha());
			request.getSession().setAttribute("empresas", empresas);			
		} catch (Exception e) {			
			sb = new StringBuilder("La Tarjeta Nro ").append(nroTarjeta).append(" no esta afiliada a empresa alguna");
			request.setAttribute("mensaje", sb.toString());
			logger.error("Cargar Empresas",e);
			return mapping.findForward("error"); 
		}
		return mapping.findForward("cargarEmpresas");		
	}
	
	public ActionForward cargarServicios(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {		
		  String idempresa = request.getParameter("empresaId");
          List<ServicioEC> servicios = null;
          try{
        	  servicios =  delegado.getServiciosEC(idempresa);
        	  request.getSession().setAttribute("servicios", servicios);        	 
          }catch(Exception e){
        	  logger.error("Cargar Servicios",e);
        	  request.setAttribute("mensaje", "El sistema no puede procesar la informacion");

        	  return mapping.findForward("error");        	 
          }
          return mapping.findForward("cargarEmpresas");	
	}
	
	public  ActionForward cargarOrdenes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
				
		HistoricosForm hf =  (HistoricosForm) form;
		String empresa = hf.getEmpresaId();
		String servicio = hf.getServicioId();
		
		String strInicio = hf.getFechaInicial();
		String strFinal = hf.getFechaFinal();
		
		Date fFinal = Util.fecha(strFinal);
		Date fInicio= Util.fecha(strInicio);
		
		
		String referencia = hf.getReferencia();
		try{
			hf.setEmpresa(delegado.nombreEmpresa(Integer.valueOf(empresa)));
			hf.setServicio(delegado.nombreServicio(Integer.valueOf(servicio)));
			Paginado<OrdenHistoricoEC> paginado = getPaginadoOrdenes(20, empresa, servicio, fInicio, fFinal, referencia);
			request.getSession().setAttribute("paginado", paginado);
			request.setAttribute(ATR_ORDENES, paginado.getItemsPagina());			
		}catch(Exception e){
			logger.error("CARGAR ORDENES", e);
      	  	request.setAttribute("mensaje", "El sistema no puede procesar la informacion");
			return mapping.findForward("error");	
		}
		return mapping.findForward("paginaOrdenes");	
	}
	
	public  ActionForward paginarOrdenes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try{
			Paginado<OrdenHistoricoEC> paginado =  (Paginado<OrdenHistoricoEC>) request.getSession().getAttribute("paginado");
			int nroPagina = Integer.valueOf(request.getParameter("nroPagina"));
			paginado.setNroPagina(nroPagina);
			request.setAttribute(ATR_ORDENES, paginado.getItemsPagina());	
		}catch(Exception e){
			logger.error("PAGINAR ORDENES", e);
      	  	request.setAttribute("mensaje", "El sistema no puede procesar la informacion");
			return mapping.findForward("error");	
		}
		return mapping.findForward("paginaOrdenes");	
	}
	

	public  ActionForward exportarOrdenes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try{
			int accion = Integer.valueOf(request.getParameter("accion"));
			Paginado<OrdenHistoricoEC> paginado =  (Paginado<OrdenHistoricoEC>) request.getSession().getAttribute("paginado");
			if( accion == 2){
				String nombre_archivo = "OrdenHistorico-" + Util.strFecha()+ ".txt";
				response.setHeader("Content-Disposition", "attachment; filename=\""	+ nombre_archivo + "\"");				
				response.setContentType("text/plain");				
				String texto = delegado.genearArchivoOrdenHistoricoEC(paginado.getItems());
				PrintWriter out = new PrintWriter(response.getOutputStream());
				out.println(texto);
				out.flush();
				out.close();
				response.getOutputStream().flush();
				response.getOutputStream().close();
				return null;
			}
			else if( accion ==  1 ){							
				String fecha = Util.strFecha();
				String nombre_archivo = "OrdenHistorico_" + fecha	+ ".xls";
				response.setHeader("Content-Disposition", "attachment; filename=\""+ nombre_archivo + "\"");
				response.setContentType("application/vnd.ms-excel");
				
				HSSFWorkbook libroXLS = delegado.generarExcelOrdenHistoricoEC("OrdenHistorico",paginado.getItems()); 
				if (libroXLS != null) {
					libroXLS.write(response.getOutputStream());
					response.getOutputStream().close();
					response.getOutputStream().flush();
				}
				return null;
			}else{				
				request.setAttribute(ATR_ORDENES, paginado.getItems());
				return mapping.findForward("exportaOrdenes");
			}
				
		}catch(Exception e){
			logger.error("EXPORTAR ORDENES", e);
      	  	request.setAttribute("mensaje", "El sistema no puede procesar la informacion");
			return mapping.findForward("error");	
		}
			
	}
	
	private Paginado<OrdenHistoricoEC> getPaginadoOrdenes(int nro,final String empresa,final String servicio,
			final Date fInicio, final Date fFinal,final String referencia){
		Paginado<OrdenHistoricoEC> paginado = new Paginado<OrdenHistoricoEC>(nro) {

			@Override
			public List<OrdenHistoricoEC> getItemsPagina() {
				try{
					return delegado.getOrdenes(empresa, servicio, fInicio, fFinal, referencia, this.getItemInicioPagina(), this.getNroItemsPagina());
				}catch(Exception e){
					logger.error("PAGINADO ORDENES - ITEMS PAGINA", e);
					return new ArrayList<OrdenHistoricoEC>();
				}				
			}

			@Override
			public int getNroTotalItems() {
				try{
					return delegado.cuentaOrdenes(empresa, servicio, fInicio, fFinal, referencia);
				}catch(Exception e){
					logger.error("PAGINADO ORDENES -  NRO TOTAL ITEMS", e);
					return 0;
				}				
			}
		};
		return paginado;
	}
	
	
	public  ActionForward cargarDetallesOrden(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		String strOrden = request.getParameter("idOrden");
		int orden = Integer.valueOf(strOrden);	
		HistoricosForm hf =  (HistoricosForm) form;
		String referencia = hf.getReferencia();
		try{			
			hf.setOrden(strOrden);
			Paginado<DetalleOrdenEC> paginado = getPaginadoDetallesOrden(20,orden, referencia);			
			
			request.getSession().setAttribute("paginado", paginado);
			if(  paginado.getNroTotalItems() > 10000 ){
				request.getSession().setAttribute("excel","true" );
			}else{
				request.getSession().setAttribute("excel", "false");
			}			
			request.setAttribute(ATR_DETALLES_ORDEN, paginado.getItemsPagina());			
		}catch(Exception e){
			logger.error("CARGAR ORDENES", e);
      	  	request.setAttribute("mensaje", "El sistema no puede procesar la informacion");
			return mapping.findForward("error");	
		}
		return mapping.findForward("paginaDetallesOrden");	
	}
			
	public  ActionForward paginarDetallesOrden(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try{
			Paginado<DetalleOrdenEC> paginado =  (Paginado<DetalleOrdenEC>) request.getSession().getAttribute("paginado");
			int nroPagina = Integer.valueOf(request.getParameter("nroPagina"));
			paginado.setNroPagina(nroPagina);
			request.setAttribute(ATR_DETALLES_ORDEN, paginado.getItemsPagina());	
		}catch(Exception e){
			logger.error("PAGINAR ORDENES", e);
      	  	request.setAttribute("mensaje", "El sistema no puede procesar la informacion");
			return mapping.findForward("error");	
		}
		return mapping.findForward("paginaDetallesOrden");	
	}
	
	public  ActionForward exportarDetallesOrden(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try{			
			int accion = Integer.valueOf(request.getParameter("accion"));
			Paginado<DetalleOrdenEC> paginado =  (Paginado<DetalleOrdenEC>) request.getSession().getAttribute("paginado");	
			request.setAttribute(ATR_DETALLES_ORDEN, paginado.getItems());
			if( accion == 2){
				String nombre_archivo = "DetallesOrden-" + Util.strFecha()+ ".txt";
				response.setHeader("Content-Disposition", "attachment; filename=\""	+ nombre_archivo + "\"");				
				response.setContentType("text/plain");				
				//String texto = delegado.genearArchivoOrdenHistoricoEC(paginado.getItems());
				String texto = delegado.generarArchivoDetallesOrdenEC(paginado.getItems());
				PrintWriter out = new PrintWriter(response.getOutputStream());
				out.println(texto);
				out.flush();
				out.close();
				response.getOutputStream().flush();
				response.getOutputStream().close();
				return null;
				
			}else if( accion ==  1 ){							
				String fecha = Util.strFecha();
				String nombre_archivo = "DetallesOrden_" + fecha	+ ".xls";
				response.setHeader("Content-Disposition", "attachment; filename=\""+ nombre_archivo + "\"");
				response.setContentType("application/vnd.ms-excel");
				
				HSSFWorkbook libroXLS = delegado.generarExcelDetallesOrdenEC("DetallesOrden",paginado.getItems()); 
				if (libroXLS != null) {
					libroXLS.write(response.getOutputStream());
					response.getOutputStream().close();
					response.getOutputStream().flush();
				}
				return null;
			}else{				
				request.setAttribute(ATR_ORDENES, paginado.getItems());
				return mapping.findForward("exportaDetallesOrden");
			}
		}catch(Exception e){
			logger.error("PAGINAR DETALLES ORDEN", e);
      	  	request.setAttribute("mensaje", "El sistema no puede procesar la informacion");
			return mapping.findForward("error");	
		}
		
	}
	
	private Paginado<DetalleOrdenEC> getPaginadoDetallesOrden(int nro,final int orden,final String referencia){
		Paginado<DetalleOrdenEC> paginado = new Paginado<DetalleOrdenEC>(nro) {

			@Override
			public List<DetalleOrdenEC> getItemsPagina() {
				try{
					return delegado.getDetallesOrden(orden, referencia, this.getItemInicioPagina(), this.getNroItemsPagina());
				}catch(Exception e){
					logger.error("PAGINADO DETALLES DE ORDEN-ITEMS", e);
					return new ArrayList<DetalleOrdenEC>();
				}				
			}

			@Override
			public int getNroTotalItems() {
				try{
					return delegado.cuentaDetallesOrden(orden, referencia);
				}catch(Exception e){
					logger.error("PAGINADO DETALLES DE ORDEN -  NRO TOTAL ITEMS", e);
					return 0;
				}				
			}
		};
		return paginado;
	}
}

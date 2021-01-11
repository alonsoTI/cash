package com.financiero.cash.action;

import static com.hiper.cash.util.CashConstants.TAMANIO_PAGINA_TRANSFERENCIAS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;

import com.financiero.cash.beans.ConsultaTransferenciasBean;
import com.financiero.cash.beans.TransferenciaBean;
import com.financiero.cash.delegate.TransferenciasDelegate;
import com.financiero.cash.form.consultas.TransferenciasForm;
import com.financiero.cash.util.AccionTransferencia;
import com.financiero.cash.util.VariablesRequest;
import com.financiero.cash.util.VariablesSession;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.xml.bean.BeanDataLoginXML;

public class ConsultarTransferenciasAction extends DispatchAction {
	
	private TransferenciasDelegate delegado =  TransferenciasDelegate.getInstance();
	private Logger logger =  Logger.getLogger(this.getClass());

	public ActionForward cargar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
		try{	
			String habil = request.getParameter("habil");
			if( habil != null ){
				if( habil.equals("0")){
					  return mapping.findForward("noPermiso");
				}
			}	 				
			String fechaActual = Fecha.getFechaActual("dd/MM/yyyy");			
			TransferenciasForm transferenciasForm = (TransferenciasForm)form;
			transferenciasForm.setFechaInicial(fechaActual);
			transferenciasForm.setFechaFinal(fechaActual);
			transferenciasForm.setEmpresa(obtenerCodigoEmpresa(request));
			return mapping.findForward("consultarTransferencias");
		}catch (Exception e) {
			logger.error(e, e);
			return mapping.findForward("error");
		}		
	}

	private BeanDataLoginXML obtenerUsuario(HttpSession session) {
		return (BeanDataLoginXML)session.getAttribute("usuarioActual");
	}
	
	public ActionForward volverConsultaTransferencias(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		    throws IOException, ServletException {		
		request.setAttribute("accion", "volverABusqueda");
		return mapping.findForward("consultarTransferencias");
	}
	
	public ActionForward cargarEmpresas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
		try{
			HttpSession session = request.getSession();		
			String habil = request.getParameter("habil");
			if( habil != null ){
				if( habil.equals("0")){
					  return mapping.findForward("noPermiso");
				}
			}	 			
			BeanDataLoginXML usuario =obtenerUsuario(session) ;
			TransferenciasForm transferenciasForm= (TransferenciasForm)form;			
			if( usuario.isM_usuarioEspecial()){
				List<LabelValueBean> empresas = (List<LabelValueBean>)session.getAttribute(VariablesSession.EMPRESAS_TRANSFERENCIAS.getDescripcion());
				if(empresas==null){
					empresas = delegado.getEmpresas();
					session.setAttribute(VariablesSession.EMPRESAS_TRANSFERENCIAS.getDescripcion(), empresas);
				}
				if (empresas != null && empresas.size() > 0) {
					transferenciasForm.setEmpresa(empresas.get(0).getValue());
				}
			}else{				
				transferenciasForm.setEmpresa((String) session.getAttribute(CashConstants.PARAM_ID_EMPRESA));
			}
			String fechaActual = Fecha.getFechaActual("dd/MM/yyyy");
			transferenciasForm.setFechaInicial(fechaActual);
			transferenciasForm.setFechaFinal(fechaActual);
			return mapping.findForward("consultarTransferencias");			
		}catch (Exception e) {
			logger.error(e, e);
			return mapping.findForward("error");
		}		
	}
	
	
	private boolean esCargaInicial(HttpServletRequest request){
		String esPag = request.getParameter("esPag");
		if(esPag==null){
			return true;
		}
		return false;
	}
	
	
	
	public ActionForward cargarTrxPendientes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
		try {
			HttpSession session = request.getSession();
			String habil = request.getParameter("habil");
			if( habil != null ){
				if( habil.equals("0")){
					  return mapping.findForward("noPermiso");
				}
			}			
			BeanPaginacion beanPaginacion = crearActualizarBeanPaginacion(request, session);
			BeanDataLoginXML usuario = obtenerUsuario(session);
			String empresa = (String) session.getAttribute(CashConstants.PARAM_ID_EMPRESA);
			ConsultaTransferenciasBean transferenciasPendientes = delegado.buscarTransferenciasPendientes(
					empresa, usuario.getM_Codigo(), beanPaginacion);
			if (esCargaInicial(request)) {
				if (transferenciasPendientes != null
						&& transferenciasPendientes.getTotalRegistrosEnviados() > 0) {					
					int totalRegistros = (int)transferenciasPendientes.getTotalRegistroConsulta();
					int tamanioPagina = beanPaginacion.getM_regPagina();
					int cantidadPaginas = totalRegistros / tamanioPagina;
                    int resto = (int) totalRegistros % tamanioPagina;
                    if (resto != 0) {
                        cantidadPaginas = cantidadPaginas + 1;
                    }
					beanPaginacion.setM_pagFinal(cantidadPaginas);
					beanPaginacion.setM_pagInicial(1);
					session.setAttribute(VariablesSession.PAGINACION_TRANSFERENCIAS_PENDIENTES.getDescripcion(), beanPaginacion);
				}
			}
			if(transferenciasPendientes!=null){
				request.setAttribute(VariablesRequest.TRANSFERENCIAS_PENDIENTES.getDescripcion(), transferenciasPendientes.getTransferencias());
			}
			return mapping.findForward("cargarTrxPendientes");
		} catch (Exception e) {
			logger.error(e, e);
			return mapping.findForward("error");
		}		
	}

	private BeanPaginacion crearActualizarBeanPaginacion(HttpServletRequest request, HttpSession session) {
		BeanPaginacion beanPaginacion = null;
		if(esCargaInicial(request)){//consulta la paginacion
			beanPaginacion = new BeanPaginacion();
			beanPaginacion.setM_regPagina(TAMANIO_PAGINA_TRANSFERENCIAS);
			beanPaginacion.setM_pagActual(1);	            
		}else{	        		        	
			beanPaginacion = (BeanPaginacion) session.getAttribute(
					VariablesSession.PAGINACION_TRANSFERENCIAS_PENDIENTES.getDescripcion());
		    String tipoPaginado = request.getParameter("tipoPaginado");
		    tipoPaginado = tipoPaginado == null ? "P" : tipoPaginado;
		    if ("P".equals(tipoPaginado)){
		    	beanPaginacion.setM_pagActual(1);
		    } else if ("U".equals(tipoPaginado)) {
		    	beanPaginacion.setM_pagActual(beanPaginacion.getM_pagFinal());
		    } else if ("S".equals(tipoPaginado)) {
		        if (beanPaginacion.getM_pagActual() < beanPaginacion.getM_pagFinal()) {
		        	beanPaginacion.setM_pagActual(beanPaginacion.getM_pagActual() + 1);
		        }
		    } else if ("A".equals(tipoPaginado)) {
		        if (beanPaginacion.getM_pagActual() > beanPaginacion.getM_pagInicial()) {
		        	beanPaginacion.setM_pagActual(beanPaginacion.getM_pagActual() - 1);
		        }
		    }
		    //session.setAttribute(VariablesSession.PAGINACION_TRANSFERENCIAS_PENDIENTES.getDescripcion(), beanPaginacion);
		}
		return beanPaginacion;
	}
	
	public ActionForward buscarTransferencias(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			HttpSession session = request.getSession();			
			String esPag = request.getParameter("esPag");
			BeanPaginacion beanPaginacion = null;
			if (esPag != null) {// consulta la paginacion
				beanPaginacion = (BeanPaginacion) session
						.getAttribute(VariablesSession.PAGINACION_CONSULTA_TRANSFERENCIAS.getDescripcion());
				String tipoPaginado = request.getParameter("tipoPaginado");
				tipoPaginado = tipoPaginado == null ? "P" : tipoPaginado;
				if ("P".equals(tipoPaginado)) {
					beanPaginacion.setM_pagActual(1);
				} else if ("U".equals(tipoPaginado)) {
					beanPaginacion.setM_pagActual(beanPaginacion.getM_pagFinal());
				} else if ("S".equals(tipoPaginado)) {
					if (beanPaginacion.getM_pagActual() < beanPaginacion.getM_pagFinal()) {
						beanPaginacion.setM_pagActual(beanPaginacion.getM_pagActual() + 1);
					}
				} else if ("A".equals(tipoPaginado)) {
					if (beanPaginacion.getM_pagActual() > beanPaginacion.getM_pagInicial()) {
						beanPaginacion.setM_pagActual(beanPaginacion.getM_pagActual() - 1);
					}
				}				
			} else {// se consulta por primera vez
				beanPaginacion = new BeanPaginacion();
				beanPaginacion.setM_regPagina(TAMANIO_PAGINA_TRANSFERENCIAS);
				beanPaginacion.setM_pagActual(1);
			}						
			TransferenciasForm transferenciasForm = (TransferenciasForm) form;			
			ConsultaTransferenciasBean transferencias = delegado.buscarTransferencias(transferenciasForm.getEmpresa(),
					transferenciasForm.getTipoTransferencia(), transferenciasForm.getDocumento(),
					transferenciasForm.getNroDocumento(), transferenciasForm.getEstado(),
					transferenciasForm.getMoneda(), transferenciasForm.getFechaInicialFormateadoYYYYMMDD(),
					transferenciasForm.getFechaFinalFormateadoYYYYMMDD(), beanPaginacion);			
			if (esPag == null) {
				if (transferencias != null && transferencias.getTotalRegistrosEnviados() > 0) {
					int totalRegistros = (int) transferencias.getTotalRegistroConsulta();
					int tamanioPagina = beanPaginacion.getM_regPagina();
					int cantidadPaginas = totalRegistros / tamanioPagina;
					int resto = (int) totalRegistros % tamanioPagina;
					if (resto != 0) {
						cantidadPaginas = cantidadPaginas + 1;
					}
					beanPaginacion.setM_pagFinal(cantidadPaginas);
					beanPaginacion.setM_pagInicial(1);
					session.setAttribute(
							VariablesSession.PAGINACION_CONSULTA_TRANSFERENCIAS.getDescripcion(),
							beanPaginacion);
				}
			}
			if (transferencias != null) {
				session.setAttribute(VariablesRequest.CONSULTA_TRANSFERENCIAS.getDescripcion(),
						transferencias.getTransferencias());
			}else{
				session.setAttribute(VariablesRequest.CONSULTA_TRANSFERENCIAS.getDescripcion(),
						new ArrayList());
			}			
			request.setAttribute("accion", "busqueda");
			return mapping.findForward("consultarTransferencias");
		} catch (Exception e) {
			logger.error(e, e);
			return mapping.findForward("error");
		}
	}
	
	public ActionForward aprobarTransferencia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		try {
			HttpSession session = request.getSession();
			BeanDataLoginXML usuario = obtenerUsuario(session);
			TransferenciaBean transferencia = (TransferenciaBean) session.getAttribute("trx");
			if (transferencia != null) {
				String idEmpresa = obtenerCodigoEmpresa(request);
				transferencia.setIdEmpresa(Long.valueOf(idEmpresa));
				int correlativoDiario = (Integer) session
						.getAttribute(VariablesSession.CORRELATIVO_DIARIO_TRANSFERENCIAS.getDescripcion());
				transferencia = delegado.aprobarTransferencia(transferencia, usuario.getM_Codigo(),
						AccionTransferencia.APROBAR, correlativoDiario);
				obtenerMensajesProcesoAprobacion(request, transferencia);
				if (transferencia.getCodigoError() == null) {
					
					//System.out.println("nro TRF="+transferencia.getNumero());
					
					transferencia = delegado.getTransferencia(transferencia.getNumero(), idEmpresa);
				}
				session.removeAttribute("trx");
				request.setAttribute("trx", transferencia);
				request.setAttribute("origen", "aprobacion");
				return mapping.findForward("verTrxActualizada");
				//return mapping.findForward("verTrxActualizadaAprobacion");
			} else {
				return mapping.findForward("iniciarOpciones");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return mapping.findForward("error");
		}
	}
	
	public ActionForward rechazarTransferencia(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
		try {
			HttpSession session = request.getSession();
			BeanDataLoginXML usuario = obtenerUsuario(session);
			TransferenciaBean transferencia = (TransferenciaBean) session.getAttribute("trx");
			if (transferencia != null) {
				String idEmpresa = obtenerCodigoEmpresa(request);
				transferencia.setIdEmpresa(Long.valueOf(idEmpresa));
				int correlativoDiario = (Integer) session
						.getAttribute(VariablesSession.CORRELATIVO_DIARIO_TRANSFERENCIAS.getDescripcion());
				transferencia = delegado.aprobarTransferencia(transferencia, usuario.getM_Codigo(),
						AccionTransferencia.RECHAZAR, correlativoDiario);
				obtenerMensajesProcesoRechazo(request, transferencia);
				if (transferencia.getCodigoError() == null) {
					transferencia = delegado.getTransferencia(transferencia.getNumero(), idEmpresa);
				}
				session.removeAttribute("trx");
				request.setAttribute("trx", transferencia);
				request.setAttribute("origen", "aprobacion");
				return mapping.findForward("verTrxActualizada");
			} else {
				return mapping.findForward("iniciarOpciones");
			}
		} catch (Exception e) {
			logger.error(e, e);
			return mapping.findForward("error");
		}		                       
	}
	
	public ActionForward buscarTrxPendiente(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
		try{
			HttpSession session = request.getSession();
			String id =  request.getParameter("idTransferencia");
			String idEmpresa = obtenerCodigoEmpresa(request);
			session.setAttribute("trx", delegado.getTransferencia(id,idEmpresa));
			session.setAttribute(VariablesSession.CORRELATIVO_DIARIO_TRANSFERENCIAS.getDescripcion(), delegado.obtenerCorrelativoTransferenciasDiarias());
			request.setAttribute("origen", "verPendiente");
			return mapping.findForward("verTrxPendiente");
		}catch(Exception e){
			logger.error(e, e);
			return mapping.findForward("error");
		}
	}
	
	public ActionForward buscarTransferencia(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
		try{
			HttpSession session = request.getSession();
			String id =  request.getParameter("idTransferencia");
			String idEmpresa = request.getParameter("idEmpresa");
			TransferenciaBean transferencia =delegado.getTransferencia(id,idEmpresa);
			transferencia.setIdEmpresa(Long.valueOf(idEmpresa));
			session.setAttribute("trx", transferencia);			
			request.setAttribute("origen", "verTrx");
			return mapping.findForward("verTrxPendiente");
		}catch(Exception e){
			logger.error(e, e);
			return mapping.findForward("error");
		}
	}
	
	private String obtenerCodigoEmpresa(HttpServletRequest request){
    	HttpSession session = request.getSession();
    	return (String) session.getAttribute(CashConstants.PARAM_ID_EMPRESA);
    }
	
	private void obtenerMensajesProcesoAprobacion(HttpServletRequest request, TransferenciaBean transferencia) {
		if (transferencia.getCodigoError() == null && transferencia.isProcesado()) {
			request.setAttribute("mensaje", "Transferencia procesada correctamente");
		} else if (transferencia.getCodigoError() == null && !transferencia.isProcesado()) {
			request.setAttribute("error", "Transferencia aprobada correctamente");
			request.setAttribute("error_descripcion", "Pendiente de aprobacion");
		} else if (transferencia.getCodigoError() != null && !transferencia.isProcesado()) {
			request.setAttribute("error", "Transferencia no aprobada");
			request.setAttribute("error_descripcion", transferencia.getMensajeError());
		} else if (transferencia.getCodigoError() != null && transferencia.isProcesado()) {
			request.setAttribute("error", "Transferencia No Procesada");
			request.setAttribute("error_descripcion", transferencia.getMensajeError());
		}
	}
	
	private void obtenerMensajesProcesoRechazo(HttpServletRequest request, TransferenciaBean transferencia) {
		if (transferencia.getCodigoError() == null) {
			request.setAttribute("mensaje", "Transferencia rechazada");		
		} else if (transferencia.getCodigoError() != null) {
			request.setAttribute("error", "Error al rechazar la transferencia");
			request.setAttribute("error_descripcion", transferencia.getMensajeError());
		}
	}
	
	public ActionForward exportarDetallesImpresion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		    throws IOException, ServletException {
		String idTrx = request.getParameter("idTrx");
		String idEmpresa = request.getParameter("idEmpresa");
		TransferenciaBean transferencia = delegado.getTransferencia(idTrx, idEmpresa);
		request.setAttribute("trx", transferencia);
		return mapping.findForward("impresionHtml");
	}

}

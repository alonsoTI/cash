package com.hiper.cash.actions;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.financiero.cash.beans.TransferenciaBean;
import com.financiero.cash.delegate.CuentasDelegate;
import com.financiero.cash.delegate.SeguridadDelegate;
import com.financiero.cash.delegate.TransferenciasDelegate;
import com.financiero.cash.exception.BFPBusinessException;
import com.financiero.cash.exception.NotAvailableException;
import com.financiero.cash.exception.NotFoundException;
import com.financiero.cash.ibs.util.IbsUtils;
import com.financiero.cash.ibs.util.TipoDocumento;
import com.financiero.cash.util.TipoTransferencia;
import com.financiero.cash.util.VariablesSession;
import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.clienteWS.GenRequestXML;
import com.hiper.cash.dao.TaServicioOpcionDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TmBancoDao;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.hibernate.TaServicioOpcionDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmBancoDaoHibernate;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.domain.TaServicioOpcion;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.entidad.BeanSuccess;
import com.hiper.cash.entidad.BeanSuccessDetail;
import com.hiper.cash.entidad.BeanTransferencia;
import com.hiper.cash.forms.TransferenciasForm;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Util;
import com.hiper.cash.xml.bean.BeanAccount;
import com.hiper.cash.xml.bean.BeanConsCtasCliente;
import com.hiper.cash.xml.bean.BeanDataLoginXML;
import com.hiper.cash.xml.bean.BeanNodoXML;
import com.hiper.cash.xml.bean.BeanRespuestaXML;
import com.hiper.cash.xml.parser.ParserXML;


public class TransferenciasAction extends DispatchAction {
	
	private Logger logger =  Logger.getLogger(this.getClass());
	private SeguridadDelegate delegadoSeguridad = SeguridadDelegate.getInstance();
	private TransferenciasDelegate delegado =  TransferenciasDelegate.getInstance();
	private String idModulo=null;

	
	@SuppressWarnings("unchecked")
	public ActionForward iniciarTrxCtaPropia(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletConfig().getServletContext();
		BeanDataLoginXML beanDataLogXML =(BeanDataLoginXML)session.getAttribute("usuarioActual") ;
		MessageResources messageResources = getResources(request);
		try{
			if (beanDataLogXML == null) {
				response.sendRedirect("cierraSession.jsp");
				return null;
			}else{            	
				String id = request.getParameter("modulo");
				if( id != null ){
					idModulo = id;
				}           			
				if( !delegadoSeguridad.verificaDisponibilidad(idModulo)){        			
					return mapping.findForward("fueraServicio");
				}         	
			}
			 
			String habil = request.getParameter("habil");
			if( habil != null ){
				if( habil.equals("0")){
					  return mapping.findForward("noPermiso");
				}
			}
			String codigoEmpresa;			
			TransferenciasForm frm = (TransferenciasForm)form;				
			frm.setTipo(TipoTransferencia.CP);
			frm.setCorrelativoDiario(delegado.obtenerCorrelativoTransferenciasDiarias());
			/* TODO revisar las validaciones que hacen referencia al usuario especial, ya que este
			 * usuario no tiene acceso a este modulo. No deberia manejarse el caso.
			*/
			if(beanDataLogXML.isM_usuarioEspecial()){	
				request.setAttribute("mensaje","El servicio no se encuentra afiliado");
				return mapping.findForward("error");
			}else{        				
				HashMap<String,List<String>> hMapEmpresas =(HashMap<String,List<String>>)session.getAttribute("hmEmpresas");				
				codigoEmpresa=Util.validarServicio(hMapEmpresas, Constantes.TX_CASH_SERVICIO_TRANS_CTAPROPIA);
				if( codigoEmpresa == null){
					request.setAttribute("mensaje","El servicio no se encuentra afiliado");
					return mapping.findForward("error");
				}
			}			
		    frm.setCodigoEmpresa(codigoEmpresa);
		    CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);            
		    CuentasDelegate cuentasDelegate = new CuentasDelegate();
		    cuentasDelegate.setCliente(cashclienteWS);            
		    BeanConsCtasCliente beanctascliente = cuentasDelegate.obtenerCuentasCliente(codigoEmpresa);
		    List<BeanAccount> listaccounts = beanctascliente.getM_Accounts();
		    session.setAttribute("tf_listaccounts", listaccounts);		    
		    return mapping.findForward("iniciarTCP");    		
		}catch(NullPointerException e){
			logger.error(e.getMessage(), e);
			request.setAttribute("mensaje", messageResources.getMessage("transferencias.error.1"));
		    return mapping.findForward("error");
		}catch(NotFoundException e){
			logger.error(e.getMessage(), e);
			request.setAttribute("mensaje", messageResources.getMessage("transferencias.error.3"));
		    return mapping.findForward("error");
		}catch(NotAvailableException e){
			logger.error(e.getMessage(), e);
			request.setAttribute("mensaje", messageResources.getMessage("bloqueos.error"));
			return mapping.findForward("error");
		}catch(Exception e){
			logger.error(e.getMessage(), e);    		
			return mapping.findForward("error");
		}    
	}	
	
	@SuppressWarnings("unchecked")
	public ActionForward iniciarTrxCtaTerceros(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletConfig().getServletContext();
		BeanDataLoginXML beanDataLogXML =(BeanDataLoginXML)session.getAttribute("usuarioActual") ;
		MessageResources messageResources = getResources(request);
		try{
			if (beanDataLogXML == null) {
				response.sendRedirect("cierraSession.jsp");
				return null;
			}else{            	
				String id = request.getParameter("modulo");
				if( id != null ){
					idModulo = id;
				}           			
				if( !delegadoSeguridad.verificaDisponibilidad(idModulo)){        			
					return mapping.findForward("fueraServicio");
				}         	
			}
			String habil = request.getParameter("habil");
			if( habil != null ){
				if( habil.equals("0")){
					  return mapping.findForward("noPermiso");
				}
			}	       
			String codigoEmpresa;			
			TransferenciasForm frm = (TransferenciasForm)form;
			frm.setCorrelativoDiario(delegado.obtenerCorrelativoTransferenciasDiarias());
			frm.setTipo(TipoTransferencia.CT);
			if(beanDataLogXML.isM_usuarioEspecial()){	
				request.setAttribute("mensaje","El servicio no se encuentra afiliado");
				return mapping.findForward("error");
			}else{        				
				HashMap<String,List<String>> hMapEmpresas =(HashMap<String,List<String>>)session.getAttribute("hmEmpresas");				
				codigoEmpresa=Util.validarServicio(hMapEmpresas, Constantes.TX_CASH_SERVICIO_TRANS_CTATERCERO);
				if( codigoEmpresa == null){
					request.setAttribute("mensaje","El servicio no se encuentra afiliado");
					return mapping.findForward("error");
				}
			}			
		    frm.setCodigoEmpresa(codigoEmpresa);
		    CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);            
		    CuentasDelegate cuentasDelegate = new CuentasDelegate();
		    cuentasDelegate.setCliente(cashclienteWS);            
		    BeanConsCtasCliente beanctascliente = cuentasDelegate.obtenerCuentasCliente(codigoEmpresa);
		    List<BeanAccount> listaccounts = beanctascliente.getM_Accounts();
		    session.setAttribute("tf_listaccounts", listaccounts);		    
		    return mapping.findForward("iniciarTCT");    		
		}catch(NullPointerException e){
			logger.error(e.getMessage(), e);
			request.setAttribute("mensaje", messageResources.getMessage("transferencias.error.1"));
		    return mapping.findForward("error");
		}catch(NotFoundException e){
			logger.error(e.getMessage(), e);
			request.setAttribute("mensaje", messageResources.getMessage("transferencias.error.3"));
		    return mapping.findForward("error");
		}catch(NotAvailableException e){
			logger.error(e.getMessage(), e);
			request.setAttribute("mensaje", messageResources.getMessage("bloqueos.error"));
			return mapping.findForward("error");
		}catch(Exception e){
			logger.error(e.getMessage(), e);    		
			return mapping.findForward("error");
		}    
	}
	
	@SuppressWarnings("unchecked")
	public ActionForward iniciarTrxInterbancarias(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    	throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletConfig().getServletContext();
		BeanDataLoginXML beanDataLogXML =(BeanDataLoginXML)session.getAttribute("usuarioActual") ;
		MessageResources messageResources = getResources(request);
		try{
			if (beanDataLogXML == null) {
				response.sendRedirect("cierraSession.jsp");
				return null;
			}else{            	
				String id = request.getParameter("modulo");
				if( id != null ){
					idModulo = id;
				}           			
				if( !delegadoSeguridad.verificaDisponibilidad(idModulo)){        			
					return mapping.findForward("fueraServicio");
				}         	
			}
			String habil = request.getParameter("habil");
			if( habil != null ){
				if( habil.equals("0")){
					  return mapping.findForward("noPermiso");
				}
			}	 
			String codigoEmpresa;			
			TransferenciasForm frm = (TransferenciasForm)form;
			frm.setCorrelativoDiario(delegado.obtenerCorrelativoTransferenciasDiarias());
			frm.setTipo(TipoTransferencia.IT);
			if(beanDataLogXML.isM_usuarioEspecial()){	
				request.setAttribute("mensaje","El servicio no se encuentra afiliado");
				return mapping.findForward("error");
			}else{
				HashMap<String,List<String>> hMapEmpresas =(HashMap<String,List<String>>)session.getAttribute("hmEmpresas");				
				codigoEmpresa=Util.validarServicio(hMapEmpresas, Constantes.TX_CASH_SERVICIO_TRANS_INTERBANCARIA);
				if( codigoEmpresa == null){
					request.setAttribute("mensaje","El servicio no se encuentra afiliado");
					return mapping.findForward("error");
				}
			}			
		    frm.setCodigoEmpresa(codigoEmpresa);
		    CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);            
		    CuentasDelegate cuentasDelegate = new CuentasDelegate();
		    cuentasDelegate.setCliente(cashclienteWS);            
		    BeanConsCtasCliente beanctascliente = cuentasDelegate.obtenerCuentasCliente(codigoEmpresa);
		    List<BeanAccount> listaccounts = beanctascliente.getM_Accounts();
		    session.setAttribute("tf_listaccounts", listaccounts);		    
		    return mapping.findForward("iniciarTIT");    		
		}catch(NullPointerException e){
			logger.error(e.getMessage(), e);
			request.setAttribute("mensaje", messageResources.getMessage("transferencias.error.1"));
		    return mapping.findForward("error");
		}catch(NotFoundException e){
			logger.error(e.getMessage(), e);
			request.setAttribute("mensaje", messageResources.getMessage("transferencias.error.3"));
		    return mapping.findForward("error");
		}catch(NotAvailableException e){
			logger.error(e.getMessage(), e);
			request.setAttribute("mensaje", messageResources.getMessage("bloqueos.error"));
			return mapping.findForward("error");
		}catch(Exception e){
			logger.error(e.getMessage(), e);    		
			return mapping.findForward("error");
		}    
	}
	
	public ActionForward confirmar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     throws IOException, ServletException {
		StringBuilder retorno = new StringBuilder("error");		
		try{			
			 HttpSession session = request.getSession();			 
			 ServletContext contexto = session.getServletContext();
			 TransferenciasForm frm = (TransferenciasForm)form;			
			 Map<String,String>  mapa = (Map<String, String>) contexto.getAttribute("mapaMoneda");
			 frm.setMoneda(mapa.get(frm.getCodigoMoneda()));
			 TipoTransferencia tipo =  frm.getTipo();
			 
			 TransferenciaBean transferencia= new TransferenciaBean();
			 retorno = new StringBuilder("iniciarT").append(tipo.name());	
			 
			 //aqui valido los datos del formularios de transferencia cuenta a terceros
			 logger.info("Tipo Transferencia enviada : "+tipo.getValor());

			 if(tipo == TipoTransferencia.CT){
				 logger.info("ENTRO AL PROCESO DE VALIDACION DE CT");
				
				 if(!IbsUtils.esNumerico(frm.getCuentaAbono())){
					 logger.error("001 -  error de cuenta de Abono ::"+frm.getCuentaAbono());
					 throw new BFPBusinessException("001", "error en cuenta de Abono");
				 }
				
				 if(!IbsUtils.esNumerico(frm.getCuentaCargo())){
					 logger.error("001 -  error de cuenta de Abono ::"+frm.getCuentaCargo());
					 throw new BFPBusinessException("001", "error en cuenta de Cargo");
				 }

				 if(!IbsUtils.esMontoValido(String.valueOf(frm.getMonto())) ){
					 logger.error("002 -  error de monto ::"+frm.getMonto());
					 throw new BFPBusinessException("002", "error en monto Ingresado");
				 }
			 }
			 
			 transferencia.setCuentaAbono(IbsUtils.formateaValorNumericoIbs(frm.getCuentaAbono(),12));
			 transferencia.setCuentaCargo(frm.getCuentaCargo());
			 transferencia.setMonto(frm.getMonto());
			 transferencia.setTipo(frm.getTipo());			 
			 transferencia.setMoneda(frm.getCodigoMoneda());
			 transferencia.setCodigoMoneda(frm.getCodigoMoneda());
	         BeanDataLoginXML usuario =(BeanDataLoginXML)session.getAttribute("usuarioActual") ;	         
			 transferencia.setUsuarioRegistro(usuario.getM_Codigo());
			 transferencia.setReferencia(frm.getReferencia().toUpperCase());
			 	 
			 
			 if( tipo.equals(TipoTransferencia.IT)){
				 StringBuilder sb = new StringBuilder(frm.getM_CtaAbonoEntidad().trim());
				 sb = sb.append(frm.getM_CtaAbonoOficina().trim()).append(frm.getM_CtaAbonoCuenta().trim()).append(frm.getM_CtaAbonoControl().trim());				 
				 transferencia.setCuentaAbono(sb.toString());
				 transferencia.setDocumento(frm.getDocumento());
				 if( frm.getDocumento().equals(TipoDocumento.DNI.name())){					 
					 transferencia.setApellidoPaterno(frm.getApellidoPaterno().toUpperCase());
					 transferencia.setApellidoMaterno(frm.getApellidoMaterno().toUpperCase());
					 transferencia.setNombres(frm.getNombres().toUpperCase());
					 transferencia.setNroDocumento(frm.getDni());
				 }
				 else if( frm.getDocumento().equals(TipoDocumento.CIP.name())){
					 transferencia.setApellidoPaterno(frm.getApellidoPaterno().toUpperCase());
					 transferencia.setApellidoMaterno(frm.getApellidoMaterno().toUpperCase());
					 transferencia.setNombres(frm.getNombres().toUpperCase());
					 transferencia.setNroDocumento(frm.getNumeroDocumento());
				 }
				 else if(frm.getDocumento().equals(TipoDocumento.RUC.name())){
					 transferencia.setApellidoPaterno(frm.getRazonSocial().toUpperCase());
					 transferencia.setNroDocumento(frm.getRuc());
				 }else{
					 transferencia.setApellidoPaterno(frm.getApellidoPaterno2().toUpperCase());
					 transferencia.setApellidoMaterno(frm.getApellidoMaterno2().toUpperCase());
					 transferencia.setNombres(frm.getNombres().toUpperCase());
					 transferencia.setNroDocumento(frm.getNumeroDocumento());
				 }
				 transferencia.setTelefono(frm.getTelefono());
				 transferencia.setDireccion(frm.getDireccion().toUpperCase());
				 transferencia.setMismo(frm.getM_FlagCliente2());
			 }
			 String idEmpresa = (String) session.getAttribute(CashConstants.PARAM_ID_EMPRESA);
			 transferencia.setIdEmpresa(Long.valueOf(idEmpresa));
			 TaServicioxEmpresa servicioEmpresa = delegado.obtenerServicioEmpresa(idEmpresa, transferencia.getTipo().getValor());
			 transferencia.setServicio(servicioEmpresa.getCsemIdServicioEmpresa());
			 
			 delegado.validarCuentas(transferencia);	 
			 session.setAttribute("trx", transferencia);
			 session.setAttribute(VariablesSession.CORRELATIVO_DIARIO_TRANSFERENCIAS.getDescripcion(), frm.getCorrelativoDiario());	
			 request.setAttribute("origen", "confirmacion");
			 return mapping.findForward("confirmarTCP");
		 }
		 catch (BFPBusinessException e) {			
			if ("001".equals(e.getCode())) {
				logger.warn(e.getMessage());
				request.setAttribute("error_cuenta", e.getMessage());
			}
			if ("002".equals(e.getCode())) {
				logger.warn(e.getMessage());
				request.setAttribute("error_monto", e.getMessage());
			}
			if ("003".equals(e.getCode())) {
				logger.warn(e.getMessage());
				request.setAttribute("error", e.getMessage());
			}
			return mapping.findForward(retorno.toString());						 		
		 }			
         catch(Exception e){
			logger.error(e.getMessage(), e);    		
			return mapping.findForward("error");
		 }
	}
	
	public ActionForward procesar(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			HttpSession session = request.getSession();
			TransferenciaBean transferencia = (TransferenciaBean) session.getAttribute("trx");
			if (transferencia != null) {
				int correlativoDiario = (Integer) session
						.getAttribute(VariablesSession.CORRELATIVO_DIARIO_TRANSFERENCIAS.getDescripcion());
				transferencia = delegado.registrarTransferencia(transferencia, correlativoDiario);
				obtenerMensajesProceso(request, transferencia);
				if (transferencia.getCodigoError() == null) {
					String idEmpresa = obtenerCodigoEmpresa(request);
					transferencia = delegado.getTransferencia(transferencia.getNumero(), idEmpresa);
					request.setAttribute("origen", "procesoOk");
				} else {
					request.setAttribute("origen", "procesoNoOk");
				}
				session.removeAttribute("trx");
				request.setAttribute("trx", transferencia);
				return mapping.findForward("procesarTCP");
			} else {
				return mapping.findForward("iniciarOpciones");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return mapping.findForward("error");
		}
	}

	private void obtenerMensajesProceso(HttpServletRequest request, TransferenciaBean transferencia) {
		if (transferencia.getCodigoError() == null && transferencia.isProcesado()) {
			request.setAttribute("mensaje", "Transferencia Procesada Correctamente");
		} else if (transferencia.getCodigoError() == null && !transferencia.isProcesado()) {
			request.setAttribute("error", "Transferencia Registrada Correctamente");
			request.setAttribute("error_descripcion", "Por Favor Aprobar Operacion");			
		} else if (transferencia.getCodigoError() != null && !transferencia.isProcesado()) {
			request.setAttribute("error", "Transferencia No Registrada");
			request.setAttribute("error_descripcion", transferencia.getMensajeError());
		} else if (transferencia.getCodigoError() != null && transferencia.isProcesado()) {
			request.setAttribute("error", "Transferencia No Procesada");
			request.setAttribute("error_descripcion", transferencia.getMensajeError());
		}		
	}
	

    public ActionForward imprimirTransferencia(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {
    	try{
    		long idOrden = Long.parseLong(request.getParameter("idOrden"));
    		long idServicioEmpresa = Long.parseLong(request.getParameter("idServicio"));
    		String modulo =  request.getParameter("modulo");
    		MessageResources messageResources = getResources(request);    		
    		List<BeanSuccessDetail> alsuccess = delegado.getDetallesImpresion(modulo,messageResources, idOrden, idServicioEmpresa);
    		request.setAttribute("alsuccess", alsuccess);
    		
    		if( modulo.equals(CashConstants.COD_MODULO_TRANSFERENCIAS)){
    			BeanSuccess success = new BeanSuccess();
                success.setM_Mensaje(messageResources.getMessage("transferencias.confirmacion.title"));
                request.setAttribute("success", success);
    		}
               		    		
    		return mapping.findForward("imprTransferencia");
    	}catch(Exception e){
    		request.setAttribute("mensaje", e.getMessage());
    		logger.error(e.getMessage(),e);
    		return mapping.findForward("error");
    	}
    }
    
    public ActionForward cargarServicios(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }else{
        	String id = request.getParameter("modulo");
			if( id != null ){
				idModulo = id;
			}			
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {				 
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
        }

        TransferenciasForm transf = (TransferenciasForm) form;
        BeanTransferencia transf_cta = (BeanTransferencia) session.getAttribute("tf_transf_cta");

        //Request
        String stipo = (String) request.getParameter("tipo");
        int itipo = Integer.parseInt(stipo);

        MessageResources messageResources = getResources(request);

        String empresa = transf.getM_Empresa();

        transf_cta.setM_Empresa(empresa);
        transf_cta.setM_Monto(transf.getM_Monto());
        transf_cta.setM_Referencia(transf.getM_Referencia());
        transf_cta.setM_Moneda(transf.getM_Moneda());
        transf_cta.setM_CtaCargo(transf.getM_CtaCargo());
        transf_cta.setM_CtaAbono(transf.getM_CtaAbono());

        //Hibernate
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        TaServicioOpcionDao servicio_opcionDAO = new TaServicioOpcionDaoHibernate();
        TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();
        TmEmpresa objempresa = empresaDAO.selectEmpresas(empresa);

        TaServicioOpcion taservicioopcion = servicio_opcionDAO.select(transf_cta.getM_Modulo(), transf_cta.getM_SubModulo());
        List servicios = servicioDAO.selectServicioxEmpresaxCode( objempresa.getCemIdEmpresa(), taservicioopcion.getId().getCsoservicioId());

        //GetInfo CashWS - CONS_CTAS_CLIENTE
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        if(cashclienteWS == null){
            request.setAttribute("mensaje", "Error. No se puede conectar al servicio web del IBS");
            return mapping.findForward("error");
        }
        ArrayList listaParametros = new ArrayList();
        //listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_CONS_CTAS_CLIENTE));
        listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_CONS_CTAS_ASOC_CLIENTE));

        if (objempresa.getCemCodigoCliente() == null){
            request.setAttribute("mensaje", messageResources.getMessage("transferencias.error.2", new Object[]{objempresa.getCemIdEmpresa()}));
            return mapping.findForward("error");
        }
        listaParametros.add(new BeanNodoXML("client_code", objempresa.getCemCodigoCliente()));
        //listaParametros.add(new BeanNodoXML("filtro", Constantes.FILTRO_ACCOUNT_TYPE));
        //+ parametros
        String req = GenRequestXML.getXML(listaParametros);
        String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);

        if(resultado==null || "".equals(resultado)){
            request.setAttribute("mensaje", messageResources.getMessage("transferencias.error.1"));
            return mapping.findForward("error");
        }

        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML)session.getAttribute("usuarioActual");
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //procesamos la respuesta(parseo xml) y enviamos a la pagina
            //BeanConsCtasCliente beanctascliente = ParserXML.getConsCtasCliente(beanResXML.getM_Respuesta(), cashclienteWS, beanDataLogXML.getM_NumTarjeta(), beanDataLogXML.getM_Token(), objempresa.getCemIdEmpresa());
            BeanConsCtasCliente beanctascliente = ParserXML.getConsCtasClienteCombos(beanResXML.getM_Respuesta());
            
            List listaccounts = beanctascliente.getM_Accounts();
            session.setAttribute("tf_listaccounts", listaccounts);
            request.setAttribute("bLista", 1);
            request.setAttribute("listaservicios", servicios);
            session.setAttribute("tf_transf_cta", transf_cta);
        }
        else{
            request.setAttribute("mensaje", messageResources.getMessage("transferencias.error.3"));
            return mapping.findForward("error");
        }

        switch (itipo){
            //TRANSFERENCIA CTAS PROPIAS
            case 1:
                return mapping.findForward("cuentasPropias");
            case 2:
                return mapping.findForward("cuentasTerceros");
            case 3:
                return mapping.findForward("cuentasInterBco");
            default:
                return mapping.findForward("cuentasPropias");
        }
    }
    
    public ActionForward obtenerNombreBco(ActionMapping mapping, ActionForm inForm, HttpServletRequest request, HttpServletResponse response) throws Exception {

        TmBancoDao bancoDao = new TmBancoDaoHibernate();
        
		String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "\n";
		xml = xml + "<respuestas>" + "\n";
    
        String m_CodigoBco = request.getParameter("codigo");

        String nombreBco = bancoDao.obtenerBanco(m_CodigoBco);
        if(nombreBco!=null && !"".equals(nombreBco)){
            xml = xml + "<respuesta valor=\"BANCO "+nombreBco+"\" />" + "\n";
        }else{
            xml = xml + "<respuesta valor='BANCO NO REGISTRADO'/>" + "\n";
        }
		
		//cerramos el archivo xml
		xml = xml + "</respuestas>" + "\n";

		// Escribimos el xml al flujo del response
		response.setContentType("application/xml");
		//para que el navegador no utilice su cache para mostrar los datos
		response.addHeader("Cache-Control","no-cache"); //HTTP 1.1
		response.addHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
		//escribimos el resultado en el flujo de salida
		PrintWriter out = response.getWriter();
		out.println(xml);
		out.flush();

		return null; // no retornamos nada
	}
    
    private String obtenerCodigoEmpresa(HttpServletRequest request){
    	HttpSession session = request.getSession();
    	return (String) session.getAttribute(CashConstants.PARAM_ID_EMPRESA);
    }
}

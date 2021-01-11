/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
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
import org.apache.struts.util.MessageResources;

import com.financiero.cash.delegate.SeguridadDelegate;
import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.clienteWS.GenRequestXML;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TaSecuencialDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.TxResultDao;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaSecuencialDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TpDetalleOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TxResultDaoHibernate;
import com.hiper.cash.descarga.excel.GeneradorPOI;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.domain.TaOrdenId;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.domain.TpDetalleOrden;
import com.hiper.cash.domain.TpDetalleOrdenId;
import com.hiper.cash.domain.TxResult;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.BeanSuccess;
import com.hiper.cash.entidad.BeanSuccessDetail;
import com.hiper.cash.entidad.Propiedades;
import com.hiper.cash.forms.LetrasForm;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;
import com.hiper.cash.xml.bean.BeanConsCtasCliente;
import com.hiper.cash.xml.bean.BeanConsDetPlanLetras;
import com.hiper.cash.xml.bean.BeanConsPlanLetras;
import com.hiper.cash.xml.bean.BeanDataLoginXML;
import com.hiper.cash.xml.bean.BeanDetalleLetra;
import com.hiper.cash.xml.bean.BeanLetra;
import com.hiper.cash.xml.bean.BeanNodoXML;
import com.hiper.cash.xml.bean.BeanRespuestaXML;
import com.hiper.cash.xml.parser.ParserXML;

/**
 *
 * @author jmoreno
 *
 */
public class LetrasAction extends DispatchAction {

    private static Logger logger = Logger.getLogger(LetrasAction.class);
	private SeguridadDelegate delegadoSeguridad = SeguridadDelegate.getInstance();
	private String idModulo = null;

    public ActionForward cargarConsulta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
       HttpSession session = request.getSession();
       BeanDataLoginXML beanDataLogXML =(BeanDataLoginXML)session.getAttribute("usuarioActual") ;
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }else {
        	
			String id =  request.getParameter("modulo");
			if( id != null ){
				idModulo =  id;
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

        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarConsulta");
        }

        session.removeAttribute("listaResult");
        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa = (List) session.getAttribute("empresa");

        List lEmpresa = null;
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        if(beanDataLogXML.isM_usuarioEspecial()){
            lEmpresa = (List) session.getAttribute("empresa");
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_CONS_LETRA);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }else{
            HashMap hMapEmpresas =(HashMap)session.getAttribute("hmEmpresas");
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_CONS_LETRA);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }

        //obtenemos los datos de la empresa que resulto al logearnos
       
        List listaEmpresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);

        //obtenemos la fecha actual del sistema
        String fechaActual = Fecha.getFechaActual("dd/MM/yyyy");

        session.setAttribute("listaEmpresas", listaEmpresas);
        //seteamos la fecha actual en el form para que se visualice en la pagina
        LetrasForm letrasForm = (LetrasForm) form;
        letrasForm.setM_Empresa("");
        letrasForm.setM_FecInicio(fechaActual);
        letrasForm.setM_FecFin(fechaActual);
        letrasForm.setM_FechaMaxAnt(Fecha.getFechaCustom("yyyyMMdd",java.util.Calendar.YEAR,-1));
        letrasForm.setM_FechaMaxPost(Fecha.getFechaCustom("yyyyMMdd",java.util.Calendar.MONTH,1));
        letrasForm.setM_fechaActualComp(Fecha.getFechaActual("yyyyMMdd"));
        //seteamos el radiobutton que estara seleccionado
        letrasForm.setM_Tipo("G");

        return mapping.findForward("cargarConsulta");
    }

    public ActionForward cargarPreliquidacion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML =(BeanDataLoginXML)session.getAttribute("usuarioActual") ;
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }else {
        	String id =  request.getParameter("modulo");
			if( id != null ){
				idModulo =  id;
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
        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarPreliquidacion");
        }

        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa =(List) session.getAttribute("empresa");
        
        List lEmpresa = null;
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        
        if(beanDataLogXML.isM_usuarioEspecial()){
            lEmpresa = (List) session.getAttribute("empresa");
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_PRELIQ_LETRA);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }else{
            HashMap hMapEmpresas =(HashMap)session.getAttribute("hmEmpresas");
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_PRELIQ_LETRA);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }

        //obtenemos los datos de la empresa que resulto al logearnos
        
        List listaEmpresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);

      
        //obtenemos la fecha actual del sistemas
        String fechaActual = Fecha.getFechaActual("dd/MM/yyyy");

        session.setAttribute("listaEmpresas", listaEmpresas);
        //seteamos la fecha actual en el form para que se visualice en la pagina
        LetrasForm letrasForm = (LetrasForm) form;
        letrasForm.setM_Empresa("");
        letrasForm.setM_FecInicio(fechaActual);
        letrasForm.setM_FecFin(fechaActual);
        letrasForm.setM_FechaMaxAnt(Fecha.getFechaCustom("yyyyMMdd",java.util.Calendar.YEAR,-1));
        letrasForm.setM_FechaMaxPost(Fecha.getFechaCustom("yyyyMMdd",java.util.Calendar.MONTH,1));
        letrasForm.setM_fechaActualComp(Fecha.getFechaActual("yyyyMMdd"));
        //seteamos el radiobutton que estara seleccionado
        letrasForm.setM_Tipo("G");

        return mapping.findForward("cargarPreliquidacion");
    }

    public ActionForward cargarCancelacion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML =(BeanDataLoginXML)session.getAttribute("usuarioActual") ;
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }else {
        	String id =  request.getParameter("modulo");
			if( id != null ){
				idModulo =  id;
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
        
        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarCancelacion");
        }

        //cargamos las empresas asociadas resultante del logueo        
//        List lEmpresa = (List) session.getAttribute("empresa");

        List lEmpresa = null;
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        if(beanDataLogXML.isM_usuarioEspecial()){
            lEmpresa = (List) session.getAttribute("empresa");
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_CANCELACION_LETRA);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }else{
            HashMap hMapEmpresas =(HashMap)session.getAttribute("hmEmpresas");
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_CANCELACION_LETRA);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }

        //obtenemos los datos de la empresa que resulto al logearnos
        
        List listaEmpresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);

       //obtenemos la fecha actual del sistemas
        String fechaActual = Fecha.getFechaActual("dd/MM/yyyy");

        session.setAttribute("listaEmpresas", listaEmpresas);
        //seteamos la fecha actual en el form para que se visualice en la pagina
        LetrasForm letrasForm = (LetrasForm) form;
        letrasForm.setM_Empresa("");
        letrasForm.setM_FecInicio(fechaActual);
        letrasForm.setM_FecFin(fechaActual);
        letrasForm.setM_fechaActualComp(Fecha.getFechaActual("yyyyMMdd"));
        letrasForm.setM_FechaMaxAnt(Fecha.getFechaCustom("yyyyMMdd",java.util.Calendar.YEAR,-1));
        letrasForm.setM_FechaMaxPost(Fecha.getFechaCustom("yyyyMMdd",java.util.Calendar.MONTH,1));

        //seteamos el radiobutton que estara seleccionado
        letrasForm.setM_Tipo("G");

        return mapping.findForward("cargarCancelacion");
    }

    public ActionForward buscarPlanilla(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        LetrasForm letrasForm = (LetrasForm) form;
        String empresa = letrasForm.getM_Empresa();
        String fecfin = letrasForm.getM_FecFin();
        String fecini = letrasForm.getM_FecInicio();
        String tipo = letrasForm.getM_Tipo();
        letrasForm.setM_FecInicio(fecini);
        letrasForm.setM_FecFin(fecfin);
        letrasForm.setM_FechaMaxAnt(Fecha.getFechaCustom("yyyyMMdd",java.util.Calendar.YEAR,-1));
        letrasForm.setM_FechaMaxPost(Fecha.getFechaCustom("yyyyMMdd",java.util.Calendar.MONTH,1));
        letrasForm.setM_fechaActualComp(Fecha.getFechaActual("yyyyMMdd"));
        //verificar de que pagina se realizó la consulta
        int from = Integer.parseInt(request.getParameter("from"));
        String forward = "";
        switch (from) {
            case 1:
                forward = "cargarConsulta";
                break; //consultas
            case 2:
                forward = "preLiqConsulta";
                break; //preliquidacion
            case 3:
                forward = "cancelarConsulta";
                break; //cancelacion
        }
   
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}

        CashClientWS cashclienteWS = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();

        if ("G".equals(tipo)) {
            listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_CONS_PLAN_LETRAS));
        } else {
            listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_CONS_PLAN_LETRAS_ACEP));
        }
        listaParametros.add(new BeanNodoXML("doc_number", empresa));
        listaParametros.add(new BeanNodoXML("num_mov", "0000000000"));
        listaParametros.add(new BeanNodoXML("fecha_ini", Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy", fecini)));
        listaParametros.add(new BeanNodoXML("fecha_fin", Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy", fecfin)));

        try {
            String req = GenRequestXML.getXML(listaParametros);
            String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);

            if (resultado == null || "".equals(resultado)) {
                request.setAttribute("mensaje", "No se encontraron resultados");
            } else {
                
                BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);                
                //si la respuesta es exitosa
                if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                    Propiedades prop = (Propiedades) context.getAttribute("propiedades");
//                    BeanConsPlanLetras beancons = ParserXML.parseConsultarPlanilla(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS());
                    int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_TOTAL_REG));
                    int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_NUM_REG));
                    BeanConsPlanLetras beancons = null;
                    if (total_number == send_number) {                    	
                        beancons = ParserXML.parseConsultarPlanilla(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS());
                    } else {
                        beancons = ParserXML.parseConsultarPlanillaPag(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS(), empresa, total_number, send_number, fecini, fecfin, cashclienteWS, tipo);
                        //beanConsRelBco = ParserXML.listarRelacionesBcoPag(cod_cliente, ruc_empresa, beanResXML.getM_Respuesta(), total_number, send_number, cashclienteWS);
                    }
                    if (beancons != null) {
                        request.setAttribute("beanLetras", beancons);

                        if (beancons.getM_Letras() != null && beancons.getM_Letras().size() > 0) {
                            request.setAttribute("listaResult", beancons.getM_Letras());
                        } else {
                            request.setAttribute("mensaje", "No se encontraron movimientos");
                        }
                    } else {
                        request.setAttribute("mensaje", "No se encontraron movimientos");
                    }
                } else if (beanResXML != null && beanResXML.getM_CodigoRetorno() != null) {// && beanResXML.getM_DescripcionError() != null && beanResXML.getM_DescripcionError().length() > 0
                    TxResultDao dao = new TxResultDaoHibernate();
                    TxResult txResult = dao.selectByCodIbs(beanResXML.getM_CodigoRetornoIBS());
                    if(txResult != null){
                        request.setAttribute("mensaje",txResult.getDrsDescription());// beanResXML.getM_DescripcionError()
                    }else{
                        request.setAttribute("mensaje",beanResXML.getM_CodigoRetornoIBS()+":"+Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);
                    }
                    
                } else {
                    request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
                }
            }
          
        } catch (Exception ex) {
            request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            logger.error(ex.toString());
        }

        session.setAttribute("tipoConsultaLetra", tipo);//para guardar el tipo de consulta q se realizó//*revisar

        return mapping.findForward(forward);
    }

    
    public ActionForward detallePlanillaLetras(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException {

				HttpSession session = request.getSession();
				ServletContext context = getServlet().getServletConfig().getServletContext();
				
				if (session.getAttribute("usuarioActual") == null) {
				    response.sendRedirect("cierraSession.jsp");
				    return null;
				}
				int from = Integer.parseInt(request.getParameter("from"));        
				String reqBack="m_Empresa="+((LetrasForm) form).getM_Empresa()+"&m_FecInicio="+((LetrasForm) form).getM_FecInicio()+"&m_FecFin="+((LetrasForm) form).getM_FecFin()+"&m_Tipo="+session.getAttribute("tipoConsultaLetra");
				request.setAttribute("reqBack", reqBack);
				String forward = "";
				switch (from) {
				    case 1:
				        forward = "mostrarDetalle";
				        break; //consultas
				    case 2:
				        forward = "mostrarDetalleLiq";
				        break; //preliquidacion
				    case 3:
				        forward = "mostrarDetalleCan";
				        break; //cancelacion
				}
				
				TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
				String m_CodigoCliente = empresaDAO.obtenerCodCliente(((LetrasForm) form).getM_Empresa());//se obtiene el codigo del cliente segun el ruc de la empresa
				empresaDAO = null;
				
				CashClientWS cashclienteWS = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
				ArrayList listaParametros = new ArrayList();
				
				listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_DET_PLAN_LETRAS));
				listaParametros.add(new BeanNodoXML("account_number", request.getParameter("m_Cuenta")));
				listaParametros.add(new BeanNodoXML("ruc",((LetrasForm) form).getM_Empresa()));
				listaParametros.add(new BeanNodoXML("num_mov", "0000000000"));
				String tipo = (String) session.getAttribute("tipoConsultaLetra");
				
				if ("G".equals(tipo)) {
				    listaParametros.add(new BeanNodoXML("client_code", m_CodigoCliente));
				} else {
				    listaParametros.add(new BeanNodoXML("client_code", m_CodigoCliente));
				    //listaParametros.add(new BeanNodoXML("acep_code", request.getParameter("m_CodAcep")));
				    listaParametros.add(new BeanNodoXML("acep_code", "99999999999"));
				    
				}
				try {
				    String req = GenRequestXML.getXML(listaParametros);
				    String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
				
				    if (resultado == null || "".equals(resultado)) {
				        request.setAttribute("mensaje", "No se encontraron resultados");
				    } else {                
				        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
				        //si la respuesta es exitosa
				        if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
				            Propiedades prop = (Propiedades) context.getAttribute("propiedades");
				            int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_TOTAL_REG));
				            int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_NUM_REG));
				            BeanConsDetPlanLetras beancons = null;
				            if(total_number == send_number){
				                if (from == 1) {//consulta
				                    beancons = ParserXML.parseConsultaDetPlanilla(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS1()
				                            , request.getParameter("m_Moneda"), request.getParameter("m_Cuenta"),false,((LetrasForm) form).getM_Empresa(),send_number);
				                }else{//preLiq o cancel
				                    beancons = ParserXML.parseConsultaDetPlanilla(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS1(),
				                            request.getParameter("m_Moneda"), request.getParameter("m_Cuenta"),true,((LetrasForm) form).getM_Empresa(),send_number);
				                }
				                
				            }else{
				                if (from == 1) {//consulta
				                    beancons = ParserXML.parseConsultaDetPlanillaPag(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS1(), request.getParameter("m_Moneda"), request.getParameter("m_Cuenta"),
				                            ((LetrasForm) form).getM_Empresa(),total_number,send_number,tipo,m_CodigoCliente,request.getParameter("m_CodAcep"),
				                            cashclienteWS,false);
				                } else {//preLiq o cancel
				                    beancons = ParserXML.parseConsultaDetPlanillaPag(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS1(), request.getParameter("m_Moneda"), request.getParameter("m_Cuenta"),
				                            ((LetrasForm) form).getM_Empresa(),total_number,send_number,tipo,m_CodigoCliente,request.getParameter("m_CodAcep"),
				                            cashclienteWS,true);
				                }
				            }
				//            if (from == 1) {//consulta
				//                beancons = ParserXML.parseConsultaDetPlanilla(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS1(), request.getParameter("m_Moneda"), request.getParameter("m_Cuenta"));
				//            } else {//preLiq o cancel
				//                beancons = ParserXML.parseConsultaDetPlanillaxEst(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS1(), request.getParameter("m_Moneda"), request.getParameter("m_Cuenta"));
				//            }
				            if (beancons != null) {
				                beancons.setM_CodigoCliente(m_CodigoCliente);
				                beancons.setM_RucEmpresa(((LetrasForm) form).getM_Empresa());
				                request.setAttribute("beanLetras", beancons);
				                if (beancons.getM_DetalleLetras() != null && beancons.getM_DetalleLetras().size() > 0) {
				                    request.setAttribute("listaResult", beancons.getM_DetalleLetras());
				                } else {
				                    request.setAttribute("mensaje", "No se encontraron movimientos");
				                }
				            } else {
				                request.setAttribute("mensaje", "No se encontraron movimientos");
				            }
				        } else if (beanResXML != null && beanResXML.getM_CodigoRetorno() != null ) {//&& beanResXML.getM_DescripcionError() != null && beanResXML.getM_DescripcionError().length() > 0
				            TxResultDao dao = new TxResultDaoHibernate();
				            TxResult txResult = dao.selectByCodIbs(beanResXML.getM_CodigoRetornoIBS());
				            if(txResult != null){
				                request.setAttribute("mensaje",txResult.getDrsDescription());// beanResXML.getM_DescripcionError()
				            }else{
				                request.setAttribute("mensaje",beanResXML.getM_CodigoRetornoIBS()+":"+Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);// beanResXML.getM_DescripcionError()
				            }
				            
				        } else {
				            request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
				        }
				    }
				    
				} catch (Exception ex) {
				    request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
				    logger.error(ex.toString());
				}
				
				return mapping.findForward(forward);
    }
 
    
    public ActionForward detallePlanillaLetrasPag(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

    	
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        int from = Integer.parseInt(request.getParameter("from"));        
        String reqBack="m_Empresa="+((LetrasForm) form).getM_Empresa()+"&m_FecInicio="+((LetrasForm) form).getM_FecInicio()+"&m_FecFin="+((LetrasForm) form).getM_FecFin()+"&m_Tipo="+session.getAttribute("tipoConsultaLetra");
        request.setAttribute("reqBack", reqBack);
        String forward = "";
        switch (from) {
            case 1:
                forward = "mostrarDetalle";
                break; //consultas
            case 2:
                forward = "mostrarDetalleLiq";
                break; //preliquidacion
            case 3:
                forward = "mostrarDetalleCan";
                break; //cancelacion
        }

        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        String m_CodigoCliente = empresaDAO.obtenerCodCliente(((LetrasForm) form).getM_Empresa());//se obtiene el codigo del cliente segun el ruc de la empresa
        empresaDAO = null;

        CashClientWS cashclienteWS = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();

        String tipoPaginado = request.getParameter("tipoPaginado");
    	BeanPaginacion bpag = null;
        
        if (tipoPaginado == null) {//verificamos si es la primera vez que se realiza la consulta
        	
        	
        	 listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_DET_PLAN_LETRAS));
             listaParametros.add(new BeanNodoXML("account_number", request.getParameter("m_Cuenta")));
             listaParametros.add(new BeanNodoXML("ruc",((LetrasForm) form).getM_Empresa()));
             listaParametros.add(new BeanNodoXML("num_mov", "0000000000"));
             String tipo = (String) session.getAttribute("tipoConsultaLetra");

             if ("G".equals(tipo)) {
                 listaParametros.add(new BeanNodoXML("client_code", m_CodigoCliente));
             } else {
                 listaParametros.add(new BeanNodoXML("client_code", m_CodigoCliente));
                 listaParametros.add(new BeanNodoXML("acep_code", request.getParameter("m_CodAcep")));            
             }
             try {
                 String req = GenRequestXML.getXML(listaParametros);
                 String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);

                 if (resultado == null || "".equals(resultado)) {
                     request.setAttribute("mensaje", "No se encontraron resultados");
                 } else {                
                     BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
                     //si la respuesta es exitosa
                     if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                         Propiedades prop = (Propiedades) context.getAttribute("propiedades");
                         int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_TOTAL_REG));
                         int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_NUM_REG));
                         BeanConsDetPlanLetras beancons = null;
                         
                         MessageResources messageResources = getResources(request);
                         int nroRegPag = send_number;
                         long cantItems = total_number;
                         
                         if(total_number!=0){
                        	 
                         int nroPag = (int) cantItems / nroRegPag;
                         int resto = (int) cantItems % nroRegPag;
                         if (resto != 0) {
                             nroPag = nroPag + 1;
                         }
                         bpag = new BeanPaginacion();
                         bpag.setM_pagActual(1);
                         bpag.setM_pagFinal(nroPag);
                         bpag.setM_pagInicial(1);
                         bpag.setM_regPagina(nroRegPag);
                         
                         //System.out.println("numero total:"+total_number);
                         //System.out.println("enviados:"+send_number);
                   
                         //System.out.println("numeros de pagina:"+nroPag);
                         //System.out.println("numeros por pagina:"+nroRegPag);
                         
                         session.setAttribute("send_number",send_number); 
                         session.setAttribute("beanPag", bpag);
                         session.setAttribute("tipoConsultaLetra",tipo);
                         session.setAttribute("m_Cuenta",request.getParameter("m_Cuenta"));
                         session.setAttribute("m_Moneda",request.getParameter("m_Moneda"));
                         session.setAttribute("m_CodAcep",request.getParameter("m_CodAcep"));
                         
                         
                      
                        	                         	                         
	                         if (from == 1) {//consulta
	                             beancons = ParserXML.parseConsultaDetPlanillaPag2(beanResXML,prop.getM_FormatoFechaWS1(), request.getParameter("m_Moneda"), request.getParameter("m_Cuenta"),
	                                     ((LetrasForm) form).getM_Empresa(),total_number,send_number,bpag.getM_pagActual(),tipo,m_CodigoCliente,request.getParameter("m_CodAcep"),
	                                     cashclienteWS,false);
	                         } else {//preLiq o cancel
	                             beancons = ParserXML.parseConsultaDetPlanillaPag2(beanResXML,prop.getM_FormatoFechaWS1(), request.getParameter("m_Moneda"), request.getParameter("m_Cuenta"),
	                                     ((LetrasForm) form).getM_Empresa(),total_number,send_number,bpag.getM_pagActual(),tipo,m_CodigoCliente,request.getParameter("m_CodAcep"),
	                                     cashclienteWS,true);
	                         }
                     
                         }
                         if (beancons != null) {
                             beancons.setM_CodigoCliente(m_CodigoCliente);
                             beancons.setM_RucEmpresa(((LetrasForm) form).getM_Empresa());
                             request.setAttribute("beanLetras", beancons);
                             if (beancons.getM_DetalleLetras() != null && beancons.getM_DetalleLetras().size() > 0) {
                                 request.setAttribute("listaResult", beancons.getM_DetalleLetras());
                             } else {
                                 request.setAttribute("mensaje", "No se encontraron movimientos");
                             }
                         } else {
                             request.setAttribute("mensaje", "No se encontraron movimientos");
                         }
                     } else if (beanResXML != null && beanResXML.getM_CodigoRetorno() != null ) {//&& beanResXML.getM_DescripcionError() != null && beanResXML.getM_DescripcionError().length() > 0
                         TxResultDao dao = new TxResultDaoHibernate();
                         TxResult txResult = dao.selectByCodIbs(beanResXML.getM_CodigoRetornoIBS());
                         if(txResult != null){
                             request.setAttribute("mensaje",txResult.getDrsDescription());// beanResXML.getM_DescripcionError()
                         }else{
                             request.setAttribute("mensaje",beanResXML.getM_CodigoRetornoIBS()+":"+Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);// beanResXML.getM_DescripcionError()
                         }
                         
                     } else {
                         request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
                     }
                 }
                 
             } catch (Exception ex) {
                 request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
                 logger.error(ex.toString());
             }

             
           
            
            
            
        } else {//si ya se realizo la consulta y solo se desea navegar por la paginacion
            bpag = (BeanPaginacion) session.getAttribute("beanPag");
            if ("P".equals(tipoPaginado)) {
                bpag.setM_pagActual(1);
            } else if ("U".equals(tipoPaginado)) {
                bpag.setM_pagActual(bpag.getM_pagFinal());
            } else if ("S".equals(tipoPaginado)) {
                if (bpag.getM_pagActual() < bpag.getM_pagFinal()) {
                    bpag.setM_pagActual(bpag.getM_pagActual() + 1);
                }

            } else if ("A".equals(tipoPaginado)) {
                if (bpag.getM_pagActual() > bpag.getM_pagInicial()) {
                    bpag.setM_pagActual(bpag.getM_pagActual() - 1);
                }
            }
            String numero_cuenta = (String)session.getAttribute("m_Cuenta");
            listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_DET_PLAN_LETRAS));
            listaParametros.add(new BeanNodoXML("account_number",numero_cuenta));
            listaParametros.add(new BeanNodoXML("ruc",((LetrasForm) form).getM_Empresa()));

            
            int secuencial = 0;
            int send_number = (Integer) session.getAttribute("send_number");
            if(bpag.getM_pagActual()>1){
            	secuencial = send_number*(bpag.getM_pagActual()-1); 
            	secuencial++;
            	 String num_movimiento = String.valueOf(secuencial);
                 while (num_movimiento.length() < 10) {
                     num_movimiento = "0" + num_movimiento;
                 }
                 listaParametros.add(new BeanNodoXML("num_mov", num_movimiento));
            }else{            	
            	 listaParametros.add(new BeanNodoXML("num_mov", "0000000000"));
            }
            
            
           
            
            String tipo = (String) session.getAttribute("tipoConsultaLetra");
            String moneda = (String)session.getAttribute("m_Moneda");
            String codigo_acep = (String)session.getAttribute("m_CodAcep");
        

            if ("G".equals(tipo)) {
                listaParametros.add(new BeanNodoXML("client_code", m_CodigoCliente));
            } else {
                listaParametros.add(new BeanNodoXML("client_code", m_CodigoCliente));
                listaParametros.add(new BeanNodoXML("acep_code", codigo_acep));            
            }
            
            
            try {
                String req = GenRequestXML.getXML(listaParametros);
                String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);

                if (resultado == null || "".equals(resultado)) {
                    request.setAttribute("mensaje", "No se encontraron resultados");
                } else {                
                    BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
                    //si la respuesta es exitosa
                    if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                        Propiedades prop = (Propiedades) context.getAttribute("propiedades");
                        int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_TOTAL_REG));
                        int send_number2 = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_NUM_REG));
                        BeanConsDetPlanLetras beancons = null;
                        
                        MessageResources messageResources = getResources(request);

                        
                        if (from == 1) {//consulta
                            beancons = ParserXML.parseConsultaDetPlanillaPag2(beanResXML,prop.getM_FormatoFechaWS1(), moneda, numero_cuenta,
                                    ((LetrasForm) form).getM_Empresa(),total_number,send_number,bpag.getM_pagActual(),tipo,m_CodigoCliente,codigo_acep,
                                    cashclienteWS,false);
                        } else {//preLiq o cancel
                            beancons = ParserXML.parseConsultaDetPlanillaPag2(beanResXML,prop.getM_FormatoFechaWS1(), moneda, numero_cuenta,
                                    ((LetrasForm) form).getM_Empresa(),total_number,send_number,bpag.getM_pagActual(),tipo,m_CodigoCliente,codigo_acep,
                                    cashclienteWS,true);
                        }
                    
                        
                        if (beancons != null) {
                            beancons.setM_CodigoCliente(m_CodigoCliente);
                            beancons.setM_RucEmpresa(((LetrasForm) form).getM_Empresa());
                            request.setAttribute("beanLetras", beancons);
                            if (beancons.getM_DetalleLetras() != null && beancons.getM_DetalleLetras().size() > 0) {
                                request.setAttribute("listaResult", beancons.getM_DetalleLetras());
                            } else {
                                request.setAttribute("mensaje", "No se encontraron movimientos");
                            }
                        } else {
                            request.setAttribute("mensaje", "No se encontraron movimientos");
                        }
                    } else if (beanResXML != null && beanResXML.getM_CodigoRetorno() != null ) {//&& beanResXML.getM_DescripcionError() != null && beanResXML.getM_DescripcionError().length() > 0
                        TxResultDao dao = new TxResultDaoHibernate();
                        TxResult txResult = dao.selectByCodIbs(beanResXML.getM_CodigoRetornoIBS());
                        if(txResult != null){
                            request.setAttribute("mensaje",txResult.getDrsDescription());// beanResXML.getM_DescripcionError()
                        }else{
                            request.setAttribute("mensaje",beanResXML.getM_CodigoRetornoIBS()+":"+Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);// beanResXML.getM_DescripcionError()
                        }
                        
                    } else {
                        request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
                    }
                }
                
            } catch (Exception ex) {
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
                logger.error(ex.toString());
            }
            
            
        }

        if(bpag!=null){
        	session.setAttribute("beanPag", bpag);        	
        }
        
        request.setAttribute("bandResult", "si");
        //System.out.println("Tipo paginacion:"+tipoPaginado);
        //System.out.println("pagina actual:"+bpag.getM_pagActual());
        
       
        return mapping.findForward(forward);

    }
    
    public ActionForward exportarPlanillaLetras(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			    throws IOException, ServletException {
			
    	
    		HttpSession session = request.getSession();
			//si termino la session debemos retornar al inicio
			

	    	String tipoPaginado = request.getParameter("tipoPaginado");
	    	BeanPaginacion bpag = null;
	        

	        ServletContext context = getServlet().getServletConfig().getServletContext();

	        if (session.getAttribute("usuarioActual") == null) {
	            response.sendRedirect("cierraSession.jsp");
	            return null;
	        }
	        int from = Integer.parseInt(request.getParameter("from"));        
	        String reqBack="m_Empresa="+((LetrasForm) form).getM_Empresa()+"&m_FecInicio="+((LetrasForm) form).getM_FecInicio()+"&m_FecFin="+((LetrasForm) form).getM_FecFin()+"&m_Tipo="+session.getAttribute("tipoConsultaLetra");
	        request.setAttribute("reqBack", reqBack);
	        String forward = "";
	        switch (from) {
	            case 1:
	                forward = "mostrarDetalle";
	                break; //consultas
	            case 2:
	                forward = "mostrarDetalleLiq";
	                break; //preliquidacion
	            case 3:
	                forward = "mostrarDetalleCan";
	                break; //cancelacion
	        }

	        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
	        String m_CodigoCliente = empresaDAO.obtenerCodCliente(((LetrasForm) form).getM_Empresa());//se obtiene el codigo del cliente segun el ruc de la empresa
	        empresaDAO = null;

	        CashClientWS cashclienteWS = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
	        ArrayList listaParametros = new ArrayList();

	        String numero_cuenta = (String)session.getAttribute("m_Cuenta");
	        listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_DET_PLAN_LETRAS));
	        listaParametros.add(new BeanNodoXML("account_number",numero_cuenta));
	        listaParametros.add(new BeanNodoXML("ruc",((LetrasForm) form).getM_Empresa()));
	        listaParametros.add(new BeanNodoXML("num_mov", "0000000000"));
	        String tipo = (String) session.getAttribute("tipoConsultaLetra");

	        if ("G".equals(tipo)) {
	            listaParametros.add(new BeanNodoXML("client_code", m_CodigoCliente));
	        } else {
	            listaParametros.add(new BeanNodoXML("client_code", m_CodigoCliente));
	            
	            /*if(request.getParameter("m_CodAcep")!= null){   //beancons != null	            	
	            	listaParametros.add(new BeanNodoXML("acep_code", request.getParameter("m_CodAcep")));
	            }*/	            
	            
	            
	            //System.out.println("COD ACEPTA="+request.getParameter("m_CodAcep"));
	            listaParametros.add(new BeanNodoXML("acep_code", "99999999999"));
	            
	            /*if(request.getParameter("m_CodAcep")!= null ){   //beancons != null	            	
	            	//listaParametros.add(new BeanNodoXML("acep_code", request.getParameter("m_CodAcep")));
	            	listaParametros.add(new BeanNodoXML("acep_code", "99999999999"));
	            }*/
	            
	            
	            
	        }
	        BeanConsDetPlanLetras beancons = null;
	        try {
	            String req = GenRequestXML.getXML(listaParametros);
	            String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);

	            if (resultado == null || "".equals(resultado)) {
	                request.setAttribute("mensaje", "No se encontraron resultados");
	            } else {                
	                BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
	                //si la respuesta es exitosa
	                if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
	                    Propiedades prop = (Propiedades) context.getAttribute("propiedades");
	                    int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_TOTAL_REG));
	                    int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_NUM_REG));
	                    
	                    
	                    //System.out.println("numero total:"+total_number);
	                    //System.out.println("enviados:"+send_number);
	                    String moneda = (String)session.getAttribute("m_Moneda");
	                    String codigo_acep = (String)session.getAttribute("m_CodAcep");
	                   
	                   
	                    if (from == 1) {//consulta
	                        beancons = ParserXML.parseConsultaDetPlanillaPag(beanResXML.getM_Respuesta(),prop.getM_FormatoFechaWS1(), moneda,numero_cuenta,
	                                ((LetrasForm) form).getM_Empresa(),total_number,send_number,tipo,m_CodigoCliente,codigo_acep,
	                                cashclienteWS,false);
	                    } else {//preLiq o cancel
	                        beancons = ParserXML.parseConsultaDetPlanillaPag(beanResXML.getM_Respuesta(),prop.getM_FormatoFechaWS1(), moneda, numero_cuenta,
	                                ((LetrasForm) form).getM_Empresa(),total_number,send_number,tipo,m_CodigoCliente,codigo_acep,
	                                cashclienteWS,true);
	                    }
	                    
	                   
	                    
	                    if (beancons != null) {
	                        beancons.setM_CodigoCliente(m_CodigoCliente);
	                        beancons.setM_RucEmpresa(((LetrasForm) form).getM_Empresa());
	                        request.setAttribute("beanLetras", beancons);
	                        if (beancons.getM_DetalleLetras() != null && beancons.getM_DetalleLetras().size() > 0) {
	                            request.setAttribute("listaResult", beancons.getM_DetalleLetras());
	                        } else {
	                            request.setAttribute("mensaje", "No se encontraron movimientos");
	                        }
	                    } else {
	                        request.setAttribute("mensaje", "No se encontraron movimientos");
	                    }
	                } else if (beanResXML != null && beanResXML.getM_CodigoRetorno() != null ) {//&& beanResXML.getM_DescripcionError() != null && beanResXML.getM_DescripcionError().length() > 0
	                    TxResultDao dao = new TxResultDaoHibernate();
	                    TxResult txResult = dao.selectByCodIbs(beanResXML.getM_CodigoRetornoIBS());
	                    if(txResult != null){
	                        request.setAttribute("mensaje",txResult.getDrsDescription());// beanResXML.getM_DescripcionError()
	                    }else{
	                        request.setAttribute("mensaje",beanResXML.getM_CodigoRetornoIBS()+":"+Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);// beanResXML.getM_DescripcionError()
	                    }
	                    
	                } else {
	                    request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
	                }
	            }
	            
	        } catch (Exception ex) {
	            request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
	            logger.error(ex.toString());
	        }


			
			
			List resultado = null;
			ArrayList lblColumnas = new ArrayList();
			ArrayList lstData = new ArrayList();
			BeanDetalleLetra bean = null;
		    
		    resultado = beancons.getM_DetalleLetras();
			  if (resultado != null && resultado.size() > 0) {
			        lblColumnas.add("Numero Letra");
			        lblColumnas.add("Fecha Venc.");
			        lblColumnas.add("Moneda");
			        lblColumnas.add("Importe Letra");
			        lblColumnas.add("Numero");
			        lblColumnas.add("Nombre");
			        lblColumnas.add("Estado");
			        			           
			            for (int i = 0; i < resultado.size(); i++) {
			                bean = (BeanDetalleLetra)resultado.get(i);
			                lstData.add(new String[]{
			                            bean.getM_NumLetra(),
			                            bean.getM_FechVenc(),
			                            bean.getM_Moneda(),
			                            bean.getM_ImpLetra(),
			                            bean.getM_NumUser(),
			                            bean.getM_NomUser(),
			                            bean.getM_Estado()
			                            
			                        });
			            }
			   
			        
			  }       
			
			if (resultado == null || resultado.size() < 1) {
			    request.setAttribute("mensaje", "No se encontraron resultados");
			    return mapping.findForward("mostrarDetalle");
			}
			
			//verificamos si se quiere exportar a formato de texto
			String accion = request.getParameter("accion");
			String formato = request.getParameter("formato");
			if ("save".equalsIgnoreCase(accion) && "txt".equalsIgnoreCase(formato)) { //descarga texto
			    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			    String nombre_archivo = "consulta-" + sdf.format(new Date()) + ".txt";
			    response.setHeader("Content-Disposition", "attachment; filename=\"" + nombre_archivo + "\"");
			    response.setContentType("text/plain");
			    //creamos la cadena de texto a descargar
			    StringBuilder strBuilder = new StringBuilder();
			
			    //jwong 16/08/2009 colocamos titulo al reporte generado
			    strBuilder.append("\t\t\tDETALLE DE LETRA\r\n\r\n");
			
			    String lblAux = null;
			    int anchoCol = 0;
			
			    for (int g = 0; g < lblColumnas.size(); g++) {
			        //1ero creamos los titulos de las columnas
			        //jwong 16/08/2009 comentado para alineacion de columnas
			
			        if (g < 2) { //aplicable a las 2 primeras columnas
			            anchoCol = 16;
			        } else {
			            anchoCol = 50;
			        }
			
			        lblAux = (String) lblColumnas.get(g);
			        if ("Código Respuesta".equalsIgnoreCase(lblAux)) {
			            anchoCol = 82;
			        }
			        strBuilder.append(Util.ajustarDato(lblAux, anchoCol + 1));
			        if (g == lblColumnas.size() - 1) {
			            strBuilder.append("\r\n");
			        }
			    }
			
			 
			    //data
			    for (int h = 0; h < lstData.size(); h++) {
			        String fila[] = (String[]) lstData.get(h);
			        for (int k = 0; k < fila.length; k++) {
			            //jwong 16/08/2009 comentado para alineacion de columnas
			            //strBuilder.append((String)fila[k]);
			            if (k < 2) { //aplicable a las 2 primeras columnas
			                anchoCol = 16;
			            } else {
			                anchoCol = 50;
			            }
			            lblAux = (String) fila[k];
			            String lblTit = (String) lblColumnas.get(k);
			            if ("Código Respuesta".equalsIgnoreCase(lblTit)) {
			                anchoCol = 82;
			            }
			            strBuilder.append(Util.ajustarDato(lblAux, anchoCol + 1));
			            if (k == fila.length - 1) {
			                strBuilder.append("\r\n");  
			            }
			        }
			    }
			
			    PrintWriter out = new PrintWriter(response.getOutputStream());
			    out.println(strBuilder);
			    out.flush();
			    out.close();
			
			    response.getOutputStream().flush();
			    response.getOutputStream().close();
			
			   
				} else if ("save".equalsIgnoreCase(accion) && "excel".equalsIgnoreCase(formato)) { //descarga excel
				    //se realizara la descarga usando POI
				    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
				    String nombre_archivo = "consulta-" + sdf.format(new Date()) + ".xls";
				    response.setHeader("Content-Disposition", "attachment; filename=\"" + nombre_archivo + "\"");
				    response.setContentType("application/vnd.ms-excel");
				
				    String[]cabecera = {numero_cuenta,m_CodigoCliente};
				    HSSFWorkbook libroXLS = GeneradorPOI.crearExcelLetras(nombre_archivo, lblColumnas, lstData, cabecera, "LETRA");
				    if (libroXLS != null) {
				        libroXLS.write(response.getOutputStream());
				        response.getOutputStream().close();
				        response.getOutputStream().flush();
				    }
				   
				}
			
			 return null;
			  
}
    
    

    public ActionForward consultaPreLiqLetxCan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        CashClientWS cashclienteWS = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        String parametros[] = request.getParameterValues("m_selec");
        //m_selec=nroLetra,codAcep,fechVen,importe,moneda,estado
        ArrayList listaCalcularLetras = new ArrayList();
        for (int i = 0; i < parametros.length; i++) {// cantidad de letras seleccionadas
            String par[] = parametros[i].split("&");
            ArrayList listaParametros = new ArrayList();
            listaParametros.add(new BeanNodoXML("account_number", request.getParameter("m_Cuenta")));
            listaParametros.add(new BeanNodoXML("num_letra", par[0]));
            listaParametros.add(new BeanNodoXML("acep_code", par[1]));
            listaParametros.add(new BeanNodoXML("ruc", par[6]));
            listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_PRELIQ_CANC_LETRAS));
            try {
                String req = GenRequestXML.getXML(listaParametros);
                String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);

                if (resultado == null || "".equals(resultado)) {
                    request.setAttribute("mensaje", "No se encontraron resultados");
                } else {                    
                    BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
                    //si la respuesta es exitosa
                    if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                        Propiedades prop = (Propiedades) context.getAttribute("propiedades");
                        BeanLetra beancons = ParserXML.parseConsultaPreLiqLetra(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWSLastMov());
                        if (beancons != null) {
                            //añadir atributos q seran mostrados en la consulta
                            beancons.setM_NumLetra(par[0]);

                            //jwong 20/04/2009se agrega la moneda a la consulta
                            beancons.setM_Moneda(par[4]);

//                            beancons.setM_Estado(par[5]);
                            beancons.setM_ImporteAnt(par[3]);
                            beancons.setM_respuesta("si");
                            calculcarITFCan(cashclienteWS, request.getParameter("m_Cuenta"), beancons,par[6]);//jmoreno 27-08-09
                            listaCalcularLetras.add(beancons);
                        }else{
                                beancons = new BeanLetra();
                                beancons.setM_respuesta("no");
                                beancons.setM_mensaje("Error en formato de respuesta");
                                beancons.setM_NumLetra(par[0]);
                                listaCalcularLetras.add(beancons);
                            }
                    } else if (beanResXML != null && beanResXML.getM_CodigoRetorno() != null ) {//&& beanResXML.getM_DescripcionError() != null && beanResXML.getM_DescripcionError().length() > 0

                        BeanLetra beancons = new BeanLetra();
                        beancons.setM_respuesta("no");
                        TxResultDao dao = new TxResultDaoHibernate();
                        TxResult txResult = dao.selectByCodIbs(beanResXML.getM_CodigoRetornoIBS());
                        if(txResult != null){
                            beancons.setM_mensaje(txResult.getDrsDescription());// beanResXML.getM_DescripcionError()
                        }else{
                            beancons.setM_mensaje(beanResXML.getM_CodigoRetornoIBS()+":"+Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);// beanResXML.getM_DescripcionError()
                        }
                        
                        beancons.setM_NumLetra(par[0]);
                        listaCalcularLetras.add(beancons);
                    } else {
//                            request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
                        BeanLetra beancons = new BeanLetra();
                        beancons.setM_respuesta("no");
                        beancons.setM_mensaje("Se encontraron problemas al procesar la información");
                        beancons.setM_NumLetra(par[0]);
                        listaCalcularLetras.add(beancons);
                    }
                }
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
        request.setAttribute("listaResult", listaCalcularLetras);
        return mapping.findForward("consultaPreLiqLetxCan");
    }

    public ActionForward mostrarPreLiqLetxRenv(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String parametros = request.getParameter("m_selec");
        //m_selec=nroLetra,codAcep,fechVen,importe,moneda,estado
        String par[] = parametros.split("&");
        String datos = "account_number=" + request.getParameter("m_Cuenta") + "&num_letra=" + par[0] + "&acep_code=" + par[1];
        request.setAttribute("datos", datos);
        request.setAttribute("num_letra", par[0]);
        request.setAttribute("fecha_venc", par[2]);
        request.setAttribute("importe_actual", par[3]);
        request.setAttribute("moneda_letra", par[4]);
        request.setAttribute("ruc_empresa", par[6]);
        
        return mapping.findForward("mostrarPreLiqLetxRenv");
    }

    public ActionForward consultaPreLiqLetxRen(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        CashClientWS cashclienteWS = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        listaParametros.add(new BeanNodoXML("account_number", request.getParameter("account_number")));
        listaParametros.add(new BeanNodoXML("num_letra", request.getParameter("num_letra")));
        listaParametros.add(new BeanNodoXML("acep_code", request.getParameter("acep_code")));
        listaParametros.add(new BeanNodoXML("ruc", request.getParameter("ruc_empresa")));
        double montoaux = 0.0;
        try {
            //montoaux = ((Math.round((Double.parseDouble(request.getParameter("monto_abonar")))*Math.pow(10,2))/Math.pow(10,2)))*100;
            montoaux = ((Math.round((Util.convertirDouble(request.getParameter("monto_abonar")))*Math.pow(10,2))/Math.pow(10,2)))*100;
        } catch (NumberFormatException nfe) {
            logger.error(nfe.toString());
            montoaux = 0.0;
        }
        long nvoMonto = (long) montoaux;
        listaParametros.add(new BeanNodoXML("monto_abonar", String.valueOf(nvoMonto)));
        listaParametros.add(new BeanNodoXML("nuevo_fecha_venc", Fecha.formatearFecha("dd/MM/yyyy","ddMMyy",request.getParameter("nuevo_fecha_venc"))));
        listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_PRELIQ_RENOV_LETRAS));
        //try {
            String req = GenRequestXML.getXML(listaParametros);
            String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
            String mensaje = "<?xml version='1.0' encoding=\"ISO-8859-1\"?><Respuestas><respuesta valor='No se encontraron resultados'/></Respuestas>";
            if (resultado != null && !("".equals(resultado))) {                 
                BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
                //si la respuesta es exitosa
                if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                    Propiedades prop = (Propiedades) context.getAttribute("propiedades");
                    //procesamos la respuesta(parseo xml) y enviamos a la pagina
                    BeanLetra beancons = ParserXML.parseConsultaPreLiqLetra(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS());

                    if (beancons != null) {
                        calculcarITF(cashclienteWS, request.getParameter("account_number"), beancons,request.getParameter("monto_abonar"),request.getParameter("ruc_empresa"));

                        mensaje = ParserXML.obtenerRespLetxRen(beancons);

                    }
                } else if (beanResXML != null && beanResXML.getM_CodigoRetorno() != null ) {//&& beanResXML.getM_DescripcionError() != null && beanResXML.getM_DescripcionError().length() > 0
                    TxResultDao dao = new TxResultDaoHibernate();
                    TxResult txResult = dao.selectByCodIbs(beanResXML.getM_CodigoRetornoIBS());
                    if(txResult != null){
                        mensaje = "<?xml version='1.0' encoding=\"ISO-8859-1\"?><Respuestas><respuesta valor='"+txResult.getDrsDescription()+"'/></Respuestas>";
                    }else{
                        mensaje = "<?xml version='1.0' encoding=\"ISO-8859-1\"?><Respuestas><respuesta valor='"+beanResXML.getM_CodigoRetornoIBS()+":"+Constantes.CODIGO_RPTA_IBS_DESCONOCIDO+"'/></Respuestas>";
                    }
                    

                } else {
                    mensaje = "<?xml version='1.0' encoding=\"ISO-8859-1\"?><Respuestas><respuesta valor='Se encontraron problemas al procesar la información'/></Respuestas>";//Se encontraron problemas al procesar la información
                }//'windows-1252'

            }
            PrintWriter out = response.getWriter();
            response.setContentType("application/xml");
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            out.println(mensaje);
            out.flush();
        
        return null;
    }
    
    private void calculcarITF(CashClientWS cashws, String cuenta, BeanLetra bean,String nvoMontoAbonado,String rucEmpresa) {
        ArrayList listaParam = new ArrayList();
        listaParam.add(new BeanNodoXML("id_trx", Constantes.IBS_CALCULA_ITF));
        listaParam.add(new BeanNodoXML("cuenta", cuenta));
        listaParam.add(new BeanNodoXML("moneda", bean.getM_Moneda()));
        listaParam.add(new BeanNodoXML("num_planilla", bean.getM_NumPlanilla()));
        listaParam.add(new BeanNodoXML("ruc", rucEmpresa));
        double monto = 0;

        //jwong 30/04/2006 correccion para manejo de falla en conversion numerica
        double aux_mora=0.0;
        double aux_portes=0.0;
        double aux_protesto=0.0;
        double aux_principal=0.0;//Monto anterior de Letra
        double aux_principalPend=0.0;//comision de renovacion
        double nuevo_monto = 0.0;
        double aux_pagoInteres = 0.0;
        double amortizacion = 0.0;
        int nroDecimales = 2;
        try{            
            nuevo_monto = Util.convertirDouble(nvoMontoAbonado);//Double.parseDouble(nvoMontoAbonado);
            aux_mora = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_Mora(), nroDecimales));
            aux_portes = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_Portes(), nroDecimales));
            aux_protesto = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_Protesto(), nroDecimales));
            aux_principal = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_PagoPrincipal(), nroDecimales));//Monto de letra anterior
            aux_principalPend = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_PrincipalPend(), nroDecimales));//Comision de Renovacion
            aux_pagoInteres = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_PagoInteres(), nroDecimales));
            amortizacion = aux_principal - nuevo_monto;
            if("-".equalsIgnoreCase(bean.getM_SignoPagInt())){
                monto = aux_mora + aux_portes + aux_protesto + amortizacion + aux_principalPend;
            }else{
                monto = aux_mora + aux_portes + aux_protesto + amortizacion + aux_principalPend + aux_pagoInteres;
            }
            /* jmoreno: Se cambio el calculo del importe Total, de acuerdo a Finnesse
             if("".equals(bean.getM_PagoPrincipal()) || " ".equals(bean.getM_PagoPrincipal())){
                bean.setM_PagoPrincipal(bean.getM_PrincipalPend());
                monto = aux_mora + aux_portes + aux_protesto + (aux_principalPend - nuevo_monto)+aux_pagoInteres;
            }else{
                monto = aux_mora + aux_portes + aux_protesto + (aux_principal - nuevo_monto)+ aux_principalPend+aux_pagoInteres;
            }*/
        }catch(NumberFormatException nfe){
            logger.error(nfe.toString());
            monto = 0.0;
        }      
        String aux_ImporteFinal = Util.formatearMontoNvo(String.valueOf(monto)).replace(",", "");
        aux_ImporteFinal = Util.formatearMontoSalida(aux_ImporteFinal, nroDecimales);
        bean.setM_ImporteFinal(aux_ImporteFinal);        
        String m_amortizacion = Util.formatearMontoNvo(String.valueOf(amortizacion)).replace(",", "");
        m_amortizacion = Util.formatearMontoSalida(m_amortizacion, nroDecimales);
        bean.setM_amortizacion(m_amortizacion);

        listaParam.add(new BeanNodoXML("importe",aux_ImporteFinal));
        String req = GenRequestXML.getXML(listaParam);
        String resultado = cashws.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if (resultado == null || "".equals(resultado)) {
            //System.out.println("No se encontraron resultados");
        } else {
            BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
            //si la respuesta es exitosa
            if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                String resp = ParserXML.obtenerItf(beanResXML.getM_Respuesta());
                bean.setM_Itf(resp.substring(0,resp.indexOf("&")));
                bean.setM_ndec_itf(resp.substring(resp.indexOf("&")+1));
            }
        }
    }

    private void calculcarITFCan(CashClientWS cashws, String cuenta, BeanLetra bean,String rucEmpresa) {
        ArrayList listaParam = new ArrayList();
        listaParam.add(new BeanNodoXML("id_trx", Constantes.IBS_CALCULA_ITF));
        listaParam.add(new BeanNodoXML("cuenta", cuenta));
        listaParam.add(new BeanNodoXML("moneda", bean.getM_Moneda()));
        listaParam.add(new BeanNodoXML("num_planilla", bean.getM_NumPlanilla()));
        listaParam.add(new BeanNodoXML("ruc", rucEmpresa));
        double monto = 0;

        //jwong 30/04/2006 correccion para manejo de falla en conversion numerica
        double aux_mora=0.0;
        double aux_portes=0.0;
        double aux_protesto=0.0;
        double aux_principal=0.0;
//        double aux_principalPend=0.0;

        //jwong 29/04/2009 comentado
         int nroDecimales = 2;
        try{           

            aux_mora = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_Mora(), nroDecimales));
            aux_portes = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_Portes(), nroDecimales));
            aux_protesto = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_Protesto(), nroDecimales));
            aux_principal = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_PagoPrincipal(), nroDecimales));
//            aux_principalPend = Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_PrincipalPend(), nroDecimales));
            monto = aux_mora + aux_portes + aux_protesto + aux_principal;
            /* jmoreno 08-09-09 Se cambio la manera de obtener el monto Total, de acuerdo a Finnesse
             if("".equals(bean.getM_PagoPrincipal()) || " ".equals(bean.getM_PagoPrincipal())){
                bean.setM_PagoPrincipal(bean.getM_PrincipalPend());                
                monto = aux_mora + aux_portes + aux_protesto + aux_principalPend;
            }else{
                monto = aux_mora + aux_portes + aux_protesto + aux_principal;
            }*/
        }catch(NumberFormatException nfe){
            logger.error(nfe.toString());
            monto = 0.0;
        }
        String montoaux = Util.formatearMontoNvo(String.valueOf(monto)).replace(",", "");
        montoaux = Util.formatearMontoSalida(montoaux, nroDecimales);
        listaParam.add(new BeanNodoXML("importe",montoaux));
        String req = GenRequestXML.getXML(listaParam);
        String resultado = cashws.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if (resultado == null || "".equals(resultado)) {
            //System.out.println("No se encontraron resultados");
        } else {
            BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
            //si la respuesta es exitosa
            if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                String resp = ParserXML.obtenerItf(beanResXML.getM_Respuesta());
                bean.setM_Itf(resp.substring(0,resp.indexOf("&")));
                bean.setM_ndec_itf(resp.substring(resp.indexOf("&")+1));
                try {                    
                    monto = (Util.convertirDouble(Util.formatearMontoSinEspacio(bean.getM_Itf(), nroDecimales))) + monto;

                } catch (NumberFormatException nfe) {
                    logger.error(nfe.toString());
                    monto = 0.0;
                }
                montoaux = Util.formatearMontoNvo(String.valueOf(monto)).replace(",", "");
                montoaux = Util.formatearMontoSalida(montoaux, nroDecimales);
                bean.setM_ImporteFinal(montoaux);
            }
        }
    }

    public ActionForward consultaCancelacionLet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
		//se agrega estas lineas para el bloqueo de los modulos
		else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
		//fin
		
		
		
        
        String mensaje = "";
        CashClientWS cashclienteWS = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);

        //jwong 04/08/2009 modificado
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML)session.getAttribute("usuarioActual");
        List listaccounts = obtenerCuentas(cashclienteWS, request.getParameter("m_CodigoCliente"), beanDataLogXML.getM_NumTarjeta(), beanDataLogXML.getM_Token(), request.getParameter("m_RucEmpresa") );
        if (listaccounts != null && listaccounts.size() > 0) {
            request.setAttribute("listaccounts", listaccounts);
            String parametros[] = request.getParameterValues("m_selec");
            //m_selec=nroLetra,codAcep,fechVen,importe,moneda,estado
            ArrayList listaCalcularLetras = new ArrayList();
            for (int i = 0; i < parametros.length; i++) {// de acuerdo a la cantidad de letras seleccionadas
                String par[] = parametros[i].split("&");
                ArrayList listaParametros = new ArrayList();
                listaParametros.add(new BeanNodoXML("account_number", request.getParameter("m_Cuenta")));//
                listaParametros.add(new BeanNodoXML("num_letra", par[0]));
                listaParametros.add(new BeanNodoXML("acep_code", par[1]));
                listaParametros.add(new BeanNodoXML("ruc", par[6]));
                listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_PRELIQ_CANC_LETRAS));
                try {
                    String req = GenRequestXML.getXML(listaParametros);
                    String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);

                    if (resultado == null || "".equals(resultado)) {
                        mensaje = "No se encontraron resultados";
                    } else {
                        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
                        //si la respuesta es exitosa
                        if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                            Propiedades prop = (Propiedades) context.getAttribute("propiedades");
                            BeanLetra beancons = ParserXML.parseConsultaPreLiqLetra(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS());
                            if (beancons != null) {
                                //añadir atributos q seran mostrados en la consulta
                                beancons.setM_NumLetra(par[0]);
                                beancons.setM_CodAceptante(par[1]);
//                            beancons.setM_Estado(par[5]);
                                beancons.setM_ImporteAnt(par[3]);
                                beancons.setM_Moneda(par[4]);
                                beancons.setM_respuesta("si");
                                calculcarITFCan(cashclienteWS, request.getParameter("m_Cuenta"), beancons,par[6]);//jmoreno 27-08-09
                                listaCalcularLetras.add(beancons);
                            }else{
                                beancons = new BeanLetra();
                                beancons.setM_respuesta("no");
                                beancons.setM_mensaje("Error en formato de respuesta");
                                beancons.setM_NumLetra(par[0]);
                                listaCalcularLetras.add(beancons);
                            }
                        } else if (beanResXML != null && beanResXML.getM_CodigoRetorno() != null ) {//&& beanResXML.getM_DescripcionError() != null && beanResXML.getM_DescripcionError().length() > 0
                                BeanLetra beancons = new BeanLetra();
                                beancons.setM_respuesta("no");
                                TxResultDao dao = new TxResultDaoHibernate();
                                TxResult txResult = dao.selectByCodIbs(beanResXML.getM_CodigoRetornoIBS());
                                if(txResult != null){
                                    beancons.setM_mensaje(txResult.getDrsDescription());
                                }else{
                                    beancons.setM_mensaje(beanResXML.getM_CodigoRetornoIBS()+":"+Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);
                                }
                                //beancons.setM_mensaje(beanResXML.getM_DescripcionError());
                                beancons.setM_NumLetra(par[0]);
                                listaCalcularLetras.add(beancons);
                        } else {
                                BeanLetra beancons = new BeanLetra();
                                beancons.setM_respuesta("no");
                                beancons.setM_mensaje("Se encontraron problemas al procesar la información");
                                beancons.setM_NumLetra(par[0]);
                                listaCalcularLetras.add(beancons);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            }
            session.setAttribute("listaLetras", listaCalcularLetras);
        } else {

            mensaje = "La transaccion obtener cuentas no devuelve resultado";
        }
        request.setAttribute("mensaje", mensaje);
        request.setAttribute("m_RucEmpresa", request.getParameter("m_RucEmpresa"));
        return mapping.findForward("consultaCancelacionLet");
    }

    public ActionForward cancelacionLetras(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
		
		//se agrega estas lineas para el bloqueo de modulos
		try {
			if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
				return mapping.findForward("fueraServicio");
			}
		} catch (Exception e) {
			logger.error("VALIDACION DE DISPONIBILIDAD", e);
			return mapping.findForward("fueraServicio");
		}
		//fin		
			
        TaServicioxEmpresaDao taServxEmpDao = new TaServicioxEmpresaDaoHibernate();
        long idServxEmp = taServxEmpDao.selectCodeServicioxEmpresa_2(request.getParameter("m_RucEmpresa"), "21");
        if (idServxEmp != -1) {
            HttpSession session = request.getSession();
            ArrayList<BeanLetra> listaResult = (ArrayList<BeanLetra>) session.getAttribute("listaLetras");
            boolean bandDet = false;
            for (BeanLetra beanLetra : listaResult) {
                if ("si".equals(beanLetra.getM_respuesta())) {
                    bandDet = true;
                }
            }
            if (bandDet) {
                TaSecuencialDao taSecdao = new TaSecuencialDaoHibernate();
                int idOrden = taSecdao.getIdEnvio(Constantes.FIELD_CASH_SECUENCIAL_ID_ORDEN);
                String param[] = request.getParameter("m_cta_cargo").split("&");
                TaOrdenDao ordenDao = new TaOrdenDaoHibernate();
                TaOrden taorden = new TaOrden();
                taorden.setId(new TaOrdenId());
                taorden.getId().setCorIdServicioEmpresa(idServxEmp);
                taorden.getId().setCorIdOrden(idOrden);
                taorden.setForFechaInicio(Fecha.getFechaActual("yyyyMMdd"));
                taorden.setForFechaRegistro(Fecha.getFechaActual("yyyyMMdd"));
                taorden.setForFechaFin(Fecha.getFechaActual("yyyyMMdd"));
                taorden.setHorHoraInicio(Fecha.getFechaActual("HHmmss"));
                taorden.setNorNumeroCuenta(param[0]);
                taorden.setDorTipoCuenta(param[1]);
                taorden.setDorReferencia("REF" + idOrden);
                taorden.setCorEstadoMontoDolares('0'); //jmoreno 25/08/09
                taorden.setCorEstadoMontoSoles('0');
                taorden.setCorEstadoMontoEuros('0');
                //jwong 11/05/2009 - Verifica si el servicio tiene aprobacion automatica
                TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
                TaServicioxEmpresa objservicioxempresa = servicioxempresaDAO.selectServicioxEmpresa(request.getParameter("m_RucEmpresa"), "21");
                if (String.valueOf(Constantes.HQL_CASH_FLAG_APROB_AUT_ENABLED).equalsIgnoreCase(String.valueOf(objservicioxempresa.getCsemFlagAprobAut()))) {
                    taorden.setCorEstado(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
                } else {
                    taorden.setCorEstado(Constantes.HQL_CASH_ESTADO_ORDEN_INGRESADO);
                }

                boolean guardar = ordenDao.insert(taorden);
                if (guardar) {
                    List listadetorden = new ArrayList();
                    int contador = 1;
                    for (BeanLetra beanLetra : listaResult) {
                        if ("si".equals(beanLetra.getM_respuesta())) {
                            TpDetalleOrden tpdetorden = new TpDetalleOrden();
                            tpdetorden.setId(new TpDetalleOrdenId());
                            tpdetorden.getId().setCdoidDetalleOrden(contador++);
                            tpdetorden.getId().setCdoidOrden(idOrden);
                            tpdetorden.getId().setCdoidServicioEmpresa(idServxEmp);
                            tpdetorden.setCdoestado(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);//jmoreno 26-06-09
                            tpdetorden.setDdonombre(beanLetra.getM_Aceptante());
                            tpdetorden.setNdocodAceptante(beanLetra.getM_CodAceptante());
                            tpdetorden.setNdonumPlanilla(beanLetra.getM_NumPlanilla());
                            tpdetorden.setNdodocumento(beanLetra.getM_NumLetra());

                            //jwong 20/04/2009 se inserta adicionalmente la mora en bd
                            tpdetorden.setCdomonedaMontoMora(beanLetra.getM_Moneda());
                            tpdetorden.setNdomontoMora(new BigDecimal(Util.formatearMontoSinEspacio(beanLetra.getM_Mora(), 2)).setScale(2));

                            //jmoreno 12-05-09 añadir itf y portes en la base de datos
                            tpdetorden.setCdomonedaItf(beanLetra.getM_Moneda());
                            tpdetorden.setNdoitf(new BigDecimal(Util.formatearMontoSinEspacio(beanLetra.getM_Itf(), 2)).setScale(2));

                            tpdetorden.setCdomonedaPortes(beanLetra.getM_Moneda());
                            tpdetorden.setNdoportes(new BigDecimal(Util.formatearMontoSinEspacio(beanLetra.getM_Portes(), 2)).setScale(2));

                            tpdetorden.setNdoprotesto(new BigDecimal(Util.formatearMontoSinEspacio(beanLetra.getM_Protesto(), 2)).setScale(2));
                            tpdetorden.setCdomonedaProtesto(beanLetra.getM_Moneda());
                            //jwong 11/05/2009 para guardar el numero de cuenta de la cancelacion de la letra
                            tpdetorden.setNdonumeroCuenta(taorden.getNorNumeroCuenta());
                            tpdetorden.setDdoreferencia(taorden.getDorReferencia());
                            tpdetorden.setDdonombreBenef(beanLetra.getM_Cliente());

//                            double montoaux = 0.0;
//                            try {
//                                montoaux = ((Math.round(((Util.convertirDouble(beanLetra.getM_ImporteFinal())) / 100) * Math.pow(10, 2)) / Math.pow(10, 2)));
//
//                            } catch (NumberFormatException nfe) {
//                                nfe.printStackTrace();
//                                montoaux = 0.0;
//                            }
//                            String aux = String.valueOf(montoaux);
                            String aux =  Util.formatearMontoSinEspacio(beanLetra.getM_ImporteFinal(),2);
                            tpdetorden.setNdomonto(new BigDecimal(aux).setScale(2));
//                            try {
//                                montoaux = ((Math.round(((Util.convertirDouble(beanLetra.getM_ImporteAnt())) / 100) * Math.pow(10, 2)) / Math.pow(10, 2)));
//                            } catch (NumberFormatException nfe) {
//                                nfe.printStackTrace();
//                                montoaux = 0.0;
//                            }
//                            aux = String.valueOf(montoaux);
                            aux =  Util.formatearMontoSinEspacio(beanLetra.getM_PagoPrincipal(),2);//beanLetra.getM_ImporteAnt()//jmoreno 08-09-09
                            tpdetorden.setNdomontoLetra(new BigDecimal(aux).setScale(2));
                            tpdetorden.setCdomoneda(beanLetra.getM_Moneda());
                            //jmoreno 08-09-09
                            tpdetorden.setDdoadicional1(Util.formatearMontoSinEspacio(beanLetra.getM_ImporteAnt(),2));//Monto Original
                            listadetorden.add(tpdetorden);
                        }
                    }//end for
                    
                    Map montos = new HashMap();
                    TpDetalleOrdenDao tpdetDao = new TpDetalleOrdenDaoHibernate();
                    guardar = tpdetDao.insert_2(listadetorden, montos);
                    if (guardar) {
                        ordenDao.update(taorden.getId().getCorIdOrden(),
                                taorden.getId().getCorIdServicioEmpresa(),
                                (BigDecimal) montos.get("PEN"),
                                (BigDecimal) montos.get("USD"),
                                (BigDecimal)montos.get("EUR"),
                                contador - 1);//numero de registros de detalleOrden
                        
                        //aqui se registra la cancelacion de la orden de letra
                        ordenDao.guardaLetrasConsultas(idServxEmp, idOrden);

                        List alsuccess = new ArrayList();
                        BeanSuccess success = new BeanSuccess();
                        success.setM_Mensaje("La orden ha sido generada con el/los registro(s) válido(s)");
                        alsuccess.add(new BeanSuccessDetail("Tipo de Operación:", "CANCELACIÓN DE LETRA"));
                        alsuccess.add(new BeanSuccessDetail("Número de Orden:", taorden.getId().getCorIdOrden() + ""));
                        alsuccess.add(new BeanSuccessDetail("Fecha/Hora:", Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss")));
                        request.setAttribute("success", success);
                        request.setAttribute("alsuccess", alsuccess);

                    } else {
                        List alsuccess = new ArrayList();
                        BeanSuccess success = new BeanSuccess();
                        success.setM_Mensaje("No se pudo generar la orden correctamente");
                        alsuccess.add(new BeanSuccessDetail("Tipo de Operación:", "CANCELACIÓN DE LETRA"));
                        request.setAttribute("success", success);
                        request.setAttribute("alsuccess", alsuccess);
                    }
                }

            } else {
                List alsuccess = new ArrayList();
                BeanSuccess success = new BeanSuccess();
                success.setM_Mensaje("La orden no contiene letras válidas para cancelar");
                alsuccess.add(new BeanSuccessDetail("Tipo de Operación:", "CANCELACIÓN DE LETRA"));
                request.setAttribute("success", success);
                request.setAttribute("alsuccess", alsuccess);
            }

            session.removeAttribute("listaLetras");
        } else {            
            List alsuccess = new ArrayList();
            BeanSuccess success = new BeanSuccess();
            success.setM_Mensaje("El servicio no se encuentra afiliado");
            alsuccess.add(new BeanSuccessDetail("Tipo de Operación:", "CANCELACIÓN DE LETRA"));
            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
        }

        return mapping.findForward("confirmarCancelacion");
    }
    //private List obtenerCuentas(CashClientWS ws,String codCliente){
    private List obtenerCuentas(CashClientWS ws,String codCliente, String numTarjeta, String token, String ruc){
        ArrayList listaParamCtas = new ArrayList();
        //listaParamCtas.add(new BeanNodoXML("id_trx", Constantes.IBS_CONS_CTAS_CLIENTE));
        listaParamCtas.add(new BeanNodoXML("id_trx", Constantes.IBS_CONS_CTAS_ASOC_CLIENTE));
        //listaParamCtas.add(new BeanNodoXML("filtro", Constantes.FILTRO_ACCOUNT_TYPE));
        listaParamCtas.add(new BeanNodoXML("client_code", codCliente));
        
        String req = GenRequestXML.getXML(listaParamCtas);
        String resultado = ws.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        List listaccounts = null;
        if (!(resultado == null) && !("".equals(resultado))) {
            BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
            if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                //BeanConsCtasCliente beanctascliente = ParserXML.getConsCtasCliente(beanResXML.getM_Respuesta(), ws, numTarjeta, token, ruc);
                BeanConsCtasCliente beanctascliente = ParserXML.getConsCtasClienteCombos(beanResXML.getM_Respuesta());
                listaccounts = beanctascliente.getM_Accounts();
            }
        }
        return listaccounts;

    }
}

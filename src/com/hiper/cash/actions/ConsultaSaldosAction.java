/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

import com.financiero.cash.adapter.LiquidadorAdapter;
import com.financiero.cash.adapter.PrestamoAdapter;
import com.financiero.cash.delegate.MovimientosDelegate;
import com.financiero.cash.delegate.RelacionesBanco;
import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.clienteWS.GenRequestXML;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.TxListFieldDao;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TxListFieldDaoHibernate;
import com.hiper.cash.descarga.excel.GeneradorPOI;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.Propiedades;
import com.hiper.cash.forms.ConsultaSaldosForm;
import com.hiper.cash.paginado.CronogramaPaginado;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;
import com.hiper.cash.xml.bean.BeanCodigoInterbancarioXML;
import com.hiper.cash.xml.bean.BeanConsMovHistoricosXML;
import com.hiper.cash.xml.bean.BeanConsRelBanco;
import com.hiper.cash.xml.bean.BeanConsultaSaldosXML;
import com.hiper.cash.xml.bean.BeanDataLoginXML;
import com.hiper.cash.xml.bean.BeanDetalleMovimiento;
import com.hiper.cash.xml.bean.BeanNodoXML;
import com.hiper.cash.xml.bean.BeanRespuestaXML;
import com.hiper.cash.xml.bean.BeanTypeAccount;
import com.hiper.cash.xml.parser.ParserXML;

/**
 * ConsultaSaldosAction
 * Procesa la informacion enviada desde el formulario Login
 * Invoca los metodos de la clase UsuarioDAO para realizar la autenticacion
 * Redirige el control al jsp principal de la aplicacion
 * @autor Jonathan Wong
 * @version 1.0
 * Fecha creacion: 06/01/2006
 */

/**
 *
 * @author jwong
 */
public class ConsultaSaldosAction extends DispatchAction {

    private static Logger logger = Logger.getLogger(ConsultaSaldosAction.class);
    private MovimientosDelegate delegadoMovimientos =  new  MovimientosDelegate();

    /**
     * cargarSaldos, carga de listas(filtros) para pagina de historico de movimientos
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward cargarSaldos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }        

        String habil = request.getParameter("habil");
        if(habil==null || (habil!=null && "1".equals(habil.trim()))){
            session.setAttribute("habil", "1");
        }
        else{
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarSaldos");
        }
        //jmoreno 07-10-09
        session.removeAttribute("listaResult");

        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa = (List) session.getAttribute("empresa");
        List lEmpresa = null;
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        
        boolean  swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        if(beanDataLogXML.isM_usuarioEspecial()){
        	
        	//System.out.println("Tar="+numTarjeta+" - Es usuario especial");
            lEmpresa = (List) session.getAttribute("empresa");
            
            ////System.out.println("Entra al usuario Especial IFFFF " );
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_CONS_SALDOSMOVIMIENTOS);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }else{
        	
        	//System.out.println("Tar="+numTarjeta+" - NOOO Es usuario especial");
        	////System.out.println("Entra al Elseeeeeeeeee  ");
            HashMap hMapEmpresas =(HashMap)session.getAttribute("hmEmpresas");
            
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_CONS_SALDOSMOVIMIENTOS);
            
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
            
        }

        //obtenemos los datos de la empresa que resulto al logearnos
        
        //System.out.println("N Empresas="+lEmpresa.size());
        
        List listaEmpresas = empresaDAO.listarClienteEmpresa(lEmpresa);

        //obtenemos el listado de tipos de informacion
        TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
        List tiposInformacion = listFieldDAO.selectListFieldByFieldName("CashTipoInformacion");

        //session.setAttribute("listaempresas", empresas);
        session.setAttribute("listaEmpresas", listaEmpresas);
        session.setAttribute("tiposInformacion", tiposInformacion);
        
        return mapping.findForward("cargarSaldos");
    }
    
    /**
     * buscarSaldos, para el manejo de la consulta de saldos
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward buscarSaldos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();
        
        try{
        	if (session.getAttribute("usuarioActual") == null) {
                response.sendRedirect("cierraSession.jsp");
                return null;
            }
        	//obtenemos los valores seleccionados en la pagina
            ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;
            String m_Empresa = consultaSaldosForm.getM_Empresa();
            
            //jwong 03/03/2009 para obtener el RUC de la empresa y el id del cliente
            String cod_cliente = null;
            String ruc_empresa = null;
            String part[] = null;
            if(m_Empresa!=null && m_Empresa.contains(";")){
                part = m_Empresa.split(";");
                ruc_empresa = part[0];
                cod_cliente = part[1];
            }
            //jwong 03/03/2009 seteamos la empresa seleccionada
            consultaSaldosForm.setM_EmpresaSel(m_Empresa);
            session.setAttribute("mIdEmpresa", m_Empresa);

            //obtenemos el tipo de consulta(0=saldos / 1=historico de movimientos / 2=saldos promedios)
            String tipo = request.getParameter("tipo");
            
            //MovimientosDelegate delegado =  new  MovimientosDelegate();
            List<BeanConsultaSaldosXML> listaResult = delegadoMovimientos.obtenerSaldos(cod_cliente);
    		request.setAttribute("listaResult", listaResult);
    		if ("0".equals(tipo)) { // consulta de saldos
    			return mapping.findForward("cargarSaldos");
    		} else if ("1".equals(tipo)) { // historico de movimientos
    			return mapping.findForward("cargarHisMov");
    		} else { // saldos promedios
    			return mapping.findForward("cargarSaldosPromedios");
    		}
        	
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
        	request.setAttribute("mensaje", "Informacion no puede ser procesada");
        	return mapping.findForward("error");
        }        
    }
    
    /**
     * listarUltimosMovimientos, 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward listarUltimosMovimientos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();
        
        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos los valores seleccionados en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;
        String m_Empresa = consultaSaldosForm.getM_Empresa();        
        
        //obtenemos los parametros de la cuenta para buscar sus ultimos movimientos
        String m_Cuenta = request.getParameter("m_Cuenta");
        String m_TipoCuenta = request.getParameter("m_TipoCuenta");
        
        //jwong 03/03/2009 obtenemos la empresa seleccionada al cargar la pagina
        String m_EmpresaSel = consultaSaldosForm.getM_EmpresaSel();
        //jwong 03/03/2009 para obtener el RUC de la empresa y el id del cliente
        String cod_cliente = null;
        String ruc_empresa = null;
        String part[] = null;
        if(m_EmpresaSel!=null && m_EmpresaSel.contains(";")){
            part = m_EmpresaSel.split(";");
            ruc_empresa = part[0];
            cod_cliente = part[1];
        }
        
        
        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;
        
        //añadimos cada uno de los parametros usados en el logueo
        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_MOV_CTAS); //id de la transaccion
        listaParametros.add(beanNodo);

//        beanNodo = new BeanNodoXML("tipocuenta", m_TipoCuenta); //tipo de cuenta
//        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT_NUMBER, m_Cuenta); //cuenta
        listaParametros.add(beanNodo);
//        beanNodo = new BeanNodoXML("query_type", m_TipoInformacion); //tipo de informacion(Cargo o Abono)
//        listaParametros.add(beanNodo);        
        
        beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
        listaParametros.add(beanNodo);
        listaParametros.add(new BeanNodoXML("id_movement", "0000000000"));        
        
      try{
        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //jwong 18/01/2009 obtenemos el nro de decimales a formatear en los montos del tipo de cambio
            Propiedades prop = (Propiedades)context.getAttribute("propiedades");           
            //procesamos la respuesta(parseo xml) y enviamos a la pagina
            BeanConsMovHistoricosXML beanXML = ParserXML.listarUltimosMovimientos(beanResXML.getM_Respuesta(), /*prop.getM_NroDecimalesWS(),*/ prop.getM_FormatoFechaWSLastMov(), prop.getM_FormatoHoraWS());
            if(beanXML!=null){
                //jwong 09/03/2009 para obtener la moneda
                String desMoneda = request.getParameter("m_Moneda");
                beanXML.setM_Moneda(desMoneda);
                beanXML.setM_Cuenta(m_Cuenta); //colocamos el numero de cuenta a mostrarse en pagina
                
                //jwong 25/03/2009 obtenemos los saldos enviados desde pagina
                String m_SaldoDisponible = request.getParameter("m_SaldoDisponible");
                String m_SaldoRetenido = request.getParameter("m_SaldoRetenido");
                String m_SaldoContable = request.getParameter("m_SaldoContable");
                beanXML.setM_SaldoDisponible(m_SaldoDisponible);
                beanXML.setM_SaldoRetenido(m_SaldoRetenido);
                beanXML.setM_SaldoContable(m_SaldoContable);
                
                request.setAttribute("beanMovimientos", beanXML);
                if(beanXML.getM_Movimientos()!=null && beanXML.getM_Movimientos().size()>0){
                    request.setAttribute("resultListaMovimientos", beanXML.getM_Movimientos());
                    request.setAttribute("m_Cuenta", m_Cuenta);
                    request.setAttribute("m_TipoCuenta", m_TipoCuenta);
                    request.setAttribute("m_Moneda", desMoneda);
                }
                else{
                    request.setAttribute("mensaje", "No se encontraron movimientos con esos parámetros de búsqueda");
                }
            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
      }catch(Exception ex){
        request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
        logger.error(ex.toString());
      }
      return mapping.findForward("cargarSaldos");
    }
    
    /**
     * cargarHisMovimientos, carga de listas(filtros) para pagina de historico de movimientos
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward cargarHisMovimientos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML)session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        String habil = request.getParameter("habil");
        if(habil==null || (habil!=null && "1".equals(habil.trim()))){
            session.setAttribute("habil", "1");
        }
        else{
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarHisMov");
        }
        //jmoreno 07-10-09
        session.removeAttribute("resultListaHisMovimientos");
        //jmoreno 10-12-09
        session.removeAttribute("bpCons");

        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa = (List) session.getAttribute("empresa");

        List lEmpresa = null;
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        
        if(beanDataLogXML.isM_usuarioEspecial()){
            lEmpresa = (List) session.getAttribute("empresa");
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_CONS_MOVHISTORICOS);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }else{
            HashMap hMapEmpresas =(HashMap)session.getAttribute("hmEmpresas");
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_CONS_MOVHISTORICOS);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }

        //obtenemos los datos de la empresa que resulto al logearnos
        
        List listaEmpresas = empresaDAO.listarClienteEmpresa(lEmpresa);

        //obtenemos el listado de tipos de informacion
        TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
        List tiposInformacion = listFieldDAO.selectListFieldByFieldName("CashTipoInformacion");

        //obtenemos la fecha actual
        String fechaActual = Fecha.getFechaActual("dd/MM/yyyy");

        //session.setAttribute("listaempresas", empresas);
        session.setAttribute("listaEmpresas", listaEmpresas);
        session.setAttribute("tiposInformacion", tiposInformacion);

        //seteamos la fecha actual para ser mostrada en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;
        consultaSaldosForm.setM_FecInicio(fechaActual);
        consultaSaldosForm.setM_FecFin(fechaActual);
        //session.setAttribute("fechaActual", fechaActual);

        //ontenemos la fecha actual con un formato para comparar en pagina
        String fechaActualComp = Fecha.getFechaActual("yyyyMMdd");
        session.setAttribute("fechaActualComp", fechaActualComp);

        return mapping.findForward("cargarHisMov");
    }

    /**
     * buscarMovimientos, para el manejo de la consulta del historico de movimientos
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward buscarMovimientos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos los valores seleccionados en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;        
        String m_TipoInformacion  = consultaSaldosForm.getM_TipoInformacion();        
        String m_FecInicio = consultaSaldosForm.getM_FecInicio();
        String m_FecFin = consultaSaldosForm.getM_FecFin();

        //jwong 03/03/2009 obtenemos la empresa seleccionada al cargar la pagina
        String m_EmpresaSel = consultaSaldosForm.getM_EmpresaSel();
        //jwong 03/03/2009 para obtener el RUC de la empresa y el id del cliente
        String cod_cliente = null;
        String ruc_empresa = null;
        String part[] = null;
        if(m_EmpresaSel!=null && m_EmpresaSel.contains(";")){
            part = m_EmpresaSel.split(";");
            ruc_empresa = part[0];
            cod_cliente = part[1];
        }

        //obtenemos el tipo de consulta(1=historico de movimientos / 2=saldos promedios)
        String tipo = request.getParameter("tipo");

        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;
        
        //obtenemos los parametros de la cuenta para buscar sus movimientos
        String m_Cuenta = request.getParameter("m_Cuenta");
        String m_TipoCuenta = request.getParameter("m_TipoCuenta");
        
        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_MOV_HISTORICOS); //id de la transaccion
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT_TYPE, m_TipoCuenta); //tipo de cuenta
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT, m_Cuenta); //cuenta
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_QUERY_TYPE, m_TipoInformacion); //tipo de informacion(Cargo o Abono)
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_BEGIN_DATE, Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy", m_FecInicio)); //fecha inicio
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_END_DATE, Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy", m_FecFin)); //fecha fin
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
        listaParametros.add(beanNodo);

        String esPag = request.getParameter("esPag");
        if(esPag != null && !esPag.equals("")){//consulta la paginacion
            BeanPaginacion bpag = (BeanPaginacion) session.getAttribute("bpCons");
            String tipoPaginado = request.getParameter("tipoPaginado");
            tipoPaginado = tipoPaginado == null ? "P" : tipoPaginado;
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
            session.setAttribute("bpCons", bpag);
            long secuencial = bpag.getM_seleccion();
            String numSecuencial = String.valueOf(secuencial);
            while (numSecuencial.length() < 10) {
                numSecuencial = "0" + numSecuencial;
            }
            listaParametros.add(new BeanNodoXML("id_movement", numSecuencial));
        }else{//se consulta por primera vez
            listaParametros.add(new BeanNodoXML("id_movement", "0000000000"));
        }
        
      try{
        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //jwong 18/01/2009 obtenemos el nro de decimales a formatear en los montos del tipo de cambio
            Propiedades prop = (Propiedades)context.getAttribute("propiedades");            
            int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
            if(esPag==null){
                if (send_number > 0) {
                    int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("total_number"));
                    int nroPag = (int) total_number / send_number;
                    int resto = (int) total_number % send_number;
                    if (resto != 0) {
                        nroPag = nroPag + 1;
                    }
                    BeanPaginacion bpag = new BeanPaginacion();
                    bpag.setM_pagActual(1);
                    bpag.setM_pagFinal(nroPag);
                    bpag.setM_pagInicial(1);
                    bpag.setM_regPagina(send_number);
                    bpag.setM_ultimoRegAct(send_number);
                    session.setAttribute("bpCons", bpag);
                }
            }
            BeanConsMovHistoricosXML beanXML = null;
            //if (total_number == send_number) {
                //procesamos la respuesta(parseo xml) y enviamos a la pagina
                beanXML = ParserXML.listarHistoricoMovimientos(beanResXML.getM_Respuesta(),
                        prop.getM_FormatoFechaWS(), prop.getM_FormatoHoraWS(),send_number);

            /*} else {
                beanXML = ParserXML.listarHistoricoMovimientosPag(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS(), prop.getM_FormatoHoraWS(),
                        cashclienteWS, total_number, send_number, m_TipoCuenta, m_Cuenta, m_TipoInformacion, m_FecInicio, m_FecFin, ruc_empresa);
            }*/
            if(beanXML!=null){
                //jwong 09/03/2009 para obtener la moneda
                String desMoneda = request.getParameter("m_Moneda");
                beanXML.setM_Moneda(desMoneda);                
                beanXML.setM_Cuenta(m_Cuenta); //colocamos el numero de cuenta a mostrarse en pagina

                //jwong 25/03/2009 obtenemos los saldos enviados desde pagina
                String m_SaldoDisponible = request.getParameter("m_SaldoDisponible");
                String m_SaldoRetenido = request.getParameter("m_SaldoRetenido");
                String m_SaldoContable = request.getParameter("m_SaldoContable");
                beanXML.setM_SaldoDisponible(m_SaldoDisponible);
                beanXML.setM_SaldoRetenido(m_SaldoRetenido);
                beanXML.setM_SaldoContable(m_SaldoContable);
                
                request.setAttribute("beanHisMovimientos", beanXML);

                if(beanXML.getM_Movimientos()!=null && beanXML.getM_Movimientos().size()>0){
                    request.setAttribute("resultListaHisMovimientos", beanXML.getM_Movimientos());
                    request.setAttribute("m_Cuenta", m_Cuenta);
                    request.setAttribute("m_TipoCuenta", m_TipoCuenta);
                    request.setAttribute("m_Moneda", desMoneda);
                    
                    String saldoDisponibleExp = "";
                    String saldoRetenidoExp = "";
                    String saldoContableExp = "";
                    if(beanXML.getM_SaldoDisponible()!=null){
                        saldoDisponibleExp = (("-".equals(beanXML.getM_SignoDisponible()))?"-":"") + beanXML.getM_SaldoDisponible();
                    }
                    if(beanXML.getM_SaldoRetenido()!=null){
                        saldoRetenidoExp = (("-".equals(beanXML.getM_SignoRetenido()))?"-":"") + beanXML.getM_SaldoRetenido();
                    }
                    if(beanXML.getM_SaldoContable()!=null){
                        saldoContableExp = (("-".equals(beanXML.getM_SignoContable()))?"-":"") + beanXML.getM_SaldoContable();
                    }
                    //jwong 18/07/2009 enviamos los saldos para la descarga
                    request.setAttribute("saldoDisponibleExp", saldoDisponibleExp);
                    request.setAttribute("saldoRetenidoExp", saldoRetenidoExp);
                    request.setAttribute("saldoContableExp", saldoContableExp);
                    
                }
                else{
                    request.setAttribute("mensaje", "No se encontraron movimientos con esos parámetros de búsqueda");
                }
                
            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
        
      }catch(Exception ex){
        request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
        logger.error(ex.toString());
      }

        if("1".equals(tipo)){ //historico de movimientos
            return mapping.findForward("cargarHisMov");
        }
        else{ //saldos promedios
            return mapping.findForward("cargarSaldosPromedios");
        }
    }
    
    public ActionForward cargarCodIntBco(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML =(BeanDataLoginXML)session.getAttribute("usuarioActual") ;
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        String habil = request.getParameter("habil");
        if(habil==null || (habil!=null && "1".equals(habil.trim()))){
            session.setAttribute("habil", "1");
        }
        else{
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarCodIntBco");
        }
        //jmoreno 07-10-09
        session.removeAttribute("listaCodigosInter");
        //jmoreno 10-12-09
        session.removeAttribute("bpCons");

        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa = (List) session.getAttribute("empresa");

        List lEmpresa = null;
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        if(beanDataLogXML.isM_usuarioEspecial()){
            lEmpresa = (List) session.getAttribute("empresa");
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_CONS_CODIGOINTERBANCARIO);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }else{
            HashMap hMapEmpresas =(HashMap)session.getAttribute("hmEmpresas");
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_CONS_CODIGOINTERBANCARIO);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }

        //obtenemos los datos de la empresa que resulto al logearnos
        
        List listaEmpresas = empresaDAO.listarClienteEmpresa(lEmpresa);

        session.setAttribute("listaEmpresas", listaEmpresas);
        
        return mapping.findForward("cargarCodIntBco");
    }
    
    public ActionForward buscarCodIntBco(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos los valores seleccionados en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;
        String m_Empresa = consultaSaldosForm.getM_Empresa();
        String m_Paginado = consultaSaldosForm.getM_Paginado();

        //jwong 03/03/2009 para obtener el RUC de la empresa y el id del cliente
        String cod_cliente = null;
        String ruc_empresa = null;
        String part[] = null;
        if(m_Empresa!=null && m_Empresa.contains(";")){
            part = m_Empresa.split(";");
            ruc_empresa = part[0];
            cod_cliente = part[1];
        }
        //jwong 03/03/2009 seteamos la empresa seleccionada
        consultaSaldosForm.setM_EmpresaSel(m_Empresa);
        
        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        //añadimos cada uno de los parametros usados en el logueo
        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_COD_INTERBANC); //id de la transaccion
        listaParametros.add(beanNodo);

//        beanNodo = new BeanNodoXML(Constantes.TAG_CLIENT_CODE, m_Empresa); //codigo de la empresa
//        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_CLIENT_CODE, cod_cliente); //codigo de la empresa
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
        listaParametros.add(beanNodo);

        String tipoPaginado = request.getParameter("tipoPaginado");
        if(tipoPaginado != null && !tipoPaginado.equals("")){//consulta la paginacion
            BeanPaginacion bpag = (BeanPaginacion) session.getAttribute("bpCons");

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
            session.setAttribute("bpCons", bpag);
            long secuencial = bpag.getM_seleccion();
            String numSecuencial = String.valueOf(secuencial);
            while (numSecuencial.length() < 4) {
                numSecuencial = "0" + numSecuencial;
            }
            listaParametros.add(new BeanNodoXML("id_movement", numSecuencial));
        }else{//se consulta por primera vez
            listaParametros.add(new BeanNodoXML("id_movement", "0000"));
        }

        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //procesamos la respuesta(parseo xml) y enviamos a la pagina
            int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
            if(tipoPaginado==null){
                if (send_number > 0) {
                    int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("total_number"));
                    int nroPag = (int) total_number / send_number;
                    int resto = (int) total_number % send_number;
                    if (resto != 0) {
                        nroPag = nroPag + 1;
                    }
                    BeanPaginacion bpag = new BeanPaginacion();
                    bpag.setM_pagActual(1);
                    bpag.setM_pagFinal(nroPag);
                    bpag.setM_pagInicial(1);
                    bpag.setM_regPagina(send_number);
                    bpag.setM_ultimoRegAct(send_number);
                    session.setAttribute("bpCons", bpag);
                }
            }
            ArrayList listaResult = ParserXML.listarCodigosIntBco(beanResXML.getM_Respuesta(),send_number);
            if(listaResult!=null && listaResult.size()>0){
                request.setAttribute("listaCodigosInter", listaResult);
            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
        return mapping.findForward("cargarCodIntBco");
    }
    
    public ActionForward cargarRelacionesBco(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML)session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        String habil = request.getParameter("habil");
        if(habil==null || (habil!=null && "1".equals(habil.trim()))){
            session.setAttribute("habil", "1");
        }
        else{
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarRelacionesBco");
        }
        //jmoreno 07-10-09
        session.removeAttribute("listaResult");

        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa = (List) session.getAttribute("empresa");

        List lEmpresa = null;
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        if(beanDataLogXML.isM_usuarioEspecial()){
            lEmpresa = (List) session.getAttribute("empresa");
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_CONS_RELACIONESBANCO);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }else{
            HashMap hMapEmpresas =(HashMap)session.getAttribute("hmEmpresas");
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_CONS_RELACIONESBANCO);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }

        //obtenemos los datos de la empresa que resulto al logearnos
       
        List listaEmpresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);

        //session.setAttribute("listaempresas", empresas);
        session.setAttribute("listaEmpresas", listaEmpresas);
        
        return mapping.findForward("cargarRelacionesBco");
    }
    
    public ActionForward buscarRelacionesBco(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML)session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos los valores seleccionados en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;
        String m_Empresa = consultaSaldosForm.getM_Empresa();       

        //jwong 09/03/2009 para obtener el RUC de la empresa y el id del cliente
        String cod_cliente = null;
        String ruc_empresa = null;
        String part[] = null;
        if(m_Empresa!=null && m_Empresa.contains(";")){
            part = m_Empresa.split(";");
            ruc_empresa = part[0];
            cod_cliente = part[1];
        }
        //jwong 09/03/2009 seteamos la empresa seleccionada
        consultaSaldosForm.setM_EmpresaSel(m_Empresa);

        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        //jwong 09/03/2009 se usara la transaccion de consulta de saldos
        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_CTAS_CLIENTE); //id de la transaccion
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_CLIENT_CODE, cod_cliente); //codigo de la empresa
        listaParametros.add(beanNodo);
        
        beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_SECUENCIAL, "0000"); //RUC de la empresa
        listaParametros.add(beanNodo);


        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("total_number"));
            int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
            BeanConsRelBanco beanConsRelBco = null;
            if(total_number == send_number){
                beanConsRelBco = ParserXML.listarRelacionesBco(beanResXML.getM_Respuesta(), ruc_empresa);
            }else{
                beanConsRelBco = ParserXML.listarRelacionesBcoPag(cod_cliente, ruc_empresa, beanResXML.getM_Respuesta(), total_number, send_number, cashclienteWS);
            }

            if(beanConsRelBco!=null && beanConsRelBco.getM_Accounts()!=null && beanConsRelBco.getM_Accounts().size()>0){
                //jwong 03/08/2009 enviamos a la pagina la empresa selecionada
                request.setAttribute("m_EmpresaExp", m_Empresa);
                
                request.setAttribute("beanConsRelBco", beanConsRelBco);
                request.setAttribute("listaRelaciones", beanConsRelBco.getM_AccountsType());
            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
        return mapping.findForward("cargarRelacionesBco");
    }

    public ActionForward cargarSaldosPromedios(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML)session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        
        String habil = request.getParameter("habil");
        if(habil==null || (habil!=null && "1".equals(habil.trim()))){
            session.setAttribute("habil", "1");
        }
        else{
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarSaldosPromedios");
        }
        //jmoreno 07-10-09
        session.removeAttribute("listaResult");
        //jmoreno 10-12-09
        session.removeAttribute("bpCons");
        
        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa = (List) session.getAttribute("empresa");

        List lEmpresa = null;
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        if(beanDataLogXML.isM_usuarioEspecial()){
        	//System.out.println("Tar="+numTarjeta+" - Es usuario especial");
            lEmpresa = (List) session.getAttribute("empresa");
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_CONS_SALDOSPROMEDIOS);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }else{
        	//System.out.println("Tar="+numTarjeta+" - NOO Es usuario especial");
            HashMap hMapEmpresas =(HashMap)session.getAttribute("hmEmpresas");
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_CONS_SALDOSPROMEDIOS);
            if(lEmpresa.size()== 0){
                request.setAttribute("mensaje","El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }


        //obtenemos los datos de la empresa que resulto al logearnos
        
        List listaEmpresas = empresaDAO.listarClienteEmpresa(lEmpresa);

        //obtenemos el listado de tipos de informacion
        TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
        List tiposInformacion = listFieldDAO.selectListFieldByFieldName("CashTipoInformacion");

        //obtenemos la fecha actual
        String fechaActual = Fecha.getFechaActual("dd/MM/yyyy");

        //session.setAttribute("listaempresas", empresas);
        session.setAttribute("listaEmpresas", listaEmpresas);
        session.setAttribute("tiposInformacion", tiposInformacion);

        //seteamos la fecha actual para ser mostrada en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;
        consultaSaldosForm.setM_FecInicio(fechaActual);
        consultaSaldosForm.setM_FecFin(fechaActual);
        //session.setAttribute("fechaActual", fechaActual);

        //ontenemos la fecha actual con un formato para comparar en pagina
        String fechaActualComp = Fecha.getFechaActual("yyyyMMdd");
        session.setAttribute("fechaActualComp", fechaActualComp);
        
        return mapping.findForward("cargarSaldosPromedios");
    }

    /**
     * exportarSaldos, para le manejo de la descarga de la consulta de saldos
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward exportarSaldos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    	
    	try{
    		
    		HttpSession session = request.getSession();
            ServletContext context = getServlet().getServletConfig().getServletContext();

            //si termino la session debemos retornar al inicio
            if (session.getAttribute("usuarioActual") == null) {
                response.sendRedirect("cierraSession.jsp");
                return null;
            }

            //obtenemos los valores seleccionados en la pagina
            //ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;
            String m_Empresa = request.getParameter("m_Empresa");
            //String m_TipoInformacion  = request.getParameter("m_TipoInformacion");
            //String m_Paginado = consultaSaldosForm.getM_Paginado();

            //jwong 03/03/2009 para obtener el RUC de la empresa y el id del cliente
            String cod_cliente = null;
            String ruc_empresa = null;
            String part[] = null;
            if(m_Empresa!=null && m_Empresa.contains(";")){
                part = m_Empresa.split(";");
                ruc_empresa = part[0];
                cod_cliente = part[1];
            }

            //jwong 03/03/2009 seteamos la empresa seleccionada
            //consultaSaldosForm.setM_EmpresaSel(m_Empresa);


            //obtenemos el tipo de consulta(0=saldos / 1=historico de movimientos / 2=saldos promedios)
            //String tipo = request.getParameter("tipo");

            //enviamos la informacion al webservice y obtenemos la respuesta
            CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
            ArrayList listaParametros = new ArrayList();
            BeanNodoXML beanNodo = null;
            String resultado = null;

            //jwong 14/08/2009 comentado
            //añadimos cada uno de los parametros usados en el logueo
            //beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_CTAS_CLIENTE); //id de la transaccion
            beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_CTAS_ASOC_CLIENTE); //id de la transaccion
            listaParametros.add(beanNodo);

//            beanNodo = new BeanNodoXML(Constantes.TAG_CLIENT_CODE, m_Empresa); //codigo de la empresa
//            listaParametros.add(beanNodo);

            beanNodo = new BeanNodoXML(Constantes.TAG_CLIENT_CODE, cod_cliente); //codigo del cliente
            listaParametros.add(beanNodo);

            /*
            //jwong 09/03/2009 para filtrar el resultado de la respuesta del webservice
            beanNodo = new BeanNodoXML(Constantes.TAG_FILTRO, "1"); //1 para que lo filtre
            listaParametros.add(beanNodo);
            */

            //jwong 03/04/2009 para filtrar el resultado de la respuesta del webservice
            //beanNodo = new BeanNodoXML(Constantes.TAG_FILTRO, "110,210;" + Constantes.TAG_ACCOUNT_TYPE);
            //jwong 14/08/2009 comentado
            //beanNodo = new BeanNodoXML(Constantes.TAG_FILTRO, Constantes.TAG_ACCOUNT_TYPE + "=110,210,330");
            //listaParametros.add(beanNodo);


            //beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
            //listaParametros.add(beanNodo);

            String req = GenRequestXML.getXML(listaParametros);
            resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
            if(resultado==null || "".equals(resultado)){
                //deberiamos retornar a la pagina con un mensaje de error
                request.setAttribute("mensaje", "No se encontraron resultados");
                return mapping.findForward("error");
            }
            //se debe parsear el xml obtenido
            BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
            BeanConsultaSaldosXML bean = null;

            //si la respuesta es exitosa
            List<BeanConsultaSaldosXML> listaResult = delegadoMovimientos.obtenerSaldos(cod_cliente);
          
            //if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
                //procesamos la respuesta(parseo xml)
                //jwong  14/08/2009 comentado
                //ArrayList listaResult = ParserXML.listarConsultaSaldos(beanResXML.getM_Respuesta()/*, prop.getM_NroDecimalesWS()*/);
                //ArrayList listaResult = ParserXML.getConsultaSaldos(beanResXML.getM_Respuesta()/*, prop.getM_NroDecimalesWS()*/);
               
                if (listaResult != null) { // jmoreno 26-08-09 Modificacion realizada para mostrar mensaje correcto
                    if (listaResult.size() > 0) {
                        request.setAttribute("listaResult", listaResult);
                        //verificamos el tipo de descarga que se hara
                        String accion = request.getParameter("accion");
                        String formato = request.getParameter("formato");
                        if ("save".equalsIgnoreCase(accion) && "txt".equalsIgnoreCase(formato)) { //descarga texto
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            String nombre_archivo = "consulta-" + sdf.format(new Date()) + ".txt";
                            response.setHeader("Content-Disposition", "attachment; filename=\"" + nombre_archivo + "\"");
                            response.setContentType("text/plain");
                            //creamos la cadena de texto a descargar
                            StringBuilder strBuilder = new StringBuilder();

                            //jwong 16/08/2009 colocamos el titulo del reporte
                            strBuilder.append("\t\t\tDETALLE DE SALDOS\r\n\r\n");

                            //1ero creamos los titulos de las columnas
                            strBuilder.append("Cuenta\t\t");
                            strBuilder.append(Util.ajustarDato("Tipo de Cuenta", 30) + "\t");
                            strBuilder.append("Moneda\t");
                            strBuilder.append(Util.ajustarDato("Saldo Disponible", 20) + "\t");
                            strBuilder.append(Util.ajustarDato("Saldo Retenido", 20) + "\t");
                            strBuilder.append("Saldo Contable\r\n");

                            for (int i = 0; i < listaResult.size(); i++) {
                                bean = (BeanConsultaSaldosXML) listaResult.get(i);
                                strBuilder.append(bean.getM_Cuenta() + "\t");
                                strBuilder.append(Util.ajustarDato(bean.getM_DescripcionCuenta(), 30) + "\t");
                                strBuilder.append(bean.getM_Moneda() + "\t");
                                strBuilder.append(Util.ajustarDato(bean.getM_SaldoDisponible(), 20) + "\t");
                                strBuilder.append(Util.ajustarDato(bean.getM_SaldoRetenido(), 20) + "\t");
                                strBuilder.append(bean.getM_SaldoContable() + "\r\n");
                            }

                            PrintWriter out = new PrintWriter(response.getOutputStream());
                            out.println(strBuilder);
                            out.flush();
                            out.close();

                            response.getOutputStream().flush();
                            response.getOutputStream().close();

                            return null;
                        } else if ("save".equalsIgnoreCase(accion) && "excel".equalsIgnoreCase(formato)) { //descarga excel
                            //se realizara la descarga usando POI
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            String nombre_archivo = "consulta-" + sdf.format(new Date()) + ".xls";
                            response.setHeader("Content-Disposition", "attachment; filename=\"" + nombre_archivo + "\"");
                            response.setContentType("application/vnd.ms-excel");

                            ArrayList lblColumnas = new ArrayList();
                            lblColumnas.add("Cuenta");
                            lblColumnas.add("Tipo de Cuenta");
                            lblColumnas.add("Moneda");
                            lblColumnas.add("Saldo Disponible");
                            lblColumnas.add("Saldo Retenido");
                            lblColumnas.add("Saldo Contable");

                            ArrayList lstData = new ArrayList();
                            for (int i = 0; i < listaResult.size(); i++) {
                                bean = (BeanConsultaSaldosXML) listaResult.get(i);
                                lstData.add(new String[]{bean.getM_Cuenta(), bean.getM_DescripcionCuenta(), bean.getM_Moneda(), bean.getM_SaldoDisponible(), bean.getM_SaldoRetenido(), bean.getM_SaldoContable()});
                            }
                            HSSFWorkbook libroXLS = GeneradorPOI.crearExcel(nombre_archivo, lblColumnas, lstData, null, "DETALLE DE SALDOS");
                            if (libroXLS != null) {
                                libroXLS.write(response.getOutputStream());
                                response.getOutputStream().close();
                                response.getOutputStream().flush();
                            }
                            return null;
                        } else { //se enviara a pagina para imprimir
                            request.setAttribute("listaResult", listaResult);
                        }
                    } else {
                        request.setAttribute("mensaje", "No existen cuentas válidas para esta consulta");
                    }
                } else {
                    request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
                }
            
          
            return mapping.findForward("exportarSaldos");
    		
    	}catch(Exception e){
    		request.setAttribute("mensaje", "La Informacion no puede ser procesada. Estamos Trabajando para Solucionar el Error");
    		return mapping.findForward("error");
    	}
        
        /*
        if("0".equals(tipo)){ //consulta de saldos
            return mapping.findForward("cargarSaldos");
        }
        else if("1".equals(tipo)){ //historico de movimientos
            return mapping.findForward("cargarHisMov");
        }
        else{ //saldos promedios
            return mapping.findForward("cargarSaldosPromedios");
        }
        */
    }

    //jwong 27/01/2009 para el manejo de la impresion/guardar archivo
    public ActionForward exportarCodIntBco(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        //obtenemos los valores seleccionados en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;
        
        //jwong 03/03/2009 obtenemos la empresa seleccionada al cargar la pagina
        String m_EmpresaSel = consultaSaldosForm.getM_EmpresaSel();
        //jwong 03/03/2009 para obtener el RUC de la empresa y el id del cliente
        String cod_cliente = null;
        //String ruc_empresa = null;
        String part[] = null;
        if(m_EmpresaSel!=null && m_EmpresaSel.contains(";")){
            part = m_EmpresaSel.split(";");
            //ruc_empresa = part[0];
            cod_cliente = part[1];
        }
        
        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        //añadimos cada uno de los parametros usados en el logueo
        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_COD_INTERBANC); //id de la transaccion
        listaParametros.add(beanNodo);

//        beanNodo = new BeanNodoXML(Constantes.TAG_CLIENT_CODE, m_Empresa); //codigo de la empresa
//        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_CLIENT_CODE, cod_cliente); //codigo de la empresa
        listaParametros.add(beanNodo);

        BeanPaginacion bpag =(BeanPaginacion) session.getAttribute("bpCons");
        if(bpag!= null ){
            long secuencial = bpag.getM_seleccion();
            String numSecuencial = String.valueOf(secuencial);
            while (numSecuencial.length() < 4) {
                numSecuencial = "0" + numSecuencial;
            }
            listaParametros.add(new BeanNodoXML("id_movement", numSecuencial));
        }else{
            listaParametros.add(new BeanNodoXML("id_movement", "0000"));
        }
        
        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //procesamos la respuesta(parseo xml) y enviamos a la pagina
            int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
            ArrayList listaResult = ParserXML.listarCodigosIntBco(beanResXML.getM_Respuesta(),send_number);
            if(listaResult!=null && listaResult.size()>0){
                //verificamos si se quiere exportar a formato de texto
                String accion = request.getParameter("accion");
                String formato = request.getParameter("formato");
                if("save".equalsIgnoreCase(accion) && "txt".equalsIgnoreCase(formato)){ //descarga texto
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String nombre_archivo = "consulta-"+sdf.format(new Date())+".txt";
                    response.setHeader("Content-Disposition","attachment; filename=\""+nombre_archivo+"\"");
                    response.setContentType("text/plain");
                    //creamos la cadena de texto a descargar
                    StringBuilder strBuilder = new StringBuilder();
                    BeanCodigoInterbancarioXML bean = null;
                    //jwong 16/08/2009 colocamos titulo al reporte generado
                    strBuilder.append("\t\t\tDETALLE DE CÓDIGOS INTERBANCARIOS\r\n\r\n");
                    
                    //1ero creamos los titulos de las columnas
                    strBuilder.append("Cuenta\t\t");
                    strBuilder.append("Moneda\t");
                    strBuilder.append(Util.ajustarDato("Tipo Cuenta",30) + "\t");
                    strBuilder.append(Util.ajustarDato("Estado",22) + "\t");
                    strBuilder.append("Código Interbancario (CCI)\r\n");
                    
                    for(int i=0 ; i<listaResult.size() ; i++){
                        bean = (BeanCodigoInterbancarioXML)listaResult.get(i);
                        strBuilder.append(bean.getM_Cuenta() + "\t");
                        strBuilder.append(bean.getM_Moneda() + "\t");
                        strBuilder.append(Util.ajustarDato(bean.getM_TipoCuenta(),30) + "\t");
                        strBuilder.append(Util.ajustarDato(bean.getM_Estado(),22) + "\t");
                        strBuilder.append(bean.getM_CodigoInterbancario() + "\r\n");
                    }
                    
                    PrintWriter out = new PrintWriter(response.getOutputStream());
                    out.println(strBuilder);
                    out.flush();
                    out.close();

                    response.getOutputStream().flush();
                    response.getOutputStream().close();

                    return null;
                }
                else if("save".equalsIgnoreCase(accion) && "excel".equalsIgnoreCase(formato)){ //descarga excel
                    //se realizara la descarga usando POI
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String nombre_archivo = "consulta-"+sdf.format(new Date())+".xls";
                    response.setHeader("Content-Disposition","attachment; filename=\""+nombre_archivo+"\"");
                    response.setContentType("application/vnd.ms-excel");
                    
                    ArrayList lblColumnas = new ArrayList();
                    lblColumnas.add("Cuenta");
                    lblColumnas.add("Moneda");
                    lblColumnas.add("Tipo Cuenta");
                    lblColumnas.add("Estado");
                    lblColumnas.add("Código Interbancario (CCI)");

                    ArrayList lstData = new ArrayList();
                    BeanCodigoInterbancarioXML bean = null;
                    for(int i=0 ; i<listaResult.size() ; i++){
                        bean = (BeanCodigoInterbancarioXML)listaResult.get(i);
                        lstData.add(new String []{bean.getM_Cuenta(),bean.getM_Moneda(),bean.getM_TipoCuenta(),bean.getM_Estado(),bean.getM_CodigoInterbancario()});
                    }
                    HSSFWorkbook libroXLS = GeneradorPOI.crearExcel(nombre_archivo, lblColumnas, lstData, null, "DETALLE DE CÓDIGOS INTERBANCARIOS");
                    if(libroXLS!=null){
                        libroXLS.write(response.getOutputStream());
                        response.getOutputStream().close();
                        response.getOutputStream().flush();
                    }
                    return null;
                }
                else{ //se enviara a pagina para imprimir
                    request.setAttribute("listaCodigosInter", listaResult);
                }
            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
        return mapping.findForward("exportarCodIntBco");
    }

    //jwong 30/01/2009 para el manejo de la impresion/guardar archivo
    public ActionForward exportarMovimientos(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos los valores seleccionados en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;
        String m_Empresa = consultaSaldosForm.getM_Empresa();
        String m_TipoInformacion  = consultaSaldosForm.getM_TipoInformacion();
        String m_Paginado = consultaSaldosForm.getM_Paginado();

        //obtenemos los parametros de la cuenta para buscar sus ultimos movimientos
        String m_Cuenta = request.getParameter("m_Cuenta");
        String m_Moneda = request.getParameter("m_Moneda");
        String m_TipoCuenta = request.getParameter("m_TipoCuenta");

        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        //añadimos cada uno de los parametros usados en el logueo
        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_MOV_CTAS); //id de la transaccion
        listaParametros.add(beanNodo);

//        beanNodo = new BeanNodoXML("tipocuenta", m_TipoCuenta); //tipo de cuenta
//        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT_NUMBER, m_Cuenta); //cuenta
        listaParametros.add(beanNodo);
//        beanNodo = new BeanNodoXML("query_type", m_TipoInformacion); //tipo de informacion(Cargo o Abono)
//        listaParametros.add(beanNodo);
      try{
        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //jwong 18/01/2009 obtenemos el nro de decimales a formatear en los montos del tipo de cambio
            Propiedades prop = (Propiedades)context.getAttribute("propiedades");

            //procesamos la respuesta(parseo xml) y enviamos a la pagina
            BeanConsMovHistoricosXML beanXML = ParserXML.listarUltimosMovimientos(beanResXML.getM_Respuesta(), /*prop.getM_NroDecimalesWS(),*/ prop.getM_FormatoFechaWSLastMov(), prop.getM_FormatoHoraWS());
            if(beanXML!=null && beanXML.getM_Movimientos()!=null && beanXML.getM_Movimientos().size()>0){
                beanXML.setM_Cuenta(m_Cuenta);
                beanXML.setM_Moneda(m_Moneda);

                //jwong 18/07/2009 obtenemos de pagina los valores de los saldos para la descarga
                String m_SaldoDisponible = request.getParameter("saldoDisponibleExp");
                String m_SaldoRetenido = request.getParameter("saldoRetenidoExp");
                String m_SaldoContable = request.getParameter("saldoContableExp");
                
                beanXML.setM_SaldoDisponible(m_SaldoDisponible);
                beanXML.setM_SaldoRetenido(m_SaldoRetenido);
                beanXML.setM_SaldoContable(m_SaldoContable);
                
                //verificamos si se quiere exportar a formato de texto
                String accion = request.getParameter("accion");
                String formato = request.getParameter("formato");
                if("save".equalsIgnoreCase(accion) && "txt".equalsIgnoreCase(formato)){ //descarga texto
                    String nomb = Fecha.getFechaActual("yyyyMMdd_HHmmss");
                    String nombre_archivo = "Rep" + nomb + ".txt";
                    //enviamos los parametros necesarios al response para que el SO lo tome como formato excel
                    response.setHeader("Content-Disposition","attachment; filename=\"" + nombre_archivo + "\"");
                    response.setContentType("text/plain");
                    
                    StringBuilder strBuilder = new StringBuilder();
                    BeanDetalleMovimiento bean = null;
                    //1ero creamos los titulos de las columnas
                    strBuilder.append("\t\t\tDETALLE DE SALDO\r\n\r\n");
                    strBuilder.append("Titular: " + beanXML.getM_Titular() + "\t");
                    strBuilder.append("Moneda: " + beanXML.getM_Moneda() + "\t");
                    strBuilder.append("Cuenta: " + beanXML.getM_Cuenta() + "\r\n\r\n");
                    
                    strBuilder.append("\tSaldo Disponible\t\t\t\t\t\t" + beanXML.getM_SaldoDisponible() + "\r\n");
                    strBuilder.append("\tSaldo Retenido  \t\t\t\t\t\t" + beanXML.getM_SaldoRetenido() + "\r\n");
                    strBuilder.append("\tSaldo Contable  \t\t\t\t\t\t" + beanXML.getM_SaldoContable() + "\r\n\r\n");
                    
                    strBuilder.append("\t\t\tDETALLE DE MOVIMIENTOS\r\n\r\n");
                    strBuilder.append("Fecha\t\t");
                    strBuilder.append("Hora\t\t");
                    strBuilder.append("Tipo Transacción\t");
                    strBuilder.append(Util.ajustarDato("Descripción", 30) + "\t\t");
                    strBuilder.append("Importe\r\n");

                    for(int i=0 ; i<beanXML.getM_Movimientos().size() ; i++){
                        bean = (BeanDetalleMovimiento)beanXML.getM_Movimientos().get(i);
                        strBuilder.append(bean.getM_Fecha() + "\t");
                        strBuilder.append(bean.getM_Hora() + "\t");
                        strBuilder.append(bean.getM_TipoTrx() + "\t\t\t");
                        strBuilder.append(Util.ajustarDato(bean.getM_Descripcion(), 30) + "\t\t");
                        strBuilder.append(bean.getM_Signo() + bean.getM_Importe() + "\r\n");
                    }

                    PrintWriter out = new PrintWriter(response.getOutputStream());
                    out.println(strBuilder);
                    out.flush();
                    out.close();

                    response.getOutputStream().flush();
                    response.getOutputStream().close();

                    return null;
                }
                else if("save".equalsIgnoreCase(accion) && "excel".equalsIgnoreCase(formato)){ //descarga excel
                    //se realizara la descarga usando POI
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String nombre_archivo = "consulta-"+sdf.format(new Date())+".xls";
                    response.setHeader("Content-Disposition","attachment; filename=\""+nombre_archivo+"\"");
                    response.setContentType("application/vnd.ms-excel");
                    
                    ArrayList lblColumnas = new ArrayList();
                    lblColumnas.add("Fecha");
                    lblColumnas.add("Hora");
                    lblColumnas.add("Tipo Transacción");
                    lblColumnas.add("Descripción");
                    lblColumnas.add("Importe");

                    ArrayList lstData = new ArrayList();
                    BeanDetalleMovimiento bean = null;
                    for(int i=0 ; i<beanXML.getM_Movimientos().size() ; i++){
                        bean = (BeanDetalleMovimiento)beanXML.getM_Movimientos().get(i);
                        lstData.add(new String []{bean.getM_Fecha(),bean.getM_Hora(),bean.getM_TipoTrx(),bean.getM_Descripcion(),bean.getM_Signo() + bean.getM_Importe()});
                    }
                    HSSFWorkbook libroXLS = GeneradorPOI.crearExcel(nombre_archivo, lblColumnas, lstData, beanXML, null);
                    if(libroXLS!=null){
                        libroXLS.write(response.getOutputStream());
                        response.getOutputStream().close();
                        response.getOutputStream().flush();
                    }
                    return null;
                }
                else{ //se enviara a pagina para imprimir
                    beanXML.setM_Cuenta(m_Cuenta); //colocamos el numero de cuenta a mostrarse en pagina
                    request.setAttribute("beanMovimientos", beanXML);
                    request.setAttribute("resultListaMovimientos", beanXML.getM_Movimientos());
                }
            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
      }catch(Exception ex){
        request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
        logger.error(ex.toString());
      }
      return mapping.findForward("exportarMovimientos");
    }

    //jwong 30/01/2009 para el manejo de la impresion/guardar archivo
    public ActionForward exportarHistoricoMov(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos los valores seleccionados en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;
        String m_Empresa = consultaSaldosForm.getM_Empresa();
        String m_TipoInformacion  = consultaSaldosForm.getM_TipoInformacion();
        String m_Paginado = consultaSaldosForm.getM_Paginado();
        String m_FecInicio = consultaSaldosForm.getM_FecInicio();
        String m_FecFin = consultaSaldosForm.getM_FecFin();

        //obtenemos los parametros de la cuenta para buscar sus ultimos movimientos
        String m_Cuenta = request.getParameter("m_Cuenta");
        String m_Moneda = request.getParameter("m_Moneda");
        String m_TipoCuenta = request.getParameter("m_TipoCuenta");
        String m_EmpresaSel = consultaSaldosForm.getM_EmpresaSel();
         String ruc_empresa = null;
        String part[] = null;
        if(m_EmpresaSel!=null && m_EmpresaSel.contains(";")){
            part = m_EmpresaSel.split(";");
            ruc_empresa = part[0];
        }
        //obtenemos el tipo de consulta(1=historico de movimientos / 2=saldos promedios)
        //String tipo = request.getParameter("tipo");

        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_MOV_HISTORICOS); //id de la transaccion
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT_TYPE, m_TipoCuenta); //tipo de cuenta
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT, m_Cuenta); //cuenta
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_QUERY_TYPE, m_TipoInformacion); //tipo de informacion(Cargo o Abono)
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_BEGIN_DATE, Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy", m_FecInicio)); //fecha inicio
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_END_DATE, Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy", m_FecFin)); //fecha fin
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
        listaParametros.add(beanNodo);
        //listaParametros.add(new BeanNodoXML("id_movement", "0000000000"));

        BeanPaginacion bpag =(BeanPaginacion) session.getAttribute("bpCons");
        if(bpag!= null ){
            long secuencial = bpag.getM_seleccion();
            String numSecuencial = String.valueOf(secuencial);
            while (numSecuencial.length() < 10) {
                numSecuencial = "0" + numSecuencial;
            }
            listaParametros.add(new BeanNodoXML("id_movement", numSecuencial));
        }else{
            listaParametros.add(new BeanNodoXML("id_movement", "0000000000"));
        }

      try{
        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //jwong 18/01/2009 obtenemos el nro de decimales a formatear en los montos del tipo de cambio
            Propiedades prop = (Propiedades)context.getAttribute("propiedades");            
            int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
            BeanConsMovHistoricosXML beanXML = null;
            //if (total_number == send_number) {
                //procesamos la respuesta(parseo xml) y enviamos a la pagina
                beanXML = ParserXML.listarHistoricoMovimientos(beanResXML.getM_Respuesta(), 
                        prop.getM_FormatoFechaWS(), prop.getM_FormatoHoraWS(),send_number);

            /*} else {
                beanXML = ParserXML.listarHistoricoMovimientosPag(beanResXML.getM_Respuesta(), prop.getM_FormatoFechaWS(), prop.getM_FormatoHoraWS(),
                        cashclienteWS, total_number, send_number, m_TipoCuenta, m_Cuenta, m_TipoInformacion, m_FecInicio, m_FecFin, ruc_empresa);
            }*/

            //procesamos la respuesta(parseo xml) y enviamos a la pagina
            //BeanConsMovHistoricosXML beanXML = ParserXML.listarHistoricoMovimientos(beanResXML.getM_Respuesta(), /*prop.getM_NroDecimalesWS(),*/ prop.getM_FormatoFechaWS(), prop.getM_FormatoHoraWS());
            if(beanXML!=null && beanXML.getM_Movimientos()!=null && beanXML.getM_Movimientos().size()>0){
                beanXML.setM_Cuenta(m_Cuenta);
                beanXML.setM_Moneda(m_Moneda);

                //jwong 18/07/2009 obtenemos de pagina los valores de los saldos para la descarga
                String m_SaldoDisponible = request.getParameter("saldoDisponibleExp");
                String m_SaldoRetenido = request.getParameter("saldoRetenidoExp");
                String m_SaldoContable = request.getParameter("saldoContableExp");

                beanXML.setM_SaldoDisponible(m_SaldoDisponible);
                beanXML.setM_SaldoRetenido(m_SaldoRetenido);
                beanXML.setM_SaldoContable(m_SaldoContable);
                
                //verificamos si se quiere exportar a formato de texto
                String accion = request.getParameter("accion");
                String formato = request.getParameter("formato");
                if("save".equalsIgnoreCase(accion) && "txt".equalsIgnoreCase(formato)){ //descarga texto
                    String nomb = Fecha.getFechaActual("yyyyMMdd_HHmmss");
                    String nombre_archivo = "Rep" + nomb + ".txt";
                    //enviamos los parametros necesarios al response para que el SO lo tome como formato excel
                    response.setHeader("Content-Disposition","attachment; filename=\"" + nombre_archivo + "\"");
                    response.setContentType("text/plain");
                    
                    StringBuilder strBuilder = new StringBuilder();
                    BeanDetalleMovimiento bean = null;
                    //1ero creamos los titulos de las columnas
                    strBuilder.append("\t\t\tDETALLE DE SALDO\r\n\r\n");
                    strBuilder.append("Titular: " + beanXML.getM_Titular() + "\t");
                    strBuilder.append("Moneda: " + beanXML.getM_Moneda() + "\t");
                    strBuilder.append("Cuenta: " + beanXML.getM_Cuenta() + "\r\n\r\n");

                    strBuilder.append("\tSaldo Disponible\t\t\t\t\t\t" + beanXML.getM_SaldoDisponible() + "\r\n");
                    strBuilder.append("\tSaldo Retenido  \t\t\t\t\t\t" + beanXML.getM_SaldoRetenido() + "\r\n");
                    strBuilder.append("\tSaldo Contable  \t\t\t\t\t\t" + beanXML.getM_SaldoContable() + "\r\n\r\n");

                    strBuilder.append("\t\t\tDETALLE DE MOVIMIENTOS\r\n\r\n");
                    strBuilder.append("Fecha\t\t");
                    strBuilder.append("Hora\t\t");
                    strBuilder.append("Tipo Transacción\t");
                    strBuilder.append(Util.ajustarDato("Descripción", 30) + "\t\t");
                    strBuilder.append("Importe\r\n");
                    for(int i=0 ; i<beanXML.getM_Movimientos().size() ; i++){
                        bean = (BeanDetalleMovimiento)beanXML.getM_Movimientos().get(i);
                        strBuilder.append(bean.getM_Fecha() + "\t");
                        strBuilder.append(bean.getM_Hora() + "\t");
                        strBuilder.append(bean.getM_TipoTrx() + "\t\t\t");
                        strBuilder.append(Util.ajustarDato(bean.getM_Descripcion(), 30) + "\t\t");
                        if("+".equals(bean.getM_Signo())){
                            strBuilder.append(bean.getM_Importe() + "\r\n");
                        }else{
                            strBuilder.append(bean.getM_Signo() + bean.getM_Importe() + "\r\n");
                        }
                        
                    }

                    PrintWriter out = new PrintWriter(response.getOutputStream());
                    out.println(strBuilder);
                    out.flush();
                    out.close();

                    response.getOutputStream().flush();
                    response.getOutputStream().close();

                    return null;
                }
                else if("save".equalsIgnoreCase(accion) && "excel".equalsIgnoreCase(formato)){ //descarga excel{
                    //se realizara la descarga usando POI
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String nombre_archivo = "consulta-"+sdf.format(new Date())+".xls";
                    response.setHeader("Content-Disposition","attachment; filename=\""+nombre_archivo+"\"");
                    response.setContentType("application/vnd.ms-excel");
                    
                    ArrayList lblColumnas = new ArrayList();
                    lblColumnas.add("Fecha");
                    lblColumnas.add("Hora");
                    lblColumnas.add("Tipo Transacción");
                    lblColumnas.add("Descripción");
                    lblColumnas.add("Importe");

                    ArrayList lstData = new ArrayList();
                    BeanDetalleMovimiento bean = null;
                    for(int i=0 ; i<beanXML.getM_Movimientos().size() ; i++){
                        bean = (BeanDetalleMovimiento)beanXML.getM_Movimientos().get(i);
                        if("+".equals(bean.getM_Signo())){
                            lstData.add(new String []{bean.getM_Fecha(),bean.getM_Hora(),bean.getM_TipoTrx(),bean.getM_Descripcion(),bean.getM_Importe()});
                        }else{
                            lstData.add(new String []{bean.getM_Fecha(),bean.getM_Hora(),bean.getM_TipoTrx(),bean.getM_Descripcion(),bean.getM_Signo() + bean.getM_Importe()});
                        }                        
                    }
                    HSSFWorkbook libroXLS = GeneradorPOI.crearExcel(nombre_archivo, lblColumnas, lstData, beanXML, null);
                    if(libroXLS!=null){
                        libroXLS.write(response.getOutputStream());
                        response.getOutputStream().close();
                        response.getOutputStream().flush();
                    }
                    return null;
                }
                else{ //se enviara a pagina para imprimir
                    beanXML.setM_Cuenta(m_Cuenta); //colocamos el numero de cuenta a mostrarse en pagina
                    request.setAttribute("beanMovimientos", beanXML);
                    request.setAttribute("resultListaMovimientos", beanXML.getM_Movimientos());
                }

            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }

      }catch(Exception ex){
        request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
        logger.error(ex.toString());
      }

      return mapping.findForward("exportarHistoricoMov");
     
    }

    /**
     * jwong 19/02/2009
     * buscarSaldosPromedios, para el manejo de la consulta a saldos promedios
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward buscarSaldosPromedios(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML)session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }        
        //obtenemos los valores seleccionados en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;       
        String m_FecFin = consultaSaldosForm.getM_FecFin();

        //jwong 03/03/2009 obtenemos la empresa seleccionada al cargar la pagina
        String m_EmpresaSel = consultaSaldosForm.getM_EmpresaSel();
        //jwong 03/03/2009 para obtener el RUC de la empresa y el id del cliente
        String cod_cliente = null;
        String ruc_empresa = null;
        String part[] = null;
        if(m_EmpresaSel!=null && m_EmpresaSel.contains(";")){
            part = m_EmpresaSel.split(";");
            ruc_empresa = part[0];
            cod_cliente = part[1];
        }

        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        //obtenemos los parametros de la cuenta para buscar sus movimientos
        String m_Cuenta = request.getParameter("m_Cuenta");
        String m_TrxDate = "";
        if(m_FecFin!=null && m_FecFin.length()>9){
            // dd/MM/yyyy
            String dia = m_FecFin.substring(0, 2);
            String mes = m_FecFin.substring(3, 5);
            String anio = m_FecFin.substring(8, 10);
            m_TrxDate = anio + mes + dia;
        }

        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_EST_CUENTA); //id de la transaccion
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT_NUMBER, m_Cuenta); //numero de cuenta
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_TRANSACTION_DATE, m_TrxDate); //yyMMdd
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
        listaParametros.add(beanNodo);
        String tipoPaginado = request.getParameter("tipoPaginado");
        if(tipoPaginado != null && !tipoPaginado.equals("")){//consulta la paginacion
            BeanPaginacion bpag = (BeanPaginacion) session.getAttribute("bpCons");
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
            session.setAttribute("bpCons", bpag);
            long secuencial = bpag.getM_seleccion();
            String numSecuencial = String.valueOf(secuencial);
            while (numSecuencial.length() < 4) {
                numSecuencial = "0" + numSecuencial;
            }
            //secuencial, que se usará para la paginacion de esta consulta
            listaParametros.add(new BeanNodoXML(Constantes.TAG_SECUENCIAL, numSecuencial));
        }else{//se consulta por primera vez
            listaParametros.add(new BeanNodoXML(Constantes.TAG_SECUENCIAL, "0000"));
        }       

      try{
        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //jwong 18/01/2009 obtenemos el nro de decimales a formatear en los montos del tipo de cambio
            Propiedades prop = (Propiedades)context.getAttribute("propiedades");
            //jmoreno 23/09/2009 Para realizar la paginacion de la consulta de estados de cuenta(Saldos promedios)
            int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
            if(tipoPaginado==null){
                if (send_number > 0) {
                    int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("total_number"));
                    int nroPag = (int) total_number / send_number;
                    int resto = (int) total_number % send_number;
                    if (resto != 0) {
                        nroPag = nroPag + 1;
                    }
                    BeanPaginacion bpag = new BeanPaginacion();
                    bpag.setM_pagActual(1);
                    bpag.setM_pagFinal(nroPag);
                    bpag.setM_pagInicial(1);
                    bpag.setM_regPagina(send_number);
                    bpag.setM_ultimoRegAct(send_number);
                    session.setAttribute("bpCons", bpag);
                }
            }
            
            BeanConsMovHistoricosXML beanXML = null;
            beanXML = ParserXML.listarSaldosPromedios(beanResXML.getM_Respuesta(), "yyyyMMdd", prop.getM_FormatoHoraWS(),send_number);
            //si es la primera vez que se realiza la consulta, se guarda el nombre del titular ya que la cons. del Ibs no lo devuelve las otras veces
            if(tipoPaginado==null){
                session.setAttribute("m_Titular",beanXML.getM_Titular());
            }
            
            if(beanXML!=null){
                //jwong 09/03/2009 para obtener la moneda
                String desMoneda = request.getParameter("m_Moneda");
                beanXML.setM_Moneda(desMoneda);                
                beanXML.setM_Cuenta(m_Cuenta); //colocamos el numero de cuenta a mostrarse en pagina

                //jwong 25/03/2009 obtenemos los saldos enviados desde pagina
                String m_SaldoDisponible = request.getParameter("m_SaldoDisponible");
                String m_SaldoRetenido = request.getParameter("m_SaldoRetenido");
                String m_SaldoContable = request.getParameter("m_SaldoContable");
                beanXML.setM_SaldoDisponible(m_SaldoDisponible);
                beanXML.setM_SaldoRetenido(m_SaldoRetenido);
                beanXML.setM_SaldoContable(m_SaldoContable);
                beanXML.setM_Titular((String)session.getAttribute("m_Titular"));
                
                request.setAttribute("beanSaldosPromedios", beanXML);

                if(beanXML.getM_Movimientos()!=null && beanXML.getM_Movimientos().size()>0){
                    request.setAttribute("resultListaMovimientos", beanXML.getM_Movimientos());
                    request.setAttribute("m_Cuenta", m_Cuenta);

                    String saldoDisponibleExp = "";
                    String saldoRetenidoExp = "";
                    String saldoContableExp = "";
                    if(beanXML.getM_SaldoDisponible()!=null){
                        saldoDisponibleExp = (("-".equals(beanXML.getM_SignoDisponible()))?"-":"") + beanXML.getM_SaldoDisponible();
                    }
                    if(beanXML.getM_SaldoRetenido()!=null){
                        saldoRetenidoExp = (("-".equals(beanXML.getM_SignoRetenido()))?"-":"") + beanXML.getM_SaldoRetenido();
                    }
                    if(beanXML.getM_SaldoContable()!=null){
                        saldoContableExp = (("-".equals(beanXML.getM_SignoContable()))?"-":"") + beanXML.getM_SaldoContable();
                    }
                    //jwong 18/07/2009 enviamos los saldos para la descarga
                    request.setAttribute("saldoDisponibleExp", saldoDisponibleExp);
                    request.setAttribute("saldoRetenidoExp", saldoRetenidoExp);
                    request.setAttribute("saldoContableExp", saldoContableExp);

                    request.setAttribute("m_Moneda", desMoneda);
                    
                    //request.setAttribute("m_TipoCuenta", m_TipoCuenta);
                }
                else{
                    request.setAttribute("mensaje", "No se encontraron movimientos con esos parámetros de búsqueda");
                }

            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
      }catch(Exception ex){
        request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
        logger.error(ex.toString());
      }
        //saldos promedios
        return mapping.findForward("cargarSaldosPromedios");
    }

     public ActionForward buscarSaldosPromediosPag(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML)session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }        
        String ruc_empresa = null;
        String m_Cuenta = ((ConsultaSaldosForm)form).getM_Cuenta();
        String m_TrxDate = "";

        String m_FecFin = ((ConsultaSaldosForm)form).getM_FecFin();
        String m_EmpresaSel = ((ConsultaSaldosForm)form).getM_EmpresaSel();
        String part[] = null;
        if(m_EmpresaSel!=null && m_EmpresaSel.contains(";")){
            part = m_EmpresaSel.split(";");
            ruc_empresa = part[0];
        }
        if(m_FecFin!=null && m_FecFin.length()>9){
            // dd/MM/yyyy
            String dia = m_FecFin.substring(0, 2);
            String mes = m_FecFin.substring(3, 5);
            String anio = m_FecFin.substring(8, 10);
            m_TrxDate = anio + mes + dia;
        }
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;
        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_EST_CUENTA); //id de la transaccion
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT_NUMBER, m_Cuenta); //numero de cuenta
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_TRANSACTION_DATE, m_TrxDate); //yyMMdd
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
        listaParametros.add(beanNodo);

        BeanPaginacion bpag = (BeanPaginacion)session.getAttribute("bpCons");
        String tipoPaginado = request.getParameter("tipoPaginado");
        tipoPaginado = tipoPaginado==null ? "P" : tipoPaginado;
        if("P".equals(tipoPaginado)){
            bpag.setM_pagActual(1);
        }else if("U".equals(tipoPaginado)){
            bpag.setM_pagActual(bpag.getM_pagFinal());
        }else if ("S".equals(tipoPaginado)){
            if(bpag.getM_pagActual()< bpag.getM_pagFinal()){
                bpag.setM_pagActual(bpag.getM_pagActual()+1);
            }

        }else if("A".equals(tipoPaginado)){
            if(bpag.getM_pagActual()> bpag.getM_pagInicial()){
                bpag.setM_pagActual(bpag.getM_pagActual()- 1);
            }
        }
        session.setAttribute("bpCons",bpag);
        long secuencial = bpag.getM_seleccion();
        String numSecuencial = String.valueOf(secuencial);
        while (numSecuencial.length() < 4) {
            numSecuencial = "0" + numSecuencial;
        }
        beanNodo = new BeanNodoXML(Constantes.TAG_SECUENCIAL, numSecuencial); //secuencial, que se usará para la paginacion de esta consulta
        listaParametros.add(beanNodo);

      try{
        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //jwong 18/01/2009 obtenemos el nro de decimales a formatear en los montos del tipo de cambio
            Propiedades prop = (Propiedades)context.getAttribute("propiedades");
            //jmoreno 23/09/2009 Para realizar la paginacion de la consulta de estados de cuenta(Saldos promedios)
           
            BeanConsMovHistoricosXML beanXML = null;
            int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
            beanXML = ParserXML.listarSaldosPromedios(beanResXML.getM_Respuesta(), "yyyyMMdd", prop.getM_FormatoHoraWS(),send_number);

            if(beanXML!=null){
                //jwong 09/03/2009 para obtener la moneda
                String desMoneda = request.getParameter("m_Moneda");
                beanXML.setM_Moneda(desMoneda);
                beanXML.setM_Cuenta(m_Cuenta); //colocamos el numero de cuenta a mostrarse en pagina

                //jwong 25/03/2009 obtenemos los saldos enviados desde pagina
                String m_SaldoDisponible = request.getParameter("m_SaldoDisponible");
                String m_SaldoRetenido = request.getParameter("m_SaldoRetenido");
                String m_SaldoContable = request.getParameter("m_SaldoContable");
                beanXML.setM_SaldoDisponible(m_SaldoDisponible);
                beanXML.setM_SaldoRetenido(m_SaldoRetenido);
                beanXML.setM_SaldoContable(m_SaldoContable);

                request.setAttribute("beanSaldosPromedios", beanXML);

                if(beanXML.getM_Movimientos()!=null && beanXML.getM_Movimientos().size()>0){
                    request.setAttribute("resultListaMovimientos", beanXML.getM_Movimientos());
                    request.setAttribute("m_Cuenta", m_Cuenta);

                    String saldoDisponibleExp = "";
                    String saldoRetenidoExp = "";
                    String saldoContableExp = "";
                    if(beanXML.getM_SaldoDisponible()!=null){
                        saldoDisponibleExp = (("-".equals(beanXML.getM_SignoDisponible()))?"-":"") + beanXML.getM_SaldoDisponible();
                    }
                    if(beanXML.getM_SaldoRetenido()!=null){
                        saldoRetenidoExp = (("-".equals(beanXML.getM_SignoRetenido()))?"-":"") + beanXML.getM_SaldoRetenido();
                    }
                    if(beanXML.getM_SaldoContable()!=null){
                        saldoContableExp = (("-".equals(beanXML.getM_SignoContable()))?"-":"") + beanXML.getM_SaldoContable();
                    }
                    //jwong 18/07/2009 enviamos los saldos para la descarga
                    request.setAttribute("saldoDisponibleExp", saldoDisponibleExp);
                    request.setAttribute("saldoRetenidoExp", saldoRetenidoExp);
                    request.setAttribute("saldoContableExp", saldoContableExp);
                    request.setAttribute("m_Moneda", desMoneda);                    
                }
                else{
                    request.setAttribute("mensaje", "No se encontraron movimientos con esos parámetros de búsqueda");
                }

            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
      }catch(Exception ex){
        request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
        logger.error(ex.toString());
      }
        //saldos promedios
        return mapping.findForward("cargarSaldosPromedios");
    }
    
    //jwong 30/01/2009 para el manejo de la impresion/guardar archivo
    public ActionForward exportarSaldosPromedios(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos los valores seleccionados en la pagina
        ConsultaSaldosForm consultaSaldosForm = (ConsultaSaldosForm)form;        
        String m_FecFin = consultaSaldosForm.getM_FecFin();

        //jwong 03/03/2009 obtenemos la empresa seleccionada al cargar la pagina
        String m_EmpresaSel = consultaSaldosForm.getM_EmpresaSel();
        //jwong 03/03/2009 para obtener el RUC de la empresa y el id del cliente
        String cod_cliente = null;
        String ruc_empresa = null;
        String part[] = null;
        if(m_EmpresaSel!=null && m_EmpresaSel.contains(";")){
            part = m_EmpresaSel.split(";");
            ruc_empresa = part[0];
            cod_cliente = part[1];
        }

        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        //obtenemos los parametros de la cuenta para buscar sus movimientos
        String m_Cuenta = request.getParameter("m_Cuenta");
        String m_TrxDate = "";
        if(m_FecFin!=null && m_FecFin.length()>9){
            // dd/MM/yyyy
            String dia = m_FecFin.substring(0, 2);
            String mes = m_FecFin.substring(3, 5);
            String anio = m_FecFin.substring(8, 10);
            m_TrxDate = anio + mes + dia;
        }

        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_EST_CUENTA); //id de la transaccion
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT_NUMBER, m_Cuenta); //numero de cuenta
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML(Constantes.TAG_TRANSACTION_DATE, m_TrxDate); //yyMMdd
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
        listaParametros.add(beanNodo);
        BeanPaginacion bpag =(BeanPaginacion) session.getAttribute("bpCons");
        if(bpag != null){
            long secuencial = bpag.getM_seleccion();
            String numSecuencial = String.valueOf(secuencial);
            while (numSecuencial.length() < 4) {
                numSecuencial = "0" + numSecuencial;
            }
            beanNodo = new BeanNodoXML(Constantes.TAG_SECUENCIAL, numSecuencial);
        }else{
            beanNodo = new BeanNodoXML(Constantes.TAG_SECUENCIAL, "0000");
        }
        
        listaParametros.add(beanNodo);
      try{
        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //jwong 18/01/2009 obtenemos el nro de decimales a formatear en los montos del tipo de cambio
            Propiedades prop = (Propiedades)context.getAttribute("propiedades");
                     
            //jmoreno 23/09/2009 Para realizar la paginacion de la consulta de estados de cuenta(Saldos promedios)
            //int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("total_number"));
            int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
            BeanConsMovHistoricosXML beanXML = null;
            //if (total_number == send_number) {
                //procesamos la respuesta(parseo xml) y enviamos a la pagina
                beanXML = ParserXML.listarSaldosPromedios(beanResXML.getM_Respuesta(), "yyyyMMdd", prop.getM_FormatoHoraWS(),send_number);
            /*}else{
                beanXML = ParserXML.listarEstadoCuentasPag(beanResXML.getM_Respuesta(), "yyyyMMdd", prop.getM_FormatoHoraWS(),
                        total_number, send_number, cashclienteWS, m_Cuenta, m_TrxDate, ruc_empresa);
            }*/
            if(beanXML!=null){
                //jwong 09/03/2009 para obtener la moneda
                String desMoneda = request.getParameter("m_Moneda");
                beanXML.setM_Moneda(desMoneda);
                beanXML.setM_Cuenta(m_Cuenta); //colocamos el numero de cuenta a mostrarse en pagina

                //jwong 18/07/2009 obtenemos de pagina los valores de los saldos para la descarga
                String m_SaldoDisponible = request.getParameter("saldoDisponibleExp");
                String m_SaldoRetenido = request.getParameter("saldoRetenidoExp");
                String m_SaldoContable = request.getParameter("saldoContableExp");

                beanXML.setM_SaldoDisponible(m_SaldoDisponible);
                beanXML.setM_SaldoRetenido(m_SaldoRetenido);
                beanXML.setM_SaldoContable(m_SaldoContable);
                beanXML.setM_Titular((String)session.getAttribute("m_Titular"));
                
                request.setAttribute("beanSaldosPromedios", beanXML);
                
                if(beanXML.getM_Movimientos()!=null && beanXML.getM_Movimientos().size()>0){
                    //verificamos si se quiere exportar a formato de texto
                    String accion = request.getParameter("accion");
                    String formato = request.getParameter("formato");
                    if("save".equalsIgnoreCase(accion) && "txt".equalsIgnoreCase(formato)){ //descarga texto
                        String nomb = Fecha.getFechaActual("yyyyMMdd_HHmmss");
                        String nombre_archivo = "Rep" + nomb + ".txt";
                        //enviamos los parametros necesarios al response para que el SO lo tome como formato excel
                        response.setHeader("Content-Disposition","attachment; filename=\"" + nombre_archivo + "\"");
                        response.setContentType("text/plain");

                        StringBuilder strBuilder = new StringBuilder();
                        BeanDetalleMovimiento bean = null;
                        //1ero creamos los titulos de las columnas
                        strBuilder.append("\t\t\tDETALLE DE SALDO\r\n");
                        strBuilder.append("Titular: " + beanXML.getM_Titular() + "\t");
                        strBuilder.append("Moneda: " + beanXML.getM_Moneda() + "\t");
                        strBuilder.append("Cuenta: " + beanXML.getM_Cuenta() + "\r\n\r\n");

                        strBuilder.append("\tSaldo Disponible\t\t\t\t\t\t" + beanXML.getM_SaldoDisponible() + "\r\n");
                        strBuilder.append("\tSaldo Retenido  \t\t\t\t\t\t" + beanXML.getM_SaldoRetenido() + "\r\n");
                        strBuilder.append("\tSaldo Contable  \t\t\t\t\t\t" + beanXML.getM_SaldoContable() + "\r\n\r\n");

                        strBuilder.append("\t\t\tDETALLE DE MOVIMIENTOS\r\n");
                        strBuilder.append("Fecha\t\t");
                        strBuilder.append("Hora\t\t");
                        strBuilder.append("Tipo Transacción\t");
                        strBuilder.append(Util.ajustarDato("Descripción", 30) + "\t\t");
                        strBuilder.append(Util.ajustarDato("Importe", 20) + "\t\t");//jmoreno 28-08-09
                        strBuilder.append("Saldo\r\n");
                        for(int i=0 ; i<beanXML.getM_Movimientos().size() ; i++){
                            bean = (BeanDetalleMovimiento)beanXML.getM_Movimientos().get(i);
                            strBuilder.append(bean.getM_Fecha() + "\t");
                            strBuilder.append(bean.getM_Hora() + "\t");
                            strBuilder.append(bean.getM_TipoTrx() + "\t\t\t");
                            strBuilder.append(Util.ajustarDato(bean.getM_Descripcion(), 30) + "\t\t");
                            strBuilder.append(Util.ajustarDato(bean.getM_Importe(),20) + "\t\t");
                            strBuilder.append(Util.ajustarDato(bean.getM_SaldoMovimiento(),20) + "\r\n");
                        }
                        PrintWriter out = new PrintWriter(response.getOutputStream());
                        out.println(strBuilder);
                        out.flush();
                        out.close();

                        response.getOutputStream().flush();
                        response.getOutputStream().close();

                        return null;
                    }
                    else if("save".equalsIgnoreCase(accion) && "excel".equalsIgnoreCase(formato)){ //descarga excel{
                        //se realizara la descarga usando POI
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String nombre_archivo = "consulta-"+sdf.format(new Date())+".xls";
                        response.setHeader("Content-Disposition","attachment; filename=\""+nombre_archivo+"\"");
                        response.setContentType("application/vnd.ms-excel");

                        ArrayList lblColumnas = new ArrayList();
                        lblColumnas.add("Fecha");
                        lblColumnas.add("Hora");
                        lblColumnas.add("Tipo Transacción");
                        lblColumnas.add("Descripción");
                        lblColumnas.add("Importe");
                        lblColumnas.add("Saldo");//jmoreno 28-08-09

                        ArrayList lstData = new ArrayList();
                        BeanDetalleMovimiento bean = null;
                        for(int i=0 ; i<beanXML.getM_Movimientos().size() ; i++){
                            bean = (BeanDetalleMovimiento)beanXML.getM_Movimientos().get(i);
                            lstData.add(new String []{bean.getM_Fecha(),bean.getM_Hora(),bean.getM_TipoTrx(),bean.getM_Descripcion(),/*bean.getM_Signo() +*/ bean.getM_Importe(),bean.getM_SaldoMovimiento()});
                        }
                        HSSFWorkbook libroXLS = GeneradorPOI.crearExcel(nombre_archivo, lblColumnas, lstData, beanXML, null);
                        if(libroXLS!=null){
                            libroXLS.write(response.getOutputStream());
                            response.getOutputStream().close();
                            response.getOutputStream().flush();
                        }
                        return null;
                    }
                    else{ //se enviara a pagina para imprimir
                        beanXML.setM_Cuenta(m_Cuenta); //colocamos el numero de cuenta a mostrarse en pagina
                        request.setAttribute("beanMovimientos", beanXML);
                        request.setAttribute("resultListaMovimientos", beanXML.getM_Movimientos());
                    }
                }
                else{
                    request.setAttribute("mensaje", "No se encontraron movimientos con esos parámetros de búsqueda");
                }
            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
      }catch(Exception ex){
        request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
        logger.error(ex.toString());
      }
      return mapping.findForward("exportarSaldosPromedios");
    }
    
    /**
     * jwong 03/08/2009 exportarRelacionesBanco
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward exportarRelacionesBco(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        //obtenemos la empresa seleccionada en la pagina
        String m_Empresa = request.getParameter("m_EmpresaExp");
        
        //jwong 09/03/2009 para obtener el RUC de la empresa y el id del cliente
        String cod_cliente = null;
        String ruc_empresa = null;
        String part[] = null;
        if(m_Empresa!=null && m_Empresa.contains(";")){
            part = m_Empresa.split(";");
            ruc_empresa = part[0];
            cod_cliente = part[1];
        }
        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        //jwong 09/03/2009 se usara la transaccion de consulta de saldos
        beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_CTAS_CLIENTE); //id de la transaccion
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_CLIENT_CODE, cod_cliente); //codigo de la empresa
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
        listaParametros.add(beanNodo);

        try{
            String req = GenRequestXML.getXML(listaParametros);
            resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
            if(resultado==null || "".equals(resultado)){
                //deberiamos retornar a la pagina con un mensaje de error
                request.setAttribute("mensaje", "No se encontraron resultados");
                return mapping.findForward("error");
            }
            //se debe parsear el xml obtenido
            BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
            BeanTypeAccount beanType = null;
            BeanConsultaSaldosXML beanaccount = null;
            String lblCol = null;
            //si la respuesta es exitosa
            if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
                //jwong 14/08/2009 comentado
                /*
                //procesamos la respuesta(parseo xml) y enviamos a la pagina
                BeanConsRelBanco beanConsRelBco = ParserXML.listarRelacionesBco(beanResXML.getM_Respuesta(), ruc_empresa);
                */
                int total_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("total_number"));
                int send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
                BeanConsRelBanco beanConsRelBco = null;
                if(total_number == send_number){
                    beanConsRelBco = ParserXML.listarRelacionesBco(beanResXML.getM_Respuesta(), ruc_empresa);
                }else{
                    beanConsRelBco = ParserXML.listarRelacionesBcoPag(cod_cliente, ruc_empresa, beanResXML.getM_Respuesta(), total_number, send_number, cashclienteWS);
                }
                
                if(beanConsRelBco!=null && beanConsRelBco.getM_Accounts()!=null && beanConsRelBco.getM_Accounts().size()>0){
                    request.setAttribute("beanConsRelBco", beanConsRelBco);
                    request.setAttribute("listaRelaciones", beanConsRelBco.getM_AccountsType());

                    //verificamos si se quiere exportar a formato de texto
                    String accion = request.getParameter("accion");
                    String formato = request.getParameter("formato");
                    if("save".equalsIgnoreCase(accion) && "txt".equalsIgnoreCase(formato)){ //descarga texto
                        String nomb = Fecha.getFechaActual("yyyyMMdd_HHmmss");
                        String nombre_archivo = "Rep" + nomb + ".txt";
                        //enviamos los parametros necesarios al response para que el SO lo tome como formato excel
                        response.setHeader("Content-Disposition","attachment; filename=\"" + nombre_archivo + "\"");
                        response.setContentType("text/plain");

                        StringBuilder strBuilder = new StringBuilder();

                        //1ero creamos los titulos de las columnas
                        strBuilder.append(Util.ajustarDato("Sectorista", 50));
                        strBuilder.append("Funcionario Cash" + "\r\n");
                        

                        lblCol = beanConsRelBco.getM_NameSec();
                        lblCol = lblCol==null?"":lblCol;
                        strBuilder.append(Util.ajustarDato("Nombre:" + lblCol, 50));
                        lblCol = beanConsRelBco.getM_NameFunc();
                        lblCol = lblCol==null?"":lblCol;
                        strBuilder.append("Nombre:" + lblCol + "\r\n");

                        lblCol = beanConsRelBco.getM_PhoneSec();
                        lblCol = lblCol==null?"":lblCol;
                        strBuilder.append(Util.ajustarDato("Teléfono:" + lblCol, 50));
                        lblCol = beanConsRelBco.getM_PhoneFunc();
                        lblCol = lblCol==null?"":lblCol;
                        strBuilder.append("Teléfono:" + lblCol + "\r\n");

                        lblCol = beanConsRelBco.getM_EmailSec();
                        lblCol = lblCol==null?"":lblCol;
                        strBuilder.append(Util.ajustarDato("Email:" + lblCol, 50));
                        lblCol = beanConsRelBco.getM_EmailFunc();
                        lblCol = lblCol==null?"":lblCol;
                        strBuilder.append("Email:" + lblCol + "\r\n");
                        
                        strBuilder.append("\t\tDETALLE DE RELACIONES CON EL BANCO\r\n");
                        for(int i=0 ; i<beanConsRelBco.getM_AccountsType().size() ; i++){
                            beanType = (BeanTypeAccount)beanConsRelBco.getM_AccountsType().get(i);
                            strBuilder.append("\t\t"+beanType.getM_Description() + "\r\n");

                            strBuilder.append(Util.ajustarDato("Cuenta", 20));
                            strBuilder.append(Util.ajustarDato("Moneda", 10));
                            strBuilder.append(Util.ajustarDato("Saldo Disponible", 18));
                            strBuilder.append("Saldo Contable\r\n");

                            for(int j=0 ; j<beanType.getM_Accounts().size() ; j++){
                                beanaccount = (BeanConsultaSaldosXML)beanType.getM_Accounts().get(j);
                                lblCol = beanaccount.getM_Cuenta();
                                lblCol = lblCol==null?"":lblCol;
                                strBuilder.append(Util.ajustarDato(lblCol, 20));

                                lblCol = beanaccount.getM_Moneda();
                                lblCol = lblCol==null?"":lblCol;
                                strBuilder.append(Util.ajustarDato(lblCol, 10));

                                lblCol = beanaccount.getM_SaldoDisponible();
                                lblCol = lblCol==null?"":lblCol;
                                strBuilder.append(Util.ajustarDato(lblCol, 18));

                                lblCol = beanaccount.getM_SaldoContable();
                                lblCol = lblCol==null?"":lblCol;
                                strBuilder.append(lblCol + "\r\n");
                            }
                            strBuilder.append("\r\n");
                        }
                        PrintWriter out = new PrintWriter(response.getOutputStream());
                        out.println(strBuilder);
                        out.flush();
                        out.close();

                        response.getOutputStream().flush();
                        response.getOutputStream().close();

                        return null;
                    }
                    else if("save".equalsIgnoreCase(accion) && "excel".equalsIgnoreCase(formato)){ //descarga excel{
                        //se realizara la descarga usando POI
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                        String nombre_archivo = "consulta-"+sdf.format(new Date())+".xls";
                        response.setHeader("Content-Disposition","attachment; filename=\""+nombre_archivo+"\"");
                        response.setContentType("application/vnd.ms-excel");

                        HSSFWorkbook libroXLS = GeneradorPOI.crearExcelRelacionesBco(nombre_archivo, beanConsRelBco, (ArrayList)beanConsRelBco.getM_AccountsType(), null);
                        if(libroXLS!=null){
                            libroXLS.write(response.getOutputStream());
                            response.getOutputStream().close();
                            response.getOutputStream().flush();
                        }
                        return null;
                    }
                    else{ //se enviara a pagina para imprimir
                        request.setAttribute("beanConsRelBco", beanConsRelBco);
                        request.setAttribute("listaRelaciones", beanConsRelBco.getM_AccountsType());
                    }
                }
                else{
                    request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
                }
            }
            else{
                //mostraremos un mensaje de error en pagina
                request.setAttribute("mensaje", "No se encontraron resultados");
            }
        }catch(Exception ex){
            request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            logger.error(ex.toString());
        }
        return mapping.findForward("exportarRelacionesBco");
    }
    
    
    
    public ActionForward iniciarCronograma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		String nroCuenta = request.getParameter("nroCuenta");
		String tipo = request.getParameter("tipoCronograma");
		try {			
			CronogramaPaginado paginado = new CronogramaPaginado(20, nroCuenta, tipo);
			PrestamoAdapter prestamo = paginado.getPrestamo();
			session.setAttribute("prestamo", prestamo);
			session.setAttribute("paginado", paginado);
			request.setAttribute("cuotas", prestamo.getCuotas());
			session.setAttribute("tipo", tipo);
			return mapping.findForward("iniciaCronograma");
		} catch (Exception e) {
			logger.error(e);
			request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
			return mapping.findForward("error");
		}			
	}	
	
	public ActionForward paginarCronograma(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try{
			HttpSession session = request.getSession();
			int nroPagina = Integer.valueOf(request.getParameter("nroPagina"));
			CronogramaPaginado paginado = (CronogramaPaginado)session.getAttribute("paginado");
			paginado.setNroPagina(nroPagina);
			session.setAttribute("paginado", paginado);
			request.setAttribute("cuotas", paginado.getItemsPagina());
			return mapping.findForward("paginaCronograma");
		}catch(Exception e){			
			logger.error(e);
			request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
			return mapping.findForward("error");
		}		
	}	
	
	
	public ActionForward buscarLiquidador(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletConfig()
				.getServletContext();
		try {			
			RelacionesBanco rb = new RelacionesBanco();
			String nroCuenta = request.getParameter("nroCuenta");			
			String nroCuotas =  request.getParameter("nroCuotas");
			LiquidadorAdapter liquidador ;
			if( nroCuotas == null || nroCuotas.length() == 0 ){
				nroCuotas = "1";				
			}
			int nro = Integer.valueOf(nroCuotas);
			rb.generarLiquidador(nroCuenta, nro, new Date());
			liquidador =  rb.generarLiquidador(nroCuenta, nro, new Date());
			request.setAttribute("liquidador", liquidador);		
			request.setAttribute("nroCuotas", nroCuotas);		
		} catch (Exception e) {
			request.setAttribute("mensaje","Se encontraron problemas al procesar la información");
			logger.error("Buscar Liquidador",e);
			return mapping.findForward("error");
		}
		return mapping.findForward("buscarLiquidador");
	}

	public ActionForward exportarCronograma(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try{
			HttpSession session = request.getSession();
			ServletContext context = getServlet().getServletConfig().getServletContext();
			int accion =  Integer.valueOf( request.getParameter("accion"));
			CronogramaPaginado paginado = (CronogramaPaginado)session.getAttribute("paginado");
			if( accion == 2 ){
				String nombre_archivo = "Cronograma-" + Util.strFecha()+ ".txt";
				response.setHeader("Content-Disposition", "attachment; filename=\""			+ nombre_archivo + "\"");
				response.setContentType("text/plain");
				String texto =  paginado.obtenerCronogramaTexto();
				PrintWriter out = new PrintWriter(response.getOutputStream());
				out.println(texto);
				out.flush();
				out.close();
				response.getOutputStream().flush();
				response.getOutputStream().close();
				return null;
			}else if( accion == 1 ){				
				String nombre_archivo = "Cronograma-" + Util.strFecha()+ ".xls";
				response.setHeader("Content-Disposition", "attachment; filename=\""	+ nombre_archivo + "\"");
				response.setContentType("application/vnd.ms-excel");

				HSSFWorkbook libroXLS = paginado.obtenerCronogramaExcel();
				if (libroXLS != null) {
					libroXLS.write(response.getOutputStream());
					response.getOutputStream().close();
					response.getOutputStream().flush();
				}
			
				return null;
			}else{
				request.setAttribute("prestamo", paginado.getPrestamoTotal());
				request.setAttribute("tipo", paginado.getTipo());
				return mapping.findForward("exportarCronograma");
			}		
			
		}catch(Exception e){
			logger.error("Exportando Cronograma",e);
			request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");			
			return mapping.findForward("error");
		}			
	}
	
	public ActionForward exportarLiquidador(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try{
			String nroCuenta = request.getParameter("nroCuenta");			
			String nroCuotas =  request.getParameter("nroCuotas");
			RelacionesBanco rb = new RelacionesBanco();
			int accion =  Integer.valueOf( request.getParameter("accion"));
			LiquidadorAdapter liquidador ;		
			
			if( nroCuotas == null || nroCuotas.length() == 0 ){
				nroCuotas = "1";				
			}
			int nro = Integer.valueOf(nroCuotas);
			
			if( accion == 2 ){
				String nombre_archivo = "Liquidador-" + Util.strFecha()+ ".txt";
				response.setHeader("Content-Disposition", "attachment; filename=\""			+ nombre_archivo + "\"");
				response.setContentType("text/plain");
				String texto = rb.generarArchivoLiquidador(nroCuenta, nro, new Date()); 
				PrintWriter out = new PrintWriter(response.getOutputStream());
				out.println(texto);
				out.flush();
				out.close();
				response.getOutputStream().flush();
				response.getOutputStream().close();
				return null;
			}else if( accion == 1 ){	
				
				String nombre_archivo = "Liquidador-" + Util.strFecha()+ ".xls";
				response.setHeader("Content-Disposition", "attachment; filename=\""	+ nombre_archivo + "\"");
				response.setContentType("application/vnd.ms-excel");

				HSSFWorkbook libroXLS = rb.generarExcelLiquidador(nroCuenta, nro, new Date());
				if (libroXLS != null) {
					libroXLS.write(response.getOutputStream());
					response.getOutputStream().close();
					response.getOutputStream().flush();
				}
			
				return null;
			}else{			
				rb.generarLiquidador(nroCuenta, nro, new Date());
				liquidador =  rb.generarLiquidador(nroCuenta, Integer.valueOf(nroCuotas), new Date());
				
				request.setAttribute("liquidador", liquidador);		
				request.setAttribute("nroCuotas", nroCuotas);
				return mapping.findForward("exportarLiquidador");
			}
		}catch(Exception e){
			request.setAttribute("mensaje","Se encontraron problemas al procesar la información");
			logger.error("Exportando Cronograma",e);
			return mapping.findForward("error");
		}	
		
	}
}

/*AprobacionesAction.java */

package com.hiper.cash.actions;

import com.financiero.cash.delegate.SeguridadDelegate;
import com.hiper.cash.dao.TaAprobacionOrdenDao;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TaServicioOpcionDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.hibernate.TaAprobacionOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioOpcionDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TpDetalleOrdenDaoHibernate;
import com.hiper.cash.domain.TaServicioOpcion;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.BeanServicio;
import com.hiper.cash.entidad.BeanSuccess;
import com.hiper.cash.entidad.BeanSuccessDetail;
import com.hiper.cash.forms.AprobacionOrdenForm;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;
import com.hiper.cash.xml.bean.BeanDataLoginXML;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

/**
 * AprobacionesAction: Clase que permite el manejo de toda
 * la lógica del módulo de Aprobaciones de la aplicación web
 * @version 1.0 01/01/2009
 * @author esilva
 * Copyright © HIPER S.A
 */
public class AprobacionesAction extends DispatchAction {

    /**
     * Método que carga la interfaz de usuario para realizar
     * aprobación de órdenes
     */
	
	private Logger logger =  Logger.getLogger(this.getClass());
	private SeguridadDelegate delegadoSeguridad = SeguridadDelegate.getInstance();
	private String idModulo =  null;
	
    public ActionForward cargarAprobaciones(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session.getAttribute("usuarioActual");

        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }else{        	
        	idModulo  = request.getParameter("modulo"); 
        	try{        		
        		if( !delegadoSeguridad.verificaDisponibilidad(idModulo)){
        			return mapping.findForward("fueraServicio");
        		}        			
        	}catch( Exception e){        		
        		logger.error("VALIDACION DE DISPONIBILIDAD", e);
        		return mapping.findForward("fueraServicio");        		
        	}        
        }
        session.removeAttribute("listaResult");
        //Autorizacion
        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarAprobaciones");
        }

        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa = (List) session.getAttribute("empresa");
        List lEmpresa = null;
        
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        
        if (beanDataLogXML.isM_usuarioEspecial()) {//verificamos si el usuario actual es Especial(Emp. Operaciones Cash)
            lEmpresa = (List) session.getAttribute("empresa");//obtenemos la lista de empresas de la session
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            //a partir de lEmpresas, obtenemos la lista de empresas que estan afiliadas al servicio
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_APROBACIONES);
            if (lEmpresa.size() == 0) {
                request.setAttribute("mensaje", "El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        } else {
            //hMapEmpresas contiene las empresas con sus respectivos servicios afiliados
            HashMap hMapEmpresas = (HashMap) session.getAttribute("hmEmpresas");
            //obtenemos la lista de empresas que estan afiliadas al servicio, segun la data obtenida del logeo en el hashMap
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_APROBACIONES);
            if (lEmpresa.size() == 0) {
                request.setAttribute("mensaje", "El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }

        //obtenemos los datos de la empresa que resulto al logearnos
       
        List listaEmpresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);
        //Codigo del usuario logueado
        List lUser = beanDataLogXML.getL_UserCodes();

        TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();
        boolean band = false;
        List listaServicios = null;
        //Verificamos si es un usuario afiliado a la empresa Operaciones Cash
        if (beanDataLogXML.isM_usuarioEspecial()) {
            if (listaEmpresas != null && listaEmpresas.size() > 0) {
                String cEmpresa = ((TmEmpresa) listaEmpresas.get(0)).getCemIdEmpresa();
                //obtenemos el listado de servicios relacionados con la empresa
                List tiposervicio = new ArrayList();
                tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_PAGO);
                tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_COBRO);
                tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_TRANSFERENCIAS);
                tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV);
                tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_LETRAS);
                listaServicios = servicioDAO.selectServicioxEmpresaxTipo(cEmpresa, tiposervicio);
                band = true;
            }

        } else {
            if (lUser != null && lUser.size() > 0) {
                HashMap hEmpresaUsers = new HashMap();
                lEmpresa = (List) session.getAttribute("empresa");//obtenemos la lista de empresas inicial de la session
                for (int i = 0; i < lEmpresa.size(); i++) {
                    hEmpresaUsers.put(lEmpresa.get(i), i);
                }
                session.setAttribute("hEmpresaUsers", hEmpresaUsers);
                String cEmpresa = ((TmEmpresa) listaEmpresas.get(0)).getCemIdEmpresa();
                Integer indice = (Integer) hEmpresaUsers.get(cEmpresa);
                if (indice.intValue() < lUser.size()) {
                    listaServicios = servicioDAO.selectServicioxEmpresaxAprobador((String) lUser.get(indice.intValue()));
                    band = true;
                }
            }
        }
        if (band) {
            session.setAttribute("listaEmpresas", listaEmpresas);
            session.setAttribute("listaServicios", listaServicios);
            //obtenemos el formulario
            AprobacionOrdenForm aprobOrdenesForm = (AprobacionOrdenForm) form;
            //reseteamos los valores del form
            aprobOrdenesForm.reset(mapping, request);
            aprobOrdenesForm.setM_Empresa(null);
            aprobOrdenesForm.setM_Servicio(null);
            aprobOrdenesForm.setValues();
            return mapping.findForward("cargarAprobaciones");
        } else {
            request.setAttribute("mensaje", "Ocurrió un error y no se pudo cargar la página.");
            return mapping.findForward("error");
        }

    }
    /* Método que permite realizar la búsqueda de servicios empresa por un
     * determinado código de usuario, porque verifica si el usuario es aprobador
     * de dicho servicio empresa.
     */

    public ActionForward buscarServiciosEmpUser(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
//        String indice = request.getParameter("indiceEmpresa");
        String idEmpresa = request.getParameter("idEmpresa");
        TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();
        List listaServicios = null;
        boolean band = false;

        //verificamos si es un usuario especial
        if (beanDataLogXML.isM_usuarioEspecial()) {
            //solo necesitamos el idEmpresa para recuperar los servicios
            List tiposervicio = new ArrayList();
            tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_PAGO);
            tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_COBRO);
            tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_TRANSFERENCIAS);
            tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV);
            tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_LETRAS);
            listaServicios = servicioDAO.selectServicioxEmpresaxTipo(idEmpresa, tiposervicio);
            band = true;
        } else {
            List lUser = beanDataLogXML.getL_UserCodes();
            HashMap hEmpresaUsers = (HashMap) session.getAttribute("hEmpresaUsers");
            Integer indice = (Integer) hEmpresaUsers.get(idEmpresa);
            if (indice.intValue() < lUser.size()) {
                listaServicios = servicioDAO.selectServicioxEmpresaxAprobador((String) lUser.get(indice.intValue()));
                lUser = null;
                band = true;
            }

        }
        if (band) {
            session.setAttribute("listaServicios", listaServicios);
            return mapping.findForward("cargarAprobaciones");
        } else {
            request.setAttribute("mensaje", "Ocurrió un error y no se pudo recargar la página.");
            return mapping.findForward("error");
        }

    }

    public ActionForward buscarOrdenesLinea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session.getAttribute("usuarioActual");

        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }else{
        	try{        		
        		if( !delegadoSeguridad.verificaDisponibilidad(idModulo)){
        			return mapping.findForward("fueraServicio");
        		}        			
        	}catch( Exception e){        		
        		logger.error("VALIDACION DE DISPONIBILIDAD", e);
        		return mapping.findForward("fueraServicio");        		
        	}        	
        }
        session.removeAttribute("beanPag");
        //Codigo del usuario logueado
        List lUser = beanDataLogXML.getL_UserCodes();

        AprobacionOrdenForm aprobOrdenesForm = (AprobacionOrdenForm) form;
//        String empresa = aprobOrdenesForm.getM_Empresa();
        String servicio = aprobOrdenesForm.getM_Servicio();

        List resultado = null;
        List lservicios = null;
        List lservicios_in = new ArrayList();

        //Todos los servicios
        if (servicio.equals(Constantes.TX_CASH_SERVICIO_TODOS)) {
            lservicios = (List) session.getAttribute("listaServicios");
            Iterator iter = lservicios.iterator();
            while (iter.hasNext()) {
                BeanServicio beanservicio = (BeanServicio) iter.next();
                if (beanservicio.getEstado().equalsIgnoreCase(Constantes.FLAG_ENABLED_SERVICIO)) {
                    lservicios_in.add(Long.parseLong(beanservicio.getM_IdServicio()));
                }
            }
        } else {
            lservicios_in.add(Long.parseLong(servicio));
        }

        if (lservicios_in.size() > 0) {
            TaOrdenDao ordenDao = new TaOrdenDaoHibernate();
            //consulta de ordenes pendientes de aprobacion
            if (beanDataLogXML.isM_usuarioEspecial()) {
                resultado = ordenDao.selectOrdenesPendAprobacion(lservicios_in, (String) lUser.get(0));
            } else {
                resultado = ordenDao.selectOrdenesPendAprobacion(lservicios_in, lUser);
            }

        }

        ((AprobacionOrdenForm) form).setValues();

        if (resultado != null && resultado.size() > 0) //Sucess
        {
            request.setAttribute("listaResult", resultado);
        } else {    //Error
            MessageResources messageResources = getResources(request);
            request.setAttribute("mensaje", messageResources.getMessage("global.listavacia2"));
        }
        return mapping.findForward("cargarAprobaciones");
    }

    public ActionForward detalleOrdenesPag(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        //obtenemos las llaves para obtener el detalle de la porden
        String idOrden = request.getParameter("m_IdOrden");
        String idServicio = request.getParameter("m_IdServicio");

        BeanPaginacion bpag = (BeanPaginacion) session.getAttribute("beanPag");
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
        session.setAttribute("beanPag", bpag);

        List resultado = null;

        TaServicioxEmpresaDao servempDao = new TaServicioxEmpresaDaoHibernate();
        TpDetalleOrdenDao detalleordenDao = new TpDetalleOrdenDaoHibernate();

        String[] ta = servempDao.selectTipoServicioxEmpresa(Long.parseLong(idServicio));

        if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equalsIgnoreCase(ta[1]) ||
                Constantes.TX_CASH_TIPO_SERVICIO_COBRO.equalsIgnoreCase(ta[1])) {
            resultado = detalleordenDao.selectDetallePago(idServicio, idOrden, bpag);
            if (resultado != null && resultado.size() > 0) {
                request.setAttribute("listaDetalle", resultado);
            }
        } else if (Constantes.TX_CASH_TIPO_SERVICIO_TRANSFERENCIAS.equalsIgnoreCase(ta[1])) {
            TaServicioOpcionDao servicio_opcionDAO = new TaServicioOpcionDaoHibernate();
            List alservicioopcion = servicio_opcionDAO.select(Constantes.HQL_MODULO_TRANSFERENCIA);
            for (Iterator it = alservicioopcion.iterator(); it.hasNext();) {
                TaServicioOpcion tso = (TaServicioOpcion) it.next();
                if (tso.getId().getCsoservicioId().equalsIgnoreCase(ta[0])) {
                    if (tso.getId().getCsoproceso().equalsIgnoreCase(Constantes.HQL_PROCESO_TRANSFERENCIA_CP)) {
                        resultado = detalleordenDao.selectDetalleTransferencia(idServicio, idOrden, bpag);
                        if (resultado != null && resultado.size() > 0) {
                            request.setAttribute("listaDetalleTransf_P", resultado);
                        }
                    } else if (tso.getId().getCsoproceso().equalsIgnoreCase(Constantes.HQL_PROCESO_TRANSFERENCIA_CT)) {
                        resultado = detalleordenDao.selectDetalleTransferencia(idServicio, idOrden, bpag);
                        if (resultado != null && resultado.size() > 0) {
                            request.setAttribute("listaDetalleTransf_T", resultado);
                        }
                    } else if (tso.getId().getCsoproceso().equalsIgnoreCase(Constantes.HQL_PROCESO_TRANSFERENCIA_I)) {
                        resultado = detalleordenDao.selectDetalleTransferencia(idServicio, idOrden, bpag);
                        if (resultado != null && resultado.size() > 0) {
                            request.setAttribute("listaDetalleTransf_I", resultado);
                        }
                    }
                    break;
                }
            }
        } else if (Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV.equalsIgnoreCase(ta[1])) {
            resultado = detalleordenDao.selectDetallePago(idServicio, idOrden, bpag);
            if (resultado != null && resultado.size() > 0) {
                request.setAttribute("listaDetallePagoServicio", resultado);
            }
        } //para letras
        else if (Constantes.TX_CASH_TIPO_SERVICIO_LETRAS.equalsIgnoreCase(ta[1])) {
            resultado = detalleordenDao.selectDetallePago(idServicio, idOrden, bpag);
            if (resultado != null && resultado.size() > 0) {
                request.setAttribute("listaDetallePagoLetras", resultado);
            }
        }

        if (resultado == null && resultado.size() == 0) {
            MessageResources messageResources = getResources(request);
            request.setAttribute("mensaje", messageResources.getMessage("global.listavacia2"));
        }
        request.setAttribute("back", "aprobaciones.do?do=buscarOrdenesLinea");
        return mapping.findForward("cargarDetalleOrdenes");
    }

    public ActionForward detalleOrdenes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        //obtenemos las llaves para obtener el detalle de la orden
        String idOrden = request.getParameter("m_IdOrden");
        String idServicio = request.getParameter("m_IdServicio");

        MessageResources messageResources = getResources(request);
        int nroRegPag = Integer.parseInt(messageResources.getMessage("paginacion.aprobaciones"));
        long cantItems = Long.parseLong(request.getParameter("m_Items"));
        int nroPag = (int) cantItems / nroRegPag;
        int resto = (int) cantItems % nroRegPag;
        if (resto != 0) {
            nroPag = nroPag + 1;
        }
        BeanPaginacion bpag = new BeanPaginacion();

        bpag.setM_pagActual(1);
        bpag.setM_pagFinal(nroPag);
        bpag.setM_pagInicial(1);
        bpag.setM_regPagina(nroRegPag);
        bpag.setM_tipo(Constantes.TIPO_PAG_APROBACIONES);
        session.setAttribute("beanPag", bpag);

        //se lo enviamos a la pagina del detalle para la impresion
        session.setAttribute("midOrden", idOrden);
        session.setAttribute("midServicio", idServicio);

        List resultado = null;

        TaServicioxEmpresaDao servempDao = new TaServicioxEmpresaDaoHibernate();
        TpDetalleOrdenDao detalleordenDao = new TpDetalleOrdenDaoHibernate();

        String[] ta = servempDao.selectTipoServicioxEmpresa(Long.parseLong(idServicio));

        if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equalsIgnoreCase(ta[1]) ||
                Constantes.TX_CASH_TIPO_SERVICIO_COBRO.equalsIgnoreCase(ta[1])) {
            resultado = detalleordenDao.selectDetallePago(idServicio, idOrden, bpag);
            if (resultado != null && resultado.size() > 0) {
                request.setAttribute("listaDetalle", resultado);
            }
        } else if (Constantes.TX_CASH_TIPO_SERVICIO_TRANSFERENCIAS.equalsIgnoreCase(ta[1])) {
            TaServicioOpcionDao servicio_opcionDAO = new TaServicioOpcionDaoHibernate();
            List alservicioopcion = servicio_opcionDAO.select(Constantes.HQL_MODULO_TRANSFERENCIA);
            for (Iterator it = alservicioopcion.iterator(); it.hasNext();) {
                TaServicioOpcion tso = (TaServicioOpcion) it.next();
                if (tso.getId().getCsoservicioId().equalsIgnoreCase(ta[0])) {
                    if (tso.getId().getCsoproceso().equalsIgnoreCase(Constantes.HQL_PROCESO_TRANSFERENCIA_CP)) {
                        resultado = detalleordenDao.selectDetalleTransferencia(idServicio, idOrden, bpag);
                        if (resultado != null && resultado.size() > 0) {
                            request.setAttribute("listaDetalleTransf_P", resultado);
                        }
                    } else if (tso.getId().getCsoproceso().equalsIgnoreCase(Constantes.HQL_PROCESO_TRANSFERENCIA_CT)) {
                        resultado = detalleordenDao.selectDetalleTransferencia(idServicio, idOrden, bpag);
                        if (resultado != null && resultado.size() > 0) {
                            request.setAttribute("listaDetalleTransf_T", resultado);
                        }
                    } else if (tso.getId().getCsoproceso().equalsIgnoreCase(Constantes.HQL_PROCESO_TRANSFERENCIA_I)) {
                        resultado = detalleordenDao.selectDetalleTransferencia(idServicio, idOrden, bpag);
                        if (resultado != null && resultado.size() > 0) {
                            request.setAttribute("listaDetalleTransf_I", resultado);
                        }
                    }
                    break;
                }
            }
        } else if (Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV.equalsIgnoreCase(ta[1])) {
            resultado = detalleordenDao.selectDetallePago(idServicio, idOrden, bpag);
            if (resultado != null && resultado.size() > 0) {
                request.setAttribute("listaDetallePagoServicio", resultado);
            }
        } //para letras
        else if (Constantes.TX_CASH_TIPO_SERVICIO_LETRAS.equalsIgnoreCase(ta[1])) {
            resultado = detalleordenDao.selectDetallePago(idServicio, idOrden, bpag);
            if (resultado != null && resultado.size() > 0) {
                request.setAttribute("listaDetallePagoLetras", resultado);
            }
        }

        if (resultado == null && resultado.size() == 0) {
            request.setAttribute("mensaje", messageResources.getMessage("global.listavacia2"));
        }
        request.setAttribute("back", "aprobaciones.do?do=buscarOrdenesLinea");
        return mapping.findForward("cargarDetalleOrdenes");
    }

    public ActionForward aprobarOrdenes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
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

        Map map_ordenes = ((AprobacionOrdenForm) form).getValues();

        TaAprobacionOrdenDao aprobDao = new TaAprobacionOrdenDaoHibernate();

        Set keys = map_ordenes.keySet();

        List ordenAprob = new ArrayList();
        List ordenNoAprob = new ArrayList();
//        boolean band = false;
        for (Iterator i = keys.iterator(); i.hasNext();) {
            String key = (String) i.next();
            String tmp[] = key.split(";");
            long korden = Long.parseLong(tmp[0]);
            long kservicio = Long.parseLong(tmp[1]);
            long kaprobador = Long.parseLong(tmp[2]);

            StringBuilder msj = new StringBuilder();
            boolean bSucess = aprobDao.insert(korden, kservicio, kaprobador, tmp[3], msj);
//            if ("true".equals(msj.toString())) {
//                band = true;
//            }
            if (bSucess) {
                ordenAprob.add(new Object[]{korden, kservicio});
            } else {
                ordenNoAprob.add(new Object[]{korden, kservicio});
            }
        }

        //Mensaje Confirmación
        MessageResources messageResources = getResources(request);
        List alsuccess = new ArrayList();
        BeanSuccessDetail sucessdetail;

        sucessdetail = new BeanSuccessDetail();

        sucessdetail.setM_Label(messageResources.getMessage("pagos.confirmacion.operacion"));
        sucessdetail.setM_Mensaje(messageResources.getMessage("aprobaciones.title.aprobar").toUpperCase());
        alsuccess.add(sucessdetail);
        if (ordenAprob.size() > 0) {
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label(messageResources.getMessage("aprobaciones.confirmacion.orden.aprob"));
            String sAprob = "";
            for (int i = 0; i < ordenAprob.size(); i++) {
                Object[] o = (Object[]) ordenAprob.get(i);
                sAprob = sAprob + "   " + o[0] + "   " + "\n";
            }
            sucessdetail.setM_MensajeArea(sAprob);
            alsuccess.add(sucessdetail);
        }
        if (ordenNoAprob.size() > 0) {
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label(messageResources.getMessage("aprobaciones.confirmacion.orden.desaprob"));

            String sNoAprob = "";
            for (int i = 0; i < ordenNoAprob.size(); i++) {
                Object[] o = (Object[]) ordenNoAprob.get(i);
                sNoAprob = sNoAprob + "   " + o[0] + "   " + "\n";
            }
            sucessdetail.setM_MensajeArea(sNoAprob);
            alsuccess.add(sucessdetail);
        }
        sucessdetail = new BeanSuccessDetail();
        sucessdetail.setM_Label(messageResources.getMessage("aprobaciones.confirmacion.date"));
        sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
        alsuccess.add(sucessdetail);

        BeanSuccess success = new BeanSuccess();

        success.setM_Titulo(messageResources.getMessage("aprobaciones.title.aprobar"));
        if (ordenAprob.size() > 0) {
            if (ordenNoAprob.size() > 0) {
                success.setM_Mensaje("Mensaje: No se aprobaron algunas órdenes. El número de aprobadores para el servicio debe ser mayor a 0");
            } else {
                success.setM_Mensaje(messageResources.getMessage("aprobaciones.confirmacion.title"));
            }
        } else {
            success.setM_Mensaje("No se aprobaron las órdenes. El número de aprobadores para el servicio debe ser mayor a 0");
        //success.setM_Mensaje(messageResources.getMessage("aprobaciones.confirmacion.error"));
        }

        success.setM_Back("aprobaciones.do?do=buscarOrdenesLinea");
        request.setAttribute("success", success);
        request.setAttribute("alsuccess", alsuccess);
        return mapping.findForward("cargarSuccess");
    }
}
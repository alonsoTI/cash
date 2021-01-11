package com.hiper.cash.actions;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.financiero.cash.delegate.OrdenDelegate;
import com.financiero.cash.delegate.SeguridadDelegate;
import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.dao.TaCuentasServicioEmpresaDao;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TaSecuencialDao;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.TxListFieldDao;
import com.hiper.cash.dao.hibernate.TaCuentasServicioEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaSecuencialDaoHibernate;
import com.hiper.cash.dao.hibernate.TpDetalleOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TxListFieldDaoHibernate;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.domain.TpDetalleOrden;
import com.hiper.cash.entidad.BeanCuenta;
import com.hiper.cash.entidad.BeanDetalleOrden;
import com.hiper.cash.entidad.BeanOrden;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.BeanSuccess;
import com.hiper.cash.entidad.BeanSuccessDetail;
import com.hiper.cash.forms.OrdenForm;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;

/**
 * 
 * @author esilva revisado por andy 11/07/2011
 */
public class OrdenAction extends DispatchAction {

	private Logger logger = Logger.getLogger(this.getClass());
	private SeguridadDelegate delegadoSeguridad = SeguridadDelegate
			.getInstance();

	public ActionForward cargarPag(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		((OrdenForm) form).reset(mapping, request);
		((OrdenForm) form).setValues();
		((OrdenForm) form).setDocumentos();
		((OrdenForm) form).setNombres();
		((OrdenForm) form).setEmails();
		((OrdenForm) form).setCuentas();
		((OrdenForm) form).setTipoCuentas();
		((OrdenForm) form).setTelefs();

		String idorden = (request.getParameter("m_IdOrden") != null) ? request
				.getParameter("m_IdOrden") : "";
		String idservemp = request.getParameter("m_IdServicio");
		String codServicio = request.getParameter("m_CodServicio");

		request.setAttribute("m_IdOrden", idorden);
		request.setAttribute("m_IdServicio", idservemp);
		request.setAttribute("m_CodServicio", codServicio);

		TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();
		TpDetalleOrdenDao detalleordenDAO = new TpDetalleOrdenDaoHibernate();
		TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();

		BeanOrden beantaorden = ordenDAO.select(idorden, idservemp);
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

		List lestados = new ArrayList();
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);
		List listaorden = detalleordenDAO.select(idservemp, idorden, lestados,
				bpag);

		List listaTipoCuenta = listFieldDAO.selectListFieldByFieldName3(
				Constantes.FIELD_CASH_TIPO_CUENTA, codServicio);

		request.setAttribute("listaTipoCuenta", listaTipoCuenta);
		request.setAttribute("beanOrden", beantaorden);
		request.setAttribute("listaorden", listaorden);
		if (listaorden != null) {
			request.setAttribute("m_NroReg", listaorden.size());
		} else {
			request.setAttribute("m_NroReg", 0);
		}

		// jwong 04/06/2009 adicionalmente se tendra que enviar el codigo de los
		// servicios
		// pago haberes
		request.setAttribute("Serv_Pag_Haberes",
				Constantes.TX_CASH_SERVICIO_PAGO_HABERES);
		// pago cts
		request.setAttribute("Serv_Pag_CTS",
				Constantes.TX_CASH_SERVICIO_PAGO_CTS);
		// pago proveedores
		request.setAttribute("Serv_Pag_Proveedores",
				Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES);

		// jwong validar si el servicio es pago de proveedores
		if (Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES
				.equalsIgnoreCase(codServicio)) {
			request.setAttribute("isPagProveedor", "true");
		} else if (Constantes.TX_CASH_SERVICIO_PAGO_CTS
				.equalsIgnoreCase(codServicio)
				|| Constantes.TX_CASH_SERVICIO_PAGO_HABERES
						.equalsIgnoreCase(codServicio)) {
			request.setAttribute("isPagPersonal", "true");
		}

		String longitud = "11";
		if (Constantes.TX_CASH_SERVICIO_PAGO_CTS.equals(codServicio)) {
			longitud = "8";
		} else if (Constantes.TX_CASH_SERVICIO_PAGO_HABERES.equals(codServicio)) {
			longitud = "8";
		} else { // se incluye el pago a proveedores y ordenes de pago
			longitud = "11";
		}
		if (Constantes.TX_CASH_SERVICIO_DEBITO_AUTOMATICO.equals(codServicio)
				|| Constantes.TX_CASH_SERVICIO_RECAUDACION_CON_BD
						.equals(codServicio)
				|| Constantes.TX_CASH_SERVICIO_ORDENES_COBRO
						.equals(codServicio)) {
			request.setAttribute("isOrdCobro", "true");
		}
		// enviamos la longitud que tendra el documento
		request.setAttribute("longitudDoc", longitud);

		request.setAttribute("onkeypress",
				"return soloDescripcion(this, event)");
		request.setAttribute("onblur", "gDescripcion(this)");

		return mapping.findForward("modificar");
	}

	public ActionForward cargar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		((OrdenForm) form).reset(mapping, request);
		((OrdenForm) form).setValues();
		((OrdenForm) form).setDocumentos();
		((OrdenForm) form).setNombres();
		((OrdenForm) form).setEmails();
		((OrdenForm) form).setCuentas();
		((OrdenForm) form).setTipoCuentas();
		((OrdenForm) form).setTelefs();

		String idorden = (request.getParameter("m_IdOrden") != null) ? request
				.getParameter("m_IdOrden") : "";
		String idservemp = request.getParameter("m_IdServicio");
		String codServicio = request.getParameter("m_CodServicio");
		request.setAttribute("m_IdOrden", idorden);
		request.setAttribute("m_IdServicio", idservemp);
		request.setAttribute("m_CodServicio", codServicio);

		TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();
		TpDetalleOrdenDao detalleordenDAO = new TpDetalleOrdenDaoHibernate();
		TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();

		BeanOrden beantaorden = ordenDAO.select(idorden, idservemp);

		MessageResources messageResources = getResources(request);
		int nroRegPag = Integer.parseInt(messageResources
				.getMessage("paginacion.ordenes"));
		long cantItems = Long.parseLong(beantaorden.getM_Items());
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
		session.setAttribute("beanPag", bpag);

		List lestados = new ArrayList();
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);
		List listaorden = detalleordenDAO.select(idservemp, idorden, lestados,
				bpag);

		// jwong 11/05/2009 comentado
		// List listaTipoCuenta =
		// listFieldDAO.selectListFieldByFieldName(Constantes.FIELD_CASH_TIPO_CUENTA);
		List listaTipoCuenta = listFieldDAO.selectListFieldByFieldName3(
				Constantes.FIELD_CASH_TIPO_CUENTA, codServicio);

		// List listaTipoMoneda =
		// listFieldDAO.selectListFieldByFieldName(Constantes.FIELD_CASH_TIPO_MONEDA);

		request.setAttribute("listaTipoCuenta", listaTipoCuenta);
		// request.setAttribute("listaTipoMoneda", listaTipoMoneda);
		request.setAttribute("beanOrden", beantaorden);
		request.setAttribute("listaorden", listaorden);
		if (listaorden != null) {
			request.setAttribute("m_NroReg", listaorden.size());
		} else {
			request.setAttribute("m_NroReg", 0);
		}

		// jwong 04/06/2009 adicionalmente se tendra que enviar el codigo de los
		// servicios
		// pago haberes
		request.setAttribute("Serv_Pag_Haberes",
				Constantes.TX_CASH_SERVICIO_PAGO_HABERES);
		// pago cts
		request.setAttribute("Serv_Pag_CTS",
				Constantes.TX_CASH_SERVICIO_PAGO_CTS);
		// pago proveedores
		request.setAttribute("Serv_Pag_Proveedores",
				Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES);

		request.setAttribute("onkeypress",
				"return soloDescripcion(this, event)");
		request.setAttribute("onblur", "gDescripcion(this)");
		// jwong validar si el servicio es pago de proveedores
		if (Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES
				.equalsIgnoreCase(codServicio)) {
			request.setAttribute("isPagProveedor", "true");
		} else if (Constantes.TX_CASH_SERVICIO_PAGO_CTS
				.equalsIgnoreCase(codServicio)
				|| Constantes.TX_CASH_SERVICIO_PAGO_HABERES
						.equalsIgnoreCase(codServicio)) {
			request.setAttribute("isPagPersonal", "true");
			request.setAttribute("onkeypress",
					"return soloDescripcionNombre(this, event)");
			request.setAttribute("onblur", "gDescripcionNombre(this)");
		}

		String longitud = "11";
		if (Constantes.TX_CASH_SERVICIO_PAGO_CTS.equals(codServicio)) {
			longitud = "8";
		} else if (Constantes.TX_CASH_SERVICIO_PAGO_HABERES.equals(codServicio)) {
			longitud = "8";
		} else { // se incluye el pago a proveedores y ordenes de pago
			longitud = "11";
		}
		if (Constantes.TX_CASH_SERVICIO_DEBITO_AUTOMATICO.equals(codServicio)
				|| Constantes.TX_CASH_SERVICIO_RECAUDACION_CON_BD
						.equals(codServicio)
				|| Constantes.TX_CASH_SERVICIO_ORDENES_COBRO
						.equals(codServicio)) {
			request.setAttribute("isOrdCobro", "true");
		}

		// enviamos la longitud que tendra el documento
		request.setAttribute("longitudDoc", longitud);

		return mapping.findForward("modificar");
	}

	public ActionForward cargarCancelarPag(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
	
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		((OrdenForm) form).setValues();
		((OrdenForm) form).setDocumentos();

		String idorden = (request.getParameter("m_IdOrden") != null) ? request
				.getParameter("m_IdOrden") : "";
		String idempresa = request.getParameter("m_IdEmpresa");
		String idservicio = request.getParameter("m_IdServicio");
		request.setAttribute("m_IdOrden", idorden);
		request.setAttribute("m_IdServicio", idservicio);

		TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();
		TpDetalleOrdenDao detalleordenDAO = new TpDetalleOrdenDaoHibernate();

		BeanOrden beanOrden = ordenDAO.select(idorden, idservicio);

		BeanPaginacion bpag = (BeanPaginacion) session.getAttribute("beanPag");
		String tipoPaginado = request.getParameter("tipoPaginado");
		tipoPaginado = tipoPaginado == null ? "P" : tipoPaginado;
		if ("P".equals(tipoPaginado)) {
			bpag.setM_pagActual(1);
			bpag.setM_tipo(Constantes.TIPO_PAG_PRIMERO);
		} else if ("U".equals(tipoPaginado)) {
			bpag.setM_pagActual(bpag.getM_pagFinal());
			bpag.setM_tipo(Constantes.TIPO_PAG_ULTIMO);
		} else if ("S".equals(tipoPaginado)) {
			if (bpag.getM_pagActual() < bpag.getM_pagFinal()) {
				bpag.setM_pagActual(bpag.getM_pagActual() + 1);
				bpag.setM_tipo(Constantes.TIPO_PAG_SIGUIENTE);
			} else {
				bpag.setM_tipo(Constantes.TIPO_PAG_ULTIMO);
			}

		} else if ("A".equals(tipoPaginado)) {
			if (bpag.getM_pagActual() > bpag.getM_pagInicial()) {
				bpag.setM_pagActual(bpag.getM_pagActual() - 1);
				bpag.setM_tipo(Constantes.TIPO_PAG_ANTERIOR);
			} else {
				bpag.setM_tipo(Constantes.TIPO_PAG_PRIMERO);
			}
		}

		List lestados = new ArrayList();
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_ERRADO);
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_EXTORNADO);
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_CANCELADO);
		List listaorden = null;
		try {
			listaorden = detalleordenDAO.selectOrdCan(idservicio, idorden,
					lestados, bpag);
			logger.info("LISTA PAG: " + listaorden.size() + " - "
					+ listaorden.toString());
		
		} catch (Exception e) {
			logger.info("ERROR: " + e);
		}
		if (listaorden != null) {
			if (Constantes.TIPO_PAG_ANTERIOR.equals(bpag.getM_tipo())
					|| Constantes.TIPO_PAG_ULTIMO.equals(bpag.getM_tipo())) {
				List listaordenOrd = new ArrayList();
				for (int i = listaorden.size() - 1; i >= 0; i--) {
					listaordenOrd.add(listaorden.get(i));
				}
				listaorden = listaordenOrd;
			}
		}

		if (listaorden != null) {
			bpag.setM_primerRegAct(Integer
					.parseInt(((BeanDetalleOrden) listaorden.get(0))
							.getM_IdDetalleOrden()));
			bpag.setM_ultimoRegAct(Integer
					.parseInt(((BeanDetalleOrden) listaorden.get(listaorden
							.size() - 1)).getM_IdDetalleOrden()));
		}
		session.setAttribute("beanPag", bpag);

		// BeanOrden
		BeanOrden beanorden = new BeanOrden();
		beanorden.setM_IdEmpresa(idempresa);
		beanorden.setM_IdServicio(idservicio);
		beanorden.setM_IdOrden(idorden);

		request.setAttribute("beanOrden", beanOrden);
		request.setAttribute("listaorden", listaorden);

		return mapping.findForward("cancelar");

	}

	public ActionForward cargarCancelar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		String tipoProceso = (session.getAttribute("mo_tipo_proceso") != null ? (String) session
				.getAttribute("mo_tipo_proceso")
				: "");

		((OrdenForm) form).setValues();
		((OrdenForm) form).setDocumentos();

		String idorden = (request.getParameter("m_IdOrden") != null) ? request
				.getParameter("m_IdOrden") : "";
		String idempresa = request.getParameter("m_IdEmpresa");
		String idservicio = request.getParameter("m_IdServicio");
		request.setAttribute("m_IdOrden", idorden);
		request.setAttribute("m_IdServicio", idservicio);

		TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();
		TpDetalleOrdenDao detalleordenDAO = new TpDetalleOrdenDaoHibernate();

		String tipo = ordenDAO.obtenerTipoOrden(idorden, idservicio);
	
		session.setAttribute("tipo_servicio", tipo);

		List lestados = new ArrayList();
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_ERRADO);
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_EXTORNADO);
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_CANCELADO);

		BeanOrden beanOrden = ordenDAO.select(idorden, idservicio);

		// Paginacion
		MessageResources messageResources = getResources(request);

		int nroRegPag = Integer.parseInt(messageResources
				.getMessage("paginacion.ordenes"));

		long cantItems = detalleordenDAO.obtenerCantItems(idorden, lestados); // Long.parseLong(beanOrden.getM_Items());

		/*
		 * Modificado por Grov 02/05/2010 int nroPag = (int)cantItems/nroRegPag;
		 * int resto = (int)cantItems%nroRegPag; if(resto != 0){ nroPag = nroPag
		 * + 1; } BeanPaginacion bpag = new BeanPaginacion();
		 * bpag.setM_pagActual(1); bpag.setM_pagFinal(nroPag);
		 * bpag.setM_pagInicial(1); bpag.setM_regPagina(nroRegPag);
		 * bpag.setM_resto(resto); bpag.setM_tipo(Constantes.TIPO_PAG_PRIMERO);
		 * fin Modificado por Grov 02/05/2010
		 */

		BeanPaginacion bpag = null;
		List listaorden = null;
		try {
			bpag = paginacion(nroRegPag, cantItems);
			listaorden = detalleordenDAO.selectOrdCan(idservicio, idorden,
					lestados, bpag);
			
		} catch (Exception e) {
			logger.error("ERROR PAGINANDO: ", e);
		}
		if (listaorden != null) {
			if (listaorden.size() > 0) {

				bpag.setM_primerRegAct(Integer
						.parseInt(((BeanDetalleOrden) listaorden.get(0))
								.getM_IdDetalleOrden()));
				bpag.setM_ultimoRegAct(Integer
						.parseInt(((BeanDetalleOrden) listaorden.get(listaorden
								.size() - 1)).getM_IdDetalleOrden()));
			}
		}
		session.setAttribute("beanPag", bpag);
		// Modificado por Grov 02/06/2010
		session.setAttribute("cantItems", cantItems);
		// Fin Modificado por Grov 02/06/2010

		// BeanOrden
		BeanOrden beanorden = new BeanOrden();
		beanorden.setM_IdEmpresa(idempresa);
		beanorden.setM_IdServicio(idservicio);
		beanorden.setM_IdOrden(idorden);

		request.setAttribute("beanOrden", beanOrden);
		request.setAttribute("listaorden", listaorden);

		return mapping.findForward("cancelar");

	}

	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		String idorden = ((OrdenForm) form).getM_IdOrden();
		String idempresa = ((OrdenForm) form).getM_IdEmpresa();
		String idservicio = ((OrdenForm) form).getM_IdServicio();
		// jwong no se modifica el
		// Map map_montos = ((OrdenForm)form).getDocumentos();
		Map map_estados = ((OrdenForm) form).getValues();
		Map map_cuentas = ((OrdenForm) form).getCuentas();
		Map map_nombres = ((OrdenForm) form).getNombres();
		Map map_telefs = ((OrdenForm) form).getTelefs();
		Map map_emails = ((OrdenForm) form).getEmails();
		Map map_tipcuentas = ((OrdenForm) form).getTipoCuentas();// esilva
		// 13/09/2009

		Map map_nrodocumentos = ((OrdenForm) form).getDocumentos();//

		TpDetalleOrdenDao detalleordenDAO = new TpDetalleOrdenDaoHibernate();
		// TaOrdenDao ordenDao = new TaOrdenDaoHibernate();
		BeanPaginacion bpag = (BeanPaginacion) session.getAttribute("beanPag");
		boolean bupdate = detalleordenDAO
				.update(idempresa, idservicio, idorden, new HashMap(),
						map_estados, map_cuentas, map_nombres, map_telefs,
						map_emails, map_tipcuentas, map_nrodocumentos, bpag);
		// Recalcular Montos
		// List estado = new ArrayList();
		// estado.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);
		// ordenDao.updateAmount(Long.parseLong(idorden),Long.parseLong(idservicio),estado);
		session.removeAttribute("beanPag");
		if (bupdate) {
			// Mensaje Confirmación
			MessageResources messageResources = getResources(request);
			List alsuccess = new ArrayList();
			BeanSuccessDetail sucessdetail;

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.operacion"));
			sucessdetail.setM_Mensaje("MODIFICACIÓN DE ORDEN");
			alsuccess.add(sucessdetail);
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.codigo"));
			sucessdetail.setM_Mensaje(idorden);
			alsuccess.add(sucessdetail);
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.date"));
			sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   "
					+ Fecha.getFechaActual("HH:mm:ss"));
			alsuccess.add(sucessdetail);

			// String codorden = (String) request.getAttribute("codorden");
			// int cont =Integer.parseInt(codorden);

			// ActionMessages messages = new ActionMessages();
			// ActionMessage am = new
			// ActionMessage("aprobaciones.confirmacion.descripcion",
			// String.valueOf(cont));
			// messages.add("message1", am);
			// saveMessages(request, messages);

			BeanSuccess success = new BeanSuccess();
			success.setM_Titulo(messageResources
					.getMessage("pagos.title.modificar"));
			success.setM_Mensaje(messageResources
					.getMessage("pagos.confirmacion.title"));
			success
					.setM_Back("mantenerOrdenes.do?do=cargarModificar\u0026accion=1");
			// success.setM_BackAccion("accion=1");
			request.setAttribute("success", success);
			request.setAttribute("alsuccess", alsuccess);
			return mapping.findForward("success");
		} else {
			return mapping.findForward("error");
		}
	}

	public ActionForward guardar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			String id = request.getParameter("modulo");
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(id)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}

		// Session
		TaOrden taorden = (TaOrden) session.getAttribute("mo_objorden");
		List listaentidad = (List) session.getAttribute("mo_listaentidad");
		// Form
		Map map_montos = ((OrdenForm) form).getDocumentos();
		Map map_estados = ((OrdenForm) form).getValues();
		Map map_cuentas = ((OrdenForm) form).getCuentas();
		Map map_nombres = ((OrdenForm) form).getNombres();
		Map map_telefs = ((OrdenForm) form).getTelefs();
		Map map_emails = ((OrdenForm) form).getEmails();

		// Hibernate
		TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();
		TpDetalleOrdenDao detalleordenDAO = new TpDetalleOrdenDaoHibernate();
		TaCuentasServicioEmpresaDao cuentasDAO = new TaCuentasServicioEmpresaDaoHibernate();
		TaSecuencialDao taSecdao = new TaSecuencialDaoHibernate();

		BeanCuenta beancuenta = cuentasDAO.select(taorden.getNorNumeroCuenta(),
				taorden.getId().getCorIdServicioEmpresa());
		if (beancuenta != null) {
			taorden.setDorTipoCuenta(beancuenta.getM_TipoCuenta());
		}

		// Set
		taorden.setForFechaRegistro(Fecha.getFechaActual("yyyyMMdd"));
		// Valida si el servicio tiene aprobacion automatica
		if (String.valueOf(Constantes.HQL_CASH_FLAG_APROB_AUT_ENABLED)
				.equalsIgnoreCase(String.valueOf(taorden.getM_FlagAprobAut())))
			taorden.setCorEstado(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
		else
			taorden.setCorEstado(Constantes.HQL_CASH_ESTADO_ORDEN_INGRESADO);
		taorden.setForFechaInicio(Fecha.convertToFechaSQL(taorden
				.getForFechaInicio()));
		taorden.setForFechaFin(Fecha
				.convertToFechaSQL(taorden.getForFechaFin()));
		taorden.setNorNumeroRegistros(0);

		int idEnvio = taSecdao
				.getIdEnvio(Constantes.FIELD_CASH_SECUENCIAL_ID_ORDEN);
		taorden.getId().setCorIdOrden(idEnvio);
		// jmoreno 09/08/09
		taorden.setCorEstadoMontoDolares('0');
		taorden.setCorEstadoMontoSoles('0');
		taorden.setCorEstadoMontoEuros('0');
		boolean bguardar = ordenDAO.insert(taorden);

		if (bguardar) {
			List listadetorden = new ArrayList();

			Map hmDetOrden = new HashMap();
			TpDetalleOrden tpdetalleorden;
			for (Iterator j = listaentidad.iterator(); j.hasNext();) {
				tpdetalleorden = (TpDetalleOrden) j.next();
				String key = tpdetalleorden.getIdEntidad();// jmoreno
				// 22-07-09(para
				// evitar el prob.
				// que el Nro de
				// doc. se repita)
				hmDetOrden.put(key, tpdetalleorden);
				tpdetalleorden = null;
			}
			long index = 0;
			for (Iterator i = map_estados.entrySet().iterator(); i.hasNext();) {
				String en = (String) ((Map.Entry) i.next()).getKey();

				tpdetalleorden = (TpDetalleOrden) hmDetOrden.get(en);

				tpdetalleorden.getId().setCdoidOrden(
						taorden.getId().getCorIdOrden()); // IdOrden
				tpdetalleorden.getId().setCdoidServicioEmpresa(
						taorden.getId().getCorIdServicioEmpresa()); // IdServicioEmpresa
				tpdetalleorden
						.setCdoestado(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);
				tpdetalleorden.getId().setCdoidDetalleOrden(index + 1); //
				tpdetalleorden.setNdomonto(new BigDecimal((String) map_montos
						.get(en)));
				if (map_nombres.get(en) != null)
					tpdetalleorden.setDdonombre((String) map_nombres.get(en));
				if (map_cuentas.get(en) != null)
					tpdetalleorden.setNdonumeroCuenta((String) map_cuentas
							.get(en));
				if (map_telefs.get(en) != null)
					tpdetalleorden.setDdotelefono((String) map_telefs.get(en));
				if (map_emails.get(en) != null)
					tpdetalleorden.setDdoemail((String) map_emails.get(en));

				// jwong 04/06/2009
				tpdetalleorden.setDdoreferencia((String) tpdetalleorden
						.getDdonombre());
				// jmoreno 18-01-10
				tpdetalleorden.setDdocontrapartida((String) tpdetalleorden
						.getDdocontrapartida());
				listadetorden.add(tpdetalleorden);
				index++;
			}
			Map montos = new HashMap();
			bguardar = detalleordenDAO.insert(listadetorden, montos);
			if (bguardar) {
				ordenDAO.update(taorden.getId().getCorIdOrden(), taorden
						.getId().getCorIdServicioEmpresa(), (BigDecimal) montos
						.get("PEN"), (BigDecimal) montos.get("USD"),
						(BigDecimal) montos.get("EUR"), listadetorden.size());

				request.setAttribute("codorden", String.valueOf(taorden.getId()
						.getCorIdOrden()));
				return mapping.findForward("guardar");
			} else {
				request.setAttribute("mensaje",
						"Error. El servicio web del IBS no devuelve resultado");
				return mapping.findForward("error");
			}
		} else {
			request.setAttribute("mensaje",
					"Error. El servicio web del IBS no devuelve resultado");
			return mapping.findForward("error");
		}
	}

	public ActionForward sendmail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		/*
		 * if (session.getAttribute("usuarioActual") == null) {
		 * response.sendRedirect("inicio.jsp"); return null; }
		 */

		try {
			// Ini Logica para mandar email
			// ...
			// Fin Logica para mandar email

			String codorden = (String) request.getAttribute("codorden");
			int cont = Integer.parseInt(codorden);

			// Mensaje Confirmación
			MessageResources messageResources = getResources(request);
			List alsuccess = new ArrayList();
			BeanSuccessDetail sucessdetail;

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.operacion"));
			sucessdetail.setM_Mensaje("INGRESO DE ORDEN");
			alsuccess.add(sucessdetail);
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.codigo"));
			sucessdetail.setM_Mensaje(String.valueOf(cont));
			alsuccess.add(sucessdetail);
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.date"));
			sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   "
					+ Fecha.getFechaActual("HH:mm:ss"));
			alsuccess.add(sucessdetail);

			BeanSuccess success = new BeanSuccess();
			success.setM_Titulo(messageResources
					.getMessage("pagos.title.ingresar"));
			success.setM_Mensaje(messageResources
					.getMessage("pagos.confirmacion.title"));
			success.setM_Back("mantenerOrdenes.do?do=cargarIngresar");

			request.setAttribute("success", success);
			request.setAttribute("alsuccess", alsuccess);
			return mapping.findForward("success");
		} catch (Exception e) {
			return mapping.findForward("error");
		}
	}

	public ActionForward cancelar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		try {
			HttpSession session = request.getSession();

			ServletContext context = getServlet().getServletConfig()
					.getServletContext();

			// si termino la session debemos retornar al inicio
			if (session.getAttribute("usuarioActual") == null) {
				response.sendRedirect("cierraSession.jsp");
				return null;
			}

			String idorden = ((OrdenForm) form).getM_IdOrden();
			String idempresa = ((OrdenForm) form).getM_IdEmpresa();
			String idservicio = ((OrdenForm) form).getM_IdServicio();
			@SuppressWarnings("unused")
			Map map_estados = ((OrdenForm) form).getValues();

			List lestadosxCancelar = new ArrayList();
			lestadosxCancelar
					.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);
			lestadosxCancelar
					.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_ERRADO);
			lestadosxCancelar
					.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_EXTORNADO);

			TpDetalleOrdenDao detalleordenDAO = new TpDetalleOrdenDaoHibernate();

			TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();

			List lestados = new ArrayList();
			lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);
			lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_ERRADO);
			lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_EXTORNADO);

			boolean bupdate = false;
			OrdenDelegate ordenDelegate = new OrdenDelegate();
			boolean esFactibleEliminarOrden = false;
			try {
				esFactibleEliminarOrden = ordenDelegate
						.esFactibleEliminarOrden(idorden, idservicio);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (esFactibleEliminarOrden) {

				// aqui cambio para esta funcion valide la cancelacion
				if (ordenDAO.cancelarOrden(Integer.parseInt(idorden),idservicio)) {
					bupdate = true;
				}

				if (bupdate) {
					// Mensaje Confirmación
					request.setAttribute("factibleEliminarOrden", "SI");
					MessageResources messageResources = getResources(request);
					List alsuccess = new ArrayList();
					BeanSuccessDetail sucessdetail;

					sucessdetail = new BeanSuccessDetail();
					sucessdetail.setM_Label(messageResources
							.getMessage("pagos.confirmacion.operacion"));
					sucessdetail.setM_Mensaje("CANCELACIÓN DE ORDEN");
					alsuccess.add(sucessdetail);
					sucessdetail = new BeanSuccessDetail();
					sucessdetail.setM_Label(messageResources
							.getMessage("pagos.confirmacion.cancelacion"));
					sucessdetail.setM_Mensaje(idorden);
					alsuccess.add(sucessdetail);
					sucessdetail = new BeanSuccessDetail();
					sucessdetail.setM_Label(messageResources
							.getMessage("pagos.confirmacion.date"));
					sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy")
							+ "   " + Fecha.getFechaActual("HH:mm:ss"));
					alsuccess.add(sucessdetail);
					BeanSuccess success = new BeanSuccess();
					success.setM_Titulo(messageResources
							.getMessage("pagos.title.cancelar"));
					success.setM_Mensaje(messageResources
							.getMessage("pagos.confirmacion.title"));
					success
							.setM_Back("mantenerOrdenes.do?do=cargarCancelar\u0026accion=1");

					request.setAttribute("success", success);
					request.setAttribute("alsuccess", alsuccess);
					return mapping.findForward("success");
				} else {
					request.setAttribute("mensaje",
							"NO SE PUDO ACTUALIZAR LAS ORDENES");
					return mapping.findForward("error");
				}

			} else {
				request.setAttribute("factibleEliminarOrden", "NO");
				MessageResources messageResources = getResources(request);

				BeanSuccess success = new BeanSuccess();
				success.setM_Titulo(messageResources
						.getMessage("pagos.title.cancelar"));

				success.setM_Mensaje(messageResources
						.getMessage("pagos.ordenes.eliminar.no"));

				success
						.setM_Back("mantenerOrdenes.do?do=cargarCancelar\u0026accion=1");

				request.setAttribute("success", success);

				return mapping.findForward("success");
			}
		} catch (Exception e) {
			request.setAttribute("mensaje", e.toString());
			return mapping.findForward("error");
		}

	}

	public ActionForward eliminarItems(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		// ServletContext context =
		// getServlet().getServletConfig().getServletContext();

		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		Map map_ordenes = ((OrdenForm) form).getValues();

		Set keys = map_ordenes.keySet();

		List ordenAprob = new ArrayList();
		List ordenNoAprob = new ArrayList();
		// boolean band = false;
		System.out
				.println("--------------Eliminando items Detalle Orden--------------");

		List listIdOrd = new ArrayList();
		List listIdDtlOrd = new ArrayList();
		List listServEmp = new ArrayList();
		for (Iterator i = keys.iterator(); i.hasNext();) {
			String key = (String) i.next();
			String tmp[] = key.split(";");
			long kDetalleOrden = Long.parseLong(tmp[0]);
			long kIdOrden = Long.parseLong(tmp[1]);
			long kIdServicio = Long.parseLong(tmp[2]);

			StringBuilder msj = new StringBuilder();

			listIdDtlOrd.add(kDetalleOrden);
			listIdOrd.add(kIdOrden);
			listServEmp.add(kIdServicio);
		}

		TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();
		TpDetalleOrdenDao detalleordenDAO = new TpDetalleOrdenDaoHibernate();

		boolean bupdate = detalleordenDAO.change_state_items(listIdDtlOrd,
				listIdOrd, listServEmp,
				Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_CANCELADO);
		// System.out.println("Updated state_details success!!!");
		
		
		//cancelos los items seleccionados
		detalleordenDAO.actualizarItemsConsultas(listIdDtlOrd,
				listIdOrd, listServEmp,
				Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_CANCELADO);
		
		

		((OrdenForm) form).setValues();
		((OrdenForm) form).setDocumentos();

		String idorden = (request.getParameter("m_IdOrden") != null) ? request
				.getParameter("m_IdOrden") : "";
		String idempresa = request.getParameter("m_IdEmpresa");
		String idservicio = request.getParameter("m_IdServicio");
		request.setAttribute("m_IdOrden", idorden);
		request.setAttribute("m_IdServicio", idservicio);

		List lestados = new ArrayList();
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_ERRADO);
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_EXTORNADO);
		lestados.add(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_CANCELADO);

		BeanOrden beanOrden = ordenDAO.select(idorden, idservicio);
		// Paginacion
		MessageResources messageResources = getResources(request);

		int nroRegPag = Integer.parseInt(messageResources
				.getMessage("paginacion.ordenes"));

		long cantItems = detalleordenDAO.obtenerCantItems(idorden, lestados); // Long.parseLong(beanOrden.getM_Items());

		BeanPaginacion bpag = null;
		List listaorden = null;
		try {
			bpag = paginacion(nroRegPag, cantItems);
			listaorden = detalleordenDAO.selectOrdCan(idservicio, idorden,
					lestados, bpag);
		} catch (Exception e) {
			// System.out.println("ERROR: " + e);
		}
		if (listaorden != null) {
			if (listaorden.size() > 0) {

				bpag.setM_primerRegAct(Integer
						.parseInt(((BeanDetalleOrden) listaorden.get(0))
								.getM_IdDetalleOrden()));
				bpag.setM_ultimoRegAct(Integer
						.parseInt(((BeanDetalleOrden) listaorden.get(listaorden
								.size() - 1)).getM_IdDetalleOrden()));
			}
		}
		session.setAttribute("beanPag", bpag);
		session.setAttribute("cantItems", cantItems);

		// BeanOrden
		BeanOrden beanorden = new BeanOrden();
		beanorden.setM_IdEmpresa(idempresa);
		beanorden.setM_IdServicio(idservicio);
		beanorden.setM_IdOrden(idorden);

		request.setAttribute("beanOrden", beanOrden);
		request.setAttribute("listaorden", listaorden);

		return mapping.findForward("cancelar");

	}

	public BeanPaginacion paginacion(int nroRegPag, long cantItems)
			throws Exception {
		try {
			int nroPag;
			int resto = (int) cantItems % nroRegPag;
			if (nroRegPag >= cantItems) {
				nroPag = 1;
			} else {
				nroPag = (int) cantItems / nroRegPag;
				if (resto != 0 ) {
					nroPag++;
				}
			}			
			BeanPaginacion bpag = new BeanPaginacion();
			bpag.setM_pagActual(1);
			bpag.setM_pagFinal(nroPag);
			bpag.setM_pagInicial(1);
			bpag.setM_regPagina(nroRegPag);
			bpag.setM_resto(resto);
			bpag.setM_tipo(Constantes.TIPO_PAG_PRIMERO);

			return bpag;
		} catch (Exception e) {
			throw e;
		}
	}
}
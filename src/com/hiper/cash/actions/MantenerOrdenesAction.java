package com.hiper.cash.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.financiero.cash.delegate.SeguridadDelegate;
import com.hiper.cash.dao.TaCuentasServicioEmpresaDao;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.hibernate.TaCuentasServicioEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.domain.TaCuentasServicioEmpresa;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.domain.TaOrdenId;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.entidad.BeanServicio;
import com.hiper.cash.entidad.BeanSuccess;
import com.hiper.cash.entidad.BeanSuccessDetail;
import com.hiper.cash.forms.ListaOrdenForm;
import com.hiper.cash.util.CollectionFilter;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;

/**
 * 
 * @author esilva
 */

public class MantenerOrdenesAction extends DispatchAction {

	private Logger logger = Logger.getLogger(this.getClass());
	private SeguridadDelegate delegadoSeguridad = SeguridadDelegate.getInstance();
	private String idModulo = null;

	// cargarIngresar
	public ActionForward cargarIngresar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
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

		// Autorizacion
		String habil = request.getParameter("habil");
		if (habil == null || (habil != null && "1".equals(habil.trim()))) {
			session.setAttribute("mo_habil", "1");
		} else {
			session.setAttribute("mo_habil", "0");
			return mapping.findForward("nuevo");
		}

		// Empresas asociadas al usuario
		List lEmpresa = (List) session.getAttribute("empresa");

		// Diferencia entre una Orden de Pago y una de Cobro
		String reqsel = (request.getParameter("sel") != null) ? request
				.getParameter("sel") : "0";
		int sel = Integer.parseInt(reqsel);

		String tiposervicio = "";
		if (sel == 1)
			tiposervicio = Constantes.TX_CASH_TIPO_SERVICIO_PAGO;
		else if (sel == 2)
			tiposervicio = Constantes.TX_CASH_TIPO_SERVICIO_COBRO;
		else
			tiposervicio = (String) session.getAttribute("mo_tiposervicio");

		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();		
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();

		List empresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);
		
		
		String cEmpresa = ((TmEmpresa) empresas.get(0)).getCemIdEmpresa();
		
		List servicios = servicioDAO.selectServicioxEmpresaxTipo(cEmpresa,tiposervicio);
		List cuentas = new ArrayList();
	
		((ListaOrdenForm) form).setEmpresa("");
		((ListaOrdenForm) form).setControl("");
		((ListaOrdenForm) form).setTipoingreso("");
		((ListaOrdenForm) form).setReferencia("");
		((ListaOrdenForm) form).setFechaInicial(Fecha
				.getFechaActual("dd/MM/yyyy"));
		((ListaOrdenForm) form).setFechaFinal(Fecha.getFechaCustom(
				"dd/MM/yyyy", java.util.Calendar.MONTH, 1));
		((ListaOrdenForm) form).setHoraVigencia(Fecha.getFechaActual("HH:mm"));
		((ListaOrdenForm) form).setFechaActualComp(Fecha
				.getFechaActual("yyyyMMdd"));		

		session.setAttribute("mo_listaempresas", empresas);
		session.setAttribute("mo_listaservicios", servicios);
		session.setAttribute("mo_listacuentas", cuentas);
		session.setAttribute("mo_tiposervicio", tiposervicio);
		return mapping.findForward("nuevo");
	}   

	public ActionForward cargarModificar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
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

		session.removeAttribute("beanPag");

		// Autorizacion
		String habil = request.getParameter("habil");
		if (habil == null || (habil != null && "1".equals(habil.trim()))) {
			session.setAttribute("mo_habil", "1");
		} else {
			session.setAttribute("mo_habil", "0");
			return mapping.findForward("modificar");
		}

		// cargamos las empresas asociadas, resultantes del inicio de sesion
		List lEmpresa = null;
		lEmpresa = (List) session.getAttribute("empresa");

		ListaOrdenForm ordenform = (ListaOrdenForm) form;

		// accion 1 -> listar ordenes segun los filtros
		// accion 0 -> inicializa filtros estado inicial
		String reqaccion = (request.getParameter("accion") != null) ? request
				.getParameter("accion") : "0";
		int accion = Integer.parseInt(reqaccion);
		if (accion == 1) {

			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {				 
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}

		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();

		List lempresas = null;
		List lservicios = null;
		List lordenes = null;

		switch (accion) {
		case 1:
			String empresa = ((ListaOrdenForm) form).getEmpresa();
			String servicio = ((ListaOrdenForm) form).getServicio();

			// Todos los servicios
			List lservicios_in = new ArrayList();

			if (servicio.equalsIgnoreCase(Constantes.TX_CASH_SERVICIO_TODOS)) {
				lservicios = (List) session.getAttribute("mo_listaservicios");
				Iterator iter = lservicios.iterator();
				while (iter.hasNext()) {
					BeanServicio beanservicio = (BeanServicio) iter.next();
					if (beanservicio.getEstado().equalsIgnoreCase(
							Constantes.FLAG_ENABLED_SERVICIO))
						lservicios_in.add(Long.parseLong(beanservicio
								.getM_IdServicioEmp()));// jmoreno 13/11/09
				}
			} else {
				if (servicio != null && !servicio.equals("")) {// jmoreno
					// 13/11/09
					lservicios_in.add(Long.parseLong(servicio));
				}
			}

			TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();
			List lestados = new ArrayList();
			lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_INGRESADO);
			// jmoreno 23/07/09 Solo se permitira modificar las ordenes
			// Ingresadas
			// lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
			// lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_PENDAUTO);

			if (lservicios_in.size() > 0) {
				lordenes = ordenDAO.select(empresa, lservicios_in, lestados);
				if (!(lordenes.size() > 0))
					request.setAttribute("bLista", "1");
			} else {
				request.setAttribute("bLista", "2");
			}
			break;
		default:
			ordenform.setServicio("");
			// Diferencia entre una Orden de Pago y una de Cobro
			String reqsel = (request.getParameter("sel") != null) ? request
					.getParameter("sel") : "0";
			int sel = Integer.parseInt(reqsel);
			String tiposervicio = "";
			if (sel == 1)
				tiposervicio = Constantes.TX_CASH_TIPO_SERVICIO_PAGO;
			else if (sel == 2)
				tiposervicio = Constantes.TX_CASH_TIPO_SERVICIO_COBRO;
			else
				tiposervicio = (String) session.getAttribute("mo_tiposervicio");

			((ListaOrdenForm) form).setEmpresa("");
			((ListaOrdenForm) form).setServicio("");
			
	        String numTarjeta= (String) session.getAttribute("tarjetaActual");
	        
	        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);			

			lempresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);
			
			String cEmpresa = ((TmEmpresa) lempresas.get(0)).getCemIdEmpresa();
			lservicios = servicioDAO.selectServicioxEmpresaxTipo(cEmpresa,
					tiposervicio);
			lordenes = new ArrayList();

			session.setAttribute("mo_listaempresas", lempresas);
			session.setAttribute("mo_listaservicios", lservicios);
			session.setAttribute("mo_tiposervicio", tiposervicio);
		}

		session.setAttribute("mo_listaordenes", lordenes);

		return mapping.findForward("modificar");
	}

	public ActionForward cargarEliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
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

		// Autorizacion
		String habil = request.getParameter("habil");
		if (habil == null || (habil != null && "1".equals(habil.trim()))) {
			session.setAttribute("mo_habil", "1");
		} else {
			session.setAttribute("mo_habil", "0");
			return mapping.findForward("cargarEliminar");
		}

		// cargamos las empresas asociadas resultante del logueo
		List lEmpresa = null;
		lEmpresa = (List) session.getAttribute("empresa");

		// accion 1 -> listar ordenes segun los filtros
		// accion 0 -> inicializa filtros estado inicial
		String reqaccion = (request.getParameter("accion") != null) ? request
				.getParameter("accion") : "0";
		int accion = Integer.parseInt(reqaccion);
		
		if (accion == 1) {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}

		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();

		List lempresas = null;
		List lservicios = null;
		List lordenes = null;

		((ListaOrdenForm) form).setValues();

		switch (accion) {
		case 1:
			String empresa = ((ListaOrdenForm) form).getEmpresa();
			String servicio = ((ListaOrdenForm) form).getServicio();

			// Todos los servicios
			List lservicios_in = new ArrayList();
			if (servicio.equalsIgnoreCase(Constantes.TX_CASH_SERVICIO_TODOS)) {
				lservicios = (List) session.getAttribute("mo_listaservicios");
				Iterator iter = lservicios.iterator();
				while (iter.hasNext()) {
					BeanServicio beanservicio = (BeanServicio) iter.next();
					if (beanservicio.getEstado().equalsIgnoreCase(
							Constantes.FLAG_ENABLED_SERVICIO))
						lservicios_in.add(Long.parseLong(beanservicio
								.getM_IdServicioEmp()));// jmoreno 13/11/09
				}
			} else {
				if (servicio != null && !servicio.equals("")) {// jmoreno
					// 13/11/09
					lservicios_in.add(Long.parseLong(servicio));
				}
			}

			TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();
			// Estado 2 : Pendientes de autorizacion;Estado 1:Ingresado
			List lestados = new ArrayList();
			lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_INGRESADO);
			lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_PENDAUTO);
			if (lservicios_in.size() > 0) {
				lordenes = ordenDAO.select(empresa, lservicios_in, lestados);
				if (!(lordenes.size() > 0))
					request.setAttribute("bLista", "1");
			} else {
				request.setAttribute("bLista", "2");
			}
			break;
		default:
			// Diferencia entre una Orden de Pago y una de Cobro
			String reqsel = (request.getParameter("sel") != null) ? request
					.getParameter("sel") : "0";
			int sel = Integer.parseInt(reqsel);
			String tiposervicio = "";
			if (sel == 1)
				tiposervicio = Constantes.TX_CASH_TIPO_SERVICIO_PAGO;
			else if (sel == 2)
				tiposervicio = Constantes.TX_CASH_TIPO_SERVICIO_COBRO;
			else
				tiposervicio = (String) session.getAttribute("mo_tiposervicio");

			((ListaOrdenForm) form).setEmpresa("");
			((ListaOrdenForm) form).setServicio("");
			
	        String numTarjeta= (String) session.getAttribute("tarjetaActual");
	        
	        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);			

			lempresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);
			
			String cEmpresa = ((TmEmpresa) lempresas.get(0)).getCemIdEmpresa();
			lservicios = servicioDAO.selectServicioxEmpresaxTipo(cEmpresa,
					tiposervicio);
			lordenes = new ArrayList();

			session.setAttribute("mo_listaempresas", lempresas);
			session.setAttribute("mo_listaservicios", lservicios);
			session.setAttribute("mo_tiposervicio", tiposervicio);
		}

		session.setAttribute("mo_listaordenes", lordenes);

		return mapping.findForward("cargarEliminar");
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		String idempresa = ((ListaOrdenForm) form).getEmpresa();
		String idservicio = ((ListaOrdenForm) form).getServicio();
		Map map_ordenes = ((ListaOrdenForm) form).getValues();
		/*
		 * jmoreno 16/11/09 Se comento porque no se usa para la actualizacion
		 * del estado del Servicio //Todos los servicios List lservicios = null;
		 * List lservicios_in = new ArrayList();
		 * if(idservicio.equalsIgnoreCase(Constantes.TX_CASH_SERVICIO_TODOS)){
		 * lservicios = (List) session.getAttribute("mo_listaservicios");
		 * Iterator iter = lservicios.iterator(); while(iter.hasNext()){
		 * BeanServicio beanservicio = (BeanServicio) iter.next(); if
		 * (beanservicio
		 * .getEstado().equalsIgnoreCase(Constantes.FLAG_ENABLED_SERVICIO))
		 * lservicios_in.add(beanservicio.getM_IdServicio()); } }else{
		 * lservicios_in.add(idservicio); } //Estado List lestados = new
		 * ArrayList();
		 * lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_INGRESADO);
		 * lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_PENDAUTO);
		 */

		TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();

		List ordenElim = new ArrayList();
		
		
		
		boolean bdelete = ordenDAO.delete(map_ordenes,Constantes.HQL_CASH_ESTADO_ORDEN_ELIMINADO, ordenElim);
		
		ordenDAO.deleteRep(ordenElim,Constantes.HQL_CASH_ESTADO_ORDEN_ELIMINADO);
		

		if (bdelete) {
			// Mensaje Confirmación
			MessageResources messageResources = getResources(request);
			List alsuccess = new ArrayList();
			BeanSuccessDetail sucessdetail;

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.operacion"));
			sucessdetail.setM_Mensaje("ELIMINACIÓN DE ORDENES");
			alsuccess.add(sucessdetail);
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.orden.elim"));
			String sAprob = "";
			for (int i = 0; i < ordenElim.size(); i++) {
				sAprob = sAprob + "   " + (Long) ordenElim.get(i) + "   "
						+ "\n";
			}
			sucessdetail.setM_MensajeArea(sAprob);
			alsuccess.add(sucessdetail);
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.date"));
			sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   "
					+ Fecha.getFechaActual("HH:mm:ss"));
			alsuccess.add(sucessdetail);

			BeanSuccess success = new BeanSuccess();
			success.setM_Titulo(messageResources
					.getMessage("pagos.title.eliminar"));
			success.setM_Mensaje(messageResources
					.getMessage("pagos.confirmacion.title"));
			success
					.setM_Back("mantenerOrdenes.do?do=cargarEliminar\u0026accion=1");
			// success.setM_BackAccion("accion=1");
			request.setAttribute("success", success);
			request.setAttribute("alsuccess", alsuccess);
			return mapping.findForward("eliminar");
		} else
			request
					.setAttribute("mensaje",
							"No se pudo realizar la operación.");
		return mapping.findForward("error");

	}

	public ActionForward cargarCancelar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
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

		// Removemos la paginacion
		session.removeAttribute("beanPag");

		// Autorizacion
		String habil = request.getParameter("habil");
		if (habil == null || (habil != null && "1".equals(habil.trim()))) {
			session.setAttribute("mo_habil", "1");
		} else {

			session.setAttribute("mo_habil", "0");
			return mapping.findForward("cargarCancelar");
		}

		// cargamos las empresas asociadas resultante del logueo
		List lEmpresa = null;
		lEmpresa = (List) session.getAttribute("empresa");

		ListaOrdenForm ordenform = (ListaOrdenForm) form;

		// accion 1 -> listar ordenes segun los filtros
		// accion 0 -> inicializa filtros estado inicial
		String reqaccion = (request.getParameter("accion") != null) ? request
				.getParameter("accion") : "0";
		int accion = Integer.parseInt(reqaccion);
		
		
		if (accion == 1) {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
		

		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();

		List lempresas = null;
		List lservicios = null;
		List lordenes = null;

		((ListaOrdenForm) form).setValues();
		String servicioDesc = "";
		switch (accion) {
		case 1:
			String empresa = ((ListaOrdenForm) form).getEmpresa();
			String servicio = ((ListaOrdenForm) form).getServicio();

			// Todos los servicios
			List lservicios_in = new ArrayList();
			if (servicio.equalsIgnoreCase(Constantes.TX_CASH_SERVICIO_TODOS)) {
				lservicios = (List) session.getAttribute("mo_listaservicios");
				Iterator iter = lservicios.iterator();
				while (iter.hasNext()) {
					BeanServicio beanservicio = (BeanServicio) iter.next();
					// jyamunaque 23-02-2011 Se captura la descripcion del
					// servicio (RECAUDACION, PAGO) para poder eliminar solo
					// items de Recaudos.
					servicioDesc = beanservicio.getM_Descripcion();
					if (beanservicio.getEstado().equalsIgnoreCase(
							Constantes.FLAG_ENABLED_SERVICIO))
						lservicios_in.add(Long.parseLong(beanservicio
								.getM_IdServicioEmp()));
				}
			} else {

				if (servicio != null && !servicio.equals("")) {// jmoreno
					// 13/11/09
					lservicios_in.add(Long.parseLong(servicio));
				}
			}

			TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();
			// Estado 2 : Pendientes de autorizacion
			// Estado
			List lestados = new ArrayList();
			lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
			lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_PROCESADO_PARCIAL);
			if (lservicios_in.size() > 0) {
				/*System.out.println("EMPRESA: " + empresa);
				System.out.println("LSERVICIOS: " + lservicios_in);
				System.out.println("ESTADOS: " + lestados);*/
				lordenes = ordenDAO.select(empresa, lservicios_in, lestados);
				if (!(lordenes.size() > 0))
					request.setAttribute("bLista", "1");
			} else {
				request.setAttribute("bLista", "2");
			}
			break;
		default:
			// Diferencia entre una Orden de Pago y una de Cobro
			String reqsel = (request.getParameter("sel") != null) ? request
					.getParameter("sel") : "0";
			int sel = Integer.parseInt(reqsel);
			String tiposervicio = "";
			if (sel == 1)
				tiposervicio = Constantes.TX_CASH_TIPO_SERVICIO_PAGO;
			else if (sel == 2)
				tiposervicio = Constantes.TX_CASH_TIPO_SERVICIO_COBRO;
			else
				tiposervicio = (String) session.getAttribute("mo_tiposervicio");

			((ListaOrdenForm) form).setEmpresa("");
			((ListaOrdenForm) form).setServicio("");
			
	        String numTarjeta= (String) session.getAttribute("tarjetaActual");
	        
	        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);

			lempresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);
			String cEmpresa = ((TmEmpresa) lempresas.get(0)).getCemIdEmpresa();
			lservicios = servicioDAO.selectServicioxEmpresaxTipo(cEmpresa,
					tiposervicio);
			lordenes = new ArrayList();

			session.setAttribute("mo_listaempresas", lempresas);
			session.setAttribute("mo_listaservicios", lservicios);
			session.setAttribute("mo_tiposervicio", tiposervicio);
		}

		// jyamunaque 23-02-2011 TIPO PROCESO = null -> PAGO; TIPO PROCESO = 0
		// -> COBROS|RECAUDACION
		servicioDesc = servicioDesc.toUpperCase();
		int kServicioDesc = servicioDesc.indexOf("PAGO");
		String tipo_proceso = (kServicioDesc >= 0 ? null : "0");
		// String tipo_proceso = (kServicioDesc >= 0 ? "0" : null);
		session.setAttribute("mo_tipo_proceso", tipo_proceso);

		session.setAttribute("mo_listaordenes", lordenes);

		if (lordenes == null || lordenes.size() < 0) {
			request.setAttribute("mo_mensaje", "true");
		}

		return mapping.findForward("cargarCancelar");
	}

	public ActionForward cancelar_borrar(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		String idempresa = ((ListaOrdenForm) form).getEmpresa();
		String idservicio = ((ListaOrdenForm) form).getServicio();
		Map map_ordenes = ((ListaOrdenForm) form).getValues();

		// Todos los servicios
		List lservicios = null;
		List lservicios_in = new ArrayList();
		if (idservicio.equalsIgnoreCase(Constantes.TX_CASH_SERVICIO_TODOS)) {
			lservicios = (List) session.getAttribute("mo_listaservicios");
			Iterator iter = lservicios.iterator();
			while (iter.hasNext()) {
				BeanServicio beanservicio = (BeanServicio) iter.next();
				if (beanservicio.getEstado().equalsIgnoreCase(
						Constantes.FLAG_ENABLED_SERVICIO))
					lservicios_in.add(beanservicio.getM_IdServicio());
			}
		} else {
			lservicios_in.add(idservicio);
		}
		// Estado
		List lestados = new ArrayList();
		lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
		lestados.add(Constantes.HQL_CASH_ESTADO_ORDEN_PENDAUTO);

		TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();

		List ordenCancel = new ArrayList();
		boolean bdelete = ordenDAO.delete(
		/* idempresa, lservicios_in, lestados, */map_ordenes,
				Constantes.HQL_CASH_ESTADO_ORDEN_CANCELADO, ordenCancel);

		ordenDAO.deleteOrdenRep(map_ordenes,
				Constantes.HQL_CASH_ESTADO_ORDEN_CANCELADO, 
				ordenCancel);
			
		
		
		if (bdelete) {
			// Mensaje Confirmación
			MessageResources messageResources = getResources(request);
			List alsuccess = new ArrayList();
			BeanSuccessDetail sucessdetail;

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.operacion"));
			sucessdetail.setM_Mensaje("CANCELACIÓN DE ORDENES");
			alsuccess.add(sucessdetail);
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.numorden"));
			sucessdetail.setM_Mensaje("");
			alsuccess.add(sucessdetail);
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label(messageResources
					.getMessage("pagos.confirmacion.date"));
			sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   "
					+ Fecha.getFechaActual("HH:mm:ss"));
			alsuccess.add(sucessdetail);

			BeanSuccess success = new BeanSuccess();
			success.setM_Titulo(messageResources
					.getMessage("pagos.title.eliminar"));
			success.setM_Mensaje(messageResources
					.getMessage("pagos.confirmacion.title"));
			success.setM_Back("mantenerOrdenes.do?do=cargarCancelar");

			request.setAttribute("success", success);
			request.setAttribute("alsuccess", alsuccess);
			return mapping.findForward("eliminar");
		} else
			return mapping.findForward("error");

	}

	public ActionForward cargarServicios(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}
		int tipo = Integer.parseInt(request.getParameter("tipo"));

		String tiposervicio = (String) session.getAttribute("mo_tiposervicio");

		String empresa = ((ListaOrdenForm) form).getEmpresa();

		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();

		List servicios = servicioDAO.selectServicioxEmpresaxTipo(empresa,
				tiposervicio);
		// List servicios = servicioDAO.selectServicioxEmpresa(empresa);

		session.setAttribute("mo_listaservicios", servicios);
		session.setAttribute("mo_listacuentas", new ArrayList());
		session.setAttribute("mo_listaordenes", new ArrayList());
		((ListaOrdenForm) form).setControl("");
		((ListaOrdenForm) form).setServicio("");
		switch (tipo) {
		case 1:
			return mapping.findForward("nuevo");
		case 2:
			return mapping.findForward("modificar");
		case 3:
			return mapping.findForward("cargarEliminar");
		case 4:
			return mapping.findForward("cargarCancelar");
		default:
			return mapping.findForward("nuevo");
		}

	}

	public ActionForward cargarCuentas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		String empresa = ((ListaOrdenForm) form).getEmpresa();
		String cod = (String) request.getParameter("cod");
		String param[] = cod.split("\\*");
		String servicioEmp = param[0];
		String servicio = param[1];

		List cuentas = new ArrayList();
		if (servicio != null) {
			TaCuentasServicioEmpresaDao cuentaDAO = new TaCuentasServicioEmpresaDaoHibernate();
			cuentas = cuentaDAO
					.selectServicioxEmpresa(/* empresa, */servicioEmp);
			if ("04".equalsIgnoreCase(servicio))
				request.setAttribute("mo_tiposervicio", "02");
		}

		((ListaOrdenForm) form).setTipoingreso("");

		session.setAttribute("mo_listacuentas", cuentas);
		return mapping.findForward("nuevo");
	}

	public ActionForward cargarOrden(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
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
		request.setAttribute("modulo", idModulo);
		List al_cuentas = (List) session.getAttribute("mo_listacuentas");

		ListaOrdenForm ordenform = (ListaOrdenForm) form;
		String empresa = ordenform.getEmpresa();
		String servicio = ordenform.getControl();
		// servicio = servicio.substring(0, 2);
		servicio = servicio.substring(0, servicio.indexOf("*"));
		String cuenta = ordenform.getCuenta();

		TaCuentasServicioEmpresa tacuenta = (TaCuentasServicioEmpresa) CollectionUtils
				.find(al_cuentas, new CollectionFilter(cuenta, 1));

		// Hibernate
		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
		TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();

		TmEmpresa objempresa = empresaDAO.selectEmpresas(empresa);
		TaServicioxEmpresa objservicioxempresa = servicioxempresaDAO
				.selectServicioxEmpresa(empresa, Long.parseLong(servicio));// jmoreno
		// 13/11/09

		// Bean Orden
		// key
		TaOrden taorden = new TaOrden();
		taorden.setId(new TaOrdenId());
		taorden.getId().setCorIdServicioEmpresa(
				objservicioxempresa.getCsemIdServicioEmpresa());
		// properties
		taorden.setNorNumeroCuenta(tacuenta.getId().getDcsemNumeroCuenta());
		taorden.setDorReferencia(ordenform.getReferencia());
		taorden.setForFechaInicio(ordenform.getFechaInicial());
		taorden.setForFechaFin(ordenform.getFechaFinal());
		taorden.setDorTipoIngreso(ordenform.getTipoingreso().charAt(0));
		taorden.setHorHoraInicio(ordenform.getHoraVigencia().substring(0, 2)
				+ ordenform.getHoraVigencia().substring(3, 5) + "00");
		// adicionales
		taorden.setM_IdServicio(ordenform.getServicio());
		taorden.setM_Servicio(objservicioxempresa.getDsemDescripcion());// jmoreno
		// 13/11/09
		taorden.setM_Empresa(objempresa.getDemNombre());
		taorden.setM_DescCuenta(tacuenta.getId().getDcsemNumeroCuenta() + " - "
				+ tacuenta.getDcsemDescripcion() + " - "
				+ tacuenta.getCcsemMoneda());
		taorden.setM_DescHora(ordenform.getHoraVigencia());
		taorden.setM_FlagAprobAut(objservicioxempresa.getCsemFlagAprobAut());
		// Validaciones

		String tipoingreso = (String) ordenform.getTipoingreso();

		if (tipoingreso.equalsIgnoreCase("1")) {
			// Carga un archivo al servidor - jmoreno
			taorden.setHorHoraInicio(Fecha.formatearFecha("HHmmss", "HH:mm",
					taorden.getHorHoraInicio()));
			session.setAttribute("objorden", taorden);// ver como manejar esto
			// para que no se quede
			// como session
			request.setAttribute("idSevxEmp", objservicioxempresa
					.getCsemIdServicioEmpresa());
			request.setAttribute("fechaIni", taorden.getForFechaInicio());
			request.setAttribute("fechaFin", taorden.getForFechaFin());

			// request.setAttribute("horaIni",Fecha.formatearFecha("HHmmss",
			// "HH:mm:ss", taorden.getHorHoraInicio()));
			request.setAttribute("horaIni", taorden.getHorHoraInicio());

			request.setAttribute("cuentaCargo", taorden.getNorNumeroCuenta());
			request.setAttribute("tipoIngreso", taorden.getDorTipoIngreso());
			request.setAttribute("referencia", taorden.getDorReferencia());
			request.setAttribute("ruc_empresa", empresa);				
			request.setAttribute("idServicio", objservicioxempresa.getTmServicio().getCsrIdServicio());
			return mapping.findForward("cargaFile");
		} else {
			// Muestra una grilla editable
			request.setAttribute("empresa", empresa);
			request.setAttribute("servicio", servicio);
			session.setAttribute("mo_objorden", taorden);

			return mapping.findForward("cargaOnline");
		}
	}

}

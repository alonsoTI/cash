package com.hiper.cash.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.struts.util.MessageResources;

import com.financiero.cash.component.ui.Paginado;
import com.financiero.cash.delegate.ComprobantesDelegate;
import com.financiero.cash.delegate.PagoServicioComprobantes;
import com.financiero.cash.delegate.TransferenciasDelegate;
import com.financiero.cash.service.ComprobantesService;
import com.financiero.cash.service.impl.ComprobantesServiceImpl;
import com.hiper.cash.dao.TaAprobacionOrdenDao;
import com.hiper.cash.dao.TaEmpresaFormatoDao;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TaServicioOpcionDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.TmFormatoDao;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.TxListFieldDao;
import com.hiper.cash.dao.hibernate.TaAprobacionOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaEmpresaFormatoDaoHibernate;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioOpcionDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmFormatoDaoHibernate;
import com.hiper.cash.dao.hibernate.TpDetalleOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TxListFieldDaoHibernate;
import com.hiper.cash.descarga.excel.GeneradorPOI;
import com.hiper.cash.domain.TaServicioOpcion;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.domain.TxListField;
import com.hiper.cash.entidad.BeanDetalleImporteEstado;
import com.hiper.cash.entidad.BeanDetalleOrden;
import com.hiper.cash.entidad.BeanMovimiento;
import com.hiper.cash.entidad.BeanOrden;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.BeanServicio;
import com.hiper.cash.entidad.BeanSuccessDetail;
import com.hiper.cash.entidad.BeanTotalesConsMov;
import com.hiper.cash.forms.ConsultaOrdenesForm;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.EstadoCash;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;
import com.hiper.cash.xml.bean.BeanDataLoginXML;

public class ComprobantesAction extends DispatchAction {

	private Logger logger = Logger.getLogger(this.getClass());

	TaServicioxEmpresaDaoHibernate servicioEmpresaDao = new TaServicioxEmpresaDaoHibernate();

	TaEmpresaFormatoDao empFormatodao = new TaEmpresaFormatoDaoHibernate();

	TmFormatoDao detalleFormatoDao = new TmFormatoDaoHibernate();

	TpDetalleOrdenDao detalleDao = new TpDetalleOrdenDaoHibernate();

	ComprobantesService comprobanteService = new ComprobantesServiceImpl();

	ComprobantesDelegate delegado = ComprobantesDelegate.getInstance();

	public ActionForward cargarConsultasOrdenes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session
				.getAttribute("usuarioActual");
		// si termino la session debemos retornar al inicio
		if (beanDataLogXML == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		// Autorizacion
		String habil = request.getParameter("habil");
		if (habil == null || (habil != null && "1".equals(habil.trim()))) {
			session.setAttribute("habil", "1");
		} else {
			session.setAttribute("habil", "0");
			return mapping.findForward("cargarConsultasOrdenes");
		}
		session.removeAttribute("beanPag");
		request.removeAttribute("listaResultOrden");
		
		
		String numTarjeta= (String) session.getAttribute("tarjetaActual");
		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
		
		boolean swverifica=empresaDAO.verificaSiTarjetaCash(numTarjeta);
		

		// cargamos las empresas asociadas resultante del logueo
		// List lEmpresa = (List) session.getAttribute("empresa");
		List lEmpresa = null;
		if (beanDataLogXML.isM_usuarioEspecial()) {// verificamos si el usuario
													// actual es Especial(Emp.
													// Operaciones Cash)
			// //System.out.println("Entrooo a usuario especial");
			lEmpresa = (List) session.getAttribute("empresa");// obtenemos la
																// lista de
																// empresas de
																// la session
			TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
			
			// a partir de lEmpresas, obtenemos la lista de empresas que estan
			// afiliadas al servicio
			
			
			
			lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa,
					Constantes.TX_CASH_SERVICIO_COMPROBANTES_ORDEN);
			if (lEmpresa.size() == 0) {
				request.setAttribute("mensaje",
						"El servicio no se encuentra afiliado");
				return mapping.findForward("error");
			}
		} else {
			// hMapEmpresas contiene las empresas con sus respectivos servicios
			// afiliados
			HashMap hMapEmpresas = (HashMap) session.getAttribute("hmEmpresas");
			// obtenemos la lista de empresas que estan afiliadas al servicio,
			// segun la data obtenida del logeo en el hashMap
			lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas,
					Constantes.TX_CASH_SERVICIO_COMPROBANTES_ORDEN);
			if (lEmpresa.size() == 0) {
				request.setAttribute("mensaje",
						"El servicio no se encuentra afiliado");
				return mapping.findForward("error");
			}
		}

		// obtenemos los datos de la empresa que resulto al logearnos
		
		List listaEmpresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);

		String cEmpresa = ((TmEmpresa) listaEmpresas.get(0)).getCemIdEmpresa();

		// obtenemos el listado de servicios relacionados con la empresa
		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();
		List tiposervicio = new ArrayList();
		tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_PAGO);
		tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_COBRO);
		tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_TRANSFERENCIAS);
		tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV);
		tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_LETRAS);
		// para obtener todos los servicios sin importar el estado en que se
		// encuentren
		List listaServicios = servicioDAO.selectServicioxEmpresaxTipoxEstado(
				cEmpresa, tiposervicio,
				Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO);// jmoreno
																	// 12/11/09
		// List listaServicios = servicioDAO.selectServicioxEmpresa(cEmpresa);

		// obtenemos el listado de estados de las ordenes
		TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
		List listaEstadosOrden = listFieldDAO
				.selectListFieldByFieldName(Constantes.FIELD_CASH_ESTADO_ORDEN);

		// obtenemos la fecha actual
		String fechaActual = Fecha.getFechaActual("dd/MM/yyyy");
		// Obtenemos la fecha actual con un formato para comparar en pagina
		String fechaActualComp = Fecha.getFechaActual("yyyyMMdd");

		// jwong 03/06/2009 obtenemos la fecha hace un año
		String fechaHace1Anho = Fecha.getFechaCustom("yyyyMMdd",
				java.util.Calendar.YEAR, -1);
		session.setAttribute("fechaHace1Anho", fechaHace1Anho);

		session.setAttribute("listaEmpresas", listaEmpresas); // listaEmpresas
		session.setAttribute("listaServicios", listaServicios);
		session.setAttribute("listaEstadosOrden", listaEstadosOrden);
		session.setAttribute("listaTipo", tiposervicio);
		session.setAttribute("fechaActualComp", fechaActualComp);

		// obtenemos el formulario
		ConsultaOrdenesForm consultaOrdenesForm = (ConsultaOrdenesForm) form;
		// reseteamos los valores del form
		consultaOrdenesForm.reset(mapping, request);
		consultaOrdenesForm.setM_Empresa(null);
		consultaOrdenesForm.setM_Servicio(null);
		consultaOrdenesForm.setM_Estado(null);
		// seteamos la fecha actual para ser mostrada en la pagina
		consultaOrdenesForm.setM_FecInicio(fechaActual);
		consultaOrdenesForm.setM_FecFin(fechaActual);
		consultaOrdenesForm.setM_ConsReferencia(null);

		return mapping.findForward("cargarConsultasOrdenes");
	}

	@SuppressWarnings("unchecked")
	public ActionForward buscarOrdenesLinea(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("usuarioActual") == null) {
				response.sendRedirect("cierraSession.jsp");
				return null;
			}
			ConsultaOrdenesForm consultaOrdenesForm = (ConsultaOrdenesForm) form;
			String servicio = consultaOrdenesForm.getM_Servicio();
			String estado = consultaOrdenesForm.getM_Estado();
			String fechaInicio = consultaOrdenesForm.getM_FecInicio();
			String fechaFin = consultaOrdenesForm.getM_FecFin();
			String referencia = consultaOrdenesForm.getM_ConsReferencia();
			String tipo = consultaOrdenesForm.getTipoProcesamiento();

			List<String> lservicios = null;
			List<Long> servicios = new ArrayList();

			if (servicio.equals(Constantes.TX_CASH_SERVICIO_TODOS)) {
				lservicios = (List<String>) session
						.getAttribute("listaServicios");
				Iterator iter = lservicios.iterator();
				while (iter.hasNext()) {
					BeanServicio beanservicio = (BeanServicio) iter.next();
					if (beanservicio.getEstado().equalsIgnoreCase(
							Constantes.FLAG_ENABLED_SERVICIO)) {
						servicios.add(Long.parseLong(beanservicio
								.getM_IdServicio()));
					}
				}
			} else {
				servicios.add(Long.parseLong(servicio));
			}

			if (servicios.size() > 0) {
				Paginado paginado = getPaginadorOrdenes(tipo, 10, servicios,
						estado, fechaInicio, fechaFin, referencia);
				List<BeanOrden> ordenes = paginado.getItemsPagina();
				session.setAttribute("paginado", paginado);
				request.setAttribute("ordenes", ordenes);
			}
			return mapping.findForward("cargarConsultasOrdenes");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return mapping.findForward("error");
		}
	}

	@SuppressWarnings("unchecked")
	public ActionForward exportarOrdenes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			int accion = Integer.valueOf(request.getParameter("accion"));
			Paginado<BeanOrden> paginado = (Paginado<BeanOrden>) request
					.getSession().getAttribute("paginado");
			if (accion == 2) {
				String nombre_archivo = "Orden-" + Util.strFecha() + ".txt";
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + nombre_archivo + "\"");
				response.setContentType("text/plain");
				String texto = delegado.generarArchivoOrdenes(paginado
						.getItems());
				PrintWriter out = new PrintWriter(response.getOutputStream());
				out.println(texto);
				out.flush();
				out.close();
				response.getOutputStream().flush();
				response.getOutputStream().close();
				return null;
			} else if (accion == 1) {
				String fecha = Util.strFecha();
				String nombre_archivo = "Orden_" + fecha + ".xls";
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + nombre_archivo + "\"");
				response.setContentType("application/vnd.ms-excel");

				HSSFWorkbook libroXLS = delegado.generarExcelOrdenes("Orden",
						paginado.getItems());
				if (libroXLS != null) {
					libroXLS.write(response.getOutputStream());
					response.getOutputStream().close();
					response.getOutputStream().flush();
				}
				return null;
			} else {
				request.setAttribute("ordenes", paginado.getItems());
				return mapping.findForward("exportaOrdenes");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return mapping.findForward("error");
		}
	}

	@SuppressWarnings("unchecked")
	public ActionForward paginarOrdenes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			HttpSession session = request.getSession();
			String nroPagina = request.getParameter("nroPagina");
			Paginado<BeanOrden> paginado = (Paginado<BeanOrden>) session
					.getAttribute("paginado");
			paginado.setNroPagina(Integer.valueOf(nroPagina));
			List<BeanOrden> ordenes = paginado.getItemsPagina();
			request.setAttribute("ordenes", ordenes);
			return mapping.findForward("paginadoOrdenes");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return mapping.findForward("error");
		}
	}

	private Paginado<BeanOrden> getPaginadorOrdenes(final String tp,
			int nroItemsPagina, final List<Long> servicios,
			final String estado, final String fechaInicio,
			final String fechaFin, final String referencia) {

		Paginado<BeanOrden> paginador = new Paginado<BeanOrden>(nroItemsPagina) {

			@Override
			public List<BeanOrden> getItemsPagina() {
				try {
					if (tp.equals(CashConstants.ITEMS_PROCESADO)) {
						return delegado.buscarOrdenesReferenciaTrx(servicios,
								estado, fechaInicio, fechaFin, referencia, this
										.getItemInicioPagina(),
								this.nroItemsPagina);
					} else {
						return delegado.buscarOrdenesReferenciaNoTrx(servicios,
								estado, fechaInicio, fechaFin, referencia, this
										.getItemInicioPagina(),
								this.nroItemsPagina);

					}

				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					return new ArrayList<BeanOrden>();
				}
			}

			@Override
			public int getNroTotalItems() {
				try {
					if (tp.equals(CashConstants.ITEMS_PROCESADO)) {
						return delegado.contarOrdenesReferenciaTrx(servicios,
								estado, fechaInicio, fechaFin, referencia);
					} else {
						return delegado.contarOrdenesReferenciaNoTrx(servicios,
								estado, fechaInicio, fechaFin, referencia);
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					return 0;
				}
			}
		};

		return paginador;
	}

	public ActionForward detalleOrdenesPag(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}
		// obtenemos las llaves para obtener el detalle de la porden
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

		String[] ta = servempDao.selectTipoServicioxEmpresa(Long
				.parseLong(idServicio));

		if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equalsIgnoreCase(ta[1])
				|| Constantes.TX_CASH_TIPO_SERVICIO_COBRO
						.equalsIgnoreCase(ta[1])) {
			resultado = detalleordenDao.selectDetallePago(idServicio, idOrden,
					bpag);
			if (resultado != null && resultado.size() > 0) {
				request.setAttribute("listaDetalle", resultado);
			}
		}
		// Cambio que se realiza para mostrar la comision cliente o empresa
		if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equalsIgnoreCase(ta[1])) {
			request.setAttribute("comprobantePago", "comprobantePago");
		} else if (Constantes.TX_CASH_TIPO_SERVICIO_TRANSFERENCIAS
				.equalsIgnoreCase(ta[1])) {
			TaServicioOpcionDao servicio_opcionDAO = new TaServicioOpcionDaoHibernate();
			List alservicioopcion = servicio_opcionDAO
					.select(Constantes.HQL_MODULO_TRANSFERENCIA);
			for (Iterator it = alservicioopcion.iterator(); it.hasNext();) {
				TaServicioOpcion tso = (TaServicioOpcion) it.next();
				if (tso.getId().getCsoservicioId().equalsIgnoreCase(ta[0])) {
					if (tso.getId().getCsoproceso().equalsIgnoreCase(
							Constantes.HQL_PROCESO_TRANSFERENCIA_CP)) {
						resultado = detalleordenDao.selectDetalleTransferencia(
								idServicio, idOrden, bpag);
						if (resultado != null && resultado.size() > 0) {
							request.setAttribute("listaDetalleTransf_P",
									resultado);
						}
					} else if (tso.getId().getCsoproceso().equalsIgnoreCase(
							Constantes.HQL_PROCESO_TRANSFERENCIA_CT)) {
						resultado = detalleordenDao.selectDetalleTransferencia(
								idServicio, idOrden, bpag);
						if (resultado != null && resultado.size() > 0) {
							request.setAttribute("listaDetalleTransf_T",
									resultado);
						}
					} else if (tso.getId().getCsoproceso().equalsIgnoreCase(
							Constantes.HQL_PROCESO_TRANSFERENCIA_I)) {
						resultado = detalleordenDao.selectDetalleTransferencia(
								idServicio, idOrden, bpag);
						if (resultado != null && resultado.size() > 0) {
							request.setAttribute("listaDetalleTransf_I",
									resultado);
						}
					}
					break;
				}
			}
		} else if (Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV
				.equalsIgnoreCase(ta[1])) {
			resultado = detalleordenDao.selectDetallePago(idServicio, idOrden,
					bpag);
			if (resultado != null && resultado.size() > 0) {
				request.setAttribute("listaDetallePagoServicio", resultado);
			}
		} // para letras
		else if (Constantes.TX_CASH_TIPO_SERVICIO_LETRAS
				.equalsIgnoreCase(ta[1])) {
			resultado = detalleordenDao.selectDetallePago(idServicio, idOrden,
					bpag);
			if (resultado != null && resultado.size() > 0) {
				request.setAttribute("listaDetallePagoLetras", resultado);
			}
		}

		if (resultado == null || resultado.size() < 1) {
			request.setAttribute("mensaje", "No se encontraron resultados");
		}
		request.setAttribute("back", "comprobantes.do?do=buscarOrdenesLinea");

		return mapping.findForward("cargarDetalleOrdenes");
	}

	public ActionForward detalleOrdenes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}
		// obtenemos las llaves para obtener el detalle de la porden
		String idOrden = request.getParameter("m_IdOrden");
		String idServicio = request.getParameter("m_IdServicio");

		MessageResources messageResources = getResources(request);
		int nroRegPag = Integer.parseInt(messageResources
				.getMessage("paginacion.comprobantes"));
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
		bpag.setM_tipo(Constantes.TIPO_PAG_COMPROBANTES);
		session.setAttribute("beanPag", bpag);

		// se lo enviamos a la pagina del detalle para la impresion
		session.setAttribute("midOrden", idOrden);
		session.setAttribute("midServicio", idServicio);

		// String ordenServicio = idOrden + ";" + idServicio;

		List resultado = null;

		TaServicioxEmpresaDao servempDao = new TaServicioxEmpresaDaoHibernate();
		TpDetalleOrdenDao detalleordenDao = new TpDetalleOrdenDaoHibernate();

		String[] ta = servempDao.selectTipoServicioxEmpresa(Long
				.parseLong(idServicio));

		if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equalsIgnoreCase(ta[1])
				|| Constantes.TX_CASH_TIPO_SERVICIO_COBRO
						.equalsIgnoreCase(ta[1])) {
			resultado = detalleordenDao.selectDetallePago(idServicio, idOrden,
					bpag);
			if (resultado != null && resultado.size() > 0) {
				request.setAttribute("listaDetalle", resultado);
			}
		}
		// Cambio que se realiza para mostrar la comision cliente o empresa
		if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equalsIgnoreCase(ta[1])) {
			request.setAttribute("comprobantePago", "comprobantePago");
		} else if (Constantes.TX_CASH_TIPO_SERVICIO_TRANSFERENCIAS
				.equalsIgnoreCase(ta[1])) {
			TaServicioOpcionDao servicio_opcionDAO = new TaServicioOpcionDaoHibernate();
			List alservicioopcion = servicio_opcionDAO
					.select(Constantes.HQL_MODULO_TRANSFERENCIA);
			for (Iterator it = alservicioopcion.iterator(); it.hasNext();) {
				TaServicioOpcion tso = (TaServicioOpcion) it.next();
				if (tso.getId().getCsoservicioId().equalsIgnoreCase(ta[0])) {
					if (tso.getId().getCsoproceso().equalsIgnoreCase(
							Constantes.HQL_PROCESO_TRANSFERENCIA_CP)) {
						resultado = detalleordenDao.selectDetalleTransferencia(
								idServicio, idOrden, bpag);
						if (resultado != null && resultado.size() > 0) {
							request.setAttribute("listaDetalleTransf_P",
									resultado);
						}
					} else if (tso.getId().getCsoproceso().equalsIgnoreCase(
							Constantes.HQL_PROCESO_TRANSFERENCIA_CT)) {
						resultado = detalleordenDao.selectDetalleTransferencia(
								idServicio, idOrden, bpag);
						if (resultado != null && resultado.size() > 0) {
							request.setAttribute("listaDetalleTransf_T",
									resultado);
						}
					} else if (tso.getId().getCsoproceso().equalsIgnoreCase(
							Constantes.HQL_PROCESO_TRANSFERENCIA_I)) {
						resultado = detalleordenDao.selectDetalleTransferencia(
								idServicio, idOrden, bpag);
						if (resultado != null && resultado.size() > 0) {
							request.setAttribute("listaDetalleTransf_I",
									resultado);
						}
					}
					break;
				}
			}
		} else if (Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV
				.equalsIgnoreCase(ta[1])) {
			resultado = detalleordenDao.selectDetallePago(idServicio, idOrden,
					bpag);
			if (resultado != null && resultado.size() > 0) {
				request.setAttribute("listaDetallePagoServicio", resultado);
			}
		} // para letras
		else if (Constantes.TX_CASH_TIPO_SERVICIO_LETRAS
				.equalsIgnoreCase(ta[1])) {
			resultado = detalleordenDao.selectDetallePago(idServicio, idOrden,
					bpag);
			if (resultado != null && resultado.size() > 0) {
				request.setAttribute("listaDetallePagoLetras", resultado);
			}
		}

		if (resultado == null || resultado.size() < 1) {
			request.setAttribute("bandDet", "0");
			request.setAttribute("mensaje", "No se encontraron resultados");
		}
		request.setAttribute("back", "comprobantes.do?do=buscarOrdenesLinea");

		return mapping.findForward("cargarDetalleOrdenes");
	}

	// jwong 22/04/2009 para manejo de la impresion del detalle de ordenes

	public ActionForward exportarDetOrdenes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}
		// obtenemos las llaves para obtener el detalle de la orden
		String idOrden = request.getParameter("m_IdOrden");
		String idServicio = request.getParameter("m_IdServicio");

		// String ordenServicio = idOrden + ";" + idServicio;

		BeanPaginacion bpag = (BeanPaginacion) session.getAttribute("beanPag");

		List resultado = null;

		TaServicioxEmpresaDao servempDao = new TaServicioxEmpresaDaoHibernate();
		TpDetalleOrdenDao detalleordenDao = new TpDetalleOrdenDaoHibernate();

		String[] ta = servempDao.selectTipoServicioxEmpresa(Long
				.parseLong(idServicio));

		ArrayList lblColumnas = new ArrayList();
		ArrayList lstData = new ArrayList();
		BeanDetalleOrden bean = null;
		if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equalsIgnoreCase(ta[1])
				|| Constantes.TX_CASH_TIPO_SERVICIO_COBRO
						.equalsIgnoreCase(ta[1])) {

			// resultado = detalleordenDao.selectDetallePago(idServicio,
			// idOrden, bpag);

			resultado = detalleordenDao.selectDetallePago(idServicio, idOrden);

			if (resultado != null && resultado.size() > 0) {
				lblColumnas.add("Id Orden");
				lblColumnas.add("Id Registro");
				lblColumnas.add("Tipo Cuenta");
				lblColumnas.add("Cuenta");
				lblColumnas.add("Tipo Doc");
				lblColumnas.add("Número Doc");
				lblColumnas.add("Nombre");
				lblColumnas.add("Referencia");
				lblColumnas.add("Moneda");
				lblColumnas.add("Importe");
				lblColumnas.add("Tipo Pago");
				lblColumnas.add("Estado");
				lblColumnas.add("Código Respuesta");
				lblColumnas.add("Fecha Proceso");
				lblColumnas.add("Nro Transacción");
				lblColumnas.add("Nro Cheque");
				// solo para ordenes de tipo Pago
				if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO
						.equalsIgnoreCase(ta[1])) {
					lblColumnas.add("Comisión Cliente");
					lblColumnas.add("Comisión Empresa");
					for (int i = 0; i < resultado.size(); i++) {
						bean = (BeanDetalleOrden) resultado.get(i);
						lstData.add(new String[] {
								bean.getM_IdOrden(),
								bean.getM_IdDetalleOrden(),
								bean.getM_DescTipoCuenta(),
								bean.getM_NumeroCuenta(),
								bean.getM_DescTipoDocumento(),
								bean.getM_Documento(),
								bean.getM_Nombre(),
								bean.getM_Referencia(),
								bean.getM_DescTipoMoneda(),
								bean.getM_Monto(),
								bean.getM_DescTipoPago(),
								bean.getM_DescEstado(),
								bean.getM_CodigoRptaIbs() + " - "
										+ bean.getM_descripcionCodIbs(),
								bean.getM_FechaProceso(), bean.getM_IdPago(),
								bean.getM_NumCheque(),
								bean.getM_montoComClienteChg(),
								bean.getM_montoComEmpresaChg() });
					}
					request.setAttribute("comprobantePago", "comprobantePago");
				} else if (Constantes.TX_CASH_TIPO_SERVICIO_COBRO
						.equalsIgnoreCase(ta[1])) {
					for (int i = 0; i < resultado.size(); i++) {
						bean = (BeanDetalleOrden) resultado.get(i);
						lstData.add(new String[] {
								bean.getM_IdOrden(),
								bean.getM_IdDetalleOrden(),
								bean.getM_DescTipoCuenta(),
								bean.getM_NumeroCuenta(),
								bean.getM_DescTipoDocumento(),
								bean.getM_Documento(),
								bean.getM_Nombre(),
								bean.getM_Referencia(),
								bean.getM_DescTipoMoneda(),
								bean.getM_Monto(),
								bean.getM_DescTipoPago(),
								bean.getM_DescEstado(),
								bean.getM_CodigoRptaIbs() + " - "
										+ bean.getM_descripcionCodIbs(),
								bean.getM_FechaProceso(), bean.getM_IdPago(),
								bean.getM_NumCheque() });
					}
				}

				request.setAttribute("listaDetalle", resultado);
			}
		} else if (Constantes.TX_CASH_TIPO_SERVICIO_TRANSFERENCIAS
				.equalsIgnoreCase(ta[1])) {
			TaServicioOpcionDao servicio_opcionDAO = new TaServicioOpcionDaoHibernate();
			List alservicioopcion = servicio_opcionDAO
					.select(Constantes.HQL_MODULO_TRANSFERENCIA);
			for (Iterator it = alservicioopcion.iterator(); it.hasNext();) {
				TaServicioOpcion tso = (TaServicioOpcion) it.next();
				if (tso.getId().getCsoservicioId().equalsIgnoreCase(ta[0])) {
					if (tso.getId().getCsoproceso().equalsIgnoreCase(
							Constantes.HQL_PROCESO_TRANSFERENCIA_CP)) {

						// resultado =
						// detalleordenDao.selectDetalleTransferencia(idServicio,
						// idOrden, bpag);

						resultado = detalleordenDao.selectDetalleTransferencia(
								idServicio, idOrden);

						if (resultado != null && resultado.size() > 0) {
							lblColumnas.add("Id Orden");
							lblColumnas.add("Id Registro");
							lblColumnas.add("Cuenta de Cargo");
							lblColumnas.add("Cuenta de Abono");
							lblColumnas.add("Moneda");
							lblColumnas.add("Importe");
							lblColumnas.add("Referencia");
							lblColumnas.add("Estado");
							lblColumnas.add("Código Respuesta");
							lblColumnas.add("Fecha Proceso");
							for (int i = 0; i < resultado.size(); i++) {
								bean = (BeanDetalleOrden) resultado.get(i);
								lstData
										.add(new String[] {
												bean.getM_IdOrden(),
												bean.getM_IdDetalleOrden(),
												bean.getM_CtaCargo(),
												bean.getM_CtaAbono(),
												bean.getM_DescTipoMoneda(),
												bean.getM_Monto(),
												bean.getM_Referencia(),
												bean.getM_DescEstado(),
												bean.getM_CodigoRptaIbs()
														+ " - "
														+ bean
																.getM_descripcionCodIbs(),
												bean.getM_FechaProceso() });
							}
							request.setAttribute("listaDetalleTransf_P",
									resultado);
						}
					} else if (tso.getId().getCsoproceso().equalsIgnoreCase(
							Constantes.HQL_PROCESO_TRANSFERENCIA_CT)) {

						// resultado =
						// detalleordenDao.selectDetalleTransferencia(idServicio,
						// idOrden, bpag);
						resultado = detalleordenDao.selectDetalleTransferencia(
								idServicio, idOrden);

						if (resultado != null && resultado.size() > 0) {
							lblColumnas.add("Id Orden");
							lblColumnas.add("Id Registro");
							lblColumnas.add("Cuenta de Cargo");
							lblColumnas.add("Cuenta de Abono CCI");
							lblColumnas.add("Moneda");
							lblColumnas.add("Importe");
							lblColumnas.add("Referencia");
							lblColumnas.add("Estado");
							lblColumnas.add("Código Respuesta");
							lblColumnas.add("Fecha Proceso");
							for (int i = 0; i < resultado.size(); i++) {
								bean = (BeanDetalleOrden) resultado.get(i);
								lstData
										.add(new String[] {
												bean.getM_IdOrden(),
												bean.getM_IdDetalleOrden(),
												bean.getM_CtaCargo(),
												bean.getM_CtaAbonoCci(),
												bean.getM_DescTipoMoneda(),
												bean.getM_Monto(),
												bean.getM_Referencia(),
												bean.getM_DescEstado(),
												bean.getM_CodigoRptaIbs()
														+ " - "
														+ bean
																.getM_descripcionCodIbs(),
												bean.getM_FechaProceso() });
							}
							request.setAttribute("listaDetalleTransf_T",
									resultado);
						}
					} else if (tso.getId().getCsoproceso().equalsIgnoreCase(
							Constantes.HQL_PROCESO_TRANSFERENCIA_I)) {
						// resultado =
						// detalleordenDao.selectDetalleTransferencia(idServicio,
						// idOrden, bpag);
						resultado = detalleordenDao.selectDetalleTransferencia(
								idServicio, idOrden);

						if (resultado != null && resultado.size() > 0) {
							lblColumnas.add("Id Orden");
							lblColumnas.add("Id Registro");
							lblColumnas.add("Cuenta de Cargo");
							lblColumnas.add("Cuenta de Abono CCI");
							lblColumnas.add("Moneda");
							lblColumnas.add("Importe");
							lblColumnas.add("Referencia");
							lblColumnas.add("Estado");
							lblColumnas.add("Código Respuesta");
							lblColumnas.add("Tipo Doc");
							lblColumnas.add("Número Doc");
							lblColumnas.add("Nombre");
							lblColumnas.add("Dirección");
							lblColumnas.add("Teléfono");
							lblColumnas.add("Fecha Proceso");

							for (int i = 0; i < resultado.size(); i++) {
								bean = (BeanDetalleOrden) resultado.get(i);
								lstData
										.add(new String[] {
												bean.getM_IdOrden(),
												bean.getM_IdDetalleOrden(),
												bean.getM_CtaCargo(),
												bean.getM_CtaAbonoCci(),
												bean.getM_DescTipoMoneda(),
												bean.getM_Monto(),
												bean.getM_Referencia(),
												bean.getM_DescEstado(),
												bean.getM_CodigoRptaIbs()
														+ " - "
														+ bean
																.getM_descripcionCodIbs(),
												bean.getM_TipoDocBenef(),
												bean.getM_NumDocBenef(),
												bean.getM_NombreBenef(),// jmoreno
																		// 07-09-09
												bean.getM_DirBenef(),
												bean.getM_TelefBenef(),
												bean.getM_FechaProceso() });
							}
							request.setAttribute("listaDetalleTransf_I",
									resultado);
						}
					}
					break;
				}
			}
		} else if (Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV
				.equalsIgnoreCase(ta[1])) {

			// resultado = detalleordenDao.selectDetallePago(idServicio,
			// idOrden, bpag);
			resultado = detalleordenDao.selectDetallePago(idServicio, idOrden);

			if (resultado != null && resultado.size() > 0) {
				lblColumnas.add("Id Orden");
				lblColumnas.add("Id Registro");
				lblColumnas.add("Cuenta");
				lblColumnas.add("Moneda");
				lblColumnas.add("Importe");
				lblColumnas.add("Referencia");
				lblColumnas.add("Estado");
				lblColumnas.add("Código Respuesta");
				lblColumnas.add("Recibo");
				lblColumnas.add("Orden Ref.");
				lblColumnas.add("Detalle Ref.");
				lblColumnas.add("Fecha Proceso");
				for (int i = 0; i < resultado.size(); i++) {
					bean = (BeanDetalleOrden) resultado.get(i);
					lstData.add(new String[] {
							bean.getM_IdOrden(),
							bean.getM_IdDetalleOrden(),
							bean.getM_NumeroCuenta(),
							bean.getM_DescTipoMoneda(),
							bean.getM_Monto(),
							bean.getM_Referencia(),
							bean.getM_DescEstado(),
							bean.getM_CodigoRptaIbs() + " - "
									+ bean.getM_descripcionCodIbs(),
							bean.getM_Documento(), bean.getM_OrdenRef(),
							bean.getM_DetalleOrdenRef(),
							bean.getM_FechaProceso() });
				}
				request.setAttribute("listaDetallePagoServicio", resultado);
			}
		} // para letras
		else if (Constantes.TX_CASH_TIPO_SERVICIO_LETRAS
				.equalsIgnoreCase(ta[1])) {

			// resultado = detalleordenDao.selectDetallePago(idServicio,
			// idOrden, bpag);
			resultado = detalleordenDao.selectDetallePago(idServicio, idOrden);

			if (resultado != null && resultado.size() > 0) {
				lblColumnas.add("Id Orden");
				lblColumnas.add("Id Registro");
				lblColumnas.add("Cuenta");
				lblColumnas.add("Moneda");
				lblColumnas.add("Principal");
				lblColumnas.add("Importe");
				lblColumnas.add("Estado");
				lblColumnas.add("Código Respuesta");
				lblColumnas.add("Aceptante");
				lblColumnas.add("Recibo");
				lblColumnas.add("Fecha Proceso");
				lblColumnas.add("Moneda Mora");
				lblColumnas.add("Mora");

				lblColumnas.add("Referencia");
				lblColumnas.add("Moneda ITF");
				lblColumnas.add("ITF");
				lblColumnas.add("Moneda Portes");
				lblColumnas.add("Portes");

				lblColumnas.add("Moneda Protesto");
				lblColumnas.add("Protesto");

				for (int i = 0; i < resultado.size(); i++) {
					bean = (BeanDetalleOrden) resultado.get(i);
					lstData.add(new String[] {
							bean.getM_IdOrden(),
							bean.getM_IdDetalleOrden(),
							bean.getM_NumeroCuenta(),
							bean.getM_DescTipoMoneda(),
							bean.getM_Principal(),
							bean.getM_Monto(),
							bean.getM_DescEstado(),
							bean.getM_CodigoRptaIbs() + " - "
									+ bean.getM_descripcionCodIbs(),
							bean.getM_CodAceptante(), bean.getM_Documento(),
							bean.getM_FechaProceso(), bean.getM_MonedaMora(),
							bean.getM_MontoMora(), bean.getM_Referencia(),
							bean.getM_DescMonedaITF(), bean.getM_ITF(),
							bean.getM_DescMonedaPortes(), bean.getM_Portes(),
							bean.getM_MonedaProtesto(), bean.getM_Protesto() });
				}
				request.setAttribute("listaDetallePagoLetras", resultado);
			}
		}

		if (resultado == null || resultado.size() < 1) {
			request.setAttribute("mensaje", "No se encontraron resultados");
			return mapping.findForward("exportarDetOrdenes");
		}

		// verificamos si se quiere exportar a formato de texto
		String accion = request.getParameter("accion");
		String formato = request.getParameter("formato");
		if ("save".equalsIgnoreCase(accion) && "txt".equalsIgnoreCase(formato)) { // descarga
																					// texto
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String nombre_archivo = "consulta-" + sdf.format(new Date())
					+ ".txt";
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ nombre_archivo + "\"");
			response.setContentType("text/plain");
			// creamos la cadena de texto a descargar
			StringBuilder strBuilder = new StringBuilder();

			// jwong 16/08/2009 colocamos titulo al reporte generado
			strBuilder.append("\t\t\tDETALLE DE ORDEN\r\n\r\n");

			String lblAux = null;
			int anchoCol = 0;

			for (int g = 0; g < lblColumnas.size(); g++) {
				// 1ero creamos los titulos de las columnas
				// jwong 16/08/2009 comentado para alineacion de columnas

				if (g < 2) { // aplicable a las 2 primeras columnas
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

			// cabecera
			/*
			 * for(int g=0; g<lblColumnas.size(); g++){ //1ero creamos los
			 * titulos de las columnas //jwong 16/08/2009 comentado para
			 * alineacion de columnas
			 * //strBuilder.append((String)lblColumnas.get(g));
			 * 
			 * if(g<2){ //aplicable a las 2 primeras columnas anchoCol = 16; }
			 * else{ anchoCol = 40; } lblAux = (String)lblColumnas.get(g);
			 * strBuilder.append(Util.ajustarDato(lblAux, anchoCol));
			 * if(g==lblColumnas.size()-1){ strBuilder.append("\r\n"); } }
			 */// jmoreno 09-09-09

			// data
			for (int h = 0; h < lstData.size(); h++) {
				String fila[] = (String[]) lstData.get(h);
				for (int k = 0; k < fila.length; k++) {
					// jwong 16/08/2009 comentado para alineacion de columnas
					// strBuilder.append((String)fila[k]);
					if (k < 2) { // aplicable a las 2 primeras columnas
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

			return null;
		} else if ("save".equalsIgnoreCase(accion)
				&& "excel".equalsIgnoreCase(formato)) { // descarga excel
			// se realizara la descarga usando POI
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String nombre_archivo = "consulta-" + sdf.format(new Date())
					+ ".xls";
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ nombre_archivo + "\"");
			response.setContentType("application/vnd.ms-excel");

			HSSFWorkbook libroXLS = GeneradorPOI.crearExcel(nombre_archivo,
					lblColumnas, lstData, null, "DETALLE DE ORDEN");
			if (libroXLS != null) {
				libroXLS.write(response.getOutputStream());
				response.getOutputStream().close();
				response.getOutputStream().flush();
			}
			return null;
		} else { // se enviara a pagina para imprimir
			return mapping.findForward("exportarDetOrdenes");
		}
	}

	// jwong 21/01/2009 detalle del importe de la orden
	public ActionForward detalleImporteOrdenes(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		// obtenemos las llaves para obtener el detalle de la porden
		String idOrden = request.getParameter("m_IdOrden");
		String idServicio = request.getParameter("m_IdServicio");

		// jwong 03/06/2009 tenemos que pasar de html a la cadena equivalente
		String referencia = Util.htmltoString(request
				.getParameter("m_Referencia"));
		String estado = Util.htmltoString(request
				.getParameter("m_DescripEstado"));

		// obtenemos el resultado de la consulta proveniente de base de datos
		BeanDetalleImporteEstado beanDetImportEstado = null;
		TpDetalleOrdenDao detalleordenDao = new TpDetalleOrdenDaoHibernate();
		// resultado = ordenDao.select(m_Empresa, listaServicios, listaEstados,
		// m_FecInicio, m_FecFin);
		// resultado = ordenDao.select(m_Empresa, listaServicios, m_Estado);
		beanDetImportEstado = detalleordenDao.selectDetImporteXEstado(
				idServicio, idOrden);

		// si la respuesta es exitosa
		if (beanDetImportEstado != null) {
			// enviamos a pagina el listado resultante
			beanDetImportEstado.setM_DetReferencia(referencia);
			beanDetImportEstado.setM_DetEstado(estado);
			beanDetImportEstado.setM_DetIDOrden(idOrden);
			request.setAttribute("beanDetImportEstado", beanDetImportEstado);
			request.setAttribute("back",
					"comprobantes.do?do=buscarOrdenesLinea");
		} else {
			// deberiamos retornar a la pagina con un mensaje de error
			request.setAttribute("mensaje", "No se encontraron resultados");
			request.setAttribute("back",
					"comprobantes.do?do=buscarOrdenesLinea");
			// return mapping.findForward("error");
		}
		return mapping.findForward("cargarDetalleOrdenes");
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

		List tiposervicio = (List) session.getAttribute("listaTipo");
		String empresa = ((ConsultaOrdenesForm) form).getM_Empresa();

		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();
		List servicios = servicioDAO.selectServicioxEmpresaxTipoxEstado(
				empresa, tiposervicio,
				Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO);// jmoreno
																	// 12/11/09
		session.setAttribute("listaServicios", servicios);
		String tipo = request.getParameter("tipo");
		if ("O".equalsIgnoreCase(tipo)) {
			return mapping.findForward("cargarConsultasOrdenes");
		} else {
			return mapping.findForward("cargarConsultaDetalles");
		}

	}

	// jmoreno 25-08-09 Para el manejo de los codigos de error para las ordenes
	// de Pago y Pago de Servicios

	public ActionForward obtenerCodError(ActionMapping mapping,
			ActionForm inForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// creamos la cabecera del xml
		String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "\n";
		xml = xml + "<respuestas>" + "\n";
		TaOrdenDao ordenDao = new TaOrdenDaoHibernate();
		String descripcion = ordenDao.getCodigosRptaIbs((String) request
				.getParameter("idOrden"));
		if (descripcion != null && !descripcion.trim().equals("")) { // si ya
																		// existe
																		// un
																		// codigo
																		// de
																		// entidad
																		// para
																		// esa
																		// empresa
			xml = xml + "<respuesta valor=\"Cargos Realizados\n" + descripcion
					+ "\" />" + "\n";
		} else {
			xml = xml
					+ "<respuesta valor=\"No se registraron codigos de error\" />";
		}
		xml = xml + "</respuestas>" + "\n";

		// Escribimos el xml al flujo del response
		response.setContentType("application/xml");
		// para que el navegador no utilice su cache para mostrar los datos
		response.addHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.addHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
												// server
		// escribimos el resultado en el flujo de salida
		PrintWriter out = response.getWriter();
		out.println(xml);
		out.flush();

		return null; // no retornamos nada
	}

	/**
	 * Método que permite cargar la interfaz para la consulta de movimientos
	 */
	public ActionForward cargarConsultaDetalles(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session
				.getAttribute("usuarioActual");
		// si termino la session debemos retornar al inicio
		if (beanDataLogXML == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		// Autorizacion
		String habil = request.getParameter("habil");
		if (habil == null || (habil != null && "1".equals(habil.trim()))) {
			session.setAttribute("habil", "1");
		} else {
			session.setAttribute("habil", "0");
			return mapping.findForward("cargarConsultasOrdenes");
		}
		session.removeAttribute("beanPag");
		// cargamos las empresas asociadas resultante del logueo
		List lEmpresa = (List) session.getAttribute("empresa");// null;
		
		String numTarjeta= (String) session.getAttribute("tarjetaActual");
		
		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
		
		boolean swverifica=empresaDAO.verificaSiTarjetaCash(numTarjeta);
		
		if (beanDataLogXML.isM_usuarioEspecial()) {// verificamos si el usuario
													// actual es Especial(Emp.
													// Operaciones Cash)
			lEmpresa = (List) session.getAttribute("empresa");// obtenemos la
																// lista de
																// empresas de
																// la session
			TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
			// a partir de lEmpresas, obtenemos la lista de empresas que estan
			// afiliadas al servicio
			lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa,
					Constantes.TX_CASH_SERVICIO_COMPROBANTES_ORDEN);
			if (lEmpresa.size() == 0) {
				request.setAttribute("mensaje",
						"El servicio no se encuentra afiliado");
				return mapping.findForward("error");
			}
		} else {
			// hMapEmpresas contiene las empresas con sus respectivos servicios
			// afiliados
			HashMap hMapEmpresas = (HashMap) session.getAttribute("hmEmpresas");
			// obtenemos la lista de empresas que estan afiliadas al servicio,
			// segun la data obtenida del logeo en el hashMap
			lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas,
					Constantes.TX_CASH_SERVICIO_COMPROBANTES_ORDEN);
			if (lEmpresa.size() == 0) {
				request.setAttribute("mensaje",
						"El servicio no se encuentra afiliado");
				return mapping.findForward("error");
			}
		}
		// obtenemos los datos de la empresa que resulto al logearnos
		
		List listaEmpresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);
		String cEmpresa = ((TmEmpresa) listaEmpresas.get(0)).getCemIdEmpresa();

		// obtenemos el listado de servicios relacionados con la empresa
		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();
		List tiposervicio = new ArrayList();
		tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_PAGO);
		tiposervicio.add(Constantes.TX_CASH_TIPO_SERVICIO_COBRO);
		List listaServicios = servicioDAO.selectServicioxEmpresaxTipoxEstado(
				cEmpresa, tiposervicio,
				Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO);// jmoreno
																	// 12/11/09

		// obtenemos el listado de estados de las ordenes
		TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
		List listaEstadosDetOrd = listFieldDAO
				.selectListFieldByFieldName(Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN);
		listaEstadosDetOrd = filtrarEstadosProcesados(listaEstadosDetOrd);

		// obtenemos la fecha actual
		String fechaActual = Fecha.getFechaActual("dd/MM/yyyy");
		// Obtenemos la fecha actual con un formato para comparar en pagina
		String fechaActualComp = Fecha.getFechaActual("yyyyMMdd");

		// jwong 03/06/2009 obtenemos la fecha hace un año
		String fechaHace1Anho = Fecha.getFechaCustom("yyyyMMdd",
				java.util.Calendar.YEAR, -1);
		session.setAttribute("fechaHace1Anho", fechaHace1Anho);

		session.setAttribute("listaEmpresas", listaEmpresas);
		session.setAttribute("listaServicios", listaServicios);
		session.setAttribute("listaEstadosDetOrd", listaEstadosDetOrd);
		session.setAttribute("listaTipo", tiposervicio);
		session.setAttribute("fechaActualComp", fechaActualComp);

		// obtenemos el formulario
		ConsultaOrdenesForm consultaOrdenesForm = (ConsultaOrdenesForm) form;
		// reseteamos los valores del form
		consultaOrdenesForm.reset(mapping, request);
		consultaOrdenesForm.setM_Empresa(null);
		consultaOrdenesForm.setM_Servicio(null);
		consultaOrdenesForm.setM_Estado(null);
		// seteamos la fecha actual para ser mostrada en la pagina
		consultaOrdenesForm.setM_FecInicio(fechaActual);
		consultaOrdenesForm.setM_FecFin(fechaActual);
		consultaOrdenesForm.setM_ConsReferencia(null);
		consultaOrdenesForm.setM_ConsContrapartida(null);

		return mapping.findForward("cargarConsultaDetalles");
	}

	/**
	 * Filtra los estados a solo los procesados
	 * 
	 * @param listaEstadosTotales
	 */
	private List<TxListField> filtrarEstadosProcesados(List listaEstadosTotales) {
		List<TxListField> estadosExitosos = new ArrayList<TxListField>();
		for (TxListField campo : (List<TxListField>) listaEstadosTotales) {
			if (EstadoCash.PROCESADO.getCodigo().equals(
					campo.getId().getClfCode())
					|| EstadoCash.ARCHIVADO.getCodigo().equals(
							campo.getId().getClfCode())
					|| EstadoCash.COBRADO.getCodigo().equals(
							campo.getId().getClfCode())
					|| EstadoCash.EN_PROCESO.getCodigo().equals(
							campo.getId().getClfCode())
					|| EstadoCash.ERRADO.getCodigo().equals(
							campo.getId().getClfCode())) {
				estadosExitosos.add(campo);
			}
		}
		return estadosExitosos;
	}

	/**
	 * Método que realiza la búsqueda de movimientos(o detalles de orden) de
	 * acuerdo a los parametros especificados por el usuario
	 */
	public ActionForward buscarDetallesLinea(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		logger.info("Inicio de buscarDetallesLinea()");
		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("usuarioActual") == null) {
				response.sendRedirect("cierraSession.jsp");
				return null;
			}
			ConsultaOrdenesForm consultaOrdenesForm = (ConsultaOrdenesForm) form;
			String servicio = consultaOrdenesForm.getM_Servicio();
			String estado = consultaOrdenesForm.getM_Estado();
			String fecinicio = consultaOrdenesForm.getM_FecInicio();
			String fecfin = consultaOrdenesForm.getM_FecFin();
			String referencia = consultaOrdenesForm.getM_ConsReferencia();
			String contrapartida = consultaOrdenesForm.getM_ConsContrapartida();
			String tipoPaginado = request.getParameter("tipoPaginado");
			BeanPaginacion bpag = null;
			BeanTotalesConsMov bTotConsMov = null;
			BeanTotalesConsMov bTotConsMov2 = null;
			if (tipoPaginado == null) {// verificamos si es la primera vez que
				// se realiza la consulta
				MessageResources messageResources = getResources(request);
				int nroRegPag = Integer.parseInt(messageResources
						.getMessage("paginacion.movimientos"));
				int cantItems =0;				
				if ("".equals(referencia) && "".equals(contrapartida)) {					
					bTotConsMov = comprobanteService.obtenerMontosMovimientos(
							servicio, estado, fecinicio, fecfin, referencia,
							contrapartida);
					cantItems = (int) bTotConsMov.getNumeroItems();					
				} else {
					cantItems=comprobanteService.obtenerCantidadMovimientos(servicio, estado, fecinicio, fecfin, referencia, contrapartida);
					bTotConsMov=null;
				}
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
				bpag.setCantidadRegistrosTotales(cantItems);
			} else {// si ya se realizo la consulta y solo se desea navegar por
				// la paginacion
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
				bTotConsMov = (BeanTotalesConsMov) session
						.getAttribute("beanTotalConsultaMovimientos");
			}
			session.setAttribute("beanPag", bpag);
			session.setAttribute("beanTotalConsultaMovimientos", bTotConsMov);

			List<BeanMovimiento> movimientos = null;
			if (bpag != null && bpag.getCantidadRegistrosTotales() > 0) {
				movimientos = comprobanteService.consultarMovimientos(servicio,
						estado, fecinicio, fecfin, referencia, contrapartida,
						bpag);
				if(bTotConsMov!=null){
					bTotConsMov2 = comprobanteService.obtenerMontosMovimientos(
						servicio, estado, fecinicio, fecfin, referencia,
						contrapartida);
				}
			}
			if (movimientos != null && movimientos.size() > 0) {
				request.setAttribute("bandResult", "si");
				request.setAttribute("beanTotalConsulta", bTotConsMov2);
				String tipoServicio = comprobanteService
						.obtenerTipoServicio(servicio);
				if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO
						.equalsIgnoreCase(tipoServicio)) {
					request.setAttribute("listaResultPago", movimientos);
				} else if (Constantes.TX_CASH_TIPO_SERVICIO_COBRO
						.equalsIgnoreCase(tipoServicio)) {
					request.setAttribute("listaResultCobro", movimientos);
				}
			} else {
				request.setAttribute("mensaje", "No se encontraron resultados");
			}
			return mapping.findForward("cargarConsultaDetalles");
		} catch (Exception e) {
			logger.error("Error procesando la accion", e);
			return mapping.findForward("error");
		}
	}

	/**
	 * Método que realiza la exportación de la consulta de movimientos(por
	 * página)
	 */
	public ActionForward exportarConsultaDetalles(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		ConsultaOrdenesForm consultaOrdenesForm = (ConsultaOrdenesForm) form;
		ActionForward forward = null;
		try {
			String servicioEmpresa = consultaOrdenesForm.getM_Servicio();
			String estado = consultaOrdenesForm.getM_Estado();
			String fecinicio = consultaOrdenesForm.getM_FecInicio();
			String fecfin = consultaOrdenesForm.getM_FecFin();
			String referencia = consultaOrdenesForm.getM_ConsReferencia();
			String contrapartida = consultaOrdenesForm.getM_ConsContrapartida();

			String accion = request.getParameter("accion");
			String formato = request.getParameter("formato");

			StringBuilder separador = new StringBuilder();
			List lblColumnas = new ArrayList();
			List listaRegistros = comprobanteService
					.seleccionarMovimientosExportacion(servicioEmpresa, estado,
							fecinicio, fecfin, referencia, contrapartida,
							formato, separador, lblColumnas);
			boolean totalRegistrosSeleccionados = false;
			if (listaRegistros!= null && listaRegistros.get(listaRegistros.size() - 1) instanceof String) {
				String lastRecord = (String) listaRegistros.get(listaRegistros
						.size() - 1);
				if ("1".equals(lastRecord)) {
					totalRegistrosSeleccionados = true;
				}
				listaRegistros.remove(listaRegistros.size() - 1);
			}
			forward = null;
			if (listaRegistros != null && listaRegistros.size() > 0) {

				if ("save".equalsIgnoreCase(accion)
						&& "txt".equalsIgnoreCase(formato)) { // descarga texto
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMdd_HHmmss");
					String nombre_archivo = "ConsMov-" + sdf.format(new Date())
							+ ".txt";
					response.setHeader("Content-Disposition",
							"attachment; filename=\"" + nombre_archivo + "\"");
					response.setContentType("text/plain");
					// creamos la cadena de texto a descargar
					StringBuilder strBuilder = new StringBuilder();
					String separadorDato = separador.toString();
					if ("TAB".equals(separadorDato)) {
						separadorDato = "\t";
					}
					String lblAux = null;
					List lFila = null;
					String lblTit = null;
					String aux[] = null;
					// si el formato tiene separador entonces ya no completamos
					// los
					// datos a la long. del campo
					if (separadorDato != null && !"".equals(separadorDato)) {
						for (int h = 0; h < listaRegistros.size(); h++) {
							lFila = (List) listaRegistros.get(h);
							for (int k = 0; k < lFila.size(); k++) {
								lblAux = (String) lFila.get(k);
								lblTit = (String) lblColumnas.get(k);
								aux = lblTit.split(",");
								if (k == lFila.size() - 1) {
									strBuilder.append(lblAux + "\r\n");
								} else {
									strBuilder.append(lblAux + separadorDato);
								}
							}
						}
					} else {// si el formato no tiene separador entonces
						// completamos
						// los datos a la long. del campo
						for (int h = 0; h < listaRegistros.size(); h++) {
							lFila = (List) listaRegistros.get(h);
							for (int k = 0; k < lFila.size(); k++) {
								lblAux = (String) lFila.get(k);
								lblTit = (String) lblColumnas.get(k);
								aux = lblTit.split(",");
								if (k == lFila.size() - 1) {
									strBuilder.append(Util.ajustarDato(lblAux,
											Integer.parseInt(aux[1]))
											+ "\r\n");
								} else {
									strBuilder.append(Util.ajustarDato(lblAux,
											Integer.parseInt(aux[1]))
											+ separadorDato);
								}
							}
						}
					}

					PrintWriter out = new PrintWriter(response
							.getOutputStream());
					out.println(strBuilder);
					out.flush();
					out.close();

					response.getOutputStream().flush();
					response.getOutputStream().close();
				} else if ("save".equalsIgnoreCase(accion)
						&& "excel".equalsIgnoreCase(formato)) {
					HSSFWorkbook libroXLS = GeneradorPOI.crearExcel(
							"Consulta de Movimientos", lblColumnas,
							listaRegistros, "Consulta de Movimientos",
							!totalRegistrosSeleccionados);

					if (libroXLS != null) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMdd_HHmmss");
						String nombre_archivo = "ConsMov-"
								+ sdf.format(new Date()) + ".xls";
						response.setHeader("Content-Disposition",
								"attachment; filename=\"" + nombre_archivo
										+ "\"");
						response.setContentType("application/vnd.ms-excel");
						libroXLS.write(response.getOutputStream());
						response.getOutputStream().close();
						response.getOutputStream().flush();
					}
				} else { // se enviara a pagina para imprimir
					forward = mapping.findForward("exportarDetOrdenes");
				}
			}
		} catch (Exception e) {
			logger.error("Ocurrio un error procesando la accion", e);
			forward = mapping.findForward("error");
		}
		return forward;
	}

	/**
	 * Método que verifica si el servicio(idServEmp) tiene asociado un formato
	 * de salida para realizar la exportación de consulta de movimientos
	 */
	public ActionForward verificarFormatoSalida(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "\n";
		xml = xml + "<respuestas>" + "\n";
		String idServEmp = ((ConsultaOrdenesForm) form).getM_Servicio();
		TaServicioxEmpresaDao dao = new TaServicioxEmpresaDaoHibernate();
		if (idServEmp != null && !"".equals(idServEmp)) {
			int codigo = dao.selectCodFormatoOut(Long.parseLong(idServEmp));
			if (codigo > 0) {
				xml = xml + "<respuesta valor='SI'/>" + "\n";// si tiene
																// asociado un
																// formato el
																// servicioEmpresa
			} else {
				xml = xml + "<respuesta valor='NO'/>" + "\n";// no tiene
																// asociado un
																// formato el
																// servicioEmpresa
			}
			xml = xml + "</respuestas>" + "\n";
		}

		// Escribimos el xml al flujo del response
		response.setContentType("application/xml");
		// para que el navegador no utilice su cache para mostrar los datos
		response.addHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.addHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
												// server
		// escribimos el resultado en el flujo de salida
		PrintWriter out = response.getWriter();
		out.println(xml);
		out.flush();
		return null;
	}

	/**
	 * Método que realiza la consulta de las órdenes que fueron consultadas para
	 * mostrar la consulta de movimientos
	 */
	public ActionForward mostrarConsEstado(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		ConsultaOrdenesForm consultaOrdenesForm = (ConsultaOrdenesForm) form;
		String servicio = consultaOrdenesForm.getM_Servicio();
		String estado = consultaOrdenesForm.getM_Estado();
		String fecini = consultaOrdenesForm.getM_FecInicio();
		String fecfin = consultaOrdenesForm.getM_FecFin();
		String referencia = consultaOrdenesForm.getM_ConsReferencia();
		String contrapartida = consultaOrdenesForm.getM_ConsContrapartida();
		TaOrdenDao dao = new TaOrdenDaoHibernate();
		BeanTotalesConsMov beanConsTot = new BeanTotalesConsMov();
		List listaOrdenes = comprobanteService.obtenerConsultaDeEstadoOrdenes(
				servicio, estado, fecini, fecfin, referencia, contrapartida,
				beanConsTot); // dao.getConsultaOrdEstado(servicio, estado,
								// fecini, fecfin, referencia, contrapartida,
								// beanConsTot);
		if (listaOrdenes != null && listaOrdenes.size() > 0) {
			request.setAttribute("listaResult", listaOrdenes);
			request.setAttribute("beanTotalConsulta", beanConsTot);
		}
		String goTo = request.getParameter("exportar");
		if ("si".equalsIgnoreCase(goTo)) {
			goTo = "exportarConsEstado";
		} else {
			goTo = "mostrarConsEstado";
		}
		return mapping.findForward(goTo);
	}

	public ActionForward iniciarDetallesOrden(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		try {
			HttpSession session = request.getSession();
			if (session.getAttribute("usuarioActual") == null) {
				response.sendRedirect("cierraSession.jsp");
				return null;
			} else {
				String strOrden = request.getParameter("m_IdOrden");
				String idServicio = request.getParameter("m_IdServicio");
				long servicio = Long.parseLong(idServicio);
				long idOrden = Long.parseLong(strOrden);

				ConsultaOrdenesForm consultaOrdenesForm = (ConsultaOrdenesForm) form;
				String referencia = consultaOrdenesForm.getM_ConsReferencia();
				String tipo = consultaOrdenesForm.getTipoProcesamiento();

				Paginado<BeanDetalleOrden> paginado = null;
				TaServicioxEmpresaDao servempDao = new TaServicioxEmpresaDaoHibernate();

				String[] ta = servempDao.selectTipoServicioxEmpresa(servicio);
				List<BeanDetalleOrden> resultado = null;
				String tipoLista = "";
				if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO
						.equalsIgnoreCase(ta[1])
						|| Constantes.TX_CASH_TIPO_SERVICIO_COBRO
								.equalsIgnoreCase(ta[1])) {

					paginado = getPaginador(tipo,10, idOrden, servicio, referencia);

					resultado = paginado.getItemsPagina();
					if (resultado != null && resultado.size() > 0) {
						request.setAttribute("listaDetalle", resultado);
						tipoLista = "listaDetalle";
					}
					session.removeAttribute("comprobantePago");
					session.setAttribute("cDOIdOrden", strOrden);
					if( tipo.equals(CashConstants.ITEMS_PROCESADO)){
						session.setAttribute("tipoProcesamiento", "Procesados");						
					}else{
						session.setAttribute("tipoProcesamiento", "No Procesados");	
					}
				}
				if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO
						.equalsIgnoreCase(ta[1])) {
					session.setAttribute("comprobantePago", "comprobantePago");
				} else if (Constantes.TX_CASH_TIPO_SERVICIO_TRANSFERENCIAS
						.equalsIgnoreCase(ta[1])) {
					TransferenciasDelegate delegado = TransferenciasDelegate
							.getInstance();
					MessageResources messageResources = getResources(request);

					List<BeanSuccessDetail> alsuccess = delegado
							.getDetallesImpresion(
									CashConstants.COD_MODULO_COMPROBANTES,
									messageResources, idOrden, servicio);
					request.setAttribute("alsuccess", alsuccess);
					request.setAttribute("orden", idOrden);
					request.setAttribute("servicio", servicio);
					return mapping.findForward("transferencia");
				} else if (Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV
						.equalsIgnoreCase(ta[1])) {
					PagoServicioComprobantes cps = new PagoServicioComprobantes(
							idOrden, servicio);
					request.setAttribute("orden", idOrden);
					request.setAttribute("servicio", servicio);
					if (cps.getTipoServicio().equals(CashConstants.SERV_AGUA)) {
						request.setAttribute("alsuccess", cps
								.obtenerDetallesImpresion());
						return mapping.findForward("pagoServicio");
					} else {
						if (cps.getTipoServicio().equals(
								CashConstants.SERV_TELEFONIA)
								|| cps.getTipoServicio().equals(
										CashConstants.SERV_LUZ)) {
							request.setAttribute("alsuccess", cps
									.obtenerDetallesImpresion());
							request.setAttribute("recibos", cps.getRecibos());
							return mapping.findForward("pagoServicio");
						} else {
							throw new Exception(
									"El tipo de pago de servicio seleccionado no puede ser procesado.");
						}

					}
				} else if (Constantes.TX_CASH_TIPO_SERVICIO_LETRAS
						.equalsIgnoreCase(ta[1])) {
					paginado = getPaginador(tipo,10, idOrden, servicio, referencia);
					resultado = paginado.getItemsPagina();
					if (resultado != null && resultado.size() > 0) {
						request.setAttribute("listaDetallePagoLetras",
								resultado);
						tipoLista = "listaDetallePagoLetras";
					}
				}
				if (paginado != null) {
					session.setAttribute("paginado", paginado);
					session.setAttribute("servicio", servicio);
					session.setAttribute("tipoLista", tipoLista);
				}
				request.setAttribute("back",
						"comprobantes.do?do=buscarOrdenesLinea");
				return mapping.findForward("paginadoDetallesOrden");
			}
		} catch (Exception e) {
			logger.error("Error en la Inicializacion del Paginado", e);
			request.setAttribute("mensaje", e.getMessage());
			return mapping.findForward("error");
		}
	}

	public ActionForward exportarDetallesOrden(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			HttpSession session = request.getSession();
			String tipoLista = (String) session.getAttribute("tipoLista");
			String comprobante = (String) session
					.getAttribute("comprobantePago");
			ArrayList lblColumnas = new ArrayList();
			ArrayList lstData = new ArrayList();
			List<BeanDetalleOrden> resultado = null;
			BeanDetalleOrden bean = null;
			Paginado<BeanDetalleOrden> paginado = (Paginado<BeanDetalleOrden>) session
					.getAttribute("paginado");
			resultado = paginado.getItems();
			int accion = Integer.valueOf(request.getParameter("accion"));
			if (accion == 0 || accion == 3) {
				request.setAttribute(tipoLista, resultado);
				if (resultado == null || resultado.size() < 1) {
					request.setAttribute("mensaje",
							"No se encontraron resultados");
				}
				return mapping.findForward("exportaDetallesOrden");
			} else {

				if (resultado != null && resultado.size() > 0) {
					if (tipoLista.equals("listaDetalle")) {
						lblColumnas.add("Id Orden");
						lblColumnas.add("Id Registro");
						lblColumnas.add("Tipo Cuenta");
						lblColumnas.add("Cuenta");
						lblColumnas.add("Tipo Doc");
						lblColumnas.add("Número Doc");
						lblColumnas.add("Nombre");
						lblColumnas.add("Referencia");
						lblColumnas.add("Moneda");
						lblColumnas.add("Importe");
						lblColumnas.add("Tipo Pago");
						lblColumnas.add("Estado");
						lblColumnas.add("Código Respuesta");
						lblColumnas.add("Fecha Proceso");
						lblColumnas.add("Nro Transacción");
						lblColumnas.add("Nro Cheque");
						if (comprobante != null
								&& comprobante.equals("comprobantePago")) {
							// Orden Cobro
							for (int i = 0; i < resultado.size(); i++) {
								bean = (BeanDetalleOrden) resultado.get(i);
								lstData
										.add(new String[] {
												bean.getM_IdOrden(),
												bean.getM_IdDetalleOrden(),
												bean.getM_DescTipoCuenta(),
												bean.getM_NumeroCuenta(),
												bean.getM_DescTipoDocumento(),
												bean.getM_Documento(),
												bean.getM_Nombre(),
												bean.getM_Referencia(),
												bean.getM_DescTipoMoneda(),
												bean.getM_Monto(),
												bean.getM_DescTipoPago(),
												bean.getM_DescEstado(),
												bean.getM_CodigoRptaIbs()
														+ " - "
														+ bean
																.getM_descripcionCodIbs(),
												bean.getM_FechaProceso(),
												bean.getM_IdPago(),
												bean.getM_NumCheque() });
							}
						} else {
							// Orden Pago
							lblColumnas.add("Comisión Cliente");
							lblColumnas.add("Comisión Empresa");
							for (int i = 0; i < resultado.size(); i++) {
								bean = (BeanDetalleOrden) resultado.get(i);
								lstData
										.add(new String[] {
												bean.getM_IdOrden(),
												bean.getM_IdDetalleOrden(),
												bean.getM_DescTipoCuenta(),
												bean.getM_NumeroCuenta(),
												bean.getM_DescTipoDocumento(),
												bean.getM_Documento(),
												bean.getM_Nombre(),
												bean.getM_Referencia(),
												bean.getM_DescTipoMoneda(),
												bean.getM_Monto(),
												bean.getM_DescTipoPago(),
												bean.getM_DescEstado(),
												bean.getM_CodigoRptaIbs()
														+ " - "
														+ bean
																.getM_descripcionCodIbs(),
												bean.getM_FechaProceso(),
												bean.getM_IdPago(),
												bean.getM_NumCheque(),
												bean.getM_montoComClienteChg(),
												bean.getM_montoComEmpresaChg() });
							}
						}
					} else {
						if (tipoLista.equals("listaDetalleTransf_P")) {
							lblColumnas.add("Id Orden");
							lblColumnas.add("Id Registro");
							lblColumnas.add("Cuenta de Cargo");
							lblColumnas.add("Cuenta de Abono");
							lblColumnas.add("Moneda");
							lblColumnas.add("Importe");
							lblColumnas.add("Referencia");
							lblColumnas.add("Estado");
							lblColumnas.add("Código Respuesta");
							lblColumnas.add("Fecha Proceso");
							for (int i = 0; i < resultado.size(); i++) {
								bean = (BeanDetalleOrden) resultado.get(i);
								lstData
										.add(new String[] {
												bean.getM_IdOrden(),
												bean.getM_IdDetalleOrden(),
												bean.getM_CtaCargo(),
												bean.getM_CtaAbono(),
												bean.getM_DescTipoMoneda(),
												bean.getM_Monto(),
												bean.getM_Referencia(),
												bean.getM_DescEstado(),
												bean.getM_CodigoRptaIbs()
														+ " - "
														+ bean
																.getM_descripcionCodIbs(),
												bean.getM_FechaProceso() });
							}
						} else if (tipoLista.equals("listaDetalleTransf_T")) {
							lblColumnas.add("Id Orden");
							lblColumnas.add("Id Registro");
							lblColumnas.add("Cuenta de Cargo");
							lblColumnas.add("Cuenta de Abono CCI");
							lblColumnas.add("Moneda");
							lblColumnas.add("Importe");
							lblColumnas.add("Referencia");
							lblColumnas.add("Estado");
							lblColumnas.add("Código Respuesta");
							lblColumnas.add("Fecha Proceso");
							for (int i = 0; i < resultado.size(); i++) {
								bean = (BeanDetalleOrden) resultado.get(i);
								lstData
										.add(new String[] {
												bean.getM_IdOrden(),
												bean.getM_IdDetalleOrden(),
												bean.getM_CtaCargo(),
												bean.getM_CtaAbonoCci(),
												bean.getM_DescTipoMoneda(),
												bean.getM_Monto(),
												bean.getM_Referencia(),
												bean.getM_DescEstado(),
												bean.getM_CodigoRptaIbs()
														+ " - "
														+ bean
																.getM_descripcionCodIbs(),
												bean.getM_FechaProceso() });
							}
						} else if (tipoLista.equals("listaDetalleTransf_I")) {
							lblColumnas.add("Id Orden");
							lblColumnas.add("Id Registro");
							lblColumnas.add("Cuenta de Cargo");
							lblColumnas.add("Cuenta de Abono CCI");
							lblColumnas.add("Moneda");
							lblColumnas.add("Importe");
							lblColumnas.add("Referencia");
							lblColumnas.add("Estado");
							lblColumnas.add("Código Respuesta");
							lblColumnas.add("Tipo Doc");
							lblColumnas.add("Número Doc");
							lblColumnas.add("Nombre");
							lblColumnas.add("Dirección");
							lblColumnas.add("Teléfono");
							lblColumnas.add("Fecha Proceso");

							for (int i = 0; i < resultado.size(); i++) {
								bean = (BeanDetalleOrden) resultado.get(i);
								lstData
										.add(new String[] {
												bean.getM_IdOrden(),
												bean.getM_IdDetalleOrden(),
												bean.getM_CtaCargo(),
												bean.getM_CtaAbonoCci(),
												bean.getM_DescTipoMoneda(),
												bean.getM_Monto(),
												bean.getM_Referencia(),
												bean.getM_DescEstado(),
												bean.getM_CodigoRptaIbs()
														+ " - "
														+ bean
																.getM_descripcionCodIbs(),
												bean.getM_TipoDocBenef(),
												bean.getM_NumDocBenef(),
												bean.getM_NombreBenef(),// jmoreno
												// 07-09-09
												bean.getM_DirBenef(),
												bean.getM_TelefBenef(),
												bean.getM_FechaProceso() });
							}
						} else if (tipoLista.equals("listaDetallePagoServicio")) {
							lblColumnas.add("Id Orden");
							lblColumnas.add("Id Registro");
							lblColumnas.add("Cuenta");
							lblColumnas.add("Moneda");
							lblColumnas.add("Importe");
							lblColumnas.add("Referencia");
							lblColumnas.add("Estado");
							lblColumnas.add("Código Respuesta");
							lblColumnas.add("Recibo");
							lblColumnas.add("Orden Ref.");
							lblColumnas.add("Detalle Ref.");
							lblColumnas.add("Fecha Proceso");
							for (int i = 0; i < resultado.size(); i++) {
								bean = (BeanDetalleOrden) resultado.get(i);
								lstData
										.add(new String[] {
												bean.getM_IdOrden(),
												bean.getM_IdDetalleOrden(),
												bean.getM_NumeroCuenta(),
												bean.getM_DescTipoMoneda(),
												bean.getM_Monto(),
												bean.getM_Referencia(),
												bean.getM_DescEstado(),
												bean.getM_CodigoRptaIbs()
														+ " - "
														+ bean
																.getM_descripcionCodIbs(),
												bean.getM_Documento(),
												bean.getM_OrdenRef(),
												bean.getM_DetalleOrdenRef(),
												bean.getM_FechaProceso() });
							}
						} else {
							// listaDetallePagoLetras
							if (tipoLista.equals("listaDetallePagoLetras")) {
								lblColumnas.add("Id Orden");
								lblColumnas.add("Id Registro");
								lblColumnas.add("Cuenta");
								lblColumnas.add("Moneda");
								lblColumnas.add("Principal");
								lblColumnas.add("Importe");
								lblColumnas.add("Estado");
								lblColumnas.add("Código Respuesta");
								lblColumnas.add("Aceptante");
								lblColumnas.add("Recibo");
								lblColumnas.add("Fecha Proceso");
								lblColumnas.add("Moneda Mora");
								lblColumnas.add("Mora");

								lblColumnas.add("Referencia");
								lblColumnas.add("Moneda ITF");
								lblColumnas.add("ITF");
								lblColumnas.add("Moneda Portes");
								lblColumnas.add("Portes");

								lblColumnas.add("Moneda Protesto");
								lblColumnas.add("Protesto");

								for (int i = 0; i < resultado.size(); i++) {
									bean = (BeanDetalleOrden) resultado.get(i);
									lstData
											.add(new String[] {
													bean.getM_IdOrden(),
													bean.getM_IdDetalleOrden(),
													bean.getM_NumeroCuenta(),
													bean.getM_DescTipoMoneda(),
													bean.getM_Principal(),
													bean.getM_Monto(),
													bean.getM_DescEstado(),
													bean.getM_CodigoRptaIbs()
															+ " - "
															+ bean
																	.getM_descripcionCodIbs(),
													bean.getM_CodAceptante(),
													bean.getM_Documento(),
													bean.getM_FechaProceso(),
													bean.getM_MonedaMora(),
													bean.getM_MontoMora(),
													bean.getM_Referencia(),
													bean.getM_DescMonedaITF(),
													bean.getM_ITF(),
													bean
															.getM_DescMonedaPortes(),
													bean.getM_Portes(),
													bean.getM_MonedaProtesto(),
													bean.getM_Protesto() });
								}
							}
						}
					}
					request.setAttribute(tipoLista, resultado);
				} else {
					request.setAttribute("mensaje",
							"No se encontraron resultados");
				}
				if (accion == 1) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMdd_HHmmss");
					String nombre_archivo = "Detalles" + sdf.format(new Date())
							+ ".xls";
					response.setHeader("Content-Disposition",
							"attachment; filename=\"" + nombre_archivo + "\"");
					response.setContentType("application/vnd.ms-excel");

					HSSFWorkbook libroXLS = GeneradorPOI.crearExcel(
							nombre_archivo, lblColumnas, lstData, null,
							"DETALLES DE ORDEN");
					if (libroXLS != null) {
						libroXLS.write(response.getOutputStream());
						response.getOutputStream().close();
						response.getOutputStream().flush();
					}
				} else {
					// accion 2
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMdd_HHmmss");
					String nombre_archivo = "Detalles-"
							+ sdf.format(new Date()) + ".txt";
					response.setHeader("Content-Disposition",
							"attachment; filename=\"" + nombre_archivo + "\"");
					response.setContentType("text/plain");

					StringBuilder strBuilder = new StringBuilder();

					strBuilder.append("\t\t\tDETALLE DE ORDEN\r\n\r\n");

					String lblAux = null;
					int anchoCol = 0;

					for (int g = 0; g < lblColumnas.size(); g++) {

						if (g < 2) {
							anchoCol = 16;
						} else {
							anchoCol = 50;
						}

						lblAux = (String) lblColumnas.get(g);
						if ("Código Respuesta".equalsIgnoreCase(lblAux)) {
							anchoCol = 82;
						}
						strBuilder.append(Util
								.ajustarDato(lblAux, anchoCol + 1));
						if (g == lblColumnas.size() - 1) {
							strBuilder.append("\r\n");
						}
					}

					for (int h = 0; h < lstData.size(); h++) {
						String fila[] = (String[]) lstData.get(h);
						for (int k = 0; k < fila.length; k++) {

							if (k < 2) {
								anchoCol = 16;
							} else {
								anchoCol = 50;
							}
							lblAux = (String) fila[k];
							String lblTit = (String) lblColumnas.get(k);
							if ("Código Respuesta".equalsIgnoreCase(lblTit)) {
								anchoCol = 82;
							}
							strBuilder.append(Util.ajustarDato(lblAux,
									anchoCol + 1));
							if (k == fila.length - 1) {
								strBuilder.append("\r\n");
							}
						}
					}

					PrintWriter out = new PrintWriter(response
							.getOutputStream());
					out.println(strBuilder);
					out.flush();
					out.close();

					response.getOutputStream().flush();
					response.getOutputStream().close();
				}
				return null;
			}

		} catch (Exception e) {
			logger.info("Error en la exportacion", e);
			return mapping.findForward("error");
		}
	}

	public ActionForward paginarDetallesOrden(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		try {
			if (session.getAttribute("usuarioActual") == null) {
				response.sendRedirect("cierraSession.jsp");
				return null;
			} else {
				String tipoLista = (String) session.getAttribute("tipoLista");
				String nroPagina = request.getParameter("nroPagina");
				List<BeanDetalleOrden> resultado = null;
				Paginado<BeanDetalleOrden> paginado = (Paginado<BeanDetalleOrden>) session
						.getAttribute("paginado");
				paginado.setNroPagina(Integer.valueOf(nroPagina));

				resultado = paginado.getItemsPagina();
				request.setAttribute(tipoLista, resultado);
				if (resultado == null || resultado.size() < 1) {
					request.setAttribute("mensaje",
							"No se encontraron resultados");
				}

				request.setAttribute("back",
						"comprobantes.do?do=buscarOrdenesLinea");
				return mapping.findForward("paginadoDetallesOrden");
			}
		} catch (Exception e) {
			logger.info("Error Construyendo el Paginado", e);
			return mapping.findForward("error");
		}
	}

	private Paginado<BeanDetalleOrden> getPaginador(final String tipo,int nroItemsPagina,
			final long orden, final long servicio, final String referencia) {
		Paginado<BeanDetalleOrden> paginador = new Paginado<BeanDetalleOrden>(
				nroItemsPagina) {

			@Override
			public List<BeanDetalleOrden> getItemsPagina() {
				try {
					return delegado.selectDetallePagoReferencia(tipo,orden, servicio,
							referencia, this.getItemInicioPagina(),
							this.nroItemsPagina);
				} catch (Exception e) {
					logger.error(e, e);
					return new ArrayList<BeanDetalleOrden>();
				}
			}

			@Override
			public int getNroTotalItems() {
				try {
					return delegado.countDetallePagoReferencia(tipo,orden, servicio,
							referencia);
				} catch (Exception e) {
					logger.error(e, e);
					return 0;
				}
			}

		};

		return paginador;
	}

	public ActionForward mostrarAprobadores(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}

		ConsultaOrdenesForm consultaOrdenesForm = (ConsultaOrdenesForm) form;

		long idOrden = Integer.parseInt(request.getParameter("codOrden"));
		long idServicioEmpresa = Integer.parseInt(request.getParameter("servemp"));		

		TaAprobacionOrdenDao dao = new TaAprobacionOrdenDaoHibernate();
		List listaAprobadores = dao.selectAprobadores(idOrden, idServicioEmpresa);

		if (listaAprobadores != null && listaAprobadores.size() > 0) {
			request.setAttribute("listaAprobadores", listaAprobadores);
		}

		return mapping.findForward("mostrarAprobadores");

	}

}
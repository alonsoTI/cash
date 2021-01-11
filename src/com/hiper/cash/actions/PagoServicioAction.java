package com.hiper.cash.actions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.financiero.cash.delegate.PagoServicioComprobantes;
import com.financiero.cash.delegate.PagoServicioDelegate;
import com.financiero.cash.delegate.SeguridadDelegate;
import com.financiero.cash.ui.model.ReciboPagoServicio;
import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.clienteWS.GenRequestXML;
import com.hiper.cash.dao.GetDataIBS;
import com.hiper.cash.dao.TaDetalleMapaCamposDao;
import com.hiper.cash.dao.TaEmpresaDeServicioDao;
import com.hiper.cash.dao.TaMapaCamposDao;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TaSecuencialDao;
import com.hiper.cash.dao.TaServicioOpcionDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.TxResultDao;
import com.hiper.cash.dao.hibernate.TaDetalleMapaCamposDaoHibernate;
import com.hiper.cash.dao.hibernate.TaEmpresaDeServicioDaoHibernate;
import com.hiper.cash.dao.hibernate.TaMapaCamposDaoHibernate;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaSecuencialDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioOpcionDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TpDetalleOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TxResultDaoHibernate;
import com.hiper.cash.dao.ws.ibs.GetDataIBSFinanciero;
import com.hiper.cash.domain.TaDetalleMapaCampos;
import com.hiper.cash.domain.TaMapaCampos;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.domain.TaOrdenId;
import com.hiper.cash.domain.TaServicioOpcion;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.domain.TpDetalleOrden;
import com.hiper.cash.domain.TpDetalleOrdenId;
import com.hiper.cash.domain.TxResult;
import com.hiper.cash.entidad.BeanDetalleOrden;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.BeanPagoServicio;
import com.hiper.cash.entidad.BeanServicio;
import com.hiper.cash.entidad.BeanSuccess;
import com.hiper.cash.entidad.BeanSuccessDetail;
import com.hiper.cash.forms.PagoServicioForm;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.CollectionFilter;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;
import com.hiper.cash.xml.bean.BeanAccount;
import com.hiper.cash.xml.bean.BeanConsCtasCliente;
import com.hiper.cash.xml.bean.BeanConsPagoServicio;
import com.hiper.cash.xml.bean.BeanDataLoginXML;
import com.hiper.cash.xml.bean.BeanNodoXML;
import com.hiper.cash.xml.bean.BeanRespuestaWSHomeBankingXML;
import com.hiper.cash.xml.bean.BeanRespuestaXML;
import com.hiper.cash.xml.bean.BeanTipoCambioXML;
import com.hiper.cash.xml.parser.ParserXML;

/**
 * 
 * @author jwong, esilva
 */
public class PagoServicioAction extends DispatchAction {

	private static Logger logger = Logger.getLogger(PagoServicioAction.class);

	private SeguridadDelegate delegadoSeguridad = SeguridadDelegate
			.getInstance();
	private PagoServicioDelegate delegadoServicio = PagoServicioDelegate
			.getInstance();

	private String idModulo = null;

	// SERVICIO PAGO ONLINE
	public ActionForward cargarPagoOnLine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
		// jmoreno 20-08-09
		String m_Grupo = request.getParameter("grupo"); // codigo del grupo
		String m_NombreGrupo = request.getParameter("nombre"); // nombre del
																// grupo
		String m_CodEntidad = request.getParameter("codentidad"); // codigo de
																	// entidad
		String m_Modulo = request.getParameter("modulo");
		String m_Submodulo = request.getParameter("submodulo");
		String data = "grupo=" + m_Grupo + "&nombre=" + m_NombreGrupo
				+ "&codentidad=" + m_CodEntidad + "&modulo=" + m_Modulo
				+ "&submodulo=" + m_Submodulo;
		request.setAttribute("data", data);

		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");

		// obtenemos la empresa proveedora del servicio seleccionada
		String m_Codigo = request.getParameter("m_Codigo"); // codigo de la
															// empresa
		String m_Nombre = null;// request.getParameter("m_Nombre"); //nombre de
								// la empresa
		String m_CodigoServicio = request.getParameter("m_CodigoServicio"); // codigo
																			// de
																			// servicio(codigo
																			// de
																			// entidad)

		List listaEmpProveedor = (List) session
				.getAttribute("listaEmpProveedor");
		BeanRespuestaWSHomeBankingXML beanWS = null;
		for (int h = 0; h < listaEmpProveedor.size(); h++) {
			beanWS = (BeanRespuestaWSHomeBankingXML) listaEmpProveedor.get(h);
			if (beanWS.getM_Codigo().equalsIgnoreCase(m_Codigo)) {
				m_Nombre = beanWS.getM_Nombre();
				break;
			}
		}

		PagoServicioForm pagoservfrm = (PagoServicioForm) form;
		pagoservfrm.setM_NumAbonado(null);
		pagoservfrm.setM_Sector(null);

		// cargamos las empresas asociadas resultante del logueo
		List lEmpresa = (List) session.getAttribute("empresa");
		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
		List empresas = empresaDAO.listarClienteEmpresa(lEmpresa);
		session.setAttribute("listaEmpresas", empresas);

		if (empresas != null && empresas.size() > 0) {
			session.setAttribute("nroEmpresas", empresas.size());
		} else {
			request.setAttribute("mensaje","No se pudo encontrar lista de empresas");
			return mapping.findForward("cargarPagoOnLine");
		}

		// Hibernate
		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();
		TaServicioOpcionDao servicio_opcionDAO = new TaServicioOpcionDaoHibernate();

		TaServicioOpcion taservicioopcion = servicio_opcionDAO.select(serv_pago
				.getM_Modulo(), serv_pago.getM_SubModulo());
		List servicios = servicioDAO.selectServicioxEmpresaxCode(
				((TmEmpresa) empresas.get(0)).getCemIdEmpresa(),
				taservicioopcion.getId().getCsoservicioId());

		List listaServicio = null;
		GetDataIBS gdi = new GetDataIBSFinanciero();
		List lparameters = new ArrayList();
		// parametro codigo de la empresa
		lparameters.add(new BeanNodoXML("idProveedor", m_Codigo));
		int ires = 0;
		CashClientWS cashclienteWS = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_HOME_BANKING_WS);
		cashclienteWS.setOperacion((String) context.getAttribute("CONS_SERV_Servicio_Operacion2_Nombre"));
		listaServicio = gdi.getServicios_Empresas_Servicio(cashclienteWS,lparameters, ires, m_Codigo);
		if (listaServicio != null && listaServicio.size() > 0) {
			// session.setAttribute("nomProveedor", m_Nombre);
			serv_pago.setM_NombreProveedor(m_Nombre);
			serv_pago.setM_Proveedor(m_Codigo);
			serv_pago.setM_CodServProveedor(m_CodigoServicio); // codigo de
																// entidad
			session.setAttribute("serv_pago", serv_pago);
			session.setAttribute("listaServicio", listaServicio);
			session.setAttribute("listaservicios", servicios);
		} else {
			request.setAttribute("mensaje",
					"No se pudo encontrar servicios para ese proveedor");
			return mapping.findForward("cargarPagoOnLine");
		}

		// verificamos si necesita seleccionar ubicacion
		String m_bSector = request.getParameter("sector");
		// removemos el listado de sectores
		session.removeAttribute("listaSector");
		if ("1".equalsIgnoreCase(m_bSector)) {
			List listaSector = null;
			List lparams = new ArrayList();
			// parametro codigo de la empresa
			lparams.add(new BeanNodoXML(Constantes.TAG_INTIDPADRE, m_Codigo));
			ires = 0;
			cashclienteWS = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_HOME_BANKING_WS);
			cashclienteWS.setOperacion((String) context.getAttribute("CONS_SERV_Servicio_Operacion3_Nombre"));
			listaSector = gdi.getSectores_Proveedor(cashclienteWS, lparams,ires);

			if (listaSector != null) {
				session.setAttribute("listaSector", listaSector);
			} else {
				request.setAttribute("mensaje","No se pudo encontrar localidades(sectores) para pagar ese servicio");
				return mapping.findForward("cargarPagoOnLine");
			}
		}

		return mapping.findForward("cargarPagoOnLine");
	}

	public ActionForward buscarPagoOnLine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}

		String m_Grupo = request.getParameter("grupo"); // codigo del grupo
		String m_NombreGrupo = request.getParameter("nombre"); // nombre del
																// grupo
		String m_CodEntidad = request.getParameter("codentidad"); // codigo de
																	// entidad
		String m_Modulo = request.getParameter("modulo");
		String m_Submodulo = request.getParameter("submodulo");
		String data = "grupo=" + m_Grupo + "&nombre=" + m_NombreGrupo
				+ "&codentidad=" + m_CodEntidad + "&modulo=" + m_Modulo
				+ "&submodulo=" + m_Submodulo;
		request.setAttribute("data", data);

		/*
		 * enviamos la informacion al webservice y obtenemos la respuesta
		 */

		CashClientWS cashclienteWS = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
		ArrayList<BeanNodoXML> listaParametros = new ArrayList<BeanNodoXML>();
		BeanNodoXML beanNodo = null;
		String resultado = null;

		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");

		// obtenemos los datos de los filtros para realizar la busqueda
		PagoServicioForm pagoServForm = (PagoServicioForm) form;
		String m_NumAbonado = pagoServForm.getM_NumAbonado();
		String m_Sector = pagoServForm.getM_Sector();

		// obtenemos el tipo de entidad, el codigo de entidad y el codigo de
		// servicio
		String m_Servicio = pagoServForm.getM_Servicio();
		String codigos[] = m_Servicio.split(";");

		beanNodo = new BeanNodoXML(Constantes.TAG_TRX_IBS,
				Constantes.IBS_CONS_PAG_SERVICIOS); // id de la transaccion
		listaParametros.add(beanNodo);

		listaParametros.add(new BeanNodoXML(Constantes.TAG_ENTITY_TYPE,
				serv_pago.getM_CodTipoEntidad())); // tipo de entidad(cod de
													// empresa)
		listaParametros.add(new BeanNodoXML(Constantes.TAG_ENTITY_CODE,
				serv_pago.getM_CodServProveedor())); // codigo de entidad
		listaParametros.add(new BeanNodoXML(Constantes.TAG_SERVICE_CODE,
				codigos[2])); // codigo del servicio
		listaParametros.add(new BeanNodoXML(Constantes.TAG_NUMBER_PAID,
				m_NumAbonado)); // numero de abonado
		if (m_Sector != null && m_Sector.length() > 0) {
			String sect[] = m_Sector.split(";");
			listaParametros.add(new BeanNodoXML(Constantes.TAG_ID_LOCAL,
					sect[0])); // codigo interno sector
			serv_pago.setM_Sector(sect[0]);
			serv_pago.setM_DescSector(sect[1]);
		}

		// jwong 23/03/2009
		// serv_pago.setM_Proveedor(codigos[0]);
		// serv_pago.setM_ServProv(codigos[1]);
		serv_pago.setM_Servicio(codigos[2]);
		serv_pago.setM_NumAbonado(m_NumAbonado);
		serv_pago.setM_NombreTipoServicio(codigos[4]);
		// serv_pago.setM
		session.setAttribute("serv_pago", serv_pago);

		try {
			String req = GenRequestXML.getXML(listaParametros);
			resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
			if (resultado == null || "".equals(resultado)) {
				// deberiamos retornar a la pagina con un mensaje de error
				request.setAttribute("mensaje", "No se encontraron resultados");
				return mapping.findForward("error");
			}
			// se debe parsear el xml obtenido
			BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);			
			// si la respuesta es exitosa
			if (beanResXML != null	&& "00".equals(	beanResXML.getM_CodigoRetorno())) {	
					List listaBusqueda = ParserXML.listarBusquedaPagoServ(beanResXML.getM_Respuesta(), codigos[0], codigos[3]);
				if (listaBusqueda != null && listaBusqueda.size() > 0) {
					request.setAttribute("listaResult", listaBusqueda);
					// pagoServForm.setM_NumSuministroIni(m_NumSuministro);
					// pagoServForm.setM_IdEmpCodCliente(pagoServForm.getM_Empresa());
				} else {
					request.setAttribute("mensaje","No se encontraron resultados");
				}
			} else if (beanResXML != null
					&& beanResXML.getM_CodigoRetorno() != null) {// &&
																	// beanResXML.getM_DescripcionError()!=null
																	// &&
																	// beanResXML.getM_DescripcionError().length()>0

				TxResultDao dao = new TxResultDaoHibernate();// jmoreno 28-08-09
				TxResult txResult = dao.selectByCodIbs(beanResXML
						.getM_CodigoRetornoIBS());
				if (txResult != null) {
					request.setAttribute("mensaje", txResult
							.getDrsDescription());
				} else {
					request.setAttribute("mensaje", beanResXML.getM_CodigoRetornoIBS()
							+ ":" + Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);
				}				
			} else {
				request.setAttribute("mensaje",
						"Se encontraron problemas al procesar la información");
			}
			return mapping.findForward("cargarPagoOnLine");
		} catch (Exception ex) {
			request.setAttribute("mensaje","Se encontraron problemas al procesar la información");
			logger.error(ex.toString(), ex);
			return mapping.findForward("error");
		}

	}

	public ActionForward cargarDetallePagoOnLine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
		String data = (String) request.getParameter("data");
		request.setAttribute("data", data);

		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");

		String lblSuministro = request.getParameter("labelSuministro");
		serv_pago.setM_LabelNumAbonado(lblSuministro);
		PagoServicioForm pagoServForm = (PagoServicioForm) form;
		String cheksValue[] = request.getParameterValues("m_chkRecibo");
		String valores[] = null;
		double importeRecibo = 0.0;

		BeanTipoCambioXML tipoCambioActual = (BeanTipoCambioXML) session
				.getAttribute("tipoCambioActual");
		double cambioVta = 0.0;
		if (tipoCambioActual != null) {
			try {
				cambioVta = Double.parseDouble(tipoCambioActual.getM_Venta());
			} catch (NumberFormatException nfe) {
				logger.error(nfe.toString());
				cambioVta = 0.0;
			}
		}

		BeanConsPagoServicio beanXML;
		ArrayList<BeanConsPagoServicio> listaResult = new ArrayList<BeanConsPagoServicio>();
		for (int h = 0; h < cheksValue.length; h++) {
			valores = cheksValue[h].split(";");
			try {
				importeRecibo = Double.parseDouble(valores[6].replaceAll(" ",
						""));

			} catch (NumberFormatException nfe) {
				logger.error(nfe.toString());
			}
			beanXML = new BeanConsPagoServicio();
			beanXML.setM_NumRecibo(valores[0]);
			beanXML.setM_CodEmpresa(valores[1]);
			beanXML.setM_NombreServicio(valores[2]);
			beanXML.setM_Moneda(valores[3]);
			beanXML.setM_FechaEmision(valores[4]);
			beanXML.setM_NombreCliente(valores[5]);
			beanXML.setM_Importe(valores[6]);
			beanXML.setM_ImporteMostrar(valores[6].replaceAll(" ", ","));
			beanXML.setM_DescrMoneda(Util.monedaMostrar(beanXML.getM_Moneda()));

			listaResult.add(beanXML);
		}

		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMinimumFractionDigits(2);
		decimalFormat.setMaximumFractionDigits(2);
		DecimalFormatSymbols simbols = new DecimalFormatSymbols();
		simbols.setDecimalSeparator('.');
		simbols.setGroupingSeparator(' ');
		simbols.setNaN("");
		decimalFormat.setDecimalFormatSymbols(simbols);

		session.setAttribute("listaResultPagar", listaResult);

		String idEmpresaCodCliente = pagoServForm.getM_Empresa();
		String codes[] = idEmpresaCodCliente.split(";");

		serv_pago.setM_Empresa(idEmpresaCodCliente);
		session.setAttribute("serv_pago", serv_pago);

		CashClientWS cashclienteWS = (CashClientWS) context
				.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
		if (cashclienteWS == null) {
			request.setAttribute("mensaje",
					"Error. No se puede conectar al servicio web del IBS");
			return mapping.findForward("error");
		}
		ArrayList listaParametros = new ArrayList();
		listaParametros.add(new BeanNodoXML("id_trx",
				Constantes.IBS_CONS_CTAS_ASOC_CLIENTE));
		listaParametros.add(new BeanNodoXML("client_code", codes[1]));

		String req = GenRequestXML.getXML(listaParametros);
		String resultado = cashclienteWS.ProcesarMensaje(req,
				Constantes.WEB_SERVICE_CASH);

		if (resultado == null || "".equals(resultado)) {
			request.setAttribute("mensaje",
					"Error. El servicio web del IBS no devuelve resultado");
			return mapping.findForward("error");
		}

		BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
		BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session
				.getAttribute("usuarioActual");
		if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
			BeanConsCtasCliente beanctascliente = ParserXML
					.getConsCtasClienteCombos(beanResXML.getM_Respuesta());
			List listaccounts = beanctascliente.getM_Accounts();
			session.setAttribute("beanctascliente", beanctascliente);
			session.setAttribute("listaccounts", listaccounts);

		} else if (beanResXML != null
				&& beanResXML.getM_CodigoRetorno() != null) {
			TxResultDao dao = new TxResultDaoHibernate();
			TxResult txResult = dao.selectByCodIbs(beanResXML
					.getM_CodigoRetornoIBS());
			if (txResult != null) {
				request.setAttribute("mensaje", txResult.getDrsDescription());
			} else {
				request.setAttribute("mensaje", beanResXML
						.getM_CodigoRetornoIBS()
						+ ":" + Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);
			}

		} else {
			request.setAttribute("mensaje",
					"Error. El servicio web del IBS no devuelve resultado");
		}
		return mapping.findForward("cargarPagoDetalleOnLine");
	}

	public ActionForward pagarOnLine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		try {
			HttpSession session = request.getSession();
			ServletContext context = session.getServletContext();
			// si termino la session debemos retornar al inicio
			if (session.getAttribute("usuarioActual") == null) {
				response.sendRedirect("cierraSession.jsp");
				return null;
			} else {
				try {
					if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
						return mapping.findForward("fueraServicio");
					}
				} catch (Exception e) {
					logger.error("VALIDACION DE DISPONIBILIDAD", e);
					return mapping.findForward("fueraServicio");
				}
			}

			// jmoreno 21-08-09
			String data = (String) request.getParameter("data");

			// obtenemos de session el objeto entidad
			// BeanConsPagoServicio entidadPagoServicio =
			// (BeanConsPagoServicio)session.getAttribute("entidadPagoServicio");
			BeanPagoServicio serv_pago = (BeanPagoServicio) session
					.getAttribute("serv_pago");

			// obtenemos el ruc y el codigo de cliente de la empresa
			// seleccionada
			PagoServicioForm pagoServForm = (PagoServicioForm) form;
			String idEmpresaCodCliente = pagoServForm.getM_Empresa();

			String codes[] = idEmpresaCodCliente.split(";");

			// validamos que la empresa tenga permiso para pago de servicios
			List listaServicios = (List) session.getAttribute("listaservicios");
			// Hiberbate
			TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
			TaServicioxEmpresa objservicioxempresa = servicioxempresaDAO
					.selectServicioxEmpresa(codes[0],
							((BeanServicio) listaServicios.get(0))
									.getM_IdServicio());

			if (objservicioxempresa == null) {
				List<BeanSuccess> alsuccess = new ArrayList<BeanSuccess>();
				BeanSuccess success = new BeanSuccess();
				success.setM_Mensaje("SERVICIO NO AFILIADO");
				String strTitulo = serv_pago.getM_Titulo();
				success.setM_Titulo(strTitulo);
				success.setM_Back("pagoServicio.do?do=cargarProveedor&" + data);
				request.setAttribute("success", success);
				request.setAttribute("alsuccess", alsuccess);
				return mapping.findForward("success");
			}

			// PAGO_SERVICIOS
			// campos requeridos para realizar el pago del servicio
			String entity_type = null; // tipo entidad
			String entity_code = null; // codigo entidad
			String service_code = null; // Codigo Servicio
			String number_paid = null; // numero abonado
			// puede ser mas de un numero de recibo
			String receipt = null; // numero recibo
			// puede ser mas de una fecha de emision
			String emission_date = null; // fecha emision
			// puede ser mas de una moneda
			String currency_2 = null; // moneda
			String ruc; // (ruc de la empresa) manejar un combo para varias
						// empresas caso excepcional

			// String amount = pagoServForm.getM_Importe().replaceAll(" ", "");
			// //monto

			entity_type = serv_pago.getM_CodTipoEntidad(); // tipo entidad
			entity_code = serv_pago.getM_CodServProveedor(); // codigo entidad
			service_code = serv_pago.getM_Servicio(); // Codigo Servicio
			number_paid = serv_pago.getM_NumAbonado(); // numero abonado

			// obtenemos el codigo de la cuenta donde se cargara el pago
			String codigoImporte = pagoServForm.getM_CuentaCargo(); // 1ero
																	// debemos
																	// quitar el
																	// importe
																	// del
																	// codigo
			String codesCtaImp[] = codigoImporte.split(";");
			String account_code = codesCtaImp[0]; // codigo de la cuenta a
													// cargar con el pago

			ruc = codes[0]; // ruc de la empresa

			// List<BeanAccount> listaccounts = (List<BeanAccount>)
			// session.getAttribute("listaccounts");

			List<BeanAccount> cuentas = (List<BeanAccount>) session.getAttribute("listaccounts");
			String nroCuentaCargo = pagoServForm.getM_CuentaCargo().split(";")[0];
			BeanAccount baCuenta = (BeanAccount) CollectionUtils.find(cuentas,new CollectionFilter(nroCuentaCargo, 1));

			String cheksValue[] = request.getParameterValues("m_chkReciboPago");
			String valores[] = null;
			double importeRecibo = 0.0;


			
			double saldoCuenta = Double.valueOf(baCuenta.getM_AvailableBalance().replace(" ", ""));
			
			// webservice
			// GetInfo CashWS - CONS_CTAS_CLIENTE
			CashClientWS cashclienteWS = (CashClientWS) context
					.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
			if (cashclienteWS == null) {
				request.setAttribute("mensaje","Error. No se puede conectar al servicio web del IBS");
				return mapping.findForward("error");
			}

			boolean isOkTrx = true;
			String mensajeIbs = "";// jmoreno 28-08-09

			TaOrden orden = null;
			String referencia = pagoServForm.getM_Referencia();
			double monto = 0.0;
			String strServicio = serv_pago.getM_NombreGrupo().toUpperCase();
			String strTipoServicio = serv_pago.getM_NombreTipoServicio().toUpperCase();
			String strMoneda = CashConstants.VAL_SOLES_SIMB;
			String cliente = "";
			String strProveedor = serv_pago.getM_NombreProveedor().toUpperCase();
			String cuenta_depos = serv_pago.getM_CuentaDepositante(); // cuenta
																		// depositante
			if( saldoCuenta < 0 ){
				request.setAttribute("mensaje","La cuenta no tiene saldo disponible");				
				return mapping.findForward("error");
			}
			for (int h = 0; h < cheksValue.length; h++) {
				valores = cheksValue[h].split(";");				
				cliente = valores[5];
				importeRecibo = Double.parseDouble(valores[6].replaceAll(" ", ""));
				monto = importeRecibo;		
				if( h == 0 ){
					if(  saldoCuenta < importeRecibo ){
						request.setAttribute("mensaje","La cuenta no tiene saldo disponible");				
						return mapping.findForward("error");
					}
				}
				if( saldoCuenta >= importeRecibo ){				
						receipt = valores[0]; // numero recibo
						emission_date = valores[4]; // fecha emision
						currency_2 = valores[3]; // moneda
		
						ArrayList<BeanNodoXML> listaParametros = new ArrayList<BeanNodoXML>();
						listaParametros.add(new BeanNodoXML("id_trx",Constantes.IBS_PAGO_SERVICIOS));
						listaParametros.add(new BeanNodoXML("entity_type", entity_type));
						listaParametros.add(new BeanNodoXML("entity_code", entity_code));
						listaParametros.add(new BeanNodoXML("service_code",service_code));
						listaParametros.add(new BeanNodoXML("number_paid", number_paid));
						listaParametros.add(new BeanNodoXML("receipt", receipt));
						listaParametros.add(new BeanNodoXML("emission_date",emission_date));
		
						currency_2 = Util.monedaSalida(currency_2);
						listaParametros.add(new BeanNodoXML("currency_2", currency_2));
		
						
						String strMonto = Util.formatearMontoSalida(valores[6].replaceAll(" ", ""),Constantes.NUMERO_DECIMALES_SALIDA); // por defecto se
																		// usaran 2
																		// decimales
						listaParametros.add(new BeanNodoXML("amount", strMonto));
		
						listaParametros.add(new BeanNodoXML("account_code",account_code));
		
						String req = GenRequestXML.getXML(listaParametros);				
						String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
						if (resultado == null || "".equals(resultado)) {
							isOkTrx = false;
						} else {
							BeanRespuestaXML beanResXML = ParserXML	.parsearRespuesta(resultado);							
							if (beanResXML != null	&& "00".equals(beanResXML.getM_CodigoRetorno())) {
								isOkTrx = isOkTrx & true;
								if (orden == null) {
									orden = delegadoServicio.pagarReciboOnLine(objservicioxempresa.getCsemIdServicioEmpresa(),currency_2, 
														account_code, baCuenta.getM_AccountType(), strServicio);
									delegadoServicio.agregarDetalle(orden,objservicioxempresa.getCsemIdServicioEmpresa(),
													strTipoServicio, referencia, monto,	nroCuentaCargo, cuenta_depos, number_paid,
													beanResXML.getM_CodigoRetornoIBS(),strProveedor, receipt, emission_date,cliente);
								} else {
									delegadoServicio.agregarDetalle(orden,objservicioxempresa.getCsemIdServicioEmpresa(),
											strTipoServicio, referencia, monto,	nroCuentaCargo, cuenta_depos, number_paid,
											beanResXML.getM_CodigoRetornoIBS(),strProveedor, receipt, emission_date,cliente);
								}
							} else {
								if (beanResXML != null	&& beanResXML.getM_CodigoRetorno() != null) {// jmoreno
																						// 28-08-09
									TxResultDao dao = new TxResultDaoHibernate();
									TxResult txResult = dao.selectByCodIbs(beanResXML
											.getM_CodigoRetornoIBS());
									if (txResult != null) {
										mensajeIbs = txResult.getDrsDescription();
									} else {
										mensajeIbs = beanResXML.getM_CodigoRetornoIBS()
												+ ":"
												+ Constantes.CODIGO_RPTA_IBS_DESCONOCIDO;
									}
		
								}
								isOkTrx = false;
							}
						}
					}
				
				saldoCuenta-= importeRecibo;
			}

			if (orden != null) {
				List<ReciboPagoServicio> recibos = delegadoServicio.registrarDetalles(orden);
				request.setAttribute("recibos", recibos);
			}
			
			if (isOkTrx && orden != null) {				
				request.setAttribute("orden", orden.getId().getCorIdOrden());
				request.setAttribute("servicio", objservicioxempresa.getCsemIdServicioEmpresa());

				String strTitulo = serv_pago.getM_Titulo();
				String strMensaje = "Transferencia Procesada Correctamente";
				String strBack = "pagoServicio.do?do=cargarProveedor&" + data;

				List<BeanSuccessDetail> alsuccess = new ArrayList<BeanSuccessDetail>();
				BeanSuccessDetail sucessdetail;

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Servicio ");
				sucessdetail.setM_Mensaje(strServicio);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Tipo Servicio ");
				sucessdetail.setM_Mensaje(strTipoServicio);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Proveedor");
				sucessdetail.setM_Mensaje(strProveedor);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Orden");
				sucessdetail.setM_Mensaje(String.valueOf(orden.getId()
						.getCorIdOrden()));
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Suministro");
				sucessdetail.setM_Mensaje(pagoServForm.getM_NumAbonado());
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Monto");
				sucessdetail.setM_Mensaje(new StringBuilder(strMoneda).append(
						orden.getNorMontoSoles()).toString());
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Cuenta de Cargo");
				sucessdetail.setM_Mensaje(nroCuentaCargo);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Referencia");
				sucessdetail.setM_Mensaje(referencia);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Fecha / Hora");
				sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy")
						+ "   " + Fecha.getFechaActual("HH:mm:ss"));
				alsuccess.add(sucessdetail);

				BeanSuccess success = new BeanSuccess();
				success.setM_Titulo(strTitulo);
				success.setM_Mensaje(strMensaje);
				success.setM_Back(strBack);

				request.setAttribute("success", success);
				request.setAttribute("alsuccess", alsuccess);
				return mapping.findForward("pagoServicio");
			} else {
				String strTitulo = serv_pago.getM_Titulo();
				String strMensaje = "La transacción no se pudo completar. "	+ mensajeIbs;
				String strBack = "pagoServicio.do?do=cargarProveedor&" + data;

				List<BeanSuccessDetail> alsuccess = new ArrayList<BeanSuccessDetail>();
				BeanSuccessDetail sucessdetail;

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Tipo de Operación");
				sucessdetail.setM_Mensaje("PAGO DE "+ serv_pago.getM_NombreGrupo().toUpperCase());
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Fecha / Hora");
				sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy")
						+ "   " + Fecha.getFechaActual("HH:mm:ss"));
				alsuccess.add(sucessdetail);

				BeanSuccess success = new BeanSuccess();
				success.setM_Titulo(strTitulo);
				success.setM_Mensaje(strMensaje);
				success.setM_Back(strBack);

				request.setAttribute("success", success);
				request.setAttribute("alsuccess", alsuccess);
				return mapping.findForward("success");
			}
		}catch (Exception e) {
			request.setAttribute("mensaje","La informacion no puede ser procesada");
			logger.error(e.getMessage(), e);
			return mapping.findForward("error");
		}

	}

	// PAGO SEDAPAL
	public ActionForward cargarPagoSedapal(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			String id = request.getParameter("modulo");
			if (id != null) {
				this.idModulo = id;
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

		// BeanPagoServicio serv_pago = (BeanPagoServicio)
		// session.getAttribute("serv_pago");
		BeanPagoServicio serv_pago = new BeanPagoServicio();
		String idProveedor = request.getParameter("idproveedor");

		serv_pago.setM_Modulo((String) request.getParameter("modulo"));
		serv_pago.setM_SubModulo((String) request.getParameter("submodulo"));
		String data = "idproveedor=" + idProveedor + "&modulo="
				+ serv_pago.getM_Modulo() + "&submodulo="
				+ serv_pago.getM_SubModulo();

		request.setAttribute("data", data);// jmoreno 20-08-09
		// limpiamos el numero de suministro
		serv_pago.setM_NumAbonado(null);

		List listaServicio = null;
		GetDataIBS gdi = new GetDataIBSFinanciero();
		List lparameters = new ArrayList();
		// parametro codigo de la empresa
		lparameters.add(new BeanNodoXML("idProveedor", idProveedor));
		int ires = 0;
		CashClientWS cashclienteWS2 = (CashClientWS) context.getAttribute(Constantes.CONTEXT_CLIENTE_HOME_BANKING_WS);
		cashclienteWS2.setOperacion((String) context.getAttribute("CONS_SERV_Servicio_Operacion2_Nombre"));
		listaServicio = gdi.getServicios_Empresas_Servicio(cashclienteWS2,lparameters, ires, idProveedor);
		if (listaServicio != null && listaServicio.size() > 0) {
			// session.setAttribute("nomProveedor", m_Nombre);
			serv_pago.setM_NombreProveedor("SEDAPAL");
			serv_pago.setM_Proveedor(idProveedor);
			session.setAttribute("listaServicio", listaServicio);
		} else {
			request.setAttribute("mensaje","No se pudo encontrar servicios para ese proveedor");
			return mapping.findForward("error");// cargarPagoSedapal - jmoreno
												// 16/12/09
		}

		// obtenemos el label del suministro
		// String lblSuministro = request.getParameter("labelSuministro");
		// serv_pago.setM_LabelNumAbonado(lblSuministro);
		// session.setAttribute("lblSuministro", lblSuministro);

		// obtenemos el numero de recibos resultado de la busqueda
		PagoServicioForm pagoServForm = (PagoServicioForm) form;

		// obtenemos el ruc y el codigo de cliente de la empresa seleccionada
		// String idEmpresaCodCliente = pagoServForm.getM_IdEmpCodCliente();

		// cargamos la fecha de vencimiento por defecto como la fecha actual
		String fecActual = Fecha.getFechaActual("dd/MM/yyyy");
		request.setAttribute("m_FechaActual", fecActual);
		serv_pago.setM_FecVencimiento(fecActual);

		// cargamos las empresas asociadas resultante del logueo
		List lEmpresa = (List) session.getAttribute("empresa");
		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
		List empresas = empresaDAO.listarClienteEmpresa(lEmpresa);
		session.setAttribute("listaEmpresas", empresas);

		if (empresas != null && empresas.size() > 0) {
			session.setAttribute("nroEmpresas", empresas.size());
		} else {
			request.setAttribute("mensaje",
					"No se pudo encontrar lista de empresas");
			return mapping.findForward("error");// cargarPagoSedapal - jmoreno
												// 16/12/09
		}

		// Hibernate
		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();
		TaServicioOpcionDao servicio_opcionDAO = new TaServicioOpcionDaoHibernate();

		TaServicioOpcion taservicioopcion = servicio_opcionDAO.select(serv_pago
				.getM_Modulo(), serv_pago.getM_SubModulo());
		List servicios = null;
		if (taservicioopcion != null) {
			servicios = servicioDAO.selectServicioxEmpresaxCode(
					((TmEmpresa) empresas.get(0)).getCemIdEmpresa(),
					taservicioopcion.getId().getCsoservicioId());
		}
		session.setAttribute("listaservicios", servicios);

		// String idEmpresaCodCliente = pagoServForm.getM_Empresa();
		// String codes[] = idEmpresaCodCliente.split(";");

		// serv_pago.setM_Empresa(idEmpresaCodCliente);
		serv_pago.setM_Empresa(((TmEmpresa) (empresas.get(0)))
				.getCemIdEmpresa());
		session.setAttribute("serv_pago", serv_pago);

		// GetInfo CashWS - CONS_CTAS_CLIENTE
		CashClientWS cashclienteWS = (CashClientWS) context
				.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
		if (cashclienteWS == null) {
			request.setAttribute("mensaje",
					"Error. No se puede conectar al servicio web del IBS");
			return mapping.findForward("error");
		}
		ArrayList listaParametros = new ArrayList();
		// listaParametros.add(new BeanNodoXML("id_trx",
		// Constantes.IBS_CONS_CTAS_CLIENTE));
		listaParametros.add(new BeanNodoXML("id_trx",
				Constantes.IBS_CONS_CTAS_ASOC_CLIENTE));

		String limpiar = request.getParameter("limpiar");
		if (limpiar == null) {
			// limpiamos la data de session
			pagoServForm.setM_CuentaCargo(null);
			pagoServForm.setM_Empresa(null);
			pagoServForm.setM_NumAbonado(null);
			pagoServForm.setM_Importe(null);
			pagoServForm.setM_Referencia(null);
		}

		// listaParametros.add(new BeanNodoXML("client_code", codes[1]));
		listaParametros.add(new BeanNodoXML("client_code",
				((TmEmpresa) (empresas.get(0))).getCemCodigoCliente()));

		// jwong 03/04/2009 para filtrar el resultado de la respuesta del
		// webservice
		// listaParametros.add(new BeanNodoXML(Constantes.TAG_FILTRO,
		// Constantes.FIELD_CASH_TIPO_MONEDA_SOLES + ";" +
		// Constantes.TAG_CURRENCY));
		// listaParametros.add(new BeanNodoXML(Constantes.TAG_FILTRO,
		// Constantes.TAG_ACCOUNT_TYPE + "=110,210;" + Constantes.TAG_CURRENCY +
		// "=" + Constantes.FIELD_CASH_SIMBOLO_MONEDA_SOLES));
		listaParametros.add(new BeanNodoXML(Constantes.TAG_FILTRO, /*
																	 * Constantes.
																	 * TAG_ACCOUNT_TYPE
																	 * +
																	 * "=110,210;"
																	 * +
																	 */
				Constantes.TAG_CURRENCY + "="
						+ Constantes.FIELD_CASH_SIMBOLO_MONEDA_SOLES));

		// + parametros
		String req = GenRequestXML.getXML(listaParametros);
		String resultado = cashclienteWS.ProcesarMensaje(req,
				Constantes.WEB_SERVICE_CASH);

		if (resultado == null || "".equals(resultado)) {
			request.setAttribute("mensaje",
					"Error. El servicio web del IBS no devuelve resultado");
			return mapping.findForward("error");
		}

		BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
		BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session
				.getAttribute("usuarioActual");
		if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
			// procesamos la respuesta(parseo xml) y enviamos a la pagina
			// BeanConsCtasCliente beanctascliente =
			// ParserXML.getConsCtasCliente(beanResXML.getM_Respuesta());
			// BeanConsCtasCliente beanctascliente =
			// ParserXML.getConsCtasCliente(beanResXML.getM_Respuesta(),
			// cashclienteWS, beanDataLogXML.getM_NumTarjeta(),
			// beanDataLogXML.getM_Token(),
			// ((TmEmpresa)(empresas.get(0))).getCemIdEmpresa());
			// BeanConsCtasCliente beanctascliente =
			// ParserXML.getConsCtasClienteCombos(beanResXML.getM_Respuesta());
			BeanConsCtasCliente beanctascliente = ParserXML
					.getConsCtasSolesClienteCombos(beanResXML.getM_Respuesta());

			List listaccounts = beanctascliente.getM_Accounts();
			session.setAttribute("beanctascliente", beanctascliente);
			session.setAttribute("listaccounts", listaccounts);
			// session.setAttribute("listaTipoMoneda", listaTipoMoneda);
			// pagoServForm.setM_IdEmpCodCliente(pagoServForm.getM_IdEmpCodCliente());
		} else if (beanResXML != null	&& beanResXML.getM_CodigoRetorno() != null) {// jmoreno 28-08-09
			TxResultDao dao = new TxResultDaoHibernate();
			TxResult txResult = dao.selectByCodIbs(beanResXML
					.getM_CodigoRetornoIBS());
			if (txResult != null) {
				request.setAttribute("mensaje", txResult.getDrsDescription());
			} else {
				request.setAttribute("mensaje", beanResXML
						.getM_CodigoRetornoIBS()
						+ ":" + Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);
			}
		} else {
			// mostraremos un mensaje de error en pagina
			request.setAttribute("mensaje",
					"Error. El servicio web del IBS no devuelve resultado");
		}
		return mapping.findForward("cargarPagoSedapal");
	}

	public ActionForward cargarCuentaSedapal(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}

		// GetInfo CashWS - CONS_CTAS_CLIENTE
		CashClientWS cashclienteWS = (CashClientWS) context
				.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
		if (cashclienteWS == null) {
			request.setAttribute("mensaje",
					"Error. No se puede conectar al servicio web del IBS");
			return mapping.findForward("error");
		}
		ArrayList listaParametros = new ArrayList();
		// listaParametros.add(new BeanNodoXML("id_trx",
		// Constantes.IBS_CONS_CTAS_CLIENTE));
		listaParametros.add(new BeanNodoXML("id_trx",
				Constantes.IBS_CONS_CTAS_ASOC_CLIENTE));

		String empresaSel = request.getParameter("empresaSel");
		// si se selecciono una empresa antes
		String codes[] = null;
		if (empresaSel != null && empresaSel.length() > 0) {
			// listaParametros.add(new BeanNodoXML("client_code", codes[1]));
			codes = empresaSel.split(";");
			listaParametros.add(new BeanNodoXML("client_code", codes[1]));
		}
		// listaParametros.add(new BeanNodoXML(Constantes.TAG_FILTRO,
		// Constantes.TAG_ACCOUNT_TYPE + "=110,210;" + Constantes.TAG_CURRENCY +
		// "=" + Constantes.FIELD_CASH_SIMBOLO_MONEDA_SOLES));
		listaParametros.add(new BeanNodoXML(Constantes.TAG_FILTRO, /*
																	 * Constantes.
																	 * TAG_ACCOUNT_TYPE
																	 * +
																	 * "=110,210;"
																	 * +
																	 */
				Constantes.TAG_CURRENCY + "="
						+ Constantes.FIELD_CASH_SIMBOLO_MONEDA_SOLES));

		// + parametros
		String req = GenRequestXML.getXML(listaParametros);
		String resultado = cashclienteWS.ProcesarMensaje(req,
				Constantes.WEB_SERVICE_CASH);

		if (resultado == null || "".equals(resultado)) {
			request.setAttribute("mensaje",
					"Error. El servicio web del IBS no devuelve resultado");
			return mapping.findForward("error");
		}
		BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
		BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session
				.getAttribute("usuarioActual");
		if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
			// procesamos la respuesta(parseo xml) y enviamos a la pagina
			// BeanConsCtasCliente beanctascliente =
			// ParserXML.getConsCtasCliente(beanResXML.getM_Respuesta(),
			// cashclienteWS, beanDataLogXML.getM_NumTarjeta(),
			// beanDataLogXML.getM_Token(), codes[0]);
			BeanConsCtasCliente beanctascliente = ParserXML
					.getConsCtasClienteCombos(beanResXML.getM_Respuesta());

			List listaccounts = beanctascliente.getM_Accounts();
			session.setAttribute("beanctascliente", beanctascliente);
			session.setAttribute("listaccounts", listaccounts);
		} else if (beanResXML != null
				&& beanResXML.getM_CodigoRetorno() != null) {// jmoreno 28-08-09
			TxResultDao dao = new TxResultDaoHibernate();
			TxResult txResult = dao.selectByCodIbs(beanResXML
					.getM_CodigoRetornoIBS());
			if (txResult != null) {
				request.setAttribute("mensaje", txResult.getDrsDescription());
			} else {
				request.setAttribute("mensaje", beanResXML
						.getM_CodigoRetornoIBS()
						+ ":" + Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);
			}
		} else {
			// mostraremos un mensaje de error en pagina
			request.setAttribute("mensaje",
					"Error. El servicio web del IBS no devuelve resultado");
		}
		return mapping.findForward("cargarPagoSedapal");
	}

	public ActionForward cargarDetallePagoSedapal(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
		// obtenemos el proveedor, para mantenerlo - jmoreno 20-08-09
		String data = (String) request.getParameter("data");
		// String data = "idproveedor="+((String)
		// request.getParameter("idproveedor"))+"&modulo"+((String)
		// request.getParameter("modulo"))+"&submodulo"+((String)
		// request.getParameter("submodulo"));

		request.setAttribute("data", data);// jmoreno 20-08-09

		// obtenemos el numero de recibos resultado de la busqueda
		PagoServicioForm pagoServForm = (PagoServicioForm) form;
		// campos de pagina
		String m_Empresa = pagoServForm.getM_Empresa();
		String m_CuentaCargo = pagoServForm.getM_CuentaCargo();
		String m_NumAbonado = pagoServForm.getM_NumAbonado();
		String m_Importe = pagoServForm.getM_Importe();
		String m_FecVencimiento = pagoServForm.getM_FecVencimiento();
		String m_Referencia = pagoServForm.getM_Referencia();
		String m_StrTag = pagoServForm.getM_Servicio();

		// debemos separar el numero de cuenta del saldo disponible
		String cuenta[] = m_CuentaCargo.split(";");
		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");
		serv_pago.setM_Titulo("Pago del Servicio de Agua");

		serv_pago.setM_Empresa(m_Empresa);
		serv_pago.setM_CuentaCargo(cuenta[0]);

		// jwong 30/04/2009 manejo de la salida de la moneda para SEDAPAL
		serv_pago.setM_Moneda(Util.monedaSalidaSEDAPAL(cuenta[2]));

		// serv_pago.setM_DescEmpresa(m_Empresa);
		serv_pago.setM_NumAbonado(m_NumAbonado);
		serv_pago.setM_MontoTotal(m_Importe);
		serv_pago.setM_MontoMostrar(Util.formatearMontoNvo(m_Importe));
		serv_pago.setM_FecVencimiento(m_FecVencimiento);
		serv_pago.setM_Referencia(m_Referencia);
		serv_pago.setM_CuentaDepositante(m_StrTag);

		session.setAttribute("serv_pago", serv_pago);
		return mapping.findForward("cargarDetallePagoSedapal");
	}
	
	private boolean verificarPagoSedapal(String trama){
        try {
        	 SAXBuilder saxbuilder = new SAXBuilder(false);
             ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(trama.getBytes());
             Document doc = saxbuilder.build(bytearrayinputstream);            
            Element elemRaiz = doc.getRootElement();                
            Element nodoReturn = (Element) elemRaiz.getChildren("return").get(0);           
            String codigo = nodoReturn.getChild("RespuestaCASH").getChild("Respuesta").getChild("mensaje").getValue().substring(0,3);           
            if( codigo.equals(CashConstants.VAL_IBS_EXITO_SEDAPAL) ){
            	return true;
            }else{
            	return false;
            }                    
        } catch (IOException io) {
            logger.error("ERROR IOException: " + io.toString(),io);            
            return false;
        } catch (org.jdom.JDOMException e) {
            logger.error("ERROR JDOMEXception: " + e.toString(),e);            
            return false;
        } catch (Exception ex) {
            logger.error("ERROR Exception: " + ex.toString(),ex);            
            return false;
        }
	}

	@SuppressWarnings("unchecked")
	public ActionForward pagarSedapal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		try {
			HttpSession session = request.getSession();
			ServletContext context = session.getServletContext();
			// si termino la session debemos retornar al inicio
			if (session.getAttribute("usuarioActual") == null) {
				response.sendRedirect("cierraSession.jsp");
				return null;
			} else {
				try {
					if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
						return mapping.findForward("fueraServicio");
					}
				} catch (Exception e) {
					logger.error("VALIDACION DE DISPONIBILIDAD", e);
					return mapping.findForward("fueraServicio");
				}
			}
			// recuperamos el idProveedor para regresar al inicio
			String data = (String) request.getParameter("data");

			// obtenemos el bean que contiene los datos necesarios para realizar
			// el pago
			BeanPagoServicio serv_pago = (BeanPagoServicio) session
					.getAttribute("serv_pago");
			BeanDataLoginXML dataLogin = (BeanDataLoginXML) session
					.getAttribute("usuarioActual");

			PagoServicioForm pagoServForm = (PagoServicioForm) form;

			// obtenemos el ruc y el codigo de cliente de la empresa
			// seleccionada
			// String idEmpresaCodCliente = pagoServForm.getM_IdEmpCodCliente();
			// String idEmpresaCodClienteNombre = pagoServForm.getM_Empresa();
			String idEmpresaCodClienteNombre = serv_pago.getM_Empresa();
			String codes[] = idEmpresaCodClienteNombre.split(";");

			// validamos que la empresa tenga permiso para pago de servicios
			List listaServicios = (List) session.getAttribute("listaservicios");
			// Hiberbate
			TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
			TaServicioxEmpresa objservicioxempresa = null;
			if (listaServicios != null && listaServicios.size() > 0) {
				objservicioxempresa = servicioxempresaDAO
						.selectServicioxEmpresa(codes[0],
								((BeanServicio) listaServicios.get(0))
										.getM_IdServicio());
			}

			if (objservicioxempresa == null) {
				List alsuccess = new ArrayList();
				BeanSuccess success = new BeanSuccess();

				success.setM_Mensaje("SERVICIO NO AFILIADO");
				String strTitulo = serv_pago.getM_Titulo();
				success.setM_Titulo(strTitulo);
				success.setM_Back("pagoServicio.do?do=cargarPagoSedapal&"
						+ data);// cargarDetallePagoSedapal - jmoreno 20-08-09

				request.setAttribute("success", success);
				request.setAttribute("alsuccess", alsuccess);
				return mapping.findForward("success");
			}
			String orden = "";
			String cuenta_depos = serv_pago.getM_CuentaDepositante(); // cuenta
																		// depositante
			String suministro = serv_pago.getM_NumAbonado(); // Nro
																// suministro???

			String strMonto = serv_pago.getM_MontoTotal(); // Monto
			double monto = Double.parseDouble(strMonto);
			// double monto = Doubl
			// formatemos el monto (1.00 --> 100) para manejo de 2 decimales
			strMonto = Util.formatearMontoSalida(strMonto,
					Constantes.NUMERO_DECIMALES_SALIDA); // por defecto se
															// usaran 2
															// decimales

			String nombre1 = codes[2]; // Nombre titular 1
			// String nombre2;
			String strFecha = serv_pago.getM_FecVencimiento();
			String fec_venc = Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy",
					serv_pago.getM_FecVencimiento()); // Fecha vencimiento

			String referencia = serv_pago.getM_Referencia(); // referencia cobro

			String moneda = serv_pago.getM_Moneda(); // Moneda
			// enviamos el codigo de moneda
			moneda = Util.monedaSalida(moneda);

			String cuenta_cargo = serv_pago.getM_CuentaCargo(); // cuenta cargo
			String ruc = codes[0]; // ruc de la empresa

			List<BeanAccount> cuentas = (List<BeanAccount>) session
					.getAttribute("listaccounts");
			BeanAccount baCuenta = (BeanAccount) CollectionUtils.find(cuentas,
					new CollectionFilter(cuenta_cargo, 1));

			CashClientWS cashclienteWS = (CashClientWS) context
					.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
			if (cashclienteWS == null) {
				request.setAttribute("mensaje",
						"Error. No se puede conectar al servicio web del IBS");
				return mapping.findForward("error");
			}
			String mensajeIbs = "";// jmoreno 28-08-09
			boolean isOkTrx = true;
			// parametros de pago a sedapal
			ArrayList listaParametros = new ArrayList();
			listaParametros.add(new BeanNodoXML("id_trx",
					Constantes.IBS_PAGO_SERVICIOS_SEDAPAL));
			listaParametros.add(new BeanNodoXML("cuenta_depos", cuenta_depos));
			listaParametros.add(new BeanNodoXML("suministro", suministro));
			listaParametros.add(new BeanNodoXML("monto", strMonto));
			listaParametros.add(new BeanNodoXML("nombre1", nombre1));
			// listaParametros.add(new BeanNodoXML("nombre2", nombre2));
			listaParametros.add(new BeanNodoXML("fec_venc", fec_venc));
			listaParametros.add(new BeanNodoXML("referencia", referencia));
			listaParametros.add(new BeanNodoXML("moneda", moneda));
			listaParametros.add(new BeanNodoXML("cuenta_cargo", cuenta_cargo));
			listaParametros.add(new BeanNodoXML("ruc", ruc));

			String req = GenRequestXML.getXML(listaParametros);
			String resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
			if (resultado == null || "".equals(resultado)) {
				isOkTrx = false;
			} else {				
				BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);			
				if (beanResXML != null	&& "00".equals(beanResXML.getM_CodigoRetorno())) {
					if( verificarPagoSedapal(resultado)	){
						isOkTrx = true;
						String nroTarjeta = (String) session.getAttribute("tarjetaActual");
						orden = delegadoServicio.pagarReciboAgua(
								objservicioxempresa.getCsemIdServicioEmpresa(),
								monto, moneda, cuenta_cargo, baCuenta
										.getM_AccountType(), cuenta_depos,
								suministro, referencia, nroTarjeta, strFecha,
								beanResXML.getM_CodigoRetornoIBS());
					}else{
						isOkTrx = false;
						mensajeIbs = "Numero de recibo invalido";
					}					
				} else {
					if (beanResXML != null	&& beanResXML.getM_CodigoRetorno() != null) {																			
						TxResultDao dao = new TxResultDaoHibernate();
						TxResult txResult = dao.selectByCodIbs(beanResXML.getM_CodigoRetornoIBS());
						if (txResult != null) {
							mensajeIbs = txResult.getDrsDescription();
						} else {
							mensajeIbs = beanResXML.getM_CodigoRetornoIBS()+ ":"+ Constantes.CODIGO_RPTA_IBS_DESCONOCIDO;
						}
					}
					isOkTrx = false;
				}
			}

			if (isOkTrx) {

				request.setAttribute("orden", orden);
				request.setAttribute("servicio", objservicioxempresa
						.getCsemIdServicioEmpresa());

				String strTitulo = serv_pago.getM_Titulo();
				String strMensaje = "Transferencia Procesada Correctamente";
				String strBack = "pagoServicio.do?do=cargarPagoSedapal&" + data;
				List alsuccess = new ArrayList();

				BeanSuccessDetail sucessdetail;

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Proveedor");
				sucessdetail.setM_Mensaje(CashConstants.EMP_SERV_SEDAPAL);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Orden");
				sucessdetail.setM_Mensaje(orden);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Suministro");
				sucessdetail.setM_Mensaje(suministro);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Monto");
				sucessdetail.setM_Mensaje(new StringBuilder(baCuenta
						.getM_Currency()).append(" ").append(
						Util.formatDouble2(monto)).toString());
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Cuenta de Cargo");
				sucessdetail.setM_Mensaje(cuenta_cargo);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Fecha Vencimiento");
				sucessdetail.setM_Mensaje(strFecha);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Referencia");
				sucessdetail.setM_Mensaje(referencia);
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Fecha / Hora");
				sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy")
						+ "   " + Fecha.getFechaActual("HH:mm:ss"));
				alsuccess.add(sucessdetail);

				BeanSuccess success = new BeanSuccess();
				success.setM_Titulo(strTitulo);
				success.setM_Mensaje(strMensaje);
				success.setM_Back(strBack);

				request.setAttribute("success", success);
				request.setAttribute("alsuccess", alsuccess);
				return mapping.findForward("pagoServicio");
			} else {
				String strTitulo = serv_pago.getM_Titulo();
				String strMensaje = "La transacción no se pudo completar. "	+ mensajeIbs; 
				String strBack = "pagoServicio.do?do=cargarPagoSedapal&" + data;
				List alsuccess = new ArrayList();
				BeanSuccessDetail sucessdetail;

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Tipo de Operación");
				sucessdetail.setM_Mensaje("PAGO DE AGUA SEDAPAL");
				alsuccess.add(sucessdetail);

				sucessdetail = new BeanSuccessDetail();
				sucessdetail.setM_Label("Fecha / Hora");
				sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy")
						+ "   " + Fecha.getFechaActual("HH:mm:ss"));
				alsuccess.add(sucessdetail);

				BeanSuccess success = new BeanSuccess();
				success.setM_Titulo(strTitulo);
				success.setM_Mensaje(strMensaje);
				success.setM_Back(strBack);

				request.setAttribute("success", success);
				request.setAttribute("alsuccess", alsuccess);
				return mapping.findForward("success");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			request.setAttribute("mensaje",
					"La informacion no pudo ser procesada");
			return mapping.findForward("error");
		}

	}

	public ActionForward imprimirPagoServicio(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {
			long orden = Long.parseLong(request.getParameter("idOrden"));
			long servicio = Long.parseLong(request.getParameter("idServicio"));
			PagoServicioComprobantes cps = new PagoServicioComprobantes(orden,
					servicio);
			if (cps.getTipoServicio().equals(CashConstants.SERV_AGUA)) {
				request.setAttribute("alsuccess", cps
						.obtenerDetallesImpresion());
				return mapping.findForward("imprPagoServicio");
			} else {
				if (cps.getTipoServicio().equals(CashConstants.SERV_TELEFONIA) || cps.getTipoServicio().equals(CashConstants.SERV_LUZ)) {
					request.setAttribute("alsuccess", cps
							.obtenerDetallesImpresion());
					request.setAttribute("recibos", cps.getRecibos());
					return mapping.findForward("imprPagoServicio");
				} else {
					throw new Exception(
							"El tipo de pago de servicio seleccionado no puede ser procesado.");
				}
			}
		} catch (Exception e) {
			logger.error("ERROR: Impresion", e);
			request.setAttribute("mensaje",
					"La informacion no pudo ser procesada");
			return mapping.findForward("error");
		}
	}

	// PAGO CLARO
	public ActionForward cargarPagoClaro(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
		// jmoreno 20-08-09
		String m_Grupo = request.getParameter("grupo"); // codigo del grupo
		String m_NombreGrupo = request.getParameter("nombre"); // nombre del
																// grupo
		String m_CodEntidad = request.getParameter("codentidad"); // codigo de
																	// entidad
		String m_Modulo = request.getParameter("modulo");
		String m_Submodulo = request.getParameter("submodulo");
		String data = "grupo=" + m_Grupo + "&nombre=" + m_NombreGrupo
				+ "&codentidad=" + m_CodEntidad + "&modulo=" + m_Modulo
				+ "&submodulo=" + m_Submodulo;
		request.setAttribute("data", data);

		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");

		// obtenemos la empresa proveedora del servicio seleccionada
		String m_Codigo = request.getParameter("m_Codigo"); // codigo de la
															// empresa
		String m_Nombre = request.getParameter("m_Nombre"); // nombre de la
															// empresa
		String m_CodigoServicio = request.getParameter("m_CodigoServicio"); // codigo
																			// de
																			// servicio(codigo
																			// de
																			// entidad)

		PagoServicioForm pagoservfrm = (PagoServicioForm) form;
		pagoservfrm.setM_NumAbonado(null);
		pagoservfrm.setM_Sector(null);

		// pagoservfrm.setM_NumSuministroIni(null);

		// cargamos las empresas asociadas resultante del logueo
		List lEmpresa = (List) session.getAttribute("empresa");
		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
		List empresas = empresaDAO.listarClienteEmpresa(lEmpresa);
		session.setAttribute("listaEmpresas", empresas);

		if (empresas != null && empresas.size() > 0) {
			session.setAttribute("nroEmpresas", empresas.size());
		} else {
			request.setAttribute("mensaje",
					"No se pudo encontrar lista de empresas");
			return mapping.findForward("cargarPagoOnLine");
		}

		// Hibernate
		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();
		TaServicioOpcionDao servicio_opcionDAO = new TaServicioOpcionDaoHibernate();

		TaServicioOpcion taservicioopcion = servicio_opcionDAO.select(serv_pago
				.getM_Modulo(), serv_pago.getM_SubModulo());
		List servicios = servicioDAO.selectServicioxEmpresaxCode(
				((TmEmpresa) empresas.get(0)).getCemIdEmpresa(),
				taservicioopcion.getId().getCsoservicioId());

		List listaServicio = null;
		GetDataIBS gdi = new GetDataIBSFinanciero();
		List lparameters = new ArrayList();
		// parametro codigo de la empresa
		lparameters.add(new BeanNodoXML("idProveedor", m_Codigo));
		int ires = 0;
		CashClientWS cashclienteWS = (CashClientWS) context
				.getAttribute(Constantes.CONTEXT_CLIENTE_HOME_BANKING_WS);
		cashclienteWS.setOperacion((String) context
				.getAttribute("CONS_SERV_Servicio_Operacion2_Nombre"));
		listaServicio = gdi.getServicios_Empresas_Servicio(cashclienteWS,
				lparameters, ires, m_Codigo);
		if (listaServicio != null && listaServicio.size() > 0) {
			// session.setAttribute("nomProveedor", m_Nombre);
			serv_pago.setM_NombreProveedor(m_Nombre);
			serv_pago.setM_Proveedor(m_Codigo);
			serv_pago.setM_CodServProveedor(m_CodigoServicio); // codigo de
																// entidad

			session.setAttribute("serv_pago", serv_pago);
			session.setAttribute("listaServicio", listaServicio);
			session.setAttribute("listaservicios", servicios);
		} else {
			request.setAttribute("mensaje",
					"No se pudo encontrar servicios para ese proveedor");
			return mapping.findForward("cargarPagoOnLine");
		}

		/*
		 * //verificamos si necesita seleccionar ubicacion String m_bSector =
		 * request.getParameter("sector"); //removemos el listado de sectores
		 * session.removeAttribute("listaSector");
		 * if("1".equalsIgnoreCase(m_bSector)){ List listaSector = null; List
		 * lparams = new ArrayList(); //parametro codigo de la empresa
		 * lparams.add(new BeanNodoXML(Constantes.TAG_INTIDPADRE, m_Codigo));
		 * ires = 0; cashclienteWS =
		 * (CashClientWS)context.getAttribute(Constantes
		 * .CONTEXT_CLIENTE_HOME_BANKING_WS);
		 * cashclienteWS.setOperacion((String)
		 * context.getAttribute("CONS_SERV_Servicio_Operacion3_Nombre"));
		 * listaSector = gdi.getSectores_Proveedor(cashclienteWS, lparameters,
		 * ires);
		 * 
		 * session.setAttribute("listaSector", listaSector); }
		 */

		return mapping.findForward("cargarPagoClaro");
	}

	public ActionForward buscarPagoClaro(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
		String m_Grupo = request.getParameter("grupo"); // codigo del grupo
		String m_NombreGrupo = request.getParameter("nombre"); // nombre del
																// grupo
		String m_CodEntidad = request.getParameter("codentidad"); // codigo de
																	// entidad
		String m_Modulo = request.getParameter("modulo");
		String m_Submodulo = request.getParameter("submodulo");
		String data = "grupo=" + m_Grupo + "&nombre=" + m_NombreGrupo
				+ "&codentidad=" + m_CodEntidad + "&modulo=" + m_Modulo
				+ "&submodulo=" + m_Submodulo;
		request.setAttribute("data", data);
		// jmoreno 20-08-09
		// String data = request.getParameter("data");
		// request.setAttribute("data", data);

		// enviamos la informacion al webservice y obtenemos la respuesta
		CashClientWS cashclienteWS = (CashClientWS) context
				.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
		ArrayList listaParametros = new ArrayList();
		BeanNodoXML beanNodo = null;
		String resultado = null;

		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");

		// obtenemos los datos de los filtros para realizar la busqueda
		PagoServicioForm pagoServForm = (PagoServicioForm) form;
		String m_NumAbonado = pagoServForm.getM_NumAbonado();
		// String m_Sector = pagoServForm.getM_Sector();

		// obtenemos el tipo de entidad, el codigo de entidad y el codigo de
		// servicio
		String m_Servicio = pagoServForm.getM_Servicio();
		String codigos[] = m_Servicio.split(";");
		// Para obtener el ruc de la empresa jmoreno 28-08-09
		String ruc_codCliente = pagoServForm.getM_Empresa();
		String codigos2[] = ruc_codCliente.split(";");

		beanNodo = new BeanNodoXML(Constantes.TAG_TRX_IBS,
				Constantes.IBS_CONS_PAG_SERVICIOS_CLARO); // id de la
															// transaccion
		listaParametros.add(beanNodo);

		listaParametros.add(new BeanNodoXML("entity_code", serv_pago
				.getM_CodServProveedor())); // codigo servicio proveedor(codig
											// de entidad)
		listaParametros.add(new BeanNodoXML("service_code", codigos[2])); // codigo
																			// de
																			// servicio
		listaParametros.add(new BeanNodoXML("number_paid", m_NumAbonado)); // numero
																			// de
																			// abonado
		listaParametros.add(new BeanNodoXML("ruc", codigos2[0]));
		/*
		 * if(m_Sector!=null && m_Sector.length()>0){ String sect[] =
		 * m_Sector.split(";"); listaParametros.add(new
		 * BeanNodoXML(Constantes.TAG_ID_LOCAL, sect[0])); //codigo interno
		 * sector serv_pago.setM_Sector(sect[0]);
		 * serv_pago.setM_DescSector(sect[1]); }
		 */

		// jwong 23/03/2009
		// serv_pago.setM_Proveedor(codigos[0]);
		// serv_pago.setM_ServProv(codigos[1]);
		serv_pago.setM_Servicio(codigos[2]);
		serv_pago.setM_NumAbonado(m_NumAbonado);
		session.setAttribute("serv_pago", serv_pago);

		try {
			String req = GenRequestXML.getXML(listaParametros);
			resultado = cashclienteWS.ProcesarMensaje(req,
					Constantes.WEB_SERVICE_CASH);
			if (resultado == null || "".equals(resultado)) {
				// deberiamos retornar a la pagina con un mensaje de error
				request.setAttribute("mensaje", "No se encontraron resultados");
				return mapping.findForward("error");
			}
			// se debe parsear el xml obtenido
			BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

			// si la respuesta es exitosa
			if (beanResXML != null
					&& "00".equals(beanResXML.getM_CodigoRetorno())) {
				List listaBusqueda = ParserXML.listarBusquedaPagoServClaro(
						beanResXML.getM_Respuesta(), codigos[0], codigos[3]);
				if (listaBusqueda != null && listaBusqueda.size() > 0) {
					request.setAttribute("listaResult", listaBusqueda);
					// pagoServForm.setM_NumSuministroIni(m_NumSuministro);
					// pagoServForm.setM_IdEmpCodCliente(pagoServForm.getM_Empresa());
				} else {
					request.setAttribute("mensaje",
							"No se encontraron resultados");
				}
			} else if (beanResXML != null
					&& beanResXML.getM_CodigoRetorno() != null) {// &&
																	// beanResXML.getM_DescripcionError()
																	// != null
																	// &&
																	// beanResXML.getM_DescripcionError().length()
																	// > 0
				TxResultDao dao = new TxResultDaoHibernate();// jmoreno 27-08-09
				TxResult txResult = dao.selectByCodIbs(beanResXML
						.getM_CodigoRetornoIBS());
				if (txResult != null) {
					request.setAttribute("mensaje", txResult
							.getDrsDescription());// beanResXML.getM_DescripcionError()
				} else {
					request.setAttribute("mensaje", beanResXML
							.getM_CodigoRetornoIBS()
							+ ":" + Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);
				}

			} else {
				request.setAttribute("mensaje",
						"Se encontraron problemas al procesar la información");
			}
		} catch (Exception ex) {
			request.setAttribute("mensaje",
					"Se encontraron problemas al procesar la información");
			logger.error(ex.toString());
		}

		return mapping.findForward("cargarPagoClaro");
	}

	public ActionForward cargarDetallePagoClaro(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
		// jmoreno 20-08-09
		String data = request.getParameter("data");
		request.setAttribute("data", data);

		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");

		// obtenemos el label del suministro
		String lblSuministro = request.getParameter("labelSuministro");
		serv_pago.setM_LabelNumAbonado(lblSuministro);
		// session.setAttribute("lblSuministro", lblSuministro);

		// obtenemos el numero de recibos resultado de la busqueda
		PagoServicioForm pagoServForm = (PagoServicioForm) form;
		String cheksValue[] = request.getParameterValues("m_chkRecibo");
		String valores[] = null;
		double importeRecibo = 0.0;
		// double importeTotalSoles = 0.0;
		// double importeTotalDolares = 0.0;

		// obtenemos el tipo de cambio actual
		BeanTipoCambioXML tipoCambioActual = (BeanTipoCambioXML) session
				.getAttribute("tipoCambioActual");
		// obtenemos el tipo de cambio para la venta
		double cambioVta = 0.0;
		if (tipoCambioActual != null) {
			try {
				cambioVta = Double.parseDouble(tipoCambioActual.getM_Venta());
			} catch (NumberFormatException nfe) {
				logger.error(nfe.toString());
				cambioVta = 0.0;
			}
		}

		BeanConsPagoServicio beanXML;
		ArrayList listaResult = new ArrayList();
		for (int h = 0; h < cheksValue.length; h++) {
			valores = cheksValue[h].split(";");
			try {
				// transformamos los importes de los recibos seleccionados en
				// decimales
				importeRecibo = Double.parseDouble(valores[9].replaceAll(" ",
						""));
				// verificamos la moneda para sumar el importe en soles o
				// dolares
				/*
				 * if(Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equalsIgnoreCase(valores
				 * [3]) || "604".equalsIgnoreCase(valores[3]) ||
				 * "S/.".equalsIgnoreCase(valores[3])){ importeTotalSoles =
				 * importeTotalSoles + importeRecibo; } else{
				 * importeTotalDolares = importeTotalDolares + importeRecibo; }
				 */
				// importeTotal = importeTotal + importeRecibo;
			} catch (NumberFormatException nfe) {
				logger.error(nfe.toString());
			}
			// recibos a pagar
			beanXML = new BeanConsPagoServicio();
			beanXML.setM_NumRecibo(valores[0]);
			beanXML.setM_Descripcion(valores[1]);
			beanXML.setM_Moneda(valores[2]);
			beanXML.setM_EstadoDeuda(valores[3]);

			beanXML.setM_TipoDocumento(valores[4]);
			beanXML.setM_NumeroDocumento(valores[5]);
			beanXML.setM_FechaEmision(valores[6]);
			beanXML.setM_FechaVencimiento(valores[7]);
			beanXML.setM_Referencia(valores[8]);
			beanXML.setM_Importe(valores[9]);
			beanXML.setM_ImporteMostrar(valores[9].replace(" ", ","));
			beanXML.setM_CodEmpresa(valores[10]);
			beanXML.setM_NombreServicio(valores[11]);

			beanXML.setM_DescrMoneda(Util.monedaMostrar(beanXML.getM_Moneda()));
			listaResult.add(beanXML);
		}

		// creamos bean conteniendo los datos resumen de los recibos
		// seleccionados
		// BeanDetallePagoServicio beanDetPagServ = new
		// BeanDetallePagoServicio();

		// enviamos a pagina el importe total a pagar
		// beanDetPagServ.setM_Importe(Util.formatearMonto(("" + importeTotal),
		// 2));
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMinimumFractionDigits(2);
		decimalFormat.setMaximumFractionDigits(2);
		DecimalFormatSymbols simbols = new DecimalFormatSymbols();
		simbols.setDecimalSeparator('.');
		simbols.setGroupingSeparator(' ');
		simbols.setNaN("");
		decimalFormat.setDecimalFormatSymbols(simbols);

		// serv_pago.setM_MontoTotal(decimalFormat.format(importeTotal));

		// serv_pago.setM_MontoTotalSoles(Util.sumaSolesDolares("sol",importeTotalSoles,importeTotalDolares,cambioVta));
		// serv_pago.setM_MontoTotalDolares(Util.sumaSolesDolares("dol",importeTotalSoles,importeTotalDolares,cambioVta));

		// beanDetPagServ.setM_NumSuministro(pagoServForm.getM_NumSuministroIni());
		// serv_pago.setM_NumAbonado(pagoServForm.getM_NumSuministroIni());

		// session.setAttribute("detallePagoServ", beanDetPagServ);
		session.setAttribute("listaResultPagar", listaResult);

		// obtenemos el ruc y el codigo de cliente de la empresa seleccionada
		// String idEmpresaCodCliente = pagoServForm.getM_IdEmpCodCliente();
		String idEmpresaCodCliente = pagoServForm.getM_Empresa();
		String codes[] = idEmpresaCodCliente.split(";");

		serv_pago.setM_Empresa(idEmpresaCodCliente);
		session.setAttribute("serv_pago", serv_pago);

		// GetInfo CashWS - CONS_CTAS_CLIENTE
		CashClientWS cashclienteWS = (CashClientWS) context
				.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
		if (cashclienteWS == null) {
			request.setAttribute("mensaje",
					"Error. No se puede conectar al servicio web del IBS");
			return mapping.findForward("error");
		}
		ArrayList listaParametros = new ArrayList();
		// listaParametros.add(new BeanNodoXML("id_trx",
		// Constantes.IBS_CONS_CTAS_CLIENTE));
		listaParametros.add(new BeanNodoXML("id_trx",
				Constantes.IBS_CONS_CTAS_ASOC_CLIENTE));

		listaParametros.add(new BeanNodoXML("client_code", codes[1]));
		// + parametros
		// jwong 04/04/2009 para manejo del filtro en el tipo de cuenta
		// listaParametros.add(new BeanNodoXML(Constantes.TAG_FILTRO,
		// Constantes.TAG_ACCOUNT_TYPE + "=110,210"));

		String req = GenRequestXML.getXML(listaParametros);
		String resultado = cashclienteWS.ProcesarMensaje(req,
				Constantes.WEB_SERVICE_CASH);

		if (resultado == null || "".equals(resultado)) {
			request.setAttribute("mensaje",
					"Error. El servicio web del IBS no devuelve resultado");
			return mapping.findForward("error");
		}

		BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
		BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session
				.getAttribute("usuarioActual");
		if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
			// procesamos la respuesta(parseo xml) y enviamos a la pagina
			// BeanConsCtasCliente beanctascliente =
			// ParserXML.getConsCtasCliente(beanResXML.getM_Respuesta());
			// BeanConsCtasCliente beanctascliente =
			// ParserXML.getConsCtasCliente(beanResXML.getM_Respuesta(),
			// cashclienteWS, beanDataLogXML.getM_NumTarjeta(),
			// beanDataLogXML.getM_Token(), codes[0]);
			BeanConsCtasCliente beanctascliente = ParserXML
					.getConsCtasClienteCombos(beanResXML.getM_Respuesta());

			List listaccounts = beanctascliente.getM_Accounts();
			session.setAttribute("beanctascliente", beanctascliente);
			session.setAttribute("listaccounts", listaccounts);
			// session.setAttribute("listaTipoMoneda", listaTipoMoneda);
			// pagoServForm.setM_IdEmpCodCliente(pagoServForm.getM_IdEmpCodCliente());
		} else if (beanResXML != null
				&& beanResXML.getM_CodigoRetorno() != null) {// &&
																// beanResXML.getM_DescripcionError()
																// != null &&
																// beanResXML.getM_DescripcionError().length()
																// > 0
			TxResultDao dao = new TxResultDaoHibernate();// jmoreno 27-08-09
			TxResult txResult = dao.selectByCodIbs(beanResXML
					.getM_CodigoRetornoIBS());
			if (txResult != null) {
				request.setAttribute("mensaje", txResult.getDrsDescription());
			} else {
				request.setAttribute("mensaje", beanResXML
						.getM_CodigoRetornoIBS()
						+ ":" + Constantes.CODIGO_RPTA_IBS_DESCONOCIDO);
			}

		} else {
			// mostraremos un mensaje de error en pagina
			request.setAttribute("mensaje",
					"Error. El servicio web del IBS no devuelve resultado");
		}
		return mapping.findForward("cargarDetallePagoClaro");
	}

	public ActionForward pagarClaro(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
		// jmoreno 20-08-09
		String data = request.getParameter("data");

		// obtenemos de session el objeto entidad
		// BeanConsPagoServicio entidadPagoServicio =
		// (BeanConsPagoServicio)session.getAttribute("entidadPagoServicio");
		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");

		PagoServicioForm pagoServForm = (PagoServicioForm) form;
		// obtenemos el ruc y el codigo de cliente de la empresa seleccionada
		// String idEmpresaCodCliente = pagoServForm.getM_IdEmpCodCliente();
		String idEmpresaCodCliente = pagoServForm.getM_Empresa();

		String codes[] = idEmpresaCodCliente.split(";");

		// validamos que la empresa tenga permiso para pago de servicios
		List listaServicios = (List) session.getAttribute("listaservicios");
		// Hiberbate
		TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
		TaServicioxEmpresa objservicioxempresa = servicioxempresaDAO
				.selectServicioxEmpresa(codes[0],
						((BeanServicio) listaServicios.get(0))
								.getM_IdServicio());

		if (objservicioxempresa == null) {
			List alsuccess = new ArrayList();
			BeanSuccess success = new BeanSuccess();

			success.setM_Mensaje("SERVICIO NO AFILIADO");
			String strTitulo = serv_pago.getM_Titulo();
			success.setM_Titulo(strTitulo);
			success.setM_Back("pagoServicio.do?do=cargarProveedor&" + data);
			// success.setM_Back("pagoServicio.do?do=cargarDetallePagoClaro");
			// //jmoreno 20-08-09

			request.setAttribute("success", success);
			request.setAttribute("alsuccess", alsuccess);
			return mapping.findForward("success");
		}

		// PAGO_SERVICIOS
		// campos requeridos para realizar el pago del servicio
		String tipo_entidad = null; // tipo entidad
		String cod_entidad = null; // codigo entidad
		String cod_servicio = null; // Codigo Servicio
		String num_abonado = null; // numero abonado
		String cuenta_cargo = null; // cuenta cargo
		String nom_cliente = null; // nombre cliente
		String ruc = null; // (ruc de la empresa) manejar un combo para varias
							// empresas caso excepcional
		String currency_2 = null; // moneda

		tipo_entidad = serv_pago.getM_Proveedor(); // tipo entidad
		cod_entidad = serv_pago.getM_ServProv(); // codigo entidad
		cod_servicio = serv_pago.getM_Servicio(); // Codigo Servicio
		num_abonado = serv_pago.getM_NumAbonado(); // numero abonado
		ruc = codes[0]; // ruc de la empresa

		// obtenemos el codigo de la cuenta donde se cargara el pago
		String codigoImporte = pagoServForm.getM_CuentaCargo(); // 1ero debemos
																// quitar el
																// importe del
																// codigo
		String codesCtaImp[] = codigoImporte.split(";");
		cuenta_cargo = codesCtaImp[0]; // codigo de la cuenta a cargar con el
										// pago

		String cheksValue[] = request.getParameterValues("m_chkRecibo");
		String valores[] = null;
		double importeRecibo = 0.0;

		BeanConsPagoServicio beanXML;

		// webservice
		// GetInfo CashWS - CONS_CTAS_CLIENTE
		CashClientWS cashclienteWS = (CashClientWS) context
				.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
		if (cashclienteWS == null) {
			request.setAttribute("mensaje",
					"Error. No se puede conectar al servicio web del IBS");
			return mapping.findForward("error");
		}

		boolean isOkTrx = true;
		String mensajeIbs = "";
		for (int h = 0; h < cheksValue.length; h++) {
			valores = cheksValue[h].split(";");
			try {
				// transformamos los importes de los recibos seleccionados en
				// decimales
				importeRecibo = Double.parseDouble(valores[9].replaceAll(" ",
						""));
			} catch (NumberFormatException nfe) {
				logger.error(nfe.toString());
			}
			nom_cliente = null; // nombre cliente

			// amount = pagoServForm.getM_Importe().replaceAll(" ", ""); //monto
			ArrayList listaParametros = new ArrayList();
			listaParametros.add(new BeanNodoXML("id_trx",
					Constantes.IBS_PAGO_SERVICIOS_CLARO));
			// listaParametros.add(new BeanNodoXML("tipo_entidad",
			// serv_pago.getM_CodServProveedor()));
			listaParametros.add(new BeanNodoXML("cod_entidad", serv_pago
					.getM_CodServProveedor()));
			listaParametros.add(new BeanNodoXML("cod_servicio", cod_servicio));
			listaParametros.add(new BeanNodoXML("num_abonado", num_abonado));
			listaParametros.add(new BeanNodoXML("cuenta_cargo", cuenta_cargo));
			// listaParametros.add(new BeanNodoXML("nom_cliente", nom_cliente));
			listaParametros.add(new BeanNodoXML("ruc", ruc));

			// enviamos el codigo de moneda
			currency_2 = Util.monedaSalida(currency_2);
			listaParametros.add(new BeanNodoXML("currency_2", currency_2));
			// formatemos el monto (1.00 --> 100) para manejo de 2 decimales
			String strMonto = Util.formatearMontoSalida(valores[9].replaceAll(
					" ", ""), Constantes.NUMERO_DECIMALES_SALIDA); // por
																	// defecto
																	// se usaran
																	// 2
																	// decimales
			listaParametros.add(new BeanNodoXML("amount", strMonto));

			String req = GenRequestXML.getXML(listaParametros);
			String resultado = cashclienteWS.ProcesarMensaje(req,
					Constantes.WEB_SERVICE_CASH);

			if (resultado == null || "".equals(resultado)) {
				isOkTrx = false;
			} else {
				BeanRespuestaXML beanResXML = ParserXML
						.parsearRespuesta(resultado);
				if (beanResXML != null
						&& "00".equals(beanResXML.getM_CodigoRetorno())) {
					isOkTrx = isOkTrx & true;
				} else {
					if (beanResXML != null
							&& beanResXML.getM_CodigoRetorno() != null) {// &&
																			// beanResXML.getM_DescripcionError()
																			// !=
																			// null
																			// &&
																			// beanResXML.getM_DescripcionError().length()
																			// >
																			// 0
						TxResultDao dao = new TxResultDaoHibernate();
						TxResult txResult = dao.selectByCodIbs(beanResXML
								.getM_CodigoRetornoIBS());
						if (txResult != null) {
							mensajeIbs = txResult.getDrsDescription();
						} else {
							mensajeIbs = beanResXML.getM_CodigoRetornoIBS()
									+ ":"
									+ Constantes.CODIGO_RPTA_IBS_DESCONOCIDO;
						}

					}
					isOkTrx = false;
				}
			}
		}

		// Mensaje Confirmación
		if (isOkTrx) {
			String strTitulo = serv_pago.getM_Titulo();
			String strMensaje = "La transacción ha sido procesada correctamente";
			String strBack = "pagoServicio.do?do=cargarProveedor&" + data;
			// String strBack = "pagoServicio.do?do=cargarPagoClaro";//jmoreno
			// 20-08-09

			List alsuccess = new ArrayList();
			BeanSuccessDetail sucessdetail;

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Tipo de Operación");
			sucessdetail.setM_Mensaje("PAGO DE "
					+ serv_pago.getM_NombreGrupo().toUpperCase());
			alsuccess.add(sucessdetail);

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Fecha / Hora");
			sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   "
					+ Fecha.getFechaActual("HH:mm:ss"));
			alsuccess.add(sucessdetail);

			BeanSuccess success = new BeanSuccess();
			success.setM_Titulo(strTitulo);
			success.setM_Mensaje(strMensaje);
			success.setM_Back(strBack);

			request.setAttribute("success", success);
			request.setAttribute("alsuccess", alsuccess);
			return mapping.findForward("success");
		} else {
			String strTitulo = serv_pago.getM_Titulo();
			String strMensaje = "La transacción no se pudo completar. "
					+ mensajeIbs;
			String strBack = "pagoServicio.do?do=cargarProveedor&" + data;
			// String strBack = "pagoServicio.do?do=cargarPagoClaro";//jmoreno
			// 20-08-09

			List alsuccess = new ArrayList();
			BeanSuccessDetail sucessdetail;

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Tipo de Operación");
			sucessdetail.setM_Mensaje("PAGO DE "
					+ serv_pago.getM_NombreGrupo().toUpperCase());
			alsuccess.add(sucessdetail);

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Fecha / Hora");
			sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   "
					+ Fecha.getFechaActual("HH:mm:ss"));
			alsuccess.add(sucessdetail);

			BeanSuccess success = new BeanSuccess();
			success.setM_Titulo(strTitulo);
			success.setM_Mensaje(strMensaje);
			success.setM_Back(strBack);

			request.setAttribute("success", success);
			request.setAttribute("alsuccess", alsuccess);
			return mapping.findForward("success");
		}
	}

	// SERVICIO PAGO OFFLINE
	public ActionForward cargarPagoOffLine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}

		session.removeAttribute("listaResult");
		((PagoServicioForm) form).setCriterios();

		MessageResources messageResources = getResources(request);

		// cargamos las empresas asociadas resultante del logueo
		List lEmpresa = (List) session.getAttribute("empresa");
		List listaCriterios = null;

		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");

		// Hibernate
		TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
		TaServicioxEmpresaDao servicioDAO = new TaServicioxEmpresaDaoHibernate();
		TaServicioOpcionDao servicio_opcionDAO = new TaServicioOpcionDaoHibernate();
		TaMapaCamposDao mapa_camposDAO = new TaMapaCamposDaoHibernate();
		TaDetalleMapaCamposDao detalle_mapa_camposDAO = new TaDetalleMapaCamposDaoHibernate();
		
		String numTarjeta= (String) session.getAttribute("tarjetaActual");


		 boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
		

		List empresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);
		TaServicioOpcion taservicioopcion = servicio_opcionDAO.select(serv_pago
				.getM_Modulo(), serv_pago.getM_SubModulo());

		if (taservicioopcion == null) {
			request.setAttribute("mensaje", messageResources
					.getMessage("pagos.servicio.error.servicio"));
			return mapping.findForward("error");
		}

		List servicios = servicioDAO.selectServicioxEmpresaxCode((serv_pago
				.getM_Empresa() != null) ? serv_pago.getM_Empresa()
				: ((TmEmpresa) empresas.get(0)).getCemIdEmpresa(),
				taservicioopcion.getId().getCsoservicioId());
		// List servicios = servicioDAO.selectServicioxEmpresaxCode(
		// ((TmEmpresa) empresas.get(0)).getCemIdEmpresa(),
		// taservicioopcion.getId().getCsoservicioId());

		String idCodigo = request.getParameter("m_Codigo");
		String nomProveedor = request.getParameter("m_Nombre"); // Nombre del
																// Proveedor
		String rucProveedor = request.getParameter("m_Ruc"); // Ruc del
																// Proveedor

		if (idCodigo != null)
			serv_pago.setM_Proveedor(idCodigo);
		if (nomProveedor != null)
			serv_pago.setM_NombreProveedor(nomProveedor);
		if (rucProveedor != null)
			serv_pago.setM_RucProveedor(rucProveedor);

		if ((serv_pago.getM_Proveedor() != null)
				&& (serv_pago.getM_RucProveedor() != null)) {
			/*
			 * CashClientWS cashclienteWS =
			 * (CashClientWS)context.getAttribute(Constantes
			 * .CONTEXT_CLIENTE_HOME_BANKING_WS);
			 * cashclienteWS.setOperacion((String
			 * )context.getAttribute("CONS_SERV_Servicio_Operacion2_Nombre"));
			 * 
			 * GetDataIBS gdi = new GetDataIBSFinanciero(); List lparameters =
			 * new ArrayList(); lparameters.add(new BeanNodoXML("idProveedor",
			 * idCodigo)); int ires = 0; listaServProv =
			 * gdi.getServicios_Empresas_Servicio(cashclienteWS, lparameters,
			 * ires);
			 */
			//System.out.println("SELECT EMPRESA BY CODE: "+ serv_pago.getM_RucProveedor());
			TmEmpresa objEmpresa = empresaDAO.selectEmpresaByCode(serv_pago
					.getM_RucProveedor());

			if (objEmpresa != null) {
				TaServicioxEmpresa servicioRecaudacion = servicioDAO
						.selectServicioxEmpresa(objEmpresa.getCemIdEmpresa(),
								Constantes.HQL_CASH_SERVICIO_RECAUDACION_BD);
				// listaCriterios =
				// mapa_camposDAO.selectMapaCampos(servicioRecaudacion.getCsemIdServicioEmpresa());
				if (servicioRecaudacion != null) {
					// jwong 24/04/2009 obtenemos el mapa de campos
					TaMapaCampos mapaCampos = mapa_camposDAO
							.selectMapaCampos(servicioRecaudacion
									.getCsemIdServicioEmpresa());

					if (mapaCampos != null) {
						// jwong 24/04/2009 obtenemos el listado de criterios
						listaCriterios = detalle_mapa_camposDAO
								.selectDetalleMapaCampos(mapaCampos
										.getCmcidMapaCampos());
					} else {
						request.setAttribute("mensaje", messageResources
								.getMessage("pagos.servicio.error.criterio"));
						return mapping.findForward("error");
					}
				} else {
					request.setAttribute("mensaje", messageResources
							.getMessage("pagos.servicio.error.criterio"));
					return mapping.findForward("error");
				}
			} else {
				//System.out.println("Primer Else");
				request.setAttribute("mensaje", messageResources.getMessage("pagos.servicio.error.proveedor"));
				return mapping.findForward("error");
			}
		} else {
			//System.out.println("Segundo Else");
			request.setAttribute("mensaje", messageResources
					.getMessage("pagos.servicio.error.proveedor"));
			return mapping.findForward("error");
		}

		if (listaCriterios != null && listaCriterios.size() > 0) {
			session.setAttribute("listaCriterios", listaCriterios);
			session.setAttribute("serv_pago", serv_pago);
			session.setAttribute("listaempresas", empresas);
			if (empresas.size() > 1) {
				session.setAttribute("bLista", 1);
			}
			session.setAttribute("listaservicios", servicios);
		} else {
			request.setAttribute("mensaje", messageResources
					.getMessage("pagos.servicio.error.criterio"));
			return mapping.findForward("error");
		}

		PagoServicioForm pagoForm = (PagoServicioForm) form;
		pagoForm.reset(mapping, request);
		pagoForm.setM_Proveedor(nomProveedor);
		pagoForm.setM_Codigo(null);
		pagoForm.setM_Nombre(null);

		return mapping.findForward("cargarPagoOffLine");
	}

	public ActionForward cargarPagoDetalleOffLine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletConfig()
				.getServletContext();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}

		BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session
				.getAttribute("usuarioActual");
		// Form
		PagoServicioForm pagoForm = (PagoServicioForm) form;
		String empresa = pagoForm.getM_Empresa();
		String codigo = pagoForm.getM_Codigo();
		String nombre = pagoForm.getM_Nombre();

		Map map_ordenes = ((PagoServicioForm) form).getValues();

		// Sesion
		List listaEmpresas = (List) session.getAttribute("listaempresas");
		List listaServProv = (List) session.getAttribute("listaservprov");
		List listaResult = (List) session.getAttribute("listaResult");
		List listaResultPagar = new ArrayList();
		Set keys = map_ordenes.keySet();

		BigDecimal bmonto = new BigDecimal("0.0").setScale(2);

		for (Iterator i = keys.iterator(); i.hasNext();) {
			String key = (String) i.next();
			String tmp[] = key.split(";");
			String korden = (String) tmp[0];
			String kservicio = (String) tmp[1];
			String kdetorden = (String) tmp[2];

			BeanDetalleOrden objDetOrden = (BeanDetalleOrden) CollectionUtils
					.find(listaResult, new CollectionFilter(korden, kservicio,
							kdetorden, 1));

			if (objDetOrden != null) {
				bmonto = bmonto
						.add((objDetOrden.getM_BigDecMonto() != null) ? objDetOrden
								.getM_BigDecMonto()
								: new BigDecimal(0));
				listaResultPagar.add(objDetOrden);
			}
		}

		TmEmpresa objempresa = (TmEmpresa) CollectionUtils.find(listaEmpresas,
				new CollectionFilter(empresa, 1));
		// BeanRespuestaWSHomeBankingXML objservemp =
		// (BeanRespuestaWSHomeBankingXML)
		// CollectionUtils.find(listaServProv,new CollectionFilter(codServicio,
		// 1));

		String descEmpresa = objempresa.getDemNombre();

		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");
		serv_pago.setM_DescEmpresa(descEmpresa);
		serv_pago.setM_Empresa(empresa);
		// serv_pago.setM_NombreServProv(objservemp.getM_Nombre());
		// serv_pago.setM_ServProv(codServicio);
		serv_pago.setM_MontoTotal(Util.formatearMontoNvo(bmonto.toString()));// jmoreno
																				// 28-08-09
		// serv_pago.setM_MontoTotal(bmonto.toString());
		// WEB SERVICE
		GetDataIBS gdi = new GetDataIBSFinanciero();
		List lparameters = new ArrayList();
		// lparameters.add(new BeanNodoXML("id_trx",
		// Constantes.IBS_CONS_CTAS_CLIENTE));
		lparameters.add(new BeanNodoXML("id_trx",
				Constantes.IBS_CONS_CTAS_ASOC_CLIENTE));

		lparameters.add(new BeanNodoXML("client_code", objempresa
				.getCemCodigoCliente()));
		// lparameters.add(new BeanNodoXML("filtro",
		// Constantes.FILTRO_ACCOUNT_TYPE));
		int ires = 0;
		// BeanConsCtasCliente beanctascliente =
		// gdi.getConsCtasCliente((CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS),
		// lparameters, ires);
		// BeanConsCtasCliente beanctascliente =
		// gdi.getConsCtasCliente((CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS),
		// lparameters, ires, beanDataLogXML.getM_NumTarjeta(),
		// beanDataLogXML.getM_Token(), objempresa.getCemIdEmpresa());
		BeanConsCtasCliente beanctascliente = gdi.getConsCtasCliente(
				(CashClientWS) context
						.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS),
				lparameters, ires);
		List listaccounts = beanctascliente.getM_Accounts();

		session.setAttribute("listaccounts", listaccounts);
		session.setAttribute("serv_pago", serv_pago);
		session.setAttribute("listaResultPagar", listaResultPagar);

		return mapping.findForward("cargarPagoDetalleOffLine");
	}

	// jmoreno 25-08-09 : Para el manejo de la paginacion en los pagos offLine
	public ActionForward buscarPagoOffLinePag(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
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
		// session.removeAttribute("listaResult");
		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");
		List resultado = null;

		// Form
		PagoServicioForm pagoForm = (PagoServicioForm) form;
		String empresa = pagoForm.getM_Empresa();
		String nomProveedor = pagoForm.getM_Proveedor();

		Map criterios = pagoForm.getCriterios();
		serv_pago.setM_Empresa(empresa);
		// Hibernate
		TaOrdenDao ordenDao = new TaOrdenDaoHibernate();

		List listaEmpresas = (List) session.getAttribute("listaempresas");
		List listaCriterios = (List) session.getAttribute("listaCriterios");
		List listaCriteriosFilter = new ArrayList();
		for (Iterator it = listaCriterios.iterator(); it.hasNext();) {
			TaDetalleMapaCampos tadmc = (TaDetalleMapaCampos) it.next();
			String key = String.valueOf(tadmc.getId().getCdmposicion());
			if (criterios.containsKey(key)) {
				tadmc.setValor((String) criterios.get(key));
				listaCriteriosFilter.add(tadmc);
			}
		}

		// TmEmpresa objempresa = empresaDAO.selectEmpresas(empresa);
		TmEmpresa objempresa = (TmEmpresa) CollectionUtils.find(listaEmpresas,
				new CollectionFilter(empresa, 1));
		String descEmpresa = objempresa.getDemNombre();

		resultado = ordenDao.selectOrdenesPendCobro(serv_pago
				.getM_RucProveedor(), descEmpresa.trim(), listaCriteriosFilter,
				bpag);

		if (resultado != null && resultado.size() > 0) // Sucess
		{
			if (Constantes.TIPO_PAG_ANTERIOR.equals(bpag.getM_tipo())
					|| Constantes.TIPO_PAG_ULTIMO.equals(bpag.getM_tipo())) {
				List listaordenOrd = new ArrayList();
				for (int i = resultado.size() - 1; i >= 0; i--) {
					listaordenOrd.add(resultado.get(i));
				}
				resultado = listaordenOrd;
			}
			bpag.setM_primerRegAct((int) (((BeanDetalleOrden) resultado.get(0))
					.getM_ItemDetalle()));
			bpag.setM_ultimoRegAct((int) ((BeanDetalleOrden) resultado
					.get(resultado.size() - 1)).getM_ItemDetalle());
			session.setAttribute("listaResult", resultado);
		} else
			// Error
			request.setAttribute("mensaje", "No se encontraron resultados");

		session.setAttribute("serv_pago", serv_pago);
		((PagoServicioForm) form).setValues();

		return mapping.findForward("cargarPagoOffLine");
	}

	public ActionForward buscarPagoOffLine(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}

		session.removeAttribute("listaResult");

		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");

		List resultado = null;

		// Form
		PagoServicioForm pagoForm = (PagoServicioForm) form;
		String empresa = pagoForm.getM_Empresa();
		String nomProveedor = pagoForm.getM_Proveedor();
		Map criterios = pagoForm.getCriterios();

		serv_pago.setM_Empresa(empresa);
		// Hibernate
		TaOrdenDao ordenDao = new TaOrdenDaoHibernate();

		List listaEmpresas = (List) session.getAttribute("listaempresas");
		List listaCriterios = (List) session.getAttribute("listaCriterios");
		List listaCriteriosFilter = new ArrayList();
		for (Iterator it = listaCriterios.iterator(); it.hasNext();) {
			// jwong 24/04/2009
			// TaMapaCampos tmc = (TaMapaCampos) it.next();
			TaDetalleMapaCampos tadmc = (TaDetalleMapaCampos) it.next();

			// String key = String.valueOf(tmc.getId().getCmcposicion());
			String key = String.valueOf(tadmc.getId().getCdmposicion());
			if (criterios.containsKey(key)) {
				// tmc.setValor((String) criterios.get(key));
				// listaCriteriosFilter.add(tmc);
				tadmc.setValor((String) criterios.get(key));
				listaCriteriosFilter.add(tadmc);
			}
		}

		// TmEmpresa objempresa = empresaDAO.selectEmpresas(empresa);
		TmEmpresa objempresa = (TmEmpresa) CollectionUtils.find(listaEmpresas,
				new CollectionFilter(empresa, 1));
		String descEmpresa = objempresa.getDemNombre();
		TpDetalleOrdenDao detalleDao = new TpDetalleOrdenDaoHibernate();
		long numTotalReg = detalleDao.obtenerCantItemsCobro(serv_pago
				.getM_RucProveedor(), listaCriterios);
		MessageResources messageResources = getResources(request);
		int nroRegPag = Integer.parseInt(messageResources
				.getMessage("paginacion.pagoservicios"));
		int nroPag = (int) numTotalReg / nroRegPag;
		int resto = (int) numTotalReg % nroRegPag;
		if (resto != 0) {
			nroPag = nroPag + 1;
		}
		BeanPaginacion bpag = new BeanPaginacion();
		bpag.setM_pagActual(1);
		bpag.setM_pagFinal(nroPag);
		bpag.setM_pagInicial(1);
		bpag.setM_regPagina(nroRegPag);
		bpag.setM_resto(resto);
		bpag.setM_tipo(Constantes.TIPO_PAG_PRIMERO);

		resultado = ordenDao.selectOrdenesPendCobro(serv_pago
				.getM_RucProveedor(), descEmpresa.trim(), listaCriteriosFilter,
				bpag);

		if (resultado != null && resultado.size() > 0) // Sucess
		{
			bpag.setM_primerRegAct((int) (((BeanDetalleOrden) resultado.get(0))
					.getM_ItemDetalle()));
			bpag.setM_ultimoRegAct((int) ((BeanDetalleOrden) resultado
					.get(resultado.size() - 1)).getM_ItemDetalle());
			session.setAttribute("listaResult", resultado);
		} else
			// Error
			request.setAttribute("mensaje", "No se encontraron resultados");

		session.setAttribute("beanPag", bpag);
		session.setAttribute("serv_pago", serv_pago);
		((PagoServicioForm) form).setValues();

		return mapping.findForward("cargarPagoOffLine");
	}

	public ActionForward pagarOffLine(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}
		// Session
		BeanPagoServicio serv_pago = (BeanPagoServicio) session
				.getAttribute("serv_pago");
		List listaServicios = (List) session.getAttribute("listaservicios");
		List listaResultPagar = (List) session.getAttribute("listaResultPagar");
		List listaaccounts = (List) session.getAttribute("listaccounts");
		PagoServicioForm pagoForm = (PagoServicioForm) form;
		String account = pagoForm.getM_CuentaCargo();

		BeanAccount objAccount = (BeanAccount) CollectionUtils.find(
				listaaccounts, new CollectionFilter(account, 1));

		// Hiberbate
		TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
		TaSecuencialDao taSecdao = new TaSecuencialDaoHibernate();
		TaOrdenDao ordenDAO = new TaOrdenDaoHibernate();
		TpDetalleOrdenDao detalleordenDAO = new TpDetalleOrdenDaoHibernate();

		TaServicioxEmpresa objservicioxempresa = servicioxempresaDAO
				.selectServicioxEmpresa(serv_pago.getM_Empresa(),
						((BeanServicio) listaServicios.get(0))
								.getM_IdServicio());

		if (objservicioxempresa == null) {
			MessageResources messageResources = getResources(request);
			List alsuccess = new ArrayList();
			BeanSuccess success = new BeanSuccess();

			success.setM_Mensaje("SERVICIO NO AFILIADO");
			String titulo = serv_pago.getM_NombreGrupo().substring(0, 1)
					.toUpperCase()
					+ serv_pago.getM_NombreGrupo().substring(1);
			success.setM_Titulo(messageResources.getMessage(
					"pagos.servicio.title", new Object[] { titulo }));
			success.setM_Back("pagoServicio.do?do=cargarPagoOffLine");
			// success.setM_Back("pagoServicio.do?do=cargarPagoDetalleOffLine");//jmoreno
			// 16-12-09

			request.setAttribute("success", success);
			request.setAttribute("alsuccess", alsuccess);
			return mapping.findForward("success");
		}

		// BeanOrden
		TaOrden taorden = new TaOrden();
		int idEnvio = taSecdao
				.getIdEnvio(Constantes.FIELD_CASH_SECUENCIAL_ID_ORDEN);
		taorden.setId(new TaOrdenId(idEnvio, objservicioxempresa
				.getCsemIdServicioEmpresa()));
		taorden.setForFechaRegistro(Fecha.getFechaActual("yyyyMMdd"));
		taorden.setForFechaInicio(Fecha.getFechaActual("yyyyMMdd"));
		taorden.setForFechaFin(Fecha.getFechaActual("yyyyMMdd"));
		taorden.setHorHoraInicio(Fecha.getFechaActual("HHmmss"));
		taorden.setNorNumeroCuenta(objAccount.getM_AccountCode());
		taorden.setDorTipoCuenta(objAccount.getM_AccountType());
		taorden.setCorEstadoMontoDolares('0');// jmoreno 24-08-09 - Para que el
												// batch genere los cargos
		taorden.setCorEstadoMontoEuros('0');
		taorden.setCorEstadoMontoSoles('0');
		if (String.valueOf(Constantes.HQL_CASH_FLAG_APROB_AUT_ENABLED)
				.equalsIgnoreCase(
						String.valueOf(objservicioxempresa
								.getCsemFlagAprobAut())))
			taorden.setCorEstado(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
		else
			taorden.setCorEstado(Constantes.HQL_CASH_ESTADO_ORDEN_INGRESADO);
		taorden.setNorNumeroRegistros(0);
		// ... (taOrden)
		// Guarda la ORDEN PAGO DE SERVICIO
		boolean bguardar = ordenDAO.insert(taorden);
		if (bguardar) {
			List listadetorden = new ArrayList();

			BeanDetalleOrden beandetorden;
			long index = Constantes.CODE_BASE_IDORDEN;
			for (Iterator it = listaResultPagar.iterator(); it.hasNext();) {
				beandetorden = (BeanDetalleOrden) it.next();

				TpDetalleOrden tpdetalleorden = new TpDetalleOrden();
				tpdetalleorden.setId(new TpDetalleOrdenId(taorden.getId()
						.getCorIdOrden(), taorden.getId()
						.getCorIdServicioEmpresa(), index + 1));

				tpdetalleorden.setNdonumeroCuenta(account);
				tpdetalleorden.setNdomonto(beandetorden.getM_BigDecMonto());
				tpdetalleorden.setCdomoneda(beandetorden.getM_IdTipoMoneda());
				// tpdetalleorden.setDdoreferencia(beantransf.getM_Referencia());
				tpdetalleorden
						.setCdoestado(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO);

				// Referencia a la orden de cobro
				tpdetalleorden.setCdoidOrdenRef(Long.parseLong(beandetorden
						.getM_IdOrden()));
				tpdetalleorden.setCdoidDetalleOrdenRef(Long
						.parseLong(beandetorden.getM_IdDetalleOrden()));
				// tpdetalleorden.setDdoadicional3(String.valueOf(index));

				// Data Duplicada para consulta de comprobantes
				tpdetalleorden.setDdonombre(beandetorden.getM_NomCliente());
				tpdetalleorden.setNdodocumento(beandetorden.getM_NumRecibo());
				tpdetalleorden
						.setDdoreferencia(beandetorden.getM_Descripcion());
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
				// return mapping.findForward("guardar");
			} else {
				request.setAttribute("mensaje",
						"No se pudo generar la orden correctamente");
				return mapping.findForward("error");
			}

			if (bguardar) {
				MessageResources messageResources = getResources(request);
				List alsuccess = new ArrayList();
				BeanSuccess success = new BeanSuccess();

				success
						.setM_Mensaje(messageResources
								.getMessage("pagos.servicioOffLine.confirmacion.title"));
				String titulo = serv_pago.getM_NombreGrupo().substring(0, 1)
						.toUpperCase()
						+ serv_pago.getM_NombreGrupo().substring(1);
				success.setM_Titulo(messageResources.getMessage(
						"pagos.servicio.title", new Object[] { titulo }));
				success.setM_Back("pagoServicio.do?do=cargarPagoOffLine");

				alsuccess.add(new BeanSuccessDetail(messageResources
						.getMessage("pagos.servicio.confirmacion.operacion"),
						"PAGO DE SERVICIOS"));
				alsuccess.add(new BeanSuccessDetail(messageResources
						.getMessage("pagos.servicio.confirmacion.codigo"),
						taorden.getId().getCorIdOrden() + ""));
				alsuccess.add(new BeanSuccessDetail(messageResources
						.getMessage("pagos.confirmacion.date"), Fecha
						.getFechaActual("dd/MM/yy")
						+ "   " + Fecha.getFechaActual("HH:mm:ss")));

				request.setAttribute("success", success);
				request.setAttribute("alsuccess", alsuccess);
				// return mapping.findForward("success");
			} else {
				request
						.setAttribute("mensaje",
								"Error. Ocurrio un error al registrar la transferencia.");
				return mapping.findForward("error");
			}
		} else {
			request.setAttribute("mensaje", "No se encontraron resultados");
			return mapping.findForward("error");
		}

		return mapping.findForward("success");
	}

	public ActionForward cargarProveedor(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletConfig()
				.getServletContext();

		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		} else {
			idModulo = request.getParameter("modulo");
			try {
				if (!delegadoSeguridad.verificaDisponibilidad(idModulo)) {
					return mapping.findForward("fueraServicio");
				}
			} catch (Exception e) {
				logger.error("VALIDACION DE DISPONIBILIDAD", e);
				return mapping.findForward("fueraServicio");
			}
		}

		BeanPagoServicio serv_pago = new BeanPagoServicio();

		String titulo = null;
		List listaEmpProveedor = null;

		String m_Grupo = request.getParameter("grupo"); // codigo del grupo
		String m_NombreGrupo = request.getParameter("nombre"); // nombre del
																// grupo
		String m_CodEntidad = request.getParameter("codentidad"); // codigo de
																	// entidad

		serv_pago.setM_Grupo(m_Grupo);
		serv_pago.setM_NombreGrupo(m_NombreGrupo);
		serv_pago.setM_Modulo((String) request.getParameter("modulo"));
		serv_pago.setM_SubModulo((String) request.getParameter("submodulo"));

		String data = "grupo=" + m_Grupo + "&nombre=" + m_NombreGrupo
				+ "&codentidad=" + m_CodEntidad + "&modulo="
				+ serv_pago.getM_Modulo() + "&submodulo="
				+ serv_pago.getM_SubModulo();

		request.setAttribute("data", data);

		if (m_Grupo != null) {
			CashClientWS cashclienteWS = (CashClientWS) context
					.getAttribute(Constantes.CONTEXT_CLIENTE_HOME_BANKING_WS);
			cashclienteWS.setOperacion((String) context
					.getAttribute("CONS_SERV_Servicio_Operacion1_Nombre"));
			GetDataIBS gdi = new GetDataIBSFinanciero();
			List lparameters = new ArrayList();

			lparameters.add(new BeanNodoXML("id_TipoProveedor", m_Grupo));
			int ires = 0;
			listaEmpProveedor = gdi.getEmpresas_Servicio(cashclienteWS,lparameters, ires);

			titulo = "Pago del Servicio de "	+ m_NombreGrupo.substring(0, 1).toUpperCase()+ m_NombreGrupo.substring(1);
			serv_pago.setM_Titulo(titulo);
			session.setAttribute("titulo", titulo);
		}

		if (listaEmpProveedor != null && listaEmpProveedor.size() > 0) {
			TaEmpresaDeServicioDao empresa_servicioDAO = new TaEmpresaDeServicioDaoHibernate();
			Map empresa_servicio = empresa_servicioDAO.selectEmpresaDeServicio(Integer.parseInt(m_Grupo));

			for (Iterator it = listaEmpProveedor.iterator(); it.hasNext();) {
				BeanRespuestaWSHomeBankingXML brh = (BeanRespuestaWSHomeBankingXML) it
						.next();
				if (empresa_servicio != null
						&& empresa_servicio.containsKey(brh.getM_Codigo())) {
					serv_pago.setM_CodTipoEntidad(m_CodEntidad);
					String m_accion = (String) empresa_servicio.get(brh
							.getM_Codigo())
							+ "&" + data;
					brh.setM_Accion(m_accion);
				} else
					brh.setM_Accion("pagoServicio.do?do=cargarPagoOffLine");
			}
			// request.setAttribute("listaEmpProveedor", listaEmpProveedor);
			session.setAttribute("listaEmpProveedor", listaEmpProveedor);
			session.setAttribute("serv_pago", serv_pago);
		} else {
			request.setAttribute("mensaje", "No se encontraron resultados");
		}

		// Remove Session
		session.removeAttribute("listaResult");

		return mapping.findForward("cargarProveedor");
	}


}

package com.hiper.cash.actions;

import static com.hiper.cash.util.CashConstants.RES_IBS;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.financiero.cash.delegate.SeguridadDelegate;
import com.financiero.cash.des.TripleDES;
import com.financiero.cash.exception.NotAvailableException;
import com.financiero.cash.exception.NotFoundException;
import com.financiero.cash.ibs.util.IbsUtils;
import com.financiero.cash.util.VariablesSession;
import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.clienteWS.GenRequestXML;
import com.hiper.cash.dao.MensajesDAO;
import com.hiper.cash.dao.TarjetasDAO;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.TxListFieldDao;
import com.hiper.cash.dao.TxParameterDao;
import com.hiper.cash.dao.UsuariosDAO;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TxListFieldDaoHibernate;
import com.hiper.cash.dao.hibernate.TxParameterDaoHibernate;
import com.hiper.cash.dao.jdbc.MensajesDAOImpl;
import com.hiper.cash.dao.jdbc.TarjetasDAOJDBC;
import com.hiper.cash.dao.jdbc.UsuariosDAOImpl;
import com.hiper.cash.dao.ws.SixLinux;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.domain.TxListField;
import com.hiper.cash.domain.TxParameter;
import com.hiper.cash.entidad.BeanUsuarioCaptura;
import com.hiper.cash.entidad.Propiedades;
import com.hiper.cash.forms.LoginForm;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;
import com.hiper.cash.xml.bean.BeanDataLoginXML;
import com.hiper.cash.xml.bean.BeanNodoXML;
import com.hiper.cash.xml.bean.BeanRespuestaWSLoginXML;
import com.hiper.cash.xml.bean.BeanRespuestaXML;
import com.hiper.cash.xml.parser.ParserXML;

public class AutenticarAction extends DispatchAction {

	private static Logger logger = Logger.getLogger(AutenticarAction.class);
	private static final String CONTENT_TYPE = "application/xml";
	private SeguridadDelegate delegadoSeguridad = SeguridadDelegate
			.getInstance();
	private TmEmpresaDao empresaDao = new TmEmpresaDaoHibernate();

	private boolean validarNumeroTarjeta(String numeroTarjeta) {
		if (numeroTarjeta.length() != 16) {
			return false;
		}
		try {
			char c;
			for (int i = 0; i < numeroTarjeta.length(); i++) {
				c = numeroTarjeta.charAt(i);
				Integer.parseInt(String.valueOf(c));
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private void cargarDataUsuario(HttpSession session,
			BeanRespuestaXML beanResXML, BeanRespuestaWSLoginXML beanLoginXML) {
		XMLOutputter outputter = new XMLOutputter();
		outputter.getFormat().setEncoding("windows-1252");
		session.setAttribute("xmlLogin",
				outputter.outputString(beanResXML.getM_Respuesta()));
		HashMap hMapEmpresas = new HashMap();
		BeanDataLoginXML beanDataLogXML1 = ParserXML.obtenerDataLogin(
				beanResXML.getM_Respuesta(), hMapEmpresas);
		session.setAttribute("usuarioActual", beanDataLogXML1);
	}

	private void cargarDataEmpresa(HttpSession session, String numTarjeta,
			String clave, BeanRespuestaXML beanResXML,
			BeanRespuestaWSLoginXML beanLoginXML, Propiedades prop) {
		XMLOutputter outputter = new XMLOutputter();
		outputter.getFormat().setEncoding("windows-1252");
		session.setAttribute("xmlLogin",
				outputter.outputString(beanResXML.getM_Respuesta()));
		HashMap hMapEmpresas = new HashMap();
		BeanDataLoginXML beanDataLogXML1 = ParserXML.obtenerDataLogin(
				beanResXML.getM_Respuesta(), hMapEmpresas);
		if (beanDataLogXML1.getL_Empresas().contains("00000000000")) {
			TmEmpresaDao empresaDao = new TmEmpresaDaoHibernate();
			beanDataLogXML1.getL_Empresas().clear();
			beanDataLogXML1.getL_Empresas().add(
					empresaDao.selectCodEmpresas("00000000000"));
		}

		session.setAttribute("hmEmpresas", hMapEmpresas);
		String fecha = Fecha.getFechaActual("EEEE, MMMM dd, yyyy");
		fecha = fecha.substring(0, 1).toUpperCase() + fecha.substring(1);

		beanDataLogXML1.setM_Fecha(fecha);
		String hora = Fecha.getFechaActual("HH:mm:ss");
		beanDataLogXML1.setM_Hora(hora);

		beanDataLogXML1.setM_MsjBienvenida("Bienvenido");
		beanDataLogXML1.setM_PaginaClose("Salir");
		beanDataLogXML1.setM_PaginaInicio("Inicio");

		beanDataLogXML1.setM_NumTarjeta(numTarjeta);
		beanDataLogXML1.setM_Clave(clave);

		beanDataLogXML1.setM_Token(beanLoginXML.getM_Token());
		session.setAttribute("usuarioActual", beanDataLogXML1);
		session.setAttribute("tarjetaActual", beanDataLogXML1.getM_NumTarjeta());
		session.setAttribute("timeTipoCambio", prop.getM_TimeTipoCambio());
		session.setAttribute("empresa", beanDataLogXML1.getL_Empresas());
	}

	private String getUsuario(BeanDataLoginXML data) {
		StringBuilder sb = new StringBuilder(data.getM_Apellido()).append(" ")
				.append(data.getM_Nombre());
		return sb.toString();
	}

	public ActionForward validarTCO(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		MessageResources messageResources = getResources(request);
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletConfig()
				.getServletContext();
		Propiedades prop = (Propiedades) context.getAttribute("propiedades");
		try {
			String clave = request.getParameter("password");
			String tco = (String) session.getAttribute("tco");
			String coordenada = (String) session.getAttribute("coordenada");

			/*
			 * 
			 * if (this.delegadoSeguridad.validarCoordenada(tco, coordenada,
			 * clave, prop.getLimiteIntentosTCO())) { return
			 * mapping.findForward("iniciarOpciones"); } else {
			 * logger.error("Clave de la coordenada es erroneo");
			 * request.setAttribute("error_tco",
			 * messageResources.getMessage("tco.error.ingresarClave"));
			 * request.setAttribute("coordenadaUI",
			 * SeguridadDelegate.codificarCoordenada(coordenada)); return
			 * mapping.findForward("iniciarCoordenada"); }
			 */

			boolean validarTCO = this.delegadoSeguridad.validarCoordenada(tco,
					coordenada, clave, prop.getLimiteIntentosTCO());

			// forzar la validacion de tarjeta TCO
			// validarTCO = true;

			if (validarTCO) {
				return mapping.findForward("iniciarOpciones");
			} else {
				logger.error("Clave de la coordenada es erroneo");
				request.setAttribute("error_tco",
						messageResources.getMessage("tco.error.ingresarClave"));
				request.setAttribute("coordenadaUI",
						SeguridadDelegate.codificarCoordenada(coordenada));
				return mapping.findForward("iniciarCoordenada");
			}

		} catch (NotAvailableException e) {
			request.setAttribute("error_tco",
					messageResources.getMessage("tco.error.bloqueoIntentos"));
			logger.error(e.getMessage(), e);
			return mapping.findForward("errorCoordenada");
		} catch (Exception e) {
			request.setAttribute("error_tco",
					messageResources.getMessage("tco.error"));
			logger.error(e.getMessage(), e);
		}
		return mapping.findForward("errorCoordenada");
	}

	public ActionForward iniciarSesion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		MessageResources messageResources = getResources(request);
		ServletContext context = getServlet().getServletConfig()
				.getServletContext();
		CashClientWS cashclienteWS = (CashClientWS) context
				.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
		if (cashclienteWS == null) {
			session.removeAttribute("xmlLogin");
			logger.error("Web Service Cash no se pudo conectar");
			request.setAttribute("error_login",
					messageResources.getMessage("login.error.webServiceCash"));
			return mapping.findForward("error");
		}
		if (estaSistemaActivo(cashclienteWS)) {

			LoginForm lf = (LoginForm) form;
			String numTarjeta = lf.getNumeroTarjeta();
			String clave = lf.getContrasegnia();
			clave = TripleDES.encriptar(clave);
			String resultado = "";
			List<BeanNodoXML> listaParametros = new ArrayList<BeanNodoXML>();

			lf.setValidaClaveSeis("");

			BeanNodoXML beanNodo = null;
			String req = null;
			try {
				if (numTarjeta == null || clave == null) {
					request.setAttribute("error_login",
							messageResources.getMessage("login.error.nulo"));
					return mapping.findForward("error");
				} else {
					if (!validarNumeroTarjeta(numTarjeta)) {
						request.setAttribute("error_login", messageResources
								.getMessage("login.error.numeroTarjeta"));
						return mapping.findForward("error");
					}
					if (clave.length() == 0) {
						request.setAttribute("error_login", messageResources
								.getMessage("login.error.contrasegnia"));
						return mapping.findForward("error");
					}
				}
				CashClientWS clienteLoginWS = (CashClientWS) context
						.getAttribute("clienteLoginWS");
				if (clienteLoginWS == null) {
					session.removeAttribute("xmlLogin");
					logger.error("Web Service Login no se puede conectar");
					request.setAttribute("error_login", messageResources
							.getMessage("login.error.webServiceLogin"));
					return mapping.findForward("error");
				}
				clienteLoginWS.setOperacion((String) context
						.getAttribute("LOG_BCO_Servicio_Operacion_Nombre"));
				beanNodo = new BeanNodoXML("EPad", numTarjeta.trim());
				listaParametros.add(beanNodo);
				beanNodo = new BeanNodoXML("EPin", clave.trim());
				listaParametros.add(beanNodo);
				beanNodo = new BeanNodoXML("strURL", request.getRequestURL()
						.toString());
				listaParametros.add(beanNodo);
				beanNodo = new BeanNodoXML("strFormulario", "");
				listaParametros.add(beanNodo);
				beanNodo = new BeanNodoXML("strQueryString",
						request.getQueryString());
				listaParametros.add(beanNodo);
				beanNodo = new BeanNodoXML("strServerName",
						request.getServerName());
				listaParametros.add(beanNodo);
				beanNodo = new BeanNodoXML("strUserAgent",
						(String) request.getParameter("agenteUsuario"));
				listaParametros.add(beanNodo);
				beanNodo = new BeanNodoXML("strUserIP", request.getRemoteAddr());
				listaParametros.add(beanNodo);
				beanNodo = new BeanNodoXML("strUserHostName",
						request.getRemoteHost());
				listaParametros.add(beanNodo);
				beanNodo = new BeanNodoXML("strUserIsAutenticado", "");
				listaParametros.add(beanNodo);
				beanNodo = new BeanNodoXML("strUserName", "");
				listaParametros.add(beanNodo);

				req = GenRequestXML.getXMLLogin(listaParametros);
				
				logger.info("req=>"+req);
				
				resultado = clienteLoginWS.ProcesarMensaje(req,
						Constantes.WEB_SERVICE_BANCO);

				if (resultado != null) {
					logger.info("<login>" + resultado + "</login>");
				} else {
					logger.error("Resultado del Web Service Login es Nulo");
					request.setAttribute("error_login", messageResources
							.getMessage("login.error.webServiceLogin"));
					return mapping.findForward("error");
				}

				BeanRespuestaWSLoginXML beanLoginXML = ParserXML
						.parsearRespuestaWSLogin(resultado);

				/*
				 * se comenta para forzar que se ingrese con la tarjeta de cash
				 * ingresada
				 */

				// beanLoginXML.setM_CodRptaTx(Constantes.IBS_CODE_OK_LOGIN);				
				
				logger.info("==>numTarjeta:"+numTarjeta);
				logger.info("==>getM_CodRptaTx:"+beanLoginXML.getM_CodRptaTx());
				logger.info("==>getM_DescRptaTx:"+beanLoginXML.getM_DescRptaTx());

				if (beanLoginXML != null
						&& Constantes.IBS_CODE_OK_LOGIN.equals(beanLoginXML
								.getM_CodRptaTx())) {
					

					listaParametros = new ArrayList<BeanNodoXML>();
					beanNodo = new BeanNodoXML("id_trx", "LOGIN_CASH");
					listaParametros.add(beanNodo);
					beanNodo = new BeanNodoXML("cod_lang", "es");
					listaParametros.add(beanNodo);
					beanNodo = new BeanNodoXML("ntarjeta", numTarjeta);
					listaParametros.add(beanNodo);
					beanNodo = new BeanNodoXML("password", clave);
					listaParametros.add(beanNodo);
					req = GenRequestXML.getXML(listaParametros);

					resultado = cashclienteWS.ProcesarMensaje(req,
							Constantes.WEB_SERVICE_CASH);

					if (resultado == null || "".equals(resultado)) {
						session.removeAttribute("xmlLogin");
						logger.error("Resultado del Web Service Cash es Nulo");
						request.setAttribute("error_login", messageResources
								.getMessage("login.error.webServiceCash"));
						return mapping.findForward("error");
					}

					TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();

					if (empresaDAO.verificaSiTarjetaCash(numTarjeta)) {
						session.setAttribute("perfilCash", "S");
					} else {
						session.setAttribute("perfilCash", "N");
					}

					BeanUsuarioCaptura busuario=null;
					try{
						UsuariosDAO dUsuario = new UsuariosDAOImpl();
						busuario = dUsuario.getDatosUsuarioByTarjeta(numTarjeta);
						
						logger.info("nro tarje="+busuario.getNroTarjeta());
						logger.info("FlagMigra="+busuario.getFlagMigra());
						
					}catch(Exception exx){
						logger.error("Error......."+exx.getMessage());
						exx.printStackTrace();
					}

					
					
					BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
					if (beanResXML != null) {
						session.setAttribute("beanResXML", beanResXML);
						if ("00".equals(beanResXML.getM_CodigoRetorno())) {
							Propiedades prop = (Propiedades) context.getAttribute("propiedades");
							try {
								if (prop.isConsultaTCO()) {
									if (delegadoSeguridad
											.esUsuarioSoloConsulta(numTarjeta)) {

										cargarDataEmpresa(session, numTarjeta,
												clave, beanResXML,
												beanLoginXML, prop);

										// si la tarjeta es de solo consultas se
										// va directamente al menu principal
										return mapping
												.findForward("iniciarOpciones");

									} else {

										// si no ingresa a la ventana del modulo
										// de TCO

										String tco = delegadoSeguridad
												.obtenerTCO(numTarjeta);
										try {
											if (this.delegadoSeguridad
													.validarActividadTCO(
															tco,
															prop.getLimiteIntentosTCO(),
															prop.getNumeroBloqueosTCO())) {
												cargarDataEmpresa(session,
														numTarjeta, clave,
														beanResXML,
														beanLoginXML, prop);
												session.setAttribute("tco", tco);
												String coordenada = delegadoSeguridad
														.generarCoordenada();
												session.setAttribute(
														"coordenada",
														coordenada);
												request.setAttribute(
														"coordenadaUI",
														SeguridadDelegate
																.codificarCoordenada(coordenada));
												session.setAttribute(
														"usuarioTCO",
														getUsuario((BeanDataLoginXML) session
																.getAttribute("usuarioActual")));
												return mapping
														.findForward("iniciarCoordenada");
											} else {
												cargarDataUsuario(session,
														beanResXML,
														beanLoginXML);
												request.setAttribute(
														"error_tco",
														messageResources
																.getMessage("tco.error.bloqueoIntentos"));
												request.setAttribute(
														"usuarioTCO",
														getUsuario((BeanDataLoginXML) session
																.getAttribute("usuarioActual")));
												return mapping
														.findForward("errorCoordenada");
											}
										} catch (NotFoundException e) {
											logger.error(e.getMessage(), e);
											cargarDataUsuario(session,
													beanResXML, beanLoginXML);
											request.setAttribute(
													"usuarioTCO",
													getUsuario((BeanDataLoginXML) session
															.getAttribute("usuarioActual")));
											request.setAttribute(
													"error_tco",
													messageResources
															.getMessage("tco.error.bloqueoConsecutivo"));
											return mapping
													.findForward("errorCoordenada");
										}
									}
								} else {
									cargarDataEmpresa(session, numTarjeta,
											clave, beanResXML, beanLoginXML,
											prop);
									return mapping
											.findForward("iniciarOpciones");
								}
							} catch (NotFoundException e) {
								logger.error(e.getMessage(), e);
								cargarDataUsuario(session, beanResXML,
										beanLoginXML);
								request.setAttribute("usuarioTCO",
										getUsuario((BeanDataLoginXML) session
												.getAttribute("usuarioActual")));
								request.setAttribute(
										"error_tco",
										messageResources
												.getMessage("tco.error.tarjetaCancelada"));
								return mapping.findForward("errorCoordenada");
							} catch (NotAvailableException e) {
								logger.error(e.getMessage(), e);
								if (!prop.isConsultaAfiliacionTCO()) {
									cargarDataEmpresa(session, numTarjeta,
											clave, beanResXML, beanLoginXML,
											prop);
									return mapping
											.findForward("iniciarOpciones");
								} else {
									cargarDataUsuario(session, beanResXML,
											beanLoginXML);
									request.setAttribute(
											"usuarioTCO",
											getUsuario((BeanDataLoginXML) session
													.getAttribute("usuarioActual")));
									request.setAttribute(
											"error_tco",
											messageResources
													.getMessage("tco.error.tarjetaNoAfiliada"));
									return mapping
											.findForward("errorCoordenada");
								}
							} catch (Exception e) {
								logger.error(e.toString(), e);
								request.setAttribute(
										"error_login",
										messageResources
												.getMessage("login.error.webServiceCash"));
								return mapping.findForward("error");
							}
						} else {
							session.removeAttribute("xmlLogin");
							StringBuilder sb = new StringBuilder("Descripcion Errror Web Service Cash : ").append(beanResXML.getM_DescripcionError());
							logger.error(sb);
							sb = new StringBuilder("Detalle Errror Web Service Cash : ").append(beanResXML.getM_DescripcionError());
							logger.error(sb);
							//request.setAttribute("error_login",messageResources.getMessage("login.error.webServiceCash"));
							if(busuario!=null){
								if(busuario.getFlagMigra().equals("S")){									
									request.setAttribute("error_login",messageResources.getMessage("login.error.usuarioMigrado"));
								}else{
									request.setAttribute("error_login",messageResources.getMessage("login.error.webServiceCash"));
								}
								
							}else{
								logger.error("Error el bean esta nulo");
								request.setAttribute("error_login",messageResources.getMessage("login.error.webServiceCash"));
							}
							
							
							return mapping.findForward("error");
						}
					} else {
						session.removeAttribute("xmlLogin");
						request.setAttribute("error_login", messageResources
								.getMessage("login.error.webServiceCash"));
						logger.error("Error paresendo respuesta de Web Service Cash");
						return mapping.findForward("error");
					}
				} else {
					if (beanLoginXML != null
							&& beanLoginXML.getM_DescRptaTx() != null) {

						String mensaje = "";

						if (beanLoginXML.getM_CodRptaTx().equals("1003")) {
							// request.setAttribute("error_login",
							// messageResources.getMessage("login.error.clave.error"));
							mensaje = messageResources
									.getMessage("login.error.clave.error");

						} else if (beanLoginXML.getM_CodRptaTx()
								.equals("99001")) {
							mensaje = messageResources
									.getMessage("login.error.clave.bloqueada");

						} else if (beanLoginXML.getM_CodRptaTx().equals("1004")) {

							lf.setValidaClaveSeis("NO");
							mensaje = messageResources
									.getMessage("login.error.clave.singenerar6digitos");

						} else {
							mensaje = beanLoginXML.getM_DescRptaTx().toString();
						}

						session.removeAttribute("xmlLogin");
						// request.setAttribute("error_login",
						// beanLoginXML.getM_DescRptaTx());
						request.setAttribute("error_login", mensaje);

						StringBuilder sb = new StringBuilder(
								"Descripcion Errror Web Service Cash: ")
								.append(beanLoginXML.getM_DescRptaTx());
						logger.error(sb);

					} else {
						session.removeAttribute("xmlLogin");
						StringBuilder sb = new StringBuilder(
								"La tarjeta numero ").append(numTarjeta)
								.append("no se valido correctamente");
						request.setAttribute("error_login", messageResources
								.getMessage("login.error.webServiceCash"));
						logger.error(sb);
					}
					return mapping.findForward("error");
				}
			} catch (Exception ex) {
				logger.error(ex.toString(), ex);
				session.removeAttribute("xmlLogin");
				request.setAttribute("error_login", messageResources
						.getMessage("login.error.webServiceCash"));
				return mapping.findForward("error");
			}
		} else {
			// logger.error(ex.to);
			session.removeAttribute("xmlLogin");
			request.setAttribute("error_login",
					messageResources.getMessage("login.error.webServiceCash"));
			return mapping.findForward("error");
		}

	}

	public ActionForward cerrarSesion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		logger.info("Cerarr Sesion");
		HttpSession session = request.getSession();
		session.setAttribute("beanDataLogXML", null); // Elimina la sesion
		session.setAttribute("perfilCash", null);
		session.setAttribute("tarjetaActual", null);

		session.invalidate(); // Invalida la sesion
		logger.info("Cerro sesion");
		return mapping.findForward("loguot"); // retorna
	}

	public ActionForward obtenerOpciones(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		logger.info("Obetener Opciones");
		HttpSession session = request.getSession();
		String xml = (String) session.getAttribute("xmlLogin");

		// Escribimos el xml al flujo del response
		response.setContentType(CONTENT_TYPE);
		// para que el navegador no utilice su cache para mostrar los datos
		/*
		 * response.addHeader("Cache-Control","no-cache"); //HTTP 1.1
		 * response.addHeader("Pragma","no-cache"); //HTTP 1.0
		 * response.setDateHeader("Expires", 0); //prevents caching at the proxy
		 * server
		 */
		response.addHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.addHeader("Pragma", "public"); // HTTP 1.0
		// escribimos el resultado en el flujo de salida
		PrintWriter out = response.getWriter();
		out.println(xml);
		out.flush();

		return null; // no retornamos nada
	}

	// jwong envio de descripcion de error(en caso existiera)
	public ActionForward obtenerError(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		logger.info("Obtener Error");
		HttpSession session = request.getSession();
		String xml = "<?xml version=\"1.0\" encoding=\"windows-1252\"?>";
		xml = xml + "<errores>";

		String msjLogin = (String) session.getAttribute("msjLogin");
		String titMsjLogin = (String) session.getAttribute("titMsjLogin");

		if (msjLogin != null) { // entonces existe error
			xml = xml + "<m_Existe>00</m_Existe>" + "<m_Descripcion>"
					+ msjLogin + "</m_Descripcion>" + "<m_Detalle>"
					+ titMsjLogin + "</m_Detalle>";
		} else {
			xml = xml + "<m_Existe>99</m_Existe>";
		}
		xml = xml + "</errores>";

		// Escribimos el xml al flujo del response
		response.setContentType(CONTENT_TYPE);
		// para que el navegador no utilice su cache para mostrar los datos
		/*
		 * response.addHeader("Cache-Control","no-cache"); //HTTP 1.1
		 * response.addHeader("Pragma","no-cache"); //HTTP 1.0
		 * response.setDateHeader("Expires", 0); //prevents caching at the proxy
		 * server
		 */
		response.addHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.addHeader("Pragma", "public"); // HTTP 1.0

		// escribimos el resultado en el flujo de salida
		PrintWriter out = response.getWriter();
		out.println(xml);
		out.flush();

		return null; // no retornamos nada
	}

	// jwong 16/03/2009 manejo del cambio de clave
	public ActionForward cambiarClave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletConfig()
				.getServletContext();

		logger.info("Cambiar Clave");

		// si termino la session debemos retornar al inicio
		if (session.getAttribute("usuarioActual") == null) {
			response.sendRedirect("cierraSession.jsp");
			return null;
		}
		// //////////////////////////////////////////////////////////////////////////////
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
		// server
		// //////////////////////////////////////////////////////////////////////////////

		// obtenemos el numero de tarjeta encriptado de la session
		BeanDataLoginXML dataLogin = (BeanDataLoginXML) session
				.getAttribute("usuarioActual");

		// obtenemos los parametros enviados para realizar el cambio de clave
		String claveAnterior = (String) request.getParameter("claveAnterior");
		String claveNueva = (String) request.getParameter("claveNueva");
		String claveConfirm = (String) request
				.getParameter("claveConfirmacion");

		BeanRespuestaXML beanResXML = null;
		String resultado = "";
		try {
			CashClientWS clienteLoginPasswordWS = (CashClientWS) context
					.getAttribute("clienteLoginWS");
			// CashClientWS clienteLoginPasswordWS =
			// (CashClientWS)context.getAttribute("clienteSeguridadWS");
			// CashClientWS clienteLoginPasswordWS =
			// (CashClientWS)context.getAttribute("cashclienteWS");

			clienteLoginPasswordWS.setOperacion((String) context
					.getAttribute("LOG_BCO_OP_CambioClave"));
			// clienteLoginPasswordWS.setOperacion((String)context.getAttribute("SEG_BCO_OP_CambioClave"));

			ArrayList listaParametros = new ArrayList();
			BeanNodoXML beanNodo = null;

			beanNodo = new BeanNodoXML(Constantes.TAG_EPAD,
					dataLogin.getM_NumTarjeta()); // numero de tarjeta
													// encriptada
			listaParametros.add(beanNodo);

			beanNodo = new BeanNodoXML(Constantes.TAG_EAPIN, claveAnterior); // clave
			// anterior
			// encriptada
			listaParametros.add(beanNodo);

			beanNodo = new BeanNodoXML(Constantes.TAG_ENPIN, claveNueva); // clave
			// nueva
			// encriptada
			listaParametros.add(beanNodo);

			beanNodo = new BeanNodoXML(Constantes.TAG_ECPIN, claveConfirm); // confirmacion
			// de
			// clave
			// encriptada
			listaParametros.add(beanNodo);

			// String req = GenRequestXML.getXML(listaParametros);
			String req = GenRequestXML.getXMLLogin(listaParametros);

			resultado = clienteLoginPasswordWS.ProcesarMensaje(req,
					Constantes.WEB_SERVICE_BANCO);

			if (resultado != null && !resultado.equals("")) {
				BeanRespuestaWSLoginXML beanLoginXML = ParserXML
						.parsearRespuestaWSLogin(resultado);
				if (beanLoginXML != null
						&& Constantes.IBS_CODE_OK_LOGIN.equals(beanLoginXML
								.getM_CodRptaTx())) {
					// msjLoginChange
					session.setAttribute("msjLoginChange",
							"Se ha cambiado su clave con éxito.");
				} else if (beanLoginXML != null
						&& beanLoginXML.getM_DescRptaTx() != null) {
					session.setAttribute("msjLoginChange",
							beanLoginXML.getM_DescRptaTx());
				} else {
					session.setAttribute("msjLoginChange",
							"Error al cambiar su clave, inténtelo mas tarde.");
				}
			} else {
				logger.error("CambiarClave: RESULTADO del WEBSERVICE LOGIN es nulo");
				session.setAttribute("msjLoginChange",
						"Error al cambiar su clave, inténtelo mas tarde.");
			}

		} catch (Exception ex) {
			logger.error("ERROR:" + ex.toString());
			// jwong 16/03/2009 seteamos en session el bean con los mensajes de
			// error
			session.setAttribute("msjLoginChange",
					"Error al cambiar su clave, inténtelo mas tarde.");
			// return mapping.findForward("error");
		}
		return mapping.findForward("cambioPass");
	}

	// jwong envio de descripcion de error(en caso existiera)
	public ActionForward obtenerErrorChangePass(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		logger.info("Obtener Error Chabg Pass");
		HttpSession session = request.getSession();
		String xml = "<?xml version=\"1.0\" encoding=\"windows-1252\"?>";
		xml = xml + "<errores>";

		String msjLogin = (String) session.getAttribute("msjLoginChange");
		if (msjLogin != null) { // entonces existe error
			xml = xml + "<m_Existe>00</m_Existe>" + "<m_Descripcion>"
					+ msjLogin + "</m_Descripcion>" + "<m_Detalle></m_Detalle>";
		} else {
			xml = xml + "<m_Existe>99</m_Existe>";
		}
		xml = xml + "</errores>";

		// eliminamos de la session el mensaje
		session.removeAttribute("msjLoginChange");

		// Escribimos el xml al flujo del response
		response.setContentType(CONTENT_TYPE);
		// para que el navegador no utilice su cache para mostrar los datos
		/*
		 * response.addHeader("Cache-Control","no-cache"); //HTTP 1.1
		 * response.addHeader("Pragma","no-cache"); //HTTP 1.0
		 * response.setDateHeader("Expires", 0); //prevents caching at the proxy
		 * server
		 */
		response.addHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.addHeader("Pragma", "public"); // HTTP 1.0
		// escribimos el resultado en el flujo de salida
		PrintWriter out = response.getWriter();
		out.println(xml);
		out.flush();

		return null; // no retornamos nada
	}

	// jwong envio de llaves para la encriptacion
	public ActionForward obtenerKeys(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		logger.info("Obtener KEYS");
		// //System.out.println("Obtener  Keysssssssssssssssssss");

		// buscaremos en base de datos las llaves para ser enviadas al
		// ActionScript
		// obtenemos el listado de llaves
		TxParameterDao parameterDAO = new TxParameterDaoHibernate();
		TxParameter key1 = parameterDAO.selectById(Constantes.FIELD_CASH_KEY1); // key
		// 1
		TxParameter key2 = parameterDAO.selectById(Constantes.FIELD_CASH_KEY2); // key
		// 2
		TxParameter key3 = parameterDAO.selectById(Constantes.FIELD_CASH_KEY3); // key
		// 3

		String xml = "<?xml version=\"1.0\" encoding=\"windows-1258\"?>";
		xml = xml + "<keys>";

		if (key1 != null && key2 != null && key3 != null) { // si se encontraron
			// las 3 llaves
			logger.info("SE OBTUVO: LLAVE 1, LLAVE 2, LLAVE 3");
			xml = xml + "<m_Key1>" + key1.getDpmDescription() + "</m_Key1>"
					+ "<m_Key2>" + key2.getDpmDescription() + "</m_Key2>"
					+ "<m_Key3>" + key3.getDpmDescription() + "</m_Key3>";
		}
		xml = xml + "</keys>";

		// //System.out.println("XML: " +xml);

		// Escribimos el xml al flujo del response
		response.setContentType(CONTENT_TYPE);
		// para que el navegador no utilice su cache para mostrar los datos
		/*
		 * response.addHeader("Cache-Control","no-cache"); //HTTP 1.1
		 * response.addHeader("Pragma","no-cache"); //HTTP 1.0
		 * response.setDateHeader("Expires", 0); //prevents caching at the proxy
		 * server
		 */
		response.addHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.addHeader("Pragma", "public"); // HTTP 1.0

		// escribimos el resultado en el flujo de salida
		PrintWriter out = response.getWriter();
		out.println(xml);
		out.flush();

		return null; // no retornamos nada
	}

	// jwong 17/03/2009 generacion de nueva clave
	public ActionForward generarClave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		logger.info("Generar Clave");
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletConfig()
				.getServletContext();

		/*
		 * //si termino la session debemos retornar al inicio if
		 * (session.getAttribute("usuarioActual") == null) {
		 * response.sendRedirect("cierraSession.jsp"); return null; }
		 */

		// obtenemos el numero de tarjeta encriptado de la session
		// BeanDataLoginXML dataLogin =
		// (BeanDataLoginXML)session.getAttribute("usuarioActual");

		// obtenemos los parametros enviados para realizar el cambio de clave
		String numTarjeta = (String) request.getParameter("numTarjeta");
		String claveTarjeta = (String) request.getParameter("claveTarjeta");
		// String claveAnterior = (String)request.getParameter("claveAnterior");
		String claveNueva = (String) request.getParameter("claveNueva");
		String claveConfirm = (String) request
				.getParameter("claveConfirmacion");

		TarjetasDAO daoTarj = new TarjetasDAOJDBC();

		int validador = 0;
		validador = daoTarj.obtenerDiasAfilicacion(numTarjeta, 30);

		if (validador > 0) {

			request.setAttribute("mensajeTarjeta", numTarjeta);
			return mapping.findForward("cargaCreaClave2");

		} else {

			String resultado = "";
			try {
				// CashClientWS clienteLoginPasswordWS =
				// (CashClientWS)context.getAttribute("clienteLoginWS");
				CashClientWS clienteLoginPasswordWS = (CashClientWS) context
						.getAttribute("clienteSeguridadWS");

				// clienteLoginPasswordWS.setOperacion((String)context.getAttribute("LOG_BCO_Servicio_Operacion_Genera_Clave"));
				clienteLoginPasswordWS.setOperacion((String) context
						.getAttribute("SEG_BCO_OP_GeneraClave"));
				ArrayList listaParametros = new ArrayList();
				BeanNodoXML beanNodo = null;

				beanNodo = new BeanNodoXML(Constantes.TAG_EPAD, numTarjeta); // numero
				// de
				// tarjeta
				// encriptada
				listaParametros.add(beanNodo);

				beanNodo = new BeanNodoXML(Constantes.TAG_EAPIN, claveTarjeta); // clave
				// tarjeta
				// encriptada
				listaParametros.add(beanNodo);

				beanNodo = new BeanNodoXML(Constantes.TAG_ENPIN, claveNueva); // clave
				// nueva
				// encriptada
				listaParametros.add(beanNodo);

				beanNodo = new BeanNodoXML(Constantes.TAG_ECPIN, claveConfirm); // confirmacion
				// de
				// clave
				// encriptada
				listaParametros.add(beanNodo);

				String req = GenRequestXML.getXMLLogin(listaParametros);// getXML
																		// -
				// jmoreno
				// 17-08-09
				resultado = clienteLoginPasswordWS.ProcesarMensaje(req,
						Constantes.WEB_SERVICE_BANCO);

				if (resultado != null && !resultado.equals("")) {
					BeanRespuestaWSLoginXML beanLoginXML = ParserXML
							.parsearRespuestaWSLogin(resultado);
					if (beanLoginXML != null
							&& Constantes.IBS_CODE_OK_LOGIN.equals(beanLoginXML
									.getM_CodRptaTx())) {
						// msjLoginChange
						session.setAttribute("msjLoginCreaPass",
								"Se ha creado su clave con éxito.");
					} else if (beanLoginXML != null
							&& beanLoginXML.getM_DescRptaTx() != null) {

						if (beanLoginXML.getM_CodRptaTx().equals("2007")) {
							session.setAttribute("msjLoginCreaPass",
									"Contraseña de 4 dígitos no es correcta");
						} else {
							session.setAttribute("msjLoginCreaPass",
									beanLoginXML.getM_DescRptaTx());
						}

					} else {
						session.setAttribute("msjLoginCreaPass",
								"Error al crear su clave, inténtelo mas tarde.");
					}
				} else {
					logger.error("RESULTADO del WEBSERVICE LOGIN es nulo");
					session.setAttribute("msjLoginCreaPass",
							"Error al crear su clave, inténtelo mas tarde.");
				}

			} catch (Exception ex) {
				logger.error("ERROR:" + ex.toString());
				// jwong 16/03/2009 seteamos en session el bean con los mensajes
				// de
				// error
				session.setAttribute("msjLoginCreaPass",
						"Error al crear su clave, inténtelo mas tarde.");
				// return mapping.findForward("error");
			}
			return mapping.findForward("creaClave");

		}// end if
	}

	// jwong envio de descripcion de error(en caso existiera)
	public ActionForward obtenerErrorCrearPass(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		logger.info("Obtener Error Crear Pass");
		HttpSession session = request.getSession();
		String xml = "<?xml version=\"1.0\" encoding=\"windows-1252\"?>";
		xml = xml + "<errores>";

		String msjLogin = (String) session.getAttribute("msjLoginCreaPass");
		if (msjLogin != null) { // entonces existe error
			xml = xml + "<m_Existe>00</m_Existe>" + "<m_Descripcion>"
					+ msjLogin + "</m_Descripcion>" + "<m_Detalle></m_Detalle>";
		} else {
			xml = xml + "<m_Existe>99</m_Existe>";
		}
		xml = xml + "</errores>";
		// eliminamos de la session el mensaje
		session.removeAttribute("msjLoginCreaPass");
		// Escribimos el xml al flujo del response
		response.setContentType(CONTENT_TYPE);
		// para que el navegador no utilice su cache para mostrar los datos
		/*
		 * response.addHeader("Cache-Control","no-cache"); //HTTP 1.1
		 * response.addHeader("Pragma","no-cache"); //HTTP 1.0
		 * response.setDateHeader("Expires", 0); //prevents caching at the proxy
		 * server
		 */
		response.addHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.addHeader("Pragma", "public"); // HTTP 1.0
		// escribimos el resultado en el flujo de salida
		PrintWriter out = response.getWriter();
		out.println(xml);
		out.flush();

		return null; // no retornamos nada
	}

	// jwong 27/08/2009 para iniciar el login sin mensajes de error
	public ActionForward iniClean(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		logger.info("Ini Clean");
		HttpSession session = request.getSession();
		session.removeAttribute("msjLogin");
		session.removeAttribute("titMsjLogin");

		return mapping.findForward("loguot");
	}

	public ActionForward obtenerOpcionesEmpresas(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		logger.info("Obtener Opciones Empresas");
		HttpSession session = request.getSession();
		List empresas = (List) session.getAttribute("empresa");
		// System.out.println("Lista de empresassss: " + empresas.size());

		String numTarjeta = (String) session.getAttribute("tarjetaActual");

		TmEmpresaDao empresa = new TmEmpresaDaoHibernate();

		// boolean swverifica= empresa.verificaSiTarjetaCash(numTarjeta);

		List listaTemp = null;
		listaTemp = (List) session.getAttribute("listaTemp");

		// List lista = empresa.listarEmpresa(swverifica,empresas);
		List lista = empresa.listarEmpresa(empresas);

		// System.out.println("Lista TamaÑo: " + lista.size());

		if (listaTemp == null || listaTemp.isEmpty()) {
			listaTemp = lista;
		}

		if (lista.size() == 1) {
			lista = listaTemp;
		}

		session.setAttribute("listaTemp", listaTemp);
		// System.out.println("Lista de empresas TEMP: " + listaTemp.size());
		request.setAttribute("listaEmpresas", lista);
		// System.out.println("Lista de empresas request: " + lista.size());

		List<TmEmpresa> empre = (List<TmEmpresa>) lista;

		if (lista.size() == 1) {
			// //System.out.println("Empresa " +
			// empre.get(0).getCemIdEmpresa());
		}

		return mapping.findForward("opciones");

	}

	public ActionForward menuEmpresa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		logger.info("Menu Empresas");
		String id_empresa = request.getParameter("cem");
		String req = null;
		String resultado = "";
		HttpSession session = request.getSession();
		session.setAttribute(CashConstants.PARAM_ID_EMPRESA, id_empresa);
		if (StringUtils.isNotBlank(id_empresa)) {
			TmEmpresa empresa = empresaDao.selectEmpresaByCode(id_empresa);
			if (empresa != null) {
				session.setAttribute(
						VariablesSession.NOMBRE_EMPRESA_SELECCIONADA
								.getDescripcion(), empresa.getDemNombre()
								.toUpperCase());
			}
		}
		BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session
				.getAttribute("usuarioActual");
		// empresa_code
		ServletContext context = getServlet().getServletConfig()
				.getServletContext();
		ArrayList listaParametros = new ArrayList();
		BeanNodoXML beanNodo = null;

		// Hasta aqui se comenta para obviar la autentificacion
		CashClientWS cashclienteWS = (CashClientWS) context
				.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
		if (cashclienteWS == null) {
			session.removeAttribute("xmlLogin");
			session.setAttribute("msjLogin",
					"Error. No se puede conectar al servicio web del Cash");
			session.setAttribute("titMsjLogin", "Error Web Service Cash");
			return mapping.findForward("error");
		}

		listaParametros = new ArrayList();

		// añadimos cada uno de los parametros usados en el logueo
		beanNodo = new BeanNodoXML("id_trx", "LOGIN_CASH");
		listaParametros.add(beanNodo);

		beanNodo = new BeanNodoXML("cod_lang", "es");
		listaParametros.add(beanNodo);

		beanNodo = new BeanNodoXML("ntarjeta", beanDataLogXML.getM_NumTarjeta());
		listaParametros.add(beanNodo);
		// / ////System.out.println("mi claveeeee " +
		// beanDataLogXML.getM_Clave());

		beanNodo = new BeanNodoXML("password", beanDataLogXML.getM_Clave());
		listaParametros.add(beanNodo);

		beanNodo = new BeanNodoXML("id_empresa", id_empresa);
		listaParametros.add(beanNodo);

		req = GenRequestXML.getXML(listaParametros);

		// //System.out.println("Servicio LOginnnnnnnnnnnnnnnnnnnnn");
		resultado = cashclienteWS.ProcesarMensaje(req,
				Constantes.WEB_SERVICE_CASH);

		// //System.out.println("Resultadooooooo......." + resultado);

		if (resultado == null || "".equals(resultado)) {

			// deberiamos retornar a la pagina de logueo con un mensaje de error
			session.removeAttribute("xmlLogin");
			session.setAttribute("msjLogin",
					"Error al recibir la respuesta del Host");
			session.setAttribute("titMsjLogin", "Error Host");
			return mapping.findForward("error");
		}

		// se debe parsear el xml obtenido
		BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
		if (beanResXML != null) {
			session.setAttribute("beanResXML", beanResXML);
			if ("00".equals(beanResXML.getM_CodigoRetorno())) {
				// colocaremos en session el xml de respuesta(en forma de
				// String)
				XMLOutputter outputter = new XMLOutputter();
				outputter.getFormat().setEncoding("windows-1252");

				session.setAttribute("xmlLogin",
						outputter.outputString(beanResXML.getM_Respuesta()));
				session.removeAttribute("msjLogin"); // quitamos el mensaje de
				// error en el logueo en
				// caso exista
				session.removeAttribute("titMsjLogin"); // quitamos el titulo
				// del mensaje de error
				// en caso exista

				// obtenemos la data relacionada con el user logueado
				HashMap hMapEmpresas = new HashMap();// donde se obtendrá los
				// servicios afiliados
				// por empresa
				BeanDataLoginXML beanDataLogXML1 = ParserXML.obtenerDataLogin(
						beanResXML.getM_Respuesta(), hMapEmpresas);
				// guardamos en session el hashMap de servicios por empresa
				session.setAttribute("hmEmpresas", hMapEmpresas);
				// antes de guardar en sesion la data del login, debemos setear
				// la hora y la fecha y el mensaje de bienvenida
				String fecha = Fecha.getFechaActual("EEEE, MMMM dd, yyyy"); // obtiene
				// la
				// fecha
				// cambiamos la primera letra de la fecha a mayuscula
				fecha = fecha.substring(0, 1).toUpperCase()
						+ fecha.substring(1);

				beanDataLogXML1.setM_Fecha(fecha);
				String hora = Fecha.getFechaActual("HH:mm:ss"); // obtiene la
				// hora
				beanDataLogXML1.setM_Hora(hora);

				beanDataLogXML1.setM_MsjBienvenida("Bienvenido"); // saludo de
				// Bienvenida
				beanDataLogXML1.setM_PaginaClose("Salir"); // link para cerrar
				// session
				beanDataLogXML1.setM_PaginaInicio("Inicio"); // link para pagina
				// de inicio

				// esilva
				beanDataLogXML1.setM_NumTarjeta(beanDataLogXML
						.getM_NumTarjeta());

				beanDataLogXML1.setM_Token(beanDataLogXML.getM_Token());// se
				// comenta
				// para
				// obviar
				// el
				// login

				// ahora seteamos el bean en session
				session.setAttribute("usuarioActual", beanDataLogXML1);

				// /Guardo numero tarjeta en Session
				session.setAttribute("tarjetaActual",
						beanDataLogXML1.getM_NumTarjeta());
				//

				// jwong 29/12/2008
				// seteamos en session el tiempo entre actualizacion del tipo de
				// cambio
				Propiedades prop = (Propiedades) context
						.getAttribute("propiedades");
				session.setAttribute("timeTipoCambio",
						prop.getM_TimeTipoCambio());

				// setetamos en session la cuenta de la empresa
				session.setAttribute("empresa", beanDataLogXML1.getL_Empresas());
				return mapping.findForward("menu");
			} else {
				// deberiamos retornar a la pagina de logueo con un mensaje de
				// error
				session.removeAttribute("xmlLogin");
				session.setAttribute("msjLogin",
						beanResXML.getM_DescripcionError());
				session.setAttribute("titMsjLogin",
						beanResXML.getM_DetalleError());
				return mapping.findForward("error");
			}
		} else {
			// deberiamos retornar a la pagina de logueo con un mensaje de error
			session.removeAttribute("xmlLogin");
			session.setAttribute("msjLogin",
					"Error al recibir la respuesta del Host");
			session.setAttribute("titMsjLogin", "Error Host");
			return mapping.findForward("error");
		}

		// return mapping.findForward("menu");
	}

	private boolean estaSistemaActivo(CashClientWS cashWS) {
		String mensajeError = "";
		String cashWSTrama = "<data><request><id_trx>CONS_TIPO_CAMBIO</id_trx><ruc>00000000000</ruc></request></data>";
		SixLinux sixLinux = SixLinux.getInstance();
		String cm = RES_IBS
				.getString("consultas.relacionesBanco.obtenerCuentas.app");
		String trx = RES_IBS
				.getString("consultas.relacionesBanco.obtenerCuentas.trx");
		short length = Short.parseShort(RES_IBS
				.getString("consultas.relacionesBanco.obtenerCuentas.length"));
		String message = "";
		try {
			String response = cashWS.procesarMensaje(cashWSTrama);
			Document doc = Util.parse(response, false);
			Element raiz = doc.getRootElement();
			Element nodoReturn = (Element) raiz.getChild("return");
			Element nodoHeader = nodoReturn.getChild("RespuestaCASH").getChild(
					"Header");
			Element nodoError = nodoReturn.getChild("RespuestaCASH").getChild(
					"Error");
			String codigoRetornoIBS = nodoHeader
					.getChildTextTrim("CodigoRetornoIBS");
			if (nodoError == null) {
				if (IbsUtils.esRespuestaExitosa(codigoRetornoIBS)) {
					logger.info("Sistema Cash Management Operando Correctamente");
					return true;
				} else {
					try {
						logger.error(response);
						response = sixLinux.enviarMensaje(cm, trx, length,
								message);
					} catch (Exception e) {
						if (e instanceof WebServiceException) {
							logger.error("Six Linux No Disponible - El servicio web del IBS no responde");
						} else {
							logger.error("Error Desconocido");
						}
						logger.error(e, e);
					}
				}
			} else {
				String errorDescripcion = nodoError
						.getChildTextTrim("descripcion");
				String errorDetalle = nodoError.getChildTextTrim("detalle");
				logger.error(response);
				if (codigoRetornoIBS.equals("M")) {
					response = sixLinux.enviarMensaje(cm, trx, length, message);
					if (response.contains(trx) && response.contains(message)) {
						logger.error("IBS No Disponible: Las colas se encuentran inactivas");
					} else {
						logger.error("IBS No disponible");
					}
					logger.error(new StringBuilder("Descripcion: ")
							.append(errorDescripcion));
					logger.error(new StringBuilder("Detalle: ")
							.append(errorDetalle));
					logger.error(new StringBuilder("Codigo Retorno IBS: ")
							.append(codigoRetornoIBS));
				} else {
					logger.error("HiperCenter No Disponible");
					logger.error(new StringBuilder("Descripcion: ")
							.append(errorDescripcion));
					logger.error(new StringBuilder("Detalle: ")
							.append(errorDetalle));
					logger.error(new StringBuilder("Codigo Retorno IBS: ")
							.append(codigoRetornoIBS));
				}
			}
		} catch (WebServiceException ex) {
			if (ex.getCause() == null) {
				mensajeError = "La configuracion de la ruta del CashWS es erronea";
			} else {
				if (ex.getCause() instanceof UnknownHostException) {
					mensajeError = "El balanceador no es visible desde la ubicacion actual";

				}
				if (ex.getCause() instanceof ConnectException) {
					mensajeError = "El servicio del balanceador no se encuentra disponible";
					logger.error(mensajeError);
				}
			}
			logger.error(mensajeError);
			logger.error(ex.getMessage(), ex);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
		return false;
	}

	public ActionForward cargaCreaClave(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		logger.info("Cargar Generar Clave");
		HttpSession session = request.getSession();
		ServletContext context = getServlet().getServletConfig()
				.getServletContext();

		try {
			session.setAttribute("msjLoginCreaPass", "");
		} catch (Exception ex) {
			logger.error("ERROR:" + ex.toString());
			session.setAttribute("msjLoginCreaPass",
					"Error al crear su clave, inténtelo mas tarde.");
		}
		return mapping.findForward("cargaCreaClave");
	}

	public ActionForward iniciarCapturaDatosUsuario(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		logger.info("Obtener Opciones Empresas");

		HttpSession session = request.getSession();
		List empresas = (List) session.getAttribute("empresa");
		// System.out.println("Lista de empresassss: " + empresas.size());

		String numTarjeta = (String) session.getAttribute("tarjetaActual");

		
		//aqui registro el login del usuario en el log
		
		
		//dMensaje.registrarMensaje(programa, tipoEnt, servicio, mensaje);;
		
		
		TmEmpresaDao empresa = new TmEmpresaDaoHibernate();

		// boolean swverifica= empresa.verificaSiTarjetaCash(numTarjeta);

		List listaTemp = null;
		listaTemp = (List) session.getAttribute("listaTemp");

		// List lista = empresa.listarEmpresa(swverifica,empresas);
		List lista = empresa.listarEmpresa(empresas);

		logger.info("Lista TamaÑo: " + lista.size());

		if (listaTemp == null || listaTemp.isEmpty()) {
			listaTemp = lista;
		}

		if (lista.size() == 1) {
			lista = listaTemp;
		}

		session.setAttribute("listaTemp", listaTemp);
		logger.info("Lista de empresas TEMP: " + listaTemp.size());
		request.setAttribute("listaEmpresas", lista);
		logger.info("Lista de empresas request: " + lista.size());

		List<TmEmpresa> empre = (List<TmEmpresa>) lista;

		if (lista.size() == 1) {
			logger.info("Empresa " +empre.get(0).getCemIdEmpresa());
		}

		String empresaDefault = "";
		String empresaDefaultID = "";
		String empresaDefaultRuc = "";

		for (int i = 0; i < empre.size(); i++) {

			empresaDefault = empre.get(i).getDemNombre();
			empresaDefaultID = empre.get(i).getCemIdEmpresa();
			empresaDefaultRuc = empre.get(i).getcEmRUC();

		}

		logger.info("Empresa Default: " + empresaDefault);

		session.setAttribute("m_frm_numeroTarjeta", numTarjeta);
		session.setAttribute("m_frm_empresa", empresaDefault);
		session.setAttribute("m_frm_ruc", empresaDefaultRuc);
		session.setAttribute("m_frm_codcliente", empresaDefaultID);
		

		UsuariosDAO dUsuario = new UsuariosDAOImpl();

		BeanUsuarioCaptura busuario = dUsuario.getDatosUsuarioByTarjeta(numTarjeta);
		
		
		//correo del usuario
		session.setAttribute("m_frm_usuario_correo", busuario.getEmail());		
		session.setAttribute("m_frm_usuario_apellidos", busuario.getApellidos());
		session.setAttribute("m_frm_usuario_nombres", busuario.getNombres());
		

		if (empresaDefaultID.equals("00000000000")) {
			return mapping.findForward("iniciarOpcionesComun");
		} else {

			if (busuario.getTipoDoc() == null
					|| busuario.getTipoDoc().equals("")
					|| busuario.getNroDoc() == null
					|| busuario.getNroDoc().equals("")
					|| busuario.getEmail() == null
					|| busuario.getEmail().equals("")) {

				return mapping.findForward("registrarDatosUsuario");

			} else {
				
				
				try {
					
					TxListFieldDao listaCampoDAO = new TxListFieldDaoHibernate();
					@SuppressWarnings("unchecked")
					List<TxListField> listaCampoVal = listaCampoDAO
							.selectListFieldByFieldName3(
									Constantes.CAMPO_LISTA_VALIDA_MIGRACION_NOMBRE,
									Constantes.CAMPO_LISTA_VALIDA_MIGRACION_CODE);
					
					String swValidarMigracion="";
					for(int k=0; k< listaCampoVal.size();k++ ){
						swValidarMigracion=listaCampoVal.get(k).getDlfDescription();
					}
					
					if(swValidarMigracion.equals(Constantes.CAMPO_LISTA_VALIDA_MIGRACION_VALOR)){
						logger.info("Se inicia el proceso de validacion.............................");						
						logger.info("Usuario: "+numTarjeta+" ==> "+dUsuario.validarSiUsuarioMigro(numTarjeta));
						
						boolean swUsuarioMigro=false;
						boolean swEmpresaMigro=false;
						
						swUsuarioMigro=dUsuario.validarSiUsuarioMigro(numTarjeta);
						swEmpresaMigro=dUsuario.validarSiEmpresaMigro(busuario.getIdEmpresa());
						
						logger.info("Usuario Migrado="+swUsuarioMigro);
						logger.info("Empresa Migrada="+swEmpresaMigro);
						
						//if(dUsuario.validarSiUsuarioMigro(numTarjeta) || dUsuario.validarSiEmpresaMigro(busuario.getIdEmpresa()) ){
						if(swUsuarioMigro && swEmpresaMigro ){
							//return mapping.findForward("");
							return mapping.findForward("registrarMigracionUsuario");	
						}else{
							return mapping.findForward("iniciarOpcionesComun");
						}
						
					}else{
						return mapping.findForward("iniciarOpcionesComun");
					}

				} catch (Exception ex1) {
					logger.error("Error en proceso de Login: "+ex1.getMessage());	
					return mapping.findForward("iniciarOpcionesComun");
				}	
				
				
				//return mapping.findForward("iniciarOpcionesComun");
			}		

		}

	}

	public ActionForward validarDatosUsuario(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		logger.info("Obtener Opciones Empresas");
		HttpSession session = request.getSession();
		List empresas = (List) session.getAttribute("empresa");
		// System.out.println("Lista de empresassss: " + empresas.size());

		String numTarjeta = (String) session.getAttribute("tarjetaActual");

		TmEmpresaDao empresa = new TmEmpresaDaoHibernate();

		// boolean swverifica= empresa.verificaSiTarjetaCash(numTarjeta);

		List listaTemp = null;
		listaTemp = (List) session.getAttribute("listaTemp");

		// List lista = empresa.listarEmpresa(swverifica,empresas);
		List lista = empresa.listarEmpresa(empresas);

		// System.out.println("Lista TamaÑo: " + lista.size());

		if (listaTemp == null || listaTemp.isEmpty()) {
			listaTemp = lista;
		}

		if (lista.size() == 1) {
			lista = listaTemp;
		}

		session.setAttribute("listaTemp", listaTemp);
		// System.out.println("Lista de empresas TEMP: " + listaTemp.size());
		request.setAttribute("listaEmpresas", lista);
		// System.out.println("Lista de empresas request: " + lista.size());

		List<TmEmpresa> empre = (List<TmEmpresa>) lista;

		if (lista.size() == 1) {
			// //System.out.println("Empresa " +
			// empre.get(0).getCemIdEmpresa());
		}

		if (request.getParameter("cboTipoDoc") != null
				&& request.getParameter("txtNroDoc") != null
				&& request.getParameter("txtEmail") != null
				&& request.getParameter("cboOperador") != null
				&& request.getParameter("txtNroCelular") != null
				&& request.getParameter("txtNombres") != null
				&& request.getParameter("txtApellidos") != null) {

			BeanUsuarioCaptura busuario = new BeanUsuarioCaptura();

			busuario.setTipoDoc(request.getParameter("cboTipoDoc").trim());
			busuario.setNroDoc(request.getParameter("txtNroDoc").trim());
			busuario.setEmail(request.getParameter("txtEmail").trim());
			busuario.setOperador(request.getParameter("cboOperador").trim());
			busuario.setNroMovil(request.getParameter("txtNroCelular").trim());

			busuario.setNombres(request.getParameter("txtNombres").trim()
					.toUpperCase());
			busuario.setApellidos(request.getParameter("txtApellidos").trim()
					.toUpperCase());

			busuario.setNroTarjeta(numTarjeta);

			logger.info("Tipo Doc:" + busuario.getTipoDoc());
			logger.info("nro Doc:" + busuario.getNroDoc());
			logger.info("email:" + busuario.getEmail());
			logger.info("operador:" + busuario.getOperador());
			logger.info("celular:" + busuario.getNroMovil());
			logger.info("ID Tarjeta:" + busuario.getNroTarjeta());

			UsuariosDAO duser = new UsuariosDAOImpl();

			if (!duser.registrarDatosUsuario(busuario)) {
				logger.error("Error en registro de datos de Captura de Usuarios...");

			}

		} else {
			logger.error("datos de captura estan en blanco...");

		}

		//aqui se deberia capturar para validar si el usuario pertenece a una empresa ya migrada
		
		try {
			
			TxListFieldDao listaCampoDAO = new TxListFieldDaoHibernate();
			@SuppressWarnings("unchecked")
			List<TxListField> listaCampoVal = listaCampoDAO
					.selectListFieldByFieldName3(
							Constantes.CAMPO_LISTA_VALIDA_MIGRACION_NOMBRE,
							Constantes.CAMPO_LISTA_VALIDA_MIGRACION_CODE);
			
			String swValidarMigracion="";
			for(int k=0; k< listaCampoVal.size();k++ ){
				swValidarMigracion=listaCampoVal.get(k).getDlfDescription();
			}
			
			UsuariosDAO dusuario = new UsuariosDAOImpl();
			
			if(swValidarMigracion.equals(Constantes.CAMPO_LISTA_VALIDA_MIGRACION_VALOR)
					&& dusuario.validarSiUsuarioMigro(numTarjeta) ){
				logger.info("Hola aqui inicia el proceso de validacion.............................");						
				
				return mapping.findForward("registrarMigracionUsuario");
			}else{
				return mapping.findForward("opciones");
			}

		} catch (Exception ex1) {
			logger.error("Error en proceso de Login: "+ex1.getMessage());	
			return mapping.findForward("opciones");
		}	
		
				
		//return mapping.findForward("opciones");

	}
	
	
	
	public ActionForward procesarMigracionUsuario(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

	
		try{
			HttpSession session = request.getSession();
			String numTarjeta = (String) session.getAttribute("tarjetaActual");
			logger.info("numeroTarjeta: "+numTarjeta);
			
			
			UsuariosDAO dusuario = new  UsuariosDAOImpl();
			
			dusuario.confirmarNotificacioUsuarioMigrado(numTarjeta);
			
			dusuario.deshabilitarUsuarioPlataforma(numTarjeta);
			
			
			TxListFieldDao listaCampoDAO = new TxListFieldDaoHibernate();
			@SuppressWarnings("unchecked")
			List<TxListField> listaCampoVal = listaCampoDAO
					.selectListFieldByFieldName3(
							Constantes.CAMPO_LISTA_CASHURLEASYCASH,
							Constantes.CAMPO_LISTA_VALIDA_MIGRACION_CODE);
			
			String urlNuevoCashMulti="";
			for(int k=0; k< listaCampoVal.size();k++ ){
				urlNuevoCashMulti=listaCampoVal.get(k).getDlfDescription();
			}			
			
			logger.info("urlNuevoCashMulti: "+urlNuevoCashMulti);
			
			request.setAttribute("url_nuevo_cash", urlNuevoCashMulti);
			
			
			session.setAttribute("beanDataLogXML", null); // Elimina la sesion
			session.setAttribute("perfilCash", null);
			session.setAttribute("tarjetaActual", null);

			session.invalidate(); // Invalida la sesion
			
			
		}catch(Exception ex){
			logger.error("Error en procesarMigracionUsuario: "+ex.getMessage());
			//return mapping.findForward("error");
			
		}
		
		
		//response.sendRedirect("redireccionarNuevoCash.jsp");
		 //return null;
		 
		return mapping.findForward("redireccionarNuevoCash");
		

	}

}

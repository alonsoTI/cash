/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.ingresarBD;

import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_ARCHIVO_VACIO;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_DESCONOCIDO;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_GENERAR_XML;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_GUARDANDO_DATOS;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_VALIDACION_CTS;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_VALIDACION_CUENTAS;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_VALIDACION_DATOS;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.EXITO;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_VALIDACION_MONTOS;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.financiero.cash.hilos.CashThreadCarga;
import com.financiero.cash.service.impl.ValidadorServiceImpl;
import com.hiper.cash.dao.TaLogValidacionDao;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.hibernate.TaLogValidacionDaoHibernate;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.jdbc.DBManager;
import com.hiper.cash.domain.TaLogValidacion;
import com.hiper.cash.domain.TaLogValidacionId;
import com.hiper.cash.ingresarBD.campos.CampoANS;
import com.hiper.cash.ingresarBD.campos.CampoDEC;
import com.hiper.cash.ingresarBD.campos.CampoFEC;
import com.hiper.cash.ingresarBD.campos.CampoHOR;
import com.hiper.cash.ingresarBD.campos.CampoNUM;
import com.hiper.cash.ingresarBD.dbaccess.CBBuzonEnvio;
import com.hiper.cash.ingresarBD.dbaccess.CBDetalleBuzon;
import com.hiper.cash.ingresarBD.dbaccess.CBEmpresaFormato;
import com.hiper.cash.ingresarBD.dbaccess.CBSecuencial;
import com.hiper.cash.ingresarBD.entity.BuzonEnvio;
import com.hiper.cash.ingresarBD.util.LogErrores;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Mensajes;
import com.hiper.cash.util.Util;

/**
 * 
 * @author jmoreno
 */
public class Validador {
	private final static int EXITO_EN_VALIDACION_CUENTAS_Y_PAGOS_CTS = 0;

	private static final String FILE_TMP_CARGA = CashConstants.RES_FINANCIERO
			.getString("ruta_FileTMP");

	private FormatoServicio formatoServicio;
	private Document docXml;
	private int posicionActual = 0;
	private int nlineas;
	private List<String> lineasbug = new ArrayList<String>();
	private List listaCondicionales;
	private String lin = "";

	private TaServicioxEmpresaDaoHibernate taServicioxEmpresaDaoHibernate = new TaServicioxEmpresaDaoHibernate();
	private TaLogValidacionDao logValidacionDao = new TaLogValidacionDaoHibernate();
	private CBBuzonEnvio buzonEnvioDao = new CBBuzonEnvio();
	private CBDetalleBuzon detalleBuzonDao = new CBDetalleBuzon();
	private TaOrdenDao ordenDao = new TaOrdenDaoHibernate();
	// TODO integrar con otro secuencialDao existente
	private CBSecuencial secuencialDao = new CBSecuencial();
	private ValidadorServiceImpl validadorService = ValidadorServiceImpl
			.getInstance();

	public String getLin() {
		return lin;
	}

	public void setLin(String lin) {
		this.lin = lin;
	}

	static Logger logger = Logger.getLogger(Validador.class);

	public Validador() {
		docXml = new Document();
		formatoServicio = new FormatoServicio();

	}

	/**
	 * Método que genera un archivo XML, a partir del String recibido
	 * 
	 * @param pathForm
	 *            contenido del XML
	 * @return true: si se genera el XML,false: si no genera el XML
	 */
	private boolean generarXml(String idEmpresa) {
		logger.debug("Inicio: generarXml()");
		InputStream in = null;
		SAXBuilder builder = null;
		try {
			CBEmpresaFormato dao = new CBEmpresaFormato();
			String aux = dao.obtenerXmlIn(idEmpresa);
			if (!aux.equals("")) {
				// File nvoFile = new File("D:/CASHFINFILES/temp");
				File nvoFile = new File(FILE_TMP_CARGA);
				FileOutputStream fos = new FileOutputStream(nvoFile);
				DataOutputStream out = new DataOutputStream(fos);
				out.write(aux.getBytes());
				out.close();
				fos.close();
				builder = new SAXBuilder(false);
				in = new FileInputStream(nvoFile);
				docXml = builder.build(in);
				nvoFile.delete();
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		logger.debug("Fin: generarXml()");
		return true;
	}

	private boolean obtenerCondicionales(List lcampos) {
		listaCondicionales = new ArrayList();
		Element ecampo = null;
		for (int j = 0; j < lcampos.size(); j++) {
			ecampo = (Element) lcampos.get(j);
			String temp = ecampo.getAttributeValue(Mensajes.XML_CONDICIONAL);
			if (temp != null && !"".equals(temp) && "si".equals(temp)) {
				listaCondicionales.add(ecampo
						.getAttributeValue(Mensajes.XML_NOMBRE));
			}

		}
		return true;
	}

	/**
	 * Método que recupera del XML todos los tipos de formato de campo por
	 * codigo de servicio para ser guardados dentro de la propiedad de
	 * clase:formatoServicio
	 * 
	 * @param codServicioEmpresa
	 *            codigo del servicio x empresa
	 * @param error
	 *            mensaje de errores que ocurrieron durante la ejecución del
	 *            método
	 * @return true: si no hay errores false: si hay errores
	 */
	private boolean obtenerCampos(String codServicioEmpresa) {

		logger.debug("Inicio: obtenerCampos()");

		boolean band = false;// para saber si el servicio existe o no

		Element efServ = null;// elemento formato del servicio

		Element ecampo = null;// elemento campo

		HashMap campos = null;// haspmap de todos los campos que contiene un
								// determinado formato

		try {
			Element raiz = docXml.getRootElement();
			List lformatos = (List) raiz.getChildren("servicio");
			List lcampos = null;
			for (int i = 0; i < lformatos.size(); i++) {
				efServ = (Element) lformatos.get(i);
				if (efServ.getAttribute("id").getValue()
						.equals(codServicioEmpresa)) {
					band = true;
					lcampos = (List) efServ.getChildren("campo");
					obtenerCondicionales(lcampos);
					campos = new HashMap();
					for (int j = 0; j < lcampos.size(); j++) {
						ecampo = (Element) lcampos.get(j);
						Campo c = null;
						c = (Campo) Class.forName(
								Campo.class.getPackage().getName()
										+ ".campos.Campo"
										+ ecampo.getAttributeValue("tipo"))
								.newInstance();
						if (c.init(ecampo)) {
							campos.put(Integer.parseInt(ecampo
									.getAttributeValue("posicion")), c);
						} else {
							// pwError.println(LogErrores.getERROR());//los
							// errores se escribiran en base de datos
							// pero este error de formato,como se mostrara?
							logger.error("Error al iniciar el campo");
							LogErrores.resetError();
							return false;
						}

					}
					lcampos = null;
					formatoServicio.setCampos(campos);
				}
			}
			logger.debug("Fin: obtenerCampos()");
			return band;
		} catch (Exception e) {
			logger.error("Fin: obtenerCampos()", e);
			return false;
		}
	}

	/**
	 * Método que valida la información contenida en el archivo con ruta
	 * pathDat, de acuerdo al formato del servicio con código nombreServ.
	 * 
	 * @param pathDat
	 *            ruta del archivo de datos a validar
	 * @param nombreServ
	 *            codigo del servicio
	 * @param errorValidacion
	 *            errores producidos durante la validacion del archivo
	 * @return true: si no existe errores en el archivo,false: si existe errores
	 *         en el archivo
	 */
	// TODO se deberia separar el metodo en 2 metodos. Uno con la
	// responsabilidad de validar
	// y el otro con la de guardar los datos
	public int validarArchivo(String idEmpresa, String pathDat,
			String idUsuario, long idEnvio, String idServxEmp,
			String fechaIniVig, String fechaFinVig, String horaInicio,
			String tipoCuenta, String cuentaCargo, String tipoIngreso,
			String referencia) {
		logger.info("Iniciando validar archivo con parametros");
		logger.info("idEmpresa: " + idEmpresa);
		logger.info("idEnvio: " + idEnvio);
		logger.info("idServxEmp: " + idServxEmp);
		logger.info("fechaIniVig: " + fechaIniVig);
		logger.info("fechaFinVig: " + fechaFinVig);
		logger.info("horaInicio: " + horaInicio);
		logger.info("tipoCuenta: " + tipoCuenta);
		logger.info("cuentaCargo: " + cuentaCargo);
		logger.info("tipoIngreso: " + tipoIngreso);
		int retorno = 0;
		if (generarXml(idEmpresa)) {
			try {
				File fdata = new File(pathDat);
				if (fdata.length() > 0) {
					if (!validarDatos(pathDat, idServxEmp, idEnvio)) {
						retorno = ERROR_VALIDACION_DATOS;

					} else {
						int codigoError = validacionCuentasYPagosCTS(pathDat,
								cuentaCargo, idServxEmp);
						logger.info("Resultado de validacion cuentas y CTS: "
								+ codigoError);
						if (codigoError != EXITO_EN_VALIDACION_CUENTAS_Y_PAGOS_CTS) {
							retorno = codigoError;
						}
						if (codigoError == EXITO_EN_VALIDACION_CUENTAS_Y_PAGOS_CTS) // Si
																					// no
																					// hay
																					// error
						{
							if (validadorService.validarArchivoDePagosCCI(
									pathDat, Long.valueOf(idServxEmp), idEnvio)) {

								if (guardarDatos(pathDat, idEnvio, idUsuario,
										idServxEmp, fechaIniVig, fechaFinVig,
										horaInicio, tipoCuenta, cuentaCargo,
										tipoIngreso, referencia)) {

									retorno = EXITO;

								} else {
									retorno = ERROR_GUARDANDO_DATOS;
								}
							} else {
								retorno = ERROR_VALIDACION_DATOS;
							}
						}
					}
				} else {
					// TODO, no deberia permitir archivos vacios
					retorno = ERROR_ARCHIVO_VACIO;
				}
			} catch (Exception e) {
				logger.error("Error validando archivo: " + pathDat, e);
				retorno = ERROR_DESCONOCIDO;
			}
		} else {
			logger.error("error en generar el XML");
			retorno = ERROR_GENERAR_XML;
		}
		logger.info("Fin validar Archivo con resultado " + retorno);
		return retorno;
	}

	/*
	 * TODO Ver si se puede incluir estas validaciones dentro de esquema de
	 * validacion que valida los pagos cci
	 */
	/***
	 * Validaciones adicionales de pertenecia de cuentas y validacion especial
	 * para pagos CTS
	 * 
	 * @param pathDat
	 * @param cuentaCargo
	 * @param idServxEmp
	 * @return 0=No hay error, >0=Hubo un error
	 */
	private int validacionCuentasYPagosCTS(String pathDat, String cuentaCargo,
			String idServxEmp) {

		int codigoError = EXITO_EN_VALIDACION_CUENTAS_Y_PAGOS_CTS;

		try {

			logger.info("Inicio de Validacion de Cuentas y Pago CTS......");

			BufferedReader entrada = new BufferedReader(new FileReader(pathDat));
			String linea = null;
			Campo campo = null;
			Map lcampos = formatoServicio.getCampos();

			int indicadorEsPagoCTS = 0;

			indicadorEsPagoCTS = taServicioxEmpresaDaoHibernate
					.selectCountCTS(idServxEmp);

			int pos = 0;
			
		
			
			while (entrada.ready()) {
				linea = entrada.readLine();
				pos++;
				posicionActual = 0;
				campo = null;
				for (int i = 0; i < lcampos.size(); i++) {
					campo = (Campo) lcampos.get(i);
					linea = linea.substring(posicionActual);
					posicionActual = campo.leerCampo(linea);
					
					if (campo.getNombre().equals(
							Mensajes.CAMPO_DBCUENTA_EMPRESA)) {
						String val = ((String) campo.getValor()).trim();
						val = Util.cero_left(val, 12);
						if (!val.equals(cuentaCargo)) {
							logger.error("La Cuenta Cargo no Coincide en la linea "
									+ pos + ", debe ser " + cuentaCargo);
							this.lin = ",Linea de error: " + pos;
							lineasbug.add(this.lin);
							codigoError = ERROR_VALIDACION_CUENTAS;
						}
					}

					
					if (campo.getNombre().equals("nDBuMonto")){
						String valorCampo = campo.getValor().toString();						
						logger.info("Monto Valor:  "+valorCampo);						
						if(valorCampo!=null){
							if(!valorCampo.equals("")){
								int varMnto=0;
								try{
									varMnto=Integer.parseInt(valorCampo);
									
									if(varMnto<=0){
										logger.error("Monto Valor vacio ");
										this.lin = ",Linea de error: " + pos;
										lineasbug.add(this.lin);
										codigoError = ERROR_VALIDACION_MONTOS;	
										
									}
									
								}catch(Exception e1){
									logger.error("Monto Valor vacio ");
									this.lin = ",Linea de error: " + pos;
									lineasbug.add(this.lin);									
									codigoError = ERROR_VALIDACION_MONTOS;	
								}
								
							}else{
								logger.error("Monto Valor vacio ");
								this.lin = ",Linea de error: " + pos;
								lineasbug.add(this.lin);								
								codigoError = ERROR_VALIDACION_MONTOS;
							}
							
						}else{
							logger.error("Monto Valor vacio ");
							this.lin = ",Linea de error: " + pos;
							lineasbug.add(this.lin);							
							codigoError = ERROR_VALIDACION_MONTOS;
						}						
						
						
						
					}
					
					if (indicadorEsPagoCTS > 0) {
						if (campo.getNombre().length() > 0) {
							if (campo.getNombre().equals("dDBuAdicional7")) {
								String valorCampo = campo.getValor().toString();
								// TODO no debe validar cada caracter sino el
								// valor completo
								for (int j = 0; j < valorCampo.length(); j++) {
									if (!Character
											.isDigit(valorCampo.charAt(j))) {
										// en la pantalla de resultado de
										// errores se mostrar� un error generico
										logger.error("El caracter: \""
												+ valorCampo.charAt(j)
												+ "\" no es numerico");
										codigoError = ERROR_VALIDACION_CTS;
									}
								}
							}
						}
					}

				}// end-for
			}// end-while
			entrada.close();
			logger.info("Fin de Validacion de Cuentas y Pago CTS......");
		} catch (Exception e) {
			logger.error("Error en metodo validacionCuentasYPagosCTS", e);
		}
		return codigoError;

		// logger.info("Inicio de Validacion de Cuentas y Pago CTS......");
		// logger.info("Fin de Validacion de Cuentas y Pago CTS......");
		// return 0;
	}

	public List<String> getLineasbug() {
		return lineasbug;
	}

	public void setLineasbug(List<String> lineasbug) {
		this.lineasbug = lineasbug;
	}

	private String valorInsertar(Campo campo) {
		String vc = "null";
		if ((campo instanceof CampoANS) || (campo instanceof CampoNUM)) {
			if (campo.getNombre().equals(Mensajes.CAMPO_DBCUENTA_ABONO)
					|| campo.getNombre().equals(
							Mensajes.CAMPO_DBCUENTA_ABONO_CCI)
					|| campo.getNombre().equals(Mensajes.CAMPO_DBCUENTA_CARGO)
					|| campo.getNombre()
							.equals(Mensajes.CAMPO_DBCUENTA_EMPRESA)
					|| campo.getNombre().equals(Mensajes.CAMPO_DBNUMERO_CUENTA)) {
				String vc_1 = ((String) campo.getValor()).trim();
				if (vc_1.length() > 0 && vc_1.length() < 12) {
					while (vc_1.length() < 12) {
						vc_1 = "0" + vc_1;
					}
				}
				campo.setValor(vc_1);
			}
			if (("varchar".equals(campo.getTipoBd()))
					|| ("char".equals(campo.getTipoBd()))) {
				vc = ((String) campo.getValor()).trim();
				vc = vc.replaceAll("'", "''");
				vc = "'" + vc + "'";

			} else if (("int".equals(campo.getTipoBd()))
					|| ("bigint".equals(campo.getTipoBd()))) {
				vc = "" + ((String) campo.getValor()).trim();
			}

		} else if (campo instanceof CampoDEC) {
			CampoDEC nvo = (CampoDEC) campo;
			vc = nvo.parseToDecimal(((String) campo.getValor()).trim());
			if ("".equals(vc)) {
				vc = "0.0";
			}

		} else if (campo instanceof CampoFEC) {

			CampoFEC nvo = (CampoFEC) campo;
			// FormateadorFecha formateador = nvo.getFormatoFecha();
			// formateador.parse(((String) campo.getValor()).trim());
			// vc = "'" + nvo.getFormatoFecha().YEAR +
			// nvo.getFormatoFecha().MONTH + nvo.getFormatoFecha().DAY + "'";
			// Se tiene que cambiar el formato de la fecha al formato en el que
			// se guarda en base de datos
			String formatIn = nvo.getFormatoFecha().getFormato()
					.replaceFirst("mm", "MM");
			vc = "'"
					+ Fecha.formatearFecha(formatIn, "yyyyMMdd",
							(String) campo.getValor()) + "'";

		} else if (campo instanceof CampoHOR) {

			CampoHOR nvo = (CampoHOR) campo;
			// FormateadorHora formateador = nvo.getFormatoHora();
			// formateador.parse(((String) campo.getValor()).trim());
			// vc = "'" + formateador.HOUR + formateador.MINUTE +
			// formateador.SECOND + "'";
			String formatIn = nvo.getFormatoHora().getFormato();// .replaceFirst("hh","HH");
			vc = "'"
					+ Fecha.formatearFecha(formatIn, "HHmmss",
							(String) campo.getValor()) + "'";

		}
		return vc;
	}

	private boolean validarLinea(String linea, HashMap listaCampos,
			StringBuilder auxerr) {
		boolean error = true;
		Campo campo = null;
		String valorCondicion = null;
		HashMap valoresCond = new HashMap();
		for (int i = 0; i < listaCampos.size(); i++) {
			campo = (Campo) listaCampos.get(i);
			linea = linea.substring(posicionActual);
			posicionActual = campo.leerCampo(linea);
			for (int j = 0; j < listaCondicionales.size(); j++) {
				if (campo.getNombre()
						.equals((String) listaCondicionales.get(j))) {

					valoresCond.put(campo.getNombre(),
							(String) campo.getValor());
				}
			}
			valorCondicion = (String) valoresCond.get(campo.getReferencia());
			if (posicionActual != -1) {// si la posicion actual es valida
				if (!campo.validarTipo(valorCondicion)) {
					auxerr.append(" Campo " + campo.getNombre() + ": "
							+ (String) campo.getValor() + ":"
							+ LogErrores.getERROR() + "\n");
					LogErrores.resetError();
					error = false;
				}
			} else {
				auxerr.append(" Campo " + campo.getNombre() + ": "
						+ (String) campo.getValor() + ":"
						+ LogErrores.getERROR() + "\n");
				LogErrores.resetError();
				error = false;
				i = listaCampos.size();
			}
		}
		if (linea.length() > posicionActual && error) {
			auxerr.append(" Esta linea contiene m�s columnas que lo especificado en el formato\n");
			error = false;
		}
		return error;
	}

	/**
	 * 
	 * @param pathDat
	 *            , archivo a validar
	 * @param idServicioEmpresa
	 * @param idEnvio
	 *            , usado para insertar los errores encontradoss
	 * @return
	 */
	private boolean validarDatos(String pathDat, String idServicioEmpresa,
			long idEnvio) {
		boolean retorno = true;
		try {
			BufferedReader entrada = null;
			entrada = new BufferedReader(new FileReader(pathDat));
			String linea = null;
			StringBuilder auxerr = null;

			if (logger.isDebugEnabled()) {
				logger.debug("Validando archivo " + pathDat
						+ " con IdServicioEmpresa: " + idServicioEmpresa);
			}
			if (obtenerCampos(idServicioEmpresa)) {// obtenemos los campos con
													// sus formatos respectivos
													// para el servicio
				nlineas = 0;
				while (entrada.ready()) {// validamos linea por linea el
											// archivos de datos
					auxerr = new StringBuilder();
					nlineas++;
					linea = entrada.readLine();
					posicionActual = 0;
					if (!validarLinea(linea, formatoServicio.getCampos(),
							auxerr)) {// escribir en BD - los errores
						String mensajeErrorLinea = auxerr.toString();
						if (logger.isDebugEnabled()) {
							logger.debug("Error al validar linea " + nlineas
									+ ": " + mensajeErrorLinea);
						}
						TaLogValidacion logValidacion = new TaLogValidacion();
						TaLogValidacionId id = new TaLogValidacionId();
						logValidacion.setDlvaDescripcion(mensajeErrorLinea);
						id.setClvaIdEnvio((int) idEnvio);
						id.setNlvaNumLinea(nlineas);
						logValidacion.setId(id);
						logValidacionDao.insertarLogValidacion(logValidacion);
						retorno = false;
					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Linea " + nlineas
									+ " validada correctamente");
						}

					}
					linea = null;
					auxerr = null;

				}
				entrada.close();
			} else {
				logger.error("No se encontro el idServEmp:" + idServicioEmpresa
						+ " en el XML");
				retorno = false;
			}
			// logger.debug("==> Se termino de validar el archivo ......................");

			return retorno;
		} catch (Exception e) {
			logger.error("Error validando el archivo: " + pathDat, e);
			return false;
		}
	}

	private StringBuilder obtenerSqlInsert() {
		StringBuilder sql = new StringBuilder();
		sql.append("insert into tpDetalleBuzon (cDBuIdDetalleBuzon,cDBuIdEnvio,");
		HashMap lcampos = formatoServicio.getCampos();
		for (int i = 0; i < lcampos.size() - 1; i++) {
			sql.append(((Campo) lcampos.get(i)).getNombre() + ",");
		}
		sql.append(((Campo) lcampos.get(lcampos.size() - 1)).getNombre()
				+ ") values (");
		lcampos = null;
		return sql;
	}

	private boolean guardarDatos(String pathDat, long idEnvio,
			String idUsuario, String idServxEmp, String fechaIniVig,
			String fechaFinVig, String horaInicio, String tipoCuenta,
			String cuentaCargo, String tipoIngreso, String referencia) {

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		logger.info("Inicio Insercion Base datos:" + sdf.format(new Date()));
		BuzonEnvio be = new BuzonEnvio();
		be.setEstado("0");
		be.setFechaCreacion(fechaActual());
		be.setFechaFinVig(fechaFinVig);
		be.setFechaInicioVig(fechaIniVig);
		be.setHoraInicio(horaInicio);
		be.setTipoCuenta(tipoCuenta);
		be.setTipoIngreso(tipoIngreso);
		be.setCuentaCargo(cuentaCargo);
		be.setReferencia(referencia);
		be.setIdEnvio(idEnvio);

		// aqui comento para que no obtenga un nuevo correlativo para el IDOrden
		long idOrden = obtenerNvoSec();

		// logger.info("La orden generada es: " + idOrden);
		be.setIdOrden(idOrden);
		be.setUsuario(idUsuario);
		be.setIdServEmp(Long.parseLong(idServxEmp));

		if (buzonEnvioDao.insertar(be)) {
			logger.info("Insertado BuzonEnvio");
			try {
				BufferedReader entrada = null;
				entrada = new BufferedReader(new FileReader(pathDat));
				StringBuilder sql_insert = obtenerSqlInsert();
				String linea = null;
				Campo campo = null;
				HashMap lcampos = formatoServicio.getCampos();
				long idDetalleBuzon = 1;

				// declaro las conexiones hacia la bd de cash
				Connection conn = null;
				conn = DBManager.getConnectionDBTransaccional();
				Statement stmt = conn.createStatement();
				int contadorDetBuz = 0;

				// logger.info("Inicio de Insertar los detalles ..........");

				// obtengo la matriz del mapa de campos del servicio

				//String[] columns;

				//int posicion_campo = 0;
				
				//CBEmpresaFormato bformato= new CBEmpresaFormato();
				
				//String mi_matriz[] = new String[100];
				
				//mi_matriz=bformato.obtenerDetallesFormato(bformato.obtenerCodigoFormato(Integer.parseInt(idServxEmp)));
				
				
				
				
				while (entrada.ready()) {// insertamos linea por linea el
											// archivos de datos

					contadorDetBuz++;

					linea = entrada.readLine();

					//columns = linea.split("\t", -1);
					//int nro_columnas = 0;
					//nro_columnas = columns.length;
					//logger.info("nro_columnas ==> " + nro_columnas);

					//logger.info("Linea " + idDetalleBuzon + ": ==> " + linea);
					
					
					//posicion_campo=FindStringIndex("dDBuCodOrientacion", mi_matriz)-1;					
					//logger.info("la posicion del campo dDBuCodOrientacion es: "+(posicion_campo) +" y su valor es : "+columns[posicion_campo]);
					 
										
					//posicion_campo=FindStringIndex("nDBuMonto", mi_matriz)-1;					
					//logger.info("la posicion del campo nDBuMonto es: "+(posicion_campo) +" y su valor es : "+columns[posicion_campo]);
					 					
					
					
					posicionActual = 0;
					campo = null;
					StringBuilder sql = new StringBuilder();
					sql.append(sql_insert.toString() + idDetalleBuzon + ","
							+ idEnvio + ",");

					// logger.info("Size="+lcampos.size());

					for (int i = 0; i < lcampos.size(); i++) {
						campo = (Campo) lcampos.get(i);
						linea = linea.substring(posicionActual);

						// logger.info("Campo:"+campo.getNombre());
						// logger.info("==>"+linea);

						 posicionActual = campo.leerCampo(linea);

						// logger.info("posicionActual="+posicionActual);

						if (i == lcampos.size() - 1) {
							sql.append(valorInsertar(campo) + ")");
						} else {
							sql.append(valorInsertar(campo) + ",");
						}
					}

					// aqui se inserta los items en la tabla detalle buzon
					// detalleBuzonDao.insertarConex(sql.toString());

					stmt.addBatch(sql.toString());

					if (contadorDetBuz % 1000 == 0) {
						stmt.executeBatch();
					}

					// finalizo el ingreso

					idDetalleBuzon++;
					linea = null;
					sql = null;

				}// end-while

				// logger.info("ANDY==> FINN de Insertar los detalles ..........");

				entrada.close();

				if (contadorDetBuz % 1000 != 0) {
					stmt.executeBatch();

				}

				// cierro la conexion
				conn.close();

				// logger.info("ANDY==> Antes de Invocar el procedimiento almacenado .......");

				if (buzonEnvioDao.actualizarEstado("1", idEnvio)) {

					// logger.info("Fin Insercion Base datos:"+ sdf.format(new
					// Date()));

					CashThreadCarga miHiloTrx = new CashThreadCarga(idEnvio,
							be.getIdOrden(), be.getIdServEmp());
					miHiloTrx.start();

					// ordenDao.ejecutaBuzonesID(idEnvio,
					// be.getIdOrden(),be.getIdServEmp());

					// logger.info("Despues de Invocar el procedimiento almacenado .......");

					return true;
				} else {
					logger.error("No se pudo actualizar el estado en BuzonEnvio");
					return false;
				}
			} catch (Exception e) {
				logger.error("ERROR:" + e.toString(), e);
				return false;
			}
		} else {
			logger.error("No se pudo insertar en BuzonEnvio");
			return false;
		}
	}

	private String fechaActual() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	private long obtenerNvoSec() {
		return secuencialDao.generarSecuencial();
	}

	public int FindStringIndex(String word, String arrayToSearch[]) {

		int i = 0;

		for (i = 0; i < arrayToSearch.length; ++i) {
			if (word.equals(arrayToSearch[i])) {
				break;
			}

		}

		return i;

	}

}
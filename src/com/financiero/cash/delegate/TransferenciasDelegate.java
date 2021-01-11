package com.financiero.cash.delegate;


import static com.hiper.cash.util.CashConstants.TAMANIO_PAGINA_TRANSFERENCIAS;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;

import com.financiero.cash.beans.AccionTransferenciaBean;
import com.financiero.cash.beans.ClienteBean;
import com.financiero.cash.beans.ConsultaTransferenciasBean;
import com.financiero.cash.beans.TransferenciaBean;
import com.financiero.cash.exception.BFPBusinessException;
import com.financiero.cash.ibs.util.IbsUtils;
import com.financiero.cash.ibs.util.TipoDocumento;
import com.financiero.cash.util.AccionTransferencia;
import com.financiero.cash.util.CodigosIBS;
import com.financiero.cash.util.EstadoTransferencia;
import com.financiero.cash.util.TipoMoneda;
import com.financiero.cash.util.TipoTransferencia;
import com.financiero.cash.util.TipoTransferenciaPorMonedas;
import com.financiero.cash.util.spring.ConfigLoader;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TmBancoDao;
import com.hiper.cash.dao.TmServicioDao;
import com.hiper.cash.dao.TmUsuarioDAO;
import com.hiper.cash.dao.TransferenciaDAO;
import com.hiper.cash.dao.TxListFieldDao;
import com.hiper.cash.dao.TxResultDao;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmBancoDaoHibernate;
import com.hiper.cash.dao.hibernate.TmServicioDaoHibernate;
import com.hiper.cash.dao.hibernate.TmUsuarioDAOHibernate;
import com.hiper.cash.dao.hibernate.TxListFieldDaoHibernate;
import com.hiper.cash.dao.hibernate.TxResultDaoHibernate;
import com.hiper.cash.dao.ws.ibs.TransferenciaDaoIbs;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.domain.TmServicio;
import com.hiper.cash.domain.TmUsuario;
import com.hiper.cash.domain.TpDetalleOrden;
import com.hiper.cash.domain.TxListField;
import com.hiper.cash.domain.TxResult;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.BeanSuccessDetail;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;

public class TransferenciasDelegate {
        
    private static final char ESTADO_CUENTA_ACTIVA = 'A';
    
    private static final String CODIGO_ERROR_PROCESO_VALIDACION = "003";

    private static final String CODIGO_ERROR_CUENTA = "001";

    private static final String CODIGO_ERROR_MONTO = "002";

    private static final String MSG_KEY_VALIDACION_LIMITE_DIARIO = "validacion.pagoscci.mensajes.validacionLimiteDiario";

    private static final String MSG_VALIDACION_LIMITE_DIARIO = CashConstants.RES_CASH
            .getString(MSG_KEY_VALIDACION_LIMITE_DIARIO);

    private static final String MSG_KEY_VALIDACION_NO_DISPONIBLE = "validacion.pagoscci.mensajes.validacionNoDisponible";

    private static final String MSG_VALIDACION_NO_DISPONIBLE = CashConstants.RES_CASH
            .getString(MSG_KEY_VALIDACION_NO_DISPONIBLE);

    private static final String MSG_KEY_SALDO_CTA_CARGO_INSUFICIENTE = "validacion.pagoscci.mensajes.saldoCargoInsuficiente";

    private static final String MSG_SALDO_CTA_CARGO_INSUFICIENTE = CashConstants.RES_CASH
            .getString(MSG_KEY_SALDO_CTA_CARGO_INSUFICIENTE);

    private static final String MSG_KEY_VALIDACION_CTAS_TERCEROS = "validacion.pagoscci.mensajes.cuentaMismoCliente";

    private static final String MSG_VALIDACION_CTAS_TERCEROS = CashConstants.RES_CASH
            .getString(MSG_KEY_VALIDACION_CTAS_TERCEROS);

    private static final String MSG_KEY_VALIDACION_CTAS_PROPIAS = "validacion.pagoscci.mensajes.cuentaDiferenteCliente";

    private static final String MSG_VALIDACION_CTAS_PROPIAS = CashConstants.RES_CASH
            .getString(MSG_KEY_VALIDACION_CTAS_PROPIAS);

    private static final String MSG_KEY_CUENTA_NO_PERMITE_TRANSFERENCIAS = "validacion.pagoscci.mensajes.cuentaNoPermiteTransferencias";

    private static final String MSG_CUENTA_NO_PERMITE_TRANSFERENCIAS = CashConstants.RES_CASH
            .getString(MSG_KEY_CUENTA_NO_PERMITE_TRANSFERENCIAS);

    private static final String MSG_KEY_ERROR_CUENTA_DESTINO = "validacion.pagoscci.mensajes.cuentaNoPermiteAbonos";

    private static final String MSG_ERROR_CUENTA_DESTINO = CashConstants.RES_CASH
            .getString(MSG_KEY_ERROR_CUENTA_DESTINO);

    private static final String MSG_KEY_MONEDAS_DIFERENTES_PAGOS_CCI = "validacion.pagoscci.mensajes.diferenteMonedaPagoCCI";

    private static final String MSG_MONEDAS_DIFERENTES_PAGOS_CCI = CashConstants.RES_CASH
            .getString(MSG_KEY_MONEDAS_DIFERENTES_PAGOS_CCI);

    private static final String PATTERN_MONTO = "###0.00";

	private Logger logger = Logger.getLogger(this.getClass());

	private static TransferenciasDelegate instanciaUnica;

	private TaOrdenDao daoOrden = new TaOrdenDaoHibernate();

	private TxListFieldDao daoField = new TxListFieldDaoHibernate();

	private TmServicioDao daoServicio = new TmServicioDaoHibernate();

	private TaServicioxEmpresaDao daoServicioEmpresa = new TaServicioxEmpresaDaoHibernate();

	private TxResultDao daoResult = new TxResultDaoHibernate();

	private TmUsuarioDAO daoUsuario = new TmUsuarioDAOHibernate();

	private TmBancoDao daoBanco = new TmBancoDaoHibernate();	
	private TipoCambioDelegate tipoCambioDelegate = TipoCambioDelegate.getInstance();
	private static final String FIELD_NAME_TIPO_CUENTA="CashTipoCuenta";
	
	
	
	private TransferenciaDAO transferenciaDaoIbs = null;

	private TransferenciasDelegate() {
		try {
			transferenciaDaoIbs = (TransferenciaDAO) ConfigLoader.getInstance()
					.getBean("transferenciaDao");
		} catch (Exception e) {
			logger.error("Error accediendo a bean de spring", e);
			throw new RuntimeException("Error accediendo a bean de spring", e);
		}
	}

	public static TransferenciasDelegate getInstance() {
		if (instanciaUnica == null) {
			instanciaUnica = new TransferenciasDelegate();
		}
		return instanciaUnica;
	}

	/**
	 * @param monto
	 * @return
	 * @throws Exception
	 */
	public String strMontoIBS(double monto) throws Exception {
		String strMonto = String.valueOf(monto);
		return strMontoIBS(strMonto);
	}

	public String strMontoIBS(String monto) throws Exception {
		int n = monto.length();
		String[] numeros = monto.split("\\.");
		StringBuilder sb = new StringBuilder();
		if (n > 10) {
			throw new Exception(
					"El monto de la operacion excede el limite permitido");
		}
		int limite = 15 - n;
		if (numeros.length == 2) {

			boolean esDecena = (numeros[1].length() == 1);
			if (esDecena) {
				limite--;
			}
			for (int i = 0; i < limite; i++) {
				sb = sb.append("0");
			}
			sb = sb.append(numeros[0]).append(numeros[1]);
			if (esDecena) {
				sb = sb.append("0");
			}
		} else {
			limite -= 3;
			if (numeros.length == 1) {
				for (int i = 0; i < limite; i++) {
					sb = sb.append("0");
				}
				sb = sb.append(numeros[0]).append("00");
			} else {
				throw new java.lang.NumberFormatException(
						"El formato del monto es invalido");
			}
		}

		return sb.toString();
	}

	public List<BeanSuccessDetail> getDetallesImpresion(String modulo,
			MessageResources messageResources, long idOrden,
			long idServicioEmpresa) throws Exception {
		List<BeanSuccessDetail> alsuccess = new ArrayList<BeanSuccessDetail>();
		TaOrden orden = daoOrden.getOrdenRep(idOrden, idServicioEmpresa);
		TpDetalleOrden detalle = orden.getTpDetalleOrdens().get(0);
		alsuccess.add(new BeanSuccessDetail(messageResources
				.getMessage("transferencias.confirmacion.operacion"), orden
				.getTaServicioxEmpresa().getTmServicio().toString()));
		alsuccess.add(new BeanSuccessDetail(messageResources
				.getMessage("transferencias.msg.cargo"), orden
				.getNorNumeroCuenta()));
		alsuccess.add(new BeanSuccessDetail(messageResources
				.getMessage("transferencias.msg.titularCuentaCargo"), orden
				.getTaServicioxEmpresa().getTmEmpresa().toString()));
		alsuccess.add(new BeanSuccessDetail(messageResources
				.getMessage("transferencias.confirmacion.codigo"), orden
				.toString()));
		String idServicio = orden.getTaServicioxEmpresa().getTmServicio()
				.getCsrIdServicio();
		if (idServicio.equals(CashConstants.COD_TRANS_INTERBANCARIA)) {
			int n = detalle.getNdonumCuentaAbonoCci().length();
			StringBuilder sb = new StringBuilder();
			if (n > 2) {
				sb = sb.append(
						detalle.getNdonumCuentaAbonoCci().substring(0, 3))
						.append("-");
				if (n > 5) {
					sb = sb.append(
							detalle.getNdonumCuentaAbonoCci().substring(3, 6))
							.append("-");
					if (n > 17) {
						sb = sb.append(
								detalle.getNdonumCuentaAbonoCci().substring(6,
										18)).append("-");
						if (n > 19) {
							sb = sb.append(detalle.getNdonumCuentaAbonoCci()
									.substring(18, 20));
						} else {
							sb = sb.append(detalle.getNdonumCuentaAbonoCci()
									.substring(18, n));
						}
					} else {
						sb = sb.append(detalle.getNdonumCuentaAbonoCci()
								.substring(6, n));
					}
				} else {
					sb = sb.append(detalle.getNdonumCuentaAbonoCci().substring(
							3, n));
				}
			} else {
				sb = sb.append(detalle.getNdonumCuentaAbonoCci()
						.substring(0, n));
			}

			alsuccess.add(new BeanSuccessDetail(messageResources
					.getMessage("transferencias.msg.abono"), sb.toString()));
			alsuccess.add(new BeanSuccessDetail(messageResources
					.getMessage("transferencias.msg.titularCuentaAbono"),
					new StringBuilder(detalle.getDdonombreBenef()).append(" ")
							.append(detalle.getDdoapePatBenef()).append(" ")
							.append(detalle.getDdoapeMatBenef()).toString()));
		} else {
			if (idServicio.equals(CashConstants.COD_TRANS_TERCEROS)) {
				alsuccess.add(new BeanSuccessDetail(messageResources
						.getMessage("transferencias.msg.abono"), detalle
						.getNdonumCuentaAbonoCci()));
			} else {
				alsuccess.add(new BeanSuccessDetail(messageResources
						.getMessage("transferencias.msg.abono"), detalle
						.getNdonumCuentaAbono()));
			}
			alsuccess.add(new BeanSuccessDetail(messageResources
					.getMessage("transferencias.msg.titularCuentaAbono"),
					detalle.getDdonombreBenef()));
		}

		alsuccess.add(new BeanSuccessDetail(messageResources
				.getMessage("transferencias.msg.referencia"), orden
				.getDorReferencia()));
		String monto = new StringBuilder(Util.getMonedaCash(detalle
				.getCdomoneda()))
				.append(" ")
				.append(Util
						.formatearMontoNvo(detalle.getNdomonto().toString()))
				.toString();
		alsuccess.add(new BeanSuccessDetail(messageResources
				.getMessage("transferencias.msg.monto"), monto));
		String fecha = new StringBuilder(Fecha.convertFromFechaSQL(orden
				.getForFechaRegistro())).append(" ")
				.append(Fecha.convertFromTimeSQL(orden.getHorHoraInicio()))
				.toString();
		alsuccess.add(new BeanSuccessDetail(messageResources
				.getMessage("pagos.confirmacion.date"), fecha));
		TxListField field = daoField.selectListField(
				Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN,
				String.valueOf(detalle.getCdoestado()));
		if (modulo.equals(CashConstants.COD_MODULO_COMPROBANTES)) {
			String codigoIBS = detalle.getCdocodigoRptaIbs();
			if (codigoIBS == null) {
				alsuccess.add(new BeanSuccessDetail(messageResources
						.getMessage("transferencias.msg.estado"), field
						.getDlfDescription()));
			} else {
				if (!IbsUtils.esRespuestaExitosa(codigoIBS)) {
					TxResultDao dao = new TxResultDaoHibernate();
					TxResult txResult = dao.selectByCodIbs(codigoIBS);
					String mensajeIBS = "";
					if (txResult != null) {
						mensajeIBS = new StringBuilder(codigoIBS).append(" - ")
								.append(txResult.getDrsDescription())
								.toString();
					} else {
						mensajeIBS = new StringBuilder(codigoIBS).append(" - ")
								.append(Constantes.CODIGO_RPTA_IBS_DESCONOCIDO)
								.toString();
					}
					alsuccess.add(new BeanSuccessDetail(messageResources
							.getMessage("transferencias.msg.estado"), field
							.getDlfDescription()));
					alsuccess.add(new BeanSuccessDetail(messageResources
							.getMessage("transferencias.msg.descripcion"),
							mensajeIBS));
				} else {
					alsuccess.add(new BeanSuccessDetail(messageResources
							.getMessage("transferencias.msg.estado"), field
							.getDlfDescription()));
				}
			}
		}
		return alsuccess;
	}

	public boolean verificarTipoTransferencia(
			TaServicioxEmpresa servicioEmpresa, int tipo) {
		TmServicio servicio = daoServicio.getServicio(servicioEmpresa
				.getCsemIdServicioEmpresa());
		int idServicio = Integer.valueOf(servicio.getCsrIdServicio());
		if (tipo == 1) {
			if (idServicio == 11) {
				return true;
			}
			return false;
		} else if (tipo == 2) {
			if (idServicio == 12) {
				return true;
			}
			return false;
		} else if (tipo == 3) {
			if (idServicio == 15) {
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Rediseño del Modulo de Transferencias
	 * 
	 * @throws SQLException
	 * 
	 */

	public List<LabelValueBean> getEmpresas() throws Exception {
		List<LabelValueBean> empresas = new ArrayList<LabelValueBean>();
		List<Object[]> lista = daoServicioEmpresa
				.buscarEmpresasTransferencias();
		for (Object[] fila : lista) {
			LabelValueBean empresa = new LabelValueBean();
			empresa.setLabel((String) fila[1]);
			empresa.setValue((String) fila[0]);
			empresas.add(empresa);
		}
		return empresas;
	}

	public ConsultaTransferenciasBean buscarTransferenciasPendientes(
			String empresa, String usuario, BeanPaginacion beanPaginacion)
			throws Exception {
		ConsultaTransferenciasBean consultaTransferenciasBean = transferenciaDaoIbs
				.buscarTransferenciasPendientes(empresa, usuario,
						beanPaginacion);
		if (logger.isDebugEnabled()) {
			logger.debug("Bean consulta encontrada: "
					+ ToStringBuilder
							.reflectionToString(consultaTransferenciasBean));
			if (consultaTransferenciasBean != null
					&& consultaTransferenciasBean.getTransferencias() != null) {
				logger.debug("Numero de trx enviadas: "
						+ consultaTransferenciasBean.getTransferencias().size());
			}
		}
		return consultaTransferenciasBean;
	}

	public TransferenciaBean getTransferencia(String id, String idEmpresa) {
		long idl = Long.valueOf(id);
		return getTransferencia(idl, idEmpresa);
	}

	public TransferenciaBean getTransferencia(long id, String idEmpresa) {
		TransferenciaBean transferencia = transferenciaDaoIbs
				.buscarTransferencia(id, idEmpresa);
		if (transferencia == null) {
			throw new BFPBusinessException(
					"No se encontro alguna transferencia para la consulta enviada");
		}
		TmUsuario usuario = null;
		try {
			
			logger.info("IdEmpresa="+transferencia.getIdEmpresa());
			logger.info("Usuario Registro="+transferencia.getUsuarioRegistro());
			
			usuario = daoUsuario.buscarId(transferencia.getUsuarioRegistro());
		} catch (SQLException e) {
			throw new RuntimeException(
					"error obteniendo el usuario de registro", e);

		}
		if (usuario != null) {
			transferencia.setNombreUsuarioRegistro(usuario.toString());
		}
		setNombreBancoInterbancaria(transferencia);
		TxListField tipoCuenta = daoField.selectListField(
				FIELD_NAME_TIPO_CUENTA, transferencia.getTipoCuentaAbono());
		if (tipoCuenta != null) {
			String descripcionCuentaAbono = StringUtils.trimToEmpty(tipoCuenta
					.getDlfDescription())
					+ " "
					+ TipoMoneda.getLabelMoneda(transferencia
							.getMonedaCuentaAbono());
			transferencia.setDescripcionCuentaAbono(descripcionCuentaAbono);
		}
		tipoCuenta = daoField.selectListField(FIELD_NAME_TIPO_CUENTA,
				transferencia.getTipoCuentaCargo());
		if (tipoCuenta != null) {
			String descripcionCuentaCargo = StringUtils.trimToEmpty(tipoCuenta
					.getDlfDescription())
					+ " "
					+ TipoMoneda.getLabelMoneda(transferencia
							.getMonedaCuentaCargo());
			transferencia.setDescripcionCuentaCargo(descripcionCuentaCargo);
		}
		List<AccionTransferenciaBean> acciones = transferencia.getAcciones();
		if (acciones != null) {
			for (Iterator<AccionTransferenciaBean> iterator = acciones
					.iterator(); iterator.hasNext();) {
				AccionTransferenciaBean accionTransferenciaBean = iterator
						.next();
				String codigoUsuario = accionTransferenciaBean
						.getCodigoUsuario();
				if (StringUtils.isNotBlank(codigoUsuario)) {
					TmUsuario usuarioAccion = null;
					try {
						usuarioAccion = daoUsuario.buscarId(codigoUsuario);
					} catch (SQLException e) {
						throw new RuntimeException(
								"error obteniendo el usuario de acciones", e);
					}
					if (usuarioAccion != null) {
						accionTransferenciaBean.setNombreUsuario(usuarioAccion
								.toString());
					}
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Transferencia encontrada: " + transferencia.debug());
		}
		calcularMontoAbonado(transferencia);
		return transferencia;
	}

	private void setNombreBancoInterbancaria(TransferenciaBean bean) {
		if (bean.getTipo() == TipoTransferencia.IT) {
			StringBuilder sb = new StringBuilder("0");
			sb = sb.append(bean.getCuentaAbono().substring(0, 3));
			bean.setNombreBanco(daoBanco.obtenerBanco(sb.toString()));
		}
	}

	public TransferenciaBean aprobarTransferencia(
			TransferenciaBean transferencia, String usuario,
			AccionTransferencia accion, int correlativoDiario) {
		if (transferencia.getTipo() == TipoTransferencia.CP
				|| transferencia.getTipo() == TipoTransferencia.CT) {
			transferencia = transferenciaDaoIbs.registrarAccion(transferencia,
					usuario, accion, correlativoDiario);
		} else if (transferencia.getTipo() == TipoTransferencia.IT) {
			transferencia = transferenciaDaoIbs.registrarAccionIB(
					transferencia, usuario, accion, correlativoDiario);
		} else {
			throw new RuntimeException(
					"No se puede determinar el tipo de transferencia");
		}
		if (transferencia.getCodigoError() != null) {
			TxResult result = obtenerCodigoErrorIBS(transferencia
					.getCodigoError());
			if (result != null) {
				transferencia.setMensajeError(result.getDrsDescription());
			} else {
				transferencia
						.setMensajeError("La transferencia no se puede realizar");
			}
			if (logger.isDebugEnabled()) {
				logger.debug("La descricpion del error al aprobar es: "
						+ transferencia.getMensajeError());
			}
		}
		return transferencia;
	}

	public TransferenciaBean registrarTransferencia(
			TransferenciaBean transferencia, int correlativoDiario) {
		setNumeroAprobadores(transferencia);
		if (transferencia.getTipo() == TipoTransferencia.CP
				|| transferencia.getTipo() == TipoTransferencia.CT) {
			transferencia = transferenciaDaoIbs.registrar(transferencia,
					correlativoDiario);
		} else if (transferencia.getTipo() == TipoTransferencia.IT) {
			transferencia = transferenciaDaoIbs.registrarIB(transferencia,
					correlativoDiario);
		} else {
			throw new RuntimeException(
					"No se puede determinar el tipo de transferencia");
		}
		if (transferencia.getCodigoError() != null) {
			TxResult result = obtenerCodigoErrorIBS(transferencia
					.getCodigoError());
			if (result != null) {
				String descripcionError = result.getDrsDescription();
				if (logger.isDebugEnabled()) {
					logger.debug("El error es: " + descripcionError);
				}
				transferencia.setMensajeError(descripcionError);
			} else {
				transferencia
						.setMensajeError("La transferencia no se puede realizar");
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Transferencia despues del registro: "
					+ transferencia.debug());
		}
		return transferencia;
	}

	private void setNumeroAprobadores(TransferenciaBean transferencia) {
		TaServicioxEmpresa servicio;
		try {
			servicio = daoServicioEmpresa.getServicioEmpresa(transferencia
					.getServicio());
		} catch (Exception e) {
			throw new RuntimeException("Error obteniendo servicio", e);
		}
		if (CashConstants.FLAG_NO_APROBACION_AUTOMATICA == servicio
				.getCsemFlagAprobAut()) {
			transferencia.setNumeroAprobadores(servicio
					.getNsemNumeroAprobadores());
		} else {
			transferencia.setNumeroAprobadores(0);
		}
	}
	


	public void validarCuentas(TransferenciaBean transferencia) {
		if (logger.isDebugEnabled()) {
			logger.debug("Inicia la validacion de la transferencia: "
					+ transferencia.debug());
		}
		validarNoMismaCuenta(transferencia);
		TipoTransferencia tipo = transferencia.getTipo();
		
		String tramaRespuesta = invocarValidacionTransferenciaYManejarRespuesta(transferencia);

		char estadoCuentaOrigen = obtenerEstadoCuentaOrigenTramaValidacionCuentas(tramaRespuesta);
		String codigoCuentaOrigen = obtenerCodigoCuentaOrigenTramaValidacionCuentas(tramaRespuesta);
		String tipoDocumentoOrigen = StringUtils.trim(tramaRespuesta.substring(
				77, 77 + 4));
		String numeroDocumentoOrigen = StringUtils.trim(tramaRespuesta
				.substring(81, 81 + 15));

		String tipoDocumentoDestino = "";
		String numeroDocumentoDestino = "";
		char estadoCuentaDestino = (char) 0;
		String codigoCuentaDestino = "";

		if (tipo != TipoTransferencia.IT) {
			tipoDocumentoDestino = StringUtils.trim(tramaRespuesta.substring(
					246, 246 + 4));
			numeroDocumentoDestino = StringUtils.trim(tramaRespuesta.substring(
					250, 250 + 15));
			estadoCuentaDestino = tramaRespuesta.charAt(225);
			codigoCuentaDestino = tramaRespuesta.substring(205, 209);
		}
		debugDatosOrigenDestino(estadoCuentaOrigen, codigoCuentaOrigen,
				tipoDocumentoOrigen, numeroDocumentoOrigen,
				tipoDocumentoDestino, numeroDocumentoDestino,
				estadoCuentaDestino, codigoCuentaDestino);

		validacionOrigenTransferenciasCuentasPropias(tipo, tipoDocumentoOrigen,
				numeroDocumentoOrigen, tipoDocumentoDestino,
				numeroDocumentoDestino);
		validacionOrigenTransferenciaCuentasTerceros(tipo, tipoDocumentoOrigen,
				numeroDocumentoOrigen, tipoDocumentoDestino,
				numeroDocumentoDestino);
		validarEstadoTipoCuentaOrigen(estadoCuentaOrigen, codigoCuentaOrigen);
		validarEstadoTipoCuentaDestino(tipo, estadoCuentaDestino,
				codigoCuentaDestino);
		validarSaldoDisponible(transferencia, tipo, tramaRespuesta,
				tipoDocumentoDestino, numeroDocumentoDestino);
		if (tipo == TipoTransferencia.IT) {
			validarLimitesMontoPorTransferenciaIB(transferencia, tramaRespuesta);
		}
		validarLimitesMontoDiarioTransferencia(transferencia, tramaRespuesta);

		obtenerDescripcionCuentas(transferencia, tipo, tramaRespuesta);
		if (tipo != TipoTransferencia.IT) {
			setDatosTitularCuentaDestino(transferencia, tramaRespuesta,
					tipoDocumentoDestino, numeroDocumentoDestino);
		}
		calcularMontoAbonado(transferencia);
		transferencia.setEstado(EstadoTransferencia.PENDIENTE_APROBACION);
	}

	public String obtenerCodigoCuentaOrigenTramaValidacionCuentas(
			String tramaRespuesta) {
		String codigoCuentaOrigen = tramaRespuesta.substring(36, 40);
		return codigoCuentaOrigen;
	}

	public char obtenerEstadoCuentaOrigenTramaValidacionCuentas(
			String tramaRespuesta) {
		char estadoCuentaOrigen = tramaRespuesta.charAt(56);
		return estadoCuentaOrigen;
	}
	
    private String obtenerMonedaCuentaOrigenTramaValidacionCuentas(String tramaRespuesta)
    {
        return tramaRespuesta.substring(186, 189);
    }

    public void validarMonedaPagoCCI(String tramaRespuesta, String monedaPagoCCI)
    {
        String monedaCuentaOrigen = obtenerMonedaCuentaOrigenTramaValidacionCuentas(tramaRespuesta);
        if (!monedaPagoCCI.equals(monedaCuentaOrigen))
        {
            throw new BFPBusinessException(MSG_MONEDAS_DIFERENTES_PAGOS_CCI);
        }
    }

	/**
	 * Invoca la validacion de datos de transferencia
	 * 
	 * @param transferencia
	 * @return
	 */
	public String invocarValidacionTransferenciaYManejarRespuesta(
			TransferenciaBean transferencia) {
		String tramaRespuesta = transferenciaDaoIbs.validarDatosTransferencia(transferencia);
		manejarCodigoErrorIBS(tramaRespuesta);
		return tramaRespuesta;
	}

	public String invocarValidacionTransferenciaYManejarRespuestaPagosCCI(
			TransferenciaBean transferencia) {
		String tramaRespuesta = transferenciaDaoIbs.validarDatosTransferencia(transferencia);
		manejarCodigoErrorIBSPagosCCI(tramaRespuesta);
		return tramaRespuesta;
	}

	private void calcularMontoAbonado(TransferenciaBean transferencia) {
		TipoTransferenciaPorMonedas tipoPorMoneda = transferencia
				.getTipoTransferenciaPorMoneda();
		if (tipoPorMoneda == TipoTransferenciaPorMonedas.MISMA_MONEDA) {
			transferencia.calcularMontoAbonado();
		} else {
			double tipoCambio = transferencia.getTipoCambioRegistro();
			if (tipoCambio == 0.0) {
				if (logger.isDebugEnabled()) {
					logger.debug("El tipo de cambio que se usara es el actual");
				}
				if (tipoPorMoneda == TipoTransferenciaPorMonedas.SOLES_DOLARES) {
					if (logger.isDebugEnabled()) {
						logger.debug("Transferencia bimoneda de soles a dolares");
					}
					tipoCambio = tipoCambioDelegate.obtenerTipoCambioVenta();
				}
				if (tipoPorMoneda == TipoTransferenciaPorMonedas.DOLARES_SOLES) {
					logger.debug("Transferencia bimoneda de dolares a soles");
					tipoCambio = tipoCambioDelegate.obtenerTipoCambioCompra();
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Se encontro registrado el tipo de cambio: "
							+ tipoCambio);
				}
			}
			if (tipoCambio != 0.0) {
				if (logger.isDebugEnabled()) {
					logger.debug("Tipo de cambio a usar para la equivalencia: "
							+ tipoCambio);
				}
				transferencia.calcularMontoAbonado(tipoCambio);
			} else {
				throw new RuntimeException(
						"El tipo de cambio no se encuentra disponible");
			}
		}
	}

	private void validarNoMismaCuenta(TransferenciaBean transferencia) {
		if (transferencia.getCuentaAbono().equals(
				transferencia.getCuentaCargo())) {
			throw new BFPBusinessException(CODIGO_ERROR_CUENTA,
					"La cuenta de abono debe ser distinta a la cuenta de cargo");
		}
	}

	private void debugDatosOrigenDestino(char estadoCuentaOrigen,
			String codigoCuentaOrigen, String tipoDocumentoOrigen,
			String numeroDocumentoOrigen, String tipoDocumentoDestino,
			String numeroDocumentoDestino, char estadoCuentaDestino,
			String codigoCuentaDestino) {
		if (logger.isDebugEnabled()) {
			logger.debug("Estado de la cuenta origen: " + estadoCuentaOrigen);
			logger.debug("Codigo de cuenta origen: " + codigoCuentaOrigen);
			logger.debug("Estado de la cuenta destino: " + estadoCuentaDestino);
			logger.debug("Codigo de cuenta destino: " + codigoCuentaDestino);
			logger.debug("Tipo y numero documento origen: "
					+ tipoDocumentoOrigen + ", " + numeroDocumentoOrigen);
			logger.debug("Tipo y numero documento destino: "
					+ tipoDocumentoDestino + ", " + numeroDocumentoDestino);
		}
	}

	/**
	 * Mapea el codigo de respuesta devuelto por IBS a excepciones
	 * personalizadas Las excepciones tienen un codigo de error que indica cual
	 * es el origen del error: la cuenta, el monto o cualquier otro
	 * 
	 * @param tramaRespuesta
	 */

	private void manejarCodigoErrorIBS(String tramaRespuesta) {
		String codigoIBS = obtenerCodigoRespuestaIBS(tramaRespuesta);
		if (!IbsUtils.esRespuestaExitosa(codigoIBS)) {
			logger.error("IBS devolvio codigo de error: '" + codigoIBS + "'");
			TxResult result = obtenerCodigoErrorIBS(codigoIBS);
			if (result != null) {
				if (result.getCrsTipoMensajes() != null
						&& CodigosIBS.CUENTA_NO_EXISTE_NEW.getCodigo().equals(
								codigoIBS)) {
					throw new BFPBusinessException(CODIGO_ERROR_CUENTA,
							result.getDrsDescription());
				}
				if (result.getCrsTipoMensajes() == null
						&& (CodigosIBS.CUENTA_NO_EXISTE_1.getCodigo().equals(
								codigoIBS) || CodigosIBS.CUENTA_NO_EXISTE_2
								.getCodigo().equals(codigoIBS))) {
					throw new BFPBusinessException(CODIGO_ERROR_CUENTA,
							result.getDrsDescription());
				}
				throw new BFPBusinessException(CODIGO_ERROR_PROCESO_VALIDACION,
						result.getDrsDescription());
			} else {
				throw new BFPBusinessException(CODIGO_ERROR_PROCESO_VALIDACION,
						MSG_VALIDACION_NO_DISPONIBLE);
			}
		}
	}

	private void manejarCodigoErrorIBSPagosCCI(String tramaRespuesta) {
	    String codigoIBS = obtenerCodigoRespuestaIBS(tramaRespuesta);
		if (!IbsUtils.esRespuestaExitosa(codigoIBS)) {
			logger.error("IBS devolvio codigo de error: '" + codigoIBS + "'");
			TxResult result = obtenerCodigoErrorIBS(codigoIBS);
			IbsUtils.validarColasNoDisponibles(tramaRespuesta);
			if (result != null) {
				if (result.getCrsTipoMensajes() != null
						&& CodigosIBS.CUENTA_NO_EXISTE_NEW.getCodigo().equals(
								codigoIBS)) {
					throw new BFPBusinessException(CODIGO_ERROR_CUENTA,
							result.getDrsDescription());
				}
				if (result.getCrsTipoMensajes() == null
						&& (CodigosIBS.CUENTA_NO_EXISTE_1.getCodigo().equals(
								codigoIBS) || CodigosIBS.CUENTA_NO_EXISTE_2
								.getCodigo().equals(codigoIBS))) {
					throw new BFPBusinessException(CODIGO_ERROR_CUENTA,
							result.getDrsDescription());
				}
				throw new BFPBusinessException(CODIGO_ERROR_PROCESO_VALIDACION,
						result.getDrsDescription());
			} else {
				throw new BFPBusinessException(CODIGO_ERROR_PROCESO_VALIDACION,
						MSG_VALIDACION_NO_DISPONIBLE);
			}
		}
	}

    private String obtenerCodigoRespuestaIBS(String tramaRespuesta)
    {        
        String codigoIBS = tramaRespuesta.substring(17, 21);
        return codigoIBS;
    }
	
    

	/**
	 * Valida la cuenta de abono en caso sea una transferencia diferente de
	 * interbancaria
	 * 
	 * @param tipo
	 * @param estadoCuentaDestino
	 * @param codigoCuentaDestino
	 */
	private void validarEstadoTipoCuentaDestino(TipoTransferencia tipo,
			char estadoCuentaDestino, String codigoCuentaDestino) {
		if (tipo != TipoTransferencia.IT) {
			if (estadoCuentaDestino != 'A' && estadoCuentaDestino != 'T') {
				logger.error("Error al validar cuenta destino. Estado es: "
						+ estadoCuentaDestino + ". "
						+ "La descripcion del error es: " + MSG_ERROR_CUENTA_DESTINO);
				throw new BFPBusinessException(CODIGO_ERROR_CUENTA,
						MSG_ERROR_CUENTA_DESTINO);
			}
			if (codigoCuentaDestino.equals(CashConstants.COD_IBS_CUENTA_VISA)) {
				logger.error("El codigo cuenta destino es invalido: "
						+ codigoCuentaDestino);
				throw new BFPBusinessException(CODIGO_ERROR_CUENTA,
						"La cuenta no esta permitida para abono");
			}
			if (codigoCuentaDestino.equals(CashConstants.COD_IBS_CUENTA_CTS)) {
				logger.error("El codigo cuenta destino es invalido: "
						+ codigoCuentaDestino);
				throw new BFPBusinessException(CODIGO_ERROR_CUENTA,
						"La cuenta del tipo CTS no permiten abono");
			}
		}
	}

	/**
	 * Valida que cuenta de cargo este activa y que sea diferente de CTS
	 * 
	 * @param estadoCuentaOrigen
	 * @param codigoCuentaOrigen
	 */
	public void validarEstadoTipoCuentaOrigen(char estadoCuentaOrigen,
			String codigoCuentaOrigen) {
		if (!(estadoCuentaOrigen == ESTADO_CUENTA_ACTIVA
				&& !codigoCuentaOrigen.equals(CashConstants.COD_IBS_CUENTA_CTS) && codigoCuentaOrigen
					.charAt(3) != 'G')) {
			logger.error("Error al validar cuenta de origen. Error es: "
					+ MSG_CUENTA_NO_PERMITE_TRANSFERENCIAS);
			logger.error("Estado de la cuenta origen: " + estadoCuentaOrigen);
			logger.error("Codigo de cuenta origen: " + codigoCuentaOrigen);
			throw new BFPBusinessException(CODIGO_ERROR_CUENTA, MSG_CUENTA_NO_PERMITE_TRANSFERENCIAS);
		}
	}

	/**
	 * setea al bean de transferencia los datos de cuentas como moneda cuenta y
	 * descripcion y nombre banco en interbancaria
	 * 
	 * @param transferencia
	 * @param tipo
	 * @param tramaRespuesta
	 */
	private void obtenerDescripcionCuentas(TransferenciaBean transferencia,
			TipoTransferencia tipo, String tramaRespuesta) {
		String tipoCuentaOrigen = tramaRespuesta.substring(36, 38);
		String monedaCuentaOrigen = tramaRespuesta.substring(186, 189);
		transferencia.setDescripcionCuentaCargo(Util
				.obtenerDescripcionTipoCuenta(tipoCuentaOrigen,
						monedaCuentaOrigen));
		transferencia.setMonedaCuentaCargo(monedaCuentaOrigen);
		if (tipo == TipoTransferencia.IT) {
			setNombreBancoInterbancaria(transferencia);
		} else {
			String tipoCuentaDestino = tramaRespuesta.substring(205, 207);
			String monedaCuentaDestino = tramaRespuesta.substring(355, 358);
			transferencia.setMonedaCuentaAbono(monedaCuentaDestino);
			transferencia.setDescripcionCuentaAbono(Util
					.obtenerDescripcionTipoCuenta(tipoCuentaDestino,
							monedaCuentaDestino));
		}
	}	


	/**
	 * Valida el saldo disponible en la cuenta de origen
	 * 
	 * @param transferencia
	 * @param tipo
	 * @param tramaRespuesta
	 * @param tipoDocumentoDestino
	 * @param numeroDocumentoDestino
	 */
	private void validarSaldoDisponible(TransferenciaBean transferencia,
			TipoTransferencia tipo, String tramaRespuesta,
			String tipoDocumentoDestino, String numeroDocumentoDestino) {
		char signo = tramaRespuesta.charAt(189);
		double montoDisponible = IbsUtils.convertirDouble(tramaRespuesta.substring(190, 205), 2);
		if(logger.isDebugEnabled()){
			logger.debug("Signo del saldo: "+signo);
			logger.debug("Saldo disponible: "+montoDisponible);
		}
		if (signo == CashConstants.SIGNO_POSITIVO) {
			double montoTransferir = transferencia.getMonto();
			if (montoDisponible < montoTransferir) {
				logger.error("Saldo disponible " + montoDisponible
						+ " es menor al monto a transferir " + montoTransferir);
				throw new BFPBusinessException(CODIGO_ERROR_MONTO,
						MSG_SALDO_CTA_CARGO_INSUFICIENTE);
			}
		} else {
			logger.error("El saldo de la cta de cargo es negativo");
			throw new BFPBusinessException(CODIGO_ERROR_MONTO,
					MSG_SALDO_CTA_CARGO_INSUFICIENTE);
		}
	}

	private void validarLimitesMontoDiarioTransferencia(
			TransferenciaBean transferencia, String tramaRespuesta) {
		String codigoMoneda = transferencia.getCodigoMoneda();
		double monto = transferencia.getMonto();
		if (TipoMoneda.PEN.esCodigo(codigoMoneda)) {
			double limiteDiarioSoles = obtenerLimiteDiarioSoles(tramaRespuesta);
			double acumuladoSoles = obtenerAcumuladoSoles(tramaRespuesta);
			if (logger.isDebugEnabled()) {
				logger.debug("LimiteDiarioSoles: " + limiteDiarioSoles);
				logger.debug("AcumuladoSoles: " + acumuladoSoles);
			}
			validarLimitesMontoDiarioTransferenciaSoles(limiteDiarioSoles,
					acumuladoSoles, monto);
		}
		if (TipoMoneda.USD.esCodigo(codigoMoneda)) {
			double limiteDiarioDolares = obtenerLimiteDiarioDolares(tramaRespuesta);
			double acumuladoDolares = obtenerAcumuladoDolares(tramaRespuesta);
			if (logger.isDebugEnabled()) {

				logger.debug("LimiteDiarioDolares: " + limiteDiarioDolares);
				logger.debug("AcumuladoDolares: " + acumuladoDolares);
			}
			validarLimitesMontoDiarioTransferenciaDolares(limiteDiarioDolares,
					acumuladoDolares, monto);
		}
	}

	public void validarLimitesMontoDiarioTransferenciaSoles(
			double limiteDiarioSoles, double acumuladoSoles,
			double montoTransferenciaSoles) {
		boolean excede = false;
		excede = (montoTransferenciaSoles + acumuladoSoles > limiteDiarioSoles);
		if (excede) {
			logger.error("El monto a transferir " + montoTransferenciaSoles
					+ " mas el acumulado " + acumuladoSoles
					+ " excede el limite diario en soles " + limiteDiarioSoles);
		}
		if (excede) {
			throw new BFPBusinessException(CODIGO_ERROR_MONTO,
					MSG_VALIDACION_LIMITE_DIARIO);
		}
	}

	public void validarLimitesMontoDiarioTransferenciaDolares(
			double limiteDiarioDolares, double acumuladoDolares,
			double montoTransferenciaDolares) {
		boolean excede = false;
		excede = (montoTransferenciaDolares + acumuladoDolares > limiteDiarioDolares);
		if (excede) {
			logger.error("El monto a transferir " + montoTransferenciaDolares
					+ " mas el acumulado " + acumuladoDolares
					+ " excede el limite diario en dolares "
					+ limiteDiarioDolares);
		}
		if (excede) {
			throw new BFPBusinessException(CODIGO_ERROR_MONTO,
					MSG_VALIDACION_LIMITE_DIARIO);
		}
	}
	
	public double obtenerAcumuladoDolares(String tramaRespuesta) {
		double acumuladoDolares = IbsUtils.convertirDouble(
				tramaRespuesta.substring(419, 434), 2);
		return acumuladoDolares;
	}

	private double obtenerLimiteDiarioDolares(String tramaRespuesta) {
		double limiteDiarioDolares = IbsUtils.convertirDouble(
				tramaRespuesta.substring(404, 419), 2);
		return limiteDiarioDolares;
	}

	public double obtenerAcumuladoSoles(String tramaRespuesta) {
		double acumuladoSoles = IbsUtils.convertirDouble(
				tramaRespuesta.substring(389, 404), 2);
		return acumuladoSoles;
	}

	private double obtenerLimiteDiarioSoles(String tramaRespuesta) {
		double limiteDiarioSoles = IbsUtils.convertirDouble(
				tramaRespuesta.substring(374, 389), 2);
		return limiteDiarioSoles;
	}

	private void validarLimitesMontoPorTransferenciaIB(TransferenciaBean transferencia, String tramaRespuesta) {		
		double montoITSoles = IbsUtils.convertirDouble(tramaRespuesta.substring(434,449),2);
		double montoITDolares= IbsUtils.convertirDouble(tramaRespuesta.substring(449,464),2);
		if(logger.isDebugEnabled()){
			logger.debug("Monto maximo soles IT: "+montoITSoles);
			logger.debug("Monto maximo dolares IT: "+montoITDolares);
			logger.debug("Monto a transferir: "+transferencia.getMonto());
		}
		boolean excede = false;
		if (TipoMoneda.PEN.esCodigo(transferencia.getCodigoMoneda())) {
			excede = (transferencia.getMonto() > montoITSoles);
			if (excede) {
				logger.error("El  monto a transferir "
						+ transferencia.getMonto()
						+ " excede el limite IT en soles " + montoITSoles);
			}
		} else if (TipoMoneda.USD.esCodigo(transferencia.getCodigoMoneda())) {
			excede = (transferencia.getMonto() > montoITDolares);
			if (excede) {
				logger.error("El  monto a transferir "
						+ transferencia.getMonto()
						+ " excede el limite IT en dolares " + montoITDolares);
			}
		}
		if (excede) {
			StringBuilder mensaje = new StringBuilder(
					"El monto ingresado excede el maximo permitido por transferencia "
							+ "interbancaria, El maximo permitido en");
			if (TipoMoneda.PEN.esCodigo(transferencia.getCodigoMoneda())) {
				mensaje = mensaje.append(" soles es ");
				mensaje = mensaje.append(TipoMoneda.PEN.getValor()).append(" ");
				mensaje = mensaje.append(Util.formatearMonto(montoITSoles,
						PATTERN_MONTO));
			} else {
				mensaje = mensaje.append(" dolares es ");
				mensaje = mensaje.append(TipoMoneda.USD.getValor()).append(" ");
				mensaje = mensaje.append(Util.formatearMonto(montoITDolares,
						PATTERN_MONTO));
			}
			throw new BFPBusinessException(CODIGO_ERROR_MONTO,
					mensaje.toString());
		}
	}

	private void setDatosTitularCuentaDestino(TransferenciaBean transferencia,
			String tramaRespuesta, String tipoDocumentoDestino,
			String numeroDocumentoDestino) {
		transferencia.setDocumento(tipoDocumentoDestino);
		transferencia.setNroDocumento(numeroDocumentoDestino);
		transferencia.setApellidoPaterno(tramaRespuesta.substring(295, 325)
				.trim());
		if (TipoDocumento.DNI.name().equals(tipoDocumentoDestino)) {
			transferencia.setNombres(tramaRespuesta.substring(265, 295).trim());
			transferencia.setApellidoMaterno(tramaRespuesta.substring(325, 355)
					.trim());
		}
	}

	private void validacionOrigenTransferenciaCuentasTerceros(
			TipoTransferencia tipo, String tipoDocumentoOrigen,
			String numeroDocumentoOrigen, String tipoDocumentoDestino,
			String numeroDocumentoDestino) {
		if (tipo == TipoTransferencia.CT) {
			if (StringUtils.equals(tipoDocumentoOrigen, tipoDocumentoDestino)
					&& StringUtils.equals(numeroDocumentoOrigen,
							numeroDocumentoDestino)) {
				throw new BFPBusinessException(CODIGO_ERROR_CUENTA,
						MSG_VALIDACION_CTAS_TERCEROS);
			}
		}
	}

	private void validacionOrigenTransferenciasCuentasPropias(
			TipoTransferencia tipo, String tipoDocumentoOrigen,
			String numeroDocumentoOrigen, String tipoDocumentoDestino,
			String numeroDocumentoDestino) {
		if (tipo == TipoTransferencia.CP) {
			if (!(StringUtils.equals(tipoDocumentoOrigen, tipoDocumentoDestino) && StringUtils
					.equals(numeroDocumentoOrigen, numeroDocumentoDestino))) {
				throw new BFPBusinessException(CODIGO_ERROR_CUENTA,
						MSG_VALIDACION_CTAS_PROPIAS);
			}
		}
	}

	public TaServicioxEmpresa obtenerServicioEmpresa(String empresa,
			String tipoTransferencia) {
		TaServicioxEmpresa servicio;
		try {
			servicio = daoServicioEmpresa.buscarServicioEmpresa(empresa,
					tipoTransferencia);
		} catch (SQLException e) {
			throw new RuntimeException("Error obteniendo el servicioxempresa",
					e);
		}
		return servicio;
	}

	public ClienteBean validarInterbancaria(String cuentaCargo,
			String cuentaAbono, double monto) throws Exception {
		ClienteBean cliente = new ClienteBean();
		return cliente;
	}

	public ConsultaTransferenciasBean buscarTransferencias(String idEmpresa,
			String tipoTransferencia, String tipoDocumento,
			String nroDocumento, String estadoTransferencia,
			String codigoMoneda, String fechaIni, String fechaFin,
			BeanPaginacion beanPaginacion) {
		ConsultaTransferenciasBean consultaTransferenciasBean = transferenciaDaoIbs
				.buscarTransferencias(idEmpresa, tipoTransferencia,
						tipoDocumento, nroDocumento, estadoTransferencia,
						codigoMoneda, fechaIni, fechaFin,
						beanPaginacion.getM_regPagina(),
						beanPaginacion.getM_seleccion());
		if (logger.isDebugEnabled()) {
			logger.debug("Bean consulta transferencias encontrada: "
					+ ToStringBuilder
							.reflectionToString(consultaTransferenciasBean));
			if (consultaTransferenciasBean != null
					&& consultaTransferenciasBean.getTransferencias() != null) {
				logger.debug("Numero de trx enviadas: "
						+ consultaTransferenciasBean.getTransferencias().size());
			}
		}
		return consultaTransferenciasBean;
	}

	public List<TransferenciaBean> buscarTransferencias(String idEmpresa,
			String tipoTransferencia, String tipoDocumento,
			String nroDocumento, String estadoTransferencia,
			String codigoMoneda, String fechaIni, String fechaFin) {
		long posicionActual = 0;
		long numeroRegistrosConsulta = 0;
		List<TransferenciaBean> resultados = new ArrayList<TransferenciaBean>();
		ConsultaTransferenciasBean consultaTransferenciasBean = null;
		do {
			consultaTransferenciasBean = transferenciaDaoIbs
					.buscarTransferencias(idEmpresa, tipoTransferencia,
							tipoDocumento, nroDocumento, estadoTransferencia,
							codigoMoneda, fechaIni, fechaFin,
							TAMANIO_PAGINA_TRANSFERENCIAS, posicionActual);
			if (logger.isDebugEnabled()) {
				logger.debug("Posicion actual consulta: " + posicionActual);
			}
			posicionActual += TAMANIO_PAGINA_TRANSFERENCIAS;
			if (consultaTransferenciasBean != null) {
				numeroRegistrosConsulta = consultaTransferenciasBean
						.getTotalRegistroConsulta();
				if (logger.isDebugEnabled()) {
					logger.debug("Bean consulta transferencias encontrada: "
							+ new ToStringBuilder(consultaTransferenciasBean)
									.append("totalRegistroConsulta",
											consultaTransferenciasBean
													.getTotalRegistroConsulta())
									.append("totalRegistrosEnviados",
											consultaTransferenciasBean
													.getTotalRegistrosEnviados())
									.append("posicionActual",
											consultaTransferenciasBean
													.getPosicionActual()));
				}
				resultados.addAll(consultaTransferenciasBean
						.getTransferencias());
			}
		} while (posicionActual < numeroRegistrosConsulta);
		if (logger.isDebugEnabled()) {
			logger.debug("Registros encontrados: " + resultados.size());
		}
		return resultados;
	}

	public int obtenerCorrelativoTransferenciasDiarias() {
		/*
		 * El correlativo no se esta validando, salvo el valor cero. Es un
		 * pendiente hacer que IBS lo valide. Se utilizara una constante
		 */
		return CashConstants.CORRELATIVO_DIARIO;
	}

	public TxResult obtenerCodigoErrorIBS(String codigoIBS) {
		TxResult beanCodigoIBS = daoResult.obtenerNuevoCodigoIBS(codigoIBS);
		if (beanCodigoIBS == null) {
			return daoResult.selectByCodIbs(codigoIBS);
		} else {
			return beanCodigoIBS;
		}
	}

	public Map<String, String> obtenerLimitesTransferenciasFormateado() {
		Map<String, Double> limites = transferenciaDaoIbs
				.obtenerLimitesTransferencias();
		Map<String, String> limitesFormateado = null;
		if (limites != null) {
			limitesFormateado = new HashMap<String, String>();
			for (Iterator<Entry<String, Double>> iterator = limites.entrySet()
					.iterator(); iterator.hasNext();) {
				Entry<String, Double> entry = iterator.next();
				limitesFormateado.put(entry.getKey(),
						Util.formatearMonto(entry.getValue(), 2));
			}
		}
		return limitesFormateado;
	}

	public Map<String, Double> obtenerLimitesTransferencias() {
		return transferenciaDaoIbs.obtenerLimitesTransferencias();
	}

    public double obtenerLimitesDiarioTransferenciasSoles()
    {
        return transferenciaDaoIbs.obtenerLimitesTransferencias()
                .get(TransferenciaDaoIbs.CLAVE_MAP_LIMITE_DIARIO_SOLES);
    }

    public double obtenerLimitesDiarioTransferenciasDolares()
    {
        return transferenciaDaoIbs.obtenerLimitesTransferencias()
                .get(TransferenciaDaoIbs.CLAVE_MAP_LIMITE_DIARIO_DOLARES);

    }
    
    public double obtenerLimiteTransferenciaITSoles()
    {
        double limiteITSoles = 0.0;
        try
        {
            limiteITSoles = transferenciaDaoIbs.obtenerLimitesTransferencias().get(
                    TransferenciaDaoIbs.CLAVE_MAP_LIMITE_TRANSFERENCIA_IT_SOLES);
        }
        catch (Exception e)
        {
            throw new RuntimeException("No se pudo obtener el limite IT en soles. Verificar conexion con IBS/Six");
        }
        return limiteITSoles;
    }

    public double obtenerLimiteTransferenciaITDolares()
    {
        double limiteITDolares = 0.0;
        try
        {
            limiteITDolares = transferenciaDaoIbs.obtenerLimitesTransferencias().get(
                    TransferenciaDaoIbs.CLAVE_MAP_LIMITE_TRANSFERENCIAS_IT_DOLARES);
        }
        catch (Exception e)
        {
            throw new RuntimeException("No se pudo obtener el limite IT en dolares. Verificar conexion con IBS/Six");
        }
        return limiteITDolares;
    }

}
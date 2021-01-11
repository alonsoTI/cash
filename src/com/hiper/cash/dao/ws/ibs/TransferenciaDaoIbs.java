package com.hiper.cash.dao.ws.ibs;

import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.APP_CONSULTA_DETALLE;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.APP_CONSULTA_TRANSFERENCIAS;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.APP_REGISTRO_APROBACION;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.APP_REGISTRO_APROBACION_IB;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.APP_VALIDAR_CTAS_CONSULTAR_LIMITES;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.LONG_CONSULTA_DETALLE;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.LONG_CONSULTA_TRANSFERENCIAS;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.LONG_REGISTRO_APROBACION;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.LONG_REGISTRO_APROBACION_IB;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.LONG_VALIDAR_CTAS_CONSULTAR_LIMITES;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.TRX_CONSULTA_DETALLE;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.TRX_CONSULTA_TRANSFERENCIAS;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.TRX_REGISTRO_APROBACION;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.TRX_REGISTRO_APROBACION_IB;
import static com.hiper.cash.dao.ws.ibs.ConstantesIBS.TRX_VALIDAR_CTAS_CONSULTAR_LIMITES;
import static com.hiper.cash.util.CashConstants.STR_DDMMYYYYhhmmss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.financiero.cash.beans.AccionTransferenciaBean;
import com.financiero.cash.beans.ConsultaTransferenciasBean;
import com.financiero.cash.beans.TransferenciaBean;
import com.financiero.cash.exception.BFPBusinessException;
import com.financiero.cash.ibs.util.Constantes;
import com.financiero.cash.ibs.util.IbsUtils;
import com.financiero.cash.ibs.util.TipoDocumento;
import com.financiero.cash.util.AccionTransferencia;
import com.financiero.cash.util.EstadoTransferencia;
import com.financiero.cash.util.TipoMoneda;
import com.financiero.cash.util.TipoMovimientoTransferencia;
import com.financiero.cash.util.TipoTransferencia;
import com.googlecode.ehcache.annotations.Cacheable;
import com.hiper.cash.dao.TransferenciaDAO;
import com.hiper.cash.dao.TxResultDao;
import com.hiper.cash.dao.hibernate.TxResultDaoHibernate;
import com.hiper.cash.dao.ws.SixLinux;
import com.hiper.cash.domain.TxResult;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Util;

@Component( value="transferenciaDao")
public class TransferenciaDaoIbs implements TransferenciaDAO {

	public static final String CLAVE_MAP_LIMITE_TRANSFERENCIAS_IT_DOLARES = "montoITDolares";
	public static final String CLAVE_MAP_LIMITE_TRANSFERENCIA_IT_SOLES = "montoITSoles";
	public static final String CLAVE_MAP_LIMITE_DIARIO_DOLARES = "limiteDiarioDolares";
    public static final String CLAVE_MAP_LIMITE_DIARIO_SOLES = "limiteDiarioSoles";
    private SixLinux cliente = SixLinux.getInstance();
	private Logger logger = Logger.getLogger(this.getClass());	
	private TxResultDao txResultDao = new TxResultDaoHibernate();
	
	private TransferenciaDaoIbs() {		
	}

	@Override
	public TransferenciaBean registrar(TransferenciaBean transferencia, int correlativoDiario) {
		if(logger.isDebugEnabled() && transferencia!=null){
			logger.debug("Datos a registrar:" +transferencia.debug());
		}
        StringBuilder sb = armarTramaRegistroTransferencia(transferencia, correlativoDiario);
        logger.info("trama registro de transferencia: " + sb.toString());
        String tramaRptaIBS = cliente.enviarMensaje(APP_REGISTRO_APROBACION, TRX_REGISTRO_APROBACION,
                LONG_REGISTRO_APROBACION, sb.toString());
        logger.info("trama rpta registro transferencia: " + tramaRptaIBS);		
		return extraeRespuestaRegistroTransferencia(transferencia, tramaRptaIBS);
	}

	private TransferenciaBean extraeRespuestaRegistroTransferencia(TransferenciaBean transferencia, String tramaRptaIBS) {
		String codigoRptaIBS = IbsUtils.obtenerCodigoRespuestaIBS(tramaRptaIBS);
		if (!IbsUtils.esRespuestaExitosa(codigoRptaIBS)) {
			logger.warn("La transferencia no se puede realizar. Se obtuvo el codigo de error: "
					+ codigoRptaIBS+" . "+obtenerMensajeError(codigoRptaIBS));
			transferencia.setCodigoError(codigoRptaIBS);
		} else {
			int posicionActualTrama = Constantes.POSICION_INICIO_DATOS_TRAMA;
			long numeroTransferencia = Long.valueOf(tramaRptaIBS.substring(posicionActualTrama,
					posicionActualTrama += 12));
			transferencia.setNumero(numeroTransferencia);
			char flagProcesado = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 1)
					.charAt(0);
			transferencia.setProcesado(flagProcesado == '1' ? true : false);
		}
		return transferencia;
	}

	

	@Override
	public TransferenciaBean registrarIB(TransferenciaBean transferencia, int correlativoDiario) {
		StringBuilder sb = armarTramaRegistroTransferencia(transferencia, correlativoDiario);

			logger.info("trama registro de transferencia IB: " + sb.toString());
		
		String tramaRptaIBS = cliente.enviarMensaje(APP_REGISTRO_APROBACION_IB, TRX_REGISTRO_APROBACION_IB,
				LONG_REGISTRO_APROBACION_IB, sb.toString());
		

			logger.info("trama rpta registro transferencia IB: " + tramaRptaIBS);
		
		return extraeRespuestaRegistroTransferencia(transferencia, tramaRptaIBS);
	}

	@Override
	public TransferenciaBean buscarTransferencia(long id, String idEmpresa) {
		if (logger.isDebugEnabled()) {
			logger.debug("Id: " + id);
			logger.debug("IdEmpresa " + idEmpresa);
		}
		String tramaConsultaTransferencia = armarTramaConsultaTransferencia(id, idEmpresa).toString();

			logger.info("Trama de buscarTransferencia: '" + tramaConsultaTransferencia + "'");
		
		String tramaRespuesta = cliente.enviarMensaje(APP_CONSULTA_DETALLE, TRX_CONSULTA_DETALLE,
				LONG_CONSULTA_DETALLE, tramaConsultaTransferencia);
		
			logger.info("Trama de rpta buscarTransferencia: '" + tramaRespuesta + "'");
		
		String codigoIBS = IbsUtils.obtenerCodigoRespuestaIBS(tramaRespuesta);
		TransferenciaBean transferencia = null;
		if (IbsUtils.esRespuestaExitosa(codigoIBS)) {
			transferencia = new TransferenciaBean();
			int posicionActualTrama = Constantes.POSICION_INICIO_DATOS_TRAMA;
			String campoTrama = tramaRespuesta.substring(posicionActualTrama,
					posicionActualTrama += 12);
			transferencia.setNumero(Long.valueOf(campoTrama));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 1);
			transferencia.setEstado(campoTrama.charAt(0));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 20);
			transferencia.setCuentaCargo(campoTrama);
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 20);
			transferencia.setCuentaAbono(campoTrama);
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 3);
			transferencia.setCodigoMoneda(campoTrama);
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 18);
			transferencia.setMonto(IbsUtils.convertirDouble(campoTrama, 2));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 14);
			transferencia.setFechaRegistro(Util.obtenerFecha(campoTrama, STR_DDMMYYYYhhmmss));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 7);
			transferencia.setUsuarioRegistro(StringUtils.trimToNull(campoTrama));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 50);
			transferencia.setReferencia(StringUtils.trimToNull(campoTrama));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 2);
			transferencia.setTipo(campoTrama);
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 1);
			transferencia.setCodigoTipoDocumento(campoTrama.charAt(0));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 15);
			transferencia.setNroDocumento(StringUtils.trimToNull(campoTrama));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 30);
			transferencia.setApellidoPaterno((StringUtils.trimToNull(campoTrama)));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 30);
			transferencia.setApellidoMaterno((StringUtils.trimToNull(campoTrama)));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 30);
			transferencia.setNombres((StringUtils.trimToNull(campoTrama)));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 1);
			transferencia.setMismo(campoTrama.charAt(0));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 65);
			transferencia.setDireccion((StringUtils.trimToNull(campoTrama)));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 10);
			transferencia.setTelefono((campoTrama));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 2);
			transferencia.setTipoCuentaCargo(StringUtils.trimToNull(campoTrama));
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 3);
			transferencia.setMonedaCuentaCargo(TipoMoneda.validarCodigoMonedaValido(campoTrama));
			//moneda de la transferencia es igual a la moneda de la cuenta cargo
			transferencia.setCodigoMoneda(transferencia.getMonedaCuentaCargo());
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 2);			
			transferencia.setTipoCuentaAbono(StringUtils.trimToNull(campoTrama));			
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 3);
			transferencia.setMonedaCuentaAbono(TipoMoneda.validarCodigoMonedaValido(campoTrama));
			int numeroAcciones = Integer.valueOf(tramaRespuesta.substring(posicionActualTrama,
					posicionActualTrama += 2));			
			campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama+=11);
			transferencia.setTipoCambioRegistro(IbsUtils.convertirDouble(campoTrama,6));
			if (numeroAcciones > 0) {
				List<AccionTransferenciaBean> accionesTransferencia = new ArrayList<AccionTransferenciaBean>();
				for (int i = 0; i < numeroAcciones; i++) {					
					AccionTransferenciaBean accionBean = new AccionTransferenciaBean();
					campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 7);					
					accionBean.setCodigoUsuario(StringUtils.trimToNull(campoTrama));
					campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 14);					
					accionBean.setFecha(Util.obtenerFecha(campoTrama, STR_DDMMYYYYhhmmss));
					campoTrama = tramaRespuesta.substring(posicionActualTrama, posicionActualTrama += 1);					
					accionBean.setTipoMovimientoTransferencia(TipoMovimientoTransferencia
							.getTipoMovimiento(campoTrama.charAt(0)));
					accionesTransferencia.add(accionBean);
				}
				transferencia.setAcciones(accionesTransferencia);
			}
		} else {
			logger.warn("No se pudo consultar el detalle de la transferencia. Se obtuvo el codigo de error: "
					+ codigoIBS+" . "+obtenerMensajeError(codigoIBS));
		}
		return transferencia;
	}


	@Override
	public ConsultaTransferenciasBean buscarTransferenciasPendientes(String idEmpresa, String usuario,
			BeanPaginacion beanPaginacion) {
		StringBuilder sb = armarTramaBusquedaTransferenciasPendientes(idEmpresa, usuario, beanPaginacion);


			logger.info("trama de busqueda de trx pendientes: " + sb.toString());
		

		String tramaRptaIBS = cliente.enviarMensaje(APP_CONSULTA_TRANSFERENCIAS, TRX_CONSULTA_TRANSFERENCIAS,
				LONG_CONSULTA_TRANSFERENCIAS, sb.toString());

			logger.info("trama rpta busqueda de trx pendientes: " + tramaRptaIBS);
		
		ConsultaTransferenciasBean resultados = extraerResultadosConsultaTransferencias(tramaRptaIBS);
		return resultados;
	}

	private ConsultaTransferenciasBean extraerResultadosConsultaTransferencias(String tramaRptaIBS) {
		String codigoRptaIBS = IbsUtils.obtenerCodigoRespuestaIBS(tramaRptaIBS);
		List<TransferenciaBean> transferencias = new ArrayList<TransferenciaBean>();
		ConsultaTransferenciasBean resultados = null;
		if (IbsUtils.esRespuestaExitosa(codigoRptaIBS)) {
			// obtener la cabecera
			resultados = new ConsultaTransferenciasBean();
			int posicionActualTrama = Constantes.POSICION_INICIO_DATOS_TRAMA;
			String campoTrama = tramaRptaIBS
					.substring(posicionActualTrama, posicionActualTrama += 10);
			resultados.setTotalRegistroConsulta(Long.valueOf(campoTrama));
			campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 10);
			resultados.setTotalRegistrosEnviados(Long.valueOf(campoTrama));
			campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 10);
			resultados.setPosicionActual(Long.valueOf(campoTrama));
			// obtener la lista de resultados
			for (int i = 0; i < resultados.getTotalRegistrosEnviados(); i++) {
				TransferenciaBean transferencia = new TransferenciaBean();
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 12);
				transferencia.setNumero(Long.valueOf(campoTrama));
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 20);
				transferencia.setCuentaCargo(campoTrama);
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 20);
				transferencia.setCuentaAbono(campoTrama);
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 2);
				transferencia.setTipo(campoTrama);
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 30);
				transferencia.setApellidoPaterno((StringUtils.trimToNull(campoTrama)));
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 30);
				transferencia.setApellidoMaterno((StringUtils.trimToNull(campoTrama)));
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 30);
				transferencia.setNombres((StringUtils.trimToNull(campoTrama)));
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 1);
				transferencia.setEstado(campoTrama.charAt(0));
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 14);
				transferencia.setFechaRegistro(Util.obtenerFecha(campoTrama, STR_DDMMYYYYhhmmss));
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 3);
				transferencia.setCodigoMoneda(campoTrama);
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 18);
				transferencia.setMonto(IbsUtils.convertirDouble(campoTrama, 2));
				campoTrama = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 3);
				transferencia.setMonedaCuentaAbono(campoTrama);
				transferencias.add(transferencia);
			}
			resultados.setTransferencias(transferencias);
		}else if(codigoRptaIBS.equals(CashConstants.COD_IBS_NO_EXISTEN_TRANSFERENCIAS)){
		    resultados = new ConsultaTransferenciasBean();
		    resultados.setTransferencias(transferencias);
			logger.info("No existen transferencias registradas");
			
		}		
		else {
			logger.warn("no se puede consultar las transferencias. Se obtuvo el error: " + codigoRptaIBS+ " . "
					+obtenerMensajeError(codigoRptaIBS));
		}
		return resultados;
	}

	private StringBuilder armarTramaConsultaTransferencia(Long idTransferencia, String idEmpresa) {
		StringBuilder sb = new StringBuilder();
		sb = sb.append(CashConstants.PREFIJO_CASH_MANAGEMENT);
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(idEmpresa, 9));
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(idTransferencia, 12));
		return sb;
	}

	private StringBuilder armarTramaRegistroTransferencia(TransferenciaBean transferencia, int correlativoDiario) {
		StringBuilder sb = new StringBuilder();
		sb = sb.append(CashConstants.PREFIJO_CASH_MANAGEMENT);
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(transferencia.getIdEmpresa(), 9));
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(0, 12));
		sb = sb.append(IbsUtils.formatearValorCaracterIbs(transferencia.getUsuarioRegistro(),7)); 				
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(transferencia.getTipo().getValor(), 2));
		sb = sb.append(AccionTransferencia.REGISTRAR.getCodigo());
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(correlativoDiario, 5));
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(transferencia.getCuentaCargo(), 20));
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(transferencia.getCuentaAbono(), 20));
		sb = sb.append(IbsUtils.formatearValorCaracterIbs(transferencia.getNombres(), 30));
		sb = sb.append(IbsUtils.formatearValorCaracterIbs(transferencia.getApellidoPaterno(), 30));
		sb = sb.append(IbsUtils.formatearValorCaracterIbs(transferencia.getApellidoMaterno(), 30));
		if (transferencia.getDocumento() != null) {
			char tipoDocumentoIBS = TipoDocumento.getCodigoIBS(transferencia.getDocumento());
			if (CashConstants.CHAR_BLANK_SPACE.equals(tipoDocumentoIBS)) {
				throw new RuntimeException("El tipo de documento no es valido");
			}
			sb = sb.append(tipoDocumentoIBS);
		} else {
			sb = sb.append(CashConstants.CHAR_BLANK_SPACE);
		}
		sb = sb.append(IbsUtils.formatearValorCaracterIbs(transferencia.getNroDocumento(), 15));
		sb = sb.append(IbsUtils.formatearValorCaracterIbs(transferencia.getDireccion(), 65));
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(transferencia.getTelefono(), 10));
		sb = sb.append(IbsUtils.formatearValorCaracterIbs(transferencia.getReferencia(), 50));
		sb = sb.append(transferencia.getCodigoMoneda());		
		String montoFormateado = IbsUtils.formateaValorDecimalIbs(transferencia.getMonto(), 18, 2);		
		if(logger.isDebugEnabled()){
			logger.debug("monto formateado para registro de trx: "+montoFormateado);
		}
		sb = sb.append(montoFormateado);
		sb = sb.append(transferencia.getMismo());
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(transferencia.getNumeroAprobadores(), 2));
		return sb;
	}

	private StringBuilder armarTramaBusquedaTransferenciasPendientes(String idEmpresa, String usuario,
			BeanPaginacion beanPaginacion) {
		StringBuilder sb = new StringBuilder();
		sb = sb.append(CashConstants.PREFIJO_CASH_MANAGEMENT);
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(idEmpresa, 9));
		sb = sb.append(IbsUtils.formatearValorCaracterIbs(usuario,7));
		sb = sb.append(IbsUtils.cadenaConCeros(2));// tipo transferencia
		sb = sb.append(EstadoTransferencia.PENDIENTE_APROBACION.getValor());
		sb = sb.append(IbsUtils.cadenaConCeros(1));		
		sb = sb.append(IbsUtils.cadenaConEspacios(15));
		sb = sb.append(IbsUtils.cadenaConEspacios(3));
		sb = sb.append(IbsUtils.cadenaConCeros(16));
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(beanPaginacion.getM_regPagina(), 10));
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(beanPaginacion.getM_seleccion(), 10));
		return sb;
	}

	private StringBuilder armarTramaBusquedaTransferencias(String idEmpresa, String tipoTransferencia,
			String tipoDocumento, String nroDocumento, String estadoTransferencia, String codigoMoneda,
			String fechaIni, String fechaFin, int numeroRegistros, long posicionInicial) {
		if(logger.isDebugEnabled()){
			logger.debug("Empresa: "+idEmpresa);
			logger.debug("Tipo transferencia: "+tipoTransferencia);
			logger.debug("Tipo documento: "+tipoDocumento);
			logger.debug("Nro documento: "+nroDocumento);
			logger.debug("Estado transferencia: "+estadoTransferencia);
			logger.debug("Codigo moneda: "+codigoMoneda);
			logger.debug("Fecha inicio: "+fechaIni);
			logger.debug("Fecha fin: "+fechaFin);
			logger.debug("Numero registros: "+numeroRegistros);
			logger.debug("Posicion inicial: "+posicionInicial);
		}
		StringBuilder sb = new StringBuilder();
		sb = sb.append(CashConstants.PREFIJO_CASH_MANAGEMENT);
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(idEmpresa, 9));
		sb = sb.append(IbsUtils.cadenaConEspacios(7));
		TipoTransferencia tipo = TipoTransferencia.getTipo(tipoTransferencia);
		if (tipo != null) {
			sb = sb.append(tipo.getValor());
		} else {
			sb = sb.append(IbsUtils.cadenaConCeros(2));
		}
		if (StringUtils.isNotBlank(estadoTransferencia)) {
			EstadoTransferencia estado = EstadoTransferencia.getEstado(estadoTransferencia.charAt(0));
			if (estado != null) {
				sb = sb.append(estado.getValor());
			} else {
				sb = sb.append(IbsUtils.cadenaConCeros(1));
			}
		} else {
			sb = sb.append(IbsUtils.cadenaConCeros(1));
		}
		if (StringUtils.isNotBlank(tipoDocumento)) {
			TipoDocumento tipoDeDocumento = TipoDocumento.getTipoDocumento(tipoDocumento.charAt(0));
			if (tipoDeDocumento != null) {
				sb = sb.append(tipoDeDocumento.getCodigoIBS());
			} else {
				sb = sb.append(IbsUtils.cadenaConCeros(1));
			}
		} else {
			sb = sb.append(IbsUtils.cadenaConCeros(1));
		}
		sb = sb.append(IbsUtils.formatearValorCaracterIbs(nroDocumento, 15));
		String codigoTipoMoneda = TipoMoneda.validarCodigoMonedaValido(codigoMoneda);
		if (StringUtils.isNotEmpty(codigoTipoMoneda)) {
			sb = sb.append(codigoTipoMoneda);
		} else {
			sb = sb.append(IbsUtils.cadenaConEspacios(3));
		}
		if (fechaIni != null && fechaIni.length() == 8) {
			sb = sb.append(fechaIni);
		} else {
			sb = sb.append(IbsUtils.cadenaConCeros(8));
		}
		if (fechaFin != null && fechaFin.length() == 8) {
			sb = sb.append(fechaFin);
		} else {
			sb = sb.append(IbsUtils.cadenaConCeros(8));
		}
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(numeroRegistros, 10));
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(posicionInicial, 10));
		return sb;
	}

	@Override
	public TransferenciaBean registrarAccion(TransferenciaBean transferencia, String usuario,
			AccionTransferencia accion, int correlativoDiario) {		
		StringBuilder sb = armarTramaRegistrarAccionTransferencia(transferencia.getIdEmpresa(),
				transferencia.getNumero(), usuario, accion.getCodigo(), correlativoDiario);

			logger.info("trama de registrar accion: " + sb.toString());
		
		String tramaRptaIBS = cliente.enviarMensaje(APP_REGISTRO_APROBACION, TRX_REGISTRO_APROBACION,
				LONG_REGISTRO_APROBACION, sb.toString());
		
			logger.info("trama rpta registrar accion: " + tramaRptaIBS);
		
		extraerRespuestaRegistrarAccion(transferencia, tramaRptaIBS);
		return transferencia;
	}

	private void extraerRespuestaRegistrarAccion(TransferenciaBean transferencia, String tramaRptaIBS) {
		String codigoRptaIBS = IbsUtils.obtenerCodigoRespuestaIBS(tramaRptaIBS);
		if (!IbsUtils.esRespuestaExitosa(codigoRptaIBS)) {
			logger.warn("La accion no se pudo registrar. Se obtuvo el codigo de error: " + codigoRptaIBS+ " . "+
					obtenerMensajeError(codigoRptaIBS));
			transferencia.setCodigoError(codigoRptaIBS);
		} else {
			int posicionActualTrama = Constantes.POSICION_INICIO_DATOS_TRAMA;
			posicionActualTrama+=12;
			char flagProcesado = tramaRptaIBS.substring(posicionActualTrama, posicionActualTrama += 1)
					.charAt(0);
			transferencia.setProcesado(flagProcesado == '1' ? true : false);
		}
	}

	@Override
	public TransferenciaBean registrarAccionIB(TransferenciaBean transferencia, String usuario,
			AccionTransferencia accion, int correlativoDiario) {
		StringBuilder sb = armarTramaRegistrarAccionTransferencia(transferencia.getIdEmpresa(),
				transferencia.getNumero(), usuario, accion.getCodigo(),correlativoDiario);
		
			logger.info("trama de registrar accion IB: " + sb.toString());
		
		String tramaRptaIBS = cliente.enviarMensaje(APP_REGISTRO_APROBACION_IB, TRX_REGISTRO_APROBACION_IB,
				LONG_REGISTRO_APROBACION_IB, sb.toString());

		
			logger.info("trama rpta registrar accion IB: " + tramaRptaIBS);
		
		extraerRespuestaRegistrarAccion(transferencia, tramaRptaIBS);
		return transferencia;
	}

	private StringBuilder armarTramaRegistrarAccionTransferencia(long empresa, long nroTransferencia,
			String usuario, char flagAccionTransferencia, int correlativoDiario) {
		if(logger.isDebugEnabled()){
			logger.debug("Empresa: "+empresa);
			logger.debug("Nro Transferencia: "+nroTransferencia);
			logger.debug("Usuario: "+usuario);
			logger.debug("Accion: "+flagAccionTransferencia);
			logger.debug("Correlativo diario: "+correlativoDiario);
		}
		StringBuilder sb = new StringBuilder();
		sb = sb.append(CashConstants.PREFIJO_CASH_MANAGEMENT);
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(empresa, 9));
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(nroTransferencia, 12));
		sb = sb.append(IbsUtils.formatearValorCaracterIbs(usuario,7));
		sb = sb.append(IbsUtils.cadenaConCeros(2));
		sb = sb.append(flagAccionTransferencia);
		sb = sb.append(IbsUtils.formateaValorNumericoIbs(correlativoDiario, 5));
		sb = sb.append(IbsUtils.cadenaConCeros(20 + 20));
		sb = sb.append(IbsUtils.cadenaConEspacios(90));
		sb = sb.append(IbsUtils.cadenaConCeros(1));
		sb = sb.append(IbsUtils.cadenaConEspacios(80));
		sb = sb.append(IbsUtils.cadenaConCeros(10));
		sb = sb.append(IbsUtils.cadenaConEspacios(53));
		sb = sb.append(IbsUtils.cadenaConCeros(21));
		return sb;
	}

	@Override
	public ConsultaTransferenciasBean buscarTransferencias(String idEmpresa, String tipoTransferencia,
			String tipoDocumento, String nroDocumento, String estadoTransferencia, String codigoMoneda,
			String fechaIni, String fechaFin, int numeroRegistros, long posicionInicial) {		
		StringBuilder sb = armarTramaBusquedaTransferencias(idEmpresa, tipoTransferencia, tipoDocumento,
				nroDocumento, estadoTransferencia, codigoMoneda, fechaIni, fechaFin, numeroRegistros,
				posicionInicial);
		
			logger.info("trama de busqueda de trxs: " + sb.toString());
		
		String tramaRptaIBS = cliente.enviarMensaje(APP_CONSULTA_TRANSFERENCIAS, TRX_CONSULTA_TRANSFERENCIAS,
				LONG_CONSULTA_TRANSFERENCIAS, sb.toString());

		
			logger.info("trama rpta busqueda de trxs: " + tramaRptaIBS);
		
		ConsultaTransferenciasBean resultados = extraerResultadosConsultaTransferencias(tramaRptaIBS);
		return resultados;
	}
	
	@Override
	@Cacheable(cacheName = "oneHourCache")
	public Map<String,Double> obtenerLimitesTransferencias(){
		String tramaRptaIBS = invocarConsultaLimitesTransferencias();
		String codigoRptaIBS = IbsUtils.obtenerCodigoRespuestaIBS(tramaRptaIBS);
		Map<String,Double> limites = null;
		if (IbsUtils.esRespuestaExitosa(codigoRptaIBS)) {
			double montoITSoles = IbsUtils.convertirDouble(tramaRptaIBS.substring(434,449),2);
			double montoITDolares= IbsUtils.convertirDouble(tramaRptaIBS.substring(449,464),2);
			double limiteDiarioSoles = IbsUtils.convertirDouble(tramaRptaIBS.substring(374,389),2);			
			double limiteDiarioDolares= IbsUtils.convertirDouble(tramaRptaIBS.substring(404,419),2);
			if(logger.isDebugEnabled()){
				logger.debug("montoITSoles: "+montoITSoles);
				logger.debug("montoITDolares: "+montoITDolares);
				logger.debug("limiteDiarioSoles: "+limiteDiarioSoles);
				logger.debug("limiteDiarioDolares: "+limiteDiarioDolares);
			}
			limites = new HashMap<String, Double>();
			limites.put(CLAVE_MAP_LIMITE_TRANSFERENCIA_IT_SOLES, montoITSoles);
			limites.put(CLAVE_MAP_LIMITE_TRANSFERENCIAS_IT_DOLARES, montoITDolares);
			limites.put(CLAVE_MAP_LIMITE_DIARIO_SOLES, limiteDiarioSoles);			
			limites.put(CLAVE_MAP_LIMITE_DIARIO_DOLARES, limiteDiarioDolares);						
		} else {
			logger.error("Se obtuvo el codigo de error al obtener los limites de la transferencia "
					+ codigoRptaIBS + ". " + obtenerMensajeError(codigoRptaIBS));
		}
		return limites;		
	}

	private String invocarConsultaLimitesTransferencias() {
		StringBuilder sb = new StringBuilder();

		sb.append(IbsUtils.cadenaConEspacios(1));
		sb.append(IbsUtils.cadenaConCeros(41));
		sb.append(IbsUtils.cadenaConEspacios(15));
		
			logger.info("trama consulta de limites: "+sb.toString());
		

		String tramaRptaIBS = cliente.enviarMensaje(APP_VALIDAR_CTAS_CONSULTAR_LIMITES, TRX_VALIDAR_CTAS_CONSULTAR_LIMITES,
				LONG_VALIDAR_CTAS_CONSULTAR_LIMITES, sb.toString());
		

		
			logger.info("trama rpta consulta de limites: " + tramaRptaIBS);
		
		return tramaRptaIBS;
	}
	
	private String obtenerMensajeError(String codigoIBS) {
		TxResult beanCodigoIBS = txResultDao.obtenerNuevoCodigoIBS(codigoIBS);
		if (beanCodigoIBS == null) {
			beanCodigoIBS = txResultDao.selectByCodIbs(codigoIBS);
		}
		String mensaje = null;
		if (beanCodigoIBS != null) {
			mensaje = beanCodigoIBS.getDrsDescription();
		}
		return mensaje;
	}
	
    public String validarDatosTransferencia(TransferenciaBean transferencia)
    {
        String tramaValidacionCuentas = armarTramaValidacionCuentas(transferencia);
        logger.info("Trama de validacionCuentas: " + tramaValidacionCuentas);
        String tramaRespuesta = cliente.enviarMensaje(APP_VALIDAR_CTAS_CONSULTAR_LIMITES,
                TRX_VALIDAR_CTAS_CONSULTAR_LIMITES, LONG_VALIDAR_CTAS_CONSULTAR_LIMITES, tramaValidacionCuentas);
        logger.info("Trama de rpta validacionCuentas: " + tramaRespuesta);
        return tramaRespuesta;
    }
	
	private String armarTramaValidacionCuentas(TransferenciaBean transferencia) {
        TipoTransferencia tipo = transferencia.getTipo();
        StringBuilder sb = new StringBuilder();
        if (tipo != TipoTransferencia.IT) {
            sb = new StringBuilder("C");
            sb = sb.append(IbsUtils.formateaValorNumericoIbs(transferencia.getCuentaCargo(), 20));
            sb = sb.append(IbsUtils.formateaValorNumericoIbs(transferencia.getCuentaAbono(), 20));
            sb = sb.append(IbsUtils.cadenaConCeros(16));
        } else {
            String codigo = transferencia.getCuentaAbono().substring(0, 3);
            if (codigo.contains(CashConstants.COD_BANCO_FINANCIERO)) {
                throw new BFPBusinessException(CashConstants.CODIGO_ERROR_CUENTA,
                        "La cuenta de abono pertenece al Banco Pichincha");
            }
            sb = new StringBuilder("I");            
            sb = sb.append(IbsUtils.formateaValorNumericoIbs(transferencia.getCuentaCargo(), 20));
            sb = sb.append(IbsUtils.formateaValorNumericoIbs(transferencia.getCuentaAbono(), 20));                      
            sb = sb.append(TipoDocumento.getCodigoIBS(transferencia.getDocumento()));
            sb = sb.append(transferencia.getNroDocumento());            
        }
        return sb.toString();
    }
}

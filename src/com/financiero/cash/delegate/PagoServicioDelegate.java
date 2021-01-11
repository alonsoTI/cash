package com.financiero.cash.delegate;

import static com.hiper.cash.util.CashConstants.EMP_SERV_SEDAPAL;
import static com.hiper.cash.util.CashConstants.SERV_AGUA;
import static com.hiper.cash.util.CashConstants.VAL_IBS_DOLARES;
import static com.hiper.cash.util.CashConstants.VAL_IBS_EUROS;
import static com.hiper.cash.util.CashConstants.VAL_IBS_SOLES;
import static com.hiper.cash.util.CashConstants.VAL_SOLES_SIMB;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.financiero.cash.ui.model.ReciboPagoServicio;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TaSecuencialDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TaSecuencialDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TpDetalleOrdenDaoHibernate;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.domain.TaOrdenId;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.domain.TpDetalleOrden;
import com.hiper.cash.domain.TpDetalleOrdenId;
import com.hiper.cash.domain.TpDetalleOrdenIdRep;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;

public class PagoServicioDelegate {

	private static PagoServicioDelegate instanciaUnica;
	
	private TaServicioxEmpresaDao daoServicioEmpresa = new TaServicioxEmpresaDaoHibernate();
	private TpDetalleOrdenDao daoDetalle = new TpDetalleOrdenDaoHibernate();
	private TaOrdenDao daoOrden = new TaOrdenDaoHibernate();

	public static PagoServicioDelegate getInstance() {
		if (instanciaUnica == null) {
			instanciaUnica = new PagoServicioDelegate();
		}
		return instanciaUnica;
	}

	public PagoServicioDelegate() {
		
	}

	public String pagarReciboAgua(long idServEmp, double monto, String moneda,
			String nroCuentaOrigen, String tipoCuentaOrigen,
			String nroCuentaDestino, String nroDocumento, String referencia,
			String nroTarjeta, String fechaVencimiento, String codigoIBS) throws Exception {

		TaOrdenDao daoOrden = new TaOrdenDaoHibernate();
		TpDetalleOrdenDao daoDetalle = new TpDetalleOrdenDaoHibernate();
		TaSecuencialDao daoSecuencial = new TaSecuencialDaoHibernate();
		TaServicioxEmpresa servicioEmpresa = daoServicioEmpresa.getServicioEmpresa(idServEmp);
		

		Long idOrden = new Long(daoSecuencial.getIdEnvio(Constantes.FIELD_CASH_SECUENCIAL_ID_ORDEN));

		TaOrdenId id = new TaOrdenId(idOrden, idServEmp);

		TaOrden orden = new TaOrden();
		orden.setId(id);
		String fecha = Fecha.getFechaActual("yyyyMMdd");
		String hora = Fecha.getFechaActual("HHmmss");
		orden.setForFechaRegistro(fecha);
		orden.setForFechaInicio(fecha);
		orden.setForFechaFin(fecha);
		orden.setHorHoraInicio(hora);
		orden.setCorEstado(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
		orden.setNorNumeroCuenta(nroCuentaOrigen);
		orden.setDorReferencia(SERV_AGUA);

		
		orden.setCorMoneda(moneda);
		orden.setNorNumeroRegistros(0);
		orden.setCorEstadoMontoDolares('0');
		orden.setCorEstadoMontoSoles('0');
		orden.setCorEstadoMontoEuros('0');
		orden.setDorTipoCuenta(tipoCuentaOrigen);
		daoOrden.insert(orden);		
		

		TpDetalleOrden detalle = new TpDetalleOrden();
		
		TpDetalleOrdenId idDetalle = new TpDetalleOrdenId();
		TpDetalleOrdenIdRep idDetalleRep = new TpDetalleOrdenIdRep();
		
		idDetalle.setCdoidOrden(id.getCorIdOrden());
		idDetalle.setCdoidServicioEmpresa(idServEmp);
		idDetalle.setCdoidDetalleOrden(1);

		idDetalleRep.setCdoidOrden(id.getCorIdOrden());
		idDetalleRep.setCdoidServicioEmpresa(idServEmp);
		idDetalleRep.setCdoidDetalleOrden(1);
		
		detalle.setId(idDetalle);
		detalle.setDdoreferencia(referencia);
		detalle.setNdomonto(new BigDecimal(monto));
		detalle.setCdomoneda(VAL_IBS_SOLES);
		detalle.setNdonumeroCuenta(EMP_SERV_SEDAPAL);
		detalle.setNdonumCuentaAbono(nroCuentaDestino);
		detalle.setNdonumCuentaCargo(nroCuentaOrigen);
		detalle.setNdodocumento(nroDocumento);
		detalle.setCdoestado(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
		detalle.setFdofechaProceso(fecha);
		detalle.setDdocontrapartida(EMP_SERV_SEDAPAL);
		detalle.setCdocodigoRptaIbs(codigoIBS);
		detalle.setHdohoraProceso(hora);		
		detalle.setFdofechaVenc(Util.strFecha(fechaVencimiento, CashConstants.YYYYMMDD));
		
	
		
		Map<String, BigDecimal> montos = daoDetalle.registrar(detalle);
		daoOrden.update(idOrden, idServEmp, montos.get(VAL_IBS_SOLES), montos
				.get(VAL_IBS_DOLARES), montos.get(VAL_IBS_EUROS), 1);
		
		// Replica		
		orden.setNorMontoSoles(montos.get(VAL_IBS_SOLES));
		orden.setNorMontoDolares(montos.get(VAL_IBS_DOLARES));
		orden.setNorMontoEuros(montos.get(VAL_IBS_EUROS));
		orden.setNombreEstado("Procesado");
		orden.setNorNumeroRegistros(1);
		orden.setNombreServicio(servicioEmpresa.getDsemDescripcion());
		long idItemDetalle= daoDetalle.obtenerIdDetalleOrden(detalle);
		
		detalle.setIdRep(idDetalleRep);
		detalle.setCdoidItemDetalle(idItemDetalle);
		daoOrden.insertReplica(orden);		
		daoDetalle.registrarReplica(detalle);
		
		return idOrden.toString();
	}


	public TaOrden pagarReciboOnLine(long idServEmp,String moneda, String nroCuentaOrigen,	String tipoCuentaOrigen,
									String servicio) throws Exception {

		TaOrdenDao daoOrden = new TaOrdenDaoHibernate();
		TaSecuencialDao daoSecuencial = new TaSecuencialDaoHibernate();

		Long idOrden = new Long(daoSecuencial.getIdEnvio(Constantes.FIELD_CASH_SECUENCIAL_ID_ORDEN));

		TaOrdenId id = new TaOrdenId(idOrden, idServEmp);

		TaOrden orden = new TaOrden();
		orden.setId(id);
		String fecha = Fecha.getFechaActual("yyyyMMdd");
		String hora = Fecha.getFechaActual("HHmmss");
		orden.setForFechaRegistro(fecha);
		orden.setForFechaInicio(fecha);
		orden.setForFechaFin(fecha);
		orden.setHorHoraInicio(hora);
		orden.setCorEstado(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
		orden.setNorNumeroCuenta(nroCuentaOrigen);
		orden.setDorReferencia(servicio.toUpperCase());		
		
		orden.setCorMoneda(moneda);
		orden.setNorNumeroRegistros(0);
		orden.setCorEstadoMontoDolares('0');
		orden.setCorEstadoMontoSoles('0');
		orden.setCorEstadoMontoEuros('0');
		orden.setDorTipoCuenta(tipoCuentaOrigen);
		orden.setNorMontoSoles(new BigDecimal("0.0").setScale(2));
		daoOrden.insert(orden);
		orden.setTpDetalleOrdens(new ArrayList<TpDetalleOrden>());
		
		return orden;
	}
	
	
	private void  pagarReciboOnLineReplica(TaOrden orden) throws Exception {
		TaOrdenDao daoOrden = new TaOrdenDaoHibernate();
		int n = orden.getTpDetalleOrdens().size();
		orden.setNorNumeroRegistros(n);
		daoOrden.insertReplica(orden);		
	}
	
	public void agregarDetalle(TaOrden orden,long idServEmp,String tipoServicio,
								String referencia, double monto,
								String nroCuentaOrigen, String nroCuentaDestino,
								String nroDocumento,String codigoIBS,
								String referenciaEmpresa,String recibo,
								String fechaEmision,String cliente) throws Exception{
		
		String hora = Fecha.getFechaActual("HHmmss");		
		TpDetalleOrden detalle = new TpDetalleOrden();
		List<TpDetalleOrden> detalles = orden.getTpDetalleOrdens();
		
		TpDetalleOrdenId idDetalle = new TpDetalleOrdenId();
		idDetalle.setCdoidOrden(orden.getId().getCorIdOrden());
		idDetalle.setCdoidServicioEmpresa(idServEmp);
		idDetalle.setCdoidDetalleOrden(detalles.size()+1);
		detalle.setId(idDetalle);
		detalle.setDdoreferencia(referencia);
		detalle.setNdomonto(BigDecimal.valueOf(monto));	
		detalle.setCdomoneda(VAL_IBS_SOLES);
		detalle.setNdonumeroCuenta(referenciaEmpresa);
		detalle.setNdonumCuentaAbono(nroCuentaDestino);
		detalle.setNdonumCuentaCargo(nroCuentaOrigen);
		detalle.setNdodocumento(nroDocumento);
		detalle.setDdocomprobante(recibo);		
		StringBuilder sb = new StringBuilder(referenciaEmpresa.trim());		
		detalle.setDdocontrapartida(sb.toString());
		detalle.setDdodescripcion(tipoServicio);
		detalle.setCdoestado(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO);
		detalle.setFdofechaProceso(orden.getForFechaRegistro());
		detalle.setHdohoraProceso(hora);
		detalle.setDdoreferenciaAdicional(fechaEmision);
		if( cliente.length() > 50 ){
			detalle.setDdonombreBenef(cliente.substring(0, 49));
		}else{
			detalle.setDdonombreBenef(cliente);
		}
		
		detalle.setCdocodigoRptaIbs(codigoIBS);
		detalles.add(detalle);
		
	}
	
	public List<ReciboPagoServicio> registrarDetalles(TaOrden orden) throws Exception{
		
		List<TpDetalleOrden> detalles =  orden.getTpDetalleOrdens();
		Map<String, BigDecimal> montos  =daoDetalle.registrar(detalles);
		TaOrdenId id = orden.getId();
		daoOrden.update(id.getCorIdOrden(), id.getCorIdServicioEmpresa(),montos.get(VAL_IBS_SOLES), montos.get(VAL_IBS_DOLARES), montos.get(VAL_IBS_EUROS) , detalles.size());
		
		TaServicioxEmpresa servicioEmpresa = daoServicioEmpresa.getServicioEmpresa(id.getCorIdServicioEmpresa());
		orden.setNorMontoSoles(montos.get(VAL_IBS_SOLES));
		orden.setNorMontoDolares(montos.get(VAL_IBS_DOLARES));
		orden.setNorMontoEuros(montos.get(VAL_IBS_EUROS));
		orden.setNombreEstado("Procesado");
		orden.setNombreServicio(servicioEmpresa.getDsemDescripcion());
		
		pagarReciboOnLineReplica(orden);
		
		TpDetalleOrdenIdRep idDetalleRep;		
		
		long idItemDetalle = 0;
		List<ReciboPagoServicio> recibos = new ArrayList<ReciboPagoServicio>();
		ReciboPagoServicio recibo = null;
		for( TpDetalleOrden detalle : detalles){
				recibo = new ReciboPagoServicio();
				recibo.setCliente(detalle.getDdonombreBenef());
				recibo.setFechaEmision(Fecha.convertFromFechaSQL(detalle.getDdoreferenciaAdicional()));
				recibo.setNroRecibo(detalle.getDdocomprobante());
				recibo.setImporte(new StringBuilder(VAL_SOLES_SIMB).append(detalle.getNdomonto()).toString());
				recibos.add(recibo);
				
				// replica
				idDetalleRep = new TpDetalleOrdenIdRep();	
				idDetalleRep.setCdoidOrden(id.getCorIdOrden());
				idDetalleRep.setCdoidServicioEmpresa(id.getCorIdServicioEmpresa());
				idDetalleRep.setCdoidDetalleOrden(detalle.getId().getCdoidDetalleOrden());
				idItemDetalle= daoDetalle.obtenerIdDetalleOrden(detalle);		
				detalle.setIdRep(idDetalleRep);
				detalle.setCdoidItemDetalle(idItemDetalle);
				daoDetalle.registrarReplica(detalle);
		}
		return recibos;
	}
	
	
	

}

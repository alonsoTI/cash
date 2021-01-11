package com.financiero.cash.delegate;

import static com.hiper.cash.util.CashConstants.VAL_SOLES_SIMB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.financiero.cash.dao.EntityDAO;
import com.financiero.cash.ui.model.ReciboPagoServicio;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.hibernate.TaOrdenDaoHibernate;
import com.hiper.cash.dao.hibernate.TpDetalleOrdenDaoHibernate;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.domain.TpDetalleOrden;
import com.hiper.cash.entidad.BeanSuccessDetail;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Fecha;

public class PagoServicioComprobantes {

	private TaOrden orden;
	private List<ReciboPagoServicio> recibos;
	private TaOrdenDao daoOrden = new TaOrdenDaoHibernate();
	private TpDetalleOrdenDao daoDetalleOrden = new TpDetalleOrdenDaoHibernate();

	public PagoServicioComprobantes(long idOrden, long servicio)
			throws Exception {		
		orden = daoOrden.getOrdenRep(idOrden, servicio);
	}

	public String getTipoServicio() {
		return orden.getDorReferencia();
	}

	public List<BeanSuccessDetail> obtenerDetallesImpresion() throws Exception {
		if (orden.getDorReferencia().equals(CashConstants.SERV_AGUA)) {
			return obtenerDetallesImpresionAgua();
		} else {
			String tipo = getTipoServicio();
			if (tipo.equals(CashConstants.SERV_TELEFONIA)
					|| tipo.equals(CashConstants.SERV_LUZ)) {
				return obtenerDetallesImpresionOnLine();
			} else {
				return null;
			}
		}

	}

	public List<ReciboPagoServicio> getRecibos() {
		return recibos;
	}

	private void generarRecibos(List<TpDetalleOrden> detalles) {
		ReciboPagoServicio recibo = null;
		recibos = new ArrayList<ReciboPagoServicio>();
		for (TpDetalleOrden detalle : detalles) {
			recibo = new ReciboPagoServicio();
			recibo.setCliente(detalle.getDdonombreBenef());
			recibo.setFechaEmision(Fecha.convertFromFechaSQL(detalle.getDdoreferenciaAdicional()));
			recibo.setNroRecibo(detalle.getDdocomprobante());
			recibo.setImporte(new StringBuilder(VAL_SOLES_SIMB).append(detalle.getNdomonto()).toString());
			recibos.add(recibo);
		}
	}

	private List<BeanSuccessDetail> obtenerDetallesImpresionOnLine()
			throws Exception {
		List<BeanSuccessDetail> alsuccess = new ArrayList<BeanSuccessDetail>();
		BeanSuccessDetail sucessdetail;
		
		List<TpDetalleOrden> detalles  =  daoDetalleOrden.listaTpDetalleOrden(orden.getId().getCorIdOrden(), orden.getId().getCorIdServicioEmpresa());
		

		if( detalles != null && detalles.size()>0){
			generarRecibos(detalles);
			TpDetalleOrden detalle = detalles.get(0);
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Servicio");
			sucessdetail.setM_Mensaje(orden.getDorReferencia());
			alsuccess.add(sucessdetail);
	
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Tipo Servicio");
			sucessdetail.setM_Mensaje(detalle.getDdodescripcion());
			alsuccess.add(sucessdetail);
	
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Proveedor");
			sucessdetail.setM_Mensaje(detalle.getNdonumeroCuenta());
			alsuccess.add(sucessdetail);
	
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Orden");
			sucessdetail.setM_Mensaje(String.valueOf(orden.getId().getCorIdOrden()));
			alsuccess.add(sucessdetail);
	
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Suministro");
			sucessdetail.setM_Mensaje(detalle.getNdodocumento());
			alsuccess.add(sucessdetail);
	
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Monto");
			sucessdetail.setM_Mensaje(new StringBuilder(CashConstants.VAL_SOLES_SIMB).append(" ").append(orden.getNorMontoSoles()).toString());
			alsuccess.add(sucessdetail);
	
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Cuenta de Cargo");
			sucessdetail.setM_Mensaje(orden.getNorNumeroCuenta());
			alsuccess.add(sucessdetail);
	
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Referencia");
			sucessdetail.setM_Mensaje(detalle.getDdoreferencia());
			alsuccess.add(sucessdetail);
	
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Fecha / Hora");
			sucessdetail.setM_Mensaje(new StringBuilder(Fecha.convertFromFechaSQL(orden.getForFechaInicio())).append(" / ").append(Fecha.convertFromTimeSQL(orden.getHorHoraInicio())).toString());
			alsuccess.add(sucessdetail);
		}

		return alsuccess;
	}

	private List<BeanSuccessDetail> obtenerDetallesImpresionAgua()
			throws Exception {
		List<BeanSuccessDetail> alsuccess = new ArrayList<BeanSuccessDetail>();
		BeanSuccessDetail sucessdetail;

		TpDetalleOrden detalle = daoDetalleOrden.getTpDetalleOrden(orden.getId().getCorIdOrden(), orden.getId().getCorIdServicioEmpresa(), 1);

		if (detalle != null) {
			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Servicio");
			sucessdetail.setM_Mensaje(orden.getDorReferencia());
			alsuccess.add(sucessdetail);

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Proveedor");
			sucessdetail.setM_Mensaje(detalle.getNdonumeroCuenta());
			alsuccess.add(sucessdetail);

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Orden");
			sucessdetail.setM_Mensaje(String.valueOf(orden.getId().getCorIdOrden()));
			alsuccess.add(sucessdetail);

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Suministro");
			sucessdetail.setM_Mensaje(detalle.getNdodocumento());
			alsuccess.add(sucessdetail);

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Monto");
			sucessdetail.setM_Mensaje(new StringBuilder(CashConstants.VAL_SOLES_SIMB).append(" ").append(orden.getNorMontoSoles()).toString());
			alsuccess.add(sucessdetail);

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Cuenta de Cargo");
			sucessdetail.setM_Mensaje(orden.getNorNumeroCuenta());
			alsuccess.add(sucessdetail);

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Fecha Vencimiento");
			sucessdetail.setM_Mensaje(Fecha.convertFromFechaSQL(detalle.getFdofechaVenc()));
			alsuccess.add(sucessdetail);

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Referencia");
			sucessdetail.setM_Mensaje(detalle.getDdoreferencia());
			alsuccess.add(sucessdetail);

			sucessdetail = new BeanSuccessDetail();
			sucessdetail.setM_Label("Fecha / Hora");
			sucessdetail.setM_Mensaje(new StringBuilder(Fecha.convertFromFechaSQL(orden.getForFechaInicio())).append(" / ").append(Fecha.convertFromTimeSQL(orden.getHorHoraInicio())).toString());
			alsuccess.add(sucessdetail);			
		} 
		return alsuccess;
	}

}

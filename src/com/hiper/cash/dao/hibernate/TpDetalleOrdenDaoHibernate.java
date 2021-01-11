/*
 * To change this template, choose Tools |  Templates
 * and open the template in the editor.
 */
package com.hiper.cash.dao.hibernate;

import static com.hiper.cash.util.CashConstants.VAL_IBS_DOLARES;
import static com.hiper.cash.util.CashConstants.VAL_IBS_EUROS;
import static com.hiper.cash.util.CashConstants.VAL_IBS_SOLES;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.financiero.cash.dao.EntityDAO;
import com.hiper.cash.dao.TpDetalleOrdenDao;
import com.hiper.cash.dao.TxResultDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TaDetalleMapaCampos;
import com.hiper.cash.domain.TpDetalleOrden;
import com.hiper.cash.domain.TpDetalleOrdenId;
import com.hiper.cash.domain.TxListField;
import com.hiper.cash.domain.TxResult;
import com.hiper.cash.entidad.BeanDetalleImporteEstado;
import com.hiper.cash.entidad.BeanDetalleOrden;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.TipoConsulta;
import com.hiper.cash.util.Util;

/**
 * 
 * @author esilva 
 */
public class TpDetalleOrdenDaoHibernate implements TpDetalleOrdenDao {

	private Logger logger = Logger.getLogger(TpDetalleOrdenDaoHibernate.class);
	
	static Map<String,String> descripcionesCodigosIBS;
	static Map<String,String> descripcionesTipoMoneda;

	
	static{
		descripcionesCodigosIBS = new HashMap<String, String>();
		TxResultDao txResultDao = new TxResultDaoHibernate();
		List<TxResult> descripciones = txResultDao.seleccionarTabla();
		for(TxResult descripcion: descripciones){
			descripcionesCodigosIBS.put(descripcion.getId().getCrsResultExt(), descripcion.getDrsDescription());
		}
		
		descripcionesTipoMoneda = new HashMap<String, String>();
		TxListFieldDaoHibernate txListFieldDao = new TxListFieldDaoHibernate();
		List<TxListField> descripcionesMonedas = txListFieldDao.selectListFieldByFieldName("CashTipoMoneda");
		for(TxListField descripcion : descripcionesMonedas){
			descripcionesTipoMoneda.put(descripcion.getId().getClfCode(), descripcion.getDlfDescription());
		}

	}
		
	public List selectDetallePago(String servicio, String idorden,
			BeanPaginacion bpag) {
		Session session = null;
		List result;
		try {
			long lservicio = Long.parseLong(servicio);
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			result = session.getNamedQuery("getDetalleOrdenesCobroPago")
					.setParameter("servicioid", lservicio).setParameter(
							"idorden", Long.parseLong(idorden)).setParameter(
							"idDetOrd", bpag.getM_seleccion()).setMaxResults(
							bpag.getM_regPagina()).list();

			session.getTransaction().commit();

			Iterator iter = result.iterator();
			BeanDetalleOrden beandetorden;
			ArrayList alresult = new ArrayList();

			while (iter.hasNext()) {

				Object[] al_detorden = (Object[]) iter.next();
				beandetorden = new BeanDetalleOrden();

				TpDetalleOrden tp = (TpDetalleOrden) al_detorden[0];
				TxListField tl1 = (TxListField) al_detorden[1];
				TxListField tl2 = (TxListField) al_detorden[2];
				TxListField tl3 = (TxListField) al_detorden[3];
				TxListField tl4 = (TxListField) al_detorden[4];

				TxListField tl5 = (TxListField) al_detorden[5];
				TxListField tl6 = (TxListField) al_detorden[6];
				TxListField tl7 = (TxListField) al_detorden[7];
				TxResult tl8 = (TxResult) al_detorden[8];

				// KEY
				beandetorden.setM_IdOrden(String.valueOf(tp.getId()
						.getCdoidOrden()));
				beandetorden.setM_IdServicio(String.valueOf(tp.getId()
						.getCdoidServicioEmpresa()));
				beandetorden.setM_IdDetalleOrden(String.valueOf(tp.getId()
						.getCdoidDetalleOrden()));
				// DESC
				beandetorden.setM_Documento(tp.getNdodocumento());
				beandetorden.setM_Nombre(tp.getDdonombre());
				beandetorden.setM_NumeroCuenta(tp.getNdonumeroCuenta());
				beandetorden.setM_Monto(Util.formatearMontoNvo(tp.getNdomonto()
						.toString()));
				beandetorden.setM_Telefono((tp.getDdotelefono() == null || tp
						.getDdotelefono().length() == 0) ? " - " : tp
						.getDdotelefono());
				beandetorden.setM_Email((tp.getDdoemail() == null || tp
						.getDdoemail().length() == 0) ? " - " : tp
						.getDdoemail());
				beandetorden
						.setM_DescTipoPago((tp.getDdotipoPago() == null || tp
								.getDdotipoPago().length() == 0) ? " - " : tp
								.getDdotipoPago());
				beandetorden
						.setM_DescTipoDocumento((tp.getDdotipoDocumento() == null || tp
								.getDdotipoDocumento().length() == 0) ? " - "
								: tp.getDdotipoDocumento());

				beandetorden.setM_Referencia(tp.getDdoreferencia());// PS
				beandetorden.setM_OrdenRef(String
						.valueOf(tp.getCdoidOrdenRef()));// PS
				beandetorden.setM_DetalleOrdenRef(String.valueOf(tp
						.getCdoidDetalleOrdenRef()));// PS

				// jmoreno 12/05/2009 campos adicionales
				beandetorden.setM_ITF(Util.formatearMontoNvo(String.valueOf(tp
						.getNdoitf() == null ? "0" : tp.getNdoitf())));
				beandetorden.setM_Portes(Util.formatearMontoNvo(String
						.valueOf(tp.getNdoportes() == null ? "0" : tp
								.getNdoportes())));
				beandetorden.setM_MonedaITF(String
						.valueOf(tp.getCdomonedaItf() == null ? "-" : tp
								.getCdomonedaItf()));
				beandetorden.setM_MonedaPortes(String.valueOf(tp
						.getCdomonedaPortes() == null ? "-" : tp
						.getCdomonedaPortes()));
				// jmoreno 25-06-09
				beandetorden.setM_MonedaProtesto(String.valueOf(tp
						.getCdomonedaProtesto() == null ? "-" : tp
						.getCdomonedaProtesto()));
				beandetorden.setM_Protesto(Util.formatearMontoNvo(String
						.valueOf(tp.getNdoprotesto() == null ? "0" : tp
								.getNdoprotesto())));

				// jwong campos adicionales(para letras)
				beandetorden.setM_CodAceptante(tp.getNdocodAceptante());
				beandetorden.setM_MontoMora(Util.formatearMontoNvo(String
						.valueOf(tp.getNdomontoMora() == null ? "0" : tp
								.getNdomontoMora())));
				beandetorden
						.setM_MonedaMora(tp.getCdomonedaMontoMora() == null ? " - "
								: tp.getCdomonedaMontoMora());

				// jwong 14/05/2009 campo adicional
				beandetorden.setM_IdPago(tp.getNdoidPago() == null ? " - " : tp
						.getNdoidPago());

				// jwong 04/06/2009 campo adicional cheque
				beandetorden
						.setM_NumCheque(tp.getNdonumCheque() == null ? " - "
								: tp.getNdonumCheque());

				// jwong 08/06/2009 campo adicional principal(cancelacion de
				// letras)
				beandetorden.setM_Principal(Util.formatearMontoNvo(String
						.valueOf(tp.getNdomontoLetra() == null ? "0" : tp
								.getNdomontoLetra())));

				if (tp.getFdofechaProceso() == null
						|| tp.getFdofechaProceso().length() == 0) {
					beandetorden.setM_FechaProceso(" - ");
				} else {

					if (tp.getHdohoraProceso() == null
							|| tp.getHdohoraProceso().length() == 0)
						beandetorden.setM_FechaProceso(Fecha
								.convertFromFechaSQL(tp.getFdofechaProceso()));
					else
						beandetorden.setM_FechaProceso(Fecha
								.convertFromFechaSQL(tp.getFdofechaProceso())
								+ " - "
								+ Fecha.convertFromTimeSQL(tp
										.getHdohoraProceso()));

					// jwong 11/05/2009 para mostrar solo la fecha
					// beandetorden.setM_FechaProceso(Fecha.convertFromFechaSQL(tp.getFdofechaProceso()));
				}
				if (tl1 != null) {
					beandetorden.setM_DescTipoMoneda(tl1.getDlfDescription());
				}
				if (tl2 != null) {
					beandetorden.setM_DescTipoCuenta(tl2.getDlfDescription());
				}
				if (tl3 != null) {
					beandetorden.setM_DescEstado(tl3.getDlfDescription());
				}

				if (tl4 != null) {
					beandetorden.setM_MonedaMora(tl4.getDlfDescription());
				}

				// para obtener la descripcion de la moneda del itf y de los
				// portes

				if (tl5 != null) {
					beandetorden.setM_DescMonedaITF(tl5.getDlfDescription());
				}
				if (tl6 != null) {
					beandetorden.setM_DescMonedaPortes(tl6.getDlfDescription());
				}
				if (tl7 != null) {
					beandetorden.setM_MonedaProtesto(tl7.getDlfDescription());
				}
				// jmoreno 30-07-09 Para los codigos de error del Ibs
				beandetorden.setM_CodigoRptaIbs(tp.getCdocodigoRptaIbs());
				if (tl8 != null) {
					beandetorden
							.setM_descripcionCodIbs(tl8.getDrsDescription());
				}
				// jmoreno 04/12/09
				beandetorden.setM_montoComClienteChg(Util
						.formatearMontoNvo(String.valueOf(tp
								.getNdomontoCliComisionChg() == null ? "0" : tp
								.getNdomontoCliComisionChg())));
				beandetorden.setM_montoComEmpresaChg(Util
						.formatearMontoNvo(String.valueOf(tp
								.getNdomontoEmpComisionChg() == null ? "0" : tp
								.getNdomontoEmpComisionChg())));
				alresult.add(beandetorden);
				beandetorden = null;
			}
			result = null;
			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(),ex);
			return null;
		}finally{
			session.close();
		}

	}

	public List selectDetallePago(String servicio, String idorden) {
		Session session = null;
		List result;
		try {
			long lservicio = Long.parseLong(servicio);
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			result = session.getNamedQuery("getDetalleOrdenesCobroPago")
					.setParameter("servicioid", lservicio).setParameter(
							"idorden", Long.parseLong(idorden)).setParameter(
							"idDetOrd", 0).list();

			session.getTransaction().commit();

			Iterator iter = result.iterator();
			BeanDetalleOrden beandetorden;
			ArrayList alresult = new ArrayList();

			while (iter.hasNext()) {

				Object[] al_detorden = (Object[]) iter.next();
				beandetorden = new BeanDetalleOrden();

				TpDetalleOrden tp = (TpDetalleOrden) al_detorden[0];
				TxListField tl1 = (TxListField) al_detorden[1];
				TxListField tl2 = (TxListField) al_detorden[2];
				TxListField tl3 = (TxListField) al_detorden[3];
				TxListField tl4 = (TxListField) al_detorden[4];

				TxListField tl5 = (TxListField) al_detorden[5];
				TxListField tl6 = (TxListField) al_detorden[6];
				TxListField tl7 = (TxListField) al_detorden[7];
				TxResult tl8 = (TxResult) al_detorden[8];

				// KEY
				beandetorden.setM_IdOrden(String.valueOf(tp.getId()
						.getCdoidOrden()));
				beandetorden.setM_IdServicio(String.valueOf(tp.getId()
						.getCdoidServicioEmpresa()));
				beandetorden.setM_IdDetalleOrden(String.valueOf(tp.getId()
						.getCdoidDetalleOrden()));
				// DESC
				beandetorden.setM_Documento(tp.getNdodocumento());
				beandetorden.setM_Nombre(tp.getDdonombre());
				beandetorden.setM_NumeroCuenta(tp.getNdonumeroCuenta());
				beandetorden.setM_Monto(Util.formatearMontoNvo(tp.getNdomonto()
						.toString()));
				beandetorden.setM_Telefono((tp.getDdotelefono() == null || tp
						.getDdotelefono().length() == 0) ? " - " : tp
						.getDdotelefono());
				beandetorden.setM_Email((tp.getDdoemail() == null || tp
						.getDdoemail().length() == 0) ? " - " : tp
						.getDdoemail());
				beandetorden
						.setM_DescTipoPago((tp.getDdotipoPago() == null || tp
								.getDdotipoPago().length() == 0) ? " - " : tp
								.getDdotipoPago());
				beandetorden
						.setM_DescTipoDocumento((tp.getDdotipoDocumento() == null || tp
								.getDdotipoDocumento().length() == 0) ? " - "
								: tp.getDdotipoDocumento());

				beandetorden.setM_Referencia(tp.getDdoreferencia());// PS
				beandetorden.setM_OrdenRef(String
						.valueOf(tp.getCdoidOrdenRef()));// PS
				beandetorden.setM_DetalleOrdenRef(String.valueOf(tp
						.getCdoidDetalleOrdenRef()));// PS

				// jmoreno 12/05/2009 campos adicionales
				beandetorden.setM_ITF(Util.formatearMontoNvo(String.valueOf(tp
						.getNdoitf() == null ? "0" : tp.getNdoitf())));
				beandetorden.setM_Portes(Util.formatearMontoNvo(String
						.valueOf(tp.getNdoportes() == null ? "0" : tp
								.getNdoportes())));
				beandetorden.setM_MonedaITF(String
						.valueOf(tp.getCdomonedaItf() == null ? "-" : tp
								.getCdomonedaItf()));
				beandetorden.setM_MonedaPortes(String.valueOf(tp
						.getCdomonedaPortes() == null ? "-" : tp
						.getCdomonedaPortes()));
				// jmoreno 25-06-09
				beandetorden.setM_MonedaProtesto(String.valueOf(tp
						.getCdomonedaProtesto() == null ? "-" : tp
						.getCdomonedaProtesto()));
				beandetorden.setM_Protesto(Util.formatearMontoNvo(String
						.valueOf(tp.getNdoprotesto() == null ? "0" : tp
								.getNdoprotesto())));

				// jwong campos adicionales(para letras)
				beandetorden.setM_CodAceptante(tp.getNdocodAceptante());
				beandetorden.setM_MontoMora(Util.formatearMontoNvo(String
						.valueOf(tp.getNdomontoMora() == null ? "0" : tp
								.getNdomontoMora())));
				beandetorden
						.setM_MonedaMora(tp.getCdomonedaMontoMora() == null ? " - "
								: tp.getCdomonedaMontoMora());

				// jwong 14/05/2009 campo adicional
				beandetorden.setM_IdPago(tp.getNdoidPago() == null ? " - " : tp
						.getNdoidPago());

				// jwong 04/06/2009 campo adicional cheque
				beandetorden
						.setM_NumCheque(tp.getNdonumCheque() == null ? " - "
								: tp.getNdonumCheque());

				// jwong 08/06/2009 campo adicional principal(cancelacion de
				// letras)
				beandetorden.setM_Principal(Util.formatearMontoNvo(String
						.valueOf(tp.getNdomontoLetra() == null ? "0" : tp
								.getNdomontoLetra())));

				if (tp.getFdofechaProceso() == null
						|| tp.getFdofechaProceso().length() == 0) {
					beandetorden.setM_FechaProceso(" - ");
				} else {

					if (tp.getHdohoraProceso() == null
							|| tp.getHdohoraProceso().length() == 0)
						beandetorden.setM_FechaProceso(Fecha
								.convertFromFechaSQL(tp.getFdofechaProceso()));
					else
						beandetorden.setM_FechaProceso(Fecha
								.convertFromFechaSQL(tp.getFdofechaProceso())
								+ " - "
								+ Fecha.convertFromTimeSQL(tp
										.getHdohoraProceso()));

					// jwong 11/05/2009 para mostrar solo la fecha
					// beandetorden.setM_FechaProceso(Fecha.convertFromFechaSQL(tp.getFdofechaProceso()));
				}
				if (tl1 != null) {
					beandetorden.setM_DescTipoMoneda(tl1.getDlfDescription());
				}
				if (tl2 != null) {
					beandetorden.setM_DescTipoCuenta(tl2.getDlfDescription());
				}
				if (tl3 != null) {
					beandetorden.setM_DescEstado(tl3.getDlfDescription());
				}

				if (tl4 != null) {
					beandetorden.setM_MonedaMora(tl4.getDlfDescription());
				}

				// para obtener la descripcion de la moneda del itf y de los
				// portes

				if (tl5 != null) {
					beandetorden.setM_DescMonedaITF(tl5.getDlfDescription());
				}
				if (tl6 != null) {
					beandetorden.setM_DescMonedaPortes(tl6.getDlfDescription());
				}
				if (tl7 != null) {
					beandetorden.setM_MonedaProtesto(tl7.getDlfDescription());
				}
				// jmoreno 30-07-09 Para los codigos de error del Ibs
				beandetorden.setM_CodigoRptaIbs(tp.getCdocodigoRptaIbs());
				if (tl8 != null) {
					beandetorden
							.setM_descripcionCodIbs(tl8.getDrsDescription());
				}
				// jmoreno 04/12/09
				beandetorden.setM_montoComClienteChg(Util
						.formatearMontoNvo(String.valueOf(tp
								.getNdomontoCliComisionChg() == null ? "0" : tp
								.getNdomontoCliComisionChg())));
				beandetorden.setM_montoComEmpresaChg(Util
						.formatearMontoNvo(String.valueOf(tp
								.getNdomontoEmpComisionChg() == null ? "0" : tp
								.getNdomontoEmpComisionChg())));
				alresult.add(beandetorden);
				beandetorden = null;
			}
			result = null;
			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(),ex);
			return null;
		}finally{
			session.close();
		}

	}

	public List selectDetalleTransferencia(String servicio, String idorden,
			BeanPaginacion bpag) {
		Session session = null;
		List result;
		try {
			long lservicio = Long.parseLong(servicio);
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			result = session.getNamedQuery("getDetalleOrdenesTransferencia")
					.setParameter("servicioid", lservicio).setParameter(
							"idorden", Long.parseLong(idorden)).setParameter(
							"idDetOrd", bpag.getM_seleccion()).setMaxResults(
							bpag.getM_regPagina()).list();
			session.getTransaction().commit();
			Iterator iter = result.iterator();
			BeanDetalleOrden beandetorden;
			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				Object[] al_detorden = (Object[]) iter.next();
				beandetorden = new BeanDetalleOrden();

				TpDetalleOrden tp = (TpDetalleOrden) al_detorden[0];
				TxListField tl1 = (TxListField) al_detorden[1];
				TxListField tl3 = (TxListField) al_detorden[2];
				TxResult tl4 = (TxResult) al_detorden[3];

				// KEY
				beandetorden.setM_IdOrden(String.valueOf(tp.getId()
						.getCdoidOrden()));
				beandetorden.setM_IdServicio(String.valueOf(tp.getId()
						.getCdoidServicioEmpresa()));
				beandetorden.setM_IdDetalleOrden(String.valueOf(tp.getId()
						.getCdoidDetalleOrden()));
				// DESC
				beandetorden.setM_CtaCargo(tp.getNdonumCuentaCargo());// CP - CT
				beandetorden.setM_CtaAbono(tp.getNdonumCuentaAbono());// CP
				beandetorden.setM_CtaAbonoCci(tp.getNdonumCuentaAbonoCci());// CT
																			// -
																			// CI
				beandetorden.setM_TipoDocBenef(tp.getCdotipoDocBenef());// CI
				beandetorden.setM_NumDocBenef(tp.getNdonumDocBenef());// CI
				beandetorden
						.setM_NombreBenef(tp.getDdoapePatBenef() + " "
								+ tp.getDdoapeMatBenef() + " "
								+ tp.getDdonombreBenef());// CI
				beandetorden.setM_DirBenef(tp.getDdodireccionBenef());// CI
				beandetorden.setM_TelefBenef(tp.getDdotlfBenef());// CI
				beandetorden.setM_Referencia(tp.getDdoreferencia());// CP - CT -
																	// CI
				beandetorden.setM_Monto(Util.formatearMontoNvo(tp
						.getNdomontoAbonado().toString()));// CP - CT - CI

				if (tp.getFdofechaProceso() == null
						|| tp.getFdofechaProceso().length() == 0) {
					beandetorden.setM_FechaProceso(" - ");
				} else {
					if (tp.getHdohoraProceso() == null
							|| tp.getHdohoraProceso().length() == 0) {
						beandetorden.setM_FechaProceso(Fecha
								.convertFromFechaSQL(tp.getFdofechaProceso()));
					} else {
						beandetorden.setM_FechaProceso(Fecha
								.convertFromFechaSQL(tp.getFdofechaProceso())
								+ " - "
								+ Fecha.convertFromTimeSQL(tp
										.getHdohoraProceso()));
					}
				}
				if (tl1 != null) {
					beandetorden.setM_DescTipoMoneda(tl1.getDlfDescription());// CP
																				// -
																				// CT
																				// -
																				// CI
				}
				if (tl3 != null) {
					beandetorden.setM_DescEstado(tl3.getDlfDescription());// CP
																			// -
																			// CT
																			// -
																			// CI
				}
				beandetorden.setM_CodigoRptaIbs(tp.getCdocodigoRptaIbs());
				if (tl4 != null) {
					beandetorden
							.setM_descripcionCodIbs(tl4.getDrsDescription());
				}
				alresult.add(beandetorden);
				beandetorden = null;
			}
			result = null;
			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(),ex);
			return null;
		}finally{
			session.close();
		}

	}

	public List selectDetalleTransferencia(String servicio, String idorden) {
		Session session = null;
		List result;
		try {
			long lservicio = Long.parseLong(servicio);
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			result = session.getNamedQuery("getDetalleOrdenesTransferencia")
					.setParameter("servicioid", lservicio).setParameter(
							"idorden", Long.parseLong(idorden)).setParameter(
							"idDetOrd", 0).list();
			session.getTransaction().commit();
			Iterator iter = result.iterator();
			BeanDetalleOrden beandetorden;
			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				Object[] al_detorden = (Object[]) iter.next();
				beandetorden = new BeanDetalleOrden();

				TpDetalleOrden tp = (TpDetalleOrden) al_detorden[0];
				TxListField tl1 = (TxListField) al_detorden[1];
				TxListField tl3 = (TxListField) al_detorden[2];
				TxResult tl4 = (TxResult) al_detorden[3];

				// KEY
				beandetorden.setM_IdOrden(String.valueOf(tp.getId()
						.getCdoidOrden()));
				beandetorden.setM_IdServicio(String.valueOf(tp.getId()
						.getCdoidServicioEmpresa()));
				beandetorden.setM_IdDetalleOrden(String.valueOf(tp.getId()
						.getCdoidDetalleOrden()));
				// DESC
				beandetorden.setM_CtaCargo(tp.getNdonumCuentaCargo());// CP - CT
				beandetorden.setM_CtaAbono(tp.getNdonumCuentaAbono());// CP
				beandetorden.setM_CtaAbonoCci(tp.getNdonumCuentaAbonoCci());// CT
																			// -
																			// CI
				beandetorden.setM_TipoDocBenef(tp.getCdotipoDocBenef());// CI
				beandetorden.setM_NumDocBenef(tp.getNdonumDocBenef());// CI
				beandetorden
						.setM_NombreBenef(tp.getDdoapePatBenef() + " "
								+ tp.getDdoapeMatBenef() + " "
								+ tp.getDdonombreBenef());// CI
				beandetorden.setM_DirBenef(tp.getDdodireccionBenef());// CI
				beandetorden.setM_TelefBenef(tp.getDdotlfBenef());// CI
				beandetorden.setM_Referencia(tp.getDdoreferencia());// CP - CT -
																	// CI
				beandetorden.setM_Monto(Util.formatearMontoNvo(tp
						.getNdomontoAbonado().toString()));// CP - CT - CI

				if (tp.getFdofechaProceso() == null
						|| tp.getFdofechaProceso().length() == 0) {
					beandetorden.setM_FechaProceso(" - ");
				} else {
					if (tp.getHdohoraProceso() == null
							|| tp.getHdohoraProceso().length() == 0) {
						beandetorden.setM_FechaProceso(Fecha
								.convertFromFechaSQL(tp.getFdofechaProceso()));
					} else {
						beandetorden.setM_FechaProceso(Fecha
								.convertFromFechaSQL(tp.getFdofechaProceso())
								+ " - "
								+ Fecha.convertFromTimeSQL(tp
										.getHdohoraProceso()));
					}
				}
				if (tl1 != null) {
					beandetorden.setM_DescTipoMoneda(tl1.getDlfDescription());// CP
																				// -
																				// CT
																				// -
																				// CI
				}
				if (tl3 != null) {
					beandetorden.setM_DescEstado(tl3.getDlfDescription());// CP
																			// -
																			// CT
																			// -
																			// CI
				}
				beandetorden.setM_CodigoRptaIbs(tp.getCdocodigoRptaIbs());
				if (tl4 != null) {
					beandetorden
							.setM_descripcionCodIbs(tl4.getDrsDescription());
				}
				alresult.add(beandetorden);
				beandetorden = null;
			}
			result = null;
			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(),ex);
			return null;
		}finally{
			session.close();
		}

	}

	public List select(String servicio, String idorden, List estado,
			BeanPaginacion bpag) {
		Session session = null;
		String strQuery;
		strQuery = " select tp.cdoidOrden, tp.cdoidServicioEmpresa, tp.cdoidDetalleOrden, "
				+ "  tp.ndomonto, tp.cdomoneda, "
				+ "  tp.ddonombre, tp.ndonumeroCuenta, "
				+ "  tp.ddotipoCuenta, tp.ddotelefono, tp.ddoemail, "
				+ "  tp.ddodescripcion, tp.ndodocumento, tp.cdoestado, "
				+ "  tl.dlfDescription as descMon, "
				+ "  tl2.dlfDescription as descTipoCta, "
				+ "  tl3.dlfDescription as descEstado, "
				+ "  tp.fdofechaProceso "
				+
				// jwong 03/06/2009 para obtener el id del servicio
				// "  ,tse.cSEmIdServicio as idTipServ " +

				// jwong 04/06/2009 modificado para que se muestre en ventanilla
				"  ,tp.ddoreferencia "
				+ " from TpDetalleOrden tp  with(nolock)"
				+ " left join TxListField tl on(tp.cdomoneda = tl.clfCode and tl.dlfFieldName = :fieldid) "
				+ " left join TxListField tl2 on(tp.ddotipoCuenta = tl2.clfCode and tl2.dlfFieldName = :fieldid2) "
				+ " left join TxListField tl3 on(tp.cdoestado = tl3.clfCode and tl3.dlfFieldName = :fieldid3) "
				+
				// jwong 03/06/2009 para obtener el id del servicio
				// " left join TaServicioxEmpresa tse on(tp.cdoidServicioEmpresa = tse.csemidServicioEmpresa) "
				// +
				// " , taServicioxEmpresa tse " +

				" where "
				+ " tp.cdoidServicioEmpresa = :servicioid and "
				+ " tp.cdoidOrden = :idorden and "
				+ " tp.cdoestado in (:estadoList) "
				+ " and tp.cdoidDetalleOrden > :idDetOrd "
				+ " order by tp.cdoidDetalleOrden asc ";
		List result;

		try {
			long lservicio = Long.parseLong(servicio);
			session = HibernateUtil.getSessionFactory().openSession();
			result = session.createSQLQuery(strQuery).setParameter(
					"servicioid", lservicio).setParameterList("estadoList",
					estado).setParameter("idorden", Long.parseLong(idorden))
					.setParameter("fieldid", "CashTipoMoneda").setParameter(
							"fieldid2", "CashTipoCuenta").setParameter(
							"fieldid3", "CashEstadoDetalleOrden").setParameter(
							"idDetOrd", bpag.getM_seleccion()).setMaxResults(
							bpag.getM_regPagina()).list();			

			Iterator iter = result.iterator();
			BeanDetalleOrden beandetorden;
			ArrayList alresult = new ArrayList();
			String aux = null;
			while (iter.hasNext()) {
				Object[] al_detorden = (Object[]) iter.next();
				beandetorden = new BeanDetalleOrden();
				// LLAVE
				beandetorden.setM_IdOrden(al_detorden[0].toString());
				// beandetorden.setM_IdEmpresa(al_detorden[1].toString());
				beandetorden.setM_IdServicio(al_detorden[1].toString());
				beandetorden.setM_IdDetalleOrden(al_detorden[2].toString());
				// DESC
				beandetorden.setM_Monto(al_detorden[3].toString());
				beandetorden.setM_IdTipoMoneda((String) al_detorden[4]);
				beandetorden.setM_Nombre((String) al_detorden[5]);
				beandetorden.setM_NumeroCuenta((String) al_detorden[6]);
				beandetorden.setM_longitudCuenta(beandetorden
						.getM_NumeroCuenta().length());// jmoreno 15-08-09
				beandetorden.setM_IdTipoCuenta((String) al_detorden[7]);
				beandetorden.setM_Telefono((String) al_detorden[8]);
				beandetorden.setM_Email((String) al_detorden[9]);
				beandetorden.setM_Descripcion((String) al_detorden[10]);
				beandetorden.setM_Documento((String) al_detorden[11]);
				beandetorden.setM_IDEstado(al_detorden[12].toString());
				beandetorden.setM_DescTipoMoneda((String) al_detorden[13]);
				beandetorden.setM_DescTipoCuenta((String) al_detorden[14]);
				beandetorden.setM_DescEstado((String) al_detorden[15]);
				// jwong 15/01/2009 columna agregada
				aux = (String) al_detorden[16];
				beandetorden.setM_FechaProceso((aux != null && aux.trim()
						.length() == 8) ? Fecha.convertFromFechaSQL(aux) : "");

				// jwong 03/06/2009 para obtener el id del servicio
				// beandetorden.setM_TipoServicio((String)
				// al_detorden[17].toString());

				// jwong 04/06/2009 modificado para que se muestre en ventanilla
				beandetorden.setM_Referencia((String) al_detorden[17]);
				beandetorden.setM_Nombre(beandetorden.getM_Referencia());

				alresult.add(beandetorden);
				beandetorden = null;
			}
			result = null;
			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(),ex);
			return new ArrayList();
		}finally{
			session.close();
		}

	}

	public List selectOrdCan(String servicio, String idorden, List estado,
			BeanPaginacion bpag) throws Exception {
		Session session = null;
		String strQuery;
		long indice = 0;
		int regPag = 0;
		if (Constantes.TIPO_PAG_SIGUIENTE.equalsIgnoreCase(bpag.getM_tipo())
				|| Constantes.TIPO_PAG_PRIMERO.equalsIgnoreCase(bpag
						.getM_tipo())) {
			strQuery = " select tp.cdoidOrden, tp.cdoidServicioEmpresa, tp.cdoidDetalleOrden, "
					+ "  tp.ndomonto, tp.cdomoneda, "
					+ "  tp.ddonombre, tp.ndonumeroCuenta, "
					+ "  tp.ddotipoCuenta, tp.ddotelefono, tp.ddoemail, "
					+ "  tp.ddodescripcion, tp.ndodocumento, tp.cdoestado, "
					+ "  tl.dlfDescription as descMon, "
					+ "  tl2.dlfDescription as descTipoCta, "
					+ "  tl3.dlfDescription as descEstado, "
					+ "  tp.fdofechaProceso, "
					+ "  tp.ddoreferencia "
					+ " from TpDetalleOrden tp  with(nolock)"
					+ " left join TxListField tl on(tp.cdomoneda = tl.clfCode and tl.dlfFieldName = :fieldid) "
					+ " left join TxListField tl2 on(tp.ddotipoCuenta = tl2.clfCode and tl2.dlfFieldName = :fieldid2) "
					+ " left join TxListField tl3 on(tp.cdoestado = tl3.clfCode and tl3.dlfFieldName = :fieldid3) "
					+ " where "
					+ " tp.cdoidServicioEmpresa = :servicioid and "
					+ " tp.cdoidOrden = :idorden and "
					+ " tp.cdoestado in (:estadoList) "
					+ " and tp.cdoidDetalleOrden > :idDetOrd "
					+ " order by tp.cdoidDetalleOrden asc ";

		} else if (Constantes.TIPO_PAG_ANTERIOR.equalsIgnoreCase(bpag
				.getM_tipo())) {
			strQuery = " select tp.cdoidOrden, tp.cdoidServicioEmpresa, tp.cdoidDetalleOrden, "
					+ "  tp.ndomonto, tp.cdomoneda, "
					+ "  tp.ddonombre, tp.ndonumeroCuenta, "
					+ "  tp.ddotipoCuenta, tp.ddotelefono, tp.ddoemail, "
					+ "  tp.ddodescripcion, tp.ndodocumento, tp.cdoestado, "
					+ "  tl.dlfDescription as descMon, "
					+ "  tl2.dlfDescription as descTipoCta, "
					+ "  tl3.dlfDescription as descEstado, "
					+ "  tp.fdofechaProceso "
					+ "  ,tp.ddoreferencia "
					+ " from TpDetalleOrden tp  with(nolock)"
					+ " left join TxListField tl on(tp.cdomoneda = tl.clfCode and tl.dlfFieldName = :fieldid) "
					+ " left join TxListField tl2 on(tp.ddotipoCuenta = tl2.clfCode and tl2.dlfFieldName = :fieldid2) "
					+ " left join TxListField tl3 on(tp.cdoestado = tl3.clfCode and tl3.dlfFieldName = :fieldid3) "
					+ " where "
					+ " tp.cdoidServicioEmpresa = :servicioid and "
					+ " tp.cdoidOrden = :idorden and "
					+ " tp.cdoestado in (:estadoList) "
					+ " and tp.cdoidDetalleOrden < :idDetOrd "
					+ " order by tp.cdoidDetalleOrden desc ";

		} else {
			strQuery = " select tp.cdoidOrden, tp.cdoidServicioEmpresa, tp.cdoidDetalleOrden, "
					+ "  tp.ndomonto, tp.cdomoneda, "
					+ "  tp.ddonombre, tp.ndonumeroCuenta, "
					+ "  tp.ddotipoCuenta, tp.ddotelefono, tp.ddoemail, "
					+ "  tp.ddodescripcion, tp.ndodocumento, tp.cdoestado, "
					+ "  tl.dlfDescription as descMon, "
					+ "  tl2.dlfDescription as descTipoCta, "
					+ "  tl3.dlfDescription as descEstado, "
					+ "  tp.fdofechaProceso "
					+ "  ,tp.ddoreferencia "
					+ " from TpDetalleOrden tp  with(nolock)"
					+ " left join TxListField tl on(tp.cdomoneda = tl.clfCode and tl.dlfFieldName = :fieldid) "
					+ " left join TxListField tl2 on(tp.ddotipoCuenta = tl2.clfCode and tl2.dlfFieldName = :fieldid2) "
					+ " left join TxListField tl3 on(tp.cdoestado = tl3.clfCode and tl3.dlfFieldName = :fieldid3) "
					+ " where "
					+ " tp.cdoidServicioEmpresa = :servicioid and "
					+ " tp.cdoidOrden = :idorden and "
					+ " tp.cdoestado in (:estadoList) " +
					// " and tp.cdoidDetalleOrden < :idDetOrd "+
					" order by tp.cdoidDetalleOrden desc ";
		}
		if (Constantes.TIPO_PAG_SIGUIENTE.equalsIgnoreCase(bpag.getM_tipo())) {
			indice = bpag.getM_ultimoRegAct();
			regPag = bpag.getM_regPagina();
		} else if (Constantes.TIPO_PAG_ANTERIOR.equalsIgnoreCase(bpag
				.getM_tipo())) {
			indice = bpag.getM_primerRegAct();
			regPag = bpag.getM_regPagina();
		} else if (Constantes.TIPO_PAG_PRIMERO.equalsIgnoreCase(bpag
				.getM_tipo())) {
			indice = 0;
			regPag = bpag.getM_regPagina();
		} else if (Constantes.TIPO_PAG_ULTIMO
				.equalsIgnoreCase(bpag.getM_tipo())) {
			if (bpag.getM_resto() != 0) {
				regPag = bpag.getM_resto();
			} else {
				regPag = bpag.getM_regPagina();
			}
		}

		List result;

		try {
			long lservicio = Long.parseLong(servicio);
			session = HibernateUtil.getSessionFactory().openSession();			
			if (Constantes.TIPO_PAG_ULTIMO.equalsIgnoreCase(bpag.getM_tipo())) {
				result = session.createSQLQuery(strQuery).setParameter(
						"servicioid", lservicio).setParameterList("estadoList",
						estado)
						.setParameter("idorden", Long.parseLong(idorden))
						.setParameter("fieldid", "CashTipoMoneda")
						.setParameter("fieldid2", "CashTipoCuenta")
						.setParameter("fieldid3", "CashEstadoDetalleOrden")
						.setMaxResults(regPag).list();
				
			} else {
				result = session.createSQLQuery(strQuery).setParameter(
						"servicioid", lservicio).setParameterList("estadoList",
						estado)
						.setParameter("idorden", Long.parseLong(idorden))
						.setParameter("fieldid", "CashTipoMoneda")
						.setParameter("fieldid2", "CashTipoCuenta")
						.setParameter("fieldid3", "CashEstadoDetalleOrden")
						.setParameter("idDetOrd", indice).setMaxResults(regPag)
						.list();

			}

			Iterator iter = result.iterator();
			BeanDetalleOrden beandetorden;
			ArrayList alresult = new ArrayList();
			String aux = null;
			while (iter.hasNext()) {
				Object[] al_detorden = (Object[]) iter.next();
				beandetorden = new BeanDetalleOrden();
				// LLAVE
				beandetorden.setM_IdOrden(al_detorden[0].toString());
				// beandetorden.setM_IdEmpresa(al_detorden[1].toString());
				beandetorden.setM_IdServicio(al_detorden[1].toString());
				beandetorden.setM_IdDetalleOrden(al_detorden[2].toString());
				// DESC
				beandetorden.setM_Monto(al_detorden[3].toString());
				beandetorden.setM_IdTipoMoneda((String) al_detorden[4]);
				beandetorden.setM_Nombre((String) al_detorden[5]);
				beandetorden.setM_NumeroCuenta((String) al_detorden[6]);
				beandetorden.setM_IdTipoCuenta((String) al_detorden[7]);
				beandetorden.setM_Telefono((String) al_detorden[8]);
				beandetorden.setM_Email((String) al_detorden[9]);
				beandetorden.setM_Descripcion((String) al_detorden[10]);
				beandetorden.setM_Documento((String) al_detorden[11]);
				beandetorden.setM_IDEstado(al_detorden[12].toString());
				beandetorden.setM_DescTipoMoneda((String) al_detorden[13]);
				beandetorden.setM_DescTipoCuenta((String) al_detorden[14]);
				beandetorden.setM_DescEstado((String) al_detorden[15]);
				// jwong 15/01/2009 columna agregada
				aux = (String) al_detorden[16];
				beandetorden.setM_FechaProceso((aux != null && aux.trim()
						.length() == 8) ? Fecha.convertFromFechaSQL(aux) : "");

				// jwong 03/06/2009 para obtener el id del servicio
				// beandetorden.setM_TipoServicio((String)
				// al_detorden[17].toString());

				// jwong 04/06/2009 modificado para que se muestre en ventanilla
				beandetorden.setM_Referencia((String) al_detorden[17]);
				beandetorden.setM_Nombre(beandetorden.getM_Referencia());

				alresult.add(beandetorden);
				beandetorden = null;
			}
			result = null;
			return alresult;
		}catch (Exception ex) {
			throw ex;			
		}finally{
			session.close();
		}

	}

	public boolean update(String empresa, String servicio, String idorden,
			Map mapmontos, Map mapestados, Map map_cuentas, Map map_nombres,
			Map map_telefs, Map map_emails, Map map_tipcuentas,
			Map map_nrodocumentos, BeanPaginacion bpag) {
		Session session = null;
		Session session2=null;
		try {
			long lservicio = Long.parseLong(servicio);
			session = HibernateUtil.getSessionFactory().openSession();			
			List result = session // .createQuery("from TpDetalleOrden tp where tp.id.cemIdEmpresa = :empresaid and tp.id.csemIdServicioEmpresa = :servicioid and tp.id.corIdOrden = :idorden")
					.createQuery(
							"from TpDetalleOrden tp where tp.id.cdoidOrden = :idorden and tp.id.cdoidServicioEmpresa = :servicioid and tp.id.cdoidDetalleOrden > :idDetOrd") // .setParameter("empresaid",
																																												// empresa)
					.setParameter("servicioid", lservicio).setParameter(
							"idorden", Long.parseLong(idorden)).setParameter(
							"idDetOrd", bpag.getM_seleccion()).setMaxResults(
							bpag.getM_regPagina()).list();			
			
			for (Iterator i = result.iterator(); i.hasNext();) {
				TpDetalleOrden tpdetalle = (TpDetalleOrden) i.next();
				String estado = (String) mapestados.get((tpdetalle.getId()
						.getCdoidDetalleOrden())
						+ "");
				long en = tpdetalle.getId().getCdoidDetalleOrden();
				if (estado != null && estado.equalsIgnoreCase("on")) {
					if (mapmontos.get((en) + "") != null) {
						tpdetalle.setNdomonto(new BigDecimal((String) mapmontos
								.get((en) + "")).setScale(2));
					}
					if (map_nombres.get((en) + "") != null) {
						tpdetalle.setDdonombre((String) map_nombres.get((en)
								+ ""));
						// jwong 04/06/2009
						tpdetalle.setDdoreferencia((String) map_nombres
								.get((en) + ""));
					}
					if (map_cuentas.get((en) + "") != null) {// jmoreno 15-08-09
						String numCtas = (String) map_cuentas.get((en) + "");
						if (numCtas.length() > 0) {
							while (numCtas.length() < 12) {
								numCtas = "0" + numCtas;
							}
						}
						tpdetalle.setNdonumeroCuenta(numCtas);
						// tpdetalle.setNdonumeroCuenta((String)
						// map_cuentas.get((en) + ""));
					}
					if (map_telefs.get((en) + "") != null) {
						tpdetalle.setDdotelefono((String) map_telefs.get((en)
								+ ""));
					}
					if (map_emails.get((en) + "") != null) {
						tpdetalle.setDdoemail((String) map_emails
								.get((en) + ""));
					}
					if (map_tipcuentas.get((en) + "") != null)// esilva
																// 13/05/2009
					{
						tpdetalle.setDdotipoCuenta((String) map_tipcuentas
								.get((en) + ""));
					}

					if (map_nrodocumentos.get((en) + "") != null)//
					{
						tpdetalle.setNdodocumento((String) map_nrodocumentos
								.get((en) + ""));
					}

					session2 = HibernateUtil.getSessionFactory()
							.openSession();
					session2.beginTransaction();
					session2.saveOrUpdate(tpdetalle);
					session2.getTransaction().commit();
				} else {
					// tpdetalle.setCdoestado(Constantes.HQL_CASH_ESTADO_DETALLE_ORDEN_ELIMINADO);
				}
				// tpdetalle = null;
			}
			
		} catch (Exception e) {
			logger.error(e.toString(),e);
			return false;
		}finally{
			session.close();
			session2.close();
		}
		return true;
	}

	public boolean insert(List listadetorden, Map montos) {
		Session session = null;
		BigDecimal bsoles = new BigDecimal("0.0").setScale(2);
		BigDecimal bdolares = new BigDecimal("0.0").setScale(2);
		BigDecimal beuros = new BigDecimal("0.0").setScale(2);// jmoreno
																// 03-09-09
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			for (Iterator i = listadetorden.iterator(); i.hasNext();) {
				TpDetalleOrden tpdetalle = (TpDetalleOrden) i.next();
				if (tpdetalle.getCdomoneda() != null) {
					if (tpdetalle.getCdomoneda().trim().equalsIgnoreCase("PEN")) {
						// bsoles.add((tpdetalle.getDdomonto() !=
						// null)?tpdetalle.getDdomonto():new BigDecimal(0));
						bsoles = bsoles.add(tpdetalle.getNdomonto());
					} else if (tpdetalle.getCdomoneda().trim()
							.equalsIgnoreCase("USD")) {
						bdolares = bdolares
								.add((tpdetalle.getNdomonto() != null) ? tpdetalle
										.getNdomonto()
										: new BigDecimal(0));
					} else if (tpdetalle.getCdomonedaMontoAbonado().trim()
							.equalsIgnoreCase("EUR")) {// jmoreno 03-09-09
						beuros = beuros
								.add((tpdetalle.getNdomonto() != null) ? tpdetalle
										.getNdomonto()
										: new BigDecimal("0.0").setScale(2));
					}
				}
				session.saveOrUpdate(tpdetalle);
				// tpdetalle = null;
			}
			session.getTransaction().commit();
			montos.put("PEN", bsoles);
			montos.put("USD", bdolares);
			montos.put("EUR", beuros);
		} catch (Exception e) {
			logger.error(e.toString(),e);
			return false;
		}finally{
			session.close();
		}
		return true;
	}

	// Insert Detalle Orden Tipo Transferencias
	public boolean insertTransferencia(List listadetorden, Map montos) {
		Session session = null;
		BigDecimal bsoles = new BigDecimal("0.0").setScale(2);
		BigDecimal bdolares = new BigDecimal("0.0").setScale(2);
		BigDecimal beuros = new BigDecimal("0.0").setScale(2);// jmoreno
																// 03-09-09
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			for (Iterator i = listadetorden.iterator(); i.hasNext();) {
				TpDetalleOrden tpdetalle = (TpDetalleOrden) i.next();			
				if (tpdetalle.getCdomonedaMontoAbonado() != null) {
					if (tpdetalle.getCdomonedaMontoAbonado().trim()
							.equalsIgnoreCase("PEN")) {
						bsoles = bsoles
								.add((tpdetalle.getNdomontoAbonado() != null) ? tpdetalle
										.getNdomontoAbonado()
										: new BigDecimal("0.0").setScale(2));
					} else if (tpdetalle.getCdomonedaMontoAbonado().trim()
							.equalsIgnoreCase("USD")) {
						bdolares = bdolares
								.add((tpdetalle.getNdomontoAbonado() != null) ? tpdetalle
										.getNdomontoAbonado()
										: new BigDecimal("0.0").setScale(2));
					} else if (tpdetalle.getCdomonedaMontoAbonado().trim()
							.equalsIgnoreCase("EUR")) {// jmoreno 03-09-09
						beuros = beuros
								.add((tpdetalle.getNdomontoAbonado() != null) ? tpdetalle
										.getNdomontoAbonado()
										: new BigDecimal("0.0").setScale(2));
					}
				}
				session.saveOrUpdate(tpdetalle);				
			}
			session.getTransaction().commit();
			montos.put("PEN", bsoles);
			montos.put("USD", bdolares);
			montos.put("EUR", beuros);
		} catch (Exception e) {
			logger.error(e.toString(),e);
			return false;
		}
		return true;
	}

	// jwong 21/01/2009 detalle de los importes de las ordenes por estado
	public BeanDetalleImporteEstado selectDetImporteXEstado(String servicio,
			String idorden) {
		Session session = null;
		Session session2 = null;
		String strQuery;
		strQuery = " select tp.cdoestado, tp.ndomonto, tp.cdomoneda "
				+ " from TpDetalleOrden tp "
				+ " where tp.id.cdoidServicioEmpresa = :servicioid "
				+ " and tp.id.cdoidOrden = :idorden ";

		BeanDetalleImporteEstado beanDet;
		List result;
		List result2;
		try {
			long lservicio = Long.parseLong(servicio);
			session = HibernateUtil.getSessionFactory().openSession();			
			result = session.createQuery(strQuery) // .setParameter("empresaid",
													// empresa)
					.setParameter("servicioid", lservicio).setParameter(
							"idorden", Long.parseLong(idorden)).list();
			session2 = HibernateUtil.getSessionFactory()
					.openSession();
			strQuery = "select nOrMontoSoles,nOrMontoDolares from taOrden with(nolock) where cOrIdOrden = :idOrden";			
			result2 = session2.createSQLQuery(strQuery).setParameter("idOrden",
					idorden).list();
			
		} catch (Exception ex) {
			logger.error(ex.toString(),ex);
			return null;
		}
		finally{
			session.close();
			session2.close();
		}
		Iterator iter = result.iterator();
		String estado = null;
		String monto = null;
		String moneda = null;
		double importe = 0.0;

		// creamos el bean con el resumen de los importes por estado de las
		// ordenes
		beanDet = new BeanDetalleImporteEstado();
		Iterator iter2 = result2.iterator();
		while (iter2.hasNext()) {
			Object[] al_detorden = (Object[]) iter2.next();
			if (al_detorden[0] != null) {
				beanDet.setM_valorSolesTotal(Util
						.formatearMontoNvo(al_detorden[0].toString()));
			} else {
				beanDet.setM_valorSolesTotal("0.00");
			}
			if (al_detorden[1] != null) {
				beanDet.setM_valorDolaresTotal(Util
						.formatearMontoNvo(al_detorden[1].toString()));
			} else {
				beanDet.setM_valorDolaresTotal("0.00");
			}

		}

		while (iter.hasNext()) {
			Object[] al_detorden = (Object[]) iter.next();
			estado = al_detorden[0].toString();
			monto = al_detorden[1] != null ? al_detorden[1].toString() : "0";
			moneda = al_detorden[2] != null ? al_detorden[2].toString() : "";
			try {
				importe = Double.parseDouble(monto);
			} catch (NumberFormatException nfe) {
				importe = 0.0;
				nfe.printStackTrace();
			}
			if (estado != null && !"".equals(estado)) {
				if (Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN_INGRESADO
						.equals(estado)) {
					// verificamos el tipo de moneda
					if (Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(moneda)) {
						beanDet
								.setM_NumEnviados(beanDet.getM_NumEnviados() + 1);
						beanDet.setM_ValorSolesEnviado(beanDet
								.getM_ValorSolesEnviado()
								+ importe);
					} else if (Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES
							.equals(moneda)) {
						beanDet.setM_NumEnviadosDol(beanDet
								.getM_NumEnviadosDol() + 1);
						beanDet.setM_ValorDolaresEnviado(beanDet
								.getM_ValorDolaresEnviado()
								+ importe);
					}
				} else if (Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN_PROCESADO
						.equals(estado)) {
					// verificamos el tipo de moneda
					if (Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(moneda)) {
						beanDet
								.setM_NumProcesados(beanDet
										.getM_NumProcesados() + 1);
						beanDet.setM_ValorSolesProcesado(beanDet
								.getM_ValorSolesProcesado()
								+ importe);
					} else if (Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES
							.equals(moneda)) {
						beanDet.setM_NumProcesadosDol(beanDet
								.getM_NumProcesadosDol() + 1);
						beanDet.setM_ValorDolaresProcesado(beanDet
								.getM_ValorDolaresProcesado()
								+ importe);
					}
				} else if (Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN_ERRADO
						.equals(estado)) {
					// verificamos el tipo de moneda
					if (Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(moneda)) {
						beanDet.setM_NumErrados(beanDet.getM_NumErrados() + 1);
						beanDet.setM_ValorSolesErrado(beanDet
								.getM_ValorSolesErrado()
								+ importe);
					} else if (Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES
							.equals(moneda)) {
						beanDet
								.setM_NumErradosDol(beanDet
										.getM_NumErradosDol() + 1);
						beanDet.setM_ValorDolaresErrado(beanDet
								.getM_ValorDolaresErrado()
								+ importe);
					}
				} else if (Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN_COBRADO
						.equals(estado)) {// jmoreno 21-08-09
					// verificamos el tipo de moneda
					if (Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(moneda)) {
						beanDet
								.setM_NumCobrados(beanDet.getM_NumCobrados() + 1);
						beanDet.setM_ValorSolesCobrados(beanDet
								.getM_ValorSolesCobrados()
								+ importe);
					} else if (Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES
							.equals(moneda)) {
						beanDet.setM_NumCobradosDol(beanDet
								.getM_NumCobradosDol() + 1);
						beanDet.setM_ValorDolaresCobrados(beanDet
								.getM_ValorDolaresCobrados()
								+ importe);
					}
				} else if (Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN_ELIMINADO
						.equals(estado)) {
					// verificamos el tipo de moneda
					if (Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(moneda)) {
						beanDet
								.setM_NumCancelados(beanDet
										.getM_NumCancelados() + 1);
						beanDet.setM_ValorSolesCancelados(beanDet
								.getM_ValorSolesCancelados()
								+ importe);
					} else if (Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES
							.equals(moneda)) {
						beanDet.setM_NumCanceladosDol(beanDet
								.getM_NumCanceladosDol() + 1);
						beanDet.setM_ValorDolaresCancelados(beanDet
								.getM_ValorDolaresCancelados()
								+ importe);
					}
				} else if (Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN_COBRADOSPARCIAL
						.equals(estado)) {
					// verificamos el tipo de moneda
					if (Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(moneda)) {
						beanDet.setM_NumCobradosParc(beanDet
								.getM_NumCobradosParc() + 1);
						beanDet.setM_ValorSolesCobradosParc(beanDet
								.getM_ValorSolesCobradosParc()
								+ importe);
					} else if (Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES
							.equals(moneda)) {
						beanDet.setM_NumCobradosParcDol(beanDet
								.getM_NumCobradosParcDol() + 1);
						beanDet.setM_ValorDolaresCobradosParc(beanDet
								.getM_ValorDolaresCobradosParc()
								+ importe);
					}
				} else if (Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN_PENDIENTECONF
						.equals(estado)) {
					// verificamos el tipo de moneda
					if (Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(moneda)) {
						beanDet
								.setM_NumPendConf(beanDet.getM_NumPendConf() + 1);
						beanDet.setM_ValorSolesPendConf(beanDet
								.getM_ValorSolesPendConf()
								+ importe);
					} else if (Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES
							.equals(moneda)) {
						beanDet.setM_NumPendConfDol(beanDet
								.getM_NumPendConfDol() + 1);
						beanDet.setM_ValorDolaresPendConf(beanDet
								.getM_ValorDolaresPendConf()
								+ importe);
					}
				} else if (Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN_EXTORNADO
						.equals(estado)) {
					// verificamos el tipo de moneda
					if (Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(moneda)) {
						beanDet
								.setM_NumExtornados(beanDet
										.getM_NumExtornados() + 1);
						beanDet.setM_ValorSolesExtornados(beanDet
								.getM_ValorSolesExtornados()
								+ importe);
					} else if (Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES
							.equals(moneda)) {
						beanDet.setM_NumExtornadosDol(beanDet
								.getM_NumExtornadosDol() + 1);
						beanDet.setM_ValorDolaresExtornados(beanDet
								.getM_ValorDolaresExtornados()
								+ importe);
					}
				} else if (Constantes.FIELD_CASH_ESTADO_DETALLE_ORDEN_ARCHIVADOS
						.equals(estado)) {
					// verificamos el tipo de moneda
					if (Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(moneda)) {
						beanDet
								.setM_NumArchivados(beanDet
										.getM_NumArchivados() + 1);
						beanDet.setM_ValorSolesArchivados(beanDet
								.getM_ValorSolesArchivados()
								+ importe);
					} else if (Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES
							.equals(moneda)) {
						beanDet.setM_NumArchivadosDol(beanDet
								.getM_NumArchivadosDol() + 1);
						beanDet.setM_ValorDolaresArchivados(beanDet
								.getM_ValorDolaresArchivados()
								+ importe);
					}
				}
			}
		}
		// antes de retornar debemos manejar el formato de dos decimales para
		// los montos
		beanDet.setM_StrValorSolesEnviado(Util.formatearDecimal(beanDet
				.getM_ValorSolesEnviado(), 2));
		beanDet.setM_StrValorDolaresEnviado(Util.formatearDecimal(beanDet
				.getM_ValorDolaresEnviado(), 2));
		beanDet.setM_StrValorEurosEnviado(Util.formatearDecimal(beanDet
				.getM_ValorEurosEnviado(), 2));

		beanDet.setM_StrValorSolesProcesado(Util.formatearDecimal(beanDet
				.getM_ValorSolesProcesado(), 2));
		beanDet.setM_StrValorDolaresProcesado(Util.formatearDecimal(beanDet
				.getM_ValorDolaresProcesado(), 2));
		beanDet.setM_StrValorEurosProcesado(Util.formatearDecimal(beanDet
				.getM_ValorEurosProcesado(), 2));

		beanDet.setM_StrValorSolesErrado(Util.formatearDecimal(beanDet
				.getM_ValorSolesErrado(), 2));
		beanDet.setM_StrValorDolaresErrado(Util.formatearDecimal(beanDet
				.getM_ValorDolaresErrado(), 2));
		beanDet.setM_StrValorEurosErrado(Util.formatearDecimal(beanDet
				.getM_ValorEurosErrado(), 2));
		// jmoreno 21-08-09
		beanDet.setM_StrValorSolesCobrados(Util.formatearDecimal(beanDet
				.getM_ValorSolesCobrados(), 2));
		beanDet.setM_StrValorDolaresCobrados(Util.formatearDecimal(beanDet
				.getM_ValorDolaresCobrados(), 2));
		beanDet.setM_StrValorEurosCobrados(Util.formatearDecimal(beanDet
				.getM_ValorEurosCobrados(), 2));

		beanDet.setM_StrValorSolesCancelados(Util.formatearDecimal(beanDet
				.getM_ValorSolesCancelados(), 2));
		beanDet.setM_StrValorDolaresCancelados(Util.formatearDecimal(beanDet
				.getM_ValorDolaresCancelados(), 2));
		beanDet.setM_StrValorEurosCancelados(Util.formatearDecimal(beanDet
				.getM_ValorEurosCancelados(), 2));

		beanDet.setM_StrValorSolesCobradosParc(Util.formatearDecimal(beanDet
				.getM_ValorSolesCobradosParc(), 2));
		beanDet.setM_StrValorDolaresCobradosParc(Util.formatearDecimal(beanDet
				.getM_ValorDolaresCobradosParc(), 2));
		beanDet.setM_StrValorEurosCobradosParc(Util.formatearDecimal(beanDet
				.getM_ValorEurosCobradosParc(), 2));

		beanDet.setM_StrValorSolesPendConf(Util.formatearDecimal(beanDet
				.getM_ValorSolesPendConf(), 2));
		beanDet.setM_StrValorDolaresPendConf(Util.formatearDecimal(beanDet
				.getM_ValorDolaresPendConf(), 2));
		beanDet.setM_StrValorEurosPendConf(Util.formatearDecimal(beanDet
				.getM_ValorEurosPendConf(), 2));

		beanDet.setM_StrValorSolesExtornados(Util.formatearDecimal(beanDet
				.getM_ValorSolesExtornados(), 2));
		beanDet.setM_StrValorDolaresExtornados(Util.formatearDecimal(beanDet
				.getM_ValorDolaresExtornados(), 2));
		beanDet.setM_StrValorEurosExtornados(Util.formatearDecimal(beanDet
				.getM_ValorEurosExtornados(), 2));

		beanDet.setM_StrValorSolesArchivados(Util.formatearDecimal(beanDet
				.getM_ValorSolesArchivados(), 2));
		beanDet.setM_StrValorDolaresArchivados(Util.formatearDecimal(beanDet
				.getM_ValorDolaresArchivados(), 2));
		beanDet.setM_StrValorEurosArchivados(Util.formatearDecimal(beanDet
				.getM_ValorEurosArchivados(), 2));

		beanDet.setM_NumTotalSoles(beanDet.getM_NumArchivados()
				+ beanDet.getM_NumCancelados() + beanDet.getM_NumCobrados()
				+ beanDet.getM_NumCobradosParc() + beanDet.getM_NumEnviados()
				+ beanDet.getM_NumErrados() + beanDet.getM_NumExtornados()
				+ beanDet.getM_NumPendConf() + beanDet.getM_NumProcesados());
		beanDet.setM_NumTotalDolares(beanDet.getM_NumArchivadosDol()
				+ beanDet.getM_NumCanceladosDol()
				+ beanDet.getM_NumCobradosDol()
				+ beanDet.getM_NumCobradosParcDol()
				+ beanDet.getM_NumEnviadosDol() + beanDet.getM_NumErradosDol()
				+ beanDet.getM_NumExtornadosDol()
				+ beanDet.getM_NumPendConfDol()
				+ beanDet.getM_NumProcesadosDol());

		return beanDet;
	}

	
	public boolean change_state_items(List lstIdDtlOrd, List lstIdOrd,
			List lstServEmp, Character state) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			String strQuery = "update TpDetalleOrden set cdoestado = "
					+ state
					+ " "
					+ " where cdoIdOrden in (:lstIdOrd) and cdoIdDetalleOrden in (:lstIdDtlOrd) and cdoIdServicioEmpresa in  (:lstServEmp)";
			int i = session.createSQLQuery(strQuery).setParameterList(
					"lstIdOrd", lstIdOrd).setParameterList("lstIdDtlOrd",
					lstIdDtlOrd).setParameterList("lstServEmp", lstServEmp)
					.executeUpdate();

			session.getTransaction().commit();
			if (i <= 0) {
				return false;
			} else {
				logger.info("Se actualizarón a Estado Cancelado " + i
						+ " detalles");
				return true;
			}

		} catch (Exception e) {
			logger.error(e.toString(),e);
			return false;
		}finally{
			session.close();
		}
	}

	// Para Letras
	public boolean insert_2(List listadetorden, Map montos) {
		Session session = null;
		BigDecimal bsoles = new BigDecimal("0.0").setScale(2);
		BigDecimal bdolares = new BigDecimal("0.0").setScale(2);
		BigDecimal beuros = new BigDecimal("0.0").setScale(2);// jmoreno
																// 03-09-09
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			for (Iterator i = listadetorden.iterator(); i.hasNext();) {
				TpDetalleOrden tpdetalle = (TpDetalleOrden) i.next();
				if (tpdetalle.getCdomoneda() != null) {
					if (tpdetalle.getCdomoneda().trim().equalsIgnoreCase("PEN")) {
						bsoles = bsoles
								.add((tpdetalle.getNdomonto() != null) ? tpdetalle
										.getNdomonto()
										: new BigDecimal("0.0").setScale(2));
					} else if (tpdetalle.getCdomoneda().trim()
							.equalsIgnoreCase("USD")) {
						bdolares = bdolares
								.add((tpdetalle.getNdomonto() != null) ? tpdetalle
										.getNdomonto()
										: new BigDecimal("0.0").setScale(2));
					} else if (tpdetalle.getCdomoneda().trim()
							.equalsIgnoreCase("EUR")) {// jmoreno 03-09-09
						beuros = beuros
								.add((tpdetalle.getNdomonto() != null) ? tpdetalle
										.getNdomonto()
										: new BigDecimal("0.0").setScale(2));
					}
				}
				session.saveOrUpdate(tpdetalle);
				// tpdetalle = null;
			}
			session.getTransaction().commit();
			montos.put("PEN", bsoles);
			montos.put("USD", bdolares);
			montos.put("EUR", beuros);
		} catch (Exception e) {
			logger.error(e.toString(),e);
			return false;
		}finally{
			session.close();
		}
		return true;
	}

	public long obtenerCantItems(String idOrden, List estadosxCancelar) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();		
			List result = session
					.createQuery(
							"select count(tp.id.cdoidDetalleOrden) from TpDetalleOrden tp where tp.id.cdoidOrden = :idOrden and tp.cdoestado in (:estadosxCan)")
					.setParameter("idOrden", Long.parseLong(idOrden))
					.setParameterList("estadosxCan", estadosxCancelar).list();

			Iterator iter = result.iterator();
			Long code = null;
			if (iter.hasNext()) {
				code = (Long) iter.next();
			}
			long cantidad = code.longValue();
			return cantidad;
		} catch (Exception e) {			
			logger.error(e.toString(),e);
			return 0;
		}finally{
			session.close();
		}

	}

	// jmoreno 28-05-09 Para obtener la cantidad total de detalles de ordenes
	// para Pago de Servicios, para la paginacion
	public long obtenerCantItemsCobro(String rucEmpresa, List criterios) {
		Session session = null;
		StringBuilder strQuery = new StringBuilder();
		try {
			session = HibernateUtil.getSessionFactory().openSession();			
			strQuery.append(" select count(cDOIdDetalleOrden) from tpdetalleorden tp  with(nolock)");
			strQuery.append(" where tp.cDOIdOrden in (");
			strQuery.append(" select cOrIdOrden from taOrden");
			strQuery.append(" where cOrIdServicioEmpresa in");
			strQuery
					.append(" (select cSEmIdServicioEmpresa from taServicioxEmpresa");
			strQuery.append(" where cSEmIdEmpresa = :rucprov ");
			strQuery.append(" and cSEmIdServicio = '05'");
			strQuery
					.append(" and (select cEmEstado from tmempresa where cEmIdEmpresa = cSEmIdEmpresa ) = '0'");
			strQuery.append(" and cSEmEstado ='0')");
			strQuery.append(" and cOrEstado in ('1','6'))");
			strQuery.append(" and tp.cDOEstado = '0'");
			for (Iterator it = criterios.iterator(); it.hasNext();) {
				TaDetalleMapaCampos tadc = (TaDetalleMapaCampos) it.next();
				if (tadc.getValor() != null && tadc.getValor().length() > 0) {
					if ("NUM".equalsIgnoreCase(tadc.getDdmtipoDato()))
						strQuery.append(" AND tp.").append(
								tadc.getDdmcampoRef()).append(" ='").append(
								tadc.getValor()).append("'");
					else if ("ANS".equalsIgnoreCase(tadc.getDdmtipoDato()))
						strQuery.append(" AND tp.").append(
								tadc.getDdmcampoRef()).append(" LIKE '%")
								.append(tadc.getValor()).append("%'");
					else
						strQuery.append(" AND tp.").append(
								tadc.getDdmcampoRef()).append(" ='").append(
								tadc.getValor()).append("'");
				}

			}
			List result = session.createSQLQuery(strQuery.toString())
					.setParameter("rucprov", rucEmpresa).list();					
			Iterator iter = result.iterator();
			Integer code = null;
			if (iter.hasNext()) {
				code = (Integer) iter.next();
			}
			long cantidad = code.intValue();
			return cantidad;
		} catch (Exception e) {
			logger.error(e.toString(),e);
			return 0;
		}finally{
			session.close();
		}

	}
		

	
	/*
	 * (non-Javadoc)
	 * @see com.hiper.cash.dao.TpDetalleOrdenDao#seleccionarMovimientosNuevosCobros(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.hiper.cash.entidad.BeanPaginacion, com.hiper.cash.entidad.BeanTotalesConsMov)
	 */
	public List<Object[]> seleccionarMovimientosNuevosCobros(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida, BeanPaginacion bpag) {
		String strQuery = obtenerNuevoQueryConsultaMovimientos(idServEmp,
				estado, fecini, fecfin, referencia, contrapartida);
		@SuppressWarnings("unchecked")
		List<Object[]> queryResult = (List<Object[]>)ejecutarQueryConsultaMovimientos(
				idServEmp, estado, fecini, fecfin, referencia, contrapartida,
				strQuery, bpag);
		return queryResult;
	}		
	
	public List<Object[]> obtenerMontosMovimientosNuevosCobros(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida) {
		String strQuery = obtenerNuevoQueryConsultaMovimientos(idServEmp,
				estado, fecini, fecfin, referencia, contrapartida,2);
		List<Object[]> queryResult = ejecutarQueryConsultaMovimientos(
				idServEmp, estado, fecini, fecfin, referencia, contrapartida,
				strQuery, null);		
		return queryResult;
	}
		
	
	public List ejecutarConsultaMovimientosExportacion(String strQuery,
			String idServEmp, String estado, String fecini, String fecfin,
			String referencia, String contrapartida, String formato, boolean addOrderBy) {				
		if (estado != null && estado.length() > 0) {
			strQuery += " and tp.cDOEstado = :estadoid ";
		}
		if (referencia != null && referencia.length() > 0) {
			strQuery += " and tp.ddoReferencia like :referencia ";
		}
		if (contrapartida != null && contrapartida.length() > 0) {
			strQuery += " and tp.ddoContrapartida = :contrapartida ";
		}
		String parteSQL = "tp.fdofechaproceso";
		strQuery += " and " + parteSQL + " >= :fechaIni and " + parteSQL
				+ " <= :fechaFin ";
		if(addOrderBy){
			strQuery += " order by tp.cDOIdOrden asc";
		}
		if(logger.isDebugEnabled()){
			logger.debug("Query exportacion: "+strQuery);
		}
		List resultadosQuery;
		Session session=null;
		try {
			session = HibernateUtil.getReportsSessionFactory().openSession();		
			Query query = session.createSQLQuery(strQuery);
			query.setParameter("idServEmp", idServEmp);
			query.setParameter("fechaIni", Fecha.convertToFechaSQL(fecini));
			query.setParameter("fechaFin", Fecha.convertToFechaSQL(fecfin));
			if (estado != null && estado.length() > 0) {
				query.setParameter("estadoid", estado);
			}
			if (referencia != null && referencia.length() > 0) {
				query.setParameter("referencia", "%" + referencia + "%");
			}
			if (contrapartida != null && contrapartida.length() > 0) {
				query.setParameter("contrapartida", contrapartida );
			}
			resultadosQuery = query.list();			
		} finally{
			session.close();
		}
		return resultadosQuery;
	}    

	
	public List<BeanDetalleOrden> selectDetallePagoReferenciaNoTrx(long orden,
			long servicio, String referencia, int inicio, int maximo)
			throws Exception {
		List<Object[]> result = new ArrayList<Object[]>();
		Iterator<Object[]> iter;
		Map<String, Object> parametros = new HashMap<String, Object>();	
		parametros.put("idorden", orden);
		parametros.put("servicioid", servicio);
		
		
		
		StringBuilder sb = new StringBuilder("SELECT cDOIdOrden,cDOIdDetalleOrden,dDOTipoCuenta, NdonumeroCuenta,DdotipoPago, ");
		sb = sb.append("DdotipoDocumento,nDoDocumento,nombreEstadoItem,Ddoreferencia,dDONombre, ");
		sb = sb.append("FdofechaProceso,HdohoraProceso,CdocodigoRptaIbs,cDOMoneda,Ndomonto, ");
		sb = sb.append("NdoidPago,NdonumCheque,nDOMontoCliComisionCHG,nDOMontoEmpComisionCHG ");		
		sb = sb.append("FROM TpDetalleOrdenNoTrx ");
		//sb = sb.append("WHERE cDOIdOrden=:idorden AND cDOIdServicioEmpresa=:servicioid AND flagEstadoRep=0 ");
		sb = sb.append("WHERE cDOIdOrden=:idorden AND cDOIdServicioEmpresa=:servicioid ");
		//agregado para filtrar las transacciones realizadas
		sb = sb.append("AND  cDOIdDetalleOrden NOT IN (SELECT cDOIdDetalleOrden FROM TpDetalleOrdenTrx WHERE cDOIdOrden=:idorden AND cDOIdServicioEmpresa=:servicioid  ) ");
		
		
		if( referencia != null && referencia.length() > 0 ){
			sb = sb.append(" AND (ddoreferencia  like :ref OR ddocontrapartida like :ref)");
			parametros.put("ref", "%"+referencia+"%");
		}else{
			sb = sb.append(" ORDER BY cDOIdDetalleOrden");
		}	
		
		Session session=null;
		try {
			session = HibernateUtil.getReportsSessionFactory().openSession();
			Query query = EntityDAO.createQuery(session, TipoConsulta.SQL,
					sb.toString(), parametros, inicio, maximo);
			result = query.list();
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		iter = result.iterator();
		int i = 0;
		List<BeanDetalleOrden> lista = new ArrayList<BeanDetalleOrden>();
		BeanDetalleOrden beandetorden;
		String tipoCuenta,tipoMoneda;
		String fechaProceso,horaProceso;
		while (iter.hasNext()) {
			i=0;
			Object[] al_servicio = (Object[]) iter.next();			
			beandetorden = new BeanDetalleOrden();
			beandetorden.setM_IdOrden(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_IdDetalleOrden(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;		
			tipoCuenta = al_servicio[i] !=null ? al_servicio[i].toString() : "";i++;
			if( tipoCuenta.equals(CashConstants.COD_TC_AHORROS)){
				beandetorden.setM_DescTipoCuenta(CashConstants.VAL_TC_AHORROS);
			}else if(tipoCuenta.equals(CashConstants.COD_TC_CORRIENTE)){
				beandetorden.setM_DescTipoCuenta(CashConstants.VAL_TC_CORRIENTE);
			}else if(tipoCuenta.equals(CashConstants.COD_TC_CTS)){
				beandetorden.setM_DescTipoCuenta(CashConstants.VAL_TC_CTS);
			}else{
				beandetorden.setM_DescTipoCuenta(tipoCuenta);
			}			
			beandetorden.setM_NumeroCuenta(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_DescTipoPago(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_DescTipoDocumento(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;			
			beandetorden.setM_Documento(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_DescEstado(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_Referencia(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_Nombre(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			
			fechaProceso = al_servicio[i] !=null ? al_servicio[i].toString() : "";i++;
			horaProceso = al_servicio[i] !=null ? al_servicio[i].toString() : "";i++;
			
			if ( fechaProceso.length() == 0) {
				beandetorden.setM_FechaProceso(" - ");
			} else {
				if (horaProceso.length() == 0)
					beandetorden.setM_FechaProceso(Fecha.convertFromFechaSQL(fechaProceso));
				else
					beandetorden.setM_FechaProceso(Fecha
							.convertFromFechaSQL(fechaProceso)
							+ " - "
							+ Fecha.convertFromTimeSQL(horaProceso));
				
			}
			beandetorden.setM_CodigoRptaIbs(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_descripcionCodIbs("Codigo de Operacion");
			tipoMoneda = al_servicio[i] !=null ? al_servicio[i].toString() : "";i++;
			if( tipoMoneda.equals(CashConstants.VAL_IBS_SOLES)){
				beandetorden.setM_DescTipoMoneda(CashConstants.VAL_SOLES_SIMB);
			}else{
				beandetorden.setM_DescTipoMoneda(CashConstants.VAL_DOLARES_SIMB);
			}
			beandetorden.setM_Monto(Util.formatearMontoNvo(al_servicio[i] !=null ? al_servicio[i].toString() : "0"));i++;
			beandetorden.setM_IdPago(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;			
			beandetorden.setM_NumCheque(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_montoComClienteChg(Util.formatearMontoNvo(String.valueOf(al_servicio[i] !=null ? al_servicio[i].toString() : "0")));i++;
			beandetorden.setM_montoComEmpresaChg(Util.formatearMontoNvo(String.valueOf(al_servicio[i] !=null ? al_servicio[i].toString() : "0")));i++;		
			lista.add(beandetorden);
		}
		return lista;
	}	
	
	public int cuentaDetallePagoReferenciaNoTrx(long orden, long servicio,
			String referencia) throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();	
		parametros.put("idorden", orden);
		parametros.put("servicioid", servicio);
		
		
		StringBuilder sb = new StringBuilder("SELECT count(*) ");
		
		sb = sb.append("FROM TpDetalleOrdenNoTrx ");
		//sb = sb.append("WHERE cDOIdOrden=:idorden AND cDOIdServicioEmpresa=:servicioid AND flagEstadoRep=0 ");
		sb = sb.append("WHERE cDOIdOrden=:idorden AND cDOIdServicioEmpresa=:servicioid  ");
		//agregado para filtrar las transacciones realizadas
		sb = sb.append("AND  cDOIdDetalleOrden NOT IN (SELECT cDOIdDetalleOrden FROM TpDetalleOrdenTrx WHERE cDOIdOrden=:idorden AND cDOIdServicioEmpresa=:servicioid  ) ");
		

		
		
		if( referencia != null && referencia.length() > 0 ){
			sb = sb.append("AND (ddoreferencia  like :ref OR ddocontrapartida like :ref)");
			parametros.put("ref", "%"+referencia+"%");
		}
		Session session=null;
		Integer obj=null;
		try {
			session = HibernateUtil.getReportsSessionFactory().openSession();
			Query query = EntityDAO.createQuery(session, TipoConsulta.SQL,
					sb.toString(), parametros);
			obj = (Integer) query.uniqueResult();
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}		
		return obj.intValue();		
	}
	
	public List<BeanDetalleOrden> selectDetallePagoReferenciaTrx(long orden,
			long servicio, String referencia, int inicio, int maximo)
			throws Exception {
		List<Object[]> result = new ArrayList<Object[]>();
		Iterator<Object[]> iter;
		Map<String, Object> parametros = new HashMap<String, Object>();	
		parametros.put("idorden", orden);
		parametros.put("servicioid", servicio);	
		
		
		StringBuilder sb = new StringBuilder("SELECT cDOIdOrden,cDOIdDetalleOrden,dDOTipoCuenta, NdonumeroCuenta,DdotipoPago, ");
		sb = sb.append("DdotipoDocumento,nDoDocumento,nombreEstadoItem,Ddoreferencia,dDONombre, ");
		sb = sb.append("FdofechaProceso,HdohoraProceso,CdocodigoRptaIbs,cDOMoneda,Ndomonto, ");
		sb = sb.append("NdoidPago,NdonumCheque,nDOMontoCliComisionCHG,nDOMontoEmpComisionCHG, ");
		sb = sb.append(" nDOMontoLetra,nDOCodAceptante,nDOMontoMora,cDOMonedaMontoMora, ");
		sb = sb.append(" nDOItf, cDOMonedaItf, cDOMonedaPortes,nDOPortes, nDOProtesto, cDOMonedaProtesto ");
		sb = sb.append("FROM TpDetalleOrdenTrx ");
		sb = sb.append("WHERE cDOIdOrden=:idorden AND cDOIdServicioEmpresa=:servicioid AND flagEstadoRep=0 ");
		if( referencia != null && referencia.length() > 0 ){
			sb = sb.append("AND (ddoreferencia  like :ref OR ddocontrapartida like :ref) ORDER BY cDOIdDetalleOrden");
			parametros.put("ref", "%"+referencia+"%");
		}else{
			sb = sb.append(" ORDER BY cDOIdDetalleOrden");
		}		
		Session session=null;	
		try {
			session = HibernateUtil.getReportsSessionFactory().openSession();
			Query query = EntityDAO.createQuery(session, TipoConsulta.SQL,
					sb.toString(), parametros, inicio, maximo);
			result = query.list();
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		iter = result.iterator();
		int i = 0;
		List<BeanDetalleOrden> lista = new ArrayList<BeanDetalleOrden>();
		BeanDetalleOrden beandetorden;
		String tipoCuenta,tipoMoneda;
		String fechaProceso,horaProceso;
		while (iter.hasNext()) {
			i=0;
			Object[] al_servicio = (Object[]) iter.next();			
			beandetorden = new BeanDetalleOrden();
			beandetorden.setM_IdOrden(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_IdDetalleOrden(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;		
			tipoCuenta = al_servicio[i] !=null ? al_servicio[i].toString() : "";i++;
			if( tipoCuenta.equals(CashConstants.COD_TC_AHORROS)){
				beandetorden.setM_DescTipoCuenta(CashConstants.VAL_TC_AHORROS);
			}else if(tipoCuenta.equals(CashConstants.COD_TC_CORRIENTE)){
				beandetorden.setM_DescTipoCuenta(CashConstants.VAL_TC_CORRIENTE);
			}else if(tipoCuenta.equals(CashConstants.COD_TC_CTS)){
				beandetorden.setM_DescTipoCuenta(CashConstants.VAL_TC_CTS);
			}else{
				beandetorden.setM_DescTipoCuenta(tipoCuenta);
			}			
			beandetorden.setM_NumeroCuenta(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_DescTipoPago(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_DescTipoDocumento(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;			
			beandetorden.setM_Documento(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_DescEstado(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_Referencia(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_Nombre(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			
			fechaProceso = al_servicio[i] !=null ? al_servicio[i].toString() : "";i++;
			horaProceso = al_servicio[i] !=null ? al_servicio[i].toString() : "";i++;
			
			if ( fechaProceso.length() == 0) {
				beandetorden.setM_FechaProceso(" - ");
			} else {
				if (horaProceso.length() == 0)
					beandetorden.setM_FechaProceso(Fecha.convertFromFechaSQL(fechaProceso));
				else
					beandetorden.setM_FechaProceso(Fecha
							.convertFromFechaSQL(fechaProceso)
							+ " - "
							+ Fecha.convertFromTimeSQL(horaProceso));
				
			}
			beandetorden.setM_CodigoRptaIbs(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_descripcionCodIbs(descripcionesCodigosIBS.get(beandetorden.getM_CodigoRptaIbs()));
			tipoMoneda = al_servicio[i] !=null ? al_servicio[i].toString() : "";i++;
			if( tipoMoneda.equals(CashConstants.VAL_IBS_SOLES)){
				beandetorden.setM_DescTipoMoneda(CashConstants.VAL_SOLES_SIMB);
			}else{
				beandetorden.setM_DescTipoMoneda(CashConstants.VAL_DOLARES_SIMB);
			}
			beandetorden.setM_Monto(Util.formatearMontoNvo(al_servicio[i] !=null ? al_servicio[i].toString() : "0"));i++;
			beandetorden.setM_IdPago(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;			
			beandetorden.setM_NumCheque(al_servicio[i] !=null ? al_servicio[i].toString() : "");i++;
			beandetorden.setM_montoComClienteChg(Util.formatearMontoNvo(String.valueOf(al_servicio[i] !=null ? al_servicio[i].toString() : "0")));i++;
			beandetorden.setM_montoComEmpresaChg(Util.formatearMontoNvo(String.valueOf(al_servicio[i] !=null ? al_servicio[i].toString() : "0")));i++;
			beandetorden.setM_Principal(Util.formatearMontoNvo(String.valueOf(al_servicio[i] == null ? "0" : al_servicio[i])));i++;
			beandetorden.setM_CodAceptante(al_servicio[i]==null?"" : al_servicio[i].toString());i++;
			beandetorden.setM_MontoMora(Util.formatearMontoNvo(String.valueOf(al_servicio[i] == null ? "0" : al_servicio[i])));i++;
			beandetorden.setM_MonedaMora(al_servicio[i] == null ? " - " : al_servicio[i].toString());i++;
			beandetorden.setM_ITF(Util.formatearMontoNvo(String.valueOf(al_servicio[i] == null ? "0" : al_servicio[i])));i++;
			beandetorden.setM_MonedaITF(String.valueOf(al_servicio[i] == null ? "-" : al_servicio[i]));i++;
			beandetorden.setM_MonedaPortes(String.valueOf(al_servicio[i] == null ? "-" : al_servicio[i]));i++;
			beandetorden.setM_Portes(Util.formatearMontoNvo(String.valueOf(al_servicio[i] == null ? "0" : al_servicio[i])));i++;			
			beandetorden.setM_Protesto(Util.formatearMontoNvo(String.valueOf(al_servicio[i] == null ? "0" :al_servicio[i])));i++;
			beandetorden.setM_MonedaProtesto(String.valueOf(al_servicio[i] == null ? "-" : al_servicio[i]));i++;
			beandetorden.setM_DescMonedaITF(descripcionesTipoMoneda.get(beandetorden.getM_MonedaITF()));
			beandetorden.setM_DescMonedaPortes(descripcionesTipoMoneda.get(beandetorden.getM_MonedaPortes()));
			beandetorden.setM_MonedaProtesto(descripcionesTipoMoneda.get(beandetorden.getM_MonedaProtesto()));
			beandetorden.setM_MonedaMora(descripcionesTipoMoneda.get(beandetorden.getM_MonedaMora()));

			lista.add(beandetorden);
		}
		return lista;
	}	
	
	public int cuentaDetallePagoReferenciaTrx(long orden, long servicio,
			String referencia) throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();	
		parametros.put("idorden", orden);
		parametros.put("servicioid", servicio);
		
		
		StringBuilder sb = new StringBuilder("SELECT count(*) ");
		
		sb = sb.append("FROM TpDetalleOrdenTrx ");
		sb = sb.append("WHERE cDOIdOrden=:idorden AND cDOIdServicioEmpresa=:servicioid AND flagEstadoRep=0 ");
		if( referencia != null && referencia.length() > 0 ){
			sb = sb.append("AND (ddoreferencia  like :ref OR ddocontrapartida like :ref)");
			parametros.put("ref", "%"+referencia+"%");
		}
		Session session=null;
		Integer obj=null;
		try {
			session = HibernateUtil.getReportsSessionFactory()
					.openSession();
			Query query = EntityDAO.createQuery(session, TipoConsulta.SQL,
					sb.toString(), parametros);
			obj = (Integer) query.uniqueResult();
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		return obj.intValue();		
	}
			

	public Map<String, BigDecimal> registrar(List<TpDetalleOrden> lista)
			throws Exception {
		Session session = null;
		BigDecimal bsoles = new BigDecimal("0.0").setScale(2);
		BigDecimal bdolares = new BigDecimal("0.0").setScale(2);
		BigDecimal beuros = new BigDecimal("0.0").setScale(2);
		Map<String,BigDecimal> montos = new HashMap<String,BigDecimal>();
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			for (Iterator<TpDetalleOrden> i = lista.iterator(); i.hasNext();) {
				TpDetalleOrden tpdetalle = i.next();
				if (tpdetalle.getCdomoneda() != null) {
					if (tpdetalle.getCdomoneda().trim().equalsIgnoreCase(VAL_IBS_SOLES)) {						
						bsoles = bsoles.add(tpdetalle.getNdomonto());						
					} else if (tpdetalle.getCdomoneda().trim().equalsIgnoreCase(VAL_IBS_DOLARES)) {
						bdolares = bdolares	.add((tpdetalle.getNdomonto() != null) ? tpdetalle.getNdomonto()
										: new BigDecimal(0));
					} else if (tpdetalle.getCdomonedaMontoAbonado().trim().equalsIgnoreCase(VAL_IBS_EUROS)) {
						beuros = beuros.add((tpdetalle.getNdomonto() != null) ? tpdetalle.getNdomonto()	: new BigDecimal("0.0").setScale(2));
					}
				}
				session.saveOrUpdate(tpdetalle);
			}
			session.getTransaction().commit();
			montos.put(VAL_IBS_SOLES, bsoles);
			montos.put(VAL_IBS_DOLARES, bdolares);
			montos.put(VAL_IBS_EUROS, beuros);
			return montos;
		} catch (NumberFormatException nfe) {			
			throw new Exception("ERROR: El monto  ingresado no tiene el formato adecuado");
		} catch (Exception e) {			
			throw e;
		}finally{
			session.close();
		}
	}
	
	public void registrarReplica(List<TpDetalleOrden> lista)
	throws Exception {
		Session session = null;
		//BigDecimal bsoles = new BigDecimal("0.0").setScale(2);
		//BigDecimal bdolares = new BigDecimal("0.0").setScale(2);
		//BigDecimal beuros = new BigDecimal("0.0").setScale(2);
		//Map<String,BigDecimal> montos = new HashMap<String,BigDecimal>();
		try {
			session = HibernateUtil.getReportsSessionFactory().openSession();
			session.beginTransaction();
			for (Iterator<TpDetalleOrden> i = lista.iterator(); i.hasNext();) {
				TpDetalleOrden tpdetalle = i.next();
				/*if (tpdetalle.getCdomoneda() != null) {
					if (tpdetalle.getCdomoneda().trim().equalsIgnoreCase(VAL_IBS_SOLES)) {						
						//bsoles = bsoles.add(tpdetalle.getNdomonto());						
					} else if (tpdetalle.getCdomoneda().trim().equalsIgnoreCase(VAL_IBS_DOLARES)) {
						bdolares = bdolares	.add((tpdetalle.getNdomonto() != null) ? tpdetalle.getNdomonto()
										: new BigDecimal(0));
					} else if (tpdetalle.getCdomonedaMontoAbonado().trim().equalsIgnoreCase(VAL_IBS_EUROS)) {
						beuros = beuros.add((tpdetalle.getNdomonto() != null) ? tpdetalle.getNdomonto()	: new BigDecimal("0.0").setScale(2));
					}
				}*/				
				session.saveOrUpdate(tpdetalle);
			}
			session.getTransaction().commit();
			//montos.put(VAL_IBS_SOLES, bsoles);
			//montos.put(VAL_IBS_DOLARES, bdolares);
			//montos.put(VAL_IBS_EUROS, beuros);
			//return montos;
		} catch (NumberFormatException nfe) {			
			throw new Exception("ERROR: El monto  ingresado no tiene el formato adecuado");
		} catch (Exception e) {			
			throw e;
		}finally{
			session.close();
		}
	}
	
	
	public Map<String, BigDecimal> registrar(TpDetalleOrden detalle)
			throws Exception {
		List<TpDetalleOrden> detalles = new  ArrayList<TpDetalleOrden>();
		detalles.add(detalle);
		return registrar(detalles);
	}

	public void registrarReplica(TpDetalleOrden detalle) throws Exception {
		List<TpDetalleOrden> detalles = new  ArrayList<TpDetalleOrden>();
		detalles.add(detalle);
		registrarReplica(detalles);		
	}
	
	
	//@param tipoQuery 0=detalleMov,  2=montos
	private String obtenerNuevoQueryConsultaMovimientos(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida) {
		return obtenerNuevoQueryConsultaMovimientos(idServEmp, estado, fecini,
				fecfin, referencia, contrapartida, 0);
	}
	
	
	/**
	 * 
	 * @param idServEmp
	 * @param estado
	 * @param fecini
	 * @param fecfin
	 * @param referencia
	 * @param contrapartida
	 * @param tipoQuery 0=detalleMov, 2=montos
	 * @return
	 */
	private String obtenerNuevoQueryConsultaMovimientos(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida, int tipoQuery) {			
		String sqlColumns = null;		
		if(tipoQuery==0) {						
			sqlColumns= " tp.fdoFechaProceso as fechaDeMovimiento, " +//0
					" tp.ddoTipoPago, " +//1
					" tp.ndoNumeroCuenta," +//2
					" tp.ddoContrapartida, "+//3
					" tp.ddoNombre, " +//4
					" tp.ndoMonto," +//5 : valor enviado
					" tp.cdoMoneda," +//6
					" tp.nombreEstadoItem," +//7
					" tp.ddoReferencia, "+//8
					" tp.cdoIdOrden," +//9
					" ta.dorReferencia," +//10
					" tp.ndonumCheque," +//11
					" ta.nombreEmpresa, "+//12
					" tp.nDOMontoVentanilla," +//13: valor procesado
					" tp.nDOMontoNeto," +//14 : valor neto
					" tp.cDOIdAgencia," +//15
					" tp.ddocodorientacion ";					
		}else if(tipoQuery==2){
			sqlColumns=" count(*) as registros ,tp.cDOMoneda , " +
					" sum(tp.nDOMontoVentanilla) as montoVent,sum(tp.nDOMontoNeto) as montoNeto ";							
		}if (tipoQuery==1) {
			sqlColumns = " count(*) ";
		}
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("select ");
		queryBuilder.append(sqlColumns);
		queryBuilder.append("from "+Constantes.NOMBRE_TABLA_ORDEN+ " ta join "+ Constantes.NOMBRE_TABLA_DETALLE_ORDEN_TRX +" tp ");
		queryBuilder.append("on (ta.cOrIdOrden = tp.cDOIdOrden and ta.cOrIdServicioEmpresa=tp.cDOIdServicioEmpresa) where ");
		queryBuilder.append("tp.cdoIdServicioEmpresa = :idServEmp ");
		queryBuilder.append("and tp.flagEstadoRep = 0 ");
		String parteSQL = "tp.fdofechaproceso";
		queryBuilder.append("and " + parteSQL + " >= :fechaIni ");
		queryBuilder.append("and " + parteSQL + " <= :fechaFin ");
		if (estado != null && estado.length() > 0) {
			queryBuilder.append(" and tp.cDOEstado = :estadoid ");
		}
		if (referencia != null && referencia.length() > 0) {
			queryBuilder.append(" and tp.ddoReferencia like :referencia ");
		}
		if (contrapartida != null && contrapartida.length() > 0) {
			queryBuilder
					.append(" and tp.ddoContrapartida = :contrapartida ");
		}
		if (tipoQuery==0) {
			queryBuilder.append(" order by fDOFechaProceso asc, hDOHoraProceso asc ");
		}else if (tipoQuery==2) {
			queryBuilder.append(" group by cDOMoneda ");
		}
		if (logger.isDebugEnabled()) {
			logger.debug(queryBuilder.toString());
		}
		return queryBuilder.toString();
	}
	
			
	private List ejecutarQueryConsultaMovimientos(String idServEmp,
			String estado, String fecini, String fecfin, String fecIniNuevo, String fecFinNuevo, String referencia, String contrapartida,
			String strQuery, BeanPaginacion bpag) {
		
		Session session=null;
		List resultados;
		try {			
			session = HibernateUtil.getReportsSessionFactory().openSession();
			Query query = session.createSQLQuery(strQuery);
			query.setParameter("idServEmp", idServEmp);
			if (fecini.length() > 0 && fecfin.length() > 0) {
				query.setParameter("fechaIni", Fecha.convertToFechaSQL(fecini));
				query.setParameter("fechaFin", Fecha.convertToFechaSQL(fecfin));
			}
			if (fecIniNuevo.length() > 0 && fecFinNuevo.length() > 0) {
				query.setParameter("fechaIniNuevo", Fecha.convertToFechaSQL(fecIniNuevo));
				query.setParameter("fechaFinNuevo", Fecha.convertToFechaSQL(fecFinNuevo));
			}
			if (estado != null && estado.length() > 0) {
				query.setParameter("estadoid", estado);
			}
			if (referencia != null && referencia.length() > 0) {
				query.setParameter("referencia","%"+referencia+"%");
			}
			if (contrapartida != null && contrapartida.length() > 0) {
				query.setParameter("contrapartida", contrapartida);
			}
			if(bpag!=null){
				query.setFirstResult((int) bpag.getM_seleccion()).setMaxResults(
					bpag.getM_regPagina());
			}
			resultados = query.list();			
		} 
		finally{
			session.close();
		}
		return resultados;
	}
	
	private List ejecutarQueryConsultaMovimientos(String idServEmp,
			String estado, String fecini, String fecfin, String referencia, String contrapartida,
			String strQuery, BeanPaginacion bpag) {
		return ejecutarQueryConsultaMovimientos(idServEmp, estado, fecini, fecfin, "", "", referencia, contrapartida, strQuery, bpag);
	}

	@Override
	public void actualizarItemsConsultas(List lstIdDtlOrd, List lstIdOrd,
			List lstServEmp, Character state) {
		
		Session session = null;
		String nombreEstado="";
		String estado2="";
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			estado2=String.valueOf(state);
			
			//System.out.println("==>"+estado2);

			
			if(estado2.equals("0")){
				nombreEstado="Ingresado";				
			}else if(estado2.equals("1")){
				nombreEstado="Procesado";				
			}else if(estado2.equals("2")){
				nombreEstado="Errado";				
			}else if(estado2.equals("3")){
				nombreEstado="Vencido";				
			}else if(estado2.equals("4")){
				nombreEstado="Cobrado";				
			}else if(estado2.equals("5")){
				nombreEstado="Cancelado";				
			}else if(estado2.equals("6")){
				nombreEstado="Cobro Parcial";				
			}else if(estado2.equals("7")){
				nombreEstado="Pend. Confirmación";				
			}else if(estado2.equals("8")){
				nombreEstado="Ingresado(Ex)";				
			}else if(estado2.equals("9")){
				nombreEstado="Archivado";				
			}else if(estado2.equals("E")){
				nombreEstado="En Proceso";				
			}else{
				nombreEstado="";
			}
			
			String strQuery = "update BFP_CASH_CONSULTAS.dbo.TpDetalleOrdenNoTrx set cdoestado = '"+ state+ "', nombreEstadoItem='"+nombreEstado+"' "
					+ " where cdoIdOrden in (:lstIdOrd) and cdoIdDetalleOrden in (:lstIdDtlOrd) and cdoIdServicioEmpresa in  (:lstServEmp)";
			int i = session.createSQLQuery(strQuery).setParameterList(
					"lstIdOrd", lstIdOrd).setParameterList("lstIdDtlOrd",
					lstIdDtlOrd).setParameterList("lstServEmp", lstServEmp)
					.executeUpdate();

			session.getTransaction().commit();
			if (i <= 0) {
				//return false;
			} else {
				logger.info("Se actualizarón a Estado Cancelado " + i
						+ " detalles");
				//return true;
			}

		} catch (Exception e) {
			logger.error(e.toString(),e);
			//return false;
		}finally{
			session.close();
		}		
	}
  
	
	public int obtenerCantidadMovimientos(String idServEmp,
			String estado, String fecini, String fecfin, String referencia,
			String contrapartida) {
		String strQuery = obtenerNuevoQueryConsultaMovimientos(idServEmp,
				estado, fecini, fecfin, referencia, contrapartida,1);
		List queryResult = ejecutarQueryConsultaMovimientos(
				idServEmp, estado, fecini, fecfin, referencia, contrapartida,
				strQuery, null);
		int count=0;
		if(queryResult!=null && queryResult.size()>0){
			count= (Integer)queryResult.get(0);
		}
		return count;
	}

	
	public long obtenerIdDetalleOrden(TpDetalleOrden detalle) throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();	
		parametros.put("idorden", detalle.getId().getCdoidOrden());
		parametros.put("servicioid", detalle.getId().getCdoidServicioEmpresa());
		parametros.put("idDetalle", detalle.getId().getCdoidDetalleOrden());		
		StringBuilder sb = new StringBuilder("SELECT cDOIdItemDetalle ");		
		sb = sb.append("FROM TpDetalleOrden ");
		sb = sb.append("WHERE cDOIdOrden=:idorden AND cDOIdServicioEmpresa=:servicioid AND cDOIdDetalleOrden=:idDetalle ");
		
		Session session=null;
		BigInteger bg = new BigInteger("0");
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			Query query = EntityDAO.createQuery(session, TipoConsulta.SQL,sb.toString(), parametros);
			bg  =  (BigInteger)query.uniqueResult();			
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		return bg.longValue();		
	}

	
	public TpDetalleOrden getTpDetalleOrden(long idOrden,
			long idServicioEmpresa, long idItemDetalle) {
		Session session=null;		
		TpDetalleOrdenId id = new  TpDetalleOrdenId(idOrden,idServicioEmpresa,idItemDetalle);
		try {
			session = HibernateUtil.getReportsSessionFactory().openSession();
			return (TpDetalleOrden) session.get(TpDetalleOrden.class, id);
			//Query query = EntityDAO.createQuery(session, TipoConsulta.SQL,sb.toString(), parametros);
			//bg  =  (BigInteger)query.uniqueResult();			
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<TpDetalleOrden> listaTpDetalleOrden(long idOrden,
			long idServicioEmpresa) {
		StringBuilder sql = new StringBuilder();

		sql = sql.append("SELECT tp FROM TpDetalleOrden tp WHERE ").append(" tp.id.cdoidServicioEmpresa = :servicioid AND ").append("tp.id.cdoidOrden = :idorden ");
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("servicioid", idServicioEmpresa);		
		parametros.put("idorden", idOrden);		
		Session session=null;
		try {
			session = HibernateUtil.getReportsSessionFactory().openSession();
			Query q = EntityDAO.createQuery(session, TipoConsulta.HQL, sql.toString(), parametros);
			return q.list();
			//return (TpDetalleOrden) session.get(TpDetalleOrden.class, id);				
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		return null;
	}


	

	


	

	
}

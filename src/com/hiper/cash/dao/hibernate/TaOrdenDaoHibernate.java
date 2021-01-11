/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.financiero.cash.dao.EntityDAO;
import com.hiper.cash.dao.TaOrdenDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TaDetalleMapaCampos;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.domain.TaOrdenId;
import com.hiper.cash.domain.TpDetalleOrden;
import com.hiper.cash.entidad.BeanDetalleOrden;
import com.hiper.cash.entidad.BeanOrden;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.entidad.BeanTotalesConsMov;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.TipoConsulta;
import com.hiper.cash.util.Util;

public class TaOrdenDaoHibernate implements TaOrdenDao {

	private static Logger logger = Logger.getLogger(TaOrdenDaoHibernate.class);

	public List select(String empresa, List servicio, List estado) { // jmoreno
																		// 13/11/09
		String strQuery;
		Session session = null;
		strQuery = "select ta.id.corIdOrden, tm.cemIdEmpresa, ta.id.corIdServicioEmpresa, "
				+ "tm.demNombre, ta.norNumeroCuenta,ta.dorReferencia, ta.forFechaInicio,ta.forFechaFin, "
				+ "tp.dsemDescripcion, tl.dlfDescription, tp.tmServicio.csrIdServicio "
				+ "from TaOrden ta, TmEmpresa tm, TaServicioxEmpresa tp, TxListField tl where "
				+ "ta.id.corIdServicioEmpresa in (:servicioList) and tp.csemIdServicioEmpresa = ta.id.corIdServicioEmpresa "
				+ "and tp.tmEmpresa.cemIdEmpresa = tm.cemIdEmpresa and ta.corEstado = tl.id.clfCode and tl.id.dlfFieldName = :campid "
				+ "and ta.corEstado in (:estadoList) and ta.forFechaFin >= :fechabase order by ta.id.corIdOrden desc";
		List result;
		try {

			String fechabase = Util.getFechaActualSQL();
			Query q;
			session = HibernateUtil.getSessionFactory().openSession();			
			q = session.createQuery(strQuery).setParameterList("servicioList",
					servicio).setParameterList("estadoList", estado)			
					.setParameter("campid", "CashEstadoOrden").setParameter(
							"fechabase", fechabase);
			result = q.list();			

			Iterator iter = result.iterator();
			BeanOrden beanorden;
			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				Object[] al_servicio = (Object[]) iter.next();
				beanorden = new BeanOrden();
				beanorden.setM_IdOrden(al_servicio[0].toString());
				beanorden.setM_IdEmpresa(al_servicio[1].toString());
				beanorden.setM_IdServicio(al_servicio[2].toString());

				beanorden.setM_Empresa((String) al_servicio[3]);
				beanorden.setM_CuentaCargo((String) al_servicio[4]);
				beanorden.setM_Referencia((String) al_servicio[5]);
				beanorden.setM_FecInicio(Fecha
						.convertFromFechaSQL((String) al_servicio[6]));
				beanorden.setM_FecVenc(Fecha
						.convertFromFechaSQL((String) al_servicio[7]));
				beanorden.setM_Servicio((String) al_servicio[8]);
				beanorden.setM_DescripEstado((String) al_servicio[9]);

				// jwong 11/05/2009 nuevo campo para manejo del codigo del
				// servicio
				beanorden.setM_CodServicio((String) al_servicio[10]);
				alresult.add(beanorden);
				beanorden = null;
			}
			result = null;

			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString());
			return new ArrayList();
		}
		finally{
			session.close();
		}
	}

	public BeanOrden select(String idorden, String idservemp) {
		Session session = null;
		StringBuilder strQuery = new StringBuilder();
		strQuery
				.append(
						" select ta.id.corIdOrden, ta.id.corIdServicioEmpresa, ")
				.append(" ta.forFechaRegistro, ")
				.append(" ta.forFechaInicio, ")
				.append(" ta.forFechaFin, ")
				.append(" ta.horHoraInicio, ")
				.append(" ta.dorReferencia, ")
				// .append(" ta.corEstado, ")
				.append(" ta.dorTipoCuenta, ")
				.append(" ta.norNumeroCuenta, ")
				.append(" tm.demNombre, ")
				// descripcion de la empresa
				.append(" tp.tmServicio.dsrDescripcion, ")
				// descripcion del servicio
				.append(" tx2.dlfDescription, ")
				// descripcion del estado
				.append(" ta.norNumeroRegistros ")
				// numero de Registros
				.append(
						" from TaOrden ta, TmEmpresa tm, TaServicioxEmpresa tp, TxListField tx2 ")
				.append(" where ta.id.corIdServicioEmpresa = :idservemp ")
				.append(
						" and tp.csemIdServicioEmpresa = ta.id.corIdServicioEmpresa ")

				.append(
						" and tp.csemEstado = '"
								+ Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO
								+ "' ")

				.append(" and tp.tmEmpresa.cemIdEmpresa = tm.cemIdEmpresa ")
				.append(" and tx2.id.clfCode = ta.corEstado ").append(
						" and tx2.id.dlfFieldName = :codEstado ").append(
						" and ta.id.corIdOrden = :idorden ");

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			
			Object[] result = (Object[]) session.createQuery(
					strQuery.toString()).setParameter("idorden",
					Long.parseLong(idorden)).setParameter("idservemp",
					Long.parseLong(idservemp)).setParameter("codEstado",
					"CashEstadoOrden").uniqueResult();

			// session.load
			BeanOrden beanorden = null;
			if (result != null) {

				beanorden = new BeanOrden();
				beanorden.setM_IdOrden(result[0].toString());
				beanorden.setM_IdServicio(result[1].toString());
				// beanorden.setM_FecRegistro((result[2] == null || ((String)
				// result[2]).length() ==
				// 0)?" - ":Fecha.convertFromFechaSQL((String) result[2]));
				beanorden
						.setM_FecInicio((result[3] == null || ((String) result[3])
								.length() == 0) ? " - " : Fecha
								.convertFromFechaSQL((String) result[3]));
				beanorden
						.setM_FecVenc((result[4] == null || ((String) result[4])
								.length() == 0) ? " - " : Fecha
								.convertFromFechaSQL((String) result[4]));
				// beanorden.setM_HoraVigencia((result[5] == null || ((String)
				// result[5]).length() == 0)?" - ":(String) result[5]);
				beanorden
						.setM_DescHora((result[5] == null || ((String) result[5])
								.length() == 0) ? " - " : ((String) result[5])
								.substring(0, 2)
								+ ":" + ((String) result[5]).substring(2, 4));
				beanorden
						.setM_Referencia((result[6] == null || ((String) result[6])
								.length() == 0) ? " - " : (String) result[6]);

				beanorden
						.setM_DescCuenta(((result[7] == null || ((String) result[7])
								.length() == 0) ? "" : (String) result[7])
								+ " - "
								+ ((result[8] == null || ((String) result[8])
										.length() == 0) ? ""
										: (String) result[8]));
				// beanorden.setM_TipoCuenta((result[7] == null || ((String)
				// result[7]).length() == 0)?" - ":(String) result[7]);
				// beanorden.setM_CuentaCargo((result[8] == null || ((String)
				// result[8]).length() == 0)?" - ":(String) result[8]);
				beanorden
						.setM_Empresa((result[9] == null || ((String) result[9])
								.length() == 0) ? " - " : (String) result[9]);
				beanorden
						.setM_Servicio((result[10] == null || ((String) result[10])
								.length() == 0) ? " - " : (String) result[10]);
				beanorden.setM_DescripEstado((result[11] == null) ? " - "
						: result[11].toString());
				beanorden.setM_Items((result[12] == null) ? " 0 " : result[12]
						.toString());
			}
			return beanorden;
		} catch (Exception e) {
			logger.error(e.toString(),e);
			return null;
		}finally{
			session.close();
		}
	}

	// jmoreno 16/11/09 Modificado
	public boolean delete(Map ordenes, char estado_out, List elim) {
		Session session = null;
		boolean band = false;
		
		String fechaActual="";
		
		//instancio la clase de utilidades
		Util dutil = new Util();
		
		fechaActual=dutil.getFechaActualSQL();
		

		
		try {
			Iterator k = ordenes.keySet().iterator();
			while (k.hasNext()) {
				String key = (String) k.next();
				String keys[] = key.split("\\*");
				session = HibernateUtil.getSessionFactory().openSession();
				
				/*
				session.beginTransaction();		
				String sql = "update TaOrden set corEstado = :estadoOut where id.corIdOrden = :idOrden and id.corIdServicioEmpresa = :idServEmp ";
				int updatedEntities = session.createQuery(sql)
						.setParameter("estadoOut", estado_out)
						.setParameter("idOrden",Long.parseLong(keys[0]))
						.setParameter("idServEmp",Long.parseLong(keys[1])).executeUpdate();
				session.getTransaction().commit();
				*/
				
				session.beginTransaction();
				String strQuery = "update TaOrden set corEstado = :estadoOut,fOrFechaCancelacion=:fechaCanc where corIdOrden = :idOrden and corIdServicioEmpresa = :idServEmp ";
				int updatedEntities = session.createSQLQuery(strQuery)
						.setParameter("estadoOut", estado_out)						
						.setParameter("fechaCanc", fechaActual)						
						.setParameter("idOrden",Long.parseLong(keys[0]))
						.setParameter("idServEmp",Long.parseLong(keys[1]))
						.executeUpdate();
				session.getTransaction().commit();
				
				
				if (updatedEntities == 1) {
					elim.add(Long.parseLong(keys[0]));
					band = true;
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(),e);
			return false;
		}finally{
			session.close();
		}

		return band;
	}

	public boolean insert(TaOrden taorden) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			session.saveOrUpdate(taorden);
			session.getTransaction().commit();
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}finally{
			session.close();
		}
		return true;
	}

	public boolean insertReplica(TaOrden taorden) {
		Session session = null;
		try {
			session = HibernateUtil.getReportsSessionFactory().openSession();
			session.beginTransaction();
			session.saveOrUpdate(taorden);
			session.getTransaction().commit();			
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}finally{
			session.close();
		}
		return true;
	}
	

	

	public List selectOrdenesPendAprobacion(List servicio, List user) {
		Session session = null;
		String strQuery;
		Fecha dutil = new Fecha();
		List estado = new ArrayList();
		estado.add(Constantes.HQL_CASH_ESTADO_ORDEN_INGRESADO);
		estado.add(Constantes.HQL_CASH_ESTADO_ORDEN_PENDAUTO);
		strQuery = " select ta.id.corIdOrden, ta.id.corIdServicioEmpresa, ta.id.corIdServicioEmpresa, "
				+ " tm.demNombre, ta.norNumeroCuenta, "
				+ " ta.dorReferencia, "
				+ " ta.forFechaRegistro, "
				+ " ta.forFechaInicio, "
				+ " ta.horHoraInicio, "
				+ " ta.forFechaFin, "
				+ " tp.dsemDescripcion, "
				+ " tx2.dlfDescription, "
				+ // descripcion del estado
				" ta.norNumeroRegistros, "
				+ // cantidad de items
				" ta.dorTipoCuenta, "
				+ " tas.id.cascodigo, "
				+ " tas.id.casidAprobador, "
				+ " ta.norMontoSoles, "
				+ // monto soles
				" ta.norMontoDolares, "
				+ // monto dolares
				" ta.norMontoEuros "
				+ // monto euros
				" from TaOrden ta, TmEmpresa tm, TaServicioxEmpresa tp, TaAprobadorServicio tas "
				+ ", TxListField tx2 "
				+ // descripcion del estado
				" where "
				+ " ta.id.corIdServicioEmpresa in (:servicioList)"
				+ " and tp.csemIdServicioEmpresa = ta.id.corIdServicioEmpresa "
				+ " and tp.csemIdServicioEmpresa = tas.id.csemIdServicioEmpresa "
				+ " and tp.csemEstado = '"
				+ Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO
				+ "' "
				+ " and tas.id.casidAprobador in (:userid) "
				+ " and tas.casestado = :estadoaprobadorid "
				+ " and tp.tmEmpresa.cemIdEmpresa = tm.cemIdEmpresa "
				+ " and tas.id.cascodigo not in (select tao.caoidAprobador from TaAprobacionOrden tao where tao.id.caoidOrden = ta.id.corIdOrden and tao.id.caoidServEmp = ta.id.corIdServicioEmpresa)"
				+ " and tx2.id.clfCode = ta.corEstado "
				+ " and tx2.id.dlfFieldName = :codEstado "
				+ " and ta.corEstado in (:estadoList) "
				+ " and ta.forFechaFin >= :fechaActual "
				+ " order by ta.id.corIdOrden desc";

		List result;
		try {
			session = HibernateUtil.getSessionFactory().openSession();			
			Query query = session.createQuery(strQuery);
			query.setParameterList("servicioList", servicio);
			query.setParameterList("estadoList", estado);
			query.setParameter("estadoaprobadorid",
					Constantes.HQL_CASH_ESTADO_APROBADOR_SERVICIO_HABILITADO);
			query.setParameter("codEstado", "CashEstadoOrden");
			query.setParameter("fechaActual", dutil.getFechaActual("yyyyMMdd"));
			query.setParameterList("userid", user);
			result = query.list();			

			Iterator iter = result.iterator();
			BeanOrden beanorden;
			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				Object[] al_servicio = (Object[]) iter.next();
				beanorden = new BeanOrden();
				beanorden.setM_IdOrden(al_servicio[0].toString());
				beanorden.setM_IdEmpresa(al_servicio[1].toString());
				beanorden.setM_IdServicio(al_servicio[2].toString());

				beanorden.setM_Empresa((String) al_servicio[3]);
				beanorden.setM_CuentaCargo((String) al_servicio[4]);
				beanorden
						.setM_Referencia((al_servicio[5] != null && ((String) al_servicio[5])
								.length() > 0) ? (String) al_servicio[5]
								: " - ");
				beanorden.setM_FecRegistro(Fecha
						.convertFromFechaSQL((String) al_servicio[6]));
				beanorden.setM_FecInicio(Fecha
						.convertFromFechaSQL((String) al_servicio[7]));
				beanorden.setM_HoraVigencia((String) al_servicio[8]);
				beanorden.setM_FecVenc(Fecha
						.convertFromFechaSQL((String) al_servicio[9]));
				beanorden.setM_Servicio((String) al_servicio[10]);
				beanorden.setM_DescripEstado((String) al_servicio[11]);
				beanorden.setM_Items(al_servicio[12].toString());
				beanorden.setM_TipoCuenta((String) al_servicio[13]);
				beanorden.setM_IdAprobador(al_servicio[14].toString());
				beanorden.setM_IdUsuario(al_servicio[15].toString());
				beanorden.setM_ValorSoles((al_servicio[16] == null) ? " 0.00 "
						: Util.formatearMontoNvo(al_servicio[16].toString()));
				beanorden
						.setM_ValorDolares((al_servicio[17] == null) ? " 0.00 "
								: Util.formatearMontoNvo(al_servicio[17]
										.toString()));
				beanorden.setM_ValorEuros((al_servicio[18] == null) ? " 0.00 "
						: Util.formatearMontoNvo(al_servicio[18].toString()));
				alresult.add(beanorden);
				beanorden = null;
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

	// jmoreno 18/11/09
	public List selectOrdenesPendAprobacion(List servicio, String idUser) {
		Session session = null;
		String strQuery;

		Fecha dutil = new Fecha();

		List estado = new ArrayList();
		estado.add(Constantes.HQL_CASH_ESTADO_ORDEN_INGRESADO);
		estado.add(Constantes.HQL_CASH_ESTADO_ORDEN_PENDAUTO);

		strQuery = " select ta.id.corIdOrden, ta.id.corIdServicioEmpresa, ta.id.corIdServicioEmpresa, "
				+ " tm.demNombre, ta.norNumeroCuenta, "
				+ " ta.dorReferencia, "
				+ " ta.forFechaRegistro, "
				+ " ta.forFechaInicio, "
				+ " ta.horHoraInicio, "
				+ " ta.forFechaFin, "
				+ " tp.dsemDescripcion, "
				+ " tx2.dlfDescription, "
				+ // descripcion del estado
				" ta.norNumeroRegistros, "
				+ // cantidad de items
				" ta.dorTipoCuenta, "
				+ " ta.norMontoSoles, "
				+ // monto soles
				" ta.norMontoDolares, "
				+ // monto dolares
				" ta.norMontoEuros "
				+ // monto euros
				" from TaOrden ta, TmEmpresa tm, TaServicioxEmpresa tp,TxListField tx2 "
				+ // descripcion del estado
				" where "
				+ " ta.id.corIdServicioEmpresa in (:servicioList)"
				+ " and tp.csemIdServicioEmpresa = ta.id.corIdServicioEmpresa "
				+ " and tp.csemEstado = '"
				+ Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO
				+ "' "
				+ " and tp.tmEmpresa.cemIdEmpresa = tm.cemIdEmpresa "
				+ " and tx2.id.clfCode = ta.corEstado "
				+ " and tx2.id.dlfFieldName = :codEstado "
				+ " and ta.corEstado in (:estadoList) "
				+ " and ta.forFechaFin >= :fechaActual "
				+ "order by ta.id.corIdOrden desc";

		List result;
		try {
			session = HibernateUtil.getSessionFactory().openSession();			
			Query query = session.createQuery(strQuery);
			query.setParameterList("servicioList", servicio);
			query.setParameterList("estadoList", estado);
			query.setParameter("codEstado", "CashEstadoOrden");
			// se agrega este parametro para el filtro de las ordenes, para que
			// no se apruebe una orden con fecha de vencimiento superior a la
			// actual
			query.setParameter("fechaActual", dutil.getFechaActual("yyyyMMdd"));
			result = query.list();		

			Iterator iter = result.iterator();
			BeanOrden beanorden;
			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				Object[] al_servicio = (Object[]) iter.next();
				beanorden = new BeanOrden();
				beanorden.setM_IdOrden(al_servicio[0].toString());
				beanorden.setM_IdEmpresa(al_servicio[1].toString());
				beanorden.setM_IdServicio(al_servicio[2].toString());
				beanorden.setM_Empresa((String) al_servicio[3]);
				beanorden.setM_CuentaCargo((String) al_servicio[4]);
				beanorden
						.setM_Referencia((al_servicio[5] != null && ((String) al_servicio[5])
								.length() > 0) ? (String) al_servicio[5]
								: " - ");
				beanorden.setM_FecRegistro(Fecha
						.convertFromFechaSQL((String) al_servicio[6]));
				beanorden.setM_FecInicio(Fecha
						.convertFromFechaSQL((String) al_servicio[7]));
				beanorden.setM_HoraVigencia((String) al_servicio[8]);
				beanorden.setM_FecVenc(Fecha
						.convertFromFechaSQL((String) al_servicio[9]));
				beanorden.setM_Servicio((String) al_servicio[10]);
				beanorden.setM_DescripEstado((String) al_servicio[11]);
				beanorden.setM_Items(al_servicio[12].toString());
				beanorden.setM_TipoCuenta((String) al_servicio[13]);
				beanorden.setM_IdAprobador("0");// Para indicar que no existe
												// aprobador
				beanorden.setM_IdUsuario(idUser);
				beanorden.setM_ValorSoles((al_servicio[14] == null) ? " 0.00 "
						: Util.formatearMontoNvo(al_servicio[14].toString()));
				beanorden
						.setM_ValorDolares((al_servicio[15] == null) ? " 0.00 "
								: Util.formatearMontoNvo(al_servicio[15]
										.toString()));
				beanorden.setM_ValorEuros((al_servicio[16] == null) ? " 0.00 "
						: Util.formatearMontoNvo(al_servicio[16].toString()));
				alresult.add(beanorden);
				beanorden = null;
			}
			result = null;
			return alresult;
		}  catch (Exception ex) {
			logger.error(ex.toString(),ex);
			return new ArrayList();
		} finally{
			session.close();
		}
	}

	public List selectOrdenesPendCobro(String proveedor, String empresa,
			List criterios, BeanPaginacion bpag) {
		Session session = null;
		StringBuilder strQuery = new StringBuilder();
		StringBuilder strQueryAux = new StringBuilder();
		for (Iterator it = criterios.iterator(); it.hasNext();) {
			TaDetalleMapaCampos tadc = (TaDetalleMapaCampos) it.next();
			if (tadc.getValor() != null && tadc.getValor().length() > 0) {
				if ("NUM".equalsIgnoreCase(tadc.getDdmtipoDato()))
					strQueryAux.append(" AND tp.")
							.append(tadc.getDdmcampoRef()).append(" ='")
							.append(tadc.getValor()).append("'");
				else if ("ANS".equalsIgnoreCase(tadc.getDdmtipoDato()))
					strQueryAux.append(" AND tp.")
							.append(tadc.getDdmcampoRef()).append(" LIKE '%")
							.append(tadc.getValor()).append("%'");
				else
					strQueryAux.append(" AND tp.")
							.append(tadc.getDdmcampoRef()).append(" ='")
							.append(tadc.getValor()).append("'");
			}
		}

		long indice = 0;
		int regPag = 0;
		strQuery
				.append(" select tp.cdoidOrden,tp.cdoidDetalleOrden,tp.ndomonto,tp.cdomoneda,tp.ddonombre,tp.ndodocumento,tp.ddoreferencia,tp.cDOIdItemDetalle, ");
		strQuery
				.append(" (select dlfDescription from TxListField tl where tl.clfCode = cdomoneda AND tl.dlfFieldName = 'CashTipoMoneda')as moneda ");
		strQuery.append(" from tpdetalleorden tp with(nolock) ");
		strQuery
				.append(" where tp.cDOIdOrden in (select cOrIdOrden from taOrden where cOrIdServicioEmpresa in ");
		strQuery
				.append(" (select cSEmIdServicioEmpresa from taServicioxEmpresa where cSEmIdEmpresa = :rucprov ");
		strQuery
				.append(" and cSEmIdServicio = '05' and (select cEmEstado from tmempresa where cEmIdEmpresa = cSEmIdEmpresa ) = '0' ");
		strQuery
				.append(" and cSEmEstado ='0') and cOrEstado in ('1','6')) and tp.cDOEstado = '0' ");
		String condicion = "";
		if (Constantes.TIPO_PAG_SIGUIENTE.equalsIgnoreCase(bpag.getM_tipo())
				|| Constantes.TIPO_PAG_PRIMERO.equalsIgnoreCase(bpag
						.getM_tipo())) {
			condicion = strQueryAux.toString()
					+ " AND tp.cdoidItemDetalle > :indice order by tp.cdoidItemDetalle asc";
		} else if (Constantes.TIPO_PAG_ANTERIOR.equalsIgnoreCase(bpag
				.getM_tipo())) {
			condicion = strQueryAux.toString()
					+ " AND tp.cdoidItemDetalle < :indice order by tp.cdoidItemDetalle desc";
		} else {
			condicion = strQueryAux.toString()
					+ " order by tp.cdoidItemDetalle desc";
		}
		strQuery.append(condicion);
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
		try {
			List result;
			session = HibernateUtil.getSessionFactory().openSession();		
			if (Constantes.TIPO_PAG_ULTIMO.equalsIgnoreCase(bpag.getM_tipo())) {
				result = session.createSQLQuery(strQuery.toString())
						.setParameter("rucprov", proveedor).setMaxResults(
								regPag).list();
			} else {
				result = session.createSQLQuery(strQuery.toString())
						.setParameter("rucprov", proveedor).setParameter(
								"indice", indice).setMaxResults(regPag).list();
			}			
			Iterator iter = result.iterator();
			ArrayList resultado = new ArrayList();
			while (iter.hasNext()) {
				Object[] det_orden = (Object[]) iter.next();
				BeanDetalleOrden nvo = new BeanDetalleOrden();
				nvo.setM_IdOrden(det_orden[0].toString());
				nvo.setM_IdDetalleOrden(det_orden[1].toString());
				if (det_orden[2] != null) {
					BigDecimal monto = new BigDecimal(det_orden[2].toString());
					nvo.setM_BigDecMonto(monto);
					nvo.setM_Monto(Util.formatearMontoNvo(det_orden[2]
							.toString()));
				} else {
					BigDecimal monto = new BigDecimal(0);
					nvo.setM_BigDecMonto(monto);
					nvo.setM_Monto(monto.toString());
				}
				nvo.setM_IdTipoMoneda(det_orden[3].toString());
				if (det_orden[4] != null) {
					nvo.setM_NomCliente(det_orden[4].toString());
				} else {
					nvo.setM_NomCliente("");
				}
				if (det_orden[5] != null) {
					nvo.setM_NumRecibo(det_orden[5].toString());
				} else {
					nvo.setM_NumRecibo("");
				}
				if (det_orden[6] != null) {
					nvo.setM_Descripcion(det_orden[6].toString());
				} else {
					nvo.setM_Descripcion("");
				}
				nvo.setM_ItemDetalle(Long.parseLong(det_orden[7].toString()));
				if (det_orden[8] != null) {
					nvo.setM_DescTipoMoneda(det_orden[8].toString());
				} else {
					nvo.setM_DescTipoMoneda("");
				}
				resultado.add(nvo);
			}
			return resultado;
		} catch (Exception ex) {
			logger.error(ex.toString(),ex);
			return new ArrayList();
		}finally{
			session.close();
		}
	}
	

	

	// Update tpDetalleOrden
	public boolean update(long orden, long servicio, BigDecimal montoSoles,
			BigDecimal montoDolares, BigDecimal montoEuros, int num) {
		Session session = null;
		try {
			String update = "update taOrden set nOrMontoSoles = ?, nOrMontoDolares = ?,norMontoEuros = ?, nOrNumeroRegistros = ? "
					+ "where cOrIdOrden = ? and cOrIdServicioEmpresa = ?";
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Query queryObject = session.createSQLQuery(update);
			queryObject.setBigDecimal(0, montoSoles);
			queryObject.setBigDecimal(1, montoDolares);
			queryObject.setBigDecimal(2, montoEuros);
			queryObject.setInteger(3, num);
			queryObject.setLong(4, orden);
			queryObject.setLong(5, servicio);
			int ires = queryObject.executeUpdate();
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return false;
		}
		finally{
			session.close();
		}

	}
		

	public String getCodigosRptaIbs(String idOrden) {
		Session session = null;
		String strQuery;
		List result;
		String respuesta = "";
		try {
			strQuery = " select ta.cOrCodRptaIBSSoles,"
					+ " (select tx.dRsDescription from TxResult tx where ta.cOrCodRptaIBSSoles = tx.cRsResultExt) as descripSoles,"
					+ " ta.cOrCodRptaIBSDolares,"
					+ " (select tx.dRsDescription from TxResult tx where ta.cOrCodRptaIBSDolares= tx.cRsResultExt)as descripDolares,"
					+ " ta.cOrCodRptaIBSEuros,"
					+ " (select tx.dRsDescription from TxResult tx where ta.cOrCodRptaIBSEuros = tx.cRsResultExt)as descripEuros"
					+ " from taorden ta where cOrIdOrden = :idOrden";

			session = HibernateUtil.getSessionFactory().openSession();			
			result = session.createSQLQuery(strQuery).setParameter("idOrden",
					Integer.parseInt(idOrden)).list();
			
			Iterator iter = result.iterator();
			while (iter.hasNext()) {
				Object[] var_descrip = (Object[]) iter.next();
				if (var_descrip[0] != null && var_descrip[1] != null
						&& !(var_descrip[0].toString().trim().equals(""))
						&& !(var_descrip[1].toString().trim().equals(""))) {
					respuesta = "Soles:" + var_descrip[0].toString() + "-"
							+ var_descrip[1].toString() + "\n";
				}
				if (var_descrip[2] != null && var_descrip[3] != null
						&& !(var_descrip[2].toString().trim().equals(""))
						&& !(var_descrip[3].toString().trim().equals(""))) {
					respuesta = respuesta + "Dólares:"
							+ var_descrip[2].toString() + "-"
							+ var_descrip[3].toString() + "\n";
				}
				if (var_descrip[4] != null && var_descrip[5] != null
						&& !(var_descrip[4].toString().trim().equals(""))
						&& !(var_descrip[5].toString().trim().equals(""))) {
					respuesta = respuesta + "Euros:"
							+ var_descrip[4].toString() + "-"
							+ var_descrip[5].toString();
				}
			}
		} catch (Exception e) {
			logger.error(e.toString(),e);
			return "";
		}
		finally{
			session.close();
		}
		return respuesta;
	}

	public List getConsultaOrdEstado(String idServEmp, String estado,
			String fecini, String fecfin, String referencia,
			String contrapartida, BeanTotalesConsMov beanTotales) {
		String strSQLQuery = "select ta.nombreEmpresa, ta.nombreServicio, ta.cOrIdOrden, ta.nombreEstadoOrden, tp.cDOMoneda, "
				+ " count(distinct(tp.cDOIdDetalleOrden)), sum(tp.nDOMonto), sum(tp.nDOMontoVentanilla) from taOrdenRep ta join "
				+ " tpDetalleOrdenTrx tp on (ta.cOrIdOrden=tp.cDOIdOrden and ta.cOrIdServicioEmpresa=tp.cDOIdServicioEmpresa) "
				+ " where ta.cOrIdServicioEmpresa=:IdServEmp and ta.fOrFechaRegistro>=:fechaIni and ta.fOrFechaRegistro<=:fechaFin ";
		if (estado != null && estado.length() > 0) {
			strSQLQuery += " and tp.cdoestado = :estadoid ";
		}
		if (referencia != null && referencia.length() > 0) {
			strSQLQuery += " and tp.ddoreferencia like :referencia ";
		}
		if (contrapartida != null && contrapartida.length() > 0) {
			strSQLQuery += " and tp.ddonomContrapartida = :contrapartida ";
		}
		strSQLQuery += " group by ta.cOrIdOrden , tp.cDOMoneda , ta.nombreEmpresa , ta.nombreServicio , ta.nombreEstadoOrden ";
		Session session=null;
		try {
			session = HibernateUtil.getReportsSessionFactory()
					.openSession();			
			Query query = session.createSQLQuery(strSQLQuery).setParameter(
					"IdServEmp", Long.parseLong(idServEmp)).setParameter(
					"fechaIni", Fecha.convertToFechaSQL(fecini)).setParameter(
					"fechaFin", Fecha.convertToFechaSQL(fecfin));
			if (estado != null && estado.length() > 0) {
				query.setParameter("estadoid", estado);
			}
			if (referencia != null && referencia.length() > 0) {
				query.setParameter("referencia", "%" + referencia + "%");
			}
			if (contrapartida != null && contrapartida.length() > 0) {
				query.setParameter("contrapartida", contrapartida);
			}
			return query.list();
		}
		finally{
			session.close();
		}
	}
	

	@Override
	public String obtenerTipoOrden(String orden, String servicio) {
		String tipo = "";
		Session session = null;
		Query query = null;
		try {
			logger.info("Orden:" + orden);
			logger.info("Servicio: " + servicio);
			String sql = "SELECT ta.taServicioxEmpresa.tmServicio.csrTipo FROM TaOrden ta WHERE ta.id.corIdOrden=:orden and ta.id.corIdServicioEmpresa=:servicio";
			session = HibernateUtil.getSessionFactory().openSession();			
			query = session.createQuery(sql);
			query.setParameter("orden", Long.parseLong(orden));
			query.setParameter("servicio", Long.parseLong(servicio));
			tipo = (String) query.uniqueResult();			
		} catch (Exception e) {
			logger.error(e);
		}finally{
			session.close();
		}

		return tipo;
	}

	@Override
	public String obtenerFechaOrden(String idOrden, String idServEmp)
			throws Exception {
		String fecha = null;
		Session session = null;
		Query query = null;
		try {
			String sql = "SELECT ta.forFechaInicio FROM TaOrden ta WHERE ta.id.corIdOrden=:orden and ta.id.corIdServicioEmpresa=:servicio";
			session = HibernateUtil.getSessionFactory().openSession();			
			query = session.createQuery(sql);
			query.setParameter("orden", Long.parseLong(idOrden));
			query.setParameter("servicio", Long.parseLong(idServEmp));
			fecha = (String) query.uniqueResult();			
		} catch (Exception e) {			
			logger.error(e);
		}finally{
			session.close();
		}

		return fecha;
	}

	@Override
	public boolean cancelarOrden(int idOrden, String idServEmp) {
		boolean retorno = false;
		Session session = null;
		String tipoOrden = "";
		//se agrega la variable fechaActual
		String fechaActual="";
		
		//instancio la clase de utilidades
		Util dutil = new Util();		
		//obtengo la fecha actual
		fechaActual=dutil.getFechaActualSQL();
		

		tipoOrden = obtenerTipoOrden(String.valueOf(idOrden), idServEmp);

		if (tipoOrden.equals("01")) {// orden de pagos

			try {
				String update = "update taOrden set cOrEstado = ?, fOrFechaCancelacion = ?  where cOrIdOrden = ? and ( cOrEstado = ?  or corEstado= ? ) ";

				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();
				Query queryObject = session.createSQLQuery(update);
				queryObject.setString(0, String.valueOf(Constantes.HQL_CASH_ESTADO_ORDEN_VENCIDO));
				//se agregra la fecha de cancelacion
				queryObject.setString(1,fechaActual)
				;
				queryObject.setInteger(2, idOrden);
				queryObject.setString(3,String.valueOf(Constantes.HQL_CASH_ESTADO_ORDEN_PROCESADO_PARCIAL));
				queryObject.setString(4, String.valueOf(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO));

				int ires = queryObject.executeUpdate(); 

				session.getTransaction().commit();
								
				if (ires > 0) {
					logger.info(ires + ":  Se Actualizo Orden con Id "+ idOrden + " actualizada a estado: vencido ");	
					
					updateOrdenRep(Long.parseLong(idServEmp),idOrden,String.valueOf(Constantes.HQL_CASH_ESTADO_ORDEN_VENCIDO));
										
					retorno = true;
				}
				

			} catch (Exception e) {
				logger.error(e.toString(),e);
				retorno = false;
			}finally{
				session.close();
			}

		}

		if (tipoOrden.equals("02")) {// orden de cobros

			try {
				String update = "update taOrden set cOrEstado = ?, fOrFechaCancelacion = ? where cOrIdOrden = ? and ( cOrEstado = ? or corEstado= ? )";

				session = HibernateUtil.getSessionFactory().openSession();
				session.beginTransaction();

				Query queryObject = session.createSQLQuery(update);
				queryObject.setString(0, String.valueOf(Constantes.HQL_CASH_ESTADO_ORDEN_REVOCADO));
				//se agregra la fecha de cancelacion
				queryObject.setString(1, fechaActual);					
				queryObject.setInteger(2, idOrden);
				queryObject.setString(3,String.valueOf(Constantes.HQL_CASH_ESTADO_ORDEN_PROCESADO_PARCIAL));
				queryObject.setString(4, String.valueOf(Constantes.HQL_CASH_ESTADO_ORDEN_APROBADO));

				int ires = queryObject.executeUpdate();
								
				session.getTransaction().commit();
				
				if (ires > 0) {
					
					updateOrdenRep(Long.parseLong(idServEmp),idOrden,String.valueOf(Constantes.HQL_CASH_ESTADO_ORDEN_REVOCADO));
					
					retorno = true;
				}				
				

			} catch (Exception e) {
				logger.error(e.toString(),e);
				retorno = false;
			}finally{
				session.close();
			}
		}
		return retorno;
	}

	@Override
	public String validarCancelarOrden(String idOrden, String idServEmp) {
		Session session = null;
		String resultado = "";
		List result = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			Query query = session.createSQLQuery(
					"EXEC SPU_VALIDA_CANCELAR_ORDEN :codOrden, :codIdServEmp")
					.setParameter("codOrden", idOrden).setParameter(
							"codIdServEmp", idServEmp);

			result = query.list();
			session.getTransaction().commit();

			if (result.size() > 0) {
				resultado = result.get(0).toString();
			}

		} catch (Exception ex) {
			logger.error(ex.toString());
		} finally {
			result = null;
			session.close();
		}
		return resultado;
	}

	private TaServicioxEmpresaDao daoServicioEmpresa = new TaServicioxEmpresaDaoHibernate();

	public TaOrden getOrdenRep(long idOrden, long idServicioEmpresa)
			throws Exception {
		TaOrdenId pk = new TaOrdenId(idOrden, idServicioEmpresa);		
		TaOrden orden = getOrdenRep(pk);
		Session session=null;
		List<TpDetalleOrden> detalles=null;
		try {
			session = HibernateUtil.getReportsSessionFactory().openSession();
			String sql = "SELECT obj FROM TpDetalleOrden obj WHERE obj.id.cdoidOrden=:idOrden";
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("idOrden", idOrden);
			detalles = EntityDAO.find(session, TpDetalleOrden.class, sql,
					parametros);
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}		
		orden.setTpDetalleOrdens(detalles);
		orden.setTaServicioxEmpresa(daoServicioEmpresa.getServicioEmpresa(idServicioEmpresa));
		return orden;
	}
	
	private TaOrden getOrdenRep(TaOrdenId pk)throws Exception {		
		TaOrden orden=null;
		Session session=null;
		try {
			session = HibernateUtil.getReportsSessionFactory()
					.openSession();
			orden = (TaOrden) session.get(TaOrden.class, pk);
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		return orden;
	}

		
	public List<BeanOrden> buscarOrdenesReferencia(List<Long> servicios,
			String estado, String fechaInicio, String fechaFin,
			String referencia, int inicio, int nroRegistros) throws Exception {

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("codEstado", "CashEstadoOrden");
		parametros.put("servicioList", servicios);
		StringBuilder sb = new StringBuilder("select distinct ");
		sb = sb
				.append("ta.id.corIdOrden, ta.id.corIdServicioEmpresa, ta.id.corIdServicioEmpresa,  ");
		sb = sb
				.append("ta.norNumeroCuenta,  ta.dorReferencia, ta.forFechaInicio,  ");
		sb = sb
				.append("ta.forFechaFin,  tp.dsemDescripcion,  tx2.dlfDescription,  ");
		sb = sb
				.append("ta.norNumeroRegistros,  ta.norMontoSoles,  ta.norMontoDolares,  ");
		sb = sb
				.append("ta.norMontoEuros,  tp.tmServicio.csrTipo,  ta.corEstado  ");
		sb = sb
				.append("FROM TpDetalleOrden td,TaOrden ta, TaServicioxEmpresa tp, TxListField tx2  ");
		sb = sb.append("WHERE  ta.id.corIdServicioEmpresa in (:servicioList) ");
		sb = sb
				.append("and tp.csemIdServicioEmpresa = ta.id.corIdServicioEmpresa  ");
		sb = sb
				.append("and td.id.cdoidOrden = ta.id.corIdOrden  and  tx2.id.clfCode = ta.corEstado  ");
		sb = sb.append("and tx2.id.dlfFieldName = :codEstado  ");

		if (estado != null && estado.length() > 0) {
			sb = sb.append(" and ta.corEstado = :estadoid ");
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			sb = sb.append(" and ta.forFechaInicio <= :fechafin ");
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			sb = sb.append(" and ta.forFechaInicio >= :fechaini ");
		}
		if (referencia != null && referencia.length() > 0) {
			sb = sb.append(" and (ta.dorReferencia like :referencia");
			sb = sb
					.append("  or td.ddoreferencia like :ref or td.ddocontrapartida like :ref)");
		}
		sb = sb.append("order by ta.id.corIdOrden desc");

		if (estado != null && estado.length() > 0) {
			parametros.put("estadoid", estado);
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			parametros.put("fechaini", Fecha.convertToFechaSQL(fechaInicio));
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			parametros.put("fechafin", Fecha.convertToFechaSQL(fechaFin));
		}

		if (referencia != null && referencia.length() > 0) {
			parametros.put("ref", "%" + referencia + "%");
			parametros.put("referencia", "%" + referencia + "%");
		}

		List<Object[]> result = new ArrayList<Object[]>();
		Iterator<Object[]> iter;
		BeanOrden beanorden;
		List<BeanOrden> alresult = new ArrayList<BeanOrden>();

		result = EntityDAO
				.find(sb.toString(), parametros, inicio, nroRegistros);
		iter = result.iterator();
		int i = 0;
		while (iter.hasNext()) {
			i = 0;
			Object[] al_servicio = (Object[]) iter.next();
			beanorden = new BeanOrden();
			if (al_servicio[i] != null) {
				beanorden.setM_IdOrden(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_IdEmpresa(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_IdServicio(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_CuentaCargo(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_Referencia(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_FecInicio(Fecha
						.convertFromFechaSQL(al_servicio[i].toString()));
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_FecVenc(Fecha.convertFromFechaSQL(al_servicio[i]
						.toString()));
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_Servicio(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_DescripEstado(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_Items(al_servicio[i].toString());
			}
			i++;
			beanorden.setM_ValorSoles((al_servicio[i] == null) ? " 0.00 "
					: Util.formatearMontoNvo(al_servicio[i].toString()));
			i++;
			beanorden.setM_ValorDolares((al_servicio[i] == null) ? " 0.00 "
					: Util.formatearMontoNvo(al_servicio[i].toString()));
			i++;
			beanorden.setM_ValorEuros((al_servicio[i] == null) ? " 0.00 "
					: Util.formatearMontoNvo(al_servicio[i].toString()));
			i++;
			if (al_servicio[i] != null) {
				if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equals(al_servicio[i]
						.toString())
						|| Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV
								.equals(al_servicio[i].toString())) {
					i++;
					if (al_servicio[i] != null) {
						if (Constantes.HQL_CASH_ESTADO_ORDEN_PROCESADO_PARCIALMENTE
								.compareTo(al_servicio[i].toString()) == 0
								|| Constantes.HQL_CASH_ESTADO_ORDEN_RECHAZADO
										.compareTo(al_servicio[i].toString()) == 0
								|| Constantes.HQL_CASH_ESTADO_ORDEN_ERRADO
										.compareTo(al_servicio[i].toString()) == 0
								|| Constantes.HQL_CASH_ESTADO_ORDEN_CERRADO
										.compareTo(al_servicio[i].toString()) == 0) {
							beanorden.setM_TipoServicio("1");// para indicar que
							// si se tiene
							// que mostrar
							// el error(si
							// es que lo
							// hubiera)
						} else {
							beanorden.setM_TipoServicio("0");
						}
					} else {
						beanorden.setM_TipoServicio("0");
					}
				} else {
					beanorden.setM_TipoServicio("0");
				}
			}
			alresult.add(beanorden);
		}

		return alresult;
	}

	@Override
	public int contarOrdenesReferencia(List<Long> servicios, String estado,
			String fechaInicio, String fechaFin, String referencia)
			throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("servicioList", servicios);

		StringBuilder sb = new StringBuilder(
				"SELECT count(distinct ta.cOrIdOrden) FROM TaOrden ta ");
		sb = sb
				.append("INNER JOIN TpDetalleOrden td ON ta.cOrIdOrden = td.cDOIdOrden ");
		sb = sb.append("WHERE ta.cOrIdServicioEmpresa in (:servicioList) ");

		if (estado != null && estado.length() > 0) {
			sb = sb.append(" and ta.corEstado = :estadoid ");
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			sb = sb.append(" and ta.forFechaInicio <= :fechafin ");
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			sb = sb.append(" and ta.forFechaInicio >= :fechaini ");
		}
		if (referencia != null && referencia.length() > 0) {
			sb = sb.append(" and (ta.dorReferencia like :referencia");
			sb = sb
					.append("  or td.ddoReferencia like :ref or td.ddocontrapartida like :ref)");
		}

		if (estado != null && estado.length() > 0) {
			parametros.put("estadoid", estado);
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			parametros.put("fechaini", Fecha.convertToFechaSQL(fechaInicio));
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			parametros.put("fechafin", Fecha.convertToFechaSQL(fechaFin));
		}

		if (referencia != null && referencia.length() > 0) {
			parametros.put("ref", "%" + referencia + "%");
			parametros.put("referencia", "%" + referencia + "%");
		}

		return EntityDAO.countSQL(sb.toString(), parametros);
	}

	public List<BeanOrden> buscarOrdenesReferenciaNoTrx(List<Long> servicios,
			String estado, String fechaInicio, String fechaFin,
			String referencia, int inicio, int nroRegistros) throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("servicioList", servicios);
		StringBuilder sb = new StringBuilder("select distinct ");
		sb = sb
				.append("cOrIdOrden,cOrIdServicioEmpresa,nombreServicio,norNumeroCuenta, ");
		sb = sb
				.append("dorReferencia, forFechaInicio,forFechaFin,cOrEstado,nombreEstadoOrden, ");
		sb = sb.append("norNumeroRegistros,norMontoSoles,  norMontoDolares ");
		sb = sb.append("FROM taOrdenRep  ");
		sb = sb
				.append("INNER JOIN tpDetalleOrdenNoTrx ON cOrIdOrden=cDOIdOrden ");
		sb = sb.append("AND cOrIdServicioEmpresa=cDOIdServicioEmpresa ");
		sb = sb.append("WHERE  cOrIdServicioEmpresa in (:servicioList) ");

		if (estado != null && estado.length() > 0) {
			sb = sb.append(" and corEstado = :estadoid ");
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			sb = sb.append(" and forFechaInicio <= :fechafin ");
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			sb = sb.append(" and forFechaInicio >= :fechaini ");
		}
		if (referencia != null && referencia.length() > 0) {
			sb = sb.append(" and (dorReferencia like :referencia");
			sb = sb
					.append("  or ddoreferencia like :ref or ddocontrapartida like :ref)");
		}
		sb = sb.append("order by corIdOrden desc");

		if (estado != null && estado.length() > 0) {
			parametros.put("estadoid", estado);
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			parametros.put("fechaini", Fecha.convertToFechaSQL(fechaInicio));
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			parametros.put("fechafin", Fecha.convertToFechaSQL(fechaFin));
		}

		if (referencia != null && referencia.length() > 0) {
			parametros.put("ref", "%" + referencia + "%");
			parametros.put("referencia", "%" + referencia + "%");
		}

		List<Object[]> result = new ArrayList<Object[]>();
		Iterator<Object[]> iter;
		BeanOrden beanorden;
		List<BeanOrden> alresult = new ArrayList<BeanOrden>();
		
		Session session=null;
		try {
			session = HibernateUtil.getReportsSessionFactory()
					.openSession();			
			Query query = EntityDAO.createQuery(session, TipoConsulta.SQL, sb
					.toString(), parametros, inicio, nroRegistros);
			result = query.list();
		} finally{
			session.close();
		}

		iter = result.iterator();
		int i = 0;
		while (iter.hasNext()) {
			i = 0;
			Object[] al_servicio = (Object[]) iter.next();
			beanorden = new BeanOrden();
			if (al_servicio[i] != null) {
				beanorden.setM_IdOrden(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_IdServicio(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_Servicio(al_servicio[i].toString());
			} else {
				beanorden.setM_Servicio("");
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_CuentaCargo(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_Referencia(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_FecInicio(Fecha
						.convertFromFechaSQL(al_servicio[i].toString()));
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_FecVenc(Fecha.convertFromFechaSQL(al_servicio[i]
						.toString()));
			}
			i++;
			if (al_servicio[i] != null) {
				String tipoOrden = obtenerTipoOrden(beanorden.getM_IdOrden(),
						beanorden.getM_IdServicio());
				if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equals(tipoOrden)
						|| Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV
								.equals(tipoOrden)) {
					if (Constantes.HQL_CASH_ESTADO_ORDEN_PROCESADO_PARCIALMENTE
							.compareTo(al_servicio[i].toString()) == 0
							|| Constantes.HQL_CASH_ESTADO_ORDEN_RECHAZADO
									.compareTo(al_servicio[i].toString()) == 0
							|| Constantes.HQL_CASH_ESTADO_ORDEN_ERRADO
									.compareTo(al_servicio[i].toString()) == 0
							|| Constantes.HQL_CASH_ESTADO_ORDEN_CERRADO
									.compareTo(al_servicio[i].toString()) == 0) {
						beanorden.setM_TipoServicio("1");
					} else {
						beanorden.setM_TipoServicio("0");
					}
				} else {
					beanorden.setM_TipoServicio("0");
				}
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_DescripEstado(al_servicio[i].toString());
			} else {
				beanorden.setM_DescripEstado("");
			}
			i++;

			if (al_servicio[i] != null) {
				beanorden.setM_Items(al_servicio[i].toString());
			}
			i++;
			beanorden.setM_ValorSoles((al_servicio[i] == null) ? " 0.00 "
					: Util.formatearMontoNvo(al_servicio[i].toString()));
			i++;
			beanorden.setM_ValorDolares((al_servicio[i] == null) ? " 0.00 "
					: Util.formatearMontoNvo(al_servicio[i].toString()));
			i++;

			alresult.add(beanorden);
		}

		return alresult;
	}

	@Override
	public List<BeanOrden> buscarOrdenesReferenciaTrx(List<Long> servicios,
			String estado, String fechaInicio, String fechaFin,
			String referencia, int inicio, int nroRegistros) throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("servicioList", servicios);
		StringBuilder sb = new StringBuilder("select distinct ");
		sb = sb
				.append("cOrIdOrden,cOrIdServicioEmpresa,nombreServicio,norNumeroCuenta, ");
		sb = sb
				.append("dorReferencia, forFechaInicio,forFechaFin,cOrEstado,nombreEstadoOrden, ");
		sb = sb.append("norNumeroRegistros,norMontoSoles,  norMontoDolares ");
		sb = sb.append("FROM taOrdenRep  ");
		sb = sb
				.append("INNER JOIN tpDetalleOrdenTrx ON cOrIdOrden=cDOIdOrden ");
		sb = sb.append("AND cOrIdServicioEmpresa=cDOIdServicioEmpresa ");
		sb = sb.append("WHERE  cOrIdServicioEmpresa in (:servicioList) ");

		if (estado != null && estado.length() > 0) {
			sb = sb.append(" and corEstado = :estadoid ");
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			sb = sb.append(" and forFechaInicio <= :fechafin ");
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			sb = sb.append(" and forFechaInicio >= :fechaini ");
		}
		if (referencia != null && referencia.length() > 0) {
			sb = sb.append(" and (dorReferencia like :referencia");
			sb = sb
					.append("  or ddoreferencia like :ref or ddocontrapartida like :ref)");
		}
		sb = sb.append("order by corIdOrden desc");

		if (estado != null && estado.length() > 0) {
			parametros.put("estadoid", estado);
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			parametros.put("fechaini", Fecha.convertToFechaSQL(fechaInicio));
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			parametros.put("fechafin", Fecha.convertToFechaSQL(fechaFin));
		}

		if (referencia != null && referencia.length() > 0) {
			parametros.put("ref", "%" + referencia + "%");
			parametros.put("referencia", "%" + referencia + "%");
		}

		List<Object[]> result = new ArrayList<Object[]>();
		Iterator<Object[]> iter;
		BeanOrden beanorden;
		List<BeanOrden> alresult = new ArrayList<BeanOrden>();
		Session session=null;
		try {
			session = HibernateUtil.getReportsSessionFactory()
					.openSession();		
			Query query = EntityDAO.createQuery(session, TipoConsulta.SQL, sb
					.toString(), parametros, inicio, nroRegistros);
			result = query.list();
		} finally{
			session.close();
		}
		
		iter = result.iterator();
		int i = 0;
		while (iter.hasNext()) {
			i = 0;
			Object[] al_servicio = (Object[]) iter.next();
			beanorden = new BeanOrden();
			if (al_servicio[i] != null) {
				beanorden.setM_IdOrden(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_IdServicio(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_Servicio(al_servicio[i].toString());
			} else {
				beanorden.setM_Servicio("");
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_CuentaCargo(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_Referencia(al_servicio[i].toString());
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_FecInicio(Fecha
						.convertFromFechaSQL(al_servicio[i].toString()));
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_FecVenc(Fecha.convertFromFechaSQL(al_servicio[i]
						.toString()));
			}
			i++;
			if (al_servicio[i] != null) {
				String tipoOrden = obtenerTipoOrden(beanorden.getM_IdOrden(),
						beanorden.getM_IdServicio());
				if (Constantes.TX_CASH_TIPO_SERVICIO_PAGO.equals(tipoOrden)
						|| Constantes.TX_CASH_TIPO_SERVICIO_PAGOSERV
								.equals(tipoOrden)) {
					if (Constantes.HQL_CASH_ESTADO_ORDEN_PROCESADO_PARCIALMENTE
							.compareTo(al_servicio[i].toString()) == 0
							|| Constantes.HQL_CASH_ESTADO_ORDEN_RECHAZADO
									.compareTo(al_servicio[i].toString()) == 0
							|| Constantes.HQL_CASH_ESTADO_ORDEN_ERRADO
									.compareTo(al_servicio[i].toString()) == 0
							|| Constantes.HQL_CASH_ESTADO_ORDEN_CERRADO
									.compareTo(al_servicio[i].toString()) == 0) {
						beanorden.setM_TipoServicio("1");
					} else {
						beanorden.setM_TipoServicio("0");
					}
				} else {
					beanorden.setM_TipoServicio("0");
				}
			}
			i++;
			if (al_servicio[i] != null) {
				beanorden.setM_DescripEstado(al_servicio[i].toString());
			} else {
				beanorden.setM_DescripEstado("");
			}
			i++;

			if (al_servicio[i] != null) {
				beanorden.setM_Items(al_servicio[i].toString());
			}
			i++;
			beanorden.setM_ValorSoles((al_servicio[i] == null) ? " 0.00 "
					: Util.formatearMontoNvo(al_servicio[i].toString()));
			i++;
			beanorden.setM_ValorDolares((al_servicio[i] == null) ? " 0.00 "
					: Util.formatearMontoNvo(al_servicio[i].toString()));
			i++;

			alresult.add(beanorden);
		}

		return alresult;
	}

	@Override
	public int contarOrdenesReferenciaNoTrx(List<Long> servicios,
			String estado, String fechaInicio, String fechaFin,
			String referencia) throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("servicioList", servicios);

		StringBuilder sb = new StringBuilder(
				"SELECT count(distinct cOrIdOrden) FROM taOrdenRep ");
		sb = sb
				.append("INNER JOIN tpDetalleOrdenNoTrx  ON cOrIdOrden=cDOIdOrden ");
		sb = sb.append("AND cOrIdServicioEmpresa=cDOIdServicioEmpresa ");
		sb = sb.append("WHERE cOrIdServicioEmpresa in (:servicioList) ");

		if (estado != null && estado.length() > 0) {
			sb = sb.append(" and corEstado = :estadoid ");
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			sb = sb.append(" and forFechaInicio <= :fechafin ");
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			sb = sb.append(" and forFechaInicio >= :fechaini ");
		}
		if (referencia != null && referencia.length() > 0) {
			sb = sb.append(" and (dorReferencia like :referencia");
			sb = sb
					.append("  or ddoReferencia like :ref or ddocontrapartida like :ref)");
		}

		if (estado != null && estado.length() > 0) {
			parametros.put("estadoid", estado);
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			parametros.put("fechaini", Fecha.convertToFechaSQL(fechaInicio));
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			parametros.put("fechafin", Fecha.convertToFechaSQL(fechaFin));
		}

		if (referencia != null && referencia.length() > 0) {
			parametros.put("ref", "%" + referencia + "%");
			parametros.put("referencia", "%" + referencia + "%");
		}
		Session session= null;
		Integer obj;
		try {
			session = HibernateUtil.getReportsSessionFactory()
					.openSession();	
			Query query = EntityDAO.createQuery(session, TipoConsulta.SQL, sb
					.toString(), parametros);
			obj = (Integer) query.uniqueResult();
		} finally {
			session.close();
		}
		return obj.intValue();
	}

	@Override
	public int contarOrdenesReferenciaTrx(List<Long> servicios, String estado,
			String fechaInicio, String fechaFin, String referencia)
			throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("servicioList", servicios);

		StringBuilder sb = new StringBuilder(
				"SELECT count(distinct cOrIdOrden) FROM taOrdenRep ");
		sb = sb
				.append("INNER JOIN tpDetalleOrdenTrx  ON cOrIdOrden=cDOIdOrden ");
		sb = sb.append("AND cOrIdServicioEmpresa=cDOIdServicioEmpresa ");
		sb = sb.append("WHERE cOrIdServicioEmpresa in (:servicioList) ");

		if (estado != null && estado.length() > 0) {
			sb = sb.append(" and corEstado = :estadoid ");
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			sb = sb.append(" and forFechaInicio <= :fechafin ");
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			sb = sb.append(" and forFechaInicio >= :fechaini ");
		}
		if (referencia != null && referencia.length() > 0) {
			sb = sb.append(" and (dorReferencia like :referencia");
			sb = sb
					.append("  or ddoReferencia like :ref or ddocontrapartida like :ref)");
		}

		if (estado != null && estado.length() > 0) {
			parametros.put("estadoid", estado);
		}
		if (fechaInicio != null && fechaInicio.length() > 0) {
			parametros.put("fechaini", Fecha.convertToFechaSQL(fechaInicio));
		}
		if (fechaFin != null && fechaFin.length() > 0) {
			parametros.put("fechafin", Fecha.convertToFechaSQL(fechaFin));
		}

		if (referencia != null && referencia.length() > 0) {
			parametros.put("ref", "%" + referencia + "%");
			parametros.put("referencia", "%" + referencia + "%");
		}
		Session session=null;
		
		Integer obj;
		try {
			session = HibernateUtil.getReportsSessionFactory()
					.openSession();
			
			Query query = EntityDAO.createQuery(session, TipoConsulta.SQL, sb
					.toString(), parametros);

			obj = (Integer) query.uniqueResult();
		} finally {
			session.close();
		}

		return obj.intValue();
	}
	
	
	public void ejecutaBuzonesID(long idenvio,long idOrden, long idServEmp){    	
	   	 Session session = null; 
	        try{
	            session = HibernateUtil.getSessionFactory().openSession();
	            session.beginTransaction();
	            Query query = session.createSQLQuery("EXEC BFSP_PORTAL_BUZON_ORDENES_ID :codenvio, :codorden, :codservemp")
	            .setParameter("codenvio", idenvio)
	            .setParameter("codorden", idOrden)
	            .setParameter("codservemp", idServEmp);	            
	            query.executeUpdate();	            
	            session.getTransaction().commit();        	         	           
	        }
	        catch(Exception ex){
	            logger.error(ex.toString(),ex);	            
	        }finally{
	        	session.close();
	        }
	   }

	@Override
	public void guardaTransfConsultas(long idServEmp, long idOrden,
			long idDetalle) {	
		 Session session = null; 
	        try{
	            session = HibernateUtil.getSessionFactory().openSession();
	            session.beginTransaction();
	            Query query = session.createSQLQuery("EXEC BFSP_PORTAL_INS_ORDEN :codservemp, :codorden, :coddetalle")
	            .setParameter("codservemp", idServEmp)	            
	            .setParameter("codorden", idOrden)
	            .setParameter("coddetalle", idDetalle);	            
	            query.executeUpdate();	            
	            session.getTransaction().commit();        	         	           
	        }
	        catch(Exception ex){
	            logger.error(ex.toString(),ex);	            
	        }
	        finally{
	        	session.close();
	        }
	}

	@Override
	public void deleteOrdenRep(Map ordenes, char estadoOut, List elim) {
		 Session session = null;	        
	        try{                     
	            Iterator k = ordenes.keySet().iterator();
	            while (k.hasNext()) {
	                String key = (String) k.next();
	                String keys[] = key.split("\\*");	                
	                session = HibernateUtil.getSessionFactory().openSession();
	                session.beginTransaction();	                	                
	                Query query = session.createSQLQuery("EXEC BFSP_PORTAL_UPD_ESTADO_ORD :codservemp, :codorden, :estado")
		            .setParameter("codservemp", Long.parseLong(keys[1]))	            
		            .setParameter("codorden", Long.parseLong(keys[0]))
		            .setParameter("estado", estadoOut);		            
		            query.executeUpdate();	                	                          
	            }                    
	        }catch(Exception e){
	            logger.error(e.toString(),e);	           
	        }finally{
	        	session.close();
	        }
	}

	@Override
	public void updateOrdenRep(long idServEmp, long idOrden, String estado) {
		 Session session = null; 
	        try{
	            session = HibernateUtil.getSessionFactory().openSession();
	            session.beginTransaction();
	            Query query = session.createSQLQuery("EXEC BFSP_PORTAL_UPD_ESTADO_ORD :codservemp, :codorden, :estado")
	            .setParameter("codservemp", idServEmp)	            
	            .setParameter("codorden", idOrden)
	            .setParameter("estado", estado);		            
	            query.executeUpdate();
	            session.getTransaction().commit();        	           
	        }
	        catch(Exception ex){
	            logger.error(ex.toString(),ex);	            
	        }
	        finally{
	        	session.close();
	        }
	}

	@Override
	public void deleteRep(List ordenes, char estado_out) {
		
		
		Session session = null;			
				
		String estado2=String.valueOf(estado_out);
		
		String nombreEstado="";
		
		String fechaActual="";
		
		//instancio la clase de utilidades
		Util dutil = new Util();
		
		fechaActual=dutil.getFechaActualSQL();		
		
		
		
		if(estado2.equals("0")){
			nombreEstado="Ingresado";				
		}else if(estado2.equals("1")){
			nombreEstado="Aprobado";				
		}else if(estado2.equals("2")){
			nombreEstado="Pend. Autorización";				
		}else if(estado2.equals("3")){
			nombreEstado="Procesado";				
		}else if(estado2.equals("4")){
			nombreEstado="Vencido";				
		}else if(estado2.equals("5")){
			nombreEstado="Cancelado";				
		}else if(estado2.equals("6")){
			nombreEstado="Procesado Parcial";				
		}else if(estado2.equals("7")){
			nombreEstado="Eliminado";				
		}else if(estado2.equals("8")){
			nombreEstado="Rechazado";				
		}else if(estado2.equals("9")){
			nombreEstado="Errado";				
		}else if(estado2.equals("A")){
			nombreEstado="Cerrado";				
		}else{
			nombreEstado="";
		}		
		
		
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			
			for(int i=0; i<ordenes.size();i++){
				
				//System.out.println("ANDY==>Entro aqui......"+ordenes.get(i).hashCode());
				
				session.beginTransaction();
				String sql = "update BFP_CASH_CONSULTAS.dbo.TaOrdenRep set corEstado = '"+estado2+"'" +
						", nombreEstadoOrden='"+nombreEstado+"' " +
						", fOrFechaCancelacion='"+fechaActual+"' " +
								"where corIdOrden = "+ordenes.get(i).hashCode();
				
				//System.out.println("ANDY==>Entro aqui consulta SQL......"+sql);
				
				Query query = session.createSQLQuery(sql);
				query.executeUpdate();				
				session.getTransaction().commit();				
				
			}			
			
			
		} catch (Exception e) {
			logger.error("error"+e.toString(),e);
			//return false;
		}finally{
			session.close();
		}

		//return band;
		
	}

	@Override
	public void guardaLetrasConsultas(long idServEmp, long idOrden) {
		 Session session = null; 
	        try{
	            session = HibernateUtil.getSessionFactory().openSession();
	            session.beginTransaction();
	            Query query = session.createSQLQuery("EXEC BFSP_PORTAL_LETRAS :codservemp, :codorden")
	            .setParameter("codservemp", idServEmp)	            
	            .setParameter("codorden", idOrden);		            
	            query.executeUpdate();
	            session.getTransaction().commit();        	           
	        }
	        catch(Exception ex){
	            logger.error(ex.toString(),ex);	            
	        }
	        finally{
	        	session.close();
	        }
	

	}

}
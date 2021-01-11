/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.financiero.cash.dao.EntityDAO;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.domain.TmServicio;
import com.hiper.cash.entidad.BeanServicio;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;

/**
 * 
 * @author financiero
 */
public class TaServicioxEmpresaDaoHibernate implements TaServicioxEmpresaDao {

	private static Logger logger = Logger
			.getLogger(TaServicioxEmpresaDaoHibernate.class);

	// jmoreno 12/11/09
	public List selectServicioxEmpresaxAprobador(String codUsuario) {
		Session session = null;
		String strQuery;
		List result;
		strQuery = "select tse.csemIdServicioEmpresa,tse.dsemDescripcion,tse.csemEstado from TaServicioxEmpresa tse "
				+ "inner join TaAprobadorServicio tas on tas.cSEmIdServicioEmpresa = tse.cSEmIdServicioEmpresa and tas.casestado = :estadoAprobador "
				+ "inner join TmServicio ts on ts.cSrIdServicio =tse.cSEmIdServicio and ts.csrFlagInformacion ='1'"
				+ "where tas.casidAprobador = :codUsuario and tse.csemEstado = :estadoServicio "
				+ "and tse.fSEmFechaInicio <= :fechaActual and tse.fSEmFechaFin >= :fechaActual order by tse.cSEmIdServicio asc";

		String fechaActual = Fecha.getFechaActual("yyyyMMdd");
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			result = session
					.createSQLQuery(strQuery)
					.setParameter("codUsuario", codUsuario)
					.setParameter("estadoServicio",
							Constantes.TX_CASH_ESTADO_SERVICIO_EMPRESA_VIGENTE)
					.setParameter(
							"estadoAprobador",
							Constantes.HQL_CASH_ESTADO_APROBADOR_SERVICIO_HABILITADO)
					.setParameter("fechaActual", fechaActual).list();

			Iterator iter = result.iterator();

			BeanServicio beanservicio;
			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				Object[] al_servicio = (Object[]) iter.next();
				beanservicio = new BeanServicio();
				beanservicio.setM_IdServicio(al_servicio[0].toString());
				beanservicio.setM_Descripcion(al_servicio[1].toString());
				beanservicio.setEstado(al_servicio[2].toString());
				alresult.add(beanservicio);
				beanservicio = null;
			}
			result = null;
			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}
	}

	public List selectServicioxEmpresaxTipo(String empresa, List tipo) {
		List result;
		Session session = null;
		String strQuery;

		strQuery = "select tse.cSEmIdServicioEmpresa,tse.dSEmDescripcion,tse.cSEmEstado from taServicioxEmpresa tse "
				+ "inner join TmServicio ts on ts.csrIdServicio = tse.cSEmIdServicio and ts.csrTipo in (:tipo) "
				+ "and ts.csrFlagInformacion = :tipoInfo where tse.cSEmIdEmpresa = :empresaid and tse.csemEstado = :estadoServ "
				+ "and tse.fSEmFechaInicio <= :fechaActual and tse.fSEmFechaFin >= :fechaActual order by tse.cSEmIdServicio asc";
		String fechaActual = Fecha.getFechaActual("yyyyMMdd");

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			result = session
					.createSQLQuery(strQuery)
					.setParameter("empresaid", empresa)
					.setParameterList("tipo", tipo)
					.setParameter(
							"tipoInfo",
							Constantes.TX_CASH_SERVICIO_ESTADO_FLAG_INFORMACION_HABIL)
					// Para excluir el servicio Rec. sin BD
					.setParameter("estadoServ",
							Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO)
					.setParameter("fechaActual", fechaActual).list();

			Iterator iter = result.iterator();
			BeanServicio beanservicio;
			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				Object[] al_servicio = (Object[]) iter.next();
				beanservicio = new BeanServicio();
				beanservicio.setM_IdServicio(al_servicio[0].toString());
				beanservicio.setM_Descripcion((String) al_servicio[1]);
				beanservicio.setEstado(al_servicio[2].toString());
				alresult.add(beanservicio);
				beanservicio = null;
			}
			result = null;
			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}

	}

	// jmoreno 11/11/09 - modificado
	public List selectServicioxEmpresaxTipo(String empresa, String tipo) {
		List result;
		Session session = null;
		String strQuery;

		strQuery = "select tse.cSEmIdServicioEmpresa,tse.cSEmIdServicio,tse.dSEmDescripcion,tse.cSEmEstado from taServicioxEmpresa tse "
				+ "inner join TmServicio ts on ts.csrIdServicio = tse.cSEmIdServicio and ts.csrTipo = :tipo "
				+ "and ts.csrFlagInformacion = :tipoInfo where tse.cSEmIdEmpresa = :empresaid and tse.csemEstado = :estadoServ "
				+ "and tse.fSEmFechaInicio <= :fechaActual and tse.fSEmFechaFin >= :fechaActual order by tse.cSEmIdServicio asc";
		String fechaActual = Fecha.getFechaActual("yyyyMMdd");
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			result = session
					.createSQLQuery(strQuery)
					.addScalar("cSEmIdServicioEmpresa")
					.addScalar("cSEmIdServicio", Hibernate.STRING)
					.addScalar("dSEmDescripcion")
					.addScalar("cSEmEstado")
					.setParameter("empresaid", empresa)
					.setParameter("tipo", tipo)
					.setParameter(
							"tipoInfo",
							Constantes.TX_CASH_SERVICIO_ESTADO_FLAG_INFORMACION_HABIL)
					// Para excluir el servicio Rec. sin BD
					.setParameter("estadoServ",
							Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO)
					.setParameter("fechaActual", fechaActual).list();

			Iterator iter = result.iterator();

			BeanServicio beanservicio;
			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				Object[] al_servicio = (Object[]) iter.next();
				beanservicio = new BeanServicio();
				beanservicio.setM_IdServicioEmp(al_servicio[0].toString());
				beanservicio.setM_IdServicio(al_servicio[1].toString());
				beanservicio.setM_Descripcion((String) al_servicio[2]);
				beanservicio.setEstado(al_servicio[3].toString());
				alresult.add(beanservicio);
				beanservicio = null;
			}
			result = null;
			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}
	}

	public List selectServicioxEmpresaxCode(String empresa, String code) {
		List result, result2;
		Session session = null;

		String strQuery, strQuery2;

		strQuery = "select tl.csrIdServicio, tl.dsrDescripcion, tl.csrTipo from TmServicio tl where tl.csrIdServicio = :fieldid";
		strQuery2 = "select distinct ta.tmServicio.csrIdServicio from TaServicioxEmpresa ta where ta.tmEmpresa.cemIdEmpresa = :empresaid and ta.csemEstado = :estadoid";

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			result = session.createQuery(strQuery)
					.setParameter("fieldid", code).list();

			result2 = session
					.createQuery(strQuery2)
					.setParameter("empresaid", empresa)
					.setParameter("estadoid",
							Constantes.TX_CASH_ESTADO_SERVICIO_EMPRESA_VIGENTE)
					.list();

			Iterator iter = result.iterator();

			BeanServicio beanservicio;
			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				Object[] al_servicio = (Object[]) iter.next();
				beanservicio = new BeanServicio();
				beanservicio.setM_IdServicio((String) al_servicio[0]);
				beanservicio.setM_Descripcion((String) al_servicio[1]);

				beanservicio.setEstado("0");
				for (int i = 0; i < result2.size(); i++) {
					if (((String) al_servicio[0])
							.equalsIgnoreCase((String) result2.get(i))) {
						beanservicio.setEstado("1");
						break;
					}
				}

				alresult.add(beanservicio);
				beanservicio = null;
			}
			result = null;
			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}
	}

	public TaServicioxEmpresa selectServicioxEmpresa(String empresa,
			String servicio) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			TaServicioxEmpresa result = (TaServicioxEmpresa) session
					.createQuery(
							" from TaServicioxEmpresa ta "
									+ " where ta.tmEmpresa.cemIdEmpresa = :empresaid "
									+ "  and ta.tmServicio.csrIdServicio = :servicioid "
									+ "  and ta.csemEstado = :estadoid")
					.setParameter("empresaid", empresa)
					.setParameter("servicioid", servicio)
					.setParameter("estadoid",
							Constantes.TX_CASH_ESTADO_SERVICIO_EMPRESA_VIGENTE)
					.uniqueResult();
			return result;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}
	}

	public List selectServEmpByIdServ(String empresa, List listaIdServ) {
		Session session = null;
		List listaServicios = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			List result = session
					.createQuery(
							" from TaServicioxEmpresa ta "
									+ " where ta.tmEmpresa.cemIdEmpresa = :empresaid "
									+ "  and ta.tmServicio.csrIdServicio in (:listaIdServ)"
									+ "  and ta.csemEstado = :estadoid order by ta.dsemDescripcion asc")
					.setParameter("empresaid", empresa)
					.setParameterList("listaIdServ", listaIdServ)
					.setParameter("estadoid",
							Constantes.TX_CASH_ESTADO_SERVICIO_EMPRESA_VIGENTE)
					.list();
			Iterator it = result.iterator();
			listaServicios = new ArrayList();
			while (it.hasNext()) {
				TaServicioxEmpresa aux = (TaServicioxEmpresa) it.next();
				BeanServicio nvo = new BeanServicio();
				nvo.setM_Descripcion(aux.getDsemDescripcion());
				nvo.setM_IdServicio(String.valueOf(aux
						.getCsemIdServicioEmpresa()));
				listaServicios.add(nvo);
			}
			return listaServicios;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return listaServicios;
		} finally {
			session.close();
		}

	}

	public TaServicioxEmpresa selectServicioxEmpresa(String empresa,
			long servemp) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			TaServicioxEmpresa result = (TaServicioxEmpresa) session
					.createQuery(
							"from TaServicioxEmpresa ta where ta.tmEmpresa.cemIdEmpresa = :empresaid and ta.csemIdServicioEmpresa = :servempid and ta.csemEstado = :estadoid")
					.setParameter("empresaid", empresa)
					.setParameter("servempid", servemp)
					.setParameter("estadoid",
							Constantes.TX_CASH_ESTADO_SERVICIO_EMPRESA_VIGENTE)
					.uniqueResult();
			return result;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}
	}

	// jwong 14/01/2009 para seleccionar solo el listado de codigos de los
	// servicios relacionados con una empresa
	public List selectCodeServicioxEmpresa(String empresa) {
		Session session = null;
		List result;
		String strQuery = " select distinct ta.tmServicio.csrIdServicio "
				+ " from TaServicioxEmpresa ta "
				+ " where ta.tmEmpresa.cemIdEmpresa = :empresaid "
				+ " and ta.csemEstado = :estadoid ";
		try {
			session = HibernateUtil.getSessionFactory().openSession();

			result = session
					.createQuery(strQuery)
					.setParameter("empresaid", empresa)
					.setParameter("estadoid",
							Constantes.TX_CASH_ESTADO_SERVICIO_EMPRESA_VIGENTE)
					.list();

			Iterator iter = result.iterator();

			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				alresult.add((String) iter.next());
			}
			result = null;

			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}
	}

	public String[] selectTipoServicioxEmpresa(long servemp) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			String tipo_servicio[] = { null, null };
			List result = session
					.createQuery(
							" select ta.tmServicio.csrIdServicio, ta.tmServicio.csrTipo "
									+ " from TaServicioxEmpresa ta "
									+ " where ta.csemIdServicioEmpresa =:servicioid "
									+ " and ta.csemEstado = :estadoid ")
					.setLong("servicioid", servemp)
					.setParameter("estadoid",
							Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO)
					.list();

			Iterator iter = result.iterator();
			if (iter.hasNext()) {
				Object[] obj = (Object[]) iter.next();
				tipo_servicio[0] = (String) obj[0];
				tipo_servicio[1] = (String) obj[1];
			}

			return tipo_servicio;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}

	}

	// LETRAS
	public long selectCodeServicioxEmpresa_2(String idEmpresa, String idServicio) {
		Session session = null;
		long idServxEmp = -1;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			List result = session
					.createQuery(
							" select ta.csemIdServicioEmpresa "
									+ " from TaServicioxEmpresa ta "
									+ " where ta.tmEmpresa.cemIdEmpresa = :idEmpresa "
									+ " and ta.tmServicio.csrIdServicio = :idServicio "
									+ " and ta.csemEstado='0'")
					.setString("idEmpresa", idEmpresa)
					.setString("idServicio", idServicio).list();

			Iterator iter = result.iterator();
			Long code = null;
			if (iter.hasNext()) {
				code = (Long) iter.next();
			}
			idServxEmp = code.longValue();
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return -1;
		} finally {
			session.close();
		}
		return idServxEmp;
	}

	/**
	 * 
	 * @param empresa
	 * @param tipo
	 * @param estado
	 *            , si estado==null buscara todos los estados, si estado=="0"
	 *            buscara totos los habilitados, si estado=="1" buscara totos
	 *            los deshabilitados
	 * @return
	 */
	// jwong 09/05/2009 para busqueda por el estado(para comprobantes - busca
	// todos los estados)
	public List selectServicioxEmpresaxTipoxEstado(String empresa, List tipo,
			String estado) {
		List result;
		Session session = null;

		String strQuery;
		String condicion = "";
		if (estado != null
				&& (Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO
						.equals(estado) || Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_INACTIVO
						.equals(estado))) {
			condicion = " and ta.csemEstado = '" + estado + "' ";
		}

		// strQuery =
		// "select tl.csrIdServicio, tl.dsrDescripcion, tl.csrTipo from TmServicio tl where tl.csrTipo in (:tipoList) order by tl.dsrDescripcion ";
		// strQuery2 =
		// "select distinct ta.tmServicio.csrIdServicio from TaServicioxEmpresa ta where ta.tmEmpresa.cemIdEmpresa = :empresaid "
		// + condicion;

		strQuery = "select tse.csemIdServicioEmpresa,tse.dsemDescripcion,tse.csemEstado from TaServicioxEmpresa tse "
				+ "inner join TmServicio ts on ts.csrIdServicio = tse.cSEmIdServicio and ts.csrTipo in (:tipoList) "
				+ "where tse.cSEmIdEmpresa = :empresaid and tse.csemEstado = :estadoServ and tse.fSEmFechaInicio <= "
				+ ":fechaActual and tse.fSEmFechaFin >= :fechaActual order by tse.cSEmIdServicio asc";
		String fechaActual = Fecha.getFechaActual("yyyyMMdd");

		try {
			session = HibernateUtil.getSessionFactory().openSession();

			result = session.createSQLQuery(strQuery)
					.setParameterList("tipoList", tipo)
					.setParameter("empresaid", empresa)
					.setParameter("estadoServ", estado)
					.setParameter("fechaActual", fechaActual).list();

			Iterator iter = result.iterator();

			BeanServicio beanservicio;
			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				Object[] al_servicio = (Object[]) iter.next();
				beanservicio = new BeanServicio();
				beanservicio.setM_Descripcion(al_servicio[1].toString());
				beanservicio.setM_IdServicio(al_servicio[0].toString());
				beanservicio.setEstado(al_servicio[2].toString());
				alresult.add(beanservicio);
				beanservicio = null;
			}
			result = null;
			return alresult;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}

	}

	public List selectEmpresasByIdServ(boolean flag, List lempresa,
			String idServicio) {
		List result;
		Session session = null;
		String strQuery;
		ArrayList listaEmpresa;// lista de empresas que tienen asociadas el
								// servicio con IdServicio
		try {
			session = HibernateUtil.getSessionFactory().openSession();

			if (flag) {

				strQuery = "select distinct (cSEmIdEmpresa) from taservicioxempresa ta where  "
						+ " ta.cSEmIdServicio= :idServicio and ta.cSEmEstado = :estadoid";
				result = session
						.createSQLQuery(strQuery)
						.setParameter("idServicio", idServicio)
						.setParameter(
								"estadoid",
								Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO)
						.list();

			} else {
				strQuery = "select distinct (cSEmIdEmpresa) from taservicioxempresa ta where cSEmIdEmpresa in(:listEmpresa) "
						+ "and ta.cSEmIdServicio= :idServicio and ta.cSEmEstado = :estadoid";
				result = session
						.createSQLQuery(strQuery)
						.setParameterList("listEmpresa", lempresa)
						.setParameter("idServicio", idServicio)
						.setParameter(
								"estadoid",
								Constantes.HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO)
						.list();
			}

			Iterator iter = result.iterator();
			String rucEmpresa = "";
			listaEmpresa = new ArrayList();
			while (iter.hasNext()) {
				rucEmpresa = (String) iter.next();
				listaEmpresa.add(rucEmpresa);
			}
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}
		return listaEmpresa;
	}

	public int selectCodFormatoOut(long servemp) {
		List result;
		Session session = null;
		int idFormatoSalida = 0;
		String strQuery = "select ts.cseformatoSalida from TaServicioxEmpresa ts where ts.csemIdServicioEmpresa = :idServEmp";
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			result = session.createQuery(strQuery)
					.setParameter("idServEmp", servemp).list();
			Iterator it = result.iterator();
			while (it.hasNext()) {
				Integer codigo = (Integer) it.next();
				idFormatoSalida = codigo.intValue();
			}
			return idFormatoSalida;
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return 0;
		} finally {
			session.close();
		}
	}

	/**
	 * valida si el servemp es PAGO DE CTS**
	 */
	public int selectCountCTS(String servemp) throws HibernateException {
		int total = 0;
		Session session = null;
		StringBuilder queryEmpresa = new StringBuilder();
		queryEmpresa
				.append("SELECT count(*) from TaServicioxEmpresa tse inner join TmServicio ts on ts.csrIdServicio = tse.cSEmIdServicio "
						+ "where ts.csrIdServicio='02' and tse.csemIdServicioEmpresa='"
						+ servemp + "'");
		session = HibernateUtil.getSessionFactory().openSession();
		try {
			Query query = session.createSQLQuery(queryEmpresa.toString());
			total = ((Number) query.uniqueResult()).intValue();
		} catch (Exception e) {
			logger.error(Constantes.MENSAJE_ERROR_CONEXION_HIBERNATE, e);
		} finally {
			session.close();
		}
		return total;
	}

	@Override
	public TmEmpresa getEmpresa(long idServicioEmpresa) throws Exception {
		String sql = "SELECT obj.tmEmpresa FROM TaServicioxEmpresa obj WHERE obj.csemIdServicioEmpresa=:idServicio";
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idServicio", idServicioEmpresa);
		return EntityDAO.findUnique(TmEmpresa.class, sql, parametros);
	}

	public TmEmpresa getEmpresa(String codigo) throws Exception {
		String sql = "SELECT obj FROM TmEmpresa obj WHERE obj.cemIdEmpresa=:idServicio";
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idServicio", codigo);
		return EntityDAO.findUnique(TmEmpresa.class, sql, parametros);
	}

	@Override
	public TmServicio getServicio(long idServicioEmpresa) throws Exception {
		String sql = "SELECT obj.tmServicio FROM TaServicioxEmpresa obj WHERE obj.csemIdServicioEmpresa=:idServicio";
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idServicio", idServicioEmpresa);
		return EntityDAO.findUnique(TmServicio.class, sql, parametros);
	}

	@Override
	public TaServicioxEmpresa getServicioEmpresa(long idServicioEmpresa)
			throws Exception {
		TaServicioxEmpresa servicio = EntityDAO.findById(
				TaServicioxEmpresa.class, idServicioEmpresa);
		servicio.setTmEmpresa(getEmpresa(idServicioEmpresa));
		servicio.setTmServicio(getServicio(idServicioEmpresa));
		return servicio;
	}

	/**
	 * Implementacion para obtener el Id y Descripcion de ServicioEmpresa
	 * 
	 */
	public List<Object[]> buscarServiciosTipo(String empresa, String tipo)
			throws SQLException {

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("empresaid", empresa);
		parametros.put("estadoid",
				Constantes.TX_CASH_ESTADO_SERVICIO_EMPRESA_VIGENTE);
		parametros.put("tipo", tipo);
		String sql = "select ta.csemIdServicioEmpresa,ta.dsemDescripcion from TaServicioxEmpresa ta "
				+ "where ta.tmEmpresa.cemIdEmpresa = :empresaid and ta.csemEstado = :estadoid and ta.tmServicio.csrTipo = :tipo";
		try {
			return EntityDAO.find(sql, parametros);
		} catch (Exception e) {
			throw new SQLException(e);
		}

	}

	public TaServicioxEmpresa buscarServicioEmpresa(String empresa,
			String servicio) throws SQLException {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			TaServicioxEmpresa result = (TaServicioxEmpresa) session
					.createQuery(
							" from TaServicioxEmpresa ta "
									+ " where ta.tmEmpresa.cemIdEmpresa = :empresaid "
									+ "  and ta.tmServicio.csrIdServicio = :servicioid "
									+ "  and ta.csemEstado = :estadoid")
					.setParameter("empresaid", empresa)
					.setParameter("servicioid", servicio)
					.setParameter("estadoid",
							Constantes.TX_CASH_ESTADO_SERVICIO_EMPRESA_VIGENTE)
					.uniqueResult();
			return result;
		} catch (Exception ex) {
			throw new SQLException(ex);
		} finally {
			session.close();
		}
	}

	public List<Object[]> buscarEmpresasTransferencias() throws SQLException {
		try {
			Session session = null;
			StringBuilder sql = new StringBuilder(
					"select distinct cSEmIdEmpresa,dEMNombre from  BFPCash_HCenter.dbo.BFVW_SEL_EMPRESAS_NAME ");
			sql = sql
					.append(" inner join BFPCash_HCenter.dbo.taServicioXEmpresa  on cSEmIdEmpresa=cEmIdEmpresa ");
			sql = sql
					.append(" inner join BFPCash_HCenter.dbo.tmServicio on cSemIdServicio=cSrIdServicio ");
			sql = sql
					.append(" where cSrTipo='03' AND cSEmIdEmpresa!='00000000000' order by dEMNombre");

			session = HibernateUtil.getSessionFactory().openSession();

			Query q = session.createSQLQuery(sql.toString());
			return q.list();

		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

}
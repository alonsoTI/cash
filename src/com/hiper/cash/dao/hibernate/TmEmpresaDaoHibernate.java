/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.dao.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.util.Constantes;

/**
 * 
 * @author esilva
 */
public class TmEmpresaDaoHibernate implements TmEmpresaDao {

	private static Logger logger = Logger
			.getLogger(TmEmpresaDaoHibernate.class);

	public TmEmpresa selectEmpresas(String codigo) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			TmEmpresa empresa = (TmEmpresa) session
					.createQuery("from TmEmpresa where cemIdEmpresa = ? ")
					.setString(0, codigo).uniqueResult();
			return empresa;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}
	}

	// Get Empresa By Code
	public TmEmpresa selectEmpresaByCode(String code) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			TmEmpresa empresa = (TmEmpresa) session
					.createQuery("from TmEmpresa where cemIdEmpresa =:codeid ")
					.setString("codeid", code).uniqueResult();
			return empresa;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}
	}

	public List listarEmpresa(boolean flag, List empresa) {

		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			List result = null;

			if (flag) {
				String sql = "select distinct tm.* from TmEmpresa tm inner join taServicioxEmpresa ser on tm.cEmIdEmpresa=ser.cSEmIdEmpresa "
						+ "where tm.cemEstado ='0' and ser.csemEstado='0' order by tm.demNombre asc ";

				// String sql =
				// "select distinct tm.* from TmEmpresa tm where tm.cemEstado ='0' order by tm.demNombre asc ";

				SQLQuery query = session.createSQLQuery(sql);

				query.addEntity(TmEmpresa.class);
				result = query.list();

			} else {

				result = session
						.createQuery(
								"from TmEmpresa tm where tm.cemIdEmpresa in (:empresalist) order by tm.demNombre asc ")
						.setParameterList("empresalist", empresa).list();

			}

			return result;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}

	}

	public List listarEmpresa(List empresa) {

		Session session = null;
		try {

			boolean validaTarjetaCash = false;
			for (int i = 0; i < empresa.size(); i++) {
				// System.out.println("PORTAL==> idEmpresa="+ empresa.get(i).toString());
				if (empresa.get(i).toString().equals("00000000000")) {
					validaTarjetaCash = true;
					break;
				}
			}

			session = HibernateUtil.getSessionFactory().openSession();
			List result = null;
			if (validaTarjetaCash) {
				
				/*String sql = "select distinct tm.* from TmEmpresa tm inner join taServicioxEmpresa ser on tm.cEmIdEmpresa=ser.cSEmIdEmpresa "
						+ "where tm.cemEstado ='0' and ser.csemEstado='0' and tm.cemIdEmpresa <> '00000000000' order by tm.demNombre asc ";
*/
				String sql = "select tm.* from TmEmpresa tm where tm.cemIdEmpresa = '00000000000' order by tm.demNombre asc";

				
				SQLQuery query = session.createSQLQuery(sql);

				query.addEntity(TmEmpresa.class);
				result = query.list();

			} else {
				result = session
						.createQuery(
								"from TmEmpresa tm where tm.cemIdEmpresa in (:empresalist) order by tm.demNombre asc ")
						.setParameterList("empresalist", empresa).list();
			}

			return result;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}

	}

	// jwong 17/02/2009 para manejo de codigo de cliente
	public List listarClienteEmpresa(List empresa) {

		Session session = null;
		try {

			// aqui listare la lista de empresas enviadas
			// System.out.println("PORTAL==>n(listado)=" + empresa.size());

			// TmEmpresa beanEmp=null;
			boolean validaTarjetaCash = false;
			for (int i = 0; i < empresa.size(); i++) {
				// System.out.println("PORTAL==> idEmpresa="+
				// empresa.get(i).toString());
				if (empresa.get(i).toString().equals("00000000000")) {
					validaTarjetaCash = true;
					break;
				}
			}

			session = HibernateUtil.getSessionFactory().openSession();
			List result = null;

			if (validaTarjetaCash) {

				/*
				 * result = session .createQuery(
				 * "from TmEmpresa tm where tm.cemCodigoCliente is not null and tm.cemIdEmpresa <> '00000000000' and tm.cemEstado='0' order by tm.demNombre asc"
				 * ) .list();
				 */

				String sql = "select distinct tm.* from TmEmpresa tm inner join taServicioxEmpresa ser on tm.cEmIdEmpresa=ser.cSEmIdEmpresa "
						+ "where tm.cemEstado ='0' and ser.csemEstado='0' and tm.cemIdEmpresa <> '00000000000' order by tm.demNombre asc ";

				SQLQuery query = session.createSQLQuery(sql);

				query.addEntity(TmEmpresa.class);
				result = query.list();

			} else {
				result = session
						.createQuery(
								"from TmEmpresa tm where tm.cemCodigoCliente is not null and tm.cemIdEmpresa in (:empresalist) order by tm.demNombre asc")
						.setParameterList("empresalist", empresa).list();

			}

			return result;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		} finally {
			session.close();
		}

	}

	// jwong 26/02/2009 seleccion de todos los codigos de empresa
	public List selectCodEmpresas() {
		Session session = null;
		List result;
		String strQuery = " select distinct cemIdEmpresa " + " from TmEmpresa ";

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			result = session.createQuery(strQuery).list();

			Iterator iter = result.iterator();

			ArrayList alresult = new ArrayList();
			while (iter.hasNext()) {
				alresult.add((String) iter.next());
			}
			result = null;
			return alresult;
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return null;
		} finally {
			session.close();
		}

	}

	public String selectCodEmpresas(String ruc) {
		Session session = null;
		String codigoCliente = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			List result = session
					.createQuery(
							"select tm.cemIdEmpresa from TmEmpresa tm where tm.cemIdEmpresa = :ruc")
					.setParameter("ruc", ruc).list();
			Iterator iter = result.iterator();
			if (iter.hasNext()) {
				codigoCliente = (String) iter.next();
			}
			return codigoCliente;
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return null;
		} finally {
			session.close();
		}

	}

	// jmoreno 23/03/2009
	public String obtenerCodCliente(String ruc) {
		Session session = null;
		String codigoCliente = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			List result = session
					.createQuery(
							"select tm.cemCodigoCliente from TmEmpresa tm where tm.cemIdEmpresa = :ruc")
					.setParameter("ruc", ruc).list();
			Iterator iter = result.iterator();
			if (iter.hasNext()) {
				codigoCliente = (String) iter.next();
			}
			return codigoCliente;
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return null;
		} finally {
			session.close();
		}
	}

	@Override
	public List<TmEmpresa> buscarTodos() {
		List<TmEmpresa> resultados = null;
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			resultados = (List<TmEmpresa>) session
					.createQuery("from TmEmpresa").list();
		} catch (Exception e) {
			throw new RuntimeException(
					Constantes.MENSAJE_ERROR_CONEXION_HIBERNATE, e);
		} finally {
			session.close();
		}
		return resultados;
	}

	@Override
	public boolean verificaSiTarjetaCash(String numTarjeta) {
		Session session = null;
		List result;
		boolean valida = false;
		int cantidad = 0;
		try {

			String strQuery = "SELECT B.ducuenta, COUNT(*) as 'nfila' "
					+ "FROM BFPCash_Seguridad..taUsuarioRol A "
					+ "INNER JOIN BFPCash_Seguridad..tmUsuario B ON A.cURUsuario=B.cUsuario "
					+ "inner join BFPCash_Seguridad..tmRol C on A.cURRol=C.cRol "
					+ "inner join BFPCash_HCenter..tmEmpresa D on B.cUIdEmpresa=D.cEmIdEmpresa "
					+ "where B.cUIdEmpresa='00000000000' and C.cTipo='02' and B.cUEstado='0' "
					+ "and B.ducuenta=:numTarjetaID " + "group by B.ducuenta";

			session = HibernateUtil.getSeguridadSessionFactory().openSession();
			session.beginTransaction();

			Query query = session.createSQLQuery(strQuery).setParameter(
					"numTarjetaID", numTarjeta);
			result = query.list();
			session.getTransaction().commit();
			Iterator iter = result.iterator();

			while (iter.hasNext()) {
				Object[] al_servicio = (Object[]) iter.next();
				cantidad = Integer.parseInt(al_servicio[1].toString());
			}
			result = null;

			if (cantidad > 0) {
				valida = true;
			} else {
				valida = false;
			}

		} catch (Exception ex) {
			logger.error(ex.toString());
			ex.printStackTrace();
			valida = false;
		} finally {
			session.close();
		}
		return valida;
	}
}

package com.financiero.cash.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.financiero.cash.dao.ECEntityDAO;
import com.financiero.cash.dao.service.HistoricoDAO;
import com.financiero.cash.ui.model.DetalleOrdenEC;
import com.financiero.cash.ui.model.EmpresaEC;
import com.financiero.cash.ui.model.OrdenHistoricoEC;
import com.financiero.cash.ui.model.ServicioEC;
import com.hiper.cash.dao.hibernate.util.HibernateUtil;
import com.hiper.cash.util.Util;

public class HibernateHistoricoDAO implements HistoricoDAO {

	@Override
	public String buscarNombreEmpresa(int idEmpresa) throws Exception {
		String sql = "select nombre from dbo.empresas where id_empresa=:empresa"; 
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("empresa", new Integer(idEmpresa));
		
		String nombre=  (String)ECEntityDAO.getSingleResultSQL(sql, parametros);
		return nombre;
	}

	@Override
	public String buscarNombreServicio(int idServicio) throws Exception {
		String sql = "select descripcion_servicio from dbo.servicios where id_servicio=:servicio"; 
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("servicio", new Integer(idServicio));
		
		String nombre=  (String)ECEntityDAO.getSingleResultSQL(sql, parametros);
		return nombre;
	}

	@Override
	public int cuentaDetallesOrden(int orden, String referencia)
			throws Exception {
		
		String sql;			
		sql = "SELECT count(*) "+
        " FROM dbo.Items INNER JOIN dbo.Bancos ON dbo.Items.Banco = dbo.Bancos.Codigo_Banco "+
        " WHERE Id_Sobre=:orden and  dbo.Items.Referencia like :referencia ";		
		StringBuilder sb = new StringBuilder("%").append(referencia).append("%");		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("orden", new Integer(orden));
		parametros.put("referencia", sb.toString());
		Integer cantidad =  (Integer)ECEntityDAO.getSingleResultSQL(sql, parametros);
		
		return cantidad;	
	}

	@Override
	public int cuentaOrdenes(String empresa, String servicio, Date fInicio,
			Date fFinal, String referencia) throws Exception {
		String sql;			
		sql = "SELECT count(DISTINCT dbo.Sobres.Id_Sobre) " +			
		" FROM dbo.Contratos INNER JOIN " +
		" dbo.Empresas ON dbo.Contratos.Id_Empresa = dbo.Empresas.Id_Empresa INNER JOIN" +
		" dbo.Servicios ON dbo.Contratos.Id_Servicio = dbo.Servicios.Id_Servicio INNER JOIN" +
		" dbo.Sobres ON dbo.Contratos.Id_Contrato = dbo.Sobres.Id_Contrato INNER JOIN" +
		" dbo.Totales ON dbo.Sobres.Id_Sobre = dbo.Totales.Sobre_id LEFT JOIN" +
		" dbo.Items ON dbo.Sobres.Id_Sobre=	dbo.Items.Id_Sobre " + 
		" WHERE dbo.Empresas.Id_Empresa = :empresa and dbo.Servicios.Id_Servicio=:servicio" +
		" and dbo.Sobres.Fecha_Inicio_Proceso between  :finicio and :ffinal " +
		"and ( dbo.Sobres.referencia like :referencia or dbo.items.referencia like :referencia )";			
		StringBuilder sb = new StringBuilder("%").append(referencia).append("%");
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("empresa", empresa);
		parametros.put("servicio", servicio);
		parametros.put("referencia", sb.toString());
		parametros.put("finicio", fInicio);
		parametros.put("ffinal", fFinal);
		Integer cantidad =  (Integer)ECEntityDAO.getSingleResultSQL(sql, parametros);
		return cantidad;
	}

	@Override
	public List<DetalleOrdenEC> getDetallesOrden(int orden, String referencia,
			int inicio, int nroRegistros) throws Exception {
		String sql = " SELECT dbo.Items.ContraPartida, dbo.Items.Referencia, "
				+ "dbo.Items.Valor, dbo.Items.Moneda, dbo.Items.FormaPago, "
				+ "dbo.Items.Estado_Proceso, dbo.Items.NombreContrapartida, "
				+ "dbo.Items.Referencia_Adicional, dbo.Items.Pais, "
				+ "dbo.Items.Tipo_Cuenta, dbo.Items.Numero_Cuenta, "
				+ "dbo.Bancos.Nombre_Banco AS Banco, dbo.Items.Id_Item, "
				+ "dbo.Items.Id_Sobre, dbo.Items.MensajeProceso, dbo.Items.Secuencial_Cobro, "
				+ "dbo.Items.TipoId_Cliente + '|' + dbo.Items.NumeroId_Cliente AS Identificacion,  Fecha_Proceso, "
				+ "CONVERT(Varchar(15), dbo.Items.Saldo, 1) AS Saldo, dbo.Items.Localidad, '*' AS Accion, '' AS nuevoEstado_Proceso, dbo.Items.Id_Contrato, "
				+ " '' AS nuevoFormaPago, '' AS nuevaLocalidad, dbo.Bancos.Codigo_Banco, "
				+ " CASE Estado_Proceso WHEN 'ELIMINADO' THEN 'X' ELSE '' END AS last_Estado_proceso "
				+ " FROM     dbo.Items INNER JOIN dbo.Bancos ON dbo.Items.Banco = dbo.Bancos.Codigo_Banco "
				+ " WHERE Id_Sobre=:orden and dbo.Items.Referencia like :referencia Order by Id_item ";

		StringBuilder sb = new StringBuilder("%").append(referencia).append("%");
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("orden", new Integer(orden));
		parametros.put("referencia", sb.toString());
		List<Object[]> lista = ECEntityDAO.getResultListSQL(sql, parametros, inicio,nroRegistros);

		DetalleOrdenEC detalleOrden;
		List<DetalleOrdenEC> detalles = new ArrayList<DetalleOrdenEC>();
		int indice = 0;
		BigDecimal numero;
		Date date;

		for (Object[] fila : lista) {
			indice = 0;
			detalleOrden = new DetalleOrdenEC();			
			detalleOrden.setContraPartida(fila[indice++].toString());
			detalleOrden.setReferencia(fila[indice++].toString());
			numero = (BigDecimal) fila[indice++];
			detalleOrden.setValor(numero.doubleValue());
			detalleOrden.setMoneda(fila[indice++].toString());
			indice += 2;
			detalleOrden.setContraPartidaAdicional(fila[indice++].toString());
			detalleOrden.setReferenciaAdicional(fila[indice++].toString());
			detalleOrden.setPais(fila[indice++].toString());
			detalleOrden.setCuenta(new	StringBuffer(fila[indice++].toString()).append(fila[indice++].toString()).toString() );
			detalleOrden.setBanco(fila[indice++].toString());
			//numero = (BigDecimal) fila[indice++];
			detalleOrden.setIdDetalleOrden((Integer)fila[indice++]);
			//numero = (BigDecimal) fila[indice++];
			detalleOrden.setIdSobre((Integer)fila[indice++]);
			detalleOrden.setMensajeProceso(fila[indice++].toString()); 
			indice+=2;
			date =  (Date)fila[indice++];
			detalleOrden.setFechaProceso(date);
			detalles.add(detalleOrden);
		}

		return detalles;
	}

	@Override
	public List<EmpresaEC> getEmpresas(List<Integer> codigos) throws Exception {
		List<EmpresaEC> empresas =  new ArrayList<EmpresaEC>();
		EmpresaEC item = null;		
		String sql = "SELECT Id_Empresa,Nombre FROM Empresas  WHERE Id_Empresa in (:empresa)";
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("empresa", codigos);
		List<Object[]> lista = ECEntityDAO.getResultListSQL(sql, parametros);
		int indice;
		for (Object[] fila : lista) {	
			indice = 0;
         	item = new EmpresaEC();
         	item.setId(fila[indice++].toString());
         	item.setDescripcion(fila[indice++].toString());               
            empresas.add(item);
        }
		return empresas;
	}

	@Override
	public List<OrdenHistoricoEC> getOrdenes(String empresa, String servicio,
			Date fInicio, Date fFinal, String referencia, int inicio,
			int nroRegistros) throws Exception {
			
		List<OrdenHistoricoEC> ordenes = new ArrayList<OrdenHistoricoEC>();
		String sql =  "SELECT DISTINCT  dbo.Sobres.Id_Sobre,"
				+ " LTRIM(RTRIM(dbo.Sobres.Tipo_Cuenta)) + ' ' + LTRIM(RTRIM(dbo.Sobres.Numero_Cuenta)) AS Cuenta,"
				+ " dbo.Sobres.Codigo_Moneda,dbo.Sobres.Referencia,"
				+ " dbo.Sobres.Fecha_Inicio_Proceso, dbo.Sobres.Fecha_Vencimiento_Proceso,"
				+ " dbo.Totales.Numero_Items, dbo.Totales.Valor_Sobre, dbo.Sobres.Estado_Sobre,"
				+ " dbo.Sobres.Fecha_Creacion "
				+ " FROM dbo.Contratos INNER JOIN "
				+ " dbo.Empresas ON dbo.Contratos.Id_Empresa = dbo.Empresas.Id_Empresa INNER JOIN"
				+ " dbo.Servicios ON dbo.Contratos.Id_Servicio = dbo.Servicios.Id_Servicio INNER JOIN"
				+ " dbo.Sobres ON dbo.Contratos.Id_Contrato = dbo.Sobres.Id_Contrato INNER JOIN"
				+ " dbo.Totales ON dbo.Sobres.Id_Sobre = dbo.Totales.Sobre_id LEFT JOIN "
				+ " dbo.Items ON dbo.Sobres.Id_Sobre=dbo.Items.Id_Sobre"
				+ " WHERE dbo.Empresas.Id_Empresa =:empresa and dbo.Servicios.Id_Servicio=:servicio"
				+ " and dbo.Sobres.Fecha_Inicio_Proceso between  :finicio and :ffinal "
				+ " and ( dbo.Sobres.referencia like :referencia  or dbo.items.referencia like :referencia)"
				+ " order by dbo.Sobres.Fecha_Creacion ASC";
		StringBuilder sb = new StringBuilder("%").append(referencia).append("%");
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("empresa", empresa);
		parametros.put("servicio", servicio);
		parametros.put("referencia", sb.toString());
		parametros.put("finicio", fInicio);
		parametros.put("ffinal", fFinal);
		List<Object[]> lista = ECEntityDAO.getResultListSQL(sql, parametros, inicio,	nroRegistros);
		//System.out.println("LISTAAAAAAAAAAAAAAAAAAAAAAAa: " + lista.size());
		OrdenHistoricoEC orden;
		BigDecimal numero;
		int indice;
		for (Object[] fila : lista) {		
				orden = new OrdenHistoricoEC();
				indice=0;
				orden.setIdSobre((Integer)fila[indice++]);
				orden.setCuenta(fila[indice++].toString());
				orden.setCodigoMoneda(fila[indice++].toString());
				orden.setReferencia(fila[indice++].toString());
				orden.setFechaInicio((Date)fila[indice++]);
				orden.setFechaVencimiento((Date)fila[indice++]);		
				orden.setNroItems((Integer)fila[indice++]);
				numero = (BigDecimal)fila[indice++];
				orden.setValorSobre(numero.doubleValue());
				orden.setEstadoSobre(fila[indice++].toString());
				ordenes.add(orden);
				
		}	
		
		return ordenes;
	}

	@Override
	public List<ServicioEC> getServicios(String empresa) throws Exception {
		List<ServicioEC> servicios  = new ArrayList<ServicioEC>();
		String sql = "SELECT DISTINCT  dbo.Contratos.Id_Servicio,dbo.Servicios.Descripcion_Servicio " +
 		"FROM dbo.Empresas " +
 		"INNER JOIN EasySeguridad.dbo.UserEmpServ ON dbo.Empresas.Id_Empresa = EasySeguridad.dbo.UserEmpServ.ID_Empresa " +
 		"INNER JOIN dbo.Servicios ON EasySeguridad.dbo.UserEmpServ.ID_Servicio = dbo.Servicios.Id_Servicio " +
 		"INNER JOIN dbo.Contratos ON dbo.Contratos.Id_Empresa = dbo.Empresas.Id_Empresa AND dbo.Contratos.Id_Servicio = dbo.Servicios.Id_Servicio " +
 		"INNER JOIN EasySeguridad.dbo.Usuarios ON EasySeguridad.dbo.UserEmpServ.Usuario = EasySeguridad.dbo.Usuarios.NombreCorto " +
 		"WHERE   dbo.Contratos.Id_Empresa=:empresa " +
 		"Order By Descripcion_Servicio asc";
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("empresa", empresa);
		List<Object[]> lista = ECEntityDAO.getResultListSQL(sql, parametros);
		ServicioEC item = null;
		int indice;
		for (Object[] fila : lista) {		
			indice = 0;
			item = new ServicioEC();
        	item.setId(fila[indice++].toString());
        	item.setDescripcion(fila[indice++].toString());       
        	servicios.add(item);
		}
		return servicios;
	}

	@Override
	public List<Integer> obtenerCodigoEmpresaEasyCash(List<String> idEmpresa) throws Exception {	
		List<Integer> codigos= new ArrayList<Integer>();
		Session session=null;
		List<Integer> lista=null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			String sql = "SELECT CodigoAnterior FROM tmEmpresa  where cEmIdEmpresa in (:empresa)";
			Query q = session.createSQLQuery(sql).setParameterList("empresa",
					idEmpresa);
			lista = q.list();
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		for( Integer codigo : lista ){
			if( codigo != null ){
				codigos.add(codigo);
			}
		}		
		return codigos;
	}
	

	@Override
	public List<String> obtenerIdEmpresa(String nroTarjeta) throws Exception {
		List<String> empresas = new ArrayList<String>();
		Session session=null;
		List<String> lista=null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			String sql = "SELECT cUIdEmpresa FROM BFPCash_Seguridad.dbo.tmUsuario  where dUCuenta =:tarjeta";
			Query q = session.createSQLQuery(sql).setParameter("tarjeta",
					nroTarjeta);
			lista = q.list();
		} catch (Exception e) {
			Util.propagateHibernateException(e);
		} finally {
			session.close();
		}
		for (String fila : lista) {	
			empresas.add(fila);			
		}		
		return empresas;
	}

	

}

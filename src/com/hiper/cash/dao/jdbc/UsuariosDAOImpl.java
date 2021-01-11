package com.hiper.cash.dao.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hiper.cash.dao.UsuariosDAO;
import com.hiper.cash.entidad.BeanUsuarioCaptura;

public class UsuariosDAOImpl implements UsuariosDAO {

	private static Logger logger = Logger.getLogger(UsuariosDAOImpl.class);

	@Override
	public BeanUsuarioCaptura getDatosUsuarioByTarjeta(String idTarjeta) {
		CallableStatement cstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		BeanUsuarioCaptura bean = null;

		try {

			conn = DBManager.getConnectionDBSeguridad();

			String SQL = "{call BFPSP_PORTAL_SEL_DATOS_USUARIO (?)}";
			cstmt = conn.prepareCall(SQL);
			cstmt.setString(1, idTarjeta);
			rs = cstmt.executeQuery();

			while (rs.next()) {
				bean = new BeanUsuarioCaptura();

				bean.setNroTarjeta(rs.getString("dUCuenta"));
				bean.setTipoDoc(rs.getString("tipoDoc"));
				bean.setNroDoc(rs.getString("nroDoc"));
				bean.setEmail(rs.getString("dUEmail"));
				bean.setOperador(rs.getString("operadorTlf"));
				bean.setNroMovil(rs.getString("dUTelefono"));				
				bean.setNombres(rs.getString("dUNombre"));
				bean.setApellidos(rs.getString("dUApellido"));				
				bean.setFlagMigra(rs.getString("flagMigracion"));	
				
				

			}
		} catch (SQLException sqle) {
			logger.error("Error: " + sqle);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (cstmt != null) {
					cstmt.close();
					cstmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bean;
	}

	@Override
	public boolean registrarDatosUsuario(BeanUsuarioCaptura bean) {
		boolean respuesta = true;
		CallableStatement cstmt = null;
		Connection conn = null;

		try {

			conn = DBManager.getConnectionDBSeguridad();
			String SQL = "{call BFPSP_PORTAL_ACT_DATOS_USUARIO (?,?,?,?,?,?,?,?)}";
			cstmt = conn.prepareCall(SQL);

			cstmt.setString(1, bean.getNombres());
			cstmt.setString(2, bean.getApellidos());
			cstmt.setString(3, bean.getTipoDoc());
			cstmt.setString(4, bean.getNroDoc());
			cstmt.setString(5, bean.getEmail());
			cstmt.setString(6, bean.getOperador());
			cstmt.setString(7, bean.getNroMovil());
			cstmt.setString(8, bean.getNroTarjeta());

			cstmt.executeUpdate();

		} catch (Exception e) {
			respuesta = false;
			logger.error("Error: " + e.getMessage());
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
					cstmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return respuesta;
	}

	@Override
	public List<BeanUsuarioCaptura> getListaUsuarioByTarjeta(String idTarjeta) {
		
		List<BeanUsuarioCaptura> lista = new ArrayList<BeanUsuarioCaptura>(0);
		CallableStatement cstmt = null;
		Connection conn = null;
		ResultSet rs = null;

		try {

			conn = DBManager.getConnectionDBSeguridad();

			String SQL = "{call BFPSP_PORTAL_SEL_DATOS_USUARIO (?)}";
			cstmt = conn.prepareCall(SQL);
			cstmt.setString(1, idTarjeta);
			rs = cstmt.executeQuery();
			
			BeanUsuarioCaptura bean = null;
			
			while (rs.next()) {
				
				bean = new BeanUsuarioCaptura();

				bean.setNroTarjeta(rs.getString("dUCuenta"));
				bean.setTipoDoc(rs.getString("tipoDoc"));
				bean.setNroDoc(rs.getString("nroDoc"));
				bean.setEmail(rs.getString("dUEmail"));
				bean.setOperador(rs.getString("operadorTlf"));
				bean.setNroMovil(rs.getString("dUTelefono"));				
				bean.setNombres(rs.getString("dUNombre"));
				bean.setApellidos(rs.getString("dUApellido"));
				bean.setIdEmpresa(rs.getString("cUIdEmpresa"));
				
				lista.add(bean);
			}
		} catch (SQLException sqle) {
			logger.error("Error: " + sqle);
			lista = null;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (cstmt != null) {
					cstmt.close();
					cstmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	@Override
	public boolean validarSiUsuarioMigro(String idTarjeta) {
		boolean respuesta = false;		
		CallableStatement cstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		int retorno = 0;

		try {

			conn = DBManager.getConnectionDBEasySeguridad();

			String SQL = "{call spUsuarioEstaMigrado (?,?)}";
			cstmt = conn.prepareCall(SQL);
			cstmt.setString(1, idTarjeta);
			cstmt.registerOutParameter(2, Types.BIT);
			cstmt.executeUpdate();			
			retorno = cstmt.getInt(2);
			
			
			if (retorno == 1) {
				respuesta = true;
			} else {
				respuesta = false;
			}

			
		} catch (Exception sqle) {
			logger.error("Error: " + sqle);
			respuesta = false;
			
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (cstmt != null) {
					cstmt.close();
					cstmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return respuesta;
	}

	@Override
	public boolean validarSiEmpresaMigro(String idEmpresa) {
		boolean respuesta = false;		
		CallableStatement cstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		int retorno = 0;

		try {

			conn = DBManager.getConnectionDBEasyCashManagement();

			String SQL = "{call spEmpresaEstaMigrada (?,?)}";
			cstmt = conn.prepareCall(SQL);
			cstmt.setString(1, idEmpresa);
			cstmt.registerOutParameter(2, Types.BIT);
			cstmt.executeUpdate();			
			retorno = cstmt.getInt(2);
			
			
			if (retorno == 1) {
				respuesta = true;
			} else {
				respuesta = false;
			}

			
		} catch (Exception sqle) {
			logger.error("Error: " + sqle);
			respuesta = false;
			
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (cstmt != null) {
					cstmt.close();
					cstmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return respuesta;

	}

	@Override
	public boolean confirmarNotificacioUsuarioMigrado(String idTarjeta) {
		boolean respuesta = false;		
		CallableStatement cstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		int retorno = 0;

		try {

			conn = DBManager.getConnectionDBEasyCashManagement();

			String SQL = "{call spConfirmarNotificacionUsuarioMigrado (?,?)}";
			cstmt = conn.prepareCall(SQL);
			cstmt.setString(1, idTarjeta);
			cstmt.registerOutParameter(2, Types.BIT);
			cstmt.executeUpdate();			
			retorno = cstmt.getInt(2);
			
			
			if (retorno == 1) {
				respuesta = true;
			} else {
				respuesta = false;
			}

			
		} catch (Exception sqle) {
			logger.error("Error: " + sqle);
			respuesta = false;
			
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (cstmt != null) {
					cstmt.close();
					cstmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return respuesta;

	}

	@Override
	public boolean deshabilitarUsuarioPlataforma(String idTarjeta) {

		boolean respuesta = true;
		CallableStatement cstmt = null;
		Connection conn = null;

		try {

			conn = DBManager.getConnectionDBSeguridad();
			String SQL = "{call BFPSP_PORTAL_ACT_DESHABILITAR_USUARIO (?)}";
			cstmt = conn.prepareCall(SQL);
			cstmt.setString(1, idTarjeta);
			cstmt.executeUpdate();

		} catch (Exception e) {
			respuesta = false;
			logger.error("Error: " + e.getMessage());
		} finally {
			try {
				if (cstmt != null) {
					cstmt.close();
					cstmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return respuesta;

		
	}

}

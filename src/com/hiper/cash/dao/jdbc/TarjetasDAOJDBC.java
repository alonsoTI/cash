package com.hiper.cash.dao.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hiper.cash.dao.TarjetasDAO;

public class TarjetasDAOJDBC implements TarjetasDAO {

	private static Logger logger = Logger.getLogger(TarjetasDAOJDBC.class);
	
	@Override
	public int obtenerDiasAfilicacion(String numeroTarjeta,int numeroDias) {
		
		CallableStatement cstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		int resultado=0;

		try {

			conn = DBManager.getConnectionDBSeguridad();		

			String SQL = "{call BFPSP_PORTAL_VALIDA_VIGENCIA_CLAVE4A6 (?,?)}";
			cstmt = conn.prepareCall(SQL);
			cstmt.setString(1, numeroTarjeta);
			cstmt.setInt(2, numeroDias);
			rs = cstmt.executeQuery();
			
			while (rs.next()) {
				resultado=rs.getInt("nfilas");
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
		return resultado;
	}

}

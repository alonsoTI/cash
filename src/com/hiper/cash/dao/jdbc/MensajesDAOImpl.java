package com.hiper.cash.dao.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hiper.cash.dao.MensajesDAO;

public class MensajesDAOImpl implements MensajesDAO {

	private static Logger logger = Logger.getLogger(MensajesDAOImpl.class);

	@Override
	public void registrarMensaje(String programa, String tipoEnt,
			String servicio, String mensaje) throws SQLException {

		// boolean valExisteError = false;

		CallableStatement cstmt = null;
		Connection conn = null;
		try {

			conn = DBManager.getConnectionDBTransaccional();
			conn.setAutoCommit(false);

			String SQL = "{call BFPSP_CASH_INS_MSG (?,?,?,?)}";

			cstmt = conn.prepareCall(SQL);
			cstmt.setString(1, programa);
			cstmt.setString(2, tipoEnt);
			cstmt.setString(3, servicio);
			cstmt.setString(4, mensaje);
			cstmt.executeUpdate();
			conn.commit();

		} catch (SQLException sqle) {

			conn.rollback();
			sqle.printStackTrace();
			logger.error("Error :" + sqle);
			// valExisteError = false;

		} finally {

			if (cstmt != null) {
				cstmt.close();
				cstmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}

		}

	}

}

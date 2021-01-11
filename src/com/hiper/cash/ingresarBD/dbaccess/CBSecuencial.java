/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD.dbaccess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.hiper.cash.dao.jdbc.DBManager;
import com.hiper.cash.util.Constantes;

/**
 * 
 * @author jmoreno
 */
public class CBSecuencial {
	private long valorActual;

	static Logger logger = Logger.getLogger(CBSecuencial.class);

	public synchronized long generarSecuencial() {
		boolean incremento = incrementarSecuencial();
		if (incremento) {
			if (obtenerSecuencial()) {
				return this.valorActual;
			} else {
				return -1;
			}

		} else {
			return -1;// no se pudo incrementar el valorActual
		}
	}

	private boolean incrementarSecuencial() {
		boolean retorno = true;
		String sql = "update taSecuencial set nSeUltValor=nSeUltValor+1 where cSeId='ID_ORDEN'";
		Connection conn = null;
		try {
			conn = DBManager.getConnectionDBTransaccional();
			Statement st = conn.createStatement();
			int upd = st.executeUpdate(sql);
			if (upd != 1) {
				logger.error("No se pudo incrementar el secuencial");
				retorno = false;
			}
		} catch (Exception e) {
			logger.error(Constantes.MENSAJE_ERROR_CONEXION_DB, e);
			retorno = false;
		} finally {
			DBManager.closeConnection(conn);
		}

		return retorno;
	}

	private boolean obtenerSecuencial() {
		boolean retorno = true;
		String sql = "select ts.nSeUltValor from taSecuencial ts where ts.cSeId='ID_ORDEN'";
		Connection conn = null;
		try {
			conn = DBManager.getConnectionDBTransaccional();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				this.valorActual = rs.getLong(1);
			} else {
				logger.error("No se pudo incrementar el secuencial");
				retorno = false;
			}
		} catch (Exception e) {
			logger.error(Constantes.MENSAJE_ERROR_CONEXION_DB, e);
			retorno = false;
		} finally {
			DBManager.closeConnection(conn);
		}
		return retorno;
	}
}

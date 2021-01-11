/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD.dbaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hiper.cash.dao.jdbc.DBManager;
import com.hiper.cash.ingresarBD.entity.BuzonEnvio;
import com.hiper.cash.util.Constantes;

/**
 * 
 * @author jmoreno
 */
public class CBBuzonEnvio {
	static Logger logger = Logger.getLogger(CBBuzonEnvio.class);

	public boolean insertar(BuzonEnvio be) {
		boolean retorno = true;
		String sql = "insert into taBuzonEnvio values(?,?,?,?,?,?,?,?,?,?,?,?,?);";
		Connection conn = null;
		try {
			conn = DBManager.getConnectionDBTransaccional();
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setLong(1, be.getIdEnvio());
			pstm.setString(2, be.getFechaCreacion());
			pstm.setString(3, be.getFechaInicioVig());
			pstm.setString(4, be.getFechaFinVig());
			pstm.setString(5, be.getHoraInicio());
			pstm.setString(6, be.getTipoCuenta());
			pstm.setString(7, be.getCuentaCargo());
			pstm.setString(8, be.getTipoIngreso());
			pstm.setString(9, be.getReferencia());
			pstm.setString(10, be.getEstado());
			pstm.setString(11, be.getUsuario());
			pstm.setLong(12, be.getIdOrden());
			pstm.setLong(13, be.getIdServEmp());
			int up = pstm.executeUpdate();
			if (up != 1) {
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

	public boolean actualizarEstado(String estado, long idEnvio) {
		boolean retorno = true;
		String sql = "update taBuzonEnvio set cBEnEstado = ? where cBEnIdEnvio = ?";
		Connection conn = null;
		try {
			conn = DBManager.getConnectionDBTransaccional();
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, estado);
			pstm.setLong(2, idEnvio);
			int up = pstm.executeUpdate();
			if (up != 1) {
				retorno = false;
				throw new SQLException(
						"No se pudo actualizar el estado del registro");
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

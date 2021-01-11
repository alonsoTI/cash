/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD.dbaccess;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.hiper.cash.dao.jdbc.DBManager;
import com.hiper.cash.util.Constantes;

/**
 * 
 * @author hcash1
 */
public class CBEmpresaFormato {
	static Logger logger = Logger.getLogger(CBEmpresaFormato.class);

	public String obtenerXmlIn(String codEmpresa) {
		String retorno = "";
		String sql = "select ta.dEFXMLEnt from taEmpresaFormato ta where ta.cEFIdEmpresa=?";
		Connection conn = null;
		try {
			conn = DBManager.getConnectionDBTransaccional();
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, codEmpresa);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				retorno = rs.getString(1);
			} else {
				retorno = "";
			}
		} catch (Exception e) {
			logger.error(Constantes.MENSAJE_ERROR_CONEXION_DB, e);
			retorno = "";
		} finally {
			DBManager.closeConnection(conn);
		}
		return retorno;
	}

	public String obtenerEncIn(String codEmpresa) {
		String retorno = "";
		String sql = "select ta.dEFENCEnt from taEmpresaFormato ta where ta.cEFIdEmpresa=?";
		Connection conn = null;
		try {
			conn = DBManager.getConnectionDBTransaccional();
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, codEmpresa);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				retorno = rs.getString(1);
			} else {
				retorno = "";
			}
		} catch (Exception e) {
			logger.error(Constantes.MENSAJE_ERROR_CONEXION_DB, e);
			retorno = "";
		} finally {
			DBManager.closeConnection(conn);
		}
		return retorno;
	}

	/*public int obtenerCodigoFormato(int idServEmp) {

		CallableStatement cstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		int respuesta = 0;
		try {

			conn = DBManager.getConnectionDBTransaccional();

			String SQL = "{call SPU_SSIS_OBTENER_FORMATO (?)}";
			cstmt = conn.prepareCall(SQL);
			cstmt.setInt(1, idServEmp);
			rs = cstmt.executeQuery();

			while (rs.next()) {
				respuesta = rs.getInt("codFormato");
			}

		} catch (SQLException sqle) {
			logger.error(Constantes.MENSAJE_ERROR_CONEXION_DB, sqle);

		} finally {
			DBManager.closeConnection(conn);
		}
		return respuesta;
	}*/

	/*public String[] obtenerDetallesFormato(int codFormato) {

		CallableStatement cstmt = null;
		ResultSet rs = null;
		Connection conn = null;
		String mi_matriz[] = new String[100];

		try {

			conn = DBManager.getConnectionDBTransaccional();

			String SQL = "{call SPU_SSIS_OBTENER_DETALLES_FORMATO (?)}";
			cstmt = conn.prepareCall(SQL);
			cstmt.setInt(1, codFormato);
			rs = cstmt.executeQuery();
			int i = 0;

			while (rs.next()) {
				i = i + 1;
				mi_matriz[i] = rs.getString("dDFNombreCampo");
			}

		} catch (SQLException sqle) {
			logger.error(Constantes.MENSAJE_ERROR_CONEXION_DB, sqle);

		} finally {
			DBManager.closeConnection(conn);
		}
		return mi_matriz;
	}*/

}

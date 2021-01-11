/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD.dbaccess;

import java.sql.Connection;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.hiper.cash.dao.jdbc.DBManager;
import com.hiper.cash.util.Constantes;

/**
 *
 * @author jmoreno
 */
public class CBDetalleBuzon {    

    static Logger logger = Logger.getLogger(CBDetalleBuzon.class);
    
	
    
	public boolean insertarConex(String sql) {
		boolean retorno = true;
		Connection conn = null;
		try {
			
			logger.info("ANDY==>Insertar Linea: "+sql);
			
			conn = DBManager.getConnectionDBTransaccional();
			Statement st = conn.createStatement();
			int insert = st.executeUpdate(sql);
			if (insert < 1) {
				retorno = false;
			}
		} catch (Exception e) {
			logger.error(Constantes.MENSAJE_ERROR_CONEXION_DB, e);
			retorno = false;
		}finally {
			DBManager.closeConnection(conn);
		}
		return retorno;
	}
}

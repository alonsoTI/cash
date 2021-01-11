package com.hiper.cash.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


import com.hiper.cash.util.Constantes;

public class DBManager {
	private static Logger logger = Logger.getLogger(DBManager.class);

	private DBManager() {
	}

	public static Connection getConnectionDBSeguridad(){
		return getConnection(Constantes.JNDI_DB_SEGURIDAD);
	}

	public static Connection getConnectionDBTransaccional() {
		return getConnection(Constantes.JNDI_DB_TRANSACCIONAL);
	}
	
	public static Connection getConnectionDBEasyCashManagement() {
		return getConnection(Constantes.JNDI_DB_EASY_CASHMANAGEMENT);
	}	

	public static Connection getConnectionDBEasySeguridad() {
		return getConnection(Constantes.JNDI_DB_EASY_SEGURIDAD);
	}	
	
	
	
	private static Connection getConnection(String jndiName) {
		try {
			javax.naming.Context ctx = new InitialContext();
			DataSource dataSource = (DataSource) ctx
					.lookup(jndiName);
			return dataSource.getConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			logger.error("ERROR DEVOLVIENDO LA CONECCION AL POOL", e);
		}
	}

}

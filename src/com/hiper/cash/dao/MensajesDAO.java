package com.hiper.cash.dao;

import java.sql.SQLException;

public interface MensajesDAO {
	public void registrarMensaje(String programa, String tipoEnt,
			String servicio, String mensaje) throws SQLException;

}

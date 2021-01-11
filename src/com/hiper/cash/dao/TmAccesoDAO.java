package com.hiper.cash.dao;

import java.sql.SQLException;

import com.hiper.cash.domain.TmAcceso;

public interface TmAccesoDAO {
	TmAcceso getTmAcceso(String tco)throws SQLException;
	boolean crear(String tco)throws SQLException;
	boolean actualizar(TmAcceso acceso)throws SQLException;
	boolean esConsulta(String tarjeta)throws SQLException;
}

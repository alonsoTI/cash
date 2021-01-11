package com.hiper.cash.dao;

import java.sql.SQLException;
import java.util.List;

import com.hiper.cash.domain.TxDisponibilidad;

public interface TxDisponibilidadDAO {
	//TxDisponibilidad findById(String id) throws SQLException;	
	List<TxDisponibilidad> findAll() throws SQLException;
}

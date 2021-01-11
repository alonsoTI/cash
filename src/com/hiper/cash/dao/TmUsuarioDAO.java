package com.hiper.cash.dao;

import java.sql.SQLException;
import java.util.List;

import com.hiper.cash.domain.TmUsuario;

public interface TmUsuarioDAO {
	TmUsuario buscarId(String idUsuario)throws SQLException;
	List<TmUsuario> buscarTodos();
}

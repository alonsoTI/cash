package com.hiper.cash.dao;

import java.util.List;

import com.hiper.cash.domain.TmFormato;

/**
 * Dao de tabla
 * 
 * @author LUITOZ
 * 
 */
public interface TmFormatoDao {	

	TmFormato obtenerFormatoById(int formatoId);
	List<String> obtenerNombresCampos(int formatoId);
}

package com.hiper.cash.dao;

import java.util.List;

import com.hiper.cash.entidad.BeanUsuarioCaptura;

public interface UsuariosDAO {

	public BeanUsuarioCaptura getDatosUsuarioByTarjeta(String idTarjeta);
	
	public List<BeanUsuarioCaptura> getListaUsuarioByTarjeta(String idTarjeta);

	public boolean registrarDatosUsuario(BeanUsuarioCaptura bean);
	
	//Se agrega para realizar las validaciones del proceso de migracion
	
	public boolean validarSiUsuarioMigro(String idTarjeta);
	
	public boolean validarSiEmpresaMigro(String idEmpresa);
	
	public boolean confirmarNotificacioUsuarioMigrado(String idTarjeta);
	
	public boolean deshabilitarUsuarioPlataforma(String idTarjeta);
	
	
	

}

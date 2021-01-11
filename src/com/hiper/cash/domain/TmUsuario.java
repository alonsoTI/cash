package com.hiper.cash.domain;

import org.apache.commons.lang.StringUtils;

public class TmUsuario {

	private String id;
	private String nombre;
	private String apellido;	
	private String tarjeta;
	
	public TmUsuario() {
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}
	
	@Override
	public String toString() {
		return new StringBuilder(StringUtils.trimToEmpty(this.nombre)).append(" ").append(StringUtils.trimToEmpty(apellido)).toString();		
	}
	
	
}

package com.hiper.cash.entidad;

import java.io.Serializable;

public class BeanUsuarioCaptura implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nombres;
	private String apellidos;
	private String nroTarjeta;
	private String tipoDoc;
	private String nroDoc;
	private String email;
	private String operador;
	private String nroMovil;
	private String idEmpresa;
	private String flagMigra;
	

	public BeanUsuarioCaptura() {

		this.nombres = "";
		this.apellidos = "";
		this.nroTarjeta = "";
		this.tipoDoc = "";
		this.nroDoc = "";
		this.email = "";
		this.operador = "";
		this.nroMovil = "";
		this.idEmpresa= "";
		this.flagMigra= "";
	}

	public String getNroTarjeta() {
		return nroTarjeta;
	}

	public void setNroTarjeta(String nroTarjeta) {
		this.nroTarjeta = nroTarjeta;
	}

	public String getTipoDoc() {
		return tipoDoc;
	}

	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}

	public String getNroDoc() {
		return nroDoc;
	}

	public void setNroDoc(String nroDoc) {
		this.nroDoc = nroDoc;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public String getNroMovil() {
		return nroMovil;
	}

	public void setNroMovil(String nroMovil) {
		this.nroMovil = nroMovil;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(String idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getFlagMigra() {
		return flagMigra;
	}

	public void setFlagMigra(String flagMigra) {
		this.flagMigra = flagMigra;
	}

}

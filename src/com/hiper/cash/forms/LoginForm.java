package com.hiper.cash.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class LoginForm extends ActionForm {

	private String numeroTarjeta;
	private String contrasegnia;	
	private String validaClaveSeis;

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		return super.validate(mapping, request);
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public String getContrasegnia() {
		return contrasegnia;
	}

	public void setContrasegnia(String contrasegnia) {
		this.contrasegnia = contrasegnia;
	}

	public String getValidaClaveSeis() {
		return validaClaveSeis;
	}

	public void setValidaClaveSeis(String validaClaveSeis) {
		this.validaClaveSeis = validaClaveSeis;
	}
	
	

}

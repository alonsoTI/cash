package com.hiper.cash.domain;

import java.util.Date;

public class TxDisponibilidad {

	private String id;
	private Date fInicio;
	private Date fFinal;
	
	public TxDisponibilidad() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getfInicio() {
		return fInicio;
	}

	public void setfInicio(Date fInicio) {
		this.fInicio = fInicio;
	}

	public Date getfFinal() {
		return fFinal;
	}

	public void setfFinal(Date fFinal) {
		this.fFinal = fFinal;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return id.toString();
	}
	
	
}

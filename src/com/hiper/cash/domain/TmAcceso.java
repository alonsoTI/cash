package com.hiper.cash.domain;

import java.util.Date;

public class TmAcceso {
	private String tarjeta;
	private Date fechaAcceso;
	private int numeroIntento;
	private int numeroBloqueo;
		
	public TmAcceso() {
		numeroIntento=0;
		fechaAcceso=new Date();
	}
	
	 
	
	public String getTarjeta() {
		return tarjeta;
	}


	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}


	public Date getFechaAcceso() {
		return fechaAcceso;
	}


	public void setFechaAcceso(Date fechaAcceso) {
		this.fechaAcceso = fechaAcceso;
	}


	public int getNumeroIntento() {
		return numeroIntento;
	}


	public void setNumeroIntento(int numeroIntento) {
		this.numeroIntento = numeroIntento;
	}
	
	
	public void setNumeroBloqueo(int numeroBloqueo) {
		this.numeroBloqueo = numeroBloqueo;
	}
	
	public int getNumeroBloqueo() {
		return numeroBloqueo;
	}
	
	@Override
	public String toString() {
		return new StringBuilder(this.tarjeta).append(" - ").append(this.numeroIntento).append(" - ").append(this.fechaAcceso.toString()).toString();
	}
	
	
}

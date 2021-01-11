package com.financiero.cash.util;

public enum TipoTransferencia {	
	
	CP("11","CASH-PROP","Propias","Transferencia Cuentas Propias","cuentas propias"),
	CT("12","CASH-TERC","Terceros","Transferencia Cuentas Terceros","cuentas de terceros"),
	IT("15","CM-IT","Interbancarias","Transferencia Interbancaria","interbancarias");
	
	private String valor;
	private String glosa;	
	private String nombre;
	private String nombreCompleto;
	private String nombreAbreviado;

	private TipoTransferencia(String v,String g,String n,String c, String x) {
		this.valor =v;
		this.glosa = g;
		this.nombre = n;
		this.nombreCompleto =  c;
		this.nombreAbreviado= x;
	}
	
	public String getValor() {
		return valor;
	}
	
	public String getGlosa() {
		
		return glosa;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public static TipoTransferencia getTipo(String valor){
		for( TipoTransferencia tipo : values()){
			if( tipo.getValor().equals(valor)){
				return tipo;
			}
		}
		return null;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public String getNombreAbreviado() {
		return nombreAbreviado;
	}
	
	public String getName(){
		return name();
	}
	
	@Override
	public String toString() {
		return nombre;
	}
	
}

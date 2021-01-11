package com.financiero.cash.util;

public enum TipoMoneda {
	USD("US$","Dolares"),PEN("S/.","Soles");
	
	private String valor;
	private String label;
	
	private TipoMoneda(String valor, String label) {
		this.valor = valor;
		this.label = label;
	}
	
	public String getValor() {
		return valor;
	}
	
	public static String getSimbolo(String codigo){
		for( TipoMoneda tipo : values() ){
			if( tipo.name().equals(codigo)){
				return tipo.getValor();
			}
		}
		return ""; 
	}
	
	public static String validarCodigoMonedaValido(String codigo){
		for( TipoMoneda tipo : values() ){
			if( tipo.name().equals(codigo)){
				return tipo.name();
			}
		}
		return "";
	}
	
	public static String getLabelMoneda(String codigo){
		for( TipoMoneda tipo : values() ){
			if( tipo.name().equals(codigo)){
				return tipo.label;
			}
		}		
		return "";
	}
	
	
	public boolean esCodigo(String codigo){
		return codigo.equals(name());
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
}

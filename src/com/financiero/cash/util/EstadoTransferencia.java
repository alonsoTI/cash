package com.financiero.cash.util;

import java.util.ArrayList;
import java.util.List;

public enum EstadoTransferencia {
	
	PROCESADO('1',"Procesado"),
	PENDIENTE_APROBACION('2',"Pendiente"),
	VENCIDO('4',"Vencido"),
	RECHAZADO('3',"Rechazado");
	
	private char valor;
	private String nombre;
		
	EstadoTransferencia(char v,String n){
		this.valor=v;
		this.nombre = n;
	}	
	
	
	public char getValor() {
		return valor;
	}
	
	public String getStrValor() {
		return String.valueOf(this.valor);
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public static EstadoTransferencia getEstado(char e){
		for(EstadoTransferencia estado : values() ){
			if(estado.valor == e){
				return estado;
			}
		}
		return null;
	}
	
	public static String getNombre(char est){
		for( EstadoTransferencia estado : values()){
			if( estado.getValor() == est){
				return estado.getNombre();
			}
		}
		return "";
	}
	
	public static List<String> getValores(){
		List<String> valores = new ArrayList<String>();
		for( EstadoTransferencia estado : values()){
			valores.add(String.valueOf(estado.valor));
		}
		return valores;
	}
	
	
	@Override
	public String toString() {
		return nombre;
	}
	
}

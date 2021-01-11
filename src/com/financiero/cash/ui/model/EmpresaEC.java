package com.financiero.cash.ui.model;

public class EmpresaEC {
	private String id;
	private String descripcion;
	
	public EmpresaEC() {
		// TODO Auto-generated constructor stub
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.descripcion;
	}
	
}

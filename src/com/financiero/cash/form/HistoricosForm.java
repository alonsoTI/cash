package com.financiero.cash.form;

import org.apache.struts.action.ActionForm;

public class HistoricosForm extends ActionForm{

	
	private static final long serialVersionUID = 1L;
	
	private String empresa;
	private String servicio;
	private String fechaInicial;
	private String fechaFinal;
	private String referencia;
	
	private String empresaId;
	private String servicioId;
	private String orden;


	public HistoricosForm() {
	}
	
	public String getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	
	public String getServicio() {
		return servicio;
	}
	
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
	public void setFechaInicial(String fechaInicio) {
		this.fechaInicial = fechaInicio;
	}
	
	
	public String getFechaFinal() {
		return fechaFinal;
	}
	
	public String getFechaInicial() {
		return fechaInicial;
	}
	
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	
	public String getReferencia() {
		return referencia;
	}
	
	public void setEmpresaId(String empresaId) {
		this.empresaId = empresaId;
	}
	
	public String getEmpresaId() {
		return empresaId;
	}
	
	public void setServicioId(String servicioId) {
		this.servicioId = servicioId;
	}
	
	public String getServicioId() {
		return servicioId;
	}
	
	public void setOrden(String orden) {
		this.orden = orden;
	}
	
	public String getOrden() {
		return orden;
	}
	
}

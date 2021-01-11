package com.financiero.cash.ui.model;

import java.util.Date;

import com.hiper.cash.util.Util;


public class OrdenHistoricoEC {
	
	private Integer idSobre;
	private String nombre;
	private String descripcion;
	private String referencia;
	private Integer nroItems;
	private Date fechaCreacion;
	private Double valorSobre;
	private String estadoSobre;
	private String cuenta;
	private Date fechaInicio;
	private Date fechaVencimiento;
	private String codigoMoneda;
	private String mensajeProceso;
	
	public OrdenHistoricoEC() {
	}

	public Integer getIdSobre() {
		return idSobre;
	}

	public void setIdSobre(Integer idSobre) {
		this.idSobre = idSobre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Integer getNroItems() {
		return nroItems;
	}

	public void setNroItems(Integer nroItems) {
		this.nroItems = nroItems;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Double getValorSobre() {
		return valorSobre;
	}

	public void setValorSobre(Double valorSobre) {
		this.valorSobre = valorSobre;
	}

	public String getEstadoSobre() {
		return estadoSobre;
	}

	public void setEstadoSobre(String estadoSobre) {
		this.estadoSobre = estadoSobre;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getFechaInicio(){
		return Util.strFecha(this.fechaInicio);
	}
	/*
	public Date getFechaInicio() {
		return fechaInicio;
	}*/

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaVencimiento(){
		return Util.strFecha(this.fechaVencimiento);
	}
	/*public Date getFechaVencimiento() {
		return fechaVencimiento;
	}*/

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getCodigoMoneda() {
		return codigoMoneda;
	}

	public void setCodigoMoneda(String codigoMoneda) {
		this.codigoMoneda = codigoMoneda;
	}

	public String getMensajeProceso() {
		return mensajeProceso;
	}

	public void setMensajeProceso(String mensajeProceso) {
		this.mensajeProceso = mensajeProceso;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.idSobre.toString();
	}
}

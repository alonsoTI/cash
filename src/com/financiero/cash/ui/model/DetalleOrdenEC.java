package com.financiero.cash.ui.model;

import java.util.Date;

import static com.hiper.cash.util.Util.strFecha;

public class DetalleOrdenEC {
	
	private Integer idSobre;
	private Integer idDetalleOrden;
	private String contraPartida;
	private String referencia;
	private Double valor;
	private String moneda;
	private String mensajeProceso;
	private String pais;
	private Date fechaProceso;
	private String banco;
	private String cuenta;
	
	private String contraPartidaAdicional;
	private String referenciaAdicional;
	
	
	public DetalleOrdenEC() {		
	}
	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public void setMensajeProceso(String mensajeProceso) {
		this.mensajeProceso = mensajeProceso;
	}
	
	public String getMensajeProceso() {
		return mensajeProceso;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getFechaProceso(){
		return strFecha(this.fechaProceso);
	}
	/*public Date getFechaProceso() {
		return fechaProceso;
	}*/

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
	
	public Integer getIdDetalleOrden() {
		return idDetalleOrden;
	}
	
	public void setIdDetalleOrden(Integer idDetalleOrden) {
		this.idDetalleOrden = idDetalleOrden;
	}
	
	public String getContraPartida() {
		return contraPartida;
	}
	public void setContraPartida(String contraPartida) {
		this.contraPartida = contraPartida;
	}
	
	public void setIdSobre(Integer idSobre) {
		this.idSobre = idSobre;
	}
	
	public Integer getIdSobre() {
		return idSobre;
	}
	
	public void setContraPartidaAdicional(String contraPartidaAdicional) {
		this.contraPartidaAdicional = contraPartidaAdicional;
	}
	
	public String getContraPartidaAdicional() {
		return contraPartidaAdicional;
	}
	
	public void setReferenciaAdicional(String referenciaAdicional) {
		this.referenciaAdicional = referenciaAdicional;
	}
	
	public String getReferenciaAdicional() {
		return referenciaAdicional;
	}
}

package com.hiper.cash.domain;

// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA

/**
 * TaAprobacionOrden generated by hbm2java 
 * 
 * Modificado el 27/06/2011 por Andy Q.
 */
public class TaAprobacionOrden implements java.io.Serializable {

	private TaAprobacionOrdenId id;
	private TaOrden taOrden;
	private Long caoidAprobador;
	private String faofechaAprobacion;
	private String hOrHoraRegistro;

	public TaAprobacionOrden() {
	}

	public TaAprobacionOrden(TaAprobacionOrdenId id, TaOrden taOrden) {
		this.id = id;
		this.taOrden = taOrden;
	}

	public TaAprobacionOrden(TaAprobacionOrdenId id, TaOrden taOrden,
			Long caoidAprobador, String faofechaAprobacion,
			String hOrHoraRegistro) {
		this.id = id;
		this.taOrden = taOrden;
		this.caoidAprobador = caoidAprobador;
		this.faofechaAprobacion = faofechaAprobacion;
		this.hOrHoraRegistro = hOrHoraRegistro;
	}

	public TaAprobacionOrdenId getId() {
		return this.id;
	}

	public void setId(TaAprobacionOrdenId id) {
		this.id = id;
	}

	public TaOrden getTaOrden() {
		return this.taOrden;
	}

	public void setTaOrden(TaOrden taOrden) {
		this.taOrden = taOrden;
	}

	public Long getCaoidAprobador() {
		return this.caoidAprobador;
	}

	public void setCaoidAprobador(Long caoidAprobador) {
		this.caoidAprobador = caoidAprobador;
	}

	public String getFaofechaAprobacion() {
		return this.faofechaAprobacion;
	}

	public void setFaofechaAprobacion(String faofechaAprobacion) {
		this.faofechaAprobacion = faofechaAprobacion;
	}

	public String gethOrHoraRegistro() {
		return hOrHoraRegistro;
	}

	public void sethOrHoraRegistro(String hOrHoraRegistro) {
		this.hOrHoraRegistro = hOrHoraRegistro;
	}

}
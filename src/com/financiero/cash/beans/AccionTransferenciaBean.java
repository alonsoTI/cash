package com.financiero.cash.beans;

import java.util.Date;

import com.financiero.cash.util.TipoMovimientoTransferencia;

public class AccionTransferenciaBean {
	private String codigoUsuario;
	private String nombreUsuario;
	private Date fecha;
	private TipoMovimientoTransferencia tipoMovimientoTransferencia;

	public AccionTransferenciaBean() {
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public TipoMovimientoTransferencia getTipoMovimientoTransferencia() {
		return tipoMovimientoTransferencia;
	}

	public void setTipoMovimientoTransferencia(TipoMovimientoTransferencia tipoMovimientoTransferencia) {
		this.tipoMovimientoTransferencia = tipoMovimientoTransferencia;
	}
	
	public String getCodigoTipoAccion(){	
		return String.valueOf(tipoMovimientoTransferencia.getCodigo());
	}

}

package com.financiero.cash.beans;

import java.util.List;

public class ConsultaTransferenciasBean {
	private long totalRegistroConsulta;
	private long totalRegistrosEnviados;
	private long posicionActual;
	private List<TransferenciaBean> transferencias;

	public long getTotalRegistroConsulta() {
		return totalRegistroConsulta;
	}

	public void setTotalRegistroConsulta(long totalRegistroConsulta) {
		this.totalRegistroConsulta = totalRegistroConsulta;
	}

	public long getTotalRegistrosEnviados() {
		return totalRegistrosEnviados;
	}

	public void setTotalRegistrosEnviados(long totalRegistrosEnviados) {
		this.totalRegistrosEnviados = totalRegistrosEnviados;
	}

	public long getPosicionActual() {
		return posicionActual;
	}

	public void setPosicionActual(long posicionActual) {
		this.posicionActual = posicionActual;
	}

	public List<TransferenciaBean> getTransferencias() {
		return transferencias;
	}

	public void setTransferencias(List<TransferenciaBean> transferencias) {
		this.transferencias = transferencias;
	}

}

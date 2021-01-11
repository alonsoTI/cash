package com.financiero.cash.util;

public enum AccionTransferencia {
	REGISTRAR('0'), APROBAR('1'), RECHAZAR('2');
	private char codigo;

	private AccionTransferencia(char codigo) {
		this.codigo = codigo;
	}

	public char getCodigo() {
		return codigo;
	}

	public void setCodigo(char codigo) {
		this.codigo = codigo;
	}
}

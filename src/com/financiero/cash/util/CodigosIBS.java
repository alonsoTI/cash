package com.financiero.cash.util;

public enum CodigosIBS {
	CUENTA_NO_EXISTE_1("2012"),
	CUENTA_NO_EXISTE_2("3007"),
	CUENTA_NO_EXISTE_NEW("0007")
	;
	private String codigo;

	private CodigosIBS(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}

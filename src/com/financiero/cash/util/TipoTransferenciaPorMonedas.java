package com.financiero.cash.util;

/**
 * 
 * @author luitoz
 *
 */
public enum TipoTransferenciaPorMonedas {
	MISMA_MONEDA('1'), SOLES_DOLARES('2'), DOLARES_SOLES('3');;
	private char value;

	TipoTransferenciaPorMonedas(char value) {
		this.value = value;
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}
}

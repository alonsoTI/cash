package com.financiero.cash.util;

public enum TipoMovimientoTransferencia {
	APROBACION('A'), RECHAZO('R');
	private char codigo;

	private TipoMovimientoTransferencia(char codigo) {
		this.codigo = codigo;
	}

	public char getCodigo() {
		return codigo;
	}

	public void setCodigo(char codigo) {
		this.codigo = codigo;
	}
	
	public static TipoMovimientoTransferencia getTipoMovimiento(char tipoMovimiento){
		for( TipoMovimientoTransferencia d :values() ){
			if( d.codigo ==tipoMovimiento){
				return d;
			}
		}
		return null;	
	}
}

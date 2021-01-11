package com.hiper.cash.util;


public enum EstadoCash {
	INGRESADO("0"), PROCESADO("1"), ERRADO("2"), VENCIDO("3"), COBRADO("4"), CANCELADO(
			"5"), COBRO_PARCIAL("6"), PENDIENTE_CONFIRMACION("7"), EXTORNADO(
			"8"), ARCHIVADO("9"), EN_PROCESO("E");
	
	private String codigo;

	EstadoCash(String codEstado) {
		this.codigo = codEstado;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public static EstadoCash getEnum(String codigo){		
		for(EstadoCash estado:EstadoCash.values()){
			if(estado.getCodigo().equals(codigo)){
				return estado;
			}
		}
		throw new RuntimeException("No se encontro un enum para el valor ingresado");
	}
}

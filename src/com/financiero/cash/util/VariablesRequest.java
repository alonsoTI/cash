package com.financiero.cash.util;

public enum VariablesRequest {
	TRANSFERENCIAS_PENDIENTES("transferencias_pendientes"),
	CONSULTA_TRANSFERENCIAS("consulta_transferencias"),
	LIMITES_TRANSFERENCIAS("map_limites_transferencias"),
	MENSAJE_EQUIVALENCIAS_MONTOS("mensajeMontoEquivalente"),
	MENSAJE_VALIDACION_CARGAS("mensaje_validacion"),
	MODULO("modulo"),
	ID_SERVICIO_EMPRESA("idSevxEmp"),
	RUC_EMPRESA("ruc_empresa");	
	private String descripcion;

	private VariablesRequest(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}

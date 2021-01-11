package com.financiero.cash.util;

public enum VariablesSession {
	PAGINACION_TRANSFERENCIAS_PENDIENTES("bean_paginacion_transferencias_pendientes"),
	PAGINACION_CONSULTA_TRANSFERENCIAS("bean_paginacion_consulta_transferencias"),
	EMPRESAS_TRANSFERENCIAS("empresasTransferencias"),
	CORRELATIVO_DIARIO_TRANSFERENCIAS("correlativo_diario_transferencias_value"),
	NOMBRE_EMPRESA_SELECCIONADA("nombre_empresa_seleccionada");
	private String descripcion;

	private VariablesSession(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}

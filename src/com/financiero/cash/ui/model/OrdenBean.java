package com.financiero.cash.ui.model;

import com.hiper.cash.domain.TaOrden;

public class OrdenBean {

	private Long nroOrden;
	
	public OrdenBean(TaOrden orden) {
		// TODO Auto-generated constructor stub
		this.nroOrden = orden.getId().getCorIdOrden();
	}
	
	public void setNroOrden(Long nroOrden) {
		this.nroOrden = nroOrden;
	}
	
	public Long getNroOrden() {
		return nroOrden;
	}
}

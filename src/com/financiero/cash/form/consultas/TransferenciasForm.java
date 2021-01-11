package com.financiero.cash.form.consultas;

import static com.hiper.cash.util.CashConstants.STR_DD_MM_YYYY;
import static com.hiper.cash.util.CashConstants.STR_YYYYMMDD;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;

import com.hiper.cash.util.Util;

public class TransferenciasForm extends ActionForm {

	private static final long serialVersionUID = 6981597730739617798L;
	private String estado;
	private String documento;
	private String nroDocumento;
	private String empresa;
	private String tipoTransferencia;
	private String moneda;
	private String fechaInicial;
	private String fechaFinal;

	public TransferenciasForm() {

	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public String getEmpresa() {
		return empresa;
	}

	public String getTipoTransferencia() {
		return tipoTransferencia;
	}

	public void setTipoTransferencia(String tipoTransferencia) {
		this.tipoTransferencia = tipoTransferencia;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getFechaInicialFormateadoYYYYMMDD() {
		if (StringUtils.isNotBlank(fechaInicial)) {
			Date dateFechaInicial = Util.obtenerFecha(fechaInicial,
					STR_DD_MM_YYYY);
			SimpleDateFormat formatter = new SimpleDateFormat(STR_YYYYMMDD);
			return formatter.format(dateFechaInicial);
		}
		return "";
	}

	public String getFechaFinalFormateadoYYYYMMDD() {
		if (StringUtils.isNotBlank(fechaFinal)) {
			Date dateFechaFinal = Util.obtenerFecha(fechaFinal, STR_DD_MM_YYYY);
			SimpleDateFormat formatter = new SimpleDateFormat(STR_YYYYMMDD);
			return formatter.format(dateFechaFinal);
		}
		return "";
	}

}

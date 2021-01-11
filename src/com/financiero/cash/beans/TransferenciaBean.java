package com.financiero.cash.beans;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.financiero.cash.ibs.util.TipoDocumento;
import com.financiero.cash.util.EstadoTransferencia;
import com.financiero.cash.util.TipoMoneda;
import com.financiero.cash.util.TipoTransferencia;
import com.financiero.cash.util.TipoTransferenciaPorMonedas;

public class TransferenciaBean {

	private long numero;
	private EstadoTransferencia estado;
	private String cuentaCargo;
	private String tipoCuentaCargo;
	private String monedaCuentaCargo;
	private String descripcionCuentaCargo;
	private String cuentaAbono;
	private String tipoCuentaAbono;
	private String monedaCuentaAbono;
	private String descripcionCuentaAbono;
	private double monto;
	private double montoAbonado;

	private Date fechaRegistro;
	private String documento;
	private Character codigoTipoDocumento;
	private String nroDocumento;
	private String nombres;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String moneda;
	private String codigoMoneda;
	private String labelMoneda;
	private TipoTransferencia tipo;

	private long servicio;
	private long idEmpresa;
	private String referencia;
	private String usuarioRegistro;
	private String nombreUsuarioRegistro;

	private String mensajeError;
	private String codigoError;
	private String direccion;
	private String telefono;
	private String nombreBanco;

	private char mismo = '0';

	private List<AccionTransferenciaBean> acciones;

	private long numeroAprobadores;
	private boolean procesado = false;

	private double tipoCambioRegistro;

	public TransferenciaBean() {
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	public long getNumero() {
		return numero;
	}

	public void setEstado(char estado) {
		setEstado(EstadoTransferencia.getEstado(estado));
	}

	public void setEstado(EstadoTransferencia estado) {
		this.estado = estado;
	}

	public EstadoTransferencia getEstado() {
		return estado;
	}

	public void setCuentaCargo(String cuentaCargo) {
		this.cuentaCargo = cuentaCargo;
	}

	public String getCuentaCargo() {
		return cuentaCargo;
	}

	public void setCuentaAbono(String cuentaAbono) {
		this.cuentaAbono = cuentaAbono;
	}

	public String getCuentaAbono() {
		return cuentaAbono;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public double getMonto() {
		return monto;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	// descripcion del tipo de documento
	public String getDocumento() {
		if (StringUtils.isEmpty(documento)) {
			if (codigoTipoDocumento != null) {
				return TipoDocumento
						.getDescripcionTipoDocumento(codigoTipoDocumento);
			}
			return null;
		} else {
			return documento;
		}
	}

	public TipoDocumento getTipoDocumento() {
		return TipoDocumento.getTipoDocumento(documento.charAt(0));
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setServicio(long servicio) {
		this.servicio = servicio;
	}

	public long getServicio() {
		return servicio;
	}

	public void setCodigoMoneda(String codigoMoneda) {
		this.codigoMoneda = TipoMoneda.validarCodigoMonedaValido(codigoMoneda);
		setMoneda(TipoMoneda.getSimbolo(codigoMoneda));
		labelMoneda = TipoMoneda.getLabelMoneda(codigoMoneda);
	}

	/**
	 * example: PEN, USD
	 * 
	 * @return
	 */
	public String getCodigoMoneda() {
		return codigoMoneda;
	}

	public void setTipo(String tipo) {
		setTipo(TipoTransferencia.getTipo(tipo));
	}

	public void setTipo(TipoTransferencia tipo) {
		this.tipo = tipo;
	}

	public TipoTransferencia getTipo() {
		return tipo;
	}

	public void setNombreUsuarioRegistro(String nombreUsuarioRegistro) {
		this.nombreUsuarioRegistro = nombreUsuarioRegistro;
	}

	public String getNombreUsuarioRegistro() {
		return nombreUsuarioRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setMismo(char mismo) {
		this.mismo = mismo;
	}

	public char getMismo() {
		return mismo;
	}

	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	public String getCodigoError() {
		return codigoError;
	}

	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

	public String getNombreBanco() {
		return nombreBanco;
	}

	public long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public long getNumeroAprobadores() {
		return numeroAprobadores;
	}

	public void setNumeroAprobadores(long numeroAprobadores) {
		this.numeroAprobadores = numeroAprobadores;
	}

	public Character getCodigoTipoDocumento() {
		return codigoTipoDocumento;
	}

	public void setCodigoTipoDocumento(Character codigoTipoDocumento) {
		this.codigoTipoDocumento = codigoTipoDocumento;
	}

	public String getDescripcionCuentaCargo() {
		return descripcionCuentaCargo;
	}

	public void setDescripcionCuentaCargo(String descripcionCuentaCargo) {
		this.descripcionCuentaCargo = descripcionCuentaCargo;
	}

	public String getDescripcionCuentaAbono() {
		return descripcionCuentaAbono;
	}

	public void setDescripcionCuentaAbono(String descripcionCuentaAbono) {
		this.descripcionCuentaAbono = descripcionCuentaAbono;
	}

	public String getTipoCuentaCargo() {
		return tipoCuentaCargo;
	}

	public void setTipoCuentaCargo(String tipoCuentaCargo) {
		this.tipoCuentaCargo = tipoCuentaCargo;
	}

	public String getTipoCuentaAbono() {
		return tipoCuentaAbono;
	}

	public void setTipoCuentaAbono(String tipoCuentaAbono) {
		this.tipoCuentaAbono = tipoCuentaAbono;
	}

	public String getLabelMismo() {
		if (mismo == '0') {
			return "No";
		}
		if (mismo == '1') {
			return "Si";
		}
		return null;
	}

	public String getLabelMoneda() {
		return labelMoneda;
	}

	public void setLabelMoneda(String labelMoneda) {
		this.labelMoneda = labelMoneda;
	}

	public String getNombreBeneficiario() {
		return StringUtils.defaultString(apellidoPaterno) + " "
				+ StringUtils.defaultString(apellidoMaterno) + " "
				+ StringUtils.defaultString(nombres);
	}

	public String getCuentaCargoFormateado() {
		String cuentaCargoFormateado = "";
		if (StringUtils.isNotBlank(cuentaCargo)) {
			if (cuentaCargo.length() > 12) {
				cuentaCargoFormateado = cuentaCargo.substring(8);
			} else {
				cuentaCargoFormateado = cuentaCargo;
			}
		}
		return cuentaCargoFormateado;
	}

	public String getCuentaAbonoFormateado() {
		String cuentaAbonoFormateado = "";
		if (tipo != null) {
			if (tipo != TipoTransferencia.IT) {
				if (StringUtils.isNotBlank(cuentaAbono)) {
					if (cuentaAbono.length() > 12) {
						cuentaAbonoFormateado = cuentaAbono.substring(8);
					} else {
						cuentaAbonoFormateado = cuentaAbono;
					}
				}
			} else {
				cuentaAbonoFormateado = cuentaAbono;
			}
		}
		return cuentaAbonoFormateado;
	}

	public String getCodigoTipo() {
		return tipo.name();
	}

	public boolean isProcesado() {
		return procesado;
	}

	public void setProcesado(boolean procesado) {
		this.procesado = procesado;
	}

	public List<AccionTransferenciaBean> getAcciones() {
		return acciones;
	}

	public void setAcciones(List<AccionTransferenciaBean> acciones) {
		this.acciones = acciones;
	}

	public String getMonedaCuentaCargo() {
		return monedaCuentaCargo;
	}

	public void setMonedaCuentaCargo(String monedaCuentaCargo) {
		this.monedaCuentaCargo = monedaCuentaCargo;
	}

	public String getMonedaCuentaAbono() {
		return monedaCuentaAbono;
	}

	public void setMonedaCuentaAbono(String monedaCuentaAbono) {
		this.monedaCuentaAbono = monedaCuentaAbono;
	}

	public String getNombreEstado() {
		if (estado != null) {
			return estado.getNombre();
		} else {
			return "";
		}
	}

	public String debug() {
		String strDebug = (ToStringBuilder.reflectionToString(this));
		strDebug += " acciones: ";
		if (this.acciones != null && this.acciones.size() > 0) {
			for (AccionTransferenciaBean accion : this.acciones) {
				strDebug += ToStringBuilder.reflectionToString(accion);
			}
		}
		return strDebug;
	}

	public double getMontoAbonado() {
		return montoAbonado;
	}

	public void setMontoAbonado(double montoAbonado) {
		this.montoAbonado = montoAbonado;
	}

	public double getTipoCambioRegistro() {
		return tipoCambioRegistro;
	}

	public void setTipoCambioRegistro(double tipoCambioRegistro) {
		this.tipoCambioRegistro = tipoCambioRegistro;
	}

	/**
	 * si la moneda destino no existe(como en trx IB) que sea igual a la moneda
	 * origen. Usado en las pantallas de registro y aprobacion
	 * 
	 * @return
	 */
	public String getSimboloMonedaAbono() {
		String simboloMoneda = "";
		if (StringUtils.isNotBlank(monedaCuentaAbono)) {
			simboloMoneda = TipoMoneda.getSimbolo(monedaCuentaAbono);
		} else {
			simboloMoneda = moneda;
		}
		return simboloMoneda;
	}

	/**
	 * si la moneda destino no existe(como en trx IB) que no haya moneda de
	 * abono Usado en los listados de consultas
	 * 
	 * @return
	 */
	public String getSimboloMonedaAbonoConsultas() {
		return TipoMoneda.getSimbolo(monedaCuentaAbono);
	}

	public TipoTransferenciaPorMonedas getTipoTransferenciaPorMoneda() {
		TipoTransferenciaPorMonedas respuesta = TipoTransferenciaPorMonedas.MISMA_MONEDA;
		if (this.tipo == TipoTransferencia.IT) {
			return respuesta;
		}
		if (StringUtils.equals(this.getMonedaCuentaAbono(),
				this.getMonedaCuentaCargo())) {
			respuesta = TipoTransferenciaPorMonedas.MISMA_MONEDA;
		} else {
			if (TipoMoneda.PEN.esCodigo(this.getMonedaCuentaCargo())
					&& TipoMoneda.USD.esCodigo(this.getMonedaCuentaAbono())) {
				respuesta = TipoTransferenciaPorMonedas.SOLES_DOLARES;
			}
			if (TipoMoneda.USD.esCodigo(this.getMonedaCuentaCargo())
					&& TipoMoneda.PEN.esCodigo(this.getMonedaCuentaAbono())) {
				respuesta = TipoTransferenciaPorMonedas.DOLARES_SOLES;
			}
		}
		return respuesta;
	}

	/**
	 * Es de responsabilidad del metodo que invoca este metodo pasar el correcto
	 * tipo de cambio
	 * 
	 * @param tipoCambio
	 */
	public void calcularMontoAbonado(double tipoCambio) {
		TipoTransferenciaPorMonedas tipoPorMonedas = getTipoTransferenciaPorMoneda();
		if (tipoPorMonedas == TipoTransferenciaPorMonedas.MISMA_MONEDA) {
			montoAbonado = monto;
		}
		if (tipoPorMonedas == TipoTransferenciaPorMonedas.SOLES_DOLARES) {
			montoAbonado = this.getMonto() / tipoCambio;
		}
		if (tipoPorMonedas == TipoTransferenciaPorMonedas.DOLARES_SOLES) {
			montoAbonado = this.getMonto() * tipoCambio;
		}
	}

	public void calcularMontoAbonado() {
		TipoTransferenciaPorMonedas tipoPorMonedas = getTipoTransferenciaPorMoneda();
		if (tipoPorMonedas == TipoTransferenciaPorMonedas.MISMA_MONEDA) {
			montoAbonado = monto;
		}
	}
}

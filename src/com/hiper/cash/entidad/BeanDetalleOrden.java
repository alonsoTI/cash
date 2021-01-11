/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

import java.math.BigDecimal;

import com.hiper.cash.util.Util;

/**
 * 
 * @author esilva
 */
public class BeanDetalleOrden implements java.io.Serializable {
	private String m_IdOrden = "";
	private String m_IdEmpresa = "";
	private String m_IdServicio = "";
	private String m_IdDetalleOrden = "";

	private String m_Documento = "";
	private String m_Nombre = "";
	private String m_NumeroCuenta = "";
	private String m_IdTipoCuenta = "";
	private String m_DescTipoCuenta = "";
	private String m_Monto = "";
	private BigDecimal m_BigDecMonto; // En BigDecimal
	private String m_IdTipoMoneda = "";
	private String m_DescTipoMoneda = "";
	private String m_Telefono = "";
	private String m_Email = "";
	private String m_Descripcion = "";
	private String m_IDEstado = "";
	private String m_DescEstado = "";
	private String m_IdTipoPago = "";
	private String m_DescTipoPago = "";
	private String m_IdTipoDocumento = "";
	private String m_DescTipoDocumento = "";
	private String m_FechaProceso = "";

	private String m_CtaCargo = "";
	private String m_CtaAbono = "";
	private String m_MontoTransf = "";
	private String m_MonedaTransf = "";
	private String m_Referencia = "";

	// Transferencia Interbancaria
	private String m_CtaAbonoCci = "";
	private String m_BancoBenef = "";
	private String m_TipoDocBenef = "";
	private String m_NumDocBenef = "";
	private String m_NombreBenef = "";
	private String m_ApePatBenef = "";
	private String m_ApeMatBenef = "";
	private String m_DirBenef = "";
	private String m_TelefBenef = "";

	// EntidadEmpresa
	private String m_IdEntidad = "";
	private String m_IdServicioEntidad = "";
	private String m_IdTipo = "";
	private String m_DescTipo = "";

	// Pago Servicio
	private String m_NumRecibo = "";
	private String m_NomCliente = "";
	private String fechaEmision = "";

	private String m_OrdenRef = "";
	private String m_DetalleOrdenRef = "";

	// jwong 17/04/2009 letras
	private String m_CodAceptante = "";
	private String m_MonedaMora = "";
	private String m_MontoMora = "";

	// jmoreno 12/05/2009 nuevos campos
	private String m_ITF = "";
	private String m_Portes = "";
	private String m_MonedaITF = "";
	private String m_MonedaPortes = "";
	private String m_DescMonedaITF = "";
	private String m_DescMonedaPortes = "";
	// jmoreno 25-06-09
	private String m_MonedaProtesto = "";
	private String m_Protesto = "";
	// private String m_IdItemDetalle;
	private String m_IdPago = "";

	// jwong 03/06/2009 nuevo campo
	private String m_TipoServicio = "";

	// jwong 04/06/2009 nuevo campo para manejo de numero de cheque
	private String m_NumCheque = "";

	// jwong 08/06/2009 nuevo campo para manejo del monto principal(cancelacion
	// de letras)
	private String m_Principal = "";
	// jmoreno 21/07/09 nuevo campo para el manejo del codigo de respuesta del
	// IBS
	private String m_CodigoRptaIbs = "";
	private String m_descripcionCodIbs = "";
	// jmoreno 15-08-09 Para la longitud de la cuenta
	private int m_longitudCuenta = 0;
	// jmoreno 25-08-09
	private long m_ItemDetalle;
	// jmoreno 04/12/09
	private String m_montoComClienteChg = "";
	private String m_montoComEmpresaChg = "";
	


	public BeanDetalleOrden() {
		this.m_BigDecMonto = new BigDecimal(0);
	}

	public BeanDetalleOrden(long m_IdOrden, long m_IdServicio,
			long m_IdDetalleOrden, BigDecimal m_Monto, String m_IdTipoMoneda,
			String m_NomCliente, String m_NumRecibo, String m_Descripcion,
			String m_DescTipoMoneda/* ,long m_ItemDetalle */) {
		this.m_IdOrden = String.valueOf(m_IdOrden);
		this.m_IdServicio = String.valueOf(m_IdServicio);
		this.m_IdDetalleOrden = String.valueOf(m_IdDetalleOrden);
		this.m_BigDecMonto = (m_Monto != null) ? m_Monto : new BigDecimal(0);
		this.m_Monto = m_Monto.toString();
		this.m_IdTipoMoneda = m_IdTipoMoneda;
		this.m_NumRecibo = m_NumRecibo;
		this.m_NomCliente = m_NomCliente;
		this.m_Descripcion = m_Descripcion;
		this.m_DescTipoMoneda = m_DescTipoMoneda;
		// this.m_ItemDetalle = m_ItemDetalle;
	}

	public String getM_IdOrden() {
		return m_IdOrden;
	}

	public void setM_IdOrden(String m_IdOrden) {
		if (m_IdOrden != null) {
			this.m_IdOrden = m_IdOrden;
		} else {
			this.m_IdOrden = m_IdOrden;
		}

	}

	public String getM_IdEmpresa() {
		return m_IdEmpresa;
	}

	public void setM_IdEmpresa(String m_IdEmpresa) {
		if (m_IdEmpresa != null) {
			this.m_IdEmpresa = m_IdEmpresa;
		} else {
			this.m_IdEmpresa = "";
		}

	}

	public String getM_IdServicio() {
		return m_IdServicio;
	}

	public void setM_IdServicio(String m_IdServicio) {
		if (m_IdServicio != null) {
			this.m_IdServicio = m_IdServicio;
		} else {
			this.m_IdServicio = "";
		}

	}

	public String getM_IdDetalleOrden() {
		return m_IdDetalleOrden;
	}

	public void setM_IdDetalleOrden(String m_IdDetalleOrden) {
		if (m_IdDetalleOrden != null) {
			this.m_IdDetalleOrden = m_IdDetalleOrden;
		} else {
			this.m_IdDetalleOrden = "";
		}

	}

	public String getM_Documento() {
		return m_Documento;
	}

	public void setM_Documento(String m_Documento) {
		if (m_Documento != null) {
			this.m_Documento = m_Documento;
		} else {
			this.m_Documento = "";
		}

	}

	public String getM_Nombre() {
		return m_Nombre;
	}

	public void setM_Nombre(String m_Nombre) {
		if (m_Nombre != null) {
			this.m_Nombre = Util.strHTML(m_Nombre);
		} else {
			this.m_Nombre = "";
		}

	}

	public String getM_NumeroCuenta() {
		return m_NumeroCuenta;
	}

	public void setM_NumeroCuenta(String m_NumeroCuenta) {
		if (m_NumeroCuenta != null) {
			this.m_NumeroCuenta = m_NumeroCuenta;
		} else {
			this.m_NumeroCuenta = "";
		}

	}

	public String getM_IdTipoCuenta() {
		return m_IdTipoCuenta;
	}

	public void setM_IdTipoCuenta(String m_IdTipoCuenta) {
		if (m_IdTipoCuenta != null) {
			this.m_IdTipoCuenta = m_IdTipoCuenta;
		} else {
			this.m_IdTipoCuenta = "";
		}

	}

	public String getM_DescTipoCuenta() {
		return m_DescTipoCuenta;
	}

	public void setM_DescTipoCuenta(String m_DescTipoCuenta) {
		if (m_DescTipoCuenta != null) {
			this.m_DescTipoCuenta = m_DescTipoCuenta;
		} else {
			this.m_DescTipoCuenta = "";
		}

	}

	public String getM_Monto() {
		return m_Monto;
	}

	public void setM_Monto(String m_Monto) {
		if (m_Monto != null) {
			this.m_Monto = m_Monto;
		} else {
			this.m_Monto = "";
		}

	}

	public String getM_IdTipoMoneda() {
		return m_IdTipoMoneda;
	}

	public void setM_IdTipoMoneda(String m_IdTipoMoneda) {
		if (m_IdTipoMoneda != null) {
			this.m_IdTipoMoneda = m_IdTipoMoneda;
		} else {
			this.m_IdTipoMoneda = "";
		}

	}

	public String getM_DescTipoMoneda() {
		return m_DescTipoMoneda;
	}

	public void setM_DescTipoMoneda(String m_DescTipoMoneda) {
		if (m_DescTipoMoneda != null) {
			this.m_DescTipoMoneda = m_DescTipoMoneda;
		} else {
			this.m_DescTipoMoneda = m_DescTipoMoneda;
		}

	}

	public String getM_Telefono() {
		return m_Telefono;
	}

	public void setM_Telefono(String m_Telefono) {
		if (m_Telefono != null) {
			this.m_Telefono = m_Telefono;
		} else {
			this.m_Telefono = "";
		}

	}

	public String getM_Email() {
		return m_Email;
	}

	public void setM_Email(String m_Email) {
		if (m_Email != null) {
			this.m_Email = m_Email;
		} else {
			this.m_Email = "";
		}

	}

	public String getM_Descripcion() {
		return m_Descripcion;
	}

	public void setM_Descripcion(String m_Descripcion) {
		if (m_Descripcion != null) {
			this.m_Descripcion = m_Descripcion;
		} else {
			this.m_Descripcion = "";
		}

	}

	public String getM_IDEstado() {
		return m_IDEstado;
	}

	public void setM_IDEstado(String m_IDEstado) {
		if (m_IDEstado != null) {
			this.m_IDEstado = m_IDEstado;
		} else {
			this.m_IDEstado = "";
		}

	}

	public String getM_DescEstado() {
		return m_DescEstado;
	}

	public void setM_DescEstado(String m_DescEstado) {
		if (m_DescEstado != null) {
			this.m_DescEstado = m_DescEstado;
		} else {
			this.m_DescEstado = "";
		}

	}

	public String getM_IdTipoPago() {
		return m_IdTipoPago;
	}

	public void setM_IdTipoPago(String m_IdTipoPago) {
		if (m_IdTipoPago != null) {
			this.m_IdTipoPago = m_IdTipoPago;
		} else {
			this.m_IdTipoPago = "";
		}

	}

	public String getM_DescTipoPago() {
		return m_DescTipoPago;
	}

	public void setM_DescTipoPago(String m_DescTipoPago) {
		if (m_DescTipoPago != null) {
			this.m_DescTipoPago = m_DescTipoPago;
		} else {
			this.m_DescTipoPago = "";
		}

	}

	public String getM_IdTipoDocumento() {
		return m_IdTipoDocumento;
	}

	public void setM_IdTipoDocumento(String m_IdTipoDocumento) {
		if (m_IdTipoDocumento != null) {
			this.m_IdTipoDocumento = m_IdTipoDocumento;
		} else {
			this.m_IdTipoDocumento = "";
		}

	}

	public String getM_DescTipoDocumento() {
		return m_DescTipoDocumento;
	}

	public void setM_DescTipoDocumento(String m_DescTipoDocumento) {
		if (m_DescTipoDocumento != null) {
			this.m_DescTipoDocumento = m_DescTipoDocumento;
		} else {
			this.m_DescTipoDocumento = "";
		}

	}

	public String getM_IdEntidad() {
		return m_IdEntidad;
	}

	public void setM_IdEntidad(String m_IdEntidad) {
		if (m_IdEntidad != null) {
			this.m_IdEntidad = m_IdEntidad;
		} else {
			this.m_IdEntidad = "";
		}

	}

	public String getM_IdServicioEntidad() {
		return m_IdServicioEntidad;
	}

	public void setM_IdServicioEntidad(String m_IdServicioEntidad) {
		if (m_IdServicioEntidad != null) {
			this.m_IdServicioEntidad = m_IdServicioEntidad;
		} else {
			this.m_IdServicioEntidad = "";
		}

	}

	public String getM_DescTipo() {
		return m_DescTipo;
	}

	public void setM_DescTipo(String m_DescTipo) {
		if (m_DescTipo != null) {
			this.m_DescTipo = m_DescTipo;
		} else {
			this.m_DescTipo = "";
		}

	}

	public String getM_IdTipo() {
		return m_IdTipo;
	}

	public void setM_IdTipo(String m_IdTipo) {
		if (m_IdTipo != null) {
			this.m_IdTipo = m_IdTipo;
		} else {
			this.m_IdTipo = "";
		}

	}

	public String getM_FechaProceso() {
		return m_FechaProceso;
	}

	public void setM_FechaProceso(String m_FechaProceso) {
		if (m_FechaProceso != null) {
			this.m_FechaProceso = m_FechaProceso;
		} else {
			this.m_FechaProceso = "";
		}

	}

	public String getM_CtaCargo() {
		return m_CtaCargo;
	}

	public void setM_CtaCargo(String m_CtaCargo) {
		if (m_CtaCargo != null) {
			this.m_CtaCargo = m_CtaCargo;
		} else {
			this.m_CtaCargo = "";
		}

	}

	public String getM_CtaAbono() {
		return m_CtaAbono;
	}

	public void setM_CtaAbono(String m_CtaAbono) {
		if (m_CtaAbono != null) {
			this.m_CtaAbono = m_CtaAbono;
		} else {
			this.m_CtaAbono = "";
		}

	}

	public String getM_MontoTransf() {
		return m_MontoTransf;
	}

	public void setM_MontoTransf(String m_MontoTransf) {
		if (m_MontoTransf != null) {
			this.m_MontoTransf = m_MontoTransf;
		} else {
			this.m_MontoTransf = "";
		}

	}

	public String getM_MonedaTransf() {
		return m_MonedaTransf;
	}

	public void setM_MonedaTransf(String m_MonedaTransf) {
		if (m_MonedaTransf != null) {
			this.m_MonedaTransf = m_MonedaTransf;
		} else {
			this.m_MonedaTransf = "";
		}
	}

	public String getM_CtaAbonoCci() {
		return m_CtaAbonoCci;
	}

	public void setM_CtaAbonoCci(String m_CtaAbonoCci) {
		if (m_CtaAbonoCci != null) {
			this.m_CtaAbonoCci = m_CtaAbonoCci;
		} else {
			this.m_CtaAbonoCci = "";
		}

	}

	public String getM_BancoBenef() {
		return m_BancoBenef;
	}

	public void setM_BancoBenef(String m_BancoBenef) {
		if (m_BancoBenef != null) {
			this.m_BancoBenef = m_BancoBenef;
		} else {
			this.m_BancoBenef = "";
		}

	}

	public String getM_TipoDocBenef() {
		return m_TipoDocBenef;
	}

	public void setM_TipoDocBenef(String m_TipoDocBenef) {
		if (m_TipoDocBenef != null) {
			this.m_TipoDocBenef = m_TipoDocBenef;
		} else {
			this.m_TipoDocBenef = "";
		}
	}

	public String getM_NumDocBenef() {
		return m_NumDocBenef;
	}

	public void setM_NumDocBenef(String m_NumDocBenef) {
		if (m_NumDocBenef != null) {
			this.m_NumDocBenef = m_NumDocBenef;
		} else {
			this.m_NumDocBenef = "";
		}

	}

	public String getM_NombreBenef() {
		return m_NombreBenef;
	}

	public void setM_NombreBenef(String m_NombreBenef) {
		if (m_NombreBenef != null) {
			this.m_NombreBenef = m_NombreBenef;
		} else {
			this.m_NombreBenef = "";
		}

	}

	public String getM_ApePatBenef() {
		return m_ApePatBenef;
	}

	public void setM_ApePatBenef(String m_ApePatBenef) {
		if (m_ApePatBenef != null) {
			this.m_ApePatBenef = m_ApePatBenef;
		} else {
			this.m_ApePatBenef = "";
		}

	}

	public String getM_ApeMatBenef() {
		return m_ApeMatBenef;
	}

	public void setM_ApeMatBenef(String m_ApeMatBenef) {
		if (m_ApeMatBenef != null) {
			this.m_ApeMatBenef = m_ApeMatBenef;
		} else {
			this.m_ApeMatBenef = "";
		}

	}

	public String getM_DirBenef() {
		return m_DirBenef;
	}

	public void setM_DirBenef(String m_DirBenef) {
		if (m_DirBenef != null) {
			this.m_DirBenef = m_DirBenef;
		} else {
			this.m_DirBenef = "";
		}

	}

	public String getM_TelefBenef() {
		return m_TelefBenef;
	}

	public void setM_TelefBenef(String m_TelefBenef) {
		if (m_TelefBenef != null) {
			this.m_TelefBenef = m_TelefBenef;
		} else {
			this.m_TelefBenef = "";
		}

	}

	public String getM_NumRecibo() {
		return m_NumRecibo;
	}

	public void setM_NumRecibo(String m_NumRecibo) {
		if (m_NumRecibo != null) {
			this.m_NumRecibo = m_NumRecibo;
		} else {
			this.m_NumRecibo = "";
		}

	}

	public String getM_NomCliente() {
		return m_NomCliente;
	}

	public void setM_NomCliente(String m_NomCliente) {
		if (m_NomCliente != null) {
			this.m_NomCliente = m_NomCliente;
		} else {
			this.m_NomCliente = "";
		}

	}

	public BigDecimal getM_BigDecMonto() {
		return m_BigDecMonto;
	}

	public void setM_BigDecMonto(BigDecimal m_BigDecMonto) {
		this.m_BigDecMonto = m_BigDecMonto;
	}

	public String getM_OrdenRef() {
		return m_OrdenRef;
	}

	public void setM_OrdenRef(String m_OrdenRef) {
		if (m_OrdenRef != null) {
			this.m_OrdenRef = m_OrdenRef;
		} else {
			this.m_OrdenRef = "";
		}

	}

	public String getM_DetalleOrdenRef() {
		return m_DetalleOrdenRef;
	}

	public void setM_DetalleOrdenRef(String m_DetalleOrdenRef) {
		if (m_DetalleOrdenRef != null) {
			this.m_DetalleOrdenRef = m_DetalleOrdenRef;
		} else {
			this.m_DetalleOrdenRef = "";
		}

	}

	public String getM_Referencia() {
		return m_Referencia;
	}

	public void setM_Referencia(String m_Referencia) {
		if (m_Referencia != null) {
			this.m_Referencia = m_Referencia;
		} else {
			this.m_Referencia = "";
		}

	}

	/**
	 * @return the m_CodAceptante
	 */
	public String getM_CodAceptante() {
		return m_CodAceptante;
	}

	/**
	 * @param m_CodAceptante
	 *            the m_CodAceptante to set
	 */
	public void setM_CodAceptante(String m_CodAceptante) {
		if (m_CodAceptante != null) {
			this.m_CodAceptante = m_CodAceptante;
		} else {
			this.m_CodAceptante = "";
		}

	}

	/**
	 * @return the m_MonedaMora
	 */
	public String getM_MonedaMora() {
		return m_MonedaMora;
	}

	/**
	 * @param m_MonedaMora
	 *            the m_MonedaMora to set
	 */
	public void setM_MonedaMora(String m_MonedaMora) {
		if (m_MonedaMora != null) {
			this.m_MonedaMora = m_MonedaMora;
		} else {
			this.m_MonedaMora = "";
		}

	}

	/**
	 * @return the m_MontoMora
	 */
	public String getM_MontoMora() {
		return m_MontoMora;
	}

	/**
	 * @param m_MontoMora
	 *            the m_MontoMora to set
	 */
	public void setM_MontoMora(String m_MontoMora) {
		if (m_MontoMora != null) {
			this.m_MontoMora = m_MontoMora;
		} else {
			this.m_MontoMora = "";
		}

	}

	/**
	 * @return the m_ITF
	 */
	public String getM_ITF() {
		return m_ITF;
	}

	/**
	 * @param m_ITF
	 *            the m_ITF to set
	 */
	public void setM_ITF(String m_ITF) {
		if (m_ITF != null) {
			this.m_ITF = m_ITF;
		} else {
			this.m_ITF = "";
		}

	}

	/**
	 * @return the m_Portes
	 */
	public String getM_Portes() {
		return m_Portes;
	}

	/**
	 * @param m_Portes
	 *            the m_Portes to set
	 */
	public void setM_Portes(String m_Portes) {
		if (m_Portes != null) {
			this.m_Portes = m_Portes;
		} else {
			this.m_Portes = "";
		}

	}

	/**
	 * @return the m_MonedaITF
	 */
	public String getM_MonedaITF() {
		return m_MonedaITF;
	}

	/**
	 * @param m_MonedaITF
	 *            the m_MonedaITF to set
	 */
	public void setM_MonedaITF(String m_MonedaITF) {
		if (m_MonedaITF != null) {
			this.m_MonedaITF = m_MonedaITF;
		} else {
			this.m_MonedaITF = "";
		}

	}

	/**
	 * @return the m_MonedaPortes
	 */
	public String getM_MonedaPortes() {
		return m_MonedaPortes;
	}

	/**
	 * @param m_MonedaPortes
	 *            the m_MonedaPortes to set
	 */
	public void setM_MonedaPortes(String m_MonedaPortes) {
		if (m_MonedaPortes != null) {
			this.m_MonedaPortes = m_MonedaPortes;
		} else {
			this.m_MonedaPortes = "";
		}

	}

	/**
	 * @return the m_DescMonedaITF
	 */
	public String getM_DescMonedaITF() {
		return m_DescMonedaITF;
	}

	/**
	 * @param m_DescMonedaITF
	 *            the m_DescMonedaITF to set
	 */
	public void setM_DescMonedaITF(String m_DescMonedaITF) {
		if (m_DescMonedaITF != null) {
			this.m_DescMonedaITF = m_DescMonedaITF;
		} else {
			this.m_DescMonedaITF = "";
		}

	}

	/**
	 * @return the m_DescMonedaPortes
	 */
	public String getM_DescMonedaPortes() {
		return m_DescMonedaPortes;
	}

	/**
	 * @param m_DescMonedaPortes
	 *            the m_DescMonedaPortes to set
	 */
	public void setM_DescMonedaPortes(String m_DescMonedaPortes) {
		if (m_DescMonedaPortes != null) {
			this.m_DescMonedaPortes = m_DescMonedaPortes;
		} else {
			this.m_DescMonedaPortes = "";
		}

	}

	public String getM_IdPago() {
		return m_IdPago;
	}

	/**
	 * @param m_IdPago
	 *            the m_IdPago to set
	 */
	public void setM_IdPago(String m_IdPago) {
		if (m_IdPago != null) {
			this.m_IdPago = m_IdPago;
		} else {
			this.m_IdPago = "";
		}

	}

	/**
	 * @return the m_TipoServicio
	 */
	public String getM_TipoServicio() {
		return m_TipoServicio;
	}

	/**
	 * @param m_TipoServicio
	 *            the m_TipoServicio to set
	 */
	public void setM_TipoServicio(String m_TipoServicio) {
		if (m_TipoServicio != null) {
			this.m_TipoServicio = m_TipoServicio;
		} else {
			this.m_TipoServicio = "";
		}

	}

	/**
	 * @return the m_NumCheque
	 */
	public String getM_NumCheque() {
		return m_NumCheque;
	}

	/**
	 * @param m_NumCheque
	 *            the m_NumCheque to set
	 */
	public void setM_NumCheque(String m_NumCheque) {
		if (m_NumCheque != null) {
			this.m_NumCheque = m_NumCheque;
		} else {
			this.m_NumCheque = "";
		}

	}

	/**
	 * @return the m_Principal
	 */
	public String getM_Principal() {
		return m_Principal;
	}

	/**
	 * @param m_Principal
	 *            the m_Principal to set
	 */
	public void setM_Principal(String m_Principal) {
		if (m_Principal != null) {
			this.m_Principal = m_Principal;
		} else {
			this.m_Principal = "";
		}

	}

	public String getM_MonedaProtesto() {
		return m_MonedaProtesto;
	}

	public void setM_MonedaProtesto(String m_MonedaProtesto) {
		if (m_MonedaProtesto != null) {
			this.m_MonedaProtesto = m_MonedaProtesto;
		} else {
			this.m_MonedaProtesto = "";
		}

	}

	public String getM_Protesto() {
		return m_Protesto;
	}

	public void setM_Protesto(String m_Protesto) {
		if (m_Protesto != null) {
			this.m_Protesto = m_Protesto;
		} else {
			this.m_Protesto = "";
		}

	}

	public String getM_CodigoRptaIbs() {
		return m_CodigoRptaIbs;
	}

	public void setM_CodigoRptaIbs(String m_CodigoRptaIbs) {
		if (m_CodigoRptaIbs != null) {
			this.m_CodigoRptaIbs = m_CodigoRptaIbs;
		} else {
			this.m_CodigoRptaIbs = "";
		}

	}

	public String getM_descripcionCodIbs() {
		return m_descripcionCodIbs;
	}

	public void setM_descripcionCodIbs(String m_descripcionCodIbs) {
		if (m_descripcionCodIbs != null) {
			this.m_descripcionCodIbs = m_descripcionCodIbs;
		} else {
			this.m_descripcionCodIbs = "";
		}

	}

	public int getM_longitudCuenta() {
		return m_longitudCuenta;
	}

	public void setM_longitudCuenta(int m_longitudCuenta) {
		this.m_longitudCuenta = m_longitudCuenta;
	}

	public long getM_ItemDetalle() {
		return m_ItemDetalle;
	}

	public void setM_ItemDetalle(long m_ItemDetalle) {
		this.m_ItemDetalle = m_ItemDetalle;
	}

	public String getM_montoComClienteChg() {
		return m_montoComClienteChg;
	}

	public void setM_montoComClienteChg(String m_montoComCliente) {
		this.m_montoComClienteChg = m_montoComCliente;
	}

	public String getM_montoComEmpresaChg() {
		return m_montoComEmpresaChg;
	}

	public void setM_montoComEmpresaChg(String m_montoComEmpresa) {
		this.m_montoComEmpresaChg = m_montoComEmpresa;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}
	
	public String getFechaEmision() {
		return fechaEmision;
	}
	

	
}

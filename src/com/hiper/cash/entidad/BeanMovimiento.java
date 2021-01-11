/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

import com.hiper.cash.util.Util;

/**
 * BeanMovimiento
 * @version 1.0 04/01/2010
 * @author jmoreno
 * Copyright © HIPER S.A 
 */
public class BeanMovimiento {
    private String m_FechaProceso = "";
    private String m_Pais = "";
    private String m_Banco = "";
    private String m_FormaPago = "";
    private String m_CuentaDestino = "";
    private String m_Contrapartida = "";
    private String m_Nombre = "";
    private String m_ValorEnviado = "";//monto bruto
    private String m_ValorPro = "";//monto ventanilla
    private String m_ValorNeto = "";
    private String m_Moneda = "";
    private String m_Estado = "";
    private String m_Referencia = "";
    private String m_IdOrden = "";
    private String m_Comprobante = "";
    private String m_Descripcion = "";
    private String m_Desglose = "";
    private String m_NroRefBanco = "";
    private String m_Cuenta = "";
    private String m_NroDocumento = "";
    private String m_Oficina = "";
    private String m_Empresa = "";
    private String m_codorientacion="";

    public String getM_codorientacion() {
		return m_codorientacion;
	}

	public void setM_codorientacion(String mCodorientacion) {
		m_codorientacion = mCodorientacion;
	}

	public String getM_FechaProceso() {
        return m_FechaProceso;
    }

    public void setM_FechaProceso(String m_FechaProceso) {
        this.m_FechaProceso = m_FechaProceso;
    }

    public String getM_Pais() {
        return m_Pais;
    }

    public void setM_Pais(String m_Pais) {
        this.m_Pais = m_Pais;
    }

    public String getM_Banco() {
        return m_Banco;
    }

    public void setM_Banco(String m_Banco) {
        this.m_Banco = m_Banco;
    }

    public String getM_FormaPago() {
        return m_FormaPago;
    }

    public void setM_FormaPago(String m_FormaPago) {
        this.m_FormaPago = m_FormaPago;
    }

    public String getM_CuentaDestino() {
        return m_CuentaDestino;
    }

    public void setM_CuentaDestino(String m_CuentaDestino) {
        this.m_CuentaDestino = m_CuentaDestino;
    }

    public String getM_Contrapartida() {
        return m_Contrapartida;
    }

    public void setM_Contrapartida(String m_Contrapartida) {
        this.m_Contrapartida = m_Contrapartida;
    }

    public String getM_Nombre() {
        return m_Nombre;
    }

    public void setM_Nombre(String m_Nombre) {
        this.m_Nombre = m_Nombre;
    }

    public String getM_ValorEnviado() {
        return m_ValorEnviado;
    }

    public void setM_ValorEnviado(String m_ValorEnviado) {
        this.m_ValorEnviado = m_ValorEnviado;
    }

    public String getM_ValorPro() {
        return m_ValorPro;
    }

    public void setM_ValorPro(String m_ValorPro) {
        this.m_ValorPro = m_ValorPro;
    }

    public String getM_ValorNeto() {
        return m_ValorNeto;
    }

    public void setM_ValorNeto(String m_ValorNeto) {
        this.m_ValorNeto = m_ValorNeto;
    }

    public String getM_Moneda() {
        return m_Moneda;
    }

    public void setM_Moneda(String m_Moneda) {
        this.m_Moneda = m_Moneda;
    }

    public String getM_Estado() {
        return m_Estado;
    }

    public void setM_Estado(String m_Estado) {
        this.m_Estado = m_Estado;
    }

    public String getM_Referencia() {
        return m_Referencia;
    }

    public void setM_Referencia(String m_Referencia) {
    	this.m_Referencia = Util.strHTML(m_Referencia);       
    }

    public String getM_IdOrden() {
        return m_IdOrden;
    }

    public void setM_IdOrden(String m_IdOrden) {
        this.m_IdOrden = m_IdOrden;
    }

    public String getM_Comprobante() {
        return m_Comprobante;
    }

    public void setM_Comprobante(String m_Comprobante) {
        this.m_Comprobante = m_Comprobante;
    }

    public String getM_Descripcion() {
        return m_Descripcion;
    }

    public void setM_Descripcion(String m_Descripcion) {
        this.m_Descripcion = m_Descripcion;
    }

    public String getM_Desglose() {
        return m_Desglose;
    }

    public void setM_Desglose(String m_Desglose) {
        this.m_Desglose = m_Desglose;
    }

    public String getM_Cuenta() {
        return m_Cuenta;
    }

    public void setM_Cuenta(String m_Cuenta) {
        this.m_Cuenta = m_Cuenta;
    }

    public String getM_NroDocumento() {
        return m_NroDocumento;
    }

    public void setM_NroDocumento(String m_NroDocumento) {
        this.m_NroDocumento = m_NroDocumento;
    }

    public String getM_Oficina() {
        return m_Oficina;
    }

    public void setM_Oficina(String m_Oficina) {
        this.m_Oficina = m_Oficina;
    }

    public String getM_Empresa() {
        return m_Empresa;
    }

    public void setM_Empresa(String m_Empresa) {
        this.m_Empresa = m_Empresa;
    }

    public String getM_NroRefBanco() {
        return m_NroRefBanco;
    }

    public void setM_NroRefBanco(String m_NroRefBanco) {
        this.m_NroRefBanco = m_NroRefBanco;
    }

}

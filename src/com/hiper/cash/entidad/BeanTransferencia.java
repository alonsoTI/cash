/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

/**
 *
 * @author esilva
 */
public class BeanTransferencia {

    private String m_Empresa = "";
    private String m_Servicio = "";

    private String m_CtaCargo = "";
    private String m_CtaTypCargo = "";
    private String m_CtaAbono = "";
    private String m_CtaTypAbono = "";
    private String m_Monto = "";
    private String m_Moneda = "";
    private String m_Referencia = "";
    
    private String m_NumCuentaAbonoCci = "";
    private String m_BancoBenef = "";
    private String m_TipoDocBenef = "";
    private String m_NumDocBenef = "";
    private String m_NombreBenef = "";
    private String m_ApePatBenef = "";
    private String m_ApeMatBenef = "";
    private String m_DireccionBenef = "";
    private String m_TlfBenef = "";

    private String m_CtaAbonoEntidad = "";
    private String m_CtaAbonoOficina = "";
    private String m_CtaAbonoCuenta = "";
    private String m_CtaAbonoControl = "";

    private String m_Modulo = "";
    private String m_SubModulo = "";

    private String m_TipoOper = "";

    //jwong 08/05/2009
    private String m_IdTipoDocBenef = "";

    //jwong 12/05/2009
    private String m_FlagCliente = "";
    //jmoreno 21/07/09
    private String m_NombreBanco = "";
    private String m_MontoMostrar = "";
    
    private String m_RazonSocial ="";
    
    private String titulaCuentaCargo = "";

    public String getM_Empresa() {
        return m_Empresa;
    }

    public void setM_Empresa(String m_Empresa) {
        this.m_Empresa = m_Empresa;
    }

    public String getM_Servicio() {
        return m_Servicio;
    }

    public void setM_Servicio(String m_Servicio) {
        this.m_Servicio = m_Servicio;
    }

    public String getM_CtaCargo() {
        return m_CtaCargo;
    }

    public void setM_CtaCargo(String m_CtaCargo) {
        this.m_CtaCargo = m_CtaCargo;
    }

    public String getM_CtaAbono() {
        return m_CtaAbono;
    }

    public void setM_CtaAbono(String m_CtaAbono) {
        this.m_CtaAbono = m_CtaAbono;
    }

    public String getM_Monto() {
        return m_Monto;
    }

    public void setM_Monto(String m_Monto) {
        this.m_Monto = m_Monto;
    }

    public String getM_Moneda() {
        return m_Moneda;
    }

    public void setM_Moneda(String m_Moneda) {
        this.m_Moneda = m_Moneda;
    }

    public String getM_Referencia() {
        return m_Referencia;
    }

    public void setM_Referencia(String m_Referencia) {
        this.m_Referencia = m_Referencia;
    }

    public String getM_NumCuentaAbonoCci() {
        return m_NumCuentaAbonoCci;
    }

    public void setM_NumCuentaAbonoCci(String m_NumCuentaAbonoCci) {
        this.m_NumCuentaAbonoCci = m_NumCuentaAbonoCci;
    }

    public String getM_BancoBenef() {
        return m_BancoBenef;
    }

    public void setM_BancoBenef(String m_BancoBenef) {
        this.m_BancoBenef = m_BancoBenef;
    }

    public String getM_TipoDocBenef() {
        return m_TipoDocBenef;
    }

    public void setM_TipoDocBenef(String m_TipoDocBenef) {
        this.m_TipoDocBenef = m_TipoDocBenef;
    }

    public String getM_NumDocBenef() {
        return m_NumDocBenef;
    }

    public void setM_NumDocBenef(String m_NumDocBenef) {
        this.m_NumDocBenef = m_NumDocBenef;
    }

    public String getM_NombreBenef() {
        return m_NombreBenef;
    }

    public void setM_NombreBenef(String m_NombreBenef) {
        this.m_NombreBenef = m_NombreBenef;
    }

    public String getM_ApePatBenef() {
        return m_ApePatBenef;
    }

    public void setM_ApePatBenef(String m_ApePatBenef) {
        this.m_ApePatBenef = m_ApePatBenef;
    }

    public String getM_ApeMatBenef() {
        return m_ApeMatBenef;
    }

    public void setM_ApeMatBenef(String m_ApeMatBenef) {
        this.m_ApeMatBenef = m_ApeMatBenef;
    }

    public String getM_DireccionBenef() {
        return m_DireccionBenef;
    }

    public void setM_DireccionBenef(String m_DireccionBenef) {
        this.m_DireccionBenef = m_DireccionBenef;
    }

    public String getM_TlfBenef() {
        return m_TlfBenef;
    }

    public void setM_TlfBenef(String m_TlfBenef) {
        this.m_TlfBenef = m_TlfBenef;
    }

    public String getM_CtaAbonoEntidad() {
        return m_CtaAbonoEntidad;
    }

    public void setM_CtaAbonoEntidad(String m_CtaAbonoEntidad) {
        this.m_CtaAbonoEntidad = m_CtaAbonoEntidad;
    }

    public String getM_CtaAbonoOficina() {
        return m_CtaAbonoOficina;
    }

    public void setM_CtaAbonoOficina(String m_CtaAbonoOficina) {
        this.m_CtaAbonoOficina = m_CtaAbonoOficina;
    }

    public String getM_CtaAbonoCuenta() {
        return m_CtaAbonoCuenta;
    }

    public void setM_CtaAbonoCuenta(String m_CtaAbonoCuenta) {
        this.m_CtaAbonoCuenta = m_CtaAbonoCuenta;
    }

    public String getM_CtaAbonoControl() {
        return m_CtaAbonoControl;
    }

    public void setM_CtaAbonoControl(String m_CtaAbonoControl) {
        this.m_CtaAbonoControl = m_CtaAbonoControl;
    }

    public String getM_Modulo() {
        return m_Modulo;
    }

    public void setM_Modulo(String m_Modulo) {
        this.m_Modulo = m_Modulo;
    }

    public String getM_SubModulo() {
        return m_SubModulo;
    }

    public void setM_SubModulo(String m_SubModulo) {
        this.m_SubModulo = m_SubModulo;
    }

    public String getM_TipoOper() {
        return m_TipoOper;
    }

    public void setM_TipoOper(String m_TipoOper) {
        this.m_TipoOper = m_TipoOper;
    }

    public String getM_CtaTypCargo() {
        return m_CtaTypCargo;
    }

    public void setM_CtaTypCargo(String m_CtaTypCargo) {
        this.m_CtaTypCargo = m_CtaTypCargo;
    }

    public String getM_CtaTypAbono() {
        return m_CtaTypAbono;
    }

    public void setM_CtaTypAbono(String m_CtaTypAbono) {
        this.m_CtaTypAbono = m_CtaTypAbono;
    }

    /**
     * @return the m_IdTipoDocBenef
     */
    public String getM_IdTipoDocBenef() {
        return m_IdTipoDocBenef;
    }

    /**
     * @param m_IdTipoDocBenef the m_IdTipoDocBenef to set
     */
    public void setM_IdTipoDocBenef(String m_IdTipoDocBenef) {
        this.m_IdTipoDocBenef = m_IdTipoDocBenef;
    }

    /**
     * @return the m_FlagCliente
     */
    public String getM_FlagCliente() {
        return m_FlagCliente;
    }

    /**
     * @param m_FlagCliente the m_FlagCliente to set
     */
    public void setM_FlagCliente(String m_FlagCliente) {
        this.m_FlagCliente = m_FlagCliente;
    }

    public String getM_NombreBanco() {
        return m_NombreBanco;
    }

    public void setM_NombreBanco(String m_NombreBanco) {
        this.m_NombreBanco = m_NombreBanco;
    }

    public String getM_MontoMostrar() {
        return m_MontoMostrar;
    }

    public void setM_MontoMostrar(String m_MontoMostrar) {
        this.m_MontoMostrar = m_MontoMostrar;
    }
    
    public void setM_RazonSocial(String mRazonSocial) {
		m_RazonSocial = mRazonSocial;
	}
    
    public String getM_RazonSocial() {
		return m_RazonSocial;
	}
    
    public void setTitulaCuentaCargo(String titulaCuentaCargo) {
		this.titulaCuentaCargo = titulaCuentaCargo;
	}
    
    public String getTitulaCuentaCargo() {
		return titulaCuentaCargo;
	}
}

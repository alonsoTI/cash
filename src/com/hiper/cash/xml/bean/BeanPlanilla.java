/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

/**
 *
 * @author esilva
 */
public class BeanPlanilla {//BeanLetra
    private String m_Cuenta;
    private String m_Moneda;
    private String m_Tipo;
    private String m_NomAcep;
    private String m_CodAcep;//xAceptante
    private String m_Saldo;
    private String m_FecVenc;

    public String getM_Cuenta() {
        return m_Cuenta;
    }

    public void setM_Cuenta(String m_Cuenta) {
        this.m_Cuenta = m_Cuenta;
    }

    public String getM_Moneda() {
        return m_Moneda;
    }

    public void setM_Moneda(String m_Moneda) {
        this.m_Moneda = m_Moneda;
    }

    public String getM_Tipo() {
        return m_Tipo;
    }

    public void setM_Tipo(String m_Tipo) {
        this.m_Tipo = m_Tipo;
    }

    public String getM_NomAcep() {
        return m_NomAcep;
    }

    public void setM_NomAcep(String m_NomAcep) {
        this.m_NomAcep = m_NomAcep;
    }

    public String getM_Saldo() {
        return m_Saldo;
    }

    public void setM_Saldo(String m_Saldo) {
        this.m_Saldo = m_Saldo;
    }

    public String getM_FecVenc() {
        return m_FecVenc;
    }

    public void setM_FecVenc(String m_FecVenc) {
        this.m_FecVenc = m_FecVenc;
    }
    //jmoreno
     public java.util.Map getParametrosUrl(){
        java.util.Map parametros = new java.util.HashMap();
        parametros.put("m_Cuenta",this.m_Cuenta);
        parametros.put("m_Moneda",this.m_Moneda); // para ser mostrados en el detalle de Planilla
        parametros.put("m_CodAcep",this.getM_CodAcep());
        return parametros;
    }

    public String getM_CodAcep() {
        return m_CodAcep;
    }

    public void setM_CodAcep(String m_CodAcep) {
        this.m_CodAcep = m_CodAcep;
    }
    
}

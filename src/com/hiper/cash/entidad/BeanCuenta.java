/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

/**
 *
 * @author esilva
 */
public class BeanCuenta {
    private String m_NumeroCuenta ="";
    private String m_ServEmp ="";
    private String m_TipoCuenta ="";
    private String m_Descripcion ="";
    private String m_Estado ="";
    private String m_Moneda ="";

    public String getM_NumeroCuenta() {
        return m_NumeroCuenta;
    }

    public void setM_NumeroCuenta(String m_NumeroCuenta) {
        if(m_NumeroCuenta!=null){
            this.m_NumeroCuenta = m_NumeroCuenta;
        }else{
            this.m_NumeroCuenta = "";
        }
        
    }

    public String getM_ServEmp() {
        return m_ServEmp;
    }

    public void setM_ServEmp(String m_ServEmp) {
        if(m_ServEmp!=null){
            this.m_ServEmp = m_ServEmp;
        }else{
            this.m_ServEmp = m_ServEmp;
        }
        
    }

    public String getM_TipoCuenta() {
        return m_TipoCuenta;
    }

    public void setM_TipoCuenta(String m_TipoCuenta) {
        if(m_TipoCuenta!=null){
            this.m_TipoCuenta = m_TipoCuenta;
        }else{
            this.m_TipoCuenta = "";
        }
        
    }

    public String getM_Descripcion() {
        return m_Descripcion;
    }

    public void setM_Descripcion(String m_Descripcion) {
        if(m_Descripcion!=null){
            this.m_Descripcion = m_Descripcion;
        }else{
            this.m_Descripcion = "";
        }
        
    }

    public String getM_Estado() {
        return m_Estado;
    }

    public void setM_Estado(String m_Estado) {
        if(m_Estado!=null){
            this.m_Estado = m_Estado;
        }else{
            this.m_Estado = "";
        }
        
    }

    public String getM_Moneda() {
        return m_Moneda;
    }

    public void setM_Moneda(String m_Moneda) {
        if(m_Moneda!=null){
            this.m_Moneda = m_Moneda;
        }else{
            this.m_Moneda = "";
        }
        
    }

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

/**
 *
 * @author esilva
 */
public class BeanTipoPagoServicio {

    private String m_TipoPagoServicio = "";
    private String m_Servicio = "";
    private String m_Descripcion = "";

    public String getM_TipoPagoServicio() {
        return m_TipoPagoServicio;
    }

    public void setM_TipoPagoServicio(String m_TipoPagoServicio) {
        if(m_TipoPagoServicio != null){
            this.m_TipoPagoServicio = m_TipoPagoServicio;
        }else{
            this.m_TipoPagoServicio = "";
        }
        
    }

    public String getM_Servicio() {
        return m_Servicio;
    }

    public void setM_Servicio(String m_Servicio) {
        if(m_Servicio != null){
            this.m_Servicio = m_Servicio;
        }else{
            this.m_Servicio = "";
        }
        
    }

    public String getM_Descripcion() {
        return m_Descripcion;
    }

    public void setM_Descripcion(String m_Descripcion) {
        if(m_Descripcion != null){
            this.m_Descripcion = m_Descripcion;
        }else{
            this.m_Descripcion = "";
        }
        
    }

    
}

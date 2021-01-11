/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

/**
 *
 * @author esilva
 */
public class BeanConfirmDetail {
    private String m_Label ="";
    private String m_Mensaje ="";

    public BeanConfirmDetail(String m_Label, String m_Mensaje){
        this.m_Label = m_Label;
        this.m_Mensaje = m_Mensaje;
    }

    public String getM_Label() {
        return m_Label;
    }

    public void setM_Label(String m_Label) {
        if(m_Label!=null){
            this.m_Label = m_Label;
        }else{
            this.m_Label = "";
        }
        
    }

    public String getM_Mensaje() {
        return m_Mensaje;
    }

    public void setM_Mensaje(String m_Mensaje) {
        if(m_Mensaje!=null){
            this.m_Mensaje = m_Mensaje;
        }else{
            this.m_Mensaje = "";
        }
        
    }
}

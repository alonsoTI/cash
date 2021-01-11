/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

/**
 *
 * @author Elvis
 */
public class BeanSuccess {
    private String m_Titulo = "";
    private String m_Mensaje = "";
    private String m_Detalle = "";
    private String m_Back = "";
    private String m_BackAccion = "";

    public String getM_Titulo() {
        return m_Titulo;
    }

    public void setM_Titulo(String m_Titulo) {
        if(m_Titulo != null){
            this.m_Titulo = m_Titulo;
        }else{
            this.m_Titulo = "";
        }
        
    }

    public String getM_Mensaje() {
        return m_Mensaje;
    }

    public void setM_Mensaje(String m_Mensaje) {
        if(m_Mensaje != null){
            this.m_Mensaje = m_Mensaje;
        }else{
            this.m_Mensaje = "";
        }
        
    }

    public String getM_Back() {
        return m_Back;
    }

    public void setM_Back(String m_Back) {
        if(m_Back != null){
            this.m_Back = m_Back;
        }else{
            this.m_Back = "";
        }
        
    }

    public String getM_Detalle() {
        return m_Detalle;
    }

    public void setM_Detalle(String m_Detalle) {
        if(m_Detalle != null){
            this.m_Detalle = m_Detalle;
        }else{
            this.m_Detalle = "";
        }
        
    }

    public String getM_BackAccion() {
        return m_BackAccion;
    }

    public void setM_BackAccion(String m_BackAccion) {
        if(m_BackAccion != null){
            this.m_BackAccion = m_BackAccion;
        }else {
            this.m_BackAccion = "";
        }
        
    }

    

}

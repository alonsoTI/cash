/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

/**
 *
 * @author esilva
 */
public class BeanConfirm {
    private String m_Titulo ="";
    private String m_Mensaje ="";
    private String m_Detalle ="";
    private String m_Back ="";
    private String m_BackAccion ="";
    private String m_Forward ="";
    private String m_ForwardAccion ="";

    public String getM_Titulo() {
        return m_Titulo;
    }

    public void setM_Titulo(String m_Titulo) {
        if(m_Titulo!=null){
            this.m_Titulo = m_Titulo;
        }else{
            this.m_Titulo = "";
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

    public String getM_Detalle() {
        return m_Detalle;
    }

    public void setM_Detalle(String m_Detalle) {
        if(m_Detalle!=null){
            this.m_Detalle = m_Detalle;
        }else{
            this.m_Detalle = "";
        }

    }

    public String getM_Back() {
        return m_Back;
    }

    public void setM_Back(String m_Back) {
        if(m_Back!=null){
            this.m_Back = m_Back;
        }else{
            this.m_Back = "";
        }
        
    }

    public String getM_BackAccion() {
        return m_BackAccion;
    }

    public void setM_BackAccion(String m_BackAccion) {
        if(m_BackAccion!=null){
            this.m_BackAccion = m_BackAccion;
        }else{
            this.m_BackAccion = "";
        }

    }

    public String getM_Forward() {
        return m_Forward;
    }

    public void setM_Forward(String m_Forward) {
        if(m_Forward!=null){
            this.m_Forward = m_Forward;
        }else{
            this.m_Forward = "";
        }
        
    }

    public String getM_ForwardAccion() {
        return m_ForwardAccion;
    }

    public void setM_ForwardAccion(String m_ForwardAccion) {
        if(m_ForwardAccion!=null){
            this.m_ForwardAccion = m_ForwardAccion;
        }else{
            this.m_ForwardAccion = "";
        }
        
    }
    

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

import java.io.Serializable;

/**
 *
 * @author esilva
 */
public class BeanRespuestaWSHomeBankingXML implements Serializable{
    private String m_Codigo;
    private String m_Nombre;
    private String m_Grupo;
    private String m_Modo;


    private String m_CodigoServicio;
    private String m_CodigoInterno;
    private String m_Ruc;
    private String m_SectorVisible;  //no usado
    private String m_DDNVisible; //no usado
    private String m_LabelCodigoServicio; //etiqueta a mostrar en pagina para el numero de suministro

    private String m_Accion;

    //JWONG
    private String m_TipoEntidad; //tipo de entidad (codigo de empresa)

    public String getM_Codigo() {
        return m_Codigo;
    }

    public void setM_Codigo(String m_Codigo) {
        this.m_Codigo = m_Codigo;
    }

    public String getM_Nombre() {
        return m_Nombre;
    }

    public void setM_Nombre(String m_Nombre) {
        this.m_Nombre = m_Nombre;
    }

    public java.util.Map getParametrosUrl(){
        java.util.Map parametros = new java.util.HashMap();
        parametros.put("m_Codigo",this.m_Codigo);
        //parametros.put("m_Grupo",this.m_Grupo);
        parametros.put("m_Nombre",this.m_Nombre);
        parametros.put("m_CodigoServicio", this.m_CodigoServicio);
        parametros.put("m_Ruc", this.m_Ruc);
        return parametros;
    }

    /**
     * @return the m_Grupo
     */
    public String getM_Grupo() {
        return m_Grupo;
    }

    /**
     * @param m_Grupo the m_Grupo to set
     */
    public void setM_Grupo(String m_Grupo) {
        this.m_Grupo = m_Grupo;
    }

    public String getM_Modo() {
        return m_Modo;
    }

    public void setM_Modo(String m_Modo) {
        this.m_Modo = m_Modo;
    }

    public String getM_CodigoServicio() {
        return m_CodigoServicio;
    }

    public void setM_CodigoServicio(String m_CodigoServicio) {
        this.m_CodigoServicio = m_CodigoServicio;
    }

    public String getM_SectorVisible() {
        return m_SectorVisible;
    }

    public void setM_SectorVisible(String m_SectorVisible) {
        this.m_SectorVisible = m_SectorVisible;
    }

    public String getM_DDNVisible() {
        return m_DDNVisible;
    }

    public void setM_DDNVisible(String m_DDNVisible) {
        this.m_DDNVisible = m_DDNVisible;
    }



    public String getM_TipoCodigoEntidadServicio(){
        return this.m_Codigo + ";" + this.m_LabelCodigoServicio;
    }

    public String getM_LabelCodigoServicio() {
        return m_LabelCodigoServicio;
    }

    public void setM_LabelCodigoServicio(String m_LabelCodigoServicio) {
        this.m_LabelCodigoServicio = m_LabelCodigoServicio;
    }


    //JWONG
    public String getM_TipoEntidad() {
        return m_TipoEntidad;
    }
    public void setM_TipoEntidad(String m_TipoEntidad) {
        this.m_TipoEntidad = m_TipoEntidad;
    }
    public String getM_TipoCodigoEntidadServicio2(){
        return this.m_TipoEntidad + ";" + this.m_Codigo + ";" + this.m_CodigoServicio + ";" + this.m_LabelCodigoServicio + ";" + this.m_Nombre;
    }

    public String getM_Accion() {
        return m_Accion;
    }

    public void setM_Accion(String m_Accion) {
        this.m_Accion = m_Accion;
    }


    public String getM_CodigoInterno() {
        return m_CodigoInterno;
    }
    public void setM_CodigoInterno(String m_CodigoInterno) {
        this.m_CodigoInterno = m_CodigoInterno;
    }
    public String getM_CodigoInterno_Nombre() {
        return m_CodigoInterno + ";" + m_Nombre;
    }

    public String getM_Ruc() {
        return m_Ruc;
    }

    public void setM_Ruc(String m_Ruc) {
        this.m_Ruc = m_Ruc;
    }
}

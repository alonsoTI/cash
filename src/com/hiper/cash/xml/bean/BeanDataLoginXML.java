/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

import java.util.List;

/**
 *
 * @author jwong
 *
 */
public class BeanDataLoginXML {
    private String m_Apellido;
    private String m_Nombre;
    private String m_Empresa;

    private String m_Fecha;
    private String m_Hora;
    private String m_MsjBienvenida;

    private String m_PaginaInicio;
    private String m_PaginaClose;

    //esilva
    private String m_NumTarjeta;
    private String m_Clave;
    public String getM_Clave() {
		return m_Clave;
	}

	public void setM_Clave(String mClave) {
		m_Clave = mClave;
	}

	private List l_Empresas;
    private List l_UserCodes;

    private String m_Codigo;

    //jwong 10/02/2009 dato enviado en la validacion del login del financiero
    private String m_Token;

    //jmoreno 17/11/09 Para verificar si el usuario es un Usuario Especial(Empresa Operaciones Cash)
    private boolean m_usuarioEspecial;
    /**
     * @return the m_Apellido
     */
    public String getM_Apellido() {
        return m_Apellido;
    }

    /**
     * @param m_Apellido the m_Apellido to set
     */
    public void setM_Apellido(String m_Apellido) {
        this.m_Apellido = m_Apellido;
    }

    /**
     * @return the m_Nombre
     */
    public String getM_Nombre() {
        return m_Nombre;
    }

    /**
     * @param m_Nombre the m_Nombre to set
     */
    public void setM_Nombre(String m_Nombre) {
        this.m_Nombre = m_Nombre;
    }

    /**
     * @return the m_Empresa
     */
    public String getM_Empresa() {
        return m_Empresa;
    }

    /**
     * @param m_Empresa the m_Empresa to set
     */
    public void setM_Empresa(String m_Empresa) {
        this.m_Empresa = m_Empresa;
    }

    /**
     * @return the m_Fecha
     */
    public String getM_Fecha() {
        return m_Fecha;
    }

    /**
     * @param m_Fecha the m_Fecha to set
     */
    public void setM_Fecha(String m_Fecha) {
        this.m_Fecha = m_Fecha;
    }

    /**
     * @return the m_Hora
     */
    public String getM_Hora() {
        return m_Hora;
    }

    /**
     * @param m_Hora the m_Hora to set
     */
    public void setM_Hora(String m_Hora) {
        this.m_Hora = m_Hora;
    }

    /**
     * @return the m_MsjBienvenida
     */
    public String getM_MsjBienvenida() {
        return m_MsjBienvenida;
    }

    /**
     * @param m_MsjBienvenida the m_MsjBienvenida to set
     */
    public void setM_MsjBienvenida(String m_MsjBienvenida) {
        this.m_MsjBienvenida = m_MsjBienvenida;
    }

    /**
     * @return the m_PaginaClose
     */
    public String getM_PaginaClose() {
        return m_PaginaClose;
    }

    /**
     * @param m_PaginaClose the m_PaginaClose to set
     */
    public void setM_PaginaClose(String m_PaginaClose) {
        this.m_PaginaClose = m_PaginaClose;
    }

    /**
     * @return the m_PaginaInicio
     */
    public String getM_PaginaInicio() {
        return m_PaginaInicio;
    }

    /**
     * @param m_PaginaInicio the m_PaginaInicio to set
     */
    public void setM_PaginaInicio(String m_PaginaInicio) {
        this.m_PaginaInicio = m_PaginaInicio;
    }

    public String getM_NumTarjeta() {
        return m_NumTarjeta;
    }

    public void setM_NumTarjeta(String m_NumTarjeta) {
        this.m_NumTarjeta = m_NumTarjeta;
    }

    public List getL_Empresas() {
        return l_Empresas;
    }

    public void setL_Empresas(List l_Empresas) {
        this.l_Empresas = l_Empresas;
    }

    /**
     * @return the m_Token
     */
    public String getM_Token() {
        return m_Token;
    }

    /**
     * @param m_Token the m_Token to set
     */
    public void setM_Token(String m_Token) {
        this.m_Token = m_Token;
    }

    public String getM_Codigo() {
        return m_Codigo;
    }

    public void setM_Codigo(String m_Codigo) {
        this.m_Codigo = m_Codigo;
    }

    public List getL_UserCodes() {
        return l_UserCodes;
    }

    public void setL_UserCodes(List l_UserCodes) {
        this.l_UserCodes = l_UserCodes;
    }

    public boolean isM_usuarioEspecial() {
        return m_usuarioEspecial;
    }

    public void setM_usuarioEspecial(boolean m_usuarioEspecial) {
        this.m_usuarioEspecial = m_usuarioEspecial;
    }
   
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

import org.jdom.Element;

/**
 *
 * @author jwong
 */
public class BeanRespuestaXML {
    //Header
    private String m_CodigoTransaccionIBS;
    private String m_CodigoTransaccion;
    private String m_IdTransaccion;
    private String m_NumeroOperacion;
    private String m_CodigoRetorno;

    //contenido del tag de respuesta
    private Element m_Respuesta;
    
    //Error
    private String m_DescripcionError;
    private String m_DetalleError;

    //data obtenida solo en el caso del logueo
    private String m_Empresa; //codigo de empresa
    private String m_Nombre; //nombre del usuario logueado
    private String m_Apellido; //apellido del usuario logueado
    //jmoreno 27-08-09
    private String m_CodigoRetornoIBS;

    /**
     * @return the m_CodigoTransaccionIBS
     */
    public String getM_CodigoTransaccionIBS() {
        return m_CodigoTransaccionIBS;
    }

    /**
     * @param m_CodigoTransaccionIBS the m_CodigoTransaccionIBS to set
     */
    public void setM_CodigoTransaccionIBS(String m_CodigoTransaccionIBS) {
        this.m_CodigoTransaccionIBS = m_CodigoTransaccionIBS;
    }

    /**
     * @return the m_CodigoTransaccion
     */
    public String getM_CodigoTransaccion() {
        return m_CodigoTransaccion;
    }

    /**
     * @param m_CodigoTransaccion the m_CodigoTransaccion to set
     */
    public void setM_CodigoTransaccion(String m_CodigoTransaccion) {
        this.m_CodigoTransaccion = m_CodigoTransaccion;
    }

    /**
     * @return the m_IdTransaccion
     */
    public String getM_IdTransaccion() {
        return m_IdTransaccion;
    }

    /**
     * @param m_IdTransaccion the m_IdTransaccion to set
     */
    public void setM_IdTransaccion(String m_IdTransaccion) {
        this.m_IdTransaccion = m_IdTransaccion;
    }

    /**
     * @return the m_NumeroOperacion
     */
    public String getM_NumeroOperacion() {
        return m_NumeroOperacion;
    }

    /**
     * @param m_NumeroOperacion the m_NumeroOperacion to set
     */
    public void setM_NumeroOperacion(String m_NumeroOperacion) {
        this.m_NumeroOperacion = m_NumeroOperacion;
    }

    /**
     * @return the m_CodigoRetorno
     */
    public String getM_CodigoRetorno() {
        return m_CodigoRetorno;
    }

    /**
     * @param m_CodigoRetorno the m_CodigoRetorno to set
     */
    public void setM_CodigoRetorno(String m_CodigoRetorno) {
        this.m_CodigoRetorno = m_CodigoRetorno;
    }

    /**
     * @return the m_Respuesta
     */
    public Element getM_Respuesta() {
        return m_Respuesta;
    }

    /**
     * @param m_Respuesta the m_Respuesta to set
     */
    public void setM_Respuesta(Element m_Respuesta) {
        this.m_Respuesta = m_Respuesta;
    }

    /**
     * @return the m_DescripcionError
     */
    public String getM_DescripcionError() {
        return m_DescripcionError;
    }

    /**
     * @param m_DescripcionError the m_DescripcionError to set
     */
    public void setM_DescripcionError(String m_DescripcionError) {
        this.m_DescripcionError = m_DescripcionError;
    }

    /**
     * @return the m_DetalleError
     */
    public String getM_DetalleError() {
        return m_DetalleError;
    }

    /**
     * @param m_DetalleError the m_DetalleError to set
     */
    public void setM_DetalleError(String m_DetalleError) {
        this.m_DetalleError = m_DetalleError;
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

    public String getM_CodigoRetornoIBS() {
        return m_CodigoRetornoIBS;
    }

    public void setM_CodigoRetornoIBS(String m_CodigoRetornoIBS) {
        this.m_CodigoRetornoIBS = m_CodigoRetornoIBS;
    }
}
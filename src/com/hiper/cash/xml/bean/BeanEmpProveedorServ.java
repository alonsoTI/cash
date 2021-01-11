/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

/**
 *
 * @author jwong
 */
public class BeanEmpProveedorServ {
    private String m_CodEmpProveedor;
    private String m_Nombre;
    private String m_Descripcion;
    private String m_Servicio;
    //tipo de servicio al que pertenece el proveedor(agua, luz, telefonia, universidades, colegios, etc)
    private String m_TipoServicio;

    /**
     * @return the m_CodEmpProveedor
     */
    public String getM_CodEmpProveedor() {
        return m_CodEmpProveedor;
    }

    /**
     * @param m_CodEmpProveedor the m_CodEmpProveedor to set
     */
    public void setM_CodEmpProveedor(String m_CodEmpProveedor) {
        this.m_CodEmpProveedor = m_CodEmpProveedor;
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
     * @return the m_Descripcion
     */
    public String getM_Descripcion() {
        return m_Descripcion;
    }

    /**
     * @param m_Descripcion the m_Descripcion to set
     */
    public void setM_Descripcion(String m_Descripcion) {
        this.m_Descripcion = m_Descripcion;
    }

    /**
     * @return the m_Servicio
     */
    public String getM_Servicio() {
        return m_Servicio;
    }

    /**
     * @param m_Servicio the m_Servicio to set
     */
    public void setM_Servicio(String m_Servicio) {
        this.m_Servicio = m_Servicio;
    }


    public java.util.Map getParametrosUrl(){
        java.util.Map parametros = new java.util.HashMap();
        parametros.put("m_CodEmpProveedor",this.m_CodEmpProveedor);
        parametros.put("m_Servicio",this.m_Servicio);
        parametros.put("m_Nombre", this.m_Nombre);
        parametros.put("m_TipoServicio", this.m_TipoServicio);
        return parametros;
    }

    /**
     * @return the m_TipoServicio
     */
    public String getM_TipoServicio() {
        return m_TipoServicio;
    }

    /**
     * @param m_TipoServicio the m_TipoServicio to set
     */
    public void setM_TipoServicio(String m_TipoServicio) {
        this.m_TipoServicio = m_TipoServicio;
    }
}

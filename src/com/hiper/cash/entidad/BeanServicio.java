
package com.hiper.cash.entidad;

import java.io.Serializable;

/**
 *
 * @author esilva
 */
public class BeanServicio implements Serializable{
    private String m_IdServicioEmp = "";//jmoreno 11/11/09
    private String m_IdServicio = "";
    private String m_Descripcion = "";
    private String estado = "";

    public String getM_IdServicio() {
        return m_IdServicio;
    }

    public void setM_IdServicio(String m_IdServicio) {
        this.m_IdServicio = m_IdServicio;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getM_IdServicioEmp() {
        return m_IdServicioEmp;
    }

    public void setM_IdServicioEmp(String m_IdServicioEmp) {
        this.m_IdServicioEmp = m_IdServicioEmp;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(m_Descripcion).append("-").append(m_IdServicioEmp);
    	return sb.toString();
    }
}

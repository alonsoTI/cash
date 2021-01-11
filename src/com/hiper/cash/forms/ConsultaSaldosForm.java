/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.forms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jwong
 */
public class ConsultaSaldosForm extends ActionForm {
    private String m_Empresa;
    private String m_TipoInformacion;
    private String m_Paginado;

    private String m_Cuenta;
    private String m_FecInicio;
    private String m_FecFin;

	//jwong 03/03/2009 para el manejo de la empresa seleccionada
    private String m_EmpresaSel;
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
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
     * @return the m_TipoInformacion
     */
    public String getM_TipoInformacion() {
        return m_TipoInformacion;
    }

    /**
     * @param m_TipoInformacion the m_TipoInformacion to set
     */
    public void setM_TipoInformacion(String m_TipoInformacion) {
        this.m_TipoInformacion = m_TipoInformacion;
    }

    /**
     * @return the m_Paginado
     */
    public String getM_Paginado() {
        return m_Paginado;
    }

    /**
     * @param m_Paginado the m_Paginado to set
     */
    public void setM_Paginado(String m_Paginado) {
        this.m_Paginado = m_Paginado;
    }

    /**
     * @return the m_Cuenta
     */
    public String getM_Cuenta() {
        return m_Cuenta;
    }

    /**
     * @param m_Cuenta the m_Cuenta to set
     */
    public void setM_Cuenta(String m_Cuenta) {
        this.m_Cuenta = m_Cuenta;
    }

    /**
     * @return the m_FecInicio
     */
    public String getM_FecInicio() {
        return m_FecInicio;
    }

    /**
     * @param m_FecInicio the m_FecInicio to set
     */
    public void setM_FecInicio(String m_FecInicio) {
        this.m_FecInicio = m_FecInicio;
    }

    /**
     * @return the m_FecFin
     */
    public String getM_FecFin() {
        return m_FecFin;
    }

    /**
     * @param m_FecFin the m_FecFin to set
     */
    public void setM_FecFin(String m_FecFin) {
        this.m_FecFin = m_FecFin;
    }
	public String getM_EmpresaSel() {
        return m_EmpresaSel;
    }

    public void setM_EmpresaSel(String m_EmpresaSel) {
        this.m_EmpresaSel = m_EmpresaSel;
    }
}

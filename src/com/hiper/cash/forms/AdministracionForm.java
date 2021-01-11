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
public class AdministracionForm extends ActionForm {
    private String m_Empresa;
    private String m_IdServEmp;//jmoreno 22/12/09
    private String m_IdServEmpHab;//jmoreno 22/12/09
    private String m_Paginado;
    
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

    public String getM_IdServEmp() {
        return m_IdServEmp;
    }

    public void setM_IdServEmp(String m_IdServEmp) {
        this.m_IdServEmp = m_IdServEmp;
    }

    public String getM_IdServEmpHab() {
        return m_IdServEmpHab;
    }

    public void setM_IdServEmpHab(String m_IdServEmpHab) {
        this.m_IdServEmpHab = m_IdServEmpHab;
    }

    
}

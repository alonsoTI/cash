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
public class ConsultaOrdenesForm extends ActionForm {
    private String m_Empresa;
    private String m_Servicio;
    private String m_Estado;

    private String m_FecInicio;
    private String m_FecFin;

    private String m_ConsReferencia;
    private String m_ConsContrapartida;
    private String tipoProcesamiento = "P";

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

    /**
     * @return the m_Estado
     */
    public String getM_Estado() {
        return m_Estado;
    }

    /**
     * @param m_Estado the m_Estado to set
     */
    public void setM_Estado(String m_Estado) {
        this.m_Estado = m_Estado;
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

    public String getM_ConsReferencia() {
        return m_ConsReferencia;
    }

    public void setM_ConsReferencia(String m_ConsReferencia) {
        this.m_ConsReferencia = m_ConsReferencia;
    }

    public String getM_ConsContrapartida() {
        return m_ConsContrapartida;
    }

    public void setM_ConsContrapartida(String m_ConsContrapartida) {
        this.m_ConsContrapartida = m_ConsContrapartida;
    }
    
    public String getTipoProcesamiento() {
		return tipoProcesamiento;
	}
    
    public void setTipoProcesamiento(String tipoProcesamiento) {
		this.tipoProcesamiento = tipoProcesamiento;
	}
    
    
}
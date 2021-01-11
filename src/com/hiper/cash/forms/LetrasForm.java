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
public class LetrasForm extends ActionForm {
    private String m_Empresa;
    private String m_Tipo; //girador o aceptante
    private String m_Paginado;

    private String m_FecInicio;
    private String m_FecFin;
    private String m_fechaActualComp;//para el calendario
    private String m_FechaMaxAnt;
    private String m_FechaMaxPost;

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
     * @return the m_Tipo
     */
    public String getM_Tipo() {
        return m_Tipo;
    }

    /**
     * @param m_Tipo the m_Tipo to set
     */
    public void setM_Tipo(String m_Tipo) {
        this.m_Tipo = m_Tipo;
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

    public String getM_fechaActualComp() {
        return m_fechaActualComp;
    }

    public void setM_fechaActualComp(String m_fechaActualComp) {
        this.m_fechaActualComp = m_fechaActualComp;
    }

    public String getM_FechaMaxAnt() {
        return m_FechaMaxAnt;
    }

    public void setM_FechaMaxAnt(String m_FechaMaxAnt) {
        this.m_FechaMaxAnt = m_FechaMaxAnt;
    }

    public String getM_FechaMaxPost() {
        return m_FechaMaxPost;
    }

    public void setM_FechaMaxPost(String m_FechaMaxPost) {
        this.m_FechaMaxPost = m_FechaMaxPost;
    }
}

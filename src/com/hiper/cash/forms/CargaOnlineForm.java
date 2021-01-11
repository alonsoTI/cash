/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.forms;



import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import fr.improve.struts.taglib.layout.util.DefaultForm;

/**
 *
 * @author esilva
 */
public class CargaOnlineForm extends DefaultForm {

    private String dorReferencia;
    private String norCuentaCargo;
    private String forFechaInicio;
    private String forFechaFin;
    private String m_HoraVigencia;
    private String m_IdOrden;
    private String m_IdEmpresa;
    private String m_IdServicio;

    private String m_Empresa;
    private String m_Servicio;


    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    /**
     * @return the dorReferencia
     */
    public String getDorReferencia() {
        return dorReferencia;
    }

    /**
     * @param dorReferencia the dorReferencia to set
     */
    public void setDorReferencia(String dorReferencia) {
        this.dorReferencia = dorReferencia;
    }

    /**
     * @return the norCuentaCargo
     */
    public String getNorCuentaCargo() {
        return norCuentaCargo;
    }

    /**
     * @param norCuentaCargo the norCuentaCargo to set
     */
    public void setNorCuentaCargo(String norCuentaCargo) {
        this.norCuentaCargo = norCuentaCargo;
    }

    /**
     * @return the forFechaInicio
     */
    public String getForFechaInicio() {
        return forFechaInicio;
    }

    /**
     * @param forFechaInicio the forFechaInicio to set
     */
    public void setForFechaInicio(String forFechaInicio) {
        this.forFechaInicio = forFechaInicio;
    }

    /**
     * @return the forFechaFin
     */
    public String getForFechaFin() {
        return forFechaFin;
    }

    /**
     * @param forFechaFin the forFechaFin to set
     */
    public void setForFechaFin(String forFechaFin) {
        this.forFechaFin = forFechaFin;
    }

    /**
     * @return the m_IdOrden
     */
    public String getM_IdOrden() {
        return m_IdOrden;
    }

    /**
     * @param m_IdOrden the m_IdOrden to set
     */
    public void setM_IdOrden(String m_IdOrden) {
        this.m_IdOrden = m_IdOrden;
    }

    /**
     * @return the m_IdEmpresa
     */
    public String getM_IdEmpresa() {
        return m_IdEmpresa;
    }

    /**
     * @param m_IdEmpresa the m_IdEmpresa to set
     */
    public void setM_IdEmpresa(String m_IdEmpresa) {
        this.m_IdEmpresa = m_IdEmpresa;
    }

    /**
     * @return the m_IdServicio
     */
    public String getM_IdServicio() {
        return m_IdServicio;
    }

    /**
     * @param m_IdServicio the m_IdServicio to set
     */
    public void setM_IdServicio(String m_IdServicio) {
        this.m_IdServicio = m_IdServicio;
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

    public String getM_HoraVigencia() {
        return m_HoraVigencia;
    }

    public void setM_HoraVigencia(String m_HoraVigencia) {
        this.m_HoraVigencia = m_HoraVigencia;
    }


}

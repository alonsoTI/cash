/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.forms;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import fr.improve.struts.taglib.layout.util.DefaultForm;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jwong
 */
public class AprobacionOrdenForm extends DefaultForm {
    private String m_Empresa;
    private String m_Servicio;
    private String m_Paginado;

    private Map values = new HashMap();

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

    public String getValue(String in_key) {
        return (String) values.get(in_key);
    }

    public void setValue(String in_key, String in_value) {
        this.values.put(in_key, in_value);
    }
    public Map getValues(){
        return this.values;
    }
    public void setValues(){
        this.values = new HashMap();
    }

}

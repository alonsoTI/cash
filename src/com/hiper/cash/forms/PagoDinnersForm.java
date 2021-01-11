/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author jwong
 */
public class PagoDinnersForm extends ActionForm {
    private String m_Cuenta;
    private String m_TipoMoneda;
    private String m_Importe;
    private String m_Card1;
    private String m_Card2;
    private String m_Card3;
    private String m_CardName;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
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
     * @return the m_TipoMoneda
     */
    public String getM_TipoMoneda() {
        return m_TipoMoneda;
    }

    /**
     * @param m_TipoMoneda the m_TipoMoneda to set
     */
    public void setM_TipoMoneda(String m_TipoMoneda) {
        this.m_TipoMoneda = m_TipoMoneda;
    }

    /**
     * @return the m_Importe
     */
    public String getM_Importe() {
        return m_Importe;
    }

    /**
     * @param m_Importe the m_Importe to set
     */
    public void setM_Importe(String m_Importe) {
        this.m_Importe = m_Importe;
    }

    /**
     * @return the m_Card1
     */
    public String getM_Card1() {
        return m_Card1;
    }

    /**
     * @param m_Card1 the m_Card1 to set
     */
    public void setM_Card1(String m_Card1) {
        this.m_Card1 = m_Card1;
    }

    /**
     * @return the m_Card2
     */
    public String getM_Card2() {
        return m_Card2;
    }

    /**
     * @param m_Card2 the m_Card2 to set
     */
    public void setM_Card2(String m_Card2) {
        this.m_Card2 = m_Card2;
    }

    /**
     * @return the m_Card3
     */
    public String getM_Card3() {
        return m_Card3;
    }

    /**
     * @param m_Card3 the m_Card3 to set
     */
    public void setM_Card3(String m_Card3) {
        this.m_Card3 = m_Card3;
    }

    /**
     * @return the m_CardName
     */
    public String getM_CardName() {
        return m_CardName;
    }

    /**
     * @param m_CardName the m_CardName to set
     */
    public void setM_CardName(String m_CardName) {
        this.m_CardName = m_CardName;
    }

}

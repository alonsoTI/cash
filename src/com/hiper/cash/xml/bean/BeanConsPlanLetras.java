/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

import java.util.List;

/**
 *
 * @author esilva
 */
public class BeanConsPlanLetras {
    private String m_ClientCode;
    private String m_ClientName;
    private String m_TotalReg;
    private String m_NumReg;

    private List m_Letras;

    public String getM_ClientCode() {
        return m_ClientCode;
    }

    public void setM_ClientCode(String m_ClientCode) {
        this.m_ClientCode = m_ClientCode;
    }

    public String getM_ClientName() {
        return m_ClientName;
    }

    public void setM_ClientName(String m_ClientName) {
        this.m_ClientName = m_ClientName;
    }

    public String getM_TotalReg() {
        return m_TotalReg;
    }

    public void setM_TotalReg(String m_TotalReg) {
        this.m_TotalReg = m_TotalReg;
    }

    public String getM_NumReg() {
        return m_NumReg;
    }

    public void setM_NumReg(String m_NumReg) {
        this.m_NumReg = m_NumReg;
    }

    public List getM_Letras() {
        return m_Letras;
    }

    public void setM_Letras(List m_Letras) {
        this.m_Letras = m_Letras;
    }
}

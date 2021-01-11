/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

/**
 * BeanTotalesConsMov
 * @version 1.0 05/01/2010
 * @author jmoreno
 * Copyright © HIPER S.A 
 */
public class BeanTotalesConsMov {
    private String m_NumItems = "";
    private String m_MontoSoles = "";
    private String m_MontoDolares = "";
    private String m_MontoVentSoles = "";
    private String m_MontoVentDolares = "";
    private String m_MontoNetoSoles = "";
    private String m_MontoNetoDolares = "";
    private long numeroItems=0;

    public String getM_NumItems() {
        return m_NumItems;
    }

    public void setM_NumItems(String m_NumItems) {
        this.m_NumItems = m_NumItems;
    }

    public String getM_MontoSoles() {
        return m_MontoSoles;
    }

    public void setM_MontoSoles(String m_MontoSoles) {
        this.m_MontoSoles = m_MontoSoles;
    }

    public String getM_MontoDolares() {
        return m_MontoDolares;
    }

    public void setM_MontoDolares(String m_MontoDolares) {
        this.m_MontoDolares = m_MontoDolares;
    }

    public String getM_MontoVentSoles() {
        return m_MontoVentSoles;
    }

    public void setM_MontoVentSoles(String m_MontoVentSoles) {
        this.m_MontoVentSoles = m_MontoVentSoles;
    }

    public String getM_MontoVentDolares() {
        return m_MontoVentDolares;
    }

    public void setM_MontoVentDolares(String m_MontoVentDolares) {
        this.m_MontoVentDolares = m_MontoVentDolares;
    }

    public String getM_MontoNetoSoles() {
        return m_MontoNetoSoles;
    }

    public void setM_MontoNetoSoles(String m_MontoNetoSoles) {
        this.m_MontoNetoSoles = m_MontoNetoSoles;
    }

    public String getM_MontoNetoDolares() {
        return m_MontoNetoDolares;
    }

    public void setM_MontoNetoDolares(String m_MontoNetoDolares) {
        this.m_MontoNetoDolares = m_MontoNetoDolares;
    }

	public long getNumeroItems() {
		return numeroItems;
	}

	public void setNumeroItems(long numeroItems) {
		this.numeroItems = numeroItems;
	}       
}

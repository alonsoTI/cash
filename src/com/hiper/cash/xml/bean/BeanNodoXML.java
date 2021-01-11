/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

/**
 *
 * @author jwong
 */
public class BeanNodoXML {
    private String m_Nodo;
    private String m_Valor;

    public BeanNodoXML(){
        this.m_Nodo = null;
        this.m_Valor = null;
    }
    
    public BeanNodoXML(String m_Nodo, String m_Valor){
        this.m_Nodo = m_Nodo;
        this.m_Valor = m_Valor;
    }

    /**
     * @return the m_Nodo
     */
    public String getM_Nodo() {
        return m_Nodo;
    }

    /**
     * @param m_Nodo the m_Nodo to set
     */
    public void setM_Nodo(String m_Nodo) {
        this.m_Nodo = m_Nodo;
    }

    /**
     * @return the m_Valor
     */
    public String getM_Valor() {
        return m_Valor;
    }

    /**
     * @param m_Valor the m_Valor to set
     */
    public void setM_Valor(String m_Valor) {
        this.m_Valor = m_Valor;
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

import java.util.List;

/**
 *
 * @author jwong
 */
public class BeanTypeAccount implements Comparable {
    private String m_Code;
    private String m_Description;
    private List m_Accounts;

    /**
     * @return the m_Code
     */
    public String getM_Code() {
        return m_Code;
    }

    /**
     * @param m_Code the m_Code to set
     */
    public void setM_Code(String m_Code) {
        this.m_Code = m_Code;
    }

    /**
     * @return the m_Description
     */
    public String getM_Description() {
        return m_Description;
    }

    /**
     * @param m_Description the m_Description to set
     */
    public void setM_Description(String m_Description) {
        this.m_Description = m_Description;
    }

    /**
     * @return the m_Accounts
     */
    public List getM_Accounts() {
        return m_Accounts;
    }

    /**
     * @param m_Accounts the m_Accounts to set
     */
    public void setM_Accounts(List m_Accounts) {
        this.m_Accounts = m_Accounts;
    }

    public String toString() {
        return m_Description;
    }
    public int compareTo(Object o) {
        BeanTypeAccount otroBeanType = (BeanTypeAccount) o;
        //podemos hacer esto porque String implementa Comparable
        return m_Description.compareTo(otroBeanType.getM_Description());
    }
}
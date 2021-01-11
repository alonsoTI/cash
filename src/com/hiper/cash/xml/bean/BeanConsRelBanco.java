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
public class BeanConsRelBanco {

    private String m_CardNumber;
    private String m_ClientCode;
    private String m_ClientName;
    private String m_ClientType;
    private String m_DocType;
    private String m_CardType;
    
    private List m_Accounts;

    //jwong 09/01/2009 lista de tipos de cuentas(grupos de cuentas)
    private List m_AccountsType;
    //jwong 10/01/2009 datos del Sectorista
    private String m_NameSec;
    private String m_PhoneSec;
    private String m_EmailSec;
    
    //jwong 10/01/2009 datos del Funcionario Cash
    private String m_NameFunc;
    private String m_PhoneFunc;
    private String m_EmailFunc;
    
    public String getM_CardNumber() {
        return m_CardNumber;
    }

    public void setM_CardNumber(String m_CardNumber) {
        this.m_CardNumber = m_CardNumber;
    }

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

    public String getM_ClientType() {
        return m_ClientType;
    }

    public void setM_ClientType(String m_ClientType) {
        this.m_ClientType = m_ClientType;
    }

    public String getM_DocType() {
        return m_DocType;
    }

    public void setM_DocType(String m_DocType) {
        this.m_DocType = m_DocType;
    }

    public String getM_CardType() {
        return m_CardType;
    }

    public void setM_CardType(String m_CardType) {
        this.m_CardType = m_CardType;
    }

    public List getM_Accounts() {
        return m_Accounts;
    }

    public void setM_Accounts(List m_Accounts) {
        this.m_Accounts = m_Accounts;
    }

    /**
     * @return the m_AccountsType
     */
    public List getM_AccountsType() {
        return m_AccountsType;
    }

    /**
     * @param m_AccountsType the m_AccountsType to set
     */
    public void setM_AccountsType(List m_AccountsType) {
        this.m_AccountsType = m_AccountsType;
    }

    /**
     * @return the m_NameSec
     */
    public String getM_NameSec() {
        return m_NameSec;
    }

    /**
     * @param m_NameSec the m_NameSec to set
     */
    public void setM_NameSec(String m_NameSec) {
        this.m_NameSec = m_NameSec;
    }

    /**
     * @return the m_PhoneSec
     */
    public String getM_PhoneSec() {
        return m_PhoneSec;
    }

    /**
     * @param m_PhoneSec the m_PhoneSec to set
     */
    public void setM_PhoneSec(String m_PhoneSec) {
        this.m_PhoneSec = m_PhoneSec;
    }

    /**
     * @return the m_EmailSec
     */
    public String getM_EmailSec() {
        return m_EmailSec;
    }

    /**
     * @param m_EmailSec the m_EmailSec to set
     */
    public void setM_EmailSec(String m_EmailSec) {
        this.m_EmailSec = m_EmailSec;
    }

    /**
     * @return the m_NameFunc
     */
    public String getM_NameFunc() {
        return m_NameFunc;
    }

    /**
     * @param m_NameFunc the m_NameFunc to set
     */
    public void setM_NameFunc(String m_NameFunc) {
        this.m_NameFunc = m_NameFunc;
    }

    /**
     * @return the m_PhoneFunc
     */
    public String getM_PhoneFunc() {
        return m_PhoneFunc;
    }

    /**
     * @param m_PhoneFunc the m_PhoneFunc to set
     */
    public void setM_PhoneFunc(String m_PhoneFunc) {
        this.m_PhoneFunc = m_PhoneFunc;
    }

    /**
     * @return the m_EmailFunc
     */
    public String getM_EmailFunc() {
        return m_EmailFunc;
    }

    /**
     * @param m_EmailFunc the m_EmailFunc to set
     */
    public void setM_EmailFunc(String m_EmailFunc) {
        this.m_EmailFunc = m_EmailFunc;
    }

}
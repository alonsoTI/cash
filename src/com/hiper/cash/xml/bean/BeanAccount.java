/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

import java.io.Serializable;

/**
 *
 * @author esilva
 */
public class BeanAccount implements Serializable {
    private String m_AccountType;
    private String m_Currency;
    private String m_AccountCode;
    private String m_Sign;
    private String m_Amount;

    private String m_AccountDescription;
    private String m_Mancomunado;
    private String m_AccountSituation;
    private String m_CountableBalance;
    private String m_AvailableBalance;
    private String m_RetainedBalance;
    private String m_LoanNumber;

    public String getM_AccountType() {
        return m_AccountType;
    }

    public void setM_AccountType(String m_AccountType) {
        this.m_AccountType = m_AccountType;
    }

    public String getM_Currency() {
        return m_Currency;
    }

    public void setM_Currency(String m_Currency) {
        this.m_Currency = m_Currency;
    }

    public String getM_AccountCode() {
        return m_AccountCode;
    }

    public void setM_AccountCode(String m_AccountCode) {
        this.m_AccountCode = m_AccountCode;
    }

    public String getM_Sign() {
        return m_Sign;
    }

    public void setM_Sign(String m_Sign) {
        this.m_Sign = m_Sign;
    }

    public String getM_Amount() {
        return m_Amount;
    }

    public void setM_Amount(String m_Amount) {
        this.m_Amount = m_Amount;
    }

    public String getM_AccountDescription() {
        return m_AccountDescription;
    }

    public void setM_AccountDescription(String m_AccountDescription) {
        this.m_AccountDescription = m_AccountDescription;
    }

    public String getM_Mancomunado() {
        return m_Mancomunado;
    }

    public void setM_Mancomunado(String m_Mancomunado) {
        this.m_Mancomunado = m_Mancomunado;
    }

    public String getM_AccountSituation() {
        return m_AccountSituation;
    }

    public void setM_AccountSituation(String m_AccountSituation) {
        this.m_AccountSituation = m_AccountSituation;
    }

    public String getM_CountableBalance() {
        return m_CountableBalance;
    }

    public void setM_CountableBalance(String m_CountableBalance) {
        this.m_CountableBalance = m_CountableBalance;
    }

    public String getM_AvailableBalance() {
        return m_AvailableBalance;
    }

    public void setM_AvailableBalance(String m_AvailableBalance) {
        this.m_AvailableBalance = m_AvailableBalance;
    }

    public String getM_RetainedBalance() {
        return m_RetainedBalance;
    }

    public void setM_RetainedBalance(String m_RetainedBalance) {
        this.m_RetainedBalance = m_RetainedBalance;
    }

    public String getM_LoanNumber() {
        return m_LoanNumber;
    }

    public void setM_LoanNumber(String m_LoanNumber) {
        this.m_LoanNumber = m_LoanNumber;
    }

    //jwong 19/03/2009 para manejo de la consulta en pago de servicios
    public String getM_CodigoImporte() {
        return this.m_AccountCode + ";" + this.m_AvailableBalance;
    }
    //jwong 19/03/2009 para manejo de la consulta en pago de servicios
    public String getM_DescripcionSaldoDisponible() {
        return this.m_AccountDescription + " " + this.m_AccountCode + " - Saldo: " + this.m_Currency + " " + this.m_AvailableBalance;
    }
	//jwong 31/03/2009 para manejo de pago SEDAPAL
    public String getM_CodigoImporteMoneda() {
        return this.m_AccountCode + ";" + this.m_AvailableBalance + ";" + this.m_Currency;
    }
}

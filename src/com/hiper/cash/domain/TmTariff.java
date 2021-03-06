package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * TmTariff generated by hbm2java
 */
public class TmTariff  implements java.io.Serializable {


     private String ctfTariffId;
     private String ftfAplicDate;
     private String ctfApplicationId;
     private String ctfCurrency;
     private String ctfDescription1;
     private BigDecimal ntfAmount1;
     private String ctfDescription2;
     private BigDecimal ntfAmount2;
     private String ctfDescription3;
     private BigDecimal ntfAmount3;
     private String ctfDescription4;
     private BigDecimal ntfAmount4;
     private BigDecimal ctfDeniedAmount;
     private String ctfModifyUser;
     private String ftfModifyDate;
     private String htfModifyTime;

    public TmTariff() {
    }

	
    public TmTariff(String ctfTariffId, String ftfAplicDate, String ctfApplicationId, String ctfCurrency) {
        this.ctfTariffId = ctfTariffId;
        this.ftfAplicDate = ftfAplicDate;
        this.ctfApplicationId = ctfApplicationId;
        this.ctfCurrency = ctfCurrency;
    }
    public TmTariff(String ctfTariffId, String ftfAplicDate, String ctfApplicationId, String ctfCurrency, String ctfDescription1, BigDecimal ntfAmount1, String ctfDescription2, BigDecimal ntfAmount2, String ctfDescription3, BigDecimal ntfAmount3, String ctfDescription4, BigDecimal ntfAmount4, BigDecimal ctfDeniedAmount, String ctfModifyUser, String ftfModifyDate, String htfModifyTime) {
       this.ctfTariffId = ctfTariffId;
       this.ftfAplicDate = ftfAplicDate;
       this.ctfApplicationId = ctfApplicationId;
       this.ctfCurrency = ctfCurrency;
       this.ctfDescription1 = ctfDescription1;
       this.ntfAmount1 = ntfAmount1;
       this.ctfDescription2 = ctfDescription2;
       this.ntfAmount2 = ntfAmount2;
       this.ctfDescription3 = ctfDescription3;
       this.ntfAmount3 = ntfAmount3;
       this.ctfDescription4 = ctfDescription4;
       this.ntfAmount4 = ntfAmount4;
       this.ctfDeniedAmount = ctfDeniedAmount;
       this.ctfModifyUser = ctfModifyUser;
       this.ftfModifyDate = ftfModifyDate;
       this.htfModifyTime = htfModifyTime;
    }
   
    public String getCtfTariffId() {
        return this.ctfTariffId;
    }
    
    public void setCtfTariffId(String ctfTariffId) {
        this.ctfTariffId = ctfTariffId;
    }
    public String getFtfAplicDate() {
        return this.ftfAplicDate;
    }
    
    public void setFtfAplicDate(String ftfAplicDate) {
        this.ftfAplicDate = ftfAplicDate;
    }
    public String getCtfApplicationId() {
        return this.ctfApplicationId;
    }
    
    public void setCtfApplicationId(String ctfApplicationId) {
        this.ctfApplicationId = ctfApplicationId;
    }
    public String getCtfCurrency() {
        return this.ctfCurrency;
    }
    
    public void setCtfCurrency(String ctfCurrency) {
        this.ctfCurrency = ctfCurrency;
    }
    public String getCtfDescription1() {
        return this.ctfDescription1;
    }
    
    public void setCtfDescription1(String ctfDescription1) {
        this.ctfDescription1 = ctfDescription1;
    }
    public BigDecimal getNtfAmount1() {
        return this.ntfAmount1;
    }
    
    public void setNtfAmount1(BigDecimal ntfAmount1) {
        this.ntfAmount1 = ntfAmount1;
    }
    public String getCtfDescription2() {
        return this.ctfDescription2;
    }
    
    public void setCtfDescription2(String ctfDescription2) {
        this.ctfDescription2 = ctfDescription2;
    }
    public BigDecimal getNtfAmount2() {
        return this.ntfAmount2;
    }
    
    public void setNtfAmount2(BigDecimal ntfAmount2) {
        this.ntfAmount2 = ntfAmount2;
    }
    public String getCtfDescription3() {
        return this.ctfDescription3;
    }
    
    public void setCtfDescription3(String ctfDescription3) {
        this.ctfDescription3 = ctfDescription3;
    }
    public BigDecimal getNtfAmount3() {
        return this.ntfAmount3;
    }
    
    public void setNtfAmount3(BigDecimal ntfAmount3) {
        this.ntfAmount3 = ntfAmount3;
    }
    public String getCtfDescription4() {
        return this.ctfDescription4;
    }
    
    public void setCtfDescription4(String ctfDescription4) {
        this.ctfDescription4 = ctfDescription4;
    }
    public BigDecimal getNtfAmount4() {
        return this.ntfAmount4;
    }
    
    public void setNtfAmount4(BigDecimal ntfAmount4) {
        this.ntfAmount4 = ntfAmount4;
    }
    public BigDecimal getCtfDeniedAmount() {
        return this.ctfDeniedAmount;
    }
    
    public void setCtfDeniedAmount(BigDecimal ctfDeniedAmount) {
        this.ctfDeniedAmount = ctfDeniedAmount;
    }
    public String getCtfModifyUser() {
        return this.ctfModifyUser;
    }
    
    public void setCtfModifyUser(String ctfModifyUser) {
        this.ctfModifyUser = ctfModifyUser;
    }
    public String getFtfModifyDate() {
        return this.ftfModifyDate;
    }
    
    public void setFtfModifyDate(String ftfModifyDate) {
        this.ftfModifyDate = ftfModifyDate;
    }
    public String getHtfModifyTime() {
        return this.htfModifyTime;
    }
    
    public void setHtfModifyTime(String htfModifyTime) {
        this.htfModifyTime = htfModifyTime;
    }




}



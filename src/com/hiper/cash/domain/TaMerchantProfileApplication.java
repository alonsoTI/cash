package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * TaMerchantProfileApplication generated by hbm2java
 */
public class TaMerchantProfileApplication  implements java.io.Serializable {


     private TaMerchantProfileApplicationId id;
     private TmMerchantProfile tmMerchantProfile;
     private String cmaaccessType;
     private String cmacloseType;
     private String cmaaccessTrx;
     private String cmacardWriter;
     private String cmatip;
     private String cmamultCurrency;
     private String cmareqLastNumber;
     private String cmareqDocId;
     private String cmareqCashBack;
     private String cmatransTip;
     private BigDecimal nmafloorLimit;
     private BigDecimal nmarecAmount;
     private String cmamultiproduct;
     private String cmatrxDefault;
     private String cmamodifyUser;
     private String fmamodifyDate;
     private String hmamodifyTime;

    public TaMerchantProfileApplication() {
    }

	
    public TaMerchantProfileApplication(TaMerchantProfileApplicationId id, TmMerchantProfile tmMerchantProfile) {
        this.id = id;
        this.tmMerchantProfile = tmMerchantProfile;
    }
    public TaMerchantProfileApplication(TaMerchantProfileApplicationId id, TmMerchantProfile tmMerchantProfile, String cmaaccessType, String cmacloseType, String cmaaccessTrx, String cmacardWriter, String cmatip, String cmamultCurrency, String cmareqLastNumber, String cmareqDocId, String cmareqCashBack, String cmatransTip, BigDecimal nmafloorLimit, BigDecimal nmarecAmount, String cmamultiproduct, String cmatrxDefault, String cmamodifyUser, String fmamodifyDate, String hmamodifyTime) {
       this.id = id;
       this.tmMerchantProfile = tmMerchantProfile;
       this.cmaaccessType = cmaaccessType;
       this.cmacloseType = cmacloseType;
       this.cmaaccessTrx = cmaaccessTrx;
       this.cmacardWriter = cmacardWriter;
       this.cmatip = cmatip;
       this.cmamultCurrency = cmamultCurrency;
       this.cmareqLastNumber = cmareqLastNumber;
       this.cmareqDocId = cmareqDocId;
       this.cmareqCashBack = cmareqCashBack;
       this.cmatransTip = cmatransTip;
       this.nmafloorLimit = nmafloorLimit;
       this.nmarecAmount = nmarecAmount;
       this.cmamultiproduct = cmamultiproduct;
       this.cmatrxDefault = cmatrxDefault;
       this.cmamodifyUser = cmamodifyUser;
       this.fmamodifyDate = fmamodifyDate;
       this.hmamodifyTime = hmamodifyTime;
    }
   
    public TaMerchantProfileApplicationId getId() {
        return this.id;
    }
    
    public void setId(TaMerchantProfileApplicationId id) {
        this.id = id;
    }
    public TmMerchantProfile getTmMerchantProfile() {
        return this.tmMerchantProfile;
    }
    
    public void setTmMerchantProfile(TmMerchantProfile tmMerchantProfile) {
        this.tmMerchantProfile = tmMerchantProfile;
    }
    public String getCmaaccessType() {
        return this.cmaaccessType;
    }
    
    public void setCmaaccessType(String cmaaccessType) {
        this.cmaaccessType = cmaaccessType;
    }
    public String getCmacloseType() {
        return this.cmacloseType;
    }
    
    public void setCmacloseType(String cmacloseType) {
        this.cmacloseType = cmacloseType;
    }
    public String getCmaaccessTrx() {
        return this.cmaaccessTrx;
    }
    
    public void setCmaaccessTrx(String cmaaccessTrx) {
        this.cmaaccessTrx = cmaaccessTrx;
    }
    public String getCmacardWriter() {
        return this.cmacardWriter;
    }
    
    public void setCmacardWriter(String cmacardWriter) {
        this.cmacardWriter = cmacardWriter;
    }
    public String getCmatip() {
        return this.cmatip;
    }
    
    public void setCmatip(String cmatip) {
        this.cmatip = cmatip;
    }
    public String getCmamultCurrency() {
        return this.cmamultCurrency;
    }
    
    public void setCmamultCurrency(String cmamultCurrency) {
        this.cmamultCurrency = cmamultCurrency;
    }
    public String getCmareqLastNumber() {
        return this.cmareqLastNumber;
    }
    
    public void setCmareqLastNumber(String cmareqLastNumber) {
        this.cmareqLastNumber = cmareqLastNumber;
    }
    public String getCmareqDocId() {
        return this.cmareqDocId;
    }
    
    public void setCmareqDocId(String cmareqDocId) {
        this.cmareqDocId = cmareqDocId;
    }
    public String getCmareqCashBack() {
        return this.cmareqCashBack;
    }
    
    public void setCmareqCashBack(String cmareqCashBack) {
        this.cmareqCashBack = cmareqCashBack;
    }
    public String getCmatransTip() {
        return this.cmatransTip;
    }
    
    public void setCmatransTip(String cmatransTip) {
        this.cmatransTip = cmatransTip;
    }
    public BigDecimal getNmafloorLimit() {
        return this.nmafloorLimit;
    }
    
    public void setNmafloorLimit(BigDecimal nmafloorLimit) {
        this.nmafloorLimit = nmafloorLimit;
    }
    public BigDecimal getNmarecAmount() {
        return this.nmarecAmount;
    }
    
    public void setNmarecAmount(BigDecimal nmarecAmount) {
        this.nmarecAmount = nmarecAmount;
    }
    public String getCmamultiproduct() {
        return this.cmamultiproduct;
    }
    
    public void setCmamultiproduct(String cmamultiproduct) {
        this.cmamultiproduct = cmamultiproduct;
    }
    public String getCmatrxDefault() {
        return this.cmatrxDefault;
    }
    
    public void setCmatrxDefault(String cmatrxDefault) {
        this.cmatrxDefault = cmatrxDefault;
    }
    public String getCmamodifyUser() {
        return this.cmamodifyUser;
    }
    
    public void setCmamodifyUser(String cmamodifyUser) {
        this.cmamodifyUser = cmamodifyUser;
    }
    public String getFmamodifyDate() {
        return this.fmamodifyDate;
    }
    
    public void setFmamodifyDate(String fmamodifyDate) {
        this.fmamodifyDate = fmamodifyDate;
    }
    public String getHmamodifyTime() {
        return this.hmamodifyTime;
    }
    
    public void setHmamodifyTime(String hmamodifyTime) {
        this.hmamodifyTime = hmamodifyTime;
    }




}



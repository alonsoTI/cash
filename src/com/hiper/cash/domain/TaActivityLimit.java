package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * TaActivityLimit generated by hbm2java
 */
public class TaActivityLimit  implements java.io.Serializable {


     private TaActivityLimitId id;
     private TmTransactionType tmTransactionType;
     private TmGroupCard tmGroupCard;
     private BigDecimal nallimitAmount;
     private Short nallimitTimes;
     private String calaction;
     private BigDecimal nallimitAmountDollar;
     private String calmodifyUser;
     private String falmodifyDate;
     private String halmodifyTime;

    public TaActivityLimit() {
    }

	
    public TaActivityLimit(TaActivityLimitId id, TmTransactionType tmTransactionType, TmGroupCard tmGroupCard) {
        this.id = id;
        this.tmTransactionType = tmTransactionType;
        this.tmGroupCard = tmGroupCard;
    }
    public TaActivityLimit(TaActivityLimitId id, TmTransactionType tmTransactionType, TmGroupCard tmGroupCard, BigDecimal nallimitAmount, Short nallimitTimes, String calaction, BigDecimal nallimitAmountDollar, String calmodifyUser, String falmodifyDate, String halmodifyTime) {
       this.id = id;
       this.tmTransactionType = tmTransactionType;
       this.tmGroupCard = tmGroupCard;
       this.nallimitAmount = nallimitAmount;
       this.nallimitTimes = nallimitTimes;
       this.calaction = calaction;
       this.nallimitAmountDollar = nallimitAmountDollar;
       this.calmodifyUser = calmodifyUser;
       this.falmodifyDate = falmodifyDate;
       this.halmodifyTime = halmodifyTime;
    }
   
    public TaActivityLimitId getId() {
        return this.id;
    }
    
    public void setId(TaActivityLimitId id) {
        this.id = id;
    }
    public TmTransactionType getTmTransactionType() {
        return this.tmTransactionType;
    }
    
    public void setTmTransactionType(TmTransactionType tmTransactionType) {
        this.tmTransactionType = tmTransactionType;
    }
    public TmGroupCard getTmGroupCard() {
        return this.tmGroupCard;
    }
    
    public void setTmGroupCard(TmGroupCard tmGroupCard) {
        this.tmGroupCard = tmGroupCard;
    }
    public BigDecimal getNallimitAmount() {
        return this.nallimitAmount;
    }
    
    public void setNallimitAmount(BigDecimal nallimitAmount) {
        this.nallimitAmount = nallimitAmount;
    }
    public Short getNallimitTimes() {
        return this.nallimitTimes;
    }
    
    public void setNallimitTimes(Short nallimitTimes) {
        this.nallimitTimes = nallimitTimes;
    }
    public String getCalaction() {
        return this.calaction;
    }
    
    public void setCalaction(String calaction) {
        this.calaction = calaction;
    }
    public BigDecimal getNallimitAmountDollar() {
        return this.nallimitAmountDollar;
    }
    
    public void setNallimitAmountDollar(BigDecimal nallimitAmountDollar) {
        this.nallimitAmountDollar = nallimitAmountDollar;
    }
    public String getCalmodifyUser() {
        return this.calmodifyUser;
    }
    
    public void setCalmodifyUser(String calmodifyUser) {
        this.calmodifyUser = calmodifyUser;
    }
    public String getFalmodifyDate() {
        return this.falmodifyDate;
    }
    
    public void setFalmodifyDate(String falmodifyDate) {
        this.falmodifyDate = falmodifyDate;
    }
    public String getHalmodifyTime() {
        return this.halmodifyTime;
    }
    
    public void setHalmodifyTime(String halmodifyTime) {
        this.halmodifyTime = halmodifyTime;
    }




}



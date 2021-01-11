package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * TmAcquirer generated by hbm2java
 */
public class TmAcquirer  implements java.io.Serializable {


     private String cacAcquirerId;
     private String dacDescription;
     private String cacPrimaryHost;
     private String cacHostBackup;
     private String cacMsgFormat;
     private String hacSettleHour;
     private String cacProcessType;
     private String cacSettleFlag;
     private String cacTypeSettle;
     private String cacNii;
     private String cacKey;
     private String cacEchoTest;
     private String cacModifyUser;
     private String facModifyDate;
     private String hacModifyTime;
     private Set tmGroupCards = new HashSet(0);

    public TmAcquirer() {
    }

	
    public TmAcquirer(String cacAcquirerId, String dacDescription, String cacPrimaryHost, String cacHostBackup, String cacMsgFormat, String hacSettleHour, String cacProcessType, String cacSettleFlag) {
        this.cacAcquirerId = cacAcquirerId;
        this.dacDescription = dacDescription;
        this.cacPrimaryHost = cacPrimaryHost;
        this.cacHostBackup = cacHostBackup;
        this.cacMsgFormat = cacMsgFormat;
        this.hacSettleHour = hacSettleHour;
        this.cacProcessType = cacProcessType;
        this.cacSettleFlag = cacSettleFlag;
    }
    public TmAcquirer(String cacAcquirerId, String dacDescription, String cacPrimaryHost, String cacHostBackup, String cacMsgFormat, String hacSettleHour, String cacProcessType, String cacSettleFlag, String cacTypeSettle, String cacNii, String cacKey, String cacEchoTest, String cacModifyUser, String facModifyDate, String hacModifyTime, Set tmGroupCards) {
       this.cacAcquirerId = cacAcquirerId;
       this.dacDescription = dacDescription;
       this.cacPrimaryHost = cacPrimaryHost;
       this.cacHostBackup = cacHostBackup;
       this.cacMsgFormat = cacMsgFormat;
       this.hacSettleHour = hacSettleHour;
       this.cacProcessType = cacProcessType;
       this.cacSettleFlag = cacSettleFlag;
       this.cacTypeSettle = cacTypeSettle;
       this.cacNii = cacNii;
       this.cacKey = cacKey;
       this.cacEchoTest = cacEchoTest;
       this.cacModifyUser = cacModifyUser;
       this.facModifyDate = facModifyDate;
       this.hacModifyTime = hacModifyTime;
       this.tmGroupCards = tmGroupCards;
    }
   
    public String getCacAcquirerId() {
        return this.cacAcquirerId;
    }
    
    public void setCacAcquirerId(String cacAcquirerId) {
        this.cacAcquirerId = cacAcquirerId;
    }
    public String getDacDescription() {
        return this.dacDescription;
    }
    
    public void setDacDescription(String dacDescription) {
        this.dacDescription = dacDescription;
    }
    public String getCacPrimaryHost() {
        return this.cacPrimaryHost;
    }
    
    public void setCacPrimaryHost(String cacPrimaryHost) {
        this.cacPrimaryHost = cacPrimaryHost;
    }
    public String getCacHostBackup() {
        return this.cacHostBackup;
    }
    
    public void setCacHostBackup(String cacHostBackup) {
        this.cacHostBackup = cacHostBackup;
    }
    public String getCacMsgFormat() {
        return this.cacMsgFormat;
    }
    
    public void setCacMsgFormat(String cacMsgFormat) {
        this.cacMsgFormat = cacMsgFormat;
    }
    public String getHacSettleHour() {
        return this.hacSettleHour;
    }
    
    public void setHacSettleHour(String hacSettleHour) {
        this.hacSettleHour = hacSettleHour;
    }
    public String getCacProcessType() {
        return this.cacProcessType;
    }
    
    public void setCacProcessType(String cacProcessType) {
        this.cacProcessType = cacProcessType;
    }
    public String getCacSettleFlag() {
        return this.cacSettleFlag;
    }
    
    public void setCacSettleFlag(String cacSettleFlag) {
        this.cacSettleFlag = cacSettleFlag;
    }
    public String getCacTypeSettle() {
        return this.cacTypeSettle;
    }
    
    public void setCacTypeSettle(String cacTypeSettle) {
        this.cacTypeSettle = cacTypeSettle;
    }
    public String getCacNii() {
        return this.cacNii;
    }
    
    public void setCacNii(String cacNii) {
        this.cacNii = cacNii;
    }
    public String getCacKey() {
        return this.cacKey;
    }
    
    public void setCacKey(String cacKey) {
        this.cacKey = cacKey;
    }
    public String getCacEchoTest() {
        return this.cacEchoTest;
    }
    
    public void setCacEchoTest(String cacEchoTest) {
        this.cacEchoTest = cacEchoTest;
    }
    public String getCacModifyUser() {
        return this.cacModifyUser;
    }
    
    public void setCacModifyUser(String cacModifyUser) {
        this.cacModifyUser = cacModifyUser;
    }
    public String getFacModifyDate() {
        return this.facModifyDate;
    }
    
    public void setFacModifyDate(String facModifyDate) {
        this.facModifyDate = facModifyDate;
    }
    public String getHacModifyTime() {
        return this.hacModifyTime;
    }
    
    public void setHacModifyTime(String hacModifyTime) {
        this.hacModifyTime = hacModifyTime;
    }
    public Set getTmGroupCards() {
        return this.tmGroupCards;
    }
    
    public void setTmGroupCards(Set tmGroupCards) {
        this.tmGroupCards = tmGroupCards;
    }




}


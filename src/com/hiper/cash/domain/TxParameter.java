package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA



/**
 * TxParameter generated by hbm2java
 */
public class TxParameter  implements java.io.Serializable {


     private String cpmParameterId;
     private Long npmNumericValue;
     private String dpmTextValue;
     private String dpmDescription;
     private String cpmModifyUser;
     private String fpmModifyDate;
     private String hpmModifyTime;

    public TxParameter() {
    }

	
    public TxParameter(String cpmParameterId) {
        this.cpmParameterId = cpmParameterId;
    }
    public TxParameter(String cpmParameterId, Long npmNumericValue, String dpmTextValue, String dpmDescription, String cpmModifyUser, String fpmModifyDate, String hpmModifyTime) {
       this.cpmParameterId = cpmParameterId;
       this.npmNumericValue = npmNumericValue;
       this.dpmTextValue = dpmTextValue;
       this.dpmDescription = dpmDescription;
       this.cpmModifyUser = cpmModifyUser;
       this.fpmModifyDate = fpmModifyDate;
       this.hpmModifyTime = hpmModifyTime;
    }
   
    public String getCpmParameterId() {
        return this.cpmParameterId;
    }
    
    public void setCpmParameterId(String cpmParameterId) {
        this.cpmParameterId = cpmParameterId;
    }
    public Long getNpmNumericValue() {
        return this.npmNumericValue;
    }
    
    public void setNpmNumericValue(Long npmNumericValue) {
        this.npmNumericValue = npmNumericValue;
    }
    public String getDpmTextValue() {
        return this.dpmTextValue;
    }
    
    public void setDpmTextValue(String dpmTextValue) {
        this.dpmTextValue = dpmTextValue;
    }
    public String getDpmDescription() {
        return this.dpmDescription;
    }
    
    public void setDpmDescription(String dpmDescription) {
        this.dpmDescription = dpmDescription;
    }
    public String getCpmModifyUser() {
        return this.cpmModifyUser;
    }
    
    public void setCpmModifyUser(String cpmModifyUser) {
        this.cpmModifyUser = cpmModifyUser;
    }
    public String getFpmModifyDate() {
        return this.fpmModifyDate;
    }
    
    public void setFpmModifyDate(String fpmModifyDate) {
        this.fpmModifyDate = fpmModifyDate;
    }
    public String getHpmModifyTime() {
        return this.hpmModifyTime;
    }
    
    public void setHpmModifyTime(String hpmModifyTime) {
        this.hpmModifyTime = hpmModifyTime;
    }




}



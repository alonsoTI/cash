package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA



/**
 * TxListField generated by hbm2java
 */
public class TxListField  implements java.io.Serializable {


     private TxListFieldId id;
     private String dlfDescription;
     private String clfModifyUser;
     private String flfModifyDate;
     private String hlfModifyTime;

    public TxListField() {
    }

	
    public TxListField(TxListFieldId id, String dlfDescription) {
        this.id = id;
        this.dlfDescription = dlfDescription;
    }
    public TxListField(TxListFieldId id, String dlfDescription, String clfModifyUser, String flfModifyDate, String hlfModifyTime) {
       this.id = id;
       this.dlfDescription = dlfDescription;
       this.clfModifyUser = clfModifyUser;
       this.flfModifyDate = flfModifyDate;
       this.hlfModifyTime = hlfModifyTime;
    }
   
    public TxListFieldId getId() {
        return this.id;
    }
    
    public void setId(TxListFieldId id) {
        this.id = id;
    }
    public String getDlfDescription() {
        return this.dlfDescription;
    }
    
    public void setDlfDescription(String dlfDescription) {
        this.dlfDescription = dlfDescription;
    }
    public String getClfModifyUser() {
        return this.clfModifyUser;
    }
    
    public void setClfModifyUser(String clfModifyUser) {
        this.clfModifyUser = clfModifyUser;
    }
    public String getFlfModifyDate() {
        return this.flfModifyDate;
    }
    
    public void setFlfModifyDate(String flfModifyDate) {
        this.flfModifyDate = flfModifyDate;
    }
    public String getHlfModifyTime() {
        return this.hlfModifyTime;
    }
    
    public void setHlfModifyTime(String hlfModifyTime) {
        this.hlfModifyTime = hlfModifyTime;
    }
    public String getId_Description() {
        return this.id.getClfCode() +";"+ this.dlfDescription;
    }
}



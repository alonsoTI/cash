package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA



/**
 * TaTerminalApplication generated by hbm2java
 */
public class TaTerminalApplication  implements java.io.Serializable {


     private TaTerminalApplicationId id;
     private TmTerminal tmTerminal;
     private TmApplication tmApplication;
     private String ctaterminalSn;
     private Integer ntafilesGroup;
     private Long ctafilesVersion;
     private Character ctastatus;
     private Long ntaapplicationFlowSize;
     private String ctamodifyUser;
     private String ftamodifyDate;
     private String htamodifyTime;
     private String ctareqCashBack;
     private String ctamultiproduct;
     private String ctacardWriter;
     private Character ctaonlineTip;
     private Character ctasaveMoney;

    public TaTerminalApplication() {
    }

	
    public TaTerminalApplication(TaTerminalApplicationId id, TmTerminal tmTerminal, TmApplication tmApplication) {
        this.id = id;
        this.tmTerminal = tmTerminal;
        this.tmApplication = tmApplication;
    }
    public TaTerminalApplication(TaTerminalApplicationId id, TmTerminal tmTerminal, TmApplication tmApplication, String ctaterminalSn, Integer ntafilesGroup, Long ctafilesVersion, Character ctastatus, Long ntaapplicationFlowSize, String ctamodifyUser, String ftamodifyDate, String htamodifyTime, String ctareqCashBack, String ctamultiproduct, String ctacardWriter, Character ctaonlineTip, Character ctasaveMoney) {
       this.id = id;
       this.tmTerminal = tmTerminal;
       this.tmApplication = tmApplication;
       this.ctaterminalSn = ctaterminalSn;
       this.ntafilesGroup = ntafilesGroup;
       this.ctafilesVersion = ctafilesVersion;
       this.ctastatus = ctastatus;
       this.ntaapplicationFlowSize = ntaapplicationFlowSize;
       this.ctamodifyUser = ctamodifyUser;
       this.ftamodifyDate = ftamodifyDate;
       this.htamodifyTime = htamodifyTime;
       this.ctareqCashBack = ctareqCashBack;
       this.ctamultiproduct = ctamultiproduct;
       this.ctacardWriter = ctacardWriter;
       this.ctaonlineTip = ctaonlineTip;
       this.ctasaveMoney = ctasaveMoney;
    }
   
    public TaTerminalApplicationId getId() {
        return this.id;
    }
    
    public void setId(TaTerminalApplicationId id) {
        this.id = id;
    }
    public TmTerminal getTmTerminal() {
        return this.tmTerminal;
    }
    
    public void setTmTerminal(TmTerminal tmTerminal) {
        this.tmTerminal = tmTerminal;
    }
    public TmApplication getTmApplication() {
        return this.tmApplication;
    }
    
    public void setTmApplication(TmApplication tmApplication) {
        this.tmApplication = tmApplication;
    }
    public String getCtaterminalSn() {
        return this.ctaterminalSn;
    }
    
    public void setCtaterminalSn(String ctaterminalSn) {
        this.ctaterminalSn = ctaterminalSn;
    }
    public Integer getNtafilesGroup() {
        return this.ntafilesGroup;
    }
    
    public void setNtafilesGroup(Integer ntafilesGroup) {
        this.ntafilesGroup = ntafilesGroup;
    }
    public Long getCtafilesVersion() {
        return this.ctafilesVersion;
    }
    
    public void setCtafilesVersion(Long ctafilesVersion) {
        this.ctafilesVersion = ctafilesVersion;
    }
    public Character getCtastatus() {
        return this.ctastatus;
    }
    
    public void setCtastatus(Character ctastatus) {
        this.ctastatus = ctastatus;
    }
    public Long getNtaapplicationFlowSize() {
        return this.ntaapplicationFlowSize;
    }
    
    public void setNtaapplicationFlowSize(Long ntaapplicationFlowSize) {
        this.ntaapplicationFlowSize = ntaapplicationFlowSize;
    }
    public String getCtamodifyUser() {
        return this.ctamodifyUser;
    }
    
    public void setCtamodifyUser(String ctamodifyUser) {
        this.ctamodifyUser = ctamodifyUser;
    }
    public String getFtamodifyDate() {
        return this.ftamodifyDate;
    }
    
    public void setFtamodifyDate(String ftamodifyDate) {
        this.ftamodifyDate = ftamodifyDate;
    }
    public String getHtamodifyTime() {
        return this.htamodifyTime;
    }
    
    public void setHtamodifyTime(String htamodifyTime) {
        this.htamodifyTime = htamodifyTime;
    }
    public String getCtareqCashBack() {
        return this.ctareqCashBack;
    }
    
    public void setCtareqCashBack(String ctareqCashBack) {
        this.ctareqCashBack = ctareqCashBack;
    }
    public String getCtamultiproduct() {
        return this.ctamultiproduct;
    }
    
    public void setCtamultiproduct(String ctamultiproduct) {
        this.ctamultiproduct = ctamultiproduct;
    }
    public String getCtacardWriter() {
        return this.ctacardWriter;
    }
    
    public void setCtacardWriter(String ctacardWriter) {
        this.ctacardWriter = ctacardWriter;
    }
    public Character getCtaonlineTip() {
        return this.ctaonlineTip;
    }
    
    public void setCtaonlineTip(Character ctaonlineTip) {
        this.ctaonlineTip = ctaonlineTip;
    }
    public Character getCtasaveMoney() {
        return this.ctasaveMoney;
    }
    
    public void setCtasaveMoney(Character ctasaveMoney) {
        this.ctasaveMoney = ctasaveMoney;
    }




}


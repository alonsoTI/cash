package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * TaTipoPagoServicio generated by hbm2java
 */
public class TaTipoPagoServicio  implements java.io.Serializable {


     private TaTipoPagoServicioId id;
     private TaServicioxEmpresa taServicioxEmpresa;
     private TmTipoPago tmTipoPago;
     private String ctpsmodifyUser;
     private String ftpsmodifyDate;
     private String htpsmodifyTime;
     private Set taCamposRequeridoses = new HashSet(0);

    public TaTipoPagoServicio() {
    }

	
    public TaTipoPagoServicio(TaTipoPagoServicioId id, TaServicioxEmpresa taServicioxEmpresa, TmTipoPago tmTipoPago) {
        this.id = id;
        this.taServicioxEmpresa = taServicioxEmpresa;
        this.tmTipoPago = tmTipoPago;
    }
    public TaTipoPagoServicio(TaTipoPagoServicioId id, TaServicioxEmpresa taServicioxEmpresa, TmTipoPago tmTipoPago, String ctpsmodifyUser, String ftpsmodifyDate, String htpsmodifyTime, Set taCamposRequeridoses) {
       this.id = id;
       this.taServicioxEmpresa = taServicioxEmpresa;
       this.tmTipoPago = tmTipoPago;
       this.ctpsmodifyUser = ctpsmodifyUser;
       this.ftpsmodifyDate = ftpsmodifyDate;
       this.htpsmodifyTime = htpsmodifyTime;
       this.taCamposRequeridoses = taCamposRequeridoses;
    }
   
    public TaTipoPagoServicioId getId() {
        return this.id;
    }
    
    public void setId(TaTipoPagoServicioId id) {
        this.id = id;
    }
    public TaServicioxEmpresa getTaServicioxEmpresa() {
        return this.taServicioxEmpresa;
    }
    
    public void setTaServicioxEmpresa(TaServicioxEmpresa taServicioxEmpresa) {
        this.taServicioxEmpresa = taServicioxEmpresa;
    }
    public TmTipoPago getTmTipoPago() {
        return this.tmTipoPago;
    }
    
    public void setTmTipoPago(TmTipoPago tmTipoPago) {
        this.tmTipoPago = tmTipoPago;
    }
    public String getCtpsmodifyUser() {
        return this.ctpsmodifyUser;
    }
    
    public void setCtpsmodifyUser(String ctpsmodifyUser) {
        this.ctpsmodifyUser = ctpsmodifyUser;
    }
    public String getFtpsmodifyDate() {
        return this.ftpsmodifyDate;
    }
    
    public void setFtpsmodifyDate(String ftpsmodifyDate) {
        this.ftpsmodifyDate = ftpsmodifyDate;
    }
    public String getHtpsmodifyTime() {
        return this.htpsmodifyTime;
    }
    
    public void setHtpsmodifyTime(String htpsmodifyTime) {
        this.htpsmodifyTime = htpsmodifyTime;
    }
    public Set getTaCamposRequeridoses() {
        return this.taCamposRequeridoses;
    }
    
    public void setTaCamposRequeridoses(Set taCamposRequeridoses) {
        this.taCamposRequeridoses = taCamposRequeridoses;
    }




}


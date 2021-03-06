package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA



/**
 * TaDetalleMapaCampos generated by hbm2java
 */
public class TaDetalleMapaCampos  implements java.io.Serializable {


     private TaDetalleMapaCamposId id;
     private TaMapaCampos taMapaCampos;
     private String ddmetiqueta;
     private String ddmnomVariable;
     private String ddmdescripcion;
     private Integer ndmlongMax;
     private Integer ndmlongMin;
     private String ddmtipoDato;
     private Character cdmflagObligatorio;
     private String ddmcontrolGui;
     private String ddmtablaRef;
     private String ddmcampoRef;
     private String ddmvalidacion;
     private String cdmmodifyUser;
     private String fdmmodifyDate;
     private String hdmmodifyTime;

     private String valor;

    public TaDetalleMapaCampos() {
    }

	
    public TaDetalleMapaCampos(TaDetalleMapaCamposId id, TaMapaCampos taMapaCampos) {
        this.id = id;
        this.taMapaCampos = taMapaCampos;
    }
    public TaDetalleMapaCampos(TaDetalleMapaCamposId id, TaMapaCampos taMapaCampos, String ddmetiqueta, String ddmnomVariable, String ddmdescripcion, Integer ndmlongMax, Integer ndmlongMin, String ddmtipoDato, Character cdmflagObligatorio, String ddmcontrolGui, String ddmtablaRef, String ddmcampoRef, String ddmvalidacion, String cdmmodifyUser, String fdmmodifyDate, String hdmmodifyTime) {
       this.id = id;
       this.taMapaCampos = taMapaCampos;
       this.ddmetiqueta = ddmetiqueta;
       this.ddmnomVariable = ddmnomVariable;
       this.ddmdescripcion = ddmdescripcion;
       this.ndmlongMax = ndmlongMax;
       this.ndmlongMin = ndmlongMin;
       this.ddmtipoDato = ddmtipoDato;
       this.cdmflagObligatorio = cdmflagObligatorio;
       this.ddmcontrolGui = ddmcontrolGui;
       this.ddmtablaRef = ddmtablaRef;
       this.ddmcampoRef = ddmcampoRef;
       this.ddmvalidacion = ddmvalidacion;
       this.cdmmodifyUser = cdmmodifyUser;
       this.fdmmodifyDate = fdmmodifyDate;
       this.hdmmodifyTime = hdmmodifyTime;
    }
   
    public TaDetalleMapaCamposId getId() {
        return this.id;
    }
    
    public void setId(TaDetalleMapaCamposId id) {
        this.id = id;
    }
    public TaMapaCampos getTaMapaCampos() {
        return this.taMapaCampos;
    }
    
    public void setTaMapaCampos(TaMapaCampos taMapaCampos) {
        this.taMapaCampos = taMapaCampos;
    }
    public String getDdmetiqueta() {
        return this.ddmetiqueta;
    }
    
    public void setDdmetiqueta(String ddmetiqueta) {
        this.ddmetiqueta = ddmetiqueta;
    }
    public String getDdmnomVariable() {
        return this.ddmnomVariable;
    }
    
    public void setDdmnomVariable(String ddmnomVariable) {
        this.ddmnomVariable = ddmnomVariable;
    }
    public String getDdmdescripcion() {
        return this.ddmdescripcion;
    }
    
    public void setDdmdescripcion(String ddmdescripcion) {
        this.ddmdescripcion = ddmdescripcion;
    }
    public Integer getNdmlongMax() {
        return this.ndmlongMax;
    }
    
    public void setNdmlongMax(Integer ndmlongMax) {
        this.ndmlongMax = ndmlongMax;
    }
    public Integer getNdmlongMin() {
        return this.ndmlongMin;
    }
    
    public void setNdmlongMin(Integer ndmlongMin) {
        this.ndmlongMin = ndmlongMin;
    }
    public String getDdmtipoDato() {
        return this.ddmtipoDato;
    }
    
    public void setDdmtipoDato(String ddmtipoDato) {
        this.ddmtipoDato = ddmtipoDato;
    }
    public Character getCdmflagObligatorio() {
        return this.cdmflagObligatorio;
    }
    
    public void setCdmflagObligatorio(Character cdmflagObligatorio) {
        this.cdmflagObligatorio = cdmflagObligatorio;
    }
    public String getDdmcontrolGui() {
        return this.ddmcontrolGui;
    }
    
    public void setDdmcontrolGui(String ddmcontrolGui) {
        this.ddmcontrolGui = ddmcontrolGui;
    }
    public String getDdmtablaRef() {
        return this.ddmtablaRef;
    }
    
    public void setDdmtablaRef(String ddmtablaRef) {
        this.ddmtablaRef = ddmtablaRef;
    }
    public String getDdmcampoRef() {
        return this.ddmcampoRef;
    }
    
    public void setDdmcampoRef(String ddmcampoRef) {
        this.ddmcampoRef = ddmcampoRef;
    }
    public String getDdmvalidacion() {
        return this.ddmvalidacion;
    }
    
    public void setDdmvalidacion(String ddmvalidacion) {
        this.ddmvalidacion = ddmvalidacion;
    }
    public String getCdmmodifyUser() {
        return this.cdmmodifyUser;
    }
    
    public void setCdmmodifyUser(String cdmmodifyUser) {
        this.cdmmodifyUser = cdmmodifyUser;
    }
    public String getFdmmodifyDate() {
        return this.fdmmodifyDate;
    }
    
    public void setFdmmodifyDate(String fdmmodifyDate) {
        this.fdmmodifyDate = fdmmodifyDate;
    }
    public String getHdmmodifyTime() {
        return this.hdmmodifyTime;
    }
    
    public void setHdmmodifyTime(String hdmmodifyTime) {
        this.hdmmodifyTime = hdmmodifyTime;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }




}



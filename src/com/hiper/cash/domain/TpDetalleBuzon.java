package com.hiper.cash.domain;
// Generated 03/08/2009 06:16:35 PM by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * TpDetalleBuzon generated by hbm2java
 */
public class TpDetalleBuzon  implements java.io.Serializable {


     private TpDetalleBuzonId id;
     private TaBuzonEnvio taBuzonEnvio;
     private String ddbuCodOrientacion;
     private Integer ndbuSecuencial;
     private String ndbuCuentaEmpresa;
     private String ddbuComprobante;
     private String ddbuNomContrapartida;
     private String ddbuContrapartida;
     private String cdbuCodBanco;
     private String ndbuDocumento;
     private String ddbuNombre;
     private String ddbuDireccion;
     private String ddbuCiudad;
     private String ddbuLocalidad;
     private String ndbuNumeroCuenta;
     private String ddbuTipoCuenta;
     private BigDecimal ndbuMonto;
     private String ddbuMoneda;
     private String ddbuTelefono;
     private String ddbuEmail;
     private String ddbuDescripcion;
     private Character cdbuEstado;
     private String ddbuTipoPago;
     private String ddbuTipoDocumento;
     private String fdbuFechaProceso;
     private String hdbuHoraProceso;
     private String ndbuNumCuentaAbono;
     private String ndbuNumCuentaCargo;
     private String ddbuReferencia;
     private String ndbuNumCuentaAbonoCci;
     private String ddbuBancoBenef;
     private String cdbuTipoDocBenef;
     private String ndbuNumDocBenef;
     private String ddbuNombreBenef;
     private String ddbuApepatBenef;
     private String ddbuApeMatBenef;
     private String ddbuDireccionBenef;
     private String ddbuTlfBenef;
     private String fdbuFechaVenc;
     private String ddbuAdicional1;
     private String ddbuAdicional2;
     private String ddbuAdicional3;
     private String ddbuAdicional4;
     private String ddbuAdicional5;
     private String ddbuAdicional6;
     private String ddbuAdicional7;
     private String ddbuAdicional8;
     private String ddbuAdicional9;
     private String ddbuAdicional10;
     private String ddbuReferenciaAdicional;

    public TpDetalleBuzon() {
    }

	
    public TpDetalleBuzon(TpDetalleBuzonId id, TaBuzonEnvio taBuzonEnvio) {
        this.id = id;
        this.taBuzonEnvio = taBuzonEnvio;
    }
    public TpDetalleBuzon(TpDetalleBuzonId id, TaBuzonEnvio taBuzonEnvio, String ddbuCodOrientacion, Integer ndbuSecuencial, String ndbuCuentaEmpresa, String ddbuComprobante, String ddbuNomContrapartida, String ddbuContrapartida, String cdbuCodBanco, String ndbuDocumento, String ddbuNombre, String ddbuDireccion, String ddbuCiudad, String ddbuLocalidad, String ndbuNumeroCuenta, String ddbuTipoCuenta, BigDecimal ndbuMonto, String ddbuMoneda, String ddbuTelefono, String ddbuEmail, String ddbuDescripcion, Character cdbuEstado, String ddbuTipoPago, String ddbuTipoDocumento, String fdbuFechaProceso, String hdbuHoraProceso, String ndbuNumCuentaAbono, String ndbuNumCuentaCargo, String ddbuReferencia, String ndbuNumCuentaAbonoCci, String ddbuBancoBenef, String cdbuTipoDocBenef, String ndbuNumDocBenef, String ddbuNombreBenef, String ddbuApepatBenef, String ddbuApeMatBenef, String ddbuDireccionBenef, String ddbuTlfBenef, String fdbuFechaVenc, String ddbuAdicional1, String ddbuAdicional2, String ddbuAdicional3, String ddbuAdicional4, String ddbuAdicional5, String ddbuAdicional6, String ddbuAdicional7, String ddbuAdicional8, String ddbuAdicional9, String ddbuAdicional10, String ddbuReferenciaAdicional) {
       this.id = id;
       this.taBuzonEnvio = taBuzonEnvio;
       this.ddbuCodOrientacion = ddbuCodOrientacion;
       this.ndbuSecuencial = ndbuSecuencial;
       this.ndbuCuentaEmpresa = ndbuCuentaEmpresa;
       this.ddbuComprobante = ddbuComprobante;
       this.ddbuNomContrapartida = ddbuNomContrapartida;
       this.ddbuContrapartida = ddbuContrapartida;
       this.cdbuCodBanco = cdbuCodBanco;
       this.ndbuDocumento = ndbuDocumento;
       this.ddbuNombre = ddbuNombre;
       this.ddbuDireccion = ddbuDireccion;
       this.ddbuCiudad = ddbuCiudad;
       this.ddbuLocalidad = ddbuLocalidad;
       this.ndbuNumeroCuenta = ndbuNumeroCuenta;
       this.ddbuTipoCuenta = ddbuTipoCuenta;
       this.ndbuMonto = ndbuMonto;
       this.ddbuMoneda = ddbuMoneda;
       this.ddbuTelefono = ddbuTelefono;
       this.ddbuEmail = ddbuEmail;
       this.ddbuDescripcion = ddbuDescripcion;
       this.cdbuEstado = cdbuEstado;
       this.ddbuTipoPago = ddbuTipoPago;
       this.ddbuTipoDocumento = ddbuTipoDocumento;
       this.fdbuFechaProceso = fdbuFechaProceso;
       this.hdbuHoraProceso = hdbuHoraProceso;
       this.ndbuNumCuentaAbono = ndbuNumCuentaAbono;
       this.ndbuNumCuentaCargo = ndbuNumCuentaCargo;
       this.ddbuReferencia = ddbuReferencia;
       this.ndbuNumCuentaAbonoCci = ndbuNumCuentaAbonoCci;
       this.ddbuBancoBenef = ddbuBancoBenef;
       this.cdbuTipoDocBenef = cdbuTipoDocBenef;
       this.ndbuNumDocBenef = ndbuNumDocBenef;
       this.ddbuNombreBenef = ddbuNombreBenef;
       this.ddbuApepatBenef = ddbuApepatBenef;
       this.ddbuApeMatBenef = ddbuApeMatBenef;
       this.ddbuDireccionBenef = ddbuDireccionBenef;
       this.ddbuTlfBenef = ddbuTlfBenef;
       this.fdbuFechaVenc = fdbuFechaVenc;
       this.ddbuAdicional1 = ddbuAdicional1;
       this.ddbuAdicional2 = ddbuAdicional2;
       this.ddbuAdicional3 = ddbuAdicional3;
       this.ddbuAdicional4 = ddbuAdicional4;
       this.ddbuAdicional5 = ddbuAdicional5;
       this.ddbuAdicional6 = ddbuAdicional6;
       this.ddbuAdicional7 = ddbuAdicional7;
       this.ddbuAdicional8 = ddbuAdicional8;
       this.ddbuAdicional9 = ddbuAdicional9;
       this.ddbuAdicional10 = ddbuAdicional10;
       this.ddbuReferenciaAdicional = ddbuReferenciaAdicional;
    }
   
    public TpDetalleBuzonId getId() {
        return this.id;
    }
    
    public void setId(TpDetalleBuzonId id) {
        this.id = id;
    }
    public TaBuzonEnvio getTaBuzonEnvio() {
        return this.taBuzonEnvio;
    }
    
    public void setTaBuzonEnvio(TaBuzonEnvio taBuzonEnvio) {
        this.taBuzonEnvio = taBuzonEnvio;
    }
    public String getDdbuCodOrientacion() {
        return this.ddbuCodOrientacion;
    }
    
    public void setDdbuCodOrientacion(String ddbuCodOrientacion) {
        this.ddbuCodOrientacion = ddbuCodOrientacion;
    }
    public Integer getNdbuSecuencial() {
        return this.ndbuSecuencial;
    }
    
    public void setNdbuSecuencial(Integer ndbuSecuencial) {
        this.ndbuSecuencial = ndbuSecuencial;
    }
    public String getNdbuCuentaEmpresa() {
        return this.ndbuCuentaEmpresa;
    }
    
    public void setNdbuCuentaEmpresa(String ndbuCuentaEmpresa) {
        this.ndbuCuentaEmpresa = ndbuCuentaEmpresa;
    }
    public String getDdbuComprobante() {
        return this.ddbuComprobante;
    }
    
    public void setDdbuComprobante(String ddbuComprobante) {
        this.ddbuComprobante = ddbuComprobante;
    }
    public String getDdbuNomContrapartida() {
        return this.ddbuNomContrapartida;
    }
    
    public void setDdbuNomContrapartida(String ddbuNomContrapartida) {
        this.ddbuNomContrapartida = ddbuNomContrapartida;
    }
    public String getDdbuContrapartida() {
        return this.ddbuContrapartida;
    }
    
    public void setDdbuContrapartida(String ddbuContrapartida) {
        this.ddbuContrapartida = ddbuContrapartida;
    }
    public String getCdbuCodBanco() {
        return this.cdbuCodBanco;
    }
    
    public void setCdbuCodBanco(String cdbuCodBanco) {
        this.cdbuCodBanco = cdbuCodBanco;
    }
    public String getNdbuDocumento() {
        return this.ndbuDocumento;
    }
    
    public void setNdbuDocumento(String ndbuDocumento) {
        this.ndbuDocumento = ndbuDocumento;
    }
    public String getDdbuNombre() {
        return this.ddbuNombre;
    }
    
    public void setDdbuNombre(String ddbuNombre) {
        this.ddbuNombre = ddbuNombre;
    }
    public String getDdbuDireccion() {
        return this.ddbuDireccion;
    }
    
    public void setDdbuDireccion(String ddbuDireccion) {
        this.ddbuDireccion = ddbuDireccion;
    }
    public String getDdbuCiudad() {
        return this.ddbuCiudad;
    }
    
    public void setDdbuCiudad(String ddbuCiudad) {
        this.ddbuCiudad = ddbuCiudad;
    }
    public String getDdbuLocalidad() {
        return this.ddbuLocalidad;
    }
    
    public void setDdbuLocalidad(String ddbuLocalidad) {
        this.ddbuLocalidad = ddbuLocalidad;
    }
    public String getNdbuNumeroCuenta() {
        return this.ndbuNumeroCuenta;
    }
    
    public void setNdbuNumeroCuenta(String ndbuNumeroCuenta) {
        this.ndbuNumeroCuenta = ndbuNumeroCuenta;
    }
    public String getDdbuTipoCuenta() {
        return this.ddbuTipoCuenta;
    }
    
    public void setDdbuTipoCuenta(String ddbuTipoCuenta) {
        this.ddbuTipoCuenta = ddbuTipoCuenta;
    }
    public BigDecimal getNdbuMonto() {
        return this.ndbuMonto;
    }
    
    public void setNdbuMonto(BigDecimal ndbuMonto) {
        this.ndbuMonto = ndbuMonto;
    }
    public String getDdbuMoneda() {
        return this.ddbuMoneda;
    }
    
    public void setDdbuMoneda(String ddbuMoneda) {
        this.ddbuMoneda = ddbuMoneda;
    }
    public String getDdbuTelefono() {
        return this.ddbuTelefono;
    }
    
    public void setDdbuTelefono(String ddbuTelefono) {
        this.ddbuTelefono = ddbuTelefono;
    }
    public String getDdbuEmail() {
        return this.ddbuEmail;
    }
    
    public void setDdbuEmail(String ddbuEmail) {
        this.ddbuEmail = ddbuEmail;
    }
    public String getDdbuDescripcion() {
        return this.ddbuDescripcion;
    }
    
    public void setDdbuDescripcion(String ddbuDescripcion) {
        this.ddbuDescripcion = ddbuDescripcion;
    }
    public Character getCdbuEstado() {
        return this.cdbuEstado;
    }
    
    public void setCdbuEstado(Character cdbuEstado) {
        this.cdbuEstado = cdbuEstado;
    }
    public String getDdbuTipoPago() {
        return this.ddbuTipoPago;
    }
    
    public void setDdbuTipoPago(String ddbuTipoPago) {
        this.ddbuTipoPago = ddbuTipoPago;
    }
    public String getDdbuTipoDocumento() {
        return this.ddbuTipoDocumento;
    }
    
    public void setDdbuTipoDocumento(String ddbuTipoDocumento) {
        this.ddbuTipoDocumento = ddbuTipoDocumento;
    }
    public String getFdbuFechaProceso() {
        return this.fdbuFechaProceso;
    }
    
    public void setFdbuFechaProceso(String fdbuFechaProceso) {
        this.fdbuFechaProceso = fdbuFechaProceso;
    }
    public String getHdbuHoraProceso() {
        return this.hdbuHoraProceso;
    }
    
    public void setHdbuHoraProceso(String hdbuHoraProceso) {
        this.hdbuHoraProceso = hdbuHoraProceso;
    }
    public String getNdbuNumCuentaAbono() {
        return this.ndbuNumCuentaAbono;
    }
    
    public void setNdbuNumCuentaAbono(String ndbuNumCuentaAbono) {
        this.ndbuNumCuentaAbono = ndbuNumCuentaAbono;
    }
    public String getNdbuNumCuentaCargo() {
        return this.ndbuNumCuentaCargo;
    }
    
    public void setNdbuNumCuentaCargo(String ndbuNumCuentaCargo) {
        this.ndbuNumCuentaCargo = ndbuNumCuentaCargo;
    }
    public String getDdbuReferencia() {
        return this.ddbuReferencia;
    }
    
    public void setDdbuReferencia(String ddbuReferencia) {
        this.ddbuReferencia = ddbuReferencia;
    }
    public String getNdbuNumCuentaAbonoCci() {
        return this.ndbuNumCuentaAbonoCci;
    }
    
    public void setNdbuNumCuentaAbonoCci(String ndbuNumCuentaAbonoCci) {
        this.ndbuNumCuentaAbonoCci = ndbuNumCuentaAbonoCci;
    }
    public String getDdbuBancoBenef() {
        return this.ddbuBancoBenef;
    }
    
    public void setDdbuBancoBenef(String ddbuBancoBenef) {
        this.ddbuBancoBenef = ddbuBancoBenef;
    }
    public String getCdbuTipoDocBenef() {
        return this.cdbuTipoDocBenef;
    }
    
    public void setCdbuTipoDocBenef(String cdbuTipoDocBenef) {
        this.cdbuTipoDocBenef = cdbuTipoDocBenef;
    }
    public String getNdbuNumDocBenef() {
        return this.ndbuNumDocBenef;
    }
    
    public void setNdbuNumDocBenef(String ndbuNumDocBenef) {
        this.ndbuNumDocBenef = ndbuNumDocBenef;
    }
    public String getDdbuNombreBenef() {
        return this.ddbuNombreBenef;
    }
    
    public void setDdbuNombreBenef(String ddbuNombreBenef) {
        this.ddbuNombreBenef = ddbuNombreBenef;
    }
    public String getDdbuApepatBenef() {
        return this.ddbuApepatBenef;
    }
    
    public void setDdbuApepatBenef(String ddbuApepatBenef) {
        this.ddbuApepatBenef = ddbuApepatBenef;
    }
    public String getDdbuApeMatBenef() {
        return this.ddbuApeMatBenef;
    }
    
    public void setDdbuApeMatBenef(String ddbuApeMatBenef) {
        this.ddbuApeMatBenef = ddbuApeMatBenef;
    }
    public String getDdbuDireccionBenef() {
        return this.ddbuDireccionBenef;
    }
    
    public void setDdbuDireccionBenef(String ddbuDireccionBenef) {
        this.ddbuDireccionBenef = ddbuDireccionBenef;
    }
    public String getDdbuTlfBenef() {
        return this.ddbuTlfBenef;
    }
    
    public void setDdbuTlfBenef(String ddbuTlfBenef) {
        this.ddbuTlfBenef = ddbuTlfBenef;
    }
    public String getFdbuFechaVenc() {
        return this.fdbuFechaVenc;
    }
    
    public void setFdbuFechaVenc(String fdbuFechaVenc) {
        this.fdbuFechaVenc = fdbuFechaVenc;
    }
    public String getDdbuAdicional1() {
        return this.ddbuAdicional1;
    }
    
    public void setDdbuAdicional1(String ddbuAdicional1) {
        this.ddbuAdicional1 = ddbuAdicional1;
    }
    public String getDdbuAdicional2() {
        return this.ddbuAdicional2;
    }
    
    public void setDdbuAdicional2(String ddbuAdicional2) {
        this.ddbuAdicional2 = ddbuAdicional2;
    }
    public String getDdbuAdicional3() {
        return this.ddbuAdicional3;
    }
    
    public void setDdbuAdicional3(String ddbuAdicional3) {
        this.ddbuAdicional3 = ddbuAdicional3;
    }
    public String getDdbuAdicional4() {
        return this.ddbuAdicional4;
    }
    
    public void setDdbuAdicional4(String ddbuAdicional4) {
        this.ddbuAdicional4 = ddbuAdicional4;
    }
    public String getDdbuAdicional5() {
        return this.ddbuAdicional5;
    }
    
    public void setDdbuAdicional5(String ddbuAdicional5) {
        this.ddbuAdicional5 = ddbuAdicional5;
    }
    public String getDdbuAdicional6() {
        return this.ddbuAdicional6;
    }
    
    public void setDdbuAdicional6(String ddbuAdicional6) {
        this.ddbuAdicional6 = ddbuAdicional6;
    }
    public String getDdbuAdicional7() {
        return this.ddbuAdicional7;
    }
    
    public void setDdbuAdicional7(String ddbuAdicional7) {
        this.ddbuAdicional7 = ddbuAdicional7;
    }
    public String getDdbuAdicional8() {
        return this.ddbuAdicional8;
    }
    
    public void setDdbuAdicional8(String ddbuAdicional8) {
        this.ddbuAdicional8 = ddbuAdicional8;
    }
    public String getDdbuAdicional9() {
        return this.ddbuAdicional9;
    }
    
    public void setDdbuAdicional9(String ddbuAdicional9) {
        this.ddbuAdicional9 = ddbuAdicional9;
    }
    public String getDdbuAdicional10() {
        return this.ddbuAdicional10;
    }
    
    public void setDdbuAdicional10(String ddbuAdicional10) {
        this.ddbuAdicional10 = ddbuAdicional10;
    }
    public String getDdbuReferenciaAdicional() {
        return this.ddbuReferenciaAdicional;
    }
    
    public void setDdbuReferenciaAdicional(String ddbuReferenciaAdicional) {
        this.ddbuReferenciaAdicional = ddbuReferenciaAdicional;
    }




}



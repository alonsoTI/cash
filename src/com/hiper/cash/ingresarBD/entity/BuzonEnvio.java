/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD.entity;

/**
 *
 * @author jmoreno
 */
public class BuzonEnvio {
    private long IdEnvio;
    private String FechaCreacion;
    private String FechaInicioVig;
    private String FechaFinVig;
    private String HoraInicio;
    private String TipoCuenta;
    private String CuentaCargo;
    private String TipoIngreso;
    private String Referencia;
    private String Estado;
    private String Usuario;
    private long IdOrden;
    private long IdServEmp;
    
    public long getIdEnvio() {
        return IdEnvio;
    }

    public void setIdEnvio(long IdEnvio) {
        this.IdEnvio = IdEnvio;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public long getIdOrden() {
        return IdOrden;
    }

    public void setIdOrden(long IdOrden) {
        this.IdOrden = IdOrden;
    }

    public String getFechaInicioVig() {
        return FechaInicioVig;
    }

    public void setFechaInicioVig(String FechaInicioVig) {
        this.FechaInicioVig = FechaInicioVig;
    }

    public String getFechaFinVig() {
        return FechaFinVig;
    }

    public void setFechaFinVig(String FechaFinVig) {
        this.FechaFinVig = FechaFinVig;
    }

    public String getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraInicio(String HoraInicio) {
        this.HoraInicio = HoraInicio;
    }

    public String getTipoCuenta() {
        return TipoCuenta;
    }

    public void setTipoCuenta(String TipoCuenta) {
        this.TipoCuenta = TipoCuenta;
    }

    public String getCuentaCargo() {
        return CuentaCargo;
    }

    public void setCuentaCargo(String CuentaCargo) {
        this.CuentaCargo = CuentaCargo;
    }

    public String getTipoIngreso() {
        return TipoIngreso;
    }

    public void setTipoIngreso(String TipoIngreso) {
        this.TipoIngreso = TipoIngreso;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String Referencia) {
        this.Referencia = Referencia;
    }

    public long getIdServEmp() {
        return IdServEmp;
    }

    public void setIdServEmp(long IdServEmp) {
        this.IdServEmp = IdServEmp;
    }

   
}

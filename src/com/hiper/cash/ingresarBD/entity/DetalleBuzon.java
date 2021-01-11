/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD.entity;

/**
 *
 * @author jmoreno
 */
public class DetalleBuzon {
    private int IdDetalleBuzon;
    private int IdEnvio;
    private String Documento;
    private String Nombre;
    private String NumeroCuenta;
    private String TipoCuenta;
    private double Monto;//decimal
    private String Moneda;
    private String Telefono;
    private String Email;
    private String Descripcion;
    private String Estado;
    private String TipoPago;
    private String TipoDocumento;
    private String FechaProceso;
    private String HoraProceso;
    private String NumCuentaAbono;
    private String NumCuentaCargo;
    private String Referencia;
    private String NumCuentaAbonoCCI;
    private String BancoBenef;
    private String TipoDocBenef;
    private String NumDocBenef;
    private String NombreBenef;
    private String ApepatBenef;
    private String ApeMatBenef;
    private String DireccionBenef;
    private String TlfBenef;
    private String FechaVenc;
    private String Adicional1;
    private String Adicional2;
    private String Adicional3;
    private String Adicional4;
    private String Adicional5;

    public int getIdDetalleBuzon() {
        return IdDetalleBuzon;
    }

    public void setIdDetalleBuzon(int IdDetalleBuzon) {
        this.IdDetalleBuzon = IdDetalleBuzon;
    }

    public int getIdEnvio() {
        return IdEnvio;
    }

    public void setIdEnvio(int IdEnvio) {
        this.IdEnvio = IdEnvio;
    }

    public String getDocumento() {
        return Documento;
    }

    public void setDocumento(String Documento) {
        this.Documento = Documento;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getNumeroCuenta() {
        return NumeroCuenta;
    }

    public void setNumeroCuenta(String NumeroCuenta) {
        this.NumeroCuenta = NumeroCuenta;
    }

    public String getTipoCuenta() {
        return TipoCuenta;
    }

    public void setTipoCuenta(String TipoCuenta) {
        this.TipoCuenta = TipoCuenta;
    }

    public double getMonto() {
        return Monto;
    }

    public void setMonto(double Monto) {
        this.Monto = Monto;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String Moneda) {
        this.Moneda = Moneda;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getTipoPago() {
        return TipoPago;
    }

    public void setTipoPago(String TipoPago) {
        this.TipoPago = TipoPago;
    }

    public String getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(String TipoDocumento) {
        this.TipoDocumento = TipoDocumento;
    }

    public String getFechaProceso() {
        return FechaProceso;
    }

    public void setFechaProceso(String FechaProceso) {
        this.FechaProceso = FechaProceso;
    }

    public String getHoraProceso() {
        return HoraProceso;
    }

    public void setHoraProceso(String HoraProceso) {
        this.HoraProceso = HoraProceso;
    }

    public String getNumCuentaAbono() {
        return NumCuentaAbono;
    }

    public void setNumCuentaAbono(String NumCuentaAbono) {
        this.NumCuentaAbono = NumCuentaAbono;
    }

    public String getNumCuentaCargo() {
        return NumCuentaCargo;
    }

    public void setNumCuentaCargo(String NumCuentaCargo) {
        this.NumCuentaCargo = NumCuentaCargo;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String Referencia) {
        this.Referencia = Referencia;
    }

    public String getNumCuentaAbonoCCI() {
        return NumCuentaAbonoCCI;
    }

    public void setNumCuentaAbonoCCI(String NumCuentaAbonoCCI) {
        this.NumCuentaAbonoCCI = NumCuentaAbonoCCI;
    }

    public String getBancoBenef() {
        return BancoBenef;
    }

    public void setBancoBenef(String BancoBenef) {
        this.BancoBenef = BancoBenef;
    }

    public String getTipoDocBenef() {
        return TipoDocBenef;
    }

    public void setTipoDocBenef(String TipoDocBenef) {
        this.TipoDocBenef = TipoDocBenef;
    }

    public String getNumDocBenef() {
        return NumDocBenef;
    }

    public void setNumDocBenef(String NumDocBenef) {
        this.NumDocBenef = NumDocBenef;
    }

    public String getNombreBenef() {
        return NombreBenef;
    }

    public void setNombreBenef(String NombreBenef) {
        this.NombreBenef = NombreBenef;
    }

    public String getApepatBenef() {
        return ApepatBenef;
    }

    public void setApepatBenef(String ApepatBenef) {
        this.ApepatBenef = ApepatBenef;
    }

    public String getApeMatBenef() {
        return ApeMatBenef;
    }

    public void setApeMatBenef(String ApeMatBenef) {
        this.ApeMatBenef = ApeMatBenef;
    }

    public String getDireccionBenef() {
        return DireccionBenef;
    }

    public void setDireccionBenef(String DireccionBenef) {
        this.DireccionBenef = DireccionBenef;
    }

    public String getTlfBenef() {
        return TlfBenef;
    }

    public void setTlfBenef(String TlfBenef) {
        this.TlfBenef = TlfBenef;
    }

    public String getFechaVenc() {
        return FechaVenc;
    }

    public void setFechaVenc(String FechaVenc) {
        this.FechaVenc = FechaVenc;
    }

    public String getAdicional1() {
        return Adicional1;
    }

    public void setAdicional1(String Adicional1) {
        this.Adicional1 = Adicional1;
    }

    public String getAdicional2() {
        return Adicional2;
    }

    public void setAdicional2(String Adicional2) {
        this.Adicional2 = Adicional2;
    }

    public String getAdicional3() {
        return Adicional3;
    }

    public void setAdicional3(String Adicional3) {
        this.Adicional3 = Adicional3;
    }

    public String getAdicional4() {
        return Adicional4;
    }

    public void setAdicional4(String Adicional4) {
        this.Adicional4 = Adicional4;
    }

    public String getAdicional5() {
        return Adicional5;
    }

    public void setAdicional5(String Adicional5) {
        this.Adicional5 = Adicional5;
    }
}

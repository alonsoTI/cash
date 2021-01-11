package com.financiero.cash.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hiper.cash.util.Util.strFecha;

public class PrestamoAdapter {

    private String nroCuenta;
    private Integer nroCuotas;
    private Double tasa;
    private Date fechaApertura;
    private Date fechaVencimiento;
    private Double saldo;
    private Double interes;
    private Double mora;
    private Double ICV;
    private Double comision;
    private Double IGV;
    private Double seguro;
    private Double total;
    private List<CuotaAdapter> cuotas;
    private String tipo;

    public PrestamoAdapter(String nro) {
        this.nroCuenta = nro;       
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb = sb.append("\n- Nro Cuotas: ").append(nroCuotas.toString());
        sb = sb.append("\n- Tasa : ").append(tasa.toString());
        sb = sb.append("\n- Fecha Apertura: ").append(fechaApertura.toString());
        sb = sb.append("\n- Fecha Vencimiento: ").append(fechaVencimiento.toString());
        sb = sb.append("\n- Saldo: ").append(saldo.toString());
        sb = sb.append("\n- Interes: ").append(interes.toString());
        sb = sb.append("\n- Mora:  ").append(mora.toString());
        sb = sb.append("\n- ICV: ").append(ICV.toString());
        sb = sb.append("\n- IGV: ").append(IGV.toString());
        sb = sb.append("\n- Comision: ").append(comision.toString());
        sb = sb.append("\n- Seguro: ").append(seguro.toString());
        sb = sb.append("\n- Total: ").append(total.toString());
        sb = sb.append("\n- Coutas: \n").append(cuotas);
        return sb.toString();

    }

    public Integer getNroCuotas() {
        return nroCuotas;
    }

    public void setNroCuotas(Integer nroCuotas) {
        this.nroCuotas = nroCuotas;
    }

    public Double getTasa() {
        return tasa;
    }

    public void setTasa(Double tasa) {
        this.tasa = tasa;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Double getInteres() {
        return interes;
    }

    public void setInteres(Double interes) {
        this.interes = interes;
    }

    public Double getMora() {
        return mora;
    }

    public void setMora(Double mora) {
        this.mora = mora;
    }

    public Double getComision() {
        return comision;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }

    public Double getSeguro() {
        return seguro;
    }

    public void setSeguro(Double seguro) {
        this.seguro = seguro;
    }

    public void setCuotas(List<CuotaAdapter> cuotas) {
        this.cuotas = cuotas;
    }

    public List<CuotaAdapter> getCuotas() {
        return cuotas;
    }

    public String getStrNroCuotas() {
        return this.nroCuotas.toString();
    }

    public void setICV(Double ICV) {
        this.ICV = ICV;
    }

    public Double getICV() {
        return ICV;
    }

    public void setIGV(Double IGV) {
        this.IGV = IGV;
    }

    public Double getIGV() {
        return IGV;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Double getSaldo() {
        return saldo;
    }

    public String getStrFechaApertura() {
        return strFecha(fechaApertura);
    }

    public String getStrFechaVencimiento() {
        return strFecha(fechaVencimiento);
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setNroCuenta(String nroCuenta) {
        this.nroCuenta = nroCuenta;
    }

    public String getNroCuenta() {
        return nroCuenta;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getTotal() {
        return total;
    }

    
    public void agregarCuotas(List<CuotaAdapter> cuotas){
    	this.cuotas.addAll(cuotas);
    }
    
}

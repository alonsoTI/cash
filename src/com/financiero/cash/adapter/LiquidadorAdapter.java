package com.financiero.cash.adapter;

import java.util.Date;

import static com.hiper.cash.util.Util.formatDouble2;
import static com.hiper.cash.util.Util.strFecha;

public class LiquidadorAdapter {
    private String nroCuenta;
    private Integer nroCuotas;    
    private Date fechaLiquidacion;
    private String cliente;
    private String moneda;
    private Date fechaVencimiento;
    private Date fechaVencimientoProximaCuota;
    private Double pagoPrincipal = 0.0;
    private Double pagoInteres = 0.0;
    private Double ICV = 0.0;
    private Double IGV = 0.0;
    private Double interesMoratorio = 0.0;
    private Double comisionCuotaVencida = 0.0;
    private Double seguro = 0.0;
    private Double seguroTodoRiesgo = 0.0;
    private Double total = 0.0;
    private Double ITF = 0.0;
    private Double totalITF = 0.0;
    private Double portes = 0.0;

    public LiquidadorAdapter(String cuenta) {
        this.nroCuenta = cuenta;
        this.fechaLiquidacion = new Date();
    }

     @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb = sb.append("\n-Fecha Liquidacion: ").append(strFecha(getFechaLiquidacion()));
        sb = sb.append("\n-Fecha Cliente: ").append(getCliente());
        sb = sb.append("\n-Moneda: ").append(getMoneda());
        sb = sb.append("\n-Fecha Vencimiento: ").append(strFecha(getFechaVencimiento()));
        sb = sb.append("\n-Pago Princial: ").append(getPagoPrincipal());
        sb = sb.append("\n-Pago Intereses: ").append(getPagoInteres());
        sb = sb.append("\n-ICV: ").append(getICV());
        sb = sb.append("\n-IGV: ").append(IGV);
        sb = sb.append("\n-Interes Moratorio: ").append(getInteresMoratorio());
        sb = sb.append("\n-Couta Vencida: ").append(comisionCuotaVencida);
        sb = sb.append("\n-Seguro: ").append(seguro);
        sb = sb.append("\n-Seguro Todo Riesgo: ").append(seguroTodoRiesgo);
        sb = sb.append("\n-Total: ").append(total);
        sb = sb.append("\n-ITF: ").append(ITF);
        sb = sb.append("\n-totalITF: ").append(totalITF);
         sb = sb.append("\n-POrtes: ").append(this.portes);
        
        return sb.toString();
    }


    public void setFechaLiquidacion(Date fechaProceso) {
        this.fechaLiquidacion = fechaProceso;
    }

    public Date getFechaLiquidacion() {
        return fechaLiquidacion;
    }

    /**
     * @return the cliente
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the moneda
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * @return the fechaVencimiento
     */
    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    /**
     * @param fechaVencimiento the fechaVencimiento to set
     */
    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /**
     * @return the pagoPrincipal
     */
    
    
    public String getPagoPrincipal(){
    	return formatDouble2(this.pagoPrincipal);
    }
    /*
    public Double getPagoPrincipal() {
        return pagoPrincipal;
    }*/

    /**
     * @param pagoPrincipal the pagoPrincipal to set
     */
    public void setPagoPrincipal(Double pagoPrincipal) {
        this.pagoPrincipal = pagoPrincipal;
    }
    
    
    

    /**
     * @return the pagoInteres
     */
    
    public String getPagoInteres() {
        return formatDouble2(this.pagoInteres);
    }
    /*
    public Double getPagoInteres() {
        return pagoInteres;
    }*/

    /**
     * @param pagoInteres the pagoInteres to set
     */
    public void setPagoInteres(Double pagoInteres) {
        this.pagoInteres = pagoInteres;
    }

    /**
     * @return the ICV
     */
    
    public String getICV() {
    	 return formatDouble2(this.ICV);
        //return ICV;
    }
    
   /* public Double getICV() {
        return ICV;
    }*/

    /**
     * @param ICV the ICV to set
     */   
    
    public void setICV(Double ICV) {
        this.ICV = ICV;
    }

    /**
     * @return the interesMoratorio
     */
    
    
    public String getInteresMoratorio() {
        return formatDouble2(this.interesMoratorio);
    }
    /*
    public Double getInteresMoratorio() {
        return interesMoratorio;
    }*/

    /**
     * @param interesMoratorio the interesMoratorio to set
     */
    public void setInteresMoratorio(Double interesMoratorio) {
        this.interesMoratorio = interesMoratorio;
    }

    /**
     * @return the comisionCuotaVencida
     */
    
    public String getComisionCuotaVencida() {
        return formatDouble2(this.comisionCuotaVencida);
    }
/*
    public Double getComisionCuotaVencida() {
        return comisionCuotaVencida;
    }*/

    /**
     * @param comisionCuotaVencida the comisionCuotaVencida to set
     */
    public void setComisionCuotaVencida(Double comisionCuotaVencida) {
        this.comisionCuotaVencida = comisionCuotaVencida;
    }

    /**
     * @return the seguro
     */
    
    public String getSeguro() {
        return formatDouble2(this.seguro);
    }
/*
    public Double getSeguro() {
        return seguro;
    }*/

    /**
     * @param seguro the seguro to set
     */
    public void setSeguro(Double seguro) {
        this.seguro = seguro;
    }

    /**
     * @return the seguroTodoRiesgo
     */
    
    public String getSeguroTodoRiesgo() {
        return formatDouble2(this.seguroTodoRiesgo);
    }
    /*
    public Double getSeguroTodoRiesgo() {
        return seguroTodoRiesgo;
    }*/

    /**
     * @param seguroTodoRiesgo the seguroTodoRiesgo to set
     */
    public void setSeguroTodoRiesgo(Double seguroTodoRiesgo) {
        this.seguroTodoRiesgo = seguroTodoRiesgo;
    }

    /**
     * @return the total
     */
    
    
    public String getTotal() {
        return formatDouble2(this.total);
    }
    /*
    public Double getTotal() {
        return total;
    }*/

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * @return the ITF
     */
    
    public String getITF() {
        return formatDouble2(this.ITF);
    }
    /*
    public Double getITF() {
        return ITF;
    }*/

    /**
     * @param ITF the ITF to set
     */
    public void setITF(Double ITF) {
        this.ITF = ITF;
    }

    /**
     * @return the totalITF
     */
    
    public String getTotalITF() {
        return formatDouble2(this.totalITF);
    }
    /*
    public double getTotalITF() {
        return totalITF;
    }*/

    /**
     * @param totalITF the totalITF to set
     */
    public void setTotalITF(double totalITF) {
        this.setTotalITF((Double) totalITF);
    }

    public void setIGV(Double IGV) {
        this.IGV = IGV;
    }


    public String getIGV() {
        return formatDouble2(this.IGV);
    }   
    /*
    public Double getIGV() {
        return IGV;
    } */  

    public String getMonedaContrato(){
        return new StringBuilder(getNroCuenta()).append(" - ").append(moneda).toString();
    }

    public void setFechaVencimientoProximaCuota(Date fechaVencimientoProximaCouta) {
        this.fechaVencimientoProximaCuota = fechaVencimientoProximaCouta;
    }

    public Date getFechaVencimientoProximaCuota() {
        return fechaVencimientoProximaCuota;
    }

    public String getStrFechaVencimientoProximaCuota(){
         return strFecha(this.fechaVencimientoProximaCuota);
    }

     public String getStrFechaLiquidacion(){
        return strFecha(this.fechaLiquidacion);
    }

    public String getStrFechaVencimiento(){
        return strFecha(this.fechaVencimiento);
    }

    /**
     * @return the nroCuenta
     */
    public String getNroCuenta() {
        return nroCuenta;
    }

    /**
     * @param nroCuenta the nroCuenta to set
     */
    public void setNroCuenta(String nroCuenta) {
        this.nroCuenta = nroCuenta;
    }

    /**
     * @return the nroCuotas
     */
    public Integer getNroCuotas() {
        return nroCuotas;
    }

    /**
     * @param nroCuotas the nroCuotas to set
     */
    public void setNroCuotas(Integer nroCuotas) {
        this.nroCuotas = nroCuotas;
    }

    /**
     * @param totalITF the totalITF to set
     */
    public void setTotalITF(Double totalITF) {
        this.totalITF = totalITF;
    }

    public void setPortes(Double portes) {
        this.portes = portes;
    }

    public Double getPortes() {
        return portes;
    }
    
    public void calcularTotal(){
    	this.totalITF =  this.total + this.ITF;
    }
    
}

package com.financiero.cash.adapter;

import java.util.Date;

import static com.hiper.cash.util.Util.strFecha;
import static com.hiper.cash.util.Util.formatDouble2;

public class CuotaAdapter {

	private Integer nro;
    private Date fechaVencimiento;
    private Double principal;
    private Double interes;
    private Double comision;
    private Double mora;
    private Double interesCompensatorio;
    private Double interesMoratorio;
    private Double totalCuota;
    private Double seguro;
    private Double IGV;

    public CuotaAdapter() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb = sb.append("\n       -Nro: ").append(nro.toString());
        sb = sb.append("       -Fecha Vencimiento: ").append(strFecha(this.fechaVencimiento));
        sb = sb.append("       -Principal: ").append(this.getPrincipal());
        sb = sb.append("       -Interes: ").append(this.getInteres());
        sb = sb.append("       -Comision: ").append(this.getComision());
        sb = sb.append("\n       -Mora: ").append(this.getMora());       
        //sb = sb.append("       -IM: ").append(this.getInteresMoratorio());
        sb = sb.append("       -IGV: ").append(this.getIGV());
        sb = sb.append("       -ICV: ").append(this.getInteresCompensatorio());
        sb = sb.append("       -Total Cuota: ").append(this.getTotalCuota());
        sb = sb.append("       -Seguro: ").append(this.getSeguro());
         sb = sb.append("\n");
        return  sb.toString();
    }

    public Integer getNro() {
        return nro;
    }

    public void setNro(Integer nro) {
        this.nro = nro;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    /*public Double getPrincipal() {
        return principal;
    }*/

    public String getPrincipal(){
        return formatDouble2(this.principal);
    }

    public void setPrincipal(Double monto) {
        this.principal = monto;
    }

    /*public Double getInteres() {
        return interes;
    }*/

    public String getInteres(){
       return formatDouble2(this.interes);
    }

    public void setInteres(Double interes) {
        this.interes = interes;
    }

    public String getMora(){
        return formatDouble2(this.mora);
    }
    /*public Double getMora() {
        return mora;
    }*/

    public void setMora(Double mora) {
        this.mora = mora;
    }

    public void setComision(Double comision) {
        this.comision = comision;
    }

    /*public Double getComision() {
        return comision;
    }*/

    public String getComision() {
       return formatDouble2(this.comision);
    }

   /* public Double getInteresCompensatorio() {
        return interesCompensatorio;
    }*/

     public String getInteresCompensatorio() {
        return formatDouble2(this.interesCompensatorio);
    }

    public void setInteresCompensatorio(Double interesCompensatorio) {
        this.interesCompensatorio = interesCompensatorio;
    }

    /*public Double getInteresMoratorio() {
        return interesMoratorio;
    }*/

     public String  getInteresMoratorio() {
       return formatDouble2(this.interesMoratorio);
    }

    public void setInteresMoratorio(Double interesMoratorio) {
        this.interesMoratorio = interesMoratorio;
    }
 
   /* public Double getSeguro() {
        return seguro;
    }*/

     public String getSeguro() {
        return formatDouble2(this.seguro);
    }

    public void setSeguro(Double seguro) {
        this.seguro = seguro;
    }

    /*public Double getTotalCuota() {
        return totalCuota;
    }*/

     public String getTotalCuota() {
         return formatDouble2(this.totalCuota);
    }

    public void setTotalCuota(Double totalCuota) {
        this.totalCuota = totalCuota;
    }    

    public String getStrNro() {
        return this.nro.toString();
    }

    public String getStrFechaVencimiento() {
        
        return strFecha(fechaVencimiento);
    }

    public void setIGV(Double IGV) {
        this.IGV = IGV;
    }


    public String getIGV(){
        return formatDouble2(this.IGV);
    }
}

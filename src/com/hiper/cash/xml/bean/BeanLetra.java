/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

/**
 *
 * @author jmoreno
 */
public class BeanLetra {//BeanPreLiqLetra
     private String m_NumPlanilla;
     private String m_Principal;
     private String m_ndec_prin;
     private String m_SignPrincipal;
     private String m_Interes;
     private String m_ndec_int;
     private String m_PrincipalPend;
     private String m_ndec_prinPend;
     private String m_InteresPend;
     private String m_ndec_int_pend;
     private String m_PagoInteres;
     private String m_ndec_pago_int;
     private String m_SignoPagInt;
     private String m_PagoPrincipal;
     private String m_ndec_pago_princ;
     private String m_Portes;
     private String m_ndec_portes;
     private String m_Mora;
     private String m_ndec_mora;
     private String m_Protesto;
     private String m_ndec_protesto;
     private String m_InteresRef;

     private String m_FechaVenc;
     private String m_Cliente;
     private String m_Oficina;
     private String m_Provisionar;
     private String m_Moneda;
     private String m_Descripcion;
     private String m_Aceptante;
     private String m_CodAceptante;
     private String m_ImporteAnt;
     
     //para mostrar en la pantalla
     private String m_Estado;
     private String m_NumLetra;
     private String m_Itf;
     private String m_ndec_itf;
     private String m_ImporteFinal;

     //jmoreno 27-08-09
     private String m_amortizacion;
     //para saber si el ibs devolvio respuesta
     private String m_respuesta;
     private String m_mensaje;
    public String getM_NumPlanilla() {
        return m_NumPlanilla;
    }

    public void setM_NumPlanilla(String m_NumPlanilla) {
        this.m_NumPlanilla = m_NumPlanilla;
    }

    public String getM_Principal() {
        return m_Principal;
    }

    public void setM_Principal(String m_Principal) {
        this.m_Principal = m_Principal;
    }

    public String getM_PrincipalPend() {
        return m_PrincipalPend;
    }

    public void setM_PrincipalPend(String m_PrincipalPend) {
        this.m_PrincipalPend = m_PrincipalPend;
    }

    public String getM_InteresPend() {
        return m_InteresPend;
    }

    public void setM_InteresPend(String m_InteresPend) {
        this.m_InteresPend = m_InteresPend;
    }

    public String getM_Mora() {
        return m_Mora;
    }

    public void setM_Mora(String m_Mora) {
        this.m_Mora = m_Mora;
    }

    public String getM_Portes() {
    
        return m_Portes;
    }

    public void setM_Portes(String m_Portes) {
        this.m_Portes = m_Portes;
    }

    public String getM_Protesto() {
        return m_Protesto;
    }

    public void setM_Protesto(String m_Protesto) {
        this.m_Protesto = m_Protesto;
    }

    public String getM_InteresRef() {
        return m_InteresRef;
    }

    public void setM_InteresRef(String m_InteresRef) {
        this.m_InteresRef = m_InteresRef;
    }

    public String getM_FechaVenc() {
        return m_FechaVenc;
    }

    public void setM_FechaVenc(String m_FechaVenc) {
        this.m_FechaVenc = m_FechaVenc;
    }

    public String getM_Cliente() {
        return m_Cliente;
    }

    public void setM_Cliente(String m_Cliente) {
        this.m_Cliente = m_Cliente;
    }

    public String getM_Oficina() {
        return m_Oficina;
    }

    public void setM_Oficina(String m_Oficina) {
        this.m_Oficina = m_Oficina;
    }

    public String getM_Provisionar() {
        return m_Provisionar;
    }

    public void setM_Provisionar(String m_Provisionar) {
        this.m_Provisionar = m_Provisionar;
    }

    public String getM_PagoPrincipal() {
     
        return m_PagoPrincipal;
    }

    public void setM_PagoPrincipal(String m_PagoPrincipal) {
        this.m_PagoPrincipal = m_PagoPrincipal;
    }

    public String getM_PagoInteres() {
        return m_PagoInteres;
    }

    public void setM_PagoInteres(String m_PagoInteres) {
        this.m_PagoInteres = m_PagoInteres;
    }

    public String getM_SignoPagInt() {
        return m_SignoPagInt;
    }

    public void setM_SignoPagInt(String m_SignoPagInt) {
        this.m_SignoPagInt = m_SignoPagInt;
    }

    public String getM_Moneda() {
        return m_Moneda;
    }

    public void setM_Moneda(String m_Moneda) {
        this.m_Moneda = m_Moneda;
    }

    public String getM_Descripcion() {
        return m_Descripcion;
    }

    public void setM_Descripcion(String m_Descripcion) {
        this.m_Descripcion = m_Descripcion;
    }

    public String getM_Aceptante() {
        return m_Aceptante;
    }

    public void setM_Aceptante(String m_Aceptante) {
        this.m_Aceptante = m_Aceptante;
    }

    public String getM_ImporteAnt() {
        return m_ImporteAnt;
    }

    public void setM_ImporteAnt(String m_ImporteAnt) {
        this.m_ImporteAnt = m_ImporteAnt;
    }

    public String getM_Estado() {
        return m_Estado;
    }

    public void setM_Estado(String m_Estado) {
        this.m_Estado = m_Estado;
    }

    public String getM_NumLetra() {
        return m_NumLetra;
    }

    public void setM_NumLetra(String m_NumLetra) {
        this.m_NumLetra = m_NumLetra;
    }

    public String getM_SignPrincipal() {
        return m_SignPrincipal;
    }

    public void setM_SignPrincipal(String m_SignPrincipal) {
        this.m_SignPrincipal = m_SignPrincipal;
    }

    public String getM_Interes() {
        return m_Interes;
    }

    public void setM_Interes(String m_Interes) {
        this.m_Interes = m_Interes;
    }

    public String getM_Itf() {
        return m_Itf;
    }

    public void setM_Itf(String m_Itf) {
        this.m_Itf = m_Itf;
    }

    public String getM_ImporteFinal() {
        return m_ImporteFinal;
    }

    public void setM_ImporteFinal(String m_ImporteFinal) {
        this.m_ImporteFinal = m_ImporteFinal;
    }

    public String getM_respuesta() {
        return m_respuesta;
    }

    public void setM_respuesta(String m_respuesta) {
        this.m_respuesta = m_respuesta;
    }

    public String getM_mensaje() {
        return m_mensaje;
    }

    public void setM_mensaje(String m_mensaje) {
        this.m_mensaje = m_mensaje;
    }

    public String getM_ndec_prin() {
        return m_ndec_prin;
    }

    public void setM_ndec_prin(String m_ndec_prin) {
        this.m_ndec_prin = m_ndec_prin;
    }

    public String getM_ndec_int() {
        return m_ndec_int;
    }

    public void setM_ndec_int(String m_ndec_int) {
        this.m_ndec_int = m_ndec_int;
    }

    public String getM_ndec_prinPend() {
        return m_ndec_prinPend;
    }

    public void setM_ndec_prinPend(String m_ndec_prinPend) {
        this.m_ndec_prinPend = m_ndec_prinPend;
    }

    public String getM_ndec_int_pend() {
        return m_ndec_int_pend;
    }

    public void setM_ndec_int_pend(String m_ndec_int_pend) {
        this.m_ndec_int_pend = m_ndec_int_pend;
    }

    public String getM_ndec_pago_int() {
        return m_ndec_pago_int;
    }

    public void setM_ndec_pago_int(String m_ndec_pago_int) {
        this.m_ndec_pago_int = m_ndec_pago_int;
    }

    public String getM_ndec_pago_princ() {
        return m_ndec_pago_princ;
    }

    public void setM_ndec_pago_princ(String m_ndec_pago_princ) {
        this.m_ndec_pago_princ = m_ndec_pago_princ;
    }

    public String getM_ndec_portes() {
        return m_ndec_portes;
    }

    public void setM_ndec_portes(String m_ndec_portes) {
        this.m_ndec_portes = m_ndec_portes;
    }

    public String getM_ndec_mora() {
        return m_ndec_mora;
    }

    public void setM_ndec_mora(String m_ndec_mora) {
        this.m_ndec_mora = m_ndec_mora;
    }

    public String getM_ndec_protesto() {
        return m_ndec_protesto;
    }

    public void setM_ndec_protesto(String m_ndec_protesto) {
        this.m_ndec_protesto = m_ndec_protesto;
    }

    public String getM_ndec_itf() {
        return m_ndec_itf;
    }

    public void setM_ndec_itf(String m_ndec_itf) {
        this.m_ndec_itf = m_ndec_itf;
    }

    public String getM_CodAceptante() {
        return m_CodAceptante;
    }

    public void setM_CodAceptante(String m_CodAceptante) {
        this.m_CodAceptante = m_CodAceptante;
    }

    public String getM_amortizacion() {
        return m_amortizacion;
    }

    public void setM_amortizacion(String m_amortizacion) {
        this.m_amortizacion = m_amortizacion;
    }

}

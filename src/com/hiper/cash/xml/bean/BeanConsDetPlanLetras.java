/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

import java.util.List;

/**
 *
 * @author jmoreno
 */
public class BeanConsDetPlanLetras {
    private String m_Prestamo;
    private String m_Saldo;
    private String m_FechaVenc;

    private List m_DetalleLetras;
    private String m_Cuenta; //para la consulta de Calcular letra
    private String m_CodigoCliente;// para la consulta de Ctas
    private String m_RucEmpresa;// para los reportes
    public String getM_Prestamo() {
        return m_Prestamo;
    }

    public void setM_Prestamo(String m_Prestamo) {
        this.m_Prestamo = m_Prestamo;
    }

    public String getM_Saldo() {
        return m_Saldo;
    }

    public void setM_Saldo(String m_Saldo) {
        this.m_Saldo = m_Saldo;
    }

    public String getM_FechaVenc() {
        return m_FechaVenc;
    }

    public void setM_FechaVenc(String m_FechaVenc) {
        this.m_FechaVenc = m_FechaVenc;
    }

    public List getM_DetalleLetras() {
        return m_DetalleLetras;
    }

    public void setM_DetalleLetras(List m_DetalleLetras) {
        this.m_DetalleLetras = m_DetalleLetras;
    }

    public String getM_Cuenta() {
        return m_Cuenta;
    }

    public void setM_Cuenta(String m_Cuenta) {
        this.m_Cuenta = m_Cuenta;
    }

    public String getM_CodigoCliente() {
        return m_CodigoCliente;
    }

    public void setM_CodigoCliente(String m_CodigoCliente) {
        this.m_CodigoCliente = m_CodigoCliente;
    }

    public String getM_RucEmpresa() {
        return m_RucEmpresa;
    }

    public void setM_RucEmpresa(String m_RucEmpresa) {
        this.m_RucEmpresa = m_RucEmpresa;
    }
}

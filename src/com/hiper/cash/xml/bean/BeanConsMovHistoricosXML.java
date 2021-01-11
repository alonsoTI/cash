/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

import java.util.List;

/**
 *
 * @author jwong
 */
public class BeanConsMovHistoricosXML {
    private String m_Titular;
    private String m_Moneda;
    private String m_Cuenta;
    private String m_Fecha;
    private String m_Hora;

    private String m_SignoDisponible;
    private String m_SaldoDisponible;

    private String m_SignoRetenido;
    private String m_SaldoRetenido;

    private String m_SignoContable;
    private String m_SaldoContable;

    //jwong 19/01/2009 para el saldo anterior en detalle de ultimos movimientos en consulta de saldos
    private String m_SignoAnterior;
    private String m_SaldoAnterior;
    
    private List m_Movimientos;

    //jwong 29/02/2009 para data adicional en saldos promedios
    private String m_Estado;
    private String m_SaldoConsulta;
    private String m_Interes;
    private String m_Saldo;
    private String m_LineaAdministrada;
    private String m_FechaSaldoIni;
    private String m_FechaSaldoFin;
    private String m_SaldoInicial;
    private String m_SaldoFinal;
    
    /**
     * @return the m_Titular
     */
    public String getM_Titular() {
        return m_Titular;
    }

    /**
     * @param m_Titular the m_Titular to set
     */
    public void setM_Titular(String m_Titular) {
        this.m_Titular = m_Titular;
    }

    /**
     * @return the m_Moneda
     */
    public String getM_Moneda() {
        return m_Moneda;
    }

    /**
     * @param m_Moneda the m_Moneda to set
     */
    public void setM_Moneda(String m_Moneda) {
        this.m_Moneda = m_Moneda;
    }

    /**
     * @return the m_Cuenta
     */
    public String getM_Cuenta() {
        return m_Cuenta;
    }

    /**
     * @param m_Cuenta the m_Cuenta to set
     */
    public void setM_Cuenta(String m_Cuenta) {
        this.m_Cuenta = m_Cuenta;
    }

    /**
     * @return the m_Fecha
     */
    public String getM_Fecha() {
        return m_Fecha;
    }

    /**
     * @param m_Fecha the m_Fecha to set
     */
    public void setM_Fecha(String m_Fecha) {
        this.m_Fecha = m_Fecha;
    }

    /**
     * @return the m_SaldoDisponible
     */
    public String getM_SaldoDisponible() {
        return m_SaldoDisponible;
    }

    /**
     * @param m_SaldoDisponible the m_SaldoDisponible to set
     */
    public void setM_SaldoDisponible(String m_SaldoDisponible) {
        this.m_SaldoDisponible = m_SaldoDisponible;
    }

    /**
     * @return the m_SaldoRetenido
     */
    public String getM_SaldoRetenido() {
        return m_SaldoRetenido;
    }

    /**
     * @param m_SaldoRetenido the m_SaldoRetenido to set
     */
    public void setM_SaldoRetenido(String m_SaldoRetenido) {
        this.m_SaldoRetenido = m_SaldoRetenido;
    }

    /**
     * @return the m_SaldoContable
     */
    public String getM_SaldoContable() {
        return m_SaldoContable;
    }

    /**
     * @param m_SaldoContable the m_SaldoContable to set
     */
    public void setM_SaldoContable(String m_SaldoContable) {
        this.m_SaldoContable = m_SaldoContable;
    }

    /**
     * @return the m_Movimientos
     */
    public List getM_Movimientos() {
        return m_Movimientos;
    }

    /**
     * @param m_Movimientos the m_Movimientos to set
     */
    public void setM_Movimientos(List m_Movimientos) {
        this.m_Movimientos = m_Movimientos;
    }

    /**
     * @return the m_Hora
     */
    public String getM_Hora() {
        return m_Hora;
    }

    /**
     * @param m_Hora the m_Hora to set
     */
    public void setM_Hora(String m_Hora) {
        this.m_Hora = m_Hora;
    }

    /**
     * @return the m_SaldoAnterior
     */
    public String getM_SaldoAnterior() {
        return m_SaldoAnterior;
    }

    /**
     * @param m_SaldoAnterior the m_SaldoAnterior to set
     */
    public void setM_SaldoAnterior(String m_SaldoAnterior) {
        this.m_SaldoAnterior = m_SaldoAnterior;
    }

    /**
     * @return the m_SignoDisponible
     */
    public String getM_SignoDisponible() {
        return m_SignoDisponible;
    }

    /**
     * @param m_SignoDisponible the m_SignoDisponible to set
     */
    public void setM_SignoDisponible(String m_SignoDisponible) {
        this.m_SignoDisponible = m_SignoDisponible;
    }

    /**
     * @return the m_SignoRetenido
     */
    public String getM_SignoRetenido() {
        return m_SignoRetenido;
    }

    /**
     * @param m_SignoRetenido the m_SignoRetenido to set
     */
    public void setM_SignoRetenido(String m_SignoRetenido) {
        this.m_SignoRetenido = m_SignoRetenido;
    }

    /**
     * @return the m_SignoContable
     */
    public String getM_SignoContable() {
        return m_SignoContable;
    }

    /**
     * @param m_SignoContable the m_SignoContable to set
     */
    public void setM_SignoContable(String m_SignoContable) {
        this.m_SignoContable = m_SignoContable;
    }

    /**
     * @return the m_SignoAnterior
     */
    public String getM_SignoAnterior() {
        return m_SignoAnterior;
    }

    /**
     * @param m_SignoAnterior the m_SignoAnterior to set
     */
    public void setM_SignoAnterior(String m_SignoAnterior) {
        this.m_SignoAnterior = m_SignoAnterior;
    }

    /**
     * @return the m_Estado
     */
    public String getM_Estado() {
        return m_Estado;
    }

    /**
     * @param m_Estado the m_Estado to set
     */
    public void setM_Estado(String m_Estado) {
        this.m_Estado = m_Estado;
    }

    /**
     * @return the m_SaldoConsulta
     */
    public String getM_SaldoConsulta() {
        return m_SaldoConsulta;
    }

    /**
     * @param m_SaldoConsulta the m_SaldoConsulta to set
     */
    public void setM_SaldoConsulta(String m_SaldoConsulta) {
        this.m_SaldoConsulta = m_SaldoConsulta;
    }

    /**
     * @return the m_Interes
     */
    public String getM_Interes() {
        return m_Interes;
    }

    /**
     * @param m_Interes the m_Interes to set
     */
    public void setM_Interes(String m_Interes) {
        this.m_Interes = m_Interes;
    }

    /**
     * @return the m_Saldo
     */
    public String getM_Saldo() {
        return m_Saldo;
    }

    /**
     * @param m_Saldo the m_Saldo to set
     */
    public void setM_Saldo(String m_Saldo) {
        this.m_Saldo = m_Saldo;
    }

    /**
     * @return the m_LineaAdministrada
     */
    public String getM_LineaAdministrada() {
        return m_LineaAdministrada;
    }

    /**
     * @param m_LineaAdministrada the m_LineaAdministrada to set
     */
    public void setM_LineaAdministrada(String m_LineaAdministrada) {
        this.m_LineaAdministrada = m_LineaAdministrada;
    }

    /**
     * @return the m_FechaSaldoIni
     */
    public String getM_FechaSaldoIni() {
        return m_FechaSaldoIni;
    }

    /**
     * @param m_FechaSaldoIni the m_FechaSaldoIni to set
     */
    public void setM_FechaSaldoIni(String m_FechaSaldoIni) {
        this.m_FechaSaldoIni = m_FechaSaldoIni;
    }

    /**
     * @return the m_FechaSaldoFin
     */
    public String getM_FechaSaldoFin() {
        return m_FechaSaldoFin;
    }

    /**
     * @param m_FechaSaldoFin the m_FechaSaldoFin to set
     */
    public void setM_FechaSaldoFin(String m_FechaSaldoFin) {
        this.m_FechaSaldoFin = m_FechaSaldoFin;
    }

    /**
     * @return the m_SaldoInicial
     */
    public String getM_SaldoInicial() {
        return m_SaldoInicial;
    }

    /**
     * @param m_SaldoInicial the m_SaldoInicial to set
     */
    public void setM_SaldoInicial(String m_SaldoInicial) {
        this.m_SaldoInicial = m_SaldoInicial;
    }

    /**
     * @return the m_SaldoFinal
     */
    public String getM_SaldoFinal() {
        return m_SaldoFinal;
    }

    /**
     * @param m_SaldoFinal the m_SaldoFinal to set
     */
    public void setM_SaldoFinal(String m_SaldoFinal) {
        this.m_SaldoFinal = m_SaldoFinal;
    }
}

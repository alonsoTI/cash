/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

/**
 *
 * @author jwong
 */
public class BeanDetalleMovimiento {
    private String m_Fecha;
    private String m_Hora;
    private String m_TipoTrx;
    private String m_Descripcion;

    private String m_Signo;
    private String m_Importe;

    private String m_Moneda;
    private String m_Referencia;

    //jwong 19/02/2009 campos adicionales para saldos promedios
    private String m_SignoSaldoMov;
    private String m_SaldoMovimiento;
    private String m_Indicador;
    
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
     * @return the m_TipoTrx
     */
    public String getM_TipoTrx() {
        return m_TipoTrx;
    }

    /**
     * @param m_TipoTrx the m_TipoTrx to set
     */
    public void setM_TipoTrx(String m_TipoTrx) {
        this.m_TipoTrx = m_TipoTrx;
    }

    /**
     * @return the m_Descripcion
     */
    public String getM_Descripcion() {
        return m_Descripcion;
    }

    /**
     * @param m_Descripcion the m_Descripcion to set
     */
    public void setM_Descripcion(String m_Descripcion) {
        this.m_Descripcion = m_Descripcion;
    }

    /**
     * @return the m_Importe
     */
    public String getM_Importe() {
        return m_Importe;
    }

    /**
     * @param m_Importe the m_Importe to set
     */
    public void setM_Importe(String m_Importe) {
        this.m_Importe = m_Importe;
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
     * @return the m_Referencia
     */
    public String getM_Referencia() {
        return m_Referencia;
    }

    /**
     * @param m_Referencia the m_Referencia to set
     */
    public void setM_Referencia(String m_Referencia) {
        this.m_Referencia = m_Referencia;
    }

    /**
     * @return the m_Signo
     */
    public String getM_Signo() {
        return m_Signo;
    }

    /**
     * @param m_Signo the m_Signo to set
     */
    public void setM_Signo(String m_Signo) {
        this.m_Signo = m_Signo;
    }

    /**
     * @return the m_SaldoMovimiento
     */
    public String getM_SaldoMovimiento() {
        return m_SaldoMovimiento;
    }

    /**
     * @param m_SaldoMovimiento the m_SaldoMovimiento to set
     */
    public void setM_SaldoMovimiento(String m_SaldoMovimiento) {
        this.m_SaldoMovimiento = m_SaldoMovimiento;
    }

    /**
     * @return the m_Indicador
     */
    public String getM_Indicador() {
        return m_Indicador;
    }

    /**
     * @param m_Indicador the m_Indicador to set
     */
    public void setM_Indicador(String m_Indicador) {
        this.m_Indicador = m_Indicador;
    }

    /**
     * @return the m_SignoSaldoMov
     */
    public String getM_SignoSaldoMov() {
        return m_SignoSaldoMov;
    }

    /**
     * @param m_SignoSaldoMov the m_SignoSaldoMov to set
     */
    public void setM_SignoSaldoMov(String m_SignoSaldoMov) {
        this.m_SignoSaldoMov = m_SignoSaldoMov;
    }
}
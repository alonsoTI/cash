/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

/**
 *
 * @author jwong
 */
public class BeanDetalleImporteEstado {
    private String m_DetIDOrden ="";
    private String m_DetReferencia ="";
    
    private long m_NumEnviados = 0;//para soles, el mismo standar para todas jmoreno 21-08-09
    private long m_NumEnviadosDol = 0;
    private double m_ValorSolesEnviado = 0.0;
    private double m_ValorDolaresEnviado = 0.0;
    private double m_ValorEurosEnviado = 0.0;

    private long m_NumProcesados = 0;
    private long m_NumProcesadosDol = 0;
    private double m_ValorSolesProcesado = 0.0;
    private double m_ValorDolaresProcesado = 0.0;
    private double m_ValorEurosProcesado = 0.0;
    
    private long m_NumErrados = 0;
    private long m_NumErradosDol = 0;
    private double m_ValorSolesErrado = 0.0;
    private double m_ValorDolaresErrado = 0.0;
    private double m_ValorEurosErrado = 0.0;

    private String m_DetEstado ="";

    
    //jwong 08/05/2009 nuevos campos para manejo del formato de montos con dos decimales.
    private String m_StrValorSolesEnviado ="";
    private String m_StrValorDolaresEnviado ="";
    private String m_StrValorEurosEnviado ="";

    private String m_StrValorSolesProcesado ="";
    private String m_StrValorDolaresProcesado ="";
    private String m_StrValorEurosProcesado ="";

    private String m_StrValorSolesErrado ="";
    private String m_StrValorDolaresErrado ="";
    private String m_StrValorEurosErrado ="";

    //jmoreno 21-08-09
    private long m_NumCobrados = 0;
    private long m_NumCobradosDol = 0;
    private double m_ValorSolesCobrados = 0.0;
    private double m_ValorDolaresCobrados = 0.0;
    private double m_ValorEurosCobrados = 0.0;

    private long m_NumCancelados = 0;
    private long m_NumCanceladosDol = 0;
    private double m_ValorSolesCancelados = 0.0;
    private double m_ValorDolaresCancelados = 0.0;
    private double m_ValorEurosCancelados = 0.0;

    private long m_NumCobradosParc = 0;
    private long m_NumCobradosParcDol = 0;
    private double m_ValorSolesCobradosParc = 0.0;
    private double m_ValorDolaresCobradosParc = 0.0;
    private double m_ValorEurosCobradosParc = 0.0;

    private long m_NumPendConf = 0;
    private long m_NumPendConfDol = 0;
    private double m_ValorSolesPendConf = 0.0;
    private double m_ValorDolaresPendConf = 0.0;
    private double m_ValorEurosPendConf = 0.0;

    private long m_NumExtornados = 0;
    private long m_NumExtornadosDol = 0;
    private double m_ValorSolesExtornados = 0.0;
    private double m_ValorDolaresExtornados = 0.0;
    private double m_ValorEurosExtornados = 0.0;

    private long m_NumArchivados= 0;
    private long m_NumArchivadosDol= 0;
    private double m_ValorSolesArchivados = 0.0;
    private double m_ValorDolaresArchivados = 0.0;
    private double m_ValorEurosArchivados = 0.0;

    private String m_StrValorSolesCobrados ="";
    private String m_StrValorDolaresCobrados ="";
    private String m_StrValorEurosCobrados ="";

    private String m_StrValorSolesCancelados ="";
    private String m_StrValorDolaresCancelados ="";
    private String m_StrValorEurosCancelados ="";

    private String m_StrValorSolesCobradosParc ="";
    private String m_StrValorDolaresCobradosParc ="";
    private String m_StrValorEurosCobradosParc ="";

    private String m_StrValorSolesPendConf ="";
    private String m_StrValorDolaresPendConf ="";
    private String m_StrValorEurosPendConf ="";

    private String m_StrValorSolesExtornados ="";
    private String m_StrValorDolaresExtornados ="";
    private String m_StrValorEurosExtornados ="";

    private String m_StrValorSolesArchivados ="";
    private String m_StrValorDolaresArchivados ="";
    private String m_StrValorEurosArchivados ="";

    private long m_NumTotalSoles = 0;
    private long m_NumTotalDolares = 0;

    private String m_valorSolesTotal = "";
    private String m_valorDolaresTotal = "";
    /**
     * @return the m_NumEnviados
     */
    public long getM_NumEnviados() {
        return m_NumEnviados;
    }

    /**
     * @param m_NumEnviados the m_NumEnviados to set
     */
    public void setM_NumEnviados(long m_NumEnviados) {
        this.m_NumEnviados = m_NumEnviados;
    }

    /**
     * @return the m_ValorSolesEnviado
     */
    public double getM_ValorSolesEnviado() {
        return m_ValorSolesEnviado;
    }

    /**
     * @param m_ValorSolesEnviado the m_ValorSolesEnviado to set
     */
    public void setM_ValorSolesEnviado(double m_ValorSolesEnviado) {
        this.m_ValorSolesEnviado = m_ValorSolesEnviado;
    }

    /**
     * @return the m_ValorDolaresEnviado
     */
    public double getM_ValorDolaresEnviado() {
        return m_ValorDolaresEnviado;
    }

    /**
     * @param m_ValorDolaresEnviado the m_ValorDolaresEnviado to set
     */
    public void setM_ValorDolaresEnviado(double m_ValorDolaresEnviado) {
        this.m_ValorDolaresEnviado = m_ValorDolaresEnviado;
    }

    /**
     * @return the m_ValorEurosEnviado
     */
    public double getM_ValorEurosEnviado() {
        return m_ValorEurosEnviado;
    }

    /**
     * @param m_ValorEurosEnviado the m_ValorEurosEnviado to set
     */
    public void setM_ValorEurosEnviado(double m_ValorEurosEnviado) {
        this.m_ValorEurosEnviado = m_ValorEurosEnviado;
    }

    /**
     * @return the m_NumProcesados
     */
    public long getM_NumProcesados() {
        return m_NumProcesados;
    }

    /**
     * @param m_NumProcesados the m_NumProcesados to set
     */
    public void setM_NumProcesados(long m_NumProcesados) {
        this.m_NumProcesados = m_NumProcesados;
    }

    /**
     * @return the m_ValorSolesProcesado
     */
    public double getM_ValorSolesProcesado() {
        return m_ValorSolesProcesado;
    }

    /**
     * @param m_ValorSolesProcesado the m_ValorSolesProcesado to set
     */
    public void setM_ValorSolesProcesado(double m_ValorSolesProcesado) {
        this.m_ValorSolesProcesado = m_ValorSolesProcesado;
    }

    /**
     * @return the m_ValorDolaresProcesado
     */
    public double getM_ValorDolaresProcesado() {
        return m_ValorDolaresProcesado;
    }

    /**
     * @param m_ValorDolaresProcesado the m_ValorDolaresProcesado to set
     */
    public void setM_ValorDolaresProcesado(double m_ValorDolaresProcesado) {
        this.m_ValorDolaresProcesado = m_ValorDolaresProcesado;
    }

    /**
     * @return the m_ValorEurosProcesado
     */
    public double getM_ValorEurosProcesado() {
        return m_ValorEurosProcesado;
    }

    /**
     * @param m_ValorEurosProcesado the m_ValorEurosProcesado to set
     */
    public void setM_ValorEurosProcesado(double m_ValorEurosProcesado) {
        this.m_ValorEurosProcesado = m_ValorEurosProcesado;
    }

    /**
     * @return the m_NumErrados
     */
    public long getM_NumErrados() {
        return m_NumErrados;
    }

    /**
     * @param m_NumErrados the m_NumErrados to set
     */
    public void setM_NumErrados(long m_NumErrados) {
        this.m_NumErrados = m_NumErrados;
    }

    /**
     * @return the m_ValorSolesErrado
     */
    public double getM_ValorSolesErrado() {
        return m_ValorSolesErrado;
    }

    /**
     * @param m_ValorSolesErrado the m_ValorSolesErrado to set
     */
    public void setM_ValorSolesErrado(double m_ValorSolesErrado) {
        this.m_ValorSolesErrado = m_ValorSolesErrado;
    }

    /**
     * @return the m_ValorDolaresErrado
     */
    public double getM_ValorDolaresErrado() {
        return m_ValorDolaresErrado;
    }

    /**
     * @param m_ValorDolaresErrado the m_ValorDolaresErrado to set
     */
    public void setM_ValorDolaresErrado(double m_ValorDolaresErrado) {
        this.m_ValorDolaresErrado = m_ValorDolaresErrado;
    }

    /**
     * @return the m_ValorEurosErrado
     */
    public double getM_ValorEurosErrado() {
        return m_ValorEurosErrado;
    }

    /**
     * @param m_ValorEurosErrado the m_ValorEurosErrado to set
     */
    public void setM_ValorEurosErrado(double m_ValorEurosErrado) {
        this.m_ValorEurosErrado = m_ValorEurosErrado;
    }

    
    public String getM_DetReferencia() {
        return m_DetReferencia;
    }

    public void setM_DetReferencia(String m_DetReferencia) {
        if(m_DetReferencia !=null){
            this.m_DetReferencia = m_DetReferencia;
        }else{
            this.m_DetReferencia = "";
        }
        
    }

    public String getM_DetIDOrden() {
        return m_DetIDOrden;
    }

    public void setM_DetIDOrden(String m_DetIDOrden) {
        this.m_DetIDOrden = m_DetIDOrden;
    }

    public String getM_DetEstado() {
        return m_DetEstado;
    }

    public void setM_DetEstado(String m_DetEstado) {
        this.m_DetEstado = m_DetEstado;
    }

    /**
     * @return the m_StrValorSolesEnviado
     */
    public String getM_StrValorSolesEnviado() {
        return m_StrValorSolesEnviado;
    }

    /**
     * @param m_StrValorSolesEnviado the m_StrValorSolesEnviado to set
     */
    public void setM_StrValorSolesEnviado(String m_StrValorSolesEnviado) {
        this.m_StrValorSolesEnviado = m_StrValorSolesEnviado;
    }

    /**
     * @return the m_StrValorDolaresEnviado
     */
    public String getM_StrValorDolaresEnviado() {
        return m_StrValorDolaresEnviado;
    }

    /**
     * @param m_StrValorDolaresEnviado the m_StrValorDolaresEnviado to set
     */
    public void setM_StrValorDolaresEnviado(String m_StrValorDolaresEnviado) {
        this.m_StrValorDolaresEnviado = m_StrValorDolaresEnviado;
    }

    /**
     * @return the m_StrValorEurosEnviado
     */
    public String getM_StrValorEurosEnviado() {
        return m_StrValorEurosEnviado;
    }

    /**
     * @param m_StrValorEurosEnviado the m_StrValorEurosEnviado to set
     */
    public void setM_StrValorEurosEnviado(String m_StrValorEurosEnviado) {
        this.m_StrValorEurosEnviado = m_StrValorEurosEnviado;
    }

    /**
     * @return the m_StrValorSolesProcesado
     */
    public String getM_StrValorSolesProcesado() {
        return m_StrValorSolesProcesado;
    }

    /**
     * @param m_StrValorSolesProcesado the m_StrValorSolesProcesado to set
     */
    public void setM_StrValorSolesProcesado(String m_StrValorSolesProcesado) {
        this.m_StrValorSolesProcesado = m_StrValorSolesProcesado;
    }

    /**
     * @return the m_StrValorDolaresProcesado
     */
    public String getM_StrValorDolaresProcesado() {
        return m_StrValorDolaresProcesado;
    }

    /**
     * @param m_StrValorDolaresProcesado the m_StrValorDolaresProcesado to set
     */
    public void setM_StrValorDolaresProcesado(String m_StrValorDolaresProcesado) {
        this.m_StrValorDolaresProcesado = m_StrValorDolaresProcesado;
    }

    /**
     * @return the m_StrValorEurosProcesado
     */
    public String getM_StrValorEurosProcesado() {
        return m_StrValorEurosProcesado;
    }

    /**
     * @param m_StrValorEurosProcesado the m_StrValorEurosProcesado to set
     */
    public void setM_StrValorEurosProcesado(String m_StrValorEurosProcesado) {
        this.m_StrValorEurosProcesado = m_StrValorEurosProcesado;
    }

    /**
     * @return the m_StrValorSolesErrado
     */
    public String getM_StrValorSolesErrado() {
        return m_StrValorSolesErrado;
    }

    /**
     * @param m_StrValorSolesErrado the m_StrValorSolesErrado to set
     */
    public void setM_StrValorSolesErrado(String m_StrValorSolesErrado) {
        this.m_StrValorSolesErrado = m_StrValorSolesErrado;
    }

    /**
     * @return the m_StrValorDolaresErrado
     */
    public String getM_StrValorDolaresErrado() {
        return m_StrValorDolaresErrado;
    }

    /**
     * @param m_StrValorDolaresErrado the m_StrValorDolaresErrado to set
     */
    public void setM_StrValorDolaresErrado(String m_StrValorDolaresErrado) {
        this.m_StrValorDolaresErrado = m_StrValorDolaresErrado;
    }

    /**
     * @return the m_StrValorEurosErrado
     */
    public String getM_StrValorEurosErrado() {
        return m_StrValorEurosErrado;
    }

    /**
     * @param m_StrValorEurosErrado the m_StrValorEurosErrado to set
     */
    public void setM_StrValorEurosErrado(String m_StrValorEurosErrado) {
        
        this.m_StrValorEurosErrado = m_StrValorEurosErrado;
    }

    public long getM_NumCobrados() {
        return m_NumCobrados;
    }

    public void setM_NumCobrados(long m_NumCobrados) {
        this.m_NumCobrados = m_NumCobrados;
    }

    public double getM_ValorSolesCobrados() {
        return m_ValorSolesCobrados;
    }

    public void setM_ValorSolesCobrados(double m_ValorSolesCobrados) {
        this.m_ValorSolesCobrados = m_ValorSolesCobrados;
    }

    public double getM_ValorDolaresCobrados() {
        return m_ValorDolaresCobrados;
    }

    public void setM_ValorDolaresCobrados(double m_ValorDolaresCobrados) {
        this.m_ValorDolaresCobrados = m_ValorDolaresCobrados;
    }

    public double getM_ValorEurosCobrados() {
        return m_ValorEurosCobrados;
    }

    public void setM_ValorEurosCobrados(double m_ValorEurosCobrados) {
        this.m_ValorEurosCobrados = m_ValorEurosCobrados;
    }

    public long getM_NumCancelados() {
        return m_NumCancelados;
    }

    public void setM_NumCancelados(long m_NumCancelados) {
        this.m_NumCancelados = m_NumCancelados;
    }

    public double getM_ValorSolesCancelados() {
        return m_ValorSolesCancelados;
    }

    public void setM_ValorSolesCancelados(double m_ValorSolesCancelados) {
        this.m_ValorSolesCancelados = m_ValorSolesCancelados;
    }

    public double getM_ValorDolaresCancelados() {
        return m_ValorDolaresCancelados;
    }

    public void setM_ValorDolaresCancelados(double m_ValorDolaresCancelados) {
        this.m_ValorDolaresCancelados = m_ValorDolaresCancelados;
    }

    public double getM_ValorEurosCancelados() {
        return m_ValorEurosCancelados;
    }

    public void setM_ValorEurosCancelados(double m_ValorEurosCancelados) {
        this.m_ValorEurosCancelados = m_ValorEurosCancelados;
    }

    public long getM_NumCobradosParc() {
        return m_NumCobradosParc;
    }

    public void setM_NumCobradosParc(long m_NumCobradosParc) {
        this.m_NumCobradosParc = m_NumCobradosParc;
    }

    public double getM_ValorSolesCobradosParc() {
        return m_ValorSolesCobradosParc;
    }

    public void setM_ValorSolesCobradosParc(double m_ValorSolesCobradosParc) {
        this.m_ValorSolesCobradosParc = m_ValorSolesCobradosParc;
    }

    public double getM_ValorDolaresCobradosParc() {
        return m_ValorDolaresCobradosParc;
    }

    public void setM_ValorDolaresCobradosParc(double m_ValorDolaresCobradosParc) {
        this.m_ValorDolaresCobradosParc = m_ValorDolaresCobradosParc;
    }

    public double getM_ValorEurosCobradosParc() {
        return m_ValorEurosCobradosParc;
    }

    public void setM_ValorEurosCobradosParc(double m_ValorEurosCobradosParc) {
        this.m_ValorEurosCobradosParc = m_ValorEurosCobradosParc;
    }
    public double getM_ValorSolesPendConf() {
        return m_ValorSolesPendConf;
    }

    public void setM_ValorSolesPendConf(double m_ValorSolesPendConf) {
        this.m_ValorSolesPendConf = m_ValorSolesPendConf;
    }

    public double getM_ValorDolaresPendConf() {
        return m_ValorDolaresPendConf;
    }

    public void setM_ValorDolaresPendConf(double m_ValorDolaresPendConf) {
        this.m_ValorDolaresPendConf = m_ValorDolaresPendConf;
    }

    public double getM_ValorEurosPendConf() {
        return m_ValorEurosPendConf;
    }

    public void setM_ValorEurosPendConf(double m_ValorEurosPendConf) {
        this.m_ValorEurosPendConf = m_ValorEurosPendConf;
    }

    public double getM_ValorSolesExtornados() {
        return m_ValorSolesExtornados;
    }

    public void setM_ValorSolesExtornados(double m_ValorSolesExtornados) {
        this.m_ValorSolesExtornados = m_ValorSolesExtornados;
    }

    public double getM_ValorDolaresExtornados() {
        return m_ValorDolaresExtornados;
    }

    public void setM_ValorDolaresExtornados(double m_ValorDolaresExtornados) {
        this.m_ValorDolaresExtornados = m_ValorDolaresExtornados;
    }

    public double getM_ValorEurosExtornados() {
        return m_ValorEurosExtornados;
    }

    public void setM_ValorEurosExtornados(double m_ValorEurosExtornados) {
        this.m_ValorEurosExtornados = m_ValorEurosExtornados;
    }

    public double getM_ValorSolesArchivados() {
        return m_ValorSolesArchivados;
    }

    public void setM_ValorSolesArchivados(double m_ValorSolesArchivados) {
        this.m_ValorSolesArchivados = m_ValorSolesArchivados;
    }

    public double getM_ValorDolaresArchivados() {
        return m_ValorDolaresArchivados;
    }

    public void setM_ValorDolaresArchivados(double m_ValorDolaresArchivados) {
        this.m_ValorDolaresArchivados = m_ValorDolaresArchivados;
    }

    public double getM_ValorEurosArchivados() {
        return m_ValorEurosArchivados;
    }

    public void setM_ValorEurosArchivados(double m_ValorEurosArchivados) {
        this.m_ValorEurosArchivados = m_ValorEurosArchivados;
    }

    public long getM_NumPendConf() {
        return m_NumPendConf;
    }

    public void setM_NumPendConf(long m_NumPendConf) {
        this.m_NumPendConf = m_NumPendConf;
    }

    public long getM_NumExtornados() {
        return m_NumExtornados;
    }

    public void setM_NumExtornados(long m_NumExtornados) {
        this.m_NumExtornados = m_NumExtornados;
    }

    public long getM_NumArchivados() {
        return m_NumArchivados;
    }

    public void setM_NumArchivados(long m_NumArchivados) {
        this.m_NumArchivados = m_NumArchivados;
    }

    public String getM_StrValorSolesCobrados() {
        return m_StrValorSolesCobrados;
    }

    public void setM_StrValorSolesCobrados(String m_StrValorSolesCobrados) {
        this.m_StrValorSolesCobrados = m_StrValorSolesCobrados;
    }

    public String getM_StrValorDolaresCobrados() {
        return m_StrValorDolaresCobrados;
    }

    public void setM_StrValorDolaresCobrados(String m_StrValorDolaresCobrados) {
        this.m_StrValorDolaresCobrados = m_StrValorDolaresCobrados;
    }

    public String getM_StrValorEurosCobrados() {
        return m_StrValorEurosCobrados;
    }

    public void setM_StrValorEurosCobrados(String m_StrValorEurosCobrados) {
        this.m_StrValorEurosCobrados = m_StrValorEurosCobrados;
    }

    public String getM_StrValorSolesCancelados() {
        return m_StrValorSolesCancelados;
    }

    public void setM_StrValorSolesCancelados(String m_StrValorSolesCancelados) {
        this.m_StrValorSolesCancelados = m_StrValorSolesCancelados;
    }

    public String getM_StrValorDolaresCancelados() {
        return m_StrValorDolaresCancelados;
    }

    public void setM_StrValorDolaresCancelados(String m_StrValorDolaresCancelados) {
        this.m_StrValorDolaresCancelados = m_StrValorDolaresCancelados;
    }

    public String getM_StrValorEurosCancelados() {
        return m_StrValorEurosCancelados;
    }

    public void setM_StrValorEurosCancelados(String m_StrValorEurosCancelados) {
        this.m_StrValorEurosCancelados = m_StrValorEurosCancelados;
    }

    public String getM_StrValorSolesCobradosParc() {
        return m_StrValorSolesCobradosParc;
    }

    public void setM_StrValorSolesCobradosParc(String m_StrValorSolesCobradosParc) {
        this.m_StrValorSolesCobradosParc = m_StrValorSolesCobradosParc;
    }

    public String getM_StrValorDolaresCobradosParc() {
        return m_StrValorDolaresCobradosParc;
    }

    public void setM_StrValorDolaresCobradosParc(String m_StrValorDolaresCobradosParc) {
        this.m_StrValorDolaresCobradosParc = m_StrValorDolaresCobradosParc;
    }

    public String getM_StrValorEurosCobradosParc() {
        return m_StrValorEurosCobradosParc;
    }

    public void setM_StrValorEurosCobradosParc(String m_StrValorEurosCobradosParc) {
        this.m_StrValorEurosCobradosParc = m_StrValorEurosCobradosParc;
    }

    public String getM_StrValorSolesPendConf() {
        return m_StrValorSolesPendConf;
    }

    public void setM_StrValorSolesPendConf(String m_StrValorSolesPendConf) {
        this.m_StrValorSolesPendConf = m_StrValorSolesPendConf;
    }

    public String getM_StrValorDolaresPendConf() {
        return m_StrValorDolaresPendConf;
    }

    public void setM_StrValorDolaresPendConf(String m_StrValorDolaresPendConf) {
        this.m_StrValorDolaresPendConf = m_StrValorDolaresPendConf;
    }

    public String getM_StrValorEurosPendConf() {
        return m_StrValorEurosPendConf;
    }

    public void setM_StrValorEurosPendConf(String m_StrValorEurosPendConf) {
        this.m_StrValorEurosPendConf = m_StrValorEurosPendConf;
    }

    public String getM_StrValorSolesExtornados() {
        return m_StrValorSolesExtornados;
    }

    public void setM_StrValorSolesExtornados(String m_StrValorSolesExtornados) {
        this.m_StrValorSolesExtornados = m_StrValorSolesExtornados;
    }

    public String getM_StrValorDolaresExtornados() {
        return m_StrValorDolaresExtornados;
    }

    public void setM_StrValorDolaresExtornados(String m_StrValorDolaresExtornados) {
        this.m_StrValorDolaresExtornados = m_StrValorDolaresExtornados;
    }

    public String getM_StrValorEurosExtornados() {
        return m_StrValorEurosExtornados;
    }

    public void setM_StrValorEurosExtornados(String m_StrValorEurosExtornados) {
        this.m_StrValorEurosExtornados = m_StrValorEurosExtornados;
    }

    public String getM_StrValorSolesArchivados() {
        return m_StrValorSolesArchivados;
    }

    public void setM_StrValorSolesArchivados(String m_StrValorSolesArchivados) {
        this.m_StrValorSolesArchivados = m_StrValorSolesArchivados;
    }

    public String getM_StrValorDolaresArchivados() {
        return m_StrValorDolaresArchivados;
    }

    public void setM_StrValorDolaresArchivados(String m_StrValorDolaresArchivados) {
        this.m_StrValorDolaresArchivados = m_StrValorDolaresArchivados;
    }

    public String getM_StrValorEurosArchivados() {
        return m_StrValorEurosArchivados;
    }

    public void setM_StrValorEurosArchivados(String m_StrValorEurosArchivados) {
        this.m_StrValorEurosArchivados = m_StrValorEurosArchivados;
    }

    public long getM_NumEnviadosDol() {
        return m_NumEnviadosDol;
    }

    public void setM_NumEnviadosDol(long m_NumEnviadosDol) {
        this.m_NumEnviadosDol = m_NumEnviadosDol;
    }

    public long getM_NumProcesadosDol() {
        return m_NumProcesadosDol;
    }

    public void setM_NumProcesadosDol(long m_NumProcesadosDol) {
        this.m_NumProcesadosDol = m_NumProcesadosDol;
    }

    public long getM_NumErradosDol() {
        return m_NumErradosDol;
    }

    public void setM_NumErradosDol(long m_NumErradosDol) {
        this.m_NumErradosDol = m_NumErradosDol;
    }

    public long getM_NumCobradosDol() {
        return m_NumCobradosDol;
    }

    public void setM_NumCobradosDol(long m_NumCobradosDol) {
        this.m_NumCobradosDol = m_NumCobradosDol;
    }

    public long getM_NumCanceladosDol() {
        return m_NumCanceladosDol;
    }

    public void setM_NumCanceladosDol(long m_NumCanceladosDol) {
        this.m_NumCanceladosDol = m_NumCanceladosDol;
    }

    public long getM_NumCobradosParcDol() {
        return m_NumCobradosParcDol;
    }

    public void setM_NumCobradosParcDol(long m_NumCobradosParcDol) {
        this.m_NumCobradosParcDol = m_NumCobradosParcDol;
    }

    public long getM_NumPendConfDol() {
        return m_NumPendConfDol;
    }

    public void setM_NumPendConfDol(long m_NumPendConfDol) {
        this.m_NumPendConfDol = m_NumPendConfDol;
    }

    public long getM_NumExtornadosDol() {
        return m_NumExtornadosDol;
    }

    public void setM_NumExtornadosDol(long m_NumExtornadosDol) {
        this.m_NumExtornadosDol = m_NumExtornadosDol;
    }

    public long getM_NumArchivadosDol() {
        return m_NumArchivadosDol;
    }

    public void setM_NumArchivadosDol(long m_NumArchivadosDol) {
        this.m_NumArchivadosDol = m_NumArchivadosDol;
    }

    public long getM_NumTotalSoles() {
        return m_NumTotalSoles;
    }

    public void setM_NumTotalSoles(long m_NumTotalSoles) {
        this.m_NumTotalSoles = m_NumTotalSoles;
    }

    public long getM_NumTotalDolares() {
        return m_NumTotalDolares;
    }

    public void setM_NumTotalDolares(long m_NumTotalDolares) {
        this.m_NumTotalDolares = m_NumTotalDolares;
    }

    public String getM_valorSolesTotal() {
        return m_valorSolesTotal;
    }

    public void setM_valorSolesTotal(String m_valorSolesTotal) {
        this.m_valorSolesTotal = m_valorSolesTotal;
    }

    public String getM_valorDolaresTotal() {
        return m_valorDolaresTotal;
    }

    public void setM_valorDolaresTotal(String m_valorDolaresTotal) {
        this.m_valorDolaresTotal = m_valorDolaresTotal;
    }

    
}

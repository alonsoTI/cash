/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

import java.util.HashMap;
import java.util.Map;

import com.hiper.cash.util.Util;

/**
 *
 * @author esilva
 */
public class BeanOrden {
    private String m_IdOrden = "";
    private String m_IdEmpresa = "";
    private String m_IdServicio = "";
    private String m_Empresa = "";
    private String m_Servicio = "";
    private String m_CuentaCargo = "";
    private String m_Referencia = "";
    private String m_FecInicio = "";
    private String m_FecRegistro = "";
    private String m_FecVenc = "";
    private String m_HoraVigencia = "";
    private String m_Items = "";
    private String m_Valor = "";
    private String m_Sobregiro = "";
    private String m_TipoIngreso = "";
    private String m_TipoCuenta = "";
    private String m_IdAprobador = "";
    
	private String m_DescripMoneda = ""; //descripcion de la moneda
    private String m_DescripEstado = ""; //descripcion del estado
    private String m_DescCuenta = ""; //descripcion de la cuenta
    private String m_DescHora = ""; // descripcion de la hora

    //jwong 21/01/2009 nuevos campos
    private String m_ValorSoles = ""; //valor del importe en soles
    private String m_ValorDolares = ""; //valor del importe en dolares
    private String m_ValorEuros = ""; //valor del importe en euros

    //jwong 11/05/2009 nuevo campo
    private String m_CodServicio = ""; //codigo del servicio
    //jmoreno 25-08-09
    private String m_TipoServicio = "";
    private String m_IdUsuario = "";//Para identificar que usuario realizó la aprobacion de la orden
    public String getM_IdOrden() {
        return m_IdOrden;
    }

    public void setM_IdOrden(String m_IdOrden) {
        this.m_IdOrden = m_IdOrden;
    }

    public String getM_Empresa() {
        return m_Empresa;
    }

    public void setM_Empresa(String m_Empresa) {
        this.m_Empresa = m_Empresa;
    }

    public String getM_CuentaCargo() {
        return m_CuentaCargo;
    }

    public void setM_CuentaCargo(String m_CuentaCargo) {
        if(m_CuentaCargo != null){
            this.m_CuentaCargo = m_CuentaCargo;
        }else{
            this.m_CuentaCargo = "";
        }
        
    }

    public String getM_Referencia() {
        return m_Referencia;
    }

    public void setM_Referencia(String m_Referencia) {
        if(m_Referencia != null){
            this.m_Referencia = m_Referencia;
        }else{
            this.m_Referencia = "";
        }
        
    }

    public String getM_FecInicio() {
        return m_FecInicio;
    }

    public void setM_FecInicio(String m_FecInicio) {        
        if(m_FecInicio != null){
            this.m_FecInicio = m_FecInicio;
        }else{
            this.m_FecInicio = "";
        }

    }

    public String getM_FecVenc() {
        return m_FecVenc;
    }

    public void setM_FecVenc(String m_FecVenc) {
        if(m_FecVenc != null){
            this.m_FecVenc = m_FecVenc;
        }else{
            this.m_FecVenc = "";
        }
        
    }

    public String getM_Items() {
        return m_Items;
    }

    public void setM_Items(String m_Items) {
        if(m_Items != null){
            this.m_Items = m_Items;
        }else{
            this.m_Items = "0";
        }
        
    }

    public String getM_Valor() {
        return m_Valor;
    }

    public void setM_Valor(String m_Valor) {
        this.m_Valor = m_Valor;
    }

    public java.util.Map getParametrosUrl(){
        java.util.Map parametros = new java.util.HashMap();
        parametros.put("m_IdOrden",this.m_IdOrden);
        parametros.put("m_IdEmpresa",this.m_IdEmpresa);
        parametros.put("m_IdServicio",this.m_IdServicio);
        //jwong 11/05/2009 se añade parametro
        parametros.put("m_CodServicio",this.m_CodServicio);
        //jmoreno 31-07-09
        parametros.put("m_Items",this.m_Items);
        return parametros;
    }
    
    public Map<String,String> getParametrosIniciarDetallesOrden(){
        Map<String,String> parametros = new HashMap<String,String>();
        parametros.put("m_IdOrden",this.m_IdOrden);        
        parametros.put("m_IdServicio",this.m_IdServicio);        
        return parametros;
    }

    /**
     * @return the m_IdEmpresa
     */
    public String getM_IdEmpresa() {
        return m_IdEmpresa;
    }

    /**
     * @param m_IdEmpresa the m_IdEmpresa to set
     */
    public void setM_IdEmpresa(String m_IdEmpresa) {
        this.m_IdEmpresa = m_IdEmpresa;
    }

    /**
     * @return the m_IdServicio
     */
    public String getM_IdServicio() {
        return m_IdServicio;
    }

    /**
     * @param m_IdServicio the m_IdServicio to set
     */
    public void setM_IdServicio(String m_IdServicio) {
        this.m_IdServicio = m_IdServicio;
    }

    public String getM_Sobregiro() {
        return m_Sobregiro;
    }

    public void setM_Sobregiro(String m_Sobregiro) {
        this.m_Sobregiro = m_Sobregiro;
    }

    public String getM_TipoIngreso() {
        return m_TipoIngreso;
    }

    public void setM_TipoIngreso(String m_TipoIngreso) {
        this.m_TipoIngreso = m_TipoIngreso;
    }

    public String getM_Servicio() {
        return m_Servicio;
    }

    public void setM_Servicio(String m_Servicio) {
        this.m_Servicio = m_Servicio;
    }

    public String getM_HoraVigencia() {
        return m_HoraVigencia;
    }

    public void setM_HoraVigencia(String m_HoraVigencia) {
        if(m_HoraVigencia != null){
            this.m_HoraVigencia = m_HoraVigencia;
        }else{
            this.m_HoraVigencia = "";
        }
        
    }

	/**
     * @return the m_DescripMoneda
     */
    public String getM_DescripMoneda() {
        return m_DescripMoneda;
    }

    /**
     * @param m_DescripMoneda the m_DescripMoneda to set
     */
    public void setM_DescripMoneda(String m_DescripMoneda) {
        if(m_DescripMoneda != null){
            this.m_DescripMoneda = m_DescripMoneda;
        }else{
            this.m_DescripMoneda = "";
        }
        
    }

    /**
     * @return the m_DescripEstado
     */
    public String getM_DescripEstado() {
        return m_DescripEstado;
    }

    /**
     * @param m_DescripEstado the m_DescripEstado to set
     */
    public void setM_DescripEstado(String m_DescripEstado) {
        if(m_DescripEstado != null){
            this.m_DescripEstado = m_DescripEstado;
        }else{
            this.m_DescripEstado = "";
        }
        
    }

    /**
     * @return the m_ValorSoles
     */
    public String getM_ValorSoles() {
        return m_ValorSoles;
    }

    /**
     * @param m_ValorSoles the m_ValorSoles to set
     */
    public void setM_ValorSoles(String m_ValorSoles) {
        if(m_ValorSoles != null){
            this.m_ValorSoles = m_ValorSoles;
        }else{
            this.m_ValorSoles = "";
        }
        
    }

    /**
     * @return the m_ValorDolares
     */
    public String getM_ValorDolares() {
        return m_ValorDolares;
    }

    /**
     * @param m_ValorDolares the m_ValorDolares to set
     */
    public void setM_ValorDolares(String m_ValorDolares) {
        if(m_ValorDolares != null){
            this.m_ValorDolares = m_ValorDolares;
        }else{
            this.m_ValorDolares = "";
        }
        
    }

    /**
     * @return the m_ValorEuros
     */
    public String getM_ValorEuros() {
        return m_ValorEuros;
    }

    /**
     * @param m_ValorEuros the m_ValorEuros to set
     */
    public void setM_ValorEuros(String m_ValorEuros) {
        if(m_ValorEuros != null){
            this.m_ValorEuros = m_ValorEuros;
        }else{
            this.m_ValorEuros = "";
        }
        
    }

    //jwong 21/01/2009 parametros para obtener el detalle de los importes por estado de una orden
    public java.util.Map getParamDetImportEstadoUrl(){
        java.util.Map parametros = new java.util.HashMap();
        parametros.put("m_IdOrden",this.m_IdOrden);
        parametros.put("m_IdServicio",this.m_IdServicio);
        //parametros.put("m_Referencia",this.m_Referencia);
        //parametros.put("m_DescripEstado",this.m_DescripEstado);

        parametros.put("m_Referencia", Util.stringToHtml(this.m_Referencia));
        parametros.put("m_DescripEstado", Util.stringToHtml(this.m_DescripEstado));
        
        return parametros;
    }

    public String getM_TipoCuenta() {
        return m_TipoCuenta;
    }

    public void setM_TipoCuenta(String m_TipoCuenta) {
        if(m_TipoCuenta != null){
            this.m_TipoCuenta = m_TipoCuenta;
        }else{
            this.m_TipoCuenta = "";
        }
        
    }

    public String getM_IdAprobador() {
        return m_IdAprobador;
    }

    public void setM_IdAprobador(String m_IdAprobador) {
        this.m_IdAprobador = m_IdAprobador;
    }

    public String getM_FecRegistro() {
        return m_FecRegistro;
    }

    public void setM_FecRegistro(String m_FecRegistro) {
        if(m_FecRegistro != null){
            this.m_FecRegistro = m_FecRegistro;
        }else{
            this.m_FecRegistro = "";
        }
        
    }

    public String getM_DescCuenta() {
        return m_DescCuenta;
    }

    public void setM_DescCuenta(String m_DescCuenta) {
        if(m_DescCuenta != null){
            this.m_DescCuenta = m_DescCuenta;
        }else{
            this.m_DescCuenta = "";
        }
        
    }

    public String getM_DescHora() {
        return m_DescHora;
    }

    public void setM_DescHora(String m_DescHora) {
        if(m_DescHora != null){
            this.m_DescHora = m_DescHora;
        }else{
            this.m_DescHora = "";
        }
        
    }

    /**
     * @return the m_CodServicio
     */
    public String getM_CodServicio() {
        return m_CodServicio;
    }

    /**
     * @param m_CodServicio the m_CodServicio to set
     */
    public void setM_CodServicio(String m_CodServicio) {
        this.m_CodServicio = m_CodServicio;
    }

    public String getM_TipoServicio() {
        return m_TipoServicio;
    }

    public void setM_TipoServicio(String m_TipoServicio) {
        this.m_TipoServicio = m_TipoServicio;
    }

    public String getM_IdUsuario() {
        return m_IdUsuario;
    }

    public void setM_IdUsuario(String m_IdUsuario) {
        this.m_IdUsuario = m_IdUsuario;
    }

}


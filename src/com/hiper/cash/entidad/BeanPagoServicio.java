/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

import com.hiper.cash.util.Fecha;

/**
 *
 * @author esilva
 */
public class BeanPagoServicio {
    
    private String m_Empresa = "";
    private String m_Servicio = "";


    private String m_Modulo = "";
    private String m_SubModulo = "";

    //Grupo
    private String m_Grupo = "";
    private String m_NombreGrupo = "";
    private String m_CodTipoEntidad = "";
    //Proveedor
    private String m_Proveedor = "";
    private String m_NombreProveedor = "";
    private String m_CodServProveedor = "";
    private String m_RucProveedor = "";

    //Servicio del Proveedor
    private String m_ServProv = "";
    private String m_NombreServProv = "";
    private String m_CodServServProv = "";
    private String m_NombreTipoServicio = "";
    //Descripcion
    private String m_DescEmpresa = "";

    //Total
    private String m_MontoTotal = "";
    private String m_MontoTotalSoles = "";
    private String m_MontoTotalDolares = "";
    
    //jwong 30/03/2009
    private String m_NumAbonado = ""; //codigo
    private String m_LabelNumAbonado = "";
    private String m_Titulo = "";
    private String m_CuentaCargo = "";
    private String m_CuentaDepositante = "";
    private String m_FecVencimiento = "";
    private String m_Referencia = "";
    private String m_Sector = "";
    private String m_DescSector = "";
    private String m_Moneda = "";

    private String m_MontoMostrar = "";

    public String getM_Modulo() {
        return m_Modulo;
    }

    public void setM_Modulo(String m_Modulo) {
        if(m_Modulo != null){
            this.m_Modulo = m_Modulo;
        }else{
            this.m_Modulo = "";
        }
        
    }

    public String getM_SubModulo() {
        return m_SubModulo;
    }

    public void setM_SubModulo(String m_SubModulo) {
        if(m_SubModulo != null){
            this.m_SubModulo = m_SubModulo;
        }else{
            this.m_SubModulo = "";
        }
        
    }

    public String getM_Grupo() {
        return m_Grupo;
    }

    public void setM_Grupo(String m_Grupo) {
        if(m_Grupo != null){
            this.m_Grupo = m_Grupo;
        }else{
            this.m_Grupo = "";
        }
        
    }

    public String getM_NombreGrupo() {
        return m_NombreGrupo;
    }

    public void setM_NombreGrupo(String m_NombreGrupo) {
        if(m_NombreGrupo != null){
            this.m_NombreGrupo = m_NombreGrupo;
        }else{
            this.m_NombreGrupo = "";
        }
        
    }

    public String getM_Proveedor() {
        return m_Proveedor;
    }

    public void setM_Proveedor(String m_Proveedor) {
        if(m_Proveedor != null){
            this.m_Proveedor = m_Proveedor;
        }else{
            this.m_Proveedor = "";
        }
        
    }

    public String getM_NombreProveedor() {
        return m_NombreProveedor;
    }

    public void setM_NombreProveedor(String m_NombreProveedor) {
        if(m_NombreProveedor != null){
            this.m_NombreProveedor = m_NombreProveedor;
        }else{
            this.m_NombreProveedor = "";
        }
        
    }

    public String getM_Empresa() {
        return m_Empresa;
    }

    public void setM_Empresa(String m_Empresa) {
        if(m_Empresa != null){
            this.m_Empresa = m_Empresa;
        }else{
            this.m_Empresa = "";
        }
        
    }

    public String getM_Servicio() {
        return m_Servicio;
    }

    public void setM_Servicio(String m_Servicio) {
        if(m_Servicio != null){
            this.m_Servicio = m_Servicio;
        }else{
            this.m_Servicio = "";
        }
        
    }

    public String getM_DescEmpresa() {
        return m_DescEmpresa;
    }

    public void setM_DescEmpresa(String m_DescEmpresa) {
        if(m_DescEmpresa != null){
            this.m_DescEmpresa = m_DescEmpresa;
        }else{
            this.m_DescEmpresa = "";
        }
        
    }

    public String getM_ServProv() {
        return m_ServProv;
    }

    public void setM_ServProv(String m_ServProv) {
        if(m_ServProv != null){
            this.m_ServProv = m_ServProv;
        }else{
            this.m_ServProv = "";
        }
        
    }

    public String getM_NombreServProv() {
        return m_NombreServProv;
    }

    public void setM_NombreServProv(String m_NombreServProv) {
        if(m_NombreServProv != null){
            this.m_NombreServProv = m_NombreServProv;
        }else{
            this.m_NombreServProv = "";
        }
        
    }

    public String getM_MontoTotal() {
        return m_MontoTotal;
    }

    public void setM_MontoTotal(String m_MontoTotal) {
        if(m_MontoTotal != null){
            this.m_MontoTotal = m_MontoTotal;
        }else{
            this.m_MontoTotal = "";
        }
        
    }

    /**
     * @return the m_NumAbonado
     */
    public String getM_NumAbonado() {
        return m_NumAbonado;
    }

    /**
     * @param m_NumAbonado the m_NumAbonado to set
     */
    public void setM_NumAbonado(String m_NumAbonado) {
        if(m_NumAbonado != null){
            this.m_NumAbonado = m_NumAbonado;
        }else{
            this.m_NumAbonado = "";
        }
        
    }

    /**
     * @return the m_LabelNumAbonado
     */
    public String getM_LabelNumAbonado() {
        return m_LabelNumAbonado;
    }

    /**
     * @param m_LabelNumAbonado the m_LabelNumAbonado to set
     */
    public void setM_LabelNumAbonado(String m_LabelNumAbonado) {
        if(m_LabelNumAbonado != null){
            this.m_LabelNumAbonado = m_LabelNumAbonado;
        }else{
            this.m_LabelNumAbonado = "";
        }
        
    }

    /**
     * @return the m_Titulo
     */
    public String getM_Titulo() {
        return m_Titulo;
    }

    /**
     * @param m_Titulo the m_Titulo to set
     */
    public void setM_Titulo(String m_Titulo) {
        if(m_Titulo != null){
            this.m_Titulo = m_Titulo;
        }else{
            this.m_Titulo = "";
        }
        
    }

    /**
     * @return the m_CuentaCargo
     */
    public String getM_CuentaCargo() {
        return m_CuentaCargo;
    }

    /**
     * @param m_CuentaCargo the m_CuentaCargo to set
     */
    public void setM_CuentaCargo(String m_CuentaCargo) {
        if(m_CuentaCargo != null){
            this.m_CuentaCargo = m_CuentaCargo;
        }else{
            this.m_CuentaCargo = "";
        }
        
    }

    /**
     * @return the m_FecVencimiento
     */
    public String getM_FecVencimiento() {
        return m_FecVencimiento;
    }

    /**
     * @param m_FecVencimiento the m_FecVencimiento to set
     */
    public void setM_FecVencimiento(String m_FecVencimiento) {
        if(m_FecVencimiento != null){
            this.m_FecVencimiento = m_FecVencimiento;
        }else{
            this.m_FecVencimiento = "";
        }
        
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
        if(m_Referencia != null){
            this.m_Referencia = m_Referencia;
        }else{
            this.m_Referencia = "";
        }
        
    }

    /**
     * @return the m_Sector
     */
    public String getM_Sector() {
        return m_Sector;
    }

    /**
     * @param m_Sector the m_Sector to set
     */
    public void setM_Sector(String m_Sector) {
        if(m_Sector != null){
            this.m_Sector = m_Sector;
        }else{
            this.m_Sector = "";
        }
        
    }

    /**
     * @return the m_DescSector
     */
    public String getM_DescSector() {
        return m_DescSector;
    }

    /**
     * @param m_DescSector the m_DescSector to set
     */
    public void setM_DescSector(String m_DescSector) {
        if(m_DescSector != null){
            this.m_DescSector = m_DescSector;
        }else{
            this.m_DescSector = "";
        }
        
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
        if(m_Moneda != null){
            this.m_Moneda = m_Moneda;
        }else{
            this.m_Moneda = "";
        }
        
    }

    /**
     * @return the m_CuentaDepositante
     */
    public String getM_CuentaDepositante() {
        return m_CuentaDepositante;
    }

    /**
     * @param m_CuentaDepositante the m_CuentaDepositante to set
     */
    public void setM_CuentaDepositante(String m_CuentaDepositante) {
        if(m_CuentaDepositante != null){
            this.m_CuentaDepositante = m_CuentaDepositante;
        }else{
            this.m_CuentaDepositante = "";
        }
        
    }

    public String getM_CodServProveedor() {
        return m_CodServProveedor;
    }

    public void setM_CodServProveedor(String m_CodServProveedor) {
        if(m_CodServProveedor != null){
            this.m_CodServProveedor = m_CodServProveedor;
        }else{
            this.m_CodServProveedor = "";
        }
        
    }

    public String getM_CodServServProv() {
        return m_CodServServProv;
    }

    public void setM_CodServServProv(String m_CodServServProv) {
        if(m_CodServServProv != null){
            this.m_CodServServProv = m_CodServServProv;
        }else{
            this.m_CodServServProv = "";
        }

    }

    public String getM_CodTipoEntidad() {
        return m_CodTipoEntidad;
    }

    public void setM_CodTipoEntidad(String m_CodTipoEntidad) {
        if(m_CodTipoEntidad != null){
            this.m_CodTipoEntidad = m_CodTipoEntidad;
        }else{
            this.m_CodTipoEntidad = "";
        }

    }

    public String getM_RucProveedor() {
        return m_RucProveedor;
    }

    public void setM_RucProveedor(String m_RucProveedor) {
        if(m_RucProveedor != null){
            this.m_RucProveedor = m_RucProveedor;
        }else{
            this.m_RucProveedor = "";
        }

    }

/**
     * @return the m_MontoTotalSoles
     */
    public String getM_MontoTotalSoles() {
        return m_MontoTotalSoles;
    }

    /**
     * @param m_MontoTotalSoles the m_MontoTotalSoles to set
     */
    public void setM_MontoTotalSoles(String m_MontoTotalSoles) {
        if(m_MontoTotalSoles != null){
            this.m_MontoTotalSoles = m_MontoTotalSoles;
        }else{
            this.m_MontoTotalSoles = "";
        }

    }

    /**
     * @return the m_MontoTotalDolares
     */
    public String getM_MontoTotalDolares() {
        return m_MontoTotalDolares;


    }

    /**
     * @param m_MontoTotalDolares the m_MontoTotalDolares to set
     */
    public void setM_MontoTotalDolares(String m_MontoTotalDolares) {
        if(m_MontoTotalDolares != null){
            this.m_MontoTotalDolares = m_MontoTotalDolares;
        }else{
            this.m_MontoTotalDolares = "";
        }

    }
    public String getM_FecVencFormateada() {
        return Fecha.formatearFecha("yyyyMMdd", "dd/MM/yyyy", m_FecVencimiento);
    }

    public String getM_MontoMostrar() {
        return m_MontoMostrar;
    }

    public void setM_MontoMostrar(String m_MontoMostrar) {
        this.m_MontoMostrar = m_MontoMostrar;
    }

    public void setM_NombreTipoServicio(String mNombreTipoServicio) {
		m_NombreTipoServicio = mNombreTipoServicio;
	}
    
    public String getM_NombreTipoServicio() {
		return m_NombreTipoServicio;
	}

}

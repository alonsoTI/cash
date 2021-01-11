/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

import com.hiper.cash.util.Fecha;

/**
 *
 * @author jwong
 */
public class BeanConsPagoServicio {
    private String m_NumRecibo; //codigo
    private String m_FechaEmision;
    private String m_NombreCliente;

    private String m_Moneda;
    private String m_Importe;
    private String m_ImporteMostrar;

    private String m_CodEmpresa;
    private String m_NombreServicio;


    //jwong 23/03/2009 nuevos campos
    private String m_EntityType;
    private String m_EntityCode;
    private String m_ServiceCode;
    private String m_NumberPaid;


    //jwong 31/03/2009
    private String m_EstadoDeuda;
    private String m_Descripcion;
    private String m_TipoDocumento;
    private String m_NumeroDocumento;
    private String m_FechaVencimiento;
    private String m_Referencia;

    //jwong 14/08/2009
    private String m_DescrMoneda;
    
    /**
     * @return the m_NumRecibo
     */
    public String getM_NumRecibo() {
        return m_NumRecibo;
    }

    /**
     * @param m_NumRecibo the m_NumRecibo to set
     */
    public void setM_NumRecibo(String m_NumRecibo) {
        this.m_NumRecibo = m_NumRecibo;
    }

    /**
     * @return the m_FechaEmision
     */
    public String getM_FechaEmision() {
        return m_FechaEmision;
    }

    /**
     * @param m_FechaEmision the m_FechaEmision to set
     */
    public void setM_FechaEmision(String m_FechaEmision) {
        this.m_FechaEmision = m_FechaEmision;
    }

    /**
     * @return the m_NombreCliente
     */
    public String getM_NombreCliente() {
        return m_NombreCliente;
    }

    /**
     * @param m_NombreCliente the m_NombreCliente to set
     */
    public void setM_NombreCliente(String m_NombreCliente) {
        this.m_NombreCliente = m_NombreCliente;
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

    public java.util.Map getParametrosUrl(){
        java.util.Map parametros = new java.util.HashMap();
        parametros.put("m_NumRecibo",this.m_NumRecibo);
        parametros.put("m_CodEmpresa",this.m_CodEmpresa);
        parametros.put("m_NombreServicio", this.m_NombreServicio);

        parametros.put("m_Moneda", this.m_Moneda);
        parametros.put("m_FechaEmision", this.m_FechaEmision);
        parametros.put("m_NombreCliente", this.m_NombreCliente);
        parametros.put("m_Importe", this.m_Importe);
        
        return parametros;
    }

    /**
     * @return the m_CodEmpresa
     */
    public String getM_CodEmpresa() {
        return m_CodEmpresa;
    }

    /**
     * @param m_CodEmpresa the m_CodEmpresa to set
     */
    public void setM_CodEmpresa(String m_CodEmpresa) {
        this.m_CodEmpresa = m_CodEmpresa;
    }

    //jwong 16/03/2009
    public String getParametrosCadena(){
        String cadena = this.m_NumRecibo + ";" +
                        this.m_CodEmpresa + ";" +
                        this.m_NombreServicio + ";" +
                        this.m_Moneda + ";" +
                        this.m_FechaEmision + ";" +
                        this.m_NombreCliente + ";" +
                        this.m_Importe;

        return cadena;
    }

    //jwong 16/03/2009
    public String getParametrosCadenaClaro(){
        String cadena = this.m_NumRecibo + ";" +
                        this.m_Descripcion + ";" +
                        this.m_Moneda + ";" +
                        this.m_EstadoDeuda + ";" +
                        this.m_TipoDocumento + ";" +
                        this.m_NumeroDocumento + ";" +
                        this.m_FechaEmision + ";" +
                        this.m_FechaVencimiento + ";" +
                        this.m_Referencia + ";" +
                        this.m_Importe + ";" +
                        this.m_CodEmpresa + ";" +
                        this.m_NombreServicio;

        return cadena;
    }
    
    /**
     * @return the m_NombreServicio
     */
    public String getM_NombreServicio() {
        return m_NombreServicio;
    }

    /**
     * @param m_NombreServicio the m_NombreServicio to set
     */
    public void setM_NombreServicio(String m_NombreServicio) {
        this.m_NombreServicio = m_NombreServicio;
    }

    /**
     * @return the m_EntityType
     */
    public String getM_EntityType() {
        return m_EntityType;
    }

    /**
     * @param m_EntityType the m_EntityType to set
     */
    public void setM_EntityType(String m_EntityType) {
        this.m_EntityType = m_EntityType;
    }

    /**
     * @return the m_EntityCode
     */
    public String getM_EntityCode() {
        return m_EntityCode;
    }

    /**
     * @param m_EntityCode the m_EntityCode to set
     */
    public void setM_EntityCode(String m_EntityCode) {
        this.m_EntityCode = m_EntityCode;
    }

    /**
     * @return the m_ServiceCode
     */
    public String getM_ServiceCode() {
        return m_ServiceCode;
    }

    /**
     * @param m_ServiceCode the m_ServiceCode to set
     */
    public void setM_ServiceCode(String m_ServiceCode) {
        this.m_ServiceCode = m_ServiceCode;
    }

    /**
     * @return the m_NumberPaid
     */
    public String getM_NumberPaid() {
        return m_NumberPaid;
    }

    /**
     * @param m_NumberPaid the m_NumberPaid to set
     */
    public void setM_NumberPaid(String m_NumberPaid) {
        this.m_NumberPaid = m_NumberPaid;
    }

    /**
     * @return the m_EstadoDeuda
     */
    public String getM_EstadoDeuda() {
        return m_EstadoDeuda;
    }

    /**
     * @param m_EstadoDeuda the m_EstadoDeuda to set
     */
    public void setM_EstadoDeuda(String m_EstadoDeuda) {
        this.m_EstadoDeuda = m_EstadoDeuda;
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
     * @return the m_TipoDocumento
     */
    public String getM_TipoDocumento() {
        return m_TipoDocumento;
    }

    /**
     * @param m_TipoDocumento the m_TipoDocumento to set
     */
    public void setM_TipoDocumento(String m_TipoDocumento) {
        this.m_TipoDocumento = m_TipoDocumento;
    }

    /**
     * @return the m_NumeroDocumento
     */
    public String getM_NumeroDocumento() {
        return m_NumeroDocumento;
    }

    /**
     * @param m_NumeroDocumento the m_NumeroDocumento to set
     */
    public void setM_NumeroDocumento(String m_NumeroDocumento) {
        this.m_NumeroDocumento = m_NumeroDocumento;
    }

    /**
     * @return the m_FechaVencimiento
     */
    public String getM_FechaVencimiento() {
        return m_FechaVencimiento;
    }

    /**
     * @param m_FechaVencimiento the m_FechaVencimiento to set
     */
    public void setM_FechaVencimiento(String m_FechaVencimiento) {
        this.m_FechaVencimiento = m_FechaVencimiento;
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
//jwong 04/04/2009 manejo de la fecha formateada
    public String getM_FecVencFormateada() {
        return Fecha.formatearFecha("yyyyMMdd", "dd/MM/yyyy", m_FechaVencimiento);
    }
    public String getM_FecEmisFormateada() {
        return Fecha.formatearFecha("yyyyMMdd", "dd/MM/yyyy", m_FechaEmision);
    }

    /**
     * @return the m_DescrMoneda
     */
    public String getM_DescrMoneda() {
        return m_DescrMoneda;
    }

    /**
     * @param m_DescrMoneda the m_DescrMoneda to set
     */
    public void setM_DescrMoneda(String m_DescrMoneda) {
        this.m_DescrMoneda = m_DescrMoneda;
    }

    public String getM_ImporteMostrar() {
        return m_ImporteMostrar;
    }

    public void setM_ImporteMostrar(String m_ImporteMostrar) {
        this.m_ImporteMostrar = m_ImporteMostrar;
    }
}
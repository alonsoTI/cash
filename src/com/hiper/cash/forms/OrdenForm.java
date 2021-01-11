/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.forms;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import fr.improve.struts.taglib.layout.util.DefaultForm;
/**
 *
 * @author esilva
 */
public class OrdenForm extends DefaultForm{

    private String dorReferencia;
    private String norCuentaCargo;
    private String forFechaInicio;
    private String forFechaFin;

    private String m_IdOrden;
    private String m_IdEmpresa;
    private String m_IdServicio;

    
    private Map nombres = new HashMap();
    private Map cuentas = new HashMap();
    private Map emails = new HashMap();
    private Map telefs = new HashMap();
    private Map documentos = new HashMap();
    private Map values = new HashMap();
    private Map tipocuentas = new HashMap();
    private Map tipomonedas = new HashMap();
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    	////System.out.println("Entro al Form Action 	");
    	
    	////System.out.println("idOrden	"	+ this.m_IdOrden);
    	////System.out.println("idEmpresa	"	+ this.m_IdEmpresa);
    	////System.out.println("idServicio	"	+ this.m_IdServicio );
    	
        return super.validate(mapping, request);
    }

    public String getDorReferencia() {
        return dorReferencia;
    }

    public void setDorReferencia(String dorReferencia) {
        this.dorReferencia = dorReferencia;
    }

    public String getNorCuentaCargo() {
        return norCuentaCargo;
    }

    public void setNorCuentaCargo(String norCuentaCargo) {
        this.norCuentaCargo = norCuentaCargo;
    }

    public String getForFechaInicio() {
        return forFechaInicio;
    }

    public void setForFechaInicio(String forFechaInicio) {
        this.forFechaInicio = forFechaInicio;
    }

    public String getForFechaFin() {
        return forFechaFin;
    }

    public void setForFechaFin(String forFechaFin) {
        this.forFechaFin = forFechaFin;
    }

    public String getDocumento(String in_key) {
        return (String) getDocumentos().get(in_key);
    }

    public void setDocumento(String in_key, String in_value) {
        this.getDocumentos().put(in_key, in_value);
    }

    public String getNombre(String in_key) {
        return (String) nombres.get(in_key);
    }

    public void setNombre(String in_key, String in_value) {
        this.nombres.put(in_key, in_value);
    }
     public String getCunta(String in_key) {
        return (String) cuentas.get(in_key);
    }

    public void setCunta(String in_key, String in_value) {
        this.cuentas.put(in_key, in_value);
    }

    public String getValue(String in_key) {
        return (String) values.get(in_key);
    }

    public void setValue(String in_key, String in_value) {
        this.values.put(in_key, in_value);
    }

    public Map getDocumentos(){
        return this.documentos;
    }
    public Map getValues(){
        return this.values;
    }
    public void setValues(){
        this.values = new HashMap();
    }
    public void setDocumentos(){
        this.setDocumentos(new HashMap());
    }
    public Map getNombres(){
        return this.nombres;
    }
    public void setNombres(){
        this.nombres = new HashMap();
    }
    public Map getCuentas(){
        return this.cuentas;
    }
    public void setCuentas(){
        this.cuentas = new HashMap();
    }
    public Map getTipoCuentas(){
        return this.tipocuentas;
    }
    public void setTipoCuentas(){
        this.tipocuentas = new HashMap();
    }
    public String getTipoCunta(String in_key) {
        return (String) tipocuentas.get(in_key);
    }

    public void setTipoCunta(String in_key, String in_value) {
        this.tipocuentas.put(in_key, in_value);
    }
    public Map getTipoMonedas(){
        return this.tipomonedas;
    }
    public void setTipoMonedas(){
        this.tipomonedas = new HashMap();
    }
    public String getTipoMoneda(String in_key) {
        return (String) tipomonedas.get(in_key);
    }

    public void setTipoMoneda(String in_key, String in_value) {
        this.tipomonedas.put(in_key, in_value);
    }

    /**
     * @return the m_IdOrden
     */
    public String getM_IdOrden() {
        return m_IdOrden;
    }

    /**
     * @param m_IdOrden the m_IdOrden to set
     */
    public void setM_IdOrden(String m_IdOrden) {
        this.m_IdOrden = m_IdOrden;
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

    //Email
    public String getEmail(String in_key) {
        return (String) emails.get(in_key);
    }
    public void setEmail(String in_key, String in_value) {
        this.emails.put(in_key, in_value);
    }
    public Map getEmails(){
        return this.emails;
    }
    public void setEmails(){
        this.emails = new HashMap();
    }
    //Telefono
    public String getTelef(String in_key) {
        return (String) telefs.get(in_key);
    }
    public void setTelef(String in_key, String in_value) {
        this.telefs.put(in_key, in_value);
    }
    public Map getTelefs(){
        return this.telefs;
    }
    public void setTelefs(){
        this.telefs = new HashMap();
    }

    /**
     * @param documentos the documentos to set
     */
    public void setDocumentos(Map documentos) {
        this.documentos = documentos;
    }
    
}

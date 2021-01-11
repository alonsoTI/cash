/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import fr.improve.struts.taglib.layout.util.DefaultForm;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author esilva
 */
public class PagoServicioForm extends DefaultForm {

    private String m_Empresa;
    private String m_Servicio;

    private String m_Proveedor;
    private String m_ServProv;
    private String m_Codigo;
    private String m_Nombre;

    private String m_CuentaCargo;

    private Map values = new HashMap();
    
    private Map criterios = new HashMap();


    //JWONG
    private String m_NumAbonado;
    private String m_Referencia;
    private String m_Importe;
    private String m_FecVencimiento;
    private String m_Sector;
    //jmoreno
    private String idProveedor;


    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public String getM_Servicio() {
        return m_Servicio;
    }

    public void setM_Servicio(String m_Servicio) {
        this.m_Servicio = m_Servicio;
    }

    public String getM_Codigo() {
        return m_Codigo;
    }

    public void setM_Codigo(String m_Codigo) {
        this.m_Codigo = m_Codigo;
    }

    public String getM_Nombre() {
        return m_Nombre;
    }

    public void setM_Nombre(String m_Nombre) {
        this.m_Nombre = m_Nombre;
    }

    public String getM_Proveedor() {
        return m_Proveedor;
    }

    public void setM_Proveedor(String m_Proveedor) {
        this.m_Proveedor = m_Proveedor;
    }

    //Map
    public String getValue(String in_key) {
        return (String) values.get(in_key);
    }
    public void setValue(String in_key, String in_value) {
        this.values.put(in_key, in_value);
    }
    public Map getValues(){
        return this.values;
    }
    public void setValues(){
        this.values = new HashMap();
    }

    public String getM_Empresa() {
        return m_Empresa;
    }

    public void setM_Empresa(String m_Empresa) {
        this.m_Empresa = m_Empresa;
    }

    public String getM_ServProv() {
        return m_ServProv;
    }

    public void setM_ServProv(String m_ServProv) {
        this.m_ServProv = m_ServProv;
    }

    public String getM_CuentaCargo() {
        return m_CuentaCargo;
    }

    public void setM_CuentaCargo(String m_CuentaCargo) {
        this.m_CuentaCargo = m_CuentaCargo;
    }

    public String getM_Referencia() {
        return m_Referencia;
    }

    public void setM_Referencia(String m_Referencia) {
        this.m_Referencia = m_Referencia;
    }

    public String getM_Importe() {
        return m_Importe;
    }

    public void setM_Importe(String m_Importe) {
        this.m_Importe = m_Importe;
    }

    public String getCriterio(String in_key) {
        return (String) criterios.get(in_key);
    }
    public void setCriterio(String in_key, String in_value) {
        this.criterios.put(in_key, in_value);
    }
    public Map getCriterios(){
        return this.criterios;
    }
    public void setCriterios(){
        this.criterios = new HashMap();
    }

    //JWONG
    public String getM_NumAbonado() {
        return m_NumAbonado;
    }
    public void setM_NumAbonado(String m_NumAbonado) {
        this.m_NumAbonado = m_NumAbonado;
    }
    public String getM_FecVencimiento() {
        return m_FecVencimiento;
    }
    public void setM_FecVencimiento(String m_FecVencimiento) {
        this.m_FecVencimiento = m_FecVencimiento;
    }
    public String getM_Sector() {
        return m_Sector;
    }
    public void setM_Sector(String m_Sector) {
        this.m_Sector = m_Sector;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }
}

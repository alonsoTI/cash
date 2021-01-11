/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.forms;

import com.hiper.cash.util.Fecha;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;

import fr.improve.struts.taglib.layout.datagrid.Datagrid;

import fr.improve.struts.taglib.layout.util.DefaultForm;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author esilva
 */
public class ListaOrdenForm extends DefaultForm {

    private String empresa;
    private String servicio;
    private String cuenta;
    private String sobregiro;
    private String referencia;
    private String tipoingreso;
    private String fechaInicial;
    private String fechaFinal;
    private String fechaActualComp;
    private String fechaActualMax;
    private String horaVigencia;
    private Datagrid subjects = null;

    private Map titles = new HashMap();
    private Map values = new HashMap();

    private String control;

    public String getTitle(String in_key) {
		return (String) titles.get(in_key);
	}
	public void setTitle(String in_key, String in_value) {
		titles.put(in_key, in_value);
	}

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    /**
     * @return the empresa
     */
    public String getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the servicio
     */
    public String getServicio() {
        return servicio;
    }

    /**
     * @param servicio the servicio to set
     */
    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    /**
     * @return the cuenta
     */
    public String getCuenta() {
        return cuenta;
    }

    /**
     * @param cuenta the cuenta to set
     */
    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    /**
     * @return the sobregiro
     */
    public String getSobregiro() {
        return sobregiro;
    }

    /**
     * @param sobregiro the sobregiro to set
     */
    public void setSobregiro(String sobregiro) {
        this.sobregiro = sobregiro;
    }

    /**
     * @return the referencia
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * @param referencia the referencia to set
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * @return the tipoingreso
     */
    public String getTipoingreso() {
        return tipoingreso;
    }

    /**
     * @param tipoingreso the tipoingreso to set
     */
    public void setTipoingreso(String tipoingreso) {
        this.tipoingreso = tipoingreso;
    }

    /**
     * @return the fechaInicial
     */
    public String getFechaInicial() {
        return fechaInicial;
    }

    /**
     * @param fechaInicial the fechaInicial to set
     */
    public void setFechaInicial(String fechaInicial) {
        /*if (fechaInicial == null || fechaInicial.length() == 0 )
            this.fechaInicial = "";
        else
            this.fechaInicial = Fecha.convertToFechaSQL(fechaInicial);*/
        this.fechaInicial = fechaInicial;
    }

    /**
     * @return the fechaFinal
     */
    public String getFechaFinal() {
        return fechaFinal;
    }

    /**
     * @param fechaFinal the fechaFinal to set
     */
    public void setFechaFinal(String fechaFinal) {
        /*if (fechaFinal == null || fechaFinal.length() == 0 )
            this.fechaFinal = "";
        else
            this.fechaFinal = Fecha.convertToFechaSQL(fechaFinal);*/
        this.fechaFinal = fechaFinal;
    }

    public Datagrid getSubjects() {
		return subjects;
	}

	public void setSubjects(Datagrid datagrid) {
		subjects = datagrid;
	}

    public String getValue(String in_key) {
        return (String) values.get(in_key);
    }

    public void setValue(String in_key, String in_value) {
        this.values.put(in_key, in_value);
    }

    public Map getValues(){
        return this.values;
    }
    public void setValues() {
		values = new HashMap();
	}

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getHoraVigencia() {
        return horaVigencia;
    }

    public void setHoraVigencia(String horaVigencia) {
        this.horaVigencia = horaVigencia;
    }

    public String getFechaActualComp() {
        return fechaActualComp;
    }

    public void setFechaActualComp(String fechaActualComp) {
        this.fechaActualComp = fechaActualComp;
    }

    public String getFechaActualMax() {
        return fechaActualMax;
    }

    public void setFechaActualMax(String fechaActualMax) {
        this.fechaActualMax = fechaActualMax;
    }

   
}

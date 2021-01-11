/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.financiero.cash.form;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

/**
 *
 * @author ANDQUI
 */
public class InputForm extends org.apache.struts.action.ActionForm {

    private String empresa;
    private String servicio;
    private List empresaList;
    private List servicioList;
    private List ordenesList;
    private List detallesList;

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        // if (getName() == null || getName().length() < 1) {
        //  errors.add("name", new ActionMessage("error.name.required"));
        // TODO: add 'error.name.required' key to your resources
        //}
        return errors;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public List getEmpresaList() {
        return empresaList;
    }

    public void setEmpresaList(List empresaList) {
        this.empresaList = empresaList;
    }

    public List getServicioList() {
        return servicioList;
    }

    public void setServicioList(List servicioList) {
        this.servicioList = servicioList;
    }

    public List getOrdenesList() {
        return ordenesList;
    }

    public void setOrdenesList(List ordenesList) {
        this.ordenesList = ordenesList;
    }

    public List getDetallesList() {
        return detallesList;
    }

    public void setDetallesList(List detallesList) {
        this.detallesList = detallesList;
    }
}

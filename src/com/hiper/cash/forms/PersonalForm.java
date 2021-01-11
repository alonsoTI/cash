/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author jwong
 */
public class PersonalForm extends ActionForm {
    private String m_Empresa;
    private String m_IdServEmp;//jmoreno 29/12/09    
    private String m_IdEmpleado;
    private String m_Nombre;
    private String m_DNI;
    private String m_NroCelular;
    private String m_Email;
    
    //Pago Planilla:
    private String m_FormaPagoPla;
    private String m_TipoCuentaPla; //(Ahorros, Cuenta Corriente)
    private String m_MonedaPla; //(Soles, Dólares)
    private String m_NroCuentaPla;
    private String m_ImportePla;

    //Pago CTS;
    private String m_FormaPagoCTS;
    private String m_TipoCuentaCTS; //(Ahorros, Cuenta Corriente)
    private String m_MonedaCTS; //(Soles, Dólares)
    private String m_NroCuentaCTS;
    private String m_ImporteCTS;

    //jwong 12/02/2009 para mantener la empresa y el id de proveedor original en caso se quiera modificar
    private String m_EmpresaOriginal;
    private String m_IdEmpleadoOriginal;

    //jwong 23/04/2009 nuevo campo contrapartida
    private String m_Contrapartida;


    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    /**
     * @return the m_IdEmpleado
     */
    public String getM_IdEmpleado() {
        return m_IdEmpleado;
    }

    /**
     * @param m_IdEmpleado the m_IdEmpleado to set
     */
    public void setM_IdEmpleado(String m_IdEmpleado) {
        this.m_IdEmpleado = m_IdEmpleado;
    }

    /**
     * @return the m_Nombre
     */
    public String getM_Nombre() {
        return m_Nombre;
    }

    /**
     * @param m_Nombre the m_Nombre to set
     */
    public void setM_Nombre(String m_Nombre) {
        this.m_Nombre = m_Nombre;
    }

    /**
     * @return the m_DNI
     */
    public String getM_DNI() {
        return m_DNI;
    }

    /**
     * @param m_DNI the m_DNI to set
     */
    public void setM_DNI(String m_DNI) {
        this.m_DNI = m_DNI;
    }

    /**
     * @return the m_NroCelular
     */
    public String getM_NroCelular() {
        return m_NroCelular;
    }

    /**
     * @param m_NroCelular the m_NroCelular to set
     */
    public void setM_NroCelular(String m_NroCelular) {
        this.m_NroCelular = m_NroCelular;
    }

    /**
     * @return the m_Email
     */
    public String getM_Email() {
        return m_Email;
    }

    /**
     * @param m_Email the m_Email to set
     */
    public void setM_Email(String m_Email) {
        this.m_Email = m_Email;
    }

        /**
     * @return the m_TipoCuentaPla
     */
    public String getM_TipoCuentaPla() {
        return m_TipoCuentaPla;
    }

    /**
     * @param m_TipoCuentaPla the m_TipoCuentaPla to set
     */
    public void setM_TipoCuentaPla(String m_TipoCuentaPla) {
        this.m_TipoCuentaPla = m_TipoCuentaPla;
    }

    /**
     * @return the m_MonedaPla
     */
    public String getM_MonedaPla() {
        return m_MonedaPla;
    }

    /**
     * @param m_MonedaPla the m_MonedaPla to set
     */
    public void setM_MonedaPla(String m_MonedaPla) {
        this.m_MonedaPla = m_MonedaPla;
    }

    /**
     * @return the m_NroCuentaPla
     */
    public String getM_NroCuentaPla() {
        return m_NroCuentaPla;
    }

    /**
     * @param m_NroCuentaPla the m_NroCuentaPla to set
     */
    public void setM_NroCuentaPla(String m_NroCuentaPla) {
        this.m_NroCuentaPla = m_NroCuentaPla;
    }

    /**
     * @return the m_ImportePla
     */
    public String getM_ImportePla() {
        return m_ImportePla;
    }

    /**
     * @param m_ImportePla the m_ImportePla to set
     */
    public void setM_ImportePla(String m_ImportePla) {
        this.m_ImportePla = m_ImportePla;
    }

    /**
     * @return the m_TipoCuentaCTS
     */
    public String getM_TipoCuentaCTS() {
        return m_TipoCuentaCTS;
    }

    /**
     * @param m_TipoCuentaCTS the m_TipoCuentaCTS to set
     */
    public void setM_TipoCuentaCTS(String m_TipoCuentaCTS) {
        this.m_TipoCuentaCTS = m_TipoCuentaCTS;
    }

    /**
     * @return the m_MonedaCTS
     */
    public String getM_MonedaCTS() {
        return m_MonedaCTS;
    }

    /**
     * @param m_MonedaCTS the m_MonedaCTS to set
     */
    public void setM_MonedaCTS(String m_MonedaCTS) {
        this.m_MonedaCTS = m_MonedaCTS;
    }

    /**
     * @return the m_NRoCuentaCTS
     */
    public String getM_NroCuentaCTS() {
        return m_NroCuentaCTS;
    }

    /**
     * @param m_NRoCuentaCTS the m_NRoCuentaCTS to set
     */
    public void setM_NroCuentaCTS(String m_NroCuentaCTS) {
        this.m_NroCuentaCTS = m_NroCuentaCTS;
    }

    /**
     * @return the m_ImporteCTS
     */
    public String getM_ImporteCTS() {
        return m_ImporteCTS;
    }

    /**
     * @param m_ImporteCTS the m_ImporteCTS to set
     */
    public void setM_ImporteCTS(String m_ImporteCTS) {
        this.m_ImporteCTS = m_ImporteCTS;
    }

    /**
     * @return the m_Empresa
     */
    public String getM_Empresa() {
        return m_Empresa;
    }

    /**
     * @param m_Empresa the m_Empresa to set
     */
    public void setM_Empresa(String m_Empresa) {
        this.m_Empresa = m_Empresa;
    }

    /**
     * @return the m_EmpresaOriginal
     */
    public String getM_EmpresaOriginal() {
        return m_EmpresaOriginal;
    }

    /**
     * @param m_EmpresaOriginal the m_EmpresaOriginal to set
     */
    public void setM_EmpresaOriginal(String m_EmpresaOriginal) {
        this.m_EmpresaOriginal = m_EmpresaOriginal;
    }

    /**
     * @return the m_IdEmpleadoOriginal
     */
    public String getM_IdEmpleadoOriginal() {
        return m_IdEmpleadoOriginal;
    }

    /**
     * @param m_IdEmpleadoOriginal the m_IdEmpleadoOriginal to set
     */
    public void setM_IdEmpleadoOriginal(String m_IdEmpleadoOriginal) {
        this.m_IdEmpleadoOriginal = m_IdEmpleadoOriginal;
    }

    public String getM_FormaPagoPla() {
        return m_FormaPagoPla;
    }

    public void setM_FormaPagoPla(String m_FormaPagoPla) {
        this.m_FormaPagoPla = m_FormaPagoPla;
    }

    public String getM_FormaPagoCTS() {
        return m_FormaPagoCTS;
    }

    public void setM_FormaPagoCTS(String m_FormaPagoCTS) {
        this.m_FormaPagoCTS = m_FormaPagoCTS;
    }

    /**
     * @return the m_Contrapartida
     */
    public String getM_Contrapartida() {
        return m_Contrapartida;
    }

    /**
     * @param m_Contrapartida the m_Contrapartida to set
     */
    public void setM_Contrapartida(String m_Contrapartida) {
        this.m_Contrapartida = m_Contrapartida;
    }

    public String getM_IdServEmp() {
        return m_IdServEmp;
    }

    public void setM_IdServEmp(String m_IdServEmp) {
        this.m_IdServEmp = m_IdServEmp;
    }
}

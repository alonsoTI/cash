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
public class ProveedorForm extends ActionForm {
    private String m_Empresa;
    private String m_IdServEmp;//jmoreno 22/12/09
    private String m_IdProveedor;
    private String m_Nombre;
    private String m_TipoDocumento; //(DNI, RUC);
    private String m_NroDocumento;
    private String m_FormaPago;
    //forma de pago:
    // * Cheque de Gerencia
    // * Abono en Cuenta Financiero
    // * Abono en Cuenta otro Banco

    //Si es Abono Cuenta Financiero:
    private String m_TipoCuenta; //(Ahorros, Cuenta Corriente)
    //private String m_MonedaFinan; //(Soles, Dólares)
    private String m_NroCuenta;

    //Si es Cuenta otro Banco:
    private String m_Banco;
    //private String m_MonedaOtrBco; //(Soles, Dólares)
    private String m_NroCuentaCCI;

    //jwong 12/02/2009 para mantener la empresa y el id de proveedor original en caso se quiera modificar
    private String m_EmpresaOriginal;
    private String m_IdProveedorOriginal;

    //jwong 23/04/2009 nuevo campo contrapartida
    private String m_Contrapartida;

    //jwong 23/04/2009 nuevo campo importe
    private String m_Moneda;
    private String m_Importe;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    /**
     * @return the m_IdProveedor
     */
    public String getM_IdProveedor() {
        return m_IdProveedor;
    }

    /**
     * @param m_IdProveedor the m_IdProveedor to set
     */
    public void setM_IdProveedor(String m_IdProveedor) {
        this.m_IdProveedor = m_IdProveedor;
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
     * @return the m_NroDocumento
     */
    public String getM_NroDocumento() {
        return m_NroDocumento;
    }

    /**
     * @param m_NroDocumento the m_NroDocumento to set
     */
    public void setM_NroDocumento(String m_NroDocumento) {
        this.m_NroDocumento = m_NroDocumento;
    }

    /**
     * @return the m_FormaPago
     */
    public String getM_FormaPago() {
        return m_FormaPago;
    }

    /**
     * @param m_FormaPago the m_FormaPago to set
     */
    public void setM_FormaPago(String m_FormaPago) {
        this.m_FormaPago = m_FormaPago;
    }

    /**
     * @return the m_TipoCuenta
     */
    public String getM_TipoCuenta() {
        return m_TipoCuenta;
    }

    /**
     * @param m_TipoCuenta the m_TipoCuenta to set
     */
    public void setM_TipoCuenta(String m_TipoCuenta) {
        this.m_TipoCuenta = m_TipoCuenta;
    }
    /*
    public String getM_MonedaFinan() {
        return m_MonedaFinan;
    }
    public void setM_MonedaFinan(String m_MonedaFinan) {
        this.m_MonedaFinan = m_MonedaFinan;
    }
    */
    /**
     * @return the m_NroCuenta
     */
    public String getM_NroCuenta() {
        return m_NroCuenta;
    }

    /**
     * @param m_NroCuenta the m_NroCuenta to set
     */
    public void setM_NroCuenta(String m_NroCuenta) {
        this.m_NroCuenta = m_NroCuenta;
    }

    /**
     * @return the m_Banco
     */
    public String getM_Banco() {
        return m_Banco;
    }

    /**
     * @param m_Banco the m_Banco to set
     */
    public void setM_Banco(String m_Banco) {
        this.m_Banco = m_Banco;
    }
    /*
    public String getM_MonedaOtrBco() {
        return m_MonedaOtrBco;
    }
    public void setM_MonedaOtrBco(String m_MonedaOtrBco) {
        this.m_MonedaOtrBco = m_MonedaOtrBco;
    }
    */
    /**
     * @return the m_NroCuentaCCI
     */
    public String getM_NroCuentaCCI() {
        return m_NroCuentaCCI;
    }

    /**
     * @param m_NroCuentaCCI the m_NroCuentaCCI to set
     */
    public void setM_NroCuentaCCI(String m_NroCuentaCCI) {
        this.m_NroCuentaCCI = m_NroCuentaCCI;
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
     * @return the m_IdProveedorOriginal
     */
    public String getM_IdProveedorOriginal() {
        return m_IdProveedorOriginal;
    }

    /**
     * @param m_IdProveedorOriginal the m_IdProveedorOriginal to set
     */
    public void setM_IdProveedorOriginal(String m_IdProveedorOriginal) {
        this.m_IdProveedorOriginal = m_IdProveedorOriginal;
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

    public String getM_IdServEmp() {
        return m_IdServEmp;
    }

    public void setM_IdServEmp(String m_IdServEmp) {
        this.m_IdServEmp = m_IdServEmp;
    }
}
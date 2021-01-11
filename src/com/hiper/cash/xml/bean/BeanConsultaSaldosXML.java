/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

/**
 *
 * @author jwong
 */
public class BeanConsultaSaldosXML {
    private String m_Cuenta;
    private String m_Moneda;
    private String m_SaldoDisponible;
    private String m_SaldoRetenido;
    private String m_SaldoContable;

    //jwong 16/01/2009 nuevos campos
    private String m_TipoCuenta;
    //private String m_TipoInformacion; //Cargo, Abono

    //jwong 19/01/2009 signos de los montos
    private String m_SignoDisponible;
    private String m_SignoRetenido;
    private String m_SignoContable;

    //jwong 18/02/2009 para mostrar la descripcion de la cuenta
    private String m_DescripcionCuenta;
    
    private String prestamo;
    /**
     * @return the m_Cuenta
     */
    public String getM_Cuenta() {
        return m_Cuenta;
    }

    /**
     * @param m_Cuenta the m_Cuenta to set
     */
    public void setM_Cuenta(String m_Cuenta) {
        this.m_Cuenta = m_Cuenta;
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
     * @return the m_SaldoDisponible
     */
    public String getM_SaldoDisponible() {
        return m_SaldoDisponible;
    }

    /**
     * @param m_SaldoDisponible the m_SaldoDisponible to set
     */
    public void setM_SaldoDisponible(String m_SaldoDisponible) {
        this.m_SaldoDisponible = m_SaldoDisponible;
    }

    /**
     * @return the m_SaldoRetenido
     */
    public String getM_SaldoRetenido() {
        return m_SaldoRetenido;
    }

    /**
     * @param m_SaldoRetenido the m_SaldoRetenido to set
     */
    public void setM_SaldoRetenido(String m_SaldoRetenido) {
        this.m_SaldoRetenido = m_SaldoRetenido;
    }

    /**
     * @return the m_SaldoContable
     */
    public String getM_SaldoContable() {
        return m_SaldoContable;
    }

    /**
     * @param m_SaldoContable the m_SaldoContable to set
     */
    public void setM_SaldoContable(String m_SaldoContable) {
        this.m_SaldoContable = m_SaldoContable;
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
    public String getM_TipoInformacion() {
        return m_TipoInformacion;
    }
    public void setM_TipoInformacion(String m_TipoInformacion) {
        this.m_TipoInformacion = m_TipoInformacion;
    }
    */
    public java.util.Map getParametrosUrl(){
        java.util.Map parametros = new java.util.HashMap();
        parametros.put("m_Cuenta",this.m_Cuenta);
        parametros.put("m_TipoCuenta",this.m_TipoCuenta);
        //parametros.put("m_TipoInformacion",this.m_TipoInformacion);
        //jwong 09/03/2009 para enviar la moneda
        parametros.put("m_Moneda", this.m_Moneda);

        //jwong 25/03/2009 tenemos que enviar los montos de los saldos saldoDisponible, saldoRetenido y saldoContable
        parametros.put("m_SaldoDisponible", this.m_SaldoDisponible);
        parametros.put("m_SaldoRetenido", this.m_SaldoRetenido);
        parametros.put("m_SaldoContable", this.m_SaldoContable);
        
        return parametros;
    }

    /**
     * @return the m_SignoDisponible
     */
    public String getM_SignoDisponible() {
        return m_SignoDisponible;
    }

    /**
     * @param m_SignoDisponible the m_SignoDisponible to set
     */
    public void setM_SignoDisponible(String m_SignoDisponible) {
        this.m_SignoDisponible = m_SignoDisponible;
    }

    /**
     * @return the m_SignoRetenido
     */
    public String getM_SignoRetenido() {
        return m_SignoRetenido;
    }

    /**
     * @param m_SignoRetenido the m_SignoRetenido to set
     */
    public void setM_SignoRetenido(String m_SignoRetenido) {
        this.m_SignoRetenido = m_SignoRetenido;
    }

    /**
     * @return the m_SignoContable
     */
    public String getM_SignoContable() {
        return m_SignoContable;
    }

    /**
     * @param m_SignoContable the m_SignoContable to set
     */
    public void setM_SignoContable(String m_SignoContable) {
        this.m_SignoContable = m_SignoContable;
    }

    /**
     * @return the m_DescripcionCuenta
     */
    public String getM_DescripcionCuenta() {
        return m_DescripcionCuenta;
    }

    /**
     * @param m_DescripcionCuenta the m_DescripcionCuenta to set
     */
    public void setM_DescripcionCuenta(String m_DescripcionCuenta) {
        this.m_DescripcionCuenta = m_DescripcionCuenta;
    }
    
    public void setPrestamo(String prestamo) {
		this.prestamo = prestamo;
	}
    
    public String getPrestamo() {
		return prestamo;
	}
}

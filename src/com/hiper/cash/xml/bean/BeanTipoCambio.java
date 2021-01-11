/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.xml.bean;

/**
 *
 * @author jwong
 */
public class BeanTipoCambio {
    private String m_Moneda;
    private String m_Compra;
    private String m_Venta;
    private double precioCompra;
    private double precioVenta;

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
     * @return the m_Compra
     */
    public String getM_Compra() {
        return m_Compra;
    }

    /**
     * @param m_Compra the m_Compra to set
     */
    public void setM_Compra(String m_Compra) {
        this.m_Compra = m_Compra;
    }

    /**
     * @return the m_Venta
     */
    public String getM_Venta() {
        return m_Venta;
    }

    /**
     * @param m_Venta the m_Venta to set
     */
    public void setM_Venta(String m_Venta) {
        this.m_Venta = m_Venta;
    }

	public double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}
    
    
    
}

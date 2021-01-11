/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

/**
 *
 * @author jwong
 */
public class BeanDetallePagoServicio {
    private String m_Importe = "";
    private String m_FechaVencimiento = ""; //(dd/mm/aaaa)
    private String m_Referencia = "";

    private String m_CuentaCargo = "";
    private String m_NumSuministro = "";

    

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
        if(m_Importe != null){
            this.m_Importe = m_Importe;
        }else{
            this.m_Importe = "";
        }
        
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
        if(m_FechaVencimiento != null){
            this.m_FechaVencimiento = m_FechaVencimiento;
        }else{
            this.m_FechaVencimiento = "";
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
     * @return the m_NumSuministro
     */
    public String getM_NumSuministro() {
        return m_NumSuministro;
    }

    /**
     * @param m_NumSuministro the m_NumSuministro to set
     */
    public void setM_NumSuministro(String m_NumSuministro) {
        if(m_NumSuministro != null){
            this.m_NumSuministro = m_NumSuministro;
        }else{
            this.m_NumSuministro = "";
        }
        
    }
}

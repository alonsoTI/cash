/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.entidad;

/**
 *
 * @author jmoreno
 */
public class BeanPaginacion {
    private int m_pagInicial;
    private int m_pagFinal;
    private int m_pagActual;
    private int m_regPagina;
    private long m_seleccion;    
    private String m_tipo;
    private int m_primerRegAct;
    private int m_ultimoRegAct;
    private int m_resto;
    private int cantidadRegistrosTotales;
    public BeanPaginacion(){
        m_resto = 0;
    }
    public BeanPaginacion(long m_totalRegistros,int m_regPagina){
        m_pagInicial = 1;
        this.m_regPagina = m_regPagina;
    }


    public int getM_pagInicial() {
        return m_pagInicial;
    }

    public void setM_pagInicial(int m_pagInicial) {
        this.m_pagInicial = m_pagInicial;
    }

    public int getM_pagFinal() {
        return m_pagFinal;
    }

    public void setM_pagFinal(int m_pagFinal) {
        this.m_pagFinal = m_pagFinal;
    }

    public int getM_pagActual() {
        return m_pagActual;
    }

    public void setM_pagActual(int m_pagActual) {
        this.m_pagActual = m_pagActual;
        this.m_seleccion = this.m_regPagina * (this.m_pagActual - 1);
    }

    public long getM_seleccion() {
        return m_seleccion;
    }

    public void setM_seleccion(long m_seleccion) {
        this.m_seleccion = m_seleccion;
    }

    public int getM_regPagina() {
        return m_regPagina;
    }

    public void setM_regPagina(int m_regPagina) {
        this.m_regPagina = m_regPagina;
    }

    public String getM_tipo() {
        return m_tipo;
    }

    public void setM_tipo(String m_tipo) {
        this.m_tipo = m_tipo;
    }

    public int getM_primerRegAct() {
        return m_primerRegAct;
    }

    public void setM_primerRegAct(int m_primerRegAct) {
        this.m_primerRegAct = m_primerRegAct;
    }

    public int getM_ultimoRegAct() {
        return m_ultimoRegAct;
    }

    public void setM_ultimoRegAct(int m_ultimoRegAct) {
        this.m_ultimoRegAct = m_ultimoRegAct;
    }

    public int getM_resto() {
        return m_resto;
    }

    public void setM_resto(int m_resto) {
        this.m_resto = m_resto;
    }
	public int getCantidadRegistrosTotales() {
		return cantidadRegistrosTotales;
	}
	public void setCantidadRegistrosTotales(int cantidadRegistrosTotales) {
		this.cantidadRegistrosTotales = cantidadRegistrosTotales;
	}
   
}

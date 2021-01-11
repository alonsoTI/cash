/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.util;

/**
 *
 * @author jmoreno
 */
public abstract class Formateador {
    protected  String formato;
    protected boolean valido;
    protected boolean haySeparadores;
    protected int indices[];
    protected String separadores[];
    public Formateador(){
    }
    public Formateador(String formato){
        this.formato=formato;
        haySeparadores=false;
        indices= new int[2];
        separadores=new String[2];
    }
    public String getFormato() {
        return formato;
    }
    public void setFormato(String formato) {
        this.formato = formato;
    }
    public abstract boolean esValido();
    public abstract boolean parse(String valor);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.ingresarBD;

import org.jdom.Element;

import com.hiper.cash.ingresarBD.util.LogErrores;
import com.hiper.cash.util.Mensajes;

/**
 *
 * @author jmoreno
 */
public abstract class Campo {

    private String nombre;
    private String posicion;
    private String tipo;
    private int longitud;
    private String separador;
    private Object valor;//valor del campo
    private String valPermitidos;
    private String tipoBd;//para saber que tipo de campo es en base de datos
    private String condicional;
    private String condicion;
    private String referencia;
    public Campo() {
    }

    public abstract boolean validarTipo(String vcondicion);
    
    public abstract int leerCampo(String cadena);

    public boolean init(Element campo) {

        String temp = campo.getAttributeValue(Mensajes.XML_NOMBRE);
        if(temp!=null && !"".equals(temp)){
            setNombre(temp);
            temp=campo.getAttributeValue(Mensajes.XML_LONGITUD);
            if(temp!=null && !"".equals(temp)){
                setLongitud(Integer.parseInt(temp));
                temp=campo.getAttributeValue(Mensajes.XML_POSICION);
                if(temp!=null && !"".equals(temp)){
                    setPosicion(temp);
                    temp=campo.getAttributeValue(Mensajes.XML_TIPO_BD);
                    if(temp!=null && !"".equals(temp)){
                        setTipoBd(temp);
                        setSeparador(campo.getAttributeValue(Mensajes.XML_SEPARADOR));
                        setValPermitidos(campo.getAttributeValue(Mensajes.XML_VALORES_PERMITIDOS));
                        setCondicion(campo.getAttributeValue(Mensajes.XML_CONDICION));
                        setReferencia(campo.getAttributeValue(Mensajes.XML_REFENCIA));
                        return true;
                    }else{
                        LogErrores.setERROR("El formato del campo no contiene el atributo: TipoBd\n");
                        return false;
                    }

                }else{
                    LogErrores.setERROR("El formato del campo no contiene el atributo: Posicion\n");
                    return false;
                }

            }else{
                LogErrores.setERROR("El formato del campo no contiene el atributo: Longitud\n");
                return false;
            }

        }else{
            LogErrores.setERROR("El formato del campo no contiene el atributo: Nombre\n");
            return false;
        }

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getLongitud() {
        return longitud;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public String getSeparador() {
        return separador;
    }

    public void setSeparador(String separador) {
        this.separador = separador;
        if (separador != null) {
            if (separador.equals("TAB")) {
                this.separador = "\t";
            }
        }
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public String getValPermitidos() {
        return valPermitidos;
    }

    public void setValPermitidos(String valPermitidos) {
        this.valPermitidos = valPermitidos;
    }   

    public String getTipoBd() {
        return tipoBd;
    }

    public void setTipoBd(String tipoBd) {
        this.tipoBd = tipoBd;
    }

    public String getCondicional() {
        return condicional;
    }

    public void setCondicional(String condicional) {
        this.condicional = condicional;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}

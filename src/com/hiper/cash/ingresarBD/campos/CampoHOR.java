/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD.campos;

import com.hiper.cash.ingresarBD.util.LogErrores;
import com.hiper.cash.ingresarBD.Campo;
import com.hiper.cash.util.FormateadorHora;
import com.hiper.cash.util.Mensajes;

import org.jdom.Element;

/**
 *
 * @author jmoreno
 */
public class CampoHOR extends Campo {
    
    private FormateadorHora formatoHora;
    public int leerCampo(String contenido){
        int posicion=0;
        String valorCampo="";

           if(getSeparador()!=null){//tiene separador
               int indOf=contenido.indexOf(getSeparador());
               if(indOf!=-1){//existe separador en la cadena
                    valorCampo=contenido.substring(0,indOf);
                    posicion=indOf+1;
               }else{
                    valorCampo=contenido;//*
                    LogErrores.setERROR(Mensajes.MNS_NOEXISTE_SEPARADOR.replaceFirst("1",getSeparador()));
                    posicion=-1;
               }
           }else{
               if (contenido.length() >= getLongitud()) {
                    valorCampo = contenido.substring(0, getLongitud());
                    posicion = getLongitud();
                } else {
                    valorCampo = contenido;
                    posicion = contenido.length();;
                }
           }
       setValor(valorCampo);
       valorCampo=null;
       return posicion;
    }
    public boolean validar(String valorCampo){
        if (getFormatoHora().parse(valorCampo)) {
            int hora = Integer.parseInt(FormateadorHora.HOUR);
            int minuto = Integer.parseInt(FormateadorHora.MINUTE);
            int segundo = Integer.parseInt(FormateadorHora.SECOND);
            if (hora < 0 || hora > 23 || minuto < 0 || minuto > 59 || segundo < 0 || segundo > 59) {
                LogErrores.setERROR(Mensajes.MNS_HORA_RANGO_ERROR);
                return false;
            }
            return true;
        }
        LogErrores.setERROR(Mensajes.MNS_HORA_NOCOINCIDE_FORMATO + getFormatoHora().getFormato());
        return false;
    }
    public boolean validarTipo(String vcondicion){
        String valorCampo = (String) getValor();
        valorCampo=valorCampo.trim();//para quitar los espacios en blanco
        String valorCondicion = this.getCondicion();
        if (valorCondicion != null && vcondicion != null) {
            if (valorCondicion.equals(vcondicion)) {
                if (!" ".equals(valorCampo) && !"".equals(valorCampo)) {
                    return validar(valorCampo);
                } else {
                    LogErrores.setERROR("Este campo se debe ingresar");
                    return false;
                }
            } else {
                /*si dependen de otros campos y el valor no coincide con el requerido,
                 * entonces este campo no debe venir
                 */
                if (" ".equals(valorCampo) || "".equals(valorCampo)) {
                    return true;
                } else {
                    LogErrores.setERROR("Este campo no se debe ingresar");
                    return false;
                }
            }
        } else {
            if (!" ".equals(valorCampo) || !"".equals(valorCampo)) {//para aceptar campos sin valorCampo de campo
                return validar(valorCampo);
            } else {
                return true;
            }
        }
        
    }

    public boolean init(Element campo){
        if(super.init(campo)){
            String temp=campo.getAttributeValue(Mensajes.XML_FORMATO_HORA);
            if(temp!=null && !"".equals(temp)){
                setFormatoHora(new FormateadorHora(temp));
                if(getFormatoHora().esValido()){
                    return true;
                }else{
                    LogErrores.setERROR("El formato de Hora no es valido:"+getFormatoHora().getFormato());
                }
                
            }else{
                LogErrores.setERROR("El formato deL campo no contiene:Formato de Hora\n");
            }
        }
        return false;        
    }

    public FormateadorHora getFormatoHora() {
        return formatoHora;
    }

    public void setFormatoHora(FormateadorHora formatoHora) {
        this.formatoHora = formatoHora;
    }
   
}

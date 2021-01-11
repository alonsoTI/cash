/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.ingresarBD.campos;


import com.hiper.cash.ingresarBD.util.LogErrores;
import com.hiper.cash.ingresarBD.Campo;
import com.hiper.cash.util.FormateadorFecha;
import com.hiper.cash.util.Mensajes;

import org.jdom.Element;

/**
 *
 * @author jmoreno
 */
public class CampoFEC extends Campo {

    private FormateadorFecha formatoFecha;

    public int leerCampo(String contenido) {
        int posicion = 0;
        String valorCampo = "";
        if (getSeparador() != null) {//tiene separador
            int indOf=contenido.indexOf(getSeparador());
           if(indOf!=-1){//existe separador en la cadena
                valorCampo=contenido.substring(0,indOf);
                posicion=indOf+1;
           }else{
                valorCampo=contenido;//*
                LogErrores.setERROR(Mensajes.MNS_NOEXISTE_SEPARADOR.replaceFirst("1",getSeparador()));
                posicion=-1;
           }
        } else {
            /*para verificar que este campo no este al final de un linea de Datos
             * y asi no lanze una excepcion del tipo outOfRange*/
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

    private boolean validar(String valorCampo){
        if (getFormatoFecha().parse(valorCampo)) {
            int year = Integer.parseInt(FormateadorFecha.YEAR);
            int month = Integer.parseInt(FormateadorFecha.MONTH);
            int day = Integer.parseInt(FormateadorFecha.DAY);
            LogErrores.setERROR(Mensajes.MNS_FECHA_RANGO_ERROR);
            //Verificamos si los rangos del dia,mes y año son correctos
            if (month < 1 || month > 12 || day < 1 || year < 1) {
                return false;
            }
            if (month == 2) {
                if (year % 4 == 0) {
                    if (day > 29) {
                        return false;
                    }
                } else {
                    if (day > 28) {
                        return false;
                    }
                }
            }
            //Meses de ENERO,MARZO,MAYO,JULIO,AGOSTO,OCTUBRE,DICIEMBRE
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (day > 31) {
                    return false;
                }
            }
            //Meses de ABRIL,JUNIO,SEPTIEMBRE,NOVIEMBRE
            if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day > 30) {
                    return false;
                }
            }
            LogErrores.resetError();
            return true;
        }
        LogErrores.setERROR(Mensajes.MNS_FECHA_NOCOINCIDE_FORMATO + getFormatoFecha().getFormato());
        return false;
    }
    public boolean validarTipo(String vcondicion) {
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
    public boolean init(Element campo) {
        if (super.init(campo)) {
            String formatoTemp = campo.getAttributeValue(Mensajes.XML_FORMATO_FECHA);
            if (formatoTemp != null && !"".equals(formatoTemp)) {
                setFormatoFecha(new FormateadorFecha(formatoTemp));
                if (getFormatoFecha().esValido()) {
                    return true;
                }else{
                    LogErrores.setERROR("El formato de Fecha no es valido:"+getFormatoFecha().getFormato());
                }
                
            }else{
                LogErrores.setERROR("El formato deL campo no contiene un Formato de Fecha\n");
            }
        }
        return false;
    }

    public FormateadorFecha getFormatoFecha() {
        return formatoFecha;
    }

    public void setFormatoFecha(FormateadorFecha formatoFecha) {
        this.formatoFecha = formatoFecha;
    }
    
}

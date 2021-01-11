/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD.campos;

import com.hiper.cash.ingresarBD.Campo;
import com.hiper.cash.ingresarBD.util.LogErrores;
import com.hiper.cash.util.Mensajes;

/**
 *
 * @author jmoreno
 */
public class CampoDEC extends Campo {

    private int decimales;

    public int leerCampo(String contenido){
        int posicion=0;
        String valorCampo="";
        if(getSeparador()!=null){//separador especificado en el formado, con campos de long. fija y variable
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
           if(contenido.length()>=getLongitud()){//el 1 por el punto decimal
                valorCampo=contenido.substring(0,getLongitud());
                posicion=getLongitud();
           }else{
                valorCampo=contenido;
                posicion=contenido.length();
           }
       }
       setValor(valorCampo);
       valorCampo=null;
       return posicion;
     
    }
    public boolean validar(String valorCampo){
        if(valorCampo.length() <= this.getLongitud()){
                for(int i=0;i<valorCampo.length();i++ ){
                    if(!Character.isDigit(valorCampo.charAt(i))){
                        LogErrores.setERROR("El caracter: \""+valorCampo.charAt(i)+"\" no es numerico");
                        return false;
                    }
                }
                if(validarValores()){
                    return true;
                }else{
                    LogErrores.setERROR(Mensajes.MNS_VALPERMITIDOS_ERROR+this.getValPermitidos());
                    return false;
                }
            }else{
                    LogErrores.setERROR(Mensajes.MNS_CAMPOLONGITUD_MAXIMA+this.getLongitud());
                    return false;
        }
    }
    public boolean validarTipo(String vcondicion){
        String valorCampo=(String) getValor();
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
    public String parseToDecimal(String v){
        Float valorEnDec=Float.parseFloat(v);
        valorEnDec=valorEnDec/100;
        return ""+valorEnDec;
    }
    public boolean validarTipoDecimal(){
        
        String valorCampo=(String) getValor();
       
        if(!" ".equals(valorCampo)){
            int puntoD=0;
            /*verificamos si el valorCampo del campo solo contiene números y el punto decimal*/
             for(int i=0;i<valorCampo.length();i++ ){
                if(!Character.isDigit(valorCampo.charAt(i))){
                    if(!".".equals(Character.toString(valorCampo.charAt(i)))){
                        LogErrores.setERROR("El caracter: \""+valorCampo.charAt(i)+"\" no es numérico");
                        return false;
                    }else{
                        puntoD+=1;
                        if(puntoD>1){
                            LogErrores.setERROR(Mensajes.MNS_NUMMAXIMO_PUNTO);
                            return false;
                        }
                    }
                }
            }
            /* Verificamos si el numero cumple con la cantidad de decimales
             * y con la longitud maxima permitida */
            if(puntoD==1){
                int indice=valorCampo.indexOf(".");
                String entero=valorCampo.substring(0,indice);
                String decimal=valorCampo.substring(indice+1);
                if(decimal.length()<=getDecimales()){
                    if((decimal.length()+entero.length())<=getLongitud()){

                        if (validarValores()) {
                            return true;
                        } else {
                            LogErrores.setERROR(Mensajes.MNS_VALPERMITIDOS_ERROR + this.getValPermitidos());
                            return false;
                        }
                    }else{
                        LogErrores.setERROR(Mensajes.MNS_CAMPOLONGITUD_MAXIMA+getLongitud());
                        return false;
                    }
                }else{
                    LogErrores.setERROR(Mensajes.MNS_CAMPODECIMALES_MAXIMA+getDecimales());
                    return false;
                }
            }else{
                LogErrores.setERROR(Mensajes.MNS_NOEXISTE_PUNTO);
                return false;
            }

        }else{
            ////System.out.println("El valor del campo decimal no contiene valor");
            return true;
        }
      
    }

    public int getDecimales() {
        return decimales;
    }

    public void setDecimales(int decimales) {
        this.decimales = decimales;
    }

//    @Override
//    public boolean init(Element campo) {
//        if(super.init(campo)){
//            String temp=campo.getAttributeValue(Mensajes.XML_CANT_DECIMALES);
//            if (temp!= null && !"".equals(temp)) {
//                setDecimales(Integer.parseInt(temp));
//                return true;
//            }
//           LogErrores.setERROR("El formato del campo no contiene el atributo: número de decimales\n");
//        }
//        return false;        
//    }
    private boolean validarValores(){
        String valores=getValPermitidos();
        if(valores!=null){
            String lista[]=valores.split(",");
            for(int i=0;i<lista.length;i++){
                if(lista[i].equals(getValor().toString().trim())){//jmoreno 06-01-2010
                    return true;
                }
            }
            return false;
        }else{
            return true;//el campo carece de valores permitidos
        }
    }
   
}

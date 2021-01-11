/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD.campos;

import com.hiper.cash.ingresarBD.util.LogErrores;
import com.hiper.cash.ingresarBD.Campo;
import com.hiper.cash.util.Mensajes;

/**
 *
 * @author jmoreno
 */
public class CampoNUM extends Campo {

    public int leerCampo(String contenido){
        int posicion=0;
        String valorCampo="";
        if(getSeparador()!=null){//separador especificado en el formado
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
          if(contenido.length()>=getLongitud()){
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
       //<editor-fold defaultstate="collapsed" desc="logica anterior">
       /*
           if(getSeparador()!=null){//tiene separador
               int indOf=contenido.indexOf(getSeparador());
               valorCampo=contenido.substring(0,indOf);
               posicion=indOf+1;
           }else{
               valorCampo=contenido.substring(0,getLongitud());
               posicion=getLongitud();
           }
        
        setValor(valorCampo);
        return posicion;*/
        //</editor-fold>
    }
public boolean validarTipo(String vcondicion){
        
	  //ANDY : CAMBIO PARA OBTENER EL NOMBRE DEL CAMPO	
	   String nameCampo=(String) getNombre();
	   	   
	   String valorCampo=(String) getValor();
        valorCampo=valorCampo.trim();//para quitar los espacios en blanco
        String valorCondicion = this.getCondicion();
        
        ////System.out.println("ANDY==>ENTRO AQUI A VALIDA CAMPONUM  LINEA 63, EL VALOR ES="+valorCampo);
        
        if (valorCondicion != null && vcondicion != null) {
            if (valorCondicion.equals(vcondicion)) {
                if(!(" ".equals(valorCampo)) && !("".equals(valorCampo))){//para aceptar campos sin valorCampo de campo
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
                }else{
                    LogErrores.setERROR("Este campo se debe ingresar");
                    return false;
                }
            }else {
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
        }else{
            if(!" ".equals(valorCampo)|| !"".equals(valorCampo)){//para aceptar campos sin valorCampo de campo
                if(valorCampo.length() <= this.getLongitud()){
                	////System.out.println("ANDY==> INGRESO AQUI  -->"+nameCampo);
                    
                	if(!nameCampo.equals("nDBuDocumento")){
                		////System.out.println("ANDY==>nDBuDocumento AQUI  -->"+nameCampo);
                      for(int i=0;i<valorCampo.length();i++ ){
                        if(!Character.isDigit(valorCampo.charAt(i))){
                            LogErrores.setERROR("El caracter: \""+valorCampo.charAt(i)+"\" no es numerico");
                            return false;
                        }
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

            }else{
                return true;
            }
        }

    }
    private boolean validarValores(){
    	////System.out.println("ANDY==>VALOR="+getValor().toString().trim());
        String valores=getValPermitidos();
        if(valores!=null){
            String lista[]=valores.split(",");
            for(int i=0;i<lista.length;i++){
            	//System.out.println("ANDY==>valores permitidos  -->"+lista[i]);
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

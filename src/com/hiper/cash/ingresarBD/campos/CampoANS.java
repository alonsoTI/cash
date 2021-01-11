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
public class CampoANS extends Campo { //longitud fija
    public int leerCampo(String contenido){
    	
      // //System.out.print("LeerCampo ANS");
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
    }
    private boolean validar(String valorCampo){
        if (valorCampo.length() <= this.getLongitud()) {
            if (validarValores()) {
                return true;
            } else {
                LogErrores.setERROR(Mensajes.MNS_VALPERMITIDOS_ERROR + this.getValPermitidos());
                return false;
            }

        } else {
            LogErrores.setERROR(Mensajes.MNS_CAMPOLONGITUD_MAXIMA + this.getLongitud());
            return false;
        }
    }

    public boolean validarTipo(String vcondicion){
        String valorCampo = (String) this.getValor();        
        String nombreCampo=(String) this.getNombre();
        
        nombreCampo = nombreCampo.trim();
        valorCampo = valorCampo.trim();//para quitar los espacios en blanco
        String valorCondicion = this.getCondicion();

       
        ////System.out.println("ANDY==>"+valorCampo+" - "+this.getNombre());
        
        /*
        if(nombreCampo.length()>0){
	        if(nombreCampo.equals("dDBuAdicional7")){   	  
	      	  for(int i=0;i<valorCampo.length();i++ ){
		          if(!Character.isDigit(valorCampo.charAt(i))){
		              LogErrores.setERROR("El caracter: \""+valorCampo.charAt(i)+"\" no es numerico");
		              return false;
		          }
	         } //end for   	  
	        }//end if        
        }
        */
        
        if (valorCondicion != null && vcondicion != null) {
            if (valorCondicion.equals(vcondicion)) {
                if (!" ".equals(valorCampo) && !"".equals(valorCampo)) {
                    return validar(valorCampo);
                } else {
                    LogErrores.setERROR("Este campo se debe ingresar");
                    return false;
                }
            } else {
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
    
    
 	/*if(nombreCampo.equals("dDBuAdicional7")){    		
        /* for(int i=0;i<valorCampo.length();i++ ){
           /*if(!Character.isDigit(valorCampo.charAt(i))){
               LogErrores.setERROR("El caracter: \""+valorCampo.charAt(i)+"\" no es numerico");
               return false;
           }*/
         //}
      		
   	//}   
    
    
    /*
    public boolean validarTipo(){
       String valorCampo=(String)this.getValor();
       valorCampo=valorCampo.trim();//para quitar los espacios en blanco
        if(!" ".equals(valorCampo) || !"".equals(valorCampo)){//para aceptar campos sin valorCampo de campo
            if(valorCampo.length()<=this.getLongitud()){
                int ascii=0;
                for(int i=0;i<valorCampo.length();i++ ){
                   ascii=(int)valorCampo.charAt(i);
                    if( ascii < 32 || ascii > 126 ){
                       LogErrores.setERROR("El caracter: \""+valorCampo.charAt(i)+"\" no es alfanumerico");
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
            return true;
        }
    }
    
    */
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

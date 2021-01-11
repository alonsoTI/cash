/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.util;


/**
 *
 * @author jmoreno
 */
public class FormateadorFecha extends Formateador{
     private static final String day="d";
    private static final String month="m";
    private static final String year="y";
    private static final String Fday="dd";
    private static final String Fmonth="mm";
    private static final String FyearI="yyyy";
    private static final String FyearII="yy";
    public static String DAY;
    public static String MONTH;
    public static String YEAR;
    public FormateadorFecha(String formato){
        super(formato);
    }
    public boolean esValido(){
        valido=false;
        int indIni=0,indFin=0,ind=0;
        String yform="",Mform="",dform="";
        try {
            indIni=getFormato().indexOf(year);
            indFin=getFormato().lastIndexOf(year)+1;
            yform=getFormato().substring(indIni, indFin);
            if(indFin<getFormato().length()&& getFormato().length()>=8){
                haySeparadores=true;
                indices[ind]=indFin;
                separadores[ind]=getFormato().substring(indFin, indFin+1);                
                ind=1;
            }

            indIni=getFormato().indexOf(month);
            indFin=getFormato().lastIndexOf(month)+1;
            Mform=getFormato().substring(indIni, indFin);
            if(indFin<getFormato().length()&& getFormato().length()>=8){
                haySeparadores=true;
                indices[ind]=indFin;
                separadores[ind]=getFormato().substring(indFin, indFin+1);                
                ind=1;
            }

            indIni=getFormato().indexOf(day);
            indFin=getFormato().lastIndexOf(day)+1;
            dform=getFormato().substring(indIni, indFin);
            if(indFin<getFormato().length() && getFormato().length()>=8){
                haySeparadores=true;
                indices[ind]=indFin;
                separadores[ind]=getFormato().substring(indFin, indFin+1);            
            }
            
            if((yform.equals(FyearI) || yform.equals(FyearII)) && Mform.equals(Fmonth) && dform.equals(Fday)){
                if(formato.length()==8 && yform.equals(FyearI)){
                    haySeparadores=false;
                }
                valido=true;
                yform=null;Mform=null;dform=null;
                return valido;
            }
           yform=null;Mform=null;dform=null;
           return valido;
        } catch (StringIndexOutOfBoundsException e) {
            /*cuando en el formato existen caracteres diferentes a los aceptado, pues
             no los encuentra y el indice es -1, y en las funciones substring los buscamos
             por eso lanza este tipo de excepcion*/
            return valido;
        }

    }
    public boolean parse(String valor){
        if(valido){//si el formato es valido.
            if(valor.length()!=getFormato().length()){//el tamaño del valor con el del formato deben coincidir
                return false;
            }
            if(haySeparadores){
                for(int i=0;i<indices.length;i++){
                    if(!separadores[i].equals(valor.substring(indices[i],indices[i]+1))){
                        return false;
                    }
                }
            }
            int indIni=0,indFin=0;
            try {
                indIni=getFormato().indexOf(year);
                indFin=getFormato().lastIndexOf(year);
                YEAR=valor.substring(indIni, indFin+1);
                indIni=getFormato().indexOf(month);
                indFin=getFormato().lastIndexOf(month);
                MONTH=valor.substring(indIni, indFin+1);
                indIni=getFormato().indexOf(day);
                indFin=getFormato().lastIndexOf(day);
                DAY=valor.substring(indIni, indFin+1);
            } catch (java.lang.NumberFormatException e) {
                return false;
            }
            return true;
        }
        return false;
    }
}

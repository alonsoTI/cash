/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.util;


/**
 *
 * @author jmoreno
 */
public class FormateadorHora extends Formateador{
    private static final String hour="h";
    private static final String minute="m";
    private static final String second="s";
    private static final String Fhour="hh";
    private static final String Fminute="mm";
    private static final String Fsecond="ss";
    public static String HOUR;
    public static String MINUTE;
    public static String SECOND;

    public FormateadorHora(String formato){
        super(formato);
    }
    public boolean esValido(){
        valido=false;
        int indIni=0,indFin=0,ind=0;
        String hform="",mform="",sform="";
        try {
            indIni=getFormato().indexOf(hour);
            indFin=getFormato().lastIndexOf(hour)+1;
            hform=getFormato().substring(indIni, indFin);
            if(indFin<getFormato().length()&& getFormato().length()>6){
                haySeparadores=true;
                indices[ind]=indFin;
                separadores[ind]=getFormato().substring(indFin, indFin+1);                
                ind=1;
            }

            indIni=getFormato().indexOf(minute);
            indFin=getFormato().lastIndexOf(minute)+1;
            mform=getFormato().substring(indIni, indFin);
            if(indFin<getFormato().length()&& getFormato().length()>6){
                haySeparadores=true;
                indices[ind]=indFin;
                separadores[ind]=getFormato().substring(indFin, indFin+1);                
                ind=1;
            }

            indIni=getFormato().indexOf(second);
            indFin=getFormato().lastIndexOf(second)+1;
            sform=getFormato().substring(indIni, indFin);
            if(indFin<getFormato().length()&& getFormato().length()>6){
                haySeparadores=true;
                indices[ind]=indFin;
                separadores[ind]=getFormato().substring(indFin, indFin+1);                
            }
            
            if(hform.equals(Fhour) && mform.equals(Fminute) && sform.equals(Fsecond)){
                valido=true;
                hform=null;mform=null;sform=null;
                return valido;
            }
            hform=null;mform=null;sform=null;
            return valido;
        }catch (StringIndexOutOfBoundsException e) {
            return valido;
        }
    }
    public boolean parse(String valor){
        if(valido){
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
                indIni=getFormato().indexOf(hour);
                indFin=getFormato().lastIndexOf(hour);
                HOUR=valor.substring(indIni, indFin+1);
                indIni=getFormato().indexOf(minute);
                indFin=getFormato().lastIndexOf(minute);
                MINUTE=valor.substring(indIni, indFin+1);
                indIni=getFormato().indexOf(second);
                indFin=getFormato().lastIndexOf(second);
                SECOND=valor.substring(indIni, indFin+1);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}

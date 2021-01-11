/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author jwong
 */
public class Fecha {
	
	public static final DateFormat DATE_FORMAT_CASH = new SimpleDateFormat("dd/MM/yyyy");
    /**
     * getFechaActual, obtiene la fecha actual del sistema
	 * @autor jwong
     * @param formato, formato en que se requiere la fecha
     * @return
     */    
    public static String getFechaActual(String formato) {
        String fechaActual = null;
        SimpleDateFormat sdf = null;
        if(formato!=null){
            sdf = new SimpleDateFormat(formato);
            fechaActual = sdf.format(new java.util.Date());
        }
        return fechaActual;
    }
    
    
    /**
     * Añade la cantidad especificada de tiempo a la fecha actual
     * y lo obtiene en el formato deseado
     * @autor esilva
     * @param formato formato en que se requiere la fecha
     * @param field campo del Calendar
     * @param amount cantidad de tiempo a ser añadida al campo especificado
     * @return
     */
    public static String getFechaCustom(String formato, int field, int amount) {
        String fechaActual = null;
        SimpleDateFormat sdf = null;

        if(formato!=null){
            sdf = new SimpleDateFormat(formato);
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.add(field, amount);
            fechaActual = sdf.format(calendar.getTime());
        }
        return fechaActual;
    }

    //esilva
    public static String convertToFechaSQL( String fecha )
    {
        return (fecha.substring(6, 10) + fecha.substring(3, 5) + fecha.substring(0, 2));
    }
	
    public static String convertFromFechaSQL( String fecha )
    {
		//jwong 18/01/2009 modificado para manejo de nulos
        if(fecha!=null && fecha.length()>7){
            return (fecha.substring(6, 8) + "/" + fecha.substring(4, 6) + "/" + fecha.substring(0, 4));
        }
        else{
            return "";
        }
    }
	//jwong 18/01/2009 para manejo de hora
    public static String convertFromTimeSQL( String hora ) {
        if(hora!=null && hora.length()>5){
            return (hora.substring(0, 2) + ":" + hora.substring(2, 4) + ":" + hora.substring(4));
        }
        else{
            return "";
        }
    }

    /**
     * getFechaActual, obtiene la fecha actual del sistema
     * @autor jwong
     * @param formato, formato en que se requiere la fecha
     * @return
     */
    public static String formatearFecha(String formatoIN, String formatoOUT, String fecha) {
        String fechaResult = "";
        SimpleDateFormat sdfIN = null;
        SimpleDateFormat sdfOUT = null;
        if(formatoIN!=null && formatoOUT!=null && fecha!=null){
            try {
                sdfIN = new SimpleDateFormat(formatoIN);
                sdfOUT = new SimpleDateFormat(formatoOUT);
                Date dtTmp = sdfIN.parse(fecha);
                fechaResult = sdfOUT.format(dtTmp);
            } catch (ParseException ex) {
                fechaResult = "";                
            }
        }
        return fechaResult;
    }
    
    public static Date addToFecha(int cantidad, int field, Date date){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(field, cantidad);
    	return cal.getTime();
    }

}

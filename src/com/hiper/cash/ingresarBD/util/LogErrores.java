/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD.util;

/**
 *
 * @author jmoreno
 */
public class LogErrores {
    private static StringBuilder ERROR=new StringBuilder();
    //private static StringBuilder otro=new StringBuilder();
    
    public static void resetError(){
        ERROR=new StringBuilder();
    }

    /**
     * @return the ERROR
     */
    public static String getERROR() {
        return ERROR.toString();
    }

    /**
     * @param aERROR the ERROR to set
     */
    public static void setERROR(String aERROR) {
        ERROR.append(aERROR);
    }
   
}

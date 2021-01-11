/*
 * TripleDES.java
 *
 * Created on 18 de julio de 2006, 02:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.hiper.cash.ingresarBD.crypto;

/**
 *
 * @author MVillarreal
 */
public final class TripleDES {

    private static DES desEncryptor = new DES();
    private final static int CIPHER = 1;
    private final static int DECIPHER = 2;

    /** Creates a new instance of TripleDES */
    public TripleDES() {
        desEncryptor = new DES();
    }

    /**
     * Encripta una cadena usando el algoritmo Triple DES
     * @param data  Cadena a encriptar
     * @param key1  Clave 1 a usar en la encriptacion (16 digitos)
     * @param key2  Clave 2 a usar en la encriptacion (16 digitos)
     * @param key3  Clave 3 a usar en la encriptacion (16 digitos)
     * @return Devuelve la cadena encriptada
     */
    public static String cipher(String data, String key1, String key2, String key3) {
        return HP_3DES(CIPHER, data, key1, key2, key3);
    }

    /**
     * Desencripta una cadena usando el algoritmo Triple DES
     * @param data  Cadena a desencriptar
     * @param key1  Clave 1 a usar en la desencriptacion (16 digitos)
     * @param key2  Clave 2 a usar en la desencriptacion (16 digitos)
     * @param key3  Clave 3 a usar en la desencriptacion (16 digitos)
     * @return Devuelve la cadena desencriptada
     */
    public static String decipher(String data, String key1, String key2, String key3) {
        return HP_3DES(DECIPHER, data, key1, key2, key3);
    }

    private static String HP_3DES(int tipo, String Data_in, String key1, String key2, String key3) {
        String outputTotal = "";
        String input = "";
        String output = "";
        int posicion = 0, data_in_len = Data_in.length();
        try {
            while (posicion < data_in_len) {
                if (posicion + 8 <= data_in_len) {
                    input = Data_in.substring(posicion, posicion + 8);
                } else {
                    input = Data_in.substring(posicion, data_in_len);
                }

                if (input.length() < 8) {
                    while (input.length() < 8) {
                        input = input.concat(" ");
                    }
                }

                input = toHexString(input);
                if (tipo == CIPHER) {
                    //Encripta
                    output = desEncryptor.cipher(input, key1);
                    //Desencripta
                    output = desEncryptor.decipher(output.toUpperCase(), key2);
                    //Encripta
                    output = desEncryptor.cipher(output.toUpperCase(), key3);
                }
                if (tipo == DECIPHER)
                {
                    //Desencripta
                    output = desEncryptor.decipher(input, key3);
                    //Encripta
                    output = desEncryptor.cipher(output.toUpperCase(), key2);
                    //Desencripta
                    output = desEncryptor.decipher(output.toUpperCase(), key1);
                }
                posicion += 8;
                outputTotal = outputTotal + output;
            }
            outputTotal = toString (outputTotal);
        }
        catch(Exception e)
        {
//            ////System.out.println("ERROR: HP_3DES "+ e.getMessage());
        }
        return outputTotal;
    }

    public static String toHexString(String data) {
        char[] buf;
        byte[] buf2 = new byte[data.length()];
        int j = 0;
        int offset = 0;
        int k, length;
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        
        for (int i = 0; i < data.length (); i++)
        {
            buf2[i] = (byte) data.charAt (i);
        }
        length = buf2.length;
        buf = new char[length * 2];
        
        for (int i = offset; i < offset + length; i++)
        {
            k = buf2[i];
            buf[j++] = hexDigits[(k >>> 4) & 0x0F];
            buf[j++] = hexDigits[k & 0x0F];
        }
        return new String(buf);
    }

    public static String toString(String s) {
        int index = 0;
        String cadena = "";
        while (index < s.length()) {
            String aux = s.substring(index, index + 2);
            index = index + 2;
            int b = Integer.parseInt(aux, 16);
            cadena = cadena + (char) b;
        }
        return cadena;
    }

    public static int verificaClave(String s) {
        try {
            int index = 0;
            String cadena = "";
            while (index < s.length()) {
                String aux = s.substring(index, index + 2);
                index = index + 2;
                int b = Integer.parseInt (aux, 16);
                cadena = cadena + (char)b;
            }
        }
        catch(Exception e)
        {
            return 1;
        }
        return 0;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.ingresarBD;

import java.util.*;

/**
 *
 * @author jmoreno
 */
public class FormatoServicio {
    private HashMap campos;
    private String nombre;
    private String id;

    public HashMap getCampos() {
        return campos;
    }

    public void setCampos(HashMap campos) {
        this.campos = campos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

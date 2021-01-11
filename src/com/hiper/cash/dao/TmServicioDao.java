/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import com.hiper.cash.domain.TmServicio;


public interface TmServicioDao {
    public TmServicio select(String codigo);
    public TmServicio getServicio(long idServicioEmpresa);
    String obtenerIdServicio(long idServicioEmpresa);    
}

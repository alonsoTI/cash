/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import java.util.List;

import com.hiper.cash.domain.TaLogValidacion;
import com.hiper.cash.entidad.BeanPaginacion;

/**
 * 
 * @author jmoreno
 */
public interface TaLogValidacionDao
{
    List obtenerLogErrores(int idEnvio);

    List obtenerLogErrores(int idEnvio, BeanPaginacion bpag);

    long obtenerCantErrores(int idEnvio);

    void insertarLogValidacion(TaLogValidacion lv);
    
    TaLogValidacion obtenerLogError(int idEnvio, long numeroLinea);
}

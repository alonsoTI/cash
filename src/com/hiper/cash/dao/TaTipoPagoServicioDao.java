/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import java.util.List;

import com.hiper.cash.entidad.BeanTipoPagoServicio;

/**
 *
 * @author esilva
 */
public interface TaTipoPagoServicioDao {

    List<BeanTipoPagoServicio> select(long idservemp);
}

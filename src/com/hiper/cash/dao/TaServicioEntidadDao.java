/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import com.hiper.cash.domain.TaServicioEntidad;



/**
 *
 * @author esilva
 */
public interface TaServicioEntidadDao {
    public abstract TaServicioEntidad selectServicioEntidad(long cSEnIdServEmp, String cSEnIdEntidad, String cSEnTipoEntidad);
    public abstract boolean insert(TaServicioEntidad taServEntidad);
    public abstract boolean delete(TaServicioEntidad taServEntidad);
}

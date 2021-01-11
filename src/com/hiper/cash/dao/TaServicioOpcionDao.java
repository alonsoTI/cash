/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import com.hiper.cash.domain.TaServicioOpcion;
import java.util.List;

/**
 *
 * @author esilva
 */
public interface TaServicioOpcionDao {
    public abstract TaServicioOpcion select(String modulo, String submodulo);
    public abstract List select(String modulo);
}

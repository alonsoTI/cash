/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import java.util.List;

/**
 *
 * @author jwong
 */
public interface TmBancoDao {
    public List select();
    public abstract String obtenerBanco(String codigoBco);
}

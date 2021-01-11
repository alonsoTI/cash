/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;
import java.util.List;

import com.hiper.cash.domain.TxResult;
/**
 *
 * @author jmoreno
 */
public interface TxResultDao {
    public abstract TxResult selectByCodIbs(String codIbs);
    TxResult obtenerNuevoCodigoIBS(String codigoIBS);
    List<TxResult> seleccionarTabla();
}

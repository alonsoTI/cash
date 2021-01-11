/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import java.util.List;

/**
 *
 * @author esilva
 */
public interface TaAprobacionOrdenDao {
    public abstract boolean insert(long orden, long servicio, long aprobador,String idUsuario,StringBuilder mensaje);
    public abstract List selectAprobadores(long orden, long idServicioEmpresa);
}

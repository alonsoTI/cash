/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import com.hiper.cash.entidad.BeanCuenta;
import java.util.List;

/**
 *
 * @author esilva
 */
public interface TaCuentasServicioEmpresaDao {

    public abstract List selectServicioxEmpresa(/*String empresa,*/ String servicio);
    public abstract BeanCuenta select(String cuenta, long servemp);
	public abstract String getTipoCuenta(String numCta,long idServxEmp);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import com.hiper.cash.domain.TaEntidadEmpresa;
import java.util.List;

/**
 *
 * @author esilva
 */
public interface TaEntidadEmpresaDao {

    public abstract List selectEntidadEmpresa(String empresa, long servicio,  String tipoEntidad);
	
	//jwong 22/01/2009 para el mantenimiento de proveedores y de personal
    public abstract List selectEntidadEmpresaByTipo(String idServEmp, String tipoEntidad);
    //jwong 22/01/2009 para insertar nuevo proveedor o personal
    public abstract boolean insert(TaEntidadEmpresa taEntEmp);
    //jwong 22/01/2009 busqueda por id
    //public abstract TaEntidadEmpresa selectEntidadEmpresaById(String empresa, String idEntidad);
	//jwong 12/02/2009 para eliminar
    public abstract boolean delete(TaEntidadEmpresa taEntEmp);

    public abstract TaEntidadEmpresa selectEntidadEmpresa(String idServEmp, String idEntidad, String type);
}

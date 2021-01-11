/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import com.hiper.cash.domain.TmEmpresa;
import java.util.List;

/**
 *
 * @author esilva
 */
public interface TmEmpresaDao {

    
	   public abstract List listarEmpresa(List empresa);
	    public abstract List listarEmpresa(boolean flag, List empresa);    
	    public abstract TmEmpresa selectEmpresaByCode(String code);
	    public abstract TmEmpresa selectEmpresas(String codigo);

	    //jwong 17/02/2009 para manejo de codigo de cliente
	    public abstract List listarClienteEmpresa(List empresa);
	    //jwong 26/02/2009 seleccion de todos los codigos de empresa
	    public abstract List selectCodEmpresas();
	    public abstract String selectCodEmpresas(String cemidempresa);
	    public String obtenerCodCliente(String ruc);
	    List<TmEmpresa> buscarTodos();
	    
	    public boolean verificaSiTarjetaCash(String numTarjeta);
}

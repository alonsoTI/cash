/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import com.hiper.cash.domain.TmFuncionario;

/**
 *
 * @author jwong
 */
public interface TmFuncionarioDao {
    
    
    public abstract TmFuncionario selectFuncionarioByEmpresaAndType(String empresa, String tipoFuncionario);
}

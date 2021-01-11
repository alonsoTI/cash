/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import java.util.List;
import java.util.Map;

import com.hiper.cash.domain.TxListField;

/**
 *
 * @author jwong
 */
public interface TxListFieldDao {
    
    public abstract List selectListFieldByFieldName(String dlfFieldName);
    public abstract Map<String,String> mapaMoneda();
    public abstract List<TxListField> listaMoneda();

    public abstract TxListField selectListField(String idFieldName, String code);	   

    //jwong 30/04/2009 para manejo de los tipod de cuebtas en caso de CTS y Planilla
    public abstract List selectListFieldByFieldName3(String dlfFieldName, String tipo);
}
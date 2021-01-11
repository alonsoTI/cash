/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import com.hiper.cash.domain.TxParameter;

/**
 *
 * @author jwong
 */
public interface TxParameterDao {
    public abstract TxParameter selectById(String cPmParameterId);
}

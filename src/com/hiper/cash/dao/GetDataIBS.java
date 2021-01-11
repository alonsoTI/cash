/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.xml.bean.BeanConsCtasCliente;
import java.util.List;

/**
 *
 * @author esilva
 */
public interface GetDataIBS {

    //HOME_BANKING_WS
    public abstract List getEmpresas_Servicio(CashClientWS cws, List parameters, int ires);
    public abstract List getServicios_Empresas_Servicio(CashClientWS cws, List parameters, int ires);

    //CASH_WS
    //public abstract BeanConsCtasCliente getConsCtasCliente(CashClientWS cws, List parameters, int ires);
    //public abstract BeanConsCtasCliente getConsCtasCliente(CashClientWS cws, List parameters, int ires, String numTarjeta, String token, String ruc);
    public abstract BeanConsCtasCliente getConsCtasCliente(CashClientWS cws, List parameters, int ires);

    //JWONG
    public List getServicios_Empresas_Servicio(CashClientWS cws, List parameters, int ires, String tipoEntidad);
    public List getSectores_Proveedor(CashClientWS cws, List parameters, int ires);
}

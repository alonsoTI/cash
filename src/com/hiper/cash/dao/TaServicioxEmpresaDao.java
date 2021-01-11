/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao;

import java.sql.SQLException;
import java.util.List;

import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.domain.TmServicio;

/**
 *
 * @author esilva
 */
public interface TaServicioxEmpresaDao {
    
    public abstract List selectServicioxEmpresaxTipo(String empresa, List tipo);
    public abstract List selectServicioxEmpresaxTipo(String empresa, String tipo);
    //jmoreno 21/07/09
    public abstract List selectServicioxEmpresaxAprobador(String codUsuario);
    public abstract List selectServicioxEmpresaxCode(String empresa, String code);    
    public abstract List selectCodeServicioxEmpresa(String empresa);
    public abstract TaServicioxEmpresa selectServicioxEmpresa(String empresa, String servicio);
    public abstract TaServicioxEmpresa selectServicioxEmpresa(String empresa, long servemp);
    public abstract String[] selectTipoServicioxEmpresa(long servemp);       
    public abstract long selectCodeServicioxEmpresa_2(String idEmpresa,String idServicio);
    //jwong 09/05/2009 para busqueda por el estado(para comprobantes - busca todos los estados)
    public abstract List selectServicioxEmpresaxTipoxEstado(String empresa, List tipo, String estado);
    public abstract List selectServEmpByIdServ(String empresa,List listaIdServ);
    //public abstract List selectEmpresasByIdServ(List lempresa,String idServicio);
    public abstract int selectCodFormatoOut(long servemp);    
    public abstract int selectCountCTS(String servemp);
    

    
    TaServicioxEmpresa getServicioEmpresa(long idServicioEmpresa) throws Exception;
    TmEmpresa getEmpresa( long idServicioEmpresa ) throws Exception;
    TmEmpresa getEmpresa( String codigo) throws Exception;
    TmServicio getServicio( long idServicioEmpresa) throws Exception;
    List<Object[]> buscarServiciosTipo(String empresa,String tipo)throws SQLException;
    TaServicioxEmpresa buscarServicioEmpresa(String empresa, String servicio)throws SQLException;
    
    List<Object[]>  buscarEmpresasTransferencias()throws SQLException;
    
    public abstract List selectEmpresasByIdServ(boolean flag, List lempresa,String idServicio);
}

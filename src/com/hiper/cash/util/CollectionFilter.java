/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.util;
import org.apache.commons.collections.Predicate;

/**
 *
 * @author Elvis Silva
 */
public class CollectionFilter implements Predicate{

    private String id;
    private String id1;
    private String id2;
    private int tipo;

    public CollectionFilter(String id, int tipo) {
        this.id = id;
        this.tipo = tipo;
    }
    public CollectionFilter(String id, String id1, String id2, int tipo) {
        this.id = id;
        this.id1 = id1;
        this.id2 = id2;
        this.tipo = tipo;
    }
    public boolean evaluate(Object object) {
        boolean returnValue = false;
        if(object instanceof com.hiper.cash.domain.TaCuentasServicioEmpresa){
            com.hiper.cash.domain.TaCuentasServicioEmpresa vo = (com.hiper.cash.domain.TaCuentasServicioEmpresa)object;
            switch (tipo){
                case 1: returnValue = vo.getId().getDcsemNumeroCuenta().trim().compareTo(id.toString())==0?true:false;
                        break;
                default : returnValue = false;
            }
        }else if(object instanceof com.hiper.cash.domain.TmEmpresa){
            com.hiper.cash.domain.TmEmpresa vo = (com.hiper.cash.domain.TmEmpresa)object;
            switch (tipo){
                case 1: returnValue = vo.getCemIdEmpresa().trim().compareTo(id.toString())==0?true:false;
                        break;
                default : returnValue = false;
            }
        }else if(object instanceof com.hiper.cash.xml.bean.BeanAccount){
            com.hiper.cash.xml.bean.BeanAccount vo = (com.hiper.cash.xml.bean.BeanAccount)object;
            switch (tipo){
                case 1: returnValue = vo.getM_AccountCode().trim().compareTo(id.toString())==0?true:false;
                        break;
                default : returnValue = false;
            }
        }else if(object instanceof com.hiper.cash.xml.bean.BeanRespuestaWSHomeBankingXML){
            com.hiper.cash.xml.bean.BeanRespuestaWSHomeBankingXML vo = (com.hiper.cash.xml.bean.BeanRespuestaWSHomeBankingXML)object;
            switch (tipo){
                case 1: returnValue = vo.getM_Codigo().trim().compareTo(id.toString())==0?true:false;
                        break;
                default : returnValue = false;
            }
        }else if(object instanceof com.hiper.cash.entidad.BeanDetalleOrden){
            com.hiper.cash.entidad.BeanDetalleOrden vo = (com.hiper.cash.entidad.BeanDetalleOrden)object;
            switch (tipo){
                case 1: returnValue = (vo.getM_IdOrden().trim().compareTo(id.toString())==0 &&
                                      vo.getM_IdServicio().trim().compareTo(id1.toString())==0 &&
                                      vo.getM_IdDetalleOrden().trim().compareTo(id2.toString())==0 )?true:false;
                        break;
                default : returnValue = false;
            }
        }

        return returnValue;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.dao.ws.ibs;

import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.clienteWS.GenRequestXML;
import com.hiper.cash.dao.GetDataIBS;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.xml.bean.BeanConsCtasCliente;
import com.hiper.cash.xml.bean.BeanRespuestaXML;
import com.hiper.cash.xml.parser.ParserXML;
import java.util.List;

/**
 *
 * @author esilva
 */
public class GetDataIBSFinanciero implements GetDataIBS{

    //HOME_BANKING_WS
    public List getEmpresas_Servicio(CashClientWS cws, List parameters, int ires){
        if (cws == null){
            ires = 1;//No se puede conectar al servicio web del IBS
            return null;
        }

        String req = GenRequestXML.getXMLLogin(parameters);
        String response = cws.ProcesarMensaje(req,Constantes.WEB_SERVICE_BANCO);

        if(response==null || "".equals(response)){
            ires = 2;
            return null;//Resultado del WS IBS es nulo o no hay respuesta
        }

        List listaResponse = ParserXML.parsearRespuestaWSHomeBanking(response);
        if(listaResponse!=null && listaResponse.size()>0){
            ires = 0;
            return listaResponse;
        }
        else{
            ires = 3;
            return null;//Error parseo de la respuesta
        }
    }
    public List getServicios_Empresas_Servicio(CashClientWS cws, List parameters, int ires){
        if (cws == null){
            ires = 1;//No se puede conectar al servicio web del IBS
            return null;
        }

        String req = GenRequestXML.getXMLLogin(parameters);
        String response = cws.ProcesarMensaje(req,Constantes.WEB_SERVICE_BANCO);

        if(response==null || "".equals(response)){
            ires = 2;
            return null;//Resultado del WS IBS es nulo o no hay respuesta
        }

        List listaResponse = ParserXML.parsearRespuestaWSHomeBanking2(response);
        if(listaResponse!=null && listaResponse.size()>0){
            ires = 0;
            return listaResponse;
        }
        else{
            ires = 3;
            return null;//Error parseo de la respuesta
        }
    }
    
    //CASH_WS
    //public BeanConsCtasCliente getConsCtasCliente(CashClientWS cws, List parameters, int ires, String numTarjeta, String token, String ruc){
    public BeanConsCtasCliente getConsCtasCliente(CashClientWS cws, List parameters, int ires){
        if (cws == null){
            ires = 1;//No se puede conectar al servicio web del IBS
            return null;
        }

        String req = GenRequestXML.getXML(parameters);
        String response = cws.ProcesarMensaje(req,Constantes.WEB_SERVICE_BANCO);

        if(response==null || "".equals(response)){
            ires = 2;
            return null;//Resultado del WS IBS es nulo o no hay respuesta
        }

        BeanRespuestaXML beanResponse = ParserXML.parsearRespuesta(response);
        if(beanResponse!=null && "00".equals(beanResponse.getM_CodigoRetorno())){
            //BeanConsCtasCliente beanctascliente = ParserXML.getConsCtasCliente(beanResponse.getM_Respuesta(), cws, numTarjeta, token, ruc);
            BeanConsCtasCliente beanctascliente = ParserXML.getConsCtasClienteCombos(beanResponse.getM_Respuesta());
            ires = 0;
            return beanctascliente;
        }
        else{
            ires = 3;
            return null;//Error parseo de la respuesta
        }
    }

    //JWONG
    public List getServicios_Empresas_Servicio(CashClientWS cws, List parameters, int ires, String tipoEntidad){
        if (cws == null){
            ires = 1;//No se puede conectar al servicio web del IBS
            return null;
        }

        String req = GenRequestXML.getXMLLogin(parameters);
        String response = cws.ProcesarMensaje(req,Constantes.WEB_SERVICE_BANCO);

        if(response==null || "".equals(response)){
            ires = 2;
            return null;//Resultado del WS IBS es nulo o no hay respuesta
        }

        List listaResponse = ParserXML.parsearRespuestaWSHomeBanking3(response, tipoEntidad);
        if(listaResponse!=null && listaResponse.size()>0){
            ires = 0;
            return listaResponse;
        }
        else{
            ires = 3;
            return null;//Error parseo de la respuesta
        }
    }
    public List getSectores_Proveedor(CashClientWS cws, List parameters, int ires){
        if (cws == null){
            ires = 1;//No se puede conectar al servicio web del IBS
            return null;
        }

        String req = GenRequestXML.getXMLLogin(parameters);
        String response = cws.ProcesarMensaje(req,Constantes.WEB_SERVICE_BANCO);

        if(response==null || "".equals(response)){
            ires = 2;
            return null;//Resultado del WS IBS es nulo o no hay respuesta
        }

        List listaResponse = ParserXML.parsearRespuestaWSHomeBanking4(response);
        if(listaResponse!=null && listaResponse.size()>0){
            ires = 0;
            return listaResponse;
        }
        else{
            ires = 3;
            return null;//Error parseo de la respuesta
        }
    }
}

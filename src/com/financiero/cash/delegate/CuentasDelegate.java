package com.financiero.cash.delegate;

import java.util.ArrayList;
import java.util.List;

import com.financiero.cash.exception.NotFoundException;
import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.clienteWS.GenRequestXML;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.xml.bean.BeanAccount;
import com.hiper.cash.xml.bean.BeanConsCtasCliente;
import com.hiper.cash.xml.bean.BeanNodoXML;
import com.hiper.cash.xml.bean.BeanRespuestaXML;
import com.hiper.cash.xml.parser.ParserXML;

public class CuentasDelegate {

	public CashClientWS cliente; 
	
	public CuentasDelegate() {
		
	}
	
	public void setCliente(CashClientWS cliente) {
		this.cliente = cliente;
	}
	
	public BeanConsCtasCliente obtenerCuentasCliente(String codigoEmpresa) throws Exception{		
        List<BeanNodoXML> listaParametros = new ArrayList<BeanNodoXML>();
        listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_CONS_CTAS_ASOC_CLIENTE));
        listaParametros.add(new BeanNodoXML("client_code", codigoEmpresa));       
        
        String req = GenRequestXML.getXML(listaParametros);
        String resultado = cliente.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);        
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
        BeanConsCtasCliente beanctascliente = ParserXML.getConsCtasClienteCombos(beanResXML.getM_Respuesta());
        
        if( beanResXML==null || !"00".equals(beanResXML.getM_CodigoRetorno()) ){
        	throw new NotFoundException();
        }
        List<BeanAccount> listaccounts = beanctascliente.getM_Accounts();
        if( listaccounts ==  null ){
        	listaccounts = new ArrayList<BeanAccount>();
        }
        return beanctascliente;
	}
	
}

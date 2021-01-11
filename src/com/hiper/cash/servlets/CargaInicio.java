/*
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.servlets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import com.financiero.cash.beans.TipoDocumentoBean;
import com.financiero.cash.ibs.util.TipoDocumento;
import com.financiero.cash.util.EstadoTransferencia;
import com.financiero.cash.util.TipoMoneda;
import com.financiero.cash.util.TipoTransferencia;
import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.dao.TxListFieldDao;
import com.hiper.cash.dao.hibernate.TxListFieldDaoHibernate;
import com.hiper.cash.domain.TxListField;
import com.hiper.cash.entidad.Propiedades;


public class CargaInicio extends HttpServlet {

    static Logger logger = Logger.getLogger(CargaInicio.class);
    /*
     * Metodo de inicializacion del servlet
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            ServletContext contexto = config.getServletContext();
            //creando el objeto propiedades
            Propiedades pro = new Propiedades();
            // obteniendo acceso al archivo financiero.properties
            ResourceBundle res = ResourceBundle.getBundle("financiero");

            //jwong 16/01/2009
            //numero de decimales entregados por el webservice(para ser formateado)
            try {
                pro.setM_NroDecimalesWS(Integer.parseInt(res.getString("nroDecimalesWS").trim()));               
                pro.setConsultaAfiliacionTCO( Boolean.parseBoolean(res.getString("consultaAfiliacionTCO")));
                pro.setConsultaTCO( Boolean.parseBoolean(res.getString("consultaTCO")));
                pro.setLimiteIntentosTCO( Integer.parseInt(res.getString("limiteIntentosTCO")));
                pro.setNumeroBloqueosTCO(Integer.parseInt(res.getString("limiteBloqueosTCO")));
            } catch (Exception nfe) {
                pro.setM_NroDecimalesWS(-1);
                logger.error(nfe,nfe);
            }            
            //jwong 18/01/2009 formato de la fecha con la que llega del webservice
            pro.setM_FormatoFechaWS(res.getString("formatoFechaWS"));

            //jwong 18/01/2009 formato de la hora con la que llega del webservice
            pro.setM_FormatoHoraWS(res.getString("formatoHoraWS"));

            //esilva
            pro.setM_FormatoFechaWS1(res.getString("formatoFechaWS1"));

            //jwong 18/02/2009 formato de fecha de ultimos movimientos que llega del webservice
            pro.setM_FormatoFechaWSLastMov(res.getString("formatoFechaWSLastMov"));

            //jwong 25/02/2009 para manejo de la ruta del manual Cash
            pro.setM_RutaManual(res.getString("rutaManualCash"));
            pro.setM_RutaDemoFlash(res.getString("rutaDemoFlash"));

            pro.setM_TimeTipoCambio(res.getString("timeTipoCambio"));
            
            //registrando el objeto pro en el contexto del servlet
            config.getServletContext().setAttribute("propiedades", pro);
            
            TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
            List<TxListField> listaMoneda = listFieldDAO.listaMoneda();            
            Map<String,String> mapa = getMapaMoneda(listaMoneda);      
            contexto.setAttribute("listaMoneda", listaMoneda);            
            contexto.setAttribute("mapaMoneda", mapa);
            contexto.setAttribute("tiposTransferencia", getTiposTransferencia());
            contexto.setAttribute("estadosTransferencia", getEstados());			
            contexto.setAttribute("tiposDocumento", getTiposDocumento());
            contexto.setAttribute("tiposDocumento2", getTiposDocumentoIT());
            contexto.setAttribute("tiposMoneda", getTiposMoneda());
            
            //CASH WEB SERVICE
            logger.info("Constructor WS CASH");
            CashClientWS cashclienteWS = new CashClientWS(
                    res.getString("APP_Servicio_Nombre"),
                    res.getString("APP_Servicio_NombrePuerto"),
                    res.getString("APP_Servicio_Localizacion"),
                    res.getString("APP_Servicio_Operacion_Nombre"),
                    res.getString("APP_Servicio_Operacion_NombreParametro"),
                    res.getString("APP_Servicio_TargetNameSpace"),
                    res.getString("APP_Servicio_XML_Parseo"),
                    res.getString("APP_Servicio_SoapVersion"),
                    res.getString("APP_Servicio_Cabecera"),
                    res.getString("APP_Servicio_Encoding"));
            logger.info("Success WS IBS");
            config.getServletContext().setAttribute("cashclienteWS", cashclienteWS);

            //Login
            logger.info("Constructor WS LOGIN");
            CashClientWS clienteLoginWS = new CashClientWS(
                    res.getString("APP_LOG_BCO_Servicio_Nombre"),
                    res.getString("APP_LOG_BCO_Servicio_NombrePuerto"),
                    res.getString("APP_LOG_BCO_Servicio_Localizacion"),
                    null,
                    res.getString("APP_LOG_BCO_Servicio_Operacion_NombreParametro"),
                    res.getString("APP_LOG_BCO_Servicio_TargetNameSpace"),
                    res.getString("APP_LOG_BCO_Servicio_XML_Parseo"),
                    res.getString("APP_LOG_BCO_SoapVersion"),
                    res.getString("APP_LOG_BCO_Servicio_Cabecera"),
                    res.getString("APP_LOG_BCO_Servicio_Encoding"));
            logger.info("Success WS LOGIN");
            config.getServletContext().setAttribute("clienteLoginWS", clienteLoginWS);

            //HomeBanking
            logger.info("Constructor WS HOMEBANKING");
            CashClientWS clienteHomeBankingWS = new CashClientWS(
                    res.getString("APP_CONS_SERV_Servicio_Nombre"),
                    res.getString("APP_CONS_SERV_Servicio_NombrePuerto"),
                    res.getString("APP_CONS_SERV_Servicio_Localizacion"),
                    null,
                    res.getString("APP_CONS_SERV_Servicio_Operacion_NombreParametro"),
                    res.getString("APP_CONS_SERV_Servicio_TargetNameSpace"),
                    res.getString("APP_CONS_SERV_Servicio_XML_Parseo"),
                    res.getString("APP_CONS_SERV_SoapVersion"),
                    res.getString("APP_CONS_SERV_Servicio_Cabecera"),
                    res.getString("APP_CONS_SERV_Servicio_Encoding"));
            logger.info("Success WS HOMEBANKING");
            config.getServletContext().setAttribute("clienteHomeBankingWS", clienteHomeBankingWS);

            config.getServletContext().setAttribute("LOG_BCO_Servicio_Operacion_Nombre", res.getString("APP_LOG_BCO_Servicio_Operacion_Nombre"));
            config.getServletContext().setAttribute("LOG_BCO_OP_CambioClave", res.getString("APP_LOG_BCO_Servicio_Operacion_Cambio_Clave"));
            config.getServletContext().setAttribute("LOG_BCO_OP_GeneraClave", res.getString("APP_LOG_BCO_Servicio_Operacion_Genera_Clave"));
            config.getServletContext().setAttribute("CONS_SERV_Servicio_Operacion1_Nombre", res.getString("APP_CONS_SERV_Servicio_Operacion1_Nombre"));
            config.getServletContext().setAttribute("CONS_SERV_Servicio_Operacion2_Nombre", res.getString("APP_CONS_SERV_Servicio_Operacion2_Nombre"));
            config.getServletContext().setAttribute("CONS_SERV_Servicio_Operacion3_Nombre", res.getString("APP_CONS_SERV_Servicio_Operacion3_Nombre"));

            //jwong

            //Login
            logger.info("Constructor WS SEGURIDAD");
            CashClientWS clienteSeguridadWS = new CashClientWS(
                    res.getString("APP_SEG_BCO_Servicio_Nombre"),
                    res.getString("APP_SEG_BCO_Servicio_NombrePuerto"),
                    res.getString("APP_SEG_BCO_Servicio_Localizacion"),
                    null,
                    res.getString("APP_SEG_BCO_Servicio_Operacion_NombreParametro"),
                    res.getString("APP_SEG_BCO_Servicio_TargetNameSpace"),
                    res.getString("APP_SEG_BCO_Servicio_XML_Parseo"),
                    res.getString("APP_SEG_BCO_SoapVersion"),
                    res.getString("APP_SEG_BCO_Servicio_Cabecera"),
                    res.getString("APP_SEG_BCO_Servicio_Encoding"));
            logger.info("Success WS SEGURIDAD");
            config.getServletContext().setAttribute("clienteSeguridadWS", clienteSeguridadWS);
            config.getServletContext().setAttribute("SEG_BCO_OP_CambioClave", res.getString("APP_SEG_BCO_Servicio_Operacion_Cambio_Clave"));
            config.getServletContext().setAttribute("SEG_BCO_OP_GeneraClave", res.getString("APP_SEG_BCO_Servicio_Operacion_Genera_Clave"));
        } catch (Exception e) {
            logger.error("InicioServlet.init :" + e.getMessage());
        }
    }
    
    private List<TipoDocumentoBean> getTiposDocumentoIT(){
    	List<TipoDocumentoBean> tipos = new ArrayList<TipoDocumentoBean>();    	
    	tipos.add(new TipoDocumentoBean("DNI"));
    	tipos.add(new TipoDocumentoBean("CIP"));
    	tipos.add(new TipoDocumentoBean("PAS"));
    	tipos.add(new TipoDocumentoBean("CE"));
    	tipos.add(new TipoDocumentoBean("RUC"));
    	return tipos;
    }
    
    private Map<String,String>  getMapaMoneda(List<TxListField> listaMoneda){
    	Map<String,String> mapa = new HashMap<String,String>();
        for( Object obj : listaMoneda){
        	TxListField tx = (TxListField)obj;
        	mapa.put(tx.getId().getClfCode(), tx.getDlfDescription());
        }
        
        return mapa;
    }
    
    private List<LabelValueBean> getEstados() {
		List<LabelValueBean> estados = new ArrayList<LabelValueBean>();
		for (EstadoTransferencia estado : EstadoTransferencia.values()) {
			estados.add(new LabelValueBean(estado.getNombre(), String.valueOf(estado.getValor())));
		}
		return estados;
	}

    private List<LabelValueBean> getTiposTransferencia() {
		List<LabelValueBean> tiposTransferencia = new ArrayList<LabelValueBean>();
		for (TipoTransferencia tipo : TipoTransferencia.values()) {
			tiposTransferencia.add(new LabelValueBean(tipo.getNombreAbreviado(), tipo.getValor()));
		}
		return tiposTransferencia;
	}
	
    private List<LabelValueBean> getTiposDocumento() {
		List<LabelValueBean> tiposDocumento = new ArrayList<LabelValueBean>();
		for (TipoDocumento tipo : TipoDocumento.values()) {
			tiposDocumento.add(new LabelValueBean(tipo.name(), String.valueOf(tipo.getCodigoIBS())));
		}
		return tiposDocumento;
	}
	
    private List<LabelValueBean> getTiposMoneda() {
		List<LabelValueBean> tiposMoneda = new ArrayList<LabelValueBean>();
		for (TipoMoneda tipo : TipoMoneda.values()) {
			tiposMoneda.add(new LabelValueBean(tipo.getLabel(), tipo.name()));
		}
		return tiposMoneda;
	}
}
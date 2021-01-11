/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.clienteWS;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;

import com.hiper.cash.dao.MensajesDAO;
import com.hiper.cash.dao.jdbc.MensajesDAOImpl;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Util;

/**
 *
 * @author jwong
 */
public class CashClientWS {
    private String  nombre;
    private String  puerto;
    private String  location;
    
    private String  operacion;
    private String  operacionParametro;
    private String  targetNameSpace;

    private String  xmlOperacion_inicial;
    private String  xmlOperacion_final;
    private String  xmlParseo;

    private QName svcQname;
    private QName portQName;

    private Transformer trans;
    //private static SAXBuilder builder;

    //jwong 09/02/2009 parametro adicional
    private String soapHttpBinding;
    private String  elemCabXml;

    static Logger logger = Logger.getLogger(CashClientWS.class);

    public CashClientWS(String servicioNombre, String servicioPuerto, String servicioLocation,
            String servicioOperacion, String servicioParametroOperacion, String servicioTargetNameSpace, String servicioParseoXML,
            String httpBinding, String servicioEleCabecera, String encoding){
        try {
            nombre = servicioNombre;
            puerto = servicioPuerto;
            location = servicioLocation;
            operacion = servicioOperacion;
            operacionParametro = servicioParametroOperacion;
            targetNameSpace = servicioTargetNameSpace;
            xmlParseo = servicioParseoXML;

//obtiene elemento extra de cabecera cgonzales 11/12/2008
            //proy 4441 Bfinanciero
            elemCabXml = servicioEleCabecera;

            String elemaux1 = "";
            String elemaux2 = "";
            if(!"".equals(elemCabXml)){
                elemaux1 = elemCabXml + ":";
                elemaux2 = ":" + elemCabXml;
            }
            xmlOperacion_inicial = "<"+ elemaux1 + "" + operacion + " xmlns"+ elemaux2 +"=\"" + targetNameSpace + "\">";//

            xmlOperacion_final = "</"+ elemaux1 + "" + operacion + ">";


            // Define the service.
            svcQname = new QName( getTargetNameSpace(), getNombre());
            portQName = new QName( getTargetNameSpace(), getPuerto());


            trans = TransformerFactory.newInstance().newTransformer();

            if(encoding != null)
            {                
                if (encoding.length() > 0 ){
                    trans.setOutputProperty(OutputKeys.ENCODING, encoding);
                }
            }
//            trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
//            trans.setOutputProperty(OutputKeys.ENCODING, "euc-kr");

            //builder = new SAXBuilder();

            //jwong 09/02/2009 parametro adicional
            soapHttpBinding = httpBinding;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String ProcesarMensaje(String xml_invocacion,String nombreWS){

        ////System.out.println("Parametro: xml_invocacion " + xml_invocacion);
        ////System.out.println("parametro nombreWS " + nombreWS);
        String resultado=null;
        
        try {
        	////System.out.println("entrando al try Catch" );

        	////System.out.println("getSvcQname()" + getSvcQname().getNamespaceURI() );
        	////System.out.println("getSvcQname()" + getSvcQname().getLocalPart());
        	////System.out.println("getSvcQname()" + getSvcQname().toString()  );
        	////System.out.println("new QName(namespaceURI, localPart) " +getTargetNameSpace() +" , " +getNombre()  );
        	Service svc = Service.create(getSvcQname());
            
            ////System.out.println("despues de crear el Servicio" );
            if("1.1".equals(getSoapHttpBinding())){
                    svc.addPort(
                    getPortQName(),
                    SOAPBinding.SOAP11HTTP_BINDING, getLocation());
            }else if("1.2".equals(getSoapHttpBinding())){
            	////System.out.println("soapBinding 1.2" );
                    svc.addPort(
                    getPortQName(),
                    SOAPBinding.SOAP12HTTP_BINDING, getLocation());
            }else{
                logger.error("[HPSWSCash.procesarMensaje()]: Error version SOAP XML "+getSoapHttpBinding() +" no soportada");
                return null;
            }

            ////System.out.println("portQName  " +getPortQName());
            ////System.out.println("location   " +getLocation() );
            ////System.out.println("soapHttpBinding  " +getSoapHttpBinding());
            
            // Create the dynamic invocation object from this service.
            Dispatch dispatch = svc.createDispatch(
                    getPortQName(),
                    Source.class,
//                    Service.Mode.MESSAGE);
                    Service.Mode.PAYLOAD);


            ////System.out.println("portQName " + getPortQName());
            ////System.out.println("PAYLOAD " + Service.Mode.PAYLOAD);


            // Build the message.

            String content =  xmlOperacion_inicial ;
            if ( getXmlParseo().equals("1") ){
                String tag_ini = "<" + getOperacionParametro() + ">";
                String tag_fin = "</" + getOperacionParametro() + ">";
//                String xml_core=null;
                int tam_tag_ini = tag_ini.length();
//                int tam_tag_fin = tag_fin.length();
                int i_tag_ini = xml_invocacion.indexOf(tag_ini);
                int i_tag_fin = xml_invocacion.lastIndexOf(tag_fin) +1;
                String xml_core = xml_invocacion.substring(i_tag_ini + tam_tag_ini, i_tag_fin-1);
                xml_invocacion = tag_ini + "<![CDATA[" + xml_core + "]]>" + tag_fin;

                content = content + xml_invocacion;
            }else{

//            if ( xmlParseo.equals("1") ){
               // content = content + "<![CDATA[";
//            }
            content = content + xml_invocacion;
//            if ( xmlParseo.equals("1") ){
                //content = content + "]]>";
//            }
            }
            content = content + xmlOperacion_final;

            ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
            Source input = new StreamSource(bais);
            StreamResult req = new StreamResult(new ByteArrayOutputStream());
            trans.transform(input, req);
            ByteArrayOutputStream baos1 = (ByteArrayOutputStream) req.getOutputStream();
            resultado = baos1.toString();
            ////System.out.println("Request: " +resultado);
            
            MensajesDAO dmensaje = new MensajesDAOImpl();
            
            dmensaje.registrarMensaje(Constantes.ID_SERVICIO_LOGIN, Constantes.ID_SERVICIO_INPUT, nombreWS, resultado);
            
            
            logger.info("REQUEST "+nombreWS+": "+resultado);

            // Invoke the operation.
            ////System.out.println("content    " + content);
            bais = new ByteArrayInputStream(content.getBytes());
            input = new StreamSource(bais);
            Source output=null;
            try{
            	if( input == null ){
            		//System.out.println("INPUT = NULL");
            	}
            output = (Source) dispatch.invoke(input);
            ////System.out.println("...............Envio Respuesta.........");
            }catch(Exception e){
            	e.printStackTrace();
            }

            // Process the response.
            StreamResult result = new StreamResult(new ByteArrayOutputStream());
//            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(output, result);
            ByteArrayOutputStream baos = (ByteArrayOutputStream) result.getOutputStream();

            resultado = baos.toString();
            
            ////System.out.println("...............resultado........." + resultado);
            resultado = resultado.replace("&lt;", "<");
            resultado = resultado.replace("&gt;", ">");
            resultado = resultado.replace("xsi:nil=\"true\"", "");

            //PLOPEZ 22-05-2008 retorno carro en html
            resultado = resultado.replace("&#13;", "");

            logger.info("RESPONSE "+nombreWS+": "+resultado);
            
            dmensaje.registrarMensaje(Constantes.ID_SERVICIO_LOGIN, Constantes.ID_SERVICIO_OUTPUT, nombreWS, resultado);
            
        }
        catch (Exception e){
            logger.error(e,e);            
            return null;
        }
        return resultado;
    }
    
    public String procesarMensaje(String xml_invocacion) throws Exception{
       
        String resultado=null;        
        try {        	
        	Service svc = Service.create(getSvcQname());    
           
            if("1.1".equals(getSoapHttpBinding())){
                    svc.addPort(
                    getPortQName(),
                    SOAPBinding.SOAP11HTTP_BINDING, getLocation());
            }else if("1.2".equals(getSoapHttpBinding())){
                    svc.addPort(
                    getPortQName(),
                    SOAPBinding.SOAP12HTTP_BINDING, getLocation());
            }else{
                //logger.error("[HPSWSCash.procesarMensaje()]: Error version SOAP XML "+getSoapHttpBinding() +" no soportada");
                throw new Exception(new StringBuilder().append("[HPSWSCash.procesarMensaje()]: Error version SOAP XML ").append(getSoapHttpBinding()).append(" no soportada").toString());
                //return null;
            }
            
            Dispatch dispatch = svc.createDispatch(getPortQName(), Source.class, Service.Mode.PAYLOAD);

            String content =  xmlOperacion_inicial ;
            if ( getXmlParseo().equals("1") ){
                String tag_ini = "<" + getOperacionParametro() + ">";
                String tag_fin = "</" + getOperacionParametro() + ">";
                int tam_tag_ini = tag_ini.length();
                int i_tag_ini = xml_invocacion.indexOf(tag_ini);
                int i_tag_fin = xml_invocacion.lastIndexOf(tag_fin) +1;
                String xml_core = xml_invocacion.substring(i_tag_ini + tam_tag_ini, i_tag_fin-1);
                xml_invocacion = tag_ini + "<![CDATA[" + xml_core + "]]>" + tag_fin;
                content = content + xml_invocacion;
            }
            content = content + xmlOperacion_final;

            ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
            Source input = new StreamSource(bais);
            StreamResult req = new StreamResult(new ByteArrayOutputStream());
            trans.transform(input, req);
            ByteArrayOutputStream baos1 = (ByteArrayOutputStream) req.getOutputStream();
            resultado = baos1.toString();
            logger.info(Util.strRequest(xml_invocacion));
            //logger.info("REQUEST "+nombreWS+": "+resultado);

            
            bais = new ByteArrayInputStream(content.getBytes());
            input = new StreamSource(bais);
            Source output=null;                   	
            output = (Source) dispatch.invoke(input);            
            

            // Process the response.
            StreamResult result = new StreamResult(new ByteArrayOutputStream());
            trans.transform(output, result);
            ByteArrayOutputStream baos = (ByteArrayOutputStream) result.getOutputStream();

            resultado = baos.toString();
            
            resultado = resultado.replace("&lt;", "<");
            resultado = resultado.replace("&gt;", ">");
            resultado = resultado.replace("xsi:nil=\"true\"", "");

            resultado = resultado.replace("&#13;", "");
            logger.info(Util.strResponse(resultado));
            //logger.info("RESPONSE "+nombreWS+": "+resultado);
        }
        catch (Exception e){
            throw e;	           
        }
        return resultado;
    }

    /**
     * @return the operacion
     */
    public String getOperacion() {
        return operacion;
    }

    /**
     * @param operacion the operacion to set
     */
    public void setOperacion(String operacion) {
        this.operacion = operacion;

        String elemaux1 = "";
        String elemaux2 = "";
        if(!"".equals(elemCabXml)){
            elemaux1 = elemCabXml + ":";
            elemaux2 = ":" + elemCabXml;
        }
        this.xmlOperacion_inicial = "<"+ elemaux1 + "" + this.operacion + " xmlns"+ elemaux2 +"=\"" + getTargetNameSpace() + "\">";//

        this.xmlOperacion_final = "</"+ elemaux1 + "" + this.operacion + ">";
    }

    public static void main(String args[]){
        /*
        CashClientWS cashclienteWS = new CashClientWS(
                                        "CashWSService",
                                        "CashWSPort",
                                        //"http://192.168.61.64:8084/CashWS/CashWS",
                                        "http://192.168.61.24:8080/CashWS/CashWS",
                                        //"http://localhost:8080/CashWS/CashWS",
                                        //"http://esilva:8080/CashWS/CashWS",
                                        "ProcesarMensaje",
                                        "archivoT",
                                        "http://servicio.cash.hiper.com/",
                                        "0",
                                        "1.1","ns2","");

                                        
//        String req = "<data>" +
//                     "<![CDATA[" + "<" + "]]>request>" +
//                     "<![CDATA[" + "<" + "]]>id_trx>" +
//                        "LOGIN_CASH" +
//                     "<![CDATA[" + "<" + "]]>/id_trx>" +
//
//                     "<![CDATA[" + "<" + "]]>cod_lang>" +
//                        "es" +
//                     "<![CDATA[" + "<" + "]]>/cod_lang>" +
//
//                     "<![CDATA[" + "<" + "]]>ntarjeta>" +
//                        "123456" +
//                     "<![CDATA[" + "<" + "]]>/ntarjeta>" +
//                     "<![CDATA[" + "<" + "]]>password>" +
//                        "7FAF93F3C7E8B61D" +
//                     "<![CDATA[" + "<" + "]]>/password>" +
//                     "<![CDATA[" + "<" + "]]>/request>" +
//                     "</data>";
        
//        String req =
//            "<data>" +
//            "<![CDATA[<]]>request>" +
//                "<![CDATA[<]]>id_trx>LOGIN_CASH<![CDATA[<]]>/id_trx>" +
//                "<![CDATA[<]]>cod_lang>es<![CDATA[<]]>/cod_lang>" +
//                "<![CDATA[<]]>ntarjeta>12345<![CDATA[<]]>/ntarjeta>" +
//                "<![CDATA[<]]>password>8700B854E9F89358<![CDATA[<]]>/password>" +
//            "<![CDATA[<]]>/request>" +
//            "</data>";


        String req = "<data>" +
                     "<![CDATA[" + "<" + "]]>request>" +
                     "<![CDATA[" + "<" + "]]>id_trx>" +
                        "CONS_CTAS_CLIENTE" + //saldos
                        //"CONS_DET_SALDOS" + //saldos

                        //"CONS_COD_INTERBANC" + //cod interbancarios
                        //"CONS_REL_BANCO" + //relaciones con el banco
                        //"CONS_TIPO_CAMBIO" + //tipo de cambio


                        //"CONS_MOV_CTAS" + //ERROR EN LA DATA QUE

                        //"CONS_MOV_HISTORICOS" + //FALTA IMPLEMENTAR

                        //"CONS_EST_CUENTA" + //estado cuenta

                        //"CONS_PAG_SERVICIOS" +
                        //"CONS_DATOS_EMPRESA" +
                        
                     "<![CDATA[" + "<" + "]]>/id_trx>" +


//                     "<![CDATA[" + "<" + "]]>client_code>" +
//                        "20131415153" +
//                     "<![CDATA[" + "<" + "]]>/client_code>" +



//                     "<![CDATA[" + "<" + "]]>request_number>" +
//                        "20131415153" +
//                     "<![CDATA[" + "<" + "]]>/request_number>" +
//
//                     "<![CDATA[" + "<" + "]]>client_code>" +
//                        "20131415153" +
//                     "<![CDATA[" + "<" + "]]>/client_code>" +
//
//                     "<![CDATA[" + "<" + "]]>secuencial>" +
//                        "20131415153" +
//                     "<![CDATA[" + "<" + "]]>/secuencial>" +



                     "<![CDATA[" + "<" + "]]>/request>" +
                     "</data>";
        */

        /*
        CashClientWS cashclienteWS = new CashClientWS(
                                        "WS_HomeBanking",
                                        "WS_HomeBankingSoap12",
                                        //"http://192.168.61.64:8084/CashWS/CashWS",
                                        "http://192.168.61.64/SimHomeBankingWS/service.asmx",
                                        //"http://localhost:8080/CashWS/CashWS",
                                        //"http://esilva:8080/CashWS/CashWS",
                                        "listarEmpresas",
                                        "archivoT",
                                        "http://tempuri.org/",
                                        "0",
                                        "1.2","","");

        String req = 
                "<strGrupo>agua</strGrupo>" +
                "<strEmpresa></strEmpresa>";
        */

        /*
        CashClientWS cashclienteWS = new CashClientWS(
//                                        "WS_Login",
//                                        "WS_LoginSoap12",
//                                        //"http://192.168.61.64:8084/CashWS/CashWS",
//                                        "http://192.168.61.64/SimLoginCashWS/Service.asmx",
//                                        //"http://localhost:8080/CashWS/CashWS",
//                                        //"http://esilva:8080/CashWS/CashWS",
//                                        "validarAccesoCash",
//                                        "archivo",
//                                        "http://tempuri.org/",
//                                        "0",
//                                        "1.2","","");
                                        "WS_HomeBanking",
                                        "WS_HomeBankingSoap12",
                                        //"http://192.168.61.64:8084/CashWS/CashWS",
                                        "http://192.168.61.64/SimHomeBankingWS/service.asmx",
                                        //"http://localhost:8080/CashWS/CashWS",
                                        //"http://esilva:8080/CashWS/CashWS",
                                        "listarEmpresas",
                                        "archivo",
                                        "http://tempuri.org/",
                                        "0",
                                        "1.2","","");
        */

        
        CashClientWS cashclienteWS = new CashClientWS(
                                        "WS_Login",
                                        "WS_LoginSoap12",
                                        //"http://172.16.25.145:10/WS_CM/WS_CM_Seguridad.asmx",
                                        //"http://jayastag/ws_cm/WS_AfiliarTarjeta.asmx",
                                        "http://svaplbfdes01:90/BFP.WS.CM/WS_CM_Seguridad.asmx",
                                        "CM_Login6Digitos",
                                        "archivo",
                                        "http://tempuri.org/",
                                        "0",
                                        "1.2","", "");
        
        String req =
                "<EPad>9035990000000457</EPad><EPin>58B224901E3BAB5A</EPin><strURL>http://localhost:8080/PortalCash/login.do</strURL><strFormulario></strFormulario><strQueryString>do=iniciarSesion</strQueryString><strServerName>localhost</strServerName><strUserAgent>Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 1.1.4322)</strUserAgent><strUserIP>127.0.0.1</strUserIP><strUserHostName>127.0.0.1</strUserHostName><strUserIsAutenticado></strUserIsAutenticado><strUserName></strUserName>" ;
          

        ////System.out.println("<<<=========================================================>>>");
        ////System.out.println("XML REQUERIMIENTO ==>>> " + req);
        String resultado = cashclienteWS.ProcesarMensaje(req,"WSBANCO");
        ////System.out.println("XML RESPUESTA ==>>> " + resultado);
//        if(resultado!=null){
//            BeanRespuestaWSLoginXML beanrespXML = ParserXML.parsearRespuestaWSLogin(resultado);
//            ////System.out.println("CODIGO DE RETORNO ==>>> " + beanrespXML.getM_CodRptaTx());
//            if(!"00".equals(beanrespXML.getM_CodRptaTx())){
//                ////System.out.println("DESCRIPCION ERROR ==>>> " + beanrespXML.getM_DescRptaTx());
//            }
//        }
//        ////System.out.println("<<<=========================================================>>>");
        

        /*
        CashClientWS cashclienteWS = new CashClientWS(
                                        "CashWSService",
                                        "CashWSPort",
                                        "http://saplbfdes04:8084/CashWS/CashWS",
                                        "ProcesarMensaje",
                                        "data",
                                        "http://servicio.cash.hiper.com/",
                                        "1",
                                        "1.1","ns2", "");


        String req = "<data><request><id_trx>CONS_MOV_HISTORICOS</id_trx><account_type>210</account_type><account>000294006699</account><query_type>T</query_type><begin_date>01012008</begin_date><end_date>16042009</end_date><ruc>20429057711</ruc></request></data>";
        
        //String req = "<data><request><id_trx>CONS_EST_CUENTA</id_trx><account_number>000294006699</account_number><transaction_date>20090416</transaction_date><ruc>20110572922</ruc></request></data>";
        //String req = "<data><request><id_trx>CONS_EST_CUENTA</id_trx><account_number>000357994787</account_number><transaction_date>20090416</transaction_date><ruc>20110572922</ruc></request></data>";
        
        String resultado = cashclienteWS.ProcesarMensaje(req);
        ////System.out.println(resultado);
        */

        /*
        BeanRespuestaXML beanrespXML = ParserXML.parsearRespuesta(resultado, false);
        ////System.out.println("Retorno ==>>> " + beanrespXML.getM_CodigoRetorno());
        */
        
        /*



         <Respuesta>
             <client_name>Gonzales Zapata Cesar Enrique </client_name>
             <delimitador1>*</delimitador1>
             <available_balance>00000001234567</available_balance>
             <sign_available_balance>+</sign_available_balance>
             <countable_balance>00000001234567</countable_balance>
             <sign_countable_balance>+</sign_countable_balance>
             <interest>00000001234567</interest>
             <sign_interest>-</sign_interest>
             <previous_balance>00000001234567</previous_balance>
             <sign_previous_balance>-</sign_previous_balance>
             <office_code>002</office_code>
             <agency_code>1</agency_code>

             <movements>
                 <movimiento>
                     <movement_code>4444</movement_code>
                     <movement_description>303030303030303030303030303030</movement_description>
                     <amount>14141414141414</amount>
                     <sign>1</sign>
                     <movement_date>081122</movement_date>
                     <movement_hour>5055</movement_hour>
                 </movimiento>
             </movements>
             <delimitador1>*</delimitador1>
         </Respuesta>
         */
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the puerto
     */
    public String getPuerto() {
        return puerto;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the xmlParseo
     */
    public String getXmlParseo() {
        return xmlParseo;
    }

    /**
     * @return the svcQname
     */
    public QName getSvcQname() {
        return svcQname;
    }

    /**
     * @return the portQName
     */
    public QName getPortQName() {
        return portQName;
    }

    /**
     * @return the soapHttpBinding
     */
    public String getSoapHttpBinding() {
        return soapHttpBinding;
    }

    /**
     * @return the operacionParametro
     */
    public String getOperacionParametro() {
        return operacionParametro;
    }

    /**
     * @return the targetNameSpace
     */
    public String getTargetNameSpace() {
        return targetNameSpace;
    }
}
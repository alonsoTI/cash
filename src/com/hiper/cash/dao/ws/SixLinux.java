package com.hiper.cash.dao.ws;


import static com.hiper.cash.util.CashConstants.RES_FINANCIERO;
import static com.hiper.cash.util.CashConstants.RES_IBS;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.tempuri.WsSIXN;
import org.tempuri.WsSIXNSoap;



public class SixLinux {	
	
	private Logger logger = Logger.getLogger(this.getClass());
	private static SixLinux instance=null;
	private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "ws_SIX_N");
	private WsSIXNSoap soapPort;
	
	
	private SixLinux() {
		String wsdlUrl = RES_FINANCIERO.getString("APP_SIX_Servicio_Localizacion")+"?wsdl";		
		
		/*try {
			soapPort = new WsSIXN(new URL(wsdlUrl), SERVICE_NAME).getWsSIXNSoap();			
		} catch (Exception e) {
			logger.error("Error creando cliente sixlinux",e);
			throw new RuntimeException("Error creando cliente sixlinux",e);			
		}*/
		
		
		
		//String respuesta = null;
	

		try {

			WsSIXN service1 = null;
			//WsSIXNSoap port1 = null;

			URL url = null;
			try {
				URL baseUrl;
				baseUrl = WsSIXN.class.getResource(".");
				url = new URL(baseUrl, wsdlUrl);

			} catch (MalformedURLException e) {
				e.printStackTrace();

			}

			service1 = new WsSIXN(url, new QName("http://tempuri.org/",
					"ws_SIX_N"));
			// System.out.println("Create Web Service...");
			soapPort = service1.getWsSIXNSoap();	
			
			
		} catch (Exception ex) {
			//respuesta = null;
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		
		
		
		
	}
	
	public static SixLinux getInstance() {
	      if(instance == null) {
	         instance = new SixLinux();
	      }
	      return instance;
	}
	
	public String getToken(String pad,String pin)throws Exception{
		String ruta = RES_FINANCIERO.getString("APP_SIX_Servicio_Localizacion")+"?wsdl";		
		
		
		/*try{
			URL url =  new URL(ruta);
			WsSIXN s = new WsSIXN(url);
	        WsSIXNSoap puerto = s.getWsSIXNSoap12();	        
	        return puerto.setToken(pad, pin);	 
	        
	        
		}catch(MalformedURLException e){
			throw new Exception("El nombre de la direccion del servicio no es correcta");
		}catch(InaccessibleWSDLException e){
			throw new Exception("El servicio localizado en la ruta " + ruta + " no es accesible" );
		}catch(Exception e){
			throw e;
		}*/
		
		
		String respuesta = null;
	

		try {

			WsSIXN service1 = null;
			WsSIXNSoap port1 = null;

			URL url = null;
			try {
				URL baseUrl;
				baseUrl = WsSIXN.class.getResource(".");
				url = new URL(baseUrl, ruta);

			} catch (MalformedURLException e) {
				e.printStackTrace();

			}

			service1 = new WsSIXN(url, new QName("http://tempuri.org/",
					"ws_SIX_N"));
			// System.out.println("Create Web Service...");
			port1 = service1.getWsSIXNSoap();

			respuesta = port1.setToken(pad, pin);

		} catch (Exception ex) {
			respuesta = null;
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}

		return respuesta;
							
	}
	
	
	public String enviarMensaje(String app, String trx,short length,String message){
		
		String respuesta=null;
		try{
			
			logger.info("Trama Six.......");
			logger.info("app="+app);
			logger.info("trx="+trx);
			logger.info("length="+String.valueOf(length));
			logger.info("mensaje=["+message+"]");			
			
			
			respuesta=soapPort.sendMessage(app, trx, length, message);
			
			logger.info("respuesta=["+respuesta+"]");	
			
		}catch(Exception e){
			respuesta=null;
			e.printStackTrace();			
		}
		return respuesta;
	}
	
		
	/*public String getTrama(String app, String trx,short length,String message) throws Exception{
		String mensaje = "";
		String ruta = RES_FINANCIERO.getString("APP_SIX_Servicio_Localizacion");
		try{
			URL url =  new URL(ruta);
			WsSIXN s = new WsSIXN(url);
	        WsSIXNSoap puerto = s.getWsSIXNSoap12();	 
	        logger.info(requestMensaje(app, trx, length, message));
	        mensaje  = puerto.sendMessage(app, trx, length, message);
	        logger.info(responseMensaje(mensaje));	        
		}catch(MalformedURLException e){
			throw new Exception("El nombre de la direccion del servicio no es correcta");
		}catch(InaccessibleWSDLException e){
			throw new Exception("El servicio localizado en la ruta " + ruta + " no es accesible" );
		}catch(Exception e){
			throw e;
		}		
		return mensaje;
	}*/
	
	/*public String getTramaNoLog(String app, String trx,short length,String message) throws Exception{
		String mensaje = "";
		String ruta = RES_FINANCIERO.getString("APP_SIX_Servicio_Localizacion");
		try{
			URL url =  new URL(ruta);
			WsSIXN s = new WsSIXN(url);
	        WsSIXNSoap puerto = s.getWsSIXNSoap12();   
	        mensaje  = puerto.sendMessage(app, trx, length, message);	        	        
		}catch(MalformedURLException e){
			throw new Exception("El nombre de la direccion del servicio no es correcta");
		}catch(InaccessibleWSDLException e){
			throw new Exception("El servicio localizado en la ruta " + ruta + " no es accesible" );
		}catch(Exception e){
			throw e;
		}		
		return mensaje;
	}*/
	
	public String getTrama(String propApp, String propTrx,String propLength,String message) throws Exception{
		String app = RES_IBS.getString(propApp);
		String trx = RES_IBS.getString(propTrx);
		short length = Short.parseShort(RES_IBS.getString(propLength));	
		String trama = enviarMensaje(app, trx, length, message);
		return trama;
	}
	
	public String getTramaNoLog(String propApp, String propTrx,String propLength,String message) throws Exception{
		String app = RES_IBS.getString(propApp);
		String trx = RES_IBS.getString(propTrx);
		short length = Short.parseShort(RES_IBS.getString(propLength));	
		String trama = enviarMensaje(app, trx, length, message);
		return trama;
	}
	
	
	/*private String requestMensaje(String app, String trx,short length,String message){
		StringBuilder sb = new StringBuilder("<request>").append("<app>").append(app).append("</app>");
		sb = sb.append("<trx>").append(trx).append("</trx>");
		sb = sb.append("<length>").append(length).append("</length>");
		sb = sb.append("<message>").append(message).append("</message>").append("</request>");
		return sb.toString();
	}*/
	
	/*private String responseMensaje(String mensaje){
		StringBuilder sb = new StringBuilder("<response>").append("<mensaje>").append(mensaje).append("</mensaje>").append("</response>");
		return sb.toString();		
	}*/
	
	/*public String sendMessage (String app, String trx,short length,String message){
		return soapPort.sendMessage(app, trx, length, message);
	}*/
	
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.util;

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

    
    
/*******************************************************************************
 * Nombre HCWSClient.java<br><br>
 * 
 * Descripcion: Es la interface de conexion entre el Batch y el CashWS. 
 *
 * @author cgonzales
 *
 * @version 0.1
 ******************************************************************************/
public class HCWSClient {
    
    private String nombre;
    private String puerto;
    private String location;
    private String operacion;
    private String targetNameSpace;
    private QName svcQname;
    private QName portQName;
    private Transformer trans;
    private String xmlOperacion_inicial;
    private String xmlOperacion_final;
    private String elemCabXml;
    

       
    /***************************************************************************
     * 
     * Descripcion: Construye instancias de la propia clase
     * 
     * @param servicioNombre Nombre del servicio WS
     * 
     * @param servicioPuerto nombre del puerto conel que se estable conecion
     * 
     * @param servicioLocation Ruta de la red en la que se establece la conexión
     * 
     * @param servicioOperacion Nombred del metodo que se invocara en el ws
     * 
     * @param servicioTargetNameSpace  direccion donde se almacna el target
     * 
     * @param servicioEleCabecera elemento extra del xml a formarse
     **************************************************************************/
    public HCWSClient(String servicioNombre, 
                      String servicioPuerto,
                      String servicioLocation,
                      String servicioOperacion,
                      String servicioTargetNameSpace,
                      String servicioEleCabecera){
        
        try {
            
            nombre = servicioNombre;
            puerto = servicioPuerto;
            location = servicioLocation;
            operacion = servicioOperacion;
            targetNameSpace = servicioTargetNameSpace;
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
            svcQname = new QName(targetNameSpace, nombre);
            portQName = new QName(targetNameSpace, puerto);
            trans = TransformerFactory.newInstance().newTransformer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
    /***************************************************************************
     * Nombre: ejecutarMetodoWS<br><br>
     * 
     * Descripción: Envia el mensaje de requrmiento y recepciona el de repuesta<br>
     * 
     * @param xmlRequest
     * 
     * @param soapVersion
     * 
     * @return cadena con el xml
     **************************************************************************/
    public String ejecutarMetodoWS (String xmlRequest, String soapVersion){
        String resultado=null;

        ////System.out.println("parametro xmlRequest: " + xmlRequest);
        ////System.out.println("parametro SoapVersion: " +soapVersion);
        try {      
            
            Service svc = Service.create(svcQname);
            if("1.1".equals(soapVersion)){
                    svc.addPort(
                    portQName, 
                    SOAPBinding.SOAP11HTTP_BINDING,
                    location);
            }else if("1.2".equals(soapVersion)){
                    svc.addPort(
                    portQName, 
                    SOAPBinding.SOAP12HTTP_BINDING,
                    location);                
            }else{
                ////System.out.println("[HPSWSCash.procesarMensaje()]: Error version SOAP XML "+soapVersion +" no soportada");
                return null;
            }
            // Create the dynamic invocation object from this service.
            Dispatch dispatch = svc.createDispatch(
                    portQName, 
                    Source.class, 
                    //Service.Mode.MESSAGE);
                    Service.Mode.PAYLOAD);
                    
            String content =  xmlOperacion_inicial ;
                                               
            content = content + xmlRequest;
            
            content = content + xmlOperacion_final;
            //System.err.println(content);
            ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
            Source input = new StreamSource(bais);
            StreamResult req = new StreamResult(new ByteArrayOutputStream());
            //cgonzales encoding para soportar caracteres extranios
            //trans.setOutputProperty(OutputKeys.ENCODING, "Windows-1258");
            //////System.out.println(""+trans.getOutputProperty(OutputKeys.ENCODING));
            trans.transform(input, req);            
            ByteArrayOutputStream baos1 = (ByteArrayOutputStream) req.getOutputStream();
            resultado = baos1.toString();
            ////System.out.println("XML OUTPUT: "+resultado);
            // Invoke the operation.
            bais = new ByteArrayInputStream(content.getBytes());
            input = new StreamSource(bais);
            Source output = (Source) dispatch.invoke(input);
            
            // Process the response.
            StreamResult result = new StreamResult(new ByteArrayOutputStream());
            //trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            trans.transform(output, result);
            ByteArrayOutputStream baos = (ByteArrayOutputStream) result.getOutputStream();
            resultado = baos.toString();
            resultado = resultado.replace("&lt;", "<");
            resultado = resultado.replace("&gt;", ">");
            resultado = resultado.replace("xsi:nil=\"true\"", "");
            resultado = resultado.replace("&#13;", "");
            ////System.out.println("XML INPUT: "+resultado);
        }
        catch (Exception e){
            ////System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
         }
               
        return resultado;
    }
}

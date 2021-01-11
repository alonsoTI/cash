/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.actions;

import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.clienteWS.GenRequestXML;
import com.hiper.cash.entidad.Propiedades;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.xml.bean.BeanNodoXML;
import com.hiper.cash.xml.bean.BeanRespuestaXML;
import com.hiper.cash.xml.bean.BeanTipoCambioXML;
import com.hiper.cash.xml.parser.ParserXML;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author jwong
 */
public class TipoCambioAction extends DispatchAction {
    /**
     * cambioActual, para obtener el tipo de cambio actual
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public ActionForward cambioActual(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        //añadimos cada uno de los parametros usados en el logueo
        beanNodo = new BeanNodoXML("id_trx", "CONS_TIPO_CAMBIO"); //id de la transaccion
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML("ruc", "00000000000"); 
        listaParametros.add(beanNodo);
        
        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //procesamos la respuesta(parseo xml) y enviamos a la pagina
            /*
            ArrayList listaResult = ParserXML.listarTipoCambio(beanResXML.getM_Respuesta());
            if(listaResult!=null && listaResult.size()>0){
                request.setAttribute("listaResult", listaResult);
            }
            */

            //jwong 18/01/2009 obtenemos el nro de decimales a formatear en los montos del tipo de cambio
            Propiedades prop = (Propiedades)context.getAttribute("propiedades");
            
            //procesamos solo el primer tipo de moneda
            BeanTipoCambioXML beanTipoCambioXML = ParserXML.obtenerTipoCambio(beanResXML.getM_Respuesta()/*, prop.getM_NroDecimalesWS()*/);
            if(beanTipoCambioXML!=null){
                request.setAttribute("tipoCambio", beanTipoCambioXML);
                //jwong 04/01/2009 guardamos en session el tipo de cambio actual
                session.setAttribute("tipoCambioActual", beanTipoCambioXML);
            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
        return mapping.findForward("cargarTipoCambio");
    }
    

    private static final String CONTENT_TYPE = "application/xml";
    public ActionForward cambioActualAJAX(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        ServletContext context = getServlet().getServletConfig().getServletContext();
        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        String xml = null;
        
        //enviamos la informacion al webservice y obtenemos la respuesta
        CashClientWS cashclienteWS = (CashClientWS)context.getAttribute(Constantes.CONTEXT_CLIENTE_CASH_WS);
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        //añadimos cada uno de los parametros usados en el logueo
        beanNodo = new BeanNodoXML("id_trx", "CONS_TIPO_CAMBIO"); //id de la transaccion
        listaParametros.add(beanNodo);
        beanNodo = new BeanNodoXML("ruc", "00000000000"); 
        listaParametros.add(beanNodo);

        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            //deberiamos retornar a la pagina con un mensaje de error
            request.setAttribute("mensaje", "No se encontraron resultados");
            return mapping.findForward("error");
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);

        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //jwong 18/01/2009 obtenemos el nro de decimales a formatear en los montos del tipo de cambio
            Propiedades prop = (Propiedades)context.getAttribute("propiedades");

            //procesamos la respuesta(parseo xml) y enviamos a la pagina
            BeanTipoCambioXML beanTipoCambioXML = ParserXML.obtenerTipoCambio(beanResXML.getM_Respuesta()/*, prop.getM_NroDecimalesWS()*/);
            xml = "<?xml version=\"1.0\" encoding=\"windows-1252\"?>" + "\n";
            xml = xml + "<tipoCambio>" + "\n";
            if(beanTipoCambioXML!=null){
                //tenemos que construir el xml resultante con el tipo de cambio obtenido
                xml = xml + "<cambio m_Moneda=\"" + beanTipoCambioXML.getM_Moneda() + "\" "+
                                "m_Compra=\"" + beanTipoCambioXML.getM_Compra() + "\" " +
                                "m_Venta=\"" + beanTipoCambioXML.getM_Venta() + "\" " +
                            "/>" + "\n";

                
                //request.setAttribute("tipoCambio", beanTipoCambioXML);
                //jwong 04/01/2009 guardamos en session el tipo de cambio actual
                session.setAttribute("tipoCambioActual", beanTipoCambioXML);
            }
            else{
                request.setAttribute("mensaje", "Se encontraron problemas al procesar la información");
            }
            
            xml = xml + "</tipoCambio>" + "\n"; //cerramos el documento xml
            // Escribimos el xml al flujo del response
            response.setContentType(CONTENT_TYPE);
            //para que el navegador no utilice su cache para mostrar los datos
            response.addHeader("Cache-Control","no-cache"); //HTTP 1.1
            response.addHeader("Pragma","no-cache"); //HTTP 1.0
            response.setDateHeader("Expires", 0); //prevents caching at the proxy server
            //escribimos el resultado en el flujo de salida
            PrintWriter out = response.getWriter();
            out.println(xml);
            out.flush();
        }
        else{
            //mostraremos un mensaje de error en pagina
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
		return null; // no retornamos nada
    }
}
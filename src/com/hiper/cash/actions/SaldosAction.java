/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.actions;

import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import java.io.IOException;
import javax.servlet.ServletException;
import java.io.PrintWriter;

/**
 * SaldosAction
 * Procesa la informacion enviada desde el formulario Login
 * Invoca los metodos de la clase UsuarioDAO para realizar la autenticacion
 * Redirige el control al jsp principal de la aplicacion
 * @autor Jonathan Wong
 * @version 1.0
 * Fecha creacion: 06/01/2006
 */

/**
 *
 * @author jwong
 */
public class SaldosAction {
  /**
   *
   * @throws javax.servlet.ServletException
   * @throws java.io.IOException
   * @return
   * @param response
   * @param request
   * @param form
   * @param mapping
   */
  public ActionForward iniciarSesion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    HttpSession session = request.getSession();
    //se debe enviar los parametros del logeo al web service para su validacion en el host del banco

    //se mantendra en session el xml resultante de la consulta al host del banco

    //realizar una validacion previa para redireccionar a la pagina inicial de logueo(flex)
    //en caso no se haya validado al usuario

    /********************************* PRUEBA *********************************/
    String xml =
            "<?xml version='1.0'?>" +
            "<WSDL>" +
            "  <cod>00</cod>" +
            "  <description></description>" +
            "  <modulo codigo='I04' nombre='Seguridad' iconoOn='img/bt1.png' iconoOff='img/bt1b.png' estado='1' maxnivel='1'>" +
            "    <proceso codigo='E0026'>" +
            "		<nombre>Mantenimiento de Usuarios</nombre>" +
            "		<padre>E0026</padre>" +
            "		<nivel>1</nivel>" +
            "		<archivo>mostrarusuarios.do?do=cargar</archivo>" +
            "		<icono>icons/sUsuarios.gif</icono>" +
            "		<estado>1</estado>" +
            "	</proceso>	" +
            "  </modulo>" +
            "  <modulo codigo='I28' nombre='Monitoreo de Terminales' iconoOn='img/bt2.png' iconoOff='img/bt2b.png' estado='1' maxnivel='1'>" +
            "    <proceso codigo='E0002'>" +
            "		<nombre>Terminales</nombre>" +
            "		<padre>E0002</padre>" +
            "		<nivel>1</nivel>" +
            "		<archivo>monitoreoterminal.do?do=cargar</archivo>" +
            "		<icono>icons/sUsuarios.gif</icono>" +
            "		<estado>1</estado>" +
            "	</proceso>" +
            "	<proceso codigo='E0033'>" +
            "		<nombre>Actualizaciones Remotas</nombre>" +
            "		<padre>E0033</padre>" +
            "		<nivel>1</nivel>" +
            "		<archivo>monitoreoactualizacion.do?do=cargar</archivo>" +
            "		<icono>icons/sUsuarios.gif</icono>" +
            "		<estado>1</estado>" +
            "	</proceso>" +
            "  </modulo>" +
            "  <modulo codigo='I20' nombre='Configuraci�n de Punto de Venta' iconoOn='img/bt3.png' iconoOff='img/bt3b.png' estado='1' maxnivel='1'>" +
            "    <proceso codigo='E0020'>" +
            "		<nombre>Perfiles de Comercio</nombre>" +
            "		<padre>E0020</padre>" +
            "		<nivel>1</nivel>" +
            "		<archivo>mantenerPerfilesCom.do?do=cargar&modulo=I20&submodulo=E0020</archivo>" +
            "		<icono>icons/sUsuarios.gif</icono>" +
            "		<estado>1</estado>" +
            "	</proceso>" +
            "	<proceso codigo='E0021'>" +
            "		<nombre>Comercio</nombre>" +
            "		<padre>E0021</padre>" +
            "		<nivel>1</nivel>" +
            "		<archivo>MostrarMerchants.do?do=cargarprovincias&modulo=I20&submodulo=E0021</archivo>" +
            "		<icono>icons/sUsuarios.gif</icono>" +
            "		<estado>1</estado>" +
            "	</proceso>" +
            "  </modulo>" +
            "</WSDL>";
    /**************************************************************************/
    session.setAttribute("xmlLogin", xml);
    return mapping.findForward("inicio");
  }

  /**
   *
   * @throws javax.servlet.ServletException
   * @throws java.io.IOException
   * @return
   * @param response
   * @param request
   * @param form
   * @param mapping
   */
  public ActionForward cerrarSesion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    HttpSession session = request.getSession();
    //si termino la session debemos retornar al inicio
    if(session.getAttribute("usuarioActual") == null){
      response.sendRedirect("inicio.jsp");
      return null;
    }

    session.setAttribute("usuarioActual",null); //Elimina la sesion
    session.invalidate(); //Invalida la sesion
    ////System.out.println("Cerr� sesion");
    return mapping.findForward("success"); //retorna
  }

  //prueba de envio del xml
  private static final String CONTENT_TYPE = "application/xml";
  public ActionForward prueba(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    HttpSession session = request.getSession();
    String xml = (String)session.getAttribute("xmlLogin");

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

    return null; // no retornamos nada
  }
}
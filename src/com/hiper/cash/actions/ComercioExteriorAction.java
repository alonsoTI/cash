/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.actions;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author jwong
 */
public class ComercioExteriorAction extends DispatchAction {
    //jwong 25/02/2009 para visualizacion de manual de usuario
    public ActionForward cargarConsultaComExt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        String habil = request.getParameter("habil");
        if(habil==null || (habil!=null && "1".equals(habil.trim()))){
            session.setAttribute("habil", "1");
        }
        else{
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarConsultaComExt");
        }

        return mapping.findForward("cargarConsultaComExt");
    }
}

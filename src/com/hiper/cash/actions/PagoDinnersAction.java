/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.actions;

import com.hiper.cash.dao.TxListFieldDao;
import com.hiper.cash.dao.hibernate.TxListFieldDaoHibernate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author jwong
 */
public class PagoDinnersAction extends DispatchAction {
    public ActionForward cargar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        /*
        if (session.getAttribute("usuarioActual") == null) {
        response.sendRedirect("inicio.jsp");
        return null;
        }
        */

        //obtenemos el listado de tipos de moneda
        TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
        List tiposMoneda = listFieldDAO.selectListFieldByFieldName("CashTipoMoneda");

        //obtenemos el listado de cuentas de la empresa
        ArrayList listaCuentas = new ArrayList();
        
        session.setAttribute("listaCuentas", listaCuentas);
        session.setAttribute("tiposMoneda", tiposMoneda);

        return mapping.findForward("cargar");
    }
}

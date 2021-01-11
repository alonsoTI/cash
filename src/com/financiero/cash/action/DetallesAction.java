/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.financiero.cash.action;

import com.financiero.cash.dao.DOperacion;
import com.financiero.cash.form.InputForm;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author andrew
 */
public class DetallesAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int idOrden = 0;

        idOrden= Integer.parseInt(request.getParameter("cod"));

        DOperacion dao = new DOperacion();
        ArrayList lista = dao.getItems(idOrden);
        InputForm inputForm = (InputForm) form;
        inputForm.setDetallesList(lista);

        request.setAttribute("idorden", idOrden);
        request.setAttribute("listDetalles", lista);



        return mapping.findForward(SUCCESS);
    }
}

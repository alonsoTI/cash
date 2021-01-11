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
public class OrdenesAction extends org.apache.struts.action.Action {

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

        // pagina actual
        String pag = request.getParameter("pag");
        // numero de parametros enviados por el filtro de busqueda
        int nroitems = Integer.valueOf(request.getParameter("ni")).intValue();

        // declaro i
        int i = 0;
        // declaro el vector de los filtros
        String mivector[] = new String[nroitems];

        // seteo los valores del array con los valores de los filtros
        String cad = "";
        for (i = 0; i < nroitems; i++) {
            cad = "";
            cad = "filtro" + i;
            mivector[i] = request.getParameter(cad);
        }

        // tipo filtro de busqueda
        int sw = Integer.valueOf(request.getParameter("sw")).intValue();

        String idempresa = mivector[0];
        String idservicio = mivector[1];
        String fecha1 = mivector[2];
        String fecha2 = mivector[3];

        DOperacion dao = new DOperacion();
        ArrayList lista = dao.getOrdenes(idempresa, idservicio, fecha1, fecha2);
        InputForm inputForm = (InputForm) form;
        inputForm.setOrdenesList(lista);

        request.setAttribute("listUsers", lista);



        return mapping.findForward(SUCCESS);
    }
}

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
public class ServicioAction extends org.apache.struts.action.Action {

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

    	  String idempresa = request.getParameter("emp");
          DOperacion dao = new DOperacion();
          ArrayList lista = dao.getServiciosxEmpresas(idempresa);
          InputForm inputForm = (InputForm) form;
          inputForm.setServicioList(lista);

          // return mapping.findForward(SUCCESS);
          String jsonResult = new flexjson.JSONSerializer().serialize(lista);

          response.setContentType("text/javascript");
          response.setHeader("Cache-Control", "no-cache");

          response.getWriter().write(jsonResult);
          return null;

    }
}

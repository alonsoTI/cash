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
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author ANDQUI
 */
public class InputAction extends org.apache.struts.action.Action {

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
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	
    	HttpSession session = request.getSession();
    	
    	//obtengo la tarjeta actual de la variable session    	
    	
    	String idtarjeta ="";
    	
    	idtarjeta=(String) session.getAttribute("tarjetaActual");    		 
    	
    	////System.out.println("tarjetaActual "  +  idtarjeta);
    	
        DOperacion dao = new DOperacion();
         ArrayList lista = dao.getEmpresas(idtarjeta);
         
        InputForm inputForm = (InputForm) form;
        inputForm.setEmpresaList(lista);

        return mapping.findForward(SUCCESS);
    }

 
}

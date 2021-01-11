/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.actions;

import com.hiper.cash.dao.TaEntidadEmpresaDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.hibernate.TaEntidadEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.forms.OrdenForm;
import com.hiper.cash.util.Constantes;
import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;


/**
 *
 * @author esilva
 */
public class CargaOnlineAction extends DispatchAction{

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        /*if (session.getAttribute("usuarioActual") == null) {
        response.sendRedirect("inicio.jsp");
        return null;
        }*/

        String empresa = (String) request.getParameter("empresa");
        String servicio = (String) request.getParameter("control");
        //Hibernate
        TaEntidadEmpresaDao entidadDAO = new TaEntidadEmpresaDaoHibernate();
        TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
        //jmoreno: 13/11/09 Recuperando el servicio Empresa por idServEmp
        TaServicioxEmpresa objservicioxempresa = servicioxempresaDAO.selectServicioxEmpresa(empresa, Long.parseLong(servicio.substring(0, servicio.indexOf("*"))));
        

        //jwong 06/05/2009
        String tipoEntidad = "";
//        if(Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES.equalsIgnoreCase( objservicioxempresa.getTipoServicio())){
        if(Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES.equalsIgnoreCase(servicio.substring(servicio.indexOf("*")+1))){//jmoreno 13/11/09
            tipoEntidad = Constantes.FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR;
        }
        else{
            tipoEntidad = Constantes.FIELD_CASH_TIPO_ENTIDAD_PERSONAL;
        }
        //List listaentidad = entidadDAO.selectEntidadEmpresa(empresa, objservicioxempresa.getCsemIdServicioEmpresa());
        List listaentidad = entidadDAO.selectEntidadEmpresa(empresa, objservicioxempresa.getCsemIdServicioEmpresa(), tipoEntidad);

        //Session
        session.setAttribute("mo_listaentidad", listaentidad);

        ((OrdenForm)form).setValues();
        ((OrdenForm)form).setDocumentos();
        return mapping.findForward("view");
    }
}

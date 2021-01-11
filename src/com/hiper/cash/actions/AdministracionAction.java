/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.actions;

import com.hiper.cash.dao.TaEntidadEmpresaDao;
import com.hiper.cash.dao.TaServicioEntidadDao;
import com.hiper.cash.dao.TaServicioxEmpresaDao;
import com.hiper.cash.dao.TaTipoPagoServicioDao;
import com.hiper.cash.dao.TmBancoDao;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.TmSoporteDao;
import com.hiper.cash.dao.TxListFieldDao;
import com.hiper.cash.dao.hibernate.TaEntidadEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioEntidadDaoHibernate;
import com.hiper.cash.dao.hibernate.TaServicioxEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TaTipoPagoServicioDaoHibernate;
import com.hiper.cash.dao.hibernate.TmBancoDaoHibernate;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmSoporteDaoHibernate;
import com.hiper.cash.dao.hibernate.TxListFieldDaoHibernate;
import com.hiper.cash.domain.TaEntidadEmpresa;
import com.hiper.cash.domain.TaEntidadEmpresaId;
import com.hiper.cash.domain.TaServicioEntidad;
import com.hiper.cash.domain.TaServicioEntidadId;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.domain.TmEmpresa;
import com.hiper.cash.entidad.BeanSuccess;
import com.hiper.cash.entidad.BeanSuccessDetail;
import com.hiper.cash.entidad.Propiedades;
import com.hiper.cash.forms.AdministracionForm;
import com.hiper.cash.forms.PersonalForm;
import com.hiper.cash.forms.ProveedorForm;
import com.hiper.cash.ingresarBD.dbaccess.CBEmpresaFormato;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;
import com.hiper.cash.xml.bean.BeanDataLoginXML;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.*;
import javax.servlet.http.*;
//import org.apache.catalina.connector.ClientAbortException;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.apache.struts.actions.DispatchAction;



/**
 * AdministracionAction : Clase que permite el manejo de toda
 * la lógica del módulo de administracion de la aplicación web
 * @version 1.0 01/01/2009
 * @author jwong
 * Copyright © HIPER S.A
 */

public class AdministracionAction extends DispatchAction {

    private static Logger logger = Logger.getLogger(AdministracionAction.class);

    /**
     * Método que permite cargar la pantalla de búsqueda y mantenimiento
     * de proveedores     
     */
    public ActionForward cargarMantenimientoProveedor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarMantPersonal");
        }

        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa = (List) session.getAttribute("empresa");
        List lEmpresa = null;
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        
        if (beanDataLogXML.isM_usuarioEspecial()) {//verificamos si el usuario actual es Especial(Emp. Operaciones Cash)
            lEmpresa = (List) session.getAttribute("empresa");//obtenemos la lista de empresas de la session
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            //a partir de lEmpresas, obtenemos la lista de empresas que estan afiliadas al servicio
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_ADMINISTRACION);
            if (lEmpresa.size() == 0) {
                request.setAttribute("mensaje", "El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        } else {
            //hMapEmpresas contiene las empresas con sus respectivos servicios afiliados
            HashMap hMapEmpresas = (HashMap) session.getAttribute("hmEmpresas");
            //obtenemos la lista de empresas que estan afiliadas al servicio, segun la data obtenida del logeo en el hashMap
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_ADMINISTRACION);
            if (lEmpresa.size() == 0) {
                request.setAttribute("mensaje", "El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }

        //obtenemos los datos de la empresa que resulto al logearnos
       
        List listaEmpresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);
        session.setAttribute("listaEmpresas", listaEmpresas);
        if (listaEmpresas != null && listaEmpresas.size() > 0) {
            TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
            List listaIdServicios = new ArrayList();
            listaIdServicios.add(Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES);
            List listaServicios = servicioxempresaDAO.selectServEmpByIdServ(((TmEmpresa) listaEmpresas.get(0)).getCemIdEmpresa(), listaIdServicios);
            request.setAttribute("listaServicios", listaServicios);
        }


        return mapping.findForward("cargarMantProveedor");
    }
    /**
     * Método que realiza la carga de servicios por empresa
     *  en la pantalla de búsqueda de proveedores
     */
    public ActionForward cargarServiciosProveedor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        AdministracionForm admForm = (AdministracionForm) form;
        String m_Empresa = admForm.getM_Empresa();
        TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
        List listaIdServicios = new ArrayList();
        listaIdServicios.add(Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES);
        List listaServicios = servicioxempresaDAO.selectServEmpByIdServ(m_Empresa, listaIdServicios);
        request.setAttribute("listaServicios", listaServicios);
        return mapping.findForward("cargarMantProveedor");
    }

    public ActionForward buscarProveedor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos la empresa seleccionada
        AdministracionForm admForm = (AdministracionForm) form;
        String m_Empresa = admForm.getM_Empresa();
        String m_IdServEmp = admForm.getM_IdServEmp();

        //obtenemos el listado de proveedores de esa empresa
        TaEntidadEmpresaDao entidadEmpDAO = new TaEntidadEmpresaDaoHibernate();
        List listaProveedor = entidadEmpDAO.selectEntidadEmpresaByTipo(m_IdServEmp, Constantes.FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR);

        TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
        List listaIdServicios = new ArrayList();
        listaIdServicios.add(Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES);
        List listaServicios = servicioxempresaDAO.selectServEmpByIdServ(m_Empresa, listaIdServicios);
        request.setAttribute("listaServicios", listaServicios);

        if (listaProveedor != null && listaProveedor.size() > 0) {//jmoreno 24/09/09
            request.setAttribute("listaProveedor", listaProveedor);
        } else {
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
        return mapping.findForward("cargarMantProveedor");
    }

    public ActionForward nuevoProveedor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        //recuperamos los datos del formulario
        String empresa = ((ProveedorForm) form).getM_Empresa();        
        String idServEmp = ((ProveedorForm) form).getM_IdServEmp();

        //obtenemos el listado de tipos de documentos
        TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
        List listaTipoDocs = listFieldDAO.selectListFieldByFieldName("CashTipoDocumento");

        TaTipoPagoServicioDao tpsDAO = new TaTipoPagoServicioDaoHibernate();
        TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
        TaServicioxEmpresa objservicioxempresa = servicioxempresaDAO.selectServicioxEmpresa(empresa, Long.parseLong(idServEmp));
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        TmEmpresa objEmpresa = empresaDAO.selectEmpresas(empresa);
        request.setAttribute("m_DescEmpresa", objEmpresa.getDemNombre());
        request.setAttribute("m_Servicio", objservicioxempresa.getDsemDescripcion());

        List listaTipoPago = null;
        listaTipoPago = tpsDAO.select(Long.parseLong(idServEmp));        
        if (listaTipoPago != null && listaTipoPago.size() > 0) {
            //obtenemos el listado de tipos de cuenta            
            List listaTipoCuenta = listFieldDAO.selectListFieldByFieldName3("CashTipoCuenta", Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES);
            //obtenemos el listado de tipos de moneda
            List listaTipoMoneda = listFieldDAO.selectListFieldByFieldName("CashTipoMoneda");
            //obtenemos el listado de bancos
            TmBancoDao bancoDao = new TmBancoDaoHibernate();
            List listaBanco = bancoDao.select();

            request.setAttribute("listaTipoDocs", listaTipoDocs);
            request.setAttribute("listaTipoPago", listaTipoPago);
            request.setAttribute("listaTipoCuenta", listaTipoCuenta);
            request.setAttribute("listaTipoMoneda", listaTipoMoneda);
            request.setAttribute("listaBanco", listaBanco);
            //tambien setearemos los codigos de los tipos de tipos de pago de entidad,
            //abono en cuenta del financiero y abono en cuenta de otro banco
            request.setAttribute("CONS_ABONO_CTA_FINAN", Constantes.TIPO_PAGO_ENTIDAD_ABONOCTAFINAN);
            request.setAttribute("CONS_ABONO_CTA_OTRO", Constantes.TIPO_PAGO_ENTIDAD_ABONOCTAOTRBCO);

            return mapping.findForward("nuevoProveedor");
        } else {
            List alsuccess = new ArrayList();
            BeanSuccessDetail sucessdetail;

            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Tipo de Operación");
            sucessdetail.setM_Mensaje("CREACIÓN DE PROVEEDOR");
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Fecha / Hora");
            sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
            alsuccess.add(sucessdetail);

            BeanSuccess success = new BeanSuccess();
            success.setM_Titulo("Nuevo Proveedor");
            success.setM_Mensaje("No se puede crear nuevo proveedor, el servicio no tiene relacionado formas de pago");
            success.setM_Back("administracion.do?do=cargarMantenimientoProveedor");

            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
            return mapping.findForward("success");
        }

    }

    //jwong 12/02/2009 para la modificacion del proveedor
    public ActionForward cargarModificarProveedor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos los parametros de busqueda del proveedor a ser modificado
        String m_idServEmp = request.getParameter("m_IdServEmp");
        String m_Empresa = request.getParameter("m_Empresa");
        String m_IdProveedor = request.getParameter("m_IdProveedor");
        String m_IdTipo = request.getParameter("m_IdTipo");

        TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
        TaServicioxEmpresa objservicioxempresa = null;
        objservicioxempresa = servicioxempresaDAO.selectServicioxEmpresa(m_Empresa, Long.parseLong(m_idServEmp));
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        TmEmpresa objEmpresa = empresaDAO.selectEmpresas(m_Empresa);
        request.setAttribute("m_DescEmpresa", objEmpresa.getDemNombre());
        request.setAttribute("m_Empresa", m_Empresa);
        request.setAttribute("m_Servicio", objservicioxempresa.getDsemDescripcion());
        request.setAttribute("m_IdServEmp", m_idServEmp);

        //buscamos los datos del proveedor a modificar
        TaEntidadEmpresaDao proveedorDao = new TaEntidadEmpresaDaoHibernate();
        TaEntidadEmpresa entidEmp = proveedorDao.selectEntidadEmpresa(m_idServEmp, m_IdProveedor, m_IdTipo);

        //obtenemos el listado de tipos de documentos
        TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
        List listaTipoDocs = listFieldDAO.selectListFieldByFieldName("CashTipoDocumento");

        TaTipoPagoServicioDao tpsDAO = new TaTipoPagoServicioDaoHibernate();
        List listaTipoPago = null;
        listaTipoPago = tpsDAO.select(Long.parseLong(m_idServEmp));
        //List listaTipoPago = listFieldDAO.selectByFieldNameAndFieldDetail("CashTipoPagoEntidad", "Proveedor");
        //obtenemos el listado de tipos de cuenta        
        List listaTipoCuenta = listFieldDAO.selectListFieldByFieldName3("CashTipoCuenta", Constantes.TX_CASH_SERVICIO_PAGO_PROVEEDORES);
        List listaTipoMoneda = listFieldDAO.selectListFieldByFieldName("CashTipoMoneda");
        //obtenemos el listado de bancos
        TmBancoDao bancoDao = new TmBancoDaoHibernate();
        List listaBanco = bancoDao.select();

        //obtenemos el form y seteamos sus valores
        ProveedorForm proveedorform = (ProveedorForm) form;
        proveedorform.setM_IdServEmp(m_idServEmp);
        proveedorform.setM_Empresa(m_Empresa);
        proveedorform.setM_IdProveedor(m_IdProveedor);
        proveedorform.setM_Nombre(entidEmp.getDeenombre());
        proveedorform.setM_TipoDocumento(entidEmp.getCeetipoDocumento());
        proveedorform.setM_NroDocumento(entidEmp.getNeenumDocumento());
        proveedorform.setM_EmpresaOriginal(m_Empresa);
        proveedorform.setM_IdProveedorOriginal(m_IdProveedor);
        proveedorform.setM_Contrapartida(entidEmp.getDeecontrapartida());

        
        TaServicioEntidadDao servEntDao = new TaServicioEntidadDaoHibernate();        
        TaServicioEntidad servEntidad = servEntDao.selectServicioEntidad(Long.parseLong(m_idServEmp), m_IdProveedor, m_IdTipo);
        if (servEntidad != null) {
            proveedorform.setM_FormaPago(servEntidad.getCsetipoPago());
            proveedorform.setM_Moneda(servEntidad.getCsenMoneda());
            //debemos setear los valores de acuerdo al tipo de pago
            if (Constantes.TIPO_PAGO_ENTIDAD_ABONOCTAFINAN.equals(servEntidad.getCsetipoPago())) {//Abono en Cuenta Financiero
                proveedorform.setM_TipoCuenta(servEntidad.getCsenTipoCuenta());
                //proveedorform.setM_MonedaFinan(servEntidad.getCsenMoneda());
                proveedorform.setM_NroCuenta(servEntidad.getNsenNumCuenta());
            } else if (Constantes.TIPO_PAGO_ENTIDAD_ABONOCTAOTRBCO.equals(servEntidad.getCsetipoPago())) {//Abono en Cuenta otro Banco
                proveedorform.setM_Banco(servEntidad.getCsenCodigoOtroBanco());
                //proveedorform.setM_MonedaOtrBco(servEntidad.getCsenMoneda());
                proveedorform.setM_NroCuentaCCI(servEntidad.getCsenNumCtaCci());
            } else { //Cheque de Gerencia y Efectivo 12-08-09
                proveedorform.setM_TipoCuenta(servEntidad.getCsenTipoCuenta());
                //proveedorform.setM_MonedaFinan(servEntidad.getCsenMoneda());
                proveedorform.setM_NroCuenta(servEntidad.getNsenNumCuenta());
            }
        }

        request.setAttribute("listaTipoDocs", listaTipoDocs);
        request.setAttribute("listaTipoPago", listaTipoPago);

        request.setAttribute("listaTipoCuenta", listaTipoCuenta);
        request.setAttribute("listaTipoMoneda", listaTipoMoneda);
        request.setAttribute("listaBanco", listaBanco);

        request.setAttribute("CONS_ABONO_CTA_FINAN", Constantes.TIPO_PAGO_ENTIDAD_ABONOCTAFINAN);
        request.setAttribute("CONS_ABONO_CTA_OTRO", Constantes.TIPO_PAGO_ENTIDAD_ABONOCTAOTRBCO);

        return mapping.findForward("modificarProveedor");

    }

    public ActionForward guardarProveedor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        //obtenemos datos del formulario
        ProveedorForm proveedorForm = (ProveedorForm) form;
        String m_IdServEmp = proveedorForm.getM_IdServEmp();
        String m_Empresa = proveedorForm.getM_Empresa();
        String m_IdTipo = Constantes.FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR; //tipo proveedor

        String m_IdProveedor = proveedorForm.getM_IdProveedor();
        String m_Nombre = proveedorForm.getM_Nombre().toUpperCase();
        String m_TipoDocumento = proveedorForm.getM_TipoDocumento();
        String m_NroDocumento = proveedorForm.getM_NroDocumento();
        String m_FormaPago = proveedorForm.getM_FormaPago();
        String m_Contrapartida = proveedorForm.getM_Contrapartida().toUpperCase();
        String m_Moneda = proveedorForm.getM_Moneda();

        //forma de pago:
        // * Cheque de Gerencia
        // * Abono en Cuenta Financiero
        // * Abono en Cuenta otro Banco

        //Si es Abono Cuenta Financiero:
        String m_TipoCuenta = proveedorForm.getM_TipoCuenta();
        //String m_MonedaFinan = proveedorForm.getM_MonedaFinan();
        String m_NroCuenta = proveedorForm.getM_NroCuenta();
        //Si es Cuenta otro Banco:
        String m_Banco = proveedorForm.getM_Banco();
        //String m_MonedaOtrBco = proveedorForm.getM_MonedaOtrBco();
        String m_NroCuentaCCI = proveedorForm.getM_NroCuentaCCI();

        //antes de insertar verificamos que el id de entidadEmpresa no exista
        TaEntidadEmpresaDao proveedorDao = new TaEntidadEmpresaDaoHibernate();
        TaEntidadEmpresa entidEmp = proveedorDao.selectEntidadEmpresa(m_IdServEmp, m_IdProveedor, m_IdTipo);
        if (entidEmp != null) { //si ya existe un codigo de entidad para esa empresa
            request.setAttribute("msjExistProveedor", "Ya existe ese Id de Proveedor para esa Empresa");
            return mapping.findForward("cargarNuevoProveedor");
        }

        TaServicioEntidad servEntidad = new TaServicioEntidad();
        TaServicioEntidadId id = new TaServicioEntidadId(Long.parseLong(m_IdServEmp), m_IdProveedor, m_IdTipo);
        servEntidad.setId(id);

        //jmoreno 28/12/09
        servEntidad.setCsenIdEmpresa(m_Empresa);
        //jwong 22/02/2009 falta añadir tipo de pago
        servEntidad.setCsetipoPago(m_FormaPago);        
        servEntidad.setCsenMoneda(m_Moneda);        

        //debemos setear los valores de acuerdo al tipo de pago
        if (Constantes.TIPO_PAGO_ENTIDAD_ABONOCTAFINAN.equals(m_FormaPago)) {//Abono en Cuenta Financiero
            servEntidad.setCsenTipoCuenta(m_TipoCuenta);
            //servEntidad.setCsenMoneda(m_MonedaFinan);
            servEntidad.setNsenNumCuenta(m_NroCuenta);
        } else if (Constantes.TIPO_PAGO_ENTIDAD_ABONOCTAOTRBCO.equals(m_FormaPago)) {//Abono en Cuenta otro Banco
            servEntidad.setCsenCodigoOtroBanco(m_Banco);
            //servEntidad.setCsenMoneda(m_MonedaOtrBco);
            servEntidad.setCsenNumCtaCci(m_NroCuentaCCI);
        } /*else { //Cheque de Gerencia
            ;
        }*/
        TaServicioEntidadDao taServEntidadDao = new TaServicioEntidadDaoHibernate();
        boolean inserto = taServEntidadDao.insert(servEntidad);
        if (inserto) {
            //seteamos los valores al objeto EntidadEmpresa
            TaEntidadEmpresa taEntEmp = new TaEntidadEmpresa();
            TaEntidadEmpresaId idEntEmp = new TaEntidadEmpresaId(m_IdProveedor, m_IdTipo, Long.parseLong(m_IdServEmp));

            taEntEmp.setId(idEntEmp);
            taEntEmp.setDeenombre(m_Nombre);
            taEntEmp.setCemIdEmpresa(m_Empresa);
            taEntEmp.setDeecontrapartida(m_Contrapartida);
            taEntEmp.setCeetipoDocumento(m_TipoDocumento);
            taEntEmp.setNeenumDocumento(m_NroDocumento);
            taEntEmp.setDeeemail("");
            taEntEmp.setDeetelefono("");
            taEntEmp.setDeedescripcion("");
            boolean ins = proveedorDao.insert(taEntEmp);
            if (ins) {
                List alsuccess = new ArrayList();
                BeanSuccessDetail sucessdetail;
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Tipo de Operación");
                sucessdetail.setM_Mensaje("CREACIÓN DE PROVEEDOR");
                alsuccess.add(sucessdetail);
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Fecha / Hora");
                sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
                alsuccess.add(sucessdetail);
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("ID Proveedor");
                sucessdetail.setM_Mensaje(m_IdProveedor);
                alsuccess.add(sucessdetail);

                BeanSuccess success = new BeanSuccess();
                success.setM_Titulo("Nuevo Proveedor");
                success.setM_Mensaje("La operación ha sido realizada correctamente");
                success.setM_Back("administracion.do?do=cargarMantenimientoProveedor");

                request.setAttribute("success", success);
                request.setAttribute("alsuccess", alsuccess);
                return mapping.findForward("success");
            } else {
                //Mensaje Confirmación
                List alsuccess = new ArrayList();
                BeanSuccessDetail sucessdetail;

                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Tipo de Operación");
                sucessdetail.setM_Mensaje("CREACIÓN DE PROVEEDOR");
                alsuccess.add(sucessdetail);
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Fecha / Hora");
                sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
                alsuccess.add(sucessdetail);

                BeanSuccess success = new BeanSuccess();
                success.setM_Titulo("Nuevo Proveedor");
                success.setM_Mensaje("No se pudo guardar el Proveedor");
                success.setM_Back("administracion.do?do=cargarMantenimientoProveedor");

                request.setAttribute("success", success);
                request.setAttribute("alsuccess", alsuccess);
                return mapping.findForward("success");
            }

        } else {
            //Mensaje Confirmación
            List alsuccess = new ArrayList();
            BeanSuccessDetail sucessdetail;

            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Tipo de Operación");
            sucessdetail.setM_Mensaje("CREACIÓN DE PROVEEDOR");
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Fecha / Hora");
            sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
            alsuccess.add(sucessdetail);

            BeanSuccess success = new BeanSuccess();
            success.setM_Titulo("Nuevo Proveedor");
            success.setM_Mensaje("No se pudo guardar la relación con el servicio");
            success.setM_Back("administracion.do?do=cargarMantenimientoProveedor");

            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
            return mapping.findForward("success");
        }

    }

    public ActionForward modificarProveedor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos la empresa seleccionada
        ProveedorForm proveedorForm = (ProveedorForm) form;
        String m_Empresa = proveedorForm.getM_Empresa();
        String m_idServEmp = proveedorForm.getM_IdServEmp();
        String m_IdTipo = Constantes.FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR;

        String m_IdProveedor = proveedorForm.getM_IdProveedor();
        String m_Nombre = proveedorForm.getM_Nombre().toUpperCase();
        String m_TipoDocumento = proveedorForm.getM_TipoDocumento();
        String m_NroDocumento = proveedorForm.getM_NroDocumento();
        String m_FormaPago = proveedorForm.getM_FormaPago();
        String m_Contrapartida = proveedorForm.getM_Contrapartida().toUpperCase();
        //Si es Abono Cuenta Financiero:
        String m_TipoCuenta = proveedorForm.getM_TipoCuenta();
        //String m_MonedaFinan = proveedorForm.getM_MonedaFinan();
        String m_NroCuenta = proveedorForm.getM_NroCuenta();
        //Si es Cuenta otro Banco:
        String m_Banco = proveedorForm.getM_Banco();
        String m_NroCuentaCCI = proveedorForm.getM_NroCuentaCCI();
        String m_Moneda = proveedorForm.getM_Moneda();

        List alsuccess = new ArrayList();
        BeanSuccessDetail sucessdetail;
        
        if (m_idServEmp != null && !"".equals(m_idServEmp)) {
            
            TaServicioEntidadDao taServEntidadDao = new TaServicioEntidadDaoHibernate();
            TaServicioEntidad servEntidad = new TaServicioEntidad();
            TaServicioEntidadId id = new TaServicioEntidadId(Long.parseLong(m_idServEmp), m_IdProveedor, m_IdTipo);
            servEntidad.setId(id);
            servEntidad.setCsetipoPago(m_FormaPago);
            servEntidad.setCsenIdEmpresa(m_Empresa);
            servEntidad.setCsenMoneda(m_Moneda);

            //debemos setear los valores de acuerdo al tipo de pago
            if (Constantes.TIPO_PAGO_ENTIDAD_ABONOCTAFINAN.equals(m_FormaPago)) {//Abono en Cuenta Financiero
                servEntidad.setCsenTipoCuenta(m_TipoCuenta);
                //servEntidad.setCsenMoneda(m_MonedaFinan);
                servEntidad.setNsenNumCuenta(m_NroCuenta);
            } else if (Constantes.TIPO_PAGO_ENTIDAD_ABONOCTAOTRBCO.equals(m_FormaPago)) {//Abono en Cuenta otro Banco
                servEntidad.setCsenCodigoOtroBanco(m_Banco);
                //servEntidad.setCsenMoneda(m_MonedaOtrBco);
                servEntidad.setCsenNumCtaCci(m_NroCuentaCCI);
            } else { //Cheque de Gerencia  y Efectivo - //jmoreno 12-08-09
                servEntidad.setCsenTipoCuenta("");
                //servEntidad.setCsenMoneda(m_MonedaFinan);
                servEntidad.setNsenNumCuenta("");
            }

            //luego actualizamos la relacion
            boolean inserto = taServEntidadDao.insert(servEntidad);
            if (inserto) {
                boolean ins = false;
                try {
                    TaEntidadEmpresaDao proveedorDao = new TaEntidadEmpresaDaoHibernate();
                    //seteamos los valores al objeto EntidadEmpresa
                    TaEntidadEmpresa taEntEmp = new TaEntidadEmpresa();
                    TaEntidadEmpresaId idEntEmp = new TaEntidadEmpresaId(m_IdProveedor, m_IdTipo, Long.parseLong(m_idServEmp));

                    taEntEmp.setId(idEntEmp);
                    taEntEmp.setCemIdEmpresa(m_Empresa);
                    taEntEmp.setDeenombre(m_Nombre);
                    taEntEmp.setDeeemail("");
                    taEntEmp.setDeetelefono("");
                    taEntEmp.setDeedescripcion("");
                    taEntEmp.setDeecontrapartida(m_Contrapartida);
                    taEntEmp.setCeetipoDocumento(m_TipoDocumento);
                    taEntEmp.setNeenumDocumento(m_NroDocumento);
                    //taEntEmp.setDeetipo(Constantes.FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR);
                    //taEntEmp.setCeetipoPago(m_FormaPago);
                    ins = proveedorDao.insert(taEntEmp);
                } catch (Exception e) {
                    logger.error(e.toString());
                }

                if (ins) {
                    //Mensaje Confirmación
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("Tipo de Operación");
                    sucessdetail.setM_Mensaje("ACTUALIZACIÓN DE PROVEEDOR");
                    alsuccess.add(sucessdetail);
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("Fecha / Hora");
                    sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
                    alsuccess.add(sucessdetail);
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("ID Proveedor");
                    sucessdetail.setM_Mensaje(m_IdProveedor);
                    alsuccess.add(sucessdetail);

                    BeanSuccess success = new BeanSuccess();
                    success.setM_Titulo("Actualización de Proveedor");
                    success.setM_Mensaje("La operación ha sido realizada correctamente");
                    success.setM_Back("administracion.do?do=cargarMantenimientoProveedor");

                    request.setAttribute("success", success);
                    request.setAttribute("alsuccess", alsuccess);
                } else {
                    //Mensaje Confirmación
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("Tipo de Operación");
                    sucessdetail.setM_Mensaje("ACTUALIZACIÓN DE PROVEEDOR");
                    alsuccess.add(sucessdetail);
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("Fecha / Hora");
                    sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
                    alsuccess.add(sucessdetail);

                    BeanSuccess success = new BeanSuccess();
                    success.setM_Titulo("Actualización Proveedor");
                    success.setM_Mensaje("No se pudo actualizar los datos del Proveedor");
                    success.setM_Back("administracion.do?do=cargarMantenimientoProveedor");

                    request.setAttribute("success", success);
                    request.setAttribute("alsuccess", alsuccess);
                }
            }else{
                //Mensaje Confirmación
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Tipo de Operación");
                sucessdetail.setM_Mensaje("ACTUALIZACIÓN DE PROVEEDOR");
                alsuccess.add(sucessdetail);
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Fecha / Hora");
                sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
                alsuccess.add(sucessdetail);

                BeanSuccess success = new BeanSuccess();
                success.setM_Titulo("Actualización Proveedor");
                success.setM_Mensaje("No se pudo actualizar los datos del Proveedor");
                success.setM_Back("administracion.do?do=cargarMantenimientoProveedor");

                request.setAttribute("success", success);
                request.setAttribute("alsuccess", alsuccess);
            }

        } else {
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Tipo de Operación");
            sucessdetail.setM_Mensaje("ACTUALIZACIÓN DE PROVEEDOR");
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Fecha / Hora");
            sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
            alsuccess.add(sucessdetail);

            BeanSuccess success = new BeanSuccess();
            success.setM_Titulo("Actualización de Proveedor");
            success.setM_Mensaje("No se pudo realizar la operación");
            success.setM_Back("administracion.do?do=cargarMantenimientoProveedor");

            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
        }
        return mapping.findForward("success");
    }

    public ActionForward eliminarProveedor(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        //obtenemos la empresa seleccionada
        ProveedorForm proveedorForm = (ProveedorForm) form;
        String m_Empresa = proveedorForm.getM_Empresa();
        String m_IdServEmp = proveedorForm.getM_IdServEmp();
        String m_IdProveedor = proveedorForm.getM_IdProveedor();
        String m_IdTipo = Constantes.FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR; 

        TaEntidadEmpresaDao proveedorDao = new TaEntidadEmpresaDaoHibernate();
        //seteamos los valores al objeto EntidadEmpresa
        TaEntidadEmpresa taEntEmp = new TaEntidadEmpresa();
        TaEntidadEmpresaId idEntEmp = new TaEntidadEmpresaId(m_IdProveedor, m_IdTipo, Long.parseLong(m_IdServEmp));
        taEntEmp.setId(idEntEmp);
        taEntEmp.setCemIdEmpresa(m_Empresa);
        boolean delEnt = proveedorDao.delete(taEntEmp);

        //1ero eliminamos la relacion con el servicio de pago a proveedores        
        TaServicioEntidad servEntidad = new TaServicioEntidad();
        TaServicioEntidadId id = new TaServicioEntidadId(Long.parseLong(m_IdServEmp), m_IdProveedor, m_IdTipo);
        servEntidad.setId(id);
        servEntidad.setCsenIdEmpresa(m_Empresa);
        //eliminamos la relacion existente con pago a proveedores
        TaServicioEntidadDao taServEntidadDao = new TaServicioEntidadDaoHibernate();
        boolean delServEnt = taServEntidadDao.delete(servEntidad);


        if (delEnt && delServEnt) {
            //Mensaje Confirmación
            List alsuccess = new ArrayList();
            BeanSuccessDetail sucessdetail;

            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Tipo de Operación");
            sucessdetail.setM_Mensaje("ELIMINACIÓN DE PROVEEDOR");
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Fecha / Hora");
            sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("ID Proveedor");
            sucessdetail.setM_Mensaje(m_IdProveedor);
            alsuccess.add(sucessdetail);

            BeanSuccess success = new BeanSuccess();
            success.setM_Titulo("Eliminación de Proveedor");
            success.setM_Mensaje("La operación ha sido realizada correctamente");
            success.setM_Back("administracion.do?do=cargarMantenimientoProveedor");

            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
            return mapping.findForward("success");
        } else {
            //Mensaje Confirmación
            List alsuccess = new ArrayList();
            BeanSuccessDetail sucessdetail;

            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Tipo de Operación");
            sucessdetail.setM_Mensaje("ELIMINACIÓN DE PROVEEDOR");
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Fecha / Hora");
            sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
            alsuccess.add(sucessdetail);

            BeanSuccess success = new BeanSuccess();
            success.setM_Titulo("Eliminación de Proveedor");
            success.setM_Mensaje("La operación no se pudo realizar");
            success.setM_Back("administracion.do?do=cargarMantenimientoProveedor");

            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
            return mapping.findForward("success");
        }
    }
    
    public ActionForward cargarMantenimientoPersonal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session.getAttribute("usuarioActual");

        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarMantPersonal");
        }
        
        

        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa = (List) session.getAttribute("empresa");
        List lEmpresa = null;
        
        
        
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);
        
        if (beanDataLogXML.isM_usuarioEspecial()) {//verificamos si el usuario actual es Especial(Emp. Operaciones Cash)
            lEmpresa = (List) session.getAttribute("empresa");//obtenemos la lista de empresas de la session
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            //a partir de lEmpresas, obtenemos la lista de empresas que estan afiliadas al servicio
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_ADMINISTRACION);
            if (lEmpresa.size() == 0) {
                request.setAttribute("mensaje", "El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        } else {
            //hMapEmpresas contiene las empresas con sus respectivos servicios afiliados
            HashMap hMapEmpresas = (HashMap) session.getAttribute("hmEmpresas");
            //obtenemos la lista de empresas que estan afiliadas al servicio, segun la data obtenida del logeo en el hashMap
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_ADMINISTRACION);
            if (lEmpresa.size() == 0) {
                request.setAttribute("mensaje", "El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }

        //obtenemos los datos de la empresa que resulto al logearnos
        
        List listaEmpresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);
        if (listaEmpresas != null && listaEmpresas.size() > 0) {
            TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
            List listaIdServicios = new ArrayList();
            listaIdServicios.add(Constantes.TX_CASH_SERVICIO_PAGO_CTS);
            listaIdServicios.add(Constantes.TX_CASH_SERVICIO_PAGO_HABERES);
            List listaServiciosCts = servicioxempresaDAO.selectServEmpByIdServ(((TmEmpresa) listaEmpresas.get(0)).getCemIdEmpresa(), listaIdServicios);
            request.setAttribute("listaServicios", listaServiciosCts);

        }
        session.setAttribute("listaEmpresas", listaEmpresas);

        return mapping.findForward("cargarMantPersonal");
    }

    public ActionForward cargarServiciosPersonal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        AdministracionForm admForm = (AdministracionForm) form;
        String m_Empresa = admForm.getM_Empresa();
        List listaIdServicios = new ArrayList();
        listaIdServicios.add(Constantes.TX_CASH_SERVICIO_PAGO_CTS);
        listaIdServicios.add(Constantes.TX_CASH_SERVICIO_PAGO_HABERES);
        TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
        List listaServiciosCts = servicioxempresaDAO.selectServEmpByIdServ(m_Empresa, listaIdServicios);
        request.setAttribute("listaServicios", listaServiciosCts);
        return mapping.findForward("cargarMantPersonal");
    }

    public ActionForward buscarPersonal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos la empresa seleccionada
        AdministracionForm admForm = (AdministracionForm) form;
        String m_Empresa = admForm.getM_Empresa();
        String m_IdServEmp = admForm.getM_IdServEmp();

        //obtenemos el listado de personal de esa empresa
        TaEntidadEmpresaDao entidadEmpDAO = new TaEntidadEmpresaDaoHibernate();
        List listaPersonal = entidadEmpDAO.selectEntidadEmpresaByTipo(m_IdServEmp, Constantes.FIELD_CASH_TIPO_ENTIDAD_PERSONAL);       

        if (listaPersonal != null && listaPersonal.size() > 0) {//jmoreno 24/09/09
            request.setAttribute("listaPersonal", listaPersonal);
        } else {
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
        List listaIdServicios = new ArrayList();
        listaIdServicios.add(Constantes.TX_CASH_SERVICIO_PAGO_CTS);
        listaIdServicios.add(Constantes.TX_CASH_SERVICIO_PAGO_HABERES);
        TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
        List listaServicios = servicioxempresaDAO.selectServEmpByIdServ(m_Empresa, listaIdServicios);
        request.setAttribute("listaServicios", listaServicios);

        return mapping.findForward("cargarMantPersonal");
    }

    public ActionForward nuevoPersonal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        String empresa = ((PersonalForm) form).getM_Empresa();
        String m_IdServEmp = ((PersonalForm) form).getM_IdServEmp();
        TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
        TaTipoPagoServicioDao tpsDAO = new TaTipoPagoServicioDaoHibernate();

        TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
        TaServicioxEmpresa objservicioxempresa = servicioxempresaDAO.selectServicioxEmpresa(empresa, Long.parseLong(m_IdServEmp));

        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        TmEmpresa objEmpresa = empresaDAO.selectEmpresas(empresa);
        request.setAttribute("m_DescEmpresa", objEmpresa.getDemNombre());
        request.setAttribute("m_Servicio", objservicioxempresa.getDsemDescripcion());

        List listaTipoPagoPla = null;
        List listaTipoPago = null;

        if (m_IdServEmp != null && !"".equals(m_IdServEmp)) {
            listaTipoPago = tpsDAO.select(Long.parseLong(m_IdServEmp));
        }

        if (listaTipoPago != null && listaTipoPago.size() > 0) {            
            List listaTipoCuenta = listFieldDAO.selectListFieldByFieldName3(Constantes.FIELD_CASH_TIPO_CUENTA, objservicioxempresa.getTmServicio().getCsrIdServicio());
            List listaTipoMoneda = listFieldDAO.selectListFieldByFieldName(Constantes.FIELD_CASH_TIPO_MONEDA);

            request.setAttribute("empresa", empresa);
            request.setAttribute("listaTipoPagoPla", listaTipoPagoPla);
            request.setAttribute("listaTipoPago", listaTipoPago);
            
            request.setAttribute("listaTipoCuenta", listaTipoCuenta);
            request.setAttribute("listaTipoMoneda", listaTipoMoneda);
            request.setAttribute("tipoEntPersonal", Constantes.FIELD_CASH_TIPO_ENTIDAD_PERSONAL);

            return mapping.findForward("nuevoPersonal");
        } else {
            List alsuccess = new ArrayList();
            BeanSuccessDetail sucessdetail;

            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Tipo de Operación");
            sucessdetail.setM_Mensaje("CREACIÓN DE PERSONAL");
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Fecha / Hora");
            sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
            alsuccess.add(sucessdetail);

            BeanSuccess success = new BeanSuccess();
            success.setM_Titulo("Nuevo Empleado");
            success.setM_Mensaje("No se puede crear nuevo personal, la empresa no tiene relacionada formas de pago para el servicio");
            success.setM_Back("administracion.do?do=cargarMantenimientoPersonal");

            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
            return mapping.findForward("success");
        }

    }

    public ActionForward guardarPersonal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos el numero de personal a insertar
        String nroPersonal = request.getParameter("nroPersonal");
        int nroPer = 0;
        try {
            nroPer = Integer.parseInt(nroPersonal);
        } catch (NumberFormatException nfe) {
            logger.error(nfe.toString());
        }
        TaEntidadEmpresaDao personalDao = new TaEntidadEmpresaDaoHibernate();
        String m_Empresa = null;
        String m_IdServEmp = null;
        String m_IdEmpleado = null;
        String m_Nombre = null;
        String m_DNI = null;
        String m_NroCelular = null;
        String m_Email = null;
        String m_Contrapartida = null;

        String m_FormaPago = null;
        String m_TipoCuenta = null;
        String m_Moneda = null;
        String m_NumeroCuenta = null;
        String m_Importe = null;

        List alsuccess = new ArrayList();
        BeanSuccessDetail sucessdetail = null;
        int numeroMsjs = 1;

        //obtenemos la empresa a la que se le creara personal nuevo
        PersonalForm personalfrm = (PersonalForm) form;
        m_Empresa = personalfrm.getM_Empresa();
        m_IdServEmp = personalfrm.getM_IdServEmp();
        String m_IdTipo = Constantes.FIELD_CASH_TIPO_ENTIDAD_PERSONAL; //tipo personal
        
        if (m_IdServEmp != null && !"".equals(m_IdServEmp)) {
            //insertamos los campos ya validados
            boolean is_ok = true;
            TaServicioEntidadDao taServEntidadDao = new TaServicioEntidadDaoHibernate();
            TaEntidadEmpresa taEntEmp = null;
            TaEntidadEmpresaId idEntEmp = null;
            List errors = new ArrayList();
            for (int g = 0; g < nroPer; g++) {
                m_IdEmpleado = request.getParameter("m_IdEmpleado_" + g);
                m_Nombre = request.getParameter("m_Nombre_" + g).toUpperCase();
                m_DNI = request.getParameter("m_DNI_" + g);
                m_NroCelular = request.getParameter("m_NroCelular_" + g);
                m_Email = request.getParameter("m_Email_" + g).toUpperCase(); 
                m_Contrapartida = request.getParameter("m_Contrapartida_" + g).toUpperCase(); 

                //Forma de pago del servicio
                m_FormaPago = request.getParameter("m_FormaPago_" + g);
                m_TipoCuenta = request.getParameter("m_TipoCuenta_" + g);
                m_Moneda = request.getParameter("m_Moneda_" + g);
                m_NumeroCuenta = request.getParameter("m_NumeroCuenta_" + g);
                m_Importe = request.getParameter("m_Importe_" + g);

                if (!Constantes.TIPO_PAG_CREDITO_CUENTA.equals(m_FormaPago)) {
                    m_TipoCuenta = "";
                    m_NumeroCuenta = "";
                }
                boolean ins = false;
                boolean inserto = false;


                try {
                    //insertamos la relacion con el servicio de Pago
                    TaServicioEntidad servEntidad = new TaServicioEntidad();
                    TaServicioEntidadId idServEnt = new TaServicioEntidadId(Long.parseLong(m_IdServEmp), m_IdEmpleado, m_IdTipo);
                    servEntidad.setId(idServEnt);
                    servEntidad.setCsenIdEmpresa(m_Empresa);

                    servEntidad.setCsetipoPago(m_FormaPago);
                    servEntidad.setCsenTipoCuenta(m_TipoCuenta);
                    servEntidad.setCsenMoneda(m_Moneda);
                    servEntidad.setNsenNumCuenta(m_NumeroCuenta);
                    servEntidad.setNsenMonto(new BigDecimal(m_Importe));
                    inserto = taServEntidadDao.insert(servEntidad);
                    if (inserto) {
                        is_ok = is_ok & true;
                        //seteamos los valores al objeto EntidadEmpresa
                        taEntEmp = new TaEntidadEmpresa();
                        idEntEmp = new TaEntidadEmpresaId(m_IdEmpleado, m_IdTipo, Long.parseLong(m_IdServEmp));

                        taEntEmp.setId(idEntEmp);
                        taEntEmp.setCemIdEmpresa(m_Empresa);
                        taEntEmp.setDeenombre(m_Nombre);
                        taEntEmp.setDeeemail(m_Email);
                        taEntEmp.setDeetelefono(m_NroCelular);                        
                        taEntEmp.setDeecontrapartida(m_Contrapartida);
                        taEntEmp.setCeetipoDocumento(Constantes.FIELD_CASH_TIPO_DOCUMENTO_DNI);
                        taEntEmp.setNeenumDocumento(m_DNI);
                        ins = personalDao.insert(taEntEmp);
                    } else {

                        is_ok = false;
                        //añadir el codigo de cliente que no se pudo insertar
                        sucessdetail = new BeanSuccessDetail();
                        sucessdetail.setM_Label("Mensaje Nro");
                        sucessdetail.setM_Mensaje("" + (numeroMsjs++));
                        errors.add(sucessdetail);
                        sucessdetail = new BeanSuccessDetail();
                        sucessdetail.setM_Label("ID Empleado ");
                        sucessdetail.setM_Mensaje(m_IdEmpleado);
                        errors.add(sucessdetail);
                        sucessdetail = new BeanSuccessDetail();
                        sucessdetail.setM_Label("Detalle Mensaje");
                        sucessdetail.setM_Mensaje("No se pudo crear la relación con el servicio");
                        errors.add(sucessdetail);

                    }
                } catch (Exception eee) {
                    logger.error(eee.toString());
                    inserto = false;
                }


                if (ins) {
                    is_ok = is_ok & true;
                } else {

                    is_ok = false;
                    //añadir el codigo de cliente que no se pudo insertar
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("Mensaje Nro");
                    sucessdetail.setM_Mensaje("" + (numeroMsjs++));
                    errors.add(sucessdetail);
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("ID Empleado ");
                    sucessdetail.setM_Mensaje(m_IdEmpleado);
                    errors.add(sucessdetail);
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("Detalle Mensaje");
                    sucessdetail.setM_Mensaje("No se pudo crear el Personal");
                    errors.add(sucessdetail);

                }

            }
            if (is_ok) {
                //Mensaje Confirmación
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Tipo de Operación");
                sucessdetail.setM_Mensaje("CREACIÓN DE PERSONAL");
                alsuccess.add(sucessdetail);
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Fecha / Hora");
                sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
                alsuccess.add(sucessdetail);

                //si existe el listado de detalle de errores, se agregaran a la descripcion de la operacion
                if (errors.size() > 0) {
                    alsuccess.addAll(errors);
                }

                BeanSuccess success = new BeanSuccess();
                success.setM_Titulo("Nuevo Empleado");
                success.setM_Mensaje("La operación ha sido realizada correctamente");
                success.setM_Back("administracion.do?do=cargarMantenimientoPersonal");

                request.setAttribute("success", success);
                request.setAttribute("alsuccess", alsuccess);
            } else {
                //Mensaje Confirmación
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Tipo de Operación");
                sucessdetail.setM_Mensaje("CREACIÓN DE PERSONAL");
                alsuccess.add(sucessdetail);
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Fecha / Hora");
                sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
                alsuccess.add(sucessdetail);

                //si existe el listado de detalle de errores, se agregaran a la descripcion de la operacion
                if (errors.size() > 0) {
                    alsuccess.addAll(errors);
                }

                BeanSuccess success = new BeanSuccess();
                success.setM_Titulo("Nuevo Empleado");
                success.setM_Mensaje("La operación no se pudo realizar");
                success.setM_Back("administracion.do?do=cargarMantenimientoPersonal");

                request.setAttribute("success", success);
                request.setAttribute("alsuccess", alsuccess);
            }
        } else { //no tiene el servicio de pago a proveedores dicha empresa
            //Mensaje Confirmación
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Tipo de Operación");
            sucessdetail.setM_Mensaje("CREACIÓN DE PERSONAL");
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Fecha / Hora");
            sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
            alsuccess.add(sucessdetail);

            BeanSuccess success = new BeanSuccess();
            success.setM_Titulo("Nuevo Empleado");
            success.setM_Mensaje("No se pudo realizar la operación, la empresa no tiene relación con el servicio de pago de haberes o pago de CTS");
            success.setM_Back("administracion.do?do=cargarMantenimientoPersonal");

            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
        }
        return mapping.findForward("success");
    }
    private static final String CONTENT_TYPE = "application/xml";
    //jwong 17/02/2009 Consulta AJAX para saber si ya existe un id de personal

    public ActionForward existeIdEmpleado(ActionMapping mapping, ActionForm inForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TaEntidadEmpresaDao personalDao = new TaEntidadEmpresaDaoHibernate();
        TaEntidadEmpresa entidEmp = null;
        //creamos la cabecera del xml
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "\n";
        xml = xml + "<respuestas>" + "\n";
        //obtenemos los valores : el Id servicio,Id de personal y el tipo de entidad enviados desde pagina
        String m_idServEmp = request.getParameter("m_IdServEmp");
        String m_IdEmpleado = request.getParameter("m_IdEmpleado");
        String m_IdTipo = request.getParameter("m_IdTipo"); //tipo personal

        entidEmp = personalDao.selectEntidadEmpresa(m_idServEmp, m_IdEmpleado, m_IdTipo);
        if (entidEmp != null) { //si ya existe un codigo de entidad para esa empresa
            xml = xml + "<respuesta valor=\"SI\" />" + "\n";
        } else {
            xml = xml + "<respuesta valor=\"NO\" />" + "\n";
        }
        //cerramos el archivo xml
        xml = xml + "</respuestas>" + "\n";

        // Escribimos el xml al flujo del response
        response.setContentType(CONTENT_TYPE);
        //para que el navegador no utilice su cache para mostrar los datos
        response.addHeader("Cache-Control", "no-cache"); //HTTP 1.1
        response.addHeader("Pragma", "no-cache"); //HTTP 1.0
        response.setDateHeader("Expires", 0); //prevents caching at the proxy server
        //escribimos el resultado en el flujo de salida
        PrintWriter out = response.getWriter();
        out.println(xml);
        out.flush();

        return null; 
    }
    //jmoreno 08-07-09 Consulta AJAX para saber si ya existe un id de proveedor

    public ActionForward existeIdProveedor(ActionMapping mapping, ActionForm inForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //creamos la cabecera del xml
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "\n";
        xml = xml + "<respuestas>" + "\n";
        //obtenemos los valores de empresa, el id de personal y el tipo de entidad enviados desde pagina
        String m_idServEmp = request.getParameter("m_IdServEmp");
        String m_IdProveedor = request.getParameter("m_IdProveedor");
        String m_IdTipo = Constantes.FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR;

        TaEntidadEmpresaDao proveedorDao = new TaEntidadEmpresaDaoHibernate();
        TaEntidadEmpresa entidEmp = proveedorDao.selectEntidadEmpresa(m_idServEmp, m_IdProveedor, m_IdTipo);
        if (entidEmp != null) { //si ya existe un codigo de entidad para esa empresa
            xml = xml + "<respuesta valor=\"SI\" />" + "\n";
        } else {
            xml = xml + "<respuesta valor=\"NO\" />" + "\n";
        }
        //cerramos el archivo xml
        xml = xml + "</respuestas>" + "\n";

        // Escribimos el xml al flujo del response
        response.setContentType(CONTENT_TYPE);
        //para que el navegador no utilice su cache para mostrar los datos
        response.addHeader("Cache-Control", "no-cache"); //HTTP 1.1
        response.addHeader("Pragma", "no-cache"); //HTTP 1.0
        response.setDateHeader("Expires", 0); //prevents caching at the proxy server
        //escribimos el resultado en el flujo de salida
        PrintWriter out = response.getWriter();
        out.println(xml);
        out.flush();

        return null; // no retornamos nada
    }

    //jwong 18/02/2009 para eliminar personal
    public ActionForward eliminarPersonal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        //obtenemos la empresa seleccionada
        PersonalForm personalForm = (PersonalForm) form;
        String m_Empresa = personalForm.getM_Empresa();
        String m_IdServEmp = personalForm.getM_IdServEmp();
        String m_IdEmpleado = personalForm.getM_IdEmpleado();
        String m_IdTipo = Constantes.FIELD_CASH_TIPO_ENTIDAD_PERSONAL; //tipo personal
        
        TaEntidadEmpresaDao proveedorDao = new TaEntidadEmpresaDaoHibernate();

        //seteamos los valores al objeto EntidadEmpresa, para que se borre.
        TaEntidadEmpresa taEntEmp = new TaEntidadEmpresa();
        TaEntidadEmpresaId idEntEmp = new TaEntidadEmpresaId(m_IdEmpleado, m_IdTipo, Long.parseLong(m_IdServEmp));
        taEntEmp.setId(idEntEmp);
        taEntEmp.setCemIdEmpresa(m_Empresa);
        boolean delEnt = proveedorDao.delete(taEntEmp);

        //Luego eliminamos la relacion con el servicio de pago
        TaServicioEntidadDao servEntDao = new TaServicioEntidadDaoHibernate();
        TaServicioEntidad servEntidadCTS = servEntDao.selectServicioEntidad(Long.parseLong(m_IdServEmp), m_IdEmpleado, m_IdTipo);
        boolean delServEnt = servEntDao.delete(servEntidadCTS);
        /*Otra forma de eliminar el servicioEntidad
        TaServicioEntidad servEntidad = new TaServicioEntidad();
        TaServicioEntidadId id = new TaServicioEntidadId(Long.parseLong(m_IdServEmp), m_IdEmpleado, m_IdTipo);
        servEntidad.setId(id);
        servEntidad.setCsenIdEmpresa(m_Empresa);
        TaServicioEntidadDao taServEntidadDao = new TaServicioEntidadDaoHibernate();
        boolean delServEnt = taServEntidadDao.delete(servEntidad);*/

        if (delEnt && delServEnt) {
            //Mensaje Confirmación
            List alsuccess = new ArrayList();
            BeanSuccessDetail sucessdetail;

            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Tipo de Operación");
            sucessdetail.setM_Mensaje("ELIMINACIÓN DE PERSONAL");
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Fecha / Hora");
            sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("ID Empleado");
            sucessdetail.setM_Mensaje(m_IdEmpleado);
            alsuccess.add(sucessdetail);

            BeanSuccess success = new BeanSuccess();
            success.setM_Titulo("Eliminación de Personal");
            success.setM_Mensaje("La operación ha sido realizada correctamente");
            success.setM_Back("administracion.do?do=cargarMantenimientoPersonal");

            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
        } else {
            //Mensaje Confirmación
            List alsuccess = new ArrayList();
            BeanSuccessDetail sucessdetail;

            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Tipo de Operación");
            sucessdetail.setM_Mensaje("ELIMINACIÓN DE PERSONAL");
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Fecha / Hora");
            sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
            alsuccess.add(sucessdetail);

            BeanSuccess success = new BeanSuccess();
            success.setM_Titulo("Eliminación de Personal");
            success.setM_Mensaje("La operación no se pudo realizar");
            success.setM_Back("administracion.do?do=cargarMantenimientoPersonal");

            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
        }
        return mapping.findForward("success");
    }

    //jwong 18/02/2009 para la modificacion del personal
    public ActionForward cargarModificarPersonal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        String m_IdServEmp = request.getParameter("m_IdServEmp");
        String m_Empresa = request.getParameter("m_Empresa");
        String m_IdEmpleado = request.getParameter("m_IdEmpleado");        
        String m_IdTipo = request.getParameter("m_IdTipo");

        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        TmEmpresa objEmpresa = empresaDAO.selectEmpresas(m_Empresa);
        request.setAttribute("m_DescEmpresa", objEmpresa.getDemNombre());

        TaEntidadEmpresaDao personalDao = new TaEntidadEmpresaDaoHibernate();
        TaEntidadEmpresa entidEmp = personalDao.selectEntidadEmpresa(m_IdServEmp, m_IdEmpleado, m_IdTipo);
        
        TaServicioxEmpresaDao servicioxempresaDAO = new TaServicioxEmpresaDaoHibernate();
        TaServicioxEmpresa objservicioxempresa = servicioxempresaDAO.selectServicioxEmpresa(m_Empresa, Long.parseLong(m_IdServEmp));
        
        request.setAttribute("m_Servicio", objservicioxempresa.getDsemDescripcion());
        TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
        TaTipoPagoServicioDao tpsDAO = new TaTipoPagoServicioDaoHibernate();
        List listaTipoPago = null;

        listaTipoPago = tpsDAO.select(Long.parseLong(m_IdServEmp));
        List listaTipoCuenta = listFieldDAO.selectListFieldByFieldName3(Constantes.FIELD_CASH_TIPO_CUENTA, objservicioxempresa.getTmServicio().getCsrIdServicio());
        List listaTipoMoneda = listFieldDAO.selectListFieldByFieldName(Constantes.FIELD_CASH_TIPO_MONEDA);

        request.setAttribute("listaTipoPago", listaTipoPago);
        request.setAttribute("listaTipoCuenta", listaTipoCuenta);
        request.setAttribute("listaTipoMoneda", listaTipoMoneda);

        //obtenemos el form y seteamos sus valores
        PersonalForm personalform = (PersonalForm) form;
        personalform.setM_Empresa(m_Empresa);
        personalform.setM_IdEmpleado(m_IdEmpleado);
        personalform.setM_Nombre(entidEmp.getDeenombre());
        //personalform.setM_TipoDocumento(entidEmp.getCeetipoDocumento());
        personalform.setM_DNI(entidEmp.getNeenumDocumento());
        personalform.setM_Email(entidEmp.getDeeemail());
        personalform.setM_NroCelular(entidEmp.getDeetelefono());

        personalform.setM_EmpresaOriginal(m_Empresa);
        personalform.setM_IdEmpleadoOriginal(m_IdEmpleado);
        personalform.setM_Contrapartida(entidEmp.getDeecontrapartida());

        TaServicioEntidadDao servEntDao = new TaServicioEntidadDaoHibernate();
        TaServicioEntidad servicioEntidad = servEntDao.selectServicioEntidad(Long.parseLong(m_IdServEmp), m_IdEmpleado, m_IdTipo);
        if (servicioEntidad != null) {
            personalform.setM_FormaPagoCTS(servicioEntidad.getCsetipoPago());
            personalform.setM_TipoCuentaCTS(servicioEntidad.getCsenTipoCuenta());
            personalform.setM_MonedaCTS(servicioEntidad.getCsenMoneda());
            personalform.setM_NroCuentaCTS(servicioEntidad.getNsenNumCuenta());
            personalform.setM_ImporteCTS(servicioEntidad.getNsenMonto().toString());
        } else {
            personalform.setM_FormaPagoCTS(null);
            personalform.setM_TipoCuentaCTS(null);
            personalform.setM_MonedaCTS(null);
            personalform.setM_NroCuentaCTS(null);
            personalform.setM_ImporteCTS(null);
        }

        return mapping.findForward("modificarPersonal");
    }

    //jwong 18/02/2009
    public ActionForward modificarPersonal(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //obtenemos la empresa seleccionada
        PersonalForm personalForm = (PersonalForm) form;
        String m_IdServEmp = personalForm.getM_IdServEmp();
        String m_Empresa = personalForm.getM_Empresa();
        String m_IdTipo = Constantes.FIELD_CASH_TIPO_ENTIDAD_PERSONAL; //tipo personal

        String m_IdEmpleado = personalForm.getM_IdEmpleado();
        String m_Nombre = personalForm.getM_Nombre().toUpperCase();
        String m_NroDocumento = personalForm.getM_DNI();
        String m_Email = personalForm.getM_Email().toUpperCase();
        String m_NroCelular = personalForm.getM_NroCelular();        
        String m_Contrapartida = personalForm.getM_Contrapartida().toUpperCase();

        String m_FormaPagoCTS = personalForm.getM_FormaPagoCTS();
        String m_TipoCuentaCTS = personalForm.getM_TipoCuentaCTS();
        String m_MonedaCTS = personalForm.getM_MonedaCTS();
        String m_NumeroCuentaCTS = personalForm.getM_NroCuentaCTS();
        String m_ImporteCTS = personalForm.getM_ImporteCTS();
        if (!Constantes.TIPO_PAG_CREDITO_CUENTA.equalsIgnoreCase(m_FormaPagoCTS)) {
            m_TipoCuentaCTS = "";
            m_NumeroCuentaCTS = "";
        }

        List alsuccess = new ArrayList();
        BeanSuccessDetail sucessdetail;
        
        TaEntidadEmpresaDao personalDao = new TaEntidadEmpresaDaoHibernate();
        TaServicioEntidadDao servEntDao = new TaServicioEntidadDaoHibernate();

        if (m_IdServEmp != null && !"".equals(m_IdServEmp)) {

            //actualizamos la relacion con el servicio de pago
            TaServicioEntidad servEntidadCTS = new TaServicioEntidad();
            TaServicioEntidadId idCTS = new TaServicioEntidadId(Long.parseLong(m_IdServEmp), m_IdEmpleado, m_IdTipo);
            servEntidadCTS.setId(idCTS);
            servEntidadCTS.setCsenIdEmpresa(m_Empresa);

            //jwong 22/02/2009 falta añadir tipo de pago
            servEntidadCTS.setCsetipoPago(m_FormaPagoCTS);
            servEntidadCTS.setCsenTipoCuenta(m_TipoCuentaCTS);
            servEntidadCTS.setCsenMoneda(m_MonedaCTS);
            servEntidadCTS.setNsenNumCuenta(m_NumeroCuentaCTS);
            servEntidadCTS.setNsenMonto(new BigDecimal(m_ImporteCTS));
            boolean inserto = servEntDao.insert(servEntidadCTS);
            if (inserto == true) {
                //ahora actualizamos la entidadEmpresa
                boolean ins = false;
                try {
                    //seteamos los valores al objeto EntidadEmpresa
                    TaEntidadEmpresa taEntEmp = new TaEntidadEmpresa();
                    TaEntidadEmpresaId idEntEmp = new TaEntidadEmpresaId(m_IdEmpleado, m_IdTipo, Long.parseLong(m_IdServEmp));

                    taEntEmp.setId(idEntEmp);
                    taEntEmp.setCemIdEmpresa(m_Empresa);
                    taEntEmp.setDeenombre(m_Nombre);
                    taEntEmp.setDeeemail(m_Email);
                    taEntEmp.setDeetelefono(m_NroCelular);
                    taEntEmp.setDeecontrapartida(m_Contrapartida);
                    //taEntEmp.setDeedescripcion();                    
                    taEntEmp.setCeetipoDocumento(Constantes.FIELD_CASH_TIPO_DOCUMENTO_DNI);
                    taEntEmp.setNeenumDocumento(m_NroDocumento);
                    ins = personalDao.insert(taEntEmp);
                } catch (Exception eee) {
                    logger.error(eee.toString());
                    inserto = false;
                }



                if (ins) {
                    //Mensaje Confirmación
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("Tipo de Operación");
                    sucessdetail.setM_Mensaje("ACTUALIZACIÓN DE PERSONAL");
                    alsuccess.add(sucessdetail);
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("Fecha / Hora");
                    sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
                    alsuccess.add(sucessdetail);
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("ID Empleado");
                    sucessdetail.setM_Mensaje(m_IdEmpleado);
                    alsuccess.add(sucessdetail);

                    BeanSuccess success = new BeanSuccess();
                    success.setM_Titulo("Actualización de Personal");
                    success.setM_Mensaje("La operación ha sido realizada correctamente");
                    success.setM_Back("administracion.do?do=cargarMantenimientoPersonal");

                    request.setAttribute("success", success);
                    request.setAttribute("alsuccess", alsuccess);
                } else {

                    //Mensaje Confirmación
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("Tipo de Operación");
                    sucessdetail.setM_Mensaje("ACTUALIZACIÓN DE PERSONAL");
                    alsuccess.add(sucessdetail);
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("Fecha / Hora");
                    sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
                    alsuccess.add(sucessdetail);
                    sucessdetail = new BeanSuccessDetail();
                    sucessdetail.setM_Label("ID Empleado");
                    sucessdetail.setM_Mensaje(m_IdEmpleado);
                    alsuccess.add(sucessdetail);

                    BeanSuccess success = new BeanSuccess();
                    success.setM_Titulo("Actualización de Personal");
                    success.setM_Mensaje("No se pudo actualizar los datos del empleado");
                    success.setM_Back("administracion.do?do=cargarMantenimientoPersonal");

                    request.setAttribute("success", success);
                    request.setAttribute("alsuccess", alsuccess);

                }
            } else {
                //Mensaje Confirmación
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Tipo de Operación");
                sucessdetail.setM_Mensaje("ACTUALIZACIÓN DE PERSONAL");
                alsuccess.add(sucessdetail);
                sucessdetail = new BeanSuccessDetail();
                sucessdetail.setM_Label("Fecha / Hora");
                sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
                alsuccess.add(sucessdetail);

                BeanSuccess success = new BeanSuccess();
                success.setM_Titulo("Actualización de Personal");
                success.setM_Mensaje("No se pudo guardar la relación con el Servicio");
                success.setM_Back("administracion.do?do=cargarMantenimientoPersonal");

                request.setAttribute("success", success);
                request.setAttribute("alsuccess", alsuccess);
            }
        } else {
            //Mensaje Confirmación
            //Mensaje Confirmación
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Tipo de Operación");
            sucessdetail.setM_Mensaje("CREACIÓN DE PERSONAL");
            alsuccess.add(sucessdetail);
            sucessdetail = new BeanSuccessDetail();
            sucessdetail.setM_Label("Fecha / Hora");
            sucessdetail.setM_Mensaje(Fecha.getFechaActual("dd/MM/yy") + "   " + Fecha.getFechaActual("HH:mm:ss"));
            alsuccess.add(sucessdetail);

            BeanSuccess success = new BeanSuccess();
            success.setM_Titulo("Nuevo Empleado");
            success.setM_Mensaje("No se pudo realizar la operación");
            success.setM_Back("administracion.do?do=cargarMantenimientoPersonal");

            request.setAttribute("success", success);
            request.setAttribute("alsuccess", alsuccess);
        }
        return mapping.findForward("success");
    }

    //jwong 25/02/2009 para visualizacion de manual de usuario
    public ActionForward cargarManual(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarManual");
        }

        return mapping.findForward("cargarManual");
    }

    //jwong 25/02/2009 para visualizacion de manual de usuario
    public ActionForward enviaManual(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }
        //realizar validaciones si las hubiera
        //obtenemos la ruta del manual
        ServletContext context = session.getServletContext();
        Propiedades prop = (Propiedades) context.getAttribute("propiedades");

        //llamamos al garbage collector
        System.gc();

        try {
            //obtenemos la direccion del documento
            String direccion = prop.getM_RutaManual();
            File file = new File(direccion);
            URL url = file.toURL();
            //obtenemos los bytes del documento
            InputStream is = url.openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[8096];
            int len = 0;
            while ((len = is.read(bytes)) > 0) {
                baos.write(bytes, 0, len);
            }
            //llamamos al garbage collector
            System.gc();

            byte[] imagen = baos.toByteArray();
            baos.flush();
            baos.close();
            baos = null;

            //llamamos al garbage collector
            System.gc();

            is.close();
            is = null;
            //el tipo de contenido del documento(PDF)
            String ti_conten = "application/pdf";

            response.setContentType(ti_conten); //colocamos el tipo de contenido correspondiente al documento

            String nombre_archivo = "manual.pdf";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + nombre_archivo + "\"");

            //para que el navegador no utilice su cache para mostrar los datos
            response.addHeader("Cache-Control", "no-cache"); //HTTP 1.1
            response.addHeader("Pragma", "no-cache"); //HTTP 1.0
            response.setDateHeader("Expires", 0); //prevents caching at the proxy server
            response.flushBuffer();

            javax.servlet.ServletOutputStream bOut = response.getOutputStream();
            bOut.write(imagen);//escribimos en el flujo de salida del servlet, para que lo procese el browser
            bOut.flush();
            bOut.close();
            bOut = null;
        } /*catch (ClientAbortException cae) {
            logger.error("El cliente aborto la descargar-->> " + cae.toString());
        } */catch (Exception ioe) {
            logger.error(ioe.toString());
        } finally {
            //llamamos al garbage collector
            System.gc();
        }
        return null;
    }

    //jwong 25/02/2009 para visualizacion de manual de usuario
    public ActionForward cargarDemo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        //realizar validaciones si las hubiera
        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarDemo");
        }

        //obtenemos la ruta del demo flash
        ServletContext context = session.getServletContext();
        Propiedades prop = (Propiedades) context.getAttribute("propiedades");
        session.setAttribute("rutaDemoFlash", prop.getM_RutaDemoFlash());
        //session.setAttribute("rutaDemoSinExt", prop.getM_RutaDemoFlash().replaceAll(".swf", ""));
        return mapping.findForward("cargarDemo");
    }

    //jwong 25/02/2009 para visualizacion de manual de usuario
    public ActionForward cargarSoporte(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarSoporte");
        }

        //busqueda de soporte
        TmSoporteDao soporteDao = new TmSoporteDaoHibernate();
        List result = soporteDao.select(); //buscamos todos los registros

        if (result != null && result.size() > 0) {
            request.setAttribute("listaResult", result);
        } else {
            request.setAttribute("mensaje", "No se encontraron resultados");
        }
        return mapping.findForward("cargarSoporte");
    }


    //jwong 26/02/2008 carga del cambio de clave
    public ActionForward cargarCambioClave(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarCambioClave");
        }

        return mapping.findForward("cargarCambioClave");
    }

    public ActionForward cargarAdministracionDescargas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        BeanDataLoginXML beanDataLogXML = (BeanDataLoginXML) session.getAttribute("usuarioActual");
        //si termino la session debemos retornar al inicio
        if (beanDataLogXML == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarDescargas");
        }

        //cargamos las empresas asociadas resultante del logueo
//        List lEmpresa = (List) session.getAttribute("empresa");

        List lEmpresa = null;
        TmEmpresaDao empresaDAO = new TmEmpresaDaoHibernate();
        String numTarjeta= (String) session.getAttribute("tarjetaActual");
        boolean swverifica= empresaDAO.verificaSiTarjetaCash(numTarjeta);        
        
        
        if (beanDataLogXML.isM_usuarioEspecial()) {//verificamos si el usuario actual es Especial(Emp. Operaciones Cash)
            lEmpresa = (List) session.getAttribute("empresa");//obtenemos la lista de empresas de la session
            TaServicioxEmpresaDao servEmpDAO = new TaServicioxEmpresaDaoHibernate();
            //a partir de lEmpresas, obtenemos la lista de empresas que estan afiliadas al servicio
            lEmpresa = servEmpDAO.selectEmpresasByIdServ(swverifica,lEmpresa, Constantes.TX_CASH_SERVICIO_ADMINISTRACION);
            if (lEmpresa.size() == 0) {
                request.setAttribute("mensaje", "El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        } else {
            //hMapEmpresas contiene las empresas con sus respectivos servicios afiliados
            HashMap hMapEmpresas = (HashMap) session.getAttribute("hmEmpresas");
            //obtenemos la lista de empresas que estan afiliadas al servicio, segun la data obtenida del logeo en el hashMap
            lEmpresa = Util.buscarServiciosxEmpresa(hMapEmpresas, Constantes.TX_CASH_SERVICIO_ADMINISTRACION);
            if (lEmpresa.size() == 0) {
                request.setAttribute("mensaje", "El servicio no se encuentra afiliado");
                return mapping.findForward("error");
            }
        }
        //obtenemos los datos de la empresa que resulto al logearnos
        
        List listaEmpresas = empresaDAO.listarEmpresa(swverifica,lEmpresa);
        session.setAttribute("listaEmpresas", listaEmpresas);

        ResourceBundle res = ResourceBundle.getBundle("financiero");
        String link_jre = res.getString("href_jre_java");
        request.setAttribute("link_jre", link_jre);
        return mapping.findForward("cargarDescargas");
    }//cargarAdministracionDescargas

    public ActionForward cargarAdministracionDescargar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();

        //si termino la session debemos retornar al inicio
        if (session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("cierraSession.jsp");
            return null;
        }

        String habil = request.getParameter("habil");
        if (habil == null || (habil != null && "1".equals(habil.trim()))) {
            session.setAttribute("habil", "1");
        } else {
            session.setAttribute("habil", "0");
            return mapping.findForward("cargarDescargas");
        }


        String m_Empresa = request.getParameter("m_Empresa");
        String descarga = request.getParameter("tipo");
        ResourceBundle res = ResourceBundle.getBundle("financiero");
        String link_jre = res.getString("href_jre_java");
        request.setAttribute("link_jre", link_jre);

        String RUTA = "";
        String nomFile = "";
        if ("1".equals(descarga)) {//descarga de formatos por empresa
            CBEmpresaFormato dao = new CBEmpresaFormato();
            String aux = dao.obtenerEncIn(m_Empresa);
            if (!aux.equals("")) {
                try {
                    nomFile = m_Empresa + ".fmt";
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "attachment;filename=" + nomFile);
                    ServletOutputStream ouputStream = response.getOutputStream();
                    ouputStream.write(aux.getBytes());//datos
                    ouputStream.flush();
                    ouputStream.close();
                } catch (Exception e) {
                    logger.error(e.toString());
                }
                return null;
            } else {
                request.setAttribute("existeFormato", "La empresa no tiene asociada formatos de Entrada");
                return mapping.findForward("cargarDescargas");
            }

        } else if ("2".equals(descarga)) {//descarga del validador

            RUTA = res.getString("ruta_Validador");
            //nomFile = "ValidadorArchivo.rar";
            File fdescargar = new File(RUTA);
            if (fdescargar.exists()) {
                try {
                    FileInputStream archivo = new FileInputStream(RUTA);
                    int longitud = archivo.available();
                    byte[] datos = new byte[longitud];
                    archivo.read(datos);
                    archivo.close();
                    response.setContentType("application/octet-stream");
                    response.setHeader("Content-Disposition", "attachment;filename=" + fdescargar.getName());//nomFile
                    ServletOutputStream ouputStream = response.getOutputStream();
                    ouputStream.write(datos);
                    ouputStream.flush();
                    ouputStream.close();

                } catch (Exception e) {
                    logger.error(e.toString());
                }
                return null;
            } else {
                request.setAttribute("existeFile", "Archivo no disponible");
                return mapping.findForward("cargarDescargas");
            }

        }
        return null;
    }
}
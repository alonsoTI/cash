package com.hiper.cash.actions;

import static com.financiero.cash.util.VariablesRequest.ID_SERVICIO_EMPRESA;
import static com.financiero.cash.util.VariablesRequest.MENSAJE_VALIDACION_CARGAS;
import static com.financiero.cash.util.VariablesRequest.MODULO;
import static com.financiero.cash.util.VariablesRequest.RUC_EMPRESA;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_ARCHIVO_VACIO;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_DESCONOCIDO;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_GENERAR_XML;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_GUARDANDO_DATOS;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_RECUPERANDO_VALIDACION;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_VALIDACION_CTS;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_VALIDACION_CUENTAS;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_VALIDACION_DATOS;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.EXITO;
import static com.hiper.cash.ingresarBD.util.ConstantesResultadosValidacion.ERROR_VALIDACION_MONTOS;

import static com.hiper.cash.util.CashConstants.RES_CASH;
import static com.hiper.cash.util.CashConstants.RUTA_BFIN;
import static com.hiper.cash.util.CashConstants.RUTA_DATA;
import static com.hiper.cash.util.CashConstants.RUTA_ZIP;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.financiero.cash.delegate.SeguridadDelegate;
import com.hiper.cash.dao.TaCuentasServicioEmpresaDao;
import com.hiper.cash.dao.TaLogValidacionDao;
import com.hiper.cash.dao.TaSecuencialDao;
import com.hiper.cash.dao.hibernate.TaCuentasServicioEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TaLogValidacionDaoHibernate;
import com.hiper.cash.dao.hibernate.TaSecuencialDaoHibernate;
import com.hiper.cash.domain.TaLogValidacion;
import com.hiper.cash.domain.TaLogValidacionId;
import com.hiper.cash.domain.TaOrden;
import com.hiper.cash.entidad.BeanPaginacion;
import com.hiper.cash.forms.FileUploadForm;
import com.hiper.cash.ingresarBD.Validador;
import com.hiper.cash.ingresarBD.dbaccess.CBEmpresaFormato;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.TripleDES;

/**
 * 
 * @author jmoreno
 */
// TODO Renombrar a cargarArchivoAction. Ver si struts-config es case-sensitive
public class cargarArchivoAction extends DispatchAction
{

    private static final String FLAG_CARGANDO_NO = "NO";

    private static final String FLAG_SI_CARGANDO = "SI";

    private static final String VALIDACION_ERROR_DESCOMPRIR = "validacion.error.descomprir";

    private static final String VALIDACION_ERROR_DESENCRIPTAR = "validacion.error.desencriptar";

    private static final String VALIDACION_ERROR_CARGAR = "validacion.error.cargar";

    private static final String VALIDACION_MENSAJE_FORMATO_NODISPONIBLE = "validacion.mensaje.formato.nodisponible";

    private static final String VALIDACION_MENSAJE_TIPO_ARCHIVOS = "validacion.mensaje.tipo.archivos";

    private static final String SUBIR_FILE_FORWARD = "subirFile";    

    private static final String GET_RESULTADO_FILE_PATH = "/cargarArchivo.do?do=resultadoFile";

    private static final int TAMANIO_BUFFER = 2 * 1024;

    private static Logger logger = Logger.getLogger(cargarArchivoAction.class);

    private SeguridadDelegate delegadoSeguridad = SeguridadDelegate.getInstance();

    private CBEmpresaFormato empresaFormatoDao = new CBEmpresaFormato();

    private TaCuentasServicioEmpresaDao taCuentasServicioEmpresaDao = new TaCuentasServicioEmpresaDaoHibernate();

    private TaSecuencialDao taSecuencialDao = new TaSecuencialDaoHibernate();

    private TaLogValidacionDao logdao = new TaLogValidacionDaoHibernate();

    public ActionForward estaCargando(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + "\n";
        xml = xml + "<respuestas>" + "\n";
        String m_nvaCarga = (String) request.getParameter("nvaCarga");
        if (FLAG_SI_CARGANDO.equalsIgnoreCase(m_nvaCarga))
        {
            setFlagCargando(request, FLAG_SI_CARGANDO);
        }
        String m_Cargando = (String) request.getSession().getAttribute("cargando");
        if (m_Cargando != null)
        {
            xml = xml + "<respuesta valor=\"" + m_Cargando + "\" />" + "\n";
        }
        else
        {
            xml = xml + "<respuesta valor=\"SI\" />" + "\n";
        }

        xml = xml + "</respuestas>" + "\n";
        response.setContentType("application/xml");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        PrintWriter out = response.getWriter();
        out.println(xml);
        out.flush();
        return null;
    }

    public ActionForward subirFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String modulo = request.getParameter("modulo");
        try
        {
            if (!delegadoSeguridad.verificaDisponibilidad(modulo))
            {
                return mapping.findForward("fueraServicio");
            }
        }
        catch (Exception e)
        {
            logger.error("VALIDACION DE DISPONIBILIDAD", e);
            return mapping.findForward("fueraServicio");
        }
        String idSevxEmp = request.getParameter("idSevxEmp");
        String rucEmpresa = request.getParameter("ruc_empresa");
        return cargarArchivo(mapping, form, request, idSevxEmp, rucEmpresa, modulo);
    }

    private ActionForward cargarArchivo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            String idSevxEmp, String rucEmpresa, String modulo)
    {
        logger.info("Inicio el upload del archivo");
        request.getSession().removeAttribute("cargando");
        request.getSession().removeAttribute("beanPagCarga");
        setFlagCargando(request, FLAG_SI_CARGANDO);
        ActionRedirect actionRedirect = new ActionRedirect();
        actionRedirect.addParameter("idSevxEmp", idSevxEmp);
        actionRedirect.addParameter("ruc_empresa", (String) rucEmpresa);
        FileUploadForm fileUploadForm = (FileUploadForm) form;
        FormFile formFile = fileUploadForm.getMyFile();
        if (!tieneExtensionPermitida(formFile))
        {
            return irMensajeError(mapping, request, VALIDACION_MENSAJE_TIPO_ARCHIVOS, modulo, idSevxEmp, rucEmpresa);
        }
        if (formatoNoDisponible(rucEmpresa))
        {
            return irMensajeError(mapping, request, VALIDACION_MENSAJE_FORMATO_NODISPONIBLE, modulo, idSevxEmp,
                    rucEmpresa);
        }
        SimpleDateFormat sdf_ = new SimpleDateFormat("_yyyy-MM-dd_HHmmss_");
        String mRucFechaHora = (String) rucEmpresa + sdf_.format(new Date());
        if (tieneExtension(formFile, ".txt"))
        {
            actionRedirect.addParameter("esTxt", "true");
            String rutaFileData = subirArchivo(formFile, RUTA_DATA, mRucFechaHora);
            if (rutaFileData == null)
            {
                return irMensajeError(mapping, request, VALIDACION_ERROR_CARGAR, modulo, idSevxEmp, rucEmpresa);
            }
            validarArchivo(request, rutaFileData, actionRedirect, idSevxEmp, rucEmpresa, formFile.getFileName());
        }
        else
        {
            request.setAttribute("esTxt", "false");
            String rutaFileBFin = subirArchivo(formFile, RUTA_BFIN, mRucFechaHora);
            if (rutaFileBFin == null)
            {
                return irMensajeError(mapping, request, VALIDACION_ERROR_CARGAR, modulo, idSevxEmp, rucEmpresa);
            }
            /* desencriptando el archivo en el servidor */
            String rutaFileZip = desEncriptar(new File(rutaFileBFin), RUTA_ZIP);
            if (rutaFileZip == null)
            {
                return irMensajeError(mapping, request, VALIDACION_ERROR_DESENCRIPTAR, modulo, idSevxEmp, rucEmpresa);
            }
            /* descomprimiendo el archivo en el servidor */
            String rutaFileData = desComprimir(TAMANIO_BUFFER, new File(rutaFileZip), RUTA_DATA);
            if (rutaFileData == null)
            {
                return irMensajeError(mapping, request, VALIDACION_ERROR_DESCOMPRIR, modulo, idSevxEmp, rucEmpresa);
            }
            validarArchivo(request, rutaFileData, actionRedirect, idSevxEmp, rucEmpresa, formFile.getFileName());
        }
        return actionRedirect;
    }

    private void setFlagCargando(HttpServletRequest request, String SiNoFlag)
    {
        request.getSession().setAttribute("cargando", SiNoFlag);
        logger.info(" Estado Cargando: " + request.getSession().getAttribute("cargando"));
        if (logger.isDebugEnabled())
        {
            logger.debug("ID SESSION:" + request.getSession().getId());
        }
    }

    private ActionForward irMensajeError(ActionMapping mapping, HttpServletRequest request, String mensaje,
            String modulo, String idServEmpr, String rucEmpresa)
    {
        request.setAttribute(MENSAJE_VALIDACION_CARGAS.getDescripcion(), RES_CASH.getString(mensaje));
        request.setAttribute(MODULO.getDescripcion(), modulo);
        request.setAttribute(ID_SERVICIO_EMPRESA.getDescripcion(), idServEmpr);
        request.setAttribute(RUC_EMPRESA.getDescripcion(), rucEmpresa);
        return mapping.findForward(SUBIR_FILE_FORWARD);
    }

    private boolean tieneExtension(FormFile formFile, String extension)
    {
        return formFile.getFileName().toLowerCase().endsWith(extension);
    }

    private boolean tieneExtensionPermitida(FormFile formFile)
    {
        return tieneExtension(formFile, ".txt") || tieneExtension(formFile, ".bfin");
    }

    private boolean formatoNoDisponible(String pRuc_empresa)
    {
        String existeFormato = empresaFormatoDao.obtenerXmlIn(pRuc_empresa);
        return StringUtils.isEmpty(existeFormato.trim());
    }

    private void validarArchivo(HttpServletRequest request, String rutaFileData, ActionRedirect actionRedirect,
            String pIdSevxEmp, String pRuc_empresa, String nombreFileMostrar)
    {
        HttpSession session = request.getSession();
        TaOrden orden = (TaOrden) session.getAttribute("objorden");
        if (rutaFileData != null)
        {
            String pFechaIni = orden.getForFechaInicio();
            pFechaIni = pFechaIni.substring(6) + pFechaIni.substring(3, 5) + pFechaIni.substring(0, 2);
            String pFechaFin = orden.getForFechaFin();
            pFechaFin = pFechaFin.substring(6) + pFechaFin.substring(3, 5) + pFechaFin.substring(0, 2);
            String pHoraIni = orden.getHorHoraInicio();
            if ("".equals(pHoraIni))
            {
                SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                pHoraIni = sdf.format(new Date());
            }
            String pCuentaCargo = orden.getNorNumeroCuenta();
            String pTipoIngreso = "" + orden.getDorTipoIngreso();
            String pReferencia = orden.getDorReferencia();
            String pTipoCuenta = taCuentasServicioEmpresaDao.getTipoCuenta(pCuentaCargo, Long.parseLong(pIdSevxEmp));

            int idEnvio = taSecuencialDao.getIdEnvio(Constantes.FIELD_CASH_SECUENCIAL_ID_ENVIO);

            Validador validar = new Validador();
            
            //logger.info("PORTAL==> Inicio de la validacion .......  idEnvio="+ idEnvio);
            
            
            int bvalido = validar.validarArchivo(pRuc_empresa, rutaFileData, "", idEnvio, pIdSevxEmp, pFechaIni,
                    pFechaFin, Fecha.formatearFecha("HH:mm", "HHmmss", pHoraIni), pTipoCuenta, pCuentaCargo,
                    pTipoIngreso, pReferencia);
            
            
            //logger.info("PORTAL==> Fin de la validacion .......");
            
            
            // TODO check
            if (!validar.getLin().equals(""))
            {
                request.getSession().setAttribute("linea", validar.getLin());// 04-09-09
                request.getSession().setAttribute("lineaBug", validar.getLineasbug());// 04-09-09
            }
            request.getSession().setAttribute("bvalido", bvalido);// 04-09-09
            request.getSession().setAttribute("idEnvio", idEnvio);// 04-09-09
            actionRedirect.setPath(GET_RESULTADO_FILE_PATH);
            actionRedirect.addParameter("nomFile", rutaFileData);
            actionRedirect.addParameter("nomMostrar", nombreFileMostrar);
            setFlagCargando(request, FLAG_CARGANDO_NO);
        }
    }

    public ActionForward resultadoFile(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        try
        {
            String nomMostrar = (String) request.getParameter("nomMostrar");
            if (nomMostrar != null && !nomMostrar.trim().equals(""))
            {
                request.setAttribute("nomMostrar", nomMostrar);
            }
            int bvalido = ERROR_RECUPERANDO_VALIDACION;
            bvalido = obtenerResultadoValidacion(request, bvalido);
            String forward = "";                        

            if (bvalido == EXITO)
            {
                forward = "resultadoFile";
                request.setAttribute("mensaje_validacion", RES_CASH.getString("validacion.mensaje.ok"));// 03-09-09
            }
            else
            {
                forward = "resultadoFile";
                String descrip = "";
                int idEnvio = obtenerIdEnvio(request);
                String enlace = "";
                /*
                 * Salvo error ERROR_VALIDACION_DATOS, en la pantalla de
                 * resultados de la validacion se muestra el error configurado
                 * en el archivo de propiedades
                 */
                switch (bvalido)
                {
                case ERROR_VALIDACION_DATOS:
                    descrip = RES_CASH.getString("validacion.mensaje.validar");
                    break;
                case ERROR_GUARDANDO_DATOS:
                    descrip = RES_CASH.getString("validacion.error.conexionbd");
                    break;
                case ERROR_GENERAR_XML:
                    descrip = RES_CASH.getString("validacion.error.generarxml");
                    break;
                case ERROR_ARCHIVO_VACIO:
                    descrip = RES_CASH.getString("validacion.mensajes.vacio");
                    break;
                case ERROR_DESCONOCIDO:
                    descrip = RES_CASH.getString("validacion.error.validar");
                    break;
                case ERROR_RECUPERANDO_VALIDACION:
                    descrip = RES_CASH.getString("validacion.error.resultado");
                    break;
                case ERROR_VALIDACION_CUENTAS:
                    descrip = RES_CASH.getString("validacion.error.validacionCuentas");
                    List<String> lineaError = (List<String>) request.getSession().getAttribute("lineaBug");
                    for (int i = 0; i < lineaError.size(); i++)
                    {
                        descrip += " , " + lineaError.get(i);
                    }
                    break;
                    
                case ERROR_VALIDACION_CTS:
                    descrip = RES_CASH.getString("validacion.error.validacionFormato");
                    enlace = CashConstants.ENLACE_ERROR_FORMATO_CTS;
                    break;             
                            
                    
                case ERROR_VALIDACION_MONTOS:
                    descrip = RES_CASH.getString("validacion.error.validacionMonto");
                    lineaError = (List<String>) request.getSession().getAttribute("lineaBug");
                    for (int i = 0; i < lineaError.size(); i++)
                    {
                        descrip += " , " + lineaError.get(i);
                    }
                    break;
                    

                }

                long cantItems = logdao.obtenerCantErrores(idEnvio);
                if (logger.isDebugEnabled())
                {
                    logger.debug("Obtenido " + cantItems + " errores para idEnvio " + idEnvio);
                }
                List<TaLogValidacion> listaerrores = new ArrayList<TaLogValidacion>();
                /*
                 * Agregado debido a que cuando hay una excepcion en el metodo
                 * de validacion CCI y ya se grabaron algunos errores estos
                 * aparecen en la pantalla de resultados
                 */
                // TODO hacer que el metodo sea transaccional para que
                // rollbackee cualquier cambio en BD
                if (cantItems > 0 && bvalido != ERROR_DESCONOCIDO)
                {
                    int nroRegPag = obtenerNumeroRegistrosPaginacion(request);
                    int nroPag = (int) cantItems / nroRegPag;
                    int resto = (int) cantItems % nroRegPag;
                    if (resto != 0)
                    {
                        nroPag = nroPag + 1;
                    }
                    BeanPaginacion bpag = new BeanPaginacion();
                    bpag.setM_pagActual(1);
                    bpag.setM_pagFinal(nroPag);
                    bpag.setM_pagInicial(1);
                    bpag.setM_regPagina(nroRegPag);
                    request.getSession().setAttribute("beanPagCarga", bpag);
                    listaerrores = logdao.obtenerLogErrores(idEnvio, bpag);

                }
                // cuando los errores no son de validacion de formato
                if (listaerrores != null && listaerrores.size() == 0)
                {
                    TaLogValidacion log = new TaLogValidacion();
                    TaLogValidacionId id = new TaLogValidacionId(idEnvio, 1);
                    log.setDlvaDescripcion(descrip);
                    log.setId(id);
                    log.setEnlace(enlace);
                    listaerrores.add(log);
                }
                request.setAttribute("listaerrores", listaerrores);
            }            
            return mapping.findForward(forward);
        }

        catch (Exception e)
        {
            logger.error("Error resultadoFile", e);
        }
        return mapping.findForward("error");
    }

    private int obtenerNumeroRegistrosPaginacion(HttpServletRequest request)
    {
        MessageResources messageResources = getResources(request);
        int nroRegPag = Integer.parseInt(messageResources.getMessage("paginacion.logerrores"));
        return nroRegPag;
    }

    private int obtenerIdEnvio(HttpServletRequest request)
    {
        int idEnvio = 0;
        try
        {
            idEnvio = ((Integer) request.getSession().getAttribute("idEnvio")).intValue();
        }
        catch (Exception e)
        {
            logger.error("ERROR recuperando idEnvio", e);
        }
        return idEnvio;
    }

    private int obtenerResultadoValidacion(HttpServletRequest request, int bvalido)
    {
        try
        {
            bvalido = ((Integer) request.getSession().getAttribute("bvalido")).intValue();
        }
        catch (Exception e)
        {
            logger.error("Error recuperando respuesta", e);
        }
        return bvalido;
    }

    // jmoreno 02/10/09
    public ActionForward resultadoFilePag(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        int idEnvio = 0;
        try
        {
            idEnvio = ((Integer) request.getSession().getAttribute("idEnvio")).intValue();
        }
        catch (Exception e)
        {
            logger.error("ERROR:" + e.toString(), e);
        }
        String nomMostrar = (String) request.getParameter("nomMostrar");
        if (nomMostrar != null & !nomMostrar.trim().equals(""))
        {
            request.setAttribute("nomMostrar", nomMostrar);
        }        
        BeanPaginacion bpag = (BeanPaginacion) request.getSession().getAttribute("beanPagCarga");
        String tipoPaginado = request.getParameter("tipoPaginado");
        tipoPaginado = tipoPaginado == null ? "P" : tipoPaginado;
        if ("P".equals(tipoPaginado))
        {
            bpag.setM_pagActual(1);
        }
        else if ("U".equals(tipoPaginado))
        {
            bpag.setM_pagActual(bpag.getM_pagFinal());
        }
        else if ("S".equals(tipoPaginado))
        {
            if (bpag.getM_pagActual() < bpag.getM_pagFinal())
            {
                bpag.setM_pagActual(bpag.getM_pagActual() + 1);
            }

        }
        else if ("A".equals(tipoPaginado))
        {
            if (bpag.getM_pagActual() > bpag.getM_pagInicial())
            {
                bpag.setM_pagActual(bpag.getM_pagActual() - 1);
            }
        }
        request.getSession().setAttribute("beanPagCarga", bpag);
        List listaerrores = logdao.obtenerLogErrores(idEnvio, bpag);
        request.setAttribute("listaerrores", listaerrores);
        return mapping.findForward("resultadoFile");
    }

    public String subirArchivo(FormFile ff, String ruta, String mNombreAudit)
    {
        SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("Iniciando: Escribir archivo ...  " + sdf_.format(new Date()));
        try
        {
            InputStream is = ff.getInputStream();
            File nvo = new File(ruta + mNombreAudit + ff.getFileName());
            FileOutputStream os = new FileOutputStream(nvo);
            byte[] b = new byte[TAMANIO_BUFFER];
            int bytesRead = 0;
            while ((bytesRead = is.read(b)) != -1)
            {
                os.write(b, 0, bytesRead);
                os.flush();
            }
            os.close();
            is.close();
            logger.info("Terminando: Escribir archivo ...  " + sdf_.format(new Date()));
            return nvo.getAbsolutePath();
        }
        catch (Exception e)
        {
            logger.error("ERROR:" + e.toString());
            return null;
        }
    }

    public String desComprimir(int BUFFER, File fzip, String ruta)
    {
        SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("Iniciando: Descomprimir archivo ...  " + sdf_.format(new Date()));
        try
        {
            BufferedOutputStream dest = null;
            BufferedInputStream bis = null;
            ZipEntry entry;
            ZipFile zipfile = new ZipFile(fzip);
            Enumeration e = zipfile.entries();
            File nvo = null;
            while (e.hasMoreElements())
            {
                entry = (ZipEntry) e.nextElement();
                bis = new BufferedInputStream(zipfile.getInputStream(entry));
                int count;
                byte data[] = new byte[BUFFER];
                nvo = new File(ruta + fzip.getName().substring(0, fzip.getName().indexOf(".")) + ".txt");// +
                                                                                                         // entry.getName()//jmoreno
                                                                                                         // 02-09-09
                FileOutputStream fos = new FileOutputStream(nvo);
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = bis.read(data, 0, BUFFER)) != -1)
                {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
                bis.close();
            }
            return nvo.getAbsolutePath();
        }
        catch (Exception e)
        {
            logger.error("ERROR:" + e.toString());
            return null;
        }
    }

    private String desEncriptar(File fdatEnc, String ruta)
    {
        SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        logger.info("Iniciando: Desencriptar archivo ...  " + sdf_.format(new Date()));

        TripleDES tdes = new TripleDES();
        String key1 = "62BA37D6E56D7061";
        String key2 = "403294A4B3DA082C";
        String key3 = "2549FBD085295DAB";
        try
        {

            BufferedOutputStream dest = null;
            BufferedInputStream bis = null;
            int BUF = 16;
            byte data[] = new byte[BUF];
            int count;
            File nvo = new File(ruta + fdatEnc.getName().substring(0, fdatEnc.getName().indexOf(".")) + ".zip");// new
                                                                                                                // File(rutaDesenc);

            FileOutputStream fos = new FileOutputStream(nvo);
            InputStream is = new FileInputStream(fdatEnc);
            bis = new BufferedInputStream(is);
            dest = new BufferedOutputStream(fos, 16);
            int tam = (int) fdatEnc.length();
            int nbloq = tam / BUF;
            count = bis.read(data, 0, BUF);
            String aux = new String(data);
            aux = tdes.decipher_2(aux, key1, key2, key3);
            data = tdes.toString_2(aux);
            byte arestox[] = { data[7] };
            int restox = Integer.parseInt(new String(arestox));
            String cab = new String(data);
            cab = cab.substring(0, 8);
            if (("archivo" + restox).equals(cab))
            {

                for (int i = 0; i < nbloq - 1; i++)
                {
                    count = bis.read(data, 0, BUF);
                    String uno = new String(data);
                    uno = tdes.decipher_2(uno, key1, key2, key3);
                    data = tdes.toString_2(uno);
                    /*
                     * en el ultimo bloque se le añade solo el resto(si es que
                     * hay) para que no dañe la reconstruccion del archivo zip
                     */
                    if (i == (nbloq - 2))
                    {
                        if (restox > 0)
                        {
                            dest.write(data, 0, restox);
                        }
                        else
                        {
                            dest.write(data, 0, data.length / 2);
                        }
                    }
                    else
                    {
                        dest.write(data, 0, data.length / 2);
                    }
                }
                dest.flush();
                dest.close();
                bis.close();
                return nvo.getAbsolutePath();
            }
            else
            {
                nvo.delete();
                return null;
            }

        }
        catch (Exception e)
        {
            logger.error("ERROR:" + e.toString());
            return null;
        }
    }

    public ActionForward formatoCTS(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        String forward = "formatoCTS";
        return mapping.findForward(forward);
    }

    public ActionForward formatoCCI(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException
    {
        return mapping.findForward("formatoCCI");
    }

}

package com.financiero.cash.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Form;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResult;
import org.apache.commons.validator.ValidatorResults;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.financiero.cash.beans.TransferenciaBean;
import com.financiero.cash.dao.EntityDAO;
import com.financiero.cash.delegate.TransferenciasDelegate;
import com.financiero.cash.exception.BFPBusinessException;
import com.financiero.cash.ibs.util.IbsUtils;
import com.financiero.cash.util.TipoTransferencia;
import com.financiero.cash.util.spring.ConfigLoader;
import com.hiper.cash.dao.TaLogValidacionDao;
import com.hiper.cash.dao.TaTipoPagoServicioDao;
import com.hiper.cash.dao.TmFormatoDao;
import com.hiper.cash.dao.TmServicioDao;
import com.hiper.cash.dao.TransferenciaDAO;
import com.hiper.cash.dao.hibernate.TaLogValidacionDaoHibernate;
import com.hiper.cash.dao.hibernate.TaTipoPagoServicioDaoHibernate;
import com.hiper.cash.dao.hibernate.TmFormatoDaoHibernate;
import com.hiper.cash.dao.hibernate.TmServicioDaoHibernate;
import com.hiper.cash.domain.TaLogValidacion;
import com.hiper.cash.domain.TaLogValidacionId;
import com.hiper.cash.domain.TaServicioxEmpresa;
import com.hiper.cash.entidad.BeanTipoPagoServicio;
import com.hiper.cash.util.CashConstants;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Util;

public class ValidadorServiceImpl
{

    private static final String MSG_EXCEPTION_FORMATO_INCOMPLETO = "Formato pago CCI incompleto";   

    private static ValidadorServiceImpl instanciaUnica;

    public static final int NUMERO_LINEA_PARA_ERROR_GLOBAL_ARCHIVO = 0;

    private static final String CLAVE_MAP_MONTOS_ACUMULADO_TRANSFERENCIAS_DOLARES = "montosAcumuladoTransferenciasDolares";

    private static final String CLAVE_MAP_MONTOS_ACUMULADO_TRANSFERENCIAS_SOLES = "montosAcumuladoTransferenciasSoles";

    private static final String CLAVE_MAP_SUMA_MONTOS_SOLES_CCI = "soles";

    private static final String CLAVE_MAP_SUMA_MONTOS_DOLARES_CCI = "dolares";

    public static final String CAMPO_MONEDA = "dDBuMoneda";

    private static final String CAMPO_MONTO = "nDBuMonto";

    public static final String CAMPO_NUMERO_DOCUMENTO = "dDBuContrapartida";

    public static final String CAMPO_TIPO_DOCUMENTO = "dDBuTipoDocumento";

    private static final String CAMPO_TIPO_PAGO = "dDBuTipoPago";

    private static final String CAMPO_CUENTA_EMPRESA_CARGO = "nDBuCuentaEmpresa";

    private static final String CAMPO_CUENTA_ABONO = "nDBuNumeroCuenta";

    private static final String CAMPO_NOMBRE_COMPLETO = "dDBuReferencia";

    private static final String CAMPO_ADICIONAL_1_DIRECCION = "dDBuAdicional1";

    private static final String CAMPO_ADICIONAL_2_TELEFONO = "dDBuAdicional2";

    private static final String DELIMITADOR_TAB = "\t";

    private static final String CCI_VALIDATOR_CONFIG_XML = "cci-validator-config.xml";

    private static final String FORM_BEAN_CCI = "cciFormatForm";

    private static final String MSG_KEY_CUENTA_EMPRESA = "validacion.pagoscci.fields.nDBuCuentaEmpresa";

    private static final String MSG_KEY_MONTO = "validacion.pagoscci.fields.nDBuMonto";

    private static final String MSG_KEY_MONEDA = "validacion.pagoscci.fields.dDBuMoneda";

    private static final String MSG_KEY_CUENTA_ABONO = "validacion.pagoscci.fields.nDBuNumeroCuenta";

    private static final String MSG_KEY_TIPO_DOCUMENTO = "validacion.pagoscci.fields.dDBuTipoDocumento";

    private static final String MSG_KEY_NUMERO_DOCUMENTO = "validacion.pagoscci.fields.nDBuDocumento";

    private static final String MSG_KEY_NOMBRE_COMPLETO = "validacion.pagoscci.fields.dDBuReferencia";

    private static final String MSG_KEY_DIRECCION = "validacion.pagoscci.fields.dDBuAdicional1";

    private static final String MSG_KEY_TELEFONO = "validacion.pagoscci.fields.dDBuAdicional2";

    private static final String MSG_KEY_FORMATO_INCOMPLETO = "validacion.pagoscci.mensajes.formatoIncompleto";    

    private TaLogValidacionDao taLogValidacionDao = new TaLogValidacionDaoHibernate();

    static Logger logger = Logger.getLogger(ValidadorServiceImpl.class);

    private TmFormatoDao formatoDao = new TmFormatoDaoHibernate();

    private TaTipoPagoServicioDao tipoPagoServicioDao = new TaTipoPagoServicioDaoHibernate();

    private TransferenciasDelegate transferenciasDelegate = TransferenciasDelegate.getInstance();

    private TransferenciaDAO transferenciaDAO = null;
    
    private TmServicioDao tmServicioDao = new TmServicioDaoHibernate();

    public static ValidadorServiceImpl getInstance()
    {
        if (instanciaUnica == null)
        {
            instanciaUnica = new ValidadorServiceImpl();
        }
        return instanciaUnica;
    }

    private ValidadorServiceImpl()
    {
        try
        {
            transferenciaDAO = (TransferenciaDAO) ConfigLoader.getInstance().getBean("transferenciaDao");
        }
        catch (Exception e)
        {
            logger.error("Error accediendo a bean de spring", e);
            throw new RuntimeException("Error accediendo a bean de spring", e);
        }
    }

    private List<String> obtenerNombresCamposFormatoEntrada(long idServicioEmpresa)
    {
        TaServicioxEmpresa taServicioxEmpresa;
        try
        {
            taServicioxEmpresa = EntityDAO.findById(TaServicioxEmpresa.class, idServicioEmpresa);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        int idFormatoEntrada = taServicioxEmpresa.getCseformatoEntrada();
        return formatoDao.obtenerNombresCampos(idFormatoEntrada);
    }

    /**
     * Obtiene la clase con las mismas propiedades del formato que corresponde
     * al idServicioEmpresa
     * 
     * @param idServicioEmpresa
     * @return
     */
    private DynaClass obtenerClaseParaFormato(long idServicioEmpresa)
    {
        List<String> nombresCampos = obtenerNombresCamposFormatoEntrada(idServicioEmpresa);
        if (logger.isDebugEnabled())
        {
            logger.debug("Obtenido formato para idServicioEmpresa " + idServicioEmpresa);
            int i = 0;
            for (String campo : nombresCampos)
            {
                logger.debug(i++ + " " + campo);
            }
        }
        List<DynaProperty> propiedadesBean = null;
        if (nombresCampos != null && nombresCampos.size() > 0)
        {
            propiedadesBean = new ArrayList<DynaProperty>();
            for (String nombreCampo : nombresCampos)
            {
                propiedadesBean.add(new DynaProperty(nombreCampo, String.class));
            }
            propiedadesBean.add(new DynaProperty(CashConstants.NOMBRE_CAMPO_SOPORTA_CCI, Boolean.class));
        }
        return new BasicDynaClass("formatoPagoCci", null, propiedadesBean.toArray(new DynaProperty[propiedadesBean
                .size()]));
    }

    private boolean esCampoMonto(String nombreCampo)
    {
        return CAMPO_MONTO.equals(nombreCampo);
    }

    public boolean validarArchivoDePagosCCI(String archivo, long idServicioEmpresa, long idEnvio)
    {
        if(!esServicioDePagoProveedores(idServicioEmpresa)){
            return true;
        }
        logger.info("Inicia validacion pagosCCI");
        boolean exitoArchivo = true;
        Map<String, Double> montosCCI = null;
        try
        {
            BufferedReader entrada = new BufferedReader(new FileReader(archivo));
            // Obtenido clase con los campos del formato
            DynaClass formatoPagoCciClass = obtenerClaseParaFormato(idServicioEmpresa);
            ValidatorResources resources = getValidatorResourcesCCI();
            Validator validator = new Validator(resources, FORM_BEAN_CCI);
            boolean soportaPagosCCI = soportaPagosCCI(idServicioEmpresa);
            int numeroLinea = 1;
            montosCCI = inicializarMapaMontosCCI();
            DynaBean beanFormatoPagoCci = null;
            while (entrada.ready())
            {
                String linea = entrada.readLine();
                boolean exitoLinea;
                beanFormatoPagoCci = instanciarBeanFormatoPagoCci(formatoPagoCciClass, beanFormatoPagoCci);
                try
                {
                    exitoLinea = validarLinea(validator, formatoPagoCciClass, resources, beanFormatoPagoCci, linea,
                            idServicioEmpresa, idEnvio, numeroLinea, soportaPagosCCI);
                    obtenerMontosAcumuladoDeTransferenciasYSumarizarMontosPagosCCI(formatoPagoCciClass,
                            beanFormatoPagoCci, linea, idServicioEmpresa, numeroLinea, montosCCI, soportaPagosCCI);
                }
                // errores recuperables
                catch (ValidatorException e)
                {                    
                    exitoLinea = false;
                }
                catch (IllegalArgumentException e)
                {
                    logger.error("Error validando linea " + numeroLinea, e);
                    exitoLinea = false;
                }
                // errores no recuperables
                catch (Exception e)
                {
                    throw new RuntimeException("Error validando linea " + numeroLinea, e);
                }
                if (logger.isDebugEnabled())
                {
                    logger.debug("Terminado de validar linea " + numeroLinea + " con resultado " + exitoLinea);
                }
                exitoArchivo = exitoArchivo && exitoLinea;
                numeroLinea++;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error accediendo al archivo", e);
        }
        if (exitoArchivo)
        {
            exitoArchivo = validarLimitesMontoDiarioTransferencias(idEnvio, montosCCI);
        }
        logger.info("Resultado de la validacion pagos CCI: " + exitoArchivo);
        return exitoArchivo;
    }

    private DynaBean instanciarBeanFormatoPagoCci(DynaClass formatoPagoCciClass, DynaBean beanFormatoPagoCci)
    {
        try
        {
            beanFormatoPagoCci = formatoPagoCciClass.newInstance();
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("Error instanciando bean dinamico", e);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException("Error instanciando bean dinamico", e);
        }
        return beanFormatoPagoCci;
    }

    /**
     * Indica si el servicio empresa acepta el tipo de pago PCCI
     * 
     * @param idServicioEmpresa
     * @return
     */
    private boolean soportaPagosCCI(long idServicioEmpresa)
    {
        List<BeanTipoPagoServicio> tiposPago = tipoPagoServicioDao.select(idServicioEmpresa);
        for (BeanTipoPagoServicio tipoPago : tiposPago)
        {
            if (CashConstants.TIPO_PAGO_CCI.equals(tipoPago.getM_TipoPagoServicio()))
            {
                return true;
            }
        }
        return false;
    }

    private boolean validarLimitesMontoDiarioTransferencias(long idEnvio, Map<String, Double> montosCCI)
    {
        boolean valido = true;
        Double sumatoriaMontosSolesCci = montosCCI.get(CLAVE_MAP_SUMA_MONTOS_SOLES_CCI);
        Double sumatoriaMontosDolaresCci = montosCCI.get(CLAVE_MAP_SUMA_MONTOS_DOLARES_CCI);
        if (sumatoriaMontosDolaresCci > 0 || sumatoriaMontosSolesCci > 0)
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Valores de montos CCI: " + Util.debugMap(montosCCI));
            }
            double limiteDiarioSoles = transferenciasDelegate.obtenerLimitesDiarioTransferenciasSoles();
            double limiteDiarioDolares = transferenciasDelegate.obtenerLimitesDiarioTransferenciasDolares();

            StringBuffer mensajeErrorValidacionLimitesTransferencias = new StringBuffer();
            if (sumatoriaMontosSolesCci != null && sumatoriaMontosSolesCci.doubleValue() > 0)
            {
                try
                {
                    transferenciasDelegate.validarLimitesMontoDiarioTransferenciaSoles(limiteDiarioSoles,
                            montosCCI.get(CLAVE_MAP_MONTOS_ACUMULADO_TRANSFERENCIAS_SOLES), sumatoriaMontosSolesCci);
                }
                catch (BFPBusinessException e)
                {
                    valido = false;
                    mensajeErrorValidacionLimitesTransferencias.append(e.getMessage());
                }
            }

            if (sumatoriaMontosDolaresCci != null && sumatoriaMontosDolaresCci.doubleValue() > 0)
            {
                try
                {
                    transferenciasDelegate
                            .validarLimitesMontoDiarioTransferenciaDolares(limiteDiarioDolares,
                                    montosCCI.get(CLAVE_MAP_MONTOS_ACUMULADO_TRANSFERENCIAS_DOLARES),
                                    sumatoriaMontosDolaresCci);
                }
                catch (Exception e)
                {
                    valido = false;
                    mensajeErrorValidacionLimitesTransferencias.append(", " + e.getMessage());
                }
            }
            grabarLogErrorPagoCCI(idEnvio, NUMERO_LINEA_PARA_ERROR_GLOBAL_ARCHIVO,
                    mensajeErrorValidacionLimitesTransferencias.toString());
        }
        return valido;
    }

    private Map<String, Double> inicializarMapaMontosCCI()
    {
        Map<String, Double> montosCCI;
        montosCCI = new HashMap<String, Double>();
        montosCCI.put(CLAVE_MAP_SUMA_MONTOS_DOLARES_CCI, 0.0);
        montosCCI.put(CLAVE_MAP_SUMA_MONTOS_SOLES_CCI, 0.0);
        montosCCI.put(CLAVE_MAP_MONTOS_ACUMULADO_TRANSFERENCIAS_SOLES, null);
        montosCCI.put(CLAVE_MAP_MONTOS_ACUMULADO_TRANSFERENCIAS_DOLARES, null);
        return montosCCI;
    }

    /**
     * Se obtiene el bean dinamico(con los valores llenos) que mapea una linea
     * del archivo con un formato determinado
     * 
     * @param idServicioEmpresa
     * @param formatoPagoCciClass
     * @param camposLinea
     * @return
     */
    private DynaBean poblarBeanFormatoPago(long idServicioEmpresa, DynaClass formatoPagoCciClass,
            DynaBean beanFormatoPagoCci, String[] camposLinea, boolean soportaCCI)
    {
        int i = 0;
        for (String nombreCampo : obtenerNombresCamposFormatoEntrada(idServicioEmpresa))
        {
            beanFormatoPagoCci.set(nombreCampo, camposLinea[i++].trim());
        }
        beanFormatoPagoCci.set(CashConstants.NOMBRE_CAMPO_SOPORTA_CCI, soportaCCI);
        return beanFormatoPagoCci;
    }

    /**
     * Valida una linea del archivo de carga
     * 
     * @param validator
     * @param formatoClass
     * @param resources
     * @param linea
     * @param idServicioEmpresa
     * @param idEnvio
     * @param numeroLinea
     * @param soportaPagosCCI
     * @return
     * @throws ValidatorException
     *             Si se lanza esta excepcion la validacion de la linea termina,
     *             pero el proceso continua
     */
    public boolean validarLinea(Validator validator, DynaClass formatoClass, ValidatorResources resources,
            DynaBean beanFormatoPagoCci, String linea, long idServicioEmpresa, long idEnvio, int numeroLinea,
            boolean soportaPagosCCI) throws ValidatorException
    {
        String[] camposLinea = obtenerCamposLinea(linea);
        try
        {
            validacionCantidadCamposLinea(idServicioEmpresa, numeroLinea, camposLinea);
        }
        catch (ValidatorException e)
        {
            grabarLogErrorPagoCCI(idEnvio, numeroLinea, e.getMessage());
            throw new ValidatorException(e.getMessage());
        }
        beanFormatoPagoCci = poblarBeanFormatoPago(idServicioEmpresa, formatoClass, beanFormatoPagoCci, camposLinea,
                soportaPagosCCI);
        if (!esLineaCCI(beanFormatoPagoCci))
        {
            return true;
        }
        if (!validarSiFormatoCCIEstaCompletoYGrabarLog(formatoClass, idEnvio, numeroLinea))
        {
            throw new ValidatorException(MSG_EXCEPTION_FORMATO_INCOMPLETO);
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Linea CCI esta completa. Validando datos de forma en linea " + numeroLinea + "...");
            logger.debug("Bean de pago CCI obtenido: " + ToStringBuilder.reflectionToString(beanFormatoPagoCci));
        }
        ValidatorResults results = validarFormatoLinea(validator, beanFormatoPagoCci, numeroLinea);
        if (!guardarErroresValidacionFormatoLinea(results, resources, idEnvio, numeroLinea))
        {
            return false;
        }
        if (logger.isDebugEnabled())
        {
            logger.debug("Datos de forma correctos. Validando datos contra IBS...");
        }
        TransferenciaBean transferencia = obtenerTransferenciaBean(beanFormatoPagoCci);
        return validarDatosEnIbs(idEnvio, numeroLinea, transferencia);

    }

    private String obtenerCampoFaltanteSiFormatoIncompleto(DynaClass formatoClass)
    {
        if (formatoClass.getDynaProperty(CAMPO_ADICIONAL_1_DIRECCION) == null)
        {
            return CashConstants.RES_CASH.getString(MSG_KEY_DIRECCION);
        }
        if (formatoClass.getDynaProperty(CAMPO_ADICIONAL_2_TELEFONO) == null)
        {
            return CashConstants.RES_CASH.getString(MSG_KEY_TELEFONO);
        }
        if (formatoClass.getDynaProperty(CAMPO_CUENTA_EMPRESA_CARGO) == null)
        {
            return CashConstants.RES_CASH.getString(MSG_KEY_CUENTA_EMPRESA);
        }
        if (formatoClass.getDynaProperty(CAMPO_MONEDA) == null)
        {
            return CashConstants.RES_CASH.getString(MSG_KEY_MONEDA);
        }
        if (formatoClass.getDynaProperty(CAMPO_MONTO) == null)
        {
            return CashConstants.RES_CASH.getString(MSG_KEY_MONTO);
        }
        if (formatoClass.getDynaProperty(CAMPO_CUENTA_ABONO) == null)
        {
            return CashConstants.RES_CASH.getString(MSG_KEY_CUENTA_ABONO);
        }
        if (formatoClass.getDynaProperty(CAMPO_TIPO_DOCUMENTO) == null)
        {
            return CashConstants.RES_CASH.getString(MSG_KEY_TIPO_DOCUMENTO);
        }
        if (formatoClass.getDynaProperty(CAMPO_NUMERO_DOCUMENTO) == null)
        {
            return CashConstants.RES_CASH.getString(MSG_KEY_NUMERO_DOCUMENTO);
        }
        if (formatoClass.getDynaProperty(CAMPO_NOMBRE_COMPLETO) == null)
        {
            return CashConstants.RES_CASH.getString(MSG_KEY_NOMBRE_COMPLETO);
        }
        return null;
    }

    private boolean validarSiFormatoCCIEstaCompletoYGrabarLog(DynaClass formatoClass, long idEnvio, int numeroLinea)
    {        
        String nombreCampoFaltante = obtenerCampoFaltanteSiFormatoIncompleto(formatoClass);
        if (nombreCampoFaltante != null)
        {
            String mensajeErrorCampoFaltante = CashConstants.RES_CASH.getString(MSG_KEY_FORMATO_INCOMPLETO);
            Object[] args = { nombreCampoFaltante };
            grabarLogErrorPagoCCI(idEnvio, numeroLinea, MessageFormat.format(mensajeErrorCampoFaltante, args));
            return false;
        }
        return true;
    }

    /**
     * 
     * @param formatoClass
     * @param linea
     * @param idServicioEmpresa
     * @param numeroLinea
     * @param montosCCI
     *            En caso de algun error inespe
     */
    public void obtenerMontosAcumuladoDeTransferenciasYSumarizarMontosPagosCCI(DynaClass formatoClass,
            DynaBean beanFormatoPagoCci, String linea, long idServicioEmpresa, int numeroLinea,
            Map<String, Double> montosCCI, boolean soportaPagosCCI)
    {
        // String[] camposLinea = obtenerCamposLinea(linea);
        // validacionCantidadCamposLinea(idServicioEmpresa, numeroLinea,
        // camposLinea);
        // DynaBean beanFormatoPagoCci =
        // obtenerBeanFormatoPago(idServicioEmpresa, formatoClass, camposLinea,
        // soportaPagosCCI);

        if (esLineaCCI(beanFormatoPagoCci))
        {
            String tipoMoneda = (String) beanFormatoPagoCci.get(CAMPO_MONEDA);
            double monto = obtenerMontoFormatoPagos((String) beanFormatoPagoCci.get(CAMPO_MONTO));
            if (Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES.equals(tipoMoneda))
            {
                Double sumaPagosDolares = montosCCI.get(CLAVE_MAP_SUMA_MONTOS_DOLARES_CCI);
                sumaPagosDolares += monto;
                montosCCI.put(CLAVE_MAP_SUMA_MONTOS_DOLARES_CCI, sumaPagosDolares);
            }
            if (Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(tipoMoneda))
            {
                Double sumaPagosSoles = montosCCI.get(CLAVE_MAP_SUMA_MONTOS_SOLES_CCI);
                sumaPagosSoles += monto;
                montosCCI.put(CLAVE_MAP_SUMA_MONTOS_SOLES_CCI, sumaPagosSoles);
            }
            if (montosCCI.get(CLAVE_MAP_MONTOS_ACUMULADO_TRANSFERENCIAS_DOLARES) == null
                    && montosCCI.get(CLAVE_MAP_MONTOS_ACUMULADO_TRANSFERENCIAS_SOLES) == null)
            {
                obtenerAcumuladosTransferenciasSolesDolares(montosCCI, beanFormatoPagoCci);
            }
        }
    }

    public double obtenerMontoFormatoPagos(String strMonto)
    {
        double monto = 0.0;
        try
        {
            monto = Double.valueOf(strMonto) / 100;
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Se esperaba valor decimal: " + strMonto, e);
        }
        return monto;
    }

    private void obtenerAcumuladosTransferenciasSolesDolares(Map<String, Double> montosCCI, DynaBean beanFormatoPagoCci)
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("Obteniendo los montos acumulados de transferencia para la cuenta...");
        }
        TransferenciaBean transferencia = obtenerTransferenciaBean(beanFormatoPagoCci);
        /*
         * solo se invoca a la transaccion, mas no se verifica el codigo de
         * error, debido a que no es necesario para obtener los montos
         * acumulados
         */
        String tramaRespuesta = transferenciaDAO.validarDatosTransferencia(transferencia);
        IbsUtils.validarColasNoDisponibles(tramaRespuesta);
        double acumuladoDolares = transferenciasDelegate.obtenerAcumuladoDolares(tramaRespuesta);
        double acumuladoSoles = transferenciasDelegate.obtenerAcumuladoSoles(tramaRespuesta);
        montosCCI.put(CLAVE_MAP_MONTOS_ACUMULADO_TRANSFERENCIAS_DOLARES, acumuladoDolares);
        montosCCI.put(CLAVE_MAP_MONTOS_ACUMULADO_TRANSFERENCIAS_SOLES, acumuladoSoles);
    }

    private boolean esLineaCCI(DynaBean beanFormatoPagoCci)
    {
        return CashConstants.TIPO_PAGO_CCI.equals(beanFormatoPagoCci.get(CAMPO_TIPO_PAGO));
    }

    private boolean validarDatosEnIbs(long idEnvio, int numeroLinea, TransferenciaBean transferencia)
    {
        boolean validacionIbs = true;
        try
        {
            String tramaRespuesta = transferenciasDelegate
                    .invocarValidacionTransferenciaYManejarRespuestaPagosCCI(transferencia);
            char estadoCuentaOrigen = transferenciasDelegate
                    .obtenerEstadoCuentaOrigenTramaValidacionCuentas(tramaRespuesta);
            String codigoCuentaOrigen = transferenciasDelegate
                    .obtenerCodigoCuentaOrigenTramaValidacionCuentas(tramaRespuesta);
            transferenciasDelegate.validarEstadoTipoCuentaOrigen(estadoCuentaOrigen, codigoCuentaOrigen);            
            transferenciasDelegate.validarMonedaPagoCCI(tramaRespuesta, transferencia.getMoneda());             
        }
        catch (BFPBusinessException e)
        {
            grabarLogErrorPagoCCI(idEnvio, numeroLinea, e.getMessage());
            validacionIbs = false;
        }
        return validacionIbs;
    }

    private String[] obtenerCamposLinea(String linea)
    {
        // Se pasa el parametro -1 al metodo split para delimitar incluso los
        // campos vacios
        String[] camposLinea = linea.split(DELIMITADOR_TAB, -1);
        return camposLinea;
    }

    /**
     * Valida el bean de pagosCCI usando el framework de validacion
     * ValidatorException indica un error inesperado al validar. Debido a esto
     * se propaga como RuntimeException
     * 
     * @param validator
     * @param beanFormatoPagoCci
     * @return
     */
    private ValidatorResults validarFormatoLinea(Validator validator, DynaBean beanFormatoPagoCci, int numeroLinea)
    {
        validator.setParameter(Validator.BEAN_PARAM, beanFormatoPagoCci);
        ValidatorResults results;
        try
        {
            results = validator.validate();
        }
        catch (ValidatorException e)
        {
            throw new RuntimeException("Error ejecutando reglas validacion en linea " + numeroLinea, e);
        }
        return results;
    }

    private void validacionCantidadCamposLinea(long idServicioEmpresa, int numeroLinea, String[] camposLinea)
            throws ValidatorException
    {
        int cantidadCamposConfigurados = obtenerCantidadCamposConfigurados(idServicioEmpresa);
        int cantidadCamposLinea = camposLinea.length;
        if (cantidadCamposLinea < cantidadCamposConfigurados)
        {
            throw new ValidatorException(
                    "La linea tiene menos campos que los campos configurados del formato. Campos en linea: "
                            + cantidadCamposLinea + ", campos en el formato: " + cantidadCamposConfigurados);
        }
        if (cantidadCamposLinea > cantidadCamposConfigurados)
        {
            throw new ValidatorException(
                    "La linea tiene mas campos que los campos configurados del formato. Campos en linea: "
                            + cantidadCamposLinea + ", campos en el formato: " + cantidadCamposConfigurados);
        }
    }

    private int obtenerCantidadCamposConfigurados(long idServicioEmpresa)
    {
        int respuesta = 0;
        List<String> nombresCampos = obtenerNombresCamposFormatoEntrada(idServicioEmpresa);
        if (nombresCampos != null)
        {
            respuesta = nombresCampos.size();
        }
        return respuesta;
    }

    private TransferenciaBean obtenerTransferenciaBean(DynaBean beanFormatoPagoCci)
    {
        TransferenciaBean transferencia = new TransferenciaBean();
        // datos necesarios para la trx ibs de validacion
        String cuentaAbono = beanFormatoPagoCci.get(CAMPO_CUENTA_ABONO).toString();
        transferencia.setCuentaAbono(cuentaAbono);
        String cuentaCargo = beanFormatoPagoCci.get(CAMPO_CUENTA_EMPRESA_CARGO).toString();
        transferencia.setCuentaCargo(cuentaCargo);
        String tipoDocumento = beanFormatoPagoCci.get(CAMPO_TIPO_DOCUMENTO).toString();
        transferencia.setDocumento(tipoDocumento);
        transferencia.setNroDocumento(beanFormatoPagoCci.get(CAMPO_NUMERO_DOCUMENTO).toString());
        transferencia.setTipo(TipoTransferencia.IT);
        transferencia.setMoneda(beanFormatoPagoCci.get(CAMPO_MONEDA).toString());
        return transferencia;
    }

    private ValidatorResources getValidatorResourcesCCI()
    {
        InputStream in = null;
        ValidatorResources resources = null;
        try
        {
            in = this.getClass().getResourceAsStream(CCI_VALIDATOR_CONFIG_XML);
            resources = new ValidatorResources(in);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error obteniendo la configuracion definida en " + in.toString(), e);
        }
        catch (SAXException e)
        {
            throw new RuntimeException("Error obteniendo la configuracion definida en " + in.toString(), e);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    throw new RuntimeException("Error cerrando el stream", e);
                }
            }
        }
        return resources;
    }

    /**
     * Guarda los errores en base de datos y devuelve el resultado de la
     * validacion
     * 
     * @param results
     * @param resources
     * @param idEnvio
     * @param numeroLinea
     * @return
     */
    private boolean guardarErroresValidacionFormatoLinea(ValidatorResults results, ValidatorResources resources,
            long idEnvio, int numeroLinea)
    {
        boolean exito = true;
        // Start by getting the form for the current locale and Bean.
        Form form = resources.getForm(Locale.getDefault(), FORM_BEAN_CCI);

        // Iterate over each of the properties of the Bean which had messages.
        Iterator propertyNames = results.getPropertyNames().iterator();
        StringBuffer mensajeErrorLinea = new StringBuffer();
        boolean esPrimerError = true;
        while (propertyNames.hasNext())
        {
            String propertyName = (String) propertyNames.next();

            // Get the Field associated with that property in the Form
            Field field = form.getField(propertyName);

            // Look up the formatted name of the field from the Field arg0
            String prettyFieldName = CashConstants.RES_CASH.getString(field.getArg(0).getKey());

            // Get the result of validating the property.
            ValidatorResult result = results.getValidatorResult(propertyName);

            // Get all the actions run against the property, and iterate over
            // their names.
            Iterator<String> actions = (Iterator<String>) result.getActions();

            while (actions.hasNext())
            {
                String actName = actions.next();
                // Get the Action for that name.
                ValidatorAction action = resources.getValidatorAction(actName);

                // If the result is valid, print PASSED, otherwise print FAILED
                if (logger.isDebugEnabled())
                {
                    logger.debug(numeroLinea + ": " + propertyName + "[" + actName + "] ("
                            + (result.isValid(actName) ? "PASSED" : "FAILED") + ")");
                }

                // If the result failed, format the Action's message against the
                // formatted field name
                if (!result.isValid(actName))
                {
                    exito = false;
                    String message = CashConstants.RES_CASH.getString(action.getMsg());
                    Object[] args = { prettyFieldName };
                    if (!esPrimerError)
                    {
                        mensajeErrorLinea = mensajeErrorLinea.append(", ");
                    }
                    else
                    {
                        esPrimerError = false;
                    }
                    String mensajeValidacionCampo = MessageFormat.format(message, args);
                    mensajeErrorLinea = mensajeErrorLinea.append(mensajeValidacionCampo);
                }
            }
        }
        grabarLogErrorPagoCCI(idEnvio, numeroLinea, mensajeErrorLinea.toString());
        return exito;
    }

    private void grabarLogErrorPagoCCI(long idEnvio, int numeroLinea, String mensajeErrorLinea)
    {
        if (!StringUtils.isEmpty(mensajeErrorLinea))
        {
            if (logger.isDebugEnabled())
            {
                logger.debug("Mensaje de error linea " + numeroLinea + ": " + mensajeErrorLinea);
            }
            TaLogValidacion logValidacion = new TaLogValidacion();
            TaLogValidacionId id = new TaLogValidacionId();
            id.setClvaIdEnvio((int) idEnvio);
            id.setNlvaNumLinea(numeroLinea);
            logValidacion.setId(id);
            logValidacion.setDlvaDescripcion(mensajeErrorLinea.toString());
            logValidacion.setEnlace(CashConstants.ENLACE_ERROR_FORMATO_CCI);
            taLogValidacionDao.insertarLogValidacion(logValidacion);
        }
    }
    
    public boolean esServicioDePagoProveedores(long idServicioEmpresa)
    {     
        String codigoServicio = tmServicioDao.obtenerIdServicio(idServicioEmpresa);
        if (Constantes.TX_CASH_SERVICIO_PAGO_SERVICIOS.equals(codigoServicio))
        {
            return true;
        }
        return false;
    }
}

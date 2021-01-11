package com.hiper.cash.util;

import java.util.ResourceBundle;

public interface CashConstants
{
    static int DD_MM_YYYY = 0;

    static String STR_DD_MM_YYYY = "dd/MM/yyyy";

    static int DDMMYY = 1;

    static String STR_DDMMYY = "ddMMyy";

    static int YYYYMMDD = 3;

    static String STR_YYYYMMDD = "yyyyMMdd";

    static String VAL_DNI = "DNI";

    static String VAL_RUC = "RUC";

    static String PARAM_ID_EMPRESA = "param_id_empresa";

    static String COD_BANCO_FINANCIERO = "35";

    static String COD_IBS_CUENTA_CTS = "AHSF";

    static String COD_IBS_CUENTA_VISA = "VISA";

    static String TS_TRANSFERENCIAS = "03";

    static String VAL_STR_TODOS = "-1";

    static int VAL_INT_TODOS = -1;

    static long VAL_LONG_TODOS = -1;

    static char SIGNO_NEGATIVO = '-';

    static char SIGNO_POSITIVO = '+';

    static int NRO_PAGINA_IBS = 20;

    static String FR_DOUBLE = "%1$.2f";

    static int NRO_PAGINA_EXCEL = 10000;

    ResourceBundle RES_FINANCIERO = ResourceBundle.getBundle("financiero");

    ResourceBundle RES_IBS = ResourceBundle.getBundle("ibs");

    ResourceBundle RES_CASH = ResourceBundle.getBundle("CashResource");

    static String VAL_IBS_EXITO = "0000";

    static char FLAG_NO_APROBACION_AUTOMATICA = '1';

    static String VAL_IBS_SOLES = "PEN";

    static String VAL_IBS_DOLARES = "USD";

    static String VAL_IBS_EUROS = "EUR";

    static String VAL_SOLES_DESC = "Soles";

    static String VAL_DOLARES_DESC = "Dolares";

    static String VAL_EUROS_DESC = "EUROS";

    static String VAL_SOLES_SIMB = "S/.";

    static String VAL_DOLARES_SIMB = "US$";

    static String VAL_EUROS_SIMB = "€";

    static String EMP_SERV_SEDAPAL = "SEDAPAL";

    static String SERV_AGUA = "AGUA";

    static String SERV_TELEFONO = "TELEFONO";

    static String SERV_TELEFONIA = "TELEFONIA";

    static String SERV_LUZ = "LUZ";

    static String COD_IBS_NO_EXISTEN_TRANSFERENCIAS = "2012";
    static String COD_IBS_SALDO_INSUFICIENTE ="2037";
    static String COD_IBS_EXCEDE_LIMITE="2042";
    static String COD_IBS_CLIENTE_NO_EXISTE="2010";
    static String COD_IBS_INTERBANCARIA_NO_VALIDA="2017";
    static String COD_IBS_DOCUMENTO_NO_VALIDA="1236";
    static String COD_IBS_RUC_NO_VALIDA="1758";
    
    static String COD_TRANS_INTERBANCARIA = "15";

    static String COD_TRANS_TERCEROS = "12";

    static String COD_TRANS_CUENTASPROPIAS = "11";

    static String COD_MODULO_TRANSFERENCIAS = "I48";

    static String COD_MODULO_COMPROBANTES = "I52";

    static String COD_IBS_EXITO = "0000";

    static String VAL_IBS_EXITO_SEDAPAL = "100";

    static String VAL_SELECT_TODOS = "ALL";

    static String ITEMS_PROCESADO = "P";

    static String ITEMS_NO_PROCESADO = "N";

    static String COD_TC_AHORROS = "10";

    static String COD_TC_CORRIENTE = "20";

    static String COD_TC_CTS = "40";

    static String VAL_TC_AHORROS = "Ahorros";

    static String VAL_TC_CORRIENTE = "Corriente";

    static String VAL_TC_CTS = "CTS";

    String PREFIJO_CASH_MANAGEMENT = "CMG";

    Character CHAR_BLANK_SPACE = ' ';

    String STR_DDMMYYYYhhmmss = "ddMMyyyyhhmmss";

    int TAMANIO_PAGINA_TRANSFERENCIAS = Integer.valueOf(RES_CASH.getString("paginacion.transferencias"));

    String CORRELATIVO_DIARIO_ID = "ID_DIARIO_TRANSF";

    String FLAG_NUEVOS_CODIGOS_IBS = "N";

    String NOMBRE_REPORTE_DETALLE_TRANSFERENCIA = "registroTransferencia";

    String NOMBRE_REPORTE_CONSULTA_TRANSFERENCIAS = "consultaTransferencias";

    int CORRELATIVO_DIARIO = 1;

    // PAGO DE PROVEEDORES CCI
    String TIPO_PAGO_CCI = "PCCI";

    String NOMBRE_CAMPO_SOPORTA_CCI = "soportaCci";

    String ENLACE_ERROR_FORMATO_CTS = "formatoCTS";

    String ENLACE_ERROR_FORMATO_CCI = "formatoCCI";

    // transferencias
    String CODIGO_ERROR_PROCESO_VALIDACION = "003";

    String CODIGO_ERROR_CUENTA = "001";

    String CODIGO_ERROR_MONTO = "002";
    
    //carga de archivos    
    String RUTA_ZIP = RES_FINANCIERO.getString("ruta_FileZip");
    String RUTA_BFIN = RES_FINANCIERO.getString("ruta_FileBfin");
    String RUTA_DATA = RES_FINANCIERO.getString("ruta_FileData");    
    String RUTA_FILE_TMP = RES_FINANCIERO.getString("ruta_FileTMP");

    
    
}

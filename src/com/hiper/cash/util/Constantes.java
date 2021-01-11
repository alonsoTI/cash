package com.hiper.cash.util;

/**
 * Considerar que tambien se puede usar com.hiper.cash.util.CashConstants
 * @author luitoz
 *
 */
public class Constantes {
	
	public static final String MENSAJE_ERROR_CONEXION_HIBERNATE="Error de conexion entre hibernate y base de datos";
	public static final String MENSAJE_ERROR_CONEXION_DB="Error de acceso a datos";

    public static final char F_PTOCOMA=       0x3B;

    public static final String FIELD_CASH_SECUENCIAL_ID_ENVIO = "ID_ENVIO";
    public static final String FIELD_CASH_SECUENCIAL_ID_ORDEN = "ID_ORDEN";

    //ON-OFF - Se intercambiaron los valores - jmoreno 13/11/09
    public static final String FLAG_ENABLED_SERVICIO = "0";
    public static final String FLAG_DISABLED_SERVICIO = "1";
    
    //Estado Detalle Orden
    public static final  char HQL_CASH_ESTADO_DETALLE_ORDEN_INGRESADO = '0';
    public static final  char HQL_CASH_ESTADO_DETALLE_ORDEN_ERRADO = '2';    
    public static final  char HQL_CASH_ESTADO_DETALLE_ORDEN_CANCELADO = '5';
    public static final  char HQL_CASH_ESTADO_DETALLE_ORDEN_EXTORNADO = '8';
    
    
    //Estado Aprobador Servicio
    public static final  char HQL_CASH_ESTADO_APROBADOR_SERVICIO_HABILITADO = '0';
    public static final  char HQL_CASH_ESTADO_APROBADOR_SERVICIO_DESHABILITADO = '1';
    
    //Estado Empresa
    public static final String HQL_CASH_ESTADO_EMPRESA_ACTIVO = "0";
    public static final String HQL_CASH_ESTADO_EMPRESA_INACTIVO = "1";
    
    
    //Estado Orden
    public static final char HQL_CASH_ESTADO_ORDEN_INGRESADO = '0';
    public static final char HQL_CASH_ESTADO_ORDEN_APROBADO = '1';
    public static final char HQL_CASH_ESTADO_ORDEN_PENDAUTO = '2';
    public static final char HQL_CASH_ESTADO_ORDEN_PROCESADO = '3';
    public static final char HQL_CASH_ESTADO_ORDEN_VENCIDO = '4';
    public static final char HQL_CASH_ESTADO_ORDEN_REVOCADO = '5';
    public static final char HQL_CASH_ESTADO_ORDEN_PROCESADO_PARCIAL = '6';
    public static final char HQL_CASH_ESTADO_ORDEN_ELIMINADO = '7';
    public static final char HQL_CASH_ESTADO_ORDEN_CANCELADO = '8';
    public static final char HQL_CASH_ESTADO_ORDEN_ENVIADO = 'E';

    public static final char HQL_CASH_FLAG_APROB_AUT_ENABLED = '0';
    public static final char HQL_CASH_FLAG_APROB_AUT_DISABLED = '1';

    public static final String TX_CASH_ESTADO_ORDEN_TODOS = "00";


	//manejo de id de estados en tabla tpDetalleOrden
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN_INGRESADO = "0";
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN_PROCESADO = "1";
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN_ERRADO = "2";
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN_VENCIDO = "3";
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN_COBRADO = "4";
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN_ELIMINADO = "5";
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN_COBRADOSPARCIAL = "6";
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN_PENDIENTECONF = "7";
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN_EXTORNADO = "8";
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN_ARCHIVADOS = "9";

    //jwong 21/01/2009 manejo de id de las monedas
    public static final String FIELD_CASH_TIPO_MONEDA_SOLES = "PEN";
    public static final String FIELD_CASH_TIPO_MONEDA_DOLARES = "USD";

    //jwong 22/01/2009 manejo de los tipos de entidad de empresa
    public static final String FIELD_CASH_TIPO_ENTIDAD_PERSONAL = "01";
    public static final String FIELD_CASH_TIPO_ENTIDAD_PROVEEDOR = "02";

    //jwong 22/01/2009 manejo de los tipos de entidad de empresa    
    public static final String FIELD_CASH_TIPO_PAGO_ENTIDAD = "CashTipoPagoEntidad";

    public static final String TIPO_PAGO_ENTIDAD_CHEQUEGERENCIA = "01"; //Cheques de Gerencia
    public static final String TIPO_PAGO_ENTIDAD_ABONOCTAFINAN = "CRE"; //Abono en Cuenta Financiero
    public static final String TIPO_PAGO_ENTIDAD_ABONOCTAOTRBCO = "CCI"; //Abono en Cuenta otro Banco
    public static final String TIPO_PAGO_ENTIDAD_EFECTIVO = "04"; //Efectivo	
	//TxListField
    public static final String FIELD_CASH_SERVICIO = "CashServicio";
    public static final String FIELD_CASH_TIPO_SERVICIO = "CashTipoServicio";
    public static final String FIELD_CASH_TIPO_DOCUMENTO = "CashTipoDocumento";
    public static final String FIELD_CASH_TIPO_CUENTA = "CashTipoCuenta";
    public static final String FIELD_CASH_TIPO_PAGO = "CashTipoPago";
    public static final String FIELD_CASH_TIPO_ENTIDAD = "CashTipoEntidad";
    public static final String FIELD_CASH_TIPO_MONEDA = "CashTipoMoneda";
    public static final String FIELD_CASH_TIPO_PAGO_SERVICIO = "CashTipoPagoServicio";
	//jwong 12/01/2009 nuevas constantes
    public static final String FIELD_CASH_ESTADO_ORDEN = "CashEstadoOrden";
    public static final String FIELD_CASH_TIPO_INFORMACION = "CashTipoInformacion";
    public static final String FIELD_CASH_ESTADO_DETALLE_ORDEN = "CashEstadoDetalleOrden";

    //Estado Servicio Empresa
    public static final  char TX_CASH_ESTADO_SERVICIO_EMPRESA_VIGENTE = '0';
    public static final  char TX_CASH_ESTADO_SERVICIO_EMPRESA_DESAFILIADO = '1';
    public static final  char TX_CASH_ESTADO_SERVICIO_EMPRESA_VENCIDO = '2';
    //Servicios
    public static final String TX_CASH_SERVICIO_PAGO_HABERES = "01";
    public static final String TX_CASH_SERVICIO_PAGO_CTS = "02";
    public static final String TX_CASH_SERVICIO_PAGO_SERVICIOS="03";
	//jwong 03/03/2009
    public static final String TX_CASH_SERVICIO_PAGO_PROVEEDORES = "03";
    //Tipo Servicios
    public static final String TX_CASH_SERVICIO_TODOS = "00";
    public static final String TX_CASH_TIPO_SERVICIO_PAGO = "01";
    public static final String TX_CASH_TIPO_SERVICIO_COBRO = "02";
    public static final String TX_CASH_TIPO_SERVICIO_TRANSFERENCIAS = "03";
    public static final String TX_CASH_TIPO_SERVICIO_CONSULTAS = "04";
    public static final String TX_CASH_TIPO_SERVICIO_PAGOSERV = "05"; //servicios
    public static final String TX_CASH_TIPO_SERVICIO_LETRAS = "07"; //letras

    public static final long CODE_BASE_IDORDEN = 0;

	//DIFERENCIA TIPOS TRANSFERENCIAS
    public static final String HQL_MODULO_TRANSFERENCIA = "I48";
    public static final String HQL_PROCESO_TRANSFERENCIA_CP = "E0083";
    public static final String HQL_PROCESO_TRANSFERENCIA_CT = "E0084";
    public static final String HQL_PROCESO_TRANSFERENCIA_I = "E0085";
    //TAG RESPONSE - CONS_REL_BANCO
    public static final String TAG_CARD_NUMBER = "card_number";
    public static final String TAG_CLIENT_CODE = "client_code";
    public static final String TAG_CLIENT_NAME = "client_name";
    public static final String TAG_CLIENT_TYPE = "client_type";
    public static final String TAG_DOC_TYPE = "doc_type";
    public static final String TAG_CARD_TYPE = "card_type";
    public static final String TAG_ACCOUNTS = "accounts";
    public static final String TAG_ACCOUNT = "account";
    public static final String TAG_ACCOUNT_TYPE = "account_type";
    public static final String TAG_CURRENCY_2 = "currency_2";
    public static final String TAG_ACCOUNT_CODE = "account_code";
    public static final String TAG_SIGN = "sign1";
    public static final String TAG_AMOUNT = "amount";
    //TAG REQUEST - TRANS_CTAS_PROPIAS
    //TAG RESPONSE - TRANS_CTAS_PROPIAS
    //public static final String TAG_CLIENT_NAME = "client_name";
    public static final String TAG_CLIENT_FROM = "client_from";
    public static final String TAG_CLIENT_TO = "client_to";
    public static final String TAG_ORIGINAL_AMOUNT = "original_amount";
    public static final String TAG_CHANGE_TYPE = "change_type1";
    public static final String TAG_OUT = "out";

    //**************************************************************************
    //---------------------TAG RESPONSE - CONS_CTAS_CLIENTE---------------------
    //**************************************************************************
    public static final String TAG_TOTAL_NUMBER = "total_number";
    public static final String TAG_SEND_NUMBER = "send_number";
    //public static final String TAG_CLIENT_CODE = "client_code";
    //public static final String TAG_CLIENT_NAME = "client_name";
    public static final String TAG_OFFICIAL_CREDIT = "official_credit";
    public static final String TAG_PERSON_TYPE = "person_type";
    //public static final String TAG_CHANGE_TYPE = "change_type1";
    public static final String TAG_COUNTABLE_BALANCE_SOL = "countable_balance_sol";
    public static final String TAG_AVAILABLE_BALANCE_SOL = "available_balance_sol";
    public static final String TAG_LIQUIDATE_BALANCE_SOL = "liquidate_balance_sol";
    public static final String TAG_DEFERRED_BALANCE_SOL = "deferred_balance_sol";
    public static final String TAG_COUNTABLE_BALANCE_DOL = "countable_balance_dol";
    public static final String TAG_AVAILABLE_BALANCE_DOL = "available_balance_dol";
    public static final String TAG_LIQUIDATE_BALANCE_DOL = "liquidate_balance_dol";
    public static final String TAG_DEFERRED_BALANCE_DOL = "deferred_balance_dol";
    //public static final String TAG_ACCOUNT_CODE = "account_code";
    //public static final String TAG_ACCOUNT_TYPE = "account_type";
    public static final String TAG_ACCOUNT_DESCRIPTION = "account_description";
    public static final String TAG_MANCOMUNADO = "mancomunado";
    public static final String TAG_ACCOUNT_SITUATION = "account_situation";
    public static final String TAG_CURRENCY = "currency";
    public static final String TAG_COUNTABLE_BALANCE = "countable_balance";
    public static final String TAG_AVAILABLE_BALANCE = "available_balance";
    public static final String TAG_RETAINED_BALANCE = "retained_balance";
    public static final String TAG_LOAN_NUMBER = "loan_number";
    //**************************************************************************
    
    //JWONG 09/01/2009
    //**************************************************************************
    //---------------------TAG REQUEST - TRX IBS---------------------
    //**************************************************************************
    public static final String TAG_TRX_IBS = "id_trx";
    //**************************************************************************
    
    //JWONG 09/01/2009
    //**************************************************************************
    //---------------------TAG REQUEST - CONS_REL_BANCO---------------------
    //**************************************************************************
     public static final String TAG_OUTLIN = "outlin";
    public static final String TAG_OUTACC = "outacc";
    public static final String TAG_OUTCCY = "outccy";
    public static final String TAG_OUTTYP = "outtyp";
    public static final String TAG_OUTACP = "outacp";
    public static final String TAG_OUTAMT = "outamt";
    public static final String TAG_OUTFEC = "outfec";

    public static final String TAG_REQ_CRB_CLIENT_CODE = "client_code";

    public static final String FILTRO_ACCOUNT_TYPE = "account_type=110,210";

    //Transacciones IBS
    public static final String IBS_CONS_REL_BANCO = "CONS_REL_BANCO";
    public static final String IBS_CONS_CTAS_CLIENTE = "CONS_CTAS_CLIENTE";
    public static final String IBS_TRANS_CTAS_PROPIAS = "TRANS_CTAS_PROPIAS";
    public static final String IBS_TRANS_CTAS_TERCEROS = "TRANS_CTAS_TERCEROS";
    public static final String IBS_CONS_PLAN_LETRAS = "CONS_PLAN_LETRAS";
    public static final String IBS_DET_PLAN_LETRAS = "DET_PLAN_LETRAS";
    public static final String IBS_CONS_PLAN_LETRAS_ACEP = "CONS_PLAN_LETRAS_ACEP";
    public static final String IBS_PRELIQ_CANC_LETRAS = "PRELIQ_CANC_LETRAS";
    public static final String IBS_REL_BANCO_CONS_CUOTAS="IBS_REL_CONS_CUOTAS";
    
	//jwong 16/01/2009
    public static final String IBS_CONS_MOV_CTAS = "CONS_MOV_CTAS";

    //jwong 04/02/2009 TxParameter Ids de las llaves
    public static final String FIELD_CASH_KEY1 = "K1";
    public static final String FIELD_CASH_KEY2 = "K2";
    public static final String FIELD_CASH_KEY3 = "K3";

    public static final String IBS_CODE_OK_LOGIN = "0000";

	//jwong 16/02/2009 tipo de documento dni
    public static final String FIELD_CASH_TIPO_DOCUMENTO_DNI = "01";
    //jwong 17/02/2009 nuevos campos
    public static final String TAG_ACCOUNT_NUMBER = "account_number";
    public static final String TAG_BEGIN_DATE = "begin_date";
    public static final String TAG_END_DATE = "end_date";
    public static final String TAG_QUERY_TYPE = "query_type";
    public static final String TAG_BLOCK = "block";

    public static final String IBS_CONS_MOV_HISTORICOS = "CONS_MOV_HISTORICOS";
    public static final String IBS_CONS_COD_INTERBANC = "CONS_COD_INTERBANC";
	
	//jwong 18/02/2009
    public static final String TAG_MOVEMENT_TYPE = "movement_type";
    public static final String TAG_TRANSACTION_DATE = "transaction_date";

    public static final String IBS_CONS_EST_CUENTA = "CONS_EST_CUENTA";

    public static final String TAG_TOTAL_REG = "total_reg";
    public static final String TAG_NUM_REG = "num_reg";
	//jwong 21/02/2009
    public static final String HQL_CASH_SERVICIO_RECAUDACION_BD = "05";
    //jwong 03/03/2009
    public static final String TAG_RUC = "ruc";
	//jwong 09/03/2009
    public static final String TAG_FILTRO = "filtro";

    //jwong 10/03/2009
    public static final String IBS_CONS_GRUPO_SERVICIO = "CONS_GRUPO_SERVICIO";

    //esilva 03/03/2009
    //context
    public static final String CONTEXT_CLIENTE_HOME_BANKING_WS = "clienteHomeBankingWS";
    public static final String CONTEXT_CLIENTE_CASH_WS = "cashclienteWS";
    
    //Lista Empresas de Servicio segun Grupo
    public static final String TAG_CEWS_EMPRESAS = "Empresas";
    public static final String TAG_CEWS_EMPRESA = "Empresa";
    public static final String TAG_CEWS_CODIGO = "Codigo";
    public static final String TAG_CEWS_NOMBRE = "Nombre";
    public static final String TAG_CEWS_GRUPO = "Grupo";
    public static final String TAG_CEWS_SERVICIO = "Servicio";
    public static final String TAG_CEWS_MODO = "Modo";

    //jwong 16/03/2009
    public static final String TAG_EPAD = "EPAD";
    public static final String TAG_EAPIN = "EAPIN";
    public static final String TAG_ENPIN = "ENPIN";
    public static final String TAG_ECPIN = "ECPIN";

    public static final String TAG_CEWS_NODODIFFGR = "diffgram";
    public static final String TAG_CEWS_NODODATASET = "NewDataSet";
    public static final String TAG_CEWS_NODOTABLE = "Table_GenerarC6D";
    public static final String TAG_CEWS_CODRPTATX = "CodRptaTx";
    public static final String TAG_CEWS_DESCRPTATX = "DescRptaTx";

    public static final String TAG_CEWS_PROVEEDOR = "Proveedor";
    public static final String TAG_CEWS_INTTABLAID = "intTablaID";
    public static final String TAG_CEWS_STRDESCRIPCION = "strDescripcion";
    public static final String TAG_CEWS_STRCODIGOSERVICIO = "strCodigoServicio";
    public static final String TAG_CEWS_STRRUC = "strRUC";
    public static final String TAG_CEWS_STRCODCASH = "strCodCash";
    public static final String TAG_CEWS_STRCODIGOINTERNO = "strCodigoInterno";
    
    public static final String TAG_CEWS_SERVICIOS = "Servicios";

    public static final String TAG_CEWS_BLNSECTORVISIBLE = "blnSectorVisible";
    public static final String TAG_CEWS_STRTAG = "strTag";
    public static final String TAG_CEWS_BLNDDNVISIBLE = "blnDDNVisible";

    public static final String TAG_TIPO =  "type";
    public static final String TAG_NAME_ACEPTANT =  "acep_name";
    public static final String TAG_NAME_ACEPTANT_2 =  "nombre_acep";
    public static final String TAG_NUMBER_ACEPTANT = "num_acep";
    public static final String TAG_SALDO = "saldo";
    public static final String TAG_FECHA_VENC = "fecha_venc";
    public static final String TAG_PRESTAMO = "prestamo";
    public static final String TAG_NUMERO = "numero";
    public static final String TAG_IMPORTE = "importe";
    public static final String TAG_TASA = "tasa";
    public static final String TAG_ESTADO = "estado";
    public static final String TAG_LETRA = "letra";
    public static final String TAG_NUM_DECIMALES = "decimal";
    public static final String TAG_NUM_PLANILLA="num_planilla";
    public static final String TAG_PRINCIPAL="principal";
    public static final String TAG_SIGNO_PRINCIPAL="sign_principal";
    public static final String TAG_INTERESES="interes";
    public static final String TAG_SIGNO_INTERESE="sign_interes";
    public static final String TAG_PRINCIPAL_PEND="principal_pend";
    public static final String TAG_INTERES_PEND="interes_pend";
    public static final String TAG_MORA="mora";
    public static final String TAG_PORTES="portes";
    public static final String TAG_PROTESTO="protesto";
    public static final String TAG_INTERES_REF="interes_ref";
    public static final String TAG_CLIENTE="cliente";
    public static final String TAG_OFICINA="oficina";
    public static final String TAG_PROVISIONAR="provisionar";
    public static final String TAG_PAGO_PRINCIPAL="pago_principal";
    public static final String TAG_PAGO_INTERES="pago_interes";
    public static final String TAG_SIGNO_PAG_INT="sign_pago_int";
    public static final String TAG_MONEDA="moneda";
    public static final String TAG_DESCRIPCION="descripcion";
    public static final String TAG_ACEPTANTE="aceptante";
    public static final String IBS_PRELIQ_RENOV_LETRAS="PRELIQ_RENOV_LETRAS";
    public static final String IBS_CALCULA_ITF="CALCULA_ITF";
    public static final String TAG_MONTO_LETRA_ANT="monto_letra_ant";


    //jwong 13/03/2009
    public static final String TAG_ENTITY_TYPE = "entity_type";
    public static final String TAG_ENTITY_CODE = "entity_code";
    public static final String TAG_SERVICE_CODE = "service_code";
    public static final String TAG_NUMBER_PAID = "number_paid";


    public static final String IBS_PAGO_SERVICIOS = "PAGO_SERVICIOS";

    //jwong 23/03/2009
    public static final String TAG_ATTRIBUTE_DECIMAL = "decimal";
    public static final String IBS_CONS_PAG_SERVICIOS = "CONS_PAG_SERVICIOS";

    public static final String IBS_PAGO_SERVICIOS_SEDAPAL = "PAGO_SERVICIOS_SEDAPAL";
    public static final String TAG_INTIDPADRE = "intIdPadre";

    public static final String TAG_ID_LOCAL = "id_local";
    public static final String IBS_CONS_PAG_SERVICIOS_CLARO = "CONS_PAG_SERVICIOS_CLARO";
    public static final String IBS_PAGO_SERVICIOS_CLARO = "PAGO_SERVICIOS_CLARO";

    //Querys - esilva
    public static final String QRY_GET_ENTIDAD_EMPRESA = "getEntidadEmpresa";
    public static final String TAG_SECTORES = "Sectores";

    //jwong 04/04/2009
    public static final String FIELD_CASH_SIMBOLO_MONEDA_SOLES = "S/.";
    public static final String FIELD_CASH_SIMBOLO_MONEDA_DOLARES = "US$";

    //jwong 10/04/2009
    public static final int NUMERO_DECIMALES_SALIDA = 2;

    //jwong 24/04/2009
    public static final  char TX_CASH_SERVICIO_ESTADO_FLAG_INFORMACION_NOHABIL = '0';
    public static final  char TX_CASH_SERVICIO_ESTADO_FLAG_INFORMACION_HABIL = '1';
    
    //jwong 09/05/2009
    public static final String HQL_CASH_ESTADO_SERVICIOXEMPRESA_ACTIVO = "0";
    public static final String HQL_CASH_ESTADO_SERVICIOXEMPRESA_INACTIVO = "1";

    //jmoreno 31-07-09
    public static final String TIPO_PAG_COMPROBANTES = "C";
    public static final String TIPO_PAG_APROBACIONES = "A";
    public static final String TIPO_PAG_SIGUIENTE = "S";
    public static final String TIPO_PAG_ANTERIOR = "A";
    public static final String TIPO_PAG_PRIMERO = "P";
    public static final String TIPO_PAG_ULTIMO = "U";

    //jwong 10/08/2009
    public static final String FIELD_CASH_FORMA_PAGO_EFECTIVO = "EFE";
    public static final String FIELD_CASH_DESCR_MONEDA_SOLES = "Soles";
    public static final String FIELD_CASH_DESCR_MONEDA_DOLARES = "Dólares";
    //jwong 11/08/2009 nueva transaccion IBS
    public static final String IBS_CONS_CTAS_ASOC_CLIENTE = "CONS_CTAS_ASOC_CLIENTE";
    //jmoreno 13-08-09
    public static final String TAG_SECUENCIAL = "secuencial";
    public static final String TIPO_PAG_CREDITO_CUENTA = "CRE";
    public static final String TX_CASH_SERVICIO_ADMINISTRACION = "00";
    public static final String TX_CASH_SERVICIO_DEBITO_AUTOMATICO = "06";
    public static final String TX_CASH_SERVICIO_RECAUDACION_CON_BD = "05";
    public static final String TX_CASH_SERVICIO_CONS_SALDOSPROMEDIOS = "08";
    public static final String TX_CASH_SERVICIO_CONS_SALDOSMOVIMIENTOS = "09";
    public static final String TX_CASH_SERVICIO_CONS_MOVHISTORICOS = "10";
    public static final String TX_CASH_SERVICIO_TRANS_CTAPROPIA = "11";
    public static final String TX_CASH_SERVICIO_TRANS_CTATERCERO = "12";
    public static final String TX_CASH_SERVICIO_TRANS_INTERBANCARIA = "15";
    public static final String TX_CASH_SERVICIO_CONS_LETRA = "19";
    public static final String TX_CASH_SERVICIO_PRELIQ_LETRA = "20";
    public static final String TX_CASH_SERVICIO_CANCELACION_LETRA = "21";
    public static final String TX_CASH_SERVICIO_CONS_CODIGOINTERBANCARIO = "26";
    public static final String TX_CASH_SERVICIO_CONS_RELACIONESBANCO = "27";
    public static final String TX_CASH_SERVICIO_APROBACIONES = "29";
    public static final String TX_CASH_SERVICIO_COMPROBANTES_ORDEN = "30";    
    public static final String TX_CASH_SERVICIO_ORDENES_COBRO = "13";
    public static final String HQL_CASH_ESTADO_ORDEN_PROCESADO_PARCIALMENTE = "6";
    public static final String HQL_CASH_ESTADO_ORDEN_RECHAZADO = "8";
    public static final String HQL_CASH_ESTADO_ORDEN_ERRADO = "9";
    public static final String HQL_CASH_ESTADO_ORDEN_CERRADO = "A";//10: Para no cambiar los char por String
    public static final String TAG_NUMERO_MOVIMIENTO ="num_mov";
    public static final String CODIGO_RPTA_IBS_DESCONOCIDO="Error Desconocido";
    //jmoreno 29-09-09
    public static final char HQL_CASH_ESTADO_CUENTAXEMPRESA_ACTIVO = '0';
    public static final String RUC_EMPRESA_OPERACIONES_CASH = "00000000000";
    public static final String WEB_SERVICE_CASH = "WSCASH";
    public static final String WEB_SERVICE_BANCO = "WSBANCO";
    
    //para la exportacion de formatos de salida    
    public static final String CAMPO_DBU_MONEDA = "dDBuMoneda";
    public static final String CAMPO_DBU_REFERENCIA_SOBRE = "cDBuReferenciaSobre";
    public static final String CAMPO_DBU_PAIS = "cDBuPais";
    public static final String CAMPO_DBU_PAIS_ADICIONAL = "cDBuAdicionalPais";    
    public static final String CAMPO_DBU_NOMBREBANCO = "cDBuNombreBanco";  
    public static final String CAMPO_BA_NOMBREBANCO = "tp.nombreBanco";
    public static final String CAMPO_DO_ESTADO = "tp.cDOEstado";
    public static final String CAMPO_DBU_ESTADO = "cDBuEstado";
    public static final String CAMPO_DBU_VALORPROCESADO = "cDBuValorProcesado";     
    public static final String CAMPO_DBU_VALORENVIADO = "cDBuValorEnviado";
    public static final String CAMPO_DBU_FECHAPROCESO = "fDBuFechaProceso";   
    public static final String CAMPO_DBU_COMPROBANTECOBRO = "nDBuComprobCobro";
    public static final String CAMPO_DBU_NUMEROSUNAT = "nDBuNumeroSunat";
    public static final String CAMPO_DBU_NOMEMPRESA = "nDBuNomEmpresa";
    public static final String CAMPO_DBU_VALORNETO = "nDBuValorNeto";
    public static final String CAMPO_DBU_VALORNETO_MOVIMIENTO = "nDBuValorNetoMovimiento";
    public static final String CAMPO_DBU_VALOR_PROCESADO_MOVIMIENTO = "cDBuValorProcesadoMovimiento";
    public static final String CAMPO_DBU_FECHA_MOVIMIENTO = "fDBuFechaMovimiento";
    public static final String CAMPO_DO_ESTADO_MOVIMIENTO = "tp.cDOEstadoMovimiento";
    public static final String CAMPO_DBU_ESTADO_MOVIMIENTO = "cDBuEstadoMovimiento";
    public static final String CAMPO_DBU_FORMA_COBRO_RECAUDACION = "nDBuFormaCobroRec";
    public static final String CAMPO_DBU_PROCESO_OK = "dDBuEstadoProcesoOk";
    public static final String CAMPO_DBU_NUMDOC_REFERENCIA = "dDBuNumDocumentoReferencia";
    public static final String CAMPO_DO_NUMDOC = "tp.ndodocumento";
    public static final String CAMPO_DBU_NUMDOC = "nDBuDocumento";
    public static final String CAMPO_DO_REFERENCIA = "tp.dDOReferencia";
    public static final String CAMPO_DBU_REFERENCIA_ADICIONAL = "dDBuReferenciaAdicional2";
    public static final String CAMPO_DBU_REFERENCIA = "dDBuReferencia";
    public static final String CAMPO_PROCESO_OK = "'PROCESO OK'";    
    public static final String CAMPO_DBU_BANCOFINANCIERO = "dDBuDescBcoFinanciero";
    public static final String CAMPO_BANCOFINANCIERO = "'BCO PICHINCHA'";
    public static final String CAMPO_BLANCO = "''";
    public static final String CAMPO_PAIS = "'PE'";
    public static final String CAMPO_FORMACOBRO_REC = "'REC'";
    
    //id de campos
    public static final String ID_CAMPO_PROCESO_OK = "CampoProcesoOk";
    public static final String ID_CAMPO_MONEDA = "codMoneda";    
    public static final String ID_CAMPO_REFERENCIAORDEN = "referenciaOrden";
    public static final String ID_CAMPO_PAIS = "pais";
    public static final String ID_CAMPO_ADICIONALPAIS = "adicionalPais";
    public static final String ID_CAMPO_NOMBREBANCO = "nombreBanco";    
    public static final String ID_CAMPO_FORMACOBRO_REC = "formacobroRec";
    public static final String ID_CAMPO_ESTADO = "estadoProceso";
    public static final String ID_CAMPO_ESTADO_MOVIMIENTO = "estadoMovimiento";
    public static final String ID_CAMPO_BANCOFINANCIERO = "bancoFinanciero";
    public static final String ID_CAMPO_NUMDOCUMENTO_REFERENCIA = "numDocReferencia";
    
    public static final int MAXIMA_CANTIDAS_FILAS_EXCEL=5000;
    public static final String NOMBRE_TABLA_ORDEN= "taordenrep";
    public static final String NOMBRE_TABLA_DETALLE_ORDEN_TRX= "tpDetalleOrdenTrx";
    
    //JNDI DE LAS BASES DEL CASH
    public static final String JNDI_DB_SEGURIDAD="java:/conCashSeguridad";    
    public static final String JNDI_DB_TRANSACCIONAL="java:/conCashCenter";
    public static final String JNDI_DB_CONSULTAS="java:/conCashConsultas";
    public static final String JNDI_DB_EASY_CASHMANAGEMENT="java:/conEasyCashManagement";
    public static final String JNDI_DB_EASY_SEGURIDAD="java:/conEasyCashSeguridad";
    
    //CONSTANTES PARA LA FUNCIONALIDAD DE MIGRACION-LOGIN
    public static final String CAMPO_LISTA_VALIDA_MIGRACION_NOMBRE= "CASHVALIDAMIGRACION";
    public static final String CAMPO_LISTA_VALIDA_MIGRACION_CODE= "01";
    public static final String CAMPO_LISTA_VALIDA_MIGRACION_VALOR= "SI";
    public static final String CAMPO_LISTA_CASHURLEASYCASH= "CASHURLEASYCASH";
    
    
    public static final String ID_SERVICIO_LOGIN= "LOGIN";
    public static final String ID_SERVICIO_INPUT= "I";
    public static final String ID_SERVICIO_OUTPUT= "O";
    
    
    
    
}
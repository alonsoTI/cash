/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.hiper.cash.util;

import com.hiper.cash.procesoBatch.dbaccess.CBOrden;
import com.hiper.cash.procesoBatch.dbaccess.CBDetalleOrden;
import com.hiper.cash.procesoBatch.entity.DetalleOrden;
import com.hiper.cash.procesoBatch.entity.Orden;
import com.hiper.cash.procesoBatch.utiles.HCDefine;
//import com.hiper.cash.procesoBatch.base.HCProcesoBatch;
import com.hiper.cash.procesoBatch.utiles.HCFuncion;
import com.hiper.cash.clienteWS.CashClientWS;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.ResourceBundle;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
/**
 *
 * @author grogut
 */


public class BatchRevocados {


    /**
     * Array donde se almacenar las Ordenes que cumplen con el rango de tiempo
     * y estado, solo acepta objetos de tipo Orden
     */
    ArrayList<Orden> listaOrdenes = new ArrayList<Orden>();
    /**
     * Array que contendra los detalles de las ordenes, solo acepta objetos
     * de tipo DetalleOrden
     */
    ArrayList<DetalleOrden> listaDetOrden = new ArrayList<DetalleOrden>();
    /**
     * Instancia de la clase HCWSClient
     */
    public CashClientWS cliente;
    /**
     * Nombre del servicio de la aplicaión
     */
    private String servicioNombre;
    /**
     * Numero de puertpo del servicio;
     */
    private String servicioPuerto;
    /**
     * ruta de conexion
     */
    private String servicioLocation;
    /**
     * Nombre de metodo a ejecutarse
     */
    private String servicioOperacion;
    /**
     * Nombre del target
     */
    private String servicioTargetNameSpace;
    /**
     * Elemento de la cabecera
     */
    private String servicioEleCab;
    /**
     * Version del Soap
     */
    private String soapVersion;

    /**
     * Define el valor del tag de configuracion reintentar Si o No
     */
    private String reintentar;

    /**
     * Constructor Instancias de la Propias Clase
     */
    public BatchRevocados(){

    }


    /***************************************************************************
     * Nombre ejecutarBatch<br><br>
     *
     * Descripción: Inicia durmiendo el proceso durante un rango de tiempo     <br>
     * (ya establecido), pasado el perido busca las ordenes en la base de datos<br>
     * para hacer posterior busqueda de sus detalles<br>
     *
     * @return Confirmacion de Exito(True o False)
     ***************************************************************************/
  /*
    @SuppressWarnings("empty-statement")
    public boolean  ejecutarBatch(){

        Calendar fecha = new GregorianCalendar();
        ////System.out.println("HCProcesoBatch.ejecutarBatch "+fecha.getTime());
        fecha = null;
        boolean retorno = true;

        if(!buscarOrdenes()){
            retorno= false;
        }
        return retorno;
    }
    */

    public boolean ejecutarRevocados(String idOrden, CashClientWS clienteLoginWS ){

        ResourceBundle res = ResourceBundle.getBundle("financiero");

        cliente = clienteLoginWS;

         // cliente = new CashClientWS(res.getString("APP_Servicio_Nombre"), servicioPuerto, servicioLocation, servicioOperacion, servicioOperacion, servicioTargetNameSpace, servicioPuerto, idOrden, servicioEleCab, idOrden)
      /*  cliente = new HCWSClient(res.getString("APP_Servicio_Nombre"),
                                            res.getString("APP_Servicio_NombrePuerto"),
                                            res.getString("APP_LOG_BCO_Servicio_Localizacion"),
                                            res.getString("APP_Servicio_Operacion_Nombre"),
                                            res.getString("APP_Servicio_TargetNameSpace"),
                                            res.getString("APP_Servicio_Cabecera")
                                           );
        */

        soapVersion = res.getString("APP_Servicio_SoapVersion");

        Calendar fecha = new GregorianCalendar();
        ////System.out.println("HCProcesoBatch.ejecutarBatch "+fecha.getTime());
        fecha = null;
        boolean retorno = true;

        if(!buscarOrden(idOrden)){
            retorno= false;
        }
        return retorno;
    }

    /***************************************************************************
     * Nombre buscaOrdenes<br><br>
     *
     * Descripción: Busca las ordenes las mismas que almacena en un ArrayList<br>
     *
     * @return Confirmacion de Exito(True o False)
     **************************************************************************/
    //Modificado por grov 28/05/2010
    private boolean buscarOrden(String idOrden){
        CBOrden cbOrden = new CBOrden();
        boolean retorno = true;
        float[]montos;
        listaOrdenes = cbOrden.buscarOrden(new String []{HCDefine.ORDEN_ESTADO_VENCIDO}, idOrden  );

        if(listaOrdenes != null && listaOrdenes.size() > 0){
            for (Iterator<Orden> it = listaOrdenes.iterator(); it.hasNext();) {
                Orden orden = it.next();
                ////System.out.println("procesando Orden con Id "+orden.getIdOrden() +                                  " en Revocados");
                ArrayList<DetalleOrden> detOrden = new ArrayList<DetalleOrden>();
                if(buscarDetalleOrdenes(orden, detOrden)){
//                    if(!detOrden.isEmpty()){
                    int cError = 0;
                    int contMontosRevocados = 0;
                    montos = obtenerMonto(detOrden, orden);

                    for(int i=0; i < montos.length; i++){
                        double monto = 0f;
                        float montoRevocado = 0f;
                        if(HCDefine.ORDEN_ESTADO_MONTO_CARGADO.equals(getEstadoMontoMoneda(orden, i))){
                            monto = getMonto(orden, i) - montos[i];
                            //cgonzales formateamos el monto a solo dos digitos y con punto
                            //NumberFormat nf = NumberFormat.getInstance(Locale.US);
                            DecimalFormat df = new DecimalFormat("0.00");
                            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                            dfs.setDecimalSeparator('.');
                            df.setDecimalFormatSymbols(dfs);
                            df.setMinimumFractionDigits(2);
                            montoRevocado = Float.parseFloat(df.format(monto));
                        }
                        if(montoRevocado > 0){
                            StringBuilder ibsCode = new StringBuilder();
                            //if(!procesarMensaje(orden, montoRevocado, getMoneda(i),i)){
                            if(!procesarMensaje(orden, montoRevocado, getMoneda(i),i, ibsCode)){
                                cError++;
                            }else{
                                cbOrden.actualizarEstadoMonto(orden.getIdOrden(),
                                                              getCampoEstadoMoneda(i),
                                                              HCDefine.ORDEN_ESTADO_MONTO_REVOCADO);
                                contMontosRevocados++;
                            }
                            //cgonzales manejo para guardar el codigo de IBS
                            String campo = getNombreCampo(i);
                            if(!"".equals(campo)){
                                String sentence = campo +
                                                  " = '" +
                                                  ibsCode.toString() +
                                                  "' WHERE cOrIdOrden = " +
                                                  orden.getIdOrden();
                                if(!cbOrden.saveIbsCode(sentence)){
                                    ////System.out.println("Error al Guardar codigoIBS de Campo "+                                                       campo +                                                       " de Id Orden " +orden.getIdOrden());
                                }
                            }
                        }
                    }
                    if(cError > 0){
                        System.err.println("No se completo revocación de Orden "
                                           + orden.getIdOrden());
                        retorno = false;
                    }else if(contMontosRevocados > 0){
                        cbOrden.actualizarEstado(orden.getIdOrden(),
                                                 HCDefine.ORDEN_ESTADO_REVOCADO);
                    //cgonzales validacion si no exite monto que revocar
                    }
                    else if(contMontosRevocados == 0){
                        cbOrden.actualizarEstado(orden.getIdOrden(),
                                                 HCDefine.ORDEN_ESTADO_CERRADO);
                    }
//                    String [] detEstado = {HCDefine.DETORDEN_ESTADO_PROCESADO};
//
//                    cbOrden.siRegistrosPendientes(detEstado,
//                                                  orden.getIdOrden(), HCDefine.ORDEN_ESTADO_CERRADO);
                }else{
                    ////System.out.println("Orden " +orden.getIdOrden()+" No tiene monto para revocar");
                }
            }
        }else{
            System.err.println("No existen ordenes con estado vencido dentro " +
                               "del rango de hora y tiempo... ");
        }
        return retorno;
    }

   /****************************************************************************
    * Nombre buscarDetOrdenTransferencia<br><br>
    *
    * Descripción: Busca los detalles de la cbOrden especificada<br>
    *
    * @param orden lista de ordenes encontradas<br>
    *
    * @param detalleOrdenes Array con los Detalles de la Orden
    *
    * @return Confirmacion de Exito(True o False)
    ***************************************************************************/
    private boolean  buscarDetalleOrdenes(Orden orden, ArrayList<DetalleOrden> detalleOrdenes){

        boolean retorno = true;

        CBDetalleOrden detOden = new CBDetalleOrden();

        int idOrden = orden.getIdOrden();

        if(!detOden.buscarDetalleOrdenes(idOrden,
                                         new String[]{HCDefine.DETORDEN_ESTADO_PROCESADO},
                                         detalleOrdenes)){

          ////System.out.println("Error:"+ getClass().getName()+".bucarDetalleOrdenes:");
          retorno = false;

        }
        return retorno;
    }



    /***************************************************************************
     * Nombre procesarMensaje<br><br>
     *
     * Descripción: Genera un xml, el cual es enviado al Cliente Web Service<br>
     * devolviendo a su ves un xml de respuesta del mismo que se toma el    <br>
     * response_code. Depende de esto para actualizar el estado a           <br>
     * errado (2) o procesado(1)<br>
     *
     * @param detalleOrden detalle de cbOrden a procesar
     ***************************************************************************/
     private boolean procesarMensaje(Orden orden,
                                     float monto,
                                     String moneda,
                                     int i,
                                     StringBuilder ibsCode){
        boolean retorno = true;
        //Modificado por Grov 01/06/2010
        /*
         String xmlRequest = generarMensajeXML(orden,monto, moneda, i);
        ////System.out.println("SOAP Version:  " + soapVersion);
        ////System.out.println("XML Request: " + xmlRequest);
        String xmlResult = cliente.ejecutarMetodoWS(xmlRequest, soapVersion);
        */ 
        
        String xmlRequest = generarTramaXML(orden,monto, moneda, i);
        ////System.out.println("SOAP Version:  " + soapVersion);
        ////System.out.println("XML Request: " + xmlRequest);
        String xmlResult =  cliente.ProcesarMensaje(xmlRequest, Constantes.WEB_SERVICE_CASH);
        //Fin Modificado por grov 01/06/2010


        if(xmlResult != null && !"".equals(xmlResult)){
            String cadena = xmlResult.substring(38, xmlResult.length()); //quitamos <?xml version="1.0" encoding="UTF-8"?>
            if (!HCDefine.RESPONSE_CODE_OK.equals(obtenerCodigoRespuesta(cadena,ibsCode))) {//Si­ OK = 00
                System.err.println(getClass().getSimpleName().toString() +
                                   ".procesarmensaje():"+
                                   " Error de Abono en Cuenta Moneda "+ moneda +"...!");
                retorno = false;
            }else{
                ////System.out.println("Abono en Cuenta Exitoso: " + monto+ " " + moneda);
            }
        }else{
            System.err.println("Respuesta nula o vacia del WSBanco...!");
            retorno = false;
        }
        return retorno;

    }

    /***************************************************************************
     * Nombre generarMensajeXML<br><br>
     *
     * Descripcion: Genera xml de requerimiento<br>
     *
     * @return String con el xml generado
     **************************************************************************/
    private String generarMensajeXML(Orden orden,
                                     float monto,
                                     String moneda,
                                     int i){
        String mensaje = "";
        String sMonto = ""+(long)(monto*100);
        if(sMonto.length() < 3){
            sMonto = "000" + sMonto;
        }
        mensaje = "<data>" +
                  "<![CDATA[<request>" +
                  "<id_trx>" +
                  "ABONO_CTA" +
                  "</id_trx>" +
                  "<bank_motive>" +
                  orden.getSeMotivo() +
                  "</bank_motive>" +
                  //el manejo lo realiza el cash ws
                  //"<transaction_type_id>" +
                  //"D"+
                  //"</transaction_type_id>" +
                  "<account_type>" +
                  orden.getTipoCuenta() +
                  "</account_type>" +
                  //de card a acocunt_number
                  "<account_number>" +
                  orden.getNumeroCuenta() +
                  "</account_number>" +
                  "<currency_code>" +
                  moneda +
                  "</currency_code>" +
                  "<amount>" +
                  sMonto +
                  "</amount>" +
                  //se agrego referencia id orden
                  "<reference_mv>" +
                  "ref: "+ orden.getIdOrden() +
                  "</reference_mv>" +
                  "<service_id>" +
                  orden.getSerIdServicio() +
                  "</service_id>" +
                   //se envia la fecha del sistema
                   "<date_value>" +
                  HCFuncion.get_FechaActual_AAAAMMDD() +
                  "</date_value>" +
                  "<doc_number>" +
                  //cgonzales 01/04/2009 el WSB requiere un correlativo
                  //orden.getSEmIdEmpresa() +
                  orden.getIdOrden() + i +
                  "</doc_number>" +
                  //cgonzales 02/04/2009
                  //implmentacion de tag ruc
                  "<ruc>" +
                  orden.getSEmIdEmpresa()+
                  "</ruc>" +
                  //CGamarra 07-04-2009 Es necesario enviar el n?ºmero de la orden
                  "<id_orden>"+
                  orden.getIdOrden()+
                  "</id_orden>"+
                  "</request>]]>" +
                  "</data>";

        return mensaje;
    }

    //Modificado por Grov 01/06/2010
    private String generarTramaXML(Orden orden,
                                     float monto,
                                     String moneda,
                                     int i)
    {
        String mensaje = "";
        String sMonto = ""+(long)(monto*100);
        if(sMonto.length() < 3){
            sMonto = "000" + sMonto;
        }
        mensaje = "<data>" +
                  "<request>" +
                  "<id_trx>" +
                  "ABONO_CTA" +
                  "</id_trx>" +
                  "<bank_motive>" +
                  orden.getSeMotivo() +
                  "</bank_motive>" +
                  //el manejo lo realiza el cash ws
                  //"<transaction_type_id>" +
                  //"D"+
                  //"</transaction_type_id>" +
                  "<account_type>" +
                  orden.getTipoCuenta() +
                  "</account_type>" +
                  //de card a acocunt_number
                  "<account_number>" +
                  orden.getNumeroCuenta() +
                  "</account_number>" +
                  "<currency_code>" +
                  moneda +
                  "</currency_code>" +
                  "<amount>" +
                  sMonto +
                  "</amount>" +
                  //se agrego referencia id orden
                  "<reference_mv>" +
                  "ref: "+ orden.getIdOrden() +
                  "</reference_mv>" +
                  "<service_id>" +
                  orden.getSerIdServicio() +
                  "</service_id>" +
                   //se envia la fecha del sistema
                   "<date_value>" +
                  HCFuncion.get_FechaActual_AAAAMMDD() +
                  "</date_value>" +
                  "<doc_number>" +
                  //cgonzales 01/04/2009 el WSB requiere un correlativo
                  //orden.getSEmIdEmpresa() +
                  orden.getIdOrden() + i +
                  "</doc_number>" +
                  //cgonzales 02/04/2009
                  //implmentacion de tag ruc
                  "<ruc>" +
                  orden.getSEmIdEmpresa()+
                  "</ruc>" +
                  //CGamarra 07-04-2009 Es necesario enviar el n?ºmero de la orden
                  "<id_orden>"+
                  orden.getIdOrden()+
                  "</id_orden>"+
                  "</request>" +
                  "</data>";

        return mensaje;
    }
    //FIN MODIFICADO POR GROV 01/06/2010
    /***************************************************************************
     * Nombre obtenerCodigoRespuesta<br><br>
     *
     * Descripción: Hace un parse del xml de repuesta, extrayendo el codigo<br>
     * de retorno solamente<br>
     *
     * @param xml String con la respuesta
     *
     * @return String con el codigo de respuesta
     **************************************************************************/
    private String obtenerCodigoRespuesta(String xml){
        String codigo = "";
        try {
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(new InputSource(new StringReader(xml)));
            builder = null;
            Element elemRaiz = doc.getRootElement();
            doc = null;
            Element retorno =  elemRaiz.getChild("return");
            if(retorno.equals(null)){
                return null;
            }
            Element nodoResponse =  retorno.getChild("RespuestaCASH");
            if(nodoResponse == null){
                return null;
            }
            retorno = null;
            Element head = nodoResponse.getChild("Header");
            if(head ==null){
                return null;
            }
            nodoResponse = null;
            codigo = head.getChildText("CodigoRetorno");
            if(codigo.equals(null)){
                return null;
            }
            head = null;

        } catch (JDOMException ex) {
            ////System.out.println("HCProcesoBatch.obtenerCodigoRespuesta():"+"Error de "+ ex.getMessage());
            return null;
        } catch (IOException ex) {
            ////System.out.println("HCProcesoBatch.obtenerCodigoRespuesta():"+"Error de "+ ex.getMessage());
            return null;
        }
        return codigo;
    }

    /***************************************************************************
     * Nombre obtenerCodigoRespuesta<br><br>
     *
     * Descripción: Hace un parse del xml de repuesta, extrayendo el codigo<br>
     * de retorno solamente<br>
     *
     * @param xml String con la respuesta
     *
     * @return String con el codigo de respuesta
     **************************************************************************/
    private String obtenerCodigoRespuesta(String xml, StringBuilder ibsCode){
        String codigo = "";
        try {
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(new InputSource(new StringReader(xml)));
            builder = null;
            Element elemRaiz = doc.getRootElement();
            doc = null;
            Element retorno =  elemRaiz.getChild("return");
            if(retorno.equals(null)){
                return null;
            }
            Element nodoResponse =  retorno.getChild("RespuestaCASH");
            if(nodoResponse == null){
                return null;
            }
            retorno = null;
            Element head = nodoResponse.getChild("Header");
            if(head ==null){
                return null;
            }
            nodoResponse = null;
            codigo = head.getChildText("CodigoRetorno");
            if(codigo == null){
                return null;
            }
            //cgonzales almacena el codigo de IBS en el SB
            if(ibsCode != null){
                ibsCode.append(head.getChildText("CodigoRetornoIBS"));
            }
            head = null;

        } catch (JDOMException ex) {
            ////System.out.println("HCProcesoBatch.obtenerCodigoRespuesta():"+ "Error de "+ ex.getMessage());
            return null;
        } catch (IOException ex) {
            ////System.out.println("HCProcesoBatch.obtenerCodigoRespuesta():"+"Error de "+ ex.getMessage());
            return null;
        }
        return codigo;
    }

   /***************************************************************************
     * Nombre inicializa<br><br>
     *
     * Descripcion: Obtiene los parametros contenidos en el nodo xml recibido<br>
     * Intancia un objeto ClienteWS con los parametros de configuracion obtenidos.
     *
     * @param root elemento con todo el archivo xml <br>
     *
     * @return Confirmacion de Exito
     **************************************************************************/
   /*
    public boolean inicializa(Element root, String top, String reintentar,int modoProceso) {
        boolean retorno = true;
        try {

            servicioNombre  = root.getChild(HCDefine.APLICACION_SERVICIO_NOMBRE).getText().trim();
            servicioPuerto      = root.getChild(HCDefine.APLICACION_SERVICIO_NOMBRE_PUERTO).getText().trim();
            servicioLocation = root.getChild(HCDefine.APLICACION_SERVICIO_LOCALIZACION).getText().trim();
            servicioOperacion    = root.getChild(HCDefine.APLICACION_SERVICIO_OPERACION_NOMBRE).getText().trim();
            servicioTargetNameSpace    = root.getChild(HCDefine.APLICACION_SERVICIO_TARGET_NAME_SPACE).getText().trim();
            //obtenemos el elemento extra de la cabecera del xml cgonzales 11/12/2008 proy Bfinanciero
            servicioEleCab = root.getChild(HCDefine.APLICACION_SERVICIO_ELECABECERA).getText().trim();
            soapVersion = root.getChild(HCDefine.APLICACION_SOAPVERSION).getText().trim();
            root = null;
            cliente = new HCWSClient(servicioNombre, servicioPuerto, servicioLocation, servicioOperacion,servicioTargetNameSpace,servicioEleCab);
            //CGonzales inicializacion de variable con el valor del parametro de reintento
            this.reintentar = reintentar;

        } catch (Exception e) {
            ////System.out.println("HCProcesoBatch.inicializa():"+
                               "Error: "+
                               e.toString());
            retorno = false;
        }

        return retorno;
    }
    */
   /****************************************************************************
    *
    * Descripción: Obtiene el monto de la Orden respetivo por valor segun el indice<br>
    * (0 = soles, 1 = dolares, 2 = euros)
    *
    * @param orden Actual objeto orden procesado
    *
    * @param indice contiene el valor para definir la valor
    *
    * @return monto respectivo
    ***************************************************************************/
   private float getMonto (Orden orden,int indice){
        float monto = 0;
        if(indice == 0){
            monto = orden.getMontoSoles();
        } else if(indice == 1){
            monto = orden.getMontoDolares();
        } else if(indice == 2){
            monto = orden.getMontoEuros();
        }
        return monto;
   }

   /****************************************************************************
    *
    * Descripción: Almacena en un array de float los montos segun la valor <br>
    * del monto del detalle ubicandolos en el orden de SOLES,DOLARES,EUROS<br>
    *
    * @param detOrden Array con los detalles de las ordenes
    *
    * @return array con los montos por valor
    ***************************************************************************/
   private float[] obtenerMonto(ArrayList <DetalleOrden> detOrden, Orden orden){
        float[]montos = {0f,0f,0f};
        for (Iterator<DetalleOrden> it = detOrden.iterator(); it.hasNext();) {
                 DetalleOrden detalleOrden = it.next();

                 if(HCDefine.TIPO_MONEDA_PEN.equals(detalleOrden.getMoneda())&&
                    HCDefine.ORDEN_ESTADO_MONTO_CARGADO.equals(orden.getEstadoMontoSoles())){
                    montos [0] += detalleOrden.getMonto();

                 }
                 if(HCDefine.TIPO_MONEDA_USD.equals(detalleOrden.getMoneda())&&
                    HCDefine.ORDEN_ESTADO_MONTO_CARGADO.equals(orden.getEstadoMontoDolares())){
                    montos [1] += detalleOrden.getMonto();

                 }
                 if(HCDefine.TIPO_MONEDA_EUR.equals(detalleOrden.getMoneda())&&
                    HCDefine.ORDEN_ESTADO_MONTO_CARGADO.equals(orden.getEstadoMontoEuros())){
                    montos [2] += detalleOrden.getMonto();

                 }
        }
        return montos;
    }


  /*****************************************************************************
   * Descripción: Obtiene nombre del campo segun el indice del objeto
   *
   * @param i indice con el que se desiganara la nombre del campo
   *
   * @return nombre del campoen la tabla taOrden
   ****************************************************************************/
   private String getCampoEstadoMoneda (int iMoneda){
       String campo="";
          if(iMoneda == 0){
            campo = HCDefine.TBLORDEN_ESTADO_MONTOSOLES;
          }
          if(iMoneda == 1){
            campo = HCDefine.TBLORDEN_ESTADO_MONTODOLARES;
          }
          if(iMoneda == 2){
            campo = HCDefine.TBLORDEN_ESTADO_MONTOEUROS;
          }
        return campo;
    }

  /*****************************************************************************
   * Descripción: Obtiene el valor del campo estado del monto segun el indice<br>
   * del objeto
   *
   * @param i indice con el que se desiganara la valor del monto
   *
   * @return valor estado de monto por moneda
   ****************************************************************************/
   private String getEstadoMontoMoneda (Orden orden,int i){
       String valor="";
          if(i == 0){
            valor = orden.getEstadoMontoSoles();
          }
          if(i == 1){
            valor = orden.getEstadoMontoDolares();
          }
          if(i == 2){
            valor = orden.getEstadoMontoEuros();
          }
        return valor;
    }

   /*****************************************************************************
   * Descripción: Obtiene el tipo de moneda segun el indice del objeto
   *
   * @param iMoneda indice con el que se desiganara la moneda del monto
   *
   * @return abreviatura de la Moneda
   ****************************************************************************/
   private String getMoneda (int iMoneda){
       String moneda="";
          if(iMoneda == 0){
            moneda = HCDefine.TIPO_MONEDA_PEN;
          }
          if(iMoneda == 1){
            moneda = HCDefine.TIPO_MONEDA_USD;
          }
          if(iMoneda == 2){
            moneda = HCDefine.TIPO_MONEDA_EUR;
          }
        return moneda;
    }

   /****************************************************************************
    *
    * Definicion: Devuelve el valor del campo que sera actualizado en BD
    *
    * @param i Indice que indica la moneda actual em proceso
    *
    * @return Nombre del campo que se actualizara en bd
    *
    ***************************************************************************/
   private String getNombreCampo(int i){
       String retorno = "";
       if(i == 0){
            retorno = "cOrCodRptaIBSSoles";
       }else if (i == 1){
            retorno = "cOrCodRptaIBSDolares";
       }else if (i == 2){
            retorno = "cOrCodRptaIBSEuros";
       }
       return retorno;
   }

}

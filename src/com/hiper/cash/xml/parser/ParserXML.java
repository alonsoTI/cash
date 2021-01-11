/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hiper.cash.xml.parser;

import com.hiper.cash.clienteWS.CashClientWS;
import com.hiper.cash.clienteWS.GenRequestXML;
import com.hiper.cash.dao.TmEmpresaDao;
import com.hiper.cash.dao.TmFuncionarioDao;
import com.hiper.cash.dao.hibernate.TmEmpresaDaoHibernate;
import com.hiper.cash.dao.hibernate.TmFuncionarioDaoHibernate;
import com.hiper.cash.domain.TmFuncionario;
import com.hiper.cash.util.Constantes;
import com.hiper.cash.util.Fecha;
import com.hiper.cash.util.Util;
import com.hiper.cash.xml.bean.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


import org.apache.log4j.Logger;

/**
 *
 * @author jwong
 */
public class ParserXML {

    static Logger logger = Logger.getLogger(ParserXML.class);
   
    public static Document parse(String xml, boolean validation) throws JDOMException, IOException {
        SAXBuilder saxbuilder = new SAXBuilder(validation);
        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(xml.getBytes());
        return saxbuilder.build(bytearrayinputstream);
    }

    /**
     * parsearRespuesta, metodo generico que parsea el xml de respuesta del webservice
     * @param xml, xml a parsear(respuesta del webservice)
     * @param isLogin, valida si es respuesta a la transaccion de logueo
     * @return BeanRespuestaXML conteniendo la informacion(tags) del xml
     * @return null si ha ocurrido algun error o no ha encontrado data
     */
    public static BeanRespuestaXML parsearRespuesta(String xml) {
        BeanRespuestaXML beanXML = null;
        try {
            Document doc = parse(xml, false);
            Element elemRaiz = doc.getRootElement();                //  elemento raiz del archivo xml

            //List lista = elemRaiz.getChildren("return");
            Element nodoReturn = (Element) elemRaiz.getChildren("return").get(0);
            Element nodoHeader = nodoReturn.getChild("RespuestaCASH").getChild("Header");

            beanXML = new BeanRespuestaXML();
            beanXML.setM_CodigoTransaccionIBS(nodoHeader.getChildTextTrim("CodigoTransaccionIBS"));
            beanXML.setM_CodigoTransaccion(nodoHeader.getChildTextTrim("CodigoTransaccion"));
            beanXML.setM_IdTransaccion(nodoHeader.getChildTextTrim("IdTransaccion"));
            beanXML.setM_NumeroOperacion(nodoHeader.getChildTextTrim("NumeroOperacion"));
            beanXML.setM_CodigoRetorno(nodoHeader.getChildTextTrim("CodigoRetorno"));
            beanXML.setM_CodigoRetornoIBS(nodoHeader.getChildTextTrim("CodigoRetornoIBS"));
            //si la respuesta es exitosa
            if ("00".equals(beanXML.getM_CodigoRetorno())) {
                //seteamos todo el contenido del tag de Respuesta
                beanXML.setM_Respuesta(nodoReturn.getChild("RespuestaCASH").getChild("Respuesta"));
            } else {
                Element nodoError = nodoReturn.getChild("RespuestaCASH").getChild("Error");
                //seteamos la descripcion y el detalle del error
                beanXML.setM_DescripcionError(nodoError.getChildTextTrim("descripcion"));
                beanXML.setM_DetalleError(nodoError.getChildTextTrim("detalle"));
            }
        } catch (IOException io) {
            logger.error(io.toString(),io);            
            return null;
        } catch (org.jdom.JDOMException e) {        	
            logger.error(e.toString(),e);            
            return null;
        } catch (Exception e) {
        	 logger.error(e.toString(),e);          
            return null;
        }
        return beanXML;
    }

    /**
     * 
     * @param elemRaiz
     * @return
     */
    public static ArrayList listarConsultaSaldos(Element elemRaiz/*, int nroDecimales*/) {
        ArrayList listaResult = null;
        BeanConsultaSaldosXML beanXML = null;
        String tipoCta = null;
        
        int intTipoCuenta = 0 ;
        
        //System.out.println("LISTAR CONSULTA DE SALDOS");

        String nroDec = null;
        int nroDecimales = 0;
        try {
            Element nodoOdat1R = (Element) elemRaiz.getChildren("odat1R").get(0);
            List lista = nodoOdat1R.getChildren("account");
            if (lista != null && lista.size() > 0) {
                listaResult = new ArrayList();
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < lista.size(); i++) {
                    Element nodo = (Element) lista.get(i);

                    //verificamos el tipo de cuenta valido antes de enlistarlo
                    //solo se mostraran tipos de cuenta con codigo 210 y 110
                    tipoCta = nodo.getChildTextTrim("account_type");
                    //if(tipoCta!=null && ("110".equals(tipoCta) ||  "210".equals(tipoCta))){
                    beanXML = new BeanConsultaSaldosXML();
                    beanXML.setM_Cuenta(nodo.getChildTextTrim("account_code"));

                    beanXML.setM_TipoCuenta(tipoCta);
                    logger.info("CUENTA: " + beanXML.getM_Cuenta() + " - TIPO CUNETA: " + tipoCta);
                    //System.out.println("CUENTA: ");                    
                    //System.out.println("TIPO CUENTAAAAAAAAAAAAA: " + );
                    try{
	                   //// VALIDAR QUE SOLO SEA EL CODIGO
                    	
                    	int tipo = Integer.parseInt(tipoCta); 
	                    if( tipo > 400 && tipo != 405 && tipo != 404  && tipo != 403){
	                    	beanXML.setPrestamo("P");
	                    }else{
	                    	beanXML.setPrestamo("D");
	                    }
                    }catch(Exception e){
                    	beanXML.setPrestamo("D");                    	
                    }
                    

                    //jwong 18/02/2009 para mostrar la descripcion de la cuenta
                    beanXML.setM_DescripcionCuenta(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_DESCRIPTION));

                    beanXML.setM_Moneda(nodo.getChildTextTrim("currency"));
                    try {
                        //obtenemos el numero de decimales para el saldo contable
                        nroDec = nodo.getChild("countable_balance").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanXML.setM_SaldoContable(Util.formatearMontoConComa(nodo.getChildTextTrim("countable_balance"), nroDecimales));

                    try {
                        //obtenemos el numero de decimales para el saldo disponible
                        nroDec = nodo.getChild("available_balance").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanXML.setM_SaldoDisponible(Util.formatearMontoConComa(nodo.getChildTextTrim("available_balance"), nroDecimales));

                    try {
                        //obtenemos el numero de decimales para el saldo retenido
                        nroDec = nodo.getChild("retained_balance").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanXML.setM_SaldoRetenido(Util.formatearMontoConComa(nodo.getChildTextTrim("retained_balance"), nroDecimales));

                    listaResult.add(beanXML);
                //}

                }
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return listaResult;
    }

    /**
     * 
     * @param elemRaiz
     * @return
     */
    public static ArrayList listarCodigosIntBco(Element elemRaiz,int send_number) {
        ArrayList listaResult = null;
        BeanCodigoInterbancarioXML beanXML = null;

        try {
            Element nodoOdat1R = (Element) elemRaiz.getChildren("odat1R").get(0);
            List lista = nodoOdat1R.getChildren("grupo");
            if (lista != null && lista.size() > 0) {
                listaResult = new ArrayList();
                if(send_number > lista.size()){
                    send_number = lista.size();
                }
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < send_number; i++) {
                    Element nodo = (Element) lista.get(i);

                    beanXML = new BeanCodigoInterbancarioXML();
                    beanXML.setM_Cuenta(nodo.getChildTextTrim("cod_int1"));
                    beanXML.setM_Moneda(nodo.getChildTextTrim("cod_int2"));


                    beanXML.setM_TipoCuenta(nodo.getChildTextTrim("cod_int3"));
                    beanXML.setM_Estado(nodo.getChildTextTrim("cod_int4"));
                    beanXML.setM_CodigoInterbancario(nodo.getChildTextTrim("cod_int5"));

                    listaResult.add(beanXML);
                }
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return listaResult;
    }

    /**
     * obtenerTipoCambio, para obtener el tipo de cambio de un tipo de moneda
     * @param elemRaiz
     * @param nroDecimales, el numero de decimales con el que se formatearan los montos del toipo de cambio
     * @return
     */
    public static BeanTipoCambioXML obtenerTipoCambio(Element elemRaiz /*, int nroDecimales*/) {
        BeanTipoCambioXML beanXML = null;
        String nroDec = null;
        int nroDecimales = 0;
        try {
            if (elemRaiz != null) {
                //obtenemos el detalle del tipo de cambio
                //moneda #1
                beanXML = new BeanTipoCambioXML();

                try {
                    //obtenemos el numero de decimales para la compra
                    nroDec = elemRaiz.getChild("change_type01").getAttributeValue("decimal");
                    nroDecimales = Integer.parseInt(nroDec);
                } catch (Exception exc) {
                    nroDecimales = 0;
                }
                beanXML.setM_Compra(Util.formatearMonto(elemRaiz.getChildTextTrim("change_type01"), nroDecimales));

                try {
                    //obtenemos el numero de decimales para la venta
                    nroDec = elemRaiz.getChild("change_type02").getAttributeValue("decimal");
                    nroDecimales = Integer.parseInt(nroDec);
                } catch (Exception exc) {
                    nroDecimales = 0;
                }
                beanXML.setM_Venta(Util.formatearMonto(elemRaiz.getChildTextTrim("change_type02"), nroDecimales));
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }

    /**
     * obtenerDataLogin
     * @param elemRaiz
     * @return
     */
    public static BeanDataLoginXML obtenerDataLogin(Element elemRaiz,HashMap hMapEmpresas) {
        BeanDataLoginXML beanXML = null;
        List lista_empresas = new ArrayList();
        List lista_user_codes = new ArrayList();
        try {
            Element nodoLogin = (Element) elemRaiz.getChildren("login").get(0);
            if (nodoLogin != null) {
                beanXML = new BeanDataLoginXML();

                Element nodoE = (Element) nodoLogin.getChildren("empresas").get(0);
                List lista;
                lista = nodoE.getChildren("empresa");
                if (lista != null && lista.size() > 0) {
                    for (int i = 0; i < lista.size(); i++) {
                        Element nodo = (Element) lista.get(i);
                        lista_empresas.add(nodo.getChildText("id"));
                       
                    }
                }
                //Se obtienen las empresa con sus servicios jmoreno 16-12-09
                lista = nodoE.getChildren("empresa");
                if (lista != null && lista.size() > 0) {
                    //HashMap hMapEmpresas = new HashMap();
                    for (int i = 0; i < lista.size(); i++) {
                        Element nodoEmp = (Element) lista.get(i);
                        List listaNodoServ = nodoEmp.getChild("servicios").getChildren("servicio");
                        List listaServicios = new ArrayList();
                        if(listaNodoServ!= null && listaNodoServ.size()>0){                            
                            for(int j=0;j<listaNodoServ.size();j++){
                                Element nodoServ = (Element) listaNodoServ.get(j);
                                listaServicios.add(nodoServ.getChildTextTrim("id"));
                            }
                        }
                        hMapEmpresas.put(nodoEmp.getChildTextTrim("id"),listaServicios);
                    }
                }

                Element nodoU = (Element) nodoLogin.getChildren("user_codes").get(0);
                lista = nodoU.getChildren("user_code");
                if (lista != null && lista.size() > 0) {
                    for (int i = 0; i < lista.size(); i++) {
                        Element nodo = (Element) lista.get(i);
                        lista_user_codes.add(nodo.getText());
                    }
                }

                //jwong 26/02/2009 verificamos codigo de empresa con acceso a todas las demas
                if (lista_empresas.contains(Constantes.RUC_EMPRESA_OPERACIONES_CASH)) {
                    TmEmpresaDao empresaDao = new TmEmpresaDaoHibernate();
                    lista_empresas = empresaDao.selectCodEmpresas();
                    beanXML.setM_usuarioEspecial(true);//jmoreno 17/11/09
                }

                beanXML.setL_Empresas(lista_empresas);
                System.out.println("");
                beanXML.setL_UserCodes(lista_user_codes);
                beanXML.setM_Apellido(nodoLogin.getChildTextTrim("apellido"));
                beanXML.setM_Nombre(nodoLogin.getChildTextTrim("nombre"));
                beanXML.setM_Codigo((String) lista_user_codes.get(0));
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }    
    //jmoreno 12-08-09
    public static BeanConsRelBanco listarRelacionesBcoPag(String cod_cliente,String ruc_empresa,Element elemRaiz,int total_number,int send_number,CashClientWS cashclienteWS ) {

        int secuencial = 0;
        ArrayList listaSaldos = new ArrayList();
        listarConsultaSaldosPag(elemRaiz, listaSaldos);
        secuencial = secuencial + send_number;
        while(total_number > secuencial){
            ArrayList listaParametros = new ArrayList();
            BeanNodoXML beanNodo = null;
            beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_CTAS_CLIENTE); //id de la transaccion
            listaParametros.add(beanNodo);

            beanNodo = new BeanNodoXML(Constantes.TAG_CLIENT_CODE, cod_cliente); //codigo de la empresa
            listaParametros.add(beanNodo);

            beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
            listaParametros.add(beanNodo);
            String secuencial_enviar = String.valueOf(secuencial);
            while(secuencial_enviar.length()< 4){
                secuencial_enviar = "0" + secuencial_enviar;
            }
            beanNodo = new BeanNodoXML(Constantes.TAG_SECUENCIAL, secuencial_enviar); //Secuencial
            listaParametros.add(beanNodo);
            String resultado = null;
            String req = GenRequestXML.getXML(listaParametros);
            resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
            if(resultado != null && !"".equals(resultado)){
                BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
                if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
                    listarConsultaSaldosPag(beanResXML.getM_Respuesta(), listaSaldos);
                    send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
                    secuencial = secuencial + send_number;
                }else{
                    secuencial = total_number;
                }
            }else{
                secuencial = total_number;
            }

        }

        BeanConsRelBanco beanXML = null;
        try {
            beanXML = new BeanConsRelBanco();
            //obtenemos los datos del sectorista y del funcionario cash
            TmFuncionarioDao funcionarioDAO = new TmFuncionarioDaoHibernate();
            //sectorista
            TmFuncionario funcionario = funcionarioDAO.selectFuncionarioByEmpresaAndType(ruc_empresa, "01");
            if (funcionario != null) {
                beanXML.setM_NameSec(funcionario.getDfuNombre());
                beanXML.setM_PhoneSec(funcionario.getDfuTelefono());
                beanXML.setM_EmailSec(funcionario.getDfuEmail());
            }

            //funcionario cash
            funcionario = funcionarioDAO.selectFuncionarioByEmpresaAndType(ruc_empresa, "02");
            if (funcionario != null) {
                beanXML.setM_NameFunc(funcionario.getDfuNombre());
                beanXML.setM_PhoneFunc(funcionario.getDfuTelefono());
                beanXML.setM_EmailFunc(funcionario.getDfuEmail());
            }

            beanXML.setM_Accounts(listaSaldos);
            beanXML.setM_AccountsType(agruparXtipoCuenta(listaSaldos));

        } catch (Exception e) {
        	logger.error(e.toString(),e);          
            return null;
        }
        return beanXML;
    }
    //jmoreno 12-08-09
    public static void listarConsultaSaldosPag(Element elemRaiz,ArrayList listaResult) {
        BeanConsultaSaldosXML beanXML = null;
        String tipoCta = null;
        String nroDec = null;
        int nroDecimales = 0;
        try {
            Element nodoOdat1R = (Element) elemRaiz.getChildren("odat1R").get(0);
            List lista = nodoOdat1R.getChildren("account");
            if (lista != null && lista.size() > 0) {
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < lista.size(); i++) {
                    Element nodo = (Element) lista.get(i);

                    //verificamos el tipo de cuenta valido antes de enlistarlo
                    //solo se mostraran tipos de cuenta con codigo 210 y 110
                    tipoCta = nodo.getChildTextTrim("account_type");
                    //if(tipoCta!=null && ("110".equals(tipoCta) ||  "210".equals(tipoCta))){
                    beanXML = new BeanConsultaSaldosXML();
                    beanXML.setM_Cuenta(nodo.getChildTextTrim("account_code"));                    
                    beanXML.setM_TipoCuenta(tipoCta);
                    logger.info("CUENTA: " + beanXML.getM_Cuenta() + " - TIPO CUNETA: " + tipoCta);
                    try{                    	
                    	int tipo = Integer.parseInt(tipoCta); 
	                    if( tipo > 400 && tipo != 405 && tipo != 404 && tipo != 403){
	                    	beanXML.setPrestamo("P");
	                    }else{
	                    	beanXML.setPrestamo("D");
	                    }                    	
                    }catch(Exception e){
                    	beanXML.setPrestamo("D");
                    }

                    //jwong 18/02/2009 para mostrar la descripcion de la cuenta
                    beanXML.setM_DescripcionCuenta(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_DESCRIPTION));

                    beanXML.setM_Moneda(nodo.getChildTextTrim("currency"));
                    try {
                        //obtenemos el numero de decimales para el saldo contable
                        nroDec = nodo.getChild("countable_balance").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanXML.setM_SaldoContable(Util.formatearMontoConComa(nodo.getChildTextTrim("countable_balance"), nroDecimales));

                    try {
                        //obtenemos el numero de decimales para el saldo disponible
                        nroDec = nodo.getChild("available_balance").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanXML.setM_SaldoDisponible(Util.formatearMontoConComa(nodo.getChildTextTrim("available_balance"), nroDecimales));

                    try {
                        //obtenemos el numero de decimales para el saldo retenido
                        nroDec = nodo.getChild("retained_balance").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanXML.setM_SaldoRetenido(Util.formatearMontoConComa(nodo.getChildTextTrim("retained_balance"), nroDecimales));

                    listaResult.add(beanXML);
                //}

                }
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
        }
    }
    //listarRelacionesBco
    public static BeanConsRelBanco listarRelacionesBco(Element elemRaiz, String ruc_empresa/*, int nroDecimales*/) {
        
        BeanConsRelBanco beanXML = null;        
        try {
            beanXML = new BeanConsRelBanco();

            //obtenemos los datos del sectorista y del funcionario cash
            TmFuncionarioDao funcionarioDAO = new TmFuncionarioDaoHibernate();
            //sectorista
            TmFuncionario funcionario = funcionarioDAO.selectFuncionarioByEmpresaAndType(ruc_empresa, "01");
            if (funcionario != null) {
                beanXML.setM_NameSec(funcionario.getDfuNombre());
                beanXML.setM_PhoneSec(funcionario.getDfuTelefono());
                beanXML.setM_EmailSec(funcionario.getDfuEmail());
            }

            //funcionario cash
            funcionario = funcionarioDAO.selectFuncionarioByEmpresaAndType(ruc_empresa, "02");
            if (funcionario != null) {
                beanXML.setM_NameFunc(funcionario.getDfuNombre());
                beanXML.setM_PhoneFunc(funcionario.getDfuTelefono());
                beanXML.setM_EmailFunc(funcionario.getDfuEmail());
            }
            List listaSaldos = listarConsultaSaldos(elemRaiz);
            beanXML.setM_Accounts(listaSaldos);
            beanXML.setM_AccountsType(agruparXtipoCuenta(listaSaldos));

        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }
//jwong 09/03/2009 nueva forma de agrupar tipos de cuenta

    /*
    private static List agruparXtipoCuenta(List lista_ini) {
        List lista_gen = null;
        List lista_result = null;
        if (lista_ini != null && lista_ini.size() > 0) {
            List lista_temp = null;
            String tipoCuenta = null;
            BeanConsultaSaldosXML beanaccount = null;
            Hashtable hash = new Hashtable();
            for (int i = 0; i < lista_ini.size(); i++) {
                beanaccount = (BeanConsultaSaldosXML) lista_ini.get(i);
                tipoCuenta = beanaccount.getM_TipoCuenta();

                //verificamos que no se exista listado para ese tipo de cuenta
                lista_temp = (List) hash.get(tipoCuenta);
                if (lista_temp == null) {
                    lista_temp = new ArrayList();
                    lista_temp.add(beanaccount);

                    hash.put(tipoCuenta, lista_temp);
                } else {
                    //si existe el listado verificamos que el objeto no se haya enlistado aun
                    if (!lista_temp.contains(beanaccount)) {
                        lista_temp.add(beanaccount);
                        hash.remove(tipoCuenta);

                        hash.put(tipoCuenta, lista_temp);
                    }
                }
            }
            lista_gen = new ArrayList(hash.values());
            BeanTypeAccount beanType = null;
            lista_result = new ArrayList();
            //se incluye la descripcion para cada tipo de cuenta
            for (int h = 0; h < lista_gen.size(); h++) {
                //cada elemento es una lista conteniendo tipos de cuentas relacionadas
                List lista = (List) lista_gen.get(h);
                if (lista != null && lista.size() > 0) {
                    beanaccount = (BeanConsultaSaldosXML) lista.get(0); //obtenemos el primer elemento de la lista
                    beanType = new BeanTypeAccount();
                    beanType.setM_Code(beanaccount.getM_TipoCuenta());

                    //obtenemos la descripcion del tipo de cuenta
                    beanType.setM_Description(beanaccount.getM_DescripcionCuenta());

                    beanType.setM_Accounts(lista);
                    lista_result.add(beanType);
                }
            }
        }
        return lista_result;
    }
    */
    private static List agruparXtipoCuenta(List lista_ini) {
        List lista_gen = null;
        List lista_result = null;
        if (lista_ini != null && lista_ini.size() > 0) {
            List lista_temp = null;
            String descripcionCta = null;
            BeanConsultaSaldosXML beanaccount = null;
            Hashtable hash = new Hashtable();
            for (int i = 0; i < lista_ini.size(); i++) {
                beanaccount = (BeanConsultaSaldosXML) lista_ini.get(i);
                descripcionCta = beanaccount.getM_DescripcionCuenta();

                //verificamos que no se exista listado para ese tipo de cuenta
                lista_temp = (List) hash.get(descripcionCta);
                if (lista_temp == null) {
                    lista_temp = new ArrayList();
                    lista_temp.add(beanaccount);
                    hash.put(descripcionCta, lista_temp);
                } else {
                    //si existe el listado verificamos que el objeto no se haya enlistado aun
                    if (!lista_temp.contains(beanaccount)) {
                        lista_temp.add(beanaccount);
                        hash.remove(descripcionCta);

                        hash.put(descripcionCta, lista_temp);
                    }
                }
            }
            lista_gen = new ArrayList(hash.values());
            BeanTypeAccount beanType = null;
            lista_result = new ArrayList();
            //se incluye la descripcion para cada tipo de cuenta
            for (int h = 0; h < lista_gen.size(); h++) {
                //cada elemento es una lista conteniendo tipos de cuentas relacionadas
                List lista = (List) lista_gen.get(h);
                if (lista != null && lista.size() > 0) {
                    beanaccount = (BeanConsultaSaldosXML) lista.get(0); //obtenemos el primer elemento de la lista
                    beanType = new BeanTypeAccount();
                    beanType.setM_Code(beanaccount.getM_TipoCuenta());

                    //obtenemos la descripcion del tipo de cuenta
                    beanType.setM_Description(beanaccount.getM_DescripcionCuenta());

                    beanType.setM_Accounts(lista);
                    lista_result.add(beanType);
                }
            }
        }

        if(lista_result!=null && lista_result.size()>0){
            Collections.sort(lista_result);
        }

        return lista_result;
    }
    

    //jwong 09/03/2009 comentado
    /*
    //jwong 09/01/2009 agrupar por tipo de cuenta
    private static List agruparXtipoCuenta(List lista_ini){
    List lista_gen = null;
    List lista_result = null;
    if(lista_ini!=null && lista_ini.size()>0){
    List lista_temp = null;
    String tipoCuenta = null;
    BeanAccount beanaccount = null;
    Hashtable hash = new Hashtable();
    for(int i=0 ; i<lista_ini.size() ; i++){
    beanaccount = (BeanAccount)lista_ini.get(i);
    tipoCuenta = beanaccount.getM_AccountType();

    //verificamos que no se exista listado para ese tipo de cuenta
    lista_temp = (List)hash.get(tipoCuenta);
    if(lista_temp == null){
    lista_temp = new ArrayList();
    lista_temp.add(beanaccount);

    hash.put(tipoCuenta, lista_temp);
    }
    else{
    //si existe el listado verificamos que el objeto no se haya enlistado aun
    if(!lista_temp.contains(beanaccount)){
    lista_temp.add(beanaccount);
    hash.remove(tipoCuenta);

    hash.put(tipoCuenta, lista_temp);
    }
    }
    }
    lista_gen = new ArrayList(hash.values());
    BeanTypeAccount beanType = null;
    lista_result = new ArrayList();
    //se incluye la descripcion para cada tipo de cuenta
    for(int h=0 ; h<lista_gen.size() ; h++){
    //cada elemento es una lista conteniendo tipos de cuentas relacionadas
    List lista = (List)lista_gen.get(h);
    if(lista!=null && lista.size()>0){
    beanaccount = (BeanAccount)lista.get(0); //obtenemos el primer elemento de la lista
    beanType = new BeanTypeAccount();
    beanType.setM_Code(beanaccount.getM_AccountType());

    //obtenemos la descripcion del tipo de cuenta
    TxListFieldDao listFieldDAO = new TxListFieldDaoHibernate();
    TxListField objlistfield = listFieldDAO.selectListField(Constantes.FIELD_CASH_TIPO_CUENTA, beanaccount.getM_AccountType());
    if(objlistfield!=null){
    beanType.setM_Description(objlistfield.getDlfDescription());
    }

    beanType.setM_Accounts(lista);
    lista_result.add(beanType);
    }
    }
    }
    return lista_result;
    }*/

    //getTransferenciaCtas Propias y Terceros
    public static BeanTransCtasPropias getTransferenciaCtas(Element elemRaiz) {
        BeanTransCtasPropias beanXML = null;
        try {
            beanXML = new BeanTransCtasPropias();
            beanXML.setM_ClientName(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_NAME));
            beanXML.setM_ClientFrom(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_FROM));
            beanXML.setM_ClientTo(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_TO));
            beanXML.setM_OriginalAmount(elemRaiz.getChildTextTrim(Constantes.TAG_ORIGINAL_AMOUNT));
            beanXML.setM_ChangeType(elemRaiz.getChildTextTrim(Constantes.TAG_CHANGE_TYPE));
            beanXML.setM_Out(elemRaiz.getChildTextTrim(Constantes.TAG_OUT));

        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }
    //getConsCtasCliente

    /*
    //public static BeanConsCtasCliente getConsCtasCliente(Element elemRaiz) {
    public static BeanConsCtasCliente getConsCtasCliente(Element elemRaiz, CashClientWS cashclienteWS, String numTarjeta, String token, String ruc_empresa){
        ArrayList listaAccount = null;
        BeanConsCtasCliente beanXML = null;
        BeanAccount beanaccount = null;
        String nroDec = null;
        int nroDecimales = 0;
        try {
            beanXML = new BeanConsCtasCliente();
//            beanXML.setM_TotalNumber(elemRaiz.getChildTextTrim(Constantes.TAG_TOTAL_NUMBER));
//            beanXML.setM_SendNumber(elemRaiz.getChildTextTrim(Constantes.TAG_SEND_NUMBER));


            Element nodoOdat1F = (Element) elemRaiz.getChild("odat1F");
            beanXML.setM_ClientCode(nodoOdat1F.getChildTextTrim(Constantes.TAG_CLIENT_CODE));
            beanXML.setM_ClientName(nodoOdat1F.getChildTextTrim(Constantes.TAG_CLIENT_NAME));
//            beanXML.setM_OfficialCredit(nodoOdat1F.getChildTextTrim(Constantes.TAG_OFFICIAL_CREDIT));
//            beanXML.setM_PersonType(nodoOdat1F.getChildTextTrim(Constantes.TAG_PERSON_TYPE));
//            beanXML.setM_ChangeType(nodoOdat1F.getChildTextTrim(Constantes.TAG_CHANGE_TYPE));
//            beanXML.setM_CountableBalanceSol(nodoOdat1F.getChildTextTrim(Constantes.TAG_COUNTABLE_BALANCE_SOL));
//            beanXML.setM_AvailableBalanceSol(nodoOdat1F.getChildTextTrim(Constantes.TAG_AVAILABLE_BALANCE_SOL));
//            beanXML.setM_LiquidateBalanceSol(nodoOdat1F.getChildTextTrim(Constantes.TAG_LIQUIDATE_BALANCE_SOL));
//            beanXML.setM_DeferredBalanceSol(nodoOdat1F.getChildTextTrim(Constantes.TAG_DEFERRED_BALANCE_SOL));
//            beanXML.setM_CountableBalanceDol(nodoOdat1F.getChildTextTrim(Constantes.TAG_COUNTABLE_BALANCE_DOL));
//            beanXML.setM_AvailableBalanceDol(nodoOdat1F.getChildTextTrim(Constantes.TAG_AVAILABLE_BALANCE_DOL));
//            beanXML.setM_LiquidateBalanceDol(nodoOdat1F.getChildTextTrim(Constantes.TAG_LIQUIDATE_BALANCE_DOL));
//            beanXML.setM_DeferredBalanceDol(nodoOdat1F.getChildTextTrim(Constantes.TAG_DEFERRED_BALANCE_DOL));

            Element nodoOdat1R = (Element) elemRaiz.getChildren("odat1R").get(0);
            List lista = nodoOdat1R.getChildren(Constantes.TAG_ACCOUNT);
            if (lista != null && lista.size() > 0) {
                listaAccount = new ArrayList();

                //obtenemos el listado de cuentas que trae la consulta de relaciones con el banco
                Hashtable table = obtenerConsRelacionBco(cashclienteWS, numTarjeta, token, ruc_empresa);
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < lista.size(); i++) {
                    Element nodo = (Element) lista.get(i);

                    beanaccount = new BeanAccount();
                    beanaccount.setM_AccountCode(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_CODE));
                    beanaccount.setM_AccountType(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_TYPE));
                    beanaccount.setM_AccountDescription(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_DESCRIPTION));
                    beanaccount.setM_Mancomunado(nodo.getChildTextTrim(Constantes.TAG_MANCOMUNADO));
                    beanaccount.setM_AccountSituation(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_SITUATION));
                    beanaccount.setM_Currency(nodo.getChildTextTrim(Constantes.TAG_CURRENCY));
                    beanaccount.setM_CountableBalance(nodo.getChildTextTrim(Constantes.TAG_COUNTABLE_BALANCE));
                    try {
                        //obtenemos el numero de decimales para el saldo disponible
                        nroDec = nodo.getChild("available_balance").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanaccount.setM_AvailableBalance(Util.formatearMonto(nodo.getChildTextTrim(Constantes.TAG_AVAILABLE_BALANCE), nroDecimales));

                    beanaccount.setM_RetainedBalance(nodo.getChildTextTrim(Constantes.TAG_RETAINED_BALANCE));
                    beanaccount.setM_LoanNumber(nodo.getChildTextTrim(Constantes.TAG_LOAN_NUMBER));

                    if(table!=null && table.get(beanaccount.getM_AccountCode())!=null){
                        listaAccount.add(beanaccount);
                    }
                }
                beanXML.setM_Accounts(listaAccount);
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.toString());
            return null;
        }
        return beanXML;
    }
    */
    
    public static BeanConsCtasCliente getConsCtasCliente(Element elemRaiz){
        ArrayList listaAccount = null;
        BeanConsCtasCliente beanXML = null;
        BeanAccount beanaccount = null;
        String nroDec = null;
        int nroDecimales = 0;
        try {
            beanXML = new BeanConsCtasCliente();
            beanXML.setM_ClientCode(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_CODE));
            beanXML.setM_ClientName(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_NAME));          
            Element nodoOdat1R = (Element) elemRaiz.getChildren("accounts").get(0);
            List lista = nodoOdat1R.getChildren("account");

            if (lista != null && lista.size() > 0) {
                listaAccount = new ArrayList();
                for (int i = 0; i < lista.size(); i++) {
                    Element nodo = (Element) lista.get(i);

                    beanaccount = new BeanAccount();
                    beanaccount.setM_AccountType(nodo.getChildTextTrim("Odtipct"));
                    beanaccount.setM_AccountCode(nodo.getChildTextTrim("ODcodct"));
                    beanaccount.setM_AccountDescription(nodo.getChildTextTrim("ODdescr"));
                    beanaccount.setM_AccountSituation(nodo.getChildTextTrim("ODcondi"));
                    beanaccount.setM_Currency(nodo.getChildTextTrim("ODsmone"));
                    beanaccount.setM_AvailableBalance(nodo.getChildTextTrim("ODslddi"));
                    try {
                        nroDec = nodo.getChild("ODmonto").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanaccount.setM_AvailableBalance(Util.formatearMonto(beanaccount.getM_AvailableBalance(), nroDecimales));

                    if(beanaccount.getM_Currency()!=null && beanaccount.getM_Currency().length()>0){
                        if(Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(beanaccount.getM_Currency())){
                            beanaccount.setM_Currency(Constantes.FIELD_CASH_SIMBOLO_MONEDA_SOLES);
                            if(beanaccount.getM_AccountDescription()!=null){
                                beanaccount.setM_AccountDescription(beanaccount.getM_AccountDescription() + " " + Constantes.FIELD_CASH_DESCR_MONEDA_SOLES);
                            }
                        }
                        else if(Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES.equals(beanaccount.getM_Currency())){
                            beanaccount.setM_Currency(Constantes.FIELD_CASH_SIMBOLO_MONEDA_DOLARES);
                            if(beanaccount.getM_AccountDescription()!=null){
                                beanaccount.setM_AccountDescription(beanaccount.getM_AccountDescription() + " " + Constantes.FIELD_CASH_DESCR_MONEDA_DOLARES);
                            }
                        }
                    }

                    listaAccount.add(beanaccount);
                }
                beanXML.setM_Accounts(listaAccount);
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }
    
    public static BeanConsCtasCliente getConsCtasClienteCombos(Element elemRaiz){
        ArrayList listaAccount = null;
        BeanConsCtasCliente beanXML = null;
        BeanAccount beanaccount = null;
        String nroDec = null;
        int nroDecimales = 0;
        try {
            beanXML = new BeanConsCtasCliente();
//            beanXML.setM_TotalNumber(elemRaiz.getChildTextTrim(Constantes.TAG_TOTAL_NUMBER));
//            beanXML.setM_SendNumber(elemRaiz.getChildTextTrim(Constantes.TAG_SEND_NUMBER));


            //Element nodoOdat1F = (Element) elemRaiz.getChild("odat1F");
            //beanXML.setM_CardNumber(elemRaiz.getChildTextTrim(Constantes.TAG_CARD_NUMBER));
            beanXML.setM_ClientCode(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_CODE));
            beanXML.setM_ClientName(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_NAME));

            //public static final String TAG_CLIENT_CODE = "client_code";
            //public static final String TAG_CLIENT_NAME = "client_name";
            /*
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
            */
//            <Respuesta>
//                    <card_number             campoFI="card_number"/>
//                    <client_code             campoFI="client_code"/>
//                    <client_name             campoFI="client_name"/>
            /*
                    <document                campoFI="document"/>
                    <birth_date              campoFI="birth_date"/>
                    <client_type             campoFI="client_type"/>
                    <doc_type                campoFI="doc_type_code"/>
                    <account_quantity        campoFI="account_quantity"/>
            */
            /*
                    <accounts campoFI="accounts" tipo = "lista" filas = "account_quantity" tam_fila = "143" group_name = "account" delegateTo="CONVGENCASH">
                        <Odtipct campoFI  = "Odtipct" />
                        <ODmoned campoFI  = "ODmoned" />
                        <ODcodct campoFI  = "ODcodct" />
                        <ODsigno campoFI  = "ODsigno" />
                        <ODmonto campoFI  = "ODmonto" decimal="2"/>
                        <Odtipct2 campoFI = "Odtipct2"/>
                        <ODdescr campoFI  = "ODdescr" />
                        <ODcondi campoFI  = "ODcondi" />
                        <ODsmone campoFI  = "ODsmone" />
                        <Odsldco campoFI  = "Odsldco" />
                        <ODslddi campoFI  = "ODslddi" />
                        <ODsldlq campoFI  = "ODsldlq" />
                        <ODslddf campoFI  = "ODslddf" />
                        <ODsldrt campoFI  = "ODsldrt" />
                    </accounts>
                    <card_type               campoFI="card_type"/>
					<delim01                 campoFI="delim01"/>
//                </Respuesta>
            */


//            beanXML.setM_OfficialCredit(nodoOdat1F.getChildTextTrim(Constantes.TAG_OFFICIAL_CREDIT));
//            beanXML.setM_PersonType(nodoOdat1F.getChildTextTrim(Constantes.TAG_PERSON_TYPE));
//            beanXML.setM_ChangeType(nodoOdat1F.getChildTextTrim(Constantes.TAG_CHANGE_TYPE));
//            beanXML.setM_CountableBalanceSol(nodoOdat1F.getChildTextTrim(Constantes.TAG_COUNTABLE_BALANCE_SOL));
//            beanXML.setM_AvailableBalanceSol(nodoOdat1F.getChildTextTrim(Constantes.TAG_AVAILABLE_BALANCE_SOL));
//            beanXML.setM_LiquidateBalanceSol(nodoOdat1F.getChildTextTrim(Constantes.TAG_LIQUIDATE_BALANCE_SOL));
//            beanXML.setM_DeferredBalanceSol(nodoOdat1F.getChildTextTrim(Constantes.TAG_DEFERRED_BALANCE_SOL));
//            beanXML.setM_CountableBalanceDol(nodoOdat1F.getChildTextTrim(Constantes.TAG_COUNTABLE_BALANCE_DOL));
//            beanXML.setM_AvailableBalanceDol(nodoOdat1F.getChildTextTrim(Constantes.TAG_AVAILABLE_BALANCE_DOL));
//            beanXML.setM_LiquidateBalanceDol(nodoOdat1F.getChildTextTrim(Constantes.TAG_LIQUIDATE_BALANCE_DOL));
//            beanXML.setM_DeferredBalanceDol(nodoOdat1F.getChildTextTrim(Constantes.TAG_DEFERRED_BALANCE_DOL));

            Element nodoOdat1R = (Element) elemRaiz.getChildren("accounts").get(0);
            //List lista = elemRaiz.getChildren(Constantes.TAG_ACCOUNT);
            List lista = nodoOdat1R.getChildren("account");

            if (lista != null && lista.size() > 0) {
                listaAccount = new ArrayList();

                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < lista.size(); i++) {
                    Element nodo = (Element) lista.get(i);

                    beanaccount = new BeanAccount();

                    /*
                    beanaccount.setM_AccountCode(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_CODE));
                    beanaccount.setM_AccountType(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_TYPE));
                    beanaccount.setM_AccountDescription(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_DESCRIPTION));
                    beanaccount.setM_Mancomunado(nodo.getChildTextTrim(Constantes.TAG_MANCOMUNADO));
                    beanaccount.setM_AccountSituation(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_SITUATION));
                    beanaccount.setM_Currency(nodo.getChildTextTrim(Constantes.TAG_CURRENCY));
                    beanaccount.setM_CountableBalance(nodo.getChildTextTrim(Constantes.TAG_COUNTABLE_BALANCE));
                    */

                    beanaccount.setM_AccountType(nodo.getChildTextTrim("Odtipct"));
                    beanaccount.setM_AccountCode(nodo.getChildTextTrim("ODcodct"));
                    beanaccount.setM_AccountDescription(nodo.getChildTextTrim("ODdescr"));
                    //beanaccount.setM_Mancomunado(nodo.getChildTextTrim(""));
                    beanaccount.setM_AccountSituation(nodo.getChildTextTrim("ODcondi"));
                    beanaccount.setM_Currency(nodo.getChildTextTrim("ODsmone"));
                    beanaccount.setM_AvailableBalance(nodo.getChildTextTrim("ODslddi"));
/*
                        <ODmoned campoFI  = "ODmoned" />
                        <ODcodct campoFI  = "ODcodct" />
                        <ODsigno campoFI  = "ODsigno" />
                        <ODmonto campoFI  = "ODmonto" decimal="2"/>
                        <Odtipct2 campoFI = "Odtipct2"/>
                        <ODdescr campoFI  = "ODdescr" />
                        <ODcondi campoFI  = "ODcondi" />
                        <ODsmone campoFI  = "ODsmone" />
                        <Odsldco campoFI  = "Odsldco" />
                        <ODslddi campoFI  = "ODslddi" />
                        <ODsldlq campoFI  = "ODsldlq" />
                        <ODslddf campoFI  = "ODslddf" />
                        <ODsldrt campoFI  = "ODsldrt" />
*/
                    try {
                        //obtenemos el numero de decimales para el saldo disponible
                        nroDec = nodo.getChild("ODmonto").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanaccount.setM_AvailableBalance(Util.formatearMonto(beanaccount.getM_AvailableBalance(), nroDecimales));

                    //beanaccount.setM_RetainedBalance(nodo.getChildTextTrim(Constantes.TAG_RETAINED_BALANCE));
                    //beanaccount.setM_LoanNumber(nodo.getChildTextTrim(Constantes.TAG_LOAN_NUMBER));

                    if(beanaccount.getM_Currency()!=null && beanaccount.getM_Currency().length()>0){
                        if(Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(beanaccount.getM_Currency())){
                            beanaccount.setM_Currency(Constantes.FIELD_CASH_SIMBOLO_MONEDA_SOLES);
                            if(beanaccount.getM_AccountDescription()!=null){
                                beanaccount.setM_AccountDescription(beanaccount.getM_AccountDescription() + " " + Constantes.FIELD_CASH_DESCR_MONEDA_SOLES);
                            }
                        }
                        else if(Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES.equals(beanaccount.getM_Currency())){
                            beanaccount.setM_Currency(Constantes.FIELD_CASH_SIMBOLO_MONEDA_DOLARES);
                            if(beanaccount.getM_AccountDescription()!=null){
                                beanaccount.setM_AccountDescription(beanaccount.getM_AccountDescription() + " " + Constantes.FIELD_CASH_DESCR_MONEDA_DOLARES);
                            }
                        }
                    }

                    listaAccount.add(beanaccount);
                }
                beanXML.setM_Accounts(listaAccount);
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }

    public static BeanConsCtasCliente getConsCtasSolesClienteCombos(Element elemRaiz){
        ArrayList listaAccount = null;
        BeanConsCtasCliente beanXML = null;
        BeanAccount beanaccount = null;
        String nroDec = null;
        int nroDecimales = 0;
        try {
            beanXML = new BeanConsCtasCliente();
            beanXML.setM_ClientCode(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_CODE));
            beanXML.setM_ClientName(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_NAME));

            Element nodoOdat1R = (Element) elemRaiz.getChildren("accounts").get(0);
            //List lista = elemRaiz.getChildren(Constantes.TAG_ACCOUNT);
            List lista = nodoOdat1R.getChildren("account");

            if (lista != null && lista.size() > 0) {
                listaAccount = new ArrayList();

                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < lista.size(); i++) {
                    Element nodo = (Element) lista.get(i);
                    beanaccount = new BeanAccount();
                    beanaccount.setM_AccountType(nodo.getChildTextTrim("Odtipct"));
                    beanaccount.setM_AccountCode(nodo.getChildTextTrim("ODcodct"));
                    beanaccount.setM_AccountDescription(nodo.getChildTextTrim("ODdescr"));
                    //beanaccount.setM_Mancomunado(nodo.getChildTextTrim(""));
                    beanaccount.setM_AccountSituation(nodo.getChildTextTrim("ODcondi"));

                    beanaccount.setM_Currency(nodo.getChildTextTrim("ODsmone"));
                    beanaccount.setM_AvailableBalance(nodo.getChildTextTrim("ODslddi"));
                    try {
                        //obtenemos el numero de decimales para el saldo disponible
                        nroDec = nodo.getChild("ODmonto").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanaccount.setM_AvailableBalance(Util.formatearMonto(beanaccount.getM_AvailableBalance(), nroDecimales));

                    //beanaccount.setM_RetainedBalance(nodo.getChildTextTrim(Constantes.TAG_RETAINED_BALANCE));
                    //beanaccount.setM_LoanNumber(nodo.getChildTextTrim(Constantes.TAG_LOAN_NUMBER));

                    if(beanaccount.getM_Currency()!=null && beanaccount.getM_Currency().length()>0){
                        if(Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(beanaccount.getM_Currency())){
                            beanaccount.setM_Currency(Constantes.FIELD_CASH_SIMBOLO_MONEDA_SOLES);
                            if(beanaccount.getM_AccountDescription()!=null){
                                beanaccount.setM_AccountDescription(beanaccount.getM_AccountDescription() + " " + Constantes.FIELD_CASH_DESCR_MONEDA_SOLES);
                            }
                        }
                        else if(Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES.equals(beanaccount.getM_Currency())){
                            beanaccount.setM_Currency(Constantes.FIELD_CASH_SIMBOLO_MONEDA_DOLARES);
                            if(beanaccount.getM_AccountDescription()!=null){
                                beanaccount.setM_AccountDescription(beanaccount.getM_AccountDescription() + " " + Constantes.FIELD_CASH_DESCR_MONEDA_DOLARES);
                            }
                        }
                    }
                    if(Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equalsIgnoreCase(nodo.getChildTextTrim("ODsmone"))){
                        listaAccount.add(beanaccount);
                    }
                }
                beanXML.setM_Accounts(listaAccount);
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }

    /**
     * getConsultaSaldos, para el manejo de la nueva transaccion 3068
     * @param elemRaiz
     * @return
     */
    public static ArrayList getConsultaSaldos(Element elemRaiz/*, int nroDecimales*/) {
        ArrayList listaResult = null;
        BeanConsultaSaldosXML beanXML = null;
        //String tipoCta = null;

        String nroDec = null;
        int nroDecimales = 0;
        try {

            Element nodoOdat1R = (Element) elemRaiz.getChildren("accounts").get(0);
            List lista = nodoOdat1R.getChildren("account");
//if (lista != null && lista.size() > 0) {
            if (lista != null) { // jmoreno 26-08-09 Cambio realizado para mostrar el mensaje correctamente
                listaResult = new ArrayList();
                if(lista.size() > 0){     // jmoreno 26-08-09
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < lista.size(); i++) {
                    Element nodo = (Element) lista.get(i);

                    beanXML = new BeanConsultaSaldosXML();

                    beanXML.setM_TipoCuenta(nodo.getChildTextTrim("Odtipct")); //
                    beanXML.setM_Cuenta(nodo.getChildTextTrim("ODcodct")); //
                    beanXML.setM_DescripcionCuenta(nodo.getChildTextTrim("ODdescr")); //
                    beanXML.setM_Moneda(nodo.getChildTextTrim("ODsmone")); //

                    //obtenemos el numero de decimales para todos los montos
                    try {
                        nroDec = nodo.getChild("ODmonto").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanXML.setM_SaldoContable(Util.formatearMontoConComa(nodo.getChildTextTrim("Odsldco"), nroDecimales));
                    beanXML.setM_SaldoDisponible(Util.formatearMontoConComa(nodo.getChildTextTrim("ODslddi"), nroDecimales));
                    beanXML.setM_SaldoRetenido(Util.formatearMontoConComa(nodo.getChildTextTrim("ODsldrt"), nroDecimales));

                    if(beanXML.getM_Moneda()!=null && beanXML.getM_Moneda().length()>0){
                        if(Constantes.FIELD_CASH_TIPO_MONEDA_SOLES.equals(beanXML.getM_Moneda())){
                            beanXML.setM_Moneda(Constantes.FIELD_CASH_SIMBOLO_MONEDA_SOLES);
                            if(beanXML.getM_DescripcionCuenta()!=null){
                                beanXML.setM_DescripcionCuenta(beanXML.getM_DescripcionCuenta() + " " + Constantes.FIELD_CASH_DESCR_MONEDA_SOLES);
                            }
                        }
                        else if(Constantes.FIELD_CASH_TIPO_MONEDA_DOLARES.equals(beanXML.getM_Moneda())){
                            beanXML.setM_Moneda(Constantes.FIELD_CASH_SIMBOLO_MONEDA_DOLARES);
                            if(beanXML.getM_DescripcionCuenta()!=null){
                                beanXML.setM_DescripcionCuenta(beanXML.getM_DescripcionCuenta() + " " + Constantes.FIELD_CASH_DESCR_MONEDA_DOLARES);
                            }
                        }
                    }
                    listaResult.add(beanXML);
                }
                }                
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return listaResult;
    }
    

    private static Hashtable obtenerConsRelacionBco(CashClientWS cashclienteWS, String numTarjeta, String token, String ruc_empresa){
        Hashtable table = null;
        ArrayList listaParametros = new ArrayList();
        BeanNodoXML beanNodo = null;
        String resultado = null;

        //añadimos cada uno de los parametros usados en el logueo
        beanNodo = new BeanNodoXML(Constantes.TAG_TRX_IBS, Constantes.IBS_CONS_REL_BANCO); //id de la transaccion
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_CARD_NUMBER, numTarjeta); //numero de trajeta
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_BLOCK, token); //block(token del login)
        listaParametros.add(beanNodo);

        beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //block(token del login)
        listaParametros.add(beanNodo);

        //generamos el xml request
        String req = GenRequestXML.getXML(listaParametros);
        resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
        if(resultado==null || "".equals(resultado)){
            return null;
        }
        //se debe parsear el xml obtenido
        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
        //si la respuesta es exitosa
        if(beanResXML!=null && "00".equals(beanResXML.getM_CodigoRetorno())){
            //procesamos la respuesta(parseo xml) y enviamos a la pagina
            table = listarCtasRelacionesBco(beanResXML.getM_Respuesta());
        }
        return table;
    }
    /**
     * Para obtener el listado de cuentas habilitadas (solo el listado de cuentas)
     * @param elemRaiz
     * @return
     */
    
    private static Hashtable listarCtasRelacionesBco(Element elemRaiz) {
        Hashtable listaAccount = null;
        BeanAccount beanaccount = null;

        String nroDec = null;
        int nroDecimales = 0;
        try {
            Element nodoOdat1R = (Element)elemRaiz.getChildren(Constantes.TAG_ACCOUNTS).get(0);
            List lista = nodoOdat1R.getChildren(Constantes.TAG_ACCOUNT);
            if(lista!=null && lista.size()>0){
                listaAccount = new Hashtable();
                //obtenemos el detalle de cada una de las cuentas
                for(int i=0 ; i<lista.size() ; i++){
                    Element nodo = (Element)lista.get(i);
                    beanaccount = new BeanAccount();
                    beanaccount.setM_AccountType(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_TYPE));
                    beanaccount.setM_Currency(nodo.getChildTextTrim(Constantes.TAG_CURRENCY_2));
                    beanaccount.setM_AccountCode(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT_CODE));
                    beanaccount.setM_Sign(nodo.getChildTextTrim(Constantes.TAG_SIGN));
                    try{
                        //obtenemos el numero de decimales para el amount
                        nroDec = nodo.getChild(Constantes.TAG_AMOUNT).getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    }catch(Exception exc){
                        nroDecimales = 0;
                        exc.printStackTrace();
                    }
                    beanaccount.setM_Amount(Util.formatearMonto(nodo.getChildTextTrim(Constantes.TAG_AMOUNT), nroDecimales));
                    
                    if(beanaccount.getM_AccountCode()!=null && beanaccount.getM_AccountCode().trim().length()>12){
                        int longitud = beanaccount.getM_AccountCode().trim().length();
                        beanaccount.setM_AccountCode(beanaccount.getM_AccountCode().trim().substring(longitud-12));
                    }
                    
                    listaAccount.put(beanaccount.getM_AccountCode(), beanaccount);
                }
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return listaAccount;
    }

    //jwong para manejo del cambio de clave
    public static BeanCambioClave obtenerConfirmacionCambioClave(Element elemRaiz) {
        BeanCambioClave beanXML = null;
        try {
            beanXML = new BeanCambioClave();
            Element nodoChange = (Element) elemRaiz.getChildren("cambioClave").get(0);

            String confirmacion = nodoChange.getChild("confirmacion").getTextTrim();

            beanXML.setM_CodigoError(confirmacion);
            if (!"00".equals(confirmacion)) {
                beanXML.setM_Mensaje(nodoChange.getChild("descripcion").getTextTrim());
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }

    //jwong 16/01/2009 para el manejo de la consulta de historico de movimientos
    public static BeanConsMovHistoricosXML listarHistoricoMovimientos(Element elemRaiz, String formatoFechaIN, 
            String formatoHoraIN, int send_number) {
        BeanConsMovHistoricosXML beanXML = null;
        BeanDetalleMovimiento beanDetalleMov = null;
        ArrayList listaMov = null;

        String nroDec = null;
        int nroDecimales = 0;

        try {
            //obtenemos los datos de la cuenta
            beanXML = new BeanConsMovHistoricosXML();
            beanXML.setM_Titular(elemRaiz.getChildTextTrim("client_name"));
            beanXML.setM_Moneda(elemRaiz.getChildTextTrim("currency"));
            beanXML.setM_Cuenta(elemRaiz.getChildTextTrim("")); //se llenara en el action

            try {
                //obtenemos el numero de decimales para el saldo disponible
                nroDec = elemRaiz.getChild("available_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoDisponible(Util.formatearMontoConComa(elemRaiz.getChildTextTrim("available_balance"), nroDecimales));
            beanXML.setM_SignoDisponible(elemRaiz.getChildTextTrim("sign_available_balance"));

            try {
                //obtenemos el numero de decimales para el saldo retenido
                nroDec = elemRaiz.getChild("retained_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoRetenido(Util.formatearMontoConComa(elemRaiz.getChildTextTrim("retained_balance"), nroDecimales));
            beanXML.setM_SignoRetenido(elemRaiz.getChildTextTrim("sign_retained_balance"));

            try {
                //obtenemos el numero de decimales para el saldo contable
                nroDec = elemRaiz.getChild("countable_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoContable(Util.formatearMontoConComa(elemRaiz.getChildTextTrim("countable_balance"), nroDecimales));
            beanXML.setM_SignoContable(elemRaiz.getChildTextTrim("sign_countable_balance"));

            beanXML.setM_Fecha(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", elemRaiz.getChildTextTrim("process_date")));
            beanXML.setM_Hora(Fecha.formatearFecha("HHmm", "HH:mm:ss", elemRaiz.getChildTextTrim("process_time")));

            Element nodoOdat1R = (Element) elemRaiz.getChildren("movements").get(0);
            List lista = nodoOdat1R.getChildren("movimiento");
            if (lista != null && lista.size() > 0) {
                listaMov = new ArrayList();
                 if(lista.size() < send_number){
                    send_number = lista.size();
                }
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < send_number; i++) {
                    Element nodo = (Element) lista.get(i);
                    beanDetalleMov = new BeanDetalleMovimiento();
                    beanDetalleMov.setM_Fecha(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim("movement_date")));
                    beanDetalleMov.setM_Hora(Fecha.formatearFecha(formatoHoraIN, "HH:mm:ss", nodo.getChildTextTrim("movement_hour")));
                    beanDetalleMov.setM_TipoTrx(nodo.getChildTextTrim("movement_type"));
                    beanDetalleMov.setM_Referencia(nodo.getChildTextTrim("reference"));
                    beanDetalleMov.setM_Moneda(nodo.getChildTextTrim("currency"));

                    try {
                        //obtenemos el numero de decimales para el amount
                        nroDec = nodo.getChild("amount").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanDetalleMov.setM_Importe(Util.formatearMontoConComa(nodo.getChildTextTrim("amount"), nroDecimales));

                    //tampoco tiene signo el detalle del movimiento
                    //beanDetalleMov.setM_Signo("");

                    //Debemos colocarle la descripcion del tipo de movimiento
                    if ("DEB".equalsIgnoreCase(beanDetalleMov.getM_TipoTrx())) {
                        beanDetalleMov.setM_Signo("-");
                        beanDetalleMov.setM_TipoTrx("Débito");
                    } else if ("CRE".equalsIgnoreCase(beanDetalleMov.getM_TipoTrx())) {
                        beanDetalleMov.setM_Signo("+");
                        beanDetalleMov.setM_TipoTrx("Crédito");
                    }
                    beanDetalleMov.setM_Descripcion(nodo.getChildTextTrim("movement_description"));

                    listaMov.add(beanDetalleMov);
                }
                //añadimos la lista al bean del detalle del movimiento
                beanXML.setM_Movimientos(listaMov);
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }

    public static BeanConsMovHistoricosXML listarHistoricoMovimientosPag(Element elemRaiz, String formatoFechaIN, String formatoHoraIN,
            CashClientWS cashclienteWS, int total_number, int send_number,
            String m_TipoCuenta, String m_Cuenta, String m_TipoInformacion, String m_FecInicio, String m_FecFin, String ruc_empresa) {

        BeanConsMovHistoricosXML beanXML = null;
        ArrayList listaMov = null;
        String nroDec = null;
        int nroDecimales = 0;
        int secuencial = 0;
        try {
            //obtenemos los datos de la cuenta
            beanXML = new BeanConsMovHistoricosXML();
            beanXML.setM_Titular(elemRaiz.getChildTextTrim("client_name"));
            beanXML.setM_Moneda(elemRaiz.getChildTextTrim("currency"));
            beanXML.setM_Cuenta(elemRaiz.getChildTextTrim("")); //se llenara en el action

            try {
                //obtenemos el numero de decimales para el saldo disponible
                nroDec = elemRaiz.getChild("available_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoDisponible(Util.formatearMontoConComa(elemRaiz.getChildTextTrim("available_balance"), nroDecimales));
            beanXML.setM_SignoDisponible(elemRaiz.getChildTextTrim("sign_available_balance"));

            try {
                //obtenemos el numero de decimales para el saldo retenido
                nroDec = elemRaiz.getChild("retained_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoRetenido(Util.formatearMontoConComa(elemRaiz.getChildTextTrim("retained_balance"), nroDecimales));
            beanXML.setM_SignoRetenido(elemRaiz.getChildTextTrim("sign_retained_balance"));

            try {
                //obtenemos el numero de decimales para el saldo contable
                nroDec = elemRaiz.getChild("countable_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoContable(Util.formatearMontoConComa(elemRaiz.getChildTextTrim("countable_balance"), nroDecimales));
            beanXML.setM_SignoContable(elemRaiz.getChildTextTrim("sign_countable_balance"));

            beanXML.setM_Fecha(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", elemRaiz.getChildTextTrim("process_date")));
            beanXML.setM_Hora(Fecha.formatearFecha("HHmm", "HH:mm:ss", elemRaiz.getChildTextTrim("process_time")));

            Element nodoOdat1R = (Element) elemRaiz.getChildren("movements").get(0);
            List lista = nodoOdat1R.getChildren("movimiento");
            if (lista != null && lista.size() > 0) {
                listaMov = new ArrayList();
                parseHistoricoMovimientos(elemRaiz, listaMov, formatoFechaIN, formatoHoraIN);
                secuencial = secuencial + send_number;
                while (total_number > secuencial) {
                    ArrayList listaParametros = new ArrayList();
                    BeanNodoXML beanNodo = null;
                    beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_MOV_HISTORICOS); //id de la transaccion
                    listaParametros.add(beanNodo);
                    beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT_TYPE, m_TipoCuenta); //tipo de cuenta
                    listaParametros.add(beanNodo);
                    beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT, m_Cuenta); //cuenta
                    listaParametros.add(beanNodo);
                    beanNodo = new BeanNodoXML(Constantes.TAG_QUERY_TYPE, m_TipoInformacion); //tipo de informacion(Cargo o Abono)
                    listaParametros.add(beanNodo);
                    beanNodo = new BeanNodoXML(Constantes.TAG_BEGIN_DATE, Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy", m_FecInicio)); //fecha inicio
                    listaParametros.add(beanNodo);
                    beanNodo = new BeanNodoXML(Constantes.TAG_END_DATE, Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy", m_FecFin)); //fecha fin
                    listaParametros.add(beanNodo);
                    beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
                    listaParametros.add(beanNodo);
                    String num_movimiento = String.valueOf(secuencial);
                    while (num_movimiento.length() < 10) {
                        num_movimiento = "0" + num_movimiento;
                    }
                    listaParametros.add(new BeanNodoXML("id_movement", num_movimiento));
                    String resultado = null;
                    String req = GenRequestXML.getXML(listaParametros);
                    resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
                    if (resultado != null && !"".equals(resultado)) {
                        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
                        if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                            parseHistoricoMovimientos(beanResXML.getM_Respuesta(), listaMov, formatoFechaIN, formatoHoraIN);
                            send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
                            secuencial = secuencial + send_number;
                        } else {
                            secuencial = total_number;
                        }
                    } else {
                        secuencial = total_number;
                    }
                }
            }
            beanXML.setM_Movimientos(listaMov);
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }

    public static void parseHistoricoMovimientos(Element elemRaiz,List listaMov,String formatoFechaIN,String formatoHoraIN){
        BeanDetalleMovimiento beanDetalleMov = null;
        String nroDec = null;
        int nroDecimales = 0;
        Element nodoOdat1R = (Element) elemRaiz.getChildren("movements").get(0);
        List lista = nodoOdat1R.getChildren("movimiento");
        if (lista != null && lista.size() > 0) {

            for (int i = 0; i < lista.size(); i++) {
                Element nodo = (Element) lista.get(i);
                beanDetalleMov = new BeanDetalleMovimiento();
                beanDetalleMov.setM_Fecha(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim("movement_date")));
                beanDetalleMov.setM_Hora(Fecha.formatearFecha(formatoHoraIN, "HH:mm:ss", nodo.getChildTextTrim("movement_hour")));
                beanDetalleMov.setM_TipoTrx(nodo.getChildTextTrim("movement_type"));
                beanDetalleMov.setM_Referencia(nodo.getChildTextTrim("reference"));
                beanDetalleMov.setM_Moneda(nodo.getChildTextTrim("currency"));

                try {
                    //obtenemos el numero de decimales para el amount
                    nroDec = nodo.getChild("amount").getAttributeValue("decimal");
                    nroDecimales = Integer.parseInt(nroDec);
                } catch (Exception exc) {
                    nroDecimales = 0;
                }
                beanDetalleMov.setM_Importe(Util.formatearMontoConComa(nodo.getChildTextTrim("amount"), nroDecimales));

                //tampoco tiene signo el detalle del movimiento
                //beanDetalleMov.setM_Signo("");

                //Debemos colocarle la descripcion del tipo de movimiento
                if ("DEB".equalsIgnoreCase(beanDetalleMov.getM_TipoTrx())) {
                    beanDetalleMov.setM_Signo("-");
                    beanDetalleMov.setM_TipoTrx("Débito");
                } else if ("CRE".equalsIgnoreCase(beanDetalleMov.getM_TipoTrx())) {
                    beanDetalleMov.setM_Signo("+");
                    beanDetalleMov.setM_TipoTrx("Crédito");
                }
                beanDetalleMov.setM_Descripcion(nodo.getChildTextTrim("movement_description"));

                listaMov.add(beanDetalleMov);
            }
        }
    }
    //jwong 16/01/2009 para el manejo de la consulta de ultimos movimientos
    public static BeanConsMovHistoricosXML listarUltimosMovimientos(Element elemRaiz, /*int nroDecimales,*/ String formatoFechaIN, String formatoHoraIN) {
        BeanConsMovHistoricosXML beanXML = null;
        BeanDetalleMovimiento beanDetalleMov = null;
        ArrayList listaMov = null;

        String nroDec = null;
        int nroDecimales = 0;

        try {
            //obtenemos los datos de la cuenta
            beanXML = new BeanConsMovHistoricosXML();
            beanXML.setM_Titular(elemRaiz.getChildTextTrim("client_name"));
            beanXML.setM_Moneda(elemRaiz.getChildTextTrim("currency"));
            beanXML.setM_Cuenta(elemRaiz.getChildTextTrim("")); //se llenara en el action

            try {
                //obtenemos el numero de decimales para el saldo disponible
                nroDec = elemRaiz.getChild("available_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoDisponible(Util.formatearMontoConComa(elemRaiz.getChildTextTrim("available_balance"), nroDecimales));
            beanXML.setM_SignoDisponible(elemRaiz.getChildTextTrim("sign_available_balance"));

            try {
                //obtenemos el numero de decimales para el saldo contable
                nroDec = elemRaiz.getChild("countable_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoContable(Util.formatearMontoConComa(elemRaiz.getChildTextTrim("countable_balance"), nroDecimales));
            beanXML.setM_SignoContable(elemRaiz.getChildTextTrim("sign_countable_balance"));

            try {
                //obtenemos el numero de decimales para el saldo anterior
                nroDec = elemRaiz.getChild("previous_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoAnterior(Util.formatearMontoConComa(elemRaiz.getChildTextTrim("previous_balance"), nroDecimales));
            beanXML.setM_SignoContable(elemRaiz.getChildTextTrim("sign_previous_balance"));

            beanXML.setM_Fecha(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yy", elemRaiz.getChildTextTrim("process_date")));
            beanXML.setM_Hora(Fecha.formatearFecha(formatoHoraIN, "HH:mm:ss", elemRaiz.getChildTextTrim("process_time")));

            Element nodoOdat1R = (Element) elemRaiz.getChildren("movements").get(0);
            List lista = nodoOdat1R.getChildren("movimiento");
            if (lista != null && lista.size() > 0) {
                listaMov = new ArrayList();
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < lista.size(); i++) {
                    Element nodo = (Element) lista.get(i);
                    beanDetalleMov = new BeanDetalleMovimiento();


                    beanDetalleMov.setM_Fecha(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yy", nodo.getChildTextTrim("movement_date")));
                    beanDetalleMov.setM_Hora(Fecha.formatearFecha("HHmm", "HH:mm:ss", nodo.getChildTextTrim("movement_hour")));
//                    beanDetalleMov.setM_TipoTrx(nodo.getChildTextTrim("movement_type"));
//                    beanDetalleMov.setM_Referencia(nodo.getChildTextTrim("reference"));
//                    beanDetalleMov.setM_Moneda(nodo.getChildTextTrim("currency"));
                    //sign

                    try {
                        //obtenemos el numero de decimales para el amount
                        nroDec = nodo.getChild("amount").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanDetalleMov.setM_Importe(Util.formatearMontoConComa(nodo.getChildTextTrim("amount"), nroDecimales));

                    beanDetalleMov.setM_Signo(nodo.getChildTextTrim("sign")); //signo del importe

                    beanDetalleMov.setM_Descripcion(nodo.getChildTextTrim("movement_description"));

                    //debemos indicar el tipo de movimiento
                    if ("-".equals(beanDetalleMov.getM_Signo())) {
                        beanDetalleMov.setM_TipoTrx("Débito");
                    } else {
                        beanDetalleMov.setM_TipoTrx("Crédito");
                    }
                    listaMov.add(beanDetalleMov);
                }
                //añadimos la lista al bean del detalle del movimiento
                beanXML.setM_Movimientos(listaMov);
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }
    //jwong 09/02/2009 para manejar data de respuesta del webservice del login

    public static BeanRespuestaWSLoginXML parsearRespuestaWSLogin(String xml) {
        BeanRespuestaWSLoginXML beanXML = null;
        String m_CodRptaTx = null;
        String m_DescRptaTx = null;
        String m_Token = null;
        try {
            Document doc = parse(xml, false);
            Element elemRaiz = doc.getRootElement();                //  elemento raiz del archivo xml

            //List lista = elemRaiz.getChildren("return");
            Element nodoValida = (Element) (elemRaiz.getChildren().get(0));

            //EN HIPER***********************************************************
            //obtenemos los nodos conteniendo la data de respuesta

            /*m_CodRptaTx = nodoValida.getChildTextTrim("CodRptaTx");
            m_DescRptaTx = nodoValida.getChildTextTrim("DescRptaTx");
            if(m_CodRptaTx!=null && Constantes.IBS_CODE_OK_LOGIN.equals(m_CodRptaTx)){
            m_Token = nodoValida.getChildTextTrim("Token");
            }
            */
            //********************************************************************

            //EN FINANCIERO******************************************************
            //diffgr

            Element el1 = (Element) nodoValida.getChildren().get(1);
            //DocumentElemnet
            Element el2 = (Element) el1.getChildren().get(0);
            //Table ValidarLogin
            Element el3 = (Element) el2.getChildren().get(0);

            //obtenemos los nodos conteniendo la data de respuesta
            //m_CodRptaTx = nodoValida.getChildTextTrim("CodRptaTx");
            //m_DescRptaTx = nodoValida.getChildTextTrim("DescRptaTx");
            m_CodRptaTx = el3.getChildTextTrim("CodRptaTx");
            //m_DescRptaTx = el3.getChildTextTrim("DescRptaTx");
            m_DescRptaTx = el3.getChildTextTrim("DescRptaTx");

            if (m_CodRptaTx != null && Constantes.IBS_CODE_OK_LOGIN.equals(m_CodRptaTx)) {
                m_Token = el3.getChildTextTrim("Token");
            }

            //******************************************************************
            beanXML = new BeanRespuestaWSLoginXML();
            beanXML.setM_CodRptaTx(m_CodRptaTx);
            beanXML.setM_DescRptaTx(m_DescRptaTx);
            beanXML.setM_Token(m_Token);
        } catch (IOException e) {
        	 logger.error(e.toString(),e);     
            return null;
        } catch (org.jdom.JDOMException e) {
        	 logger.error(e.toString(),e);     
            return null;
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }
    //jmoreno 23/09/2009
    public static BeanConsMovHistoricosXML listarEstadoCuentasPag(Element elemRaiz, String formatoFechaIN, String formatoHoraIN,
            int total_number,int send_number,CashClientWS cashclienteWS,String m_Cuenta,String m_TrxDate,String ruc_empresa) {

        BeanConsMovHistoricosXML beanXML = null;
        ArrayList listaMov = null;
        String nroDec = null;
        int nroDecimales = 0;
        int secuencial = 0;
        try {

            Element nodoOdat1F = (Element) elemRaiz.getChild("odat1F");
            //obtenemos los datos de la cuenta
            beanXML = new BeanConsMovHistoricosXML();
            beanXML.setM_Titular(nodoOdat1F.getChildTextTrim("client_name"));
            beanXML.setM_Moneda(nodoOdat1F.getChildTextTrim("currency"));            
            beanXML.setM_Cuenta(nodoOdat1F.getChildTextTrim("account_description")); //tipo de cuenta

            try {
                //obtenemos el numero de decimales para el saldo disponible
                nroDec = nodoOdat1F.getChild("available_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception e) {
            	 logger.error(e.toString(),e);     
                nroDecimales = 0;
            }
            beanXML.setM_SaldoDisponible(Util.formatearMontoConComa(nodoOdat1F.getChildTextTrim("available_balance"), nroDecimales));

            try {
                //obtenemos el numero de decimales para el saldo retenido
                nroDec = nodoOdat1F.getChild("retained_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception e) {
            	 logger.error(e.toString(),e);     
                nroDecimales = 0;
            }
            beanXML.setM_SaldoRetenido(Util.formatearMontoConComa(nodoOdat1F.getChildTextTrim("retained_balance"), nroDecimales));

            try {
                //obtenemos el numero de decimales para el saldo contable
                nroDec = nodoOdat1F.getChild("countable_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception e) {
            	 logger.error(e.toString(),e);     
                nroDecimales = 0;
            }
            beanXML.setM_SaldoContable(Util.formatearMontoConComa(nodoOdat1F.getChildTextTrim("countable_balance"), nroDecimales));
            beanXML.setM_Estado(nodoOdat1F.getChildTextTrim("status"));

            try {
                //obtenemos el numero de decimales para el query_balance
                nroDec = nodoOdat1F.getChild("query_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception e) {
            	 logger.error(e.toString(),e);     
                nroDecimales = 0;
            }
            beanXML.setM_SaldoConsulta(Util.formatearMontoConComa(nodoOdat1F.getChildTextTrim("query_balance"), nroDecimales));
            beanXML.setM_Interes(nodoOdat1F.getChildTextTrim("interest"));

            try {
                //obtenemos el numero de decimales para el query_balance
                nroDec = nodoOdat1F.getChild("balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception e) {
            	 logger.error(e.toString(),e);     
                nroDecimales = 0;
            }
            beanXML.setM_Saldo(Util.formatearMontoConComa(nodoOdat1F.getChildTextTrim("balance"), nroDecimales));
            beanXML.setM_LineaAdministrada(nodoOdat1F.getChildTextTrim("adm_line"));
            beanXML.setM_FechaSaldoIni(nodoOdat1F.getChildTextTrim("initial_balance_date"));
            beanXML.setM_FechaSaldoFin(nodoOdat1F.getChildTextTrim("final_balance_date"));

            try {
                //obtenemos el numero de decimales para el initial_balance
                nroDec = nodoOdat1F.getChild("initial_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception e) {
            	 logger.error(e.toString(),e);     
                nroDecimales = 0;
            }
            beanXML.setM_SaldoInicial(nodoOdat1F.getChildTextTrim("initial_balance"));

            try {
                //obtenemos el numero de decimales para el final_balance
                nroDec = nodoOdat1F.getChild("final_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception e) {
            	 logger.error(e.toString(),e);     
                nroDecimales = 0;
            }
            beanXML.setM_SaldoFinal(nodoOdat1F.getChildTextTrim("final_balance"));

            Element nodoOdat1R = (Element) elemRaiz.getChildren("odat1R").get(0);
            List lista = nodoOdat1R.getChildren("movimiento");
            if (lista != null && lista.size() > 0) {
                listaMov = new ArrayList();
                parserEstadoCuenta(elemRaiz, listaMov, formatoFechaIN, formatoHoraIN,send_number);
                secuencial = secuencial + send_number;
                while (total_number > secuencial) {

                    ArrayList listaParametros = new ArrayList();
                    BeanNodoXML beanNodo = null;
                    beanNodo = new BeanNodoXML("id_trx", Constantes.IBS_CONS_EST_CUENTA); //id de la transaccion
                    listaParametros.add(beanNodo);

                    beanNodo = new BeanNodoXML(Constantes.TAG_ACCOUNT_NUMBER, m_Cuenta); //numero de cuenta
                    listaParametros.add(beanNodo);

                    beanNodo = new BeanNodoXML(Constantes.TAG_TRANSACTION_DATE, m_TrxDate); //yyMMdd
                    listaParametros.add(beanNodo);

                    beanNodo = new BeanNodoXML(Constantes.TAG_RUC, ruc_empresa); //RUC de la empresa
                    listaParametros.add(beanNodo);

                    String numSecuencial = String.valueOf(secuencial);
                    while (numSecuencial.length() < 4) {
                        numSecuencial = "0" + numSecuencial;
                    }
                    //secuencial que se usa en la paginacion de esta consulta
                    beanNodo = new BeanNodoXML(Constantes.TAG_SECUENCIAL, numSecuencial); 
                    listaParametros.add(beanNodo);
                    String resultado = null;
                    String req = GenRequestXML.getXML(listaParametros);
                    resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
                    if (resultado != null && !"".equals(resultado)) {
                        BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
                        if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                            send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("send_number"));
                            parserEstadoCuenta(beanResXML.getM_Respuesta(), listaMov, formatoFechaIN, formatoHoraIN,send_number);
                            secuencial = secuencial + send_number;
                        } else {
                            secuencial = total_number;
                        }
                    } else {
                        secuencial = total_number;
                    }

                }

            }
            beanXML.setM_Movimientos(listaMov);
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }

    //jmoreno 23/09/2009
    public static void parserEstadoCuenta(Element elemRaiz,List listaMov,String formatoFechaIN,String formatoHoraIN,
            int sendNumber){

            Element nodoOdat1R = (Element) elemRaiz.getChildren("odat1R").get(0);
            List lista = nodoOdat1R.getChildren("movimiento");
            BeanDetalleMovimiento beanDetalleMov = null;
            String nroDec = null;
            int nroDecimales = 0;
            if (lista != null && lista.size() > 0) {
                if(sendNumber > lista.size()){
                    sendNumber = lista.size();
                }
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < sendNumber; i++) {
                    Element nodo = (Element) lista.get(i);

                    beanDetalleMov = new BeanDetalleMovimiento();
                    beanDetalleMov.setM_Fecha(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim("movement_date")));
                    beanDetalleMov.setM_Hora(Fecha.formatearFecha(formatoHoraIN, "HH:mm:ss", nodo.getChildTextTrim("movement_hour")));
                    beanDetalleMov.setM_Descripcion(nodo.getChildTextTrim("movement_x1"));
                    beanDetalleMov.setM_Referencia(nodo.getChildTextTrim("movement_reference"));
                    beanDetalleMov.setM_Indicador(nodo.getChildTextTrim("movement_indicator"));

                    try {
                        //obtenemos el numero de decimales para el movement_amount
                        nroDec = nodo.getChild("movement_amount").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanDetalleMov.setM_Importe(Util.formatearMontoConComa(nodo.getChildTextTrim("movement_amount"), nroDecimales));
                    if(beanDetalleMov.getM_Indicador()!=null && beanDetalleMov.getM_Indicador().length()>0){
                        int indica = 0;
                        try{
                            indica = Integer.parseInt(beanDetalleMov.getM_Indicador());
                        }catch(NumberFormatException nfe){
                            nfe.printStackTrace();
                            indica = 0;
                        }
                        if(indica==1) {
                            beanDetalleMov.setM_TipoTrx("Débito");
                            beanDetalleMov.setM_Importe("-" + beanDetalleMov.getM_Importe());
                        }else if(indica==2){
                            beanDetalleMov.setM_TipoTrx("Crédito");
                        }
                        else{
                            beanDetalleMov.setM_TipoTrx(" - ");
                        }
                    }
                    else{
                        beanDetalleMov.setM_TipoTrx(" - ");
                    }

                    try {
                        //obtenemos el numero de decimales para el movement_balance
                        nroDec = nodo.getChild("movement_balance").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception e) {
                    	 logger.error(e.toString(),e);     
                        nroDecimales = 0;
                    }
                    beanDetalleMov.setM_SaldoMovimiento(Util.formatearMontoConComa(nodo.getChildTextTrim("movement_balance"), nroDecimales));
                    listaMov.add(beanDetalleMov);
                }

            }
    }
    
    //jwong 19/02/2009 manejo de respuesta de consulta de saldos promedios(Estados de cuenta)
    public static BeanConsMovHistoricosXML listarSaldosPromedios(Element elemRaiz,String formatoFechaIN, 
            String formatoHoraIN,int sendNumber) {
        BeanConsMovHistoricosXML beanXML = null;
        BeanDetalleMovimiento beanDetalleMov = null;
        ArrayList listaMov = null;

        String nroDec = null;
        int nroDecimales = 0;
        try {

            Element nodoOdat1F = (Element) elemRaiz.getChild("odat1F");

            //obtenemos los datos de la cuenta
            beanXML = new BeanConsMovHistoricosXML();
            beanXML.setM_Titular(nodoOdat1F.getChildTextTrim("client_name"));
            beanXML.setM_Moneda(nodoOdat1F.getChildTextTrim("currency"));
//            beanXML.setM_Cuenta(nodoOdat1F.getChildTextTrim("")); //se llenara en el action
            beanXML.setM_Cuenta(nodoOdat1F.getChildTextTrim("account_description")); //tipo de cuenta

            try {
                //obtenemos el numero de decimales para el saldo disponible
                nroDec = nodoOdat1F.getChild("available_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoDisponible(Util.formatearMontoConComa(nodoOdat1F.getChildTextTrim("available_balance"), nroDecimales));
            
            try {
                //obtenemos el numero de decimales para el saldo retenido
                nroDec = nodoOdat1F.getChild("retained_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoRetenido(Util.formatearMontoConComa(nodoOdat1F.getChildTextTrim("retained_balance"), nroDecimales));
            
            try {
                //obtenemos el numero de decimales para el saldo contable
                nroDec = nodoOdat1F.getChild("countable_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoContable(Util.formatearMontoConComa(nodoOdat1F.getChildTextTrim("countable_balance"), nroDecimales));
            //beanXML.setM_SignoContable(elemRaiz.getChildTextTrim("sign_countable_balance"));

            //beanXML.setM_Fecha(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", elemRaiz.getChildTextTrim("process_date")));
            //beanXML.setM_Hora(Fecha.formatearFecha("HHmm", "HH:mm:ss", elemRaiz.getChildTextTrim("process_time")));

            //jwong 19/02/2009 campos adicionales en consulta de saldos promedios
            beanXML.setM_Estado(nodoOdat1F.getChildTextTrim("status"));

            try {
                //obtenemos el numero de decimales para el query_balance
                nroDec = nodoOdat1F.getChild("query_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoConsulta(Util.formatearMontoConComa(nodoOdat1F.getChildTextTrim("query_balance"), nroDecimales));

            beanXML.setM_Interes(nodoOdat1F.getChildTextTrim("interest"));

            try {
                //obtenemos el numero de decimales para el query_balance
                nroDec = nodoOdat1F.getChild("balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_Saldo(Util.formatearMontoConComa(nodoOdat1F.getChildTextTrim("balance"), nroDecimales));

            beanXML.setM_LineaAdministrada(nodoOdat1F.getChildTextTrim("adm_line"));
            beanXML.setM_FechaSaldoIni(nodoOdat1F.getChildTextTrim("initial_balance_date"));
            beanXML.setM_FechaSaldoFin(nodoOdat1F.getChildTextTrim("final_balance_date"));

            try {
                //obtenemos el numero de decimales para el initial_balance
                nroDec = nodoOdat1F.getChild("initial_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoInicial(nodoOdat1F.getChildTextTrim("initial_balance"));

            try {
                //obtenemos el numero de decimales para el final_balance
                nroDec = nodoOdat1F.getChild("final_balance").getAttributeValue("decimal");
                nroDecimales = Integer.parseInt(nroDec);
            } catch (Exception exc) {
                nroDecimales = 0;
            }
            beanXML.setM_SaldoFinal(nodoOdat1F.getChildTextTrim("final_balance"));

            Element nodoOdat1R = (Element) elemRaiz.getChildren("odat1R").get(0);
            List lista = nodoOdat1R.getChildren("movimiento");
            if (lista != null && lista.size() > 0) {
                listaMov = new ArrayList();
                if(lista.size() < sendNumber){
                    sendNumber = lista.size();
                }
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < sendNumber; i++) {
                    Element nodo = (Element) lista.get(i);

                    beanDetalleMov = new BeanDetalleMovimiento();
                    //beanDetalleMov.setM_Fecha(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim("movement_date1")));
                    beanDetalleMov.setM_Fecha(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim("movement_date")));
                    beanDetalleMov.setM_Hora(Fecha.formatearFecha(formatoHoraIN, "HH:mm:ss", nodo.getChildTextTrim("movement_hour")));
                    beanDetalleMov.setM_Descripcion(nodo.getChildTextTrim("movement_x1"));

                    //beanDetalleMov.setM_TipoTrx(nodo.getChildTextTrim("movement_type"));
                    
                    beanDetalleMov.setM_Referencia(nodo.getChildTextTrim("movement_reference"));
                    beanDetalleMov.setM_Indicador(nodo.getChildTextTrim("movement_indicator"));
                    //beanDetalleMov.setM_Moneda(nodo.getChildTextTrim("currency"));

                    try {
                        //obtenemos el numero de decimales para el movement_amount
                        nroDec = nodo.getChild("movement_amount").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanDetalleMov.setM_Importe(Util.formatearMontoConComa(nodo.getChildTextTrim("movement_amount"), nroDecimales));
                    //03/08/2009 INDICADOR
                    /*
                    if(beanDetalleMov.getM_Importe()!=null && beanDetalleMov.getM_Importe().length()>0){
                        if ("-".equals(beanDetalleMov.getM_Importe().trim().substring(0, 1))) {
                            beanDetalleMov.setM_TipoTrx("Débito");
                        } else {
                            beanDetalleMov.setM_TipoTrx("Crédito");
                        }
                    }
                    else{
                        beanDetalleMov.setM_TipoTrx(" - ");
                    }
                    */
                    if(beanDetalleMov.getM_Indicador()!=null && beanDetalleMov.getM_Indicador().length()>0){
                        int indica = 0;
                        try{
                            indica = Integer.parseInt(beanDetalleMov.getM_Indicador());
                        }catch(NumberFormatException nfe){
                            nfe.printStackTrace();
                            indica = 0;
                        }
                        if(indica==1) {
                            beanDetalleMov.setM_TipoTrx("Débito");
                            beanDetalleMov.setM_Importe("-" + beanDetalleMov.getM_Importe());
                        }else if(indica==2){
                            beanDetalleMov.setM_TipoTrx("Crédito");
                        }
                        else{
                            beanDetalleMov.setM_TipoTrx(" - ");
                        }
                    }
                    else{
                        beanDetalleMov.setM_TipoTrx(" - ");
                    }
                    
                    
                    try {
                        //obtenemos el numero de decimales para el movement_balance
                        nroDec = nodo.getChild("movement_balance").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanDetalleMov.setM_SaldoMovimiento(Util.formatearMontoConComa(nodo.getChildTextTrim("movement_balance"), nroDecimales));

                    listaMov.add(beanDetalleMov);
                }

                //añadimos la lista al bean del detalle del movimiento
                beanXML.setM_Movimientos(listaMov);
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }

    //esilva
    //Empresas de Servicio
    public static List parsearRespuestaWSHomeBanking(String xml) {
        ArrayList listaResult = null;
        BeanRespuestaWSHomeBankingXML beanXML = null;

        try {
            Document doc = parse(xml, false);
            Element elemRaiz = doc.getRootElement();
            Element nodoResult = (Element) (elemRaiz.getChildren().get(0));
            Element nodoDiffgr = (Element) nodoResult.getChildren().get(1);
            Element nodoDataSet = (Element) nodoDiffgr.getChildren(Constantes.TAG_CEWS_NODODATASET).get(0);
            List lProveedor = nodoDataSet.getChildren(Constantes.TAG_CEWS_PROVEEDOR);

            if (lProveedor != null && lProveedor.size() > 0) {
                listaResult = new ArrayList();
                for (int i = 0; i < lProveedor.size(); i++) {
                    Element nodo = (Element) lProveedor.get(i);
                    beanXML = new BeanRespuestaWSHomeBankingXML();
                    beanXML.setM_Codigo(nodo.getChildTextTrim(Constantes.TAG_CEWS_INTTABLAID)); //codigo de empresa
                    beanXML.setM_Nombre(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRDESCRIPCION)); //nombre de la empresa
                    beanXML.setM_CodigoServicio(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRCODIGOSERVICIO));
                    /*if (nodo.getChildTextTrim(Constantes.TAG_CEWS_STRRUC)!= null){//Por el cambio del Ruc por el CodCliente
                        beanXML.setM_Ruc(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRRUC));
                    }*/
                    beanXML.setM_Ruc(Util.completarCampo(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRCODCASH), 9,"0",0));
                    listaResult.add(beanXML);
                }
            }
        } catch (IOException e) {
        	 logger.error(e.toString(),e);     
            return null;
        } catch (org.jdom.JDOMException e) {
        	 logger.error(e.toString(),e);     
            return null;
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return listaResult;
    }
    //Servicios Ofrecidos por las Empresas de Servicio

    public static List parsearRespuestaWSHomeBanking2(String xml) {
        ArrayList listaResult = null;
        BeanRespuestaWSHomeBankingXML beanXML = null;

        try {
            Document doc = parse(xml, false);
            Element elemRaiz = doc.getRootElement();
            Element nodoResult = (Element) (elemRaiz.getChildren().get(0));
            Element nodoDiffgr = (Element) nodoResult.getChildren().get(1);
            Element nodoDataSet = (Element) nodoDiffgr.getChildren(Constantes.TAG_CEWS_NODODATASET).get(0);
            List lServicio = nodoDataSet.getChildren(Constantes.TAG_CEWS_SERVICIOS);

            if (lServicio != null && lServicio.size() > 0) {
                listaResult = new ArrayList();
                for (int i = 0; i < lServicio.size(); i++) {
                    Element nodo = (Element) lServicio.get(i);
                    beanXML = new BeanRespuestaWSHomeBankingXML();

                    beanXML.setM_Grupo(nodo.getChildTextTrim(Constantes.TAG_CEWS_INTTABLAID));
                    beanXML.setM_Nombre(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRDESCRIPCION));
                    beanXML.setM_SectorVisible(nodo.getChildTextTrim(Constantes.TAG_CEWS_BLNSECTORVISIBLE));
                    beanXML.setM_LabelCodigoServicio(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRTAG));
                    beanXML.setM_Codigo(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRCODIGOSERVICIO));
                    beanXML.setM_DDNVisible(nodo.getChildTextTrim(Constantes.TAG_CEWS_BLNDDNVISIBLE));
                    beanXML.setM_Modo(nodo.getChildTextTrim(Constantes.TAG_CEWS_MODO));
                    listaResult.add(beanXML);
                }
            }
        } catch (IOException e) {
        	 logger.error(e.toString(),e);     
            return null;
        } catch (org.jdom.JDOMException e) {
        	 logger.error(e.toString(),e);     
            return null;
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return listaResult;
    }
    //Servicios Ofrecidos por las Empresas de Servicio

    public static List parsearRespuestaWSHomeBanking3(String xml, String tipoEntidad) {
        ArrayList listaResult = null;
        BeanRespuestaWSHomeBankingXML beanXML = null;

        try {
            Document doc = parse(xml, false);
            Element elemRaiz = doc.getRootElement();
            Element nodoResult = (Element) (elemRaiz.getChildren().get(0));
            Element nodoDiffgr = (Element) nodoResult.getChildren().get(1);
            Element nodoDataSet = (Element) nodoDiffgr.getChildren(Constantes.TAG_CEWS_NODODATASET).get(0);
            List lServicio = nodoDataSet.getChildren(Constantes.TAG_CEWS_SERVICIOS);

            if (lServicio != null && lServicio.size() > 0) {
                listaResult = new ArrayList();
                for (int i = 0; i < lServicio.size(); i++) {
                    Element nodo = (Element) lServicio.get(i);
                    beanXML = new BeanRespuestaWSHomeBankingXML();

                    beanXML.setM_Codigo(nodo.getChildTextTrim(Constantes.TAG_CEWS_INTTABLAID)); //codigo de entidad
                    beanXML.setM_Nombre(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRDESCRIPCION));
                    beanXML.setM_SectorVisible(nodo.getChildTextTrim(Constantes.TAG_CEWS_BLNSECTORVISIBLE));
                    beanXML.setM_LabelCodigoServicio(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRTAG));
                    beanXML.setM_CodigoServicio(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRCODIGOSERVICIO)); //codigo de servicio
                    beanXML.setM_DDNVisible(nodo.getChildTextTrim(Constantes.TAG_CEWS_BLNDDNVISIBLE));
                    beanXML.setM_Modo(nodo.getChildTextTrim(Constantes.TAG_CEWS_MODO));

                    //jwong 18/03/2009 incluiremos ademas el tipo de entidad
                    beanXML.setM_TipoEntidad(tipoEntidad);

                    listaResult.add(beanXML);
                }
            }
        } catch (IOException e) {
        	 logger.error(e.toString(),e);     
            return null;
        } catch (org.jdom.JDOMException e) {
        	 logger.error(e.toString(),e);     
            return null;
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return listaResult;
    }
    //jmoreno
    public static BeanConsPlanLetras parseConsultarPlanilla(Element elemRaiz, String formatoFechaIN) {
        ArrayList listaLetra = null;
        BeanConsPlanLetras beanXML = null;
        BeanPlanilla beanletra = null;

        try {
            beanXML = new BeanConsPlanLetras();
            beanXML.setM_ClientCode(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_CODE));
            //System.out.println("CODIGO CLIENTE:"+beanXML.getM_ClientCode());
            beanXML.setM_ClientName(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENT_NAME));
            beanXML.setM_TotalReg(elemRaiz.getChildTextTrim(Constantes.TAG_TOTAL_REG));
            long numReg = Long.parseLong(elemRaiz.getChildTextTrim(Constantes.TAG_NUM_REG));
            beanXML.setM_NumReg(numReg + "");

            if (numReg > 0) {
                Element nodoOdat1R = (Element) elemRaiz.getChildren("odat1R").get(0);
                List lista = nodoOdat1R.getChildren(Constantes.TAG_OUTLIN);
                if (lista != null && lista.size() > 0) {
                    listaLetra = new ArrayList();
                    //obtenemos el detalle de cada una delas cuentas
                    for (int i = 0; i < lista.size(); i++) {
                        Element nodo = (Element) lista.get(i);
                        beanletra = new BeanPlanilla();
                        beanletra.setM_Cuenta(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT));
                        beanletra.setM_Moneda(nodo.getChildTextTrim(Constantes.TAG_CURRENCY));
                        beanletra.setM_Tipo(nodo.getChildTextTrim(Constantes.TAG_TIPO));
                        beanletra.setM_NomAcep(nodo.getChildTextTrim(Constantes.TAG_NAME_ACEPTANT));
                        beanletra.setM_CodAcep(beanXML.getM_ClientCode());
                        beanletra.setM_Saldo(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_SALDO), Integer.parseInt(nodo.getChild(Constantes.TAG_SALDO).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                        beanletra.setM_FecVenc(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim(Constantes.TAG_FECHA_VENC)));
                        //nodo.getChildTextTrim(Constantes.TAG_FECHA_VENC)
                        listaLetra.add(beanletra);
                    }
                    beanXML.setM_Letras(listaLetra);
                }
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }
    //jmoreno 27-08-09
    public static BeanConsPlanLetras parseConsultarPlanillaPag(Element elemRpta, String formatoFechaIN,String ruc_empresa,
            int total_number,int send_number,String fechaIni,String fechaFin,CashClientWS cashclienteWS,String tipo) {
        BeanConsPlanLetras beanXML = null;
        int secuencial = 0;
        ArrayList listaLetra = new ArrayList();
        String codigoCliente = elemRpta.getChildTextTrim(Constantes.TAG_CLIENT_CODE);
        String tipoConsulta = "";
        if ("G".equals(tipo)) {
            tipoConsulta = Constantes.IBS_CONS_PLAN_LETRAS;
        } else {
            tipoConsulta = Constantes.IBS_CONS_PLAN_LETRAS_ACEP;
        }
        try {
            beanXML = new BeanConsPlanLetras();
            beanXML.setM_ClientCode(elemRpta.getChildTextTrim(Constantes.TAG_CLIENT_CODE));            
            beanXML.setM_ClientName(elemRpta.getChildTextTrim(Constantes.TAG_CLIENT_NAME));
            beanXML.setM_TotalReg(elemRpta.getChildTextTrim(Constantes.TAG_TOTAL_REG));
            long numReg = Long.parseLong(elemRpta.getChildTextTrim(Constantes.TAG_NUM_REG));
            beanXML.setM_NumReg(numReg + "");
            if (numReg > 0) {
                parseConsPlanilla(elemRpta, listaLetra, codigoCliente, formatoFechaIN,send_number);
            }

            secuencial = secuencial + send_number;
            while (total_number > secuencial) {
                ArrayList listaParametros = new ArrayList();
                listaParametros.add(new BeanNodoXML("id_trx", tipoConsulta));
                listaParametros.add(new BeanNodoXML("doc_number", ruc_empresa));
                listaParametros.add(new BeanNodoXML("fecha_ini", Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy", fechaIni)));
                listaParametros.add(new BeanNodoXML("fecha_fin", Fecha.formatearFecha("dd/MM/yyyy", "ddMMyyyy", fechaFin)));
                String num_movimiento = String.valueOf(secuencial);
                while (num_movimiento.length() < 10) {
                    num_movimiento = "0" + num_movimiento;
                }
                listaParametros.add(new BeanNodoXML("num_mov", num_movimiento));
                String resultado = null;
                String req = GenRequestXML.getXML(listaParametros);
                resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
                if (resultado != null && !"".equals(resultado)) {
                    BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
                    if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                        
                        send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText(Constantes.TAG_NUM_REG));
                        secuencial = secuencial + send_number;
                        parseConsPlanilla(beanResXML.getM_Respuesta(), listaLetra, codigoCliente, formatoFechaIN,send_number);
                    } else {
                        secuencial = total_number;
                    }
                } else {
                    secuencial = total_number;
                }
            }
            beanXML.setM_Letras(listaLetra);
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanXML;
    }

    public static void parseConsPlanilla(Element elemRpta,List listaLetra,String codCliente,String formatoFechaIN,
            int send_number){
        BeanPlanilla beanletra = null;
        Element nodoOdat1R = (Element) elemRpta.getChildren("odat1R").get(0);
        List lista = nodoOdat1R.getChildren(Constantes.TAG_OUTLIN);
        if (lista != null && lista.size() > 0) {
            if(send_number > lista.size()){
                send_number = lista.size();
            }
            for (int i = 0; i < send_number; i++) {
                Element nodo = (Element) lista.get(i);
                beanletra = new BeanPlanilla();
                beanletra.setM_Cuenta(nodo.getChildTextTrim(Constantes.TAG_ACCOUNT));
                beanletra.setM_Moneda(nodo.getChildTextTrim(Constantes.TAG_CURRENCY));
                beanletra.setM_Tipo(nodo.getChildTextTrim(Constantes.TAG_TIPO));
                beanletra.setM_NomAcep(nodo.getChildTextTrim(Constantes.TAG_NAME_ACEPTANT));
                beanletra.setM_CodAcep(codCliente);
                beanletra.setM_Saldo(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_SALDO), Integer.parseInt(nodo.getChild(Constantes.TAG_SALDO).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                beanletra.setM_FecVenc(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim(Constantes.TAG_FECHA_VENC)));
                listaLetra.add(beanletra);
            }
        }
    }

    public static BeanConsDetPlanLetras parseConsultaDetPlanilla(Element elemRaiz, String formatoFechaIN, String moneda, String cuenta
            ,boolean isEstado,String ruc_empresa,int send_number) {
        ArrayList listaDetLetra = null;
        BeanConsDetPlanLetras beanRespuesta = null;
        BeanDetalleLetra beanDetLetra = null;
        try {
            beanRespuesta = new BeanConsDetPlanLetras();
            beanRespuesta.setM_Prestamo(elemRaiz.getChildTextTrim(Constantes.TAG_PRESTAMO));
            beanRespuesta.setM_Saldo(elemRaiz.getChildTextTrim(Constantes.TAG_SALDO));
            beanRespuesta.setM_FechaVenc(elemRaiz.getChildTextTrim(Constantes.TAG_FECHA_VENC));
            beanRespuesta.setM_Cuenta(cuenta);

            Element nodoLetra = (Element) elemRaiz.getChildren("letras").get(0);
            List lista = nodoLetra.getChildren(Constantes.TAG_LETRA);
            if (lista != null && lista.size() > 0) {
                listaDetLetra = new ArrayList();
                if(send_number > lista.size()){
                    send_number = lista.size();
                }
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < send_number; i++) {
                    Element nodo = (Element) lista.get(i);
                     if (isEstado) {//Tomar solo las que no estan pagadas
                        if (!"Pagado".equalsIgnoreCase(nodo.getChildTextTrim(Constantes.TAG_ESTADO))) {
                            beanDetLetra = new BeanDetalleLetra();
                            beanDetLetra.setM_Estado(nodo.getChildTextTrim(Constantes.TAG_ESTADO));
                            beanDetLetra.setM_Moneda(moneda);
                            beanDetLetra.setM_FechVenc(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim(Constantes.TAG_FECHA_VENC)));
                            beanDetLetra.setM_NomUser(nodo.getChildTextTrim(Constantes.TAG_NAME_ACEPTANT_2));
                            beanDetLetra.setM_ImpLetra(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_IMPORTE), Integer.parseInt(nodo.getChild(Constantes.TAG_IMPORTE).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                            beanDetLetra.setM_ImpLetrasf(nodo.getChildTextTrim(Constantes.TAG_IMPORTE));
                            beanDetLetra.setM_NumUser(nodo.getChildTextTrim(Constantes.TAG_NUMBER_ACEPTANT));
                            beanDetLetra.setM_Tasa(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_TASA), Integer.parseInt(nodo.getChild(Constantes.TAG_TASA).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                            beanDetLetra.setM_NumLetra(nodo.getChildTextTrim(Constantes.TAG_NUMERO));
                            beanDetLetra.setM_RucEmpresa(ruc_empresa);
                            listaDetLetra.add(beanDetLetra);
                        }
                    } else {
                        beanDetLetra = new BeanDetalleLetra();
                        beanDetLetra.setM_Estado(nodo.getChildTextTrim(Constantes.TAG_ESTADO));
                        beanDetLetra.setM_Moneda(moneda);
                        beanDetLetra.setM_FechVenc(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim(Constantes.TAG_FECHA_VENC)));
                        beanDetLetra.setM_NomUser(nodo.getChildTextTrim(Constantes.TAG_NAME_ACEPTANT_2));
                        beanDetLetra.setM_ImpLetra(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_IMPORTE), Integer.parseInt(nodo.getChild(Constantes.TAG_IMPORTE).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                        beanDetLetra.setM_ImpLetrasf(nodo.getChildTextTrim(Constantes.TAG_IMPORTE));
                        beanDetLetra.setM_NumUser(nodo.getChildTextTrim(Constantes.TAG_NUMBER_ACEPTANT));
                        beanDetLetra.setM_Tasa(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_TASA), Integer.parseInt(nodo.getChild(Constantes.TAG_TASA).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                        beanDetLetra.setM_NumLetra(nodo.getChildTextTrim(Constantes.TAG_NUMERO));
                        beanDetLetra.setM_RucEmpresa(ruc_empresa);
                        listaDetLetra.add(beanDetLetra);
                    }

                }
                beanRespuesta.setM_DetalleLetras(listaDetLetra);
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanRespuesta;

    }

    public static BeanConsDetPlanLetras parseConsultaDetPlanillaPag(Element elemRaiz, String formatoFechaIN, String moneda,
            String cuenta,String ruc_empresa,int total_number,int send_number,String tipo,String codCliente,String codAcept
            ,CashClientWS cashclienteWS,boolean isEstado) {
        int secuencial = 0;
        ArrayList listaDetLetra = new ArrayList();
        BeanConsDetPlanLetras beanRespuesta = null;
        
        try {
            beanRespuesta = new BeanConsDetPlanLetras();
            beanRespuesta.setM_Prestamo(elemRaiz.getChildTextTrim(Constantes.TAG_PRESTAMO));
            beanRespuesta.setM_Saldo(elemRaiz.getChildTextTrim(Constantes.TAG_SALDO));
            beanRespuesta.setM_FechaVenc(elemRaiz.getChildTextTrim(Constantes.TAG_FECHA_VENC));
            beanRespuesta.setM_Cuenta(cuenta);
            parseConsDetPlanilla(elemRaiz, listaDetLetra, moneda, formatoFechaIN,isEstado,ruc_empresa,send_number);
            secuencial = secuencial + send_number;
            while (total_number > secuencial) {
                ArrayList listaParametros = new ArrayList();
                if ("G".equals(tipo)) {
                    listaParametros.add(new BeanNodoXML("client_code", codCliente));
                } else {
                    listaParametros.add(new BeanNodoXML("client_code", codCliente));
                    listaParametros.add(new BeanNodoXML("acep_code", codAcept));
                }
                listaParametros.add(new BeanNodoXML("id_trx", Constantes.IBS_DET_PLAN_LETRAS));
                listaParametros.add(new BeanNodoXML("account_number", cuenta));
                listaParametros.add(new BeanNodoXML("ruc", ruc_empresa));
                String num_movimiento = String.valueOf(secuencial+1);
                while (num_movimiento.length() < 10) {
                    num_movimiento = "0" + num_movimiento;
                }
                listaParametros.add(new BeanNodoXML("num_mov", num_movimiento));
                String resultado = null;
                String req = GenRequestXML.getXML(listaParametros);
                resultado = cashclienteWS.ProcesarMensaje(req,Constantes.WEB_SERVICE_CASH);
                if (resultado != null && !"".equals(resultado)) {
                    BeanRespuestaXML beanResXML = ParserXML.parsearRespuesta(resultado);
                    if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {
                        
                        send_number = Integer.parseInt(beanResXML.getM_Respuesta().getChildText("num_reg"));
                        secuencial = secuencial + send_number;
                        parseConsDetPlanilla(beanResXML.getM_Respuesta(), listaDetLetra, moneda, formatoFechaIN,isEstado,ruc_empresa,send_number);
                    } else {
                        secuencial = total_number;
                    }
                } else {
                    secuencial = total_number;
                }

            }
            beanRespuesta.setM_DetalleLetras(listaDetLetra);
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanRespuesta;
    }
    
    public static BeanConsDetPlanLetras parseConsultaDetPlanillaPag2(BeanRespuestaXML beanResXML,String formatoFechaIN, String moneda,
            String cuenta,String ruc_empresa,int total_number,int send_number,int nropag,String tipo,String codCliente,String codAcept
            ,CashClientWS cashclienteWS,boolean isEstado) {
        int secuencial = 0;
        ArrayList listaDetLetra = new ArrayList();
        BeanConsDetPlanLetras beanRespuesta = null ;
        
        try {
        	 		beanRespuesta = new BeanConsDetPlanLetras();
                    if (beanResXML != null && "00".equals(beanResXML.getM_CodigoRetorno())) {

                    	 parseConsDetPlanilla(beanResXML.getM_Respuesta(), listaDetLetra, moneda, formatoFechaIN,isEstado,ruc_empresa,send_number);
             
                         
                    } else {
                        secuencial = total_number;
                    }
                
               
                beanRespuesta.setM_DetalleLetras(listaDetLetra);
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanRespuesta;
    }
    
    
    public static void parseConsDetPlanilla(Element elemRaiz,List listaDetLetra,String moneda,String formatoFechaIN,
            boolean isEstado,String ruc_empresa,int send_number){
        BeanDetalleLetra beanDetLetra = null;
        Element nodoLetra = (Element) elemRaiz.getChildren("letras").get(0);
            List lista = nodoLetra.getChildren(Constantes.TAG_LETRA);
            if (lista != null && lista.size() > 0) {
                if(send_number > lista.size()){
                    send_number = lista.size();
                }
                for (int i = 0; i < send_number; i++) {
                    Element nodo = (Element) lista.get(i);
                    beanDetLetra = new BeanDetalleLetra();
                    if(isEstado){//Tomar solo las que no estan pagadas
                        if (!"Pagado".equalsIgnoreCase(nodo.getChildTextTrim(Constantes.TAG_ESTADO))) {
                            beanDetLetra.setM_Estado(nodo.getChildTextTrim(Constantes.TAG_ESTADO));
                            beanDetLetra.setM_Moneda(moneda);
                            beanDetLetra.setM_FechVenc(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim(Constantes.TAG_FECHA_VENC)));
                            beanDetLetra.setM_NomUser(nodo.getChildTextTrim(Constantes.TAG_NAME_ACEPTANT_2));
                            beanDetLetra.setM_ImpLetra(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_IMPORTE), Integer.parseInt(nodo.getChild(Constantes.TAG_IMPORTE).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                            beanDetLetra.setM_ImpLetrasf(nodo.getChildTextTrim(Constantes.TAG_IMPORTE));
                            beanDetLetra.setM_NumUser(nodo.getChildTextTrim(Constantes.TAG_NUMBER_ACEPTANT));
                            beanDetLetra.setM_Tasa(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_TASA), Integer.parseInt(nodo.getChild(Constantes.TAG_TASA).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                            beanDetLetra.setM_NumLetra(nodo.getChildTextTrim(Constantes.TAG_NUMERO));
                            beanDetLetra.setM_RucEmpresa(ruc_empresa);
                            listaDetLetra.add(beanDetLetra);
                        }
                    } else {
                        beanDetLetra.setM_Estado(nodo.getChildTextTrim(Constantes.TAG_ESTADO));
                        beanDetLetra.setM_Moneda(moneda);
                        beanDetLetra.setM_FechVenc(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim(Constantes.TAG_FECHA_VENC)));
                        beanDetLetra.setM_NomUser(nodo.getChildTextTrim(Constantes.TAG_NAME_ACEPTANT_2));
                        beanDetLetra.setM_ImpLetra(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_IMPORTE), Integer.parseInt(nodo.getChild(Constantes.TAG_IMPORTE).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                        beanDetLetra.setM_ImpLetrasf(nodo.getChildTextTrim(Constantes.TAG_IMPORTE));
                        beanDetLetra.setM_NumUser(nodo.getChildTextTrim(Constantes.TAG_NUMBER_ACEPTANT));
                        beanDetLetra.setM_Tasa(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_TASA), Integer.parseInt(nodo.getChild(Constantes.TAG_TASA).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                        beanDetLetra.setM_NumLetra(nodo.getChildTextTrim(Constantes.TAG_NUMERO));
                        listaDetLetra.add(beanDetLetra);
                    }
                }
            }
    }

    public static BeanConsDetPlanLetras parseConsultaDetPlanillaxEst(Element elemRaiz, String formatoFechaIN, String moneda, String cuenta) {
        ArrayList listaDetLetra = null;
        BeanConsDetPlanLetras beanRespuesta = null;
        BeanDetalleLetra beanDetLetra = null;
        try {
            beanRespuesta = new BeanConsDetPlanLetras();
            beanRespuesta.setM_Prestamo(elemRaiz.getChildTextTrim(Constantes.TAG_PRESTAMO));
            beanRespuesta.setM_Saldo(elemRaiz.getChildTextTrim(Constantes.TAG_SALDO));
            beanRespuesta.setM_FechaVenc(elemRaiz.getChildTextTrim(Constantes.TAG_FECHA_VENC));
            beanRespuesta.setM_Cuenta(cuenta);

            Element nodoLetra = (Element) elemRaiz.getChildren("letras").get(0);
            List lista = nodoLetra.getChildren(Constantes.TAG_LETRA);
            if (lista != null && lista.size() > 0) {
                listaDetLetra = new ArrayList();
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < lista.size(); i++) {
                    Element nodo = (Element) lista.get(i);
                    if (!"Pagado".equalsIgnoreCase(nodo.getChildTextTrim(Constantes.TAG_ESTADO))) {
                        beanDetLetra = new BeanDetalleLetra();
                        beanDetLetra.setM_Estado(nodo.getChildTextTrim(Constantes.TAG_ESTADO));
                        beanDetLetra.setM_Moneda(moneda);
                        beanDetLetra.setM_FechVenc(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", nodo.getChildTextTrim(Constantes.TAG_FECHA_VENC)));
                        beanDetLetra.setM_NomUser(nodo.getChildTextTrim(Constantes.TAG_NAME_ACEPTANT_2));
                        beanDetLetra.setM_ImpLetra(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_IMPORTE), Integer.parseInt(nodo.getChild(Constantes.TAG_IMPORTE).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                        beanDetLetra.setM_ImpLetrasf(nodo.getChildTextTrim(Constantes.TAG_IMPORTE));
                        beanDetLetra.setM_NumUser(nodo.getChildTextTrim(Constantes.TAG_NUMBER_ACEPTANT));
                        beanDetLetra.setM_Tasa(Util.formatearMontoConComa(nodo.getChildTextTrim(Constantes.TAG_TASA), Integer.parseInt(nodo.getChild(Constantes.TAG_TASA).getAttributeValue(Constantes.TAG_NUM_DECIMALES))));
                        beanDetLetra.setM_NumLetra(nodo.getChildTextTrim(Constantes.TAG_NUMERO));
                        listaDetLetra.add(beanDetLetra);
                    }

                }
                beanRespuesta.setM_DetalleLetras(listaDetLetra);
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanRespuesta;

    }

    public static BeanLetra parseConsultaPreLiqLetra(Element elemRaiz, String formatoFechaIN) {

        BeanLetra beanRespuesta = null;
        int nroDecimales = 2;
        try {
            beanRespuesta = new BeanLetra();
            beanRespuesta.setM_Aceptante(elemRaiz.getChildTextTrim(Constantes.TAG_ACEPTANTE));
            beanRespuesta.setM_Cliente(elemRaiz.getChildTextTrim(Constantes.TAG_CLIENTE));
            beanRespuesta.setM_Descripcion(elemRaiz.getChildTextTrim(Constantes.TAG_DESCRIPCION));
            beanRespuesta.setM_FechaVenc(Fecha.formatearFecha(formatoFechaIN, "dd/MM/yyyy", elemRaiz.getChildTextTrim(Constantes.TAG_FECHA_VENC)));
            beanRespuesta.setM_InteresPend(elemRaiz.getChildTextTrim(Constantes.TAG_INTERES_PEND));

            beanRespuesta.setM_InteresRef(elemRaiz.getChildTextTrim(Constantes.TAG_INTERES_REF));            
            beanRespuesta.setM_Moneda(elemRaiz.getChildTextTrim(Constantes.TAG_MONEDA));
            beanRespuesta.setM_Mora(elemRaiz.getChildTextTrim(Constantes.TAG_MORA));
            beanRespuesta.setM_NumPlanilla(elemRaiz.getChildTextTrim(Constantes.TAG_NUM_PLANILLA));
            beanRespuesta.setM_Oficina(elemRaiz.getChildTextTrim(Constantes.TAG_OFICINA));
            beanRespuesta.setM_PagoInteres(elemRaiz.getChildTextTrim(Constantes.TAG_PAGO_INTERES));
            beanRespuesta.setM_PagoPrincipal(elemRaiz.getChildTextTrim(Constantes.TAG_PAGO_PRINCIPAL));
            beanRespuesta.setM_Portes(elemRaiz.getChildTextTrim(Constantes.TAG_PORTES));
            beanRespuesta.setM_Principal(elemRaiz.getChildTextTrim(Constantes.TAG_PRINCIPAL));
            beanRespuesta.setM_PrincipalPend(elemRaiz.getChildTextTrim(Constantes.TAG_PRINCIPAL_PEND));
            beanRespuesta.setM_Protesto(elemRaiz.getChildTextTrim(Constantes.TAG_PROTESTO));
            beanRespuesta.setM_Provisionar(elemRaiz.getChildTextTrim(Constantes.TAG_PROVISIONAR));
            beanRespuesta.setM_SignoPagInt(elemRaiz.getChildTextTrim(Constantes.TAG_SIGNO_PAG_INT));
            beanRespuesta.setM_Interes(elemRaiz.getChildTextTrim(Constantes.TAG_INTERESES));
            beanRespuesta.setM_SignPrincipal(elemRaiz.getChildTextTrim(Constantes.TAG_SIGNO_PRINCIPAL));
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return beanRespuesta;
    }

    public static String obtenerItf(Element elemRaiz) {
        return elemRaiz.getChildTextTrim("valor_itf")+"&"+elemRaiz.getChild("valor_itf").getAttributeValue("decimal");
//        return Util.formatearMonto(elemRaiz.getChildTextTrim("valor_itf"), Integer.parseInt(elemRaiz.getChild("valor_itf").getAttributeValue("decimal")));
    }

public static String obtenerRespLetxRen(BeanLetra bean) {

        StringBuilder respuesta = new StringBuilder();
        respuesta.append("<?xml version='1.0' encoding=\"ISO-8859-1\"?><Respuestas>");
        int nroDecimales = 2;
        try {
            respuesta.append("<respuesta valor='Ok'/>");
            respuesta.append("<respuesta valor='" + bean.getM_Aceptante() + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_Cliente() + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_Descripcion() + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_FechaVenc() + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_ImporteAnt(), nroDecimales) + "'/>");//*
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_ImporteFinal(), nroDecimales) + "'/>");//*
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_Interes(), nroDecimales) + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_InteresPend(), nroDecimales) + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_InteresRef() + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_Itf(), nroDecimales) + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_Moneda() + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_Mora(), nroDecimales) + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_NumLetra() + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_NumPlanilla() + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_Oficina() + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_PagoInteres(), nroDecimales) + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_PagoPrincipal(), nroDecimales) + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_Portes(), nroDecimales) + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_Principal(), nroDecimales) + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_PrincipalPend(), nroDecimales) + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_Protesto(), nroDecimales) + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_Provisionar() + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_SignPrincipal() + "'/>");
            respuesta.append("<respuesta valor='" + bean.getM_SignoPagInt() + "'/>");
            respuesta.append("<respuesta valor='" + Util.formatearMontoConComa(bean.getM_amortizacion(), nroDecimales) + "'/></Respuestas>");

        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }

        return respuesta.toString();
    }

    //JWONG
    public static ArrayList listarBusquedaPagoServ(Element elemRaiz, String codEmpresa, String nombreServicio) {
        ArrayList listaResult = null;
        BeanConsPagoServicio beanXML = null;
        String nroDec = null;
        int nroDecimales = 0;

        try {
            Element nodoOdat1R = (Element) elemRaiz.getChildren("receipts").get(0);
            List lista = nodoOdat1R.getChildren("recibo");
            if (lista != null && lista.size() > 0) {
                listaResult = new ArrayList();
                //obtenemos el detalle de cada una de las cuentas
                for (int i = 0; i < lista.size(); i++) {
                    Element nodo = (Element) lista.get(i);

                    beanXML = new BeanConsPagoServicio();
                    beanXML.setM_NumRecibo(nodo.getChildTextTrim("receipt_number"));
                    beanXML.setM_Moneda(nodo.getChildTextTrim("currency_2"));                    
                    beanXML.setM_NombreCliente(nodo.getChildTextTrim("name").replaceAll(";", ""));
                    beanXML.setM_FechaEmision(nodo.getChildTextTrim("emission_date"));
                    try {
                        nroDec = nodo.getChild("amount").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    } catch (Exception exc) {
                        nroDecimales = 0;
                    }
                    beanXML.setM_Importe(Util.formatearMonto(nodo.getChildTextTrim("amount"), nroDecimales));
                    beanXML.setM_ImporteMostrar(beanXML.getM_Importe().replace(" ",","));//jmoreno 21-08-09

                    beanXML.setM_CodEmpresa(codEmpresa);
                    beanXML.setM_NombreServicio(nombreServicio);

                    beanXML.setM_DescrMoneda(Util.monedaMostrar(beanXML.getM_Moneda()));

                    listaResult.add(beanXML);
                }
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return listaResult;
    }

    //jwong
    //Servicios Ofrecidos por las Empresas de Servicio
    public static List parsearRespuestaWSHomeBanking4(String xml){
        ArrayList listaResult = null;
        BeanRespuestaWSHomeBankingXML beanXML = null;

        try {
            Document doc = parse(xml, false);
            Element elemRaiz = doc.getRootElement();
            Element nodoResult = (Element) (elemRaiz.getChildren().get(0));
            Element nodoDiffgr = (Element) nodoResult.getChildren().get(1);
            Element nodoDataSet = (Element) nodoDiffgr.getChildren(Constantes.TAG_CEWS_NODODATASET).get(0);
            List lSectores = nodoDataSet.getChildren(Constantes.TAG_SECTORES);
            
            if (lSectores != null && lSectores.size() > 0) {

                listaResult = new ArrayList();
                for (int i = 0; i < lSectores.size(); i++) {

                    Element nodo = (Element) lSectores.get(i);
                    beanXML = new BeanRespuestaWSHomeBankingXML();

                    beanXML.setM_Codigo(nodo.getChildTextTrim(Constantes.TAG_CEWS_INTTABLAID)); //codigo de entidad
                    beanXML.setM_Nombre(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRDESCRIPCION));
                    beanXML.setM_CodigoInterno(nodo.getChildTextTrim(Constantes.TAG_CEWS_STRCODIGOINTERNO));

                    listaResult.add(beanXML);
                }
            }
        } catch (IOException e) {
        	 logger.error(e.toString(),e);     
            return null;
        } catch (org.jdom.JDOMException e) {
        	 logger.error(e.toString(),e);     
            return null;
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return listaResult;
    }
    //jwong 31/03/2009
    public static ArrayList listarBusquedaPagoServClaro(Element elemRaiz, String codEmpresa, String nombreServicio){
        ArrayList listaResult = null;
        BeanConsPagoServicio beanXML = null;
        String nroDec = null;
        int nroDecimales = 0;
        try {
            Element nodoOdat1R = (Element)elemRaiz.getChildren("recibos").get(0);
            List lista = nodoOdat1R.getChildren("recibo");
            if(lista!=null && lista.size()>0){
                listaResult = new ArrayList();
                //obtenemos el detalle de cada una de las cuentas
                for(int i=0 ; i<lista.size() ; i++){
                    Element nodo = (Element)lista.get(i);

                    beanXML = new BeanConsPagoServicio();
                    beanXML.setM_NumRecibo(nodo.getChildTextTrim("codigo"));
                    beanXML.setM_Descripcion(nodo.getChildTextTrim("descripcion"));
                    beanXML.setM_Moneda(nodo.getChildTextTrim("moneda"));
                    beanXML.setM_EstadoDeuda(nodo.getChildTextTrim("estado_deuda"));
                    beanXML.setM_TipoDocumento(nodo.getChildTextTrim("tipo_doc"));

                    beanXML.setM_NumeroDocumento(nodo.getChildTextTrim("num_doc"));
                    beanXML.setM_FechaEmision(nodo.getChildTextTrim("fec_emision"));
                    beanXML.setM_FechaVencimiento(nodo.getChildTextTrim("fec_venc"));
                    beanXML.setM_Referencia(nodo.getChildTextTrim("ref_deuda"));

                    try{
                        //obtenemos el numero de decimales para el saldo anterior
                        nroDec = nodo.getChild("monto").getAttributeValue("decimal");
                        nroDecimales = Integer.parseInt(nroDec);
                    }catch(Exception exc){
                        nroDecimales = Constantes.NUMERO_DECIMALES_SALIDA;
                    }
                    beanXML.setM_Importe(Util.formatearMonto(nodo.getChildTextTrim("monto"), nroDecimales));
                    beanXML.setM_ImporteMostrar(beanXML.getM_Importe().replace(" ",","));

                    beanXML.setM_CodEmpresa(codEmpresa);
                    beanXML.setM_NombreServicio(nombreServicio);

                    beanXML.setM_DescrMoneda(Util.monedaMostrar(beanXML.getM_Moneda()));

                    listaResult.add(beanXML);
                }
            }
        } catch (Exception e) {
        	 logger.error(e.toString(),e);     
            return null;
        }
        return listaResult;
    }

}
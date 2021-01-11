<%-- 
    Document   : exportarConsultaOrdenes
    Created on : 30-ene-2009, 19:04:31
    Author     : jwong
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@page import="java.util.*"%>
<%@page import="com.hiper.cash.util.Fecha"%>
<%--JWONG 29/08/2008 Para que no obtenga datos de cache--%>
<%@ include file="/cache/cache.jsp" %>
<%
  String accion = (String)request.getParameter("accion");
  //obtenemos el formato en el que se descargara el archivo
  String formato = (String)request.getParameter("formato");
  if("save".equalsIgnoreCase(accion)){
    String nomb = Fecha.getFechaActual("yyyyMMdd_HHmmss");
    String nombre_archivo = "Rep" + nomb + ".xls";
    //enviamos los parametros necesarios al response para que el SO lo tome como formato excel
    response.setHeader("Content-Disposition","attachment; filename=\"" + nombre_archivo + "\"");
    response.setContentType("application/vnd.ms-excel");
  }
  //para que siempre se actualicen los datos de la respuesta
  response.addHeader("Cache-control","no-cache");
  response.addHeader("Pragma","no-cache");
  response.setDateHeader ("Expires", 0);
%>
<html>
  <head>
    <style type='text/css'>
        body {
            margin-left: 0px;
            margin-top: 0px;
            margin-right: 0px;
            margin-bottom: 0px;
        }
        .clsText{
          font-family: Tahoma;
          font-size: 10pt;
        }

        .celdaCabecera{
          FONT-SIZE: 8pt;
          COLOR: #000000;
          FONT-FAMILY: Arial, 'Times New Roman';
          BACKGROUND-COLOR: #ffffff;

        }
        .CellColRowUser {
            font-weight:normal; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN:right; BACKGROUND-COLOR: #ffffff;
        }
        .CellColRowSalir {
            font-weight:normal; FONT-SIZE: 8pt; COLOR: #ff0000; FONT-FAMILY: Arial; TEXT-ALIGN:right; BACKGROUND-COLOR: #ffd800;
        }
        .CellColRowMensaje {
            font-weight:bolder ; FONT-SIZE: 9pt; COLOR: #ff0000; FONT-FAMILY: Arial; TEXT-ALIGN:center; BACKGROUND-COLOR: #fbfb8d;
        }
        table{
          FONT-SIZE: 8pt;
          COLOR: #000000;
          FONT-FAMILY: Arial, 'Times New Roman';
          BACKGROUND-COLOR: #ffffff
        }
        A:link {
            COLOR: #5544da; TEXT-DECORATION: underline
        }
        A:visited {
            COLOR: #5544ff; TEXT-DECORATION: underline
        }
        A:hover {
            COLOR: #5544ff; TEXT-DECORATION: underline
        }
        .Title {
            FONT-WEIGHT: bold; FONT-SIZE: 12pt; COLOR: #0062ac; FONT-FAMILY: Arial; BACKGROUND-COLOR: #ffffff; TEXT-ALIGN: left;
            border-bottom-color:#0062ac; border-bottom:2;
        }
        .TitleCursiva {
            FONT-WEIGHT: normal; FONT-SIZE: 11pt; COLOR: #0062ac; FONT-FAMILY: Arial; BACKGROUND-COLOR: #ffffff; TEXT-ALIGN: left;
            border-bottom-color:#0062ac; border-bottom:2;
            font-style:italic;
        }
        .TitleRow {
            FONT-WEIGHT: bolder; FONT-SIZE: 9pt; COLOR: #ffffff; FONT-FAMILY: Arial; BACKGROUND-COLOR: #336699; TEXT-ALIGN: left
        }
        .TitleRow2 {
            FONT-WEIGHT: bolder; FONT-SIZE: 9pt; COLOR: #ffffff; FONT-FAMILY: Arial; BACKGROUND-COLOR: #336699;
        }
        .TitleRow3 {
            FONT-WEIGHT: bold; FONT-SIZE: 12pt; COLOR: #ffffff; FONT-FAMILY: Arial; BACKGROUND-COLOR: #336699; TEXT-ALIGN: center;
        }
        .CellControl {
            FONT-WEIGHT: bolder; FONT-SIZE: 8pt; COLOR: #000000; FONT-FAMILY: Arial; TEXT-ALIGN: right;
        }

        .TitleRow4{
            FONT-WEIGHT: bold; FONT-SIZE: 9pt; COLOR: #0062ac; FONT-FAMILY: Arial; BACKGROUND-COLOR:#FFFF00; TEXT-ALIGN: center;
        }

        .TitleRowCierraSession{
            FONT-WEIGHT: bold; FONT-SIZE: 11pt; COLOR: #0062ac; FONT-FAMILY: Arial; BACKGROUND-COLOR:#ffffff; TEXT-ALIGN: center;
        }

        .TitleRow5{
            FONT-WEIGHT: bold; FONT-SIZE: 9pt; COLOR: #0062ac; FONT-FAMILY: Arial; BACKGROUND-COLOR:#FFFF00; TEXT-ALIGN: left;
        }
        .TitleRow6{
            FONT-WEIGHT: bold; FONT-SIZE: 9pt; COLOR: #0062ac; FONT-FAMILY: Arial; BACKGROUND-COLOR:#FFFF00; TEXT-ALIGN: right;
        }
        .TitleRow7{
            FONT-WEIGHT: bold; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; BACKGROUND-COLOR:#FFFF00; TEXT-ALIGN: center;
        }
        .TitleRow8{
            FONT-WEIGHT: bold; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; BACKGROUND-COLOR:#FFFF00; TEXT-ALIGN: left;
        }


        .TitleLateral {
            background-color: #336699; color: #FFFFFF; TEXT-ALIGN: left; font-size: 8pt; font-family: Arial;
        }
        .CellColLateralIn {
            background-color: #FFFFE6; color: #0062ac; TEXT-ALIGN: left; font-size: 8pt; font-family: Arial;
        }
        .CellColLateralOut {
            background-color: #FFFFCC; color: #0062ac; TEXT-ALIGN: left; font-size: 8pt; font-family: Arial;
        }


        .CellColRowSalir {
            font-weight:normal; FONT-SIZE: 8pt; COLOR: #ff0000; FONT-FAMILY: Arial; TEXT-ALIGN:right; BACKGROUND-COLOR: #ffffff;
        }
        .CellColRowFecha {
            font-weight:normal; FONT-SIZE: 8pt; COLOR: #666666; FONT-FAMILY: Arial; TEXT-ALIGN:right; BACKGROUND-COLOR: #ffffff;
        }
        .CellColCargo {
            font-weight:normal; FONT-SIZE: 8pt; COLOR: #CC0000; FONT-FAMILY: Arial; TEXT-ALIGN:right; BACKGROUND-COLOR: #ffffff;
        }
        .CellColRow7 {
            font-weight:normal; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN:center; BACKGROUND-COLOR: #ffffff;
        }
        .CellColRow8 {
            font-weight:normal; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN:left; BACKGROUND-COLOR: #ffffff;
        }
        .CellColRow6 {
            font-weight:normal; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN:right; BACKGROUND-COLOR: #ffffff;
        }
        .CellColRow5 {
            FONT-WEIGHT: bolder; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN: right; BACKGROUND-COLOR: #ffffff;
        }
        .CellColRow {
            FONT-WEIGHT: bolder; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN: right; BACKGROUND-COLOR: #FFFFCC;

        }
        .CellColRow2 {
            FONT-WEIGHT: bolder; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN: left; BACKGROUND-COLOR: #FFFFCC;

        }
        .CellColRowCenter {
            FONT-WEIGHT: bolder; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN: center; BACKGROUND-COLOR: #FFFFCC;

        }
        .CellColRow3 {
            FONT-WEIGHT: bolder; FONT-SIZE: 8pt; COLOR: #3366DE; FONT-FAMILY: Arial; TEXT-ALIGN: center; BACKGROUND-COLOR: #BDCFDE;
        }
        .CellColRow4 {
            FONT-WEIGHT: bold; FONT-SIZE: 9pt; COLOR: #3366DE; FONT-FAMILY: Arial; TEXT-ALIGN: center; BACKGROUND-COLOR: #BDCFDE;
        }
        .CellColRowE {
            FONT-WEIGHT: bolder; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN: right; BACKGROUND-COLOR: #FFFFCC;
            width: 200px;

        }

        .CellColRowLeftLetra {
            FONT-WEIGHT: bolder; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN: left; BACKGROUND-COLOR: #ffd800;

        }

        .CellScreen {
            BACKGROUND-COLOR: #ffffff
          /*BACKGROUND-COLOR: #dddddd*/
        }
        .TextBox {
            FONT-SIZE: 8pt; COLOR: #000000; FONT-FAMILY: Arial;
        }
        .Company {
            FONT-WEIGHT: bolder; FONT-SIZE: 9pt; COLOR: midnightblue; FONT-FAMILY: Arial; font-style: oblique;
        }
        .txtAyuda {
            FONT-SIZE: 8pt; COLOR: navy; FONT-FAMILY: Arial; BACKGROUND-COLOR: #ffffdd
        }
        .RowOn {
            FONT-SIZE: 8pt; COLOR: #000000; FONT-FAMILY: Arial, 'Times New Roman'; BACKGROUND-COLOR: #dddddd
        }

        .AllRowOn {
            FONT-SIZE: 8pt; COLOR: midnightblue; FONT-FAMILY: Arial; BACKGROUND-COLOR: #dddddd; FONT-WEIGHT: bolder;
        }
                .Estilo1 {
                    font-family: Tahoma;
                    font-size: 9px;
                    font-weight: bold;
                    color: #C1D2E6;
                }
            .clsLabel
            {
              font-family: Tahoma;
              font-weight: bold;
              font-size: 8pt;
              color: Navy;
            }
            .clsText
            {
              font-family: Tahoma;
              font-size: 8pt;
            }
            .clsTitle
            {
              font-family: Arial;
              font-weight: bold;
              font-size: 9pt;
              color: white;
              background-color: navy;
              text-decoration: underline;
            }
          .TESTcpYearNavigation,
            .TESTcpMonthNavigation
                    {
                    background-color:#6677DD;
                    text-align:center;
                    vertical-align:center;
                    text-decoration:none;
                    color:#FFFFFF;
                    font-weight:bold;
                    }
            .TESTcpDayColumnHeader,
            .TESTcpYearNavigation,
            .TESTcpMonthNavigation,
            .TESTcpCurrentMonthDate,
            .TESTcpCurrentMonthDateDisabled,
            .TESTcpOtherMonthDate,
            .TESTcpOtherMonthDateDisabled,
            .TESTcpCurrentDate,
            .TESTcpCurrentDateDisabled,
            .TESTcpTodayText,
            .TESTcpTodayTextDisabled,
            .TESTcpText
                    {
                    font-family:arial;
                    font-size:8pt;
                    }
            TD.TESTcpDayColumnHeader
                    {
                    text-align:right;
                    border:solid thin #6677DD;
                    border-width:0 0 1 0;
                    }
            .TESTcpCurrentMonthDate,
            .TESTcpOtherMonthDate,
            .TESTcpCurrentDate
                    {
                    text-align:right;
                    text-decoration:none;
                    }
            .TESTcpCurrentMonthDateDisabled,
            .TESTcpOtherMonthDateDisabled,
            .TESTcpCurrentDateDisabled
                    {
                    color:#D0D0D0;
                    text-align:right;
                    text-decoration:line-through;
                    }
            .TESTcpCurrentMonthDate
                    {
                    color:#6677DD;
                    font-weight:bold;
                    }
            .TESTcpCurrentDate
                    {
                    color: #FFFFFF;
                    font-weight:bold;
                    }
            .TESTcpOtherMonthDate
                    {
                    color:#808080;
                    }
            TD.TESTcpCurrentDate
                    {
                    color:#FFFFFF;
                    background-color: #6677DD;
                    border-width:1;
                    border:solid thin #000000;
                    }
            TD.TESTcpCurrentDateDisabled
                    {
                    border-width:1;
                    border:solid thin #FFAAAA;
                    }
            TD.TESTcpTodayText,
            TD.TESTcpTodayTextDisabled
                    {
                    border:solid thin #6677DD;
                    border-width:1 0 0 0;
                    }
            A.TESTcpTodayText,
            SPAN.TESTcpTodayTextDisabled
                    {
                    height:14px;
                    }
            A.TESTcpTodayText
                    {
                    color:#6677DD;
                    font-weight:bold;
                    }
            SPAN.TESTcpTodayTextDisabled
                    {
                    color:#D0D0D0;
                    }
            .TESTcpBorder{
                    border:solid thin #6677DD;
                    }
          .RowOn {
            FONT-SIZE: 8pt; COLOR: #000000; FONT-FAMILY: Arial, 'Times New Roman'; BACKGROUND-COLOR: #dddddd
          }
          .RowOff {
            FONT-SIZE: 8pt; COLOR: #000000; FONT-FAMILY: Arial, 'Times New Roman'; BACKGROUND-COLOR: #ffffff
          }
          .total {
            FONT-WEIGHT: bolder; FONT-SIZE: 10pt; COLOR: #000000; FONT-FAMILY: Arial; BACKGROUND-COLOR: #BDCFDE;
          }



         /************************************
         *         Datagrid styles          *
         * esilva *
         ************************************/

        TABLE.DATAGRID {
            background-color : #FFFFFF;
            padding : 2pt;
            border-spacing : 2pt;
            width:100%;

        }

        TH.DATAGRID {
                FONT-WEIGHT: bold;
                FONT-SIZE: 9pt;
                COLOR: #0062ac;
                FONT-FAMILY: Arial;
                BACKGROUND-COLOR:#FFFF00;
                TEXT-ALIGN: center;
        }

        TABLE.DATAGRID TR TD {
            /*font-family : verdana;
            padding: 2px;*/
            font-weight:normal;
            FONT-SIZE: 8pt;
            COLOR: #0062ac;
            FONT-FAMILY: Arial;
            TEXT-ALIGN:center;
            BACKGROUND-COLOR: #ffffff;
        }

        TR.DATAGRID {
            background-color : #FFFFFF;
        }

        TR.DATAGRID2 {
            background-color : #FFFFFF;
        }

        .DATAGRID_SEL {
            background-color : #336699;
        }

        .DATAGRID_DEL {
            text-decoration: line-through;
        }

        TR.DATAGRID_DEL TD INPUT {
            text-decoration: line-through;
        }

        TABLE.DATAGRID TR TD INPUT {
            width: 100%;
        }

         /************************************
         *         Form styles          *
         * esilva *
         ************************************/
        P.FORM {
            font-size : 16px;
            FONT-WEIGHT: bold;

            /*FONT-SIZE: 9pt;*/
            COLOR: #FFFFFF;
            FONT-FAMILY: Arial;
            BACKGROUND-COLOR:#0062ac;
            TEXT-ALIGN: center;
            vertical-align: middle;

            height: 25px;
        }

        /***********************************************
         * color of the thin line surrounding the form *
         **********************************************/
        TABLE.FORM {
                background-color : #FFFF00;
        }
        /**************************************
         * background color of the form title *
         *************************************/
        TH.FORM {
                color      : #0062ac;
                background-color : #FFFF00;
                font-weight: bold;
                font-family : verdana;
                text-align: center;
        }
        /********************************
         * background color of the form *
         *******************************/
        TD.FORM {
                color      : #0062ac;
        }

        /*******************************
         *  background color to use to *
         *  display the errors         *
         ******************************/
        TD.ERROR {
                background-color : #CC0000;
        }

        /**
         * Le style FORM servant aussi pour les listes, il faut rajouter ceci pour les listes triables.
         */
        TH.FORM TR TD {
                color      : #0062ac;
                background-color : #FFFF00;
                font-weight: bold;
                font-family : verdana;
        }

    </style>

    <%if(!"save".equalsIgnoreCase(accion)){%>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <%}%>
    <title>Descarga</title>
    <script language="JavaScript" type="text/JavaScript">
      <!--
        function abreImport( accion ) { //v3.0
          if( accion == 'print'){
              window.print();
              window.close();
          }
        }
      -->
    </script>
  </head>
  <body <%if(!"save".equalsIgnoreCase(accion)){%> onload="javascript:abreImport( '<%=accion%>' )" <%}%> >
    <br>
    <logic:notEmpty name="listaDetalle">
    <!-- ORDENES DE PAGO Y COBRO -->
    <layout:collection name="listaDetalle" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" align="center">
            <layout:collectionItem title="Id Orden" property="m_IdOrden" />
      <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
      <layout:collectionItem title="Tipo Cuenta" property="m_DescTipoCuenta" />
      <layout:collectionItem title="Cuenta" property="m_NumeroCuenta" />
      <layout:collectionItem title="Tipo Doc" property="m_DescTipoDocumento" />
      <layout:collectionItem title="N&uacute;mero Doc" property="m_Documento" />
      <layout:collectionItem title="Nombre" property="m_Nombre" />
      <layout:collectionItem title="Referencia" property="m_Referencia" />
      <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
          <layout:collectionItem title="Tipo Pago" property="m_DescTipoPago">
              <p align="center">
                  <bean:write name="orden" property="m_DescTipoPago"/>
              </p>
          </layout:collectionItem>
      <layout:collectionItem title="Estado" property="m_DescEstado" />
      <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
              <layout:link href="#" title="<%= descrip%>">
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
      </layout:collectionItem>

      <%--layout:collectionItem title="Descripci&oacute;n" property="m_Descripcion" /--%>
      <%--layout:collectionItem title="Correo" property="m_Email" /--%>
      <%--layout:collectionItem title="Tel&eacute;fono" property="m_Telefono" /--%>
      <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
      <layout:collectionItem title="Nro Transacci&oacute;n" property="m_IdPago">
          <p align="center">
             <layout:write name="orden" property="m_IdPago"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Nro Cheque" property="m_NumCheque" />
      <logic:notEmpty name= "comprobantePago">
        <layout:collectionItem title="Comisi&oacute;n Cliente" property="m_montoComClienteChg" />
        <layout:collectionItem title="Comisi&oacute;n Empresa" property="m_montoComEmpresaChg" />
      </logic:notEmpty>
    </layout:collection>

    <layout:space/>
  </logic:notEmpty>
  <!-- ORDENES TRANSFERENCIA CUENTA PROPIA -->
  <logic:notEmpty name="listaDetalleTransf_P">
    <layout:collection name="listaDetalleTransf_P" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" width="100%" align="center">
      <layout:collectionItem title="Id Orden" property="m_IdOrden" />
      <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
      <layout:collectionItem title="Cuenta de Cargo" property="m_CtaCargo" />
      <layout:collectionItem title="Cuenta de Abono" property="m_CtaAbono"/>
       <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Referencia" property="m_Referencia" />
      <layout:collectionItem title="Estado" property="m_DescEstado" />
      <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
          <layout:link href="#" title="<%= descrip%>">
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
      </layout:collectionItem>
      <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
    </layout:collection>
    <layout:space/>
  </logic:notEmpty>
  <!-- ORDENES TRANSFERENCIA CUENTA TERCEROS -->
  <logic:notEmpty name="listaDetalleTransf_T">

    <layout:collection name="listaDetalleTransf_T" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" width="100%" align="center">
      <layout:collectionItem title="Id Orden" property="m_IdOrden" />
      <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
      <layout:collectionItem title="Cuenta de Cargo" property="m_CtaCargo" />
      <layout:collectionItem title="Cuenta de Abono" property="m_CtaAbonoCci"/>
       <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
     <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Referencia" property="m_Referencia" />
      <layout:collectionItem title="Estado" property="m_DescEstado" />
      <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
          <layout:link href="#" title="<%= descrip%>">
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
      </layout:collectionItem>
      <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
    </layout:collection>
    <layout:space/>
  </logic:notEmpty>
  <!-- ORDENES TRANSFERENCIA CUENTA INTERBANCARIA -->
  <logic:notEmpty name="listaDetalleTransf_I">
      <layout:collection name="listaDetalleTransf_I" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" align="center">
          <layout:collectionItem title="Id Orden" property="m_IdOrden" />
          <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
          <layout:collectionItem title="Cuenta de Cargo" property="m_CtaCargo" />
          <layout:collectionItem title="Cuenta de Abono CCI" property="m_CtaAbonoCci" />
           <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
         <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
          <layout:collectionItem title="Referencia" property="m_Referencia" />
          <layout:collectionItem title="Estado" property="m_DescEstado" />
          <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
          <layout:link href="#" title="<%= descrip%>">
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
          </layout:collectionItem>
          <layout:collectionItem title="Tipo Documento" property="m_TipoDocBenef" />
          <layout:collectionItem title="N&uacute;mero Documento" property="m_NumDocBenef" />
          <layout:collectionItem title="Nombre" property="m_NombreBenef" />
          <layout:collectionItem title="Direcci&oacute;n" property="m_DirBenef" />
          <layout:collectionItem title="T&eacute;lefono" property="m_TelefBenef" />
          <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
      </layout:collection>
    <layout:space/>
  </logic:notEmpty>
  <!-- ORDENES PAGO DE SERVICIO -->
  <logic:notEmpty name="listaDetallePagoServicio">
    <layout:collection name="listaDetallePagoServicio" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" align="center">
      <layout:collectionItem title="Id Orden" property="m_IdOrden" />
      <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
      <layout:collectionItem title="Cuenta" property="m_NumeroCuenta"/>
       <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Referencia" property="m_Referencia" />
      <layout:collectionItem title="Estado" property="m_DescEstado" />
      <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
          <layout:link href="#" title="<%= descrip%>">
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
      </layout:collectionItem>
       <%--layout:collectionItem title="Cliente" property="m_Nombre" /--%>
        <layout:collectionItem title="Recibo" property="m_Documento" />
      <layout:collectionItem title="Orden Ref." property="m_OrdenRef" />
      <layout:collectionItem title="Detalle Ref." property="m_DetalleOrdenRef" />
      <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
    </layout:collection>
    <layout:space/>
  </logic:notEmpty>

  <!-- LETRAS -->
  <logic:notEmpty name="listaDetallePagoLetras">
      <layout:collection name="listaDetallePagoLetras" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" align="center" width="100%">
      <layout:collectionItem title="Id Orden" property="m_IdOrden" />
      <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
      <layout:collectionItem title="Cuenta" property="m_NumeroCuenta"/>
       <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Principal" property="m_Principal" />
      <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Estado" property="m_DescEstado" />
      <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
              <layout:link href="#" title="<%= descrip%>" >
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
      </layout:collectionItem>
      <layout:collectionItem title="Aceptante" property="m_CodAceptante" />
      <layout:collectionItem title="Recibo" property="m_Documento" />
      <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
      <layout:collectionItem title="Moneda Mora">
        <p align="center">
            <layout:write name="orden" property="m_MonedaMora"/>
        </p>
      </layout:collectionItem>
        <layout:collectionItem title="Mora" property="m_MontoMora" >
            <p align="right">
            <layout:write name="orden" property="m_MontoMora"/>
            </p>
        </layout:collectionItem>
      <layout:collectionItem title="Referencia" property="m_Referencia" />
       <layout:collectionItem title="Moneda ITF">
        <p align="center">
            <layout:write name="orden" property="m_DescMonedaITF"/>
        </p>
      </layout:collectionItem>
        <layout:collectionItem title="ITF" property="m_ITF" >
            <p align="right">
            <layout:write name="orden" property="m_ITF"/>
        </p>
        </layout:collectionItem>
      <layout:collectionItem title="Moneda Portes">
        <p align="center">
            <layout:write name="orden" property="m_DescMonedaPortes"/>
        </p>
      </layout:collectionItem>
        <layout:collectionItem title="Portes" property="m_Portes" >
            <p align="right">
            <layout:write name="orden" property="m_Portes"/>
            </p>
        </layout:collectionItem>
      <layout:collectionItem title="Moneda Protesto">
        <p align="center">
            <layout:write name="orden" property="m_MonedaProtesto"/>
        </p>
      </layout:collectionItem>
        <layout:collectionItem title="Protesto" property="m_Protesto">
            <p align="right">
            <layout:write name="orden" property="m_Protesto"/>
            </p>
        </layout:collectionItem>
    </layout:collection>
    <layout:space/>
  </logic:notEmpty>

  <logic:notEmpty name="beanDetImportEstado">

      <table cellpadding='2' cellspacing='2' border='0' align='center' width='100%'>
		  <tr>
			<td class='TitleRow3' colspan='14'>DETALLE TOTALES DE ORDEN</td>
		  </tr>
		  <tr class='TitleRow4'>
			<td rowspan="2">ID Orden</td>
			<td rowspan="2">Referencia</td>

            <td rowspan="2">Registros Enviados</td>
            <td colspan="2">Valor Enviados</td>


            <td rowspan="2">Registros Procesados</td>
            <td colspan="2">Valor Procesados</td>


            <td rowspan="2">Registros Errados</td>
            <td colspan="2">Valor Errados</td>

            <td rowspan="2">Estado</td>
		  </tr>

          <tr class='TitleRow4'>
            <td>Soles</td>
            <td>D&oacute;lares</td>

            <td>Soles</td>
            <td>D&oacute;lares</td>

            <td>Soles</td>
            <td>D&oacute;lares</td>
          </tr>

		  <tr>
            <td class='CellColRow7'><bean:write name="beanDetImportEstado" property="m_DetIDOrden" /></td>
			<td class='CellColRow7'><bean:write name="beanDetImportEstado" property="m_DetReferencia" /></td>

			<td class='CellColRow7'><bean:write name="beanDetImportEstado" property="m_NumEnviados" /></td>
            <td class='CellColRow6'><bean:write name="beanDetImportEstado" property="m_ValorSolesEnviado" /></td>
			<td class='CellColRow6'><bean:write name="beanDetImportEstado" property="m_ValorDolaresEnviado" /></td>

			<td class='CellColRow7'><bean:write name="beanDetImportEstado" property="m_NumProcesados" /></td>
			<td class='CellColRow6'><bean:write name="beanDetImportEstado" property="m_ValorSolesProcesado" /></td>
			<td class='CellColRow6'><bean:write name="beanDetImportEstado" property="m_ValorDolaresProcesado" /></td>

			<td class='CellColRow7'><bean:write name="beanDetImportEstado" property="m_NumErrados" /></td>
			<td class='CellColRow6'><bean:write name="beanDetImportEstado" property="m_ValorSolesErrado" /></td>
            <td class='CellColRow6'><bean:write name="beanDetImportEstado" property="m_ValorDolaresErrado" /></td>

			<td class='CellColRow7'><bean:write name="beanDetImportEstado" property="m_DetEstado" /></td>
	      </tr>
      </table>
  </logic:notEmpty>


  </body>
</html>
<%
  if("save".equalsIgnoreCase(accion)){
    response.flushBuffer();
  }
%>
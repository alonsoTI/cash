<%--
    Document   : relacionesBanco
    Created on : 03-ago-2009, 10:16:43
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
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">

  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>

  <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
  </style>

  <style type='text/css'>
    body {margin-left: 0px;margin-top: 0px;margin-right: 0px;margin-bottom: 0px;}
    table{FONT-SIZE: 8pt;COLOR: #000000;FONT-FAMILY: Arial, 'Times New Roman';BACKGROUND-COLOR: #ffffff}
    .clsText{font-family: Tahoma;font-size: 10pt;}
    .Title {FONT-WEIGHT: bolder; FONT-SIZE: 8pt; COLOR: #ffffff; FONT-FAMILY: Arial; BACKGROUND-COLOR: #336699; TEXT-ALIGN: left;}
    .TitleRow3 {FONT-WEIGHT: bold; FONT-SIZE: 12pt; COLOR: #ffffff; FONT-FAMILY: Arial; BACKGROUND-COLOR: #336699; TEXT-ALIGN: center;}
    .TitleRow4 {FONT-WEIGHT: bold; FONT-SIZE: 9pt; COLOR: #0062ac; FONT-FAMILY: Arial; BACKGROUND-COLOR:#FFFF00; TEXT-ALIGN: center;}
    .CellColRow6 {font-weight:normal; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN:right; BACKGROUND-COLOR: #ffffff;}
    .CellColRow7 {font-weight:normal; FONT-SIZE: 8pt; COLOR: #0062ac; FONT-FAMILY: Arial; TEXT-ALIGN:center; BACKGROUND-COLOR: #ffffff;}
    .CellColCargo {font-weight:normal; FONT-SIZE: 8pt; COLOR: #CC0000; FONT-FAMILY: Arial; TEXT-ALIGN:right; BACKGROUND-COLOR: #ffffff;}
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
<body <%if(!"save".equalsIgnoreCase(accion)){%> onload="javascrip:abreImport( '<%=accion%>' )" <%}%> >
  <logic:notEmpty name="beanConsRelBco">
  <logic:notEmpty name="listaRelaciones">
    <table width='100%' cellpadding='2' cellspacing='2' border='0' align='center' >
        <tr>
          <td class='TitleRow3' colspan='2' width='50%'>Sectorista</td>
          <td class='TitleRow3' colspan='2' width='50%'>Funcionario Cash</td>
        </tr>
        <tr>
          <td style='background-color: #336699; color: #FFFFFF' width='15%'>Nombre:</td>
          <td class='CellColRow2' width='35%'><bean:write name="beanConsRelBco" property="m_NameSec"/></td>

          <td style='background-color: #336699; color: #FFFFFF' width='15%'>Nombre:</td>
          <td class='CellColRow2' width='35%'><bean:write name="beanConsRelBco" property="m_NameFunc"/></td>
        </tr>
        <tr>
          <td style='background-color: #336699; color: #FFFFFF'>Tel&eacute;fono:</td>
          <td class='CellColRow2' style='background-color: #FFFFE6'><bean:write name="beanConsRelBco" property="m_PhoneSec"/></td>

          <td style='background-color: #336699; color: #FFFFFF'>Tel&eacute;fono:</td>
          <td class='CellColRow2' style='background-color: #FFFFE6'><bean:write name="beanConsRelBco" property="m_PhoneFunc"/></td>
        </tr>
        <tr>
          <td style='background-color: #336699; color: #FFFFFF' >Email:</td>
          <td class='CellColRow2'><bean:write name="beanConsRelBco" property="m_EmailSec"/></td>

          <td style='background-color: #336699; color: #FFFFFF' >Email:</td>
          <td class='CellColRow2'><bean:write name="beanConsRelBco" property="m_EmailFunc"/></td>
        </tr>
    </table>


    <table cellpadding='2' cellspacing='2' border='0' align='center' width='100%'>
        <tr>
          <td class='TitleRow3' colspan='5'>DETALLE DE RELACIONES CON EL BANCO</td>
        </tr>
        <logic:iterate id="cuenta" name="listaRelaciones" indexId="i">
            <tr>
                <td class='TitleRow3' colspan='5'>&nbsp;<bean:write name="cuenta" property="m_Description"/></td>
            </tr>
            <tr class='TitleRow4'>
                <td>Cuenta</td>
                <td>Moneda</td>
                <td>Saldo Disponible</td>
                <td>Saldo Contable</td>
            </tr>
            <logic:iterate id="tipoCuenta" name="cuenta" property="m_Accounts" indexId="j" >
                <tr>
                    <td class='CellColRow7'><bean:write name="tipoCuenta" property="m_Cuenta"/></td>
                    <td class='CellColRow7'><bean:write name="tipoCuenta" property="m_Moneda"/></td>
                    <td class='CellColRow6'>
                        <logic:lessThan name="tipoCuenta" property="m_SaldoDisponible" value="0">
                            <p align="right" class="CellColCargo">
                                <layout:write name="tipoCuenta" property="m_SaldoDisponible"/>
                            </p>
                        </logic:lessThan>
                        <logic:greaterEqual name="tipoCuenta" property="m_SaldoDisponible" value="0">
                            <p align="right">
                                <layout:write name="tipoCuenta" property="m_SaldoDisponible"/>
                            </p>
                        </logic:greaterEqual>
                    </td>
                    <td class='CellColRow6'>
                        <logic:lessThan name="tipoCuenta" property="m_SaldoContable" value="0">
                            <p align="right" class="CellColCargo">
                                <layout:write name="tipoCuenta" property="m_SaldoContable"/>
                            </p>
                        </logic:lessThan>
                        <logic:greaterEqual name="tipoCuenta" property="m_SaldoContable" value="0">
                            <p align="right">
                                <layout:write name="tipoCuenta" property="m_SaldoContable"/>
                            </p>
                        </logic:greaterEqual>
                    </td>
                </tr>
            </logic:iterate>
        </logic:iterate>
    </table>
    <layout:space/>
  </logic:notEmpty>
  </logic:notEmpty>

  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
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
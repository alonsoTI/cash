<%-- 
    Document   : exportarSaldos
    Created on : 23/04/2009, 10:22:11 AM
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
    if("txt".equalsIgnoreCase(formato)){ //formato texto
        String nombre_archivo = "Rep" + nomb + ".txt";
        //enviamos los parametros necesarios al response para que el SO lo tome como formato excel
        response.setHeader("Content-Disposition","attachment; filename=\"" + nombre_archivo + "\"");
        response.setContentType("text/plain");
    }
    else{ //formato excel
        String nombre_archivo = "Rep" + nomb + ".xls";
        //enviamos los parametros necesarios al response para que el SO lo tome como formato excel
        response.setHeader("Content-Disposition","attachment; filename=\"" + nombre_archivo + "\"");
        response.setContentType("application/vnd.ms-excel");
    }
  }
  //para que siempre se actualicen los datos de la respuesta
  response.addHeader("Cache-control","no-cache");
  response.addHeader("Pragma","no-cache");
  response.setDateHeader ("Expires", 0);
%>
<html>
  <head>
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
    <br>
    <logic:notEmpty name="listaResult">
    <table cellpadding='2' cellspacing='2' border='0' align='center' width='100%'>
        <tr>
          <td class='TitleRow3' colspan='6'>DETALLE DE SALDOS</td>
        </tr>
        <tr>
            <td class='TitleRow4'>Cuenta</td>
            <td class='TitleRow4'>Tipo de Cuenta</td>
            <td class='TitleRow4'>Moneda</td>
            <td class='TitleRow4'>Saldo Disponible</td>
            <td class='TitleRow4'>Saldo Retenido</td>
            <td class='TitleRow4'>Saldo Contable</td>
        </tr>
        <logic:iterate id="cuenta" name="listaResult" indexId="i">
            <tr>
                <td class='CellColRow7'><bean:write name="cuenta" property="m_Cuenta"/></td>
                <td class='CellColRow7'><bean:write name="cuenta" property="m_DescripcionCuenta"/></td>
                <td class='CellColRow7'><bean:write name="cuenta" property="m_Moneda"/></td>
                <logic:lessThan name="cuenta" property="m_SaldoDisponible" value="0">
                    <td class="CellColCargo">
                        <p align="right">
                        <bean:write name="cuenta" property="m_SaldoDisponible"/>
                        </p>
                    </td>
                </logic:lessThan>
                <logic:greaterEqual name="cuenta" property="m_SaldoDisponible" value="0">
                    <td class='CellColRow7'>
                        <p align="right">
                        <bean:write name="cuenta" property="m_SaldoDisponible"/>
                        </p>
                    </td>
                </logic:greaterEqual>
                <logic:lessThan name="cuenta" property="m_SaldoRetenido" value="0">
                    <td class="CellColCargo">
                        <p align="right">
                        <bean:write name="cuenta" property="m_SaldoRetenido"/>
                        </p>
                    </td>
                </logic:lessThan>
                <logic:greaterEqual name="cuenta" property="m_SaldoRetenido" value="0">
                    <td class='CellColRow7'>
                        <p align="right">
                        <bean:write name="cuenta" property="m_SaldoRetenido"/>
                        </p>
                    </td>
                </logic:greaterEqual>
                <logic:lessThan name="cuenta" property="m_SaldoContable" value="0">
                    <td class="CellColCargo">
                        <p align="right">
                        <bean:write name="cuenta" property="m_SaldoContable"/>
                        </p>
                    </td>
                </logic:lessThan>
                <logic:greaterEqual name="cuenta" property="m_SaldoContable" value="0">
                    <td class='CellColRow7'>
                        <p align="right">
                        <bean:write name="cuenta" property="m_SaldoContable"/>
                        </p>
                    </td>
                </logic:greaterEqual>
                
            </tr>
        </logic:iterate>
    </table>
    </logic:notEmpty>
  </body>
</html>
<%
  if("save".equalsIgnoreCase(accion)){
    response.flushBuffer();
  }
%>
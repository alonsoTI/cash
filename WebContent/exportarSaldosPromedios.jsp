<%-- 
    Document   : exportarSaldosPromedios
    Created on : 18/07/2009, 05:39:29 PM
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@page import="java.util.*"%>
<%@page import="com.hiper.cash.util.Fecha"%>
<%@page import="com.hiper.cash.xml.bean.BeanCodigoInterbancarioXML"%>
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
    <link href="css/Styles.css" rel="stylesheet" type="text/css">

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

    <!--detalle de historico de movimientos-->
  <logic:notEmpty name="beanSaldosPromedios">

  <table cellpadding='2' cellspacing='2' border='0' align='center' width='100%'>
	  <tr>
		<td class='TitleRow3' colspan='5'>DETALLE DE SALDO</td>
	  </tr>
	  <tr class='TitleRow4'>
          <td>Titular:&nbsp;<bean:write name="beanSaldosPromedios" property="m_Titular"/></td>
          <td>Moneda:&nbsp;<bean:write name="beanSaldosPromedios" property="m_Moneda"/></td>
		  <td>Cuenta:&nbsp;<bean:write name="beanSaldosPromedios" property="m_Cuenta"/></td>
	  </tr>
	  <tr>
		<td class='CellColRow7'>Saldo Disponible</td>
        <logic:lessThan name="beanSaldosPromedios" property="m_SaldoDisponible" value="0">
            <td class='CellColCargo' colspan='2'>
                <bean:write name="beanSaldosPromedios" property="m_SaldoDisponible"/>
            </td>
        </logic:lessThan>
        <logic:greaterEqual name="beanSaldosPromedios" property="m_SaldoDisponible" value="0">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanSaldosPromedios" property="m_SaldoDisponible"/>
            </td>
        </logic:greaterEqual>
	  </tr>
	  <tr>
		<td class='CellColRow7'>Saldo Retenido</td>
        <logic:lessThan name="beanSaldosPromedios" property="m_SaldoRetenido" value="0">
            <td class='CellColCargo' colspan='2'>
                <bean:write name="beanSaldosPromedios" property="m_SaldoRetenido"/>
            </td>
        </logic:lessThan>
        <logic:greaterEqual name="beanSaldosPromedios" property="m_SaldoRetenido" value="0">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanSaldosPromedios" property="m_SaldoRetenido"/>
            </td>
        </logic:greaterEqual>
	  </tr>
	  <tr>
		<td class='CellColRow7'>Saldo Contable</td>
        <logic:lessThan name="beanSaldosPromedios" property="m_SaldoContable" value="0">
            <td class='CellColCargo' colspan='2'>
                <bean:write name="beanSaldosPromedios" property="m_SaldoContable"/>
            </td>
        </logic:lessThan>
        <logic:greaterEqual name="beanSaldosPromedios" property="m_SaldoContable" value="0">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanSaldosPromedios" property="m_SaldoContable"/>
            </td>
        </logic:greaterEqual>
	  </tr>
  </table><br>
  <logic:notEmpty name="resultListaMovimientos">
  <%--input id="m_Cuenta" type="hidden" value="<bean:write name='m_Cuenta'/>"--%>
  <layout:collection name="resultListaMovimientos" title="DETALLE DE MOVIMIENTOS" styleClass="FORM" id="movimiento" sortAction="client" width="100%" align="center">
      <layout:collectionItem title="Fecha" property="m_Fecha"/>
      <layout:collectionItem title="Hora" property="m_Hora" />
      <layout:collectionItem title="Tipo Transacci&oacute;n" property="m_TipoTrx" />
      <layout:collectionItem title="Descripci&oacute;n" property="m_Descripcion" />

      <layout:collectionItem title="Importe">
        <logic:lessThan name="movimiento" property="m_Importe" value="0">
            <p align="right" class="CellColCargo">
                <layout:write name="movimiento" property="m_Importe"/>
            </p>
        </logic:lessThan>
        <logic:greaterEqual name="movimiento" property="m_Importe" value="0">
            <p align="right">
                <layout:write name="movimiento" property="m_Importe"/>
            </p>
        </logic:greaterEqual>
      </layout:collectionItem>

      <layout:collectionItem title="Saldo">

        <logic:lessThan name="movimiento" property="m_SaldoMovimiento" value="0">
            <p align="right" class="CellColCargo">
                <layout:write name="movimiento" property="m_SaldoMovimiento"/>
            </p>
        </logic:lessThan>
        <logic:greaterEqual name="movimiento" property="m_SaldoMovimiento" value="0">
            <p align="right">
                <layout:write name="movimiento" property="m_SaldoMovimiento"/>
            </p>
        </logic:greaterEqual>
      </layout:collectionItem>
  </layout:collection>
  <div id="comp0" style="display:none">FEC</div>
  <div id="comp2" style="display:none">ANS</div>
  <div id="comp3" style="display:none">ANS</div>
  </logic:notEmpty>
  <layout:space/>

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
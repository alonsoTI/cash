<%@ page import="com.hiper.cash.util.Util"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ include file="/cache/cache.jsp"%>
<%
	String accion = request.getParameter("accion");
	if (accion.equals("1")) {
		response.setHeader("Content-Disposition",
				"filename=historicos.xls");
		response.setContentType("application/vnd.ms-excel");
	}
	if (accion.equals("2")) {
		response.setHeader("Content-Disposition",
				"filename=historicos.pdf");
		response.setContentType("application/pdf");
	}
%>
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>Exportacion de Historicos</title>
<link href="css/cash.css" rel="stylesheet" type="text/css">



<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">


<script language="JavaScript" type="text/JavaScript">
	function imprimir() {
		window.print();
		window.close();
	}
</script>

</head>
<body style="padding: 15px;" <%if (accion.equals("0")) {%>
	onload="imprimir()" <%}%>>
<logic:notEmpty name="detallesOrden">
	 <img src="img/logo-Financiero-AMA.gif" height="40">
   	<h1 id="titulo">
		<bean:message key="historicos.consultaOrdenesHistoricos.detalles.title"/>
	</h1>
	
	<table id="formulario">
		<colgroup>
			<col class="label" />
			<col class="input-read" />
			<col class="label" />
			<col class="input-read" />			
		</colgroup>
			<tr> 
				<td><bean:message key="historicos.consultaOrdenesHistoricos.empresa"/></td>
				<td><bean:write name="historicoForm" property="empresa"/></td>
				<td><bean:message key="historicos.consultaOrdenesHistoricos.servicio"/></td>
				<td><bean:write name="historicoForm" property="servicio"/></td>
				
			</tr>
			<tr>
				<td><bean:message key="historicos.consultaOrdenesHistoricos.inicio"/></td>
				<td><bean:write name="historicoForm" property="fechaInicial"/></td>
				<td><bean:message key="historicos.consultaOrdenesHistoricos.final"/></td>
				<td><bean:write name="historicoForm" property="fechaFinal"/></td>
			</tr>
			<tr> 
				<td><bean:message key="historicos.consultaOrdenesHistoricos.orden"/></td>
				<td><bean:write name="historicoForm" property="orden"/></td>
				<td><bean:message key="historicos.consultaOrdenesHistoricos.referencia"/></td>
				<td><bean:write name="historicoForm" property="referencia"/></td>
			</tr>
			
		</table>
		<br/>
	
	<table id="hor-zebra">
			<tr>
				<th>Item</th>
				<th>Pais</th>
				<th>Banco</th>
				<th>Cuenta</th>
				<th>ContraPartida</th>
				<th>Referencia</th>				
				<th>Estado</th>
				<th align="center">Moneda</th>
				<th align="center">Fecha Proceso</th>
				<th align="right">Valor</th>
			</tr>
			<logic:iterate id="detalleOrden" name="detallesOrden" indexId="row">
				<bean:define id="mod"
					value="<%= String.valueOf(row.intValue() % 2 )%>" />
				<logic:equal value="0" name="mod">
					<tr>
				</logic:equal>
				<logic:notEqual value="0" name="mod">
					<tr class="odd">
				</logic:notEqual>
				<td><bean:write name="detalleOrden" property="idDetalleOrden" /></td>
				<td><bean:write name="detalleOrden" property="pais" /></td>
				<td><bean:write name="detalleOrden" property="banco" /></td>
				<td><bean:write name="detalleOrden" property="cuenta" /></td>
				<td><bean:write name="detalleOrden" property="contraPartida" /></td>
				<td><bean:write name="detalleOrden" property="referencia" /></td>				
				<td><bean:write name="detalleOrden" property="mensajeProceso" /></td>
				<td align="center"><bean:write name="detalleOrden" property="moneda" /></td>				
				<td align="center"><bean:write name="detalleOrden" property="fechaProceso" /></td>				
				<td align="right"><bean:write name="detalleOrden" property="valor" /></td>
				
				
				
				</tr>
			</logic:iterate>
		</table>

</logic:notEmpty>

</body>
</html>

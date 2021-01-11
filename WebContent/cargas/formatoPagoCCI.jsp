<%@ include file="/includes.jsp" %>
<%! private TransferenciasDelegate delegado =  TransferenciasDelegate.getInstance(); %>
<%! private Logger logger = Logger.getLogger(this.getClass()); %> 
<% try{Map map_limites_transferencias = delegado.obtenerLimitesTransferencias();
 pageContext.setAttribute("map_limites_transferencias", map_limites_transferencias);%>
<%@ page import="com.financiero.cash.delegate.TransferenciasDelegate, java.util.Map, org.apache.log4j.Logger" %>
<html>
<head>
<link href="css/Styles.css" rel="stylesheet" type="text/css">
</head>
<div id="formatoCarga">
<h3 class="Title">Formato de pago con C&oacutedigo de cuenta interbancaria</h3>
<p class="TitleRow9">El formato de pago con C&oacutedigo de cuenta interbancaria cuenta
	obligatoriamente con los siguientes campos:</p>
<table>
	<tr>
		<td class="CellColRow2"><b><fmt:message key="validacion.pagoscci.fields.nDBuCuentaEmpresa"/></b></td>
		<td class="CellColRow6"><fmt:message key="validacion.pagoscci.fields.nDBuCuentaEmpresa.informacion"/></td>
	</tr>
	<tr>
		<td class="CellColRow2"><fmt:message key="validacion.pagoscci.fields.nDBuMonto"/></td>
		<td class="CellColRow6"><fmt:message key="validacion.pagoscci.fields.nDBuMonto.informacion">
			<fmt:param value="${pageScope.map_limites_transferencias['limiteDiarioSoles']}"/>			
			<fmt:param value="${pageScope.map_limites_transferencias['limiteDiarioDolares']}"/>
			<fmt:param value="${pageScope.map_limites_transferencias['montoITSoles']}"/>
			<fmt:param value="${pageScope.map_limites_transferencias['montoITDolares']}"/>			
		</fmt:message></td>
	</tr>
	<tr>
		<td class="CellColRow2"><fmt:message key="validacion.pagoscci.fields.dDBuMoneda"/></td>
		<td class="CellColRow6"><fmt:message key="validacion.pagoscci.fields.dDBuMoneda.informacion"/></td>
	</tr>
	<tr>
		<td class="CellColRow2"><fmt:message key="validacion.pagoscci.fields.nDBuNumeroCuenta"/></td>
		<td class="CellColRow6"><fmt:message key="validacion.pagoscci.fields.nDBuNumeroCuenta.informacion"/></td>
	</tr>
	<tr>
		<td class="CellColRow2"><fmt:message key="validacion.pagoscci.fields.dDBuTipoDocumento"/></td>
		<td class="CellColRow6"><fmt:message key="validacion.pagoscci.fields.dDBuTipoDocumento.informacion"/></td>
	</tr>
	<tr>
		<td class="CellColRow2"><fmt:message key="validacion.pagoscci.fields.nDBuDocumento"/></td>
		<td class="CellColRow6"><fmt:message key="validacion.pagoscci.fields.nDBuDocumento.informacion"/></td>
	</tr>
	<tr>
		<td class="CellColRow2"><fmt:message key="validacion.pagoscci.fields.dDBuReferencia"/></td>
		<td class="CellColRow6"><fmt:message key="validacion.pagoscci.fields.dDBuReferencia.informacion"/></td>
	</tr>
	<tr>
		<td class="CellColRow2"><fmt:message key="validacion.pagoscci.fields.dDBuAdicional1"/></td>
		<td class="CellColRow6"><fmt:message key="validacion.pagoscci.fields.dDBuAdicional1.informacion"/></td>
	</tr>
	<tr>
		<td class="CellColRow2"><fmt:message key="validacion.pagoscci.fields.dDBuAdicional2"/></td>
		<td class="CellColRow6"><fmt:message key="validacion.pagoscci.fields.dDBuAdicional2.informacion"/>
		</td>
	</tr>
</table>
</div>
<%}catch(Exception e){logger.error("Error en jsp",e);}%>
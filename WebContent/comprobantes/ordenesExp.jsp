<%@ page import="com.hiper.cash.util.Util"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ include file="/cache/cache.jsp"%>
<%
	String accion = request.getParameter("accion");	
%>
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>Consulta de Ordenes</title>
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
	onload="imprimir();" <%}%>>
	<logic:notEmpty name="ordenes">
		<img src="img/logo-Financiero-AMA.gif" height="40">
		<h1 id="titulo">Consulta de Ordenes</h1>		
		<table id="hor-zebra" style="font-size: 10px;">
			<tr>
				<th >Orden</th>				
				<th>Servicio</th>	
				<th>Cuenta</th>	
				<th>Referencia</th>	
				<th>Inicio</th>	
				<th>Vence</th>				
				<th>Estado</th>
				<th align="center">Nro</th>
				<th align="center">S/.</th>
				<th align="center">$</th>
				<th align="center">&#8364</th>				
			</tr>
			<logic:iterate id="orden" name="ordenes" indexId="row">
				<bean:define id="mod"
					value="<%= String.valueOf(row.intValue() % 2 )%>" />
				<logic:equal value="0" name="mod">
					<tr>
				</logic:equal>
				<logic:notEqual value="0" name="mod">
					<tr class="odd">
				</logic:notEqual>
				<td>					
                    <bean:write name="orden" property="m_IdOrden"/>                						
				</td>
				<td style="font-size: 9px;"><bean:write name="orden" property="m_Servicio" /></td>	
				<td><bean:write name="orden" property="m_CuentaCargo" /></td>	
				<td><bean:write name="orden" property="m_Referencia" /></td>	
				<td><bean:write name="orden" property="m_FecInicio" /></td>	
				<td><bean:write name="orden" property="m_FecVenc" /></td>
				<td>
					<logic:equal name="orden" property="m_TipoServicio" value="0">
                    	<bean:write name="orden" property="m_DescripEstado"/>
                	</logic:equal>
                	<logic:equal name="orden" property="m_TipoServicio" value="1">
                    	 <bean:define name="orden" property="m_IdOrden" id="idorden" type="java.lang.String" />
                    	<% String metodo = "javascript:obtenerDescripcion("+idorden+");"; %>
                    	<layout:link href="<%= metodo%>">
                        	<bean:write name="orden" property="m_DescripEstado"/>
                    	</layout:link>
                	</logic:equal>
				</td>
				<td align="center"><bean:write name="orden" property="m_Items" /></td>
				<td align="center"><bean:write name="orden" property="m_ValorSoles" /></td>
				<td align="center"><bean:write name="orden" property="m_ValorDolares" /></td>
				<td align="center"><bean:write name="orden" property="m_ValorEuros" /></td>				
				</tr>


			</logic:iterate>
		</table>

	</logic:notEmpty>
   
</body>
</html>

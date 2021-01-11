<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>

<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>Detalles de Historico Orden</title>
<link href="css/cash.css" rel="stylesheet" type="text/css" >
<style type="text/css">
@import url(calendario/calendar-system.css);
</style>
<script type="text/javascript" src="js/jscalendar/calendar.js"></script>
<script type="text/javascript" src="js/jscalendar/lang/calendar-es.js"></script>
<script type="text/javascript" src="js/jscalendar/calendar-setup.js"></script>
<script type="text/javascript" src="js/funciones.js"></script>
<script type="text/javascript" SRC="js/Functions.js"></script>
<script type="text/javascript" src="config/javascript.js"></script>
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<script language="JavaScript">
		function volver(){
      		location.href = "historicos.do?do=cargarOrdenes";
    	}			
	
   
</script>
</head>
<body>

<html:form action="historicos.do" >	
	<h1>
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
		
		<logic:equal value="true" name="excel">
		
			<table id="formulario">
				<colgroup>
			<col class="label" />
		
			</colgroup>
				<tr>				
					<td> <bean:message key="historicos.consultaOrdenesHistoricos.detalles.exceso"/></td>
				</tr>
			</table>
			
				
		</logic:equal>
			
		<logic:empty name="detallesOrden">
		<br/>
		<label id="lista-vacia">
			<bean:message key="historicos.consultaOrdenesHistoricos.detalles.listaVacia" />
		</label>
		<br/>
		<br/>
		<img src="img/bt-volver.png"
					onClick="javascript:volver();" width="71" height="27"
					align="middle" onMouseOver="this.src='img/bt-volver2.png'"
					onMouseOut="this.src='img/bt-volver.png'" />
		
	</logic:empty>
	<logic:notEmpty name="detallesOrden">		
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>
				<td align="left" valign="middle"><img src="img/bt-volver.png"
					onClick="javascript:volver();" width="71" height="27"
					align="middle" onMouseOver="this.src='img/bt-volver2.png'"
					onMouseOut="this.src='img/bt-volver.png'" />
				</td>

				<td align="right" class="exportacion" valign="middle">				
				
					<a	href="historicos.do?do=exportarDetallesOrden&accion=1"> 
					<img src='img/exp/ico_excel.png' alt="Exportar Excel" /></a> 
				
			
					<a	href="historicos.do?do=exportarDetallesOrden&accion=2"> 
					<img src='img/exp/ico_text.png' alt="Exportar Texto" /></a> 
					<a		target="_blank"			
					href="historicos.do?do=exportarDetallesOrden&accion=3"> <img
					src='img/exp/ico_html.png' alt="Exportar HTML" /></a> <a
					target="_blank"
					href="historicos.do?do=exportarDetallesOrden&accion=0"> <img
					src='img/exp/ico_printer.png' alt="Impresion" /></a></td>
			</tr>
			
			
			<tr>
				<td align="right" valign="middle"></td>

				<td class="paginado" align="right" valign="middle"><logic:equal
					value="true" name="paginado" property="existeAnterior">
					<a
						href="historicos.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaInicio"/>">
					<img src='img/paginacion/ico_inicio.png' /></a>
					<a
						href="historicos.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaAnterior"/>">
					<img src='img/paginacion/ico_anterior.png' /></a>
				</logic:equal> <logic:notEqual value="true" name="paginado"
					property="existeAnterior">
					<img src='img/paginacion/ico_inicio_dis.png' />
					<img src='img/paginacion/ico_anterior_dis.png' />
				</logic:notEqual> <label> <bean:write name="paginado" property="encabezado" />
				</label> <logic:equal value="true" name="paginado"
					property="existeSiguiente">
					<a
						href="historicos.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaSiguiente"/>">
					<img src='img/paginacion/ico_siguiente.png' /></a>
					<a
						href="historicos.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaFinal"/>">
					<img src='img/paginacion/ico_final.png' /></a>
				</logic:equal> <logic:notEqual value="true" name="paginado"
					property="existeSiguiente">
					<img src='img/paginacion/ico_siguiente_dis.png' />
					<img src='img/paginacion/ico_final_dis.png' />
				</logic:notEqual></td>

			</tr>
		</table>
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
	
</html:form>

	


</body>
</html>

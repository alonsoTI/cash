<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>

<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>Historicos</title>
<link href="css/Styles.css" rel="stylesheet" type="text/css">
<link href="css/cash.css" rel="stylesheet" type="text/css">
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
<script language="javascript" src="js/Functions.js"></script>

<script language="JavaScript">

	function validar(frm, strAction, strParams){
    if( frm.empresas.value == "00" ){
        alert("Seleccionar Empresa");
        frm.empresas.focus();
    }
    else if ( frm.servicios.value == "00" ){
        alert("Seleccionar Servicio");
        frm.servicios.focus();        
    }   
    else if( frm.txtFecha1.value==frm.txtFecha2.value){      
    	 alert("Seleccionar Rango");
         frm.txtFecha1.focus();      
    }else{
    	frm.action = "historicos.do?do="+ strAction + strParams;
        frm.submit(); 
    }
}

</script>

</head>
<body>
<html:form action="historicos.do">	
	<h1>
		<bean:message key="historicos.consultaOrdenesHistoricos.title"/>
	</h1>
	<table  id="formulario">
		<colgroup>
			<col class="label" />
			<col class="input" />
			<col class="label" />
			<col class="input" />
		</colgroup>
		<tr>
			<td><bean:message key="historicos.consultaOrdenesHistoricos.empresas"/></td>
			<td><html:select property="empresaId" styleId="empresas" 
				onchange="javascript:DoSubmit('historicos.do?do=cargarServicios');">				
				<html:option value="00" >
					<bean:message key="global.seleccionar" />
				</html:option>
				
				<html:options collection="empresas" property="id" 
					labelProperty="descripcion" />
			</html:select></td>
			<td><bean:message key="historicos.consultaOrdenesHistoricos.servicios"/></td>
			<td><html:select property="servicioId" styleId="servicios" >
				<html:option value="00">
					<bean:message key="global.seleccionar" />
				</html:option>
				<logic:notEmpty name="servicios">
					<html:options collection="servicios" property="id"
						labelProperty="descripcion" />
				</logic:notEmpty>

			</html:select></td>
		</tr>

		<tr>
			<td><bean:message key="historicos.consultaOrdenesHistoricos.inicio"/></td>
			<td>
			<table style="border-collapse: collapse;" cellpadding="0"
				cellspacing="0">
				<tr>
					<td><html:text property="fechaInicial" styleId="txtFecha1"
						readonly="true" size="10" /></td>
					<td><img src="img/cal.gif" alt="calendario" name="disp2"
						border="0" id="disp2" style="cursor: pointer;"
						onmouseover="fecha2('txtFecha1',this)" /></td>
				</tr>
			</table>
			</td>


			<td><bean:message key="historicos.consultaOrdenesHistoricos.final"/></td>

			<td>
			<table style="border-collapse: collapse;" cellpadding="0"
				cellspacing="0">
				<tr>
					<td><html:text property="fechaFinal" styleId="txtFecha2"
						size="10" /></td>
					<td><img src="img/cal.gif" alt="calendario" name="disp2"
						border="0" id="disp2" style="cursor: pointer;"
						onmouseover="fecha2('txtFecha2',this)" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td><bean:message key="historicos.consultaOrdenesHistoricos.referencia"/></td>
			<td> <html:text property="referencia" size="40" /> </td>			
		</tr>
		<tr align="right">
			<td class="CellColRow5" colspan="4"><img src="img/bt-buscar.png"
				align="middle" onMouseOver="this.src='img/bt-buscarB.png'"
				onMouseOut="this.src='img/bt-buscar.png'"
				onClick="javascript:validar(document.forms[0],'cargarOrdenes','');" />
			</td>
		</tr>
	</table>	
	<logic:notEmpty name="ordenes">
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>
				<td valign="middle" align="left" class="Title">					
				</td>

				<td align="right" class="exportacion" valign="middle">
					<a	href="historicos.do?do=exportarOrdenes&accion=1"> <img
					src='img/exp/ico_excel.png' alt="Exportar Excel" /></a> 
					
					<a	href="historicos.do?do=exportarOrdenes&accion=2"> <img
					src='img/exp/ico_text.png' alt="Exportar Texto" /></a> 
					
					<a	target="_blank"
					href="historicos.do?do=exportarOrdenes&accion=3"> <img
					src='img/exp/ico_html.png' alt="Exportar HTML" /></a> 
					<a
					target="_blank"
					href="historicos.do?do=exportarOrdenes&accion=0"> <img
					src='img/exp/ico_printer.png' alt="Impresion" /></a></td>

			</tr>
			<tr>
				<td align="left" valign="middle">
				</td>

				<td class="paginado" align="right" valign="middle"><logic:equal
					value="true" name="paginado" property="existeAnterior">
					<a
						href="historicos.do?do=paginarOrdenes&nroPagina=<bean:write name="paginado" property="paginaInicio"/>">
					<img src='img/paginacion/ico_inicio.png' /></a>
					<a
						href="historicos.do?do=paginarOrdenes&nroPagina=<bean:write name="paginado" property="paginaAnterior"/>">
					<img src='img/paginacion/ico_anterior.png' /></a>
				</logic:equal> <logic:notEqual value="true" name="paginado"
					property="existeAnterior">
					<img src='img/paginacion/ico_inicio_dis.png' />
					<img src='img/paginacion/ico_anterior_dis.png' />
				</logic:notEqual> <label> <bean:write name="paginado" property="encabezado" />
				</label> <logic:equal value="true" name="paginado"
					property="existeSiguiente">
					<a
						href="historicos.do?do=paginarOrdenes&nroPagina=<bean:write name="paginado" property="paginaSiguiente"/>">
					<img src='img/paginacion/ico_siguiente.png' /></a>
					<a
						href="historicos.do?do=paginarOrdenes&nroPagina=<bean:write name="paginado" property="paginaFinal"/>">
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
				<th>Orden</th>
				<th>Cuenta</th>
				<th>Moneda</th>
				<th>Referencia</th>
				<th>Estado</th>
				<th align="center">Inicio</th>
				<th align="center">Vencimiento</th>
				<th align="right">Nro Items</th>
				<th align="right">Valor</th>

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
					<a href="historicos.do?do=cargarDetallesOrden&idOrden=<bean:write name="orden" property="idSobre" />">
						<bean:write name="orden" property="idSobre" />
					</a>
				</td>
				<td><bean:write name="orden" property="cuenta" /></td>
				<td><bean:write name="orden" property="codigoMoneda" /></td>
				<td><bean:write name="orden" property="referencia" /></td>
				<td><bean:write name="orden" property="estadoSobre" /></td>
				<td align="center"><bean:write name="orden"	property="fechaInicio" /></td>
				<td align="center"><bean:write name="orden"
					property="fechaVencimiento" /></td>
				<td align="right"><bean:write name="orden" property="nroItems" /></td>
				<td align="right"><bean:write name="orden"
					property="valorSobre" /></td>
				</tr>


			</logic:iterate>
		</table>

	</logic:notEmpty>
</html:form>
</body>
</html>

<%-- 
    Document   : relacionesBanco
    Created on : 04-dic-2008, 12:43:27
    Author     : jwong
--%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>

<html>
<head>
<title>Cronograma de Pagos</title>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
	
<link href="css/Styles.css" rel="stylesheet" type="text/css">
<link href="css/table.css" rel="stylesheet" type="text/css">
<link href="css/cash.css" rel="stylesheet" type="text/css">

<style type="text/css">
@import url(calendario/calendar-system.css);
</style>
<script type="text/javascript" src="calendario/calendar.js"></script>
<script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
<script type="text/javascript" src="calendario/calendar-setup.js"></script>
<script type="text/javascript" src="js/Functions.js"></script>

<style type="text/css">

a.linkPrestamo {
	text-decoration: underline;
}

a.paginado {
	padding-right: 10px;
	font-size: 12px;
}

label.paginado {
	color: blue;
	font-weight: bolder;
	font-size: 15px;
}

</style>


</head>
<body style="padding: 15px;">
<html:form action="consultarSaldos.do">
	<h1 id="titulo">Cronograma de <logic:equal value="1" name="tipo">
		Pagos 
		</logic:equal> <logic:equal value="2" name="tipo">
		Deudas
		</logic:equal> de la Cuenta <bean:write name="prestamo" property="nroCuenta" />
	</h1>
	<table id="ver-zebra" width="100%">

		<colgroup>
			<col class="vzebra-odd" />
			<col class="vzebra-even" />
			<col class="vzebra-odd" />
			<col class="vzebra-even" />
			<col class="vzebra-odd" />
			<col class="vzebra-even" />
		</colgroup>
		<tr>
			<td>Nro Cuotas</td>
			<td><bean:write name="prestamo" property="nroCuotas" /></td>

			<td>Tasa</td>
			<td><bean:write name="prestamo" property="tasa" />%</td>
			<td>Fecha Apertura</td>
			<td><bean:write name="prestamo" property="strFechaApertura" />
			</td>

		</tr>

		<tr>
			<td>Saldo</td>
			<td><bean:write name="prestamo" property="saldo" /></td>
			<td>Total Interes</td>
			<td><bean:write name="prestamo" property="interes" /></td>
			<td>Fecha Vencimiento</td>
			<td><bean:write name="prestamo" property="strFechaVencimiento" />
			</td>
		</tr>

		<tr>
			<td>Total Mora</td>
			<td><bean:write name="prestamo" property="mora" /></td>
			<td>IGV</td>
			<td><bean:write name="prestamo" property="IGV" /></td>
			<td>Int. Comp. Vencido</td>
			<td><bean:write name="prestamo" property="ICV" /></td>

		</tr>


		<tr>
			<td>Comision</td>
			<td><bean:write name="prestamo" property="comision" /></td>
			<td>Seguro</td>
			<td><bean:write name="prestamo" property="seguro" /></td>
			<td>Total Pagar</td>
			<td><bean:write name="prestamo" property="total" /></td>
		</tr>
	</table>
	<table width="100%">
		
		<tr >
		
			<td><a href="consultarSaldos.do?do=buscarRelacionesBco"> <img
				src="img/bt-volver.png" align="middle"
				onMouseOver="this.src='img/bt-volver2.png'"
				onMouseOut="this.src='img/bt-volver.png'" border="0"> </a></td>
			<td  class="exportacion" align="right">				
				<a    
					href="consultarSaldos.do?do=exportarCronograma&accion=1&tipoCronograma=<bean:write name="tipo" />&nroCuenta=<bean:write name="prestamo" property="nroCuenta" />">				
					<img src='img/excel.png' alt="Exportar Excel" align="middle" /></a>				
				<a  
					href="consultarSaldos.do?do=exportarCronograma&accion=2&tipoCronograma=<bean:write name="tipo" />&nroCuenta=<bean:write name="prestamo" property="nroCuenta" />">				
					<img src='img/exp/ico_text.png' alt="Exportar Texto" align="middle" /></a>				
					
				<a  target="_blank"
					href="consultarSaldos.do?do=exportarCronograma&accion=3&tipoCronograma=<bean:write name="tipo" />&nroCuenta=<bean:write name="prestamo" property="nroCuenta" />">				
					<img src='img/exp/ico_html.png' alt="Exportar HTML" align='middle'/></a>
			
				<a  target="_blank" 
					href="consultarSaldos.do?do=exportarCronograma&accion=0&tipoCronograma=<bean:write name="tipo" />&nroCuenta=<bean:write name="prestamo" property="nroCuenta" />">				
					<img src='img/printer.png'   alt="Impresion"  align='middle'/></a>	
			</td>
			
		
		</tr>
	</table>
	<logic:notEqual value="0"  name="prestamo" property="nroCuotas">	
	<table width="100%">
		<tr>
			<td class="paginado" align="right" valign="middle"><logic:equal
					value="true" name="paginado" property="existeAnterior">
					<a
						href="consultarSaldos.do?do=paginarCronograma&nroPagina=<bean:write name="paginado" property="paginaInicio"/>">
					<img src='img/paginacion/ico_inicio.png' /></a>
					<a
						href="consultarSaldos.do?do=paginarCronograma&nroPagina=<bean:write name="paginado" property="paginaAnterior"/>">
					<img src='img/paginacion/ico_anterior.png' /></a>
				</logic:equal> <logic:notEqual value="true" name="paginado"
					property="existeAnterior">
					<img src='img/paginacion/ico_inicio_dis.png' />
					<img src='img/paginacion/ico_anterior_dis.png' />
				</logic:notEqual> <label> <bean:write name="paginado" property="encabezado" />
				</label> <logic:equal value="true" name="paginado"
					property="existeSiguiente">
					<a
						href="consultarSaldos.do?do=paginarCronograma&nroPagina=<bean:write name="paginado" property="paginaSiguiente"/>">
					<img src='img/paginacion/ico_siguiente.png' /></a>
					<a
						href="consultarSaldos.do?do=paginarCronograma&nroPagina=<bean:write name="paginado" property="paginaFinal"/>">
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
			<th scope="col" align="center">Nro</th>
			<th scope="col" align="center">Fecha</th>
			<th scope="col">Principal</th>
			<th scope="col" align="right">Interes</th>
			<th scope="col">Comision</th>
			<th scope="col" align="right">Mora</th>
			<th scope="col" align="right">IGV</th>
			<th scope="col" align="right">ICV</th>
			<th scope="col" align="right">Total Cuota</th>
			<th scope="col" align="right">Seguro</th>
		</tr>
		<logic:iterate id="cuota" name="cuotas" indexId="row"	>

			<bean:define id="mod"
				value="<%= String.valueOf((row.intValue()) % 2)%>" />
			<logic:equal name="mod" value="0">
				<tr>
					<td align="center"><bean:write name="cuota" property="nro"></bean:write>
					</td>
					<td align="center"><bean:write name="cuota"
						property="strFechaVencimiento"></bean:write></td>
					<td align="right"><bean:write name="cuota"
						property="principal"></bean:write></td>
					<td align="right"><bean:write name="cuota" property="interes"></bean:write>
					</td>
					<td align="right"><bean:write name="cuota" property="comision"></bean:write>
					</td>
					<td align="right"><bean:write name="cuota" property="mora"></bean:write>
					</td>
					<td align="right"><bean:write name="cuota"
						property="IGV"></bean:write></td>
					<td align="right"><bean:write name="cuota"
						property="interesCompensatorio"></bean:write></td>
					<td align="right"><bean:write name="cuota"
						property="totalCuota"></bean:write></td>
					<td align="right"><bean:write name="cuota" property="seguro"></bean:write>
					</td>
				</tr>
			</logic:equal>
			<logic:notEqual name="mod" value="0">
				<tr class="odd">

					<td align="center"><bean:write name="cuota" property="nro"></bean:write>
					</td>
					<td align="center"><bean:write name="cuota"
						property="strFechaVencimiento"></bean:write></td>
					<td align="right"><bean:write name="cuota"
						property="principal"></bean:write></td>
					<td align="right"><bean:write name="cuota" property="interes"></bean:write>
					</td>
					<td align="right"><bean:write name="cuota" property="comision"></bean:write>
					</td>
					<td align="right"><bean:write name="cuota" property="mora"></bean:write>
					</td>
					<td align="right"><bean:write name="cuota"
						property="IGV"></bean:write></td>
					<td align="right"><bean:write name="cuota"
						property="interesCompensatorio"></bean:write></td>
					<td align="right"><bean:write name="cuota"
						property="totalCuota"></bean:write></td>
					<td align="right"><bean:write name="cuota" property="seguro"></bean:write>
					</td>

				</tr>

			</logic:notEqual>


		</logic:iterate>
	</table>
	<table width="100%">
		<tr>
			<td><a href="consultarSaldos.do?do=buscarRelacionesBco"> <img
				src="img/bt-volver.png" align="middle"
				onMouseOver="this.src='img/bt-volver2.png'"
				onMouseOut="this.src='img/bt-volver.png'" border="0"> </a>
			</td>			
		</tr>
	</table>
	</logic:notEqual>
</html:form>
</body>
</html>
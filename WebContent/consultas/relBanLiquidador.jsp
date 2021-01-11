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



</head>
<body>
<html:form action="consultarSaldos.do" onsubmit="return false">
	<h1 id="titulo">Consulta Liquidador de la Cuenta <bean:write name="liquidador" property="nroCuenta" /></h1>
	<table id="ver-zebra">
		<colgroup>
			<col class="vzebra-even" width="110" />
			<col class="vzebra-odd" />
			<col class="vzebra-even" width="110" />
			<col class="vzebra-odd" />
		</colgroup>		

		<tr>
			<td>Fecha Liquidacion</td>
			<td><bean:write name="liquidador" property="strFechaLiquidacion" />
			</td>

			<td>Nro Cuotas</td>
			<td>
				<bean:define id="cuotas" name="nroCuotas" />
				<input name="nroCuotas" size="10" value="<%=cuotas.toString()%>"> 
				<img src="img/bt-buscar.png" align="middle"
				onMouseOver="this.src = 'img/bt-buscarB.png'" onMouseOut="this.src = 'img/bt-buscar.png'" title="Liquidador"
				onClick="javascript:DoSubmit('consultarSaldos.do?do=buscarLiquidador&nroCuenta=<bean:write	name="liquidador" property="nroCuenta" />');" />

			</td>
		</tr>
		<tr>
			<td>Cliente</td>
			<td><bean:write name="liquidador" property="cliente" /></td>


			<td>Moneda</td>
			<td><bean:write name="liquidador" property="moneda" />
			</td>

		</tr>

		<tr>
			<td>Fecha Vencimiento Cuota</td>
			<td><bean:write name="liquidador" property="strFechaVencimiento" />
			</td>

			<td>Fecha Vcto. Proxima Cuota</td>
			<td><bean:write name="liquidador"
				property="strFechaVencimientoProximaCuota" /></td>
		</tr>
		<tr>
			<td colspan="4" align="CENTER">INFORMACION FINANCIERA</td>

		</tr>
		<tr>
			<td>Interes Comp. Vencido</td>
			<td><bean:write name="liquidador" property="ICV" /></td>

			<td>Interes Moratorio</td>
			<td><bean:write name="liquidador" property="interesMoratorio" />
			</td>
		</tr>
		<tr>
			<td>Comision Couta Vencida</td>
			<td><bean:write name="liquidador"
				property="comisionCuotaVencida" /></td>

			<td>Pago Principal</td>
			<td><bean:write name="liquidador" property="pagoPrincipal" />
			</td>
		</tr>
		<tr>
			<td>Pago Interes</td>
			<td><bean:write name="liquidador" property="pagoInteres" /></td>

			<td>Seguros</td>
			<td><bean:write name="liquidador" property="seguro" /></td>
		</tr>
		<tr>
			<td>IGV</td>
			<td><bean:write name="liquidador" property="IGV" /></td>

			<td>Seguro Todo Riesgo</td>
			<td><bean:write name="liquidador" property="seguroTodoRiesgo" />
			</td>
		</tr>
		<tr>
			<td>Portes</td>
			<td><bean:write name="liquidador" property="portes" /></td>
			

			<td>ITF</td>
			<td><bean:write name="liquidador" property="ITF" /></td>
		</tr>
		<tr>
			<td>Total a Pagar</td>
			<td><bean:write name="liquidador" property="total" /></td>
			<td>Total + ITF</td>
			<td><bean:write name="liquidador" property="totalITF" /></td>
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
					href="consultarSaldos.do?do=exportarLiquidador&accion=1&nroCuenta=<bean:write name="liquidador" property="nroCuenta" />&nroCuotas=<%=cuotas.toString()%>">				
					<img src='img/exp/ico_excel.png' alt="Exportar Excel" align='middle'/></a>
				<a  
					href="consultarSaldos.do?do=exportarLiquidador&accion=2&nroCuenta=<bean:write name="liquidador" property="nroCuenta" />&nroCuotas=<%=cuotas.toString()%>">				
					<img src='img/exp/ico_text.png' alt="Exportar Texto" align='middle'/></a>
				<a  target="_blank"
					href="consultarSaldos.do?do=exportarLiquidador&accion=3&nroCuenta=<bean:write name="liquidador" property="nroCuenta" />&nroCuotas=<%=cuotas.toString()%>">				
					<img src='img/exp/ico_html.png' alt="Exportar HTML" align='middle'/></a>
			
				<a  target="_blank" 
					href="consultarSaldos.do?do=exportarLiquidador&accion=0&nroCuenta=<bean:write name="liquidador" property="nroCuenta" />&nroCuotas=<%=cuotas.toString()%>">				
					<img src='img/printer.png'   alt="Impresion"  align='middle'/></a>	
			</td>
			
		
		</tr>
	</table>
	
</html:form>
</body>
</html>
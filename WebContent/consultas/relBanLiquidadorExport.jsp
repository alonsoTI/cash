<%
	String accion = request.getParameter("accion");
	if (accion.equals("1")) {
		response.setHeader("Content-Disposition",
				"filename=cronograma.xls");
		response.setContentType("application/vnd.ms-excel");
	}
	if (accion.equals("2")) {
		response.setHeader("Content-Disposition",
				"filename=cronograma.pdf");
		response.setContentType("application/pdf");
	}
%> 
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>

<html>
<head>
<title>Consulta de Liquidador</title>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<link href="css/Styles.css" rel="stylesheet" type="text/css">
<link href="css/table.css" rel="stylesheet" type="text/css">
<link href="css/cash.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
    
    function imprimir( ) {         
            window.print();
            window.close();        
    }    
  </script>


<script type="text/javascript" src="calendario/calendar.js"></script>
<script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
<script type="text/javascript" src="calendario/calendar-setup.js"></script>
<script type="text/javascript" src="js/Functions.js"></script>

<style type="text/css">
a.linkPrestamo {
	text-decoration: underline;
}
</style>


</head>
<body style="padding: 15px;" <%if (accion.equals("0")) {%> onload="imprimir()" <%}%>>
	<img src="img/logo-Financiero-AMA.gif" height="40">
	
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
				<bean:write	name="nroCuotas" />			
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

</body>
</html>
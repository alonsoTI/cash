
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
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Cronograma</title>

<link href="css/table.css" rel="stylesheet" type="text/css">
<link href="css/cash.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
    
    function imprimir( ) { //v3.0        
            window.print();
            window.close();        
    }    
  </script>
</head>
<body <%if (accion.equals("0")) {%> onload="imprimir()" <%}%>>

<img src="img/logo-Financiero-AMA.gif" height="40">
<h1 id="titulo">Cronograma de <logic:equal value="1" name="tipo">
		Pagos 
		</logic:equal> <logic:equal value="2" name="tipo">
		Deudas
		</logic:equal> de la Cuenta <bean:write name="prestamo" property="nroCuenta" /></h1>





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
		<td><bean:write name="prestamo" property="strFechaApertura" /></td>

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
	<logic:iterate id="cuota" name="prestamo" indexId="row"
		property="cuotas">

		<bean:define id="mod"
			value="<%= String.valueOf((row.intValue()) % 2)%>" />
		<logic:equal name="mod" value="0">
			<tr>
				<td align="center"><bean:write name="cuota" property="nro"></bean:write>
				</td>
				<td align="center"><bean:write name="cuota"
					property="strFechaVencimiento"></bean:write></td>
				<td align="right"><bean:write name="cuota" property="principal"></bean:write></td>
				<td align="right"><bean:write name="cuota" property="interes"></bean:write>
				</td>
				<td align="right"><bean:write name="cuota" property="comision"></bean:write>
				</td>
				<td align="right"><bean:write name="cuota" property="mora"></bean:write>
				</td>
				<td align="right"><bean:write name="cuota" property="IGV"></bean:write></td>
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
				<td align="right"><bean:write name="cuota" property="principal"></bean:write></td>
				<td align="right"><bean:write name="cuota" property="interes"></bean:write>
				</td>
				<td align="right"><bean:write name="cuota" property="comision"></bean:write>
				</td>
				<td align="right"><bean:write name="cuota" property="mora"></bean:write>
				</td>
				<td align="right"><bean:write name="cuota" property="IGV"></bean:write></td>
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





</body>
</html>
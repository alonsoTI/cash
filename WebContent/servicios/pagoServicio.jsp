<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:empty name="usuarioActual" scope="session">
	<logic:redirect href="cierraSession.jsp" />
</logic:empty>
<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">

<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">



<title>Pago Servicio</title>
<link href="css/Styles.css" rel="stylesheet" type="text/css">
<link href="css/cash.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
        	function back(){
            	var frm = document.forms[0];
            	var url = "<bean:write property='m_Back' name='success'/>";
            	url = url.replace(/amp;/gi,'');            
            	frm.action = url;
            	frm.submit();
        	}        
          
        </script>

</head>
<body style="margin: 0px 20px;">
<html:form action="pagoServicio.do">

	<h1><bean:write name='success' property='m_Titulo' /></h1>

	<table width="100%">
		<tr>
			<td class="info" style="height: 30px; font-size: 15px;"><bean:write
				name='success' property='m_Mensaje' /></td>
		</tr>
	</table>
	<table id="formulario">
		<colgroup>
			<col class="label" style="padding-left: 18px;" />
			<col class="input-read" />
		</colgroup>
		<logic:iterate name="alsuccess" id="beansuccess" indexId="j">
			<tr height="24px" valign="middle">
				<td><bean:write name="beansuccess" property="m_Label" /></td>
				<logic:notEmpty name="beansuccess" property="m_Mensaje">
					<td><bean:write name="beansuccess" property="m_Mensaje" /></td>
				</logic:notEmpty>
				<logic:empty name="beansuccess" property="m_Mensaje">
					<td></td>
				</logic:empty>
			</tr>
		</logic:iterate>
		
	</table>
	
	<br />

	<logic:notEmpty name="recibos">
		<table id="hor-zebra">
			<tr>
				<th scope="col">Recibo</th>
				<th scope="col">Cliente</th>
				<th scope="col" align="center">Emision</th>
				<th scope="col" align="right">Importe</th>

			</tr>
			<logic:iterate id="recibo" name="recibos" indexId="row">

				<bean:define id="mod"
					value="<%= String.valueOf((row.intValue()) % 2)%>" />
				<logic:equal name="mod" value="0">
					<tr>
						<td><bean:write name="recibo" property="nroRecibo"></bean:write>
						</td>
						<td><bean:write name="recibo" property="cliente"></bean:write></td>
						<td align="center"><bean:write name="recibo"
							property="fechaEmision"></bean:write></td>
						<td align="right"><bean:write name="recibo"
							property="importe"></bean:write></td>

					</tr>
				</logic:equal>
				<logic:notEqual name="mod" value="0">
					<tr class="odd">

						<td><bean:write name="recibo" property="nroRecibo"></bean:write>
						</td>
						<td><bean:write name="recibo" property="cliente"></bean:write></td>
						<td align="center"><bean:write name="recibo"
							property="fechaEmision"></bean:write></td>
						<td align="right"><bean:write name="recibo"
							property="importe"></bean:write></td>

					</tr>

				</logic:notEqual>


			</logic:iterate>
			


		</table>

	</logic:notEmpty>
	<br/>
	<table align="center" width="90%">
	
		<tr>
			<td align="left"><img src="img/bt-aceptar.png" align="middle"
				onMouseOver="this.src='img/bt-aceptar2.png'"
				onMouseOut="this.src='img/bt-aceptar.png'"
				onClick="javascript:back();" /></td>
			<td align="right" class="exportacion"><a
				href="pagoServicio.do?do=imprimirPagoServicio&idOrden=<%= request.getAttribute("orden") %>&idServicio=<%= request.getAttribute("servicio") %>" target="_blank">
			<img src="img/exp/ico_printer.png" /> </a></td>
		</tr>
	</table>

</html:form>
</body>
</html>

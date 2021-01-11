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

<title>Transferencia</title>
<link href="css/Styles.css" rel="stylesheet" type="text/css">
<link href="css/cash.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/JavaScript">
        	function back(){
            	var frm = document.forms[0];
            	var url = "comprobantes.do?do=buscarOrdenesLinea";
            	url = url.replace(/amp;/gi,'');            
            	frm.action = url;
            	frm.submit();
        	}    
          
        </script>

</head>
<body style="margin: 0px 20px;" >
<html:form action="Orden.do" onsubmit="return false;">
		<h1>Orden de Pago de Transferencia </h1>
	
	
	<table id="formulario" >
		<colgroup>
			<col class="label" style="padding-left: 18px;"/>
			<col class="input-read" />			
		</colgroup>
		<logic:iterate name="alsuccess" id="beansuccess" indexId="j">
			<tr height="24px" valign="middle" >
				<td><bean:write name="beansuccess" property="m_Label" />
				</td>
				<logic:notEmpty name="beansuccess" property="m_Mensaje">
					<td>
						<bean:write name="beansuccess" property="m_Mensaje" />
					</td>
				</logic:notEmpty>
				<logic:empty name="beansuccess" property="m_Mensaje">
					<td>
						
					</td>
				</logic:empty>
			</tr>
		</logic:iterate>
	</table>
	
	<br/>
	
	

	<br/>
	<table align="center"	width="90%">
		
		<tr>
			<td align="left"><img src="img/bt-volver.png" align="middle"
				onMouseOver="this.src='img/bt-volver2.png'"
				onMouseOut="this.src='img/bt-volver.png'"
				onClick="javascript:back();" /></td>
				
			<td align="right" class="exportacion"><a
				href="transferencias.do?do=imprimirTransferencia&modulo=I52&idOrden=<%= request.getAttribute("orden") %>&idServicio=<%= request.getAttribute("servicio") %>" target="_blank">
			<img src="img/exp/ico_printer.png" /> </a></td>			
		</tr>
	</table>

</html:form>
</body>
</html>

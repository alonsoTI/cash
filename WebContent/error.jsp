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
<link href="css/cash.css" rel="stylesheet" type="text/css">

<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">
<title>Portal Cash</title>
</head>
	<body>
	
		<h4><bean:message key="errors.mensajeUsuario" /></h4>
	
		<h5 style="color: #0062ac;">
			<logic:notEmpty name="mensaje">
				<bean:write name="mensaje" />
			</logic:notEmpty> 
			<logic:empty name="mensaje">
				<bean:message key="errors.errorInesperado" />
			</logic:empty>
		</h5>
		
		<h5 style="color: #0062ac;">
			<logic:notEmpty name="sugerencia1">
				<bean:write name="sugerencia1" />
			</logic:notEmpty>			
		</h5>
		
		<h5 style="color: #0062ac;">
			<logic:notEmpty name="sugerencia2">
				<bean:write name="sugerencia2" />
			</logic:notEmpty>			
		</h5>
	</body>
</html>
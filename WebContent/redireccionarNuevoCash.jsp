<%-- 
    Document   : Registro de Migracion de Usuarios
    Created on : 02-ene-2017, 10:18:58
    Author     : andqui
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript">
function redireccionar(){

	//alert('Hola Luz...');
	window.location.href = '<bean:write name = "url_nuevo_cash" />';
	
}
</script>
<title></title>
</head>
<body onload="redireccionar();">


</body>
</html>
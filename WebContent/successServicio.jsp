<%-- 
    Document   : success
    Created on : Dec 17, 2008, 1:48:33 AM
    Author     : Elvis
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

        <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="0">

        <style type="text/css">
        body {
            background: url(img/fondo.gif) no-repeat fixed;
            background-position: right;
        }
        </style>

        <title>Success</title>
        <link href="css/Styles.css" rel="stylesheet" type="text/css">
        
       
    </head>
    <body>
    <form>
    	<table cellpadding='2' cellspacing='2' border='0' align='center'
			width='90%' bordercolor='#0066CC'>
			<tr>
				<td>
					<h5>Estimado Cliente, Ud. podr&aacute; realizar sus operaciones en el horario de 7:00 a.m. - 10:00 p.m.</h5>
					<h5> Gracias por su Preferencia.</h5>
				</td>
			</tr>
			<tr>
				<td>
					<img src="img/bt-volver.png" align="middle" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="history.back()"/>
				</td>
			</tr>
		</table>   	

    </form>
       
    </body>
</html>

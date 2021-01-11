<%-- 
    Document   : cabeceraSesion
    Created on : 10-dic-2008, 12:15:15
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
  <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Expires" CONTENT="0">
  
  <link href="css/Styles.css" rel="stylesheet" type="text/css">
  <script language="javascript">
    function cerrarSession(){
        parent.location = "login.do?do=cerrarSesion";
    }

    function cargarInicio(){
        parent.location = "menuFlex.jsp";
        //parent.location = "animacionFlash.jsp";
    }
  </script>
</head>
<body bgcolor="#ffffff">
 
<table align="right" width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td class="celdaCabecera">&nbsp;</td></tr>
    <tr><td class="celdaCabecera">&nbsp;</td></tr>
    <tr valign="baseline">
        <td align="right" class="celdaCabecera" valign="baseline" height="100%">
            <bean:write name="usuarioActual" scope="session" property="m_Fecha"/>
            &nbsp;:::&nbsp;
            <bean:write name="usuarioActual" scope="session" property="m_MsjBienvenida"/>
            &nbsp;
            <span class='CellColRowUser'><bean:write name="usuarioActual" scope="session" property="m_Apellido"/>&nbsp;<bean:write name="usuarioActual" scope="session" property="m_Nombre"/></span>
            &nbsp;:::&nbsp;

            <span class='CellColRowUser'>
            
            <% if(session.getAttribute("perfilCash").toString().equals("S")){%>
            <a href="#"><bean:write name="usuarioActual" scope="session" property="m_PaginaInicio"/></a>
            <%} else {%>
            <a href="javascript:cargarInicio();"><bean:write name="usuarioActual" scope="session" property="m_PaginaInicio"/></a>
            <%}%>



            
            </span>
            &nbsp;:::&nbsp;
            <span class='CellColRowUser'><a href="javascript:cerrarSession();"><bean:write name="usuarioActual" scope="session" property="m_PaginaClose"/></a></span>
            &nbsp;:::&nbsp;
        </td>
    </tr>
</table>
</body>
</html>
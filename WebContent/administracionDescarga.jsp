<%-- 
    Document   : consultaMovHistorico
    Created on : 04-dic-2008, 9:44:38
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>

<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">

  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>
  <script type="text/javascript" src="config/javascript.js"></script>
   
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
  <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Expires" CONTENT="0">

  <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
  </style>
  
  <script language="JavaScript">
    <!--
   
	function cancelar(){
		location.href = "mantenimientoPersonal.jsp";
	}
        
    function enviar(tipo) {
        document.getElementById("arh_fmt").style.visibility = "hidden";
        document.getElementById("val_arch").style.visibility = "hidden";
        var frm=document.forms[0];
        frm.action="administracion.do?do=cargarAdministracionDescargar&tipo="+tipo;
        frm.submit();
    }

    -->
</script>

</head>
<body>
<html:form action="administracion.do">
<logic:notEqual name="habil" value="1">
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr valign="baseline">
        <td align="center" class="TitleRowCierraSession" valign="baseline" height="100%">
             <bean:message key="errors.authorization"/>
        </td>
    </tr>
  </table>
</logic:notEqual>
<logic:equal name="habil" value="1">
  <table width="100%" CELLSPACING="0" CELLPADDING="4">
    <tr>
      <td valign="middle" align="left" class="Title"><bean:message key="administracion.title.descargas"/></td>
      
    </tr>
  </table>
  
  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class="CellColRow">Empresa:</td>
      <td class="CellColRow2">
          <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow">
          <logic:notEmpty name="listaEmpresas">
              <html:options collection="listaEmpresas" property="cemIdEmpresa" labelProperty="demNombre"/>
          </logic:notEmpty>
        </html:select>
      </td>
    </tr>
    </table>
    <br>
    <table cellpadding="2" cellspacing="2" border="0" align="center" width="70%" >
    <tr>
        <td class="CellColRowRight" width="20%">Archivo de Formatos</td>
        <td width="50%"><img src="img/disk.png" align="middle" onClick="javascript:enviar('1');"/><div id="arh_fmt" class="CellColCargo2"><logic:notEmpty name="existeFormato"><%= request.getAttribute("existeFormato") %></logic:notEmpty></div></td>
        <!--td class="CellColCargo2" width="40%"></td-->
    </tr>
    <tr>
        <td class="CellColRowRight" width="20%">Validador de Archivos</td>
        <td width="50%"><img src="img/disk.png" align="middle" onClick="javascript:enviar('2');"/><div id="val_arch" class="CellColCargo2"><logic:notEmpty name="existeFile"><%= request.getAttribute("existeFile") %></logic:notEmpty></div></td>
        <!--td class="CellColCargo2" width="40%"></td-->
    </tr>
    <tr>
            <td colspan="2" ></td>
    </tr>
    <tr>
            <td colspan="2" ></td>
    </tr>
    <tr>
            <td colspan="2" align="center">Para descargar el JRE de Java hacer click <a href="<%=request.getAttribute("link_jre")%>" target="Sun MicroSystem">Aqu&iacute;</a></td>
    </tr>
 <%--logic:notEmpty name="existeFile">
      <tr>
            <td colspan="2" align="center" ><%= request.getAttribute("existeFile") %></td>
      </tr>
</logic:notEmpty--%>
</table>
</logic:equal>
</html:form>
</body>
</html>
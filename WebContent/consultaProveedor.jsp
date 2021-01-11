<%-- 
    Document   : consultaServicio
    Created on : 10-mar-2009, 11:57:46
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">

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
	function buscar(){
      var frm = document.forms[0];
	  frm.action = "pagoServicio.do?do=buscarPagoAgua";
      frm.submit();
    }
    function back(){
        var frm = document.forms[0];
        var url = "pagoServicio.do?do=cargarServicio";
        frm.action = url;
        frm.submit();
    }
	-->
</script>

</head>
<body>
<html:form action="pagoServicio.do">
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
        <td valign="middle" align="left" class="Title"><bean:write name="titulo" /></td>
	</tr>
  </table>

  <logic:notEmpty name="listaEmpProveedor">
  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
    
    <logic:iterate id="empProveedor" name="listaEmpProveedor" indexId="i">
        <bean:define name="empProveedor" property= "m_Accion" id="rname" type="java.lang.String" />
        <tr>
            <td class='CellColRow7'>
                <html:link href="<%= rname %>" name="empProveedor" property="parametrosUrl">
                    <bean:write name="empProveedor" property="m_Nombre"/>
                </html:link>
            </td>
        </tr>
    </logic:iterate>
  </table>
  </logic:notEmpty>

  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td align='center'><br></td>
    </tr>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
    </tr>
  </table>
  </logic:notEmpty>

  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
        <tr>
            <td align='center'><br></td>
        </tr>        
    </table>
    <input type="hidden" name="data" id="data" value="<%= request.getAttribute("data") %>">
</html:form>
</body>
</html>

<%-- 
    Document   : pagoTelefonia
    Created on : 10-mar-2009, 14:25:13
    Author     : jwong
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">

  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>

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
	-->
</script>

</head>
<body>
<html:form action="pagoServicio.do">
  <html:hidden property="m_CodEmpProveedor" styleId="m_CodEmpProveedor"/>
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      <td valign="middle" align="left" class="Title">Pago Telefonía</td>
	</tr>
  </table>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
    <tr>
      <td class="TitleLateral">Departamento:</td>
      <td class="CellColLateralIn">
        <html:select property="m_TipoMoneda" styleId="m_TipoMoneda" styleClass="CellColRow8">
            <logic:notEmpty name="listaTipoMoneda">
                <html:options collection="listaTipoMoneda" property="id.clfCode" labelProperty="dlfDescription"/>
            </logic:notEmpty>
        </html:select>
	  </td>
    </tr>

    <tr>
      <td class="TitleLateral">N&uacute;mero de Tel&eacute;fono:</td>
      <td class="CellColLateralOut">
        <html:text property="m_NumSuministro" styleId="m_NumSuministro" size="40" maxlength="15" styleClass="CellColRow8" />&nbsp;(*)
	  </td>
    </tr>
    
	<tr>
      <td class="TitleLateral">Referencia:</td>
      <td class="CellColLateralOut">
        <html:text property="m_Referencia" styleId="m_Referencia" size="48" maxlength="48" styleClass="CellColRow8" />&nbsp;(*)
	  </td>
    </tr>
    <tr>
      <td class="CellColLateralIn" colspan="2">(*) Campos requeridos para realizar transacci&oacute;n</td>
    </tr>

	<tr align="right">
      <td colspan="2" class="CellColLateralOut">
        <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:buscar();"/>
	  </td>

    </tr>
  </table>
  <br>
  <div id="cargar"/>
</html:form>
</body>
</html>
<%-- 
    Document   : manualCash
    Created on : 25/02/2009, 05:30:58 PM
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
  
  <script>
    function mostrar() {
      var frm = document.forms[0];
      frm.action = "administracion.do?do=enviaManual";
      frm.submit();
    }
  </script>

  <script type="text/javascript" src="js/embeddedcontent.js" defer="defer"></script>

</head>

<logic:notEqual name="habil" value="1">
<body>
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr valign="baseline">
        <td align="center" class="TitleRowCierraSession" valign="baseline" height="100%">
             <bean:message key="errors.authorization"/>
        </td>
    </tr>
  </table>
</body>
</logic:notEqual>
<logic:equal name="habil" value="1">
<body onload="mostrar();">
  <html:form action="administracion.do"/>

</body>
</logic:equal>
</html>
<%-- 
    Document   : soporteCash
    Created on : 25/02/2009, 05:28:02 PM
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

  <script type="text/javascript" src="config/javascript.js"></script>
  
  <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
  </style>

  <script language="JavaScript">
    <!--
	function imprimir(){
	  window.print();
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
  <table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
    <tr>
      <td valign="middle" align="left"> <bean:message key="administracion.title.soporte"/></td>
    </tr>
  </table>
  
  <!--resultados de la busqueda-->
  <logic:notEmpty name="listaResult">
    <layout:collection name="listaResult" title="DETALLE DE SOPORTE" styleClass="FORM" id="soporte" sortAction="client" width="100%" align="center">
      <layout:collectionItem title="Nombre" property="dsoNombre" sortable="true"/>
      <layout:collectionItem title="Tel&eacute;fono" property="nsoTelefono" sortable="true" />
      <layout:collectionItem title="Anexo" property="nsoAnexo" sortable="true"/>
      <layout:collectionItem title="Celular" property="nsoMovil" />
      <layout:collectionItem title="Email" property="dsoEmail" sortable="true"/>
    </layout:collection>
    <layout:space/>
    
  <div id="comp0" style="display:none">ANS</div>
  <div id="comp1" style="display:none">NUM</div>
  <div id="comp2" style="display:none">NUM</div>
  <div id="comp4" style="display:none">ANS</div>
  </logic:notEmpty>
  
  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
    </tr>
  </table>
  </logic:notEmpty>

</logic:equal>
</html:form>
</body>
</html>
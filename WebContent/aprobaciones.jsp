<%-- 
    Document   : aprobaciones
    Created on : 27/06/2011
    Author     : andy Qui
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

        <title>Aprobadores</title>
        <link href="css/Styles.css" rel="stylesheet" type="text/css">        
    </head>
    <body>
 <html:form action="comprobantes.do">
 
 
  <logic:empty name="listaAprobadores">   
   <table  cellpadding="2" cellspacing="2" border="0" align="center" style="width: 300px" >
  	<tr><td class='CellColRow'>Esta Orden no tiene aprobadores</td></tr>
  </table>   
   </logic:empty>
  <logic:notEmpty name="listaAprobadores">
        <layout:collection name="listaAprobadores" title="LISTADO DE APROBADORES" styleClass="FORM" align="center" id="orden">
              
              <layout:collectionItem title="Orden" property="m_IdOrden"  />
              <layout:collectionItem title="Servicio" property="m_servicio" />              
              <layout:collectionItem title="Fecha" property="m_fecha" />
              <layout:collectionItem title="Hora" property="m_hora" />
              <layout:collectionItem title="Aprobador" property="m_aprobador" />              
      </layout:collection>         
   <layout:space/>
  </logic:notEmpty>
  
</html:form>          
     </body>
</html>

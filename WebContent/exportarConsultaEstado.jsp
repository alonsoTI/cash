<%-- 
    Document   : consultaOrdenes
    Created on : 07-dic-2008, 12:35:14
    Author     : jwong, esilva
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
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
  <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Expires" CONTENT="0">
  <script language="JavaScript">
    function imprimir(){
        window.print();
        window.close();
    }
</script>

</head>
<body onload="javascript:imprimir();">
<html:form action="comprobantes.do">
    <logic:notEmpty name="listaResult">
        
        <layout:collection name="listaResult" title="Movimientos Pagos - Cobros" styleClass="FORM" id="orden" align="center" width="100%">
              <layout:collectionItem title="Empresa">
                  &nbsp;<layout:write name="orden" property="m_Empresa"/>
              </layout:collectionItem>
              <layout:collectionItem title="Servicio">
                  &nbsp;<layout:write name="orden" property="m_Servicio"/>
              </layout:collectionItem>
              <layout:collectionItem title="Id Orden" >
                   &nbsp;<layout:write name="orden" property="m_IdOrden"/>
              </layout:collectionItem>

              <layout:collectionItem title="Estado">
                <center>
                    &nbsp;<layout:write name="orden" property="m_Estado"/>
                </center>
              </layout:collectionItem>
                <layout:collectionItem title="Registros">
                     &nbsp;<layout:write name="orden" property="m_Registros"/>
                </layout:collectionItem>
              <layout:collectionItem title="Moneda">
                <center>
                         <layout:write name="orden" property="m_Moneda"/>
                </center>
              </layout:collectionItem>
              <layout:collectionItem title="Valor Enviado">
                <p align="right">
                    <layout:write name="orden" property="m_Monto"/>
                </p>
              </layout:collectionItem>             
              <layout:collectionItem title="Valor Procesado">
                <p align="right">
                    <layout:write name="orden" property="m_MontoVentanilla"/>
                </p>
              </layout:collectionItem>                        
              
          </layout:collection>
          
  </logic:notEmpty>
    
  <logic:notEmpty name="beanTotalConsulta">
    <table width="100%" cellpadding='2' cellspacing='2' border='0' align='center' >
        <tr>
            <td colspan="4" rowspan="2" width="50%" class="TitleRow6">Total Procesado:</td>
            <td rowspan="2" class="TitleRow4"><bean:write name="beanTotalConsulta" property="m_NumItems"/></td>
            <td class="TitleRow4"><center>PEN</center></td>
            <td class="TitleRow6"><bean:write name="beanTotalConsulta" property="m_MontoSoles"/></td>
            <td class="TitleRow6"><bean:write name="beanTotalConsulta" property="m_MontoVentSoles"/></td>
        </tr>
         <tr>
            <td class="TitleRow4"><center>USD</center></td>
            <td class="TitleRow6"><bean:write name="beanTotalConsulta" property="m_MontoDolares"/></td>
            <td class="TitleRow6"><bean:write name="beanTotalConsulta" property="m_MontoVentDolares"/></td>
         </tr>
    </table>    
  </logic:notEmpty>    
  
</html:form>
</body>
</html>
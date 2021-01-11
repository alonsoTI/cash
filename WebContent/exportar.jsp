<%-- 
    Document   : exportar
    Created on : 27-ene-2009, 14:39:03
    Author     : jwong
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@page import="java.util.*"%>
<%@page import="com.hiper.cash.util.Fecha"%>
<%@page import="com.hiper.cash.xml.bean.BeanCodigoInterbancarioXML"%>
<%--JWONG 29/08/2008 Para que no obtenga datos de cache--%>
<%@ include file="/cache/cache.jsp" %>
<%
  String accion = (String)request.getParameter("accion");
  //obtenemos el formato en el que se descargara el archivo
  String formato = (String)request.getParameter("formato");
  if("save".equalsIgnoreCase(accion)){
    String nomb = Fecha.getFechaActual("yyyyMMdd_HHmmss");
    if("txt".equalsIgnoreCase(formato)){ //formato texto
        String nombre_archivo = "Rep" + nomb + ".txt";
        //enviamos los parametros necesarios al response para que el SO lo tome como formato excel
        response.setHeader("Content-Disposition","attachment; filename=\"" + nombre_archivo + "\"");
        response.setContentType("text/plain");
    }
    else{ //formato excel
        String nombre_archivo = "Rep" + nomb + ".xls";
        //enviamos los parametros necesarios al response para que el SO lo tome como formato excel
        response.setHeader("Content-Disposition","attachment; filename=\"" + nombre_archivo + "\"");
        response.setContentType("application/vnd.ms-excel");
    }
  }
  //para que siempre se actualicen los datos de la respuesta
  response.addHeader("Cache-control","no-cache");
  response.addHeader("Pragma","no-cache");
  response.setDateHeader ("Expires", 0);
if("save".equalsIgnoreCase(accion) && "txt".equalsIgnoreCase(formato)){
  //en caso sea formato texto se debe formatear la salida
  ArrayList lista = (ArrayList)request.getAttribute("listaResult");
  if(lista!=null && lista.size()>0){
      BeanCodigoInterbancarioXML bean = null;
      out.print("Cuenta");out.print("\t");
      out.print("Moneda");out.print("\t");
      out.print("Tipo Cuenta");out.print("\t");
      out.print("Estado");out.print("\t");
      out.println("C&oacute;digo Interbancario (CCI)");
      
      for(int i=0 ; i<lista.size() ; i++){
        bean = (BeanCodigoInterbancarioXML)lista.get(i);
        out.print(bean.getM_Cuenta());out.print("\t");
        out.print(bean.getM_Moneda());out.print("\t");
        out.print(bean.getM_TipoCuenta());out.print("\t");
        out.print(bean.getM_Estado());out.print("\t");
        out.println(bean.getM_CodigoInterbancario());
      }
  }
  response.flushBuffer();
}
else{
%>
<html>
  <head>
  <%if(!"save".equalsIgnoreCase(accion)){%>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <%}%>
    <title>Descarga</title>
    <script language="JavaScript" type="text/JavaScript">
      <!--
        function abreImport( accion ) { //v3.0
          if( accion == 'print'){
              window.print();
              window.close();
          }
        }
      -->
    </script>
  </head>
  <body <%if(!"save".equalsIgnoreCase(accion)){%> onload="javascrip:abreImport( '<%=accion%>' )" <%}%> >
    <br>
    <%--resultados de la busqueda
    <logic:notEmpty name="listaResult">
        <layout:collection name="listaResult" title="DETALLE DE C&Oacute;DIGOS INTERBANCARIOS" styleClass="FORM" id="cuenta" sortAction="client" width="100%" align="center">
            <layout:collectionItem title="Cuenta" property="m_Cuenta" sortable="true" />
            <layout:collectionItem  title="Moneda" property="m_Moneda" />
            <layout:collectionItem title="Tipo Cuenta" property="m_TipoCuenta" />
            <layout:collectionItem title="Estado" property="m_Estado" />
            <layout:collectionItem title="C&oacute;digo Interbancario (CCI)" property="m_CodigoInterbancario" />
        </layout:collection>
        <layout:space/>
    </logic:notEmpty>
    --%>
    <logic:notEmpty name="listaResult">
    <table cellpadding='2' cellspacing='2' border='0' align='center' width='100%'>
        <tr>
          <td class='TitleRow3' colspan='5'>DETALLE DE C&Oacute;DIGOS INTERBANCARIOS</td>
        </tr>
        <tr class='TitleRow4'>
            <td>Cuenta</td>
            <td>Moneda</td>
            <td>Tipo Cuenta</td>
            <td>Estado</td>
            <td>C&oacute;digo Interbancario (CCI)</td>
        </tr>
        <logic:iterate id="cuenta" name="listaResult" indexId="i">
            <tr>
                <td class='CellColRow7'><bean:write name="cuenta" property="m_Cuenta"/></td>
                <td class='CellColRow7'><bean:write name="cuenta" property="m_Moneda"/></td>
                <td class='CellColRow6'><bean:write name="cuenta" property="m_TipoCuenta"/></td>
                <td class='CellColRow7'><bean:write name="cuenta" property="m_Estado"/></td>
                <td class='CellColRow7'><bean:write name="cuenta" property="m_CodigoInterbancario"/></td>
            </tr>
        </logic:iterate>
    </table>
    </logic:notEmpty>
  </body>
</html>
<%
  if("save".equalsIgnoreCase(accion)){
    response.flushBuffer();
  }
}
%>
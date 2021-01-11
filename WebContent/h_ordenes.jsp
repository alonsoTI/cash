<%-- 
    Document   : ordenes
    Created on : 15/09/2010, 05:05:27 AM
    Author     : andrew

<%@taglib uri="http://displaytag.sf.net" prefix="display" %>
<display:table id="data" name="sessionScope.InputForm.ordenesList" requestURI="/ordenesAction.do?method=ordenes" pagesize="10" export="false" >
            <display:column property="Id_Sobre" title="TV Show" sortable="true"/>
            <display:column property="Fecha_Creacion" title="User Name" sortable="true"/>
            <display:column property="Valor_Sobre" title="Email Id" sortable="true"/>
        </display:table>
--%>

<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage=""%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">  
    </head>
    <body>
        <table border="0" cellpadding="2" cellspacing="2">
            <tr bgcolor="#FFFF00">
                <th><span STYLE="color: #0062AC; font-size: 10pt">ID Orden</span></th>            
                <th><span STYLE="color: #0062AC; font-size: 10pt">Cuenta</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Moneda</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Referencia</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Inicio</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Vence</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Cantidad Items</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Valor</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Estado</span></th>
                <th>&nbsp;</th>
            </tr>
            <logic:iterate name="listUsers" id="listUserId">
                <tr>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Id_Sobre"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Cuenta"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Codigo_Moneda"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Referencia"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Inicio"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Vencimiento"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Numero_Items"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Valor_Sobre"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="MensajeProceso"/></span></td>
                    <td><a href="#" onclick="detalles(<bean:write name="listUserId" property="Id_Sobre"/>)">ver</a></td>
                </tr>
            </logic:iterate>
        </table>


       
    </body>
</html>

<%-- 
    Document   : detalles
    Created on : 15/09/2010, 08:24:52 PM
    Author     : ANDQUI
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
        <p><a href="inputAction.do?method=populate">Regresar</a></p>
        <table border="0" cellpadding="2" cellspacing="2">
            <tr bgcolor="#FFFF00">
                <th><span STYLE="color: #0062AC; font-size: 10pt">ID</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Pais</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Banco</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Cuenta</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">ContraPartida</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Referencia</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Valor</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Moneda</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Fecha</span></th>
                <th><span STYLE="color: #0062AC; font-size: 10pt">Estado</span></th>
            </tr>
            <logic:iterate name="listDetalles" id="listUserId">
                <tr>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Id_Item"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Pais"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Banco"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Cuenta"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="ContraPartida"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Referencia"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Valor"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Moneda"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="Fecha_Proceso"/></span></td>
                    <td><span STYLE="color: #0062AC; font-size: 10pt"><bean:write name="listUserId" property="MensajeProceso"/></span></td>
                </tr>
            </logic:iterate>
        </table>



    </body>
</html>
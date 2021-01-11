<%-- 
    Document   : servicios
    Created on : 15/09/2010, 04:27:59 AM
    Author     : andrew
--%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage=""%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    </head>
    <body>
        <html:form  action="/inputAction"  >

            <html:select property="servicio" style="width:150px;"  >
                <html:option value="0">--Seleccionar--</html:option>
                <html:optionsCollection name="InputForm" property="servicioList" label="servicio" value="idservicio" />
            </html:select>


        </html:form>
    </body>
</html>



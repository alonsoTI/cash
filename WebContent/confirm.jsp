<%-- 
    Document   : confirm
    Created on : 29/12/2008, 02:19:44 PM
    Author     : esilva
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page pageEncoding="UTF-8"%>
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

        <title>Confirmation</title>
        <link href="css/Styles.css" rel="stylesheet" type="text/css">
        <link href="css/cash.css" rel="stylesheet" type="text/css">
        <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
        <script language="JavaScript" type="text/JavaScript">
            function back(){
                var frm = document.forms[0];

                frm.action = "<bean:write property='m_Back' name='confirm'/>&<bean:write property='m_BackAccion' name='confirm'/>";
                //alert(frm.action);
                frm.submit();
            }
            function forward(){
            	
                var frm = document.forms[0];

                document.getElementById('btnVolver').disabled=true;
                document.getElementById('btnAceptar').disabled=true;
                
                
                
                frm.action = "<bean:write property='m_Forward' name='confirm'/>&<bean:write property='m_ForwardAccion' name='confirm'/>";
                //alert(frm.action);
                frm.submit();
            }
        </script>
    </head>
    <body>
        <html:form action="transferencias.do" onsubmit="return false;" >
            <table width="100%" CELLSPACING="0" CELLPADDING="4">
                <tr>
                    <td valign="middle" align="left" class="Title"><bean:write name='confirm' property='m_Titulo'/></td>
                    
                </tr>
            </table>
            <table width="100%" cellpadding="2" cellspacing="2" border="0" >
                <tr class="TitleRow5">
                    <td width="20%"><bean:message key="transferencias.lbl.titutar"/></td>
                    <td width="30%"><bean:write name="usuarioActual" scope="session" property="m_Nombre"/>&nbsp;<bean:write name="usuarioActual" scope="session" property="m_Apellido"/>
                    </td>
                    <td width="20%"><bean:message key="transferencias.lbl.tarjeta"/></td>
                    <td width="30%"><bean:write name="usuarioActual" scope="session" property="m_NumTarjeta"/>
                    </td>
                </tr>
                <tr class="CellColRow2" style="background-color: #FFFFE6">
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr class="CellColRow5">
                    <td colspan="4" height="10px"></td>
                </tr>
            </table>
            <table width="100%" cellpadding="2" cellspacing="2" border="0" >
                <tr class="CellColRow2">
                    <td class="TitleRow" colspan="2"><bean:write name='confirm' property='m_Mensaje'/></td>
                </tr>
                <logic:iterate name="alconfirm" id="beanconfirm" indexId="j">
                    <tr class="CellColRow2">
                        <td width="30%" style="background-color: #336699; color: #FFFFFF"><bean:write name="beanconfirm" property="m_Label"/></td>
                        <td <%if(j.intValue()%2==1){%>style="background-color: #FFFFE6"<%}%>><bean:write name="beanconfirm" property="m_Mensaje"/></td>
                    </tr>
                </logic:iterate>

                <tr><td class="CellColRow2" colspan="2" style="background-color: #FFFFFF">&nbsp;</td></tr>
                <tr>
                    <td colspan="2" align="center">
                        <!--  <img src="img/bt-volver.png" align="middle" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:back();"/>-->
                        <!--<img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:forward();"/>-->
                        <input type="button" id="btnVolver" name="btnVolver" value="Volver" onClick="javascript:back();" class="cashbutton"
                             />                        
                        <input type="button" id="btnAceptar" name="btnAceptar" value="Aceptar" onClick="javascript:forward();" class="cashbutton"
                             />
                    </td>
                </tr>
            </table>

        </html:form>
    </body>
</html>

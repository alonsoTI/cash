<%-- 
    Document   : pagoDetalleOffLine
    Created on : 26/03/2009, 06:13:20 PM
    Author     : esilva
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>

<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>Pago de Servicio OffLine</title>
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
        <script language="JavaScript">
            function aceptar(){
                var frm = document.forms[0];
                frm.action = "pagoServicio.do?do=pagarOffLine";
                frm.submit();
            }
            function cancelar(){
                location.href = "pagoServicio.do?do=buscarPagoOffLinePag";//pagoOffLine.jsp";
            }
        </script>
    </head>
    <body>
        <html:form action="pagoServicio.do">
            <table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
                <tr>
                    <td valign="middle" align="left">&nbsp;&nbsp;<bean:write name="titulo" /></td>
                </tr>
            </table>
            <table width="100%" cellpadding="2" cellspacing="2" border="0" >
                <tr class="TitleRow5">
                    <td width="20%">&nbsp;&nbsp;<bean:message key="transferencias.lbl.titutar"/></td>
                    <td width="30%">&nbsp;&nbsp;<bean:write name="usuarioActual" scope="session" property="m_Nombre"/>&nbsp;<bean:write name="usuarioActual" scope="session" property="m_Apellido"/>
                    </td>
                    <td width="20%">&nbsp;&nbsp;<bean:message key="transferencias.lbl.tarjeta"/></td>
                    <td width="30%">&nbsp;&nbsp;<bean:write name="usuarioActual" scope="session" property="m_NumTarjeta"/>
                    </td>
                </tr>
                <tr class="TitleRow5">
                    <td width="20%" <logic:notEqual name="bLista" value ="1">style="display: none"</logic:notEqual>>&nbsp;&nbsp;<bean:message key="global.empresa"/></td>
                    <td width="30%" <logic:notEqual name="bLista" value ="1">style="display: none"</logic:notEqual>>
                        &nbsp;&nbsp;<bean:write name="serv_pago" property="m_DescEmpresa"/>
                    </td>
                    <td width="20%" style="display: none">                        
                    </td>
                </tr>
                <tr class="CellColRow5">
                    <td colspan="4" height="10px"></td>
                </tr>
            </table>
            <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
                <tr class="CellColRow2">
                    <td width="20%" style="background-color: #336699; color: #FFFFFF">&nbsp;&nbsp;<bean:message key="global.proveedor"/></td>
                    <td width="80%">
                        &nbsp;&nbsp;<bean:write name="serv_pago" property="m_NombreProveedor"/>
                    </td>
                </tr>
                <tr class="CellColRow2">
                    <td style="background-color: #336699; color: #FFFFFF">&nbsp;&nbsp;<bean:message key="pagos.servicio.lbl.cargo"/></td>
                    <td>
                        <html:select property="m_CuentaCargo" styleId="m_CuentaCargo" styleClass="CellColRow2">
                            <logic:iterate id="lcargo" name="listaccounts">
                                <bean:define name="lcargo" property="m_AccountCode" id="idacccargo" type="java.lang.String" />
                                <% String idcargo = "" + idacccargo + ""; %>
                                <html:option value="<%= idcargo %>" style="background-color: #FFFFFF" >&nbsp;<bean:write name="lcargo" property="m_AccountDescription" />&nbsp;<bean:write name="lcargo" property="m_AccountCode" />&nbsp;-&nbsp;Saldo:&nbsp;<bean:write name="lcargo" property="m_Currency"/>&nbsp;<bean:write name="lcargo" property="m_AvailableBalance"/></html:option>
                            </logic:iterate>
                        </html:select>
                    </td>
                </tr>
                <tr class="CellColRow2">
                    <td style="background-color: #336699; color: #FFFFFF">&nbsp;&nbsp;<bean:message key="pagos.servicio.lbl.total"/></td>
                    <td style="background-color: #FFFFE6">
                        &nbsp;&nbsp;<bean:write name="serv_pago" property="m_MontoTotal"/>
                    </td>
                </tr>
                <tr align="right">
                    <td colspan="2" class="CellColRow5">                        
                        <img src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:cancelar();"/>
                        <img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:aceptar();"/>
                    </td>
                </tr>                
            </table>
            <br>
            <logic:notEmpty name="listaResultPagar">
                <table width="100%" cellpadding="2" cellspacing="2" border="0" >
                <tr><td class='TitleRow3'><bean:message key="pagos.servicio.list"/></td></tr>
                <tr><td align="right">&nbsp;</td>
                </tr>
                <tr><td>
                <layout:collection name="listaResultPagar" styleClass="FORM" id="orden" sortAction="client" width="100%" align="center">
                    <layout:collectionItem style="display:none">
                        <center>
                            <layout:write name="orden" property="m_IdDetalleOrden"/>
                        </center>
                    </layout:collectionItem>
                    <layout:collectionItem property="m_IdOrden" sortable="true" style="display:none" />
                    <layout:collectionItem property="m_IdServicio" sortable="true" style="display:none"/>

                    <layout:collectionItem title="Recibo" property="m_NumRecibo"  />
                    <layout:collectionItem title="Referencia" property="m_Descripcion"  />
                    <%--layout:collectionItem title="Cliente" property="m_NomCliente" /--%>
                   <layout:collectionItem title="Moneda">
                        <p align="center">
                            <layout:write name="orden" property="m_DescTipoMoneda" />
                        </p>
                    </layout:collectionItem>
                   <layout:collectionItem title="Importe">
                        <p align="right">
                        <layout:write name="orden" property="m_Monto" />
                        </p>
                    </layout:collectionItem>          
                </layout:collection>
                </td>
                </tr>
                </table>
                <layout:space/>                
            </logic:notEmpty>
        </html:form>
    </body>
</html>

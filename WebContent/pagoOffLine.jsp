<%-- 
    Document   : pagoOffLine
    Created on : 11/03/2009, 05:34:04 PM
    Author     : esilva
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
        <title>Pago de Servicio OffLine</title>
        <link href="css/Styles.css" rel="stylesheet" type="text/css">
        <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
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
            function buscar(){
                var frm = document.forms[0];
                frm.action = "pagoServicio.do?do=buscarPagoOffLine";
                frm.submit();
            }
            function continuar(){
              if(!(verificaSel() > 0)){
                    alert("Debe seleccionar al menos un pago");
                    return;
                }else{
                    var frm = document.forms[0];
                    frm.action = "pagoServicio.do?do=cargarPagoDetalleOffLine";
                    frm.submit();
                }
            }
            function pagar(){
                if(!(verificaSel() > 0)){
                    alert("Debe seleccionar al menos un pago");
                    return;
                }else{
                    var frm = document.forms[0];
                    frm.action = "pagoServicio.do?do=cargarPagoDetalleOffLine";
                    frm.submit();
                }
            }
            function  verificaSel(){
              var  frm = document.forms[0];
              for (i = 0; i < frm.elements.length; i++){
                if(frm.elements[i].type=="checkbox" && frm.elements[i].checked==true ){
                  return true
                }
              }
              return false;
            }
            function selectAllCkb(){
               var  frm = document.forms[0];
               for (i = 0; i < frm.elements.length; i++){
                 if(frm.elements[i].type=="checkbox"){
                   frm.elements[i].checked=true;
                 }
               }
            }
            function deselectAllCkb(){
               var  frm = document.forms[0];
               for (i = 0; i < frm.elements.length; i++){
                 if(frm.elements[i].type=="checkbox"){
                   frm.elements[i].checked=false;
                 }
               }
            }
            
            function ocultarResultado(){
                var btnContinuar = document.getElementById("btnContinuar");
                if(btnContinuar!=null){
                    btnContinuar.style.display = "none";
                }
                document.getElementById("tabResultado").style.display = "none";
            }
            function paginar(valor){
                location.href = valor;
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
                        <html:select name="serv_pago" property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow2" >&nbsp;&nbsp;
                            <html:options collection="listaempresas" property="cemIdEmpresa" labelProperty="demNombre" style="background-color: #FFFFFF"/>
                        </html:select>
                    </td>
                    <td width="20%" style="display: none">
                        <html:select name="serv_pago" property="m_Servicio" styleId="m_Servicio" styleClass="CellColRowE" >
                            <logic:iterate id="opt" name="listaservicios">
                                <bean:define name="opt" property="m_IdServicio" id="idservicios" type="java.lang.String" />
                                <logic:equal name ="opt" property="estado" value="1">
                                    <% String idservicio = "" + idservicios + "*1"; %>
                                    <html:option value="<%= idservicio %>" ><bean:write name="opt" property="m_Descripcion" /></html:option>
                                </logic:equal>
                                <logic:notEqual name ="opt" property="estado" value="1">
                                    <% String idservicio = "" + idservicios + "*0"; %>
                                    <html:option value="<%= idservicio %>" style="background-color: #FFFFFF" ><bean:write name="opt" property="m_Descripcion" /></html:option>
                                </logic:notEqual>
                            </logic:iterate>
                        </html:select>
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
                
                <logic:notEmpty name="listaCriterios">
                    <logic:iterate id="idRes" name="listaCriterios" indexId="i">
                        <tr class="CellColRow2">
                        <td style="background-color: #336699; color: #FFFFFF">&nbsp;&nbsp;<bean:write name="idRes" property="ddmetiqueta" />:</td>
                        <td style="background-color: #FFFFE6">
                            &nbsp;&nbsp;
                            <bean:define name="idRes" property="id.cdmposicion" id="rname" type="java.lang.Integer" />
                            <bean:define name="idRes" property="ddmtipoDato" id="rname2" type="java.lang.String" />
                            <% String cbprop = "criterio(" + rname + ")"; %>
                            <% String cbval = "" + rname2 + "";
                               String on_key = "return alphabetic(event)";
                               String on_blur = "abc(this)";
                               if ("NUM".equalsIgnoreCase(cbval)){
                                   on_key = "numero()";
                                   on_blur = "val_int(this)";
                               }else if ("ANS".equalsIgnoreCase(cbval)){
                                   on_key = "return alphabetic(event)";
                                   on_blur = "abc(this)";
                               }
                            %>
                            <html:text property="<%= cbprop %>" styleId="<%= cbprop %>" size="40" maxlength="15" styleClass="CellColRow8" onkeypress="<%= on_key %>" onblur="<%= on_blur %>" />
                        </td>
                        </tr>
                    </logic:iterate>
                </logic:notEmpty>
                <tr align="right">
                    <td colspan="2" class="CellColRow5">
                        <img src="img/bt-buscar.png" align="middle" onClick="javascript:buscar();" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'"/>
                        <logic:notEmpty name="listaResult">
                            <img src="img/bt-continuar.png" align="middle" id="btnContinuar" onMouseOver="this.src='img/bt-continuar2.png'" onMouseOut="this.src='img/bt-continuar.png'" onClick="javascript:continuar();"/>
                        </logic:notEmpty>
                    </td>
                </tr>
                <logic:notEmpty name="listaResult">
                 <tr>
                     <td colspan="3" align="center">
                        <img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('pagoServicio.do?do=buscarPagoOffLinePag&tipoPaginado=P');"/>
                        <img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('pagoServicio.do?do=buscarPagoOffLinePag&tipoPaginado=A');"/>
                        P&aacute;gina <bean:write name="beanPag" property="m_pagActual"/> de <bean:write name="beanPag" property="m_pagFinal"/>
                        <img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('pagoServicio.do?do=buscarPagoOffLinePag&tipoPaginado=S');"/>
                        <img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('pagoServicio.do?do=buscarPagoOffLinePag&tipoPaginado=U');"/>
                    </td>
                </tr>
                </logic:notEmpty>
                <html:hidden property="m_Proveedor" styleId="m_Proveedor"/>
            </table>
            <br>
            <logic:notEmpty name="listaResult">
                <table width="100%" cellpadding="2" cellspacing="2" border="0" >
                <tr><td class='TitleRow3'><bean:message key="pagos.servicio.list"/></td></tr>
                <tr><td align="right">&nbsp;<a href="javascript:selectAllCkb();">Todos</a>&nbsp;&nbsp;<a href="javascript:deselectAllCkb();">Ninguno</a>&nbsp;
                    </td>
                </tr>               
                <tr><td>
                <layout:collection name="listaResult" styleClass="FORM" id="orden" sortAction="client" width="100%" align="center">
                    <layout:collectionItem style="display:none">
                        <center>
                            <layout:write name="orden" property="m_IdDetalleOrden" />
                        </center>
                    </layout:collectionItem>
                    <layout:collectionItem property="m_IdOrden" sortable="true" style="display:none"/>
                    <layout:collectionItem property="m_IdServicio" sortable="true" style="display:none"/>

                    <layout:collectionItem title="Recibo" property="m_NumRecibo" />
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
                    <layout:collectionItem title="Pagar">
                        <center>
                            <bean:define name="orden" property="m_IdOrden" id="rname1" type="java.lang.String" />
                            <bean:define name="orden" property="m_IdServicio" id="rname2" type="java.lang.String" />
                            <bean:define name="orden" property="m_IdDetalleOrden" id="rname3" type="java.lang.String" />
                            <% String cbprop = "value(" + rname1 + ";" + rname2 + ";" + rname3 +")"; %>                            
                            <html:checkbox property="<%= cbprop %>" />
                        </center>
                    </layout:collectionItem>
                </layout:collection>
                </td>
                </tr>
                </table>
                <layout:space/>               
            </logic:notEmpty>
            <logic:notEmpty name="mensaje">
                <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
                    <tr>
                        <td class='TitleRow3'><bean:write name="mensaje"/></td>
                    </tr>
                </table>
            </logic:notEmpty>
        </html:form>

    </body>
</html>

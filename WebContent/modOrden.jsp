<%--
    Document   : modOrden
    Created on : 24/11/2008, 11:47:44 AM
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

<%-- jwong 04/06/2009 obtenemos el codigo de los servicios --%>
<%
    String longitud = (String)request.getAttribute("longitudDoc");
    String metodoPress = (String)request.getAttribute("onkeypress");
    String metodoBlur = (String)request.getAttribute("onblur");
%>
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

    <title>Modificar Orden</title>
    <link href="css/Styles.css" rel="stylesheet" type="text/css">
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
    <script type="text/javascript" src="config/javascript.js"></script>
    <style type="text/css">@import url(calendario/calendar-system.css);</style>
    <script type="text/javascript" src="calendario/calendar.js"></script>
    <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
    <script type="text/javascript" src="calendario/calendar-setup.js"></script>
    <script language="JavaScript">

        function isValid(frm, from, strTarget) {
            if(!(verificaSel() > 0)){
                alert("Debe seleccionar al menos un item");
            }else{//jmoreno 13-05-2009
               var  frm = document.forms[0];
               var longitudDoc = '<%= longitud%>';               
               for (k = 0; k < frm.elements.length; k++){
                if(frm.elements[k].type=="checkbox" && frm.elements[k].checked==true ){
                    var x = frm.elements[k].name;
                    x = x.substring(x.indexOf("(")+1,x.indexOf(")"));

                    <logic:empty name="isPagProveedor">
                    var cuenta = "cunta("+x+")";
                    var ntelef = "telef("+x+")";
                    var email = "email("+x+")";
                    </logic:empty>

                    var documento = "documento("+x+")";
                    var nombre = "nombre("+x+")";

                   for (j = 0; j < frm.elements.length; j++){
                       if(frm.elements[j].type=="text"){

                           if(frm.elements[j].name==documento){                               
                               if(frm.elements[j].value==null || frm.elements[j].value==""){
                                   alert("Ingrese un número de documento válido");
                                   frm.elements[j].focus();
                                   frm.elements[j].select();
                                   return false;
                               }else{
                                   if(longitudDoc == 8){
                                       if(frm.elements[j].value.length != longitudDoc){
                                           alert("Ingrese un número de documento válido");
                                           frm.elements[j].focus();
                                           frm.elements[j].select();
                                           return false;
                                       }
                                   }else{
                                       if(frm.elements[j].value.length != 8 && frm.elements[j].value.length != 11){
                                           alert("Ingrese un número de documento válido");
                                           frm.elements[j].focus();
                                           frm.elements[j].select();
                                           return false;
                                       }
                                   }
                               }
                           }else
                           if (frm.elements[j].name==nombre) {
                               if(frm.elements[j].value==null || frm.elements[j].value==""){
                                   alert("Ingrese Nombres válidos");
                                   frm.elements[j].focus();
                                   frm.elements[j].select();
                                   return false;
                               }
                           }

                           <logic:empty name="isPagProveedor">
                           else if (frm.elements[j].name==cuenta) {
                                if(frm.elements[j].disabled != true){
                                   if(frm.elements[j].value==null || frm.elements[j].value==""){
                                       alert("Ingrese una cuenta válida");
                                       frm.elements[j].focus();
                                       frm.elements[j].select();
                                       return false;
                                   }else{
                                       if(frm.elements[j].value.length < 9 || frm.elements[j].value.length > 12 ){
                                           alert("Ingrese un número de cuenta válida");
                                           frm.elements[j].focus();
                                           frm.elements[j].select();
                                           return false;
                                       }
                                   }
                                }
                                
                           }else if(frm.elements[j].name==ntelef){
                                   if(frm.elements[j].value.length > 0 && frm.elements[j].value.length < 7 ){
                                       alert("Ingrese un número de teléfono válido");
                                       frm.elements[j].focus();
                                       frm.elements[j].select();
                                       return false;
                                   }
                               

                           }else if (frm.elements[j].name==email){
                               if(frm.elements[j].value.length > 0 && !isEmail(frm.elements[j].value)){
                                   alert("Ingrese un e-mail válido");
                                   frm.elements[j].focus();
                                   frm.elements[j].select();
                                   return false;
                               }
                           }
                           </logic:empty>
                       }

                   }
                }
               }

                if (isValidForm2(frm,from,strTarget)){
                       return true;
                }
            }
            return false;
        }        
        function verificaSel(){
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
        function paginar(valor){
            location.href = valor;
        }
</script>
</head>
<body>
<html:form action="Orden.do" onsubmit="return false;" >
    <table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
        <tr>
            <td valign="middle" align="left"><bean:message key="pagos.title.modificar"/></td>
            <td valign="middle" align="right" colspan="3"></td>
        </tr>
    </table>

        <table width="100%" cellpadding="2" cellspacing="2" border="0" >
            <tr>
                <td class="CellColRow" width="20%"><bean:message key="global.empresa"/></td>
                <td class="CellColRow2" width="30%">&nbsp;&nbsp;<bean:write name='beanOrden' property='m_Empresa'/></td>
                <td class="CellColRow" width="20%"><bean:message key="global.servicio"/></td>
                <td class="CellColRow2" width="30%">&nbsp;&nbsp;<bean:write name='beanOrden' property='m_Servicio'/></td>
            </tr>
            <tr>
                <td class="CellColRow" ><bean:message key="global.cuentas"/></td>
                <td class="CellColRow2" colspan="3">&nbsp;&nbsp;<bean:write name='beanOrden' property='m_DescCuenta'/></td>
            </tr>
            <tr>
                <td class="CellColRow"><bean:message key="global.fechaini"/></td>
                <td class="CellColRow2">&nbsp;&nbsp;<bean:write name='beanOrden' property='m_FecInicio'/></td>
                <td class="CellColRow"><bean:message key="global.fechafin"/></td>
                <td class="CellColRow2">&nbsp;&nbsp;<bean:write name='beanOrden' property='m_FecVenc'/></td>
            </tr>
            <tr>
                <td class="CellColRow"><bean:message key="global.horavigencia"/></td>
                <td class="CellColRow2" colspan="3">&nbsp;&nbsp;<bean:write name='beanOrden' property='m_DescHora'/></td>
            </tr>
            <tr>
                <td class="CellColRow"><bean:message key="global.referencia"/></td>
                <td class="CellColRow2" colspan="3">&nbsp;&nbsp;<bean:write name='beanOrden' property='m_Referencia'/></td>
            </tr>
            <tr>
                <td class="CellColRow"><bean:message key="global.codigo"/></td>
                <td class="CellColRow2" colspan="3">&nbsp;&nbsp;<bean:write name='beanOrden' property='m_IdOrden'/></td>
            </tr>
            <html:hidden property="m_IdOrden" styleId="m_IdOrden" name="beanOrden" />
            <html:hidden property="m_IdEmpresa" styleId="m_IdEmpresa" name="beanOrden" />
            <html:hidden property="m_IdServicio" styleId="m_IdServicio" name="beanOrden" />
        </table>

        <table width="100%" cellpadding="2" cellspacing="2" border="0" >
            <tr>
                <logic:empty name="listaorden">
                    <td colspan='4'>
                    <table cellpadding='2' cellspacing='2' border='2' align='center' width='80%' bordercolor='#0066CC'>
                        <td class='TitleRow3' ><bean:message key="global.listavacia"/></td>
                    </table>
                    </td>
                </logic:empty>
                <logic:notEmpty name="listaorden">
                    <input type="hidden" name="m_NroReg" id="m_NroReg" value="<bean:write name='m_NroReg'/>"/>
                    <td colspan="4" class='TitleRow3'>
                        <bean:message key="pagos.ordenes.list"/>
                    </td>
                    </tr>                    
                    <tr>
                    <td colspan="3" align="center">
                        <img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('Orden.do?do=cargarPag&m_IdOrden=<bean:write name="m_IdOrden"/>&m_IdServicio=<bean:write name="m_IdServicio"/>&tipoPaginado=P&m_CodServicio=<bean:write name="m_CodServicio"/>');"/>
                            <img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('Orden.do?do=cargarPag&m_IdOrden=<bean:write name="m_IdOrden"/>&m_IdServicio=<bean:write name="m_IdServicio"/>&tipoPaginado=A&m_CodServicio=<bean:write name="m_CodServicio"/>');"/>
                            P&aacute;gina <bean:write name="beanPag" property="m_pagActual"/> de <bean:write name="beanPag" property="m_pagFinal"/>
                            <img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('Orden.do?do=cargarPag&m_IdOrden=<bean:write name="m_IdOrden"/>&m_IdServicio=<bean:write name="m_IdServicio"/>&tipoPaginado=S&m_CodServicio=<bean:write name="m_CodServicio"/>');"/>
                            <img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('Orden.do?do=cargarPag&m_IdOrden=<bean:write name="m_IdOrden"/>&m_IdServicio=<bean:write name="m_IdServicio"/>&tipoPaginado=U&m_CodServicio=<bean:write name="m_CodServicio"/>');"/>
                    </td>
                    <td align="right">&nbsp;&nbsp;<a href="javascript:selectAllCkb();">Todos</a>&nbsp;&nbsp;<a href="javascript:deselectAllCkb();">Ninguno</a>&nbsp;&nbsp;
                    </td>
                    </tr>
                    <tr>
                    <td colspan="4">
                        <layout:collection name="listaorden" styleClass="FORM" id="news" sortAction="client" >
                           <layout:collectionItem title="pagos.column.codigo" property="m_IdDetalleOrden" />                           
                           <layout:collectionItem title="pagos.column.documento" property="m_Documento" >
                                 <bean:define name="news" property="m_Documento" id="documentoAux" type="java.lang.String" />
                                 <bean:define name="news" property="m_IdDetalleOrden" id="contador" type="java.lang.String" />
                                 <% String cont = "documento(" + contador + ")"; %>      
                                <html:text property="<%= cont %>" styleClass="TitleRow6" maxlength="<%=longitud%>" size="<%=longitud%>" value="<%= documentoAux %>" onkeypress="numero()" onblur="val_int(this)" />
                            </layout:collectionItem>
                            <layout:collectionItem title="pagos.column.nombre" property="m_Nombre" >
                                 <bean:define name="news" property="m_Nombre" id="nombreAux" type="java.lang.String" />
                                 <bean:define name="news" property="m_IdDetalleOrden" id="contador" type="java.lang.String" />
                                 <% String cont = "nombre(" + contador + ")"; %>
                                 <html:text property="<%= cont %>" styleClass="TitleRow5" maxlength="50" size="50" value="<%= nombreAux %>" onkeypress="<%= metodoPress %>" onblur="<%= metodoBlur %>"/>
                            </layout:collectionItem>                            

                          <logic:empty name="isPagProveedor">
                              <logic:empty name="isOrdCobro">
                            <layout:collectionItem title="pagos.column.cuenta" property="m_NumeroCuenta" >
                                 <bean:define name="news" property="m_NumeroCuenta" id="cuentaAux" type="java.lang.String" />
                                 <bean:define name="news" property="m_IdDetalleOrden" id="contador" type="java.lang.String" />
                                 <% String cont = "cunta(" + contador + ")"; %>
                                 <logic:greaterThan name="news" property="m_longitudCuenta" value="0">
                                     <html:text property="<%= cont %>" styleClass="TitleRow6" maxlength="15" size="15" value="<%= cuentaAux %>" onkeypress="numero()" onblur="val_int(this)" />
                                 </logic:greaterThan >
                                 <logic:lessEqual name="news" property="m_longitudCuenta" value="0">
                                     <html:text property="<%= cont %>" styleClass="TitleRow9" maxlength="15" size="15" value="<%= cuentaAux %>" onkeypress="numero()" onblur="val_int(this)" disabled="true" />
                                 </logic:lessEqual>
                                
                            </layout:collectionItem>
                            <layout:collectionItem title="pagos.column.tipocuenta"  >
                                <bean:define name="news" property="m_IdTipoCuenta" id="rname1" type="java.lang.String" />
                                <bean:define name="news" property="m_IdDetalleOrden" id="rname2" type="java.lang.String" />
                                <% String cbprop1 = "tipoCunta(" + rname2 + ")"; %>
                                <% String cbprop2 = "" + rname1 + ""; %>
                                <!--<h><%=cbprop1%></h>-->
                                <logic:greaterThan name="news" property="m_longitudCuenta" value="0">
                                    <html:select property="<%= cbprop1 %>" styleClass="TitleRow6" value="<%=rname1%>">
                                        <logic:notEmpty name="listaTipoCuenta">
                                            <html:options collection="listaTipoCuenta" property="id.clfCode" labelProperty="dlfDescription"/>
                                        </logic:notEmpty>
                                    </html:select>
                                </logic:greaterThan>
                                <logic:lessEqual name="news" property="m_longitudCuenta" value="0">
                                    <html:select property="<%= cbprop1 %>" styleClass="TitleRow6" value="<%=rname1%>" disabled="true">
                                        <logic:notEmpty name="listaTipoCuenta">
                                            <html:options collection="listaTipoCuenta" property="id.clfCode" labelProperty="dlfDescription"/>
                                        </logic:notEmpty>
                                    </html:select>
                                </logic:lessEqual>

                            </layout:collectionItem>
                        </logic:empty>
                            <layout:collectionItem title="pagos.column.telefono" property="m_Telefono" >
                                <bean:define name="news" property="m_Telefono" id="telefAux" type="java.lang.String" />
                                 <bean:define name="news" property="m_IdDetalleOrden" id="contador" type="java.lang.String" />
                                 <% String cont = "telef(" + contador + ")"; %>
                                <html:text property="<%= cont %>" styleClass="TitleRow6" maxlength="15" size="15" value="<%= telefAux %>" onkeypress="numero()" onblur="val_int(this)" />
                            </layout:collectionItem>                            
                            <layout:collectionItem title="pagos.column.email" property="m_Email" >
                                <bean:define name="news" property="m_IdDetalleOrden" id="contador" type="java.lang.String" />
                                <% String cont = "email(" + contador + ")"; %>
                                <logic:notEmpty name="news" property="m_Email">
                                    <bean:define name="news" property="m_Email" id="emailAux" type="java.lang.String" />                                    
                                    <html:text property="<%= cont %>" styleClass="TitleRow5" maxlength="40" size="40" value="<%= emailAux %>" />
                                </logic:notEmpty>
                                <logic:empty name="news" property="m_Email">
                                    <bean:define id="emailAux" type="java.lang.String" value="" />                                    
                                    <html:text property="<%= cont %>" styleClass="TitleRow5" maxlength="40" size="40" value="<%= emailAux %>" />
                                </logic:empty>                                 
                                 
                            </layout:collectionItem>
                            
                          </logic:empty>
                            <layout:collectionItem title="pagos.column.descripcion" property="m_Descripcion"/>
                            <layout:collectionItem title="pagos.column.tipomoneda">
                                <p align="center">
                                    <bean:write name="news" property="m_DescTipoMoneda" />
                                </p>
                                
                            </layout:collectionItem>
                                <layout:collectionItem title="pagos.column.monto">
                                     <p align="right">
                                        <bean:write name="news" property="m_Monto" />
                                    </p>
                                </layout:collectionItem>
                            <layout:collectionItem title="pagos.column.estado" property="m_DescEstado" />
                            <layout:collectionItem title="pagos.column.seleccionar" >
                                <bean:define name="news" property="m_IdDetalleOrden" id="rname" type="java.lang.String" />
                                <% String cbprop = "value(" + rname + ")"; %>
                                <!--<h><%=cbprop%></>-->
                                <p align="center">
                                    <html:checkbox property="<%= cbprop %>" />
                                </p>

                            </layout:collectionItem>

                        </layout:collection>
                        <layout:space/>
                    </td>
                    <div id="comp1" style="display:none">NUM</div>
                </logic:notEmpty>
            </tr>
            <tr align="right">
              <td colspan="5" class="CellColRow5">
                <img src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:DoSubmit('mantenerOrdenes.do?do=cargarModificar&accion=1');"/>
                <img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:isValid(document.forms[0],'ModOrden','Orden.do?do=update');"/>                
              </td>
            </tr>
        </table>
</html:form>
</body>
</html>
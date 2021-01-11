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

    <title>Cancelar Orden</title>
    <link href="css/Styles.css" rel="stylesheet" type="text/css">
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
    <script type="text/javascript" src="config/javascript.js"></script>
    <style type="text/css">@import url(calendario/calendar-system.css);</style>
    <script type="text/javascript" src="calendario/calendar.js"></script>
    <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
    <script type="text/javascript" src="calendario/calendar-setup.js"></script>
    <script language="JavaScript">
        function cancelar(){

        }
        function aceptar(){

        }
        /* Modificado por Grov 02/06/2010
        function isValid(frm, from, strTarget) {
            if(!(verificaSel() > 0)){
                alert("Debe seleccionar al menos un item");
            }else{
                 if(confirm("¿Esta seguro de cancelar el/los item(s)?")){
                    if (isValidForm2(frm,from,strTarget)){
                        return true;
                    }
                 }
                
            }
            return false;
        }
        */
       function isValid(frm, from, strTarget) {
                 if(confirm("¿Esta seguro de cancelar la Orden?")){
                    if (isValidForm2(frm,from,strTarget)){
                        return true;
                    }
                 }
            return false;
        }
        // Fin Modificado por Grov 02/06/2010

        function isValid2(frm, from, strTarget) {
                 if(confirm("¿Esta seguro de eliminar los items?")){
                    if (isValidForm2(frm,from,strTarget)){
                        return true;
                    }
                 }
            return false;
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
        function paginar(accion){
            location.href = accion;
        }
</script>
</head>
<body>
<html:form action="Orden.do" onsubmit="return false;" >
    <table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
        <tr>
            <td valign="middle" align="left"><bean:message key="pagos.title.cancelar"/></td>
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
                <td class="CellColRow2">&nbsp;&nbsp;<bean:write name='beanOrden' property='m_IdOrden'/></td>
                <td class="CellColRow">Estado:</td>
                <td class="CellColRow2">&nbsp;&nbsp;<bean:write name='beanOrden' property='m_DescripEstado'/></td>
            </tr>
            <html:hidden property="m_IdOrden" name="beanOrden" />
            <html:hidden property="m_IdEmpresa" name="beanOrden" />
            <html:hidden property="m_IdServicio" name="beanOrden" />
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
                    <td colspan="4" class='TitleRow3'>
                        <bean:message key="pagos.ordenes.list"/>
                    </td>
                    </tr>
                    <tr>
                    <td colspan="3" align="center">
                        <img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('Orden.do?do=cargarCancelarPag&m_IdOrden=<bean:write name="m_IdOrden"/>&m_IdServicio=<bean:write name="m_IdServicio"/>&tipoPaginado=P');"/>
                            <img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('Orden.do?do=cargarCancelarPag&m_IdOrden=<bean:write name="m_IdOrden"/>&m_IdServicio=<bean:write name="m_IdServicio"/>&tipoPaginado=A');"/>
                            P&aacute;gina <bean:write name="beanPag" property="m_pagActual"/> de <bean:write name="beanPag" property="m_pagFinal"/>
                            <img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('Orden.do?do=cargarCancelarPag&m_IdOrden=<bean:write name="m_IdOrden"/>&m_IdServicio=<bean:write name="m_IdServicio"/>&tipoPaginado=S');"/>
                            <img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('Orden.do?do=cargarCancelarPag&m_IdOrden=<bean:write name="m_IdOrden"/>&m_IdServicio=<bean:write name="m_IdServicio"/>&tipoPaginado=U');"/>
                    </td>
                    <td align="right">
                        <!-- Modificado por Grov 02/06/2010
                        &nbsp;&nbsp;<a href="javascript:selectAllCkb();">Todos</a>&nbsp;&nbsp;<a href="javascript:deselectAllCkb();">Ninguno</a>&nbsp;&nbsp;
                         Fin Modificado por Grov -->
                        </td>
                    </tr>
                    <tr>
                    <td colspan="4">
                        <layout:collection name="listaorden" styleClass="FORM" id="news" sortAction="client"  >

                            <layout:collectionItem title="pagos.column.codigo" property="m_IdDetalleOrden" />
                            <layout:collectionItem  title="pagos.column.documento" property="m_Documento" sortable="true"/>
                            <layout:collectionItem title="pagos.column.nombre" property="m_Nombre" sortable="true"/>
                            <layout:collectionItem title="pagos.column.cuenta" property="m_NumeroCuenta" />
                            <layout:collectionItem title="pagos.column.tipocuenta" property="m_DescTipoCuenta"/>
                            <layout:collectionItem title="pagos.column.telefono" property="m_Telefono" />
                            <layout:collectionItem title="pagos.column.email" property="m_Email" />
                            <layout:collectionItem title="pagos.column.descripcion" property="m_Descripcion"/>
                            <layout:collectionItem title="pagos.column.tipomoneda" property="m_DescTipoMoneda"/>
                            <layout:collectionItem title="pagos.column.monto" property="m_Monto" />
                            <layout:collectionItem title="pagos.column.estado" property="m_DescEstado" />
                            
                           <logic:equal value="02" name="tipo_servicio">			
					<layout:collectionItem title="pagos.column.eliminar">
						<bean:define name="news" property="m_IdDetalleOrden" id="rname"
							type="java.lang.String" />
						<bean:define name="news" property="m_IdOrden" id="rname2"
							type="java.lang.String" />
						<bean:define name="news" property="m_IdServicio" id="rname3"
							type="java.lang.String" />
						<bean:define name="news" property="m_DescEstado" id="rname4"
							type="java.lang.String" />
						<bean:define name="news" property="m_IDEstado" id="rname5"
							type="java.lang.String" />

						<html:hidden property="nombres" name="beanOrden"
							value="rname;rname2;rname3" />
						<% String cbprop = "value(" + rname + ";" + rname2 + ";" + rname3+")";%>
						<center><logic:equal name="rname5" value="0">
							<html:checkbox property="<%=cbprop%>" disabled="false" />
						</logic:equal> 
						<logic:notEqual name="rname5" value="0">
							<logic:equal name="rname5" value="8">
								<html:checkbox property="<%=cbprop%>" disabled="false" />
							</logic:equal>
							<logic:notEqual name="rname5" value="8">
								<html:checkbox property="<%=cbprop%>" disabled="true" />
							</logic:notEqual>
						</logic:notEqual>
						</center>
					</layout:collectionItem>
				</logic:equal>
                           
							



                        </layout:collection>
                        <layout:space/>
                    </td>
                </logic:notEmpty>
            </tr>
            <tr align="right">
              <td colspan="5" class="CellColRow5">
                <logic:empty name="listaorden">
                    <img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:DoSubmit('mantenerOrdenes.do?do=cargarCancelar&accion=1');"/>
                </logic:empty>
                 <logic:notEmpty name="listaorden">
                     <img src="img/bt-volver.png" align="middle" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:DoSubmit('mantenerOrdenes.do?do=cargarCancelar&accion=1');"/>
                     
                     <logic:notEmpty name="mo_tipo_proceso">
                     	<img src="img/bt-eliminar.png" align="middle" onMouseOver="this.src='img/bt-eliminar2.png'" onMouseOut="this.src='img/bt-eliminar.png'" onClick="javascript:isValid2(document.forms[0],'ModOrden','Orden.do?do=eliminarItems');"/>
                 	 </logic:notEmpty>
                 </logic:notEmpty>
                <%--<img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:isValid(document.forms[0],'ModOrden','Orden.do?do=cancelar');"/>
                <img src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:DoSubmit('mantenerOrdenes.do?do=cargarCancelar&accion=1');"/>--%>
              </td>
            </tr>
        </table>
</html:form>
</body>
</html>
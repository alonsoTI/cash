<%--
    Document   : eliminarOrden
    Created on : 20/11/2008, 01:04:02 PM
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

    <title>Eliminar Ordenes</title>
    <link href="css/Styles.css" rel="stylesheet" type="text/css">
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
    <script type="text/javascript" src="config/javascript.js"></script>
    <script language="JavaScript">
        function isValid(frm, from, strTarget) {
            if(!(verificaSel() > 0)){
                alert("Debe seleccionar al menos una orden");
            }else{
                if(confirm("¿Esta seguro de eliminar la/las orden(es)?")){
                    if (isValidForm2(frm,from,strTarget)){
                        return true;
                    }
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
    </script>
</head>
<body>
<html:form action="mantenerOrdenes.do" onsubmit="return false;" >
    <logic:notEqual name="mo_habil" value="1">
    <table width="100%" cellpadding="0" cellspacing="0" border="0">
        <tr><td>&nbsp;</td></tr>
        <tr><td>&nbsp;</td></tr>
        <tr valign="baseline">
            <td align="center" class="TitleRowCierraSession" valign="baseline" height="100%">
                <bean:message key="errors.authorization"/>
            </td>
        </tr>
    </table>
    </logic:notEqual>
    <logic:equal name="mo_habil" value="1">
    <table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
        <tr>
            <td valign="middle" align="left"><bean:message key="pagos.title.eliminar"/></td>
            <td valign="middle" align="right"></td>
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>
            <td class="CellColRow"><bean:message key="global.empresa"/></td>
            <td class="CellColRow2">
                <html:select property="empresa" styleId="empresa" styleClass="CellColRow" onchange="javascript:DoSubmit('mantenerOrdenes.do?do=cargarServicios&tipo=3');">
                    <html:options collection="mo_listaempresas" property="cemIdEmpresa" labelProperty="demNombre" />
                </html:select>
            </td>
            <td class="CellColRow"><bean:message key="global.servicio"/></td>
            <td class="CellColRow2">
                <html:select property="servicio" styleId="servicio" styleClass="CellColRow">
                    <html:option value="00"><bean:message key="global.todos.servicios"/></html:option>
                    <html:options collection="mo_listaservicios" property="m_IdServicioEmp" labelProperty="m_Descripcion" />
                </html:select>
            </td>
        </tr>
        <tr align="right">
            <td class="CellColRow5" colspan="4">
                <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:isValidForm(document.forms[0],'cargarEliminar','&accion=1');"/>
            </td>
        </tr>
    </table>
    
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <logic:empty name="mo_listaordenes">
            <tr>
            <td colspan='4'>
            <%--<table cellpadding='2' cellspacing='2' border='2' align='center' width='80%' bordercolor='#0066CC'>
                <td class='TitleRow3' ><bean:message key="global.listavacia"/></td>
            </table>--%>
            <logic:equal name="bLista" value="1" >
                <p class='TitleRow3'>
                    <bean:message key="global.listavacia"/>
                </p>
            </logic:equal>
            <logic:equal name="bLista" value="2" >
                <p class='TitleRow3'>
                    <bean:message key="global.noafiliado"/>
                </p>
            </logic:equal>
            </td>
            </tr>
        </logic:empty>
        <logic:notEmpty name="mo_listaordenes">
            <tr>
            <td colspan="4" class='TitleRow3'>
                        <bean:message key="pagos.list"/>
                    </td>
                    </tr>
                    <tr>
                    <td colspan="4" align="right">&nbsp;<a href="javascript:selectAllCkb();">Todos</a>&nbsp;&nbsp;<a href="javascript:deselectAllCkb();">Ninguno</a>&nbsp;
                    </td>
                    </tr>
            <tr>
                <td colspan="4">
                    <layout:collection name="mo_listaordenes" styleClass="FORM" id="news" sortAction="client" width="100%">
                            <layout:collectionItem title="pagos.column.empresa" property="m_Empresa"/>
                            <layout:collectionItem title="pagos.column.servicio" property="m_Servicio"/>
                            <layout:collectionItem title="pagos.column.idorden" property="m_IdOrden" sortable="true"/>
                            <layout:collectionItem title="pagos.column.cuenta" property="m_CuentaCargo"/>
                            <layout:collectionItem title="pagos.column.referencia" property="m_Referencia"/>
                            <layout:collectionItem title="pagos.column.fecinicio" property="m_FecInicio" sortable="true"/>
                            <layout:collectionItem title="pagos.column.fecfin" property="m_FecVenc" sortable="true"/>
                            <layout:collectionItem title="pagos.column.estado" property="m_DescripEstado" />
                            <layout:collectionItem title="pagos.column.eliminar">
                                <center>
                                    <bean:define name="news" property="m_IdOrden" id="rname1" type="java.lang.String" />
                                    <bean:define name="news" property="m_IdServicio" id="rname2" type="java.lang.String" />
                                    <% String cbprop = "value(" + rname1 + "*" + rname2 + ")"; %>                                    
                                    <html:checkbox styleId="<%= cbprop %>" property="<%= cbprop %>" />
                                </center>
                              </layout:collectionItem>
                        </layout:collection>
                    <layout:space/>
                </td>
                <div id="comp2" style="display:none" >NUM</div>
                <div id="comp5" style="display:none">FEC</div>
                <div id="comp6" style="display:none">FEC</div>
            </tr>
            <tr>
                <td colspan="4" class="CellColRow5">
                    <img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:isValid(document.forms[0],'EliminarOrden','mantenerOrdenes.do?do=eliminar');"/>
                    <!--<img src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:DoSubmit('mantenerOrdenes.do?do=cargarEliminar&accion=1');"/>-->
                </td>
            </tr>
        </logic:notEmpty>
    </table>
    </logic:equal>
</html:form>
</body>
</html>

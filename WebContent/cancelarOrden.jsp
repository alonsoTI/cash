<%--
    Document   : revocarOrden
    Created on : 20/11/2008, 01:04:21 PM
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
    <title>Cancelar Órdenes</title>
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
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
    <script language="JavaScript">

        function isValid(frm, from, strTarget) {
            	
                 if(confirm("¿Esta seguro de cancelar la Orden?")){
                    if (isValidForm2(frm,from,strTarget)){
                        return true;
                    }
                 }
            return false;
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
            <td valign="middle" align="left"><bean:message key="pagos.title.cancelar"/></td>
            <td valign="middle" align="right"></td>
        </tr>
    </table>

    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>
            <td class="CellColRow"><bean:message key="global.empresa"/></td>
            <td class="CellColRow2">
                <html:select property="empresa" styleId="empresa" styleClass="CellColRow" onchange="javascript:DoSubmit('mantenerOrdenes.do?do=cargarServicios&tipo=4');">
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
                <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:isValidForm(document.forms[0],'cargarCancelar','&accion=1');"/>
            </td>
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
            <logic:empty name="mo_listaordenes">
                <tr>
                <td colspan='4'>
                  <%--logic:notEmpty name="mo_mensaje"--%>
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
                  <%--/logic:notEmpty--%>
                </td>
                </tr>
            </logic:empty>
            <logic:notEmpty name="mo_listaordenes">
                <tr>
                <td colspan="4">
                    <layout:collection title="pagos.list" name="mo_listaordenes" styleClass="FORM" id="news" sortAction="client" width="100%">
                        <layout:collectionItem title="pagos.column.empresa" property="m_Empresa"/>
                        <layout:collectionItem title="pagos.column.servicio" property="m_Servicio"/>
                        <layout:collectionItem title="pagos.column.idorden" property="m_IdOrden" sortable="true"/>
                        <layout:collectionItem title="pagos.column.cuenta" property="m_CuentaCargo"/>
                        <layout:collectionItem title="pagos.column.referencia" property="m_Referencia"/>
                        <layout:collectionItem title="pagos.column.fecinicio" property="m_FecInicio" sortable="true"/>
                        <layout:collectionItem title="pagos.column.fecfin" property="m_FecVenc" sortable="true"/>
                        <layout:collectionItem title="pagos.column.estado" property="m_DescripEstado" />
                        <layout:collectionItem title="pagos.column.cancelar"><center>
                             
                            <layout:link href="Orden.do?do=cargarCancelar" name="news" property="parametrosUrl">
                                <layout:img srcName="edit.gif" border="0"/>
                            </layout:link>
                                <img src="config/delete.gif" align="middle" onmouseover="" onmouseout=""  onClick="javascript:isValid(document.forms[0],'ModOrden','Orden.do?do=cancelar&m_IdOrden=<bean:write name="news" property="m_IdOrden" />&m_IdEmpresa=<bean:write name="news" property="m_IdEmpresa" />&m_IdServicio=<bean:write name="news" property="m_IdServicio" />');"/>
                            </center>
                        </layout:collectionItem>
                        
                    </layout:collection>
                    <layout:space/>
                </td>
                </tr>
                <tr>
                
            </tr>
            <div id="comp2" style="display:none" >NUM</div>
            <div id="comp5" style="display:none">FEC</div>
            <div id="comp6" style="display:none">FEC</div>
            </logic:notEmpty>        
    </table>
    </logic:equal>
</html:form>
</body>
</html>

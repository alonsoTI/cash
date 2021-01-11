<%--
    Document   : modificarOrden
    Created on : 20/11/2008, 11:06:06 AM
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

    <title>Modificar Ordenes</title>
    <link href="css/Styles.css" rel="stylesheet" type="text/css">
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
    <script type="text/javascript" src="config/javascript.js"></script>
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
            <td valign="middle" align="left"><bean:message key="pagos.title.modificar"/></td>
            <td valign="middle" align="right" colspan="3"></td>
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>
            <td class="CellColRow" width="15%"><bean:message key="global.empresa"/></td>
            <td class="CellColRow2" width="35%">
                <html:select property="empresa" styleId="empresa" styleClass="CellColRowE" onchange="javascript:DoSubmit('mantenerOrdenes.do?do=cargarServicios&tipo=2');">
                    <html:options collection="mo_listaempresas" property="cemIdEmpresa" labelProperty="demNombre" />
                </html:select>
            </td>
            <td class="CellColRow" width="15%"><bean:message key="global.servicio"/></td>
            <td class="CellColRow2" width="35%">
                <html:select property="servicio" styleId="servicio" styleClass="CellColRowE">
                    <html:option value="00"><bean:message key="global.todos.servicios"/></html:option>
                    <html:options collection="mo_listaservicios" property="m_IdServicioEmp" labelProperty="m_Descripcion" />
                </html:select>
            </td>
        </tr>
        <tr align="right">
            <td class="CellColRow5" colspan="4">
                <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:isValidForm(document.forms[0],'cargarModificar','&accion=1');"/>
            </td>
        </tr>
    </table>

    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>
            <logic:empty name="mo_listaordenes">             
                <td>
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
            </logic:empty>
            <logic:notEmpty name="mo_listaordenes">
                <td>
                    <layout:collection title="pagos.list" name="mo_listaordenes" styleClass="FORM" id="news" sortAction="client" width="100%">
                        <layout:collectionItem title="pagos.column.empresa" property="m_Empresa"/>
                        <layout:collectionItem title="pagos.column.servicio" property="m_Servicio"/>
                        <layout:collectionItem title="pagos.column.idorden" property="m_IdOrden" sortable="true"/>
                        <layout:collectionItem title="pagos.column.cuenta" property="m_CuentaCargo"/>
                        <layout:collectionItem title="pagos.column.referencia" property="m_Referencia"/>
                        <layout:collectionItem title="pagos.column.fecinicio" property="m_FecInicio" sortable="true"/>
                        <layout:collectionItem title="pagos.column.fecfin" property="m_FecVenc" sortable="true"/>
                        <layout:collectionItem title="pagos.column.estado" property="m_DescripEstado" />
                        <layout:collectionItem title="pagos.column.modificar"><center>
                            <layout:link href="Orden.do?do=cargar" name="news" property="parametrosUrl">
                                <layout:img srcName="edit.gif" border="0"/>
                            </layout:link></center>
                          </layout:collectionItem>
                    </layout:collection>
                    <layout:space/>
                </td>
                <div id="comp2" style="display:none">NUM</div>
                <div id="comp5" style="display:none">FEC</div>
                <div id="comp6" style="display:none">FEC</div>
            </logic:notEmpty>
        </tr>
    </table>
     </logic:equal>
</html:form>
</body>
</html>

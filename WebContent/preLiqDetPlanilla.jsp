<%-- 
    Document   : preLiqDetPlanilla
    Created on : 17/03/2009, 12:22:22 PM
    Author     : jmoreno
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
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link href="css/Styles.css" rel="stylesheet" type="text/css">
        <script language="javascript" SRC="js/Functions.js"></script>
        <script type="text/javascript" src="config/javascript.js"></script>

        <style type="text/css">
            body {
                background: url(img/fondo.gif) no-repeat fixed;
                background-position: right;
            }
        </style>

        <script language="JavaScript" type="text/javascript">
            
            function continuar(){
                var mensaje = "<%= request.getAttribute("mensaje")%>";
                if(mensaje == "null"){
                    var frm = document.forms[0];
                    var tipoPreLiq = frm.m_TipoPreLiq.value;
                    if(verificarSeleccion(frm)){
                        if(tipoPreLiq=="ren"){
                            frm.action = "letras.do?do=mostrarPreLiqLetxRenv";
                            frm.submit();
                        }else if(tipoPreLiq=="can"){
                            frm.action = "letras.do?do=consultaPreLiqLetxCan";
                            frm.submit();
                        }
                    }else{
                        alert("Se debe seleccionar al menos una Letra");
                    }
                }else{
                    alert("No existen registros");
                }

            }
            function regresar(){
                var req = "<%= request.getAttribute("reqBack")%>";
                var frm = document.forms[0];
                frm.action = "letras.do?do=buscarPlanilla&from=2&"+req;
                frm.submit();
            }
            function verificarSeleccion(frm){
                for (i = 0; i < frm.elements.length; i++){
                    if(frm.elements[i].type=="checkbox" && frm.elements[i].checked==true){
                        return true;
                    }
                }
                return false;
            }
            function cambiarEstadoUlt(){
                var frm = document.forms[0];
                var tam=frm.m_selec.length;

                    for(i=0;i<tam;i++){
                        isChecked= frm.m_selec[i].checked;
                        if(isChecked){
                            for(j=0;j<tam;j++){
                                if(i!=j){
                                    frm.m_selec[j].disabled=true;
                                }
                            }
                            i=frm.m_selec.length;
                        }else{
                            for(j=0;j<tam;j++){
                                frm.m_selec[j].disabled=false;
                            }
                        }
                    }

            }
            function cambiarEstado(){
                var frm = document.forms[0];
                var tipoPreLiq = frm.m_TipoPreLiq.value;
                var tam=frm.m_selec.length;
                if(tipoPreLiq=="ren"){

                    for(i=0;i<tam;i++){
                        isChecked= frm.m_selec[i].checked;
                        if(isChecked){
                            for(j=0;j<tam;j++){
                                if(i!=j){
                                    frm.m_selec[j].disabled=true;
                                }
                            }
                            i=frm.m_selec.length;
                        }else{
                            for(j=0;j<tam;j++){
                                frm.m_selec[j].disabled=false;
                            }
                        }
                    }
                }else{
                    for(j=0;j<tam;j++){
                        frm.m_selec[j].disabled=false;
                    }
                }
            }
            function cargar(){
                var frm = document.forms[0];
                var tam=frm.m_selec.length;
                if(frm.m_TipoPreLiq.value=="ren"){
                    document.getElementById('chkbx').style.visibility='hidden';

                     for(i=0;i<tam;i++){
                        isChecked= frm.m_selec[i].checked;
                        if(isChecked){
                            for(j=0;j<tam;j++){
                                if(i!=j){
                                    frm.m_selec[j].disabled=true;
                                }
                            }
                            i=frm.m_selec.length;
                        }else{
                            for(j=0;j<tam;j++){
                                frm.m_selec[j].disabled=false;
                            }
                        }
                    }
                }else{
                    for(j=0;j<tam;j++){
                        frm.m_selec[j].disabled=false;
                    }
                    document.getElementById('chkbx').style.visibility='visible';
                }
            }
            function limpiarOpciones() {
                var frm = document.forms[0];
                var tam=frm.m_selec.length;
                if(frm.m_TipoPreLiq.value=="ren"){
                    document.getElementById('chkbx').style.visibility='hidden';
                }else{
                    document.getElementById('chkbx').style.visibility='visible';
                }
                for(j=0;j<tam;j++){
                    frm.m_selec[j].disabled=false;
                    frm.m_selec[j].checked=false;
                }
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
    <body onload="cargar();"> <!--onload="cargar();"-->
        <html:form action="letras.do">
            <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
                <tr>
                    <td valign="middle" align="left" colspan="4" class="Title"><bean:message key="letras.title.mostrar.letras"/></td>
                </tr>
            </table>
            <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center" >
                <tr>
                    <td class='CellColRow' width="30%">Tipo de Preliquidaci&oacute;n:</td>

                    <td class='CellColRow2'width="70%">
                        <select id="m_TipoPreLiq" name="m_TipoPreLiq" class="CellColRow" onchange="limpiarOpciones();"> <!--onchange="limpiarOpciones();"-->
                            <option value="can">Cancelaci&oacute;n</option>
                            <option value="ren">Renovaci&oacute;n</option>
                        </select>
                    </td>

                </tr>
                <tr align="right">
                    <td colspan="4" class="CellColRow5">
                        <img src="img/bt-volver.png" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:regresar();"/>
                        <img id="btnContinuarSup" src="img/bt-continuar.png" onMouseOver="this.src='img/bt-continuar2.png'" onMouseOut="this.src='img/bt-continuar.png'" onClick="javascript:continuar();"/>
                    </td>
                </tr>
            </table>
            <table width="100%" cellpadding="2" cellspacing="2" border="0" >
                <tr>
                    <logic:notEmpty name="mensaje">
                        <td colspan='4'>

                            <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
                                <tr>
                                    <td class='TitleRow3'><bean:write name="mensaje"/></td>
                                </tr>
                            </table>

                        </td>
                    </logic:notEmpty>
                    <logic:notEmpty name="listaResult">
                        <td colspan="4" class='TitleRow3'>
                            <bean:message key="letras.title.relacion.letras"/>
                        </td>
                    </tr>
                    <tr id="chkbx">
                        <td colspan="4" align="right">&nbsp;<a href="javascript:selectAllCkb();">Todos</a>&nbsp;&nbsp;<a href="javascript:deselectAllCkb();">Ninguno</a>&nbsp;&nbsp;
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <layout:collection name="listaResult" styleClass="FORM" id="letra" width="100%" align="center" sortAction="client">
                                <layout:collectionItem title="letras.column.numero.letra" property="m_NumLetra" sortable="true" >
                                    <center>
                                        <bean:write name="letra" property="m_NumLetra"  />
                                    </center>
                                </layout:collectionItem>

                                <layout:collectionItem title="letras.column.fecha.vencimiento" property="m_FechVenc" sortable="true" >
                                    <center>
                                        <bean:write name="letra" property="m_FechVenc" />
                                    </center>
                                </layout:collectionItem>

                                <layout:collectionItem title="letras.column.moneda" property="m_Moneda" >
                                    <p align="center">
                                        <bean:write name="letra" property="m_Moneda"/>
                                    </p>
                                </layout:collectionItem>
                                <layout:collectionItem title="letras.column.importe.letra" property="m_ImpLetra" >
                                    <p align="right">
                                        <bean:write name="letra" property="m_ImpLetra"/>
                                    </p>
                                </layout:collectionItem>
                                <layout:collectionItem title="letras.column.numero.girador" property="m_NumUser" >
                                    <p align="right">
                                        <bean:write name="letra" property="m_NumUser"/>
                                    </p>
                                </layout:collectionItem>

                                <layout:collectionItem title="letras.column.nombre.girador" property="m_NomUser" sortable="true"  />

                                <%--layout:collectionItem title="letras.column.tasa" property="m_Tasa"   /--%>
                                <layout:collectionItem title="letras.column.estado" property="m_Estado"   />
                                <layout:collectionItem title="Seleccionar" >
                                    <p align="center">
                                    <input name='m_selec' type='checkbox' value='<bean:write name="letra" property="m_NumLetra" />&<bean:write name="letra" property="m_NumUser" />&<bean:write name="letra" property="m_FechVenc" />&<bean:write name="letra" property="m_ImpLetrasf" />&<bean:write name="letra" property="m_Moneda" />&<bean:write name="letra" property="m_Estado" />&<bean:write name="letra" property="m_RucEmpresa" />' onclick="cambiarEstado();" >
                                    </p>
                                </layout:collectionItem>
                            </layout:collection>
                            <layout:space/>
                        </td>
                        <div id="comp0" style="display:none">ANS</div>
                        <div id="comp1" style="display:none">FEC</div>
                        <div id="comp5" style="display:none">ANS</div>

                        <logic:notEmpty name="beanLetras">
                            <input type="hidden" id="m_Cuenta" name="m_Cuenta" value="<bean:write name='beanLetras' property='m_Cuenta' />" />
                        </logic:notEmpty>
                    </logic:notEmpty>
                </tr>
                <tr align="right">
                    <td colspan="5" class="CellColRow5">
                        <img src="img/bt-volver.png" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:regresar();"/>
                        <img id="btnContinuarInf" src="img/bt-continuar.png" onMouseOver="this.src='img/bt-continuar2.png'" onMouseOut="this.src='img/bt-continuar.png'" onClick="javascript:continuar();"/>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html>


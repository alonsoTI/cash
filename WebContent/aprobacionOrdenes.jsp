<%-- 
    Document   : aprobacionOrdenes
    Created on : 10-Feb-2008, 12:35:14
    Author     : esilva
      Modificado por andy 16/06/2011
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
        <link href="css/cash.css" rel="stylesheet" type="text/css">
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
            function imprimir(){
                window.print();
            }
            function buscar(){
                var frm = document.forms[0];
                frm.action = "aprobaciones.do?do=buscarOrdenesLinea";
                frm.submit();
            }
            function aprobar(){
                if(!(verificaSel() > 0)){
                    alert("Debe seleccionar al menos una orden");
                    return;
                }else{
                    var frm = document.forms[0];
                    frm.action = "aprobaciones.do?do=aprobarOrdenes";  
                                      
                    document.getElementById('btnAprobar').disabled=true;

                    
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
    function obtenerServicios(campo) {
        var frm = document.forms[0];
        frm.action = "aprobaciones.do?do=buscarServiciosEmpUser&indiceEmpresa="+campo.selectedIndex+"&idEmpresa="+campo.value;
        frm.submit();
    }
            
        </script>

    </head>
    <body>
        <html:form action="aprobaciones.do">
            <logic:notEqual name="habil" value="1">
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
            <logic:equal name="habil" value="1">
            <table width="100%" CELLSPACING="0" CELLPADDING="4" >
                <tr>
                    <td valign="middle" align="left" class="Title"><bean:message key="aprobaciones.title"/></td>
                </tr>
            </table>

            <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center" >
                <tr>
                    <td class='CellColRow'>Empresa:</td>
                    <td class='CellColRow2'>
                        <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow" onchange="obtenerServicios(this)" >
                            <logic:notEmpty name="listaEmpresas">
                                <html:options collection="listaEmpresas" property="cemIdEmpresa" labelProperty="demNombre"/>
                            </logic:notEmpty>
                        </html:select>
                    </td>

                    <td class='CellColRow'>Servicio:</td>
                    <td class='CellColRow2'>
                        <html:select property="m_Servicio" styleId="m_Servicio" styleClass="CellColRow">
                            <html:option value="00"><bean:message key="global.todos.servicios"/></html:option>
                            <logic:notEmpty name="listaServicios">
                                <html:options collection="listaServicios" property="m_IdServicio" labelProperty="m_Descripcion"/>
                            </logic:notEmpty>
                        </html:select>
                    </td>
                </tr>

                <tr align="right">
                    <td colspan="4" class="CellColRow5">
                        <img src="img/bt-buscar.png" align="middle" onClick="javascript:buscar();" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'"/>
                    </td>
                </tr>
            </table>
            <br>
            <logic:notEmpty name="listaResult">
                <table width="100%" cellpadding="2" cellspacing="2" border="0" >
                <tr>
                     <td colspan="4" class='TitleRow3'>
                        <bean:message key="aprobaciones.list"/>
                    </td>
                    </tr>
                    <tr>
                    <td colspan="4" align="right">&nbsp;<a href="javascript:selectAllCkb();">Todos</a>&nbsp;&nbsp;<a href="javascript:deselectAllCkb();">Ninguno</a>&nbsp;
                    </td>
                    </tr>
                    <tr>
                    <td colspan="4">
                <layout:collection name="listaResult" styleClass="FORM" id="orden" sortAction="client" align="center">
                    <layout:collectionItem title="Id Orden">
                        <center>
                        <layout:link href="aprobaciones.do?do=detalleOrdenes" name="orden" property="parametrosUrl">
                                <layout:write name="orden" property="m_IdOrden"/>
                        </layout:link>
                        </center>
                    </layout:collectionItem>
                    <layout:collectionItem title="Servicio" property="m_Servicio" sortable="true"/>
                    <layout:collectionItem title="aprobaciones.column.4">
                        <layout:write name="orden" property="m_CuentaCargo"/>
                    </layout:collectionItem>
                    <layout:collectionItem title="aprobaciones.column.6" property="m_Referencia" sortable="true"/>
                    <layout:collectionItem title="Registro" property="m_FecRegistro" sortable="true"/>
                    <layout:collectionItem title="Inicio" property="m_FecInicio" sortable="true"/>
                    <layout:collectionItem title="Vence" property="m_FecVenc" sortable="true"/>
                    <layout:collectionItem title="Estado" property="m_DescripEstado" sortable="true"/>
                    <layout:collectionItem title="Cantidad Items" property="m_Items" sortable="true"/>

                    <layout:collectionItem title="Valor Soles">
                      <p align="right">
                          <layout:write name="orden" property="m_ValorSoles"/>
                      </p>
                    </layout:collectionItem>
                    <layout:collectionItem title="Valor D&oacute;lares">
                      <p align="right">
                          <layout:write name="orden" property="m_ValorDolares"/>
                      </p>
                    </layout:collectionItem>
                    <layout:collectionItem title="Valor Euros">
                      <p align="right">
                          <layout:write name="orden" property="m_ValorEuros"/>
                      </p>
                    </layout:collectionItem>
                    
                    <layout:collectionItem title="aprobaciones.column.11">
                        <center>
                            <bean:define name="orden" property="m_IdOrden" id="rname1" type="java.lang.String" />
                            <bean:define name="orden" property="m_IdServicio" id="rname2" type="java.lang.String" />
                            <bean:define name="orden" property="m_IdAprobador" id="rname3" type="java.lang.String" />
                            <bean:define name="orden" property="m_IdUsuario" id="rname4" type="java.lang.String" />
                            <% String cbprop = "value(" + rname1 + ";" + rname2 + ";" + rname3+ ";" + rname4 +")"; %>
                            <html:checkbox property="<%= cbprop %>" />
                        </center>
                    </layout:collectionItem>
                </layout:collection>
                </td>
                <div id="comp1" style="display:none">ANS</div>
                <div id="comp3" style="display:none">ANS</div>
                <div id="comp4" style="display:none">FEC</div>
                <div id="comp5" style="display:none">FEC</div>
                <div id="comp6" style="display:none">FEC</div>
                <div id="comp7" style="display:none">ANS</div>
                <div id="comp8" style="display:none">NUM</div>
                </tr>
                </table>
                <layout:space/>
                <table cellpadding='2' cellspacing='2' border='0' align='center' width='100%'>
                <tr align="right">
                    <td align="right">                       
                        
                        <input type="button" id="btnAprobar" name="btnAprobar" value="Aprobar" onClick="javascript:aprobar();" class="cashbutton"/>
                        
                    </td>
                </tr>
                </table>
                
            </logic:notEmpty>
            <logic:notEmpty name="mensaje">
                <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
                    <tr>
                        <td class='TitleRow3'><bean:write name="mensaje"/></td>
                    </tr>
                </table>
            </logic:notEmpty>
            </logic:equal>
        </html:form>
    </body>
</html>
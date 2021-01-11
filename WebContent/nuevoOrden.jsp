<%-- 
    Document   : nuevoOrden
    Created on : 13/11/2008, 03:06:33 PM
    Author     : esilva
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

    <title>Ingresar Ordenes</title>
    <link href="css/Styles.css" rel="stylesheet" type="text/css">
        <link href="css/consolidated_common.css" rel="stylesheet" type="text/css">
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
    <style type="text/css">@import url(calendario/calendar-system.css);</style>
    <script type="text/javascript" src="calendario/calendar.js"></script>
    <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
    <script type="text/javascript" src="calendario/calendar-setup.js"></script>
    <script type="text/javascript" src="js/livevalidation_standalone.js"></script>
    
    <script language="JavaScript" type="text/javascript">
        function changeOptions(f) {
            var cod = f.value;
            if(cod != ""){
                document.forms[0].action = 'mantenerOrdenes.do?do=cargarCuentas&cod='+cod;
                document.forms[0].submit();
            }
        }
        function isValid(frm, from, strTarget) {
            var val = new Array();
            val[0] = valHora;
            if (LiveValidation.massValidate(val)){
                if (isValidForm2(frm,from,strTarget)){
                    return true;
                }
            }
            return false;
        }
        function disallowDate(date) {
          var fecActual = document.getElementById("fechaActualComp").value;
          var fecSel = date.print("%Y%m%d")

          if (  fecSel < fecActual ) {
              return true; // disable
          }
          return false; // enable other dates
        };
        
        function disallowDateDifferentToday(date) {
            var fecActual = document.getElementById("fechaActualComp").value;
            var fecSel = date.print("%Y%m%d")
            if (  fecSel != fecActual ) {
                return true; // disable
            }
            return false; // enable only today date
          };
          
        function disallowDateRef(date) {
          var field = document.getElementById("fechaInicial").value;
          var fecActual = field.substr(6, 4) + field.substr(3, 2) + field.substr(0, 2);                   
          var anio= field.substr(6, 4);
          var mes= field.substr(3, 2);
          var dia= field.substr(0, 2);
          var fecActualMax = new Date(anio,mes-1,dia);
          if('<c:out value="${sessionScope.mo_tiposervicio}"/>'=='01'){
        	  fecActualMax.setMonth(fecActualMax.getMonth() + 2);
          }
          if('<c:out value="${sessionScope.mo_tiposervicio}"/>'=='02'){
        	  fecActualMax.setMonth(fecActualMax.getMonth() + 12);
          }          
          fecActualMax = fecActualMax.print("%Y%m%d");
          var fecSel = date.print("%Y%m%d");
          if( fecSel > fecActualMax){
              return true;
          }
          if (  fecSel < fecActual ) {
            return true; 
          }
          return false; 
        };        
        
        function updateDate(cal) {
            var field_0 = document.getElementById("fechaActualComp").value;
            var field_1 = document.getElementById("fechaFinal").value;
            var fecCero = field_0.substr(0, 4) + field_0.substr(4, 2) + field_0.substr(6, 2);
            var fecFin = field_1.substr(6, 4) + field_1.substr(3, 2) + field_1.substr(0, 2);
            var date = cal.date;
            var fecSel = date.print("%Y%m%d");
            if (  fecSel > fecFin ) {
                var field = document.getElementById("fechaFinal");
                field.value = date.print("%d/%m/%Y");
            }else if ( fecSel < fecCero){
                var field = document.getElementById("fechaInicial");
                field.value = field_0.substr(6, 2)+'/' +field_0.substr(4, 2)+'/' + field_0.substr(0, 4);
            }
        }
        function updateDateRef(cal) {
            var field_0 = document.getElementById("fechaInicial").value;
            var fecIni = field_0.substr(6, 4) + field_0.substr(3, 2) + field_0.substr(0, 2);
            var date = cal.date;
            var fecSel = date.print("%Y%m%d");
            if (  fecSel < fecIni ) {
                var field = document.getElementById("fechaFinal");
                field.value = field_0.substr(0, 2) + '/' + field_0.substr(3, 2)+'/' + field_0.substr(6, 4);
            }
        }
    </script>
</head>
<body>
    <html:form action="mantenerOrdenes.do" onsubmit="return false;" style="background-color: #000000" >
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
            <td valign="middle" align="left">&nbsp;&nbsp;<bean:message key="pagos.title.ingresar"/></td>
            <!--<td valign="middle" align="right"><img src="img\printer.png" align="middle" onClick="javascript:imprimir();"/></td>-->
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>
            <td class="CellColRow" width="20%"><bean:message key="global.empresa"/></td>
            <td class="CellColRow2" width="30%">
                <html:select property="empresa" styleId="empresa" styleClass="CellColRowE" onchange="javascript:DoSubmit('mantenerOrdenes.do?do=cargarServicios&tipo=1');" style="width : 90%">
                    <html:options collection="mo_listaempresas" property="cemIdEmpresa" labelProperty="demNombre" />
                </html:select>
            </td>

            <td class="CellColRow" width="20%"><bean:message key="global.servicio"/></td>
            <td class="CellColRow2" width="30%">
                <%--<logic:iterate id="choice" name="mo_listaservicios">
                    <logic:equal name ="choice" property="estado" value="1">
                        <html:radio property="control" idName="choice" value="m_IdServicio" disabled="false" onclick="changeOptions(this);"  />
                    </logic:equal>
                    <logic:notEqual name ="choice" property="estado" value="1">
                        <html:radio property="control" idName="choice" value="m_IdServicio" disabled="true" />
                    </logic:notEqual>
                    <bean:write name="choice" property="m_Descripcion" /><br>
                </logic:iterate>--%>
                <html:select property="control" styleId="control" styleClass="CellColRowE" onchange="changeOptions(this);" style="width : 90%">
                    <html:option value=""><bean:message key="global.seleccionar"/></html:option>
                    <logic:iterate id="opt" name="mo_listaservicios">
                        <bean:define name="opt" property="m_IdServicio" id="idservicios" type="java.lang.String" />
                        <bean:define name="opt" property="m_IdServicioEmp" id="idservicioEmp" type="java.lang.String" />
                        <logic:equal name ="opt" property="estado" value="0">
                            <% String idservicio = "" + idservicioEmp + "*" + idservicios; %>
                            <html:option value="<%= idservicio %>" ><bean:write name="opt" property="m_Descripcion" /></html:option>
                        </logic:equal>
                         <logic:notEqual name ="opt" property="estado" value="0">
                            <% String idservicio = "" + idservicioEmp + "*"+idservicios; %>
                            <html:option value="<%= idservicio %>" style="background-color: #FFFFFF" ><bean:write name="opt" property="m_Descripcion" /></html:option>
                        </logic:notEqual>
                    </logic:iterate>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="CellColRow"><bean:message key="global.cuentas"/></td>
            <td class="CellColRow2" colspan="3">
                <html:select property="cuenta" styleId="cuenta" styleClass="CellColRowE" style="width : 50%">
                    <html:option value=""><bean:message key="global.seleccionar"/></html:option>
                    <logic:iterate id="cta" name="mo_listacuentas">
                        <bean:define name="cta" property="id.dcsemNumeroCuenta" id="idcuenta" type="java.lang.String" />
                        <html:option value="<%= idcuenta %>" >&nbsp;<bean:write name="cta" property="dcsemDescripcion" />&nbsp;<bean:write name="cta" property="id.dcsemNumeroCuenta" /></html:option>
                     </logic:iterate>
                </html:select>
            </td>            
        </tr>

        <tr>
            <td class="CellColRow"><bean:message key="global.fechaini"/></td>
            <td class="CellColRow2">
                <html:text property="fechaInicial" styleId="fechaInicial" styleClass="CellColRow6" maxlength="10" size="10" style="width : 40%" readonly="true"/>
                <button id="btn_ini" name="btn_ini"><img src="img/cal.gif" /></button>
                <script type="text/javascript">
                    Calendar.setup(
                    {
                        inputField : "fechaInicial" // ID campo de entrada
                        ,ifFormat : "%d/%m/%Y" // Formato de fecha
                        ,button : "btn_ini" // ID del boton
                        ,weekNumbers : false // Numero de semanas
                        ,dateStatusFunc : <c:choose><c:when test="${sessionScope.mo_tiposervicio=='02'}"> disallowDateDifferentToday </c:when>
                        					<c:otherwise>disallowDate</c:otherwise></c:choose>
                        ,onUpdate : updateDate
                    }
                );
                </script>
            </td>
            <td class="CellColRow"><bean:message key="global.fechafin"/></td>
            <td class="CellColRow2">
                <html:text property="fechaFinal" styleId="fechaFinal" styleClass="CellColRow6" maxlength="10" size="10" style="width : 40%" readonly="true"/>
                <button id="btn_fin" name="btn_fin"><img src="img/cal.gif" /></button>
                <script type="text/javascript">
                    Calendar.setup(
                    {
                        inputField : "fechaFinal" // ID campo de entrada
                        ,ifFormat : "%d/%m/%Y" // Formato de fecha
                        ,button : "btn_fin" // ID del boton
                        ,weekNumbers : false // Numero de semanas
                        ,dateStatusFunc : disallowDateRef
                        ,onUpdate : updateDateRef
                    }
                );
                </script>
            </td>
        </tr>
        <tr>
            <td class="CellColRow"><bean:message key="global.horavigencia"/></td>
            <td class="CellColRow2" colspan="3">
                <html:text property="horaVigencia" styleId="horaVigencia" maxlength="5" styleClass="CellColRow" style="width : 15%" onkeypress="return soloHora(this,event);" onblur="gHora(this);"/>&nbsp;&nbsp;(hh:mm)&nbsp;&nbsp;
                
            </td>
            <!--<td class="CellColRow" colspan="2"></td>-->
        </tr>
        <tr>
            <td class="CellColRow"><bean:message key="global.referencia"/></td>
            <td class="CellColRow2" colspan="3">
                <html:text property="referencia" styleId="referencia" maxlength="50" styleClass="CellColRow" style="width : 35%" onkeypress="return soloDescripcion(this, event)" onblur="gDescripcion(this)" />&nbsp;&nbsp;Opcional
            </td>            
        </tr>
        <logic:notEqual name="mo_tiposervicio" value="02">
        <tr>
            <td class="CellColRow"><bean:message key="global.tipoingreso"/></td>
            <td class="CellColRow2">
            <html:radio property="tipoingreso" value="1"/><bean:message key="global.archivo"/>                
            <script>
                document.forms[0].tipoingreso.checked = true;
            </script>                
            </td>
        </tr>
        </logic:notEqual>
        <logic:equal name="mo_tiposervicio" value="02">
            <html:hidden property="tipoingreso" value="1" />
        </logic:equal>
        <html:hidden property="fechaActualComp" styleId="fechaActualComp" />
        <html:hidden property="fechaActualMax" styleId="fechaActualMax"/>
        <tr align="right" >
            <td class="CellColRow5" colspan="4" style="background-color: transparent">
                <img  src="img/bt-continuar.png" align="middle" onMouseOver="this.src='img/bt-continuar2.png'" onMouseOut="this.src='img/bt-continuar.png'" onClick="javascript:isValid(document.forms[0],'NvoOrden','mantenerOrdenes.do?do=cargarOrden');"/>&nbsp;&nbsp;
            </td>
        </tr>

    </table>
    
    </logic:equal>
</html:form>
<script type="text/javascript">
    var valHora = new LiveValidation('horaVigencia', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valHora.add(Validate.Presence, {failureMessage: " La hora de vigencia es requerido"});
    valHora.add(Validate.Format, {pattern: /^([0-1][0-9]|[2][0-3])[:][0-5][0-9]$/i,failureMessage: " El formato debe ser hh:mm (24 horas)"});
    </script>
</body>
</html>

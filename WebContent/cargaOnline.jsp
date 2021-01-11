<%--
    Document   : cargaOnline
    Created on : 13/11/2008, 03:06:33 PM
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
    <title>Cargar Orden</title>
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

    <script language="JavaScript" type="text/javascript">
		var imgsrc="config/";
		var scriptsrc="config/";
		var langue="es";

        function isValid(frm, from, strTarget) {
            if(!(verificaSel() > 0)){
                alert("Debe seleccionar al menos un item");
            }
            
            else{//jmoreno 13-05-2009
               var  frm = document.forms[0];
               for (k = 0; k < frm.elements.length; k++){
                if(frm.elements[k].type=="checkbox" && frm.elements[k].checked==true ){
                    var x = frm.elements[k].name;
                    x = x.substring(x.indexOf("(")+1,x.indexOf(")"));
                    
                    var documento = "documento("+x+")";
                    
                    for (j = 0; j < frm.elements.length; j++){
                       if(frm.elements[j].type=="text"){
                           if(frm.elements[j].name==documento){                               
                               if(frm.elements[j].value == ""){
									alert("Ingrese un monto");
                                    frm.elements[j].focus();
                                    return false;
                               }else if(frm.elements[j].value <= 0){
                                   alert("El monto debe ser mayor a 0.00");
                                   frm.elements[j].focus();
                                   return false;
                               }
                               
                           }
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
<html:form action="Orden.do" onsubmit="return false;" >
    <table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
        <tr>
            <td valign="middle" align="left">&nbsp;&nbsp;<bean:message key="pagos.title.ingresar"/></td>
            <!--<td valign="middle" align="right"><img src="img\printer.png" align="middle" onClick="javascript:imprimir();"/></td>-->
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>
            <td class="CellColRow" width="20%"><bean:message key="global.empresa"/></td>
            <td class="CellColRow2" width="30%">&nbsp;&nbsp;<bean:write name='mo_objorden' property='m_Empresa'/></td>
            <td class="CellColRow" width="20%"><bean:message key="global.servicio"/></td>
            <td class="CellColRow2" width="30%">&nbsp;&nbsp;<bean:write name='mo_objorden' property='m_Servicio'/></td>
        </tr>
        <tr>
            <td class="CellColRow" ><bean:message key="global.cuentas"/></td>
            <td class="CellColRow2" colspan="3">&nbsp;&nbsp;<bean:write name='mo_objorden' property='m_DescCuenta'/></td>
        </tr>
        <tr>
            <td class="CellColRow"><bean:message key="global.fechaini"/></td>
            <td class="CellColRow2">&nbsp;&nbsp;<bean:write name='mo_objorden' property='forFechaInicio'/></td>
            <td class="CellColRow"><bean:message key="global.fechafin"/></td>
            <td class="CellColRow2">&nbsp;&nbsp;<bean:write name='mo_objorden' property='forFechaFin'/></td>
        </tr>
        <tr>
            <td class="CellColRow"><bean:message key="global.horavigencia"/></td>
            <td class="CellColRow2" colspan="3">&nbsp;&nbsp;<bean:write name='mo_objorden' property='m_DescHora'/></td>
        </tr>
        <tr>
            <td class="CellColRow"><bean:message key="global.referencia"/></td>
            <td class="CellColRow2" colspan="3">&nbsp;&nbsp;<bean:write name='mo_objorden' property='dorReferencia'/></td>
        </tr>		
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>
            <logic:empty name="mo_listaentidad">
                <td colspan='4'>
                <table cellpadding='2' cellspacing='2' border='2' align='center' width='80%'>
                    <td class='TitleRow3' ><bean:message key="global.listavacia"/></td>
                </table>
                </td>
            </logic:empty>
            <logic:notEmpty name="mo_listaentidad">
                <td colspan="4" class='TitleRow3'>
                    <bean:message key="pagos.ordenes.list"/>
                </td>
                </tr>
                <tr>
                <td colspan="4" align="right">&nbsp;<a href="javascript:selectAllCkb();">Todos</a>&nbsp;&nbsp;<a href="javascript:deselectAllCkb();">Ninguno</a>&nbsp;
                </td>
                </tr>
                <tr>
                <td colspan="4">
                    <layout:collection name="mo_listaentidad" styleClass="FORM" id="news" sortAction="client"  indexId="k" >
                        <layout:collectionItem title="pagos.column.codigo" property="idEntidad"  />
                        <layout:collectionItem title="pagos.column.tipodoc" property="ddotipoDocumento" >
                             <center>
                                     <bean:write name="news" property="ddotipoDocumento"  />
                             </center>
                        </layout:collectionItem>
                        <layout:collectionItem  title="pagos.column.documento" property="ndodocumento" sortable="true"/>
                        <layout:collectionItem title="pagos.column.nombre" property="ddonombre" sortable="true" />
                        <layout:collectionItem title="pagos.column.cuenta" property="ndonumeroCuenta" />
                        <layout:collectionItem title="pagos.column.tipocuenta" property="descTipoCuenta"/>
                        <layout:collectionItem title="pagos.column.telefono" property="ddotelefono" />
                        <layout:collectionItem title="pagos.column.email" property="ddoemail" />
                        <layout:collectionItem title="pagos.column.descripcion" property="ddodescripcion"/>
                        <layout:collectionItem title="pagos.column.tipopago" property="descTipoPago" />                        
                             <layout:collectionItem title="pagos.column.tipomoneda" property="descTipoMoneda">
                                 <p align="center">
                                     <bean:write name="news" property="descTipoMoneda" />
                                 </p>
                            </layout:collectionItem>                                                                 
                      <layout:collectionItem title="pagos.column.monto" property="ndomonto">
                           <bean:define name="news" property="idEntidad" id="rmonto" type="java.lang.String" />
                           <bean:define name="news" property="ndomonto" id="rmonto2" type="java.math.BigDecimal" />
                            <% String cbMonto = "documento(" + rmonto + ")"; %>
                            <% String monto_final = String.valueOf(rmonto2); %>                            
                            <html:text property="<%= cbMonto %>" styleClass="CellColRow" maxlength="10" value="<%= monto_final %>" onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)" />
                      </layout:collectionItem>
                        <layout:collectionItem title="pagos.column.agregar" >
                            <bean:define name="news" property="idEntidad" id="rname" type="java.lang.String" />
                            <% String cbprop = "value(" + rname + ")"; %>                            
                            <p align="center">
                                <html:checkbox property="<%= cbprop %>" />
                            </p>

                        </layout:collectionItem>

                    </layout:collection>
                    <layout:space/>
                </td>
                <div id="comp2" style="display:none">ANS</div>
                <div id="comp3" style="display:none">ANS</div>
            </logic:notEmpty>
        </tr>
        <tr align="right">
          <td colspan="5" class="CellColRow5">            
            <img src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:DoSubmit('mantenerOrdenes.do?do=cargarIngresar');"/>
            <img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:isValid(document.forms[0],'ModOrden','Orden.do?do=guardar&modulo=<bean:write name="modulo"/>');"/>
          </td>
        </tr>
    </table>
</html:form>
</body>
</html>

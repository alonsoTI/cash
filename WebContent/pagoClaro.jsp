<%-- 
    Document   : pagoClaro
    Created on : 31/03/2009, 11:13:23 PM
    Author     : Administrador
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">

  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>
  <script type="text/javascript" src="config/javascript.js"></script>
  <script type="text/javascript" src="js/Functions.js"></script>

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
    <!--
	function buscar(){
      var frm = document.forms[0];
      if(frm.m_NumAbonado.value!=""){
        frm.action = "pagoServicio.do?do=buscarPagoClaro&"+frm.data.value;
        frm.submit();
      }
	  else{
        frm.m_NumAbonado.focus();
        alert("Debe ingresar Número de Abonado para continuar");
        //alert("Debe ingresar " + document.getElementById("labelNumSuministro").innerHTML + " para continuar");
      }
    }

    function continuar(){
      //debemos validar que se haya seleccionado algun recibo para pagarlo
      if(validar()){
        var frm = document.forms[0];
        frm.action = "pagoServicio.do?do=cargarDetallePagoClaro";
        frm.submit();
      }
    }

    //validamos los campos del formulario antes de enviar a procesar
    function validar(){
        var  frm = document.forms[0];
        //verificamos la seleccion de agun recibo
        if(frm.m_NumAbonado.value=="") {
            alert("Debe ingresar Número de Abonado para continuar");
            return false;
        }
        else {
            var selRecibo = false;
            for (i = 0; i < frm.elements.length; i++){
                if(frm.elements[i].type=="checkbox" && frm.elements[i].checked==true ){
                    selRecibo = true;
                    break;
                }
            }
            if(selRecibo==false){
                alert("Debe seleccionar algun recibo para continuar");
                return false;
            }
        }
        return true;
    }
    /*
    function cambiaServicio(){
        var codeServicio = document.getElementById("m_Servicio").value;
        var codes = codeServicio.split(";");
        var labelNuSuministro = codes[3];
        //colocamos la etiqueta que corresponde
        document.getElementById("labelNumSuministro").innerHTML = labelNuSuministro + ":";
        document.getElementById("labelSuministro").value = labelNuSuministro;
    }
    */
    function ocultarResultado(){
        //al cambiar tenemos que ocultar el boton continuar y la busqueda anterior(en caso exista)
        var btnContinuar = document.getElementById("btnContinuar");
        if(btnContinuar!=null){
            btnContinuar.style.display = "none";
        }
        document.getElementById("tabResultado").style.display = "none";
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
	-->
</script>

</head>
<body>
<html:form action="pagoServicio.do">

  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
        <td valign="middle" align="left" class="Title"><bean:write name="serv_pago" property="m_Titulo" /></td>
	</tr>
  </table>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
    <tr class="TitleRow5">
        <td width="20%"><bean:message key="transferencias.lbl.titutar"/></td>
        <td width="30%">
            <bean:write name="usuarioActual" scope="session" property="m_Nombre"/>&nbsp;<bean:write name="usuarioActual" scope="session" property="m_Apellido"/>
        </td>
        <td width="20%"><bean:message key="transferencias.lbl.tarjeta"/></td>
        <td width="30%">
            <bean:write name="usuarioActual" scope="session" property="m_NumTarjeta"/>
        </td>
    </tr>

    <tr class="TitleRow5" <logic:lessEqual name="nroEmpresas" value="1">style="display:none"</logic:lessEqual> >
      <td>Empresa:</td>
      <td>
        <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow">
          <logic:notEmpty name="listaEmpresas">
              <html:options collection="listaEmpresas" property="idEmpresaCodCliente" labelProperty="demNombre"/>
          </logic:notEmpty>
        </html:select>
      </td>
    </tr>
  </table>
  <br/>
  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
    <tr>
      <td class="TitleRow2">Proveedor:</td>
      <td class="CellColRow2" >
          <bean:write name="serv_pago" property="m_NombreProveedor"/>
	  </td>
    </tr>
    <tr>
      <td class="TitleRow2">Servicio:</td>
      <td class="CellColRow2" >
        <html:select property="m_Servicio" styleId="m_Servicio" styleClass="CellColRow8" onchange="javascript:ocultarResultado();">
            <logic:notEmpty name="listaServicio">
                <html:options collection="listaServicio" property="m_TipoCodigoEntidadServicio2" labelProperty="m_Nombre"/>
            </logic:notEmpty>
        </html:select>
	  </td>
    </tr>
    <%--logic:notEmpty name="listaSector">
        <tr>
          <td class="TitleRow2">Localidad:</td>
          <td class="CellColRow2" >
            <html:select property="m_Sector" styleId="m_Sector" styleClass="CellColRow8" onchange="javascript:ocultarResultado();">
                <html:options collection="listaSector" property="m_CodigoInterno_Nombre" labelProperty="m_Nombre"/>
            </html:select>
          </td>
        </tr>
    </logic:notEmpty--%>
    <tr>
      <td class="TitleRow2">N&uacute;mero de Abonado:</td>
      <td class="CellColRow2" >
        <html:text property="m_NumAbonado" name="serv_pago" styleId="m_NumAbonado" size="40" maxlength="15" styleClass="CellColRow8" onkeypress="numero()" onblur="val_int(this)"/>
	  </td>
    </tr>

	<tr align="right">
      <td>&nbsp;</td>
      <td>
        <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:buscar();"/>
        <logic:notEmpty name="listaResult">
            <img src="img/bt-continuar.png" align="middle" id="btnContinuar" onMouseOver="this.src='img/bt-continuar2.png'" onMouseOut="this.src='img/bt-continuar.png'" onClick="javascript:continuar();"/>
        </logic:notEmpty>
	  </td>
    </tr>
  </table>

  <%--resultados de la busqueda--%>
  <div id="tabResultado">
  <logic:notEmpty name="listaResult">
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>
            <td class='TitleRow3'>DETALLE DE RECIBOS POR PAGAR</td>
        </tr>
        <tr>
            <td align="right">&nbsp;<a href="javascript:selectAllCkb();">Todos</a>&nbsp;&nbsp;<a href="javascript:deselectAllCkb();">Ninguno</a>&nbsp;</td>
        </tr>
    </table>
    <table width="100%" border="0" >
      <layout:collection name="listaResult" styleClass="FORM" id="deuda" sortAction="client" width="100%" align="center" indexId="h" >
        <layout:collectionItem title="Cod. de Producto" property="m_NumRecibo" sortable="true" />
        <layout:collectionItem title="Descripci&oacute;n" property="m_Descripcion" sortable="true" />               
        <layout:collectionItem  title="Estado Deuda" property="m_EstadoDeuda" />
        <layout:collectionItem  title="Tipo Documento" property="m_TipoDocumento" />
        <layout:collectionItem  title="N&uacute;mero Documento" property="m_NumeroDocumento" />        
        <layout:collectionItem title="Fecha Emisi&oacute;n" property="m_FecEmisFormateada" sortable="true" />
        <layout:collectionItem  title="Fecha Vencimiento" property="m_FecVencFormateada" />
        <layout:collectionItem  title="Referencia" property="m_Referencia" />
        <layout:collectionItem title="Moneda">
            <p align="center">
                <bean:write name='deuda' property='m_DescrMoneda' />
            </p>
        </layout:collectionItem>
        <layout:collectionItem title="Importe">
            <p align="right">
                <bean:write name='deuda' property='m_ImporteMostrar' />
            </p>
        </layout:collectionItem>

        <layout:collectionItem title="Pagar">
            <center>
                <input type="checkbox" id="m_chkRecibo" name="m_chkRecibo" value="<bean:write name='deuda' property='parametrosCadenaClaro' />" />
            </center>
        </layout:collectionItem>
      </layout:collection>
    </table>
    <layout:space/>
    <div id="comp0" style="display:none">NUM</div>
    <div id="comp1" style="display:none">ANS</div>
    <div id="comp3" style="display:none">ANS</div>
    <div id="comp5" style="display:none">FEC</div>
  </logic:notEmpty>
  </div>

  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
    </tr>
  </table>
  </logic:notEmpty>
  <input type="hidden" name="data" id="data" value="<%= request.getAttribute("data") %>">
</html:form>
</body>
</html>

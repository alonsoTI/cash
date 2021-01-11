<%-- 
    Document   : detallePagoSedapal
    Created on : 31/03/2009, 05:09:33 PM
    Author     : jwong
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
	function aceptar(){
      //debemos validar que se haya seleccionado algun recibo para pagarlo
      if(validar()){
        var frm = document.forms[0];
        frm.action = "pagoServicio.do?do=pagarSedapal";
        frm.submit();
      }
    }

    //validamos los campos del formulario antes de enviar a procesar
    function validar(){
        return true;
    }
    function back(){
        if(validar()){
            var frm = document.forms[0];
            frm.action = "pagoServicio.do?do=cargarPagoSedapal&"+frm.data.value+"&limpiar=no";
            //frm.action = "pagoServicio.do?do=cargarPagoSedapal&idproveedor="+frm.idProveedor.value+"&limpiar=no";
            frm.submit();
        }
    }
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
    <%--
    <tr class="TitleRow5" <logic:lessEqual name="nroEmpresas" value="1">style="display:none"</logic:lessEqual> >
      <td>Empresa:</td>
      <td>
          <bean:write property="m_DescEmpresa" name="serv_pago" />
      </td>
    </tr>
    --%>
  </table>
  <br/>
  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
    <tr>
	  <td class="TitleRow2" width="25%">Cuenta Cargo:</td>
      <td class="CellColRow2" width="75%">
          <bean:write property="m_CuentaCargo" name="serv_pago" />
      </td>
    </tr>

    <tr>
      <td class="TitleRow2">N&uacute;mero de Suministro:</td>
      <td class="CellColRow2" >
          <bean:write property="m_NumAbonado" name="serv_pago" />
      </td>
    </tr>
    <tr>
      <td class="TitleRow2">Importe:</td>
      <td class="CellColRow2">
          <bean:write property="m_MontoMostrar" name="serv_pago" />
      </td>
    </tr>
    <tr>
	  <td class='TitleRow2'>Fecha de Vencimiento:</td>
	  <td class='CellColRow2'>
          <bean:write property="m_FecVencimiento" name="serv_pago" />
	  </td>
	</tr>

    <tr>
	  <td class="TitleRow2">Referencia:</td>
      <td class="CellColRow2">
          <bean:write property="m_Referencia" name="serv_pago" />
      </td>
    </tr>

	<tr align="right">
      <td>&nbsp;</td>
      <td>        
        <img src="img/bt-volver.png" align="middle" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:back();"/>
        <img src="img/bt-aceptar.png" align="middle" id="btnAceptar" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:aceptar();"/>
      </td>
    </tr>
  </table>

  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
    </tr>
  </table>
  </logic:notEmpty>
  <input type="hidden" id="data" name="data" value="<%= request.getAttribute("data")%>" />
</html:form>
</body>
</html>

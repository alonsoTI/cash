<%-- 
    Document   : pagoSedapal
    Created on : 31-mar-2009, 13:45:45
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
    
	function continuar(){
      //debemos validar que se haya seleccionado algun recibo para pagarlo
      if(validar()){
        var frm = document.forms[0];
        frm.action = "pagoServicio.do?do=cargarDetallePagoSedapal";//&"+frm.data.value;
        //frm.action = "pagoServicio.do?do=cargarDetallePagoSedapal&idProveedor="+frm.idProveedor.value;
        frm.submit();
      }
    }
    
    //validamos los campos del formulario antes de enviar a procesar
    function validar(){
        var  frm = document.forms[0];
        if(frm.m_CuentaCargo.value==""){
            alert("Debe seleccionar Cuenta Cargo para continuar");
            return false;
        }
        else if(frm.m_NumAbonado.value.length < 8){
            alert("Ingrese un Número de Suministro correcto.[8 dígitos sin el guión - ]");
            return false;
        }
        else if(frm.m_Importe.value==""){
            alert("Debe ingresar Importe para continuar");
            return false;
        }
        else if(frm.m_FecVencimiento.value==""){
            alert("Debe ingresar Fecha de Vencimiento para continuar");
            return false;
        }
        else if(frm.m_Referencia.value==""){
            alert("Debe ingresar Referencia para continuar");
            return false;
        }
        else if(!validarMontoCuenta()){
            alert("Seleccione una cuenta que tenga saldo disponible mayor o igual al importe a pagar");
            return false;
        }
        return true;
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
    function replaceAll( text, busca, reemplaza ){
        while (text.toString().indexOf(busca) != -1){
            text = text.toString().replace(busca,reemplaza);
        }
        return text;
    }
    function validarMontoCuenta(){
        //se debe validar que el saldo de la cuenta seleccionada sea mayor o igual al monto que se pagara
        var importePagoServicio = document.getElementById("m_Importe").value.replace(" ", "");
        var codeServicio = document.getElementById("m_CuentaCargo").value;
        var codes = codeServicio.split(";");
        var saldoDisponible = replaceAll(codes[1]," ", "");
        //var saldoDisponible = codes[1].replace(" ", "");        
        //verificamos que el saldo disponible de la cuenta seleccionada pueda cubrir el pago del servicio
        if(parseFloat(saldoDisponible) < parseFloat(importePagoServicio)){
            return false;
        }
        else{return true;}
    }
     function disallowDate(date) {

      var fecActual = document.getElementById("m_FechaActual").value;
      var fecSel = date.print("%d/%m/%Y");      
      var m_fecActual = fecActual.substr(6, 4) + fecActual.substr(3, 2) + fecActual.substr(0, 2);
      var m_fecSel = fecSel.substr(6, 4) + fecSel.substr(3, 2) + fecSel.substr(0, 2);      
      if (  m_fecSel < m_fecActual) {
          return true; // disable
      }
      return false; // enable other dates
    };
	
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
        <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow" onchange="javascript:DoSubmit('pagoServicio.do?do=cargarCuentaSedapal&empresaSel=' + this.value);">
          <logic:notEmpty name="listaEmpresas">
              <html:options collection="listaEmpresas" property="idEmpresaCodClienteNombre" labelProperty="demNombre" />
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
        <html:select property="m_Servicio" styleId="m_Servicio" styleClass="CellColRow8" onchange="javascript:cambiaServicio();javascript:ocultarResultado()">
            <logic:notEmpty name="listaServicio">
                <html:options collection="listaServicio" property="m_LabelCodigoServicio" labelProperty="m_Nombre"/>
            </logic:notEmpty>
        </html:select>
	  </td>
    </tr>
    
    <tr>
      <td class="TitleRow2">N&uacute;mero de Suministro:</td>
      <td class="CellColRow2" >
        <html:text property="m_NumAbonado" styleId="m_NumAbonado" size="15" maxlength="8" styleClass="CellColRow8" onkeypress="numero()" onblur="val_int(this)"/>
	  </td>
    </tr>

    <tr>
	  <td class="TitleRow2">Cuenta Cargo:</td>
      <td class="CellColRow2">
        <html:select property="m_CuentaCargo" styleId="m_CuentaCargo" value="" styleClass="CellColRow8">
            <logic:notEmpty name="listaccounts">
                <html:options collection="listaccounts" property="m_CodigoImporteMoneda" labelProperty="m_DescripcionSaldoDisponible"/>
            </logic:notEmpty>
        </html:select>
      </td>
    </tr>

    <tr>
      <td class="TitleRow2">Importe:</td>
      <td class="CellColRow2">
        <html:text property="m_Importe" styleId="m_Importe" size="15" maxlength="15" styleClass="CellColRow8" onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)"/>
      </td>
    </tr>


    <tr>
	  <td class='TitleRow2'>Fecha de Vencimiento:</td>
	  <td class='CellColRow2'>
		<!--input type='TEXT' id='m_IniFec' size='10' maxlength='10' class="CellColRow6"/-->
      <html:text property="m_FecVencimiento" readonly="true" styleId="m_FecVencimiento" name="serv_pago" size="10" maxlength="10" styleClass="CellColRow8"/>
		<button id='btn_ini' name='btn_ini'><img src='img/cal.gif' /></button>
		<script type='text/javascript'>
		  Calendar.setup(
			{
			  inputField : "m_FecVencimiento",
			  ifFormat : "%d/%m/%Y",
			  button : "btn_ini",
              weekNumbers : false, // Numero de semanas
              dateStatusFunc : disallowDate
              
			}
		  );
		</script>
	  </td>
	</tr>

    <tr>
	  <td class="TitleRow2">Referencia Cobro:</td>
      <td class="CellColRow2">
        <html:text property="m_Referencia" styleId="m_Referencia" size="60" maxlength="15" styleClass="CellColRow8"  onkeypress="numero()" onblur="val_int(this)"/>
      </td>
    </tr>
    
	<tr align="right">
      <td>&nbsp;</td>
      <td>
        <img src="img/bt-continuar.png" align="middle" id="btnContinuar" onMouseOver="this.src='img/bt-continuar2.png'" onMouseOut="this.src='img/bt-continuar.png'" onClick="javascript:continuar();"/>
      </td>
    </tr>
  </table>
  <input type="hidden" name="m_FechaActual" value="<%= request.getAttribute("m_FechaActual") %>">
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

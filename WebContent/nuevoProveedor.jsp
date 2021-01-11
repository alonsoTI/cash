<%-- 
    Document   : consultaMovHistorico
    Created on : 04-dic-2008, 9:44:38
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">

  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>

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
    <logic:notEmpty name="msjExistProveedor">
        alert("<bean:write name='msjExistProveedor'/>");
    </logic:notEmpty>
    
	function imprimir(){
	  window.print();
	}
	function exportar(){
		location.href = "exportarExcel.html";
	}

	function verificarCta(campo) {//jmoreno
         val_int(campo)
         var cc_abono = campo.value;
          if(cc_abono.length >= 9){
            while(cc_abono.length < 12){
                cc_abono = "0" + cc_abono;
            }
            campo.value = cc_abono;
            return true;
          }
    }
    function aceptar(){
        if(validarIngreso()){
            //validamos la longitud de algunos campos antes de guardarlos
            if(validarLongitud()){
                var m_IdServEmp = document.getElementById("m_IdServEmp").value;
                var m_IdProveedor = document.getElementById("m_IdProveedor").value;
                existeIdProveedor("personal.do?do=existeIdProveedor&m_IdServEmp="+m_IdServEmp+"&m_IdProveedor="+m_IdProveedor);
            }
        }
        else{
            alert("Debe ingresar datos en todos los campos");
        }
	}
	
	function cancelar(){
		location.href = "administracion.do?do=cargarMantenimientoProveedor";
	}

    function cambiaFormaPago(){
        //var divmmm = document.getElementById("rowCargar");
		var m_FormaPago = document.getElementById("m_FormaPago");

        var isCtaFinanciero1 = document.getElementById("isCtaFinanciero1");
        //var isCtaFinanciero2 = document.getElementById("isCtaFinanciero2");

        var isCtaOtroBco1 = document.getElementById("isCtaOtroBco1");
        //var isCtaOtroBco2 = document.getElementById("isCtaOtroBco2");

        if(m_FormaPago.value=="<bean:write name='CONS_ABONO_CTA_FINAN'/>"){
            isCtaFinanciero1.style.display = "block";
            //isCtaFinanciero2.style.display = "block";

            isCtaOtroBco1.style.display = "none";
            //isCtaOtroBco2.style.display = "none";
		}
		else if(m_FormaPago.value=="<bean:write name='CONS_ABONO_CTA_OTRO'/>"){
            isCtaOtroBco1.style.display = "block";
            //isCtaOtroBco2.style.display = "block";

            isCtaFinanciero1.style.display = "none";
            //isCtaFinanciero2.style.display = "none";
		}
        else{
            isCtaOtroBco1.style.display = "none";
            //isCtaOtroBco2.style.display = "none";
            isCtaFinanciero1.style.display = "none";
            //isCtaFinanciero2.style.display = "none";
        }
    }
    function existeIdProveedor(url){
      if (window.XMLHttpRequest) { // Non-IE browsers
        req = new XMLHttpRequest();
        req.onreadystatechange = processExiste;
        try {req.open("GET", url, true);}
        catch (e) {alert(e);}
        req.send(null);
      } else if (window.ActiveXObject) { // IE
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (req) {
          req.onreadystatechange = processExiste;
          req.open("GET", url, true);
          req.send();
        }
      }
    }
    function processExiste(){
      if (req.readyState==4){
        if (req.status==200) parseExist(); //el requerimiento ha sido satisfactorio
        else alert("Ocurrió un error al procesar los datos");
      }
    }
    function parseExist(){
        //obtenemos el xml
        var response = req.responseXML;
        if(response==null) alert("Ocurrió un error al procesar los datos");
        //obtenemos la respuesta
        var respuesta = response.getElementsByTagName("respuesta");
        var valor = respuesta[0].getAttribute("valor");
        if(valor=="NO"){
                var frm = document.forms[0];
                frm.action = "proveedor.do?do=guardarProveedor";
                frm.submit();
        }
        else{
             var frm = document.forms[0];             
             alert("Ya existe ese Id de Proveedor para esa Empresa");
             frm.m_IdProveedor.select();
             frm.m_IdProveedor.focus();
        }
    }
    function validarIngreso(){
        var m_Empresa = document.getElementById("m_Empresa");
        var m_IdProveedor = document.getElementById("m_IdProveedor");
        var m_Nombre = document.getElementById("m_Nombre");
        var m_TipoDocumento = document.getElementById("m_TipoDocumento");
        var m_NroDocumento = document.getElementById("m_NroDocumento");
        var m_FormaPago = document.getElementById("m_FormaPago");
        //jwong 23/04/2009 nuevo campo
        var m_Contrapartida = document.getElementById("m_Contrapartida");
        
        //banco financiero
        var m_TipoCuenta = document.getElementById("m_TipoCuenta");
        //var m_MonedaFinan = document.getElementById("m_MonedaFinan");
        var m_NroCuenta = document.getElementById("m_NroCuenta");

        //jwong validacion de moneda
        var m_Moneda = document.getElementById("m_Moneda");

        //otro banco
        var m_Banco = document.getElementById("m_Banco");
        //var m_MonedaOtrBco = document.getElementById("m_MonedaOtrBco");
        var m_NroCuentaCCI = document.getElementById("m_NroCuentaCCI");
        
        if( m_Empresa.value!=null && m_Empresa.value!="" &&
            m_IdProveedor.value!=null && m_IdProveedor.value!="" &&
            m_Nombre.value!=null && m_Nombre.value!="" &&
            m_TipoDocumento.value!=null && m_TipoDocumento.value!="" &&
            m_NroDocumento.value!=null && m_NroDocumento.value!="" &&
            //nuevo campo
            m_Contrapartida.value!=null && m_Contrapartida.value!="" &&
            //nuevo campo
            m_Moneda.value!=null && m_Moneda.value!="" &&
            
            m_FormaPago.value!=null && m_FormaPago.value!=""){
                
            if(m_FormaPago.value=="<bean:write name='CONS_ABONO_CTA_FINAN'/>"){
                if( m_TipoCuenta.value!=null && m_TipoCuenta.value!="" &&
                    //m_MonedaFinan.value!=null && m_MonedaFinan.value!="" &&
                    m_NroCuenta.value!=null && m_NroCuenta.value!=""){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(m_FormaPago.value=="<bean:write name='CONS_ABONO_CTA_OTRO'/>"){
                if( m_Banco.value!=null && m_Banco.value!="" &&
                    //m_MonedaOtrBco.value!=null && m_MonedaOtrBco.value!="" &&
                    m_NroCuentaCCI.value!=null && m_NroCuentaCCI.value!=""){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return true;
            }
        }
        else{return false;}
    }

    //valida la longitud de algunos campos a ingresar
    function validarLongitud(){

        var m_NroDocumento = document.getElementById("m_NroDocumento");
        var m_FormaPago = document.getElementById("m_FormaPago");
        //banco financiero
        var m_NroCuenta = document.getElementById("m_NroCuenta");
        //otro banco
        var m_NroCuentaCCI = document.getElementById("m_NroCuentaCCI");
        var m_TipoDocumento = document.getElementById("m_TipoDocumento");
        if(m_TipoDocumento.value == "01" && (m_NroDocumento.value.length<8 || m_NroDocumento.value.length>8)){
            alert("Ingresar número de documento válido");
            m_NroDocumento.select();
            m_NroDocumento.focus();
            return false;
        }
        if(m_TipoDocumento.value == "02" && (m_NroDocumento.value.length < 11 || m_NroDocumento.value.length > 11)){
            alert("Ingresar número de documento válido");
            m_NroDocumento.select();
            m_NroDocumento.focus();
            return false;
        }

        if(m_FormaPago.value=="<bean:write name='CONS_ABONO_CTA_FINAN'/>"){
            if( m_NroCuenta.value.length<9){
                alert("Ingresar número de cuenta válida");
                m_NroCuenta.select();
                m_NroCuenta.focus();
                return false;
            }
        }
        else if(m_FormaPago.value=="<bean:write name='CONS_ABONO_CTA_OTRO'/>"){
            if( m_NroCuentaCCI.value.length<11){
                alert("Ingresar número de cuenta CCI válida");
                m_NroCuentaCCI.select();
                m_NroCuentaCCI.focus();
                return false;
            }
        }
        return true;
    }
     function verificarIdProveedor(campo) {//jmoreno
          val_int(campo)
          var idProveedor = campo.value;
          if(idProveedor.length==0){
              return true;
          }else{
            while(idProveedor.length < 10){
                idProveedor = "0" + idProveedor;
            }
            campo.value = idProveedor;
            return true;
          }
          
     }
    
    -->
</script>

</head>
<body onload="javascript:cambiaFormaPago();">
<html:form action="proveedor.do">
  <table width="100%" CELLSPACING="0" CELLPADDING="4">
    <tr>
      <td valign="middle" align="left" class="Title"><bean:message key="administracion.title.proveedores"/></td>
    </tr>
  </table>
  <table width="100%" cellpadding="2" cellspacing="2" border="0">
    <tr class="TitleRow5">
      <html:hidden property="m_Empresa"/>
      <td >&nbsp;Empresa:</td>
      <td ><bean:write name="m_DescEmpresa" /></td>
      <html:hidden property="m_IdServEmp"/>
        <td >Servicio:</td>
        <td ><bean:write name="m_Servicio" /></td>

    </tr>
  	<tr>
      <td class="TitleRow3" colspan="4">Datos del Proveedor</td>
    </tr>
    <tr>
      
	  <td class="CellColRow" width="25%">ID proveedor:</td>
      <td class="CellColRow2" width="25%">
        <html:text property="m_IdProveedor" styleId="m_IdProveedor" size="10" maxlength="10" value="" styleClass="CellColRow8" onkeypress="numero()" onblur="verificarIdProveedor(this)"/>
        <script>
            document.forms[0].m_IdProveedor.focus();
        </script>
      </td>
      <td colspan="2" class='CellColRow'></td>
    </tr>
    <tr>
      <td class="CellColRow">Nombre/Razón Social:</td>
      <td class="CellColRow2" colspan="1">
        <html:text property="m_Nombre" styleId="m_Nombre" style="text-transform:uppercase;" size="50" maxlength="50" value="" styleClass="CellColRow8" onkeypress="return soloDescripcion(this, event)" onblur="gDescripcion(this)"/>
      </td>
      <!--jwong nuevo campo-->
      <td class="CellColRow">Contrapartida:</td>
      <td class="CellColRow2" colspan="1">
        <html:text property="m_Contrapartida" styleId="m_Contrapartida" style="text-transform:uppercase;" size="20" maxlength="20" value="" styleClass="CellColRow8" onkeypress="return soloDescripcion(this, event)" onblur="gDescripcion(this)"/>
      </td>
    </tr>
	
	<tr>
	  <td class="CellColRow">Tipo de Documento:</td>
      <td class="CellColRow2">
        <html:select property="m_TipoDocumento" styleId="m_TipoDocumento" styleClass="CellColRow8" value="">
          <logic:notEmpty name="listaTipoDocs">
              <html:options collection="listaTipoDocs" property="id.clfCode" labelProperty="dlfDescription"/>
          </logic:notEmpty>
        </html:select>
      </td>
	  <td class="CellColRow">N&uacute;mero de Documento:</td>
      <td class="CellColRow2">
        <html:text property="m_NroDocumento" styleId="m_NroDocumento" value="" size="11" maxlength="11" styleClass="CellColRow8" onkeypress="numero()" onblur="val_int(this)"/>
      </td>
    </tr>
	
	<tr>
      <td class="CellColRow">Forma de Pago:</td>
      <td class="CellColRow2">
        <%--html:select property="m_FormaPago" styleId="m_FormaPago" styleClass="CellColRow"--%>
        <html:select property="m_FormaPago" styleId="m_FormaPago" styleClass="CellColRow8" onchange="javascript:cambiaFormaPago();" value="">
          <logic:notEmpty name="listaTipoPago">
              <html:options collection="listaTipoPago" property="m_TipoPagoServicio" labelProperty="m_Descripcion"/>
          </logic:notEmpty>
        </html:select>
      </td>
	  <!--td class="CellColRow">&nbsp;</td>
      <td class="CellColRow2">&nbsp;</td-->
      <td class='CellColRow'>Moneda:</td>
      <td class='CellColRow2'>
        <html:select property="m_Moneda" styleId="m_Moneda" styleClass="CellColRow8" value="">
          <logic:notEmpty name="listaTipoMoneda">
              <html:options collection="listaTipoMoneda" property="id.clfCode" labelProperty="dlfDescription"/>
          </logic:notEmpty>
        </html:select>
      </td>
    </tr>


    <%--
    <tr id="isCtaFinanciero" style="display:none">
      <td class="CellColRow" colspan="4">
	  	<table cellpadding='0' cellspacing='0' border='0' width='100%'>
        --%>
          <tr id="isCtaFinanciero1" style="display:none">
              <td class='CellColRow'>Tipo de Cuenta:</td>
              <td class='CellColRow2'>
                <html:select property="m_TipoCuenta" styleId="m_TipoCuenta" styleClass="CellColRow8" value="">
                  <logic:notEmpty name="listaTipoCuenta">
                      <html:options collection="listaTipoCuenta" property="id.clfCode" labelProperty="dlfDescription"/>
                  </logic:notEmpty>
                </html:select>
              </td>
              <%--
              <td class='CellColRow'>Moneda:</td>
              <td class='CellColRow2'>
                <html:select property="m_MonedaFinan" styleId="m_MonedaFinan" styleClass="CellColRow8" value="">
                  <logic:notEmpty name="listaTipoMoneda">
                      <html:options collection="listaTipoMoneda" property="id.clfCode" labelProperty="dlfDescription"/>
                  </logic:notEmpty>
                </html:select>
              </td>
          </tr>
          <tr id="isCtaFinanciero2" style="display:none">
              --%>
              <td class='CellColRow'>N&uacute;mero de Cuenta:</td>
              <!--td class='CellColRow2' colspan=3-->
              <td class='CellColRow2'>
                  <html:text property="m_NroCuenta" styleId="m_NroCuenta" size="14" maxlength="12" styleClass="CellColRow8" onkeypress="numero()" onblur="verificarCta(this)" value=""/>
              </td>
          </tr>
        <%--
        </table>
	  </td>
    </tr>
    --%>

    <%--
    <tr id="isCtaOtroBco" style="display:none">
      <td class="CellColRow" colspan="4">
	  	<table cellpadding='0' cellspacing='0' border='0' width='100%'>
        --%>
          <tr id="isCtaOtroBco1" style="display:none">
              <td class='CellColRow'>Banco:</td>
              <td class='CellColRow2'>
                <html:select property="m_Banco" styleId="m_Banco" styleClass="CellColRow8" value="">
                  <logic:notEmpty name="listaBanco">
                      <html:options collection="listaBanco" property="cbaIdBanco" labelProperty="cbaNombre"/>
                  </logic:notEmpty>
                </html:select>
              </td>
              <%--
              <td class='CellColRow'>Moneda:</td>
              <td class='CellColRow2'>
                <html:select property="m_MonedaOtrBco" styleId="m_MonedaOtrBco" styleClass="CellColRow8" value="">
                  <logic:notEmpty name="listaTipoMoneda">
                      <html:options collection="listaTipoMoneda" property="id.clfCode" labelProperty="dlfDescription"/>
                  </logic:notEmpty>
                </html:select>
              </td>
          </tr>
          <tr id="isCtaOtroBco2" style="display:none">
              --%>
              <td class='CellColRow'>Cuenta Interbancaria (CCI):</td>
              <td class='CellColRow2' colspan=3>
                  <html:text property="m_NroCuentaCCI" styleId="m_NroCuentaCCI" size="12" maxlength="14" styleClass="CellColRow8" onkeypress="numero()" onblur="verificarCta(this)" value=""/>
              </td>
          </tr>
        <%--
        </table>
	  </td>
    </tr>
	--%>
    
	<tr align="right">
      <td colspan="4" class="CellColRow5">
        <img src="img/bt-aceptar.png" align="middle" onClick="javascript:aceptar();" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'"/>
		<img src="img/bt-cancelar.png" align="middle" onClick="javascript:cancelar();" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'"/>
      </td>
    </tr>
  </table>
</html:form>
</body>
</html>
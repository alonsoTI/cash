<%-- 
    Document   : modificarPersonal
    Created on : 18-feb-2009, 9:29:15
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
    
    function imprimir(){
	  window.print();
	}
	function exportar(){
		location.href = "exportarExcel.html";
	}
	
    function aceptar(){
        //debemos validar el ingreso correcto de datos para el ultimo registro
        var valido = validarIngreso();
        if(valido==true){
            //validamos la longitud de algunos campos antes de ingresarlos
            if(validarLongitud()){
                //validamos el email de la ultima fila ingresada
                var campoemail = document.getElementById("m_Email");
                if(!isEmail(campoemail.value)){
                    alert("Formato incorrecto para email");
                    campoemail.focus();
                    campoemail.select();
                }
                else{
                    var m_Empresa = document.getElementById("m_Empresa");
                    /*m_Empresa.disabled = false;*/
                    var frm = document.forms[0];                    
                    frm.action = "personal.do?do=modificarPersonal&m_IdEmpleado="+frm.m_IdEmpleado.value;
                    frm.submit();
                }
            }
        }
        else{
            alert("Ingresar datos del personal");
        }
	}
    
    function eliminar(){
        var frm = document.forms[0];        
        frm.action = "personal.do?do=eliminarPersonal&m_IdEmpleado="+frm.m_IdEmpleado.value+"&m_Empresa="+frm.m_Empresa.value;
        frm.submit();
    }

	function cancelar(){
		location.href = "administracion.do?do=cargarMantenimientoPersonal";
	}
    
    //valida que se hayan llenado todos los campos de la fila con indice = ind
    function validarIngreso(){        
        var m_IdEmpleado = document.getElementById("m_IdEmpleado");
        var m_Nombre = document.getElementById("m_Nombre");
        var m_DNI = document.getElementById("m_DNI");
        var m_NroCelular = document.getElementById("m_NroCelular");
        var m_Email = document.getElementById("m_Email");        
        var m_Contrapartida = document.getElementById("m_Contrapartida");
        
        var m_FormaPagoCTS = document.getElementById("m_FormaPagoCTS");
        var m_TipoCuentaCTS = document.getElementById("m_TipoCuentaCTS");
        var m_MonedaCTS = document.getElementById("m_MonedaCTS");
        var m_NroCuentaCTS = document.getElementById("m_NroCuentaCTS");
        var m_ImporteCTS = document.getElementById("m_ImporteCTS");
        
         if( (m_IdEmpleado.value!=null && m_IdEmpleado.value!="") &&
            (m_Nombre.value!=null && m_Nombre.value!="") &&
            (m_DNI.value!=null && m_DNI.value!="") &&
            (m_NroCelular.value!=null && m_NroCelular.value!="") &&
            (m_Email.value!=null && m_Email.value!="") &&
            (m_Contrapartida.value!=null && m_Contrapartida.value!=""))
        {
    
            var band = false;
              
                if(m_ImporteCTS.value!=""){

                    if(((m_FormaPagoCTS.value!=null && m_FormaPagoCTS.value=="CRE") &&
                    (m_TipoCuentaCTS.value!=null && m_TipoCuentaCTS.value!="") &&
                    (m_MonedaCTS.value!=null && m_MonedaCTS.value!="") &&
                    (m_NroCuentaCTS.value!=null && m_NroCuentaCTS.value!="") &&
                    (m_ImporteCTS.value!=null && m_ImporteCTS.value!=""))
                    ||
                    ((m_FormaPagoCTS.value!=null && m_FormaPagoCTS.value!="CRE") &&
                    (m_TipoCuentaCTS.value!=null && m_TipoCuentaCTS.value!="") &&
                    (m_MonedaCTS.value!=null && m_MonedaCTS.value!="") &&
                    (m_ImporteCTS.value!=null && m_ImporteCTS.value!="")))
                    {
                        band = true;
                    }else
                    {
                        return false;
                    }


                }
                if(m_ImporteCTS.value==""){
                    return false;
                }
                if(band){
                   return true;
                }else{
                    return false;
                }

        }
        else{
            return false;
        }
    }

    function valida_decimal(campo){
        if(campo.value!=""){
            if(!valida_dec(campo.value)){
                alert("El importe debe contener solo un punto decimal");
                campo.focus();
                campo.select();
            }
        }
    }
    
    function habilitarCtaCTS(){
        var frm = document.forms[0];
        if(frm.m_FormaPagoCTS.disabled == false){
            var formaPago = frm.m_FormaPagoCTS.options[frm.m_FormaPagoCTS.selectedIndex].value;            
            if(formaPago == "CRE"){
                frm.m_NroCuentaCTS.disabled = false;
                frm.m_TipoCuentaCTS.disabled = false;
            }else{                
                frm.m_NroCuentaCTS.disabled = true;
                frm.m_NroCuentaCTS.value = "";
                frm.m_TipoCuentaCTS.disabled = true;
            }
        }
        
    }
    //valida la longitud de algunos campos a ingresar
    function validarLongitud(){
        var m_DNI = document.getElementById("m_DNI");
        var m_NroCelular = document.getElementById("m_NroCelular");

        //pago cts
        var m_FormaPagoCTS = document.getElementById("m_FormaPagoCTS");
        var m_TipoCuentaCTS = document.getElementById("m_TipoCuentaCTS");
        var m_MonedaCTS = document.getElementById("m_MonedaCTS");
        var m_NroCuentaCTS = document.getElementById("m_NroCuentaCTS");
        var m_ImporteCTS = document.getElementById("m_ImporteCTS");

        if(m_DNI.value.length<8){
            alert("Ingresar número de DNI de 8 dígitos");
            m_DNI.focus();
            m_DNI.select();
            return false;
        }
        if(m_NroCelular.value.length<8){
            alert("Ingresar número de celular válido");
            m_NroCelular.focus();
            m_NroCelular.select();
            return false;
        }
     
        if(
            (m_FormaPagoCTS.value!=null && m_FormaPagoCTS.value!="") &&
            (m_TipoCuentaCTS.value!=null && m_TipoCuentaCTS.value!="") &&
            (m_MonedaCTS.value!=null && m_MonedaCTS.value!="") &&
            (m_NroCuentaCTS.value!=null && m_NroCuentaCTS.value!="") &&
            (m_ImporteCTS.value!=null && m_ImporteCTS.value!="")){
            if(m_NroCuentaCTS.value.length<9){
                alert("Ingresar número de cuenta para pago de CTS válida");
                m_NroCuentaCTS.focus();
                m_NroCuentaCTS.select();
                return false;
            }
        }
        
        return true;
    }
    function verificarCta(campo) {
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
    
</script>

</head>
<body onload="habilitarCtaCTS();">
<html:form action="personal.do">
  <table width="100%" CELLSPACING="0" CELLPADDING="4">
    <tr>
      <td valign="middle" align="left" class="Title"><bean:message key="administracion.title.personal"/></td>
    </tr>

    <tr align="left">
      <td colspan="3">
        <img src="img/bt-aceptar.png" align="middle" onClick="javascript:aceptar();" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'"/>
        <img src="img/bt-eliminar.png" align="middle" onClick="javascript:eliminar();" onMouseOver="this.src='img/bt-eliminar2.png'" onMouseOut="this.src='img/bt-eliminar.png'"/>
        <img src="img/bt-cancelar.png" align="middle" onClick="javascript:cancelar();" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'"/>
      </td>
    </tr>
  </table>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <html:hidden property="m_Empresa"/>
      <td  class="TitleRow6">Empresa:</td>
      <td  class="TitleRow5"><bean:write name="m_DescEmpresa" /></td>
      <td  class="TitleRow6">Servicio:</td>
      <td  colspan="2" class="TitleRow5"><bean:write name="m_Servicio" /></td>
      <td colspan="6" class="TitleRow5"></td>
    </tr>
   	<tr>
      <td class="TitleRow3" colspan="11">Datos Personales</td>
    </tr>
    
    <tr>
      <!--td class="CellColRowCenter" rowspan="2">Empresa</td-->
      <td class="CellColRowCenter" rowspan="2">ID empleado</td>
      <td class="CellColRowCenter" rowspan="2">Nombre</td>
      <td class="CellColRowCenter" rowspan="2">DNI</td>
      <td class="CellColRowCenter" rowspan="2">N&uacute;mero de celular</td>
      <td class="CellColRowCenter" rowspan="2">Email</td>
      <td class="CellColRowCenter" rowspan="2">Contrapartida</td>
      <td class="CellColRowCenter" rowspan="1" colspan="5"><bean:write name="m_Servicio" />&nbsp;<logic:empty name="listaTipoPago">(No Habilitado)</logic:empty></td>
    </tr>
    <tr>     
      <!-- pago cts -->
      <td class="CellColRowCenter">Forma de Pago</td>
      <td class="CellColRowCenter">Tipo de Cuenta</td>
      <td class="CellColRowCenter">Moneda</td>
      <td class="CellColRowCenter">N&uacute;mero de Cuenta</td>
      <td class="CellColRowCenter">Importe</td>
    </tr>
    
    <tr>
       <html:hidden property="m_IdServEmp"/>
      <%--td class="CellColRow2">
        <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow8" disabled="true">
          <logic:notEmpty name="listaEmpresas">
            <html:options collection="listaEmpresas" property="cemIdEmpresa" labelProperty="demNombre"/>
          </logic:notEmpty>
        </html:select>
      </td--%>
	  <td class="CellColRow2">
          <html:text property="m_IdEmpleado" styleId="m_IdEmpleado" size="10" maxlength="10" styleClass="CellColRow8" readonly="true" onkeypress="numero()" onblur="val_int(this)" disabled="true"/>
      </td>
      <td class="CellColRow2">
        <html:text property="m_Nombre" styleId="m_Nombre" style="text-transform:uppercase;" size="50" maxlength="50" styleClass="CellColRow8" onkeypress="return soloDescripcionNombre(this, event)" onblur="gDescripcionNombre(this)"/>
        <script>
            document.forms[0].m_Nombre.focus();
        </script>
      </td>
	  <td class="CellColRow2">
        <html:text property="m_DNI" styleId="m_DNI" size="8" maxlength="8" styleClass="CellColRow8" onkeypress="numero()" onblur="val_int(this)"/>
      </td>
      <td class="CellColRow2">
        <html:text property="m_NroCelular" styleId="m_NroCelular" size="15" maxlength="15" styleClass="CellColRow8" onkeypress="numero()" onblur="val_int(this)"/>
      </td>
	  <td class="CellColRow2">
        <html:text property="m_Email" styleId="m_Email" style="text-transform:uppercase;" size="40" maxlength="40" styleClass="CellColRow8"/>
      </td>

      <td class="CellColRow2">
        <html:text property="m_Contrapartida" styleId="m_Contrapartida" style="text-transform:uppercase;" size="20" maxlength="20" styleClass="CellColRow8" onkeypress="return soloDescripcion(this, event)" onblur="gDescripcion(this)"/>
      </td>
      
      <!-- pago cts -->
      <!-- si tiene habilitado el servicio la empresa -->
      <logic:notEmpty name="listaTipoPago">
          <td class="CellColRow2">
              <html:select property="m_FormaPagoCTS" styleId="m_FormaPagoCTS" styleClass="CellColRow8" onchange="habilitarCtaCTS();">
                <logic:notEmpty name="listaTipoPago">
                    <html:options collection="listaTipoPago" property="m_TipoPagoServicio" labelProperty="m_Descripcion"/>
                </logic:notEmpty>
            </html:select>
          </td>
          <td class="CellColRow2">
              <html:select property="m_TipoCuentaCTS" styleId="m_TipoCuentaCTS" styleClass="CellColRow8" disabled="true">
                <logic:notEmpty name="listaTipoCuenta">
                    <html:options collection="listaTipoCuenta" property="id.clfCode" labelProperty="dlfDescription"/>
                </logic:notEmpty>
            </html:select>
          </td>
          <td class="CellColRow2">
            <html:select property="m_MonedaCTS" styleId="m_MonedaCTS" styleClass="CellColRow8">
                <logic:notEmpty name="listaTipoMoneda">
                    <html:options collection="listaTipoMoneda" property="id.clfCode" labelProperty="dlfDescription"/>
                </logic:notEmpty>
            </html:select>
          </td>
          <td class="CellColRow2">
            <html:text property="m_NroCuentaCTS" styleId="m_NroCuentaCTS" maxlength="12" size="20" styleClass="CellColRow8" onkeypress="numero()" onblur="verificarCta(this)"/>
          </td>
          <td class="CellColRow2">
            <html:text property="m_ImporteCTS" styleId="m_ImporteCTS" maxlength="9" size="9" styleClass="CellColRowRight" onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)"/>
          </td>
      </logic:notEmpty>
      
      <!-- si no tiene habilitado el servicio la empresa -->
      <logic:empty name="listaTipoPago">
          <td class="CellColRow2">
            <html:select property="m_FormaPagoCTS" styleId="m_FormaPagoCTS" styleClass="CellColRow8" disabled="true">
                <logic:notEmpty name="listaTipoPago">
                    <html:options collection="listaTipoPago" property="m_TipoPagoServicio" labelProperty="m_Descripcion"/>
                </logic:notEmpty>
            </html:select>
          </td>
          <td class="CellColRow2">
            <html:select property="m_TipoCuentaCTS" styleId="m_TipoCuentaCTS" styleClass="CellColRow8" disabled="true">
                <logic:notEmpty name="listaTipoCuenta">
                    <html:options collection="listaTipoCuenta" property="id.clfCode" labelProperty="dlfDescription"/>
                </logic:notEmpty>
            </html:select>
          </td>
          <td class="CellColRow2">
            <html:select property="m_MonedaCTS" styleId="m_MonedaCTS" styleClass="CellColRow8" disabled="true">
                <logic:notEmpty name="listaTipoMoneda">
                    <html:options collection="listaTipoMoneda" property="id.clfCode" labelProperty="dlfDescription"/>
                </logic:notEmpty>
            </html:select>
          </td>
          <td class="CellColRow2">
            <html:text property="m_NroCuentaCTS" readonly="true" styleId="m_NroCuentaCTS" maxlength="12" size="20" styleClass="CellColRow8" onkeypress="numero()" onblur="verificarCta(this)" disabled="true"/>
          </td>
          <td class="CellColRow2">
            <html:text property="m_ImporteCTS" readonly="true" styleId="m_ImporteCTS" maxlength="9" size="9" styleClass="CellColRowRight" onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)" disabled="true"/>
          </td>
      </logic:empty>
    </tr>
  </table>
</html:form>
</body>
</html>
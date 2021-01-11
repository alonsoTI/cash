<%-- 
    Document   : detallePagoLuz
    Created on : 27-mar-2009, 11:32:03
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>

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

	function primero(){

	}
	function anterior(){

	}
	function siguiente(){

	}
	function ultimo(){

	}
    function aceptar(){
        //antes de enviar el pago se debe validar que cubra eñ monto la cuenta seleccionada
        if(validarMontoCuenta()){
            var frm = document.forms[0];
            frm.action = "pagoServicio.do?do=pagarLuz";
            frm.submit();
        }
        else{
            document.getElementById("m_CuentaCargo").focus();
            alert("Seleccione una cuenta que tenga saldo disponible mayor o igual al importe a pagar");
        }
	}

	function cancelar(){
		location.href = "pagoLuz.jsp";
	}

    function validarMontoCuenta(){
        //se debe validar que el saldo de la cuenta seleccionada sea mayor o igual al monto que se pagara
        var importePagoServicio = document.getElementById("m_Importe").value.replace(" ", "");
        var codeServicio = document.getElementById("m_CuentaCargo").value;
        var codes = codeServicio.split(";");
        var saldoDisponible = codes[1].replace(" ", "");

        //verificamos que el saldo disponible de la cuenta seleccionada pueda cubrir el pago del servicio
        if(parseFloat(saldoDisponible) < parseFloat(importePagoServicio)){
            return false;
        }
        else{
            return true;
        }
    }
    -->
</script>

</head>
<body>
<html:form action="pagoServicio.do">
  <table width="100%" CELLSPACING="0" CELLPADDING="4">
    <tr>
      <td valign="middle" align="left" class="Title">Pago del Servicio de Luz</td>
    </tr>
  </table>
  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class="TitleRow2" width="25%" ><bean:write name="lblSuministro"/></td>
      <td class="CellColRow2" width="75%">
        <html:text property="m_NumSuministro" styleId="m_NumSuministro" readonly="true" name="detallePagoServ" size="15" maxlength="15" styleClass="CellColRow8" onkeypress="numero()" onblur="val_int(this)"/>
      </td>
    </tr>
    <tr>
	  <td class="TitleRow2">Referencia:</td>
      <td class="CellColRow2">
        <html:text property="m_Referencia" styleId="m_Referencia" size="50" maxlength="50" value="" styleClass="CellColRow8" onkeypress="soloDescripcionNombre(this,event)" onblur="gDescripcionNombre(this)"/>
      </td>
    </tr>

    <tr>
	  <td class="TitleRow2">Cuenta Cargo</td>
      <td class="CellColRow2">
        <html:select property="m_CuentaCargo" styleId="m_CuentaCargo" value="" styleClass="CellColRow2">
            <logic:notEmpty name="listaccounts">
                <html:options collection="listaccounts" property="m_CodigoImporte" labelProperty="m_DescripcionSaldoDisponible"/>
            </logic:notEmpty>
        </html:select>
      </td>
    </tr>

    <tr>
      <td class="TitleRow2">Importe Total:</td>
      <td class="CellColRow2">
        <html:text property="m_Importe" styleId="m_Importe" readonly="true" name="detallePagoServ" size="15" maxlength="15" styleClass="CellColRow8"/>
      </td>
    </tr>

    <tr>
        <td colspan="2" align="right">            
            <img src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:cancelar();"/>
            <img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:aceptar();"/>
        </td>
    </tr>
  </table>

  <%--Recibos por pagar--%>
  <logic:notEmpty name="listaResultPagar">
  <layout:collection name="listaResultPagar" title="DETALLE DE RECIBOS POR PAGAR" styleClass="FORM" id="deuda" sortAction="client" width="100%" align="center" indexId="h" >
    <layout:collectionItem title="N&uacute;mero Recibo" property="m_NumRecibo" sortable="true" />
    <layout:collectionItem title="Nombre Cliente" property="m_NombreCliente" sortable="true" />
    <layout:collectionItem title="Fecha Emisión" property="m_FechaEmision" sortable="true" />
    <layout:collectionItem  title="Moneda" property="m_Moneda" />
    <layout:collectionItem title="Importe" sortable="true">
        <div align="right">
            <bean:write name='deuda' property='m_Importe' />
        </div>
    </layout:collectionItem>

    <layout:collectionItem style="display:none">
        <input style="display:none" type="checkbox" checked id="m_chkReciboPago" name="m_chkReciboPago" value="<bean:write name='deuda' property='parametrosCadena' />" />
    </layout:collectionItem>

  </layout:collection>
  <layout:space/>
  </logic:notEmpty>
  <html:hidden property="m_IdEmpCodCliente" styleId="m_IdEmpCodCliente" />
</html:form>
</body>
</html>
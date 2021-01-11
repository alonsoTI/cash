<%-- 
    Document   : detallePagoClaro
    Created on : 31/03/2009, 11:13:56 PM
    Author     : Administrador
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
        //antes de enviar el pago se debe validar que cubra el monto la cuenta seleccionada
        //if(validarMontoCuenta()){
            var frm = document.forms[0];
            frm.action = "pagoServicio.do?do=pagarClaro";
            frm.submit();
        /*
        }
        else{
            document.getElementById("m_CuentaCargo").focus();
            alert("Seleccione una cuenta que tenga saldo disponible mayor o igual al importe a pagar");
        }
        */
	}

	function cancelar(){
		location.href ="pagoServicio.do?do=buscarPagoClaro&"+document.forms[0].data.value; //"pagoClaro.jsp";
	}
    /*
    function validarMontoCuenta(){
        //se debe validar que el saldo de la cuenta seleccionada sea mayor o igual al monto que se pagara
        var importePagoServicio;
        var codeServicio = document.getElementById("m_CuentaCargo").value;
        var codes = codeServicio.split(";");
        var saldoDisponible = codes[1].replace(" ", "");
        var moneda = codes[2];
        if(moneda == "S/."){
            importePagoServicio = document.getElementById("m_MontoTotalSoles").value.replace(" ", "");
        }
        else{
            importePagoServicio = document.getElementById("m_MontoTotalDolares").value.replace(" ", "");
        }
        //verificamos que el saldo disponible de la cuenta seleccionada pueda cubrir el pago del servicio
        if(parseFloat(saldoDisponible) < parseFloat(importePagoServicio)){
            return false;
        }
        else{
            return true;
        }
    }
    */
    -->
</script>

</head>
<body>
<html:form action="pagoServicio.do">
  <table width="100%" CELLSPACING="0" CELLPADDING="4">
    <tr>
      <td align="center" class="Title"><bean:write name="serv_pago" property="m_Titulo" /></td>
    </tr>
  </table>


  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <%--logic:notEmpty name="listaSector">
      <tr>
          <td class="TitleRow2" width="25%" >Localidad:</td>
          <td class="CellColRow2" width="75%">
              <bean:write property="m_DescSector" name="serv_pago" />
          </td>
      </tr>
    </logic:notEmpty--%>
    <tr>
      <td class="TitleRow2" width="25%" >N&uacute;mero de Abonado</td>
      <td class="CellColRow2" width="75%">
        <html:text property="m_NumAbonado" styleId="m_NumAbonado" name="serv_pago" readonly="true" size="15" maxlength="15" styleClass="CellColRow8" onkeypress="numero()" onblur="val_int(this)"/>
      </td>
    </tr>
    <tr>
	  <td class="TitleRow2">Referencia:</td>
      <td class="CellColRow2">
        <%--html:text property="m_Referencia" styleId="m_Referencia" name="serv_pago" readonly="true" size="50" maxlength="50" value="" styleClass="CellColRow8" onkeypress="return soloDescripcion(this,event)" onblur="gDescripcion(this)"/--%>
        <html:text property="m_Referencia" styleId="m_Referencia" name="serv_pago" readonly="false" size="50" maxlength="50" value="" styleClass="CellColRow8" />
      </td>
    </tr>

    <tr>
	  <td class="TitleRow2">Cuenta Cargo</td>
      <td class="CellColRow2">
        <html:select property="m_CuentaCargo" styleId="m_CuentaCargo" value="" styleClass="CellColRow2">
            <logic:notEmpty name="listaccounts">
                <html:options collection="listaccounts" property="m_CodigoImporteMoneda" labelProperty="m_DescripcionSaldoDisponible"/>
            </logic:notEmpty>
        </html:select>
      </td>
    </tr>
    <%--
    <tr>
      <td class="TitleRow2">Importe Total:</td>
      <td class="CellColRow2">
        Soles&nbsp;<html:text property="m_MontoTotalSoles" styleId="m_MontoTotalSoles" readonly="true" name="serv_pago" size="15" maxlength="15" styleClass="CellColRow8"/>
        D&oacute;lares&nbsp;<html:text property="m_MontoTotalDolares" styleId="m_MontoTotalDolares" readonly="true" name="serv_pago" size="15" maxlength="15" styleClass="CellColRow8"/>
      </td>
    </tr>
    --%>
    <tr>
        <td colspan="2" align="right">
            <img src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:cancelar();"/>
            <img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:aceptar();"/>            
        </td>
    </tr>
  </table>
  <%--Recibos por pagar--%>
  <logic:notEmpty name="listaResultPagar">
  <table width="100%" cellspacing="2" border="0" >
        <tr>
            <td class='TitleRow3'>DETALLE DE RECIBOS POR PAGAR</td>
        </tr>
    </table>
 <table width="100%" border="0" >
  <layout:collection name="listaResultPagar" styleClass="FORM" id="deuda" sortAction="client" width="100%" align="center" indexId="h" >
    <layout:collectionItem title="N&uacute;mero Recibo" property="m_NumRecibo" sortable="true" />
    <layout:collectionItem title="Descripci&oacute;n" property="m_Descripcion" sortable="true" />   
    <layout:collectionItem  title="Estado Deuda" property="m_EstadoDeuda" />
    <layout:collectionItem  title="Tipo Documento" property="m_TipoDocumento" />
    <layout:collectionItem  title="N&uacute;mero Documento" property="m_NumeroDocumento" />
    <layout:collectionItem title="Fecha Emisi&oacute;n" property="m_FecEmisFormateada" sortable="true" />
    <layout:collectionItem  title="Fecha Vencimiento" property="m_FecVencFormateada" />
    <layout:collectionItem  title="Referencia" property="m_Referencia" />
    <layout:collectionItem title="Moneda">
        <div align="center">
            <bean:write name='deuda' property='m_DescrMoneda' />
        </div>
    </layout:collectionItem>
    <layout:collectionItem title="Importe" sortable="true">
        <div align="right">
            <bean:write name='deuda' property='m_ImporteMostrar' />
        </div>
    </layout:collectionItem>

    <layout:collectionItem style="display:none">
        <input style="display:none" type="checkbox" checked id="m_chkRecibo" name="m_chkRecibo" value="<bean:write name='deuda' property='parametrosCadenaClaro' />" />
    </layout:collectionItem>
  </layout:collection>
  </table>
  <layout:space/>
  </logic:notEmpty>
  <html:hidden property="m_Empresa" styleId="m_Empresa" name="serv_pago" />
  <input type="hidden" name="data" id="data" value="<%= request.getAttribute("data") %>">
</html:form>
</body>
</html>

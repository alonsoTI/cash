<%-- 
    Document   : consultaMovHistorico
    Created on : 04-dic-2008, 9:44:38
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
    //obtenemos la fecha actual
    String fechaActual = com.hiper.cash.util.Fecha.getFechaActual("dd/MM/yyyy");
%>

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
    function buscar(){
	  //obtenemos el tipo de consulta seleccionado
	  var m_TipoConsulta =  document.getElementById("m_TipoConsulta");
      var m_TipoCon;
	  if(m_TipoConsulta==null || m_TipoConsulta.value==""){
	  	alert("Seleccione Tipo de Consulta");
	  }
	  else{
        m_TipoCon = m_TipoConsulta.value;
	  	if(m_TipoCon=="01") m_TipoCon = "Carta de cr&eacute;dito de importaci&oacute;n";
        else if(m_TipoCon=="02") m_TipoCon = "Cobranza de importación";
        else if(m_TipoCon=="03") m_TipoCon = "Carta de cr&eacute;dito de exportaci&oacute;n";
        else if(m_TipoCon=="04") m_TipoCon = "Cobranza de exportaci&oacute;n";
        else if(m_TipoCon=="05") m_TipoCon = "Financiamiento de comercio exterior";
        else if(m_TipoCon=="06") m_TipoCon = "Transferencias al exterior";
        else if(m_TipoCon=="07") m_TipoCon = "Ordenes de pago recibidas del exterior";
		  
	  	var divmmm =  document.getElementById("cargar");
		divmmm.innerHTML =
	
		"<table cellpadding='2' cellspacing='2' border='0' align='center' width='100%'>" +
		  "<tr>" +
			"<td class='TitleRow3' colspan='3'>" + m_TipoCon + "</td>" +
		  "</tr>" +
		  "<tr class='TitleRow4'>" +
			"<td width='33%'>Tipo de Operaci&oacute;n</td>" +
			"<td width='33%'>Fecha</td>" +
			"<td width='33%'>Importe</td>" +
		  "</tr>" +
		  "<tr>" +
			"<td class='CellColRow7'>Operaci&oacute;n A</td>" +
			"<td class='CellColRow7'>12/04/2007</td>" +
			"<td class='CellColRow6'>32150.18</td>" +
		  "</tr>" +
		  "<tr>" +
			"<td class='CellColRow7'>Operaci&oacute;n B</td>" +
			"<td class='CellColRow7'>15/10/2008</td>" +
			"<td class='CellColRow6'>75.00</td>" +
		  "</tr>" +
		  "<tr>" +
			"<td class='CellColRow7'>Operaci&oacute;n Y</td>" +
			"<td class='CellColRow7'>31/10/2008</td>" +
			"<td class='CellColRow6'>596.05</td>" +
		  "</tr>" +
		  "<tr>" +
			"<td class='CellColRow7'>Operaci&oacute;n Z</td>" +
			"<td class='CellColRow7'>19/12/2008</td>" +
			"<td class='CellColRow6'>781.98</td>" +
		  "</tr>" +
		"</table>";
	  }
	  
	  
	  
	}
    -->
</script>

</head>
<body>
<logic:notEqual name="habil" value="1">
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr valign="baseline">
        <td align="center" class="TitleRowCierraSession" valign="baseline" height="100%">
            No tiene permiso para ingresar a la opción
        </td>
    </tr>
  </table>
</logic:notEqual>
<logic:equal name="habil" value="1">
  <table width="100%" CELLSPACING="0" CELLPADDING="4">
    <tr>
      <td valign="middle" align="left" class="Title">Consulta de Operaciones de Comercio Exterior</td>
    </tr>
  </table>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class="CellColRow">Empresa:</td>
      <td class="CellColRow2">
        <select id="m_Empresa" class='CellColRow2'>
          <option value="codEmp1" selected >EMPRESA 1</option>
          <option value="codEmp2" >EMPRESA 2</option>
        </select>
      </td>
      <td class="CellColRow">Tipo de Consulta:</td>
      <td class="CellColRow2">
        <select id="m_TipoConsulta" class='CellColRow2'>
          <option value="01" >Carta de cr&eacute;dito de importaci&oacute;n</option>
          <option value="02" >Cobranza de importación</option>
          <option value="03" >Carta de cr&eacute;dito de exportaci&oacute;n</option>
          <option value="04" >Cobranza de exportaci&oacute;n</option>
          <option value="05" >Financiamiento de comercio exterior</option>
          <option value="06" >Transferencias al exterior</option>
          <option value="07" >Ordenes de pago recibidas del exterior</option>
        </select>
      </td>
    </tr>

	<tr>
	  <td class='CellColRow'>Desde</td>
	  <td class='CellColRow2'>
		<input type='TEXT' id='m_IniFec' size='10' maxlength='10' class="CellColRow6" value="<%=fechaActual%>"/>
		<button id='btn_ini' name='btn_ini'><img src='img/cal.gif' /></button>
		<script type='text/javascript'>
		  Calendar.setup(
			{
			  inputField : "m_IniFec",
			  ifFormat : "%d/%m/%Y",
			  button : "btn_ini"
			}
		  );
		</script>
	  </td>
	  <td class='CellColRow'>Hasta</td>
	  <td class='CellColRow2'>
		<input type='TEXT' id='m_FinFec' size='10' maxlength='10' class="CellColRow6" value="<%=fechaActual%>"/>
		<button id='btn_fin' name='btn_fin'><img src='img/cal.gif' /></button>
		<script type='text/javascript'>
		  Calendar.setup(
			{
			  inputField : "m_FinFec",
			  ifFormat : "%d/%m/%Y",
			  button : "btn_fin"
			}
		  );
		</script>
	  </td>
	</tr>

	<tr align="right">
      <td colspan="4" class="CellColRow5">
        <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:buscar();"/>
      </td>

    </tr>
  </table>
  <div id="cargar"/>
</logic:equal>
</body>
</html>
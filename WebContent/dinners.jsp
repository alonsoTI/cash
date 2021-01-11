<%-- 
    Document   : dinners
    Created on : 05-ene-2009, 15:18:58
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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

	function cancelar(){

	}
    function aceptar(){
      /*
      var divmmm =  document.getElementById("cargar");
	  divmmm.innerHTML =
		"<table cellpadding='2' cellspacing='2' border='2' align='center' width='80%' bordercolor='#0066CC'>" +
		  "<tr>" +
			"<td class='TitleRow3' colspan='5'>La Transacci&oacute;n ha sido procesada correctamente</td>" +
		  "</tr>" +
		"</table>" +

		"<table cellpadding='2' cellspacing='2' border='0' align='center' width='80%'>" +
		  "<tr>" +
			"<td align='center'>" +
				"<img src='img/bt-volver.png' align='middle' onMouseOver=\"this.src='img/bt-volver2.png'\" onMouseOut=\"this.src='img/bt-volver.png'\" onClick='javascript:volver();'/>" +
			"</td>" +
		  "</tr>" +
		"</table>";
      */
      var frm = document.forms[0];
	  frm.action = "pagoServicio.do?do=procesarPagoTarjeta";
      frm.submit();
    }

	function volver(){
      location.href = "dinners.html";
    }

    function cambiaTarjeta(){
      var tipoTarjeta = document.getElementById("m_TipoTarjeta");
      /*
      01 - Dinners
      02 - Visa Clasica
      03 - Visa Oro
      */
      
      var m_Card1 = document.getElementById("m_Card1");
      var m_Card2 = document.getElementById("m_Card2");
      var m_Card3 = document.getElementById("m_Card3");
      var m_Card4 = document.getElementById("m_Card4");
      
      if(tipoTarjeta.value == "01"){
        //limpiamos los campos del numero de tarjeta
        m_Card1.value = "";
        m_Card2.value = "";
        m_Card3.value = "";
        m_Card4.value = "";

        m_Card2.size = "6";
        m_Card2.maxLength = "6";

        m_Card4.style.visibility = "hidden";
        //.
      }
      else{ //tipoTarjeta {02, 03}
        //limpiamos los campos del numero de tarjeta
        m_Card1.value = "";
        m_Card2.value = "";
        m_Card3.value = "";
        m_Card4.value = "";
        
        m_Card2.size = "4";
        m_Card2.maxLength = "4";

        m_Card4.style.visibility = "visible";
      }
    }
    -->
</script>

</head>
<body>
<html:form action="pagoServicio.do">
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      <td valign="middle" align="left" class="Title">Pago de Tarjetas de Cr&eacute;dito</td>
	</tr>
  </table>

  <%--table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
    <tr class="TitleRow5">
      <td>Fecha:</td>
      <td>&nbsp;&nbsp;&nbsp;</td>
      <td>Hora:</td>
    </tr>
  </table--%>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
    <tr class="CellColRow2">
      <td style="background-color: #336699; color: #FFFFFF" width="20%">Cuenta Origen:</td>
      <td width="80%">
	  	<select id="m_Cuenta" class="CellColRow2">
          <option value="codCue1" selected>CTE - 1</option>
          <option value="codCue2" >CTE - 2</option>
          <option value="codCue2" >CTE - 3</option>
          <option value="codCue2" >CTE - 4</option>
        </select>
	  </td>
    </tr>
    <tr>
      <td style="background-color: #336699; color: #FFFFFF">Importe a Pagar:</td>
      <td style="background-color: #FFFFE6">
	  	<select id="m_TipoMoneda" class="CellColRow2">
          <option value="Sol" selected >S/.</option>
		  <option value="Dol" selected >$</option>
        </select>
        <input type="TEXT" id="m_Importe" size="40" maxlength="40" class="CellColRow" align="bottom"/>&nbsp;(*)
	  </td>
    </tr>

	<tr>
      <td style="background-color: #336699; color: #FFFFFF">Tipo de Tarjeta:</td>
      <td class="CellColRow2">
	  	<select id="m_TipoTarjeta" class="CellColRow2" onchange="javascript:cambiaTarjeta();">
          <option value="01" >Dinners</option>
          <option value="02" >Visa Cl&aacute;sica</option>
          <option value="03" >Visa Oro</option>
        </select>&nbsp;(*)
  	  </td>
    </tr>

	<tr>
      <td style="background-color: #336699; color: #FFFFFF">N&uacute;mero de Tarjeta:</td>
      <td style="background-color: #FFFFE6">
	  	<input type="TEXT" id="m_Card1" size="4" maxlength="4" class="CellColRow" align="bottom"/>
        <input type="TEXT" id="m_Card2" size="6" maxlength="6" class="CellColRow" align="bottom"/>
        <input type="TEXT" id="m_Card3" size="4" maxlength="4" class="CellColRow" align="bottom"/>
        <input type="TEXT" id="m_Card4" size="4" maxlength="4" class="CellColRow" align="bottom" style="visibility:hidden">&nbsp;(*)
	  </td>
    </tr>

	<tr>
      <td style="background-color: #336699; color: #FFFFFF">Nombres y Apellidos en Tarjeta:</td>
      <td class="CellColRow2" >
	  	<input type="TEXT" id="m_Card1" size="48" maxlength="48" class="CellColRow" align="bottom"/>&nbsp;(*)
	  </td>
    </tr>
    
	<tr>
      <td class="CellColRow2" colspan="2">(*) Campos requeridos para realizar transacci&oacute;n</td>
    </tr>

	<tr><td  style="background-color: #FFFFE6" colspan="2">&nbsp;</td></tr>

	<tr align="right">
      <td colspan="2" class="CellColRow5">        
		<img src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:cancelar();"/>
        <img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:aceptar();"/>
      </td>

    </tr>
  </table>
  <br>
  <div id="cargar"/>
</html:form>
</body>
</html>
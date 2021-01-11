<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%--
<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="inicio.jsp"/>
</logic:empty>
--%>

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
      var frm = document.forms[0];
	  
	  var divmmm =  document.getElementById("cargar");
	  divmmm.innerHTML = 
		"<table cellpadding='2' cellspacing='2' border='0' align='center' width='100%'>" +
		  "<tr>" +
			"<td class='TitleRow3' colspan='5'>DETALLE DE SALDOS</td>" +
		  "</tr>" +
		  "<tr class='TitleRow4'>" +
			"<td>Cuenta</td>" +
			"<td>Moneda</td>" +
			"<td>Saldo Disponible</td>" +
			"<td>Saldo Retenido</td>" +
			"<td>Saldo Contable</td>" +
		  "</tr>" +
		  "<tr class='TitleRow4'>" +
			"<td class='CellColRow7'><a href='consultaSaldosMov3.html'>CTE-2934487355</a></td>" +
			"<td class='CellColRow7'>PEN</td>" +
			"<td class='CellColRow6'>32150.18</td>" +
			"<td class='CellColRow6'>2134.45</td>" +
			"<td class='CellColRow6'>30000.01</td>" +
		  "</tr>" +
		  "<tr class='TitleRow4'>" +
			"<td class='CellColRow7'><a href='consultaSaldosMov3.html'>AHO-3129303030</a></td>" +
			"<td class='CellColRow7'>PEN</td>" +
			"<td class='CellColRow6'>1578.77</td>" +
			"<td class='CellColRow6'>0.10</td>" +
			"<td class='CellColRow6'>1578.77</td>" +
		  "</tr>" +
		  "<tr class='TitleRow4'>" +
			"<td class='CellColRow7'><a href='consultaSaldosMov3.html'>AHO-3123543213</a></td>" +
			"<td class='CellColRow7'>USD</td>" +
			"<td class='CellColRow6'>2345.00</td>" +
			"<td class='CellColRow6'>1.00</td>" +
			"<td class='CellColRow6'>2345.00</td>" +
		  "</tr>" +
		"</table>";
    }
    -->
</script>

</head>
<body>
  <table width="100%" CELLSPACING="0" CELLPADDING="4">
    <tr>
      <td valign="middle" align="left" class="Title">Consulta de Saldos y Movimientos</td>
    </tr>
  </table>
  
  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class="CellColRow">Empresa:</td>
      <td class="CellColRow2">  
        <select id="m_Empresa" class="CellColRow">
          <option value="T" selected >TODAS</option>
          <option value="codEmp1" >EMPRESA 1</option>
          <option value="codEmp2" >EMPRESA 2</option>
        </select>
      </td>
      <td class="CellColRow">&nbsp;&nbsp;&nbsp;</td>
      <td class="CellColRow">Tipo de Informaci&oacute;n:</td>
      <td class="CellColRow2">
        <select id="m_TipoInformacion" class="CellColRow">
          <option value="codTod" >Todos</option>
          <option value="codCre" >Cargo</option>
          <option value="codDeb" >Abono</option>
        </select>
      </td>
    </tr>
	<tr align="right">
      <td colspan="5" class="CellColRow5">
        <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:buscar();"/>
      </td>
      
    </tr>
  </table>
  <br>
  <div id="cargar"/>
</body>
</html>
<%-- 
    Document   : exportarExcel
    Created on : 06-ene-2009, 11:12:52
    Author     : jwong
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

  
  <%
    //obtener las variables que se pasan
    String pagina = request.getParameter("pagina");
    if(pagina!=null && !"".equals(pagina)){
        //System.out.println("Falta pagina de retorno");
    }
  %>
  <script language="JavaScript">
    <!--
    function imprimir(){
	  window.print();
	}
	function exportar(){

	}

	function guardar(){

	}
	function descargar(){

	}
	function siguiente(){

	}
	function ultimo(){

	}
    function volver(){
	  location.href = "<%=pagina%>";
    }
    -->
</script>

</head>
<body>
  <table width="80%" CELLSPACING="0" CELLPADDING="4" class="Title" >
    <tr>
      <td valign="middle" align="left">Exportaci&oacute;n</td>
    </tr>
  </table>

  <table width="80%" cellpadding="2" cellspacing="2" border="0" align="center" >
  	<tr align="left">
	  <td class="CellColRowCenter" colspan="2" align="center">Formato Excel <img src='img/excel.png' align='middle' /></td>
	  <td class="CellColRowCenter" colspan="2" align="center">Formato Texto <img src='img/page.gif' align='middle' /></td>
    </tr>

    <tr align="left">
	  <td class="CellColRow">Guardar Archivo</td>
      <td class="CellColRow2"><img src='img/disk.png' align='middle' onClick='javascript:guardar();'/></td>
	  <td class="CellColRow">Guardar Archivo</td>
      <td class="CellColRow2"><img src='img/disk.png' align='middle' onClick='javascript:guardar();'/></td>
    </tr>

	<tr>
      <td class="CellColRow">Abrir Archivo</td>
      <td class="CellColRow2"><img src='img/page_go.png' align='middle' onClick='javascript:descargar();'/></td>
	  <td class="CellColRow">Abrir Archivo</td>
      <td class="CellColRow2"><img src='img/page_go.png' align='middle' onClick='javascript:descargar();'/></td>
    </tr>
	<tr>
		<td align='center' colspan="5">&nbsp;</td>
	</tr>
	<tr>
		<td align='center' colspan="5">
			<img src='img/bt-volver.png' align='middle' onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick='javascript:volver();'/>
		</td>
	</tr>
  </table>
</body>
</html>
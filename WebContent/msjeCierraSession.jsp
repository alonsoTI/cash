<%-- 
    Document   : msjeCierraSession
    Created on : 30-ene-2009, 12:09:18
    Author     : jwong
--%>

<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
  <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Expires" CONTENT="0">

  <link href="css/Styles.css" rel="stylesheet" type="text/css">
  <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
  </style>
  
  <script language="javascript">
      function cerrarSession(){
          parent.location = "login.do?do=cerrarSesion";
      }
      setTimeout(cerrarSession, 3000); //3 segundos
  </script>

  <title>Cash Financiero</title>
</head>
<body>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr valign="baseline">
        <td align="center" class="TitleRowCierraSession" valign="baseline" height="100%">
            Su sesi&oacute;n ha finalizado
        </td>
    </tr>
</table>
</body>
</html>
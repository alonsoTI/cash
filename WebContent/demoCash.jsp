<%--
    Document   : demoCash
    Created on : 25/02/2009, 05:31:15 PM
    Author     : jwong
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <link href="css/Styles.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="config/javascript.js"></script>
    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
    <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
    <META HTTP-EQUIV="Expires" CONTENT="0">

    <style type="text/css">
      body {
          background: url(img/fondo.gif) no-repeat fixed;
          background-position: right;
      }
    </style>
</head>

<logic:notEqual name="habil" value="1">
<body>
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr valign="baseline">
        <td align="center" class="TitleRowCierraSession" valign="baseline" height="100%">
             <bean:message key="errors.authorization"/>
        </td>
    </tr>
  </table>
</body>
</logic:notEqual>
<logic:equal name="habil" value="1">
<%--
<body style="margin-bottom:0px;margin-left:0px;margin-right:0px;margin-top:0px;">
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="760" height="485">
  <param name="movie" value="videos/animacion2.swf">
  <param name="quality" value="high">
  <embed src="videos/animacion2.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="760" height="485"></embed>
</object>
</body>
--%>

  	<frameset cols="110,*" framespacing="0" frameborder="NO" border="0" >
	  <frame src="izquierdademo.jsp" name="leftFrame" id="leftFrame" noresize scrolling="no" />
	  <frame src="blanco.jsp" name="principal" id="principal" noresize scrolling="auto"  />
	</frameset>

</logic:equal>
</html>

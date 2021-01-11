<%-- 
    Document   : derechademo
    Created on : 21/05/2009, 11:37:22 AM
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%
//obtenemos el video a cargar
String video = request.getParameter("video");
String ss = (String)session.getAttribute("rutaDemoFlash");
//System.out.println("" + ss + video);
%>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
  <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Expires" CONTENT="0">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">
</head>

<body>

<object id=Player classid=CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6>
<param name="url" value="<bean:write name='rutaDemoFlash' /><%=video%>">

<%--embed 	WIDTH="591" HEIGHT="474" CONTROLLER="FALSE"
		type="application/x-mplayer2" pluginspage="http://www.microsoft.com/Windows/MediaPlayer/"
        src="<bean:write name='rutaDemoFlash' /><%=video%>" /--%>

<embed 	WIDTH="100%" HEIGHT="100%" CONTROLLER="FALSE"
		type="application/x-mplayer2" pluginspage="http://www.microsoft.com/Windows/MediaPlayer/"
        src="<bean:write name='rutaDemoFlash' /><%=video%>" />
        
</object>

</body>
</html>
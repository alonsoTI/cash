<%-- 
    Document   : success
    Created on : Dec 17, 2008, 1:48:33 AM
    Author     : Elvis
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

        <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="0">

        <style type="text/css">
        body {
            background: url(img/fondo.gif) no-repeat fixed;
            background-position: right;
        }
        </style>

        <title>Success</title>
        <link href="css/Styles.css" rel="stylesheet" type="text/css">
        <script language="JavaScript" type="text/JavaScript">
            function back(){
                var frm = document.forms[0];
                var url = "<bean:write property='m_Back' name='success'/>";
                url = url.replace(/amp;/gi,'');
                //alert("url : " + url);
                frm.action = url;
                frm.submit();
            }
        </script>
    </head>
    <body>
        <html:form action="Orden.do" onsubmit="return false;" >
            <table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
                <tr>
                    <td valign="middle" align="left" ><bean:write name='success' property='m_Titulo'/></td>
                </tr>
                <tr>
                    <td valign="middle" align="left" ><br></td>
                </tr>
            </table>
            <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%' bordercolor='#0066CC'>
                <tr>
                    <td class="TitleRow" colspan="2"><bean:write name='success' property='m_Mensaje'/></td>
                </tr>

                <logic:iterate name="alsuccess" id="beansuccess" indexId="j">
                    <tr>
                        <td class="CellColRow2" width="30%"><bean:write name="beansuccess" property="m_Label"/></td>
                        <logic:notEmpty name="beansuccess" property="m_Mensaje">
                        <td class="CellColRow2"><bean:write name="beansuccess" property="m_Mensaje"/></td>
                        </logic:notEmpty>
                        <logic:notEmpty name="beansuccess" property="m_MensajeArea">
                            <td class="CellColRow2"><html:textarea name="beansuccess" property="m_MensajeArea" styleId="m_MensajeArea" rows="4" readonly="true" /></td>
                        </logic:notEmpty>
                    </tr>
                </logic:iterate>


                <tr><td><br></td></tr>
            </table>

          
            <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
                <tr>
                    <td align='center'><br></td>
                </tr>
                <tr>
                    <td align='center'>
                        <img src="img/bt-volver.png" align="middle" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:back();"/>
                    </td>
                </tr>
            </table>

        </html:form>
    </body>
</html>

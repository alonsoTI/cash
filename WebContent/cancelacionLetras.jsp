<%-- 
    Document   : cancelacionLetras
    Created on : 19/03/2009, 05:11:32 PM
    Author     : jmoreno
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ page contentType="text/html;charset=windows-1252" import = "java.util.*" %>
<%@ page contentType="text/html;charset=windows-1252" import = "com.hiper.cash.xml.bean.BeanLetra" %>
<%@ page contentType="text/html;charset=windows-1252" import = "com.hiper.cash.xml.bean.BeanAccount" %>
<%@ page contentType="text/html;charset=windows-1252" import = "com.hiper.cash.util.Util" %>
<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>
<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">
  <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
  </style>

  <script language="JavaScript">
    <!--
    function aprobar(){
	  var frm = document.forms[0];
      frm.action = "letras.do?do=cancelacionLetras";
      frm.submit();
	}
    -->
</script>
<% int nroDecimales = 2;%>
</head>
<body>
<html:form action="letras.do">
  <input type="hidden" name="m_RucEmpresa" id="m_RucEmpresa" value="<%= request.getAttribute("m_RucEmpresa") %>">
  
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      <td valign="middle" align="left" colspan="4" class="Title"><bean:message key="letras.title.cancelacion"/></td>
	</tr>

  </table>

  <logic:notEmpty name="listaccounts">
   <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center" >
      <tr>
	  <td class='CellColRow'>Cuenta de Cargo:</td>

	  <td class='CellColRow2'>
          
          <select id="m_cta_cargo" name="m_cta_cargo" class="CellColRow2" >
              <% ArrayList<BeanAccount> listaccounts = (ArrayList<BeanAccount>)request.getAttribute("listaccounts");
                    for(BeanAccount beanaccount : listaccounts){

             %>
              <option value="<%= beanaccount.getM_AccountCode() %>&<%= beanaccount.getM_AccountType() %>"><%= beanaccount.getM_AccountDescription()+" "+beanaccount.getM_AccountCode()+" - Saldo:"+beanaccount.getM_Currency()+beanaccount.getM_AvailableBalance() %></option>
              <%
                }
              %>
			</select>
	  </td>	  
    </tr>
    <tr align="right">
      <td colspan="4" class="CellColRow5">
        <img src="img/bt-volver.png" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:history.back();"/>
        <img src="img/bt-aceptar.png" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:aprobar();"/>
      </td>
    </tr>
  </table>
  

      <logic:notEmpty name="listaLetras">
        <%
            ArrayList<BeanLetra> listaLetras = (ArrayList<BeanLetra>) session.getAttribute("listaLetras");
            for(BeanLetra beanLetra : listaLetras){
                if("si".equalsIgnoreCase(beanLetra.getM_respuesta())){

        %>
        <table cellpadding="2" cellspacing="2" border="0" align="center" >
        <tr >
          <td nowrap class='TitleRow7'>N&uacute;mero de Letra:</td>
          <td width="35%" class='TitleRow5'><%= beanLetra.getM_NumLetra() %></td>
          <td nowrap class='TitleRow7'>Vencimiento:</td>
          <td width="35%" class='TitleRow5'><%= beanLetra.getM_FechaVenc() %></td>
        </tr>

        <tr >
          <td nowrap class='TitleRow7'>Moneda:</td>
          <td width="35%" class='TitleRow5'><%= beanLetra.getM_Moneda() %></td>
          <td nowrap class='TitleRow7'>&nbsp;</td>
          <td width="35%" class='TitleRow5'>&nbsp;</td>
          </tr>
        <tr>
        
        <tr>
          <td nowrap class='CellColRowUser'>Principal:</td>
          <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_Principal() , nroDecimales) %></td>
            <td nowrap class='CellColRowUser'>Pago Principal:</td>
            <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_PagoPrincipal(), nroDecimales) %> </td>
          </tr>
        <tr>
          <td nowrap class='CellColRowUser'>Inter&eacute;s:</td>
          <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_Interes(), nroDecimales) %></td>
          <td nowrap class='CellColRowUser'>Portes:</td>
          <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_Portes(), nroDecimales) %></td>
          </tr>
        <tr>
          <td nowrap class='CellColRowUser'>Principal Pendiente:</td>
          <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_PrincipalPend(), nroDecimales) %> </td>
          <td nowrap class='CellColRowUser'>Mora:</td>
          <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_Mora(), nroDecimales) %></td>
          </tr>
        <tr>
          <td nowrap class='CellColRowUser'>Inter&eacute;s Pendiente:</td>
          <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_InteresPend(), nroDecimales) %></td>
          <td nowrap class='CellColRowUser'>Protesto:</td>
          <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_Protesto(), nroDecimales) %></td>
          </tr>
        <tr>
          <td nowrap class='CellColRowUser'>Pago de Inter&eacute;s:</td>
          <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_PagoInteres(), nroDecimales) %></td>
          <td nowrap class='CellColRowUser'>ITF:</td>
          <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_Itf(), nroDecimales) %></td>
          </tr>
        <tr>
          <td nowrap class='CellColRowUser'>Inter&eacute;s del Refinan:</td>
          <td width="35%" class='CellColRow2'><%= beanLetra.getM_InteresRef() %></td>
          <td nowrap class='CellColRowUser'>Total a Pagar:</td>
          <td width="35%" class='CellColRow2'><%= Util.formatearMontoConComa(beanLetra.getM_ImporteFinal(), nroDecimales)%></td>
          </tr>

      </table>
      <br>
       <%
            }else{%>
       <table cellpadding="2" cellspacing="2" border="0" align="center" >
          <tr>
            <td nowrap class='TitleRow7'  >N&uacute;mero de Letra:</td>
            <td colspan="3" class='TitleRow5' ><%= beanLetra.getM_NumLetra() %></td>
          </tr>
          <tr >

            <td colspan="4" class='CellColRow2' ><%= beanLetra.getM_mensaje() %></td>
          </tr>
        </table>
       <%
            }
        }
       %>
      </logic:notEmpty>
  </logic:notEmpty>
  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
    </tr>
  </table>
  </logic:notEmpty>

</html:form>
</body>
</html>

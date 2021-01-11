<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.financiero.cash.action.*, java.util.List, com.hiper.cash.domain.TmEmpresa"   %> 

<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%

 	List<TmEmpresa> empresas = (List<TmEmpresa>)request.getAttribute("listaEmpresas"); 

 	
%>
	

<script language="javascript">

function Inicio(val){
	parent.location = "login.do?do=menuEmpresa&cem="+val;
}

function ir(){
	
	sise =  <%=empresas.size() %>
	
	val = 'login.do?do=menuEmpresa&cem='+'<%= empresas.get(0).getCemIdEmpresa() %>';
	
	if(sise == 1){
		parent.location = val ;	
	}
	//alert(val);
}

</script>

  <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
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
<body onload="javascript:ir()" >

<table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
        <tr>
            <td valign="middle" align="left">&nbsp;&nbsp;Empresas Afiliadas</td> 
            <!--  <bean:message key="pagos.title.ingresar"/>   <td  valign="middle" align="right"><img src="img\printer.png" align="middle" onClick="javascript:imprimir();"/></td>-->
        </tr>
</table>

<p>
</p>

<logic:notEmpty name="listaEmpresas">
<ul>
<table width="100%" cellpadding="2" cellspacing="2" border="0" >
	
	<logic:iterate id="lista" scope="request" name="listaEmpresas"  >
		<tr>
			<td class="CellColRow5" align="left" width="20%">
				
				<li>
					<a href ="javascript:Inicio('${lista.cemIdEmpresa}');" >
						<bean:write name="lista" property="demNombre" />   
						
					</a>
				</li>
			</td>
		</tr>
	</logic:iterate>
   
</table>
	</ul>
</logic:notEmpty> 

</body>
</html>
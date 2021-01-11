<%-- 
    Document   : preliqRenovLetras
    Created on : 18/03/2009, 04:37:50 PM
    Author     : jmoreno
--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ page contentType="text/html;charset=windows-1252" import = "com.hiper.cash.util.*" %>
<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>
<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">
  <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>
  <script type="text/javascript" src="js/livevalidation_standalone.js"></script>
  <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
  </style>

  <script language="JavaScript">
    
	function imprimir(){
	  window.print();
	}    
    function preLiqRenovacion(){
        
        var frm=document.forms[0];
        var nvoMonto = frm.m_importe.value;
        var nvoFecha =frm.m_IniFec.value;
        if(nvoMonto==""){
            alert("Ingrese el nuevo Monto");
        }else if(nvoFecha==""){
            alert("Ingrese una nueva Fecha");
        }else{//jmoreno 09-10-09 Validacion de cant. max. de enteros para el nvo monto
            var  indic = nvoMonto.indexOf(".", 0);
            if(indic > -1){
                var cad = nvoMonto.substring(0,indic);
                if(cad.length > 12){
                   alert("El nuevo monto puede tener máximo 12 enteros");
                   frm.m_importe.select();
                   frm.m_importe.focus();
                   return false;
                }
            }else{
               if(nvoMonto.length > 12){
                   alert("El nuevo monto puede tener máximo 12 enteros");
                   frm.m_importe.select();
                   frm.m_importe.focus();
                   return false;
               }
           }
            var url="letras.do?do=consultaPreLiqLetxRen&"+"<%=(String)request.getAttribute("datos")%>"+"&monto_abonar="+nvoMonto+"&nuevo_fecha_venc="+nvoFecha+"&ruc_empresa="+document.forms[0].ruc_empresa.value;
            consultarWS(url);
        }
        
    }
    function consultarWS(url){
          if (window.XMLHttpRequest) { // Non-IE browsers
            req = new XMLHttpRequest();
            req.onreadystatechange = processExiste;
            try {req.open("GET", url, true);}
            catch (e) {alert(e);}
            req.send(null);
          } else if (window.ActiveXObject) { // IE
            req = new ActiveXObject("Microsoft.XMLHTTP");
            if (req) {
              req.onreadystatechange = processExiste;
              req.open("GET", url, true);
              req.send();

            }
          }

        }
     function processExiste(){

      if (req.readyState==4){
        if (req.status==200) parseExist(); //el requerimiento ha sido satisfactorio
        else alert("Ocurrió un error al procesar los datos");
      }
    }
    function parseExist(){
      var response = req.responseXML;      
      if(response==null) alert("Ocurrió un error al procesar los datos");
      var respuesta = response.getElementsByTagName("respuesta");      
      var isOk = respuesta[0].getAttribute("valor");      
      if(isOk=="Ok"){
          //document.getElementById("principal").innerHTML=respuesta[19].getAttribute("valor");
          //document.getElementById("interes").innerHTML=respuesta[7].getAttribute("valor");
          document.getElementById("pago_principal").innerHTML=respuesta[17].getAttribute("valor");
          document.getElementById("pago_interes").innerHTML=respuesta[16].getAttribute("valor");
          document.getElementById("portes").innerHTML=respuesta[18].getAttribute("valor");
          document.getElementById("protesto").innerHTML=respuesta[21].getAttribute("valor");
          document.getElementById("prin_pend").innerHTML=respuesta[20].getAttribute("valor");
          document.getElementById("interes_refin").innerHTML=respuesta[9].getAttribute("valor");
          document.getElementById('label_importe_total').style.visibility='visible';
          document.getElementById("importe_total").innerHTML=respuesta[6].getAttribute("valor");
          document.getElementById('label_importe_itf').style.visibility='visible';
          document.getElementById("importe_itf").innerHTML=respuesta[10].getAttribute("valor");
          document.getElementById("mora").innerHTML=respuesta[12].getAttribute("valor");
          document.getElementById("amortizacion").innerHTML=respuesta[25].getAttribute("valor");
          document.getElementById('mensaje').style.visibility='hidden';
      }else{
          document.getElementById("pago_interes").innerHTML="";
          document.getElementById("portes").innerHTML="";
          document.getElementById("protesto").innerHTML="";
          document.getElementById("prin_pend").innerHTML="";
          document.getElementById("interes_refin").innerHTML="";
          document.getElementById('label_importe_total').style.visibility='hidden';
          document.getElementById("importe_total").innerHTML="";
          document.getElementById('label_importe_itf').style.visibility='hidden';
          document.getElementById("importe_itf").innerHTML="";
          document.getElementById("mora").innerHTML="";
          document.getElementById("amortizacion").innerHTML="";
      }
      /*if(isOk == null){
          document.getElementById('mensaje').style.visibility='visible';
      }else{
          if(isOk ==""){
             document.getElementById('mensaje').style.visibility='visible';
          }
      }*/
      if(isOk!=null && isOk!=""){
          if(isOk == "Ok"){
               document.getElementById('mensaje').innerHTML="PROCESO EXITOSO";
          }else{
              document.getElementById('mensaje').innerHTML=isOk;
          }
         
          document.getElementById('mensaje').style.visibility='visible';
      }
      else{          
          document.getElementById('mensaje').style.visibility='visible';
      }
    }
    
</script>

</head>
<body>
<html:form action="letras.do">
    
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      <td valign="middle" align="left" colspan="4" class="Title"><bean:message key="letras.title.preliq.renovacion"/></td>
	</tr>    
  </table>
  <br>
  <table width="60%" cellpadding="2" cellspacing="2" border="0" align="center" >
  	<tr>
		<td class='CellColRowLeftLetra' width="20%">N&uacute;mero de Letra:</td>
		<td class='CellColRow2' width="30%"><%=request.getAttribute("num_letra")%></td>
	</tr>
    <!--jwong 20/04/2009 se agrega la moneda-->
    <tr>
		<td class='CellColRowLeftLetra' width="20%">Moneda:</td>
		<td class='CellColRow2' width="30%"><%=request.getAttribute("moneda_letra")%></td>
	</tr>
    <tr>
		<td class='CellColRowLeftLetra' width="20%">Monto Letra Anterior:</td>
		<td class='CellColRow2' width="30%"><div id="pago_principal"></div></td>
	</tr>
	<tr>
		<td class='CellColRowLeftLetra' width="20%">Importe Original:</td>
        <td class='CellColRow2' width="30%"><%= Util.formatearMontoConComa((String)request.getAttribute("importe_actual"),2) %></td>
	</tr>
	<tr>
		<td class='CellColRowLeftLetra' width="20%">Fecha de Vencimiento:</td>
		<td class='CellColRow2' width="30%"><%=request.getAttribute("fecha_venc")%></td>
	</tr>
    <tr>
	  <td width="20%" class='CellColRowLeftLetra'>Importe Renovaci&oacute;n:</td>
	  <td width="30%" class='CellColRow2'><input type="text" id="m_importe" class="CellColRow" maxlength="15" size="15" onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)" /></td>
    </tr>
	<tr>
	  <td width="20%" class='CellColRowLeftLetra'>Nueva Fecha de Vencimiento:</td>
	  <td width="30%" class='CellColRow2'>
	  	<div id="fecha_venc" >
			<input type='text' name='m_IniFec' id='m_IniFec' size='10' maxlength='10' class="CellColRow6" readonly/>
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
	  	</div>
	  </td>
    </tr>
  	<!--tr>
		<td class='CellColRowLeftLetra' width="20%">Principal:</td>
		<td class='CellColRow2' width="30%"><div id="principal"></div></td>
	</tr-->
    <tr>
		<td class='CellColRowLeftLetra' width="20%">Amortizaci&oacute;n:</td>
		<td class='CellColRow2' width="30%"><div id="amortizacion"></div></td>
	</tr>
	<!--tr>
		<td class='CellColRowLeftLetra' width="20%">Inter&eacute;s:</td>
		<td class='CellColRow2' width="30%"><div id="interes"></div></td>
	</tr-->	
	<tr>
		<td class='CellColRowLeftLetra' width="20%">Pago Inter&eacute;s:</td>
		<td class='CellColRow2' width="30%"><div id="pago_interes"></div></td>
	</tr>
  	<tr>
		<td class='CellColRowLeftLetra' width="20%">Portes:</td>
		<td class='CellColRow2' width="30%"><div id="portes"></div></td>
	</tr>
	<tr>
		<td width="20%" class='CellColRowLeftLetra'>Protesto:</td>
		<td width="30%" class='CellColRow2'><div id="protesto"></div></td>
	</tr>
	<tr>
		<td width="20%" class='CellColRowLeftLetra'>Mora:</td>
		<td width="30%" class='CellColRow2'><div id="mora"></div></td>
	</tr>	
	<tr>
		<td width="20%" class='CellColRowLeftLetra'>Comis. Renovaci&oacute;n:</td>
		<td width="30%" class='CellColRow2'><div id="prin_pend"></div></td>
	</tr>
	<tr>
	  <td width="20%" class='CellColRowLeftLetra'>Inter&eacute;s de Refinanciamiento:</td>
	  <td width="30%" class='CellColRow2'><div id="interes_refin"></div>
	  </td>
    </tr>
    <tr>
	  <td width="20%" class='CellColRowLeftLetra'><div id="label_importe_total" style="visibility:hidden">Importe Total de Renovaci&oacute;n:</div></td>
	  <td width="30%" class='CellColRow2'>
	  	<div id="importe_total"></div>
	  </td>
    </tr>
    <tr>
	  <td width="20%" class='CellColRowLeftLetra'><div id="label_importe_itf" style="visibility:hidden">ITF:</div></td>
	  <td width="30%" class='CellColRow2'>
	  	<div id="importe_itf"></div>
	  </td>
    </tr>	
    <tr>
	  <td colspan="2"><div class="CellColRow7" id="mensaje" style="visibility:hidden">No se encontraron resultados</div></td>
    </tr>
	<tr align="right">
      <td colspan="2" class="CellColRow5">
        <img src="img/bt-volver.png" align="middle" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:history.back();"/>
        <img src="img/bt-preliquidar.png" align="middle" onMouseOver="this.src='img/bt-preliquidar2.png'" onMouseOut="this.src='img/bt-preliquidar.png'" onClick="javascript:preLiqRenovacion();"/>		
      </td>
    </tr>
</table>
<input type="hidden" name="ruc_empresa" value="<%= (String)request.getAttribute("ruc_empresa") %>">
</html:form>
</body>
</html>

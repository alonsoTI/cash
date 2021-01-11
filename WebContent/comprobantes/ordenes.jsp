<%-- 
    Document   : consultaOrdenes
    Created on : 07-dic-2008, 12:35:14
    Author     : jwong, esilva
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<logic:empty name="usuarioActual" scope="session">
	<logic:redirect href="cierraSession.jsp" />
</logic:empty>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<link href="css/Styles.css" rel="stylesheet" type="text/css">
<link href="css/cash.css" rel="stylesheet" type="text/css">

<style type="text/css">
@import url(calendario/calendar-system.css);

.Estilo2 {
	font-size: 10pt
}

.Estilo3 {
	font-size: 12pt
}
</style>

<script LANGUAGE="javascript" SRC="js/Functions.js"></script>
<script type="text/javascript" src="config/javascript.js"></script>
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
<logic:equal name="habil" value="1">
	<script language="JavaScript">
      
     function obtenerDescripcion(idOrden){         
         var url = "comprobantes.do?do=obtenerCodError&idOrden="+idOrden;
         mostrarDetalle(url);
     }
     
     function mostrarDetalle(url){  
      if (window.XMLHttpRequest) { // Non-IE browsers
        req = new XMLHttpRequest();
        req.onreadystatechange = processMostrar;
        try {req.open("GET", url, true);}
        catch (e) {alert(e);}
        req.send(null);
      } else if (window.ActiveXObject) { // IE
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (req) {
          req.onreadystatechange = processMostrar;
          req.open("GET", url, true);
          req.send();
        }
      }
    }
    function processMostrar(){
      if (req.readyState==4){
        if (req.status==200) parseMostrar(); //el requerimiento ha sido satisfactorio
        else alert("Ocurrió un error al procesar los datos");
      }
    }
    function parseMostrar(){        
        var response = req.responseXML;
        if(response==null) alert("Ocurrió un error al procesar los datos");        
        var respuesta = response.getElementsByTagName("respuesta");
        var valor = respuesta[0].getAttribute("valor");
        if(valor!=null){
            alert(valor);
        }        
    } 
 
      function buscar(){    	 
          
            var fecIni = document.getElementById("m_FecInicio").value;
            var fecFin = document.getElementById("m_FecFin").value;
            if(!validaRangoFechasComprobantes(fecIni,fecFin)){
              	return;
            }
            var referencia = document.getElementById("m_Referencia").value;
            var l = referencia.length; 
            if(  l != 0 ){
            	if( l<4){
                	alert("La longitud minima de la referencia es de 4 caracteres");
                	return false;
            	}                	    
            }
            var fecActual = '<bean:write name="fechaActualComp"/>';
            var fecHace1Anho = '<bean:write name="fechaHace1Anho"/>';
            if(validarFechasRelacionadas(fecIni, fecFin, fecActual)){
                //if(validarFechaMenorConReferencia(fecIni,fecHace1Anho,"Fecha desde no puede ser menor a un año atrás")){
                    var frm = document.forms[0];
                    frm.action = "comprobantes.do?do=buscarOrdenesLinea";
                    frm.submit();
                //}
            }
        }
        function updateDate(cal) {
            var field_0 = document.getElementById("m_FecFin").value;
            var fecFin = field_0.substr(6, 4) + field_0.substr(3, 2) + field_0.substr(0, 2);
            var date = cal.date;
            var fecSel = date.print("%Y%m%d");
            if (  fecSel > fecFin ) {
                var field = document.getElementById("m_FecInicio");
                field.value = field_0.substr(0, 2)+'/' +field_0.substr(3, 2)+'/' + field_0.substr(6, 4);
            }
        }
        function updateDateRef(cal) {
            var field_0 = document.getElementById("m_FecInicio").value;
            var fecIni = field_0.substr(6, 4) + field_0.substr(3, 2) + field_0.substr(0, 2);
            var date = cal.date;
            var fecSel = date.print("%Y%m%d");
            if (  fecSel < fecIni ) {
                var field = document.getElementById("m_FecFin");
                field.value = field_0.substr(0, 2)+'/' +field_0.substr(3, 2)+'/' + field_0.substr(6, 4);
            }
        }

        function mostrarAprob(idorden,idServicio){
    		var url ="comprobantes.do?do=mostrarAprobadores&codOrden="+idorden+"&servemp="+idServicio;
    		var ventana = window.open(url,"Aprobaciones","toolbar=no, location=no, scrollbars=yes, resizable=no, directories=no, copyHistory=no, menubar=no,status=no,width=600px, height=350px");
    		//ventana.focus();
        	
  		  
	    }        
        
</script>
</logic:equal>
</head>
<body>
<html:form action="comprobantes.do">
	<logic:notEqual name="habil" value="1">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr valign="baseline">
				<td align="center" class="TitleRowCierraSession" valign="baseline"
					height="100%"><bean:message key="errors.authorization" /></td>
			</tr>
		</table>
	</logic:notEqual>
	<logic:equal name="habil" value="1">
		<table width="100%" CELLSPACING="0" CELLPADDING="4" border="0">
			<tr>
				<td valign="middle" align="left" class="Title"><bean:message
					key="comprobantes.title.ordenes" /></td>
			</tr>
		</table>

		<table width="100%" cellpadding="2" cellspacing="2" border="0"
			align="center">
			<tr>
				<td class='CellColRow'>Empresa</td>
				<td class='CellColRow2'><html:select property="m_Empresa"
					styleId="m_Empresa" styleClass="CellColRow"
					onchange="javascript:DoSubmit('comprobantes.do?do=cargarServicios&tipo=O');"
					style="width:260px;">
					<logic:notEmpty name="listaEmpresas">
						<html:options collection="listaEmpresas" property="cemIdEmpresa"
							labelProperty="demNombre" />
					</logic:notEmpty>
				</html:select></td>

				<td class='CellColRow'>Servicio</td>
				<td class='CellColRow2'><html:select property="m_Servicio"
					styleId="m_Servicio" styleClass="CellColRow" style="width:180px;">
					<html:option value="00">
						<bean:message key="global.todos.servicios" />
					</html:option>
					<logic:notEmpty name="listaServicios">
						<html:options collection="listaServicios" property="m_IdServicio"
							labelProperty="m_Descripcion" />
					</logic:notEmpty>
				</html:select></td>
			</tr>

			<tr>
				<td class='CellColRow'>Rango</td>
				<td class='CellColRow2'><html:text property="m_FecInicio"
					styleId="m_FecInicio" size="10" maxlength="10"
					styleClass="CellColRow" readonly="true" />
				<button id='btn_ini' name='btn_ini'><img src='img/cal.gif' /></button>
				<script type='text/javascript'>
		  Calendar.setup(
			{
			  inputField : "m_FecInicio"
			  ,ifFormat : "%d/%m/%Y"
			  ,button : "btn_ini"
              ,weekNumbers : false // Numero de semanas
              ,onUpdate : updateDate
			}
		  );
		</script> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <html:text
					property="m_FecFin" styleId="m_FecFin" size="10" maxlength="10"
					styleClass="CellColRow" readonly="true" />
				<button id='btn_fin' name='btn_fin'><img src='img/cal.gif' /></button>
				<script type='text/javascript'>
		  Calendar.setup(
			{
			  inputField : "m_FecFin"
			  ,ifFormat : "%d/%m/%Y"
			  ,button : "btn_fin"
              ,weekNumbers : false // Numero de semanas
              ,onUpdate : updateDateRef
			}
		  );
		</script></td>
				<td class='CellColRow'>Estado</td>
				<td class='CellColRow2'><html:select property="m_Estado"
					styleId="m_Estado" styleClass="CellColRow" style="width:180px;">
					<html:option value="">
						<bean:message key="global.todos" />
					</html:option>
					<logic:notEmpty name="listaEstadosOrden">
						<html:options collection="listaEstadosOrden" property="id.clfCode"
							labelProperty="dlfDescription" />
					</logic:notEmpty>
				</html:select></td>
			</tr>
			<tr>
				<td class='CellColRow'>Referencia</td>
				<td class='CellColRow2'><html:text property="m_ConsReferencia"
					styleId="m_Referencia" size="45" maxlength="50"
					styleClass="CellColRow2"
					onkeypress="return soloDescripcion(this, event)"
					onblur="gDescripcion(this)" /></td>
				<td class='CellColRow'>Items</td>
				<td class='CellColRow2'><html:radio
					property="tipoProcesamiento" value="P">Procesado</html:radio> <html:radio
					property="tipoProcesamiento" value="N">No Procesado</html:radio></td>
			</tr>
			<tr align="right">
				<td colspan="4" class="CellColRow5"><img
					src="img/bt-buscar.png" align="middle"
					onClick="javascript:buscar();"
					onMouseOver="this.src='img/bt-buscarB.png'"
					onMouseOut="this.src='img/bt-buscar.png'" /></td>
			</tr>
		</table>

		<logic:notEmpty name="ordenes">
			<table width="100%" cellpadding="2" cellspacing="2" border="0">
				<tr>
					<td class="paginado" align="left" valign="middle"><logic:equal
						value="true" name="paginado" property="existeAnterior">
						<a
							href="comprobantes.do?do=paginarOrdenes&nroPagina=<bean:write name="paginado" property="paginaInicio"/>">
						<img src='img/paginacion/ico_inicio.png' /></a>
						<a
							href="comprobantes.do?do=paginarOrdenes&nroPagina=<bean:write name="paginado" property="paginaAnterior"/>">
						<img src='img/paginacion/ico_anterior.png' /></a>
					</logic:equal> <logic:notEqual value="true" name="paginado"
						property="existeAnterior">
						<img src='img/paginacion/ico_inicio_dis.png' />
						<img src='img/paginacion/ico_anterior_dis.png' />
					</logic:notEqual> <label> <bean:write name="paginado" property="encabezado" />
					</label> <logic:equal value="true" name="paginado"
						property="existeSiguiente">
						<a
							href="comprobantes.do?do=paginarOrdenes&nroPagina=<bean:write name="paginado" property="paginaSiguiente"/>">
						<img src='img/paginacion/ico_siguiente.png' /></a>
						<a
							href="comprobantes.do?do=paginarOrdenes&nroPagina=<bean:write name="paginado" property="paginaFinal"/>">
						<img src='img/paginacion/ico_final.png' /></a>
					</logic:equal> <logic:notEqual value="true" name="paginado"
						property="existeSiguiente">
						<img src='img/paginacion/ico_siguiente_dis.png' />
						<img src='img/paginacion/ico_final_dis.png' />
					</logic:notEqual></td>
					<td align="right" class="exportacion" valign="middle"><a
						href="comprobantes.do?do=exportarOrdenes&accion=1"> <img
						src='img/exp/ico_excel.png' alt="Exportar Excel" /></a> <a
						href="comprobantes.do?do=exportarOrdenes&accion=2"> <img
						src='img/exp/ico_text.png' alt="Exportar Texto" /></a> <a
						target="_blank" href="comprobantes.do?do=exportarOrdenes&accion=3">
					<img src='img/exp/ico_html.png' alt="Exportar HTML" /></a> <a
						target="_blank" href="comprobantes.do?do=exportarOrdenes&accion=0">
					<img src='img/exp/ico_printer.png' alt="Impresion" /></a></td>
				</tr>
			</table>
			<table id="hor-zebra" style="font-size: 10px;">
				<tr>
					<th>Orden</th>
					<th>Servicio</th>
					<th>Cuenta</th>
					<th>Referencia</th>
					<th>Inicio</th>
					<th>Vence</th>
					<th>Estado</th>
					<th align="center">Nro</th>
					<th align="center">S/.</th>
					<th align="center">$</th>
					<th></th>
					<th></th>
				</tr>
				<logic:iterate id="orden" name="ordenes" indexId="row">
					<bean:define id="mod"
						value="<%= String.valueOf(row.intValue() % 2 )%>" />
					<logic:equal value="0" name="mod">
						<tr>
					</logic:equal>
					<logic:notEqual value="0" name="mod">
						<tr class="odd">
					</logic:notEqual>
					<td><layout:link
						href="comprobantes.do?do=iniciarDetallesOrden" name="orden"
						property="parametrosIniciarDetallesOrden">
						<layout:write name="orden" property="m_IdOrden" />
					</layout:link></td>
					<td style="font-size: 9px;"><bean:write name="orden"
						property="m_Servicio" /></td>
					<td><bean:write name="orden" property="m_CuentaCargo" /></td>
					<td><bean:write name="orden" property="m_Referencia" /></td>
					<td><bean:write name="orden" property="m_FecInicio" /></td>
					<td><bean:write name="orden" property="m_FecVenc" /></td>
					<td><logic:equal name="orden" property="m_TipoServicio"
						value="0">
						<bean:write name="orden" property="m_DescripEstado" />
					</logic:equal> <logic:equal name="orden" property="m_TipoServicio" value="1">
						<bean:define name="orden" property="m_IdOrden" id="idorden"
							type="java.lang.String" />
						<%
							String metodo = "javascript:obtenerDescripcion("
														+ idorden + ");";
						%>
						<layout:link href="<%= metodo%>">
							<bean:write name="orden" property="m_DescripEstado" />
						</layout:link>
					</logic:equal></td>
					<td align="center"><bean:write name="orden" property="m_Items" /></td>
					<td align="center"><bean:write name="orden"
						property="m_ValorSoles" /></td>
					<td align="center"><bean:write name="orden"
						property="m_ValorDolares" /></td>
					<td><layout:link
						href="comprobantes.do?do=detalleImporteOrdenes" name="orden"
						property="paramDetImportEstadoUrl" title="Detalle de Montos">
						<img src='img/ico_ver.png' height="20" width="20" />
					</layout:link></td>
					<td>
						<bean:define name="orden" property="m_IdOrden" id="idorden" type="java.lang.String" />
						<bean:define name="orden" property="m_IdServicio" id="idServicio" type="java.lang.String" />
<% 	String metodo2 = "javascript:mostrarAprob("+ idorden +","+idServicio+");"; %>
					 <layout:link href="<%= metodo2%>" title="Aprobadores">
						<img src='img/ico_aprobar.png' height="20" width="20" />
					</layout:link></td>
					</tr>
				</logic:iterate>
			</table>

		</logic:notEmpty>
		<logic:empty name="ordenes">
			<table cellpadding='2' cellspacing='2' border='0' align='center'
				width='90%'>
				<tr>
					<td class='TitleRow3'><bean:message key="global.listavacia" /></td>
				</tr>
			</table>
		</logic:empty>

	</logic:equal>
</html:form>
</body>
</html>
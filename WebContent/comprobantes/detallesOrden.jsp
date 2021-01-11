<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>

<logic:empty name="usuarioActual" scope="session">
	<logic:redirect href="cierraSession.jsp" />
</logic:empty>
<html>
<head>
<title>Cash Financiero - Comprobantes - Consulta Ordenes en
Linea - Detalle</title>
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
<script type="text/javascript" src="calendario/calendar.js"></script>
<script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
<script type="text/javascript" src="calendario/calendar-setup.js"></script>
<script type="text/javascript" src="js/Functions.js"></script>
<script type="text/javascript" src="config/javascript.js"></script>

<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="0">



<script language="JavaScript">
	function volver(){
      location.href = "<bean:write name='back'/>";
    }

	function exportar2(url){	    
	        var frm = document.forms[0];
	        frm.action = url ;
	        frm.submit();	   
	}

	function mostrar(){
        /*no borrar este metodo*/
    }
</script>

</head>
<body>
<html:form action="comprobantes.do">

	<h1>Detalles de Orden</h1>
	<table id="formulario">
		<colgroup>
			<col class="label" />
			<col class="input-read" />
			<col class="label" />
			<col class="input-read" />
		</colgroup>
		<tr>
			<logic:notEmpty name="cDOIdOrden">
			<td>Orden</td>
			<td><bean:write name="cDOIdOrden" /></td>
			</logic:notEmpty>
			<logic:notEmpty name="tipoProcesamiento">
			<td>Items</td>
			<td><bean:write name="tipoProcesamiento" /></td>
			</logic:notEmpty>
		</tr>
	</table>
		
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>
				<td valign="middle" align="left" class="Title"></td>
				<td align="right" class="exportacion" valign="middle"><img
					onclick="javascript:exportar2('comprobantes.do?do=exportarDetallesOrden&accion=1')"
					src='img/exp/ico_excel.png' alt="Exportar Excel" /> <img
					onclick="javascript:exportar2('comprobantes.do?do=exportarDetallesOrden&accion=2')"
					src='img/text.png' alt="Exportar Texto" /> <a target="_blank"
					href="comprobantes.do?do=exportarDetallesOrden&accion=3"> <img
					src='img/exp/ico_html.png' alt="Exportar HTML" /></a> <a
					target="_blank"
					href="comprobantes.do?do=exportarDetallesOrden&accion=0"> <img
					src='img/exp/ico_printer.png' alt="Impresion" /></a></td>
			</tr>
			<tr>
				<td align="left" valign="middle"><img src="img/bt-volver.png"
					width="71" height="27" align="middle"
					onClick="javascript:volver();"
					onMouseOver="this.src='img/bt-volver2.png'"
					onMouseOut="this.src='img/bt-volver.png'" /></td>
				<td class="paginado" align="right" valign="middle"><logic:equal
					value="true" name="paginado" property="existeAnterior">
					<a
						href="comprobantes.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaInicio"/>">
					<img src='img/paginacion/ico_inicio.png' /></a>
					<a
						href="comprobantes.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaAnterior"/>">
					<img src='img/paginacion/ico_anterior.png' /></a>
				</logic:equal> <logic:notEqual value="true" name="paginado"
					property="existeAnterior">
					<img src='img/paginacion/ico_inicio_dis.png' />
					<img src='img/paginacion/ico_anterior_dis.png' />
				</logic:notEqual> <label> <bean:write name="paginado" property="encabezado" />
				</label> <logic:equal value="true" name="paginado"
					property="existeSiguiente">
					<a
						href="comprobantes.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaSiguiente"/>">
					<img src='img/paginacion/ico_siguiente.png' /></a>
					<a
						href="comprobantes.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaFinal"/>">
					<img src='img/paginacion/ico_final.png' /></a>
				</logic:equal> <logic:notEqual value="true" name="paginado"
					property="existeSiguiente">
					<img src='img/paginacion/ico_siguiente_dis.png' />
					<img src='img/paginacion/ico_final_dis.png' />
				</logic:notEqual></td>

			</tr>
		</table>

		<logic:notEmpty name="listaDetalle">
		<layout:collection name="listaDetalle" title='DETALLE DE ORDEN'
			styleClass="FORM" id="orden" sortAction="client" align="center">
			<layout:collectionItem title="Id Orden" property="m_IdOrden" />
			<layout:collectionItem title="Id Registro"
				property="m_IdDetalleOrden" />
			<layout:collectionItem title="Tipo Cuenta"
				property="m_DescTipoCuenta" />
			<layout:collectionItem title="Cuenta" property="m_NumeroCuenta" />
			<layout:collectionItem title="Tipo Doc"
				property="m_DescTipoDocumento">
				<p align="center"><bean:write name="orden"
					property="m_DescTipoDocumento" /></p>
			</layout:collectionItem>
			<layout:collectionItem title="N&uacute;mero Doc"
				property="m_Documento" />
			<layout:collectionItem title="Referencia" property="m_Referencia" />
			<layout:collectionItem title="Nombre" property="m_Nombre" />
			<layout:collectionItem title="Moneda" property="m_DescTipoMoneda">
				<p align="center"><bean:write name="orden"
					property="m_DescTipoMoneda" /></p>
			</layout:collectionItem>
			<layout:collectionItem title="Importe" property="m_Monto">
				<p align="right"><bean:write name="orden" property="m_Monto" />
				</p>
			</layout:collectionItem>
			<layout:collectionItem title="Tipo Pago" property="m_DescTipoPago">
				<p align="center"><bean:write name="orden"
					property="m_DescTipoPago" /></p>
			</layout:collectionItem>
			<layout:collectionItem title="Estado" property="m_DescEstado" />
			<layout:collectionItem title="C&oacute;digo Respuesta"
				property="m_CodigoRptaIbs">
				<bean:define name="orden" property="m_descripcionCodIbs"
					id="descrip" type="java.lang.String" />
				<center><layout:link href="javascript:mostrar();"
					title="<%= descrip%>">
					<layout:write name="orden" property="m_CodigoRptaIbs" />
				</layout:link></center>
			</layout:collectionItem>
			<layout:collectionItem title="Fecha Proceso"
				property="m_FechaProceso" />
			<layout:collectionItem title="Nro Transacci&oacute;n"
				property="m_IdPago">
				<p align="center"><layout:write name="orden" property="m_IdPago" />
				</p>
			</layout:collectionItem>
			<layout:collectionItem title="Nro Cheque" property="m_NumCheque" />
			<logic:notEmpty name="comprobantePago">
				<layout:collectionItem title="Comisi&oacute;n Cliente"
					property="m_montoComClienteChg" />
				<layout:collectionItem title="Comisi&oacute;n Empresa"
					property="m_montoComEmpresaChg" />
			</logic:notEmpty>
		</layout:collection>
		</logic:notEmpty>
		
			<!-- LETRAS -->
		<logic:notEmpty name="listaDetallePagoLetras">		
		<layout:collection name="listaDetallePagoLetras"
			title='DETALLE DE ORDEN' styleClass="FORM" id="orden"
			sortAction="client" align="center">
			<layout:collectionItem title="Id Orden" property="m_IdOrden" />
			<layout:collectionItem title="Id Registro"
				property="m_IdDetalleOrden" />
			<layout:collectionItem title="Cuenta" property="m_NumeroCuenta" />
			<layout:collectionItem title="Moneda" property="m_DescTipoMoneda">
				<p align="center"><bean:write name="orden"
					property="m_DescTipoMoneda" /></p>
			</layout:collectionItem>
			<layout:collectionItem title="Principal" property="m_Principal" />
			<layout:collectionItem title="Importe" property="m_Monto">
				<p align="right"><bean:write name="orden" property="m_Monto" />
				</p>
			</layout:collectionItem>
			<layout:collectionItem title="Estado" property="m_DescEstado" />
			<layout:collectionItem title="C&oacute;digo Respuesta"
				property="m_CodigoRptaIbs">

				<bean:define name="orden" property="m_descripcionCodIbs"
					id="descrip" type="java.lang.String" />
				<center><layout:link href="javascript:mostrar();"
					title="<%= descrip%>">
					<layout:write name="orden" property="m_CodigoRptaIbs" />
				</layout:link></center>
			</layout:collectionItem>
			<layout:collectionItem title="Aceptante" property="m_CodAceptante" />
			<layout:collectionItem title="Recibo" property="m_Documento" />
			<layout:collectionItem title="Fecha Proceso"
				property="m_FechaProceso" />
			<layout:collectionItem title="Moneda Mora">
				<p align="center"><layout:write name="orden"
					property="m_MonedaMora" /></p>
			</layout:collectionItem>
			<layout:collectionItem title="Mora" property="m_MontoMora">
				<p align="right"><layout:write name="orden"
					property="m_MontoMora" /></p>
			</layout:collectionItem>
			<layout:collectionItem title="Referencia" property="m_Referencia" />
			<layout:collectionItem title="Moneda ITF">
				<p align="center"><layout:write name="orden"
					property="m_DescMonedaITF" /></p>
			</layout:collectionItem>
			<layout:collectionItem title="ITF" property="m_ITF">
				<p align="right"><layout:write name="orden" property="m_ITF" />
				</p>
			</layout:collectionItem>
			<layout:collectionItem title="Moneda Portes">
				<p align="center"><layout:write name="orden"
					property="m_DescMonedaPortes" /></p>
			</layout:collectionItem>
			<layout:collectionItem title="Portes" property="m_Portes">
				<p align="right"><layout:write name="orden" property="m_Portes" />
				</p>
			</layout:collectionItem>
			<layout:collectionItem title="Moneda Protesto">
				<p align="center"><layout:write name="orden"
					property="m_MonedaProtesto" /></p>
			</layout:collectionItem>
			<layout:collectionItem title="Protesto" property="m_Protesto">
				<p align="right"><layout:write name="orden"
					property="m_Protesto" /></p>
			</layout:collectionItem>
		</layout:collection>
	</logic:notEmpty>
		<layout:space />
		<table width="100%" cellpadding="2" cellspacing="2" border="0">
			<tr>
				<td align="left" valign="middle"><img src="img/bt-volver.png"
					width="71" height="27" align="middle"
					onClick="javascript:volver();"
					onMouseOver="this.src='img/bt-volver2.png'"
					onMouseOut="this.src='img/bt-volver.png'" /></td>

				<td class="paginado" align="right" valign="middle"><logic:equal
					value="true" name="paginado" property="existeAnterior">
					<a
						href="comprobantes.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaInicio"/>">
					<img src='img/paginacion/ico_inicio.png' /></a>
					<a
						href="comprobantes.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaAnterior"/>">
					<img src='img/paginacion/ico_anterior.png' /></a>
				</logic:equal> <logic:notEqual value="true" name="paginado"
					property="existeAnterior">
					<img src='img/paginacion/ico_inicio_dis.png' />
					<img src='img/paginacion/ico_anterior_dis.png' />
				</logic:notEqual> <label> <bean:write name="paginado" property="encabezado" />
				</label> <logic:equal value="true" name="paginado"
					property="existeSiguiente">
					<a
						href="comprobantes.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaSiguiente"/>">
					<img src='img/paginacion/ico_siguiente.png' /></a>
					<a
						href="comprobantes.do?do=paginarDetallesOrden&nroPagina=<bean:write name="paginado" property="paginaFinal"/>">
					<img src='img/paginacion/ico_final.png' /></a>
				</logic:equal> <logic:notEqual value="true" name="paginado"
					property="existeSiguiente">
					<img src='img/paginacion/ico_siguiente_dis.png' />
					<img src='img/paginacion/ico_final_dis.png' />
				</logic:notEqual></td>
			</tr>
		</table>




</html:form>
</body>
</html>
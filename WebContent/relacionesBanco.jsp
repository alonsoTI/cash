

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>

<html>
<head>
<title></title>
<meta http-equiv="Content-Type"	content="text/html; charset=windows-1252">
<link href="css/Styles.css" rel="stylesheet" type="text/css">

<style type="text/css">
@import url(calendario/calendar-system.css);
</style>
<script type="text/javascript" src="calendario/calendar.js"></script>
<script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
<script type="text/javascript" src="calendario/calendar-setup.js"></script>
<script type="text/javascript" src="js/Functions.js"></script>

<style type="text/css">
body {
	background: url(img/fondo.gif) no-repeat fixed;
	background-position: right;
}

a.linkPrestamo {
	text-decoration: underline;
}
</style>

<script language="JavaScript">
   
	function primero(){

	}
	function anterior(){

	}
	function siguiente(){

	}
	function ultimo(){

	}
    function buscar(){
      var frm = document.forms[0];
	  frm.action = "consultarSaldos.do?do=buscarRelacionesBco";
      frm.submit();
    }
    
</script>

</head>
<body>
<html:form action="consultarSaldos.do" onsubmit="return false">
	<logic:notEqual name="habil" value="1">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr valign="baseline">
				<td align="center" class="TitleRowCierraSession" valign="baseline"
					height="100%"><bean:message key="errors.authorization" /></td>
			</tr>
		</table>
	</logic:notEqual>
	<logic:equal name="habil" value="1">
		<table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title">
			<tr>
				<td valign="middle" align="left"><bean:message
					key="consulta.relaciones.banco" /></td>
			</tr>
		</table>

		<table width="100%" cellpadding="2" cellspacing="2" border="0"
			align="center">
			<tr>
				<td class="CellColRow">Empresa:</td>
				<td class="CellColRow2">
			
        
         <html:select property="m_Empresa"
					styleId="m_Empresa" styleClass="CellColRow">
					<logic:notEmpty name="listaEmpresas">
						<html:options collection="listaEmpresas"
							property="idEmpresaCodCliente" labelProperty="demNombre" />
					</logic:notEmpty>
				</html:select></td>


			</tr>
			<tr align="right">
				<td colspan="4" class="CellColRow5"><img
					src="img/bt-buscar.png" align="middle"
					onMouseOver="this.src='img/bt-buscarB.png'"
					onMouseOut="this.src='img/bt-buscar.png'"
					onClick="javascript:buscar();" /></td>

			</tr>
		</table>
		<!-- jwong 03/03/2009 para manejo de la empresa seleccionada -->
		<html:hidden property="m_EmpresaSel" styleId="m_EmpresaSel" />

		<logic:notEmpty name="beanConsRelBco">
			<logic:notEmpty name="listaRelaciones">
				<table width="100%" cellpadding="2" cellspacing="2" border="0">
					<tr>
						<td class='CellColRow6'><%--img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarMovimientos', 'save', 'excel');"/--%>
						<img src='img/excel.png' alt="Exportar Excel" align='middle'
							onClick="javascript:exportar('consultarSaldos.do?do=exportarRelacionesBco&m_EmpresaExp=<bean:write name='m_EmpresaExp'/>','save','excel');" />

						<%--img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarMovimientos', 'save', 'txt');"/--%>
						<img src='img/text.png' alt="Exportar Texto" align='middle'
							onClick="javascript:exportar('consultarSaldos.do?do=exportarRelacionesBco&m_EmpresaExp=<bean:write name='m_EmpresaExp'/>','save','txt');" />

						<%--img src="img/printer.png" alt="Imprimir" align="middle" onClick="javascript:exportar('consultarSaldos.do?do=exportarMovimientos', 'print');"/--%>
						<img src='img/printer.png' alt="Imprimir" align='middle'
							onClick="javascript:exportar('consultarSaldos.do?do=exportarRelacionesBco&m_EmpresaExp=<bean:write name='m_EmpresaExp'/>','print');" />
						</td>
					</tr>
				</table>

				<table cellpadding='2' cellspacing='2' border='0' align='center'
					width='100%'>
					<tr>
						<td class='TitleRow3' colspan='5'>DETALLE DE RELACIONES CON
						EL BANCO</td>
					</tr>
					<logic:iterate id="cuenta" name="listaRelaciones" indexId="i">
						<tr>
							<td class='TitleRow3' colspan='5'>&nbsp; <bean:write
								name="cuenta" property="m_Description" /></td>
						</tr>
						<tr class='TitleRow4'>
							<td>Cuenta</td>
							<td>Moneda</td>
							<td>Saldo Disponible</td>
							<td>Saldo Contable</td>
							<td width="96">Acciones</td>							
						</tr>
						<logic:iterate id="tipoCuenta" name="cuenta" property="m_Accounts"
							indexId="j">
							<tr>
								<td class='CellColRow7'><bean:write name="tipoCuenta"
									property="m_Cuenta" /></td>

								<td class='CellColRow7'><bean:write name="tipoCuenta"
									property="m_Moneda" /></td>
								<td class='CellColRow6'><logic:lessThan name="tipoCuenta"
									property="m_SaldoDisponible" value="0">
									<p align="right" class="CellColCargo"><layout:write
										name="tipoCuenta" property="m_SaldoDisponible" /></p>
								</logic:lessThan> <logic:greaterEqual name="tipoCuenta"
									property="m_SaldoDisponible" value="0">
									<p align="right"><layout:write name="tipoCuenta"
										property="m_SaldoDisponible" /></p>
								</logic:greaterEqual></td>
								<td class='CellColRow6'><logic:lessThan name="tipoCuenta"
									property="m_SaldoContable" value="0">
									<p align="right" class="CellColCargo"><layout:write
										name="tipoCuenta" property="m_SaldoContable" /></p>
								</logic:lessThan> <logic:greaterEqual name="tipoCuenta"
									property="m_SaldoContable" value="0">
									<p align="right"><layout:write name="tipoCuenta"
										property="m_SaldoContable" /></p>
								</logic:greaterEqual>
								</td>
								<logic:equal value="P" name="tipoCuenta" property="prestamo">
									<td align="center"><img src="img/cms.png" height="24"
										width="24" title="Cronograma de Pago"
										onClick="javascript:DoSubmit('consultarSaldos.do?do=iniciarCronograma&tipoCronograma=1&nroCuenta=<bean:write	name="tipoCuenta" property="m_Cuenta" />');" />


									<img src="img/cronograma.PNG" height="24" width="24"
										title="Cronograma Deuda"
										onClick="javascript:DoSubmit('consultarSaldos.do?do=iniciarCronograma&tipoCronograma=2&nroCuenta=<bean:write	name="tipoCuenta" property="m_Cuenta" />');" />

									<img src="img/calculadora.jpg" height="24" width="24"
										title="Liquidador"
										onClick="javascript:DoSubmit('consultarSaldos.do?do=buscarLiquidador&nroCuenta=<bean:write	name="tipoCuenta" property="m_Cuenta" />');" />

									</td>
								</logic:equal>
								<logic:equal value="D" name="tipoCuenta" property="prestamo">
									<td></td>
								</logic:equal>								
							</tr>
						</logic:iterate>
					</logic:iterate>
				</table>


				<layout:space />
			</logic:notEmpty>
		</logic:notEmpty>

		<!--mensaje de error en caso existan-->
		<logic:notEmpty name="mensaje">
			<table cellpadding='2' cellspacing='2' border='0' align='center'
				width='90%'>
				<tr>
					<td class='TitleRow3'><bean:write name="mensaje" /></td>
				</tr>
			</table>
		</logic:notEmpty>
	</logic:equal>
</html:form>
</body>
</html>
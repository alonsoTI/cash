<%-- 
    Document   : consultaMovHistorico
    Created on : 04-dic-2008, 9:44:38
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>

<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">

  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>
  <script type="text/javascript" src="js/Functions.js"></script>

  <script type="text/javascript" src="config/javascript.js"></script>

  <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
  </style>

<logic:equal name="habil" value="1">
  <script language="JavaScript">
    
	function imprimir(){
	  window.print();
	}
    function buscar(){
      var fecIni = document.getElementById("m_FecInicio").value;
      var fecFin = document.getElementById("m_FecFin").value;
      var fecActual = '<bean:write name="fechaActualComp"/>';
      if(validarFechasRelacionadas(fecIni, fecFin, fecActual)){
          //primero se hace una consulta de saldos
          var frm = document.forms[0];
          //parametro tipo indica el tipo de consulta(0=saldos / 1=historico de movimientos)
          frm.action = "consultarSaldos.do?do=buscarSaldos&tipo=1";
          frm.submit();
      }
	}
    function paginar(valor){
        location.href = valor;
    }
    
</script>
</logic:equal>

</head>
<body>
<html:form action="consultarSaldos.do">
<logic:notEqual name="habil" value="1">
  <table width="100%" cellpadding="0" cellspacing="0" border="0">
    <tr><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td></tr>
    <tr valign="baseline">
        <td align="center" class="TitleRowCierraSession" valign="baseline" height="100%">
            <bean:message key="errors.authorization"/>
        </td>
    </tr>
  </table>
</logic:notEqual>
<logic:equal name="habil" value="1">
  <table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
    <tr>
      <td valign="middle" align="left"><bean:message key="consulta.historico.movimiento"/></td>
    </tr>
  </table>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class="CellColRow">Empresa:</td>
      <td class="CellColRow2">
          <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow">
          <logic:notEmpty name="listaEmpresas">
              <html:options collection="listaEmpresas" property="idEmpresaCodCliente" labelProperty="demNombre"/>
          </logic:notEmpty>
        </html:select>
      </td>
      <td class="CellColRow">Tipo de Informaci&oacute;n:</td>
      <td class="CellColRow2">
        <html:select property="m_TipoInformacion" styleId="m_TipoInformacion" styleClass="CellColRow">
          <html:option value="T">Todos</html:option>
          <logic:notEmpty name="tiposInformacion">
              <html:options collection="tiposInformacion" property="id.clfCode" labelProperty="dlfDescription"/>
          </logic:notEmpty>
        </html:select>
      </td>
    </tr>

	<tr>
	  <td class='CellColRow'>Desde</td>
	  <td class='CellColRow2'>
		<!--input type='TEXT' id='m_IniFec' size='10' maxlength='10' class="CellColRow6"/-->
        <html:text property="m_FecInicio" styleId="m_FecInicio" size="10" maxlength="10" styleClass="CellColRow"/>
		<button id='btn_ini' name='btn_ini'><img src='img/cal.gif' /></button>
		<script type='text/javascript'>
		  Calendar.setup(
			{
			  inputField : "m_FecInicio",
			  ifFormat : "%d/%m/%Y",
			  button : "btn_ini"
			}
		  );
		</script>
	  </td>
	  <td class='CellColRow'>Hasta</td>
	  <td class='CellColRow2'>
        <html:text property="m_FecFin" styleId="m_FecFin" size="10" maxlength="10" styleClass="CellColRow"/>
		<button id='btn_fin' name='btn_fin'><img src='img/cal.gif' /></button>
		<script type='text/javascript'>
		  Calendar.setup(
			{
			  inputField : "m_FecFin",
			  ifFormat : "%d/%m/%Y",
			  button : "btn_fin"
			}
		  );
		</script>
	  </td>
	</tr>

	<tr align="right">
      <td colspan="4" class="CellColRow5">
        <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:buscar();"/>
      </td>

    </tr>
  </table>
  <!-- jwong 03/03/2009 para manejo de la empresa seleccionada -->
  <html:hidden property="m_EmpresaSel" styleId="m_EmpresaSel" />
  
  <!--resultados de la busqueda-->
  <logic:notEmpty name="listaResult">
  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class='CellColRow6'>
        <img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarSaldos&m_Empresa=<bean:write name="mIdEmpresa" />','save','excel');"/>
        <%--img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarMovimientos', 'save', 'excel');"/--%>
        <!--img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportarPrueba();"/-->

        <img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarSaldos&m_Empresa=<bean:write name="mIdEmpresa" />','save','txt');"/>
        <%--img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarMovimientos', 'save', 'txt');"/--%>
        <!--img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportarTextoPrueba();"/-->

        <img src="img/printer.png" alt="Imprimir" align="middle" onClick="javascript:exportar('consultarSaldos.do?do=exportarSaldos&m_Empresa==<bean:write name="mIdEmpresa" />','print');"/>
        <%--img src="img/printer.png" alt="Imprimir" align="middle" onClick="javascript:exportar('consultarSaldos.do?do=exportarMovimientos', 'print');"/--%>
      </td>
    </tr>
  </table>
  <layout:collection name="listaResult" title="DETALLE DE SALDOS" styleClass="FORM" id="cuenta" sortAction="client" width="100%" align="center">
      <layout:collectionItem title="Cuenta">
        <center>
        <layout:link href="consultarSaldos.do?do=buscarMovimientos&tipo=1" name="cuenta" property="parametrosUrl">
            <layout:write name="cuenta" property="m_Cuenta"/>
        </layout:link>
        </center>
      </layout:collectionItem>

      <layout:collectionItem title="Tipo de Cuenta" property="m_DescripcionCuenta" />
      <layout:collectionItem title="Moneda" property="m_Moneda" />

      <layout:collectionItem title="Saldo Disponible">
        <%--logic:equal name="cuenta" property="m_SignoDisponible" value="-">
            <p align="right" class="CellColCargo">
                &minus;<layout:write name="cuenta" property="m_SaldoDisponible"/>
            </p>
        </logic:equal>
        <logic:notEqual name="cuenta" property="m_SignoDisponible" value="-">
            <p align="right">
                <layout:write name="cuenta" property="m_SaldoDisponible"/>
            </p>
        </logic:notEqual--%>

        <logic:lessThan name="cuenta" property="m_SaldoDisponible" value="0">
            <p align="right" class="CellColCargo">
                <layout:write name="cuenta" property="m_SaldoDisponible"/>
            </p>
        </logic:lessThan>
        <logic:greaterEqual name="cuenta" property="m_SaldoDisponible" value="0">
            <p align="right">
                <layout:write name="cuenta" property="m_SaldoDisponible"/>
            </p>
        </logic:greaterEqual>
      </layout:collectionItem>

      <layout:collectionItem title="Saldo Retenido">
        <%--logic:equal name="cuenta" property="m_SignoRetenido" value="-">
            <p align="right" class="CellColCargo">
                &minus;<layout:write name="cuenta" property="m_SaldoRetenido"/>
            </p>
        </logic:equal>
        <logic:notEqual name="cuenta" property="m_SignoRetenido" value="-">
            <p align="right">
                <layout:write name="cuenta" property="m_SaldoRetenido"/>
            </p>
        </logic:notEqual--%>

        <logic:lessThan name="cuenta" property="m_SaldoRetenido" value="0">
            <p align="right" class="CellColCargo">
                <layout:write name="cuenta" property="m_SaldoRetenido"/>
            </p>
        </logic:lessThan>
        <logic:greaterEqual name="cuenta" property="m_SaldoRetenido" value="0">
            <p align="right">
                <layout:write name="cuenta" property="m_SaldoRetenido"/>
            </p>
        </logic:greaterEqual>
      </layout:collectionItem>

      <layout:collectionItem title="Saldo Contable">
        <%--logic:equal name="cuenta" property="m_SignoContable" value="-">
            <p align="right" class="CellColCargo">
                &minus;<layout:write name="cuenta" property="m_SaldoContable"/>
            </p>
        </logic:equal>
        <logic:notEqual name="cuenta" property="m_SignoContable" value="-">
            <p align="right">
                <layout:write name="cuenta" property="m_SaldoContable"/>
            </p>
        </logic:notEqual--%>

        <logic:lessThan name="cuenta" property="m_SaldoContable" value="0">
            <p align="right" class="CellColCargo">
                <layout:write name="cuenta" property="m_SaldoContable"/>
            </p>
        </logic:lessThan>
        <logic:greaterEqual name="cuenta" property="m_SaldoContable" value="0">
            <p align="right">
                <layout:write name="cuenta" property="m_SaldoContable"/>
            </p>
        </logic:greaterEqual>
      </layout:collectionItem>
  </layout:collection>
  <layout:space/>
  </logic:notEmpty>
  
  <!--detalle de historico de movimientos-->
  <logic:notEmpty name="beanHisMovimientos">

  <table cellpadding='2' cellspacing='2' border='0' align='center' width='100%'>
	  <tr>
		<td class='TitleRow3' colspan='5'>DETALLE DE SALDO</td>
	  </tr>
	  <tr class='TitleRow4'>
          <td>Titular:&nbsp;<bean:write name="beanHisMovimientos" property="m_Titular"/></td>
          <td>Moneda:&nbsp;<bean:write name="beanHisMovimientos" property="m_Moneda"/></td>
		  <td>Cuenta:&nbsp;<bean:write name="beanHisMovimientos" property="m_Cuenta"/></td>
          <%--
		  <td>Fecha:&nbsp;<bean:write name="beanHisMovimientos" property="m_Fecha"/></td>
          <td>Hora:&nbsp;<bean:write name="beanHisMovimientos" property="m_Hora"/></td>
          --%>
	  </tr>

      <tr>
		<td class='CellColRow7'>Saldo Disponible</td>
        
        <logic:lessThan name="beanHisMovimientos" property="m_SaldoDisponible" value="0">
            <td class='CellColCargo' colspan='2'>
                <bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>
            </td>
        </logic:lessThan>
        <logic:greaterEqual name="beanHisMovimientos" property="m_SaldoDisponible" value="0">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>
            </td>
        </logic:greaterEqual>
        
        <%--
        <logic:equal name="beanHisMovimientos" property="m_SignoDisponible" value="-">
            <td class='CellColCargo' colspan='2'>
                &minus;<bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>
            </td>
        </logic:equal>
        <logic:notEqual name="beanHisMovimientos" property="m_SignoDisponible" value="-">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>
            </td>
        </logic:notEqual>
        --%>
	  </tr>


      <tr>
		<td class='CellColRow7'>Saldo Retenido</td>
        
        <logic:lessThan name="beanHisMovimientos" property="m_SaldoRetenido" value="0">
            <td class='CellColCargo' colspan='2'>
                <bean:write name="beanHisMovimientos" property="m_SaldoRetenido"/>
            </td>
        </logic:lessThan>
        <logic:greaterEqual name="beanHisMovimientos" property="m_SaldoRetenido" value="0">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanHisMovimientos" property="m_SaldoRetenido"/>
            </td>
        </logic:greaterEqual>

        <%--
        <logic:equal name="beanHisMovimientos" property="m_SignoRetenido" value="-">
            <td class='CellColCargo' colspan='2'>
                &minus;<bean:write name="beanHisMovimientos" property="m_SaldoRetenido" />
            </td>
        </logic:equal>
        <logic:notEqual name="beanHisMovimientos" property="m_SignoRetenido" value="-">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanHisMovimientos" property="m_SaldoRetenido"/>
            </td>
        </logic:notEqual>
        --%>
	  </tr>

	  <tr>
		<td class='CellColRow7'>Saldo Contable</td>
        
        <logic:lessThan name="beanHisMovimientos" property="m_SaldoContable" value="0">
            <td class='CellColCargo' colspan='2'>
                <bean:write name="beanHisMovimientos" property="m_SaldoContable"/>
            </td>
        </logic:lessThan>
        <logic:greaterEqual name="beanHisMovimientos" property="m_SaldoContable" value="0">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanHisMovimientos" property="m_SaldoContable"/>
            </td>
        </logic:greaterEqual>

        <%--
        <logic:equal name="beanHisMovimientos" property="m_SignoContable" value="-">
            <td class='CellColCargo' colspan='2'>
                &minus;<bean:write name="beanHisMovimientos" property="m_SaldoContable"/>
            </td>
        </logic:equal>
        <logic:notEqual name="beanHisMovimientos" property="m_SignoContable" value="-">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanHisMovimientos" property="m_SaldoContable"/>
            </td>
        </logic:notEqual>
        --%>
	  </tr>
  </table><br>
  <logic:notEmpty name="resultListaHisMovimientos">
  <input id="m_Cuenta" type="hidden" value="<bean:write name='m_Cuenta'/>">
  <input id="m_TipoCuenta" type="hidden" value="<bean:write name='m_TipoCuenta'/>">

  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class='CellColRow6'>
        
        <img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarHistoricoMov&m_Cuenta=<bean:write name='m_Cuenta'/>&m_TipoCuenta=<bean:write name='m_TipoCuenta'/>&m_Moneda=<bean:write name='m_Moneda'/>&saldoDisponibleExp=<bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>&saldoRetenidoExp=<bean:write name="beanHisMovimientos" property="m_SaldoRetenido"/>&saldoContableExp=<bean:write name="beanHisMovimientos" property="m_SaldoContable"/>','save','excel');"/>      
        <img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarHistoricoMov&m_Cuenta=<bean:write name='m_Cuenta'/>&m_TipoCuenta=<bean:write name='m_TipoCuenta'/>&m_Moneda=<bean:write name='m_Moneda'/>&saldoDisponibleExp=<bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>&saldoRetenidoExp=<bean:write name="beanHisMovimientos" property="m_SaldoRetenido"/>&saldoContableExp=<bean:write name="beanHisMovimientos" property="m_SaldoContable"/>','save','txt');"/>
        <img src='img/printer.png' alt="Imprimir" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarHistoricoMov&m_Cuenta=<bean:write name='m_Cuenta'/>&m_TipoCuenta=<bean:write name='m_TipoCuenta'/>&m_Moneda=<bean:write name='m_Moneda'/>&saldoDisponibleExp=<bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>&saldoRetenidoExp=<bean:write name="beanHisMovimientos" property="m_SaldoRetenido"/>&saldoContableExp=<bean:write name="beanHisMovimientos" property="m_SaldoContable"/>','print');"/>
      </td>
    </tr>
    <tr>
        <td align="center">
            <img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('consultarSaldos.do?do=buscarMovimientos&tipoPaginado=P&esPag=si&m_Cuenta=<bean:write name='m_Cuenta'/>&m_TipoCuenta=<bean:write name='m_TipoCuenta'/>&m_Moneda=<bean:write name='m_Moneda'/>&m_SaldoDisponible=<bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>&m_SaldoRetenido=<bean:write name="beanHisMovimientos" property="m_SaldoRetenido"/>&m_SaldoContable=<bean:write name="beanHisMovimientos" property="m_SaldoContable"/>&tipo=1');"/>
            <img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('consultarSaldos.do?do=buscarMovimientos&esPag=si&tipoPaginado=A&m_Cuenta=<bean:write name='m_Cuenta'/>&m_TipoCuenta=<bean:write name='m_TipoCuenta'/>&m_Moneda=<bean:write name='m_Moneda'/>&m_SaldoDisponible=<bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>&m_SaldoRetenido=<bean:write name="beanHisMovimientos" property="m_SaldoRetenido"/>&m_SaldoContable=<bean:write name="beanHisMovimientos" property="m_SaldoContable"/>&tipo=1');"/>
            P&aacute;gina <bean:write name="bpCons" property="m_pagActual"/> de <bean:write name="bpCons" property="m_pagFinal"/>
            <img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('consultarSaldos.do?do=buscarMovimientos&esPag=si&tipoPaginado=S&m_Cuenta=<bean:write name='m_Cuenta'/>&m_TipoCuenta=<bean:write name='m_TipoCuenta'/>&m_Moneda=<bean:write name='m_Moneda'/>&m_SaldoDisponible=<bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>&m_SaldoRetenido=<bean:write name="beanHisMovimientos" property="m_SaldoRetenido"/>&m_SaldoContable=<bean:write name="beanHisMovimientos" property="m_SaldoContable"/>&tipo=1');"/>
            <img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('consultarSaldos.do?do=buscarMovimientos&esPag=si&tipoPaginado=U&m_Cuenta=<bean:write name='m_Cuenta'/>&m_TipoCuenta=<bean:write name='m_TipoCuenta'/>&m_Moneda=<bean:write name='m_Moneda'/>&m_SaldoDisponible=<bean:write name="beanHisMovimientos" property="m_SaldoDisponible"/>&m_SaldoRetenido=<bean:write name="beanHisMovimientos" property="m_SaldoRetenido"/>&m_SaldoContable=<bean:write name="beanHisMovimientos" property="m_SaldoContable"/>&tipo=1');"/>
        </td>
    </tr>
  </table>
  
  <layout:collection name="resultListaHisMovimientos" title="DETALLE DE MOVIMIENTOS" styleClass="FORM" id="movimiento" sortAction="client" width="100%" align="center">
      <layout:collectionItem title="Fecha" property="m_Fecha" sortable="true" />
      <layout:collectionItem title="Hora" property="m_Hora" />
      <layout:collectionItem title="Tipo Transacci&oacute;n" property="m_TipoTrx" sortable="true" />
      <layout:collectionItem title="Descripci&oacute;n" property="m_Descripcion" sortable="true" />

      <layout:collectionItem title="Importe">
        <logic:equal name="movimiento" property="m_Signo" value="-">
            <p align="right" class="CellColCargo">
                &minus;<layout:write name="movimiento" property="m_Importe"/>
            </p>
        </logic:equal>
        <logic:notEqual name="movimiento" property="m_Signo" value="-">
            <p align="right">
                <layout:write name="movimiento" property="m_Importe"/>
            </p>
        </logic:notEqual>
      </layout:collectionItem>
  </layout:collection>

  <div id="comp0" style="display:none" >FEC</div>
  <div id="comp2" style="display:none">ANS</div>
  <div id="comp3" style="display:none">ANS</div>
  </logic:notEmpty>
  
  <layout:space/>
  </logic:notEmpty>

  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
    </tr>
  </table>
  </logic:notEmpty>
</logic:equal>
</html:form>
</body>
</html>
<%--
    Document   : consultaSaldos
    Created on : 11/11/2008, 10:15:30 AM
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>


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
  <script language="javascript" SRC="js/Functions.js"></script>

  <script type="text/javascript" src="config/javascript.js"></script>

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
      //parametro tipo indica el tipo de consulta(0=saldos / 1=historico de movimientos)
	  frm.action = "consultarSaldos.do?do=buscarSaldos&tipo=0";
      frm.submit();
    }    
    
</script>

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
      <td valign="middle" align="left"><bean:message key="consulta.saldo.movimiento"/></td>
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
      <td class="CellColRow">&nbsp;&nbsp;&nbsp;</td>
      <!--td class="CellColRow">Tipo de Informaci&oacute;n:</td-->
      <td class="CellColRow">&nbsp;</td>
      <td class="CellColRow2">
        <%--html:select property="m_TipoInformacion" styleId="m_TipoInformacion" styleClass="CellColRow">
          <html:option value="T">Todos</html:option>
          <logic:notEmpty name="tiposInformacion">
              <html:options collection="tiposInformacion" property="id.clfCode" labelProperty="dlfDescription"/>
          </logic:notEmpty>
        </html:select--%>&nbsp;
      </td>
    </tr>
	<tr align="right">
      <td colspan="5" class="CellColRow5">
        <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:buscar();"/>
      </td>
    </tr>
  </table>
  <!-- jwong 03/03/2009 para manejo de la empresa seleccionada -->
  <html:hidden property="m_EmpresaSel" />
  
  <!--resultados de la busqueda-->
  <logic:notEmpty name="listaResult">
  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class='CellColRow6'>
        <img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarSaldos&m_Empresa=<bean:write name="mIdEmpresa" />','save','excel');"/>
        <img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarSaldos&m_Empresa=<bean:write name="mIdEmpresa" />','save','txt');"/>   
        <img src="img/printer.png" alt="Imprimir" align="middle" onClick="javascript:exportar('consultarSaldos.do?do=exportarSaldos&m_Empresa==<bean:write name="mIdEmpresa" />','print');"/>        
      </td>
    </tr>
  </table>
  <layout:collection name="listaResult" title="DETALLE DE SALDOS" styleClass="FORM" id="cuenta" sortAction="client" width="100%" align="center">
      <layout:collectionItem title="Cuenta">
        <center>
        <layout:link href="consultarSaldos.do?do=listarUltimosMovimientos" name="cuenta" property="parametrosUrl">
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
  <logic:notEmpty name="beanMovimientos">
  
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='100%'>
	  <tr>
		<td class='TitleRow3' colspan='5'>DETALLE DE SALDO</td>
	  </tr>
	  <tr class='TitleRow4'>
          <td>Titular:&nbsp;<bean:write name="beanMovimientos" property="m_Titular"/></td>
          <td>Moneda:&nbsp;<bean:write name="beanMovimientos" property="m_Moneda"/></td>
		  <td>Cuenta:&nbsp;<bean:write name="beanMovimientos" property="m_Cuenta"/></td>
          <%--
		  <td>Fecha:&nbsp;<bean:write name="beanMovimientos" property="m_Fecha"/></td>
          <td>Hora:&nbsp;<bean:write name="beanMovimientos" property="m_Hora"/></td>
          --%>
	  </tr>
	  <tr>
		<td class='CellColRow7'>Saldo Disponible</td>
        <logic:lessThan name="beanMovimientos" property="m_SaldoDisponible" value="0">
            <td class='CellColCargo' colspan='2'>
                <bean:write name="beanMovimientos" property="m_SaldoDisponible"/>
            </td>
        </logic:lessThan>
        <logic:greaterEqual name="beanMovimientos" property="m_SaldoDisponible" value="0">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanMovimientos" property="m_SaldoDisponible"/>
            </td>
        </logic:greaterEqual>
	  </tr>


      <tr>
		<td class='CellColRow7'>Saldo Retenido</td>
        <logic:lessThan name="beanMovimientos" property="m_SaldoRetenido" value="0">
            <td class='CellColCargo' colspan='2'>
                <bean:write name="beanMovimientos" property="m_SaldoRetenido"/>
            </td>
        </logic:lessThan>
        <logic:greaterEqual name="beanMovimientos" property="m_SaldoRetenido" value="0">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanMovimientos" property="m_SaldoRetenido"/>
            </td>
        </logic:greaterEqual>
	  </tr>
      
	  <tr>
		<td class='CellColRow7'>Saldo Contable</td>
        <logic:lessThan name="beanMovimientos" property="m_SaldoContable" value="0">
            <td class='CellColCargo' colspan='2'>
                <bean:write name="beanMovimientos" property="m_SaldoContable"/>
            </td>
        </logic:lessThan>
        <logic:greaterEqual name="beanMovimientos" property="m_SaldoContable" value="0">
            <td class='CellColRow6' colspan='2'>
                <bean:write name="beanMovimientos" property="m_SaldoContable"/>
            </td>
        </logic:greaterEqual>
	  </tr>
  </table><br>
  
  <logic:notEmpty name="resultListaMovimientos">
  <input id="m_Cuenta" type="hidden" value="<bean:write name='m_Cuenta'/>">
  <input id="m_TipoCuenta" type="hidden" value="<bean:write name='m_TipoCuenta'/>">
  
  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class='CellColRow6'>
        <img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarMovimientos&m_Cuenta=<bean:write name='m_Cuenta'/>&m_TipoCuenta=<bean:write name='m_TipoCuenta'/>&m_Moneda=<bean:write name='m_Moneda'/>&saldoDisponibleExp=<bean:write name="beanMovimientos" property="m_SaldoDisponible"/>&saldoRetenidoExp=<bean:write name="beanMovimientos" property="m_SaldoRetenido"/>&saldoContableExp=<bean:write name="beanMovimientos" property="m_SaldoContable"/>','save','excel');"/>
        <img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarMovimientos&m_Cuenta=<bean:write name='m_Cuenta'/>&m_TipoCuenta=<bean:write name='m_TipoCuenta'/>&m_Moneda=<bean:write name='m_Moneda'/>&saldoDisponibleExp=<bean:write name="beanMovimientos" property="m_SaldoDisponible"/>&saldoRetenidoExp=<bean:write name="beanMovimientos" property="m_SaldoRetenido"/>&saldoContableExp=<bean:write name="beanMovimientos" property="m_SaldoContable"/>','save','txt');"/>
        <img src="img/printer.png" alt="Imprimir" align="middle" onClick="javascript:exportar('consultarSaldos.do?do=exportarMovimientos&m_Cuenta=<bean:write name='m_Cuenta'/>&m_TipoCuenta=<bean:write name='m_TipoCuenta'/>&m_Moneda=<bean:write name='m_Moneda'/>&saldoDisponibleExp=<bean:write name="beanMovimientos" property="m_SaldoDisponible"/>&saldoRetenidoExp=<bean:write name="beanMovimientos" property="m_SaldoRetenido"/>&saldoContableExp=<bean:write name="beanMovimientos" property="m_SaldoContable"/>','print');"/>
        
      </td>
    </tr>   
   
    
  </table>
  
  <layout:collection name="resultListaMovimientos" title="DETALLE DE MOVIMIENTOS" styleClass="FORM" id="movimiento" sortAction="client" width="100%" align="center">
      <layout:collectionItem title="Fecha" property="m_Fecha" sortable="true"/>
      <layout:collectionItem title="Hora" property="m_Hora" />
      <layout:collectionItem title="Tipo Transacci&oacute;n" property="m_TipoTrx" sortable="true"/>
      <layout:collectionItem title="Descripci&oacute;n" property="m_Descripcion" sortable="true"/>

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
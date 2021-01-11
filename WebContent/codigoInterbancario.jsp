<%-- 
    Document   : codigoInterbancario
    Created on : 04-dic-2008, 11:10:35
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
  <script type="text/javascript" src="config/javascript.js"></script>
  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>
  <script type="text/javascript" src="js/Functions.js"></script>

  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
  <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Expires" CONTENT="0">

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
    function buscar(){
      var frm = document.forms[0];
	  frm.action = "consultarSaldos.do?do=buscarCodIntBco";
      frm.submit();
    }
    function paginar(valor){
        location.href = valor;
    }
    /*
     * Estados de Cuenta
     * Activa
     * Inactiva
     * Suspendida
     * Bloqueada
     **/
   
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
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      <td valign="middle" align="left" class="Title"><bean:message key="consulta.codigo.interbancario"/></td>
	</tr>
  </table>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class="CellColRow">Empresa:</td>
      <td class="CellColRow2" >
          <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow">
          <logic:notEmpty name="listaEmpresas">
              <html:options collection="listaEmpresas" property="idEmpresaCodCliente" labelProperty="demNombre"/>
          </logic:notEmpty>
        </html:select>
      </td>
	  <td class="CellColRow" width="50%" colspan="2" >&nbsp;</td>
    </tr>
	<tr align="right">
      <td colspan="4" class="CellColRow5" >
        <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:buscar();"/>
      </td>
    </tr>
  </table>
  <!-- jwong 03/03/2009 para manejo de la empresa seleccionada -->
  <html:hidden property="m_EmpresaSel" />

  <%--resultados de la busqueda--%>
  <logic:notEmpty name="listaCodigosInter">
  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class='CellColRow6'>
        <img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarCodIntBco', 'save', 'excel');"/>
        <img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportar('consultarSaldos.do?do=exportarCodIntBco', 'save', 'txt');"/>
        <img src="img/printer.png" alt="Imprimir" align="middle" onClick="javascript:exportar('consultarSaldos.do?do=exportarCodIntBco', 'print');"/>
      </td>
    </tr>
    <tr align="center">
        <td>
            <img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('consultarSaldos.do?do=buscarCodIntBco&tipoPaginado=P');"/>
            <img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('consultarSaldos.do?do=buscarCodIntBco&tipoPaginado=A');"/>
            P&aacute;gina <bean:write name="bpCons" property="m_pagActual"/> de <bean:write name="bpCons" property="m_pagFinal"/>
            <img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('consultarSaldos.do?do=buscarCodIntBco&tipoPaginado=S');"/>
            <img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('consultarSaldos.do?do=buscarCodIntBco&tipoPaginado=U');"/>

        </td>
    </tr>
  </table>
  
  <layout:collection name="listaCodigosInter" title="DETALLE DE C&Oacute;DIGOS INTERBANCARIOS" styleClass="FORM" id="cuenta" sortAction="client" width="100%" align="center">
    <layout:collectionItem title="Cuenta" property="m_Cuenta" sortable="true" />
    <layout:collectionItem  title="Moneda" property="m_Moneda" />

    <layout:collectionItem title="Tipo Cuenta" property="m_TipoCuenta" sortable="true" />
    <layout:collectionItem title="Estado" property="m_Estado" sortable="true" />
    
    <layout:collectionItem title="C&oacute;digo Interbancario (CCI)" property="m_CodigoInterbancario" />
  </layout:collection>
  <layout:space/>
                    
  <div id="comp0" style="display:none" >NUM</div>
  <div id="comp2" style="display:none">ANS</div>
  <div id="comp3" style="display:none">ANS</div>
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
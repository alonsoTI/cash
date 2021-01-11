<%-- 
    Document   : detalleOrdenes
    Created on : 15-ene-2009, 9:24:52
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

  <style type="text/css">
  @import url(calendario/calendar-system.css);.Estilo2 {font-size: 10pt}
  .Estilo3 {font-size: 12pt}
  </style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>
  <script type="text/javascript" src="js/Functions.js"></script>
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

  <script language="JavaScript">
    
	function imprimir(){
	  window.print();
	}

	function volver(){
      //location.href = "comprobantes.do?do=buscarOrdenesLinea";
      location.href = "<bean:write name='back'/>";
    }
    function paginar(valor){        
        location.href = valor;
    }
    function mostrar(){
        /*no borrar este metodo*/
    }
	
</script>

</head>
<body>
<html:form action="comprobantes.do">
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      <td valign="middle" align="left" class="Title"><bean:message key="comprobantes.title.detalleord"/></td>
    </tr>
  </table>

  <logic:empty name="beanDetImportEstado">
  <logic:empty name="bandDet">
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>
          <td class='CellColRow6'>
            <img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportar('comprobantes.do?do=exportarDetOrdenes&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>', 'save', 'excel');"/>
            <img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportar('comprobantes.do?do=exportarDetOrdenes&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>', 'save', 'txt');"/>
            <img src="img/printer.png" alt="Imprimir" align="middle" onClick="javascript:exportar('comprobantes.do?do=exportarDetOrdenes&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>', 'print');"/>
          </td>
        </tr>
        <tr>
          <td class='CellColRow6'>
            <logic:equal name="beanPag" property="m_tipo" value="C">
            <img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('comprobantes.do?do=detalleOrdenesPag&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>&tipoPaginado=P');"/>
            <img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('comprobantes.do?do=detalleOrdenesPag&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>&tipoPaginado=A');"/>
            P&aacute;gina <bean:write name="beanPag" property="m_pagActual"/> de <bean:write name="beanPag" property="m_pagFinal"/>
            <img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('comprobantes.do?do=detalleOrdenesPag&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>&tipoPaginado=S');"/>
            <img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('comprobantes.do?do=detalleOrdenesPag&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>&tipoPaginado=U');"/>
            </logic:equal>
            <logic:equal name="beanPag" property="m_tipo" value="A">
            <img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('aprobaciones.do?do=detalleOrdenesPag&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>&tipoPaginado=P');"/>
            <img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('aprobaciones.do?do=detalleOrdenesPag&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>&tipoPaginado=A');"/>
            P&aacute;gina <bean:write name="beanPag" property="m_pagActual"/> de <bean:write name="beanPag" property="m_pagFinal"/>
            <img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('aprobaciones.do?do=detalleOrdenesPag&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>&tipoPaginado=S');"/>
            <img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('aprobaciones.do?do=detalleOrdenesPag&m_IdOrden=<bean:write name="midOrden"/>&m_IdServicio=<bean:write name="midServicio"/>&tipoPaginado=U');"/>
            </logic:equal>
          </td>
        </tr>
    </table>
  </logic:empty>
  </logic:empty>  
  <!-- ORDENES DE PAGO Y COBRO -->
  <logic:notEmpty name="listaDetalle">
   
   <layout:collection name="listaDetalle" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" align="center">       
      <layout:collectionItem title="Id Orden" property="m_IdOrden" />
      <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
      <layout:collectionItem title="Tipo Cuenta" property="m_DescTipoCuenta" />
      <layout:collectionItem title="Cuenta" property="m_NumeroCuenta" />
      <layout:collectionItem title="Tipo Doc" property="m_DescTipoDocumento" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoDocumento"/>          
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="N&uacute;mero Doc" property="m_Documento" />
      <layout:collectionItem title="Nombre" property="m_Nombre" />
      <layout:collectionItem title="Referencia" property="m_Referencia" />
      <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>          
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>          
          </p>
      </layout:collectionItem>
          <layout:collectionItem title="Tipo Pago" property="m_DescTipoPago">
              <p align="center">
                  <bean:write name="orden" property="m_DescTipoPago"/>   
              </p>
          </layout:collectionItem>
      <layout:collectionItem title="Estado" property="m_DescEstado" />
      <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
              <layout:link href="javascript:mostrar();" title="<%= descrip%>">
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
      </layout:collectionItem>      
      <%--layout:collectionItem title="Descripci&oacute;n" property="m_Descripcion" /--%>
      <%--layout:collectionItem title="Correo" property="m_Email" /--%>
      <%--layout:collectionItem title="Tel&eacute;fono" property="m_Telefono" /--%>
      <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
      <layout:collectionItem title="Nro Transacci&oacute;n" property="m_IdPago">
          <p align="center">
             <layout:write name="orden" property="m_IdPago"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Nro Cheque" property="m_NumCheque" />
      <logic:notEmpty name= "comprobantePago">
        <layout:collectionItem title="Comisi&oacute;n Cliente" property="m_montoComClienteChg" />
        <layout:collectionItem title="Comisi&oacute;n Empresa" property="m_montoComEmpresaChg" />
      </logic:notEmpty>
    </layout:collection>
   
   <layout:space/>
  </logic:notEmpty>
  <!-- ORDENES TRANSFERENCIA CUENTA PROPIA -->
  <logic:notEmpty name="listaDetalleTransf_P">
    <layout:collection name="listaDetalleTransf_P" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" align="center">
      <layout:collectionItem title="Id Orden" property="m_IdOrden" />
      <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
      <layout:collectionItem title="Cuenta de Cargo" property="m_CtaCargo" />
      <layout:collectionItem title="Cuenta de Abono" property="m_CtaAbono"/>
       <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Referencia" property="m_Referencia" />
      <layout:collectionItem title="Estado" property="m_DescEstado" />
      <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
          <layout:link href="javascript:mostrar();" title="<%= descrip%>">
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
      </layout:collectionItem>
      <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
    </layout:collection>
   <layout:space/>
  </logic:notEmpty>
  <!-- ORDENES TRANSFERENCIA CUENTA TERCEROS -->
  <logic:notEmpty name="listaDetalleTransf_T">
    <layout:collection name="listaDetalleTransf_T" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" align="center">
      <layout:collectionItem title="Id Orden" property="m_IdOrden" />
      <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
      <layout:collectionItem title="Cuenta de Cargo" property="m_CtaCargo" />
      <layout:collectionItem title="Cuenta de Abono" property="m_CtaAbonoCci"/>
       <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
     <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Referencia" property="m_Referencia" />
      <layout:collectionItem title="Estado" property="m_DescEstado" />
      <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
          <layout:link href="javascript:mostrar();" title="<%= descrip%>">
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
      </layout:collectionItem>
      <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
    </layout:collection>
   <layout:space/>
  </logic:notEmpty>
  <!-- ORDENES TRANSFERENCIA CUENTA INTERBANCARIA -->
  <logic:notEmpty name="listaDetalleTransf_I">
      <layout:collection name="listaDetalleTransf_I" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" align="center">
          <layout:collectionItem title="Id Orden" property="m_IdOrden" />
          <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
          <layout:collectionItem title="Cuenta de Cargo" property="m_CtaCargo" />
          <layout:collectionItem title="Cuenta de Abono CCI" property="m_CtaAbonoCci" />
           <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
         <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
          <layout:collectionItem title="Referencia" property="m_Referencia" />
          <layout:collectionItem title="Estado" property="m_DescEstado" />
          <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
          <layout:link href="javascript:mostrar();" title="<%= descrip%>">
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
          </layout:collectionItem>
          <layout:collectionItem title="Tipo Documento" property="m_TipoDocBenef" />
          <layout:collectionItem title="N&uacute;mero Documento" property="m_NumDocBenef" />
          <layout:collectionItem title="Nombre" property="m_NombreBenef" />
          <layout:collectionItem title="Direcci&oacute;n" property="m_DirBenef" />
          <layout:collectionItem title="T&eacute;lefono" property="m_TelefBenef" />
          <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
      </layout:collection>
    <layout:space/>
  </logic:notEmpty>
  <!-- ORDENES PAGO DE SERVICIO -->
  <logic:notEmpty name="listaDetallePagoServicio">
    <layout:collection name="listaDetallePagoServicio" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" align="center">
      <layout:collectionItem title="Id Orden" property="m_IdOrden" />
      <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
      <layout:collectionItem title="Cuenta" property="m_NumeroCuenta"/>
       <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Referencia" property="m_Referencia" />
      <layout:collectionItem title="Estado" property="m_DescEstado" />
      <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
          <layout:link href="javascript:mostrar();" title="<%= descrip%>">
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
      </layout:collectionItem>
       <%--layout:collectionItem title="Cliente" property="m_Nombre" /--%>
        <layout:collectionItem title="Recibo" property="m_Documento" />
      <layout:collectionItem title="Orden Ref." property="m_OrdenRef" />
      <layout:collectionItem title="Detalle Ref." property="m_DetalleOrdenRef" sortable="true" />
      <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />
    </layout:collection>
   <layout:space/>
   <div id="comp10" style="display:none">NUM</div>
  </logic:notEmpty>

  <!-- LETRAS -->
  <logic:notEmpty name="listaDetallePagoLetras">
      <layout:collection name="listaDetallePagoLetras" title='DETALLE DE ORDEN' styleClass="FORM" id="orden" sortAction="client" align="center" >
      <layout:collectionItem title="Id Orden" property="m_IdOrden" />
      <layout:collectionItem title="Id Registro" property="m_IdDetalleOrden" />
      <layout:collectionItem title="Cuenta" property="m_NumeroCuenta"/>
       <layout:collectionItem title="Moneda" property="m_DescTipoMoneda" >
          <p align="center">
          <bean:write name="orden" property="m_DescTipoMoneda"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Principal" property="m_Principal" />
      <layout:collectionItem title="Importe" property="m_Monto">
          <p align="right">
          <bean:write name="orden" property="m_Monto"/>
          </p>
      </layout:collectionItem>
      <layout:collectionItem title="Estado" property="m_DescEstado" />
      <layout:collectionItem title="C&oacute;digo Respuesta" property="m_CodigoRptaIbs" >
           <bean:define name="orden" property="m_descripcionCodIbs" id="descrip" type="java.lang.String" />
          <center>
              <layout:link href="javascript:mostrar();" title="<%= descrip%>" >
            <layout:write name="orden" property="m_CodigoRptaIbs"/>
          </layout:link>
          </center>
      </layout:collectionItem>
      <layout:collectionItem title="Aceptante" property="m_CodAceptante" />
      <layout:collectionItem title="Recibo" property="m_Documento" />
      <layout:collectionItem title="Fecha Proceso" property="m_FechaProceso" />      
      <layout:collectionItem title="Moneda Mora">
        <p align="center">
            <layout:write name="orden" property="m_MonedaMora"/>
        </p>
      </layout:collectionItem>
        <layout:collectionItem title="Mora" property="m_MontoMora" >
            <p align="right">
            <layout:write name="orden" property="m_MontoMora"/>
            </p>
        </layout:collectionItem>
      <layout:collectionItem title="Referencia" property="m_Referencia" />
       <layout:collectionItem title="Moneda ITF">
        <p align="center">
            <layout:write name="orden" property="m_DescMonedaITF"/>
        </p>
      </layout:collectionItem>
        <layout:collectionItem title="ITF" property="m_ITF" >
            <p align="right">
            <layout:write name="orden" property="m_ITF"/>
        </p>
        </layout:collectionItem>
      <layout:collectionItem title="Moneda Portes">
        <p align="center">
            <layout:write name="orden" property="m_DescMonedaPortes"/>
        </p>
      </layout:collectionItem>
        <layout:collectionItem title="Portes" property="m_Portes" >
            <p align="right">
            <layout:write name="orden" property="m_Portes"/>
            </p>
        </layout:collectionItem>
      <layout:collectionItem title="Moneda Protesto">
        <p align="center">
            <layout:write name="orden" property="m_MonedaProtesto"/>
        </p>
      </layout:collectionItem>
        <layout:collectionItem title="Protesto" property="m_Protesto">
            <p align="right">
            <layout:write name="orden" property="m_Protesto"/>
            </p>
        </layout:collectionItem>
      
    </layout:collection>
   <layout:space/>
  </logic:notEmpty>
  
  <logic:notEmpty name="beanDetImportEstado">
<br>
      <table width="80%" cellpadding='2' cellspacing='2' border='0' align='center'>
  <tr>
    <td colspan="5" class="TitleRow3">DETALLE TOTALES DE ORDEN </td>
  </tr>
  <tr>
    <td width="30%" class="TitleRow">N&uacute;mero de Orden: </td>
    <td width="50%" colspan="4" class="CellColRow2"><bean:write name="beanDetImportEstado" property="m_DetIDOrden" /></td>
  </tr>
  <tr>
    <td class="TitleRow">Referencia:</td>
    <td colspan="4" class="CellColRow2"><bean:write name="beanDetImportEstado" property="m_DetReferencia"/></td>
  </tr>
   <tr>
    <td class="TitleRow">Estado Orden:</td>
    <td colspan="4" class="CellColRow2"><bean:write name="beanDetImportEstado" property="m_DetEstado" /></td>
  </tr>
  <tr>
    <td rowspan="2" class="TitleRow4">Estado Detalle Orden </td>
    <td colspan="2" class="TitleRow4">Soles</td>
    <td colspan="2" class="TitleRow4">D&oacute;lares</td>
  </tr>
  <tr>
    <td width="18%" class="TitleRow4">Nro Registros </td>
    <td width="22%" class="TitleRow4">Monto</td>
    <td width="18%" class="TitleRow4">Nro Registros </td>
    <td width="22%" class="TitleRow4">Monto</td>
  </tr>
  <tr>
    <td class="TitleRowRight">Ingresados</td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumEnviados" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorSolesEnviado" /></td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumEnviadosDol" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorDolaresEnviado" /></td>
  </tr>
  <tr>
    <td class="TitleRowRight">Procesados</td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumProcesados" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorSolesProcesado" /></td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumProcesadosDol" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorDolaresProcesado" /></td>
  </tr>
  <tr>
    <td class="TitleRowRight">Errados</td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumErrados" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorSolesErrado" /></td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumErradosDol" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorDolaresErrado" /></td>
  </tr>
  <tr>
    <td class="TitleRowRight">Cobrados</td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumCobrados" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorSolesCobrados" /></td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumCobradosDol" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorDolaresCobrados" /></td>
  </tr>
  <tr>
    <td class="TitleRowRight">Cancelados</td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumCancelados" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorSolesCancelados" /></td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumCanceladosDol" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorDolaresCancelados" /></td>
  </tr>
  <tr>
    <td class="TitleRowRight">Cobrados Parcialmente</td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumCobradosParc" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorSolesCobradosParc" /></td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumCobradosParcDol" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorDolaresCobradosParc" /></td>
  </tr>
  <tr>
    <td class="TitleRowRight">Pendiente de Confirmaci&oacute;n</td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumPendConf" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorSolesPendConf" /></td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumPendConfDol" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorDolaresPendConf" /></td>
  </tr>
  <tr>
    <td class="TitleRowRight">Extornados</td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumExtornados" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorSolesExtornados" /></td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumExtornadosDol" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorDolaresExtornados" /></td>
  </tr>
    <tr>
    <td class="TitleRowRight">Archivados</td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumArchivados" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorSolesArchivados" /></td>
    <td class="CellColRowCenter"><bean:write name="beanDetImportEstado" property="m_NumArchivadosDol" /></td>
    <td class="CellColRow"><bean:write name="beanDetImportEstado" property="m_StrValorDolaresArchivados" /></td>
  </tr>
  <tr>
    <td class="TitleRow6">TOTALES</td>
    <td class="TitleRow4"><bean:write name="beanDetImportEstado" property="m_NumTotalSoles" /></td>
    <td class="TitleRow6"><bean:write name="beanDetImportEstado" property="m_valorSolesTotal" /></td>
    <td class="TitleRow4"><bean:write name="beanDetImportEstado" property="m_NumTotalDolares" /></td>
    <td class="TitleRow6"><bean:write name="beanDetImportEstado" property="m_valorDolaresTotal" /></td>
  </tr>
</table>
  </logic:notEmpty>
  
  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <br>
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
    </tr>
  </table>
  </logic:notEmpty>
  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center" >
  	  <tr align="right">
		<td colspan="7" class="CellColRow5">
			<img src="img/bt-volver.png" width="71" height="27" align="middle" onClick="javascript:volver();" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'"/>
		</td>
	  </tr>
  </table>
  <br>
  <div id="cargar"/>
</html:form>
</body>
</html>
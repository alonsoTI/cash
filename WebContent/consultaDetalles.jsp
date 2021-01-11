<%-- 
    Document   : consultaOrdenes
    Created on : 07-dic-2008, 12:35:14
    Author     : jwong, esilva
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
     var urlExportar = "";
      function buscar(){          
            var fecIni = document.getElementById("m_FecInicio").value;
            var fecFin = document.getElementById("m_FecFin").value;            
            if(!validaRangoFechasComprobantes(fecIni,fecFin)){
            	return;
            }            
            var fecActual = '<bean:write name="fechaActualComp"/>';
            var fecHace1Anho = '<bean:write name="fechaHace1Anho"/>';
            if(!validarLongitudReferencia()){
            	return;
            }                  
                    var tablamsj = document.getElementById("msjNoResult");
                    if(tablamsj != null){
                        tablamsj.style.display = "none";
                    }
                    var tabla = document.getElementById("msjBusq");
                    tabla.style.display = "";
                    
                    var tablaReporte = document.getElementById("tablaReporte");
                    if(tablaReporte != null){
                        tablaReporte.style.display = "none";
                    }
                    var tablaTotal = document.getElementById("tablaTotal");
                    if(tablaTotal != null){
                        tablaTotal.style.display = "none";
                    }//barraFunc
                    var elemOcultar = document.getElementById("barraFunc");
                    if(elemOcultar != null){
                        elemOcultar.style.display = "none";
                    }
                    elemOcultar = document.getElementById("paginado");
                    if(elemOcultar != null){
                        elemOcultar.style.display = "none";
                    }
                    var frm = document.forms[0];
                    frm.action = "comprobantes.do?do=buscarDetallesLinea";
                    frm.submit();
                //}
            //}
        }
        
        function validarLongitudReferencia() {
        	var referencia = document.getElementById("m_Referencia").value;
            if(referencia && referencia.length < 4){
            	alert("La longitud minima de la referencia es de 4 caracteres");
            	return false;
            }
            return true;
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
     function paginar(valor){       
        location.href = valor;
     }
     function exportarConsulta(url){
    	 var fecIni = document.getElementById("m_FecInicio").value;
         var fecFin = document.getElementById("m_FecFin").value;            
         if(!validaRangoFechasComprobantes(fecIni,fecFin)){
         	return;
         }
         if(!validarLongitudReferencia()){
         	return;
         }  
        urlExportar = url;
        obtenerCodFormato("comprobantes.do?do=verificarFormatoSalida&m_Servicio="+document.forms[0].m_Servicio.value);
     }
     function obtenerCodFormato(url){
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
        //obtenemos el xml
        var response = req.responseXML;
        if(response==null) alert("Ocurrió un error al procesar los datos");
        //obtenemos la respuesta
        var respuesta = response.getElementsByTagName("respuesta");
        var valor = respuesta[0].getAttribute("valor");
        if(valor == "NO"){
            alert("No se puede exportar la consulta porque el servicio \nno tiene asociado el formato correspondiente");
        }else{
            var frm = document.forms[0];
            frm.action = urlExportar;
            frm.submit();
        }
    }
    function mostrarEstado(){        
        var frm = document.forms[0];
        frm.action = "comprobantes.do?do=mostrarConsEstado&exportar=no";
        frm.submit();
    }
</script>
</logic:equal>  
</head>
<body>
<html:form action="comprobantes.do">
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
      <td valign="middle" align="left" class="Title"> <bean:message key="comprobantes.title.detalleord"/></td>
	</tr>
  </table>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center" >
  	<tr>
	  <td class='CellColRow'>Empresa:</td>
	  <td class='CellColRow2'>
          <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow" onchange="javascript:DoSubmit('comprobantes.do?do=cargarServicios&tipo=D');">
          <logic:notEmpty name="listaEmpresas">
              <html:options collection="listaEmpresas" property="cemIdEmpresa" labelProperty="demNombre"/>
          </logic:notEmpty>
        </html:select>
	  </td>

	  <td class='CellColRow'>Servicio:</td>
	  <td class='CellColRow2'>
          <html:select property="m_Servicio" styleId="m_Servicio" styleClass="CellColRow">          
          <logic:notEmpty name="listaServicios">
              <html:options collection="listaServicios" property="m_IdServicio" labelProperty="m_Descripcion"/>
          </logic:notEmpty>
        </html:select>
	  </td>
    </tr>

  	<tr>
	  <td class='CellColRow'>Desde:</td>
	  <td class='CellColRow2'>
		<html:text property="m_FecInicio" styleId="m_FecInicio" size="10" maxlength="10" styleClass="CellColRow" readonly="true"/>
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
		</script>
	  </td>
	  <td class='CellColRow'>Hasta:</td>
	  <td class='CellColRow2'>
		<html:text property="m_FecFin" styleId="m_FecFin" size="10" maxlength="10" styleClass="CellColRow" readonly="true"/>
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
		</script>
	  </td>
	</tr>
	<tr>
	  <td class='CellColRow'>Estado:</td>
	  <td class='CellColRow2'>   
        
        <html:select property="m_Estado" styleId="m_Estado" styleClass="CellColRow">
          <html:option value=""><bean:message key="global.todos"/></html:option>
          <logic:notEmpty name="listaEstadosDetOrd">
              <html:options collection="listaEstadosDetOrd" property="id.clfCode" labelProperty="dlfDescription"/>
          </logic:notEmpty>        
        </html:select>
	  </td>
      <td class='CellColRow'></td>
	  <td class='CellColRow2'></td>
       
	</tr>
	<tr>	  
      <td class='CellColRow'>Contrapartida:</td>
	  <td class='CellColRow2'>
		<html:text property="m_ConsContrapartida" styleId="m_Contrapartida" size="20" maxlength="20" styleClass="CellColRow2" onkeypress="return soloDescripcion(this, event)" onblur="gDescripcion(this)"/>
      </td>	  
	 <td class='CellColRow'>Referencia:</td>
	  <td class='CellColRow2'>
		<html:text property="m_ConsReferencia" styleId="m_Referencia" size="50" maxlength="50" styleClass="CellColRow2" onkeypress="return soloDescripcion(this, event)" onblur="gDescripcion(this)"/>
      </td>
	</tr>
	<tr align="right">
      <td colspan="4" class="CellColRow5">
	  	<img src="img/bt-buscar.png" align="middle" onClick="javascript:buscar();" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'"/>
      </td>
    </tr> 
 <logic:notEmpty name="bandResult">
       <tr id="barraFunc">
          <td colspan="4" align="right">
            <img src='img/bt-estado.png' alt="Consulta Estado" align='middle' onMouseOver="this.src='img/bt-estado2.png'" onMouseOut="this.src='img/bt-estado.png'" onClick="javascript:mostrarEstado();"/>
            &nbsp;&nbsp;&nbsp;
            <img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportarConsulta('comprobantes.do?do=exportarConsultaDetalles&accion=save&formato=excel');"/>
            <img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportarConsulta('comprobantes.do?do=exportarConsultaDetalles&accion=save&formato=txt');"/>
          </td>
        </tr>
        <tr align="center" id="paginado">
          <td  colspan="4">
            <img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('comprobantes.do?do=buscarDetallesLinea&tipoPaginado=P');"/>
            <img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('comprobantes.do?do=buscarDetallesLinea&tipoPaginado=A');"/>
            P&aacute;gina <bean:write name="beanPag" property="m_pagActual"/> de <bean:write name="beanPag" property="m_pagFinal"/>
            <img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('comprobantes.do?do=buscarDetallesLinea&tipoPaginado=S');"/>
            <img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('comprobantes.do?do=buscarDetallesLinea&tipoPaginado=U');"/>

          </td>
        </tr>

  </logic:notEmpty>
  </table> 
  <br>
    <logic:notEmpty name="listaResultPago">
        <div id="tablaReporte">
        <layout:collection name="listaResultPago" title="CONSULTA DE MOVIMIENTOS - PAGOS" styleClass="FORM" id="movimiento" align="center" sortAction="client">
              <layout:collectionItem title="Fecha de Proceso" property="m_FechaProceso" sortable="true" />
              <layout:collectionItem title="Id Orden" property="m_IdOrden" />
              <%--layout:collectionItem title="Nombre" property="m_Nombre"/--%>
              <layout:collectionItem title="Tipo Pago" property="m_FormaPago"/>
              <%--layout:collectionItem title="Cuenta Destino" property="m_CuentaDestino" /--%>
              <layout:collectionItem title="Contrapartida" property="m_Contrapartida" />
              <layout:collectionItem title="Referencia" property="m_Referencia" sortable="true" />
              <layout:collectionItem title="Valor Enviado">
                <p align="right" style="width:70px;">
                    <layout:write name="movimiento" property="m_ValorEnviado"/>
                </p>
              </layout:collectionItem>
              <layout:collectionItem title="Valor Procesado">
                <p align="right" style="width:70px;">
                    <layout:write name="movimiento" property="m_ValorPro"/>
                </p>
              </layout:collectionItem>
              <layout:collectionItem title="Valor Neto">
                <p align="right" style="width:70px;">
                    <layout:write name="movimiento" property="m_ValorNeto"/>
                </p>
              </layout:collectionItem>             
              <layout:collectionItem title="Moneda">
                <center>
                         <layout:write name="movimiento" property="m_Moneda"/>
                </center>
              </layout:collectionItem>
              <layout:collectionItem title="Estado" property="m_Estado" />              
              <layout:collectionItem title="Comprobante" property="m_Comprobante" />
              <layout:collectionItem title="Descripci&oacuten" property="m_Descripcion"/>
              <layout:collectionItem title="Desglose" property="m_Desglose" />
               <layout:collectionItem title="Nro.Ref. Banco" property="m_NroRefBanco" />
              <layout:collectionItem title="Cuenta" property="m_Cuenta" />
              <layout:collectionItem title="Nro.Doc" property="m_NroDocumento" />
              <layout:collectionItem title="Oficina" property="m_Oficina" />
              <layout:collectionItem title="Empresa" property="m_Empresa" />
              <layout:collectionItem title="Pais" property="m_Pais" />
              <layout:collectionItem title="Banco" property="m_Banco" />
            </layout:collection>
        </div>
    <layout:space/>
  </logic:notEmpty>
    <logic:notEmpty name="listaResultCobro">
        <div id="tablaReporte">
        <layout:collection name="listaResultCobro" title="CONSULTA DE MOVIMIENTOS - COBROS" styleClass="FORM" id="movimiento" align="center" sortAction="client">
              <layout:collectionItem title="Fecha de Proceso" property="m_FechaProceso" sortable="true" />
              <layout:collectionItem title="Id Orden" property="m_IdOrden" />
              <%--layout:collectionItem title="Nombre" property="m_Nombre" /--%>
              <layout:collectionItem title="Tipo Pago" property="m_FormaPago"/>              
              <layout:collectionItem title="Contrapartida" property="m_Contrapartida" />
              <layout:collectionItem title="Referencia" property="m_Referencia" sortable="true"/>
              <layout:collectionItem title="Valor Enviado">
                <p align="right" style="width:70px;">
                    <layout:write name="movimiento" property="m_ValorEnviado"/>
                </p>
              </layout:collectionItem>
              <layout:collectionItem title="Valor Procesado">
                <p align="right" style="width:70px;">
                    <layout:write name="movimiento" property="m_ValorPro"/>
                </p>
              </layout:collectionItem>
              <layout:collectionItem title="Valor Neto">
                <p align="right" style="width:70px;">
                    <layout:write name="movimiento" property="m_ValorNeto"/>
                </p>
              </layout:collectionItem>
              <layout:collectionItem title="Moneda">
                <center>
                         <layout:write name="movimiento" property="m_Moneda"/>
                </center>
              </layout:collectionItem>
              <layout:collectionItem title="Estado" property="m_Estado" />                           
              <layout:collectionItem title="Comprobante" property="m_Comprobante" />
              <layout:collectionItem title="Descripcion" property="m_Descripcion"/>
              <layout:collectionItem title="Desglose" property="m_Desglose" />
              <layout:collectionItem title="Cuenta" property="m_Cuenta" />
              <layout:collectionItem title="Nro.Ref. Banco" property="m_NroDocumento" />
              <layout:collectionItem title="Oficina" property="m_Oficina" />
              <layout:collectionItem title="Empresa" property="m_Empresa" />
              <layout:collectionItem title="Pais" property="m_Pais" />
              <layout:collectionItem title="Banco" property="m_Banco" />
            </layout:collection>
        </div>
        <layout:space/>
    </logic:notEmpty>
  <logic:notEmpty name="beanTotalConsulta">  
    <table width="100%" cellpadding='2' cellspacing='2' border='0' align='center'id="tablaTotal">
        <tr>
        	<td class="TitleRow6" width="20%">Cantidad de registros</td>
            <td class="TitleRow4" width="10%"><bean:write name="beanTotalConsulta" property="m_NumItems"/></td>
            <td class="TitleRow6" width="15%">Total Procesado:</td>            
            <td class="TitleRow6" width="10%">S/. <bean:write name="beanTotalConsulta" property="m_MontoVentSoles"/></td>
            <td class="TitleRow6" width="10%">$ <bean:write name="beanTotalConsulta" property="m_MontoVentDolares"/></td>
            <td class="TitleRow6" width="15%">Total Neto:</td>
            <td class="TitleRow6" width="10%">S/.<bean:write name="beanTotalConsulta" property="m_MontoNetoSoles"/></td>
            <td class="TitleRow6" width="10%">$ <bean:write name="beanTotalConsulta" property="m_MontoNetoDolares"/></td>
        </tr>
    </table>
  </logic:notEmpty> 
  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <table id="msjNoResult" cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
    </tr>
  </table>
  </logic:notEmpty>
  <div id="comp0" style="display:none">FEC</div>
  <div id="comp4" style="display:none">ANS</div>
  
  <table id="msjBusq" cellpadding='2' cellspacing='2' border='0' align='center' width='90%' style="display:none">
    <tr>
        <td class='TitleRow3'>B&uacute;squeda en Proceso ...</td>
    </tr>
  </table>
  </logic:equal>
</html:form>
</body>
</html>
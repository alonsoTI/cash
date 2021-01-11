<%-- 
    Document   : pagoAgua
    Created on : 05-mar-2009, 14:42:51
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">

  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>
  <script type="text/javascript" src="config/javascript.js"></script>
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
    <!--
	function buscar(){
      var frm = document.forms[0];
      if(frm.m_NumSuministro.value!=""){
        frm.action = "pagoServicio.do?do=buscarPagoAgua";
        frm.submit();
      }
	  else{
        frm.m_NumSuministro.focus();
        alert("Debe ingresar Número de Suministro para continuar");
      }
    }
    
    function continuar(){
      //debemos validar que se haya seleccionado algun recibo para pagarlo
      if(validar()){
        var frm = document.forms[0];
        frm.action = "pagoServicio.do?do=cargarDetallePagoAgua";
        frm.submit();
      }
    }

    //validamos los campos del formulario antes de enviar a procesar
    function validar(){
        var  frm = document.forms[0];
        //verificamos la seleccion de agun recibo
        if(frm.m_NumSuministro.value=="") {
            alert("Debe ingresar Número de Suministro para continuar");
            return false;
        }
        else {
            var selRecibo = false;
            for (i = 0; i < frm.elements.length; i++){
                if(frm.elements[i].type=="checkbox" && frm.elements[i].checked==true ){
                    selRecibo = true;
                    break;
                }
            }
            if(selRecibo==false){
                alert("Debe seleccionar algun recibo para continuar");
                return false;
            }
        }
        return true;
    }
    
    function cambiaServicio(){
        var codeServicio = document.getElementById("m_Servicio").value;
        var codes = codeServicio.split(";");
        var labelNuSuministro = codes[3];
        //colocamos la etiqueta que corresponde
        document.getElementById("labelNumSuministro").innerHTML = labelNuSuministro + ":";
        document.getElementById("labelSuministro").value = labelNuSuministro;
    }
    function ocultarResultado(){
        //al cambiar tenemos que ocultar el boton continuar y la busqueda anterior(en caso exista)
        var btnContinuar = document.getElementById("btnContinuar");
        if(btnContinuar!=null){
            btnContinuar.style.display = "none";
        }
        document.getElementById("tabResultado").style.display = "none";
    }
	-->
</script>

</head>
<body onload="javascrip:cambiaServicio();">
<html:form action="pagoServicio.do">
  <html:hidden property="m_CodEmpProveedor" styleId="m_CodEmpProveedor" />
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      <td valign="middle" align="left" class="Title">Pago del Servicio de Agua</td>
	</tr>
  </table>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
    <tr class="TitleRow5">
        <td width="20%"><bean:message key="transferencias.lbl.titutar"/></td>
        <td width="30%">
            <bean:write name="usuarioActual" scope="session" property="m_Nombre"/>&nbsp;<bean:write name="usuarioActual" scope="session" property="m_Apellido"/>
        </td>
        <td width="20%"><bean:message key="transferencias.lbl.tarjeta"/></td>
        <td width="30%">
            <bean:write name="usuarioActual" scope="session" property="m_NumTarjeta"/>
        </td>
    </tr>
    
    <tr class="TitleRow5" <logic:lessEqual name="nroEmpresas" value="1">style="display:none"</logic:lessEqual> >
      <td>Empresa:</td>
      <td>
        <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow">
          <logic:notEmpty name="listaEmpresas">
              <html:options collection="listaEmpresas" property="idEmpresaCodCliente" labelProperty="demNombre"/>
          </logic:notEmpty>
        </html:select>
      </td>
    </tr>
  </table>
  <br/>
  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
    <tr>
      <td class="TitleRow2">Servicio:</td>
      <td class="CellColRow2" >
        <html:select property="m_Servicio" styleId="m_Servicio" styleClass="CellColRow8" onchange="javascript:cambiaServicio();javascript:ocultarResultado()">
            <logic:notEmpty name="listaServicio">
                <html:options collection="listaServicio" property="m_TipoCodigoEntidadServicio" labelProperty="m_Nombre"/>
            </logic:notEmpty>
        </html:select>
	  </td>
    </tr>
    <tr>
      <input type="hidden" id="labelSuministro" name="labelSuministro" />
      <td class="TitleRow2"><div id="labelNumSuministro"/></td>
      <td class="CellColRow2" >
        <html:text property="m_NumSuministro" styleId="m_NumSuministro" size="40" maxlength="15" styleClass="CellColRow8" onkeypress="numero()" onblur="val_int(this)"/>
	  </td>
    </tr>

	<tr align="right">
      <td>&nbsp;</td>
      <td>
        <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:buscar();"/>
        <logic:notEmpty name="listaResult">
            <img src="img/bt-continuar.png" align="middle" id="btnContinuar" onMouseOver="this.src='img/bt-continuar2.png'" onMouseOut="this.src='img/bt-continuar.png'" onClick="javascript:continuar();"/>
        </logic:notEmpty>
	  </td>
    </tr>
  </table>

  <%--resultados de la busqueda--%>
  <div id="tabResultado">
  <logic:notEmpty name="listaResult">
      <layout:collection name="listaResult" title="DETALLE DE RECIBOS POR PAGAR" styleClass="FORM" id="deuda" sortAction="client" width="100%" align="center" indexId="h" >
        <layout:collectionItem title="N&uacute;mero Recibo" property="m_NumRecibo" sortable="true" />
        <layout:collectionItem title="Nombre Cliente" property="m_NombreCliente" sortable="true" />
        <layout:collectionItem title="Fecha Emisión" property="m_FechaEmision" sortable="true" />
        <layout:collectionItem  title="Moneda" property="m_Moneda" />
        <layout:collectionItem title="Importe" sortable="true">
            <div align="right">
                <bean:write name='deuda' property='m_Importe' />
            </div>
        </layout:collectionItem>

        <layout:collectionItem title="Pagar">
            <center>
                <input type="checkbox" id="m_chkRecibo" name="m_chkRecibo" value="<bean:write name='deuda' property='parametrosCadena' />" />
            </center>
        </layout:collectionItem>
      </layout:collection>
      <layout:space/>
  </logic:notEmpty>
  </div>
  
  <html:hidden property="m_NumSuministroIni" styleId="m_NumSuministroIni"/>
  <html:hidden property="m_IdEmpCodCliente" styleId="m_IdEmpCodCliente"/>
  <!--mensaje de error en caso existan-->
  <logic:notEmpty name="mensaje">
  <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
    <tr>
        <td class='TitleRow3'><bean:write name="mensaje"/></td>
    </tr>
  </table>
  </logic:notEmpty>
</html:form>
</body>
</html>
<%-- 
    Document   : consultaMovHistorico
    Created on : 04-dic-2008, 9:44:38
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
	function exportar(){
		location.href = "exportarExcel.html";
	}	
    
    function buscar(){
        var frm = document.forms[0];
        if(frm.m_IdServEmp.value != null && ""!=frm.m_IdServEmp.value){
            frm.action = "administracion.do?do=buscarProveedor";
            frm.submit();
        }else{
            alert("No existe servicios afiliados");
        } 
	}
	
	function nuevo(){
		var frm = document.forms[0];
        if(frm.m_IdServEmp.value != null && ""!=frm.m_IdServEmp.value){
            frm.action = "proveedor.do?do=nuevoProveedor&m_IdServEmp="+frm.m_IdServEmp.value+"&m_Empresa="+frm.m_Empresa.value;
            frm.submit();
        }else{
            alert("No existe servicios afiliados");
        }
	}
    function obtenerServicios(){        
        var frm = document.forms[0];
        frm.action = "administracion.do?do=cargarServiciosProveedor";
        frm.submit();
    }
    
</script>

</head>
<body>
<html:form action="administracion.do">
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
  <table width="100%" CELLSPACING="0" CELLPADDING="4">
    <tr>
      <td valign="middle" align="left" class="Title"><bean:message key="administracion.title.proveedores"/></td>
    </tr>
  </table>
  
  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
    <tr>
      <td class='CellColRow'>Empresa:</td>
	  <td class='CellColRow2'>
        <html:select property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow" onchange="obtenerServicios();">
          <logic:notEmpty name="listaEmpresas">
              <html:options collection="listaEmpresas" property="cemIdEmpresa" labelProperty="demNombre"/>
          </logic:notEmpty>
        </html:select>
	  </td>
      <td class='CellColRow'>Servicio:</td>
      <td class='CellColRow2'>
            <html:select property="m_IdServEmp" styleId="m_IdServEmp" styleClass="CellColRow2">
                <logic:notEmpty name="listaServicios">
                    <html:options collection="listaServicios" property="m_IdServicio" labelProperty="m_Descripcion"/>
                </logic:notEmpty>
            </html:select>
      </td>
    </tr>
    
    <tr align="right">
      <td colspan="4" class="CellColRow5">
        <img src="img/bt-buscar.png" align="middle" onMouseOver="this.src='img/bt-buscarB.png'" onMouseOut="this.src='img/bt-buscar.png'" onClick="javascript:buscar();"/>
        <img src="img/bt-nuevo.png" align="middle" onClick="javascript:nuevo();" onMouseOver="this.src='img/bt-nuevo2.png'" onMouseOut="this.src='img/bt-nuevo.png'"/>
      </td>
    </tr>
  </table>
  
  <logic:notEmpty name="listaProveedor">
    <layout:collection name="listaProveedor" title="DETALLE DE PROVEEDORES" styleClass="FORM" id="proveedor" sortAction="client" width="100%" align="center">
      <layout:collectionItem title="ID Proveedor">
        <center>
        <layout:link href="proveedor.do?do=cargarModificarProveedor" name="proveedor" property="parametrosUrl">
            <layout:write name="proveedor" property="id.ceeidEntidad"/>
        </layout:link>
        </center>
      </layout:collectionItem>    

      <layout:collectionItem title="Nombre/Raz&oacute;n Social" property="deenombre" sortable="true"/>
      <layout:collectionItem title="Tipo Documento" property="descripcionTipoDocumento" sortable="true"/>
      <layout:collectionItem title="Nro. Documento" property="neenumDocumento" sortable="true"/>
      <layout:collectionItem title="Forma Pago" property="descripcionTipoPago" />
  </layout:collection>
  <div id="comp1" style="display:none">ANS</div>
  <div id="comp2" style="display:none">ANS</div>
  <div id="comp3" style="display:none">NUM</div>
  <div id="comp4" style="display:none">ANS</div>
  </logic:notEmpty>
  <logic:notEmpty name="mensaje"><!--jmoreno 24/09/09-->
  <br>
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
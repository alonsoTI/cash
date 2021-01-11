<%-- 
    Document   : transfCtaPropia
    Created on : 18/12/2008, 10:16:21 AM
    Author     : esilva
    Modificado por andy 16/06/2011
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
    <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
    <META HTTP-EQUIV="Expires" CONTENT="0">

    <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
    .colorCbx{
        background-color: #FFFFFF;
    }
    </style>
    <title>Transferencia entre cuentas propias</title>
    <link href="css/Styles.css" rel="stylesheet" type="text/css">
    <link href="css/consolidated_common.css" rel="stylesheet" type="text/css">
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
    <script type="text/javascript" src="js/livevalidation_standalone.js"></script>
    <script language="JavaScript" type="text/javascript">
        
        var valMonto;
        function isValid(frm, from, strTarget) {            
            var ctaCargo = document.forms[0].m_CtaCargo.options[document.forms[0].m_CtaCargo.selectedIndex].value;
            var ctaAbono = document.forms[0].m_CtaAbono.options[document.forms[0].m_CtaAbono.selectedIndex].value;
            if (ctaCargo == ctaAbono){
                alert ("Cuenta origen y destino deben ser diferentes");
                return false;
            }else{
                var val = new Array();
                val[0] = valMonto;                
                if (LiveValidation.massValidate(val)){
                    if (isValidForm2(frm,from,strTarget)){
                        return true;
                    }
                }
                return false;
            }           
        }
        
    	function cambiarMoneda() {
         	var campo = document.forms[0].m_CtaCargo;
         	campo = campo.options[campo.selectedIndex].text;
         	var  indic = campo.indexOf(":", 0);
         	var cadena = campo.substring(indic+2,indic+5);
         	var cbomoneda = document.forms[0].m_Moneda;
         	for(i=0;i < cbomoneda.length;i++){
             	if(cbomoneda.options[i].text == cadena){
                 	cbomoneda.selectedIndex = i;
                 	i = cbomoneda.length;
                 	document.getElementById("moneda").innerHTML=cadena;
             	}
         	}
        	cambiarOpcionesCbos();//jmoreno 25/09/09
    	}  	 
    	var indiceSelec = -1;    
    	function cambiarOpcionesCbos(){
        	var cbxCtaCargo = document.forms[0].m_CtaCargo;
        	var cbxCtaAbono = document.forms[0].m_CtaAbono;
        	if(cbxCtaCargo.length > 1){
            	if(indiceSelec >= 0){
                	theOption = new Option(cbxCtaCargo.options[indiceSelec].text , cbxCtaCargo.options[indiceSelec].value);
                	theOption.className = "colorCbx";
                	cbxCtaAbono.options[cbxCtaAbono.length]= theOption;
            	}
            	indiceSelec = cbxCtaCargo.selectedIndex;
            	var valorSelec = cbxCtaCargo.options[indiceSelec].value;
            	for (ii=0;ii<cbxCtaAbono.length;ii++){
	                if(cbxCtaAbono.options[ii].value == valorSelec){
    	                cbxCtaAbono.options[ii] = null;
                	}
            	}
        	}
    	}
       
    </script>
</head>
<body onload="cambiarMoneda();">
<html:form action="transferencias.do" onsubmit="return false;">
    <logic:notEqual name="tf_habil" value="1">
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
    <logic:equal name="tf_habil" value="1">
    <table width="100%" CELLSPACING="0" CELLPADDING="4">
        <tr>
            <td valign="middle" align="left" class="Title"><bean:message key="transferencias.title.ctapropias"/></td>
            
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr class="TitleRow5">
            <td width="20%"><bean:message key="transferencias.lbl.titutar"/></td>
            <td width="30%"><bean:write name="usuarioActual" scope="session" property="m_Nombre"/>&nbsp;<bean:write name="usuarioActual" scope="session" property="m_Apellido"/>
            </td>
            <td width="20%"><bean:message key="transferencias.lbl.tarjeta"/></td>
            <td width="30%"><bean:write name="usuarioActual" scope="session" property="m_NumTarjeta"/>
            
            
            </td>
        </tr>
        <tr class="TitleRow5">
            <td width="20%" <logic:notEqual name="bLista" value ="1">style="display: none"</logic:notEqual>><bean:message key="global.empresa"/></td>
            <td width="30%" <logic:notEqual name="bLista" value ="1">style="display: none"</logic:notEqual>>
                <html:select name="tf_transf_cta" property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow2" onchange="javascript:DoSubmit('transferencias.do?do=cargarServicios&tipo=1');">
                    <html:options collection="tf_listaempresas" property="cemIdEmpresa" labelProperty="demNombre" style="background-color: #FFFFFF" />
                </html:select>
            </td>
            <td width="20%" style="display: none">
                <html:select name="tf_transf_cta" property="m_Servicio" styleId="m_Servicio" styleClass="CellColRowE" >
                    <logic:iterate id="opt" name="listaservicios">
                        <bean:define name="opt" property="m_IdServicio" id="idservicios" type="java.lang.String" />
                        <logic:equal name ="opt" property="estado" value="1">
                            <% String idservicio = "" + idservicios + "*1"; %>
                            <html:option value="<%= idservicio %>" ><bean:write name="opt" property="m_Descripcion" /></html:option>
                        </logic:equal>
                        <logic:notEqual name ="opt" property="estado" value="1">
                            <% String idservicio = "" + idservicios + "*0"; %>
                            <html:option value="<%= idservicio %>" style="background-color: #FFFFFF" ><bean:write name="opt" property="m_Descripcion" /></html:option>
                        </logic:notEqual>
                    </logic:iterate>
                </html:select>
            </td>
        </tr>
        <tr class="CellColRow5">
            <td colspan="4" height="10px"></td>
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr class="CellColRow2">
            <td width="20%" style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.cargo"/></td>
            <td width="80%">
                <html:select name="tf_transf_cta" property="m_CtaCargo" styleId="m_CtaCargo" styleClass="CellColRow2" onchange="cambiarMoneda();">
                    <logic:iterate id="lcargo" name="tf_listaccounts">
                        <bean:define name="lcargo" property="m_AccountCode" id="idacccargo" type="java.lang.String" />
                        <% String idcargo = "" + idacccargo + ""; %>
                        <html:option value="<%= idcargo %>" style="background-color: #FFFFFF" >&nbsp;<bean:write name="lcargo" property="m_AccountDescription" />&nbsp;<bean:write name="lcargo" property="m_AccountCode" />&nbsp;-&nbsp;Saldo:&nbsp;<bean:write name="lcargo" property="m_Currency"/>&nbsp;<bean:write name="lcargo" property="m_AvailableBalance"/></html:option>
                    </logic:iterate>
                    <!--<html:options collection="tf_listaccounts" property="m_AccountCode" labelProperty="m_AccountCode" />-->
                </html:select>
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.abono"/></td>
            <td style="background-color: #FFFFE6">
                <html:select name="tf_transf_cta" property="m_CtaAbono" styleId="m_CtaAbono" styleClass="CellColRow2" >
                    <logic:iterate id="labono" name="tf_listaccounts">
                        <bean:define name="labono" property="m_AccountCode" id="idaccabono" type="java.lang.String" />
                        <% String idabono = "" + idaccabono + ""; %>
                        <html:option value="<%= idabono %>" style="background-color: #FFFFFF" >&nbsp;<bean:write name="labono" property="m_AccountDescription" />&nbsp;<bean:write name="labono" property="m_AccountCode" />&nbsp;-&nbsp;Saldo:&nbsp;<bean:write name="labono" property="m_Currency"/>&nbsp;<bean:write name="labono" property="m_AvailableBalance"/></html:option>
                    </logic:iterate>
                    <!--<html:options collection="tf_listaccounts" property="m_AccountCode" labelProperty="m_AccountCode" />-->
                </html:select>
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.monto"/></td>
            <td>
                <html:text name="tf_transf_cta" property="m_Monto" styleId="m_Monto" maxlength="10" size="50%" styleClass="CellColRow2" style="background-color: #FFFFFF" onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)"/>                
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.moneda"/></td>
            <td style="background-color: #FFFFE6"><div id="moneda"></div></td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.referencia"/></td>
            <td>
                <html:text name="tf_transf_cta" property="m_Referencia" styleId="m_Referencia" maxlength="50" size="50%" styleClass="CellColRow2" style="text-transform:uppercase;background-color: #FFFFFF" onkeypress="return soloDescripcionBasic(this,event);" onblur="gDescripcionBasic(this);" />
            </td>
        </tr>
        <tr><td class="CellColRow2" colspan="2" style="background-color: #FFFFFF">&nbsp;</td></tr>
        <tr>
          <td colspan="2" align="center">
            <img src="img/bt-continuar.png"  onMouseOver="this.src='img/bt-continuar2.png'" onMouseOut="this.src='img/bt-continuar.png'" onClick="javascript:isValid(document.forms[0],'TransfCtaPropia','transferencias.do?do=validar&tipo=1&m_Moneda='+document.forms[0].m_Moneda.value);"/>
          </td>
        </tr>       
    </table>
    <div style="visibility:hidden">
        <html:select name="tf_transf_cta" property="m_Moneda" styleId="m_Moneda" disabled="true">
                <html:options collection="tf_listamoneda" property="id.clfCode" labelProperty="dlfDescription" />
        </html:select>
    </div>
    </logic:equal>
    <html:hidden name="varIdTipoTransf" property="m_IdTipoTransf" value="11" />
</html:form>
<%--<form name="frm2"> //esto esta mal
    <div style="visibility:hidden">
        <html:select property="m_MonedaInv" styleId="m_MonedaInv" disabled="true">
                        <html:options collection="tf_listamoneda" property="id.clfCode" labelProperty="dlfDescription" />
                </html:select>
    </div>
</form>--%>


<script type="text/javascript">
    valMonto= new LiveValidation('m_Monto', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valMonto.add(Validate.Presence, {failureMessage: " El monto a transferir es requerido"});
    valMonto.add(Validate.MontoMin, {failureMessage: " El monto debe ser mayor a 0.00"});
    valMonto.add(Validate.Format, {pattern: /^[0-9]+(\.[0-9]+)?$/i,failureMessage: " Debe ingresar un monto válido"});
  
</script>
</body>
</html>

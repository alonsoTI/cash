<%--
    Document   : transfInterBanco
    Created on : 18/12/2008, 10:16:21 AM
    Author     : esilva
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
    </style>
    <title>Transferencias Interbancarias</title>
    <link href="css/Styles.css" rel="stylesheet" type="text/css">
    <link href="css/consolidated_common.css" rel="stylesheet" type="text/css">
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
    <script type="text/javascript" src="js/livevalidation_standalone.js"></script>
    <script language="JavaScript" type="text/javascript">
        var tipoDoc;
        //='01;DNI';
        function isValid(frm, from, strTarget) {
            var val = new Array();            
            val[0] = valNroDoc;            
            if( tipoDoc == '01;DNI'){
            	 val[1] = valApePat;
                 val[2] = valApeMat;
                 val[3] = valNombre;
                 val[4] = valDireccion;
                 val[5] = valTelef;
                 val[6] = valMonto;
                 val[7] = valCtaAbonoEntidad;
                 val[8] = valCtaAbonoOficina;
                 val[9] = valCtaAbonoCuenta;
                 val[10] = valCtaAbonoControl;                 
            }
            else{                 
            	 val[1] = valRazon;                 
                 val[2] = valDireccion;
                 val[3] = valTelef;
                 val[4] = valMonto;
                 val[5] = valCtaAbonoEntidad;
                 val[6] = valCtaAbonoOficina;
                 val[7] = valCtaAbonoCuenta;
                 val[8] = valCtaAbonoControl;    
            }            
                   
            if (LiveValidation.massValidate(val)){
                 if(validarCodBanco()){//jmoreno 24/09/09
                    if (isValidForm2(frm,from,strTarget)){
                        return true;
                    }
                 }
                
            }
            return false;
        }
    function validarCodBanco() {//jmoreno 24/09/09
        var m_nombreBanco = document.forms[0].m_NombreBanco.value;
        if(m_nombreBanco == "BANCO NO REGISTRADO"){           
            alert("Ingrese un Código de Banco válido");
            document.forms[0].m_CtaAbonoEntidad.focus();
            document.forms[0].m_CtaAbonoEntidad.select();
            return false;
        }else{
            return true;
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
    }
    function obtenerBanco(campo){
        campo.value=campo.value.toString().replace(/([^0-9])/g,"");
        var codigo = campo.value;
        if(codigo!=null && codigo!=""){            
            obtenerBancoAjax("transferencias.do?do=obtenerNombreBco&codigo=0"+codigo);
        }else
        {
            document.forms[0].m_NombreBanco.value = "";
        }
    }
    
    function obtenerBancoAjax(url){
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
        document.forms[0].m_NombreBanco.value = valor;
    }

    function ocultarFila(valor) {        
          var RUC = '02;RUC';          
          var dis = '';
          tipoDoc=valor;
          tab=document.getElementById('datos');
          if( RUC == valor){
        	  dis= 'none';
        	  tab.getElementsByTagName('tr')[5].style.display='';
        	  tab.getElementsByTagName('tr')[6].style.display=dis;
        	  tab.getElementsByTagName('tr')[7].style.display=dis;
        	  tab.getElementsByTagName('tr')[8].style.display=dis;
          }  else{
        	  dis= '';
        	  tab.getElementsByTagName('tr')[5].style.display='none';
        	  tab.getElementsByTagName('tr')[6].style.display=dis;
        	  tab.getElementsByTagName('tr')[7].style.display=dis;
        	  tab.getElementsByTagName('tr')[8].style.display=dis;
          }
    	  
    	}
    function iniciarFormulario(){
        cambiarMoneda();
        tab=document.getElementById('datos');
        if( tipoDoc == '01;DNI' ){
      	  	tab.getElementsByTagName('tr')[5].style.display='none';            
        }else{
        	  tab.getElementsByTagName('tr')[6].style.display='none';
        	  tab.getElementsByTagName('tr')[7].style.display='none';
        	  tab.getElementsByTagName('tr')[8].style.display='none';
        }        
    }	    
    </script>
</head>
<body onload="iniciarFormulario();">
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
    <table width="100%" CELLSPACING="0" CELLPADDING="4" >
        <tr>
            <td valign="middle" align="left" class="Title"><bean:message key="transferencias.title.ctainterbco"/></td>            
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
                <html:select name="tf_transf_cta" property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow2" onchange="javascript:DoSubmit('transferencias.do?do=cargarServicios&tipo=3');">
                    <html:options collection="tf_listaempresas" property="cemIdEmpresa" labelProperty="demNombre" style="background-color: #FFFFFF"/>
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
    <table width="100%" cellpadding="2" cellspacing="2" border="0" id="datos">
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
                <html:text name="tf_transf_cta" property="m_CtaAbonoEntidad" styleId="m_CtaAbonoEntidad" maxlength="3" size="3" styleClass="CellColRow2" style="background-color: #FFFFFF" onkeypress="numero()" onblur="obtenerBanco(this)" />-
                <html:text name="tf_transf_cta" property="m_CtaAbonoOficina" styleId="m_CtaAbonoOficina" maxlength="3" size="3" styleClass="CellColRow2" style="background-color: #FFFFFF" onkeypress="numero()" onblur="val_int(this)"/>-
                <html:text name="tf_transf_cta" property="m_CtaAbonoCuenta" styleId="m_CtaAbonoCuenta" maxlength="12" size="12" styleClass="CellColRow2" style="background-color: #FFFFFF" onkeypress="numero()" onblur="val_int(this)"/>-
                <html:text name="tf_transf_cta" property="m_CtaAbonoControl" styleId="m_CtaAbonoControl" maxlength="2" size="2" styleClass="CellColRow2" style="background-color: #FFFFFF" onkeypress="numero()" onblur="val_int(this)"/>                
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF">Banco:</td>
            <td><html:text name="tf_transf_cta" property="m_NombreBanco" styleId="m_NombreBanco" maxlength="30" styleClass="CellColRow2" style="background-color: #FFFFFF" size="30" readonly="true"/></td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF">Tipo Documento Beneficiario:</td>
            <td style="background-color: #FFFFE6">
                <%--html:select property="m_TipoDocBenef" name="tf_transf_cta"  styleId="m_TipoDocBenef" styleClass="CellColRow2"--%>
                <html:select property="m_IdTipoDocBenef" name="tf_transf_cta"  
                	styleId="m_IdTipoDocBenef" styleClass="CellColRow2"  
                	 onchange="ocultarFila(this.value);">
                    <%--logic:iterate id="ldoc" name="tf_listadocumento"-->
                        <%--
                        <bean:define name="ldoc" property="id.clfCode" id="idcode" type="java.lang.String" />
                        <bean:define name="ldoc" property="dlfDescription" id="idname" type="java.lang.String" />
                        <% String iddoc = "" + idcode +";"+idname +""; %>
                        <html:option value="<%= iddoc %>" style="background-color: #FFFFFF" >&nbsp;<bean:write name="ldoc" property="dlfDescription" /></html:option>
                        --%>
                        <html:options collection="tf_listadocumento" property="id_Description" labelProperty="dlfDescription" style="background-color: #FFFFFF"/>
                    <%--/logic:iterate-->
                    <%--<html:options collection="tf_listadocumento" property="id.clfCode" labelProperty="dlfDescription" style="background-color: #FFFFFF"/>--%>
                </html:select>
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF">Nro. Documento Beneficiario:</td>
            <td>
                <html:text name="tf_transf_cta" property="m_NumDocBenef" styleId="m_NumDocBenef" maxlength="15" styleClass="CellColRow2" style="background-color: #FFFFFF" onkeypress="numero()" onblur="val_int(this)"/>
            </td>
        </tr>
        
         <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF">Razon Social:</td>
            <td style="background-color: #FFFFE6">
                <html:text name="tf_transf_cta" property="m_RazonSocial" styleId="m_RazonSocial" maxlength="30"  size="50%" style = "text-transform:uppercase;background-color: #FFFFFF" styleClass="CellColRow2" onkeypress="return soloNombreBasico(this,event);" onblur="gNombreBasico(this);"/>
            </td>
        </tr>
        
  
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF">Ap. Paterno Beneficiario:</td>
            <td style="background-color: #FFFFE6">
                <html:text name="tf_transf_cta" property="m_ApePatBenef" styleId="m_ApePatBenef" maxlength="30"  size="50%" style = "text-transform:uppercase;background-color: #FFFFFF" styleClass="CellColRow2" onkeypress="return soloNombreBasico(this,event);" onblur="gNombreBasico(this);"/>
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF">Ap. Materno Beneficiario:</td>
            <td >
                <html:text name="tf_transf_cta" property="m_ApeMatBenef" styleId="m_ApeMatBenef" maxlength="30"  size="50%" styleClass="CellColRow2" style="text-transform:uppercase;background-color: #FFFFFF" onkeypress="return soloNombreBasico(this,event);" onblur="gNombreBasico(this);"/>
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF">Nombres Beneficiario:</td>
            <td style="background-color: #FFFFE6">
                <html:text name="tf_transf_cta" property="m_NombreBenef" styleId="m_NombreBenef" maxlength="30" size="50%" styleClass="CellColRow2" style="text-transform:uppercase;background-color: #FFFFFF" onkeypress="return soloNombreBasico(this,event);" onblur="gNombreBasico(this);"/>
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF">Dirección Beneficiario:</td>
            <td >
                <html:text name="tf_transf_cta" property="m_DireccionBenef" styleId="m_DireccionBenef" maxlength="50" size="50%" styleClass="CellColRow2" style="text-transform:uppercase;background-color: #FFFFFF" onkeypress="return soloDescripcionBasic(this,event);" onblur="gDescripcionBasic(this);"/>
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF">Teléfono Beneficiario:</td>
            <td style="background-color: #FFFFE6">
                <html:text name="tf_transf_cta" property="m_TlfBenef" styleId="m_TlfBenef" maxlength="18" styleClass="CellColRow2" style="background-color: #FFFFFF" onkeypress="numero()" onblur="val_int(this)"/>
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.monto"/></td>
            <td>
                <html:text name="tf_transf_cta" property="m_Monto" styleId="m_Monto" maxlength="10" styleClass="CellColRow2" style="background-color: #FFFFFF" onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)"/>
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.moneda"/></td>
            <td style="background-color: #FFFFE6"><div id="moneda"></div></td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.referencia"/></td>
            <td>
                <html:text name="tf_transf_cta" property="m_Referencia" styleId="m_Referencia" maxlength="50" size="50%" styleClass="CellColRow2" style="text-transform:uppercase;background-color: #FFFFFF" onkeypress="return soloDescripcionBasic(this,event);" onblur="gDescripcionBasic(this);"/>
            </td>
        </tr>

        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.flagcliente"/></td>
            <td style="background-color: #FFFFE6">
                <logic:equal name="tf_transf_cta" property="m_FlagCliente" value="S" >
                    <input type="checkbox" name="m_FlagCliente" id="m_FlagCliente" style="background-color: #FFFFE6" value="S" checked />
                </logic:equal>
                <logic:notEqual name="tf_transf_cta" property="m_FlagCliente" value="S" >
                    <input type="checkbox" name="m_FlagCliente" id="m_FlagCliente" style="background-color: #FFFFE6" value="S" />
                </logic:notEqual>
            </td>
        </tr>
        
        <tr> <td style="background-color: #FFFFFF"></td><td class="CellColRowMensaje" style="background-color: #FFFFFF">&nbsp;<bean:message key="transferencias.msg.montomaxtransf"/></td></tr>
        <tr>
          <td colspan="2" align="center">
            <img src="img/bt-continuar.png" align="middle" onMouseOver="this.src='img/bt-continuar2.png'" onMouseOut="this.src='img/bt-continuar.png'" onClick="javascript:isValid(document.forms[0],'TransfCtaInterBco','transferencias.do?do=validar&tipo=3&m_Moneda='+document.forms[0].m_Moneda.value);"/>
          </td>
        </tr>
    </table>
    
     <div style="visibility:hidden">
        <html:select name="tf_transf_cta" property="m_Moneda" styleId="m_Moneda" disabled="true">
                <html:options collection="tf_listamoneda" property="id.clfCode" labelProperty="dlfDescription" />
        </html:select>
    </div>
    </logic:equal>
</html:form>
<script type="text/javascript">
    var valNroDoc = new LiveValidation('m_NumDocBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valNroDoc.add(Validate.Presence, {failureMessage: " El número de documento es requerido"});
    valNroDoc.add(Validate.Custom, {against: function(value,args){
                  var m_valNroDoc = document.forms[0].m_NumDocBenef.value;
                  var m_valTipoDoc = document.forms[0].m_IdTipoDocBenef.value;
                  if(m_valTipoDoc == "01;DNI"){
                      if(m_valNroDoc.length < 8 || m_valNroDoc.length > 8 ){
                          return false;
                      }else{
                          return true;
                      }
                  }else if(m_valTipoDoc == "02;RUC"){
                      if(m_valNroDoc.length < 11 || m_valNroDoc.length > 11 ){
                          return false;
                      }else{
                          return true;
                      }
                  }
                },
                args:{},
                failureMessage: "Ingrese un número de documento válido"
    });
    tipoDoc = document.forms[0].m_IdTipoDocBenef.value;
    var valRazon = new LiveValidation('m_RazonSocial', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valRazon.add(Validate.Presence, {failureMessage: " La razon social del beneficiario es requerido"});
    var valApePat = new LiveValidation('m_ApePatBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valApePat.add(Validate.Presence, {failureMessage: " El apellido paterno del beneficiario es requerido"});
    var valApeMat = new LiveValidation('m_ApeMatBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valApeMat.add(Validate.Presence, {failureMessage: " El apellido materno del beneficiario es requerido"});
    var valNombre = new LiveValidation('m_NombreBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valNombre.add(Validate.Presence, {failureMessage: " El nombre del beneficiario es requerido"});
    var valDireccion= new LiveValidation('m_DireccionBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valDireccion.add(Validate.Presence, {failureMessage: " La dirección del beneficiario es requerido"});
    var valTelef= new LiveValidation('m_TlfBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valTelef.add(Validate.Custom, {against: function(value,args){
                  var m_valTlfBenef = document.forms[0].m_TlfBenef.value;

                  if(m_valTlfBenef.length < 7){
                    return false;
                  }else{
                    return true;
                  }
                },
                args:{},
                failureMessage: "Ingrese un número de teléfono válido"
    });
    valTelef.add(Validate.Presence, {failureMessage: " El teléfono del beneficiario es requerido"});
    var valMonto= new LiveValidation('m_Monto', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valMonto.add(Validate.Presence, {failureMessage: " El monto a transferir es requerido"});
    valMonto.add(Validate.MontoMin, {failureMessage: " El monto debe ser mayor a 0.00"});
    valMonto.add(Validate.Format, {pattern: /^[0-9]+(\.[0-9]+)?$/i,failureMessage: " Debe ingresar un monto válido"});

    var valCtaAbonoEntidad= new LiveValidation('m_CtaAbonoEntidad', { validMessage: '', wait: 500, onlyOnSubmit: true });
    valCtaAbonoEntidad.add(Validate.Presence, {failureMessage: "(*)"});    
    var valCtaAbonoOficina= new LiveValidation('m_CtaAbonoOficina', { validMessage: '', wait: 500, onlyOnSubmit: true });
    valCtaAbonoOficina.add(Validate.Presence, {failureMessage: "(*)"});
    var valCtaAbonoCuenta= new LiveValidation('m_CtaAbonoCuenta', { validMessage: '', wait: 500, onlyOnSubmit: true });     
    valCtaAbonoCuenta.add(Validate.Presence, {failureMessage: "(*)"});
    var valCtaAbonoControl= new LiveValidation('m_CtaAbonoControl',{ validMessage: '', wait: 500, onlyOnSubmit: true });
    valCtaAbonoControl.add(Validate.Presence, {failureMessage: "Ingrese un número de cuenta correcta"});
    valCtaAbonoControl.add(Validate.Custom, {against: function(value,args){
                  var m_CtaAbonoEntidad = document.forms[0].m_CtaAbonoEntidad.value;
                  var m_CtaAbonoOficina = document.forms[0].m_CtaAbonoOficina.value;
                  var m_CtaAbonoCuenta = document.forms[0].m_CtaAbonoCuenta.value;
                  var m_CtaAbonoControl = document.forms[0].m_CtaAbonoControl.value;
                  
                  if(m_CtaAbonoEntidad!="" && m_CtaAbonoOficina!="" && m_CtaAbonoCuenta!=""&& m_CtaAbonoControl!=""){                    
                    return true;
                  }else{                    
                    return false;
                  }
                },
                args:{},
                failureMessage: "Ingrese un número de cuenta correcta"
    });        
    
</script>
</body>
</html>

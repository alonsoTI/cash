<%@ page errorPage="error.jsp" %>

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
    <title>Transferencia a Terceros</title>
    <link href="css/Styles.css" rel="stylesheet" type="text/css">
    <link href="css/consolidated_common.css" rel="stylesheet" type="text/css">
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
    <script LANGUAGE="javascript" SRC="js/string.js"></script>
    <script type="text/javascript" src="js/livevalidation_standalone.js"></script>
    <script language="JavaScript" type="text/javascript">

    function validarMonto(campo){
    	var lblMonto = document.getElementById('lblMonto');        
    	var cuenta = document.forms[0].m_CtaAbono;
    	var cadenaCuenta = formatearCuentaAbono(cuenta.value);
        if(campo.value!=""){
            if(!validarDecimal(campo.value)){
            	lblMonto.innerHTML ="El importe no debe contener caracteres o espacios en blanco"; 
            	campo.select();
                campo.focus();           		
           		strMonto = "";               
            }
            else{
                var indic = campo.value.indexOf(".", 0);
                var indfin = campo.value.length;
                if(indic>-1){ //si encuentra el punto decima valida qe solo tenga dos decimales
                    var cadena = campo.value.substring(indic+1, indfin);
                    if(cadena.length>2){
                    	lblMonto.innerHTML ="El importe solo puede tener 2 decimales como maximo"; 
                    	campo.select();
                        campo.focus();          		              
                    }
                }
            }
        }
    }
    
	function formatearCuentaAbono(nroCuenta){
		var cadena = "";		
		nroCuenta = nroCuenta.trim();  
		var n = nroCuenta.length;	
        if( n < 12 ){
            var iter =  12 - n;
            var ceros = "";
            for( var i = 0 ; i < iter ; i++ ){
            	ceros = ceros + "0";    
            }
            cadena = ceros + nroCuenta; 
        }else{
            cadena = nroCuenta;
        }
        return cadena;
	}
    
    function validarCuenta(){
        var cuenta = document.forms[0].m_CtaAbono; 
        var lblCuenta = document.getElementById('lblCuenta');  
        var lblMonto = document.getElementById('lblMonto');        
        if( cuenta.value != "" ){
            var n = cuenta.value.length;             
            if(  n >=9 && n<=12){
            	var ExpReg_cad=/^\d+$/;  
                var cadena = formatearCuentaAbono(cuenta.value);
                if( ExpReg_cad.test(cuenta.value)==true ){   
                	if(  nroCuentaAbono != cuenta.value ){            		
            			validarCuentaAjax("transferencias.do?do=validarCuentaTerceros&nroCuentaDestino="+cadena+"&nroCuentaOrigen="+document.forms[0].m_CtaCargo.value+"&monto="+document.forms[0].m_Monto.value);
            			nroCuentaAbono = cadena;            		                                            
                	}else{
                		lblMonto.innerHTML ="";   
                	}	
                }else{
                	lblCuenta.innerHTML = "El numero de cuenta no debe contener caracteres o espacios en blanco"; 
                	lblCuenta.style.color = "#CC0000";
               		lblMonto.innerHTML =""; 
               	
                    cuenta.select();
                    cuenta.focus();
            	}    	     	
            }else{
            	lblCuenta.innerHTML = "El numero de cuenta esta formado por minimo 9 y maximo 12 digitos"; 
            	lblCuenta.style.color = "#CC0000";
           		lblMonto.innerHTML ="";     		
            }                            	        	
        }else{
        	lblCuenta.innerHTML = ""; 
       	 	lblMonto.innerHTML ="";          	 	
        }
    }

    function validarCuentaAjax(url){
    	if (window.XMLHttpRequest) { // Non-IE browsers
        	req = new XMLHttpRequest();
        	req.onreadystatechange = procesarRespuesta;
        	try {
            	req.open("GET", url, true);
            }catch (e){
                alert(e);
            }
        	req.send(null);
      	} else if (window.ActiveXObject) { // IE
        	req = new ActiveXObject("Microsoft.XMLHTTP");
        	alert(req);
        	if (req) {
          		req.onreadystatechange = procesarRespuesta;
          		req.open("GET", url, true);
          		req.send();
        	}else{
        		alert('Navegador No Compatible');
        	}
      	}
    }
    
    function procesarRespuesta(){
    	if (req.readyState==4 && req.status==200){			
    		var response = req.responseXML;
            if(response==null){
                 alert("Ocurrió un error al procesar los datos");
            }else{
            	 var respuesta = response.getElementsByTagName("respuesta");
                 var valor = respuesta[0].getAttribute("valor");        
                 var estado = respuesta[0].getAttribute("estado");  
                 var lblCuenta = document.getElementById('lblCuenta');  
                 var lblMonto = document.getElementById('lblMonto');          
                 if( estado == 1 ){          	    	                 	 
                	 lblCuenta.innerHTML = valor; 
                	 lblMonto.innerHTML ="";          	 
                	 lblCuenta.style.color = "#0062ac";                	
                 }else{                	
                     if( estado == 2 ){
                    	 lblCuenta.innerHTML = valor;
                    	 lblCuenta.style.color = "#CC0000";
                    	 lblMonto.innerHTML = "";
                     }
                     else{                          
                         lblMonto.innerHTML = valor;      
                         lblCuenta.innerHTML = "";                                        	   		
                     }                    	 
                 }                     
            }            
      	}
    }
        
    function verificarCta(campo) {//jmoreno
          val_int(campo);
          var cc_abono = campo.value;
          if(cc_abono.length < 9){
            alert("Ingrese una cuenta correcta");
            campo.focus();
            campo.select();
            return false;
          }else{
            while(cc_abono.length < 12){
                cc_abono = "0" + cc_abono;
            }
            campo.value = cc_abono;
            return true;
          }
    }
    
	function isValid(frm, from, strTarget) {            
    	var val = new Array();            
        val[0] = valCtaAbono;
        val[1] = valMonto;
		
        if (LiveValidation.massValidate(val)){
        	var ctaCargo = document.forms[0].m_CtaCargo.value;
            var ctaAbono = document.forms[0].m_CtaAbono.value;
            if (ctaCargo == ctaAbono){
            	alert ("Cuenta origen y destino deben ser diferentes");
                return false;
            }else{
            	if (isValidForm2(frm,from,strTarget)){
                	return true;
                }
           	}
        }
    	return false;
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
    </script>
</head>
<body onload="cambiarMoneda();">
<html:form action="transferencias.do" onsubmit="return false;" >
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
            <td valign="middle" align="left" class="Title"><bean:message key="transferencias.title.ctaterceros"/></td>            
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
                <html:select name="tf_transf_cta" property="m_Empresa" styleId="m_Empresa" styleClass="CellColRow2" onchange="javascript:DoSubmit('transferencias.do?do=cargarServicios&tipo=2');">
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
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr class="CellColRow2">
            <td width="20%" style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.cargo"/></td>
            <td width="80%">
                <html:select name="tf_transf_cta" property="m_CtaCargo" styleId="m_CtaCargo" styleClass="CellColRow2" 
                		onchange="cambiarMoneda();">
                    <logic:iterate id="lcargo" name="tf_listaccounts">
                        <bean:define name="lcargo" property="m_AccountCode" id="idacccargo" type="java.lang.String" />
                        <% String idcargo = "" + idacccargo + ""; %>
                        <html:option value="<%= idcargo %>" style="background-color: #FFFFFF" >&nbsp;<bean:write name="lcargo" property="m_AccountDescription" />&nbsp;<bean:write name="lcargo" property="m_AccountCode" />&nbsp;-&nbsp;Saldo:&nbsp;<bean:write name="lcargo" property="m_Currency"/>&nbsp;<bean:write name="lcargo" property="m_AvailableBalance"/></html:option>
                    </logic:iterate>                   
                </html:select>
            </td>
        </tr>
        <tr class="CellColRow2" >
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.abono"/></td>
            <td style="background-color: #FFFFE6" >
                <html:text name="tf_transf_cta" property="m_CtaAbono" styleId="m_CtaAbono" maxlength="12"
                 size="15" styleClass="CellColRow2" style="background-color: #FFFFFF" 
                 onkeypress="numero()"    />                
                 <logic:notEmpty name="error_cuenta" scope="request">
                 	<span id="lblCuenta2" style="color: #CC0000;">
                 		<bean:write name="error_cuenta"/>
                 	</span>
                 </logic:notEmpty>                 
            </td>          
        </tr>
       
        <tr class="CellColRow2" >
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.monto"/></td>
            <td>
                <html:text name="tf_transf_cta" property="m_Monto" styleId="m_Monto" maxlength="10" size="15" styleClass="CellColRow2"
                 style="background-color: #FFFFFF" onkeypress="return val_decimal(event)"  onblur="validarMonto(this)" />
                
                 <logic:notEmpty name="error_monto" scope="request">
                 	<span id="lblMonto2" style="color: #CC0000;">
                 		<bean:write name="error_monto"/>
                 	</span>
                 </logic:notEmpty>                
            </td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.moneda"/></td>
            <td style="background-color: #FFFFE6"><div id="moneda"></div></td>
        </tr>
        <tr class="CellColRow2">
            <td style="background-color: #336699; color: #FFFFFF"><bean:message key="transferencias.lbl.referencia"/></td>
            <td>
                <html:text name="tf_transf_cta" property="m_Referencia" styleId="m_Referencia" maxlength="50" 
                size="66" styleClass="CellColRow2" 
                style="text-transform:uppercase;background-color: #FFFFFF" 
                onkeypress="return soloDescripcionBasic(this,event);" onblur="gDescripcionBasic(this);"/>
            </td>
        </tr>
        <tr><td class="CellColRow2" colspan="2" style="background-color: #FFFFFF">&nbsp;</td></tr>
        <tr align="right">        	
        	<td colspan="2" align="center">        	
            	<img src="img/bt-continuar.png" id= "btnContinuar" 
            		 align="middle" onMouseOver="this.src='img/bt-continuar2.png'" 
            		 onMouseOut="this.src='img/bt-continuar.png'"  
            		 onClick="javascript:isValid(document.forms[0],'TransfCtaTerceros','transferencias.do?do=validar&tipo=2&m_Moneda='+document.forms[0].m_Moneda.value);"/>
          	</td>        	          
        </tr>
    </table>
     <div style="visibility:hidden">
        <html:select name="tf_transf_cta" property="m_Moneda" styleId="m_Moneda" disabled="true">
                <html:options collection="tf_listamoneda" property="id.clfCode" labelProperty="dlfDescription" />
        </html:select>
    </div>
    </logic:equal>
      <html:hidden name="varIdTipoTransf" property="m_IdTipoTransf" value="12" />
</html:form>
<script type="text/javascript">
     var cc_abono = document.forms[0].m_CtaAbono.value;
     var valCtaAbono= new LiveValidation('m_CtaAbono',{ validMessage: '', wait: 500, onlyOnSubmit: true});
         valCtaAbono.add(Validate.Presence, {failureMessage: " El número de cuenta de abono es requerido"});
            valCtaAbono.add(Validate.Custom, {against: function(value,args){                  
                  var campo = document.forms[0].m_CtaAbono;
                  val_int(campo);
                  var cc_abono = campo.value;
                  if(cc_abono.length < 9){
                    return false;
                  }else{
                    while(cc_abono.length < 12){
                        cc_abono = "0" + cc_abono;
                    }
                    campo.value = cc_abono;
                    return true;
                  }
                },
                args:{},
                failureMessage: "El numero de cuenta esta formado por minimo 9 y maximo 12 digitos"
            });
            //valCtaAbono.add(Validate.Custom, {against: function(value,args){if (args.acc_cargo == args.acc_abono)return false; else return true;}, args:{acc_cargo:document.forms[0].m_CtaCargo.value,acc_abono:cc_abono}, failureMessage: " Elija otra cuenta"});
    
    var valMonto= new LiveValidation('m_Monto', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valMonto.add(Validate.Presence, {failureMessage: " El monto a transferir es requerido"});
    valMonto.add(Validate.MontoMin, {failureMessage: " El monto debe ser mayor a 0.00"});
    valMonto.add(Validate.Format, {pattern: /^[0-9]+(\.[0-9]+)?$/i,failureMessage: "Debe ingresar un monto válido"});   
</script>
</body>
</html>

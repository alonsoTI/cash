<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="/cache/cache.jsp" %>
<%@ page import="com.financiero.cash.delegate.TransferenciasDelegate, java.util.Map, org.apache.log4j.Logger" %>
<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>
<%! private TransferenciasDelegate delegado =  TransferenciasDelegate.getInstance(); %>
<%! private Logger logger = Logger.getLogger(this.getClass()); %> 
<% try{Map map_limites_transferencias = delegado.obtenerLimitesTransferencias();
 pageContext.setAttribute("map_limites_transferencias", map_limites_transferencias);%>
<html>
<head>    
    <title>Transferencias</title>     
    <link href="css/live.css" rel="stylesheet" type="text/css">
    <link href="css/formulario.css" rel="stylesheet" type="text/css">
    <script language="javascript" SRC="js/cash.js"></script>    
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>    
    <script type="text/javascript" src="js/livevalidation_standalone.js"></script>
    <script type="text/javascript" src="js/jquery.js"></script>       
    <script language="JavaScript" type="text/javascript">
        
    	var tipoDoc;
        
        function confirmar() {
            var frm = document.forms[0];  
            var strTarget='transferencias.do?do=confirmar';                 
            var ctaCargo = frm.m_CtaCargo.options[document.forms[0].m_CtaCargo.selectedIndex].value;
            var val = new Array();                                   
            if( tipoDoc != 'RUC' ){
            	 if(tipoDoc=='DNI'){
            	 	val[0] = valNroDocDNI;
            	 }else{            		 
            		 val[0] = valNroDoc; 
            	 }
            	 if(tipoDoc=='DNI' || tipoDoc=='CIP'){
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
            	 }else{            		 
            		 val[1] = valNombre;
	                 val[2] = valDireccion;
	                 val[3] = valTelef;
	                 val[4] = valMonto;
	                 val[5] = valCtaAbonoEntidad;
	                 val[6] = valCtaAbonoOficina;
	                 val[7] = valCtaAbonoCuenta;
	                 val[8] = valCtaAbonoControl;
            	 }                                
            }
            else{                 
            	 val[0] = valNroDocRUC; 
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
                if (submit(frm,strTarget)){
                    return true;
                }
            }
            return false;                    
        }

        function obtenerBanco(campo){
            campo.value=campo.value.toString().replace(/([^0-9])/g,"");
            var codigo = campo.value;
            if(codigo!=null && codigo!=""){            
                obtenerBancoAjax("transferencias.do?do=obtenerNombreBco&codigo=0"+codigo);
            }else{
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
        
        function cambiarMoneda() {
         	var campo = document.forms[0].m_CtaCargo;
         	campo = campo.options[campo.selectedIndex].text;
         	var  indic = campo.indexOf(":", 0);
         	var cadena = campo.substring(indic+2,indic+5);
         	var cbomoneda = document.forms[0].codigoMoneda;
         	for(i=0;i < cbomoneda.length;i++){
             	if(cbomoneda.options[i].text == cadena){
                 	cbomoneda.selectedIndex = i;                 	
                 	i = cbomoneda.length;
                 	document.getElementById("moneda").innerHTML=cadena;
             	}
         	}        	
    	}    

        var tipoDoc;	

        function ocultarFila(valor) {                       
            tipoDoc=valor;
            var tab=document.getElementById('formulario');
            var fila=tab.getElementsByTagName('tr')[6];
            var fila2=tab.getElementsByTagName('tr')[8];
            if(  valor=='RUC'){          		
          	  	tab.getElementsByTagName('tr')[7].style.display='';
          	  	tab.getElementsByTagName('tr')[8].style.display='none';
          	  	tab.getElementsByTagName('tr')[9].style.display='none';      
          	  	fila.getElementsByTagName('td')[3].style.display='none';   
          	  	fila.getElementsByTagName('td')[4].style.display='';
          	  	fila.getElementsByTagName('td')[5].style.display='none';          	  	
            }  else{          	  	
          	  	tab.getElementsByTagName('tr')[7].style.display='none';
          	  	tab.getElementsByTagName('tr')[8].style.display='';
          	  	tab.getElementsByTagName('tr')[9].style.display='';
          	  	fila.getElementsByTagName('td')[4].style.display='none';
          	  	if(valor=='DNI'){          	  		
          	  		fila.getElementsByTagName('td')[3].style.display='';
          	  		fila.getElementsByTagName('td')[5].style.display='none';
          	  		fila2.getElementsByTagName('td')[2].style.display='none';
          	  		fila2.getElementsByTagName('td')[1].style.display='';
          	  		fila2.getElementsByTagName('td')[5].style.display='none';
      	  			fila2.getElementsByTagName('td')[4].style.display='';
          	  	}else if(valor=='CIP'){
	          	  	fila.getElementsByTagName('td')[5].style.display='';
	      	  		fila.getElementsByTagName('td')[3].style.display='none';
		      	  	fila2.getElementsByTagName('td')[2].style.display='none';
	      	  		fila2.getElementsByTagName('td')[1].style.display='';
	      	  		fila2.getElementsByTagName('td')[5].style.display='none';
	  	  			fila2.getElementsByTagName('td')[4].style.display='';
          	  	}          	  	
          	  	else{
          	  		fila.getElementsByTagName('td')[5].style.display='';
          	  		fila.getElementsByTagName('td')[3].style.display='none';
          	  		fila2.getElementsByTagName('td')[1].style.display='none';
      	  			fila2.getElementsByTagName('td')[2].style.display='';
      	  			fila2.getElementsByTagName('td')[4].style.display='none';
  	  				fila2.getElementsByTagName('td')[5].style.display='';
          	  	}
            }
      	}

        /*
        	td3:dni
        	td4:ruc
        	td5:other
        	
        	td0:lb
        	td1:pat1
        	td2:pat2
        	td3:lb2
        	td4:mat1
        	td5:mat2
        */
        function iniciarFormulario(){
            cambiarMoneda();
            var tab=document.getElementById('formulario');
            var fila=tab.getElementsByTagName('tr')[6];
            var fila2=tab.getElementsByTagName('tr')[8];
           	var radios = document.getElementsByName('m_FlagCliente');
           	//por defecto activo el NO
            radios[1].checked=true;
            if( tipoDoc != 'RUC' ){
          		tab.getElementsByTagName('tr')[7].style.display='none'; 
          		if(tipoDoc=='DNI'){
          			fila.getElementsByTagName('td')[5].style.display='none';
          			fila.getElementsByTagName('td')[4].style.display='none';
          			fila2.getElementsByTagName('td')[2].style.display='none';
          			fila2.getElementsByTagName('td')[5].style.display='none';
          		}
          		else if(tipoDoc=='CIP'){
          			fila.getElementsByTagName('td')[4].style.display='none';
          			fila.getElementsByTagName('td')[3].style.display='none';
          			fila2.getElementsByTagName('td')[2].style.display='none';
          			fila2.getElementsByTagName('td')[5].style.display='none';
          		}
          		else{
          			fila.getElementsByTagName('td')[4].style.display='none';
          			fila.getElementsByTagName('td')[3].style.display='none';
          			fila2.getElementsByTagName('td')[1].style.display='none';
          			fila2.getElementsByTagName('td')[4].style.display='none';
          		}
            }else{
            	tab.getElementsByTagName('tr')[8].style.display='none';
            	tab.getElementsByTagName('tr')[9].style.display='none';            	
            	fila.getElementsByTagName('td')[3].style.display='none';        	
            	fila.getElementsByTagName('td')[5].style.display='none';
            }
        }
      	
      	function verificarDatosCompletos(input){      		
      		var longitudMaxima = $(input).attr('maxLength');
      		if(input.value.length==longitudMaxima){
      			$(input).next().focus();
      		}
      	}
       
    </script>    
</head>
<body onload="iniciarFormulario();">
<html:form action="transferencias.do" onsubmit="return false;">     
		<h1><fmt:message key="transferencias.title.ctainterbco"/></h1> 
    	<fieldset>
    		<div align="right">    		
    			<table id="headEmpresa">
	    			<c:if test="${sessionScope.nombre_empresa_seleccionada!=null}">
		    			<tr>
		    				<td>Empresa:</td>
		    				<td><c:out value="${sessionScope.nombre_empresa_seleccionada}"/></td>
		    			</tr>
		    		</c:if>
		    		<c:if test="${!(sessionScope.usuarioActual.m_Nombre==null and sessionScope.usuarioActual.m_Apellido==null)}">
		    			<tr>
		    				<td>Usuario:</td>
		    				<td><c:out value="${sessionScope.usuarioActual.m_Nombre} ${sessionScope.usuarioActual.m_Apellido}"/></td>
		    			</tr>
		    		</c:if>
		    		<c:if test="${sessionScope.usuarioActual.m_NumTarjeta!=null}">
		    			<tr>
		    				<td>Nro. Pin:</td>
		    				<td><c:out value="${sessionScope.usuarioActual.m_NumTarjeta}"/></td>
		    			</tr>	    			
		    		</c:if>
	    		</table>
	    	</div>    		
    		<h3 id="ingresarDatos">Ingresar Datos Transferencia</h3>   	
    		<table id="formulario">    			    				
	    		<div class="error" >
		    		<logic:notEmpty name="error" scope="request">
		           		<bean:write name="error"/>
		           	</logic:notEmpty>
	        	</div>    		    			    			
	    		<!-- fila 0 -->
    			<tr>
    				<td width="22%">
    					<label>
    						<bean:message key="transferencias.lbl.cargo"/>
    					</label>
    				</td>
    				<td colspan="3">
    					<html:select  property="cuentaCargo" styleId="m_CtaCargo"  onchange="cambiarMoneda();">
            				<logic:iterate id="lcargo" name="tf_listaccounts">
                				<bean:define name="lcargo" property="m_AccountCode" id="idacccargo" type="java.lang.String" />
                    			<% String idcargo = "" + idacccargo + ""; %>
                    			<html:option value="<%= idcargo %>" >&nbsp;<bean:write name="lcargo" property="m_AccountDescription" />&nbsp;<bean:write name="lcargo" property="m_AccountCode" />&nbsp;:&nbsp;<bean:write name="lcargo" property="m_Currency"/>&nbsp;<bean:write name="lcargo" property="m_AvailableBalance"/></html:option>
                			</logic:iterate>
            			</html:select>              
    				</td>
    			</tr>    			
    			<tr>
	    			<td>
	    				<label><bean:message key="transferencias.lbl.moneda"/>  </label>
	    			</td>
	    			<td><span id="moneda"> </span></td>
	    		</tr>
    			<tr>
	    			<td>
	    				<label><bean:message key="transferencias.lbl.monto"/></label>
	    			</td>
	    			
	    			<td colspan="3">
	    				<html:text property="monto" styleId="m_Monto" maxlength="18"  
	         			onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)"/> <span class="error">(*)</span>
	         			<logic:notEmpty name="error_monto" scope="request">
	                		<span class="error">
	                 			<bean:write name="error_monto"/>
	                 		</span>
	                	</logic:notEmpty>	                	
	    			</td>	    			   		
	    		</tr>
	    		
    			<!-- fila 1 -->
    			<tr>
    				<td >
    					<label>
    						Cuenta Interbancaria<br>de Abono
    					</label>
    				</td>
    				<td colspan="3">    					
    					<html:text property="m_CtaAbonoEntidad" styleId="m_CtaAbonoEntidad" maxlength="3" size="3" 
    							 onkeypress="numero();" onblur="obtenerBanco(this)" style="display:inline;" onkeyup="verificarDatosCompletos(this);" />
                		<html:text property="m_CtaAbonoOficina" styleId="m_CtaAbonoOficina" maxlength="3" size="3"  
                		onkeypress="numero()" onblur="val_int(this)" style="display:inline;" onkeyup="verificarDatosCompletos(this);"/>
                		<html:text property="m_CtaAbonoCuenta" styleId="m_CtaAbonoCuenta" maxlength="12" size="12" 
                		 onkeypress="numero()" onblur="val_int(this)" style="display:inline;" onkeyup="verificarDatosCompletos(this);"/>
                		<html:text property="m_CtaAbonoControl" styleId="m_CtaAbonoControl" maxlength="2" size="2"  
                				onkeypress="numero()" onblur="val_int(this)" style="display:inline;" onkeyup="verificarDatosCompletos(this);"/>		  
            			<html:text property="m_NombreBanco" styleId="m_NombreBanco" readonly="true"  size="32" style="display:block"/>
            			<logic:notEmpty name="error_cuenta" scope="request">
			             	<span style="color: #CC0000;">
			               		<bean:write name="error_cuenta"/>
			               	</span>
		                 </logic:notEmpty>            			        
    				</td>
    			</tr>    			   		
    			<tr>
    				<td colspan="2" width="48%">
    					<label>
    						Cuenta en Banco Destino Mismo cliente (Para exoneracion ITF)
    					</label>
    				</td>
    				<td colspan="2">
    					<html:radio property="m_FlagCliente" value="1" styleId="radioFlagMismo" style="border-style: none;">SI</html:radio>
    					<html:radio property="m_FlagCliente" value="0" styleId="radioFlagMismo" style="border-style: none;">NO</html:radio>
    				</td>
    			</tr>    			
    			<tr>
    				<td>
    					<label id="lblBeneficiario">Datos del Beneficiario</label>
    				</td>
    			</tr>  			
    			<!-- fila 2 -->	
    			<tr>
	    			<td>
	    				<label>Documento</label>
	    			</td>
	    			<td>
	    				<html:select property="documento" styleId="m_IdTipoDocBenef"
	    						onchange="ocultarFila(this.value);">						
								<html:options property="documento" labelProperty="documento" collection="tiposDocumento2"  /> 				
						</html:select>
	    			</td>
	    			<td>
	    				<label>Numero</label>
	    			</td>
	    			<td>
	    				<html:text property="dni" styleId="m_NumDocBenef_DNI" maxlength="8" 
	    				onkeypress="numero()" onblur="val_int(this)"/>	    				
	    			</td>
	    			<td>
	    				<html:text property="ruc" styleId="m_NumDocBenef_RUC" maxlength="11" 
	    				onkeypress="numero()" onblur="val_int(this)"/>
	    			</td>
	    			<td>
	    				<html:text property="numeroDocumento" styleId="m_NumDocBenef" 
	    				onkeypress="numero()" onblur="val_int(this)"/>
	    			</td>
    			</tr>
    			<!-- parte persona juridica. fila 3 -->
    			<tr>
    				<td>
    					<label>Empresa</label>
    				</td>
    				<td>
    					<html:text property="razonSocial" styleId="m_RazonSocial" maxlength="30"  
    					onkeypress="return soloNombreBasico(this,event);" onblur="gNombreBasico(this);"/>
    				</td>    				    				    				
    			</tr>
    			<!-- parte de persona natural. fila 4 -->
    			<tr>
    				<td>
    					<label>Ap. Paterno</label>
    				</td>
    				<td>
    					<html:text property="apellidoPaterno" styleId="m_ApePatBenef" maxlength="30"  
    					onkeypress="return soloNombreBasico(this,event);" onblur="gNombreBasico(this);"/>
    				</td>
    				<td>
    					<html:text property="apellidoPaterno2" styleId="m_ApePatBenef2" maxlength="30"  
    					onkeypress="return soloNombreBasico(this,event);" onblur="gNombreBasico(this);"/>
    				</td>    				
    				<td>
    					<label>Ap. Materno</label>
    				</td>
    				<td>
    					<html:text property="apellidoMaterno" styleId="m_ApeMatBenef" maxlength="30"  
    					onkeypress="return soloNombreBasico(this,event);" onblur="gNombreBasico(this);"/>
    				</td>
    				<td>
    					<html:text property="apellidoMaterno2" styleId="m_ApeMatBenef2" maxlength="30"  
    					onkeypress="return soloNombreBasico(this,event);" onblur="gNombreBasico(this);"/>    					
    				</td>
    			</tr>
    			<!-- fila 5 -->
    			<tr>
    				<td>
    					<label>Nombres</label>
    				</td>
    				<td >
    					<html:text property="nombres" styleId="m_NombreBenef" maxlength="30"  
    					onkeypress="return soloNombreBasico(this,event);" onblur="gNombreBasico(this);"/>    					
    				</td>
    				
    			</tr>
    			<!-- fila 6 -->
    			<tr>
    				<td>
    					<label>Direccion</label>
    				</td>
    				<td>
    					<html:text property="direccion" styleId="m_DireccionBenef" maxlength="50" onkeypress="return soloDescripcionBasic(this,event);" onblur="gDescripcionBasic(this);"/>
    					
    				</td>
    				
    				<td>
    					<label>Telefono</label>
    				</td>
    				<td>
    					<html:text property="telefono" styleId="m_TlfBenef" maxlength="10" onkeypress="numero()" onblur="val_int(this)"/>    					
    				</td>
    			</tr>    			
	    		
	    		
	    		
	    		<tr >
	    			<td>
	    				<label>
	    				<bean:message key="transferencias.lbl.referencia"/>
	    				</label>
	    			</td>
	    			<td colspan="3">
	    				<html:text property="referencia" styleId="m_Referencia" maxlength="40"
	            	 	 onkeypress="return soloDescripcionBasic(this,event);" onblur="gDescripcionBasic(this);" />      	
	    			</td> 
	    		</tr>
	    		
    		</table>        		 
        <input type="submit" id="registrar" value="Registrar" onClick="javascript:confirmar()"/>
        <div id="aviso">
        		Importante: <fmt:message key="transferencias.msg.informacionMontosLimites">
    				<fmt:param value="${pageScope.map_limites_transferencias['limiteDiarioDolares']}"/>
    			 	<fmt:param value="${pageScope.map_limites_transferencias['limiteDiarioSoles']}"/>    			 	
    			</fmt:message>
    			<br>
    			<br>
    			(*) <fmt:message key="transferencias.msg.informacionMontosLimitesIB">
    				<fmt:param value="${pageScope.map_limites_transferencias['montoITSoles']}"/>    				    			 
    			 	<fmt:param value="${pageScope.map_limites_transferencias['montoITDolares']}"/>    			 	
    			</fmt:message>
    	</div>
    	<div id="avisoIT">
    		<fmt:message key="transferencias.msg.it.informacionHorarios"/>
    	</div>    	
    </fieldset>
        
    <div style="visibility:hidden">
        <html:select  property="codigoMoneda" styleId="codigoMoneda" >
                <html:options collection="listaMoneda"   
                property="id.clfCode" labelProperty="dlfDescription" />
        </html:select>
    </div>     
</html:form>


<script type="text/javascript">
	tipoDoc = document.forms[0].m_IdTipoDocBenef.value;
    var valMonto= new LiveValidation('m_Monto', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valMonto.add(Validate.Presence, {failureMessage: " El monto a transferir es requerido"});
    valMonto.add(Validate.MontoMin, {failureMessage: " El monto debe ser mayor a 0.00"});
    valMonto.add(Validate.Format, {pattern: /^[0-9]+(\.[0-9]+)?$/i,failureMessage: " Debe ingresar un monto válido"});

    var valNroDocRUC = new LiveValidation('m_NumDocBenef_RUC', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valNroDocRUC.add(Validate.Presence, {failureMessage: " El RUC es requerido"});
    valNroDocRUC.add( Validate.Length, { minimum: 11, maximum: 11, tooShortMessage:"El RUC es de 11 digitos"} );
        	
    var valNroDocDNI = new LiveValidation('m_NumDocBenef_DNI', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valNroDocDNI.add(Validate.Presence, {failureMessage: " El DNI es requerido"});
    valNroDocDNI.add( Validate.Length, { minimum: 8, maximum: 8, tooShortMessage:"El DNI es de 8 digitos"} );
		
	var valNroDoc = new LiveValidation('m_NumDocBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
	valNroDoc.add(Validate.Presence, {failureMessage: " El numero de documento es requerido"});	    	    

    var valRazon = new LiveValidation('m_RazonSocial', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valRazon.add(Validate.Presence, {failureMessage: "La empresa es requerida"});

        	
	var valApePat = new LiveValidation('m_ApePatBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
	valApePat.add(Validate.Presence, {failureMessage: " El apellido paterno es requerido"});
	var valApeMat = new LiveValidation('m_ApeMatBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
	valApeMat.add(Validate.Presence, {failureMessage: " El apellido materno es requerido"});
    
    var valNombre = new LiveValidation('m_NombreBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valNombre.add(Validate.Presence, {failureMessage: " El nombre es requerido"});    

    var valDireccion= new LiveValidation('m_DireccionBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valDireccion.add(Validate.Presence, {failureMessage: "La direccion es requerida"});

    var valCtaAbonoEntidad= new LiveValidation('m_CtaAbonoEntidad', { validMessage: '', wait: 500, onlyOnSubmit: true });
    valCtaAbonoEntidad.add(Validate.Presence, {failureMessage: "(*)"});    
    var valCtaAbonoOficina= new LiveValidation('m_CtaAbonoOficina', { validMessage: '', wait: 500, onlyOnSubmit: true });
    valCtaAbonoOficina.add(Validate.Presence, {failureMessage: "(*)"});
    var valCtaAbonoCuenta= new LiveValidation('m_CtaAbonoCuenta', { validMessage: '', wait: 500, onlyOnSubmit: true });     
    valCtaAbonoCuenta.add(Validate.Presence, {failureMessage: "(*)"});
    
    var valTelef= new LiveValidation('m_TlfBenef', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valTelef.add(Validate.Presence, {failureMessage: "El telefono es requerido"});
    valTelef.add( Validate.Length, { minimum: 5, tooShortMessage:"El telefono es de minimo 5 digitos"} );
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
<%}catch(Exception e){logger.error("Error en jsp",e);}%>

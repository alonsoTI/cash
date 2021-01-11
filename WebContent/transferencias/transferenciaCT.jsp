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
<% try{ Map map_limites_transferencias = delegado.obtenerLimitesTransferencias();
 pageContext.setAttribute("map_limites_transferencias", map_limites_transferencias);%>
<html>
<head>    
    <title>Transferencias CT</title>     
    <link href="css/live.css" rel="stylesheet" type="text/css">
    
    <link href="validaciones.css" rel="stylesheet" type="text/css" />
    
    <link href="css/formulario.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="js/cash.js"></script>    
    <script type="text/javascript" src="js/Functions.js"></script>    
    <script type="text/javascript" src="js/livevalidation_standalone.js"></script>
    
    
    <script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="js/jquery.numeric.js"></script>  
    
   <script type="text/javascript">
   
   $(document).ready(function(){

	   	$(".positive-integer").numeric({ decimal: false, negative: false }, function() { alert("Solo numeros enteros"); this.value = ""; this.focus(); });
	   
	});
   
	
	</script>
    
         
    <script  type="text/javascript">      

        function validarEntero2(o){
        	o.value=o.value.toString().replace(/([^\d])/g,"");	
        }
        
        function confirmar() {
            var frm = document.forms[0];  
            var strTarget='transferencias.do?do=confirmar';                 
            var ctaCargo = frm.m_CtaCargo.options[document.forms[0].m_CtaCargo.selectedIndex].value;
			
            var val = new Array();
                
            val[0] = valCtaAbono;
            val[1] = valMonto;             
            if (LiveValidation.massValidate(val)){
            	if (submit(frm,strTarget)){
                	return true;
                }
            }
            return false;                     
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
    </script>    
</head>
<body onload="cambiarMoneda();">
<html:form action="transferencias.do" onsubmit="return false;">   
		<h1><fmt:message key="transferencias.title.ctaterceros"/></h1> 
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
	    		<tr>
	    			<td>
	    				<label><bean:message key="transferencias.lbl.cargo"/></label>
	    			</td>
	    			<td colspan="3"><html:select  property="cuentaCargo" styleId="m_CtaCargo"  onchange="cambiarMoneda();">
	            		<logic:iterate id="lcargo" name="tf_listaccounts">
	                		<bean:define name="lcargo" property="m_AccountCode" id="idacccargo" type="java.lang.String" />
	                    	<% String idcargo = "" + idacccargo + ""; %>
	                    	<html:option value="<%= idcargo %>" >&nbsp;<bean:write name="lcargo" property="m_AccountDescription" />&nbsp;<bean:write name="lcargo" property="m_AccountCode" />&nbsp;:&nbsp;<bean:write name="lcargo" property="m_Currency"/>&nbsp;<bean:write name="lcargo" property="m_AvailableBalance"/></html:option>
	                	</logic:iterate>
	            	</html:select></td>
	    		</tr>
	    		<tr>
	    			<td>
	    				<label><bean:message key="transferencias.lbl.abono"/></label>
	    			</td>
	    			
	    			<td colspan="3">
	    				<html:text styleClass="positive-integer"  property="cuentaAbono" styleId="m_CtaAbono" maxlength="12" 
	    				 />
			            <logic:notEmpty name="error_cuenta" scope="request">
			              	<span style="color: #CC0000;">
			               		<bean:write name="error_cuenta"/>
			               	</span>
		                 </logic:notEmpty>
	         			
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
	         			onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)"
	         			/>
	         			<logic:notEmpty name="error_monto" scope="request">
	                		<span class="error">
	                 			<bean:write name="error_monto"/>
	                 		</span>
	                	</logic:notEmpty>
	    			</td>    			
	    		</tr>
	    		<tr>
	    			<td>
	    				<label>
	    				<bean:message key="transferencias.lbl.referencia"/> (opcional)
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
    			<fmt:message key="transferencias.msg.informacionMontosLimites">
    				<fmt:param value="${pageScope.map_limites_transferencias['limiteDiarioDolares']}"/>
    			 	<fmt:param value="${pageScope.map_limites_transferencias['limiteDiarioSoles']}"/>    			 	
    			</fmt:message>
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
	var valCtaAbono= new LiveValidation('m_CtaAbono',{ validMessage: '', wait: 500, onlyOnSubmit: true});
    
	valCtaAbono.add(Validate.Presence, {failureMessage: " El número de cuenta de abono es requerido"});    
    valCtaAbono.add( Validate.Length, { minimum: 9, maximum: 12, tooShortMessage:"El número de cuenta tiene un minimo de 9 digitos"} );    
    valCtaAbono.add( Validate.Numericality, { onlyInteger: true,notAnIntegerMessage:"El número de cuenta no debe contener caracteres",notANumberMessage:"El número de cuenta no debe contener caracteres" } );  
    
    var valMonto= new LiveValidation('m_Monto', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valMonto.add(Validate.Presence, {failureMessage: " El monto a transferir es requerido"});
    valMonto.add(Validate.MontoMin, {failureMessage: " El monto debe ser mayor a 0.00"});
    valMonto.add(Validate.Format, {pattern: /^[0-9]+(\.[0-9]+)?$/i,failureMessage: "Debe ingresar un monto válido"});   
</script>
</body>
</html>
<%}catch(Exception e){logger.error("Error en jsp",e);}%>

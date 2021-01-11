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

        function imprimir() {
            
        }

        function confirmar() {
            var frm = document.forms[0];  
            var strTarget='transferencias.do?do=confirmar';                 
            var ctaCargo = frm.m_CtaCargo.options[document.forms[0].m_CtaCargo.selectedIndex].value;
            var ctaAbono = frm.m_CtaAbono.options[document.forms[0].m_CtaAbono.selectedIndex].value;
            if (ctaCargo == ctaAbono){
                alert ("Cuenta origen y destino deben ser diferentes");
                return false;
            }else{
                var val = new Array();
                val[0] = valMonto;                
                if (LiveValidation.massValidate(val)){
                    if (submit(frm,strTarget)){
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
         	var cbomoneda = document.forms[0].codigoMoneda;
         	for(i=0;i < cbomoneda.length;i++){
             	if(cbomoneda.options[i].text == cadena){
                 	cbomoneda.selectedIndex = i;                 	
                 	i = cbomoneda.length;
                 	document.getElementById("moneda").innerHTML=cadena;
             	}
         	}
        	cambiarOpcionesCbos();
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
    	<h1><fmt:message key="transferencias.title.ctapropias"/></h1>    	
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
    				<html:select  property="cuentaAbono" styleId="m_CtaAbono"  >
	            		<logic:iterate id="labono" name="tf_listaccounts">
	                     	<bean:define name="labono" property="m_AccountCode" id="idaccabono" type="java.lang.String" />
	                       	<% String idabono = "" + idaccabono + ""; %>
	                      	 <html:option value="<%= idabono %>"  >&nbsp;<bean:write name="labono" property="m_AccountDescription" />&nbsp;<bean:write name="labono" property="m_AccountCode" />&nbsp;:&nbsp;<bean:write name="labono" property="m_Currency"/>&nbsp;<bean:write name="labono" property="m_AvailableBalance"/></html:option>
	           			</logic:iterate>
           			</html:select>
           			<br/>
           			<logic:notEmpty name="error_cuenta" scope="request">
                 		<span class="error">
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
         			onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)"/>
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
    valMonto= new LiveValidation('m_Monto', { validMessage: '', wait: 500, onlyOnSubmit: true});
    valMonto.add(Validate.Presence, {failureMessage: " El monto a transferir es requerido"});
    valMonto.add(Validate.MontoMin, {failureMessage: " El monto debe ser mayor a 0.00"});
    valMonto.add(Validate.Format, {pattern: /^[0-9]+(\.[0-9]+)?$/i,failureMessage: " Debe ingresar un monto válido"});  
</script>
</body>
</html>
<%}catch(Exception e){logger.error("Error en jsp",e);}%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>
<html>
<head>    

    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
    <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
    <META HTTP-EQUIV="Expires" CONTENT="0">


    <title>Transferencia entre cuentas propias</title> 
    <link href="css/formulario.css" rel="stylesheet" type="text/css">  
    <link href="css/live.css" rel="stylesheet" type="text/css">    
    <script language="javascript" SRC="js/cash.js"></script>  
	
   	
   	<script language="JavaScript" type="text/javascript">
   		function aceptar() {
        	var frm = document.forms[0];
        	var strTarget='cTransferencias.do?do=cargarTrx';                			        	 
        	return submit(frm,strTarget); 	    
    	}    	   		
   	</script>
    
</head>
<body>	
	<html:form action="cTransferencias.do" onsubmit="return false;">   
    	<h1>
			<bean:write name="transferencia" property="tipo.nombreCompleto"/>
    	</h1>
    	<fieldset>    		
    		<table id="formulario">
    			<tr>
    				<td>
    					<label>
    					<bean:message key="transferencias.lbl.nroTransferencia"/>
    					</label>      		
    				</td>
    				<td>
    					
    						<bean:write name="transferencia" property="numero"/>
    					
    				</td>
    				<td><label>
    					Estado
    					</label>
    				</td>
    				<td>
    					<bean:write name="transferencia" property="estado.nombre"/>
    				</td>
    			</tr>
    			
    			<tr>
    				<td><label>Cuenta Cargo</label></td>
    				<td><bean:write name="transferencia" property="cuentaCargo"/></td>
    				<td><label>Cuenta Abono</label></td>
    				<td><bean:write name="transferencia" property="cuentaAbono"/>
    					<logic:equal value="IT" name="transferencia" property="tipo">
    						<br/>
    						BANCO <bean:write name="transferencia" property="nombreBanco"/>
    					</logic:equal>
    				</td>
    			</tr>
    			<tr>
	    			<td>
	    				<label><bean:message key="transferencias.lbl.moneda"/>  </label>
	    			</td>
	    			<td><bean:write name="transferencia" property="moneda"/> </td>
	    			<td>
	    				<label >
	    					<bean:message key="transferencias.lbl.monto"/>
	    				</label>  	
	    			</td>
	    			<td>
	    				<bean:write name="transferencia" property="monto"/>
	    			</td>
    			</tr>
    			<logic:notEqual value="CP" name="transferencia" property="tipo">
    				<tr>
	    				<td>
	    					<label>Beneficiario</label>
	    				</td>
	    				<td colspan="3">
	    					<bean:write name="transferencia" property="apellidoPaterno"/>&nbsp;
	    					<bean:write name="transferencia" property="apellidoMaterno"/>&nbsp;
	    					<bean:write name="transferencia" property="nombres"/>
	    					
	    				</td>
	    			</tr>
	    			<tr>
	    				<td>
	    					<label>Documento</label>
	    				</td>
	    				<td>
	    					<bean:write name="transferencia" property="documento"/>
	    				</td>
	    				<td>
	    					<label>Nro</label>
	    				</td>
	    				<td>
	    					<bean:write name="transferencia" property="nroDocumento"/>
	    				</td>
	    			</tr>
	    		</logic:notEqual>
	    		
    			<tr>
    				<td>
    					<label>
          					<bean:message key="transferencias.lbl.referencia"/>
          				</label>
    				</td>
    				<td colspan="3">
    					<bean:write name="transferencia" property="referencia"/> 
    				</td>        	
    			</tr>
    			<logic:equal value="IT" name="transferencia" property="tipo">
    				<tr>
    					<td>
    						<label>Direccion</label>
    					</td>
    					<td>
    						<bean:write name="transferencia" property="direccion"/> 
    					</td>
    					<td>
    						<label>Telefono</label>
    					</td>
    					<td>
    						<bean:write name="transferencia" property="telefono"/> 
    					</td>
    				</tr>
    				
    			</logic:equal>
    			<logic:notEmpty name="transferencia" property="codigoError">
    				<tr>
    					<td>
    						<label>Error</label>
    					</td>
    					<td colspan="3">
    						<bean:write name="transferencia" property="codigoError"/> - 
    						<bean:write name="transferencia" property="mensajeError"/>  
    					</td>
    				</tr>
    			</logic:notEmpty>
    			<tr>
    				<td colspan="4">
    					<fieldset>    						
    						<legend>Registrado</legend>
    						<table  cellpadding="0" cellspacing="0">
    							<tr>
    								<th width="50%">Nombre</th>
	    								<th width="25%">Fecha</th>
	    								<th width="25%">Hora</th>
    							</tr>    							
    							<tr>
    								<td><bean:write name="transferencia" property="nombreUsuarioRegistro"/> </td>
    								<td><bean:write name="transferencia" property="fechaRegistro"/></td>
    								<td><bean:write name="transferencia" property="horaRegistro"/> </td>
    							</tr>
    						</table> 						
    					</fieldset>    				
    				</td>
    			</tr>
    			<logic:notEmpty name="transferencia" property="nombreUsuarioModificacion">
    				<tr>
    					<td colspan="4">
	    					<fieldset>
	    						
	    						<legend>Rechazado</legend>
	    						<table  cellpadding="0" cellspacing="0">
	    							<tr>
	    								<th width="50%">Nombre</th>
	    								<th width="25%">Fecha</th>
	    								<th width="25%">Hora</th>
	    							</tr>
	    							
	    							<tr>
	    								<td><bean:write name="transferencia" property="nombreUsuarioModificacion"/> </td>
	    								<td><bean:write name="transferencia" property="fechaProceso"/> </td>
	    								<td><bean:write name="transferencia" property="horaProceso"/> </td>
	    							</tr>
	    						</table>   						
	    					</fieldset>    				
	    				</td>    					
    				</tr>
    			</logic:notEmpty>
    			<logic:notEmpty name="transferencia" property="aprobadores">
    				<tr>
    					<td colspan="4">
	    					<fieldset>	    						
	    						<legend>Aprobadores</legend>
	    						<table  cellpadding="0" cellspacing="0">
	    							<tr>
	    								<th width="50%">Nombre</th>
	    								<th width="25%">Fecha</th>
	    								<th width="25%">Hora</th>
	    							</tr>
	    							<logic:iterate id="app" name="transferencia" property="aprobadores"	>    							
		    							<tr>
			    							<td><bean:write name="app" property="nombre"/> </td>
			    							<td><bean:write name="app" property="fecha"/> </td>
			    							<td><bean:write name="app" property="hora"/></td>
			    						</tr>
		    						</logic:iterate>	    							
	    						</table>   						
	    					</fieldset>    				
	    				</td>
    								
    				</tr>  				
    				
    			</logic:notEmpty>    			
    		</table>    		
			<input type="submit" id="registrar" value="Aceptar" onClick="javascript:aceptar()"/>        
   					    	 
        
    	</fieldset>    
	</html:form>
</body>
</html>

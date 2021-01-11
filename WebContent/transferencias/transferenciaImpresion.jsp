<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>    
    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
    <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
    <META HTTP-EQUIV="Expires" CONTENT="0">
    <title>Transferencia</title> 
    <link href="css/formulario.css" rel="stylesheet" type="text/css">  
    <link href="css/live.css" rel="stylesheet" type="text/css">             	   	   
</head>
<script type="text/javascript">
window.print();
</script>
<body>	
    <h1>    	
    	Datos de la transferencia    	   
    </h1>
                	    		      		    		
   	<table id="formulario"> 				  		    		    		   		  
       			<tr>
    				<td><label>
    					N° Transferencia:
    					</label>  	
    				</td>
    				<td >
    					<bean:write name="trx" property="numero"/>
    				</td>
    			</tr>
    			<tr>
    				<td>
    					<label>Estado:</label>
    				</td>
    				<td>
    					<bean:write name="trx" property="estado.nombre"/>
    				</td>
    			</tr>
    			<tr>
    				<td>
    					<label>Fecha:</label>
    				</td>
    				<td>
    					<fmt:formatDate pattern="dd/MM/yyyy"
                  			value="${trx.fechaRegistro}" />
    				</td>
    			</tr>
    			<tr>
	    			<td>
	    				<label>	    					
	    					Registrado por:
	    				</label>    				
	    			</td>    			
	    			<td>            			
	    				 <c:out value="${trx.nombreUsuarioRegistro}"/>  <fmt:formatDate pattern="hh:mm:ss" value="${trx.fechaRegistro}" />
	    			</td>
	    		</tr>	    		
	    		<c:forEach var="accion" items="#{trx.acciones}">

	    		
	        		<tr>
		    			<td>
		    				<label>
	    					<c:choose>		    		
	    							
			    				 <c:when test="${accion.tipoMovimientoTransferencia.codigo == 'A'.charAt(0)}">
			    				 	Aprobado		    				 
			    				 </c:when>
			    				
			    				 <c:when test="${accion.tipoMovimientoTransferencia.codigo == 'R'.charAt(0)}">
			    				 	Rechazado		    				 
			    				 </c:when>
			    				 <c:otherwise>
			    				 	&nbsp;
			    				 </c:otherwise>
		    				 </c:choose>
		    					 &nbsp; por:
		    					 
		    				</label>   				
		    			</td>    			
		    			<td>            			
		    				 <c:out value="${accion.nombreUsuario}"/>  <fmt:formatDate pattern="hh:mm:ss" value="${accion.fecha}" />
		    			</td>
		    		</tr>	
	    		
	    		
      			</c:forEach>	  		
	    		<tr>
	    			<td>
	    				<label>	    					
	    					Referencia:
	    				</label>    				
	    			</td>    			
	    			<td>            			
	    				 <c:out value="${trx.referencia}"/> 
	    			</td>
	    		</tr>	    		    			
	    		<tr>
	    			<td>
	    				<label>	    					
	    					<u><b>Cuenta de origen:</b></u>
	    				</label>    				
	    			</td>    				    			
	    		</tr>
	    		<tr>
	    			<td>
	    				<label>	    					
	    					Tipo y Número:
	    				</label>    				
	    			</td>    			
	    			<td>            			
	    				 <c:out value="Cuenta ${trx.descripcionCuentaCargo} -  ${trx.cuentaCargoFormateado}"/> 
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>
	    				<label>	    					
	    					Monto a transferir:
	    				</label>    				
	    			</td>    			
	    			<td>            			
	    				 <c:out value="${trx.moneda}"/>  <fmt:setLocale value="en_US"/><fmt:formatNumber type="number"  value="${trx.monto}" pattern="##################0.00" />
	    			</td>
	    		</tr> 	    			
    			<tr>
	    			<td>
	    				<label>	    					
	    					<u><b>Cuenta de destino:</b></u>
	    				</label>    				
	    			</td>    				    			
	    		</tr>
    			<tr>
	    			<td>
	    				<label>
	    					<c:if test="${trx.codigoTipo =='CP' or trx.codigoTipo =='CT'}">	    						    				
	    						Tipo y Número:
	    					</c:if>							
							<c:if test="${trx.codigoTipo =='IT'}">	    						    				
	    						Número
							</c:if>
	    				</label>  				
	    			</td>    			
	    			<td> 
	    				<c:if test="${trx.codigoTipo =='CP' or trx.codigoTipo =='CT'}">
							<c:out value="Cuenta ${trx.descripcionCuentaAbono} - ${trx.cuentaAbonoFormateado}"/>
	    				 </c:if>        			
	    				 <c:if test="${trx.codigoTipo =='IT'}">
	    				 	<c:out value="${trx.cuentaAbono}"/>
	    				 </c:if>	    				 
	    			</td>
	    		</tr>
	    		<c:if test="${trx.codigoTipo =='IT'}">
	    		<tr>
	    			<td>
	    				<label>	    					
	    					Banco:
	    				</label>    				
	    			</td>    			
	    			<td>            			
	    				 <c:out value="BANCO ${trx.nombreBanco}"/>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td>
	    				<label>	    					
	    					Cuenta propia:
	    				</label>    				
	    			</td>    			
	    			<td>            			
	    				 <c:out value="${trx.labelMismo}"/>
	    			</td>
	    		</tr>
	    		</c:if>
	    		<c:if test="${trx.codigoTipo =='IT' or trx.codigoTipo =='CT'}">	    			    		    			
    				<tr>
	    				<td>
	    					<label>Titular</label>
	    				</td>
	    				<td colspan="3">
	    					<bean:write name="trx" property="apellidoPaterno"/>&nbsp;
	    					<bean:write name="trx" property="apellidoMaterno"/>&nbsp;
	    					<bean:write name="trx" property="nombres"/>	    					
	    				</td>
	    			</tr>
	    			<tr>
	    				<td>
	    					<label>ID Titular</label>
	    				</td>
	    				<td>
	    					<c:out value="${trx.documento} ${trx.nroDocumento}"/>
	    				</td>	    					    				
	    			</tr>    				    				
	    		</c:if>
	    		<c:if test="${trx.codigoTipo =='IT'}">
	    		<tr>
	    			<td>
	    				<label>	    					
	    					Dirección
	    				</label>    				
	    			</td>    			
	    			<td>            			
	    				 <c:out value="${trx.direccion}"/>
	    			</td>
	    		</tr>
	    		</c:if>	    		
	    		<tr>
	    			<td>
	    				<label>	    					
	    					Monto a abonar:
	    				</label>    				
	    			</td>    			
	    			<td>            			
	    				 <c:out value="${trx.simboloMonedaAbono}"/> <fmt:setLocale value="en_US"/><fmt:formatNumber type="number"  value="${trx.montoAbonado}" pattern="##################0.00" />
	    			</td>
	    		</tr> 	    			    	
    		</table>
    	   		    		       

</body>
</html>

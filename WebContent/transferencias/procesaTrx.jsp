<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/cache/cache.jsp" %>
<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>
<html>
<head>    
    <title>Transferencias</title> 
    <link href="css/formulario.css" rel="stylesheet" type="text/css">
    <style>  		
  		#wait{
  			color: blue;
  			font-size: 14px;
  		}
  </style>
    <link href="css/live.css" rel="stylesheet" type="text/css">    
    <script type="text/javascript" src="js/cash.js"></script>
    <script type="text/javascript" src="js/jquery.js"></script>      	   
</head>
<body>	

<p/>

	<html:form action="transferencias.do" onsubmit="return false;">
	<c:if test="${requestScope.origen=='confirmacion' or requestScope.origen=='procesoNoOk' or requestScope.origen=='procesoOk'}">	
    	<h1>			
			<c:if test="${transferenciasform.tipo=='CP'}">
		    	<fmt:message key="transferencias.title.ctapropias"/>
		    </c:if>	    		
		   	<c:if test="${transferenciasform.tipo=='CT'}">
		    	<fmt:message key="transferencias.title.ctaterceros"/>	
		    </c:if>
		    <c:if test="${transferenciasform.tipo=='IT'}">
		    	<fmt:message key="transferencias.title.ctainterbco"/>			
		    </c:if>
	  	</h1>	  	
	 </c:if>	       
    	<fieldset>
		    
		    	<div align="right">    		
    			<table id="headEmpresa">
	    			<c:if test="${sessionScope.nombre_empresa_seleccionada != null}">
		    			<tr>
		    				<td>Empresa:</td>
		    				<td><c:out value="${sessionScope.nombre_empresa_seleccionada}"/></td>
		    			</tr>
		    		</c:if>
		    		<c:if test="${!(sessionScope.usuarioActual.m_Nombre == null and sessionScope.usuarioActual.m_Apellido == null)}">
		    			<tr>
		    				<td>Usuario:</td>
		    				<td><c:out value="${sessionScope.usuarioActual.m_Nombre} ${sessionScope.usuarioActual.m_Apellido}"/></td>
		    			</tr>
		    		</c:if>
		    		<c:if test="${sessionScope.usuarioActual.m_NumTarjeta != null}">
		    			<tr>
		    				<td>Nro. Pin:</td>
		    				<td><c:out value="${sessionScope.usuarioActual.m_NumTarjeta}"/></td>
		    			</tr>	    			
		    		</c:if>
	    		</table>
	    		</div>
	    		<c:if test="${requestScope.origen == 'confirmacion'}">	    		
		    		<h3 id="ingresarDatos">Confirmar datos ingresados</h3> 
		    	</c:if>

		   	<!-- Exito. No hay error -->    	
    		<c:if test="${error == null and requestScope.mensaje != null}">
    			<h3 class="sucess">    				    				    				
    				<c:out value="${requestScope.mensaje}"></c:out>
    			</h3>
    		</c:if>
    		<!-- Fin exito -->
	    	<c:if test="${requestScope.origen=='verPendiente' or requestScope.origen=='verTrx'}">
				<h3 id="datosTrx">
			    	Datos de la transferencia
			    </h3>
	   		</c:if>
	   		<!-- Error -->
   			<logic:notEmpty name="error">
   				<h3 class="error_h3">    				
   					<bean:write name="error" scope="request"/>    			
   				</h3>
	   			<logic:notEmpty name="error_descripcion">
					<h4 class="error_h4">
						<bean:write name="error_descripcion" scope="request"/>
					</h4>
	    		</logic:notEmpty>
    		</logic:notEmpty>    
    		
    		  		    			
    		<c:if test="${requestScope.origen=='procesoOk' or requestScope.origen=='aprobacion'}">    			   		    		    	
    			<input type="submit" id="exportPdf" value="Guardar en pdf" onClick="javascript:generarReporte('registroTransferencia','pdf','<c:out value="${trx.numero}"/>','<c:out value="${sessionScope.param_id_empresa}"/>')"/ >
    			<input type="submit" id="exportExcel" value="Guardar en excel" onClick="javascript:generarReporte('registroTransferencia','xls','<c:out value="${trx.numero}"/>','<c:out value="${sessionScope.param_id_empresa}"/>')"/>
    			<input type="submit" id="imprimir_2" value="Imprimir" onClick="javascript:imprimirHtml('<c:out value="${trx.numero}"/>','<c:out value="${sessionScope.param_id_empresa}"/>')" / >    			   		    	
    		</c:if>
    		<c:if test="${requestScope.origen=='verTrx'}">    			   		    		    	
    			<input type="submit" id="exportPdf" value="Guardar en pdf" onClick="javascript:generarReporte('registroTransferencia','pdf','<c:out value="${trx.numero}"/>','<c:out value="${trx.idEmpresa}"/>')"/ >
    			<input type="submit" id="exportExcel" value="Guardar en excel" onClick="javascript:generarReporte('registroTransferencia','xls','<c:out value="${trx.numero}"/>','<c:out value="${trx.idEmpresa}"/>')"/>
    			<input type="submit" id="imprimir_2" value="Imprimir" onClick="javascript:imprimirHtml('<c:out value="${trx.numero}"/>','<c:out value="${trx.idEmpresa}"/>')" / >    			   		    	
    		</c:if>
    		
    		
    		    		
    		<table id="formulario"> 		  		    		    		   		  
    		<c:if test="${requestScope.origen=='procesoOk' or requestScope.origen=='verPendiente' 
    				or requestScope.origen=='aprobacion' or requestScope.origen=='verTrx'}">
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
	    		</c:if>
	    		
	    		
	    		
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
	    				 <c:out value="${trx.simboloMonedaAbono}"/> <fmt:setLocale value="en_US"/>
	    				 <fmt:formatNumber type="number"  value="${trx.montoAbonado}" pattern="##################0.00" />&nbsp;
	    				 

	    				 <div id="indicadorAvisoMontosEquivalentes">	    				 
		    				 <c:if test="${trx.estado.nombre=='Pendiente' and trx.tipoTransferenciaPorMoneda.value!='1'}">
		    				 	(*)
		    				 </c:if>
	    				 </div>	 	    				     				 	    			
	    			</td>
	    		</tr>
	    	
	    		
	    		 	    			    	
    		</table> 		
    		
    		
    		<c:if test="${requestScope.origen=='procesoOk' or requestScope.origen=='procesoNoOk'}">
    			<input type="submit" id="registrar_l" value="Nueva Transferencia" onClick="javascript:iniciar('<c:out value="${transferenciasform.tipo.name}"/>')"/>        
   				<input type="submit" id="cancelar_l" value="Menu Principal" onClick="javascript:menu()"/>
   				            			
    		</c:if>
    		<c:if test="${requestScope.origen=='confirmacion'}">
    			<input type="submit" id="registrar" value="Registrar transferencia" onclick="javascript:procesar();"/>   			           	 
        		<input type="submit" id="cancelar" value="Cancelar" onClick="javascript:iniciar('<c:out value="${transferenciasform.tipo.name}"/>')"/>        		   		
    		</c:if>
    		<c:if test="${requestScope.origen=='verPendiente'}">
    			<input type="submit" id="aprobar" value="Aprobar" onclick="javascript:aprobar_1('<bean:write name="trx" property="numero"/>');"/>        
				<input type="submit" id="rechazar" value="Rechazar" onclick="javascript:rechazar_1('<bean:write name="trx" property="numero"/>');"/>        
				<input type="submit" id="imprimir" value="Salir" onclick="javascript:volver()"/>        
    		</c:if>
    		<c:if test="${requestScope.origen=='aprobacion'}">
    			<input type="submit" id="registrar" value="Aceptar" onClick="javascript:aceptarConsultaPendientes()"/>
    		</c:if>
    		<c:if test="${requestScope.origen=='verTrx'}">
    			<input type="submit" id="registrar" value="Aceptar" onClick="javascript:aceptarConsultaTrxs()"/>
    		</c:if>
    		
    		<br>
    		<br>
    		<!-- no esta procesado y no es misma moneda -->
    		<div id="avisoMontosEquivalentes">	    				 
			<c:if test="${trx.estado.nombre=='Pendiente' and trx.tipoTransferenciaPorMoneda.value!='1'}">
			 	<fmt:message key="transferencias.msg.informacionMontosEquivalentes"/>
			 </c:if>
			 </div>
    </fieldset>
</html:form>
<script type="text/javascript">
	function iniciar(tipo) {
		var frm = document.forms[0];
		var strTarget='transferencias.do?do=iniciarTrxCtaPropia';        	
		if( tipo == 'CT' ){
			strTarget='transferencias.do?do=iniciarTrxCtaTerceros';  
		}
		if( tipo == 'IT' ){
			strTarget='transferencias.do?do=iniciarTrxInterbancarias';  
		}	        	 
		submit(frm,strTarget); 	    
	}

	function menu() {
		var frm = document.forms[0];  
		var strTarget='login.do?do=obtenerOpcionesEmpresas';   
		submit(frm,strTarget); 	    
	}
	
	function procesar() {
		var frm = document.forms[0];  
		var strTarget='transferencias.do?do=procesar';        	
		$("<p id=\"wait\">Espere...</p>").appendTo("body");
		$("#registrar").attr('disabled','disabled');
		$("#registrar").css('color','inactivecaptiontext');
		$("#registrar").css('background-color','inactivecaption');
		$("#registrar").css('border-color','inactiveborder');
		$("#cancelar").attr('disabled','disabled');
		$("#cancelar").css('color','inactivecaptiontext');
		$("#cancelar").css('background-color','inactivecaption');
		$("#cancelar").css('border-color','inactiveborder');
		submit(frm,strTarget);     	
	}
	
    function aprobar_1(id) {
		var frm = document.forms[0];
		var strTarget='cTransferencias.do?do=aprobarTransferencia&idTransferencia='+id;
		$("<p id=\"wait\">Espere...</p>").appendTo("body");
		$("#aprobar").attr('disabled','disabled');
		$("#aprobar").css('color','inactivecaptiontext');
		$("#aprobar").css('background-color','inactivecaption');
		$("#aprobar").css('border-color','inactiveborder');
		$("#rechazar").attr('disabled','disabled');
		$("#rechazar").css('color','inactivecaptiontext');
		$("#rechazar").css('background-color','inactivecaption');
		$("#rechazar").css('border-color','inactiveborder');
		$("#imprimir").attr('disabled','disabled');
		$("#imprimir").css('color','inactivecaptiontext');
		$("#imprimir").css('background-color','inactivecaption');
		$("#imprimir").css('border-color','inactiveborder');
		submit(frm,strTarget); 	    
	}
	
	function volver() {
		var frm = document.forms[0];
		var strTarget='cTransferencias.do?do=cargarTrxPendientes';                			        	
		submit(frm,strTarget);
	}    
	   
	function rechazar_1(id) {
		var frm = document.forms[0];
		var strTarget='cTransferencias.do?do=rechazarTransferencia&idTransferencia='+id;
		$("<p id=\"wait\">Espere...</p>").appendTo("body");
		$("#rechazar").attr('disabled','disabled');
		$("#rechazar").css('color','inactivecaptiontext');
		$("#rechazar").css('background-color','inactivecaption');
		$("#rechazar").css('border-color','inactiveborder');
		$("#aprobar").attr('disabled','disabled');
		$("#aprobar").css('color','inactivecaptiontext');
		$("#aprobar").css('background-color','inactivecaption');
		$("#aprobar").css('border-color','inactiveborder');
		$("#imprimir").attr('disabled','disabled');
		$("#imprimir").css('color','inactivecaptiontext');
		$("#imprimir").css('background-color','inactivecaption');
		$("#imprimir").css('border-color','inactiveborder');
		submit(frm,strTarget); 	    
	}
	function aceptarConsultaPendientes() {
	var frm = document.forms[0];
	var strTarget='cTransferencias.do?do=cargarTrxPendientes';	                			        	
	submit(frm,strTarget); 	    
}
	function aceptarConsultaTrxs() {
	var frm = document.forms[0];
	var strTarget='cTransferencias.do?do=volverConsultaTransferencias';	                			        	
	submit(frm,strTarget); 	    
}
	function generarReporte(nombreReporte,formatoReporte,idTrx, idEmpresa){
		window.open('<%=request.getContextPath()%>/ReportServlet?reportName='+nombreReporte+'&reportType='+formatoReporte+'&idTrx='+idTrx+'&idEmpresa='+idEmpresa,
				'mywindow','toolbar=no,menubar=no,location=no,status=no,url=no,resizable=yes,width=450,'+
				'height=600,top=300,left=200');   			
	}
	function imprimirHtml(idTrx, idEmpresa){
		window.open('<%=request.getContextPath()%>/cTransferencias.do?do=exportarDetallesImpresion&idTrx='+idTrx+'&idEmpresa='+idEmpresa,
				'mywindow','toolbar=no,menubar=no,location=no,status=no,url=no,resizable=yes,width=450,'+
			'height=600,top=300,left=200');
	}   		
</script>
</body>
</html>


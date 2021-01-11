<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List" %>
<c:set var="now" value="<%=new java.util.Date()%>"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="0">
       	<title>Consulta de transferencias</title>
       	<link href="css/tabla.css" rel="stylesheet" type="text/css">
       	<link href="css/formulario.css" rel="stylesheet" type="text/css">
    	<script language="javascript" src="js/cash.js"></script>
    	<style type="text/css">@import url(calendario/calendar-system.css);.Estilo2 {font-size: 10pt} .Estilo3 {font-size: 12pt}
  		</style>
  		<script LANGUAGE="javascript" SRC="js/Functions.js"></script>
  		<script type="text/javascript" src="config/javascript.js"></script>
  		<script type="text/javascript" src="calendario/calendar.js"></script>
  		<script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  		<script type="text/javascript" src="calendario/calendar-setup.js"></script>
    	<script language="JavaScript" type="text/javascript">
    		function consultar() {
            	var frm = document.forms[0];
            	var fecIni = document.getElementById("fechaInicial").value;
                var fecFin = document.getElementById("fechaFinal").value;
                if(!validaRangoFechasComprobantes(fecIni,fecFin)){
                 	return;
                }
            	var strTarget='cTransferencias.do?do=buscarTransferencias';            	
            	submit(frm,strTarget);     	    
        	}

    		function buscar(id,idEmpresa) {  		
            	var frm = document.forms[0];  
            	var strTarget='cTransferencias.do?do=buscarTransferencia&idTransferencia='+id+'&idEmpresa='+idEmpresa;    
            	submit(frm,strTarget);
        	}
    		
    		function paginar(valor){       
    	        location.href = valor;
    	    }
    		
    		function updateDateFin(cal) {
                var field_0 = document.getElementById("fechaFinal").value;
                var fecFin = field_0.substr(6, 4) + field_0.substr(3, 2) + field_0.substr(0, 2);
                var date = cal.date;
                var fecSel = date.print("%Y%m%d");
                if (  fecSel > fecFin ) {
                    var field = document.getElementById("fechaInicial");
                    field.value = field_0.substr(0, 2)+'/' +field_0.substr(3, 2)+'/' + field_0.substr(6, 4);
                }
            }
    		
            function updateDateIni(cal) {
                var field_0 = document.getElementById("fechaInicial").value;
                var fecIni = field_0.substr(6, 4) + field_0.substr(3, 2) + field_0.substr(0, 2);
                var date = cal.date;
                var fecSel = date.print("%Y%m%d");
                if (  fecSel < fecIni ) {
                    var field = document.getElementById("fechaFinal");
                    field.value = field_0.substr(0, 2)+'/' +field_0.substr(3, 2)+'/' + field_0.substr(6, 4);
                }
            }
            
            function deshabilitarFechasPosterioresHoy(date) {            	
                var fecActualMax = '<fmt:formatDate pattern="yyyyMMdd" value="${now}"/>';                
                var fecSel = date.print("%Y%m%d");                
                if( fecSel > fecActualMax){         	
                    return true;
                }
                return false;
             }
            
            function generarReporte_1(nombreReporte,formatoReporte,idEmpresa,tipoTransferencia, tipoDocumento,nroDocumento,
            		estadoTransferencia,codigoMoneda,fechaIni,fechaFin){
            	window.open('<%=request.getContextPath()%>/ReportServlet?reportName='+nombreReporte+'&reportType='+formatoReporte+
            			'&idEmpresa='+idEmpresa+'&tipoTransferencia='+tipoTransferencia+'&tipoDocumento='+tipoDocumento+
            			'&nroDocumento='+nroDocumento+'&estadoTransferencia='+estadoTransferencia+'&codigoMoneda='+codigoMoneda+
            			'&fechaIni='+fechaIni+'&fechaFin='+fechaFin,
        				'mywindow','toolbar=no,menubar=no,location=no,status=no,url=no,resizable=yes,width=450,'+
        				'height=600,top=300,left=200'); 
            }
    	</script>
    			    
    </head>      
    <body>       
    	<html:form action="cTransferencias.do" onsubmit="return false;">
    		<h1><fmt:message key="transferencias.title.consultaTrxs"/></h1>
    		<table id="formulario">
	    			<logic:notEmpty name="empresasTransferencias">
	    				<tr>
	    					<td>
	    						<label>Empresas</label>
	    					</td>
	    					<td colspan="3">
	    						<html:select property="empresa">	    							
									<html:options property="value" labelProperty="label" collection="empresasTransferencias"  /> 
								</html:select>
	    					</td>
	    				</tr>	    			
	    			</logic:notEmpty>
	    			<tr>
	    				<td ><label>Tipo transferencia</label></td>
	    				<td >
	    					<html:select property="tipoTransferencia">
	    						<html:option value="">Todos</html:option>								
								<html:options property="value" labelProperty="label" collection="tiposTransferencia"  /> 
							</html:select>
	    				</td>
	    			</tr>
	    			<tr>
	    				<td ><label>Tipo documento</label></td>
	    				<td >
	    					<html:select property="documento">
	    						<html:option value="">Todos</html:option>					
								<html:options property="value" labelProperty="label" collection="tiposDocumento"  /> 				
							</html:select>
						</td>
						<td ><label>Numero</label></td>    				
	    				<td >
	    					<html:text property="nroDocumento" onkeypress="numero()" maxlength="11"></html:text>	
	    				</td>   				
	    			</tr>
	    			<tr>
	    				<td><label>Estado</label></td>
	    				<td>	    					
							<html:select property="estado">
								<html:option value="" >Todos</html:option>
								<html:options property="value" labelProperty="label" collection="estadosTransferencia"  /> 
							</html:select>						
	    				</td>
	    				<td><label>Moneda</label></td>
	    				<td>	    					
							<html:select property="moneda">
								<html:option value="">Todos</html:option>
								<html:options property="value" labelProperty="label" collection="tiposMoneda"  /> 
							</html:select>						
	    				</td>  					    				
	    			</tr>
	    			<tr>
	    				<td><label>Desde:</label> </td>
            			<td>
                		<html:text property="fechaInicial" styleId="fechaInicial"  maxlength="10" size="10"  readonly="true"/>
                		<button id="btn_ini" name="btn_ini"><img src="img/cal.gif" /></button>
                		<script type="text/javascript">
                    	Calendar.setup(
	                    	{
	                        	inputField : "fechaInicial" // ID campo de entrada
	                        	,ifFormat : "%d/%m/%Y" // Formato de fecha
	                        	,button : "btn_ini" // ID del boton
	                        	,weekNumbers : false // Numero de semanas                        		                        	
	                        	,dateStatusFunc : deshabilitarFechasPosterioresHoy
	                        	,onUpdate : updateDateFin
	                    	}
                		);                    	
                		</script>
            			</td>
            			<td><label>Hasta:</label> </td>
            			<td>
                		<html:text property="fechaFinal" styleId="fechaFinal"  maxlength="10" size="10"  readonly="true"/>
                		<button id="btn_fin" name="btn_fin"><img src="img/cal.gif" /></button>
                		<script type="text/javascript">
                    	Calendar.setup(
	                    	{
	                        	inputField : "fechaFinal" // ID campo de entrada
	                        	,ifFormat : "%d/%m/%Y" // Formato de fecha
	                        	,button : "btn_fin" // ID del boton
	                        	,weekNumbers : false // Numero de semanas                        		                        	
	                        	,dateStatusFunc : deshabilitarFechasPosterioresHoy
	                        	,onUpdate : updateDateIni
	                    	}
                		);                    	
                		</script>
            			</td>    					    				
	    		</tr>
	    	</table>
	    	<input type="submit" id="registrar" value="Consultar" onClick="javascript:consultar()"/>
    		<br/>
    		
    		<c:if test="${requestScope.accion=='busqueda' or requestScope.accion=='volverABusqueda'}">    			    			    					
    			<div>
	    			<logic:notEmpty name="consulta_transferencias">
	    				<input type="submit" id="exportPdf_2" value="Guardar en pdf" onClick="
		    				javascript:generarReporte_1('consultaTransferencias','pdf','<bean:write name="cTransferenciasForm" property="empresa"/>',
		    				'<bean:write name="cTransferenciasForm" property="tipoTransferencia"/>',
		    				'<bean:write name="cTransferenciasForm" property="documento"/>',
		    				'<bean:write name="cTransferenciasForm" property="nroDocumento"/>',
		    				'<bean:write name="cTransferenciasForm" property="estado"/>',
		    				'<bean:write name="cTransferenciasForm" property="moneda"/>',
		    				'<bean:write name="cTransferenciasForm" property="fechaInicial"/>',
		    				'<bean:write name="cTransferenciasForm" property="fechaFinal"/>')"/>
	    				<input type="submit" id="exportExcel_2" value="Guardar en excel" onClick="
		    				javascript:generarReporte_1('consultaTransferencias','xls','<bean:write name="cTransferenciasForm" property="empresa"/>',
		    				'<bean:write name="cTransferenciasForm" property="tipoTransferencia"/>',
		    				'<bean:write name="cTransferenciasForm" property="documento"/>',
		    				'<bean:write name="cTransferenciasForm" property="nroDocumento"/>',
		    				'<bean:write name="cTransferenciasForm" property="estado"/>',
		    				'<bean:write name="cTransferenciasForm" property="moneda"/>',
		    				'<bean:write name="cTransferenciasForm" property="fechaInicial"/>',
		    				'<bean:write name="cTransferenciasForm" property="fechaFinal"/>')"/>
		    		</logic:notEmpty>
    			</div>    			
    			<br>
    			<div id="paginacion">
    				<logic:notEmpty name="consulta_transferencias">
	        			<c:set var="bean_paginacion" value="bean_paginacion_consulta_transferencias"/>
	            		<img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('cTransferencias.do?do=buscarTransferencias&tipoPaginado=P&esPag=si');"/>
	            		<img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('cTransferencias.do?do=buscarTransferencias&tipoPaginado=A&esPag=si');"/>
	            		P&aacute;gina  <c:out value="${sessionScope[bean_paginacion].m_pagActual}" /> de <c:out value="${sessionScope[bean_paginacion].m_pagFinal}" />
	            		<img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('cTransferencias.do?do=buscarTransferencias&tipoPaginado=S&esPag=si');"/>
	            		<img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('cTransferencias.do?do=buscarTransferencias&tipoPaginado=U&esPag=si');"/>
            		</logic:notEmpty>                       
    			</div>
    			
    			<c:if test="${sessionScope.consulta_transferencias != null}">
					<layout:collection name="consulta_transferencias" title="" 
						 id="transfer"  width="100%" align="center" styleId="tabla">
	      				<layout:collectionItem title="Numero" property="numero"  />      				      				
	      				<layout:collectionItem title="Cuenta Origen" width="15%">
	      					<c:out value="${transfer.moneda} ${transfer.cuentaCargoFormateado}"></c:out>
	      				</layout:collectionItem>      				
	      				<layout:collectionItem title="Cuenta Destino" width="15%">
	      					<c:out value="${transfer.simboloMonedaAbonoConsultas} ${transfer.cuentaAbonoFormateado}"></c:out>
	      				</layout:collectionItem>
	      				<layout:collectionItem title="Monto">
	      				<fmt:setLocale value="en_US"/><fmt:formatNumber type="number"  value="${transfer.monto}" pattern="###################.00" />
	      				</layout:collectionItem>
	      				<layout:collectionItem title="Tipo" property="tipo.nombre"  />
	      				<layout:collectionItem title="Estado" property="estado.nombre"  />      				     				      			
	      				<layout:collectionItem title="Beneficiario" property="nombreBeneficiario"  />
	      				<layout:collectionItem title="Fecha registro">
						<fmt:formatDate pattern="dd/MM/yyyy"
	                  			value="${transfer.fechaRegistro}" />
	      				</layout:collectionItem>      				      				
	      				<layout:collectionItem title="">
	      					<img src="img/ico_ver.png" width="20" height="20" onClick="javascript:buscar('<bean:write name="transfer" property="numero"/>','<bean:write name="cTransferenciasForm" property="empresa"/>')"></img>      					
	      				</layout:collectionItem>        				    		
	      			</layout:collection>
				</c:if>
					
				<logic:empty name="consulta_transferencias">
					<div id="table_msg">No existen transferencias con los criteros seleccionados</div>
				</logic:empty>
				</c:if>						    		
    	</html:form>
    </body>
</html>

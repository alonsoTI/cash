<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="0">
       	<title>Transferencias Pendientes</title> 
       	<link href="css/tabla.css" rel="stylesheet" type="text/css">
       	<link href="css/formulario.css" rel="stylesheet" type="text/css">
    	<script language="javascript" src="js/cash.js"></script>
    	<script language="JavaScript" type="text/javascript">    		
    		function buscar(id) {        		
            	var frm = document.forms[0];  
            	var strTarget='cTransferencias.do?do=buscarTrxPendiente&idTransferencia='+id;    
            	submit(frm,strTarget);     	    
        	}
    		function paginar(valor){       
    	        location.href = valor;
    	     }
    	</script>    	
    </head>
    <body>       
    	<html:form action="cTransferencias.do" onsubmit="return false;">
    		<h1><fmt:message key="transferencias.title.trxsPendientes"/></h1>    			    	
    		<br/>
    		<logic:notEmpty name="transferencias_pendientes">    			    						
    			<div id="paginacion">
            		<img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('cTransferencias.do?do=cargarTrxPendientes&tipoPaginado=P&esPag=si');"/>
            		<img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('cTransferencias.do?do=cargarTrxPendientes&tipoPaginado=A&esPag=si');"/>
            		P&aacute;gina <bean:write name="bean_paginacion_transferencias_pendientes" property="m_pagActual"/> de <bean:write name="bean_paginacion_transferencias_pendientes" property="m_pagFinal"/>
            		<img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('cTransferencias.do?do=cargarTrxPendientes&tipoPaginado=S&esPag=si');"/>
            		<img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('cTransferencias.do?do=cargarTrxPendientes&tipoPaginado=U&esPag=si');"/>
       			</div>
       		</logic:notEmpty>
				<layout:collection name="transferencias_pendientes" title="" 
					 id="transfer"  width="100%" align="center" styleId="tabla">
      				<layout:collectionItem title="Numero" property="numero"  />
      				<layout:collectionItem title="Cuenta Origen" width="15%">
      					<c:out value="${transfer.moneda} ${transfer.cuentaCargoFormateado}"></c:out>
      				</layout:collectionItem>      				
      				<layout:collectionItem title="Cuenta Destino" width="20%">
      					<c:out value="${transfer.simboloMonedaAbonoConsultas} ${transfer.cuentaAbonoFormateado}"></c:out>
      				</layout:collectionItem>
      				<layout:collectionItem title="Monto">
      				<fmt:setLocale value="en_US"/><fmt:formatNumber type="number"  value="${transfer.monto}" pattern="###################.00" />
      				</layout:collectionItem>
      				<layout:collectionItem title="Tipo" property="tipo.nombre"  />
      				<layout:collectionItem title="Beneficiario" property="nombreBeneficiario"  />
      				<layout:collectionItem title="Fecha registro">
					<fmt:formatDate pattern="dd/MM/yyyy"
                  			value="${transfer.fechaRegistro}" />
      				</layout:collectionItem>      				
      				<layout:collectionItem title="">
      					<img src="img/ico_ver.png" width="20" height="20" onClick="javascript:buscar('<bean:write name="transfer" property="numero"/>')"></img>      					
      				</layout:collectionItem>        				    		
      			</layout:collection>					
				<logic:empty name="transferencias_pendientes">
					<div id="table_msg">No existen Transferencias Pendientes de Aprobacion</div>
				</logic:empty>						    		
    	</html:form>
    </body>
</html>

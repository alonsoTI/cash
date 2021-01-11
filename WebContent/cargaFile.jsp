<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.financiero.cash.delegate.TransferenciasDelegate, java.util.Map, org.apache.log4j.Logger" %>
<%! private TransferenciasDelegate delegado =  TransferenciasDelegate.getInstance(); %>
<%! private Logger logger = Logger.getLogger(this.getClass()); %> 
<% try{Map map_limites_transferencias = delegado.obtenerLimitesTransferencias();
 pageContext.setAttribute("map_limites_transferencias", map_limites_transferencias);%>
 
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>Cargar Orden</title>
    <link href="css/Styles.css" rel="stylesheet" type="text/css">
    
    <script LANGUAGE="javascript" SRC="js/Functions.js"></script>
    <script type="text/javascript" src="config/javascript.js"></script>

    <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
    <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
    <META HTTP-EQUIV="Expires" CONTENT="0">

    <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
    #aviso{
			color:#CC0000;
			font-size:11px; 
			padding-bottom: 25px;
			padding-top: 20px;			
			text-align: center;
			font-family: Arial,Helvetica,sans-serif;
		}
	#avisoIT{
			font-size: 9px;
			color: #101693;
			text-align: left;
			font-family: Arial,Helvetica,sans-serif;
		}
	#avisoPagoCCi{
			font-size: 11px;
			color: #101693;
			text-align: center;
			font-family: Arial,Helvetica,sans-serif;
		}
    </style>

    <script type="text/javascript">
        var isLoading = true;
        function subirArchivo(modulo){             
             var divmsj = document.getElementById("uploading_msj");
             if(divmsj!=null){
                 divmsj.style.visibility='hidden';
             }
             document.getElementById('uploading').style.visibility='visible';             
             document.getElementById('btnAceptar').style.visibility='hidden';
             document.getElementById('btnCancelar').style.visibility='hidden';
             var frm = document.forms[0];
             frm.action = "cargarArchivo.do?do=subirFile&modulo="+modulo
             frm.submit();
             verificarCarga(true);
        }
      function verificarCarga(bandera){
            if(isLoading){
                if(bandera){
                    setTimeout("existeCarga('cargarArchivo.do?do=estaCargando&nvaCarga=SI')",1000);
                }else{
                    setTimeout("existeCarga('cargarArchivo.do?do=estaCargando&nvaCarga=NO')",1000);
                }
                
            }
      }
      function existeCarga(url){
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
        if(valor=="SI"){            
            isLoading = true;
            verificarCarga(false);
        }
        else{                        
           isLoading = false;
        }
    }
        
	</script>
</head>
<body>

<html:form action="cargarArchivo.do" onsubmit="return false;" method="POST" enctype="multipart/form-data">
            <input type="hidden" id="idSevxEmp" name="idSevxEmp" value="<%= request.getAttribute("idSevxEmp") %>" />
            <input type="hidden" id="ruc_empresa" name="ruc_empresa" value="<%= request.getAttribute("ruc_empresa") %>" />
    <table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
        <tr>
            <td valign="middle" align="left"><bean:message key="pagos.title.ingresar"/></td>
            <td valign="middle" align="right">&nbsp;</td>
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>
            <td class="CellColRow"><bean:message key="global.empresa"/></td>
            <td class="CellColRow2">
                <bean:write name='objorden' property='m_Empresa'/>
            </td>
            <td class="CellColRow"><bean:message key="global.servicio"/></td>
            <td class="CellColRow2" >
                <bean:write name='objorden' property='m_Servicio'/>
            </td>
        </tr>
        <tr>
            <td class="CellColRow"><bean:message key="global.cuentas"/></td>
            <td class="CellColRow2">
                <bean:write name='objorden' property='norNumeroCuenta'/>
            </td>
            <td class="CellColRow" colspan="2"></td>
        </tr>
        <tr>
            <td class="CellColRow"><bean:message key="global.fechaini"/></td>
            <td class="CellColRow2">
                <bean:write name='objorden' property='forFechaInicio'/>
            </td>
            <td class="CellColRow"><bean:message key="global.fechafin"/></td>
            <td class="CellColRow2">
                <bean:write name='objorden' property='forFechaFin'/>
            </td>
        </tr>
        <tr>
            <td class="CellColRow"><bean:message key="global.horavigencia"/></td>
            <td class="CellColRow2">
                <bean:write name='objorden' property='horHoraInicio'/>
            </td>
            <td class="CellColRow" colspan="2"></td>
        </tr>
        <tr>
            <td class="CellColRow"><bean:message key="global.referencia"/></td>
            <td class="CellColRow2">
                <bean:write name='objorden' property='dorReferencia'/>
            </td>
            <td class="CellColRow" colspan="2"></td>
        </tr>
        <tr>
          <td class="CellColRow"><bean:message key="global.archivodatos"/></td>
          <td colspan="3" class="CellColRow2">		  	
              <input type="file" name="myFile" id="myFile" width="50" onkeypress="return false;" />
          </td>
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
             
        <tr align="right">
          <td colspan="5" class="CellColRow5">            
            <img id="btnCancelar" src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:DoSubmit('mantenerOrdenes.do?do=cargarIngresar');"/>
            <img id="btnAceptar" src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:subirArchivo('<c:out value="${requestScope.modulo}"/>');"/>
          </td>
        </tr>
        <c:if test="${requestScope.mensaje_validacion!=null}">
         <tr>            
                <td colspan='4'>
                <div id="uploading_msj">
                <table cellpadding='2' cellspacing='2' border='2' align='center' width='80%' bordercolor='#0066CC'>
                    <td class='TitleRow3' ><c:out value="${requestScope.mensaje_validacion}"></c:out></td>
                </table>
                </div>
                </td>            
            </tr>
        </c:if>    
    </table>    
    <div id="uploading" style="visibility:hidden">
        <table cellpadding='2' cellspacing='2' align='center' width='80%' height='35' bordercolor='#0066CC'>
           <tr align="center">
                <td class='TitleRow3' >Cargando el archivo ... </td>
           </tr>
           <tr>
               <td><div align="center"><img id="imgBusy" src="img/busy.gif" align="middle"/></div></td>
           </tr>
        </table>
    </div>
    <c:if test="${sessionScope.mo_tiposervicio eq '01' and requestScope.idServicio eq '03'}">
    	<div id="avisoPagoCCi">
    		<fmt:message key="transferencias.msg.avisoPagosProveedoresCCI"/>
    	</div>
    	<div id="aviso">
        		<fmt:message key="transferencias.msg.informacionMontosLimites">
    				<fmt:param value="${pageScope.map_limites_transferencias['limiteDiarioDolares']}"/>
    			 	<fmt:param value="${pageScope.map_limites_transferencias['limiteDiarioSoles']}"/>    			 	
    			</fmt:message>
    			<br>
    			<br>
    			<fmt:message key="transferencias.msg.informacionMontosLimitesIB">
    				<fmt:param value="${pageScope.map_limites_transferencias['montoITSoles']}"/>    				    			 
    			 	<fmt:param value="${pageScope.map_limites_transferencias['montoITDolares']}"/>    			 	
    			</fmt:message>
    	</div>
    	<div id="avisoIT">
    		<fmt:message key="transferencias.msg.it.informacionHorarios"/>
    	</div>
    </c:if>
</html:form>
</body>
</html>
<%}catch(Exception e){logger.error("Error en jsp",e);e.printStackTrace();}%>

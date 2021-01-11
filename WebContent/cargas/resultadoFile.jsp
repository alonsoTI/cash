<%@ include file="/includes.jsp" %>
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
    </style>
<script language="JavaScript">
    function paginar(valor){
        location.href = valor;
    }
</script>

    </head>
    <body>
    <html:form action="cargarArchivo.do" onsubmit="return false;" method="POST">
     <table width="100%" CELLSPACING="0" CELLPADDING="4" class="Title" >
        <tr>
            <td valign="middle" align="left">Resultado:<bean:message key="pagos.title.ingresar"/></td>
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
            <td class="CellColRow"><bean:message key="global.archivodatos"/></td>
            <td class="CellColRow2"><bean:write name='nomMostrar'/></td>
            
        </tr>
    </table>

    <table width="100%" cellpadding="2" cellspacing="2" border="0" >

        <tr align="right">
          <td colspan="5" class="CellColRow5">
            <img id="btnVolver" src="img/bt-volver.png" align="middle" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:DoSubmit('mantenerOrdenes.do?do=cargarIngresar');"/>
          </td>
        </tr>
    </table>
    <table width="100%" cellpadding="2" cellspacing="2" border="0" >
        <tr>

            <logic:notEmpty name="mensaje_validacion">
                <td colspan='4'>
                <table cellpadding='2' cellspacing='2' border='2' align='center' width='80%' bordercolor='#0066CC'>
                    <td class='TitleRow3' ><%= request.getAttribute("mensaje_validacion") %></td>
                </table>
                </td>
            </logic:notEmpty>

            <logic:notEmpty name="listaerrores">
                <td colspan="4">
                    <logic:notEmpty name="beanPagCarga">
                    <table align="center">
                        <tr><td>
                        <img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('cargarArchivo.do?do=resultadoFilePag&tipoPaginado=P&nomMostrar=<bean:write name='nomMostrar'/>');"/>
                        <img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('cargarArchivo.do?do=resultadoFilePag&tipoPaginado=A&nomMostrar=<bean:write name='nomMostrar'/>');"/>
                        P&aacute;gina <bean:write name="beanPagCarga" property="m_pagActual"/> de <bean:write name="beanPagCarga" property="m_pagFinal"/>
                        <img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('cargarArchivo.do?do=resultadoFilePag&tipoPaginado=S&nomMostrar=<bean:write name='nomMostrar'/>');"/>
                        <img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('cargarArchivo.do?do=resultadoFilePag&tipoPaginado=U&nomMostrar=<bean:write name='nomMostrar'/>');"/>
                        </td></tr>
                    </table>
                    </logic:notEmpty>
                    <layout:collection title="validacion.titulo.errores"
							name="listaerrores" styleClass="FORM" id="news" >
							<layout:collectionItem title="validacion.column.linea"
								property="id.nlvaNumLinea" sortable="true" />
							<layout:collectionItem title="validacion.column.error"
								property="dlvaDescripcion" />
							<layout:collectionItem title="Detalle">
								<center>									
                					<c:if test="${not empty news.enlace}">
										<layout:link forward="${news.enlace}"
											property="enlace">
	                    					Ver
	                					</layout:link>
                					</c:if>
								</center>
							</layout:collectionItem>
					</layout:collection> <layout:space/>
                </td>
            </logic:notEmpty>
        </tr>
    </table>

        </html:form>
    </body>
</html>

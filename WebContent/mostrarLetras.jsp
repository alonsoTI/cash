<%-- 
    Document   : mostrarLetras
    Created on : 16/03/2009, 07:25:06 PM
    Author     : jmoreno
    
    letras
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ taglib uri="/WEB-INF/struts-layout.tld" prefix="layout" %>


<logic:empty name="usuarioActual" scope="session">
    <logic:redirect href="cierraSession.jsp"/>
</logic:empty>


<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <link href="css/Styles.css" rel="stylesheet" type="text/css">        
          <script type="text/javascript" src="calendario/calendar.js"></script>
		  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
		  <script type="text/javascript" src="calendario/calendar-setup.js"></script>
		  <script type="text/javascript" src="js/Functions.js"></script>
		  <script type="text/javascript" src="config/javascript.js"></script>

        <style type="text/css">
            body {
                background: url(img/fondo.gif) no-repeat fixed;
                background-position: right;
            }
        </style>

        <script language="JavaScript">
           
            function buscar(){
                var frm = document.forms[0];
                frm.action = "consultarSaldos.do?do=buscarSaldos&tipo=0";
                frm.submit();
            }
            function regresar(){
                var req = "<%= request.getAttribute("reqBack")%>";
                var frm = document.forms[0];
                frm.action = "letras.do?do=buscarPlanilla&from=1&"+req;
                frm.submit();
            }
            function paginar(valor){       
                location.href = valor;
             }
        </script>

    </head>
    <body>
        <html:form action="letras.do">
            <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
                <tr>
                    <td valign="middle" align="left" colspan="4" class="Title"><bean:message key="letras.title.mostrar.letras"/></td>
                </tr>
                <tr align="right">
                    <td colspan="4" class="CellColRow5">
                        <img src="img/bt-volver.png" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:regresar();"/>
                    </td>
                </tr>
                <logic:notEmpty name="listaResult">
					       <tr id="barraFunc">
					          <td colspan="4" align="right">
					          
					            &nbsp;&nbsp;&nbsp;
					             	<img src='img/excel.png' alt="Exportar Excel" align='middle' onClick="javascript:exportar('letras.do?do=exportarPlanillaLetras&from=1', 'save', 'excel');"/>
						            <img src='img/text.png' alt="Exportar Texto" align='middle' onClick="javascript:exportar('letras.do?do=exportarPlanillaLetras&from=1', 'save', 'txt');"/>						           
					          </td>
					        </tr>
					        <tr align="center" id="paginado">
					          <td  colspan="4">
					            <img src='img/bt-fch-allback.png' align='middle' onClick="javascript:paginar('letras.do?do=detallePlanillaLetrasPag&from=1&tipoPaginado=P');"/>
					            <img src='img/bt-fch-back.png' align='middle' onMouseOver="this.src='img/bt-fch-back2.png'" onMouseOut="this.src='img/bt-fch-back.png'" onClick="javascript:paginar('letras.do?do=detallePlanillaLetrasPag&from=1&tipoPaginado=A');"/>
					            P&aacute;gina <bean:write name="beanPag" property="m_pagActual"/> de <bean:write name="beanPag" property="m_pagFinal"/>
					            <img src='img/bt-fch-fwd.png' align='middle' onMouseOver="this.src='img/bt-fch-fwd2.png'" onMouseOut="this.src='img/bt-fch-fwd.png'" onClick="javascript:paginar('letras.do?do=detallePlanillaLetrasPag&from=1&tipoPaginado=S');"/>
					            <img src='img/bt-fch-allfwd.png' align='middle' onClick="javascript:paginar('letras.do?do=detallePlanillaLetrasPag&from=1&tipoPaginado=U');"/>
					          </td>
					        </tr>
					
					  </logic:notEmpty>
            </table>
	       
            <logic:notEmpty name="listaResult">
                <layout:collection name="listaResult" title="letras.title.relacion.letras" styleClass="FORM" id="letra" width="100%" align="center" sortAction="client">
                    <layout:collectionItem title="letras.column.numero.letra" property="m_NumLetra" sortable="true">
                        <center>
                            <bean:write name="letra" property="m_NumLetra" />
                        </center>
                    </layout:collectionItem>

                        <layout:collectionItem title="letras.column.fecha.vencimiento" property="m_FechVenc" sortable="true" >
                            <center>
                                <bean:write name="letra" property="m_FechVenc"/>
                            </center>
                        </layout:collectionItem>
                    <layout:collectionItem title="letras.column.moneda" property="m_Moneda" >
                        <p align="center">
                            <bean:write name="letra" property="m_Moneda"/>
                        </p>
                    </layout:collectionItem>
                    <layout:collectionItem title="letras.column.importe.letra" property="m_ImpLetra" >
                        <p align="right">
                            <bean:write name="letra" property="m_ImpLetra"/>
                        </p>
                    </layout:collectionItem>
                    <layout:collectionItem title="letras.column.numero.girador" property="m_NumUser" >
                        <p align="right">
                            <bean:write name="letra" property="m_NumUser"/>
                        </p>
                    </layout:collectionItem>

                    <layout:collectionItem title="letras.column.nombre.girador" property="m_NomUser" sortable="true"  />

                    <%--layout:collectionItem title="letras.column.tasa" property="m_Tasa"   /--%>
                    <layout:collectionItem title="letras.column.estado" property="m_Estado"   />
                </layout:collection>
                <layout:space/>
                <div id="comp0" style="display:none">ANS</div>
                <div id="comp1" style="display:none">FEC</div>
                <div id="comp5" style="display:none">ANS</div>
            </logic:notEmpty>
            <!--mensaje de error en caso existan-->
            <logic:notEmpty name="mensaje">
                <table cellpadding='2' cellspacing='2' border='0' align='center' width='90%'>
                    <tr>
                        <td class='TitleRow3'><bean:write name="mensaje"/></td>
                    </tr>
                </table>
            </logic:notEmpty>
            <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center" >
                <tr align="right">
                    <td colspan="4" class="CellColRow5">
                        <img src="img/bt-volver.png" onMouseOver="this.src='img/bt-volver2.png'" onMouseOut="this.src='img/bt-volver.png'" onClick="javascript:regresar();"/>
                    </td>
                </tr>
            </table>
            

        </html:form>
    </body>
</html>

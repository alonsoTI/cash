<%-- 
    Document   : historicos
    Created on : 14/09/2010, 12:26:30 PM
    Author     : ANDQUI
--%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*,java.lang.*,java.util.*,java.util.Date,java.text.SimpleDateFormat" errorPage=""%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
            String fecha = "";
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            fecha = sdf.format(new Date());                        
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Consulta de Ordenes - Historicos</title>

        <link rel="stylesheet" type="text/css" media="all"
              href="js/jscalendar/calendar-win2k-cold-1.css" title="win2k-cold-2" />
        <script type="text/javascript" src="js/jscalendar/calendar.js"></script>
        <script type="text/javascript" src="js/jscalendar/lang/calendar-es.js"></script>
        <script type="text/javascript" src="js/jscalendar/calendar-setup.js"></script>
        <script type="text/javascript" src="js/funciones.js"></script>
        <script type="text/javascript" src="js/jquery.js"></script>       
        <script type="text/javascript">

        function listarServicios(){
            var idempresa=document.InputForm.empresa.value;


            $.getJSON("servicioAction.do",{emp: idempresa, ajax: 'true'}, function(j){
                var options = '';
                if(j.length>0){
                    for (var i = 0; i < j.length; i++) {
                        options += '<option value="' + j[i].idservicio + '">' + j[i].servicio + '</option>';
                    }
                }else{
                    options += '<option value="0">--Seleccionar--</option>';
                }
                $("select#servicios").html(options);
            })

        }   
            

         

            function listarOrdenes(){
                var idempresa=document.InputForm.empresa.value;
                var idservicio=document.InputForm.servicios.value;
                var fecha1=document.InputForm.txtFecha1.value;
                var fecha2=document.InputForm.txtFecha2.value;

                mi_array = new Array();
                mi_array[0] = idempresa;
                mi_array[1] = idservicio;
                mi_array[2] = fecha1;
                mi_array[3] = fecha2;                
      
                Pagina('1',mi_array,'capaOrdenes','ordenesAction.do',1);

            }
            function detalles(codigo){
                var url='detallesAction.do?cod='+codigo;
                location.href=url;
                ///var win=window.open(url,"Articulos","height=450,width=800,toolbar=no, location=no, scrollbars=yes, resizable=yes, directories=no,copyHistory=no, menubar=no,status=no,width=350px, height=250px");

            }
        </script>

    </head>
    <body>
        <span STYLE="color: #0062AC; font-size: 14pt">Consulta de Ordenes - Historicos</span>
        <html:form  action="/inputAction"  >
            <table border="0" cellpadding="2" cellspacing="2">
                <tr>
                    <td>
                        Empresa :                    </td>
                    <td>
                        <html:select property="empresa"  style="width:150px;" onchange="listarServicios()" >  
                            <html:option value="0">--Seleccionar--</html:option>
                            <html:optionsCollection name="InputForm" property="empresaList" label="nombres" value="idempresa" />
                        </html:select>                    </td>


                    <td>Servicios :</td>
                    <td><div id="capaServicios">
                            <select name="servicios" id="servicios" style="width:150px;">
                                <option value="0">--Seleccionar--</option>
                            </select>
                        </div>                    </td>

                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>Desde:</td>
                    <td>
                        <table style="border-collapse: collapse;" cellpadding="0"
                               cellspacing="0">
                            <tr>
                                <td><input name="txtFecha1" type="text" id="txtFecha1"
                                           value="<%=fecha%>" size="12" readonly="readonly" /></td>
                                <td><img src="img/calendar.gif" alt="calendario"
                                         name="disp2" border="0" id="disp2" style="cursor: pointer;"
                                         onmouseover="fecha2('txtFecha1',this)" /></td> 
                            </tr>
                        </table>                    </td>


                    <td>Hasta:</td>
                    <td>
                        <table style="border-collapse: collapse;" cellpadding="0"
                               cellspacing="0">
                            <tr>
                                <td><input name="txtFecha2" type="text" id="txtFecha2"
                                           value="<%=fecha%>" size="12" readonly="readonly" /></td>
                                <td><img src="img/calendar.gif" alt="calendario"
                                         name="disp2" border="0" id="disp2" style="cursor: pointer;"
                                         onmouseover="fecha2('txtFecha2',this)" /></td>
                            </tr>
                        </table>                    </td>
                    <td>                      <input name="btnBuscar" type="button" id="btnBuscar" value="Buscar" onClick="listarOrdenes()">                    </td>
                </tr>
            </table>
        </html:form>

        <div id="capaOrdenes"></div>
    </body>
</html>

<%-- 
    Document   : tipoCambio
    Created on : 09-dic-2008, 10:09:54
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
  <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Expires" CONTENT="0">

  <link href="css/Styles.css" rel="stylesheet" type="text/css">
  <script>
    /******************* AJAX *******************/
    /********************************************/
    var req;
    var d;

    ////////////////////////////////////////////////////////////////////////////
    //para obtener el detalle de un terminal usando ajax
    function actualizarTipoCambio(){
      var url = "tipoCambio.do?do=cambioActualAJAX";
      if (window.XMLHttpRequest) { // Non-IE browsers
        req = new XMLHttpRequest();
        req.onreadystatechange = processActTipoCambio;
        try {req.open("GET", url, true);}
        catch (e) {alert(e);}
        req.send(null);
      } else if (window.ActiveXObject) { // IE
        req = new ActiveXObject("Microsoft.XMLHTTP");
        if (req) {
          req.onreadystatechange = processActTipoCambio;
          req.open("GET", url, true);
          req.send();
        }
      }
    }
    function processActTipoCambio(){ //este metodo se ejecuta a medida que va cargando el XML.
      if (req.readyState==4){
        if (req.status==200) parseTipoCambio(); //el requerimiento ha sido satisfactorio
        else{
            //alert("Error en Ajax: processActTipoCambio");
            document.getElementById("bodyTipoCambio").innerHTML = "El tipo de Cambio no se encuentra disponible";
        }
      }
    }
    function parseTipoCambio(){
      //obtenemos el XML
      var response = req.responseXML;
      if(response==null){
          //alert("Error en Ajax: parseTipoCambio");
          document.getElementById("bodyTipoCambio").innerHTML = "El tipo de Cambio no se encuentra disponible";
      }
      
      //obtenemos el detalle del terminal
      var tipoCambio = response.getElementsByTagName("cambio")[0];

      //obtenemos la compra y la venta del tipo de cambio
      var m_Compra = tipoCambio.getAttribute("m_Compra");
      var m_Venta = tipoCambio.getAttribute("m_Venta");
      
      //insertamos en nuestro HTML los valores obtenidos del xml
      document.getElementById("bodyTipoCambio").innerHTML = 
            "Tipo de Cambio:" +
            "&nbsp;" +
            "Compra" +
            "&nbsp;" +
            "S/." + m_Compra +
            "&nbsp;:::&nbsp;" +
            "Venta" +
            "&nbsp;" +
            "S/." + m_Venta;
    }
    
    //actualizamos el tipo de cambio con el tiempo configurado(pasar milisegundos a minutos)
    setInterval("actualizarTipoCambio()", (<bean:write name="timeTipoCambio" /> * 60000));
    ////////////////////////////////////////////////////////////////////////////
    
  </script>
</head>
<body>
<form>
<table align='right' width='100%' border='0' cellpadding='0' cellspacing='0'>
<tr valign='top' align="right">
    <td class='CellColRowFecha' valign='top'>
        <span id="bodyTipoCambio">
            
            <logic:empty name="tipoCambio">
                El tipo de Cambio no se encuentra disponible
            </logic:empty>
            <logic:notEmpty name="tipoCambio">
            
                Tipo de Cambio:&nbsp;
                Compra&nbsp;S/.<bean:write name="tipoCambio" property="m_Compra"/>
                &nbsp;:::&nbsp;
                Venta&nbsp;S/.<bean:write name="tipoCambio" property="m_Venta"/>
            </logic:notEmpty>
        </span>
    </td>
</tr>
</table>
</form>
</body>
</html>
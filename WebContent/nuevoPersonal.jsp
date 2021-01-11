<%--
    Document   : consultaMovHistorico
    Created on : 04-dic-2008, 9:44:38
    Author     : jwong
--%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:empty name="usuarioActual" scope="session">
  <logic:redirect href="cierraSession.jsp"/>
</logic:empty>

<html>
<head>
  <title></title>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <link href="css/Styles.css" rel="stylesheet" type="text/css">

  <style type="text/css">@import url(calendario/calendar-system.css);</style>
  <script type="text/javascript" src="calendario/calendar.js"></script>
  <script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
  <script type="text/javascript" src="calendario/calendar-setup.js"></script>
  <script type="text/javascript" src="config/javascript.js"></script>

  <script type="text/javascript" src="js/Functions.js"></script>

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
    <!--
    <logic:notEmpty name="msjExistPersonal">
        alert("<bean:write name='msjExistPersonal'/>");
    </logic:notEmpty>
    
	function imprimir(){
	  window.print();
	}
	function exportar(){
		location.href = "exportarExcel.html";
	}
	
    function aceptar(){
        //debemos validar el ingreso correcto de datos para el ultimo registro
        var valido = validarIngreso(n-1);
        if(valido==true){
            //validamos la longitud de algunos campos antes de ingresarlos
            if(validarLongitud(n-1)){
                //validamos el email de la ultima fila ingresada
                var campoemail = document.getElementById("m_Email_" + (n-1));
                if(!isEmail(campoemail.value)){
                    alert("Formato incorrecto para email");
                    campoemail.focus();
                    campoemail.select();
                }
                else{
                    var m_IdServEmp = document.getElementById("m_idServEmp").value;
                    var m_IdEmpleado = document.getElementById("m_IdEmpleado_" + (n-1)).value;                    
                    //flag que indica si se envia el formulario
                    guardar = true;
                    //verificamos que no exista en la lista de nuevo Personal
                    if(existeIdGrilla(n-1,m_IdEmpleado)){
                        alert("No puede ingresar un ID duplicado");
                        document.getElementById("m_IdEmpleado_" + (n-1)).focus();
                        document.getElementById("m_IdEmpleado_" + (n-1)).select();
                    }else{
                         //verificamos que no exista ya el id de empleado usando AJAX
                         existeIdPersonal("personal.do?do=existeIdEmpleado&m_IdServEmp="+m_IdServEmp+"&m_IdEmpleado="+m_IdEmpleado+"&m_IdTipo=<bean:write name='tipoEntPersonal'/>");
                    }
                }
            }
        }
        else{
            alert("Ingresar datos del personal");
        }
	}

	function cancelar(){
		location.href = "administracion.do?do=cargarMantenimientoPersonal";
	}

    function nuevo(){
		add();
	}

////<para el manejo de crear y eliminar filas>//////////////////////////////////
	var n=1;
    var guardar=false;
    function agregar(){
        n++;
        miTabla = document.getElementById('tabla');
        fila = document.createElement('tr');
        //fila.id = 'contenedor'+n;
        fila.className = 'CellColRow2';
        
        celda = document.createElement('td');
        fila.appendChild(celda);
        novoInput = crearInputText(('m_IdEmpleado_' + (n-1)), ('m_IdEmpleado_' + (n-1)), '10', '10');
        novoInput.onkeypress = function() {numero()}; 
        novoInput.onblur = function() {verificarIdPersonal(this)}; //verificarIdPersonal
        celda.appendChild(novoInput);

        celda = document.createElement('td');
        fila.appendChild(celda);
        novoInput = crearInputText(('m_Nombre_' + (n-1)), ('m_Nombre_' + (n-1)), '50', '50');
        novoInput.style.textTransform = "uppercase";
        novoInput.onkeypress = function() {return soloDescripcionNombre(this, event)}; //"numero()";
        novoInput.onblur = function() {gDescripcionNombre(this)}; //"val_int(this)";
        celda.appendChild(novoInput);

        celda = document.createElement('td');
        fila.appendChild(celda);
        novoInput = crearInputText(('m_DNI_' + (n-1)),('m_DNI_' + (n-1)), '8', '8');
        novoInput.onkeypress = function() {numero()}; //"numero()";
        novoInput.onblur = function() {val_int(this)}; //"val_int(this)";
        celda.appendChild(novoInput);

        celda = document.createElement('td');
        fila.appendChild(celda);
        novoInput = crearInputText(('m_NroCelular_' + (n-1)),('m_NroCelular_' + (n-1)), '15', '15');
        novoInput.onkeypress = function() {numero()}; //"numero()";
        novoInput.onblur = function() {val_int(this)}; //"val_int(this)";
        celda.appendChild(novoInput);

        celda = document.createElement('td');
        fila.appendChild(celda);
        novoInput = crearInputText(('m_Email_' + (n-1)),('m_Email_' + (n-1)), '40', '40');
        //novoInput.onblur = validaEmail(novoInput);
        novoInput.style.textTransform = "uppercase";
        celda.appendChild(novoInput);

        //23/04/2009 jwong nuevo campo
        celda = document.createElement('td');
        fila.appendChild(celda);
        novoInput = crearInputText(('m_Contrapartida_' + (n-1)), ('m_Contrapartida_' + (n-1)), '20', '20');//jmoreno - (50,50)
        novoInput.style.textTransform = "uppercase";
        novoInput.onkeypress = function() {return soloDescripcion(this, event)}; //"numero()";
        novoInput.onblur = function() {gDescripcion(this)}; //"val_int(this)";
        celda.appendChild(novoInput);
        
        //PAGO CTS
        //Forma Pago CTS
        celda = document.createElement('td');
        fila.appendChild(celda);
        selformapago = document.createElement("select");
        selformapago.id = 'm_FormaPago_' + (n-1);
        selformapago.name = 'm_FormaPago_' + (n-1);
        selformapago.className = 'CellColRow8';
        selformapago.onchange = function() {habilitarCta(this)};
        //creamos los combos de la nueva fila
        <logic:notEmpty name="listaTipoPago">
            <logic:iterate id="tipopago" name="listaTipoPago" indexId="h">
                theOption=document.createElement("OPTION");
                theText=document.createTextNode("<bean:write name='tipopago' property='m_Descripcion'/>");
                theOption.appendChild(theText);
                theOption.setAttribute("value","<bean:write name='tipopago' property='m_TipoPagoServicio'/>");
                //add the option to the select
                selformapago.appendChild(theOption);
            </logic:iterate>
        </logic:notEmpty>
        
        <logic:empty name="listaTipoPago">
            selformapago.disabled = true;
        </logic:empty>        
        celda.appendChild(selformapago);
        
        //Tipo de Cuenta CTS
        celda = document.createElement('td');
        fila.appendChild(celda);
        seltipocuenta = document.createElement("select");
        seltipocuenta.id = 'm_TipoCuenta_' + (n-1);
        seltipocuenta.name = 'm_TipoCuenta_' + (n-1);
        seltipocuenta.className = 'CellColRow8';
        //creamos los combos de la nueva fila
        <logic:notEmpty name="listaTipoCuenta">
            <logic:iterate id="tipocuenta" name="listaTipoCuenta" indexId="h">
                theOption=document.createElement("OPTION");
                theText=document.createTextNode("<bean:write name='tipocuenta' property='dlfDescription'/>");
                theOption.appendChild(theText);
                theOption.setAttribute("value","<bean:write name='tipocuenta' property='id.clfCode'/>");
                //add the option to the select
                seltipocuenta.appendChild(theOption);
            </logic:iterate>
        </logic:notEmpty>

        <logic:empty name="listaTipoPago">
            seltipocuenta.disabled = true;
            seltipocuenta.disabled = true;
        </logic:empty>
        celda.appendChild(seltipocuenta);


        celda = document.createElement('td');
        fila.appendChild(celda);
        selmoneda = document.createElement("select");
        selmoneda.id = 'm_Moneda_' + (n-1);
        selmoneda.name = 'm_Moneda_' + (n-1);
        selmoneda.className = 'CellColRow8';
        //creamos los combos de la nueva fila
        <logic:notEmpty name="listaTipoMoneda">
            <logic:iterate id="tipomoneda" name="listaTipoMoneda" indexId="h">
                theOption=document.createElement("OPTION");
                theText=document.createTextNode("<bean:write name='tipomoneda' property='dlfDescription'/>");
                theOption.appendChild(theText);
                theOption.setAttribute("value","<bean:write name='tipomoneda' property='id.clfCode'/>");
                //add the option to the select
                selmoneda.appendChild(theOption);
            </logic:iterate>
        </logic:notEmpty>

        <logic:empty name="listaTipoPago">
            selmoneda.disabled = true;            
        </logic:empty>
        celda.appendChild(selmoneda);

        celda = document.createElement('td');
        fila.appendChild(celda);
        novoInput = crearInputText(('m_NumeroCuenta_' + (n-1)),('m_NumeroCuenta_' + (n-1)), '20', '12');
        novoInput.onkeypress = function() {numero()}; //"numero()";
        novoInput.onblur = function() {verificarCta(this)};
        <logic:empty name="listaTipoPago">
            novoInput.readOnly = true;
            novoInput.disabled = true;
        </logic:empty>
        celda.appendChild(novoInput);

        celda = document.createElement('td');
        fila.appendChild(celda);
        novoInput = crearInputText(('m_Importe_' + (n-1)),('m_Importe_' + (n-1)), '9', '9');
        novoInput.onkeypress = function() {return val_decimal(event)}; //"numero()";
        novoInput.onblur = function() {valida_decimal2digitos(this)}; //"val_int(this)";
        <logic:empty name="listaTipoPago">
            novoInput.readOnly = true;
            novoInput.disabled = true;
        </logic:empty>
        celda.appendChild(novoInput);       
                
        fila.appendChild(celda);
        miTabla.appendChild(fila);
        document.getElementById('nroPersonal').value = n;
        
        habilitarCta(selformapago);
    }
    
    function add() {
        if(n<1){ //si no hay ninguna fila
            agregar();
        }
        else{
            //antes de añadir debemos validar que se hayan ingresado correctamente los datos del ultimo registro
            //validamos el ingreso de los datos de la fila anterior
            var valido = validarIngreso(n-1);
            if(valido==true){
                //validamos la longitud de algunos campos antes de ingresarlos
                if(validarLongitud(n-1)){
                    //validamos el email de la ultima fila ingresada
                    var campoemail = document.getElementById("m_Email_" + (n-1));
                    if(!isEmail(campoemail.value)){
                        alert("Formato incorrecto para email");
                        campoemail.focus();
                        campoemail.select();
                    }
                    else{
                        var m_IdServEmp = document.getElementById("m_IdServEmp").value;
                        var m_IdEmpleado = document.getElementById("m_IdEmpleado_" + (n-1)).value;

                        //flag que indica si se envia el formulario
                        guardar=false;
                        //verificamos que no exista en la lista de nuevo Personal
                        if(existeIdGrilla(n-1,m_IdEmpleado)){
                            alert("No puede ingresar un ID duplicado");
                            document.getElementById("m_IdEmpleado_" + (n-1)).focus();
                            document.getElementById("m_IdEmpleado_" + (n-1)).select();
                        }else{
                            //verificamos que no exista ya el id de empleado usando AJAX
                            existeIdPersonal("personal.do?do=existeIdEmpleado&m_IdServEmp="+m_IdServEmp+"&m_IdEmpleado="+m_IdEmpleado+"&m_IdTipo=<bean:write name='tipoEntPersonal'/>");
                        }
                        
                    }
                }
            }
            else{
                alert("Ingresar datos del personal");
            }
        }
    }
   
    function habilitarCta(campo){
        if(campo.disabled == false){
            var formaPago = campo.options[campo.selectedIndex].value;
            var ncta = document.getElementById("m_NumeroCuenta_"+(n-1));
            var tipoCta = document.getElementById("m_TipoCuenta_"+(n-1));
            if(formaPago == "CRE"){
                ncta.disabled = false;
                tipoCta.disabled = false;
            }else{
                ncta.disabled = true;
                ncta.value = "";
                tipoCta.disabled = true;
            }
        }
        
    }
    function habilitarCuentas(){
        var frm = document.forms[0];       
        var formaPago = document.getElementById("m_FormaPago_0").value;
        if(formaPago == "CRE"){
            frm.m_NumeroCuenta_0.disabled = false;
            frm.m_TipoCuenta_0.disabled = false;
        }else{
            frm.m_NumeroCuenta_0.disabled = true;
            frm.m_NumeroCuenta_0.value = "";
            frm.m_TipoCuenta_0.disabled = true;
        }
    }   
    function existeIdGrilla(ind,idNvoEmp){
        var idEmp = "";
        for(i=0 ;i<ind;i++){
            idEmp = document.getElementById("m_IdEmpleado_" + i).value;
            if(idNvoEmp == idEmp){
                return true;
            }
        }
        return false;
    }
    //valida que se hayan llenado todos los campos de la fila con indice = ind
    function validarIngreso(ind){
        var m_IdEmpleado = document.getElementById("m_IdEmpleado_" + ind);
        var m_Nombre = document.getElementById("m_Nombre_" + ind);
        var m_DNI = document.getElementById("m_DNI_" + ind);
        var m_NroCelular = document.getElementById("m_NroCelular_" + ind);
        var m_Email = document.getElementById("m_Email_" + ind);

        //nuevo campo
        var m_Contrapartida = document.getElementById("m_Contrapartida_" + ind);

        //validamos si ha colocado datos en pago CTS
        //pago de CTS
        var m_FormaPago = document.getElementById("m_FormaPago_" + ind);
        var m_TipoCuenta = document.getElementById("m_TipoCuenta_" + ind);
        var m_Moneda = document.getElementById("m_Moneda_" + ind);
        var m_NumeroCuenta = document.getElementById("m_NumeroCuenta_" + ind);
        var m_Importe = document.getElementById("m_Importe_" + ind);

        if( (m_IdEmpleado.value!=null && m_IdEmpleado.value!="") &&
            (m_Nombre.value!=null && m_Nombre.value!="") &&
            (m_DNI.value!=null && m_DNI.value!="") &&
            (m_NroCelular.value!=null && m_NroCelular.value!="") &&
            (m_Email.value!=null && m_Email.value!="") &&
            (m_Contrapartida.value!=null && m_Contrapartida.value!=""))
        {

            var bandCts = false;
               
                if(m_Importe.value!=""){

                    if(((m_FormaPago.value!=null && m_FormaPago.value=="CRE") &&
                    (m_TipoCuenta.value!=null && m_TipoCuenta.value!="") &&
                    (m_Moneda.value!=null && m_Moneda.value!="") &&
                    (m_NumeroCuenta.value!=null && m_NumeroCuenta.value!="") &&
                    (m_Importe.value!=null && m_Importe.value!=""))
                    ||
                    ((m_FormaPago.value!=null && m_FormaPago.value!="CRE") &&
                    (m_TipoCuenta.value!=null && m_TipoCuenta.value!="") &&
                    (m_Moneda.value!=null && m_Moneda.value!="") &&
                    (m_Importe.value!=null && m_Importe.value!="")))
                    {
                        bandCts = true;
                    }else
                    {
                        return false;
                    }


                }
                if(m_Importe.value==""){
                    return false;
                }
                if(bandCts){
                   return true;
                }else{
                    return false;
                }

        }
        else{
            return false;
        }       
    }
    function verificarCta(campo) {
          val_int(campo)
          var cc_abono = campo.value;
          if(cc_abono.length >= 9){
            while(cc_abono.length < 12){
                cc_abono = "0" + cc_abono;
            }
            campo.value = cc_abono;
            return true;
          }
    }
    function crearInputText(name, id, size, maxlength){
        var miInput = document.createElement('input');
        miInput.type='text';
        miInput.name = name;
        miInput.id = id;
        miInput.size = size;
        miInput.maxLength = maxlength;
        if(name.substr(0, 9) == "m_Importe"){
            miInput.className = 'CellColRowRight';
        }
        else{
            miInput.className = 'CellColRow8';
        }
        
        return miInput;
    }

    function validarLongitud(ind){
        var m_DNI = document.getElementById("m_DNI_" + ind);
        var m_NroCelular = document.getElementById("m_NroCelular_" + ind);
        
        //pago de CTS
        var m_FormaPago = document.getElementById("m_FormaPago_" + ind);
        var m_TipoCuenta = document.getElementById("m_TipoCuenta_" + ind);
        var m_Moneda = document.getElementById("m_Moneda_" + ind);
        var m_NumeroCuenta = document.getElementById("m_NumeroCuenta_" + ind);
        var m_Importe = document.getElementById("m_Importe_" + ind);

        if(m_DNI.value.length<8){
            alert("Ingresar número de DNI de 8 dígitos");
            m_DNI.focus();
            m_DNI.select();
            return false;
        }
        if(m_NroCelular.value.length<8){
            alert("Ingresar número de celular válido");
            m_NroCelular.focus();
            m_NroCelular.select();
            return false;
        }
        
        if(
            (m_FormaPago.value!=null && m_FormaPago.value!="") &&
            (m_TipoCuenta.value!=null && m_TipoCuenta.value!="") &&
            (m_Moneda.value!=null && m_Moneda.value!="") &&
            (m_NumeroCuenta.value!=null && m_NumeroCuenta.value!="") &&
            (m_Importe.value!=null && m_Importe.value!="")){
            if(m_NumeroCuenta.value.length<9){
                alert("Ingresar número de cuenta válida para el pago del servicio");
                m_NumeroCuenta.focus();
                m_NumeroCuenta.select();
                return false;
            }
        }
        return true;
    }
////</para el manejo de crear y eliminar filas>////////////////////////////////
    
    /************************** AJAX **************************/
    function existeIdPersonal(url){
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
        if(valor=="NO"){
            if(guardar==true){ //si es para guardar debemos enviar el formulario
                var frm = document.forms[0];
                desbloquearTodos();//desbloqueamos los combos para guardar los datos
                frm.action = "personal.do?do=guardarPersonal";
                frm.submit();
            }
            else{ //sino es para guardar, agregaremos una fila mas
                bloquear(n-1); //bloqueamos el registro anterior para que no sea modificable
                agregar();
            }
        }
        else{
            alert("Ya existe ese Id de Empleado para esa Empresa");
            document.getElementById("m_IdEmpleado_" + (n-1)).focus();
            document.getElementById("m_IdEmpleado_" + (n-1)).select();
        }
    }

    function bloquear(ind){
        //var m_Empresa = document.getElementById("m_Empresa");
        var m_IdEmpleado = document.getElementById("m_IdEmpleado_" + ind);
        var m_Nombre = document.getElementById("m_Nombre_" + ind);
        var m_DNI = document.getElementById("m_DNI_" + ind);
        var m_NroCelular = document.getElementById("m_NroCelular_" + ind);
        var m_Email = document.getElementById("m_Email_" + ind);

        //nuevo campo
        var m_Contrapartida = document.getElementById("m_Contrapartida_" + ind);
        
        //pago de CTS
        var m_FormaPago = document.getElementById("m_FormaPago_" + ind);
        var m_TipoCuenta = document.getElementById("m_TipoCuenta_" + ind);
        var m_Moneda = document.getElementById("m_Moneda_" + ind);
        var m_NumeroCuenta = document.getElementById("m_NumeroCuenta_" + ind);
        var m_Importe = document.getElementById("m_Importe_" + ind);

        //m_Empresa.disabled = true;
        m_IdEmpleado.readOnly = true;
        m_Nombre.readOnly = true;
        m_DNI.readOnly = true;
        m_NroCelular.readOnly = true;
        m_Email.readOnly = true;
        m_Contrapartida.readOnly = true;

        m_FormaPago.disabled = true;
        m_TipoCuenta.disabled = true;
        m_Moneda.disabled = true;
        m_NumeroCuenta.readOnly = true;
        m_Importe.readOnly = true;
    }

    function desbloquearTodos(){
        for(h=0 ; h<n ; h++){
            //pago de CTS
            var m_FormaPago = document.getElementById("m_FormaPago_" + h);
            var m_TipoCuenta = document.getElementById("m_TipoCuenta_" + h);
            var m_Moneda = document.getElementById("m_Moneda_" + h);

            m_FormaPago.disabled = false;
            m_TipoCuenta.disabled = false;
            m_Moneda.disabled = false;
        }
    }

    function valida_decimal(campo){
        if(campo.value!=""){
            if(!valida_dec(campo.value)){
                alert("El importe debe contener solo un punto decimal");
                campo.select();
                campo.focus();
            }
        }
    }
    function verificarIdPersonal(campo) {//jmoreno
          val_int(campo)
          var idPersonal = campo.value;
          if(idPersonal.length==0){
              return true;
          }else{
            while(idPersonal.length < 10){
                idPersonal = "0" + idPersonal;
            }
            campo.value = idPersonal;
            return true;
          }
     }
    -->
</script>
</head>
<body onload="habilitarCuentas();">
<html:form action="personal.do">
  <table width="100%" CELLSPACING="0" CELLPADDING="4">
    <tr>
      <td valign="middle" align="left" class="Title"><bean:message key="administracion.title.personal"/></td>
    </tr>

    <tr align="left">
      <td>
        <img src="img/bt-aceptar.png" align="middle" onClick="javascript:aceptar();" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'"/>
        <img src="img/bt-nuevo.png" align="middle" onClick="javascript:nuevo();" onMouseOver="this.src='img/bt-nuevo2.png'" onMouseOut="this.src='img/bt-nuevo.png'"/>
		<img src="img/bt-cancelar.png" align="middle" onClick="javascript:cancelar();" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'"/>
      </td>
    </tr>
  </table>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" >
   <tbody id="tabla">
    <tr>
      <td  class="TitleRow6">Empresa:</td>
      <td  class="TitleRow5"><bean:write name="m_DescEmpresa" /></td>
      <td  class="TitleRow6">Servicio:</td>
      <td  colspan="2" class="TitleRow5"><bean:write name="m_Servicio" /></td>
      <td colspan="6" class="TitleRow5"></td>
    </tr>
  	<tr>
      <td class="TitleRow3" colspan="11">Datos del Personal</td>
    </tr>
        
    <tr>
      <td class="CellColRowCenter" rowspan="2">ID empleado</td>
      <td class="CellColRowCenter" rowspan="2">Nombre</td>
      <td class="CellColRowCenter" rowspan="2">DNI</td>
      <td class="CellColRowCenter" rowspan="2">N&uacute;mero de celular</td>
      <td class="CellColRowCenter" rowspan="2">Email</td>
      <td class="CellColRowCenter" rowspan="2">Contrapartida</td>
      <td class="CellColRowCenter" rowspan="1" colspan="5"><bean:write name="m_Servicio" />&nbsp;<logic:empty name="listaTipoPago">(No Habilitado)</logic:empty></td>      
    </tr>
    <tr>      
      <!-- pago cts -->
      <td class="CellColRowCenter">Forma de Pago</td>
      <td class="CellColRowCenter">Tipo de Cuenta</td>
      <td class="CellColRowCenter">Moneda</td>
      <td class="CellColRowCenter">N&uacute;mero de Cuenta</td>
      <td class="CellColRowCenter">Importe</td>
    </tr>
    <html:hidden property="m_Empresa" styleId="m_Empresa"/>
    <html:hidden property="m_IdServEmp"/>
    <tr>
      <td class="CellColRow2">
        <input type="text" id="m_IdEmpleado_0" name="m_IdEmpleado_0" size="10" maxlength="10" class="CellColRow8" onkeypress="numero()" onblur="verificarIdPersonal(this)"/>
        <script>
            document.forms[0].m_IdEmpleado_0.focus();
        </script>
      </td>
      <td class="CellColRow2">
        <input type="text" id="m_Nombre_0" name="m_Nombre_0" style="text-transform:uppercase;" size="50" maxlength="50" class="CellColRow8" onkeypress="return soloDescripcionNombre(this, event)" onblur="gDescripcionNombre(this)"/>
      </td>
	  <td class="CellColRow2">
        <input type="text" id="m_DNI_0" name="m_DNI_0" size="8" maxlength="8" class="CellColRow8" onkeypress="numero()" onblur="val_int(this)"/>
      </td>
      <td class="CellColRow2">
        <input type="text" id="m_NroCelular_0" name="m_NroCelular_0" size="15" maxlength="15" class="CellColRow8" onkeypress="numero()" onblur="val_int(this)"/>
      </td>
	  <td class="CellColRow2">
        <input type="text" id="m_Email_0" name="m_Email_0" style="text-transform:uppercase;" size="40" maxlength="40" class="CellColRow8"/>
      </td>
      <td class="CellColRow2">
        <input type="text" id="m_Contrapartida_0" name="m_Contrapartida_0" style="text-transform:uppercase;" size="20" maxlength="20" class="CellColRow8" onkeypress="return soloDescripcion(this, event)" onblur="gDescripcion(this)"/>
      </td>     
      
      <!-- pago cts -->
      <!-- si tiene habilitado el servicio la empresa -->
      <logic:notEmpty name="listaTipoPago">
          <td class="CellColRow2">
            <select id="m_FormaPago_0" name="m_FormaPago_0" class="CellColRow8" onchange="habilitarCta(this)">
              <logic:notEmpty name="listaTipoPago">
                <logic:iterate id="tipopago" name="listaTipoPago" indexId="h">
                    <option value="<bean:write name='tipopago' property='m_TipoPagoServicio'/>">
                        <bean:write name='tipopago' property='m_Descripcion'/>
                    </option>
                </logic:iterate>
              </logic:notEmpty>
            </select>
          </td>
          <td class="CellColRow2">
            <select id="m_TipoCuenta_0" name="m_TipoCuenta_0" class='CellColRow8' disabled="true">
              <logic:notEmpty name="listaTipoCuenta">
                <logic:iterate id="tipocuenta" name="listaTipoCuenta" indexId="h">
                    <option value="<bean:write name='tipocuenta' property='id.clfCode'/>">
                        <bean:write name='tipocuenta' property='dlfDescription'/>
                    </option>
                </logic:iterate>
              </logic:notEmpty>
            </select>
          </td>
          <td class="CellColRow2">
            <select id="m_Moneda_0" name="m_Moneda_0" class='CellColRow8'>
              <logic:notEmpty name="listaTipoMoneda">
                <logic:iterate id="tipomoneda" name="listaTipoMoneda" indexId="h">
                    <option value="<bean:write name='tipomoneda' property='id.clfCode'/>">
                        <bean:write name='tipomoneda' property='dlfDescription'/>
                    </option>
                </logic:iterate>
              </logic:notEmpty>
            </select>
          </td>
          <td class="CellColRow2">
            <input type="text" id="m_NumeroCuenta_0" name="m_NumeroCuenta_0" maxlength="12" size="20" class='CellColRow8' onkeypress="numero()" onblur="verificarCta(this)" disabled/>
          </td>
          <td class="CellColRow2">
            <input type="text" id="m_Importe_0" name="m_Importe_0" maxlength="9" size="9" class='CellColRowRight' onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)"/>
          </td>
      </logic:notEmpty>

      <!-- si no tiene habilitado el servicio la empresa -->
      <logic:empty name="listaTipoPago">
          <td class="CellColRow2">
            <select id="m_FormaPago_0" name="m_FormaPago_0" class="CellColRow8" disabled>
              <logic:notEmpty name="listaTipoPago">
                <logic:iterate id="tipopago" name="listaTipoPago" indexId="h">
                    <option value="<bean:write name='tipopago' property='m_TipoPagoServicio'/>">
                        <bean:write name='tipopago' property='m_Descripcion'/>
                    </option>
                </logic:iterate>
              </logic:notEmpty>
            </select>
          </td>
          <td class="CellColRow2">
            <select id="m_TipoCuenta_0" name="m_TipoCuenta_0" class='CellColRow8' disabled>
              <logic:notEmpty name="listaTipoCuenta">
                <logic:iterate id="tipocuenta" name="listaTipoCuenta" indexId="h">
                    <option value="<bean:write name='tipocuenta' property='id.clfCode'/>">
                        <bean:write name='tipocuenta' property='dlfDescription'/>
                    </option>
                </logic:iterate>
              </logic:notEmpty>
            </select>
          </td>
          <td class="CellColRow2">
            <select id="m_Moneda_0" name="m_Moneda_0" class='CellColRow8' disabled>
              <logic:notEmpty name="listaTipoMoneda">
                <logic:iterate id="tipomoneda" name="listaTipoMoneda" indexId="h">
                    <option value="<bean:write name='tipomoneda' property='id.clfCode'/>">
                        <bean:write name='tipomoneda' property='dlfDescription'/>
                    </option>
                </logic:iterate>
              </logic:notEmpty>
            </select>
          </td>
          <td class="CellColRow2">
            <input type="text" id="m_NumeroCuenta_0" name="m_NumeroCuenta_0" readonly maxlength="12" size="20" class='CellColRow8' onkeypress="numero()" onblur="verificarCta(this)" disabled="true"/>
          </td>
          <td class="CellColRow2">
            <input type="text" id="m_Importe_0" name="m_Importe_0" readonly maxlength="9" size="9" class='CellColRowRight' onkeypress="return val_decimal(event)" onblur="valida_decimal2digitos(this)" disabled="true"/>
          </td>
      </logic:empty>  

    </tr>
    <!-- nro de personal a ingresar -->
    <input type="hidden" id="nroPersonal" name="nroPersonal" value="1"/>
   </tbody>
  </table>
</html:form>
</body>
</html>
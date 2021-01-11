<%-- 
    Document   : Captura de Usuarios
    Created on : 18-nov-2015, 10:18:58
    Author     : andqui
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

    <script type="text/javascript" src="js/funcionesMI.js"></script>    
    <script type="text/javascript" src="js/jquery.js"></script>    

  <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
  <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
  <META HTTP-EQUIV="Expires" CONTENT="0">

  <style type="text/css">
    body {
        background: url(img/fondo.gif) no-repeat fixed;
        background-position: right;
    }
  </style>

  <script type="text/javascript">
    <!--

	$(document).ready(function(){
			//Aqui tu codigo
			$("#txtNroCelular").ForceNumericOnly();
			//$("#txtNroDoc").ForceNumericOnly();
			
			
	});
	
	jQuery.fn.ForceNumericOnly =
		function()
		{
			return this.each(function()
			{
				$(this).keydown(function(e)
				{
					var key = e.charCode || e.keyCode || 0;
					// allow backspace, tab, delete, enter, arrows, numbers and keypad numbers ONLY
					// home, end, period, and numpad decimal
					return (
						key == 8 || 
						key == 9 ||
						key == 13 ||
						key == 46 ||
						key == 110 ||
						key == 190 ||
						(key >= 35 && key <= 40) ||
						(key >= 48 && key <= 57) ||
						(key >= 96 && key <= 105));
				});
			});
		};		
	
	
	function cancelar(){	
		location.href = "login.do?do=cerrarSesion";	
	}
	
		
    function aceptar(){
     
		 var m_correo = document.getElementById("txtEmail").value;		 
		 var m_nrodocu = $.trim(document.getElementById("txtNroDoc").value);		 
		 var m_tipodocu = document.getElementById("cboTipoDoc").value;		
		 var m_apellidos = $.trim(document.getElementById("txtApellidos").value);		 
		 var m_nombres = $.trim(document.getElementById("txtNombres").value);	
		 var m_nrocelular = $.trim(document.getElementById("txtNroCelular").value);	
		 
		 
		 var sw=0;
		 var mensajeError='';
		 
		 
		if(m_apellidos==''){
			 sw=1;
			 mensajeError+='Ingresar el apellido\n';
		 }		 

		if(m_nombres==''){
			 sw=1;
			 mensajeError+='Ingresar el nombre\n';
		 }		 

		 
		 if(m_nrodocu==''){
			 sw=1;
			 mensajeError+='Ingresar el numero de documento\n';
		 }
		 
		 if(m_tipodocu==1 && m_nrodocu.length!=8){
			 sw=1;
			 mensajeError+='Ingresar un DNI valido\n';		 
		 }
		 
		 if(m_tipodocu==1 && isNaN(m_nrodocu)){
			 sw=1;
			 mensajeError+='Ingresar un DNI valido\n';		 
		 }
		 
		 
 		if(m_nrocelular!=''){
			if(m_nrocelular.length!=9){
				 sw=1;
				 mensajeError+='Ingresar correctamente el celular\n';
			}
		 }		 

		 
		 
		 if(validarEmail(m_correo)==false ){
		 	 sw=1;
			 mensajeError+='Ingresar correctamente el correo\n';	
		 }
		 
		 
		 
		 if(sw==0){
		 		 
			var r = confirm("¿ Desea confirmar los datos ingresados?");
			if (r == true) {
				  var frm = document.forms[0];
				  frm.action = "login.do?do=validarDatosUsuario";       
				  frm.submit();		 
			} else {
				return;
			}		 
		 
		 }else{
			 alert(mensajeError);
			 return;			 
		 
		 }
		 
		 /*
		 if(!m_nrodocu==''){
		 
			 if(validarEmail(m_correo)==true ){
				  var frm = document.forms[0];
				  frm.action = "login.do?do=validarDatosUsuario";       
				  frm.submit();
			 }else{
				alert('Ingresar correctamente el correo');
				return;
			 }	 		 
		 }else{
				alert('Ingresar el numero de documento');
				return;		 
		 }	
		 */	 
		 
    }
	
	function validaTipoDoc(){
		document.getElementById("txtNroDoc").value='';		 		 
	}

	function validaIngresoNroDoc(){
		 var m_tipodocu = document.getElementById("cboTipoDoc").value;		 		 
		 if(m_tipodocu==1){
		 	$("#txtNroDoc").ForceNumericOnly();
		 }else{
			
						
			
		 }
	}


	
	function validarEmail(email) {
		expr = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		if ( !expr.test(email) ){
			//alert("Error: La dirección de correo " + email + " es incorrecta.");
			return false;
		}else{
			return true;
		}			
	}
	

    
  
</script>

</head>
<body>
<html:form action="pagoServicio.do">
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      <td valign="middle" align="left" class="Title">Actualizaci&oacute;n de Datos de Usuario </td>
	</tr>
  </table>

  <%--table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
    <tr class="TitleRow5">
      <td>Fecha:</td>
      <td>&nbsp;&nbsp;&nbsp;</td>
      <td>Hora:</td>
    </tr>
  </table--%>

  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
   
       <tr class="CellColRow2">
      <td style="background-color: #336699; color: #FFFFFF">C&oacute;digo Cliente: </td>
      <td><%=session.getAttribute("m_frm_codcliente").toString()%>	  </td>
    </tr>
    <tr class="CellColRow2">
      <td style="background-color: #336699; color: #FFFFFF">Empresa:</td>
      <td><%=session.getAttribute("m_frm_empresa").toString()%></td>
    </tr>
    <tr class="CellColRow2">
      <td style="background-color: #336699; color: #FFFFFF">RUC:</td>
      <td><%=session.getAttribute("m_frm_ruc").toString()%></td>
    </tr>
 
	
	 <tr class="CellColRow2">
      <td style="background-color: #336699; color: #FFFFFF">Nro. Tarjeta: </td>
      <td>
	  <%=session.getAttribute("m_frm_numeroTarjeta").toString()%>      </td>
    </tr>
	
<tr class="CellColRow2">
      <td style="background-color: #336699; color: #FFFFFF">Usuario:</td>
      <td>
        <table border="0" cellpadding="0" cellspacing="0">
		
		 <tr class="CellColRow2">
          <td>Apellidos:</td>
          <td>Nombres:</td>
          <td>&nbsp;</td>
		 </tr>
        <tr class="CellColRow2">
          <td><input name="txtApellidos" type="text" class="CellColRow" id="txtApellidos" maxlength="50" value="<bean:write name="usuarioActual" scope="session" property="m_Apellido"/>"     onKeyUp="this.value=this.value.toUpperCase();"   ></td>
          <td><input name="txtNombres" type="text" class="CellColRow" id="txtNombres"  maxlength="50" value="<bean:write name="usuarioActual" scope="session" property="m_Nombre"/>"  onKeyUp="this.value=this.value.toUpperCase();" ></td>
          <td>(*) </td>
        </tr>
      </table></td>
    </tr>	
	
	
    <tr class="CellColRow2">
      <td style="background-color: #336699; color: #FFFFFF" width="20%">Tipo Documento:</td>
      <td width="80%">
	  	<select name="cboTipoDoc" class="CellColRow2" id="cboTipoDoc" onChange="validaTipoDoc();">
          <option value="1" selected>DNI</option>
          <option value="2" >CE</option>
          <option value="3" >PAS</option>
          <option value="4" >CI</option>
        </select>
	  	(*) </td>
    </tr>
    <tr>
      <td style="background-color: #336699; color: #FFFFFF">Nro Documento: </td>
      <td style="background-color: #FFFFE6"><input name="txtNroDoc" type="text" class="CellColRow" id="txtNroDoc" size="20" maxlength="20" align="bottom"	  
	  />
      <span class="CellColRow2">(*) </span></td>
    </tr>

	<tr>
	  <td style="background-color: #336699; color: #FFFFFF">Correo electr&oacute;nico:</td>
	  <td class="CellColRow2">
	  <input name="txtEmail" type="text" class="CellColRow" id="txtEmail" size="40" maxlength="100" align="bottom"
	  
	  />
      &nbsp;(*) </td>
    </tr>
	<tr>
      <td style="background-color: #336699; color: #FFFFFF">Operador:</td>
      <td class="CellColRow2">
	  	<select name="cboOperador" class="CellColRow2" id="cboOperador">
          <option value="1" selected>Movistar</option>
          <option value="2" >Claro</option>
          <option value="3" >Entel</option>
		  <option value="4" >Bitel</option>
        </select>
	  	&nbsp;</td>
    </tr>

	<tr>
      <td style="background-color: #336699; color: #FFFFFF">Celular:</td>
      <td style="background-color: #FFFFE6">
	  	<input name="txtNroCelular" type="text" class="CellColRow" id="txtNroCelular" size="15" maxlength="15" align="bottom" /></td>
    </tr>


	<tr>
      <td class="CellColRow2" colspan="2">(*) Campos requeridos </td>
    </tr>

	<tr><td  style="background-color: #FFFFE6" colspan="2">&nbsp;</td></tr>

	<tr align="right">
      <td colspan="2" class="CellColRow5">
	  <img src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:cancelar();"/>
	  <img src="img/bt-aceptar.png" align="middle" onMouseOver="this.src='img/bt-aceptar2.png'" onMouseOut="this.src='img/bt-aceptar.png'" onClick="javascript:aceptar();"/>      </td>
    </tr>
  </table>
  <br>
  <div id="cargar"/>
</html:form>
</body>
</html>
<%-- 
    Document   : Registro de Migracion de Usuarios
    Created on : 02-ene-2017, 10:18:58
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

        /*
		 var m_correo = document.getElementById("txtEmail").value;		 
		 var m_nrodocu = $.trim(document.getElementById("txtNroDoc").value);		 
		 var m_tipodocu = document.getElementById("cboTipoDoc").value;		
		 var m_apellidos = $.trim(document.getElementById("txtApellidos").value);		 
		 var m_nombres = $.trim(document.getElementById("txtNombres").value);	
		 var m_nrocelular = $.trim(document.getElementById("txtNroCelular").value);	
		 */
		 
		 var sw=0;
		 var mensajeError='';
		 

		 /*
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
		 
		 */
		 
		 if(sw==0){
		 		 
			var r = confirm("¿ Desea continuar con el proceso de Migración?");
			if (r == true) {
				  var frm = document.forms[0];
				  frm.action = "login.do?do=procesarMigracionUsuario";       
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
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0"  >
    <tr>     
      <td valign="middle" align="left" class="Cabecera"><img src="img/logo_financiero_1.png"  /></td>      
	</tr>	
  </table>
 

  <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
  
  <!-- 
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
 -->
	
	 <tr class="CellColRow2">
      <td style="background-color: #336699; color: #FFFFFF">Nro. Tarjeta: </td>
      <td>
	  <%=session.getAttribute("m_frm_numeroTarjeta").toString()%>      </td>
    </tr>

	 <tr class="CellColRow2">
      <td style="background-color: #336699; color: #FFFFFF">Usuario: </td>
      <td>
	  <%=session.getAttribute("m_frm_usuario_nombres").toString()+" "+session.getAttribute("m_frm_usuario_apellidos").toString()%>      </td>
    </tr>
	
	 <tr class="CellColRow2">
      <td style="background-color: #336699; color: #FFFFFF">Correo: </td>
      <td><%=session.getAttribute("m_frm_usuario_correo").toString()%></td>
    </tr>  
	
  </table>  

<p>
  <table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      	<td  class="TitleCursivaMensaje" align="center">
			Le damos la bienvenida a la nueva plataforma Cash Financiero.  Luego de dar click en el bot&oacute;n "Continuar"<br> 
			  le enviaremos su Usuario y Clave Inicial a su correo electr&oacute;nico:  <%=session.getAttribute("m_frm_usuario_correo").toString()%> <br> 
			y ser&aacute; direccionado a la p&aacute;gina de inicio del Nuevo Cash, donde usted deber&aacute; completar el proceso<br>
			  de cambio de claves de acuerdo con la gu&iacute;a incluida en el correo enviado.
		</td>
	</tr>
  </table>
  
    <table width="100%" cellpadding="2" cellspacing="2" border="0" align="center">
	<tr align="center">
      <td >	  
		  <img src="img/bt-continuar2.png" align="middle" onMouseOver="this.src='img/bt-continuar2.png'" onMouseOut="this.src='img/bt-continuar2.png'" onClick="javascript:aceptar();"/>
		  <img src="img/bt-cancelar.png" align="middle" onMouseOver="this.src='img/bt-cancelar2.png'" onMouseOut="this.src='img/bt-cancelar.png'" onClick="javascript:cancelar();"/>      
	  </td>
    </tr>
  </table> 
  
  <p>
	<table width="100%" CELLSPACING="0" CELLPADDING="4" border="0" >
    <tr>
      	<td valign="middle" align="left" class="TitleFooter">			
			Si desea comunicarse con su asesor de servicios llame al n&uacute;mero 612-2000, anexos: 3758, 3763, 3767, 3793, 5635, 5637.
		</td>
	</tr>
  </table>  
  <div id="cargar"/>
</html:form>
</body>
</html>
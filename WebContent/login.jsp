<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">


<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html >
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link href="css/teclado.css" rel="stylesheet" type="text/css" />
        <script src="js/jquery.js"></script>
        <title>Cash Financiero</title>

        <script>        	

        	function escribir(A,elemento){
                var strPassword= document.getElementById('password').value;    
                var strTarjeta= document.getElementById('tarjeta').value; 
                if( strTarjeta.length < 16){    
                    //$('#mensaje').html('<span >Ingresar n&uacute;mero de tarjeta de 16 d&iacute;gitos</span>');

                    $('#mensaje').html('<span >Completar los 10 &uacute;ltimos d&iacute;gitos de su PIN</span>');
                    
                }else{
                    $('#mensaje').html('<span ></span>');
                    if(A.charCodeAt(0)!=160){
                        if(document.getElementById("password").value.length<16){
                            if( strPassword.length < 6){
                                document.getElementById("password").value=document.getElementById("password").value+A;
                                window.status="";
                                elemento.blur();
                                return true;
                            }else{
                                $('#mensaje').html('<span >Ingresar s&oacute;lo 6 d&iacute;gitos para la contrase&ntilde;a</span>');
                            }                       
                        }
                    }
                }
            }
            
            function clearChar(elemento){
                var B=document.getElementById("password").value;
                var A=document.getElementById("password").value.length;
                document.getElementById("password").value=B.substring(0,A-1);
                elemento.blur();
            }
            
            function clearField(elemento){
                document.getElementById("password").value="";
                elemento.blur();
            }
            
            function iniciarSesion(){
                var strTarjeta= document.getElementById('tarjeta').value;               
                var strPassword= document.getElementById('password').value;          
                if( strTarjeta.length == 16){     
                	if( isNaN(strTarjeta)){
                        $('#mensaje').html('<span >N&uacute;mero de tarjeta solo debe contener d&iacute;gitos</span>');
                        return false;
                   	}                               
                    if( strPassword.length < 6 ){
                        //$('#mensaje').html('<span >Ingresar contrase&ntilde;a de 6 digitos</span>');
						
						$('#mensaje').html('<span>Error, cantidad m&iacute;nima de contrase&ntilde;a es de 6 d&iacute;gitos. Intente nuevamente </span>');
						
						
						
						
						
                        return false;
                    }
                    iniciarMensajeError();      
                    var frm = document.forms[0];                         
                	frm.action = "login.do?do=iniciarSesion";            	
                	frm.submit();                    
                }else{
                    //$('#mensaje').html('<span >Ingresar n&uacute;mero de tarjeta de 16 d&iacute;gitos</span>');
                    
                    $('#mensaje').html('<span >Completar los 10 &uacute;ltimos d&iacute;gitos de su PIN</span>');
                    
                    
                    return false;
                }         
            }            
            
            function validarDigitosTarjeta(evt){
                var charCode = (evt.which) ? evt.which : event.keyCode;                                      
                if (charCode > 31 && (charCode < 48 || charCode > 57)){                    
                    return false; 
                }                                    
                return true;
            }
            
            function iniciarMensajeError(){
                $('#mensaje').html('<span ></span>');
            }        
           
            function iniciar(){
            	document.getElementById("password").value="";
            	document.getElementById("tarjeta").value="903599";            	
            }

            function generarClave(){
				//location.href="login.do?do=cargaCreaClave";
            	location.href="generaClave.jsp";
            }
            
        </script>
    </head>
    <body  onload="iniciar();">
        <div id="contenedor">
       <html:form action="login.do">
       
        <table   border="0" cellspacing="0" cellpadding="0" id="table_login">
            <tr >
                <td colspan="2" >                    
                </td>
            </tr>
            <tr>
                <td>
                    <img src="img/login/logeo-pic.jpg" height="305" style="border-right: solid 1px #4B93EA;"/>
    </td>
                <td>
                    <table border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td class="texto_login texto_encabezado" style="font-weight: bolder; font-size: 13px">
                                Cash Financiero
                            </td>
                        </tr>
                        <tr>
                            <td class="texto_login texto_encabezado">
                                Su banca por internet
                            </td>
                        </tr>
                        
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                        
                        <tr>
                            <td class="texto_login">
                                Por favor ingrese los ultimos 10 d&iacute;gitos de su                             
                            </td>
                        </tr>
                        <tr>
                            <td class="texto_login">
                                 PIN.
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <table>
                                    <tr>
                                        <td colspan="2" >
                                        	<html:text  styleId="tarjeta" property="numeroTarjeta" maxlength="16"
                                        				onkeypress="return validarDigitosTarjeta(event);" 
                                                   		onfocus="iniciarMensajeError();">903599</html:text>
                                            
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="texto_login" valign="top" width="60%">
                                            Ingrese su clave  <br/>
                                            de 6 d&iacute;gitos utilizando<br/>
                                            el teclado virtual. <br/>
                                            <br/>
                                            Si a&uacute;n no tiene su  <br/>
                                            clave de 6 d&iacute;gitos <br/>
                                            haga 
                                            <a href="#"  onclick="generarClave()"  >
                                            <strong style="COLOR: #0000a0;">click aqu&iacute;</strong>
                                            </a>
                                               
                                        </td>
                                        <td align="right">
                                            <table border="0" style="margin:0px;padding-left:0px" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td>
                                                        <script type="text/javascript">
                                                            var numeros = new Array();
                                                            while(numeros.length<10){
                                                                var randomnumber=Math.floor(Math.random()*10)
                                                                if(!exist(randomnumber)){
                                                                    numeros[numeros.length]=randomnumber;
                                                                }
                                                            }
                                                            var j=0;
                                                            for ( j=0; j < numeros.length-1; j++ ){
                                                                var out = '<input class=\"button_off\" type=\"button\" style=\"width:24px;height=24px;\" onmouseup=\"this.className=\'button_on\';\" onmouseover=\"window.status = \'\';this.className=\'button_on\'\; return true;" onmousedown=\"this.className=\'button_press\';\" onmouseout=\"this.className=\'button_off\'\" onclick=\"escribir(\'';
                                                                out += numeros[j];
                                                                out += '\',this);\"';
                                                                out += 'value=\"' + numeros[j] + '\"';
                                                                out += '\/>';
                                                                if(j%3==2){
                                                                    out += '<br/>';
                                                                }
                                                                document.write(out);
                                                            }
                                                            var out = '<input class=\"button_off\" type=\"button\" style=\"width:24px;height=24px;\" onmouseup=\"this.className=\'button_on\';\" onmouseover=\"window.status = \'\';this.className=\'button_on\'\; return true;" onmousedown=\"this.className=\'button_press\';\" onmouseout=\"this.className=\'button_off\'\" onclick=\"escribir(\'';
                                                            out += numeros[j];
                                                            out += '\',this);\"';
                                                            out += 'value=\"' + numeros[j] + '\"';
                                                            out += '\/>';

                                                            document.write(out);
                                                            out = '<input class=\"button_off\" type=\"button\" style=\"width:49px;height=24px;\" onmouseup=\"this.className=\'button_on\';\" onmouseover=\"window.status = \'\';this.className=\'button_on\'\; return true;" onmousedown=\"this.className=\'button_press\';\" onmouseout=\"this.className=\'button_off\'\" onclick=\"clearChar(this);\"';

                                                            out += 'value=\"Borrar\"';
                                                            out += '\/>';
                                                            out += '<br/>';
                                                            document.write(out);
                                                            out = '<input id=\"btn_reset\" class=\"button_off\" type=\"button\" style=\"width:74px;height=24px;\" onmouseup=\"this.className=\'button_on\';\" onmouseover=\"window.status = \'\';this.className=\'button_on\'\; return true;" onmousedown=\"this.className=\'button_press\';\" onmouseout=\"this.className=\'button_off\'\" onclick=\"clearField(this);\"';

                                                            out += 'value=\"Borrar Todo\"';
                                                            out += '\/>';
                                                            document.write(out);
                                                            function exist( numero ) {
                                                                var i=0;
                                                                for ( i=0; i < numeros.length; i++ ){
                                                                    if(numeros[i]==numero){
                                                                        return true;
                                                                    }
                                                                }
                                                                return false;
                                                            }
                                                        </script>
                                                        <noscript>
                                                <h3>Tu navegador no soporta Javascript o no <br />lo tiene habilitado.</h3>
                                                </noscript>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                        	<html:password property="contrasegnia" styleId="password" maxlength="6" readonly="true" size="8"> </html:password>                                            
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>                                       	
                                            <img src="img/login/logeo-ingresar.jpg"  onMouseDown="this.src='img/login/logeo-ingresar-press.jpg'" 
                                                onMouseOut="this.src='img/login/logeo-ingresar.jpg'" onClick="iniciarSesion();" 
                                            />                                            
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>
        <div id="mensaje" class="error_login">     
        	<logic:notEmpty name="error_login" scope="request">
        		<bean:write name="error_login"/>
        	</logic:notEmpty>
        	
<logic:equal name="loginform" property="validaClaveSeis" value="NO">
A&uacute;n no se ha generado su clave de 6 d&iacute;gitos, para generar su nueva clave 6 d&iacute;gitos haga 
<a href="#" onclick="generarClave()" ><strong style='COLOR: #0000a0;'>click aqu&iacute;</strong></a>
</logic:equal>        	
        	
        </div>   
      
        </html:form>    
        </div>   
</body>
</html>
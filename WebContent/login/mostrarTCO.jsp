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
                $('#mensaje').html('<span ></span>');
                if(A.charCodeAt(0)!=160){
                	if(document.getElementById("password").value.length<16){
                            if( strPassword.length < 4){
                                document.getElementById("password").value=document.getElementById("password").value+A;
                                window.status="";
                                elemento.blur();
                                return true;
                            }else{
                                $('#mensaje').html('<span >Ingresar solo 4 digitos para la contrase&ntilde;a</span>');
                            }                       
                        }
                    }               
            }
            
            function clearChar(elemento){
                var B=document.getElementById("password").value;
                var A=document.getElementById("password").value.length;
                document.getElementById("password").value=B.substring(0,A-1);
                iniciarMensajeError();
                elemento.blur();
            }
            
            function clearField(elemento){
                document.getElementById("password").value="";
                iniciarMensajeError();
                elemento.blur();
            }
            
            function validarTCO(){                     
                var strPassword= document.getElementById('password').value;          	                            
                if( strPassword.length < 4 ){
                    $('#mensaje').html('<span >Ingresar clave de 4 digitos</span>');
                    return false;
                }
                var frm = document.forms[0];            
                frm.action = "login.do?do=validarTCO";            	
               	frm.submit();       
            }            
                       
            function iniciarMensajeError(){
                $('#mensaje').html('<span ></span>');
            }        
           
            function iniciar(){
            	document.getElementById("password").value="";            	            	
            }

            function cerrarSesion(){
            	var frm = document.forms[0];            
             	frm.action = "login.do?do=cerrarSesion";            	
             	frm.submit();         	            	
            }
        </script>
    </head>
    <body  onload="iniciar();">
        <div id="contenedor">
       <html:form action="login.do">
       
        <table   border="0" cellspacing="0" cellpadding="0" id="table_login">
            <tr >
                <td colspan="2" >
                    <img src="img/login/logeo-logo.jpg" style="border-bottom: solid 1px #4B93EA;" />
                </td>
            </tr>
            <tr>
                <td>
                    <img src="img/login/logeo-pic.jpg" />
                </td>
                <td>
                    <table border="0" cellspacing="0" cellpadding="0">
                    	<tr>
                    	 	<td class="texto_login texto_encabezado" style="font-weight: bolder;" align="left">
                                <logic:notEmpty name="usuarioTCO" >
        										<bean:write name="usuarioTCO"/>
        						</logic:notEmpty>
                            </td>
                            <td class="texto_login" style="padding-right: 20px;" align="right">
                                <a href="login.do?do=cerrarSesion" >Cerrar Sesion</a>
                            </td>
                        </tr>
                         <tr>
                            <td class="texto_login texto_encabezado">
                                <br/>
                            </td>
                        </tr>                       
                        <tr>
                        	<td >
                                
                            </td>
                            <td class="texto_login texto_encabezado">
                                <br/>
                            </td>
                        </tr>                  
                        
                        <tr>                                
                            	
                            <td class="texto_login" width="60%" align="center">                                            
                                            Confirmar la operacion ingresando los 4 numeros de la coordenada
                                            <br/>
                                            <span class="texto_coordenada" >
                                            	<logic:notEmpty name="coordenadaUI" scope="request">
        											<bean:write name="coordenadaUI" scope="request"/>
        										</logic:notEmpty>
        									</span>                                           
                                               
                            </td>
                                        <td >                   
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
                                        	<input name="password" id="password" maxlength="4" readonly="readonly" size="8" type="password"/>                                        	                                            
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>                                       	
                                            <img src="img/login/logeo-ingresar.jpg"  onMouseDown="this.src='img/login/logeo-ingresar-press.jpg'" 
                                                onMouseOut="this.src='img/login/logeo-ingresar.jpg'" onClick="validarTCO();" />                                            
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
        	<logic:notEmpty name="error_tco" scope="request">
        		<bean:write name="error_tco"/>
        	</logic:notEmpty>
        </div>   
      
        </html:form>    
        </div>   
</body>
</html>
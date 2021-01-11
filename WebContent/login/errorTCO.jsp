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
		        <table border="0" cellspacing="0" cellpadding="0" id="table_login">
		            <tr>
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
		                           
		                        </tr>
		                        
		                        <tr>
		                    	 	<td class="texto_login texto_encabezado"  align="left">
		                            	<br/>    
		                            </td>
		                           
		                        </tr>
		                              
		                              
		                        
		                        <tr>                          	
		                            <td class="texto_login"  align="justify"  style="padding-right: 20px;">                                                                          
										<logic:notEmpty name="error_tco" scope="request">
		        							<bean:write name="error_tco"/>
		        						</logic:notEmpty>                                               
		                            </td>		                            
		                        </tr>
		                        
		                         <tr>
		                    	 	<td class="texto_login texto_encabezado"  align="left">
		                            	<br/>    
		                            </td>
		                           
		                        </tr>
		                        <tr>
		                        	 <td class="texto_login" style="padding-right: 20px;" align="right">
		                                <a href="login.do?do=cerrarSesion" >Terminar</a>
		                            </td>
		                        </tr>
		                      
		                    </table>
		    			</td>
					</tr>
				</table>
	       </html:form>    
		</div>   
	</body>
</html>
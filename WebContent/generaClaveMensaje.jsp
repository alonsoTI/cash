<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="css/estiloLogin.css" rel="stylesheet" type="text/css">


<title>Genera Clave</title>

<script type="text/javascript">




	function regresar(){
			var frm = document.forms[0];
			//frm.action = "login.do?do=iniClean";
			frm.action = "generaClave.jsp";
			
			frm.submit();
		}

</script>

</head>
<body>

<table border="0" width="100%">
	<tr>
		<td bgcolor="#002B6F" height="20px;"></td>
	</tr>
	<tr>
		<td bgcolor="#FFFFFF" style="border: solid 1px #002b6f;"><img src="img/logo-Financiero-AMA.jpg"></img></td>
	</tr>
	<tr>
		<td><br>
		Genere su nueva clave de acceso a la Plataforma Cash Financiero de 6 dígitos<br>
		Usted esta en una zona segura&nbsp;<img src="img/lock.png" /> &nbsp;<br>
		<hr>
		<p />        
		<p />	
			  Para m&aacute;xima seguridad en sus operaciones en l&iacute;nea, usted
		debera generar una clave de internet de 6 d&iacute;gitos.<br>
		Siga los pasos indicados. <br>
		<br>
		<form method="post"  action="login.do" >
		<table width="557" border="0" cellpadding="2" cellspacing="2">
			<tr>
				<td colspan='2'></td>
			</tr>
			<tr>
				  <td width="144" class="textousuario"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nro Tarjeta:</td>
				  <td width="399" align="left">
		        	<logic:notEmpty name="mensajeTarjeta" scope="request">
		        		<bean:write name="mensajeTarjeta"/>
		        	</logic:notEmpty></td>
			</tr>

			<tr>
				<td colspan="2">
					<table width="436" border="1" align="center">
					  <tr bgcolor="#0066FF">
					    <td width="426" height="103" align="center" valign="middle" bgcolor="#003FA6" class="textoTR"><p>&nbsp;</p>
				        <p><strong>Han transcurrido 30 días de no utilizar la clave, por su seguridad  esta clave ha sido bloqueada. Comuníquese con Soporte Cash al 612-2000 anexos  5974,5975, 3768.</strong></p></td>
				      </tr>
				    </table>
			    </td>
		    </tr>
			<tr>
			  <td colspan="2"><blockquote>
			    <blockquote>
			      <blockquote>
			        <blockquote>
			          <p>
			            <input
					name="btnRegresar" type="button"  id="btnRegresar"
					onclick="regresar()" value="Regresar" style="width:100px;"  />
		              </p>
		            </blockquote>
		          </blockquote>
		        </blockquote>
		      </blockquote></td>
		  </tr>

		</table>
	  </form></td>
	</tr>
</table>
</body>
</html>
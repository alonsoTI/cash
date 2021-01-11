<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <META HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="0">
        <style type="text/css">
        	#hor-zebra
			{
				font-family: "Lucida Sans Unicode", "Lucida Grande", Sans-Serif;
				font-size: 12px;
				width: 100%;
				text-align: left;
				border-collapse: collapse;
				border-bottom : 1px solid gainsboro; 
			    border-left: 1px solid gainsboro;
			}
			#hor-zebra th
			{
				font-size: 12px;
				font-weight: normal;
				padding: 5px 4px;
				color: white;
			    background: cornflowerblue;
			    border-right: 1px solid gainsboro;
			    border-top: 1px solid gainsboro;
			}
			
			#hor-zebra td
			{
				padding: 6px;
				color: #669;
			    border-right: 1px solid gainsboro;
			    border-top: 1px solid gainsboro;
			}
        </style>      
        <title>Impresion de Transferencias</title>       
       	<link href="css/cash.css" rel="stylesheet" type="text/css">       
    </head>
    <body onload="javascript:window.print();javascript:window.close();">       
    	<img src="img/logo-Financiero-AMA.gif" />
    	<h1 id="titulo"  style="font-size: 24px;">Transferencias</h1>	   
    	
    	
    	
    	<logic:notEmpty  name="success">
    		<table width="100%">
				<tr>
					<td class="info" style="height: 30px; font-size: 15px;"><bean:write
					name='success' property='m_Mensaje' /></td>
				</tr>
			</table>   
    	</logic:notEmpty>	  
        <table id="formulario">
          	<colgroup>
				<col class="label" style="font-size: 16px;"/>
				<col class="input-read" style="font-size: 18px;"/>					
			</colgroup>            		
            <logic:iterate name="alsuccess" id="beansuccess" indexId="j">
             	<tr height="32px" valign="middle" >
					<td>
						<bean:write name="beansuccess" property="m_Label" />
					</td>
					<logic:notEmpty name="beansuccess" property="m_Mensaje">
						<td>
							<bean:write name="beansuccess" property="m_Mensaje" />
						</td>
					</logic:notEmpty>
					<logic:empty name="beansuccess" property="m_Mensaje">
						<td></td>
					</logic:empty>
				</tr>
            </logic:iterate>             
        </table>   
    </body>
</html>

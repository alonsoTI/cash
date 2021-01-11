function agregaRol()
{
  var form = document.forms[0];
	for (ii=0;ii<form.codigoRol.length;ii++)
	{
		isSelected = form.codigoRol.options[ii].selected
		if (isSelected)
		{
				form.lstRolUsuario.options[form.lstRolUsuario.length]= new Option(form.codigoRol.options[ii].text , form.codigoRol.options[ii].value); 
				form.codigoRol.options[ii] = null
				selected = ii
				ii--;	
		}
	}
}

function eliminaRol()
{
  var form = document.forms[0];
	for (ii=0;ii<form.lstRolUsuario.length;ii++)
	{
		isSelected = form.lstRolUsuario.options[ii].selected
		if (isSelected)
		{
			form.codigoRol.options[form.codigoRol.length]= new Option(form.lstRolUsuario.options[ii].text , form.lstRolUsuario.options[ii].value); 
			form.lstRolUsuario.options[ii] = null
			selected = ii
			ii--;	
		}
	}
}


function f_marcar(letrasid){
				var letras=letrasid.split(",");	
				var objForm = document.forms[0];
				var a
				var obj=document.getElementById('-,'+letras[1]);
				var boo=true;
				var cadena="";
				var cont=0;
				
				if(letras[0]=="-"){
					if(obj != null && obj.checked==false){
					boo=false;
					}
					if(objForm.elements != null) 
					{
						for(var i=0; i<objForm.elements.length; i++)
						{
							if(objForm.elements[i].type=="checkbox" ) {
								a=objForm.elements[i].id.split(",");						
									
								if(a[0]==letras[1] ) {
									objForm.elements[i].checked=boo;
								}								
							}						
						}
					}
				}else{
				document.getElementById('-,'+letras[0]).checked=true;
				
				}
				
			}   
   
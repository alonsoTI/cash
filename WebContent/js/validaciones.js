
function soloNumeros(){ 
	var key=window.event.keyCode;
		if (key < 48 || key > 57 ){
			window.event.keyCode=0; 
		}
}


function  sinEspacios(){
  var key=window.event.keyCode;
    if (key==32 ){
      window.event.keyCode=0; 
  }
}

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

function fecha(caja)
{
    Calendar.setup({
        inputField     :    caja.id,   // id of the input field
        ifFormat       :    "%d-%m-%Y",       // format of the input field
        showsTime      :    false,
        timeFormat     :    "24",
        //onUpdate       :    catcalc
        singleClick	   :	true
    });
}

function fecha2(caja, disp)
{
    Calendar.setup({
        inputField     :    caja,   // id of the input field
        ifFormat       :    "%d/%m/%Y",       // format of the input field
        showsTime      :    false,
        timeFormat     :    "24",
        button         :    disp,
        singleClick	   :	true,
        weekNumbers :       false,
        firstDay :0

    });
}


function objetoAjax(){//menu
    var xmlhttp=false;
    try {
        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e) {
        try {
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        } catch (E) {
            xmlhttp = false;
        }
    }

    if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
        xmlhttp = new XMLHttpRequest();
    }
    return xmlhttp;
}
function Pagina(nropagina,mi_vector,capa,url,sw){

    var n=mi_vector.length;
    var i=0;
    var filtro="";
    for(i=0;i<n;i++){
        var desc='';
        desc=mi_vector[i];
        filtro+="&filtro"+i+'='+desc;
    }

    divContenido = document.getElementById(capa);
    ajax=objetoAjax();

    ajax.open("GET", url+"?pag="+nropagina+"&sw="+sw+"&ni="+n+filtro+ "&random=" + Math.random());
    divContenido.innerHTML= 'Cargando...';

    //var image = document.createElement('img');
    //image.setAttribute('border', '0');
    //image.setAttribute('src', 'image/loader.gif');
    //divContenido.appendChild(image);

    ajax.onreadystatechange=function() {
        if (ajax.readyState==4) {
            divContenido.innerHTML = ajax.responseText;


        }
    }
    ajax.send(null);
}

function replace(texto,s1,s2){
    return texto.split(s1).join(s2);
}


function pinta(a,b,c)
{
    if(b==1){
        //a.style.backgroundColor="#FEF7CD";
        a.style.backgroundColor="#ECC5AA";
    }
    else
    {
        //a.style.backgroundColor="#FFFFFF";
        a.style.backgroundColor=c;
    }
}

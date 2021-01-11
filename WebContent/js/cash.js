var isIE;

function initRequest() {
    if (window.XMLHttpRequest) {
        if (navigator.userAgent.indexOf('MSIE') != -1) {
            isIE = true;
        }
        return new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        isIE = true;
        return new ActiveXObject("Microsoft.XMLHTTP");
    }
}


function submit(frm,strTarget)
{    
	frm.action = strTarget;
    frm.submit();    
	return true;
}


function validarEntero(o){
	o.value=o.value.toString().replace(/([^\d])/g,"");	
}
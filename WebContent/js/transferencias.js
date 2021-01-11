function confirmarTrxCP(frm, from, strTarget) {
	var frm = document.forms[0];            
    var ctaCargo = frm.m_CtaCargo.options[document.forms[0].m_CtaCargo.selectedIndex].value;
    var ctaAbono = frm.m_CtaAbono.options[document.forms[0].m_CtaAbono.selectedIndex].value;
        if (ctaCargo == ctaAbono){
                alert ("Cuenta origen y destino deben ser diferentes");
                return false;
            }else{
                var val = new Array();
                val[0] = valMonto;                
                if (LiveValidation.massValidate(val)){
                    if (isValidForm2(frm,from,strTarget)){
                        return true;
                    }
                }
                return false;
           }           
}
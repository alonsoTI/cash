String.prototype.trim = function() {
	return this.replace(/^\s+|\s+$/g,"");
};

var patronDecimal=/^[0-9]+(\.[0-9]+)?$/i;

function validarDecimal(str){    
    return patronDecimal.test(str);
}

function validarEspacioBlanco(str){
	return /\s/.test(str);
}



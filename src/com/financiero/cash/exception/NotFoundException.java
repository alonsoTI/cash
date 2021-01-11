package com.financiero.cash.exception;

public class NotFoundException extends Exception{


	public NotFoundException() {
		super("Elemento No Encontrado");
	}
	
	public NotFoundException(String msg){
		super(msg);
	}
	
}

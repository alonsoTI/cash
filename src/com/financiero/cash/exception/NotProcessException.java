package com.financiero.cash.exception;

public class NotProcessException extends Exception {
	
	public NotProcessException() {
		super("El proceso no puede continuar");
	}
	
	public NotProcessException (String msg){
		super(msg);
	}
}

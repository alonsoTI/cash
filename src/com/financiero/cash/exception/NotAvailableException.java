package com.financiero.cash.exception;

public class NotAvailableException extends Exception{

	public NotAvailableException() {
		super("No disponible");
	}
	
	public NotAvailableException(String msg){
		super(msg);
	}
}

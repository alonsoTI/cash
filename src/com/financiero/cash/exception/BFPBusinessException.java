package com.financiero.cash.exception;

public class BFPBusinessException extends RuntimeException {

	private static final long serialVersionUID = 6835035477433323729L;
	/**
	 * 001: error de cuenta
	 * 002: error de monto
	 * 003: error de proceso
	 */
	private String code;

	public BFPBusinessException(String code, String message) {
		super(message);
		this.code = code;
	}
	
	public BFPBusinessException(String message) {
		super(message);		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

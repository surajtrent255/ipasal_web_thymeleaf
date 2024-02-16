package com.ipasal.ipasalwebapp.exception;

import org.springframework.http.HttpStatus;

abstract class CustomBaseException extends RuntimeException {
	private static final long serialVersionUID = 6230258566767224924L;
	private String message;
	public CustomBaseException(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	abstract HttpStatus getStatus();
}

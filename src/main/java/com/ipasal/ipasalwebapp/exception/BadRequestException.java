package com.ipasal.ipasalwebapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends CustomBaseException {
	private static final long serialVersionUID = 3837370581861836165L;
	private static final HttpStatus status = HttpStatus.BAD_REQUEST;
	public BadRequestException(String message) {
		super(message);
	}
	public HttpStatus getStatus() {
		return status;
	}
}

package com.prince.integrations.exceptions;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ApplicationException extends RuntimeException {

	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
	private Map<String, String> errors = new HashMap<>();

	public ApplicationException(Map<String, String> errors) {
		super("Invalid data supplied.");
		this.errors = errors;
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(ErrorMessage errorMessage) {
		this(errorMessage.getMessage(), errorMessage.getStatus());
	}

	public ApplicationException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}

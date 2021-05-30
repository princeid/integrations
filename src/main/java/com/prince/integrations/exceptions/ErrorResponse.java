package com.prince.integrations.exceptions;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {
	private String message;
	private Map<String, String> errors;

	public ErrorResponse(ErrorMessage errorMessage) {
		this(errorMessage.getMessage());
	}

	public ErrorResponse(ErrorMessage errorMessage, Map<String, String> errors) {
		this(errorMessage.getMessage(), errors);
	}

	public ErrorResponse(String message) {
		this(message, null);
	}

	public ErrorResponse(String message, Map<String, String> errors) {
		this.message = message;
		this.errors = errors == null ? new HashMap<>() : errors;
	}
}

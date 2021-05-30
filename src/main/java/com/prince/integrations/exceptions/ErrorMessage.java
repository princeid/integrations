package com.prince.integrations.exceptions;

import org.springframework.http.HttpStatus;


public enum ErrorMessage {
	INTERNAL_SERVER_ERROR("Oops! Something went wrong! Help us improve your experience by sending an error report", HttpStatus.INTERNAL_SERVER_ERROR),
	SERVICE_UNAVAILABLE("Service Temporarily Unavailable. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE),
	SERVICE_NOT_SUPPORTED("Oops! Biller doesn't support this service.", HttpStatus.SERVICE_UNAVAILABLE),
	PROVIDER_NOT_FOUND("Oops! Supplied provider does not exist.", HttpStatus.NOT_FOUND),
	UNAUTHORIZED("Unauthenticated.", HttpStatus.UNAUTHORIZED),
	FORBIDDEN("Access denied.", HttpStatus.FORBIDDEN),
	NOT_FOUND("Resource not found.", HttpStatus.NOT_FOUND),
	UNPROCESSABLE_ENTITY("The given data was invalid.", HttpStatus.UNPROCESSABLE_ENTITY);

	String message;
	HttpStatus status;

	ErrorMessage(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getStatus() {
		return status;
	}
}

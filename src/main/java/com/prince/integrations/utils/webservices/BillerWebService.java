package com.prince.integrations.utils.webservices;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class BillerWebService<T> extends WebService<T> {

	public BillerWebService(String url, HttpMethod httpMethod, Object body, HttpHeaders httpHeaders, String narration, Class<T> clazz) {
		super(url, httpMethod, body, httpHeaders, narration, clazz, false);
	}

	public BillerWebService(String url, HttpMethod httpMethod, Object body, HttpHeaders httpHeaders, String narration, Class<T> clazz, boolean skipSslVerification) {
		super(url, httpMethod, body, httpHeaders, narration, clazz, skipSslVerification);
	}

	public BillerWebService(String url, HttpMethod httpMethod, Object body, HttpHeaders httpHeaders, String narration, ParameterizedTypeReference<T> parameterizedTypeReference) {
		super(url, httpMethod, body, httpHeaders, narration, parameterizedTypeReference, false);
	}

	public BillerWebService(String url, HttpMethod httpMethod, Object body, HttpHeaders httpHeaders, String narration, ParameterizedTypeReference<T> parameterizedTypeReference, boolean skipSslVerification) {
		super(url, httpMethod, body, httpHeaders, narration, parameterizedTypeReference, skipSslVerification);
	}
}

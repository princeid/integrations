package com.prince.integrations.utils.webservices;


import com.prince.integrations.exceptions.ApplicationException;
import com.prince.integrations.exceptions.ErrorMessage;
import com.prince.integrations.utils.SpringContext;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public abstract class WebService<T> {
	private static Logger logger = LoggerFactory.getLogger(WebService.class);

	private RestTemplate restTemplate;
	private ResponseEntity<T> responseEntity;

	public WebService(String url, HttpMethod httpMethod, Object body, HttpHeaders httpHeaders, String narration, Class<T> clazz, boolean skipSslVerification) throws HttpStatusCodeException {

		initializeRestTemplate(skipSslVerification, narration);

		responseEntity = Try.of(() -> restTemplate.exchange(url, httpMethod, new HttpEntity<>(body, httpHeaders), clazz))
				.onFailure(ResourceAccessException.class, e -> {
					logger.debug(ErrorMessage.SERVICE_UNAVAILABLE.getMessage(), e);
					throw new ApplicationException(ErrorMessage.SERVICE_UNAVAILABLE);
				}).get();
	}

	public WebService(String url, HttpMethod httpMethod, Object body, HttpHeaders httpHeaders, String narration, ParameterizedTypeReference<T> parameterizedTypeReference, boolean skipSslVerification) throws HttpStatusCodeException {

		initializeRestTemplate(skipSslVerification, narration);

		responseEntity = Try.of(() -> restTemplate.exchange(url, httpMethod, new HttpEntity<>(body, httpHeaders), parameterizedTypeReference))
				.onFailure(ResourceAccessException.class, e -> {
					logger.debug(ErrorMessage.SERVICE_UNAVAILABLE.getMessage(), e);
					throw new ApplicationException(ErrorMessage.SERVICE_UNAVAILABLE);
				}).get();
	}

	private void initializeRestTemplate(boolean skipSslVerification, String narration) {
		logger.debug("Narration   : {}", narration);

		restTemplate = skipSslVerification
				? SpringContext.getBean(RestTemplate.class, "restTemplate")
				: SpringContext.getBean(RestTemplate.class, "skipSslVerificationRestTemplate");
	}

	public ResponseEntity<T> getResponseEntity() {
		return responseEntity;
	}
}

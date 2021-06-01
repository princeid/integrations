package com.prince.integrations;

import com.prince.integrations.utils.webservices.BillerWebService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

public abstract class AbstractBillerService {
    public static <T> ResponseEntity<T> callClient(String url, HttpHeaders httpHeaders, String narration, Class<T> clazz, boolean skipSslVerification) {
        return callClient(url, HttpMethod.GET, null, httpHeaders, narration, clazz, skipSslVerification);
    }

    public static <T> ResponseEntity<T> callClient(String url, HttpMethod httpMethod, Object body, HttpHeaders httpHeaders, String narration, Class<T> clazz, boolean skipSslVerification) {
        return new BillerWebService<>(url, httpMethod, body, httpHeaders, narration, clazz, skipSslVerification).getResponseEntity();
    }

    public static <T> ResponseEntity<T> callClient(String url, HttpHeaders httpHeaders, String narration, ParameterizedTypeReference<T> parameterizedTypeReference, boolean skipSslVerification) {
        return callClient(url, HttpMethod.GET, null, httpHeaders, narration, parameterizedTypeReference, skipSslVerification);
    }

    public static <T> ResponseEntity<T> callClient(String url, HttpMethod httpMethod, Object body, HttpHeaders httpHeaders, String narration, ParameterizedTypeReference<T> parameterizedTypeReference, boolean skipSslVerification) {
        return new BillerWebService<>(url, httpMethod, body, httpHeaders, narration, parameterizedTypeReference, skipSslVerification).getResponseEntity();
    }

    protected HttpHeaders getRestHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        return httpHeaders;
    }



}

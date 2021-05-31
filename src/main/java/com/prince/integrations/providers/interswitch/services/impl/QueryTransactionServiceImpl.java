package com.prince.integrations.providers.interswitch.services.impl;

import com.prince.integrations.AbstractBillerService;
import com.prince.integrations.exceptions.ApplicationException;
import com.prince.integrations.exceptions.ErrorResponse;
import com.prince.integrations.exceptions.InterswitchErrorResponse;
import com.prince.integrations.providers.interswitch.pojo.AuthenticateRequest;
import com.prince.integrations.providers.interswitch.pojo.AuthenticateResponse;
import com.prince.integrations.providers.interswitch.pojo.GetBillerResponse;
import com.prince.integrations.providers.interswitch.pojo.QueryTransactionResponse;
import com.prince.integrations.providers.interswitch.services.QueryTransactionService;
import com.prince.integrations.utils.JsonUtils;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Base64;

import static com.prince.integrations.utils.Commons.*;
import static com.prince.integrations.utils.Commons.generateSignature;

@Service
public class QueryTransactionServiceImpl extends AbstractBillerService implements QueryTransactionService {


    private static final Logger log = LoggerFactory.getLogger(QueryTransactionServiceImpl.class);

    @Value("${interswitch.url}")
    private String url;

    @Value("${interswitch.client.id}")
    private String clientId;

    @Value("${interswitch.secret.key}")
    private String secretKey;

    @Value("${interswitch.terminal.id}")
    private String terminalId;


    @Override
    public QueryTransactionResponse queryTransaction(long requestReference) {
        String narration = "Requerying Transaction";

//        final Logger log = null;

        String queryTransactionUrl = url + "/api/v2/quickteller/transactions/requery";
        String httpMethod = "GET";

        QueryTransactionResponse queryTransactionResponse = Try.of(() -> callClient( queryTransactionUrl, getHttpHeaders(httpMethod, queryTransactionUrl, log), narration,  QueryTransactionResponse.class, true))
                .onFailure(RestClientResponseException.class, throwable -> {
                    ErrorResponse errorResponse = JsonUtils.cast(throwable.getResponseBodyAsString(), ErrorResponse.class);
                    throw new ApplicationException(errorResponse == null ? "An unknown error occurred." : errorResponse.getMessage());
                }).get().getBody();

        return queryTransactionResponse;
    }


    protected HttpHeaders getLoginRestHttpHeaders() {
        HttpHeaders httpHeaders = super.getRestHttpHeaders();
        httpHeaders.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + secretKey).getBytes()));
        return httpHeaders;
    }

    protected HttpHeaders getHttpHeaders(String method, String url, final Logger log) throws UnsupportedEncodingException, URISyntaxException {
        HttpHeaders httpHeaders = super.getRestHttpHeaders();
        String nonce = generateNonce();
        httpHeaders.set("Authorization", generateAuth(clientId));
        httpHeaders.set("Nonce", nonce);
        String time = getTime();
        httpHeaders.set("Timestamp", time);
        httpHeaders.set("SignatureMethod", "SHA1");
        httpHeaders.set("TerminalId", terminalId);
        httpHeaders.set("Signature", generateSignature(method, url, time, nonce, clientId, secretKey, log));
        return httpHeaders;
    }
}

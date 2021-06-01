package com.prince.integrations.providers.interswitch.services.impl;

import com.prince.integrations.AbstractBillerService;
import com.prince.integrations.exceptions.ApplicationException;
import com.prince.integrations.exceptions.ErrorResponse;
import com.prince.integrations.providers.interswitch.pojo.QueryTransactionResponse;
import com.prince.integrations.providers.interswitch.services.QueryTransactionService;
import com.prince.integrations.utils.JsonUtils;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

@Service
public class QueryTransactionServiceImpl extends AbstractBillerService implements QueryTransactionService {


    private static final Logger log = LoggerFactory.getLogger(QueryTransactionServiceImpl.class);

    HeaderUtils headerUtils = new HeaderUtils();

    @Value("https://sandbox.interswitchng.com")
    private String url;


    @Override
    public QueryTransactionResponse queryTransaction(long requestReference) {
        String narration = "Requerying Transaction";

//        final Logger log = null;

        String queryTransactionUrl = url + "/api/v2/quickteller/transactions";
        String httpMethod = "GET";

        QueryTransactionResponse queryTransactionResponse = Try.of(() -> callClient( queryTransactionUrl, headerUtils.getHttpHeaders(httpMethod, queryTransactionUrl, log), narration,  QueryTransactionResponse.class, true))
                .onFailure(RestClientResponseException.class, throwable -> {
                    ErrorResponse errorResponse = JsonUtils.cast(throwable.getResponseBodyAsString(), ErrorResponse.class);
                    throw new ApplicationException(errorResponse == null ? "An unknown error occurred." : errorResponse.getMessage());
                }).get().getBody();

        return queryTransactionResponse;
    }


}

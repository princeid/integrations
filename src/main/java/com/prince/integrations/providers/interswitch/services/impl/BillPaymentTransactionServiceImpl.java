package com.prince.integrations.providers.interswitch.services.impl;

import com.prince.integrations.AbstractBillerService;
import com.prince.integrations.exceptions.ApplicationException;
import com.prince.integrations.exceptions.ErrorResponse;
import com.prince.integrations.providers.interswitch.pojo.BillPaymentTransactionRequest;
import com.prince.integrations.providers.interswitch.pojo.BillPaymentTransactionResponse;
import com.prince.integrations.providers.interswitch.services.BillPaymentTransactionService;
import com.prince.integrations.utils.JsonUtils;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

@Service
public class BillPaymentTransactionServiceImpl extends AbstractBillerService implements BillPaymentTransactionService {

    private static final Logger log = LoggerFactory.getLogger(BillPaymentTransactionServiceImpl.class);

    @Value("https://sandbox.interswitchng.com")
    private String url;


    @Override
    public ResponseEntity<BillPaymentTransactionResponse> payBill(BillPaymentTransactionRequest billPaymentTransactionRequest) {
        String narration = "Processing payment...";
        HeaderUtils headerUtils = new HeaderUtils();

//        final Logger log = null;

        String getValidationUrl = url + "/api/v2/quickteller/transactions";
        String httpMethod = "POST";

        ResponseEntity<BillPaymentTransactionResponse> billPaymentTransactionResponse = Try.of(() -> callClient(getValidationUrl, HttpMethod.POST, billPaymentTransactionRequest, headerUtils.getHttpHeaders(httpMethod, getValidationUrl, log), narration,  BillPaymentTransactionResponse.class, true))
                .onFailure(RestClientResponseException.class, throwable -> {
                    ErrorResponse errorResponse = JsonUtils.cast(throwable.getResponseBodyAsString(), ErrorResponse.class);
                    throw new ApplicationException(errorResponse == null ? "An unknown error occurred." : errorResponse.getMessage());
                }).get();

        return billPaymentTransactionResponse;
    }

}

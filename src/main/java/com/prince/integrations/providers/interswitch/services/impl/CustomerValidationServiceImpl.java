package com.prince.integrations.providers.interswitch.services.impl;

import com.prince.integrations.AbstractBillerService;
import com.prince.integrations.exceptions.ApplicationException;
import com.prince.integrations.exceptions.ErrorResponse;
import com.prince.integrations.providers.interswitch.pojo.ValidateBillPaymentRequest;
import com.prince.integrations.providers.interswitch.pojo.CustomerValidationResponse;
import com.prince.integrations.providers.interswitch.services.CustomerValidationService;
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
public class CustomerValidationServiceImpl extends AbstractBillerService implements CustomerValidationService {

    private static final Logger log = LoggerFactory.getLogger(CustomerValidationServiceImpl.class);

//    @Value("${interswitch.url}")
//    private String url;

    @Value("https://sandbox.interswitchng.com")
    private String url;





    @Override
    public ResponseEntity<CustomerValidationResponse> validate( ValidateBillPaymentRequest validateBillPaymentRequest) {
        String narration = "Validating Customer...";
        HeaderUtils headerUtils = new HeaderUtils();

//        final Logger log = null;

        String getValidationUrl = url + "/api/v2/quickteller/customers/validations";
        String httpMethod = "POST";

        ResponseEntity<CustomerValidationResponse> customerValidationResponse = Try.of(() -> callClient(getValidationUrl, HttpMethod.POST, validateBillPaymentRequest, headerUtils.getHttpHeaders(httpMethod, getValidationUrl, log), narration,  CustomerValidationResponse.class, true))
                .onFailure(RestClientResponseException.class, throwable -> {
                    ErrorResponse errorResponse = JsonUtils.cast(throwable.getResponseBodyAsString(), ErrorResponse.class);
                    throw new ApplicationException(errorResponse == null ? "An unknown error occurred." : errorResponse.getMessage());
                }).get();

        return customerValidationResponse;

    }



}

package com.prince.integrations.providers.interswitch.services.impl;

import com.prince.integrations.AbstractBillerService;
import com.prince.integrations.exceptions.ApplicationException;
import com.prince.integrations.exceptions.ErrorResponse;
import com.prince.integrations.exceptions.InterswitchErrorResponse;
import com.prince.integrations.providers.interswitch.pojo.AuthenticateRequest;
import com.prince.integrations.providers.interswitch.pojo.AuthenticateResponse;
import com.prince.integrations.providers.interswitch.pojo.GetBillerResponse;
import com.prince.integrations.providers.interswitch.services.GetBillerService;
import com.prince.integrations.utils.JsonUtils;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

@Service
@Slf4j
public class GetBillerServiceImpl extends AbstractBillerService implements GetBillerService {

    private static final Logger log = LoggerFactory.getLogger(GetBillerServiceImpl.class);

    HeaderUtils headerUtils = new HeaderUtils();

    @Value("https://sandbox.interswitchng.com")
    private String url;


    @Override
    public GetBillerResponse getBillers() {
        String narration = "calling get-Billers endpoint";

//        final Logger log = null;

        String getBillersUrl = url + "/api/v2/quickteller/billers";
        String httpMethod = "GET";

        GetBillerResponse getBillerResponse = Try.of(() -> callClient( getBillersUrl, headerUtils.getHttpHeaders(httpMethod, getBillersUrl, log), narration,  GetBillerResponse.class, true))
                .onFailure(RestClientResponseException.class, throwable -> {
                    ErrorResponse errorResponse = JsonUtils.cast(throwable.getResponseBodyAsString(), ErrorResponse.class);
                    throw new ApplicationException(errorResponse == null ? "An unknown error occurred." : errorResponse.getMessage());
                }).get().getBody();

        return getBillerResponse;
    }

    public AuthenticateResponse login(){
        String narration = "calling login end point";

        AuthenticateRequest authenticateRequest = new AuthenticateRequest();
        authenticateRequest.setGrant_type("client_credentials");

        AuthenticateResponse authenticateResponse = Try.of(() -> callClient( url + "/passport/oauth/token", HttpMethod.POST, authenticateRequest,  headerUtils.getLoginRestHttpHeaders(), narration,  AuthenticateResponse.class, true))
                .onFailure(RestClientResponseException.class, throwable -> {
                    InterswitchErrorResponse errorResponse = JsonUtils.cast(throwable.getResponseBodyAsString(), InterswitchErrorResponse.class);
                    throw new ApplicationException(errorResponse == null ? "An unknown error occurred." : errorResponse.getErrors().get(0).getMessage());
                }).get().getBody();

        return authenticateResponse;
    }


}

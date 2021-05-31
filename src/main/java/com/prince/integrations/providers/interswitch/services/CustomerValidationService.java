package com.prince.integrations.providers.interswitch.services;

import com.prince.integrations.providers.interswitch.pojo.ValidateBillPaymentRequest;
import com.prince.integrations.providers.interswitch.pojo.CustomerValidationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CustomerValidationService {

    ResponseEntity<CustomerValidationResponse> validate(ValidateBillPaymentRequest validateBillPaymentRequest);
}

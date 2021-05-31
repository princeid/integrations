package com.prince.integrations.providers.interswitch.controller;

import com.prince.integrations.providers.interswitch.pojo.ValidateBillPaymentRequest;
import com.prince.integrations.providers.interswitch.pojo.CustomerValidationResponse;
import com.prince.integrations.providers.interswitch.services.CustomerValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/quickteller")
public class CustomerValidationController {

    private final CustomerValidationService customerValidationService;

    @Autowired
    public CustomerValidationController(CustomerValidationService customerValidationService) {
        this.customerValidationService = customerValidationService;
    }

    @PostMapping("/customer/validate")
    public ResponseEntity<CustomerValidationResponse> validateCustomer(@RequestBody ValidateBillPaymentRequest validateBillPaymentRequest){

        //        ApiResponse<CustomerValidationResponse> response = new ApiResponse<>(
//                customerValidationService.validate(billPaymentRequest),
//                "Validated Successfully",
//                HttpStatus.OK);
        return customerValidationService.validate(validateBillPaymentRequest);
    }
}

package com.prince.integrations.providers.interswitch.services;

import com.prince.integrations.providers.interswitch.pojo.BillPaymentTransactionRequest;
import com.prince.integrations.providers.interswitch.pojo.BillPaymentTransactionResponse;
import com.prince.integrations.providers.interswitch.pojo.CustomerValidationResponse;
import com.prince.integrations.providers.interswitch.pojo.ValidateBillPaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface BillPaymentTransactionService {

    ResponseEntity<BillPaymentTransactionResponse> payBill(BillPaymentTransactionRequest billPaymentTransactionRequest);
}

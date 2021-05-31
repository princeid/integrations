package com.prince.integrations.providers.interswitch.controller;

import com.prince.integrations.providers.interswitch.pojo.BillPaymentTransactionRequest;
import com.prince.integrations.providers.interswitch.pojo.BillPaymentTransactionResponse;
import com.prince.integrations.providers.interswitch.pojo.ValidateBillPaymentRequest;
import com.prince.integrations.providers.interswitch.services.BillPaymentTransactionService;
import com.prince.integrations.providers.interswitch.services.BillPaymentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/quickteller")
public class BillPaymentTransactionController {
    private final BillPaymentTransactionService billPaymentTransactionService;

    @Autowired
    public BillPaymentTransactionController(BillPaymentTransactionService billPaymentTransactionService) {
        this.billPaymentTransactionService = billPaymentTransactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<BillPaymentTransactionResponse> sendBillPaymentTransaction(@RequestBody BillPaymentTransactionRequest billPaymentTransactionRequest){

        //        ApiResponse<BillPaymentTransactionResponse> response = new ApiResponse<>(
//                billPaymentTransactionService.validate(billPaymentRequest),
//                "Validated Successfully",
//                HttpStatus.OK);
        return billPaymentTransactionService.payBill(billPaymentTransactionRequest);
    }
}

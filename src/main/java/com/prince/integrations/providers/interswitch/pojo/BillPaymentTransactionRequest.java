package com.prince.integrations.providers.interswitch.pojo;

import lombok.Data;

@Data
public class BillPaymentTransactionRequest {

        private Long amount;
        private String pinData;
        private String secureData;
        private Long msisdn;
        private String transactionRef;
        private String cardBin;

}

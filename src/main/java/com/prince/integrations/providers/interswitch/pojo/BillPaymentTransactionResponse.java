package com.prince.integrations.providers.interswitch.pojo;

import lombok.Data;

@Data
public class BillPaymentTransactionResponse {

    public int responseCode;
    public long shortTransactionRef;
    public String responseDescription;
    public String rechargePin;
    public String transactionRef;
    public String transactionResponseCode;
    public String transactionResponseDesc;
}

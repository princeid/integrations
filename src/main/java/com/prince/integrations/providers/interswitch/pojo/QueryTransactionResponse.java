package com.prince.integrations.providers.interswitch.pojo;

import lombok.Data;

@Data
public class QueryTransactionResponse {

    public BillPayment billPayment;

    public String amount;
    public String currencyCode;
    public String customer;
    public String customerEmail;
    public String customerMobile;
    public String paymentDate;
    public String requestReference;
    public String serviceCode;
    public String serviceName;
    public String serviceProviderId;
    public String status;
    public String surcharge;
    public String transactionRef;
    public String transactionResponseCode;
    public String transactionSet;

    @Data
    public static class BillPayment {
        public String biller;
        public String customerId1;
        public Object customerId2;
        public String paymentTypeName;
        public String paymentTypeCode;
        public String billerId;
    }
}

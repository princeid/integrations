package com.prince.integrations.providers.interswitch.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ValidateBillPaymentRequest {

    public List<Customer> customers;

    @Data
    public static class Customer{
        public String customerId;
        public String paymentCode;
    }

}

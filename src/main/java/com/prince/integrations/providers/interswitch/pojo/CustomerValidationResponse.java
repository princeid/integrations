package com.prince.integrations.providers.interswitch.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CustomerValidationResponse {

    @JsonProperty("Customers")
    public List<Customer> customers;

    @Data
    public static class Customer{
        public String customerId;
        public String paymentCode;
        public String responseCode;
        public String responseDescription;
        public String fullName;
        public String amount;
        public String amountType;
        public String amountTypeDescription;
    }
}

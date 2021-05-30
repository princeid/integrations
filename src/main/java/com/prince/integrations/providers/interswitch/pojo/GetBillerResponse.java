package com.prince.integrations.providers.interswitch.pojo;

import lombok.Data;

import java.util.List;

@Data
public class GetBillerResponse {
    public List<Biller> billers;

    public static class Biller{
        public String categoryid;
        public String categoryname;
        public String categorydescription;
        public String billerid;
        public String billername;
        public String customerfield1;
        public String customerfield2;
        public String supportemail;
        public String paydirectProductId;
        public String paydirectInstitutionId;
        public String narration;
        public String shortName;
        public String surcharge;
        public String currencyCode;
        public String quickTellerSiteUrlName;
        public String amountType;
        public String currencySymbol;
        public String customSectionUrl;
        public String logoUrl;
        public String type;
        public String url;
    }
}

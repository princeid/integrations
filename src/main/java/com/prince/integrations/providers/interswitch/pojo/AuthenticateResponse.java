package com.prince.integrations.providers.interswitch.pojo;

import lombok.Data;

@Data
public class AuthenticateResponse {
    public String access_token;
    public String token_type;
    public int expires_in;
    public String scope;
    public String env;
    public String client_logo;
    public String client_description;
    public String jti;
}

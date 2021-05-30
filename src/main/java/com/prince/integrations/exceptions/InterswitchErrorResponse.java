package com.prince.integrations.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InterswitchErrorResponse {
    public List<Error> errors;
    public Error error;

    @Data
    public static class Error{
        public String code;
        public String message;
    }

}

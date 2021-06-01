package com.prince.integrations.providers.interswitch.controller;

import com.prince.integrations.providers.interswitch.pojo.QueryTransactionResponse;
import com.prince.integrations.providers.interswitch.services.QueryTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v2/quickteller")
public class QueryTransactionController {

    private final QueryTransactionService queryTransactionService;

    @Autowired
    public QueryTransactionController(QueryTransactionService queryTransactionService) {

        this.queryTransactionService = queryTransactionService;
    }

    @GetMapping("/transactions/{requestReference}")
    public QueryTransactionResponse queryTransaction(@PathVariable("requestReference") long requestReference){

        return queryTransactionService.queryTransaction(requestReference);
    }

}

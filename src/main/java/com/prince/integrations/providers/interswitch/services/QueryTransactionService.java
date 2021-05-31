package com.prince.integrations.providers.interswitch.services;

import com.prince.integrations.providers.interswitch.pojo.QueryTransactionResponse;
import org.springframework.stereotype.Service;

@Service
public interface QueryTransactionService {

    QueryTransactionResponse queryTransaction(long requestReference);
}

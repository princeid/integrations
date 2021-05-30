package com.prince.integrations.providers.interswitch.services;

import com.prince.integrations.providers.interswitch.pojo.GetBillerResponse;
import org.springframework.stereotype.Service;

@Service
public interface InterswitchService {

    GetBillerResponse getBillers();
}

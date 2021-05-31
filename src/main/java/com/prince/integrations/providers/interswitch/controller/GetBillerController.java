package com.prince.integrations.providers.interswitch.controller;

import com.prince.integrations.providers.interswitch.pojo.GetBillerResponse;
import com.prince.integrations.providers.interswitch.services.GetBillerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/quickteller")
public class GetBillerController {

    private final GetBillerService getBillerService;

    @Autowired
    public GetBillerController(GetBillerService getBillerService) {

        this.getBillerService = getBillerService;
    }

    @GetMapping("/billers")
    public GetBillerResponse getAllBillers(){

        return getBillerService.getBillers();
    }

}

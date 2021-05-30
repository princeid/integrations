package com.prince.integrations.providers.interswitch.controller;

import com.prince.integrations.providers.interswitch.pojo.GetBillerResponse;
import com.prince.integrations.providers.interswitch.services.InterswitchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/quickteller")
public class InterswitchController {

    private final InterswitchService interswitchService;

    public InterswitchController(InterswitchService interswitchService) {
        this.interswitchService = interswitchService;
    }

    @GetMapping("/billers")
    public GetBillerResponse getAllBillers(){
        return interswitchService.getBillers();
    }

}

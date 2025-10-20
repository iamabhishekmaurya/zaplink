package io.zaplink.api_gateway_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiGatewayController {
    @GetMapping()
    public String getMethodName() {
        return "Hello";
    }
    
}

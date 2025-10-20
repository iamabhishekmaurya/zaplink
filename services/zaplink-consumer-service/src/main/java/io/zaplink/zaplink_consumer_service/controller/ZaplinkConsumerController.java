package io.zaplink.zaplink_consumer_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ZaplinkConsumerController {
    @GetMapping()
    public String getString() {
        return "Hello World!";
    }
    
}

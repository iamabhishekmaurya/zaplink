package io.zaplink.processor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ZaplinkProcessorController {
    @GetMapping()
    public String getString() {
        return "Hello World!";
    }
    
}

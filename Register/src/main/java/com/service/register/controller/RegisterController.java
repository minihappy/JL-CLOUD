package com.service.register.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/register")
public class RegisterController {
    @Value("${server.port}")
    String port;
    RestTemplate restTemplate;

    @Autowired
    public void restTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/add")
    public String add() {
        return "报名成功" + port;
    }
}

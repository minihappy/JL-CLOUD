package com.service.signups.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/signup")
public class SignUpController {
    RestTemplate restTemplate;

    @Autowired
    public void restTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    @Value("${server.port}")
    String port;

    @GetMapping("/add")
    public String add() {
        String forObject = restTemplate.getForObject("http://register-nacos/register/add", String.class);
        return "提交新增:" + port + "[" + forObject + "]";
    }
}

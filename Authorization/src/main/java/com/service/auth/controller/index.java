package com.service.auth.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class index {
    @GetMapping("/hello")
//    @PreAuthorize("hasAuthority('ADMIN')")//限制权限必须是ADMIN才能够访问
    public String s() {
        return "hello";
    }
}

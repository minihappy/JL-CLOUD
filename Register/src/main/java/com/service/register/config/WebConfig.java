//package com.service.register.config;
//
///**
// * @author hjl
// * @description
// * @date 2022/12/5 1:33
// */
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*") // SpringBoot2.4.0 [allowedOriginPatterns]代替[allowedOrigins]
//                .allowedMethods("*")
//                .maxAge(3600)
//                .allowCredentials(true);
//    }
//}

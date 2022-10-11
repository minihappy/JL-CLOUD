package com.service.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * @author hjl
 * @date 2022/9/20 10:51
 */
@RestController
public class KeyPairController {
//    @Autowired
//    private KeyPair keyPair;
    @GetMapping("/oauth2/hello")
    public String hello(){
        return "hello spring cloud";
    }
//    @GetMapping("/rsa/publicKey")
//    public Map<String, Object> getKey() {
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAKey key = new RSAKey.Builder(publicKey).build();
//        return new JWKSet(key).toJSONObject();
//    }
}

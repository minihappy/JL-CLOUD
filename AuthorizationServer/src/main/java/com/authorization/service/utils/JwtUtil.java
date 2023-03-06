package com.authorization.service.utils;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;

import java.security.KeyPair;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT工具类
 */

public class JwtUtil {
    //有效期为
    public static final Long JWT_TTL = 60 * 60 * 1000L;// 60 * 60 *1000  一个小时


    public String getUUID(RSAKey rsaKey) {

        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    /**
     * 生成jtw
     *
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    @SneakyThrows
    public String createJWT(String subject, Map<String, Object> customClaims, KeyPair keyPair, RSAKey rsaKey) {
        JWSSigner signer = new RSASSASigner(keyPair.getPrivate());
        JWTClaimsSet builder = getJwtBuilder(subject, null, getUUID(rsaKey), customClaims);// 设置过期时间
        SignedJWT signedJWT = createSignedJWT(builder, rsaKey);
        signedJWT.sign(signer);
        String jwt = signedJWT.serialize();
        return jwt;
    }

    /**
     * 生成jtw
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    @SneakyThrows
    public String createJWT(String subject, Long ttlMillis, Map<String, Object> customClaims, KeyPair keyPair, RSAKey rsaKey) {
        JWSSigner signer = new RSASSASigner(keyPair.getPrivate());
        JWTClaimsSet builder = getJwtBuilder(subject, ttlMillis, getUUID(rsaKey), customClaims);// 设置过期时间
        SignedJWT signedJWT = createSignedJWT(builder, rsaKey);
        signedJWT.sign(signer);
        String jwt = signedJWT.serialize();
        return jwt;
    }

    /**
     * 创建token
     *
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    @SneakyThrows
    public String createJWT(String id, String subject, Long ttlMillis, Map<String, Object> customClaims, KeyPair keyPair, RSAKey rsaKey) {
        JWSSigner signer = new RSASSASigner(keyPair.getPrivate());
        JWTClaimsSet builder = getJwtBuilder(subject, ttlMillis, id, customClaims);// 设置过期时间
        SignedJWT signedJWT = createSignedJWT(builder, rsaKey);
        signedJWT.sign(signer);
        String jwt = signedJWT.serialize();
        return jwt;
    }

//    public static void main(String[] args) throws Exception {
//        RSAKey rsaJWK = new RSAKeyGenerator(2048)
//                .keyID(UUID.randomUUID().toString())
//                .generate();
//        RSAKey rsaPublicJWK = rsaJWK.toPublicJWK();
//
//// Create RSA-signer with the private key
//        JWSSigner signer = new RSASSASigner(rsaJWK);
//
//// Prepare JWT with claims set
//        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
//                .subject("alice")
//                .issuer("https://c2id.com")
//                .expirationTime(new Date(new Date().getTime() + 60 * 1000))
//                .build();
//
//        SignedJWT signedJWT = new SignedJWT(
//                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(rsaJWK.getKeyID()).build(),
//                claimsSet);
//
//// Compute the RSA signature
//        signedJWT.sign(signer);
//
//// To serialize to compact form, produces something like
//// eyJhbGciOiJSUzI1NiJ9.SW4gUlNBIHdlIHRydXN0IQ.IRMQENi4nJyp4er2L
//// mZq3ivwoAjqa1uUkSBKFIX7ATndFF5ivnt-m8uApHO4kfIFOrW7w2Ezmlg3Qd
//// maXlS9DhN0nUk_hGI3amEjkKd0BWYCB8vfUbUv0XGjQip78AI4z1PrFRNidm7
//// -jPDm5Iq0SZnjKjCNS5Q15fokXZc8u0A
//        String s = signedJWT.serialize();
//// On the consumer side, parse the JWS and verify its RSA signature
//        signedJWT = SignedJWT.parse(s);
//        JWSVerifier verifier = new RSASSAVerifier(rsaPublicJWK);
//        System.out.println(signedJWT.verify(verifier));
//// Retrieve / verify the JWT claims according to the app requirements
//        System.out.println(signedJWT.getJWTClaimsSet().getSubject());
//        System.out.println(signedJWT.getJWTClaimsSet().getIssuer());
//        System.out.println(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime()));
////        String jwt = createJWT("2123");
////        Claims claims = parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyOTY2ZGE3NGYyZGM0ZDAxOGU1OWYwNjBkYmZkMjZhMSIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTYzOTk2MjU1MCwiZXhwIjoxNjM5OTY2MTUwfQ.NluqZnyJ0gHz-2wBIari2r3XpPp06UMn4JS2sWHILs0");
////        String subject = claims.getSubject();
////        System.out.println(subject);
////        System.out.println(claims);
//    }

    private static JWTClaimsSet getJwtBuilder(String subject, Long ttlMillis, String uuid, Map<String, Object> customClaims) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        JWTClaimsSet claimsSet;
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .subject(subject)
                .issuer("http://127.0.0.1:9000")
                .expirationTime(expDate)
                .issueTime(now)
                .jwtID(uuid);
        customClaims.forEach((key, value) -> {
            builder.claim(key, value);
        });
        claimsSet = builder.build();
        return claimsSet;
    }

    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public SignedJWT parseJWT(String jwt, JWTClaimsSet jwtClaimsSet, JWSSigner signer) throws Exception {
//        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) keyPair.getPublic());
        SignedJWT parse = SignedJWT.parse(jwt);
        return parse;
    }


    private SignedJWT createSignedJWT(JWSAlgorithm jwsAlgorithm, JOSEObjectType joseObjectType, JWTClaimsSet jwtClaimsSet, RSAKey rsaKey) {
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(jwsAlgorithm).type(joseObjectType).keyID(getUUID(rsaKey)).build(),
                jwtClaimsSet);
        return signedJWT;
    }

    private SignedJWT createSignedJWT(JWTClaimsSet jwtClaimsSet, RSAKey rsaKey) {
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build(),
                jwtClaimsSet);
        return signedJWT;
    }
}

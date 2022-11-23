package com.service.auth.config.oauth2;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer<>();
        //配置OIDC
        authorizationServerConfigurer.oidc(Customizer.withDefaults());
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        return http.requestMatcher(endpointsMatcher)
                .authorizeRequests((authorizeRequests) -> {
                    ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) authorizeRequests.anyRequest()).authenticated();
                }).csrf((csrf) -> {
                    csrf.ignoringRequestMatchers(new RequestMatcher[]{endpointsMatcher});
                }).apply(authorizationServerConfigurer)
                .and()
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)//启动oauth2资源服务配置，保护 oidc中的/userinfo信息
                .exceptionHandling(exceptions -> exceptions.
                        authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))//重定向到登录页
                .apply(authorizationServerConfigurer)
                .and()
                .build();
    }
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//
//        return http.formLogin(Customizer.withDefaults()).build();
//    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        // ---------- 1、检查当前客户端是否已注册
        // 操作数据库对象
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client-app")
                .clientSecret(passwordEncoder.encode("123456"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:9201/login/oauth2/code/api-client-oidc")
                .redirectUri("http://127.0.0.1:9201/authorized")
                .scope(OidcScopes.OPENID)
                .scope("ADMIN")
                .scope("TEST")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        RegisteredClient registeredClient_1 = registeredClientRepository.findByClientId("client-app");
        if (!(registeredClient_1 instanceof RegisteredClient) || registeredClient_1.getId().isEmpty()) {
            registeredClientRepository.save(registeredClient);
        }
        return registeredClientRepository;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {//声明式添加jwtDecoder,否则就要去yml中添加jwk-set-url
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);//根据RSA构造jwkSet
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);//JWKSource根据jwkSet进行查找返回一个jwk源
    }

    @Bean
    public RSAKey generateRsa(KeyPair keyPair) {//从密钥对中构造RSAKey
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();//生成公钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();//生成私钥
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    /**
     * KeyPairGenerator类用于生成公钥和私钥对。 密钥对生成器使用getInstance工厂方法（返回给定类的实例的静态方法）构造。
     * 用于特定算法的密钥对生成器创建可以与该算法一起使用的公钥/私钥对。 它还将算法特定的参数与生成的每个密钥相关联。
     * <p>
     * 生成密钥对的方法有两种：以算法无关的方式，并以算法特定的方式。 两者之间的唯一区别是对象的初始化：
     * <p>
     * 独立于算法的初始化
     * 所有密钥对生成器共享密钥大小和随机源的概念。 对于不同的算法来说，keyize被不同地解释（例如，在DSA算法的情况下，keysize对应于模数的长度）。 此KeyPairGenerator类中有一个initialize方法，它接受这两个普遍共享的参数类型。 还有一个只带有keysize参数，并使用SecureRandom实现的最高优先级安装的提供作为随机源。 （如果没有一个已安装的提供商提供SecureRandom的SecureRandom ，则使用系统提供的随机源。）
     * <p>
     * 由于在调用上述与算法无关的initialize方法时没有指定其他参数，因此提供者如何处理与每个密钥相关联的特定于算法的参数（如果有的话）。
     * <p>
     * 如果算法是DSA算法和密钥大小（模数的大小）为512，768，或1024，那么Sun提供使用为一组预先计算的值的p ， q和g参数。 如果模数大小不是上述值之一， Sun提供商将创建一组新的参数。 其他提供商可能预先计算的参数集超过上述三个模数大小。 还有一些可能没有预先计算的参数的列表，而不是总是创建新的参数集。
     * <p>
     * 特定于算法的初始化
     * 对于已经存在一组特定于算法的参数（例如，DSA中所谓的社区参数 ）的情况，有两种initialize方法具有AlgorithmParameterSpec参数。 其中也有一个SecureRandom的说法，而其他使用SecureRandom实现的最高优先级安装的提供作为随机源。 （如果没有一个安装的提供商提供SecureRandom的SecureRandom ，则使用系统提供的随机源。）
     * <p>
     * 如果客户端没有显式初始化KeyPairGenerator（通过调用initialize方法），则每个提供程序都必须提供（并记录）默认的初始化。 例如， Sun提供商使用1024位的默认模数大小（keysize）。
     * <p>
     * 注意，这个类是抽象的，由于历史原因而从KeyPairGeneratorSpi延伸。 应用程序开发人员只应注意本KeyPairGenerator课程中定义的方法; 超类中的所有方法都适用于希望提供自己的密钥对生成器实现的加密服务提供商。
     * <p>
     * Java平台的每个实现都需要支持以下标准KeyPairGenerator算法，并在括号中键入：
     * <p>
     * DiffieHellman （1024）
     * DSA （1024）
     * RSA （ RSA ）
     * 这些算法的描述KeyPairGenerator section Java加密体系结构标准算法名称的文档。 请参阅发行说明文件以了解是否支持其他算法。
     *
     * @return
     */
    @Bean
    public KeyPair generateRsaKey() {//从密钥对生产者生产一个密钥对
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");//返回一个RSA加密的KeyPairGenerator密钥对生产者
            keyPairGenerator.initialize(2048);//初始化密钥的大小
            keyPair = keyPairGenerator.generateKeyPair();//生成一个密钥对
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder()
                .issuer("http://auth-server:9000")
                .build();
    }
}

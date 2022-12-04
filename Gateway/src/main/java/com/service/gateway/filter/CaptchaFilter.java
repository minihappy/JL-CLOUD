package com.service.gateway.filter;


import com.service.gateway.exception.CaptchaException;
import com.service.gateway.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;


import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;


import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class CaptchaFilter implements GlobalFilter, Ordered {

    @Autowired
    RedisCache redisCache;
//    @Bean
//    public RouteLocator routes(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("cache_request_body_route", r -> r.path("/downstream/**")
//                        .filters(f -> f.prefixPath("/httpbin")
//                                .cacheRequestBody(String.class).uri(uri))
//                        .build();
//    }

    private boolean isBlank(String s) {
        if (ObjectUtils.isEmpty(s) || StringUtils.hasText(" ")) {
            return true;
        } else {
            return false;
        }
    }

    // 校验验证码逻辑
    private void validate(ServerWebExchange exchange) {
//        Map<String, String> stringStringMap = exchange.getRequest().getHeaders().toSingleValueMap();
//        log.info("map:{}", stringStringMap);
//
//        //打印head
//        Mono<MultiValueMap<String, String>> formData = exchange.getFormData();
//        //获取form-data中的内容
//        formData.subscribe(map -> {
//            Map<String, String> formMap = map.toSingleValueMap();
//            String code = formMap.get("code");
//            String uid = formMap.get("token");
//            log.info("formMap:{}", formMap);
//            if ((isBlank(code) || isBlank(uid)) || !code.equals(redisCache.getCacheMapValue("captcha", uid))) {
//                throw new CaptchaException("验证码不正确!");//授权凭证异常
//            } else {
//                redisCache.delCacheMapValue("captcha", uid);
//            }
//        });
//        //如果是获取post或者get请求之类的body的话就从exchange.getRequest().getBody()中获取
//        Map<String, String> queryParams = exchange.getRequest().getQueryParams().toSingleValueMap();
//        log.info("queryParams:{}", queryParams);
//        URI uri = exchange.getRequest().getURI();
//        log.info("uri:{}", uri);
    }

    private final List<HttpMessageReader<?>> messageReaders = getMessageReaders();

    private List<HttpMessageReader<?>> getMessageReaders() {
        return HandlerStrategies.withDefaults().messageReaders();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        if ((uri.getPath().indexOf("/user/login") > 0||uri.getPath().indexOf("/registerPersonnel/inRegister") > 0) && request.getMethod().name().equals("POST")) {//获取当前请求是不是登录的POST请求
            try {
                return logRequestBody(exchange, chain);
            } catch (CaptchaException c) {
                return Mono.error(c);//如果验证码错误的话不放行，返回验证码错误
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> logRequestBody(ServerWebExchange exchange, GatewayFilterChain chain) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    DataBufferUtils.retain(dataBuffer);//使用dataBuffer缓存数据
                    Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));//将数据缓存到Flux中
                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public Flux<DataBuffer> getBody() {//重写GetBody方法，将缓存的数据替换到heepRequest
                            return cachedFlux;
                        }
                    };

                    return ServerRequest
                            // must construct a new exchange instance, same as below
                            .create(exchange.mutate().request(mutatedRequest).build(), messageReaders)
                            .bodyToMono(MultiValueMap.class)
                            .flatMap(body -> {
                                // do what ever you want with this body string, I logged it.
                                List code = (ArrayList) body.get("code");
                                List uid = (ArrayList) body.get("token");
                                if ((isBlank(code.get(0).toString()) || isBlank(uid.get(0).toString())) || !code.get(0).equals(redisCache.getCacheMapValue("captcha", uid.get(0).toString()))) {
                                    throw new CaptchaException("验证码不正确!");//授权凭证异常
                                } else {
                                    redisCache.delCacheMapValue("captcha", uid.get(0).toString());
                                }
                                // by putting reutrn statement in here, urge Java to execute the above statements
                                // put this final return statement outside then you'll find out that above statements inside here are not executed.
                                return chain.filter(exchange.mutate().request(mutatedRequest).build());
                            });
                });
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

package com.gateway.service.filter;


import com.gateway.service.config.CorsUrlsConfig;
import com.gateway.service.config.IgnoreUrlsConfig;
import com.gateway.service.utils.RequestUtils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.DefaultCorsProcessor;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * 在 CORS 之前执行
 */
@Component
public class CorsBeforeWebFilter implements WebFilter, Ordered {

    /**
     * 最低优先级（最大值）：0
     * <p>
     * 大于 0 无效
     */
    public static final int ORDERED = Ordered.HIGHEST_PRECEDENCE + 60000;

    @Setter
    private int order = ORDERED;

    @Override
    public int getOrder() {
        return order;
    }

    private CorsUrlsConfig ignoreUrlsConfig;

    @Autowired
    public void setCloudCorsProperties(CorsUrlsConfig ignoreUrlsConfig) {
        this.ignoreUrlsConfig = ignoreUrlsConfig;
    }

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        URI uri = request.getURI();
        String path = uri.getPath();

        String origin = RequestUtils.getOrigin(request);

        // origin 为空时的处理
        if (origin == null || "null".equals(origin)) {
            List<String> allowOrginNullList = ignoreUrlsConfig.getUrls();
            if (allowOrginNullList != null) {
                // 解决 form action 提交数据无 origin 跨域问题
                if (allowOrginNullList.contains(path)) {
                    String schemeHost = RequestUtils.getSchemeHost(request);
                    response.getHeaders().setAccessControlAllowOrigin(schemeHost);
                }
            }
        } else {
            // 允许跨域时携带授权信息
            response.getHeaders().addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                    request.getHeaders().getAccessControlRequestHeaders());

            // 此处为了防止网关调用服务时，服务不存在，导致跨域问题
            HttpMethod method = request.getMethod();
            if (HttpMethod.OPTIONS == method) {
                // 响应 Access-Control-Allow-Origin 为 origin
                response.getHeaders().setAccessControlAllowOrigin(origin);
                response.getHeaders().setAccessControlAllowCredentials(true);
            }

        }

        return chain.filter(exchange);
    }

}

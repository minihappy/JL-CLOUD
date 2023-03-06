package com.gateway.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author hjl
 * @version 1.0
 * @description 跨域配置
 * @date 2023/2/16 10:04
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties("cors.request")
public class CorsUrlsConfig {
    private List<String> urls;
}

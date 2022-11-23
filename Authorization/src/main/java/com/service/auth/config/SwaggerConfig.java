//package com.service.auth.config;
//
//import io.swagger.annotations.ApiOperation;
//import springfox.documentation.service.SecurityScheme;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.oas.annotations.EnableOpenApi;
//import springfox.documentation.service.*;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.SecurityContext;
//import springfox.documentation.spring.web.plugins.Docket;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * @program: payment-demo
// * @description:
// * @author: Acerola
// * @create: 2022-03-26 09:45
// **/
//@Configuration
//@EnableOpenApi
////@EnableSwaggerBootstrapUI
//public class SwaggerConfig {
//    @Bean
//    public Docket desertsApi() {
//        return new Docket(DocumentationType.OAS_30)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.service.auth.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .groupName("hjl")
//                .enable(true);
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("Swagger3测试文档")
//                .description("文档描述信息")
//                .contact(new Contact("deserts", "#", "550610344@qq.com"))
//                .version("1.0")
//                .build();
//    }
////    @Value("${swagger.enabled}")
////    private boolean enable;
////
////    /**
////     * 创建API
////     * http:IP:端口号/swagger-ui/index.html 原生地址
////     * http:IP:端口号/doc.html bootStrap-UI地址
////     */
////    @Bean
////    public Docket createRestApi() {
////        return new Docket(DocumentationType.OAS_30).pathMapping("/")
////                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
////                /*.enable(enable)*/
////                .apiInfo(apiInfo())
////                // 设置哪些接口暴露给Swagger展示
////                .select()
////                // 扫描所有有注解的api，用这种方式更灵活
////                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
////                // 扫描指定包中的swagger注解
////                .apis(RequestHandlerSelectors.basePackage("com.service.auth.controller"))
////                // 扫描所有 .apis(RequestHandlerSelectors.any())
////                .paths(PathSelectors.regex("(?!/ApiError.*).*"))
////                .paths(PathSelectors.any())
////                .build()
////                // 支持的通讯协议集合
////                .protocols(newHashSet("https", "http"))
////                .securitySchemes(securitySchemes())
////                .securityContexts(securityContexts());
////
////    }
////
////    /**
////     * 支持的通讯协议集合
////     * @param type1
////     * @param type2
////     * @return
////     */
////    private Set<String> newHashSet(String type1, String type2){
////        Set<String> set = new HashSet<>();
////        set.add(type1);
////        set.add(type2);
////        return set;
////    }
////
////    /**
////     * 认证的安全上下文
////     */
////    private List<SecurityScheme> securitySchemes() {
////        List<SecurityScheme> securitySchemes = new ArrayList<>();
////        securitySchemes.add((SecurityScheme) new ApiKey("token", "token", "header"));
////        return securitySchemes;
////    }
////
////    /**
////     * 授权信息全局应用
////     */
////    private List<SecurityContext> securityContexts() {
////        List<SecurityContext> securityContexts = new ArrayList<>();
////        securityContexts.add(SecurityContext.builder()
////                .securityReferences(defaultAuth())
////                .forPaths(PathSelectors.any()).build());
////        return securityContexts;
////    }
////
////    private List<SecurityReference> defaultAuth() {
////        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
////        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
////        authorizationScopes[0] = authorizationScope;
////        List<SecurityReference> securityReferences = new ArrayList<>();
////        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
////        return securityReferences;
////    }
////
////    /**
////     * 添加摘要信息
////     */
////    private ApiInfo apiInfo() {
////        // 用ApiInfoBuilder进行定制
////        return new ApiInfoBuilder()
////                // 设置标题
////                .title("授权服务器")
////                // 描述
////                .description("对微服务需要授权的用户进行权限认证")
////                // 作者信息
////                .contact(new Contact("JL-Cloud", null, null))
////                // 版本
////                .version("版本号:v1.0")
////                //协议
////                .license("The Apache License")
////                //协议url
////                .licenseUrl("http://www.baidu.com")
////                .build();
////    }
//}

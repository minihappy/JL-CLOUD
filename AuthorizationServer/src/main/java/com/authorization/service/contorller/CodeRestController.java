package com.authorization.service.contorller;


import com.authorization.service.properties.ClientProperties;
import com.authorization.service.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 授权码 Code {@link RestController}
 *
 * @author xuxiaowei
 * @since 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/code")
public class CodeRestController {

    private ClientProperties cloudClientProperties;

    /**
     * 在这里只使用 {@link RestTemplate} 而不使用 <code>@FeignClient</code>， 原因是：本服务调用其他服务较少，单独引入
     * <code>@FeignClient</code> 并不合适
     */
    private RestTemplate restTemplate;

    @Autowired
    public void setCloudClientProperties(ClientProperties cloudClientProperties) {
        this.cloudClientProperties = cloudClientProperties;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 根据 授权码、状态码 获取 授权Token
     *
     * @param request  请求
     * @param response 响应
     * @param session  Session，不存在时自动创建
     * @param code     授权码
     * @param state    状态码
     * @throws IOException 重定向异常
     */
    @GetMapping(params = {OAuth2ParameterNames.CODE, OAuth2ParameterNames.STATE})
    public void index(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                      @RequestParam(OAuth2ParameterNames.CODE) String code,
                      @RequestParam(OAuth2ParameterNames.STATE) String state) throws IOException {

//        String stateName = cloudClientProperties.getStateName();
//        String sessionState = session.getAttribute(stateName) + "";
//        session.removeAttribute(stateName);
//        if (!StringUtils.hasText(sessionState) || !sessionState.equals(state)) {
//            log.error(String.valueOf("非法获取Token"));
//            return;
//        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>(8);
        map.put(OAuth2ParameterNames.CODE, code);
        map.put(OAuth2ParameterNames.STATE, state);
        map.put(OAuth2ParameterNames.CLIENT_ID, cloudClientProperties.getClientId());
        map.put(OAuth2ParameterNames.CLIENT_SECRET, cloudClientProperties.getClientSecret());
        map.put(OAuth2ParameterNames.REDIRECT_URI, cloudClientProperties.getRedirectUri());

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        String accessTokenUri = cloudClientProperties.accessTokenUri();
        @SuppressWarnings("unchecked")
        Map<String, Object> postForObject = restTemplate.postForObject(accessTokenUri, httpEntity, Map.class, map);
        DefaultMapOAuth2AccessTokenResponseConverter oauth2AccessTokenResponseConverter = new DefaultMapOAuth2AccessTokenResponseConverter();
        assert postForObject != null;
        OAuth2AccessTokenResponse oauth2AccessTokenResponse = oauth2AccessTokenResponseConverter.convert(postForObject);
        String homePage;
        final Object sessionHomePageObj = null;
//        final Object sessionHomePageObj = session.getAttribute(sessionState);
//        session.removeAttribute(sessionState);
        if (sessionHomePageObj == null) {
            homePage = cloudClientProperties.getHomePage();
        } else {
            try {
                final String sessionHomePage = sessionHomePageObj + "";
                new URL(sessionHomePage);
                log.info("使用登录参数中的登录成功主页地址：{}", sessionHomePage);
                homePage = sessionHomePage;
            } catch (Exception e) {
                log.error("非法登录成功主页地址：", e);
                homePage = cloudClientProperties.getHomePage();
                log.warn("使用默认登录成功主页地址：{}", homePage);
            }
        }

        String accessToken = oauth2AccessTokenResponse.getAccessToken().getTokenValue();
        OAuth2RefreshToken oAuth2RefreshToken = oauth2AccessTokenResponse.getRefreshToken();
        String refreshToken;
        if (oAuth2RefreshToken == null) {
            refreshToken = "";
        } else {
            refreshToken = oAuth2RefreshToken.getTokenValue();
        }

        // store=true：更新缓存Token相关信息

        response.sendRedirect(
                String.format("%s?store=true&accessToken=%s&refreshToken=%s", homePage, accessToken, refreshToken));
    }

    /**
     * 授权失败
     *
     * @param request          请求
     * @param response         响应
     * @param session          Session，不存在时自动创建
     * @param error            错误类型
     * @param errorDescription 错误描述
     * @param errorUri         错误描述的URI
     * @param state            状态码
     * @return 返回 授权失败
     */
    @GetMapping(params = {OAuth2ParameterNames.ERROR, OAuth2ParameterNames.ERROR_DESCRIPTION,
            OAuth2ParameterNames.STATE, OAuth2ParameterNames.ERROR_URI})
    public Result errorState(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                             @RequestParam(OAuth2ParameterNames.ERROR) String error,
                             @RequestParam(OAuth2ParameterNames.ERROR_DESCRIPTION) String errorDescription,
                             @RequestParam(OAuth2ParameterNames.ERROR_URI) String errorUri,
                             @RequestParam(OAuth2ParameterNames.STATE) String state) {

        // @formatter:off
        return Result.fail(400, "授权失败", Arrays.asList("error", error, "error_description", errorDescription, "error_uri", errorUri, "state", state));

        // @formatter:on
    }

}

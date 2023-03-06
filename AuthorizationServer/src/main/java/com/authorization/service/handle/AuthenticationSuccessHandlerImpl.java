package com.authorization.service.handle;

//import cloud.xuxiaowei.core.properties.CloudSecurityProperties;
//import cloud.xuxiaowei.passport.entity.UsersLogin;
//import cloud.xuxiaowei.passport.service.IUsersLoginService;
//import cloud.xuxiaowei.passport.utils.HandlerUtils;
//import cloud.xuxiaowei.system.service.IUsersService;
//import cloud.xuxiaowei.utils.Constant;
//import cloud.xuxiaowei.utils.RequestUtils;
//import cloud.xuxiaowei.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 身份验证成功处理程序
 *
 * @author xuxiaowei
 * @see ForwardAuthenticationSuccessHandler
 * @since 0.0.1
 */
@Slf4j
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
//
//	private JavaMailSender javaMailSender;
//
//	private MailProperties mailProperties;
//
//	private CloudSecurityProperties cloudSecurityProperties;
//
//	private IUsersService usersService;
//
//	private IUsersLoginService usersLoginService;
//
//	@Autowired
//	public void setUsersService(IUsersService usersService) {
//		this.usersService = usersService;
//	}
//
//	@Autowired
//	public void setMailProperties(MailProperties mailProperties) {
//		this.mailProperties = mailProperties;
//	}
//
//	/**
//	 * 注意： 当未成功配置邮箱时，{@link Autowired} 直接注入将会失败，导致程序无法启动 故将 {@link Autowired} 的 required
//	 * 设置为 false，避免程序启动失败。使用时请判断该值是否为 null
//	 */
//	@Autowired(required = false)
//	public void setJavaMailSender(JavaMailSender javaMailSender) {
//		this.javaMailSender = javaMailSender;
//	}
//
//	@Autowired
//	public void setCloudSecurityProperties(CloudSecurityProperties cloudSecurityProperties) {
//		this.cloudSecurityProperties = cloudSecurityProperties;
//	}
//
//	@Autowired
//	public void setUsersLoginService(IUsersLoginService usersLoginService) {
//		this.usersLoginService = usersLoginService;
//	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
		System.out.println("======="+authentication);

//		// 将当前用户名放入日志中
//		String userName = SecurityUtils.getUserName(authentication);
//		// 将当前用户ID放入日志中
//		Long usersId = SecurityUtils.getUsersId(authentication);
//
//		MDC.put(Constant.NAME, userName);
//		MDC.put(Constant.USERS_ID, String.valueOf(usersId));
//
//		UsersLogin usersLogin = HandlerUtils.usersLogin(userName, true, request, null);
//		usersLoginService.save(usersLogin);
//
//		String successForwardUrl = cloudSecurityProperties.getSuccessForwardUrl();
//		Assert.isTrue(UrlUtils.isValidRedirectUrl(successForwardUrl), () -> "'" + successForwardUrl + "' 不是有效的转发URL");

		request.getRequestDispatcher("/user/login").forward(request, response);
//
//		String remoteHost = request.getRemoteHost();
//		String userAgent = RequestUtils.getUserAgent(request);

//		String subject = "登录系统成功";
//		String result = "成功";
//		Runnable runnable = () -> HandlerUtils.send(usersService, javaMailSender, mailProperties, userName, subject,
//				result, remoteHost, userAgent);
//		new Thread(runnable).start();
	}

}

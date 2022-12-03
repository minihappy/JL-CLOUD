package com.service.register.Interceptor;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hjl
 * @description
 * @date 2022/12/2 10:19
 */
public class CaptchaHandle implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {    //preHandle:在方法调用前使用
        //判断用户是否登录，未登录重定向到登录页面
        String captcha = request.getParameter("captcha");
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/admin");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        //就是Controller 方法调用之后执行，但是它会在DispatcherServlet 进行视图返回渲染之前被调用
        // 所以我们可以在这个方法中对Controller 处理之后的ModelAndView 对象进行操作，比如说设置cookie，返回给前端。
        // postHandle 方法被调用的方向跟preHandle 是相反的，也就是说先声明的Interceptor 的postHandle 方法反而会后执行
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        //preHandle返回true的时候才执行
//该方法将在整个请求结束之后，也就是在DispatcherServlet 渲染了对应的视图之后执行
    }

}

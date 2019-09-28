package com.ddpzp.xiaogu_word.filter;

import com.alibaba.fastjson.JSON;
import com.ddpzp.xiaogu_word.common.Constants;
import com.ddpzp.xiaogu_word.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by dd
 * Date 2019/7/27 0:49
 */

@WebFilter(urlPatterns = "/gu/*", filterName = "loginFilter")
@Slf4j
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String uri = httpServletRequest.getRequestURI();
        log.info("request ip:[{}],uri:[{}]", request.getRemoteAddr(), uri);
        //跳过登录
        if ("/gu/user/login".equals(uri)) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = httpServletRequest.getSession();
        if (session == null) {
            log.info("session is empty!");
            //没有session返回主页
            httpServletResponse.sendRedirect("/");
            return;
        }
        String loginUser = (String) session.getAttribute(Constants.SESSION_USER_KEY);
        //未登录
        if (StringUtils.isBlank(loginUser)) {
            //如果是后台路由的页面，重定向回主页
            if (uri.contains("/page/")) {
                httpServletResponse.sendRedirect("/");
                log.info("No login，redirect to index...");
            } else {
                //如果是rest请求，返回错误信息
                httpServletResponse.setStatus(200);
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.write(JSON.toJSONString(JsonResult.error("登录状态失效，请刷新页面或注销后重新登录！")));
                writer.flush();
                writer.close();
                log.info("No login，response error message...");
            }
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

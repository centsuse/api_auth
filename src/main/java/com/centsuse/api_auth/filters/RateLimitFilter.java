package com.centsuse.api_auth.filters;

import com.centsuse.api_auth.dtos.ApiResponse;
import com.centsuse.api_auth.utils.RateLimiter;
import com.centsuse.api_auth.utils.WebUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 限流过滤器
 */
@Component
public class RateLimitFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    @Autowired
    private RateLimiter rateLimiter;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String ip = WebUtil.getClientIp(request);
        String path = request.getRequestURI();

        // 对敏感接口进行限流
        if (path.startsWith("/auth/login") || path.startsWith("/auth/register")) {
            // 同一IP每分钟最多10次请求
            boolean allowed = rateLimiter.tryAcquireByIpAndPath(ip, path, 10, 60);
            if (!allowed) {
                log.warn("IP {} 请求 {} 过于频繁，已限流", ip, path);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(objectMapper.writeValueAsString(ApiResponse.error(429, "请求过于频繁，请稍后再试")));
                writer.flush();
                writer.close();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

package com.centsuse.api_auth.interceptor;

import com.centsuse.api_auth.utils.AppCodeHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AppCodeInterceptor implements HandlerInterceptor {

    private static final String APP_CODE_HEADER = "X-App-Code";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String appCode = request.getHeader(APP_CODE_HEADER);
        
        if (appCode != null && !appCode.trim().isEmpty()) {
            AppCodeHolder.setAppCode(appCode.trim());
        } else {
            AppCodeHolder.setAppCode("default");
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AppCodeHolder.clear();
    }
}

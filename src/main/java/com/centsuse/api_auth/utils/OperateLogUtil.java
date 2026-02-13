package com.centsuse.api_auth.utils;

import com.centsuse.api_auth.dao.SysOperateLog;
import com.centsuse.api_auth.entities.AuthUser;
import com.centsuse.api_auth.mapper.SysOperateLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 操作日志工具类
 */
@Component
public class OperateLogUtil {

    private static final Logger logger = LoggerFactory.getLogger(OperateLogUtil.class);

    @Autowired
    private SysOperateLogMapper operateLogMapper;

    /**
     * 记录操作日志
     * @param module 操作模块
     * @param type 操作类型：1-新增，2-修改，3-删除，4-查询
     * @param content 操作内容
     * @param status 操作状态：0-失败，1-成功
     * @param request 请求对象
     */
    public void log(String module, Integer type, String content, Integer status, HttpServletRequest request) {
        try {
            // 获取当前用户
            AuthUser user = WebUtil.getCurrentUser();
            if (user == null) {
                // 未登录用户，只记录IP和操作
                SysOperateLog log = new SysOperateLog();
                log.setModule(module);
                log.setType(type);
                log.setContent(content);
                log.setStatus(status);
                log.setIpAddress(WebUtil.getClientIp(request));
                log.setUserAgent(request.getHeader("User-Agent"));
                log.setOperateTime(new Date());
                operateLogMapper.insert(log);
                return;
            }

            // 登录用户，记录完整信息
            SysOperateLog log = new SysOperateLog();
            log.setAppCode(user.getAppCode());
            log.setModule(module);
            log.setType(type);
            log.setContent(content);
            log.setUserId(user.getId());
            log.setUserName(user.getUsername());
            log.setIpAddress(WebUtil.getClientIp(request));
            log.setUserAgent(request.getHeader("User-Agent"));
            log.setOperateTime(new Date());
            log.setStatus(status);

            operateLogMapper.insert(log);
        } catch (Exception e) {
            // 日志记录失败不影响主业务
            logger.error("记录操作日志失败: {}", e.getMessage());
        }
    }

    /**
     * 记录操作日志（带错误信息）
     * @param module 操作模块
     * @param type 操作类型：1-新增，2-修改，3-删除，4-查询
     * @param content 操作内容
     * @param status 操作状态：0-失败，1-成功
     * @param errorMessage 错误信息
     * @param request 请求对象
     */
    public void log(String module, Integer type, String content, Integer status, String errorMessage, HttpServletRequest request) {
        try {
            // 获取当前用户
            AuthUser user = WebUtil.getCurrentUser();
            if (user == null) {
                // 未登录用户，只记录IP和操作
                SysOperateLog log = new SysOperateLog();
                log.setModule(module);
                log.setType(type);
                log.setContent(content);
                log.setStatus(status);
                log.setErrorMessage(errorMessage);
                log.setIpAddress(WebUtil.getClientIp(request));
                log.setUserAgent(request.getHeader("User-Agent"));
                log.setOperateTime(new Date());
                operateLogMapper.insert(log);
                return;
            }

            // 登录用户，记录完整信息
            SysOperateLog log = new SysOperateLog();
            log.setAppCode(user.getAppCode());
            log.setModule(module);
            log.setType(type);
            log.setContent(content);
            log.setUserId(user.getId());
            log.setUserName(user.getUsername());
            log.setIpAddress(WebUtil.getClientIp(request));
            log.setUserAgent(request.getHeader("User-Agent"));
            log.setOperateTime(new Date());
            log.setStatus(status);
            log.setErrorMessage(errorMessage);

            operateLogMapper.insert(log);
        } catch (Exception e) {
            // 日志记录失败不影响主业务
            logger.error("记录操作日志失败: {}", e.getMessage());
        }
    }

    /**
     * 记录登录操作
     * @param content 操作内容
     * @param status 操作状态：0-失败，1-成功
     * @param request 请求对象
     */
    public void logLogin(String content, Integer status, HttpServletRequest request) {
        log("认证模块", 1, content, status, request);
    }

    /**
     * 记录登出操作
     * @param content 操作内容
     * @param status 操作状态：0-失败，1-成功
     * @param request 请求对象
     */
    public void logLogout(String content, Integer status, HttpServletRequest request) {
        log("认证模块", 4, content, status, request);
    }

    /**
     * 记录注册操作
     * @param content 操作内容
     * @param status 操作状态：0-失败，1-成功
     * @param request 请求对象
     */
    public void logRegister(String content, Integer status, HttpServletRequest request) {
        log("认证模块", 1, content, status, request);
    }

    /**
     * 记录令牌刷新操作
     * @param content 操作内容
     * @param status 操作状态：0-失败，1-成功
     * @param request 请求对象
     */
    public void logRefreshToken(String content, Integer status, HttpServletRequest request) {
        log("认证模块", 2, content, status, request);
    }
}

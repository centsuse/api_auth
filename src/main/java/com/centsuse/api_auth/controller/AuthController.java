package com.centsuse.api_auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.centsuse.api_auth.dao.SysOperateLog;
import com.centsuse.api_auth.dao.SysToken;
import com.centsuse.api_auth.dao.SysUser;
import com.centsuse.api_auth.dtos.*;
import com.centsuse.api_auth.entities.AuthUser;
import com.centsuse.api_auth.mapper.SysOperateLogMapper;
import com.centsuse.api_auth.mapper.SysTokenMapper;
import com.centsuse.api_auth.mapper.SysUserMapper;
import com.centsuse.api_auth.utils.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SysTokenMapper tokenMapper;

    @Autowired
    private SysOperateLogMapper operateLogMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request,
                                   HttpServletRequest httpRequest) {
        try {
            // 身份验证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            AuthUser userDetails = (AuthUser) authentication.getPrincipal();

            // 生成令牌
            String accessToken = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            // 保存令牌信息
            saveTokenInfo(userDetails, accessToken, refreshToken, httpRequest);

            // 记录操作日志
            logOperate(userDetails, "用户登录", 1, httpRequest);

            return ResponseEntity.ok(LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtTokenUtil.getExpiration())
                    .tokenType("Bearer")
                    .userInfo(buildUserInfo(userDetails))
                    .build());

        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("用户名或密码错误"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            if (jwtTokenUtil.validateToken(refreshToken)) {
                String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
                AuthUser userDetails = (AuthUser) userDetailsService.loadUserByUsername(username);

                String newAccessToken = jwtTokenUtil.generateToken(userDetails);

                return ResponseEntity.ok(RefreshTokenResponse.builder()
                        .accessToken(newAccessToken)
                        .expiresIn(jwtTokenUtil.getExpiration())
                        .build());
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("刷新令牌无效"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("令牌刷新失败"));
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            // 更新数据库令牌状态
            SysToken tokenEntity = tokenMapper.selectOne(
                    new QueryWrapper<SysToken>().eq("access_token", token)
            );
            if (tokenEntity != null) {
                tokenEntity.setStatus(0);
                tokenMapper.updateById(tokenEntity);
            }

            // 将令牌加入黑名单
            long expiration = jwtTokenUtil.getExpirationDateFromToken(token).getTime() - System.currentTimeMillis();
            if (expiration > 0) {
                redisTemplate.opsForValue().set("blacklist:" + token, "logout",
                        Duration.ofMillis(expiration));
            }

            // 记录操作日志
            AuthUser userDetails = getCurrentUser();
            logOperate(userDetails, "用户登出", 1, request);
        }

        return ResponseEntity.ok(ApiResponse.success("登出成功"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        // 检查用户名是否已存在
        if (userMapper.selectCount(new QueryWrapper<SysUser>()
                .eq("username", request.getUsername())) > 0) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("用户名已存在"));
        }

        // 创建用户
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAppCode(request.getAppCode());
        user.setStatus(1);
        user.setIsSuperAdmin(0);
        user.setCreateTime(new Date());

        userMapper.insert(user);

        return ResponseEntity.ok(ApiResponse.success("注册成功"));
    }

    private void saveTokenInfo(AuthUser user, String accessToken, String refreshToken,
                               HttpServletRequest request) {
        SysToken token = new SysToken();
        token.setUserId(user.getId());
        token.setAppCode(user.getAppCode());
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setTokenType("Bearer");
        token.setExpiresIn(jwtTokenUtil.getExpiration());
        token.setRefreshExpiresIn(jwtTokenUtil.getRefreshExpiration());
        token.setIssueTime(new Date());
        token.setStatus(1);
        token.setClientId(getClientId(request));

        tokenMapper.insert(token);
    }

    private void logOperate(AuthUser user, String content, Integer status, HttpServletRequest request) {
        SysOperateLog log = new SysOperateLog();
        log.setAppCode(user.getAppCode());
        log.setModule("认证模块");
        // 登录操作
        log.setType(1);
        log.setContent(content);
        log.setUserId(user.getId());
        log.setUserName(user.getUsername());
        log.setIpAddress(getClientIp(request));
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setOperateTime(new Date());
        log.setStatus(status);

        operateLogMapper.insert(log);
    }

    private UserInfo buildUserInfo(AuthUser user) {
        return UserInfo.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .isSuperAdmin(user.getIsSuperAdmin())
                .build();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getClientId(HttpServletRequest request) {
        return request.getHeader("X-Client-Id") != null ?
                request.getHeader("X-Client-Id") : "unknown";
    }

    private AuthUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AuthUser) authentication.getPrincipal();
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
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
import com.centsuse.api_auth.utils.OperateLogUtil;
import com.centsuse.api_auth.utils.PasswordValidator;
import com.centsuse.api_auth.utils.WebUtil;
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

    @Autowired
    private OperateLogUtil operateLogUtil;

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
            operateLogUtil.logLogin("用户登录成功", 1, httpRequest);

            return ResponseEntity.ok(LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtTokenUtil.getExpiration())
                    .tokenType("Bearer")
                    .userInfo(buildUserInfo(userDetails))
                    .build());

        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            operateLogUtil.logLogin("用户登录失败: " + e.getMessage(), 0, httpRequest);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("用户名或密码错误"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request, HttpServletRequest httpRequest) {
        try {
            String refreshToken = request.getRefreshToken();

            // 检查刷新令牌是否在黑名单中
            if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + refreshToken))) {
                operateLogUtil.logRefreshToken("令牌刷新失败：刷新令牌已失效", 0, httpRequest);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("刷新令牌已失效"));
            }

            if (jwtTokenUtil.validateToken(refreshToken)) {
                String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
                AuthUser userDetails = (AuthUser) userDetailsService.loadUserByUsername(username);

                String newAccessToken = jwtTokenUtil.generateToken(userDetails);

                operateLogUtil.logRefreshToken("令牌刷新成功", 1, httpRequest);
                return ResponseEntity.ok(RefreshTokenResponse.builder()
                        .accessToken(newAccessToken)
                        .expiresIn(jwtTokenUtil.getExpiration())
                        .build());
            }

            operateLogUtil.logRefreshToken("令牌刷新失败：刷新令牌无效", 0, httpRequest);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("刷新令牌无效"));
        } catch (Exception e) {
            operateLogUtil.logRefreshToken("令牌刷新失败：" + e.getMessage(), 0, httpRequest);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("令牌刷新失败"));
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = WebUtil.getTokenFromRequest(request);
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
            operateLogUtil.logLogout("用户登出成功", 1, request);
        }

        return ResponseEntity.ok(ApiResponse.success("登出成功"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request, HttpServletRequest httpRequest) {
        try {
            // 检查用户名是否已存在
            if (userMapper.selectCount(new QueryWrapper<SysUser>()
                    .eq("username", request.getUsername())) > 0) {
                operateLogUtil.logRegister("用户注册失败：用户名已存在", 0, httpRequest);
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("用户名已存在"));
            }

            // 验证密码强度
            if (!PasswordValidator.validatePassword(request.getPassword())) {
                operateLogUtil.logRegister("用户注册失败：" + PasswordValidator.getPasswordStrengthHint(), 0, httpRequest);
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(PasswordValidator.getPasswordStrengthHint()));
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

            operateLogUtil.logRegister("用户注册成功：" + request.getUsername(), 1, httpRequest);
            return ResponseEntity.ok(ApiResponse.success("注册成功"));
        } catch (Exception e) {
            operateLogUtil.logRegister("用户注册失败：" + e.getMessage(), 0, httpRequest);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("注册失败：" + e.getMessage()));
        }
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

    private String getClientId(HttpServletRequest request) {
        return request.getHeader("X-Client-Id") != null ?
                request.getHeader("X-Client-Id") : "unknown";
    }
}

package com.centsuse.api_auth;

import com.centsuse.api_auth.controller.AuthController;
import com.centsuse.api_auth.dtos.LoginRequest;
import com.centsuse.api_auth.dtos.LoginResponse;
import com.centsuse.api_auth.dtos.ApiResponse;
import com.centsuse.api_auth.entities.AuthUser;
import com.centsuse.api_auth.service.impl.UserDetailsServiceImpl;
import com.centsuse.api_auth.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("认证控制器单元测试")
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("TC-AUTH-001: 用户登录成功")
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123456");

        AuthUser authUser = new AuthUser();
        authUser.setId(1L);
        authUser.setUsername("admin");
        authUser.setAppCode("default");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(authUser);
        when(jwtTokenUtil.generateToken(any())).thenReturn("accessToken");
        when(jwtTokenUtil.generateRefreshToken(any())).thenReturn("refreshToken");

        ResponseEntity<ApiResponse<LoginResponse>> response = authController.login(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData());
        assertEquals("accessToken", response.getBody().getData().getAccessToken());
        assertEquals("refreshToken", response.getBody().getData().getRefreshToken());
    }

    @Test
    @DisplayName("TC-AUTH-002: 用户登录失败-密码错误")
    void testLoginFailWrongPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("用户名或密码错误"));

        assertThrows(BadCredentialsException.class, () -> {
            authController.login(request);
        });
    }

    @Test
    @DisplayName("TC-AUTH-003: 用户登录失败-用户不存在")
    void testLoginFailUserNotFound() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("123456");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("用户名或密码错误"));

        assertThrows(BadCredentialsException.class, () -> {
            authController.login(request);
        });
    }
}

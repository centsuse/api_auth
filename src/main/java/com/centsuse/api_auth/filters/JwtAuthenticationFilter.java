package com.centsuse.api_auth.filters;

import com.centsuse.api_auth.utils.JwtTokenUtil;
import com.centsuse.api_auth.utils.WebUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author bobo
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = WebUtil.getTokenFromRequest(request);

        if (token != null) {
            try {
                // 检查令牌是否在黑名单中
                if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + token))) {
                    logger.warn("JWT令牌在黑名单中: {}", token);
                    // 继续执行，后续会被Spring Security处理
                }

                if (jwtTokenUtil.validateToken(token)) {
                    String username = jwtTokenUtil.getUsernameFromToken(token);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warn("JWT令牌验证失败: {}", token);
                }
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                logger.error("JWT令牌已过期: {}", e.getMessage());
            } catch (io.jsonwebtoken.MalformedJwtException e) {
                logger.error("JWT令牌格式错误: {}", e.getMessage());
            } catch (io.jsonwebtoken.SignatureException e) {
                logger.error("JWT令牌签名错误: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("JWT令牌验证失败: ", e);
            }
        }

        chain.doFilter(request, response);
    }
}

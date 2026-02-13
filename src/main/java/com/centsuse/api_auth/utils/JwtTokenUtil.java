package com.centsuse.api_auth.utils;

import com.centsuse.api_auth.entities.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bobo
 */
@Component
@Data
public class JwtTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    private final String secret;
    private final Long expiration;
    private final Long refreshExpiration;

    public JwtTokenUtil(
            @Value("${jwt.secret:defaultSecretKey}") String secret,
            @Value("${jwt.expiration:7200}") Long expiration,
            @Value("${jwt.refresh-expiration:2592000}") Long refreshExpiration) {
        this.secret = secret;
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
    }

    public String generateToken(AuthUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userDetails.getId());
        claims.put("username", userDetails.getUsername());
        claims.put("appCode", userDetails.getAppCode());
        claims.put("isSuperAdmin", userDetails.getIsSuperAdmin());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateRefreshToken(AuthUser userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Long getUserIdFromToken(String token) {
        return ((Number) getClaimsFromToken(token).get("userId")).longValue();
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.error("JWT令牌已过期: {}", e.getMessage());
            return false;
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            logger.error("JWT令牌格式错误: {}", e.getMessage());
            return false;
        } catch (io.jsonwebtoken.SignatureException e) {
            logger.error("JWT令牌签名错误: {}", e.getMessage());
            return false;
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            logger.error("JWT令牌不支持: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.error("JWT令牌参数错误: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("JWT令牌验证失败: {}", e.getMessage());
            return false;
        }
    }
}
package com.centsuse.api_auth.service.impl;

import com.centsuse.api_auth.configs.constants.RedisKeyConstants;
import com.centsuse.api_auth.mapper.SysPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bobo
 */
@Component
public class PermissionCacheService {
    //TODO 缓存类，后续处理
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    
    public List<GrantedAuthority> getAuthorities(Long userId) {
        String cacheKey = RedisKeyConstants.getUserPermissionsKey(userId);
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) redisTemplate.opsForValue().get(cacheKey);
        
        if (authorities == null) {
            authorities = sysPermissionMapper.selectPermissionsByUserId(userId).stream()
                .map(perm -> new SimpleGrantedAuthority(perm.getCode()))
                .collect(Collectors.toList());
                
            redisTemplate.opsForValue().set(cacheKey, authorities, Duration.ofHours(1));
        }
        
        return authorities;
    }
}
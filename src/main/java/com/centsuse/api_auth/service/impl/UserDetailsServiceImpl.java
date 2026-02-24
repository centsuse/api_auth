package com.centsuse.api_auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.centsuse.api_auth.dao.SysPermission;
import com.centsuse.api_auth.dao.SysRole;
import com.centsuse.api_auth.dao.SysUser;
import com.centsuse.api_auth.dao.SysUserApp;
import com.centsuse.api_auth.entities.AuthUser;
import com.centsuse.api_auth.mapper.SysPermissionMapper;
import com.centsuse.api_auth.mapper.SysRoleMapper;
import com.centsuse.api_auth.mapper.SysUserAppMapper;
import com.centsuse.api_auth.mapper.SysUserMapper;
import com.centsuse.api_auth.utils.AppCodeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysUserAppMapper userAppMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = userMapper.selectOne(
                new QueryWrapper<SysUser>().eq("username", username)
        );

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        if (user.getStatus() != 1) {
            throw new DisabledException("用户已被禁用");
        }

        String appCode = AppCodeHolder.getAppCodeOrDefault();
        
        SysUserApp userApp = userAppMapper.selectByUserIdAndAppCode(user.getId(), appCode);
        if (userApp == null) {
            throw new DisabledException("用户无权访问该应用: " + appCode);
        }
        
        if (userApp.getStatus() != 1) {
            throw new DisabledException("用户在该应用中已被禁用");
        }

        List<GrantedAuthority> authorities = getAuthorities(user, appCode);

        return new AuthUser(user, authorities);
    }

    private List<GrantedAuthority> getAuthorities(SysUser user, String appCode) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (user.getIsSuperAdmin() == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
            List<SysPermission> allPermissions = permissionMapper.selectList(
                    new QueryWrapper<SysPermission>().eq("app_code", appCode).eq("status", 1)
            );
            for (SysPermission permission : allPermissions) {
                authorities.add(new SimpleGrantedAuthority(permission.getCode()));
            }
            return authorities;
        }

        List<SysRole> roles = roleMapper.selectRolesByUserId(user.getId());
        for (SysRole role : roles) {
            if (role.getAppCode().equals(appCode)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
            }
        }

        List<SysPermission> permissions = permissionMapper.selectPermissionsByUserIdAndAppCode(user.getId(), appCode);
        for (SysPermission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getCode()));
        }

        return authorities;
    }
}

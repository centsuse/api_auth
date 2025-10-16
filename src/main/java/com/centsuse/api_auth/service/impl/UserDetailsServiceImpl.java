package com.centsuse.api_auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.centsuse.api_auth.dao.SysPermission;
import com.centsuse.api_auth.dao.SysRole;
import com.centsuse.api_auth.dao.SysUser;
import com.centsuse.api_auth.entities.AuthUser;
import com.centsuse.api_auth.mapper.SysPermissionMapper;
import com.centsuse.api_auth.mapper.SysRoleMapper;
import com.centsuse.api_auth.mapper.SysUserMapper;
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

/**
 * @author bobo
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户
        SysUser user = userMapper.selectOne(
                new QueryWrapper<SysUser>().eq("username", username)
        );

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        if (user.getStatus() != 1) {
            throw new DisabledException("用户已被禁用");
        }

        // 查询用户权限
        List<GrantedAuthority> authorities = getAuthorities(user.getId());

        return new AuthUser(user, authorities);
    }

    private List<GrantedAuthority> getAuthorities(Long userId) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 如果是超级管理员，拥有所有权限
        SysUser user = userMapper.selectById(userId);
        if (user.getIsSuperAdmin() == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
            return authorities;
        }

        // 获取用户角色
        List<SysRole> roles = roleMapper.selectRolesByUserId(userId);
        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
        }

        // 获取用户权限
        List<SysPermission> permissions = permissionMapper.selectPermissionsByUserId(userId);
        for (SysPermission permission : permissions) {
            authorities.add(new SimpleGrantedAuthority(permission.getCode()));
        }

        return authorities;
    }
}
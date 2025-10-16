package com.centsuse.api_auth.entities;

import com.centsuse.api_auth.dao.SysUser;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author bobo
 */
public class AuthUser extends SysUser implements UserDetails {
    private final List<GrantedAuthority> authorities;

    public AuthUser(SysUser user, List<GrantedAuthority> authorities) {
        super();
        BeanUtils.copyProperties(user, this);
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.getStatus() == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.getStatus() == 1;
    }
}

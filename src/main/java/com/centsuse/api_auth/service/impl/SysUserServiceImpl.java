package com.centsuse.api_auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.centsuse.api_auth.dao.SysUser;
import com.centsuse.api_auth.dao.SysUserRole;
import com.centsuse.api_auth.dtos.sysuser.UserCreateUpdateDTO;
import com.centsuse.api_auth.dtos.sysuser.UserQueryDTO;
import com.centsuse.api_auth.mapper.SysUserRoleMapper;
import com.centsuse.api_auth.service.SysUserService;
import com.centsuse.api_auth.mapper.SysUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
* @author bobo
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2025-10-15 15:14:42
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public Page<SysUser> getUserPage(UserQueryDTO queryDTO) {
        Page<SysUser> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(queryDTO.getUsername())) {
            wrapper.like("username", queryDTO.getUsername());
        }

        if (StringUtils.hasText(queryDTO.getNickname())) {
            wrapper.like("nickname", queryDTO.getNickname());
        }

        if (StringUtils.hasText(queryDTO.getEmail())) {
            wrapper.like("email", queryDTO.getEmail());
        }

        if (StringUtils.hasText(queryDTO.getPhone())) {
            wrapper.like("phone", queryDTO.getPhone());
        }

        if (queryDTO.getStatus() != null) {
            wrapper.eq("status", queryDTO.getStatus());
        }

        if (StringUtils.hasText(queryDTO.getAppCode())) {
            wrapper.eq("app_code", queryDTO.getAppCode());
        }

        if (queryDTO.getDeptId() != null) {
            wrapper.eq("dept_id", queryDTO.getDeptId());
        }

        wrapper.orderByDesc("create_time");

        return this.page(page, wrapper);
    }

    @Override
    @Transactional
    public Long createUser(UserCreateUpdateDTO createDTO) {
        // 检查用户名是否已存在
        if (this.getUserByUsername(createDTO.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(createDTO, user);

        // 加密密码
        user.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        // 默认非超级管理员
        user.setIsSuperAdmin(0);

        this.save(user);

        // 保存用户角色关系
        if (createDTO.getRoleIds() != null && createDTO.getRoleIds().length > 0) {
            saveUserRoles(user.getId(), createDTO.getRoleIds());
        }

        return user.getId();
    }

    @Override
    @Transactional
    public void updateUser(Long userId, UserCreateUpdateDTO updateDTO) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户名是否被其他用户使用
        SysUser existingUser = this.getUserByUsername(updateDTO.getUsername());
        if (existingUser != null && !existingUser.getId().equals(userId)) {
            throw new RuntimeException("用户名已被其他用户使用");
        }

        BeanUtils.copyProperties(updateDTO, user);
        user.setUpdateTime(new Date());

        // 如果提供了新密码，则更新密码
        if (StringUtils.hasText(updateDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        this.updateById(user);

        // 更新用户角色关系
        if (updateDTO.getRoleIds() != null) {
            // 删除旧的角色关系
            QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            userRoleMapper.delete(wrapper);

            // 保存新的角色关系
            saveUserRoles(userId, updateDTO.getRoleIds());
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 逻辑删除：将状态设置为0（禁用）
        user.setStatus(0);
        user.setUpdateTime(new Date());
        this.updateById(user);

        // 也可以选择物理删除用户角色关系
        // QueryWrapper<SysUserRole> wrapper = new QueryWrapper<>();
        // wrapper.eq("user_id", userId);
        // userRoleMapper.delete(wrapper);
    }

    @Override
    public void toggleUserStatus(Long userId, Integer status) {
        if (status != 0 && status != 1) {
            throw new RuntimeException("状态值不合法");
        }

        SysUser user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setStatus(status);
        user.setUpdateTime(new Date());
        this.updateById(user);
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(new Date());
        this.updateById(user);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        return this.getOne(wrapper);
    }

    /**
     * 保存用户角色关系
     */
    private void saveUserRoles(Long userId, Long[] roleIds) {
        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setCreateTime(new Date());
            userRoleMapper.insert(userRole);
        }
    }
}
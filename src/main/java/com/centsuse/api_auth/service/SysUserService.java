package com.centsuse.api_auth.service;

import com.centsuse.api_auth.dao.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.centsuse.api_auth.dtos.sysuser.UserCreateUpdateDTO;
import com.centsuse.api_auth.dtos.sysuser.UserQueryDTO;

/**
* @author bobo
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2025-10-15 15:14:42
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     */
    Page<SysUser> getUserPage(UserQueryDTO queryDTO);

    /**
     * 创建用户
     */
    Long createUser(UserCreateUpdateDTO createDTO);

    /**
     * 更新用户
     */
    void updateUser(Long userId, UserCreateUpdateDTO updateDTO);

    /**
     * 删除用户（逻辑删除）
     */
    void deleteUser(Long userId);

    /**
     * 启用/禁用用户
     */
    void toggleUserStatus(Long userId, Integer status);

    /**
     * 重置用户密码
     */
    void resetPassword(Long userId, String newPassword);

    /**
     * 根据用户名查询用户
     */
    SysUser getUserByUsername(String username);
}

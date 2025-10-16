package com.centsuse.api_auth.utils;

import com.centsuse.api_auth.dao.SysDataPermissionRule;
import com.centsuse.api_auth.dao.SysRole;
import com.centsuse.api_auth.entities.AuthUser;
import com.centsuse.api_auth.mapper.SysDataPermissionRuleMapper;
import com.centsuse.api_auth.mapper.SysPermissionMapper;
import com.centsuse.api_auth.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author bobo
 */
@Component("permissionCheckUtil")
public class PermissionCheckUtil {

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysDataPermissionRuleMapper dataPermissionRuleMapper;

    /**
     * 检查接口权限
     */
    public boolean checkApiPermission(String permissionCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        AuthUser user = (AuthUser) authentication.getPrincipal();

        // 超级管理员拥有所有权限
        if (user.getIsSuperAdmin() == 1) {
            return true;
        }

        // 检查用户是否有指定权限
        return permissionMapper.userHasPermission(user.getId(), permissionCode) > 0;
    }

    /**
     * 检查数据权限
     */
    public boolean checkDataPermission(Long dataId, String dataType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        AuthUser user = (AuthUser) authentication.getPrincipal();

        // 超级管理员拥有所有数据权限
        if (user.getIsSuperAdmin() == 1) {
            return true;
        }

        // 根据业务规则检查数据权限
        // 这里可以根据您的数据权限规则表(sys_data_permission_rule)实现具体逻辑
        return checkDataPermissionByRules(user.getId(), dataId, dataType);
    }

    private boolean checkDataPermissionByRules(Long userId, Long dataId, String dataType) {
        // TODO
        // 实现具体的数据权限检查逻辑
        // 可以从sys_data_permission_rule表查询用户的权限规则
        // 然后根据规则验证数据权限
        // 1. 获取用户角色
//        List<SysRole> userRoles = roleMapper.selectRolesByUserId(userId);
//
//        // 2. 查询数据权限规则
//        List<SysDataPermissionRule> rules = dataPermissionRuleMapper.selectByUserAndDataType(
//                userId, dataType);
//
//        // 3. 根据规则类型进行验证
//        for (SysDataPermissionRule rule : rules) {
//            switch (rule.getType()) {
//                // 部门权限
//                case 1:
//                    if ((checkDeptPermission(userId, dataId, rule))) {
//                        return true;
//                    }
//                    break;
//                // 自定义SQL
//                case 2:
//                    if ((checkCustomSqlPermission(userId, dataId, rule))) {
//                        return true;
//                    }
//                    break;
//                // 字段过滤
//                case 3:
//                    if ((checkFieldFilterPermission(userId, dataId, rule))) {
//                        return true;
//                    }
//                    break;
//            }
//        }
        return true;
    }
}
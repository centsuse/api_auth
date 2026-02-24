package com.centsuse.api_auth.utils;

import com.centsuse.api_auth.dao.SysDataPermissionRule;
import com.centsuse.api_auth.dao.SysRole;
import com.centsuse.api_auth.dao.SysUser;
import com.centsuse.api_auth.entities.AuthUser;
import com.centsuse.api_auth.mapper.SysDataPermissionRuleMapper;
import com.centsuse.api_auth.mapper.SysPermissionMapper;
import com.centsuse.api_auth.mapper.SysRoleMapper;
import com.centsuse.api_auth.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("permissionCheckUtil")
public class PermissionCheckUtil {

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysDataPermissionRuleMapper dataPermissionRuleMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    public boolean checkApiPermission(String permissionCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        AuthUser user = (AuthUser) authentication.getPrincipal();

        if (user.getIsSuperAdmin() == 1) {
            return true;
        }

        return permissionMapper.userHasPermission(user.getId(), permissionCode) > 0;
    }

    public boolean checkDataPermission(Long dataId, String dataType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        AuthUser user = (AuthUser) authentication.getPrincipal();

        if (user.getIsSuperAdmin() == 1) {
            return true;
        }

        return checkDataPermissionByRules(user.getId(), dataId, dataType);
    }

    private boolean checkDataPermissionByRules(Long userId, Long dataId, String dataType) {
        List<SysRole> userRoles = roleMapper.selectRolesByUserId(userId);

        for (SysRole role : userRoles) {
            if (role.getDataScope() == null) {
                continue;
            }

            switch (role.getDataScope()) {
                case 1:
                    return true;
                case 4:
                    return checkSelfPermission(userId, dataId, dataType);
                case 5:
                    return checkCustomPermission(userId, dataId, dataType);
                default:
                    return checkDeptPermission(userId, dataId, dataType, role.getDataScope());
            }
        }

        return false;
    }

    private boolean checkSelfPermission(Long userId, Long dataId, String dataType) {
        if ("user".equals(dataType)) {
            return userId.equals(dataId);
        }
        return false;
    }

    private boolean checkCustomPermission(Long userId, Long dataId, String dataType) {
        List<SysDataPermissionRule> rules = dataPermissionRuleMapper.selectByUserIdAndDataType(userId, dataType);

        for (SysDataPermissionRule rule : rules) {
            if (rule.getType() == 2 && rule.getCustomSql() != null) {
                return true;
            }
            if (rule.getType() == 3 && rule.getFieldFilters() != null) {
                return true;
            }
        }

        return false;
    }

    private boolean checkDeptPermission(Long userId, Long dataId, String dataType, Integer dataScope) {
        SysUser user = userMapper.selectById(userId);
        if (user == null || user.getDeptId() == null) {
            return false;
        }

        if ("user".equals(dataType)) {
            SysUser targetUser = userMapper.selectById(dataId);
            if (targetUser == null || targetUser.getDeptId() == null) {
                return false;
            }

            if (dataScope == 2) {
                return user.getDeptId().equals(targetUser.getDeptId());
            }

            if (dataScope == 3) {
                return isChildDept(user.getDeptId(), targetUser.getDeptId());
            }
        }

        return false;
    }

    private boolean isChildDept(Long parentDeptId, Long childDeptId) {
        return parentDeptId.equals(childDeptId);
    }
}
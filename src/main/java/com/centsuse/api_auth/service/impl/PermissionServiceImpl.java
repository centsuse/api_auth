package com.centsuse.api_auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.centsuse.api_auth.dao.SysPermission;
import com.centsuse.api_auth.dtos.permission.PermissionCreateDTO;
import com.centsuse.api_auth.dtos.permission.PermissionUpdateDTO;
import com.centsuse.api_auth.mapper.SysPermissionMapper;
import com.centsuse.api_auth.service.PermissionService;
import com.centsuse.api_auth.utils.AppCodeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Override
    public List<SysPermission> getUserPermissions(Long userId) {
        String appCode = AppCodeHolder.getAppCodeOrDefault();
        return getUserPermissions(userId, appCode);
    }

    @Override
    public List<SysPermission> getUserPermissions(Long userId, String appCode) {
        return permissionMapper.selectPermissionsByUserIdAndAppCode(userId, appCode);
    }

    @Override
    public List<SysPermission> getPermissionTree(Long userId) {
        String appCode = AppCodeHolder.getAppCodeOrDefault();
        return getPermissionTree(userId, appCode);
    }

    @Override
    public List<SysPermission> getPermissionTree(Long userId, String appCode) {
        List<SysPermission> permissions = getUserPermissions(userId, appCode);
        return buildPermissionTree(permissions, 0L);
    }

    private List<SysPermission> buildPermissionTree(List<SysPermission> permissions, Long parentId) {
        return permissions.stream()
                .filter(p -> p.getParentId() != null && p.getParentId().equals(parentId))
                .peek(p -> p.setChildren(buildPermissionTree(permissions, p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public SysPermission getPermissionById(Long permissionId) {
        return permissionMapper.selectById(permissionId);
    }

    @Override
    @Transactional
    public Long createPermission(PermissionCreateDTO createDTO) {
        SysPermission permission = new SysPermission();
        permission.setAppCode(createDTO.getAppCode());
        permission.setParentId(createDTO.getParentId() != null ? createDTO.getParentId() : 0L);
        permission.setName(createDTO.getName());
        permission.setCode(createDTO.getCode());
        permission.setType(createDTO.getType());
        permission.setPath(createDTO.getPath());
        permission.setComponent(createDTO.getComponent());
        permission.setMethod(createDTO.getMethod());
        permission.setIcon(createDTO.getIcon());
        permission.setSort(createDTO.getSort() != null ? createDTO.getSort() : 0);
        permission.setStatus(1);
        permission.setCreateTime(new java.util.Date());
        permissionMapper.insert(permission);
        return permission.getId();
    }

    @Override
    @Transactional
    public void updatePermission(Long permissionId, PermissionUpdateDTO updateDTO) {
        SysPermission permission = permissionMapper.selectById(permissionId);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }
        
        if (updateDTO.getName() != null) {
            permission.setName(updateDTO.getName());
        }
        if (updateDTO.getPath() != null) {
            permission.setPath(updateDTO.getPath());
        }
        if (updateDTO.getComponent() != null) {
            permission.setComponent(updateDTO.getComponent());
        }
        if (updateDTO.getMethod() != null) {
            permission.setMethod(updateDTO.getMethod());
        }
        if (updateDTO.getIcon() != null) {
            permission.setIcon(updateDTO.getIcon());
        }
        if (updateDTO.getSort() != null) {
            permission.setSort(updateDTO.getSort());
        }
        if (updateDTO.getStatus() != null) {
            permission.setStatus(updateDTO.getStatus());
        }
        
        permission.setUpdateTime(new java.util.Date());
        permissionMapper.updateById(permission);
    }

    @Override
    @Transactional
    public void deletePermission(Long permissionId) {
        permissionMapper.deleteById(permissionId);
    }
}

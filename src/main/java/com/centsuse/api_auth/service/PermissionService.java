package com.centsuse.api_auth.service;

import com.centsuse.api_auth.dao.SysPermission;
import com.centsuse.api_auth.dtos.permission.PermissionCreateDTO;
import com.centsuse.api_auth.dtos.permission.PermissionUpdateDTO;

import java.util.List;

public interface PermissionService {

    List<SysPermission> getUserPermissions(Long userId);

    List<SysPermission> getUserPermissions(Long userId, String appCode);

    List<SysPermission> getPermissionTree(Long userId);

    List<SysPermission> getPermissionTree(Long userId, String appCode);

    SysPermission getPermissionById(Long permissionId);

    Long createPermission(PermissionCreateDTO createDTO);

    void updatePermission(Long permissionId, PermissionUpdateDTO updateDTO);

    void deletePermission(Long permissionId);
}

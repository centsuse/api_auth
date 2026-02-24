package com.centsuse.api_auth.controller;

import com.centsuse.api_auth.dao.SysPermission;
import com.centsuse.api_auth.dtos.ApiResponse;
import com.centsuse.api_auth.dtos.permission.PermissionCreateDTO;
import com.centsuse.api_auth.dtos.permission.PermissionQueryDTO;
import com.centsuse.api_auth.dtos.permission.PermissionUpdateDTO;
import com.centsuse.api_auth.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('permission:list')")
    public ResponseEntity<ApiResponse<List<SysPermission>>> getUserPermissions(@PathVariable Long userId) {
        List<SysPermission> permissions = permissionService.getUserPermissions(userId);
        return ResponseEntity.ok(ApiResponse.success(permissions));
    }

    @GetMapping("/tree/{userId}")
    @PreAuthorize("hasAuthority('permission:list')")
    public ResponseEntity<ApiResponse<List<SysPermission>>> getPermissionTree(@PathVariable Long userId) {
        List<SysPermission> tree = permissionService.getPermissionTree(userId);
        return ResponseEntity.ok(ApiResponse.success(tree));
    }

    @GetMapping("/{permissionId}")
    @PreAuthorize("hasAuthority('permission:detail')")
    public ResponseEntity<ApiResponse<SysPermission>> getPermissionDetail(@PathVariable Long permissionId) {
        SysPermission permission = permissionService.getPermissionById(permissionId);
        if (permission == null) {
            return ResponseEntity.badRequest().body((ApiResponse<SysPermission>) ApiResponse.error("权限不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(permission));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('permission:create')")
    public ResponseEntity<ApiResponse<?>> createPermission(@RequestBody @Valid PermissionCreateDTO createDTO) {
        try {
            Long permissionId = permissionService.createPermission(createDTO);
            return ResponseEntity.ok(ApiResponse.success(permissionId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{permissionId}")
    @PreAuthorize("hasAuthority('permission:update')")
    public ResponseEntity<ApiResponse<?>> updatePermission(@PathVariable Long permissionId,
                                                        @RequestBody @Valid PermissionUpdateDTO updateDTO) {
        try {
            permissionService.updatePermission(permissionId, updateDTO);
            return ResponseEntity.ok(ApiResponse.success("更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{permissionId}")
    @PreAuthorize("hasAuthority('permission:delete')")
    public ResponseEntity<ApiResponse<?>> deletePermission(@PathVariable Long permissionId) {
        try {
            permissionService.deletePermission(permissionId);
            return ResponseEntity.ok(ApiResponse.success("删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

package com.centsuse.api_auth.controller;

import com.centsuse.api_auth.dao.SysRole;
import com.centsuse.api_auth.dtos.ApiResponse;
import com.centsuse.api_auth.dtos.role.RoleCreateDTO;
import com.centsuse.api_auth.dtos.role.RoleQueryDTO;
import com.centsuse.api_auth.dtos.role.RoleUpdateDTO;
import com.centsuse.api_auth.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('role:list')")
    public ResponseEntity<ApiResponse<List<SysRole>>> getRoleList(@ModelAttribute RoleQueryDTO queryDTO) {
        List<SysRole> roles = roleService.getRoleList(queryDTO);
        return ResponseEntity.ok(ApiResponse.success(roles));
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('role:detail')")
    public ResponseEntity<ApiResponse<SysRole>> getRoleDetail(@PathVariable Long roleId) {
        SysRole role = roleService.getRoleById(roleId);
        if (role == null) {
            return ResponseEntity.badRequest().body((ApiResponse<SysRole>) ApiResponse.error("角色不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(role));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:create')")
    public ResponseEntity<ApiResponse<?>> createRole(@RequestBody @Valid RoleCreateDTO createDTO) {
        try {
            Long roleId = roleService.createRole(createDTO);
            return ResponseEntity.ok(ApiResponse.success(roleId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<ApiResponse<?>> updateRole(@PathVariable Long roleId,
                                                   @RequestBody @Valid RoleUpdateDTO updateDTO) {
        try {
            roleService.updateRole(roleId, updateDTO);
            return ResponseEntity.ok(ApiResponse.success("更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('role:delete')")
    public ResponseEntity<ApiResponse<?>> deleteRole(@PathVariable Long roleId) {
        try {
            roleService.deleteRole(roleId);
            return ResponseEntity.ok(ApiResponse.success("删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

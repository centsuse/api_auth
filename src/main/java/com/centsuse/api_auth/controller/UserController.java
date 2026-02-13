package com.centsuse.api_auth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.centsuse.api_auth.dao.SysUser;
import com.centsuse.api_auth.dtos.ApiResponse;
import com.centsuse.api_auth.dtos.sysuser.UserCreateUpdateDTO;
import com.centsuse.api_auth.dtos.sysuser.UserQueryDTO;
import com.centsuse.api_auth.service.SysUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 * @author bobo
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserService userService;

    /**
     * 用户列表查询
     */
    @PostMapping("/list")
    @PreAuthorize("hasAuthority('user:list')")
    public ResponseEntity<ApiResponse<Page<SysUser>>> getUserList(@RequestBody UserQueryDTO queryDTO) {
        Page<SysUser> userPage = userService.getUserPage(queryDTO);
        return ResponseEntity.ok(ApiResponse.success(userPage));
    }

    /**
     * 用户详情查询
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('user:detail')")
    public ResponseEntity<ApiResponse> getUserDetail(@PathVariable Long userId) {
        SysUser user = userService.getById(userId);
        if (user == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("用户不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /**
     * 创建用户
     */
    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<ApiResponse> createUser(@RequestBody @Valid UserCreateUpdateDTO createDTO) {
        try {
            Long userId = userService.createUser(createDTO);
            return ResponseEntity.ok(ApiResponse.success(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable Long userId,
                                                     @RequestBody @Valid UserCreateUpdateDTO updateDTO) {
        try {
            userService.updateUser(userId, updateDTO);
            return ResponseEntity.ok(ApiResponse.success("更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * 删除用户（逻辑删除）
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(ApiResponse.success("删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * 启用/禁用用户
     */
    @PutMapping("/{userId}/status")
    @PreAuthorize("hasAuthority('user:status')")
    public ResponseEntity<ApiResponse<?>> toggleUserStatus(@PathVariable Long userId,
                                                           @RequestParam Integer status) {
        try {
            userService.toggleUserStatus(userId, status);
            String message = status == 1 ? "启用成功" : "禁用成功";
            return ResponseEntity.ok(ApiResponse.success(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{userId}/password")
    @PreAuthorize("hasAuthority('user:password')")
    public ResponseEntity<ApiResponse<?>> resetPassword(@PathVariable Long userId,
                                                        @RequestParam String newPassword) {
        try {
            userService.resetPassword(userId, newPassword);
            return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
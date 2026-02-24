package com.centsuse.api_auth.controller;

import com.centsuse.api_auth.dao.SysPermission;
import com.centsuse.api_auth.dtos.ApiResponse;
import com.centsuse.api_auth.dtos.menu.MenuCreateDTO;
import com.centsuse.api_auth.dtos.menu.MenuUpdateDTO;
import com.centsuse.api_auth.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/tree/{userId}")
    @PreAuthorize("hasAuthority('menu:list')")
    public ResponseEntity<ApiResponse<List<SysPermission>>> getMenuTree(@PathVariable Long userId) {
        List<SysPermission> tree = menuService.getMenuTree(userId);
        return ResponseEntity.ok(ApiResponse.success(tree));
    }

    @GetMapping("/detail/{menuId}")
    @PreAuthorize("hasAuthority('menu:detail')")
    public ResponseEntity<ApiResponse<SysPermission>> getMenuDetail(@PathVariable Long menuId) {
        SysPermission menu = menuService.getMenuById(menuId);
        if (menu == null) {
            return ResponseEntity.badRequest().body((ApiResponse<SysPermission>) ApiResponse.error("菜单不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(menu));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('menu:create')")
    public ResponseEntity<ApiResponse<?>> createMenu(@RequestBody @Valid MenuCreateDTO createDTO) {
        try {
            Long menuId = menuService.createMenu(createDTO);
            return ResponseEntity.ok(ApiResponse.success(menuId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{menuId}")
    @PreAuthorize("hasAuthority('menu:update')")
    public ResponseEntity<ApiResponse<?>> updateMenu(@PathVariable Long menuId,
                                                   @RequestBody @Valid MenuUpdateDTO updateDTO) {
        try {
            menuService.updateMenu(menuId, updateDTO);
            return ResponseEntity.ok(ApiResponse.success("更新成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasAuthority('menu:delete')")
    public ResponseEntity<ApiResponse<?>> deleteMenu(@PathVariable Long menuId) {
        try {
            menuService.deleteMenu(menuId);
            return ResponseEntity.ok(ApiResponse.success("删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

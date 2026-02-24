package com.centsuse.api_auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.centsuse.api_auth.dao.SysPermission;
import com.centsuse.api_auth.dtos.menu.MenuCreateDTO;
import com.centsuse.api_auth.dtos.menu.MenuUpdateDTO;
import com.centsuse.api_auth.mapper.SysPermissionMapper;
import com.centsuse.api_auth.service.MenuService;
import com.centsuse.api_auth.utils.AppCodeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private SysPermissionMapper permissionMapper;

    @Override
    public List<SysPermission> getMenuTree(Long userId) {
        String appCode = AppCodeHolder.getAppCodeOrDefault();
        return getMenuTree(userId, appCode);
    }

    @Override
    public List<SysPermission> getMenuTree(Long userId, String appCode) {
        List<SysPermission> allMenus = permissionMapper.selectPermissionsByUserIdAndAppCode(userId, appCode);
        
        List<SysPermission> menus = allMenus.stream()
                .filter(p -> p.getType() != null && p.getType() == 1)
                .collect(Collectors.toList());
        
        return buildMenuTree(menus, 0L);
    }

    @Override
    public SysPermission getMenuById(Long menuId) {
        return permissionMapper.selectById(menuId);
    }

    @Override
    @Transactional
    public Long createMenu(MenuCreateDTO createDTO) {
        SysPermission menu = new SysPermission();
        menu.setAppCode(createDTO.getAppCode());
        menu.setParentId(createDTO.getParentId() != null ? createDTO.getParentId() : 0L);
        menu.setName(createDTO.getName());
        menu.setCode(createDTO.getCode());
        menu.setType(1);
        menu.setPath(createDTO.getPath());
        menu.setComponent(createDTO.getComponent());
        menu.setIcon(createDTO.getIcon());
        menu.setSort(createDTO.getSort() != null ? createDTO.getSort() : 0);
        menu.setStatus(1);
        menu.setCreateTime(new java.util.Date());
        permissionMapper.insert(menu);
        return menu.getId();
    }

    @Override
    @Transactional
    public void updateMenu(Long menuId, MenuUpdateDTO updateDTO) {
        SysPermission menu = permissionMapper.selectById(menuId);
        if (menu == null) {
            throw new RuntimeException("菜单不存在");
        }
        
        if (updateDTO.getName() != null) {
            menu.setName(updateDTO.getName());
        }
        if (updateDTO.getPath() != null) {
            menu.setPath(updateDTO.getPath());
        }
        if (updateDTO.getComponent() != null) {
            menu.setComponent(updateDTO.getComponent());
        }
        if (updateDTO.getIcon() != null) {
            menu.setIcon(updateDTO.getIcon());
        }
        if (updateDTO.getSort() != null) {
            menu.setSort(updateDTO.getSort());
        }
        if (updateDTO.getStatus() != null) {
            menu.setStatus(updateDTO.getStatus());
        }
        
        menu.setUpdateTime(new java.util.Date());
        permissionMapper.updateById(menu);
    }

    @Override
    @Transactional
    public void deleteMenu(Long menuId) {
        permissionMapper.deleteById(menuId);
    }

    private List<SysPermission> buildMenuTree(List<SysPermission> menus, Long parentId) {
        return menus.stream()
                .filter(m -> m.getParentId() != null && m.getParentId().equals(parentId))
                .peek(m -> m.setChildren(buildMenuTree(menus, m.getId())))
                .collect(Collectors.toList());
    }
}

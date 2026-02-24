package com.centsuse.api_auth.service;

import com.centsuse.api_auth.dao.SysPermission;
import com.centsuse.api_auth.dtos.menu.MenuCreateDTO;
import com.centsuse.api_auth.dtos.menu.MenuUpdateDTO;

import java.util.List;

public interface MenuService {

    List<SysPermission> getMenuTree(Long userId);

    List<SysPermission> getMenuTree(Long userId, String appCode);

    SysPermission getMenuById(Long menuId);

    Long createMenu(MenuCreateDTO createDTO);

    void updateMenu(Long menuId, MenuUpdateDTO updateDTO);

    void deleteMenu(Long menuId);
}

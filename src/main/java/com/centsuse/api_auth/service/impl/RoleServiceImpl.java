package com.centsuse.api_auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.centsuse.api_auth.dao.SysRole;
import com.centsuse.api_auth.dtos.role.RoleCreateDTO;
import com.centsuse.api_auth.dtos.role.RoleQueryDTO;
import com.centsuse.api_auth.dtos.role.RoleUpdateDTO;
import com.centsuse.api_auth.mapper.SysRoleMapper;
import com.centsuse.api_auth.mapper.SysRolePermissionMapper;
import com.centsuse.api_auth.mapper.SysUserRoleMapper;
import com.centsuse.api_auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRolePermissionMapper rolePermissionMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public List<SysRole> getRoleList(RoleQueryDTO queryDTO) {
        QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
        if (queryDTO.getAppCode() != null) {
            wrapper.eq("app_code", queryDTO.getAppCode());
        }
        if (queryDTO.getName() != null) {
            wrapper.like("name", queryDTO.getName());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq("status", queryDTO.getStatus());
        }
        wrapper.orderByAsc("sort");
        return roleMapper.selectList(wrapper);
    }


    @Override
    public SysRole getRoleById(Long roleId) {
        return roleMapper.selectById(roleId);
    }


    @Override
    @Transactional
    public Long createRole(RoleCreateDTO createDTO) {
        SysRole role = new SysRole();
        role.setAppCode(createDTO.getAppCode());
        role.setName(createDTO.getName());
        role.setCode(createDTO.getCode());
        role.setDataScope(createDTO.getDataScope());
        role.setSort(createDTO.getSort());
        role.setStatus(1);
        role.setCreateTime(new java.util.Date());
        roleMapper.insert(role);
        return role.getId();
    }

    @Override
    @Transactional
    public void updateRole(Long roleId, RoleUpdateDTO updateDTO) {
        SysRole role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        
        if (updateDTO.getName() != null) {
            role.setName(updateDTO.getName());
        }
        if (updateDTO.getCode() != null) {
            role.setCode(updateDTO.getCode());
        }
        if (updateDTO.getDataScope() != null) {
            role.setDataScope(updateDTO.getDataScope());
        }
        if (updateDTO.getSort() != null) {
            role.setSort(updateDTO.getSort());
        }
        if (updateDTO.getStatus() != null) {
            role.setStatus(updateDTO.getStatus());
        }
        
        role.setUpdateTime(new java.util.Date());
        roleMapper.updateById(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        roleMapper.deleteById(roleId);
    }
}

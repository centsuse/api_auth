package com.centsuse.api_auth.service;

import com.centsuse.api_auth.dao.SysRole;
import com.centsuse.api_auth.dtos.role.RoleCreateDTO;
import com.centsuse.api_auth.dtos.role.RoleQueryDTO;
import com.centsuse.api_auth.dtos.role.RoleUpdateDTO;

import java.util.List;

public interface RoleService {

    List<SysRole> getRoleList(RoleQueryDTO queryDTO);

    SysRole getRoleById(Long roleId);

    Long createRole(RoleCreateDTO createDTO);

    void updateRole(Long roleId, RoleUpdateDTO updateDTO);

    void deleteRole(Long roleId);
}

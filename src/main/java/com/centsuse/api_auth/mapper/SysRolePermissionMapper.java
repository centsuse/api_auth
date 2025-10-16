package com.centsuse.api_auth.mapper;

import com.centsuse.api_auth.dao.SysRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author bobo
* @description 针对表【sys_role_permission(角色权限关联表)】的数据库操作Mapper
* @createDate 2025-10-15 15:14:42
* @Entity com.centsuse.api_auth.dao.SysRolePermission
*/
@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

}





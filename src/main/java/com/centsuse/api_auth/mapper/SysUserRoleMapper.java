package com.centsuse.api_auth.mapper;

import com.centsuse.api_auth.dao.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author bobo
* @description 针对表【sys_user_role(用户角色关联表)】的数据库操作Mapper
* @createDate 2025-10-15 15:14:42
* @Entity com.centsuse.api_auth.dao.SysUserRole
*/
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}





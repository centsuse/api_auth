package com.centsuse.api_auth.mapper;

import com.centsuse.api_auth.dao.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author bobo
* @description 针对表【sys_permission(权限资源表)】的数据库操作Mapper
* @createDate 2025-10-15 15:14:42
* @Entity com.centsuse.api_auth.dao.SysPermission
*/
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> selectPermissionsByUserId(Long userId);

    int userHasPermission(Long userId, String permissionCode);
}





package com.centsuse.api_auth.mapper;

import com.centsuse.api_auth.dao.SysPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> selectPermissionsByUserId(Long userId);

    int userHasPermission(Long userId, String permissionCode);

    List<SysPermission> selectPermissionsByUserIdAndAppCode(@Param("userId") Long userId, @Param("appCode") String appCode);
}





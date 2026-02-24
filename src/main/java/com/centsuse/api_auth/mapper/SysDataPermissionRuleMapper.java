package com.centsuse.api_auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.centsuse.api_auth.dao.SysDataPermissionRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysDataPermissionRuleMapper extends BaseMapper<SysDataPermissionRule> {

    @Select("SELECT * FROM sys_data_permission_rule WHERE status = 1 AND type IN (2, 3) AND (role_id IN (SELECT role_id FROM sys_user_role WHERE user_id = #{userId}) OR permission_id IN (SELECT permission_id FROM sys_role_permission WHERE role_id IN (SELECT role_id FROM sys_user_role WHERE user_id = #{userId}))")
    List<SysDataPermissionRule> selectByUserIdAndDataType(@Param("userId") Long userId, @Param("dataType") String dataType);
}





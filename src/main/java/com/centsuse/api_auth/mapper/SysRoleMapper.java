package com.centsuse.api_auth.mapper;

import com.centsuse.api_auth.dao.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author bobo
* @description 针对表【sys_role(角色表)】的数据库操作Mapper
* @createDate 2025-10-15 15:14:42
* @Entity com.centsuse.api_auth.dao.SysRole
*/
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    // TODO
    List<SysRole> selectRolesByUserId(Long userId);
}





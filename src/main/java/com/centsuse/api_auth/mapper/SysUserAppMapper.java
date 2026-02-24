package com.centsuse.api_auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.centsuse.api_auth.dao.SysUserApp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserAppMapper extends BaseMapper<SysUserApp> {

    List<SysUserApp> selectByUserId(@Param("userId") Long userId);

    SysUserApp selectByUserIdAndAppCode(@Param("userId") Long userId, @Param("appCode") String appCode);
}

package com.centsuse.api_auth.mapper;

import com.centsuse.api_auth.dao.SysOperateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author bobo
* @description 针对表【sys_operate_log(操作日志表)】的数据库操作Mapper
* @createDate 2025-10-15 15:14:42
* @Entity com.centsuse.api_auth.dao.SysOperateLog
*/
@Mapper
public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {

}





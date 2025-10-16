package com.centsuse.api_auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.centsuse.api_auth.dao.SysUser;
import com.centsuse.api_auth.service.SysUserService;
import com.centsuse.api_auth.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author bobo
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2025-10-15 15:14:42
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}





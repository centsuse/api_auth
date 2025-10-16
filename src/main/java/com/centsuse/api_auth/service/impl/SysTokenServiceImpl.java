package com.centsuse.api_auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.centsuse.api_auth.dao.SysToken;
import com.centsuse.api_auth.service.SysTokenService;
import com.centsuse.api_auth.mapper.SysTokenMapper;
import org.springframework.stereotype.Service;

/**
* @author bobo
* @description 针对表【sys_token(令牌管理表)】的数据库操作Service实现
* @createDate 2025-10-15 15:14:42
*/
@Service
public class SysTokenServiceImpl extends ServiceImpl<SysTokenMapper, SysToken>
    implements SysTokenService{

}





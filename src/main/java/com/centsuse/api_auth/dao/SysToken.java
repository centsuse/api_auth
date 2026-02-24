package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="sys_token")
@Data
public class SysToken {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String appCode;

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long expiresIn;

    private Long refreshExpiresIn;

    private String scope;

    private Date issueTime;

    private Integer status;

    private String clientId;
}
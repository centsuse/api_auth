package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="sys_app")
@Data
public class SysApp {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String appCode;

    private String appName;

    private String description;

    private String redirectUri;

    private String clientId;

    private String clientSecret;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
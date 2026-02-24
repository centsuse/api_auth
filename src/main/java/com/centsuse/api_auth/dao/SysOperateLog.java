package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="sys_operate_log")
@Data
public class SysOperateLog {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String appCode;

    private String module;

    private Integer type;

    private String content;

    private Long userId;

    private String userName;

    private String ipAddress;

    private String userAgent;

    private Date operateTime;

    private Integer status;

    private String errorMessage;
}
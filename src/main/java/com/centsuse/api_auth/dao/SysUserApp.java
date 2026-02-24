package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="sys_user_app")
@Data
public class SysUserApp {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String appCode;

    private Long deptId;

    private Integer status;

    private Date createTime;

    private Long createBy;
}

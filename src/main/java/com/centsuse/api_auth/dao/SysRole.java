package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="sys_role")
@Data
public class SysRole {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String appCode;

    private String name;

    private String code;

    private Integer dataScope;

    private Integer sort;

    private Integer status;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private Long createBy;

    private Long updateBy;
}
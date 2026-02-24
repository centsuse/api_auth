package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName(value ="sys_data_permission_rule")
@Data
public class SysDataPermissionRule {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer type;

    private Long roleId;

    private Long permissionId;

    private Object conditionExpression;

    private String customSql;

    private Object fieldFilters;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Long createBy;

    private Long updateBy;
}
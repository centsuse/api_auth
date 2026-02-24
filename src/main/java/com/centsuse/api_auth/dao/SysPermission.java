package com.centsuse.api_auth.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
import java.util.List;

@TableName(value ="sys_permission")
@Data
public class SysPermission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String appCode;
    private Long parentId;
    private String name;
    private String code;
    private Integer type;
    private String path;
    private String component;
    private String method;
    private String icon;
    private Integer sort;
    private Integer status;
    private String description;
    private Date createTime;
    private Date updateTime;
    private Long createBy;
    private Long updateBy;
    private List<SysPermission> children;
}